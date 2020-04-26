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
import co.bywarp.mapagent.data.game.GameDataType;
import co.bywarp.mapagent.data.repository.MapDataRepository;
import co.bywarp.mapagent.utils.text.Lang;

import org.bukkit.entity.Player;

public class GametypeCommand extends Command {

    private MapDataRepository repository;

    public GametypeCommand(MapDataRepository repository) {
        super(Lang.generateSingleHelp("Parse", "/gametype <type>"), true);
        this.repository = repository;
    }

    @Override
    public CommandReturn execute(Player client, String command, String[] args) {
        if (args.length != 1) {
            return CommandReturn.HELP_MENU;
        }

        GameDataType gameType = GameDataType.match(args[0]);
        if (gameType == null) {
            client.sendMessage(Lang.generate("Parse", "Invalid Gametype &f[" + args[0] + "]"));
            return CommandReturn.EXIT;
        }

        if (!repository.exists(client.getWorld())) {
            client.sendMessage(Lang.generate("Parse", "This world is not registered as a parsable map."));
            return CommandReturn.EXIT;
        }

        repository.setGame(client.getWorld(), gameType,
                (type) -> client.sendMessage(Lang.generate("Parse", "Map gametype changed to &f" + Lang.capitalizeFirst(type.name().toLowerCase()) + "&7.")),
                (ex) -> client.sendMessage(Lang.generate("Parse", "Failed to change map gametype.")));
        return CommandReturn.EXIT;
    }

}
