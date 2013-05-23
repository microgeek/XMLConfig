package me.microgeek.xmlconfig.config.datatypes;

import org.bukkit.Location;

import me.microgeek.xmlconfig.config.XMLConfig;
import me.microgeek.xmlconfig.cuboid.Cuboid;

public class CuboidDatatype implements Datatype {

    @Override
    public Object parsed(String path, XMLConfig config) {
        Location location1 = config.getLocation(path + ".1");
        Location location2 = config.getLocation(path + ".2");
        return new Cuboid(location1, location2);
    }

}
