package co.turtlegames.core.world.virtual;

import co.turtlegames.core.TurtleModule;
import co.turtlegames.core.world.gen.BarrierGenerator;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.WorldType;
import org.bukkit.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class VirtualWorldManager extends TurtleModule {

    private int _curDimensionId = 100;
    private Map<String, World> _virtualWorlds = new HashMap<>();

    public VirtualWorldManager(JavaPlugin pluginInstance) {
        super(pluginInstance,"Virtual World Manager");
    }

    @Override
    public void initializeModule() {

    }

    @Override
    public void deinitializeModule() {

        for(World world : _virtualWorlds.values())
            this.unloadVirtualWorld(world);

    }

    public World createVirtualWorld(WorldCreator creator) {
        return this.createVirtualWorld(creator, null);
    }

    public World createVirtualWorld(String name, IChunkLoader chunkLoader) {

        WorldCreator creator = new WorldCreator(name)
                                    .generator(new BarrierGenerator());

        return this.createVirtualWorld(creator, chunkLoader);

    }

    public World createVirtualWorld(WorldCreator creator, IChunkLoader chunkLoader) {

        if(_virtualWorlds.containsKey(creator.name()))
            return _virtualWorlds.get(creator.name());

        creator.environment(World.Environment.NORMAL);

        CraftServer craftServer = (CraftServer) Bukkit.getServer();

        ChunkGenerator generator = creator.generator();
        IDataManager serverNBTManager = new VServerNBTManager(creator.name(), chunkLoader);

        MinecraftServer minecraftServer = MinecraftServer.getServer();

        WorldData worldData = new WorldData(new WorldSettings(_curDimensionId, WorldSettings.EnumGamemode.SURVIVAL, false, false, WorldType.NORMAL), creator.name());

        System.out.println("Creating new virtual world with dimension id " + _curDimensionId);

        WorldServer internal = (WorldServer) new WorldServer(minecraftServer, serverNBTManager, worldData, _curDimensionId, minecraftServer.methodProfiler, creator.environment(), generator).b();
        _curDimensionId++;

        internal.scoreboard = craftServer.getScoreboardManager()
                                            .getMainScoreboard()
                                                .getHandle();

        internal.tracker = new EntityTracker(internal);
        internal.addIWorldAccess(new WorldManager(minecraftServer, internal));
        internal.worldData.setDifficulty(EnumDifficulty.EASY);
        internal.setSpawnFlags(true, true);

        minecraftServer.worlds.add(internal);

        _virtualWorlds.put(creator.name(), internal.getWorld());
        return internal.getWorld();

    }

    public void unloadVirtualWorld(World world) {

        for(Player ply : world.getEntities().stream()
                .filter(e -> e.getType() == EntityType.PLAYER)
                .map(ply -> (Player) ply)
                .collect(Collectors.toList())) {

            ply.teleport(Bukkit.getWorld("world").getSpawnLocation());
            ply.sendMessage(ChatColor.RED + "The world you were in is being destroyed.");

        }

        CraftServer craftServer = ((CraftServer)Bukkit.getServer());
        MinecraftServer server = MinecraftServer.getServer();

        WorldServer handle = ((CraftWorld)world).getHandle();

        server.worlds.remove(handle);
        craftServer.getWorlds().remove(world);

        _virtualWorlds.remove(world.getName());

    }

    public boolean isVirtualWorld(World world) {
        return _virtualWorlds.containsKey(world.getName());
    }

}
