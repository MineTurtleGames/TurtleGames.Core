package co.turtlegames.core.world.tworld;

import co.turtlegames.core.world.tworld.io.TurtleInputStream;
import co.turtlegames.core.world.tworld.io.TurtleOutputStream;
import co.turtlegames.core.world.tworld.util.IODataUtil;
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.NibbleArray;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;

import java.io.IOException;

public class TurtleWorldSection {

    private boolean _writeMask = false;

    private int _yIndex;

    private byte[] _blocks;
    private NibbleArray _data;

    private NibbleArray _blockLight;
    private NibbleArray _skylightAccess;

    public TurtleWorldSection(int yIndex, NibbleArray blockLight, byte[] blocks, NibbleArray data, NibbleArray skylightAccess) {

        _yIndex = yIndex;
        _blockLight = blockLight;
        _blocks = blocks;
        _data = data;
        _skylightAccess = skylightAccess;

    }

    public static TurtleWorldSection loadFromStream(int yIndex, TurtleInputStream stream) throws IOException {

        return new TurtleWorldSection(yIndex,
                        stream.readNibbleArray(2048),
                        stream.readByteArray(4096),
                        stream.readNibbleArray(2048),
                        stream.readNibbleArray(2048));

    }

    public static TurtleWorldSection loadFromChunk(Chunk chunk, int yIndex) {

        int yFrom = yIndex * 16;
        int yTo = (yIndex + 1) * 16;

        byte[] blocks;
        NibbleArray data;
        NibbleArray blockLight = new NibbleArray();
        NibbleArray skylightAccess = new NibbleArray();

        boolean writeMask = false;

        ChunkSection nmsSection = ((CraftChunk)chunk).getHandle().getSections()[yIndex];
        TurtleReaderUtil.TurtleData tData;

        if(nmsSection != null) {

            tData = TurtleReaderUtil.decodeInternalIdData(nmsSection.getIdArray());

            char[] reEncoded = TurtleReaderUtil.encodeInternalIdData(tData);

            blockLight = nmsSection.getEmittedLightArray();
            skylightAccess = nmsSection.getSkyLightArray();

            writeMask = !nmsSection.a();

        } else {

            tData = new TurtleReaderUtil.TurtleData(new byte[4096], new NibbleArray());
            writeMask = true;

        }

        blocks = tData.getBlocks();
        data = tData.getData();

        /*int i = 0;
        for(int x = 0; x < 16; x++) {

            for(int z = 0; z < 16; z++) {

                for(int y = yFrom; y < yTo; y++) {

                    Block block = chunk.getBlock(x, y, z);

                    if(block == null
                            || block.getType() == Material.AIR)
                                continue;

                    TurtleReaderUtil.TurtleData tData =
                            TurtleReaderUtil.getEncodedData(((CraftChunk)chunk).getHandle().getSections()[yIndex].getIdArray());

                    data.a(i, tData.getData());

                    data.a(i, block.getLightLevel());
                    data.a(i, block.getLightFromSky());

                    writeMask = true;
                    i++;

                }

            }

        }*/

        TurtleWorldSection section = new TurtleWorldSection(yIndex, blockLight, blocks, data, skylightAccess);
        section.setWriteMask(writeMask);

        return section;

    }

    public static boolean hasBlocks(byte[] bb) {

        for(byte b : bb) {

            if(b != 0)
                return true;

        }

        return false;

    }

    public void write(TurtleOutputStream stream) throws IOException {

        stream.writeNibbleArray(_blockLight);
        stream.writeByteArray(_blocks);

        stream.writeNibbleArray(_data);
        stream.writeNibbleArray(_skylightAccess);

    }

    public void setWriteMask(boolean mask) {
        _writeMask = mask;
    }

    public boolean shouldWriteMask() {
        return _writeMask;
    }

    public ChunkSection evolve() {

        ChunkSection section = new ChunkSection(_yIndex * 16, true);

        char[] encodedBlockData = TurtleReaderUtil.encodeInternalIdData(new TurtleReaderUtil.TurtleData(_blocks, _data));
        System.arraycopy(encodedBlockData, 0,  section.getIdArray(), 0, section.getIdArray().length);

        IODataUtil.copyAll(_blockLight, section.getEmittedLightArray());
        IODataUtil.copyAll(_skylightAccess, section.getSkyLightArray());

        section.recalcBlockCounts();

        return section;

    }
}
