package co.turtlegames.core.util;

import co.turtlegames.core.world.tworld.TurtleWorldMetaPoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;

public class TRegion {

    private String _world;
    private Vector _firstPosition;
    private Vector _secondPosition;

    public TRegion(World world, Vector firstPosition, Vector secondPosition) {

        _world = world.getName();
        _firstPosition = firstPosition;
        _secondPosition = secondPosition;

    }

    public TRegion(World world, TurtleWorldMetaPoint metaPoint) {
        this(world, metaPoint.getPrimaryPosition(), metaPoint.getSecondaryPosition());
    }

    public Collection<Block> getContents() {

        ArrayList<Block> blocks = new ArrayList<>();

        for (int x = _firstPosition.getBlockX(); x <= _secondPosition.getBlockX(); x++) {
            for (int y = _firstPosition.getBlockY(); y <= _secondPosition.getBlockY(); y++) {
                for (int z = _firstPosition.getBlockZ(); z <= _secondPosition.getBlockX(); z++) {
                    blocks.add(new Location(Bukkit.getWorld(_world), x, y, z).getBlock());
                }
            }
        }

        return blocks;

    }

    public boolean contains(Block block) {

        boolean isInX = block.getX() >= _firstPosition.getBlockX() && block.getX() <= _secondPosition.getBlockX();
        boolean isInY = block.getY() >= _firstPosition.getBlockX() && block.getY() <= _secondPosition.getBlockY();
        boolean isInZ = block.getZ() >= _firstPosition.getBlockX() && block.getZ() <= _secondPosition.getBlockZ();

        return isInX && isInY && isInZ;

    }

}
