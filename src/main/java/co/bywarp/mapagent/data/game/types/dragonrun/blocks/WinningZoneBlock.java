/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data.game.types.dragonrun.blocks;

import co.bywarp.mapagent.data.game.GameDataBlock;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;

public class WinningZoneBlock extends GameDataBlock {

    public WinningZoneBlock() {
        super(
                "Winning Zone",
                "winningBox",
                Material.WOOL,
                DyeColor.YELLOW.getWoolData(),
                ChatColor.YELLOW,
                false
        );
    }

}
