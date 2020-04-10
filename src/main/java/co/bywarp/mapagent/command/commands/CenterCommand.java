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
import co.bywarp.mapagent.data.MapPoint;
import co.bywarp.mapagent.data.repository.MapDataRepository;
import co.bywarp.mapagent.utils.DataUtils;
import co.bywarp.mapagent.utils.text.Lang;

import co.m1ke.basic.utils.Comparables;

import org.bukkit.entity.Player;

public class CenterCommand extends Command {

    private MapDataRepository repository;

    public CenterCommand(MapDataRepository repository) {
        super(Lang.generateHelpList("Parse", "Listing Commands:",
                "/center",
                "/center <x> <y> <z>"),
                true);
        this.repository = repository;
    }

    @Override
    public CommandReturn execute(Player client, String command, String[] args) {
        if (!repository.exists(client.getWorld())) {
            client.sendMessage(Lang.generate("Parse", "This world is not registered as a parsable map."));
            return CommandReturn.EXIT;
        }

        if (args.length == 0) {
            repository.setCenter(client.getWorld(), DataUtils.fromLocation(client.getLocation()),
                    (point) -> client.sendMessage(Lang.generate("Parse", "Map center changed to &f[" + Lang.prettifyLocation(client.getLocation()) + "]")),
                    (ex) -> client.sendMessage(Lang.generate("Parse", "Failed to change map center.")));
            return CommandReturn.EXIT;
        }

        if (args.length == 3) {
            if (!Comparables.isDouble(args[0])
                    || !Comparables.isDouble(args[1])
                    || !Comparables.isDouble(args[2])) {
                String whichBad = Lang.cond(!Comparables.isDouble(args[0]), args[0],
                        Lang.cond(!Comparables.isNumeric(args[1]), args[1], args[2]));
                client.sendMessage(Lang.generate("Parse", "Invalid Coordinates &f[" + whichBad + "]"));
                return CommandReturn.EXIT;
            }

            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);

            MapPoint center = new MapPoint(x, y, z);
            repository.setCenter(client.getWorld(), center,
                    (point) -> client.sendMessage(Lang.generate("Parse", "Map center changed to &f[" + Lang.prettifyLocation(client.getLocation()) + "]")),
                    (ex) -> client.sendMessage(Lang.generate("Parse", "Failed to change map center.")));
            return CommandReturn.EXIT;
        }

        return CommandReturn.HELP_MENU;
    }

}
