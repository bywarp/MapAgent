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
import co.bywarp.mapagent.data.game.GameDataBlock;
import co.bywarp.mapagent.data.game.GameDataManager;
import co.bywarp.mapagent.data.repository.MapDataContainer;
import co.bywarp.mapagent.data.repository.MapDataRepository;
import co.bywarp.mapagent.utils.text.Lang;
import co.bywarp.mapagent.utils.text.json.FancyMessage;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BlocksCommand extends Command {

    private GameDataManager manager;
    private MapDataRepository repository;

    public BlocksCommand(GameDataManager manager, MapDataRepository repository) {
        super(Lang.generateSingleHelp("Parse", "/blocks"), false);
        this.manager = manager;
        this.repository = repository;
    }

    @Override
    public CommandReturn execute(Player client, String command, String[] args) {
        if (args.length != 0) {
            return CommandReturn.HELP_MENU;
        }

        MapDataContainer mapData = repository.getContainer(client.getWorld());
        if (mapData == null) {
            client.sendMessage(Lang.generate("Parse", "This world does not have a map container."));
            return CommandReturn.EXIT;
        }

        ArrayList<GameDataBlock> dataBlocks = manager.getHandlers(mapData.getGame());
        if (dataBlocks == null) {
            client.sendMessage(Lang.generate("Parse", "Failed to retrieve data blocks for this map."));
            return CommandReturn.EXIT;
        }

        if (dataBlocks.isEmpty()) {
            client.sendMessage(Lang.generate("Parse", "This gametype does not have any data blocks."));
            return CommandReturn.EXIT;
        }

        client.sendMessage(Lang.generate("Parse", "Listing Data Blocks:"));
        for (GameDataBlock block : dataBlocks) {
            FancyMessage mes = new FancyMessage(Lang.colorMessage(" &7- "))
                    .then(Lang.colorMessage("&3[G]"))
                        .tooltip(Lang.colorMessage("&3&lGive Data Item\n&fClick this to receive a " + block.getColor() + block.getName() + " &fblock."))
                        .command("/give " + client.getName() + " " + block.getMaterial().getId() + ":" + block.getData() + " 1")
                    .then(Lang.colorMessage(" &8▬ "))
                    .then(Lang.colorMessage(block.getColor() + block.getName() + "&7: " + Lang.capitalizeFirst(block.getMaterial().name().toLowerCase()) + " &f(Data: " + block.getData() + ")"));
            mes.send(client.getPlayer());
        }

        return CommandReturn.EXIT;
    }

}