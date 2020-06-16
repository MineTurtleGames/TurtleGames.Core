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

        _firstPosition = new Vector(
                Math.min(_firstPosition.getBlockX(), _secondPosition.getBlockX()),
                Math.min(_firstPosition.getBlockY(), _secondPosition.getBlockY()),
                Math.min(_firstPosition.getBlockZ(), _secondPosition.getBlockZ()));

        _secondPosition = new Vector(
                Math.max(_firstPosition.getBlockX(), _secondPosition.getBlockX()),
                Math.max(_firstPosition.getBlockY(), _secondPosition.getBlockY()),
                Math.max(_firstPosition.getBlockZ(), _secondPosition.getBlockZ()));

    }

    public Vector getMinimumPosition() {
        return _firstPosition;
    }

    public Vector getMaximumPosition() {
        return _secondPosition;
    }

    public TRegion(World world, TurtleWorldMetaPoint metaPoint) {
        this(world, metaPoint.getPrimaryPosition(), metaPoint.getSecondaryPosition());
    }

    public Collection<Block> getContents() {

        ArrayList<Block> blocks = new ArrayList<>();

        for (int x = _firstPosition.getBlockX(); x <= _secondPosition.getBlockX(); x++) {
            for (int y = _firstPosition.getBlockY(); y <= _secondPosition.getBlockY(); y++) {
                for (int z = _firstPosition.getBlockZ(); z <= _secondPosition.getBlockZ(); z++) {
                    blocks.add(new Location(Bukkit.getWorld(_world), x, y, z).getBlock());
                }
            }
        }

        return blocks;

    }

    public Vector getDimensions() {
        return _secondPosition.clone().add(new Vector(1,1,1)).subtract(_firstPosition);
    }

    public boolean contains(Block block) {

        boolean isInX = block.getX() >= _firstPosition.getBlockX() && block.getX() <= _secondPosition.getBlockX();
        boolean isInY = block.getY() >= _firstPosition.getBlockY() && block.getY() <= _secondPosition.getBlockY();
        boolean isInZ = block.getZ() >= _firstPosition.getBlockZ() && block.getZ() <= _secondPosition.getBlockZ();

        return isInX && isInY && isInZ;

    }

    public int getSize() {

        Vector dimensions = this.getDimensions();
        return dimensions.getBlockX() * dimensions.getBlockY() * dimensions.getBlockZ();

    }

    @Override
    public String toString() {

        Vector min = this.getMinimumPosition();
        Vector max = this.getMaximumPosition();

        return "{ size: " + this.getSize() + " - " + min.getBlockX() + "," + min.getBlockY() + "," + min.getBlockZ()
                + " to " + max.getBlockX() + "," + max.getBlockY() + "," + max.getBlockZ() + "}";
    }

}
