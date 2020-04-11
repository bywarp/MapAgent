/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.command.commands;

import co.bywarp.mapagent.MapAgent;
import co.bywarp.mapagent.command.Command;
import co.bywarp.mapagent.command.CommandReturn;
import co.bywarp.mapagent.data.MapParseOptions;
import co.bywarp.mapagent.data.game.GameDataManager;
import co.bywarp.mapagent.data.repository.MapDataContainer;
import co.bywarp.mapagent.data.repository.MapDataRepository;
import co.bywarp.mapagent.parcel.prefs.ExtruderPreferences;
import co.bywarp.mapagent.parser.ParseState;
import co.bywarp.mapagent.parser.Parser;
import co.bywarp.mapagent.utils.text.Lang;

import co.m1ke.basic.utils.Comparables;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class ParseCommand extends Command {

    private MapAgent plugin;
    private ExtruderPreferences preferences;
    private MapDataRepository repository;
    private GameDataManager manager;

    public ParseCommand(MapAgent plugin, ExtruderPreferences preferences, MapDataRepository repository, GameDataManager manager) {
        super(Lang.generateHelpList("Parse", "Listing Commands:",
                "/parse <radius> <maxY>",
                "/parse abort"), true);
        this.plugin = plugin;
        this.preferences = preferences;
        this.repository = repository;
        this.manager = manager;
    }

    @Override
    public CommandReturn execute(Player client, String command, String[] args) {
        if (args.length == 0) {
            return CommandReturn.HELP_MENU;
        }

        Parser currentParse = plugin.getCurrentParse();
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("abort")) {
                if (currentParse == null) {
                    client.sendMessage(Lang.generate("Parse", "There are no ongoing jobs."));
                    return CommandReturn.EXIT;
                }

                if (!currentParse.isCanAbort()) {
                    client.sendMessage(Lang.generate("Parse", "You cannot abort this job now."));
                    return CommandReturn.EXIT;
                }

                client.sendMessage(Lang.generate("Parse", "Sent abort signal to the parser engine."));
                currentParse.abort();
                return CommandReturn.EXIT;
            }
            return CommandReturn.EXIT;
        }

        if (args.length == 2) {
            if (currentParse != null && currentParse.getState() != ParseState.DONE) {
                client.sendMessage(Lang.generate("Parse", "&f" + plugin.getCurrentParse().getMapName() + " &7is currently being parsed."));
                return CommandReturn.EXIT;
            }

            if (!repository.exists(client.getWorld())) {
                client.sendMessage(Lang.generate("Parse", "This world is not registered as a parsable map."));
                return CommandReturn.EXIT;
            }

            MapDataContainer container = repository.getContainer(client.getWorld());
            if (!repository.isReady(container)) {
                client.sendMessage(Lang.generate("Parse", "This map isn't ready to be parsed."));
                client.sendMessage(Lang.generate("Parse", "Please ensure the following conditions are met:"));
                client.sendMessage(Lang.colorMessage(" &7- &fThe map's name is set"));
                client.sendMessage(Lang.colorMessage(" &7- &fThe map's author is set"));
                client.sendMessage(Lang.colorMessage(" &7- &fThe map's author is not &e\"None\""));
                client.sendMessage(Lang.colorMessage(" &7- &fThe map's author is between 2, and 26 characters"));
                client.sendMessage(Lang.colorMessage(" &7- &fThe map's center is set"));
                client.sendMessage(Lang.colorMessage(" &7- &fThe map's center isn't the default &e0, 0, 0 &fcenter"));
                client.sendMessage(Lang.colorMessage(" &7- &fThe map's game is set"));
                client.sendMessage(Lang.colorMessage(" &7- &fThe map's game is valid"));
                return CommandReturn.EXIT;
            }

            if (!Comparables.isNumeric(args[0])) {
                client.sendMessage(Lang.generate("Parse", "Invalid Radius &f[" + args[0] + "]"));
                return CommandReturn.EXIT;
            }

            if (!Comparables.isNumeric(args[1])) {
                client.sendMessage(Lang.generate("Parse", "Invalid Max-Y Value &f[" + args[1] + "]"));
                return CommandReturn.EXIT;
            }

            int radius = Integer.parseInt(args[0]);
            int maxY = Integer.parseInt(args[1]);
            if (maxY > 256 || maxY < 0) {
                client.sendMessage(Lang.generate("Parse", "Invalid Max-Y Coordinate &f[" + args[1] + "]"));
                return CommandReturn.EXIT;
            }

            MapParseOptions options = new MapParseOptions(getPlugin(), container.getName(), container.getAuthor(), container.getCenter(), radius, maxY);
            Parser parser = new Parser(plugin, preferences, options, manager);
            ConversationFactory conversationFactory = new ConversationFactory(plugin)
                    .withFirstPrompt(new ConfirmationPrompt(parser, plugin, container.getName()))
                    .withEscapeSequence("/no")
                    .withModality(false)
                    .withLocalEcho(false);

            BukkitTask task = new BukkitRunnable() {

                World world = client.getWorld();

                Location center = container.getCenter().toLocation(world).clone();
                Location corner1 = center.clone().add(radius, maxY - center.clone().getY(), radius);
                Location corner2 = center.clone().subtract(radius, center.clone().getY(), radius);

                double space = 0.5;
                List<Location> list = new ArrayList<Location>() {
                    {
                        addAll(createBorder(world, corner1, corner2, space));
                    }
                };

                @Override
                public void run() {
                    list.forEach(loc -> world.playEffect(loc, Effect.COLOURED_DUST, 10));
                }

            }.runTaskTimer(plugin, 0L, 20L);

            Conversable conversable = client.getPlayer();
            Conversation conversation = conversationFactory.buildConversation(conversable);
            conversation.getContext().setSessionData("visualizer", task);

            conversable.beginConversation(conversation);
            return CommandReturn.EXIT;
        }
        return CommandReturn.HELP_MENU;
    }

    private static class ConfirmationPrompt extends StringPrompt {

        private Parser parser;
        private MapAgent plugin;
        private String mapName;

        public ConfirmationPrompt(Parser parser, MapAgent plugin, String mapName) {
            this.parser = parser;
            this.plugin = plugin;
            this.mapName = mapName;
        }

        @Override
        public String getPromptText(ConversationContext context) {
            return Lang.generate("Parse", "Are you sure you want begin parsing &f" + mapName.split("_")[1] + "&7?") + "\n"
                    + Lang.colorMessage(" &7- &fThe visualizer will help you ensure your settings are correct.") + "\n"
                    + Lang.colorMessage(" &7- &fType &a(Y)ES &fto confirm, or &c(N)O &fto abort.");
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String s) {
            Conversable conversable = context.getForWhom();
            if (conversable instanceof CommandSender) {
                BukkitTask visualizer = (BukkitTask) context.getSessionData("visualizer");
                visualizer.cancel();

                if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("y")) {
                    parser.start((Player) conversable);
                    plugin.setCurrentParse(parser);
                    return END_OF_CONVERSATION;
                }

                if (s.equalsIgnoreCase("no") || s.equalsIgnoreCase("n")) {
                    ((CommandSender) conversable).sendMessage(Lang.generate("Parse", "Cancelled parsing &f" + mapName + "&7."));
                    parser = null;
                    return END_OF_CONVERSATION;
                }

                return END_OF_CONVERSATION;
            }

            return END_OF_CONVERSATION;
        }

    }

    public List<Location> createBorder(World world, Location corner1, Location corner2, double particleDistance) {
        List<Location> result = new ArrayList<>();
        double minX = Math.min(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        for (double x = minX; x <= maxX; x += particleDistance) {
            for (double y = minY; y <= maxY; y += particleDistance) {
                for (double z = minZ; z <= maxZ; z += particleDistance) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        result.add(new Location(world, x, y, z));
                    }

                }
            }
        }

        return result;
    }



}
