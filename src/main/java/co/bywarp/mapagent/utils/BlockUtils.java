/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockUtils {

    /**
     * Used to filter out bad sign blocks.
     * @param block the block to check
     * @return returns true if the block is invalid
     */
    public static boolean validate(Block block) {
        Block down = block.getRelative(BlockFace.DOWN);
        return down == null || down.getType() == Material.AIR;
    }

}
