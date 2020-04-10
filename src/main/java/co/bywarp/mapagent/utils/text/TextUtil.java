/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.utils.text;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * TextUtil allows for the easy generation
 * and sending of chat messages, such as:
 * centered strings, formatted strings, etc.
 *
 * @author Mike70387 & Struck713
 * @since 0.1
 */
public class TextUtil {

//	private final static int CENTER_PX = 154;
	private final static int CENTER_PX = 174;

	/**
	 * Sends a centered message to the
	 * chat of the provided player.
	 *
	 * @param player the player
	 * @param message the message
	 */
	public static void sendCenteredMessage(Player player, String message) {
		if (message == null || message.equals("")) player.sendMessage("");
		message = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(message));

		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : message.toCharArray()) {
			if (c == '&') {
				previousCode = true;
			} else if (previousCode) {
				previousCode = false;
				isBold = c == 'l' || c == 'L';
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}

		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;

		StringBuilder sb = new StringBuilder();
		while (compensated < toCompensate) {
			sb.append(" ");
			compensated += spaceLength;
		}

		player.sendMessage(sb.toString() + message);
	}

	public static String getCenteredMessage(String message) {
		if (message == null || message.equals("")) return "";
		message = ChatColor.translateAlternateColorCodes('&', message);

		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : message.toCharArray()) {
			if (c == '§') {
				previousCode = true;
			} else if (previousCode) {
				previousCode = false;
				if (c == 'l' || c == 'L') {
					isBold = true;
				} else isBold = false;
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}

		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;

		StringBuilder sb = new StringBuilder();
		while (compensated < toCompensate) {
			sb.append(" ");
			compensated += spaceLength;
		}

		return sb.toString() + message;
	}

}

