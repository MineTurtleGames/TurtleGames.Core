package co.turtlegames.core.world.gen;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class BarrierGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {

        ChunkData data = this.createChunkData(world);

        data.setRegion(0, 0, 0, 16, 256, 16, Material.BARRIER);

        return data;

    }

}
