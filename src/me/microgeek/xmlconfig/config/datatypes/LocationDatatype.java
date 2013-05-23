package me.microgeek.xmlconfig.config.datatypes;

import org.bukkit.Location;
import org.bukkit.World;

import me.microgeek.xmlconfig.config.XMLConfig;

public class LocationDatatype implements DataType {

    @Override
    public Object parsed(String path, XMLConfig config) {
        World world = config.getPlugin().getServer().getWorld(config.getString(config.addPath(path, "world")));
        double x = config.getDouble(config.addPath(path, "x"));
        double y = config.getDouble(config.addPath(path, "y"));
        double z = config.getDouble(config.addPath(path, "z"));
        float yaw = config.getFloat(config.addPath(path, "yaw"));
        float pitch = config.getFloat(config.addPath(path, "pitch"));
        return new Location(world, x, y, z, yaw, pitch);
    }

}
