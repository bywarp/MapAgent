/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data.game.team;

import co.bywarp.mapagent.data.game.GameDataBlock;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import lombok.Getter;

@Getter
public class TeamData extends GameDataBlock {

    private String name;
    private ChatColor color;
    private byte woolColor;

    public TeamData(String name, ChatColor color, byte woolColor) {
        super(
                name,
                name
                        .toLowerCase()
                        .replaceAll(" ", "_")
                        .replaceAll("-", "_"),
                Material.WOOL,
                woolColor,
                color,
                false);
        this.name = name;
        this.color = color;
        this.woolColor = woolColor;
    }

}
