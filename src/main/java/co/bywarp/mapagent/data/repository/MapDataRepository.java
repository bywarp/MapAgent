/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data.repository;

import co.bywarp.mapagent.MapAgent;
import co.bywarp.mapagent.data.MapPoint;
import co.bywarp.mapagent.data.game.GameDataType;
import co.bywarp.mapagent.utils.DataUtils;
import co.bywarp.mapagent.utils.text.Lang;

import co.m1ke.basic.logger.Logger;
import co.m1ke.basic.utils.Comparables;
import co.m1ke.basic.utils.JsonUtils;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

public class MapDataRepository {

    private MapAgent plugin;
    private File file;
    private JSONObject store;
    private Logger logger;
    private HashMap<String, MapDataContainer> containers;

    public MapDataRepository(MapAgent plugin, File file) {
        this.plugin = plugin;
        this.file = file;
        this.logger = new Logger("Data Repository");
        this.containers = new HashMap<>();

        try {
            if (!Comparables.isJson(FileUtils.readFileToString(file))) {
                Bukkit.getPluginManager().disablePlugin(plugin);
                logger.severe("The data repository is not a valid JSON file.");
                return;
            }

            this.store = JsonUtils.getFromFile(file);
            if (store == null) {
                Bukkit.getPluginManager().disablePlugin(plugin);
                logger.severe("Failed to retrieve data from the repository.");
                return;
            }

            if (store.isNull("repository")) {
                return;
            }

            JSONArray repo = store.getJSONArray("repository");
            repo.forEach(obj -> {
                if (!(obj instanceof JSONObject)) {
                    return;
                }

                JSONObject entry = (JSONObject) obj;
                containers.put(entry.getString("worldName"), new MapDataContainer(
                        entry.getString("worldName"),
                        entry.getString("name"),
                        entry.getString("game"),
                        entry.getString("author"),
                        DataUtils.fromJson(entry.getJSONObject("center"))));
            });

            logger.info("Loaded " + containers.size() + " entr" + Lang.cond(containers.size() > 1, "ies", "y") + " from the repository.");
        } catch (JSONException | IOException ex) {
            logger.except(ex, "Error reading from the repository");
        }
    }

    /**
     * Creates a container entry.
     *
     * @param world the world
     * @param name the name
     * @param game the game
     */
    public void createEntry(World world, String name, String game) {
        if (exists(world)) {
            return;
        }

        store.getJSONArray("repository")
                .put(new JSONObject()
                .put("worldName", world.getName())
                .put("name", name)
                .put("game", game)
                .put("author", "None")
                .put("center", new JSONObject()
                        .put("x", 0)
                        .put("y", 90)
                        .put("z", 0)));
        containers.put(world.getName(), new MapDataContainer(
                world.getName(),
                name,
                game,
                "None",
                new MapPoint(0, 0, 0))
        );
    }

    /**
     * Checks if a world already exists.
     *
     * @param world the world
     * @return if it exists
     */
    public boolean exists(World world) {
        if (containers.containsKey(world.getName())) {
            return true;
        }

        JSONArray array = store.getJSONArray("repository");
        for (int i = 0; i < array.length(); i++) {
            JSONObject cur = array.getJSONObject(i);
            if (cur.getString("worldName").equalsIgnoreCase(world.getName())) {
                return true;
            }
        }

        return false;
    }

    public boolean isReady(MapDataContainer container) {
        return container.getName() != null
                && container.getAuthor() != null
                && !container.getAuthor().equalsIgnoreCase("None")
                && !container.getAuthor().isEmpty()
                && container.getAuthor().length() > 2
                && container.getAuthor().length() < 26
                && container.getCenter() != null
                && !((container.getCenter().getX() == 0)
                    && (container.getCenter().getY() == 0)
                    && (container.getCenter().getZ() == 0))
                && container.getGame() != null
                && GameDataType.match(container.getGame()) != null;
    }

    /**
     * Attempts to set the author of
     * a map container, provided a world.
     *
     * @param world the world
     * @param author the author
     */
    public void setAuthor(World world, String author, Consumer<String> success, Consumer<Exception> failure) {
        MapDataContainer container = getContainer(world);
        if (container == null) {
            failure.accept(new NullPointerException("MapDataContainer is null"));
            return;
        }

        container.setAuthor(author);
        containers.replace(world.getName(), container);

        this.updateStore();
        this.write();

        success.accept(author);
    }

    /**
     * Attempts to set the game of
     * a map container, provided a world.
     *
     * @param world the world
     * @param type the gametype
     */
    public void setGame(World world, GameDataType type, Consumer<GameDataType> success, Consumer<Exception> failure) {
        MapDataContainer container = getContainer(world);
        if (container == null) {
            failure.accept(new NullPointerException("MapDataContainer is null"));
            return;
        }

        container.setGame(Lang.capitalizeFirst(type.name().toLowerCase()));
        containers.replace(world.getName(), container);

        this.updateStore();
        this.write();

        success.accept(type);
    }

    /**
     * Attempts to set the center of
     * a map container, provided a world.
     *
     * @param world the world
     * @param center the center
     */
    public void setCenter(World world, MapPoint center, Consumer<MapPoint> success, Consumer<Exception> failure) {
        MapDataContainer container = getContainer(world);
        if (container == null) {
            failure.accept(new NullPointerException("MapDataContainer is null"));
            return;
        }

        container.setCenter(center);
        containers.replace(world.getName(), container);

        this.updateStore();
        this.write();

        success.accept(center);
    }

    public MapDataContainer getContainer(World world) {
        return getContainer(world.getName());
    }

    public MapDataContainer getContainer(String unsafe) {
        return containers.get(unsafe);
    }

    public void updateStore() {
        JSONArray backup = store.getJSONArray("repository");
        JSONArray updated = new JSONArray();
        for (MapDataContainer container : containers.values()) {
            MapPoint center = container.getCenter();
            updated.put(new JSONObject()
                    .put("worldName", container.getWorldName())
                    .put("name", container.getName())
                    .put("game", container.getGame())
                    .put("author", "None")
                    .put("center", new JSONObject()
                            .put("x", center.getX())
                            .put("y", center.getY())
                            .put("z", center.getZ())));
        }

        if (backup == updated) {
            return;
        }

        store.remove("repository");
        store.put("repository", updated);
    }

    public void write() {
        try {
            FileWriter writer = new FileWriter(this.file);
            writer.write(store.toString(3));
            writer.flush();
            writer.close();
        } catch (IOException ignored) {
        }
    }

}
