package co.turtlegames.core.world.tworld;

import co.turtlegames.core.world.tworld.io.TurtleInputStream;
import co.turtlegames.core.world.tworld.io.TurtleOutputStream;
import co.turtlegames.core.world.tworld.util.IODataUtil;
import net.minecraft.server.v1_8_R3.ChunkCoordIntPair;
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;

import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;

public class TurtleWorldChunk {

    private static final int SECTIONS_PER_CHUNK = 16;

    private ChunkCoordIntPair _coordinate;

    private int[] _heightMap;
    private byte[] _biomeMap;

    private BitSet _sectionMask;
    private TurtleWorldSection[] _sections;

    public TurtleWorldChunk(ChunkCoordIntPair coordinate, int[] heightMap, byte[] biomeMap, BitSet sectionMask, TurtleWorldSection[] sections) {

        _coordinate = coordinate;
        _heightMap = heightMap;
        _biomeMap = biomeMap;
        _sectionMask = sectionMask;
        _sections = sections;

    }

    public static TurtleWorldChunk loadFromStream(ChunkCoordIntPair coordinate, TurtleInputStream stream) throws IOException {

        int[] heightMap = stream.readIntArray(256);
        byte[] biomeMap = stream.readByteArray(256);

        BitSet sectionMask = stream.readBitSet(2);
        TurtleWorldSection[] sections = new TurtleWorldSection[SECTIONS_PER_CHUNK];

        for(int yIndex = 0; yIndex < SECTIONS_PER_CHUNK; yIndex++) {

            if(!sectionMask.get(yIndex))
                continue;

            sections[yIndex] = TurtleWorldSection.loadFromStream(yIndex, stream);

        }

        return new TurtleWorldChunk(coordinate,
                        heightMap,
                        biomeMap,
                        sectionMask,
                        sections);

    }

    public static TurtleWorldChunk loadFromChunk(Chunk chunk) {

        net.minecraft.server.v1_8_R3.Chunk nmsChunk = ((CraftChunk) chunk).getHandle();

        int[] heightMap = nmsChunk.heightMap;
        byte[] biomeMap = nmsChunk.getBiomeIndex();

        BitSet sectionMask = new BitSet(SECTIONS_PER_CHUNK);
        TurtleWorldSection[] sections = new TurtleWorldSection[SECTIONS_PER_CHUNK];

        for(int yIndex = 0; yIndex < SECTIONS_PER_CHUNK; yIndex++) {

            TurtleWorldSection section = TurtleWorldSection.loadFromChunk(chunk, yIndex);

            if(!section.shouldWriteMask())
                continue;

            sectionMask.set(yIndex, true);
            sections[yIndex] = section;

        }

        return new TurtleWorldChunk(new ChunkCoordIntPair(chunk.getX(), chunk.getZ()),
                                        heightMap,
                                        biomeMap,
                                        sectionMask,
                                        sections);

    }

    protected void write(TurtleOutputStream stream) throws IOException {

        stream.writeIntArray(_heightMap);
        stream.writeByteArray(_biomeMap);

        stream.writeBitSet(_sectionMask, SECTIONS_PER_CHUNK/8);

        for(int yIndex = 0; yIndex < SECTIONS_PER_CHUNK; yIndex ++) {

            if(!_sectionMask.get(yIndex))
                continue;

            TurtleWorldSection section = _sections[yIndex];
            section.write(stream);

        }

    }

    public boolean hasContents() {
        return _sectionMask.cardinality() > 0;
    }

    public net.minecraft.server.v1_8_R3.Chunk evolve(World world) {

        net.minecraft.server.v1_8_R3.Chunk chunk =
                new net.minecraft.server.v1_8_R3.Chunk(world, _coordinate.x, _coordinate.z);

        chunk.a(_heightMap);

        chunk.d(true);
        chunk.e(true);

        chunk.c(0);

        chunk.a(this.evolveSections());
        chunk.a(_biomeMap);

        return chunk;

    }

    private ChunkSection[] evolveSections() {

        ChunkSection[] evolvedSections = new ChunkSection[_sections.length];

        for(int i = 0; i < _sections.length; i++) {

            if(_sectionMask.get(i))
                evolvedSections[i] = _sections[i].evolve();
            else
                evolvedSections[i] = new ChunkSection(i * 16, true);

        }

        return evolvedSections;

    }

}
