/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class BorderUtil {

    public static List<Location> createBorder(World world, Location corner1, Location corner2, double particleDistance) {
        List<Location> result = new ArrayList<>();

        Chunk chunk1 = corner1.getChunk();
        Chunk chunk2 = corner2.getChunk();

        int chunk1X = (chunk1.getX() * 16);
        int chunk1Z = (chunk1.getZ() * 16);
        int chunk2X = (chunk2.getX() * 16);
        int chunk2Z = (chunk2.getZ() * 16);

        double minX = Math.min(chunk1X, chunk2X);
        double minY = Math.min(corner1.getY(), corner2.getY());
        double minZ = Math.min(chunk1Z, chunk2Z);
        double maxX = Math.max(chunk1X, chunk2X);
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double maxZ = Math.max(chunk1Z, chunk2Z);

        for (double x = minX; x <= maxX; x += particleDistance) {
            for (double y = minY; y <= maxY; y += particleDistance) {
                for (double z = minZ; z <= maxZ; z += particleDistance) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        result.add(new Location(world, x, y, z));
                    }

                }
            }
        }

        return result;
    }

    public static List<Location> createBorder(Chunk chunk) {
        List<Location> result = new ArrayList<>();

        World world = chunk.getWorld();
        int x1 = chunk.getX() * 16;
        int z1 = chunk.getZ() * 16;
        int x2 = x1 + 16;
        int z2 = z1 + 16;

        double minX = Math.min(x1, x2);
        double minY = 0;
        double minZ = Math.min(z1, z2);
        double maxX = Math.max(x1, x2);
        double maxY = world.getMaxHeight();
        double maxZ = Math.max(z1, z2);

        double particleDistance = 3;
        for (double x = minX; x <= maxX; x += particleDistance) {
            for (double y = minY; y <= maxY; y += particleDistance) {
                for (double z = minZ; z <= maxZ; z += particleDistance) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        result.add(new Location(world, x, y, z));
                    }

                }
            }
        }

        return result;
    }

    public static List<Location> createBorder(Location center, int radius) {
        List<Location> locations = new ArrayList<>();
        ChunkUtils.getChunks(center, radius)
                .stream()
                .map(BorderUtil::createBorder)
                .forEach(locations::addAll);
        return locations;

    }

}
