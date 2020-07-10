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
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChunkUtils {

    public static List<Block> getMatches(Chunk chunk, Material... material) {
        List<Material> materials = Arrays.asList(material);
        List<Block> blocks = new ArrayList<>();

        final int minX = chunk.getX() << 4;
        final int minZ = chunk.getZ() << 4;
        final int maxX = minX | 15;
        final int maxY = chunk.getWorld().getMaxHeight();
        final int maxZ = minZ | 15;

        for (int x = minX; x <= maxX; ++x) {
            for (int y = 0; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    Block block = chunk.getBlock(x, y, z);

                    if (block.getType() == Material.AIR) continue;
                    if (!materials.contains(block.getType())) continue;
                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    public static List<Chunk> getChunks(Location location, int radius) {
        Set<Chunk> chunks = new HashSet<>();

        World world = location.getWorld();
        Chunk chunk = location.getChunk();

        int ceilRadius = (int) Math.ceil(radius / 15.0);
        int doubleRadius = ceilRadius * 2;
        int x = chunk.getX() - ceilRadius;
        int z = chunk.getZ() - ceilRadius;

        for (int i = 0; i<doubleRadius; i++) {
            int newX = x + i;
            for (int j = 0; j <doubleRadius; j++) {
                int newZ = z + j;
                Chunk posChunk = world.getChunkAt(newX, newZ);
                chunks.add(posChunk);
            }
        }

        chunks.add(chunk);
        return new ArrayList<>(chunks);
    }

}
