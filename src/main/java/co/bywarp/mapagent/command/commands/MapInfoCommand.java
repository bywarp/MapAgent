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
import co.bywarp.mapagent.data.repository.MapDataContainer;
import co.bywarp.mapagent.data.repository.MapDataRepository;
import co.bywarp.mapagent.utils.text.Lang;

import org.bukkit.entity.Player;

public class MapInfoCommand extends Command {

    private MapDataRepository repository;

    public MapInfoCommand(MapDataRepository repository) {
        super(Lang.generateSingleHelp("Parse", "/mapinfo"), false);
        this.repository = repository;
    }

    @Override
    public CommandReturn execute(Player client, String command, String[] args) {
        if (args.length != 0) {
            return CommandReturn.HELP_MENU;
        }

        if (!repository.exists(client.getWorld())) {
            client.sendMessage(Lang.generate("Parse", "This world is not registered as a parsable map."));
            return CommandReturn.EXIT;
        }

        MapDataContainer container = repository.getContainer(client.getWorld());
        client.sendMessage(Lang.generate("Parse", "&f" + container.getWorldName() + " &7is registered as a &f" + container.getGame() + " Map&7."));
        client.sendMessage(Lang.colorMessage(" &7- &fMap Name: &e" + container.getName()));
        client.sendMessage(Lang.colorMessage(" &7- &fMap Author: &e" + container.getAuthor()));
        client.sendMessage(Lang.colorMessage(" &7- &fCenter: &e" + Lang.prettifyLocation(container.getCenter())));
        return CommandReturn.EXIT;
    }

}
