/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.parser;

import co.bywarp.mapagent.MapAgent;
import co.bywarp.mapagent.data.MapParseOptions;
import co.bywarp.mapagent.data.MapPoint;
import co.bywarp.mapagent.data.game.GameData;
import co.bywarp.mapagent.data.game.GameDataBlock;
import co.bywarp.mapagent.data.game.GameDataManager;
import co.bywarp.mapagent.data.game.marker.CornerMarker;
import co.bywarp.mapagent.data.game.numbered.NumberedDataBlock;
import co.bywarp.mapagent.data.game.team.TeamData;
import co.bywarp.mapagent.data.repository.MapDataContainer;
import co.bywarp.mapagent.data.repository.MapDataRepository;
import co.bywarp.mapagent.parcel.prefs.ExtruderPreferences;
import co.bywarp.mapagent.update.UpdateEvent;
import co.bywarp.mapagent.update.Updater;
import co.bywarp.mapagent.utils.BlockUtils;
import co.bywarp.mapagent.utils.ChunkUtils;
import co.bywarp.mapagent.utils.DataUtils;
import co.bywarp.mapagent.utils.PlayerUtils;
import co.bywarp.mapagent.utils.text.Lang;

import co.m1ke.basic.utils.Closable;
import co.m1ke.basic.utils.Comparables;
import co.m1ke.basic.utils.MathUtils;
import co.m1ke.basic.utils.TimeUtil;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lombok.Getter;

@Getter
public class ChunkParser implements Listener, Closable {

    private MapAgent plugin;
    private ExtruderPreferences preferences;
    private MapParseOptions parseOptions;
    private MapDataRepository repository;
    private GameDataManager dataManager;
    private ParseState state;
    private Updater updater;
    private DecimalFormat df;
    private NumberFormat nf;

    private String mapName;
    private String mapAuthor;
    private MapPoint center;

    private int maxY;
    private int minY;
    private int radius;
    private int processed;
    private long start;
    private int countdown;
    private boolean running;
    private boolean canAbort;

    private World world;
    private List<Chunk> chunks;
    private Location cornerA;
    private Location cornerB;

    private GameData gameData;
    private HashMap<TeamData, ArrayList<MapPoint>> spawns;
    private HashMap<String, ArrayList<MapPoint>> customLocations;
    private HashMap<String, ArrayList<NumberedDataBlock>> numberedCustomLocations;
    private HashMap<Location, Block> dataBlockCache;

    public ChunkParser(MapAgent plugin, ExtruderPreferences preferences, MapParseOptions parseOptions, MapDataRepository repository, GameDataManager dataManager) {
        this.plugin = plugin;
        this.preferences = preferences;
        this.parseOptions = parseOptions;
        this.repository = repository;
        this.dataManager = dataManager;
    }

