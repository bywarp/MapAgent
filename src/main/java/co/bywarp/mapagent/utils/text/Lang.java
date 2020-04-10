/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.utils.text;

import co.bywarp.mapagent.data.MapPoint;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.text.DecimalFormat;

public class Lang {

	// Colors
	public static final String RED = ChatColor.RED.toString();
	public static final String DARK_RED = ChatColor.DARK_RED.toString();
	public static final String ORANGE = ChatColor.GOLD.toString();
	public static final String YELLOW = ChatColor.YELLOW.toString();
	public static final String GREEN = ChatColor.GREEN.toString();
	public static final String DARK_GREEN = ChatColor.DARK_GREEN.toString();
	public static final String BLUE = ChatColor.AQUA.toString();
	public static final String DARK_BLUE = ChatColor.DARK_BLUE.toString();
	public static final String CYAN = ChatColor.DARK_AQUA.toString();
	public static final String PURPLE = ChatColor.LIGHT_PURPLE.toString();
	public static final String DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
	public static final String GRAY = ChatColor.GRAY.toString();
	public static final String DARK_GRAY = ChatColor.DARK_GRAY.toString();
	public static final String WHITE = ChatColor.RESET.toString();
	public static final String BLACK = ChatColor.BLACK.toString();

	// Formatting
	public static final String BOLD = ChatColor.BOLD.toString();
	public static final String OBF = ChatColor.MAGIC.toString();
	public static final String STRIKE = ChatColor.STRIKETHROUGH.toString();
	public static final String ITALIC = ChatColor.ITALIC.toString();
	public static final String UNDERLINE = ChatColor.UNDERLINE.toString();

 	// Lang v2
 	public static final String MODULE = DARK_GREEN + "%s " + DARK_GRAY + "» " + GRAY + "%s";
 	public static final String CMD = DARK_GREEN + "/%s " + YELLOW + "%s " + DARK_GRAY + "* " + WHITE + "%s";
 	public static final String CMD_HELP_SINGLE = DARK_GREEN + "%s " + DARK_GRAY + "» " + GRAY + "Invalid usage: " + WHITE + "%s";

 	public static final String DIVIDER = DARK_GRAY + STRIKE + "-------------------------------------------------";
	public static final String DIVIDER_NO_CENTER = DARK_GRAY + STRIKE + "-------------------------------------------------";
	public static final String SHORT_DIVIDER = DARK_GRAY + STRIKE + "-----------------------";

	public static final String PUNISH_BAN_STRING =
			Lang.colorMessage("§2§lPunishment\n"
					+ " \n"
					+ "&7You have been banned by &a%s&7 for &f%s\n"
					+ "&7%s\n"
					+ " \n"
					+ "%s\n");

	public static String cond(boolean condition, String t, String f) {
		return condition ? t : f;
	}

	public static String capitalizeFirst(String original) {
		if (original == null || original.length() == 0) {
			return original;
		}
		return original.substring(0, 1).toUpperCase() + original.substring(1);
	}

	public static ChatColor getColorForVL(int vls) {
		if (vls > 0 && vls < 4) {
			return ChatColor.GREEN;
		}
		if (vls > 3 && vls < 7) {
			return ChatColor.YELLOW;
		}
		if (vls > 6 && vls < 10) {
			return ChatColor.GOLD;
		}
		if (vls > 9 && vls < 13) {
			return ChatColor.RED;
		}
		return ChatColor.DARK_RED;
	}

	public static String getColorForElaspedTime(long ms) {
		if (ms >= 0L && ms <= 400L) {
			return GREEN;
		} else if (ms >= 401L && ms <= 800L) {
			return YELLOW;
		} else {
			return ms >= 801L ? RED : PURPLE;
		}
	}

	public static String prettifyLocation(Location location) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(location.getX()) + ", " + df.format(location.getY()) + ", " + df.format(location.getZ());
	}

	public static String prettifyLocation(MapPoint location) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(location.getX()) + ", " + df.format(location.getY()) + ", " + df.format(location.getZ());
	}

 	public static String generate(String prefix, String message) {
 		return String.format(MODULE, prefix, colorMessage(message));
 	}
 	
 	public static String generateCommand(String command, String args, String desc) {
 		return String.format(CMD, command, args, desc);
 	}

 	public static String generateSingleHelp(String head, String body) {
 		return String.format(CMD_HELP_SINGLE, colorMessage(head), colorMessage(body));
 	}
 	
 	public static String generateHelpList(String prefix, String header, String... bodies) {
 		StringBuilder sb = new StringBuilder();
 		sb.append(generate(prefix, header) + "\n");
 		for (String s : bodies) {
 			sb.append(GRAY + " - " + s + WHITE + "\n");
 		}
 		return sb.toString().trim();
 	}

 	public static String colorMessage(String message) {
 		return ChatColor.translateAlternateColorCodes('&', message);
 	}
 	
}