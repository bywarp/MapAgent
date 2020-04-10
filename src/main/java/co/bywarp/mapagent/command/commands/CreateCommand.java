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

public class CreateCommand extends Command {

    private MapDataRepository repository;

    public CreateCommand(MapDataRepository repository) {
        super(Lang.generateSingleHelp("Parse", "/create <name> <gameType>"), true);
        this.repository = repository;
    }

    @Override
    public CommandReturn execute(Player client, String command, String[] args) {
        if (args.length != 2) {
            return CommandReturn.HELP_MENU;
        }

        String name = args[0];
        if (name.isEmpty() || name.length() < 2 || name.length() > 26) {
            client.sendMessage(Lang.generate("Parse", "Invalid Name &f[" + args[0] + "]"));
            client.sendMessage(Lang.colorMessage(" &7- &f" + Lang.cond(name.isEmpty(), "Name cannot be blank", "Name must be between 2, and 26 characters") + "."));
            return CommandReturn.EXIT;
        }

        GameDataType type = GameDataType.match(args[1]);
        if (type == null) {
            client.sendMessage(Lang.generate("Parse", "Invalid Gametype &f[" + args[0] + "]"));
            return CommandReturn.EXIT;
        }

        if (repository.exists(client.getWorld())) {
            client.sendMessage(Lang.generate("Parse", "This world is already registered."));
            return CommandReturn.EXIT;
        }

        String prettyName = Lang.capitalizeFirst(type.name().toLowerCase());
        repository.createEntry(client.getWorld(), name, prettyName);
        client.sendMessage(Lang.generate("Parse", "&f" + name + " &7registered with gametype &f" + prettyName + "&7."));

        return CommandReturn.EXIT;
    }

}
