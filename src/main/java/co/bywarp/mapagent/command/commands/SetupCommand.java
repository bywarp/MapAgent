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
import co.bywarp.mapagent.utils.text.Lang;
import co.bywarp.mapagent.utils.text.TextUtil;

import org.bukkit.entity.Player;

public class SetupCommand extends Command {

    public SetupCommand() {
        super(Lang.generateSingleHelp("Parse", "/setup"), false);
    }

    @Override
    public CommandReturn execute(Player client, String command, String[] args) {
        if (args.length != 0) {
            return CommandReturn.HELP_MENU;
        }

        TextUtil.sendCenteredMessage(client, Lang.DIVIDER);
        client.sendMessage(" ");
        TextUtil.sendCenteredMessage(client, "&2&lQuickstart Guide");
        client.sendMessage(" ");
        TextUtil.sendCenteredMessage(client, "&fSetting up a map to parse is a");
        TextUtil.sendCenteredMessage(client, "&fa trivial process that can be done");
        TextUtil.sendCenteredMessage(client, "&fin around a minute's time.");
        client.sendMessage(" ");
        TextUtil.sendCenteredMessage(client, "&a&lStep 1:");
        TextUtil.sendCenteredMessage(client, "&fFirstly, teleport to your map and run");
        TextUtil.sendCenteredMessage(client, "&f&a/create <mapName> <gameType>");
        client.sendMessage(" ");
        TextUtil.sendCenteredMessage(client, "&fWhere &a<mapName> &fis the name you want");
        TextUtil.sendCenteredMessage(client, "&fto players to see when they play on your");
        TextUtil.sendCenteredMessage(client, "&fmap, and &a<gameType> &fis a valid gametype.");
        client.sendMessage(" ");
        TextUtil.sendCenteredMessage(client, "&fA list of gametypes can be seen by typing");
        TextUtil.sendCenteredMessage(client, "&a/gametypes&f.");
        client.sendMessage(" ");
        TextUtil.sendCenteredMessage(client, "&a&lStep 2:");
        TextUtil.sendCenteredMessage(client, "&fNow that you have registered your map,");
        TextUtil.sendCenteredMessage(client, "&fuse &a/author <name> &fto specify the author");
        TextUtil.sendCenteredMessage(client, "&fof the map &o(so people know it's you dummy.)");
        client.sendMessage(" ");
        TextUtil.sendCenteredMessage(client, "&a&lStep 3:");
        TextUtil.sendCenteredMessage(client, "&fFind the center of your map, and run &a/center");
        TextUtil.sendCenteredMessage(client, "&feither while standing on it, or run it specifying");
        client.sendMessage(" ");
        TextUtil.sendCenteredMessage(client, "&a&lStep 4:");
        TextUtil.sendCenteredMessage(client, "&fNow that all of the preliminary stuff is setup,");
        TextUtil.sendCenteredMessage(client, "&fyou are going to want to place down some special");
        TextUtil.sendCenteredMessage(client, "&fblocks called &eData Blocks &fwhich will allow");
        TextUtil.sendCenteredMessage(client, "&fthe parser to know what goes where.");
        client.sendMessage(" ");
        TextUtil.sendCenteredMessage(client, "&fA quick-and-easy way to find out what block does what");
        TextUtil.sendCenteredMessage(client, "&fis simply to run &a/blocks &fand you'll see!");
        client.sendMessage(" ");
        TextUtil.sendCenteredMessage(client, "&cImportant:");
        TextUtil.sendCenteredMessage(client, "&fMake sure there is a sign placed on top of");
        TextUtil.sendCenteredMessage(client, "&feach data block you place, so that if you use");
        TextUtil.sendCenteredMessage(client, "&fthat kind of block in your map, the parser");
        TextUtil.sendCenteredMessage(client, "&fwill be able to differentiate data blocks with");
        TextUtil.sendCenteredMessage(client, "&fnormal blocks in your map.");
        client.sendMessage(" ");
        TextUtil.sendCenteredMessage(client, "&a&lStep 5:");
        TextUtil.sendCenteredMessage(client, "&fOnce everything is the way you like it,");
        TextUtil.sendCenteredMessage(client, "&fyou can run &a/parse <radius> &fto parse your map.");
        TextUtil.sendCenteredMessage(client, "&fThis will scan the entire map (within the specified");
        TextUtil.sendCenteredMessage(client, "&fradius) and create a fancy map config for &eBacon &fto read.");
        client.sendMessage(" ");
        TextUtil.sendCenteredMessage(client, "&fThe radius parameter is how many blocks");
        TextUtil.sendCenteredMessage(client, "&fdiagonally from the center to scan.");
        client.sendMessage(" ");
        TextUtil.sendCenteredMessage(client, Lang.DIVIDER);
        return CommandReturn.EXIT;
    }

}
