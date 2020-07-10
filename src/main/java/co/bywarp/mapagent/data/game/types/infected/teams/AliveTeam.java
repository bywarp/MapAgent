/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data.game.types.infected.teams;

import co.bywarp.mapagent.data.game.team.TeamData;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public class AliveTeam extends TeamData {

    public AliveTeam() {
        super("Alive", ChatColor.GREEN, DyeColor.LIME.getWoolData());
    }

}
