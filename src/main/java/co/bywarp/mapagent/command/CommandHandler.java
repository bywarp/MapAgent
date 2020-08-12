/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.command;

import co.bywarp.lightkit.util.logger.Logger;
import co.bywarp.mapagent.MapAgent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.Map.Entry;

public class CommandHandler implements Listener {
	
	private static HashMap<String, Command> REGISTERED_COMMANDS = new HashMap<>();

	private MapAgent plugin;
	private Logger logger;

	public CommandHandler(MapAgent plugin) {
		this.plugin = plugin;
		this.logger = new Logger("Command");

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public void registerCommand(String name, Command command) {
		command.setPlugin(plugin);
		REGISTERED_COMMANDS.put(name, command);
		logger.info("Registered command \"" + name + "\"");
	}
	
	public void registerCommand(String name, String[] aliases, Command command) {
		command.setPlugin(plugin);
		REGISTERED_COMMANDS.put(name, command);
		logger.info("Registered command \"" + name + "\"");
		for (String alias : aliases) {
			REGISTERED_COMMANDS.put(alias, command);
			logger.info("  | Registered alias \"" + alias + "\"");
		}
	}

	public void unregisterCommand(String name) {
		Command command = REGISTERED_COMMANDS.get(name);
		if (command == null) {
			return;
		}

		command.setPlugin(null);
		REGISTERED_COMMANDS.remove(name);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player player = Bukkit.getPlayer(event.getPlayer().getUniqueId());
		if (player == null) {
			return;
		}

		String message = event.getMessage().substring(1);
		int first = message.indexOf(' ');
		
		if (first <= 0) {
			CommandReturn cancel = callCommand(player, message, new String[0]);
			event.setCancelled(cancel.isCancel());
			return;
		}
		
		String command = message.substring(0, first);
		String[] args = message.substring(first + 1).split(" ");
		
		CommandReturn cancel = callCommand(player, command, args);
		event.setCancelled(cancel.isCancel());
	}
	
	public CommandReturn callCommand(Player client, String command, String[] args) {
		for (Entry<String, Command> current : REGISTERED_COMMANDS.entrySet()) {
			Command commandObject = current.getValue();
			String name = current.getKey();
			if (command.equalsIgnoreCase(name)) {
				if (commandObject.isRequireOP() && !client.isOp()) {
					return CommandReturn.UNKNOWN_COMMAND;
				}

				CommandReturn cancel = commandObject.execute(client, command, args);
				if (cancel == CommandReturn.HELP_MENU) {
					client.sendMessage(commandObject.getHelpMenu());
					cancel = CommandReturn.EXIT;
				}

				return cancel;
			}
		}
		return CommandReturn.UNKNOWN_COMMAND;
	}

	public static HashMap<String, Command> getRegisteredCommands() {
		return REGISTERED_COMMANDS;
	}

}
