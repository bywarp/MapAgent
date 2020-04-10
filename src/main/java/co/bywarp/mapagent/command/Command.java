/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.command;

import co.bywarp.mapagent.MapAgent;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Command {

	public static final String NO_HELP_MENU = "";

	private String helpMenu;
	private boolean requireOP;
	private MapAgent plugin;

	public Command(String helpMenu, boolean requireOP) {
		this.helpMenu = helpMenu;
		this.requireOP = requireOP;
	}

	public abstract CommandReturn execute(Player client, String command, String[] args);

}