    public void start(Player player) {
        if (running) {
            return;
        }

        this.updater = new Updater(plugin);
        this.state = ParseState.INIT;

        this.df = new DecimalFormat("0.00");
        this.df.setRoundingMode(RoundingMode.CEILING);
        this.nf = NumberFormat.getInstance(Locale.US);

        this.mapName = parseOptions.getMap();
        this.mapAuthor = parseOptions.getAuthor();
        this.center = parseOptions.getCenter();

        this.maxY = parseOptions.getMaxY();
        this.minY = parseOptions.getMinY();
        this.radius = parseOptions.getRadius();
        this.processed = 0;
        this.start = System.currentTimeMillis();
        this.countdown = 100; // ticks
        this.running = true;
        this.canAbort = true;

        this.world = player.getWorld();
        this.cornerA = null;
        this.cornerB = null;

        this.gameData = dataManager.getAgentForWorld(world);
        if (gameData == null) {
            PlayerUtils.sendServerMessage(Lang.generate("Parse", "&f" + world.getName() + " &7is not properly formatted for parsing."));
            PlayerUtils.sendServerMessage(Lang.generate("Parse", "The world name must follow this format: &fGameName_MapName&7."));
            close();
            return;
        }

        this.spawns = new HashMap<>();
        this.customLocations = new HashMap<>();
        this.numberedCustomLocations = new HashMap<>();
        this.dataBlockCache = new HashMap<>();

        PlayerUtils.sendServerMessage(Lang.generate("Parse", "&f" + player.getName() + " &7has started parsing &f" + mapName + "&7."));
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void abort() {
        if (!running || !canAbort) {
            return;
        }

        this.close();
        this.plugin.setCurrentParse(null);
        this.running = false;

        // Put data blocks back
        dataBlockCache.forEach((location, block) -> {
            location.getBlock().setType(block.getType());
            location.getBlock().setData(block.getData());
        });

        dataBlockCache.clear();
        PlayerUtils.sendServerMessage(Lang.generate("Parse", "Parse for map &f" + world.getName() + " &7has been aborted."));
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (!running) {
            return;
        }

        // Initialization
        if (state == ParseState.INIT) {
            chunks = ChunkUtils.getChunks(center.toLocation(world), radius);
            if (chunks.isEmpty()) {
                PlayerUtils.sendServerMessage(Lang.generate("Parse", "No chunks found to parse."));
                abort();
                return;
            }

            state = ParseState.SCANNING;
            return;
        }

        // Scanning the map for data markers
        if (state == ParseState.SCANNING) {
            processed++;

            if (processed >= chunks.size()) {
                PlayerUtils.sendServerMessage(Lang.generate("Parse", "Scanned &f" + chunks.size() + " chunk" + TimeUtil.numberEnding(chunks.size()) + " &7in &f" + TimeUtil.getShortenedTimeValue(System.currentTimeMillis() - start) + "."));
                state = ParseState.PROCESSING;
                return;
            }

            double percentDone = MathUtils.percent(chunks.size(), processed);
            percentDone = Math.abs(100 - percentDone);

            PlayerUtils.sendActionBar(Lang.colorMessage("&2&lMap Parse &8▬ &f" + processed + "/" + chunks.size() + " &7processed &e(" + df.format(percentDone) + "%)"));

            Chunk chunk = chunks.get(processed);
            List<Block> blocks = ChunkUtils.getMatches(chunk, Material.SIGN_POST);
            blocks.removeIf(BlockUtils::validate);
            blocks.forEach(block -> {
                Block dataMarker = block.getRelative(BlockFace.DOWN);
                GameDataBlock dataBlock = dataManager.getHandler(gameData, dataMarker);
                if (dataBlock == null) {
                    return;
                }

                int x = dataMarker.getX();
                int z = dataMarker.getZ();

                Location dataMarkerCenter = dataMarker.getLocation().clone();
                dataMarkerCenter.add(x > 0 ? 0.5 : -0.5, 0.0, z > 0 ? 0.5 : -0.5);
                MapPoint point = DataUtils.fromLocation(dataMarkerCenter);

                // Hit a team spawnpoint
                if (dataBlock instanceof TeamData) {
                    DataUtils.getAndInsert(spawns, (TeamData) dataBlock, point);
                    PlayerUtils.sendServerMessage(Lang.generate("Parse", dataBlock.getColor() + dataBlock.getName() + " Team &7spawnpoint found at &f[" + Lang.prettifyLocation(dataMarker.getLocation()) + "]"));
                    clean(block, dataMarker);
                    return;
                }

                // Hit a corner
                if (dataBlock instanceof CornerMarker) {
                    if (cornerA == null) {
                        cornerA = block.getLocation();
                        PlayerUtils.sendServerMessage(Lang.generate("Parse", "&6Corner A &7found at &f[" + Lang.prettifyLocation(dataMarker.getLocation()) + "]"));
                        clean(block, dataMarker);
                        return;
                    }

                    if (cornerB == null) {
                        cornerB = block.getLocation();
                        PlayerUtils.sendServerMessage(Lang.generate("Parse", "&6Corner B &7found at &f[" + Lang.prettifyLocation(dataMarker.getLocation()) + "]"));
                        clean(block, dataMarker);
                        return;
                    }

                    PlayerUtils.sendServerMessage(Lang.generate("Parse", "&cWarning: &7Excess corner marker detected at &f[" + Lang.prettifyLocation(dataMarker.getLocation()) + "]"));
                    clean(block, dataMarker);
                    return;
                }

                Sign sign = (Sign) block.getState();
                String text = sign.getLine(0);
                if (!text.isEmpty() && Comparables.isNumeric(text)) {
                    NumberedDataBlock numberedData = new NumberedDataBlock(Integer.parseInt(text), dataMarkerCenter, dataBlock);
                    DataUtils.getAndInsert(numberedCustomLocations, dataBlock.getInternalName(), numberedData);
                    sign.setLine(0, "Scanned - Position #" + text);
                    clean(block, dataMarker);
                    PlayerUtils.sendServerMessage(Lang.generate("Parse", dataBlock.getColor() + dataBlock.getName() + " &f[#" + text + "] &7found at &f[" + Lang.prettifyLocation(dataMarker.getLocation()) + "]"));
                    return;
                }

                // Add custom location
                DataUtils.getAndInsert(customLocations, dataBlock.getInternalName(), point);
                clean(block, dataMarker);
                PlayerUtils.sendServerMessage(Lang.generate("Parse", dataBlock.getColor() + dataBlock.getName() + " &7found at &f[" + Lang.prettifyLocation(dataMarker.getLocation()) + "]"));
            });

            return;
        }

        // Done scanning, now compiling data.
        if (state == ParseState.PROCESSING) {
            JSONObject parcel = new JSONObject();
            MapDataContainer container = repository.getContainer(world);

            // Compile team spawns
            JSONObject teamSpawns = new JSONObject();
            for (Map.Entry<TeamData, ArrayList<MapPoint>> ent : spawns.entrySet()) {
                JSONArray spawns = new JSONArray();
                ent.getValue().forEach(point -> spawns.put(new JSONObject()
                        .put("x", point.getX())
                        .put("y", point.getY())
                        .put("z", point.getZ())));
                teamSpawns.put(ent.getKey().getName(), spawns);
            }

            // Compile custom locations
            JSONObject customLocs = new JSONObject();
            for (Map.Entry<String, ArrayList<MapPoint>> ent : customLocations.entrySet()) {
                JSONArray locations = new JSONArray();
                ent.getValue().forEach(point -> locations.put(new JSONObject()
                        .put("x", point.getX())
                        .put("y", point.getY())
                        .put("z", point.getZ())));
                customLocs.put(ent.getKey(), locations);
            }

            for (Map.Entry<String, ArrayList<NumberedDataBlock>> ent : numberedCustomLocations.entrySet()) {
                JSONArray locations = new JSONArray();
                LinkedList<NumberedDataBlock> sortedBlocks = new LinkedList<>(ent.getValue());
                sortedBlocks.sort(Comparator.comparing(NumberedDataBlock::getNumber));

                sortedBlocks.forEach(block -> locations.put(new JSONObject()
                        .put("x", block.getLocation().getX())
                        .put("y", block.getLocation().getY())
                        .put("z", block.getLocation().getZ())));

                customLocs.put(ent.getKey(), locations);
            }

            // Create preference parcel data
            parcel.put("name", mapName);
            parcel.put("author", mapAuthor);
            parcel.put("description", "");
            parcel.put("center", new JSONObject()
                    .put("x", center.getX())
                    .put("y", center.getY())
                    .put("z", center.getZ()));
            parcel.put("spawns", teamSpawns);
            parcel.put("custom", customLocs);
            parcel.put("data", new JSONObject(container.getData()));

            // Attempt to write parcel data to map parcel file
            try {
                File mapParcel = new File(world.getWorldFolder(), "config.json");
                if (mapParcel.exists()) {
                    FileUtils.forceDelete(mapParcel);
                }

                mapParcel.createNewFile();
                DataUtils.writeJson(mapParcel, parcel);
            } catch (IOException ex) {
                PlayerUtils.sendServerMessage(Lang.generate("Parse", "An exception occurred while exporting the preference parcel."));
                PlayerUtils.sendServerMessage(Lang.generate("Parse", "&c" + ex.getMessage() + " (" + ex.getClass().getSimpleName() + ")"));
                this.abort();
                return;
            }

            // If we want to extrude to master, try to copy to master
            if (preferences.isExtrudeToMaster()) {
                File masterRepo = preferences.getMapRepository();
                if (!masterRepo.exists()) {
                    PlayerUtils.sendServerMessage(Lang.generate("Parse", "&cWarning: &7Master map repository does not exist."));
                    PlayerUtils.sendServerMessage(Lang.colorMessage(" &7- &fThe preference parcel has been successfully exported, but we couldn't ship it off to the repository."));
                    PlayerUtils.sendServerMessage(Lang.colorMessage(" &7- &fManual action is required to deploy this map."));

                    state = ParseState.DONE;
                    canAbort = false;
                    dataBlockCache.clear();
                    return;
                }

                try {
                    world.setGameRuleValue("commandBlockOutput", "false");
                    world.setGameRuleValue("doWeatherCycle", "false");
                    world.setGameRuleValue("keepInventory", "false");
                    world.setGameRuleValue("mobGriefing", "false");
                    world.setGameRuleValue("doDaylightCycle", "false");
                    world.setGameRuleValue("doMobSpawning", "false");
                    world.setTime(6000L);
                    world.getEntities().forEach(entity -> {
                        if (!(entity instanceof LivingEntity)) {
                            return;
                        }

                        if (entity.getType() == EntityType.ARMOR_STAND) {
                            return;
                        }

                        LivingEntity ent = (LivingEntity) entity;
                        ent.setHealth(0);
                        ent.remove();
                    });

                    world.save();

                    File gameDir = new File(masterRepo, gameData.getName());
                    FileUtils.deleteDirectory(new File(gameDir, mapName)); // delete if already exists
                    FileUtils.copyDirectory(world.getWorldFolder(), new File(gameDir, mapName));
                    PlayerUtils.sendServerMessage(Lang.generate("Parse", "Deployed &f" + mapName + " &7to the map repository."));
                } catch (IOException ex) {
                    PlayerUtils.sendServerMessage(Lang.generate("Parse", "An exception occurred while deploying this map."));
                    PlayerUtils.sendServerMessage(Lang.generate("Parse", "&c" + ex.getMessage() + " (" + ex.getClass().getSimpleName() + ")"));
                }
            }

            // Finalize
            state = ParseState.DONE;
            canAbort = false;
            dataBlockCache.clear();
            return;
        }

        if (state == ParseState.DONE) {
            PlayerUtils.sendServerMessage(Lang.generate("Parse", "Finished parsing &f" + mapName + " &7in &f" + TimeUtil.getShortenedTimeValue(System.currentTimeMillis() - start) + "&7."));
            close();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload");
            return;
        }
    }

    private void clean(Block block, Block dataMarker) {
        dataBlockCache.put(block.getLocation(), block);
        dataBlockCache.put(dataMarker.getLocation(), block.getRelative(BlockFace.DOWN));

        block.setType(Material.AIR);
        block.getRelative(BlockFace.DOWN).setType(Material.AIR);

        Block under = dataMarker.getRelative(BlockFace.DOWN);
        if (under.getType() == Material.DIRT) {
            under.setType(Material.GRASS);
        }
    }

    @Override
    public void close() {
        updater.getUpdater().cancel();
        updater = null;
        plugin.setCurrentParse(null);
        HandlerList.unregisterAll(this);
    }

}
