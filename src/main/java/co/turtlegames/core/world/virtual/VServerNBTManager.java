package co.turtlegames.core.world.virtual;

import net.minecraft.server.v1_8_R3.*;

import java.io.File;
import java.util.UUID;

public class VServerNBTManager implements IDataManager,IPlayerFileData {

    private IChunkLoader _chunkLoader = null;
    private UUID _uid = UUID.randomUUID();

    public VServerNBTManager(String mapName) {}

    public VServerNBTManager(String mapName, IChunkLoader chunkLoader) {
        _chunkLoader = chunkLoader;
    }


    @Override
    public WorldData getWorldData() {
        return null;
    }

    @Override
    public void checkSession() throws ExceptionWorldConflict {

    }

    @Override
    public IChunkLoader createChunkLoader(WorldProvider worldProvider) {
        return _chunkLoader;
    }

    @Override
    public void saveWorldData(WorldData worldData, NBTTagCompound nbtTagCompound) {

    }

    @Override
    public void saveWorldData(WorldData worldData) {

    }

    @Override
    public IPlayerFileData getPlayerFileData() {
        return null;
    }

    @Override
    public void a() {

    }

    @Override
    public File getDirectory() {
        return null;
    }

    @Override
    public File getDataFile(String s) {
        return null;
    }

    @Override
    public String g() {
        return null;
    }

    @Override
    public UUID getUUID() {
        return _uid;
    }

    @Override
    public void save(EntityHuman entityHuman) {}

    @Override
    public NBTTagCompound load(EntityHuman entityHuman) {
        return null;
    }

    @Override
    public String[] getSeenPlayers() {
        return new String[0];
    }
}
