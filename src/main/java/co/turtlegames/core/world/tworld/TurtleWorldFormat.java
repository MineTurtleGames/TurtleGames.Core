package co.turtlegames.core.world.tworld;

import co.turtlegames.core.world.tworld.io.TurtleInputStream;
import co.turtlegames.core.world.tworld.io.TurtleOutputStream;
import co.turtlegames.core.world.tworld.loader.TurtleWorldLoader;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.server.v1_8_R3.ChunkCoordIntPair;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class TurtleWorldFormat {

    private static final byte CURRENT_VERSION = 1;
    private static final byte MAGIC_BYTE = (byte) 0x69;

    private byte _formatVer;

    private int _minX;
    private int _minZ;

    private byte _xWidth;
    private byte _zWidth;

    private BitSet _chunkMask;
    private Map<ChunkCoordIntPair, TurtleWorldChunk> _chunks;

    private Multimap<Byte, TurtleWorldMetadata> _metadata = MultimapBuilder.hashKeys()
                                                                .linkedListValues()
                                                                    .build();

    public static TurtleWorldFormat loadFromStream(TurtleInputStream stream) throws IOException {

        byte magic = stream.readByte();

        if(magic != MAGIC_BYTE)
            throw new IOException("Invalid input. Not tworld file");

        byte formatVer = stream.readByte();

        if(formatVer != CURRENT_VERSION)
            throw new IOException("Invalid input. tworld version not equal to current version.");

        int minX = stream.readShort();
        int minZ = stream.readShort();

        byte xWidth = stream.readByte();
        byte zWidth = stream.readByte();

        int maskSize = (int) Math.ceil((xWidth * zWidth)/8);
        BitSet chunkMask = stream.readBitSet(maskSize);

        byte[] chunkData = stream.readCompressedData();

        byte[] metadata = stream.readCompressedData();

        TurtleInputStream metaInStream = new TurtleInputStream(new ByteArrayInputStream(metadata));
        Multimap<Byte, TurtleWorldMetadata> metadataValues = MultimapBuilder.hashKeys()
                                                                .linkedListValues()
                                                                    .build();

        while(metaInStream.available() > 0) {

            TurtleWorldMetadata metadataValue = TurtleWorldMetadata.loadFromStream(metaInStream);
            metadataValues.put(metadataValue.getMetaType(), metadataValue);

        }

        metaInStream.close();

        TurtleWorldFormat worldFormat =  new TurtleWorldFormat(formatVer, minX, minZ, xWidth, zWidth, chunkMask);
        worldFormat.loadChunksWithData(chunkData);
        worldFormat.loadMetadata(metadataValues);

        return worldFormat;

    }

    public static TurtleWorldFormat loadFromLoadedChunks(World world) throws IOException {
        return TurtleWorldFormat.loadFromChunks(world.getLoadedChunks());
    }

    public static TurtleWorldFormat loadFromChunks(Chunk[] chunks) throws IOException {

        int minX = Short.MAX_VALUE;
        int minZ = Short.MAX_VALUE;

        int maxX = Short.MIN_VALUE;
        int maxZ = Short.MIN_VALUE;

        Map<ChunkCoordIntPair, TurtleWorldChunk> turtleChunks = new HashMap<>();

        for(Chunk chunk : chunks) {

            if(minX > chunk.getX())
                minX = chunk.getX();
            if(minZ > chunk.getZ())
                minZ = chunk.getZ();

            if(chunk.getX() > maxX)
                maxX = chunk.getX();
            if(chunk.getZ() > maxZ)
                maxZ = chunk.getZ();

            ChunkCoordIntPair coordinate = new ChunkCoordIntPair(chunk.getX(), chunk.getZ());
            turtleChunks.put(coordinate, TurtleWorldChunk.loadFromChunk(chunk));

        }

        byte xSize = (byte) (maxX - minX);
        byte zSize = (byte) (maxZ - minZ);

        BitSet chunkMask = new BitSet(xSize * zSize);

        for(int i = 0; i < turtleChunks.size(); i++) {

            ChunkCoordIntPair coordinate = TurtleWorldFormat.getChunkCoords(i, xSize, minX, zSize, minZ);
            TurtleWorldChunk chunk = turtleChunks.get(coordinate);

            boolean maskValue = chunk != null && chunk.hasContents();

            chunkMask.set(i, maskValue);

        }

        TurtleWorldFormat worldFormat = new TurtleWorldFormat(CURRENT_VERSION, minX, minZ, xSize, zSize, chunkMask);
        worldFormat.loadChunksFromMap(turtleChunks);

        return worldFormat;

    }

    public void loadMetadata(Multimap<Byte, TurtleWorldMetadata> metadata) {
        _metadata = metadata;
    }

    private void loadChunksFromMap(Map<ChunkCoordIntPair, TurtleWorldChunk> turtleChunks) {
        _chunks = turtleChunks;
    }

    public void write(TurtleOutputStream outStream) throws IOException {

        outStream.writeByte(MAGIC_BYTE);
        outStream.writeByte(_formatVer);

        outStream.writeShort(_minX);
        outStream.writeShort(_minZ);

        outStream.writeByte(_xWidth);
        outStream.writeByte(_zWidth);

        outStream.writeBitSet(_chunkMask, (int) Math.ceil((1.0d * _zWidth * _xWidth/8)));

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        TurtleOutputStream stream = new TurtleOutputStream(byteOutput);

        for(int i = 0; i < _chunkMask.size(); i++) {

            if(!_chunkMask.get(i))
                continue;

            ChunkCoordIntPair coord = this.getChunkCoords(i);

            TurtleWorldChunk chunk = _chunks.get(coord);

            if(chunk == null) {

                System.err.println("FATAL: Chunk missing");
                continue;

            }

            chunk.write(stream);

        }

        outStream.compressAndWrite(byteOutput.toByteArray());

        ByteArrayOutputStream metadataByteStream = new ByteArrayOutputStream();
        TurtleOutputStream metadataOutputStream = new TurtleOutputStream(metadataByteStream);

        for(TurtleWorldMetadata metadata : _metadata.values())
            metadata.write(metadataOutputStream);

        metadataOutputStream.close();

        outStream.compressAndWrite(metadataByteStream.toByteArray());

        outStream.flush();
        outStream.close();

    }

    public TurtleWorldFormat(byte version, int minX, int minZ, byte xWidth, byte zWidth, BitSet chunkMask) {

        _formatVer = version;

        _minX = minX;
        _minZ = minZ;

        _xWidth = xWidth;
        _zWidth = zWidth;

        _chunkMask = chunkMask;

    }

    private void loadChunksWithData(byte[] chunkData) throws IOException {

        TurtleInputStream stream = new TurtleInputStream(new ByteArrayInputStream(chunkData));

        _chunks = new HashMap<>();

        for(int i = 0; i < _chunkMask.length() ; i++) {

            if(!_chunkMask.get(i))
                continue;

            ChunkCoordIntPair coordinate = this.getChunkCoords(i);
            TurtleWorldChunk chunk = TurtleWorldChunk.loadFromStream(coordinate, stream);

            _chunks.put(coordinate, chunk);

        }

    }

    private int getBitIndex(ChunkCoordIntPair coords) {
        return (coords.x - _minX) + (coords.z - _minZ) * _zWidth;
    }

    private static ChunkCoordIntPair getChunkCoords(int bitIndex, int xWidth, int minX, int zWidth, int minZ) {
        return new ChunkCoordIntPair(
                bitIndex % xWidth + minX, bitIndex / zWidth + minZ);
    }

    private ChunkCoordIntPair getChunkCoords(int bitIndex) {
        return TurtleWorldFormat.getChunkCoords(bitIndex, _xWidth, _minX, _zWidth, _minZ);
    }

    public static byte getCurrentVersion() {
        return CURRENT_VERSION;
    }

    public static byte getMagicByte() {
        return MAGIC_BYTE;
    }

    public byte getFormatVer() {
        return _formatVer;
    }

    public int getXWidth() {
        return _xWidth;
    }

    public int getZWidth() {
        return _zWidth;
    }

    public BitSet getChunkMask() {
        return _chunkMask;
    }

    public TurtleWorldChunk getChunkAt(int x, int z) {
        return _chunks.get(new ChunkCoordIntPair(x, z));
    }

    public TurtleWorldLoader createChunkLoader() {
        return new TurtleWorldLoader(this);
    }

}
