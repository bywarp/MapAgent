/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data.game.marker;

import co.bywarp.mapagent.data.game.GameDataBlock;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class CornerMarker extends GameDataBlock {

    public CornerMarker() {
        super(
                "Corner",
                "corner",
                Material.SEA_LANTERN,
                (byte) 0,
                ChatColor.AQUA
        );
    }

}
