package me.microgeek.xmlconfig.cuboid;

import org.bukkit.Location;


public class Cuboid {

    private Location location1;
    private Location location2;
    
    public Cuboid(Location location1, Location location2) {
        if(location1.getWorld().getName() != location1.getWorld().getName()) {
            return;
        }
        this.location1 = location1;
        this.location2 = location2;
    }
    
    public boolean isLocationInCuboid(Location location) {
        boolean x = location.getX() > Math.min(location1.getX(), location2.getX()) && location.getX() < Math.max(location1.getX(), location2.getX());
        boolean y = location.getY() > Math.min(location1.getY(), location2.getY()) && location.getY() < Math.max(location1.getY(), location2.getY());
        boolean z = location.getZ() > Math.min(location1.getZ(), location2.getZ()) && location.getZ() < Math.max(location1.getZ(), location2.getZ());
        return x && y && z;
    }
    
}
