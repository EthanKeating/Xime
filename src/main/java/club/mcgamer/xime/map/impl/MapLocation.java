package club.mcgamer.xime.map.impl;

import club.mcgamer.xime.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

@AllArgsConstructor @Getter
public class MapLocation {

    public static MapLocation fromBukkit(Location bukkitLocation) {
        return new MapLocation(
                bukkitLocation.getX(),
                bukkitLocation.getY(),
                bukkitLocation.getZ()
        );
    }

    private final double x, y, z;

    @Override
    public String toString() {
        return x + ", " + y + ", " + z;
    }

    public Location toBukkit(World world) {
        return new Location(world, x, y, z, 0.0f, 0.0f).add(0.5, 0.0, 0.5);
    }

    public Location toBukkitNoOffset(World world) {
        return new Location(world, x, y, z, 0.0f, 0.0f);
    }

    public Pair<Integer, Integer> getChunk() {
        return new Pair<>((int)x >> 4, (int)z >> 4);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        MapLocation other = (MapLocation) obj;

        return x == other.x && y == other.y && z == other.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
