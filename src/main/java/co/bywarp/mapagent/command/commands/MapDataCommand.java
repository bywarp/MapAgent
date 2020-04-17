/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.command.commands;

import co.bywarp.mapagent.command.Command;
import co.bywarp.mapagent.command.CommandReturn;
import co.bywarp.mapagent.data.repository.MapDataRepository;
import co.bywarp.mapagent.utils.text.Lang;

import org.bukkit.entity.Player;

public class MapDataCommand extends Command {

    private MapDataRepository repository;

    public MapDataCommand(MapDataRepository repository) {
        super(Lang.generateSingleHelp("Parse", "/data <name> <value>"), true);
        this.repository = repository;
    }

    @Override
    public CommandReturn execute(Player client, String command, String[] args) {
        if (args.length != 2) {
            return CommandReturn.HELP_MENU;
        }

        if (!repository.exists(client.getWorld())) {
            client.sendMessage(Lang.generate("Parse", "This world is not registered as a parsable map."));
            return CommandReturn.EXIT;
        }

        repository.addData(client.getWorld(), args[0], args[1],
                (ent) -> client.sendMessage(Lang.generate("Parse", "Game Data &f[" + args[0] + "] &7set to &f[" + args[1] + "]")),
                (ex) -> client.sendMessage(Lang.generate("Parse", "Failed to update map data.")));

        return CommandReturn.EXIT;
    }

}
