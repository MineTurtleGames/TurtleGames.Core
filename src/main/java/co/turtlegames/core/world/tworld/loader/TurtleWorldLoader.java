package co.turtlegames.core.world.tworld.loader;

import co.turtlegames.core.world.tworld.TurtleWorldChunk;
import co.turtlegames.core.world.tworld.TurtleWorldFormat;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ExceptionWorldConflict;
import net.minecraft.server.v1_8_R3.IChunkLoader;
import net.minecraft.server.v1_8_R3.World;

import java.io.IOException;

public class TurtleWorldLoader implements IChunkLoader {

    private TurtleWorldFormat _format;

    public TurtleWorldLoader(TurtleWorldFormat turtleWorld) {
        _format = turtleWorld;
    }

    @Override
    public Chunk a(World world, int chunkX, int chunkZ) throws IOException {

        TurtleWorldChunk turtleChunk = _format.getChunkAt(chunkX, chunkZ);

        if(turtleChunk == null)
            return null;

        return turtleChunk.evolve(world);

    }

    @Override
    public void a(World world, Chunk chunk) throws IOException, ExceptionWorldConflict {
        // Save chunk
    }

    @Override
    public void b(World world, Chunk chunk) throws IOException {
        // Write chunk NBT
    }

    @Override
    public void a() {
        // Chunk tick
    }

    @Override
    public void b() {
        // Save extra data
    }

}
