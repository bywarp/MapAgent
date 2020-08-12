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

public class NameCommand extends Command {

    private MapDataRepository repository;

    public NameCommand(MapDataRepository repository) {
        super(Lang.generateSingleHelp("Parse", "/name <mapName>"), true);
        this.repository = repository;
    }

    @Override
    public CommandReturn execute(Player client, String command, String[] args) {
        if (args.length == 0) {
            return CommandReturn.HELP_MENU;
        }

        if (!repository.exists(client.getWorld())) {
            client.sendMessage(Lang.generate("Parse", "This world is not registered as a parsable map."));
            return CommandReturn.EXIT;
        }

        String mapName = String.join(" ", args);
        if (mapName.isEmpty()) {
            client.sendMessage(Lang.generate("Parse", "The map name cannot be blank."));
            return CommandReturn.EXIT;
        }

        if (mapName.length() < 3 || mapName.length() > 36) {
            client.sendMessage(Lang.generate("Parse", "Invalid Map Name &f[" + mapName + "]"));
            client.sendMessage(Lang.colorMessage(" &7- &fMap name must be &e3-36 &fcharacters."));
            return CommandReturn.EXIT;
        }

        repository.setName(client.getWorld(), mapName,
                name -> client.sendMessage(Lang.generate("Parse", "Map name changed to &f" + mapName + "&7.")),
                ex -> client.sendMessage(Lang.generate("Parse", "Failed to change map name.")));

        return CommandReturn.EXIT;
    }

}
