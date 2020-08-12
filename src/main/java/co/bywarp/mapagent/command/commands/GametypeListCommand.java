/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.command.commands;

import co.bywarp.lightkit.util.TimeUtils;
import co.bywarp.mapagent.command.Command;
import co.bywarp.mapagent.command.CommandReturn;
import co.bywarp.mapagent.data.game.GameDataType;
import co.bywarp.mapagent.utils.text.Lang;

import org.bukkit.entity.Player;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GametypeListCommand extends Command {

    public GametypeListCommand() {
        super(Lang.generateSingleHelp("Parse", "/gametypes"), false);
    }

    @Override
    public CommandReturn execute(Player client, String command, String[] args) {
        if (args.length != 0) {
            return CommandReturn.HELP_MENU;
        }

        GameDataType[] dataTypes = GameDataType.values();
        client.sendMessage(Lang.generate("Parse", "There are &f" + dataTypes.length + " &7valid gametype" + TimeUtils.numberEnding(dataTypes.length) + "."));
        client.sendMessage(Lang.generate("Parse", "Valid Gametypes: &f" + Stream
                .of(dataTypes)
                .map(GameDataType::name)
                .collect(Collectors.joining("&7, &f"))));

        return CommandReturn.EXIT;
    }

}
