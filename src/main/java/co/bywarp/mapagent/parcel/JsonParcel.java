/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.parcel;

import co.bywarp.lightkit.util.Closable;
import co.bywarp.lightkit.util.JsonUtils;
import co.bywarp.lightkit.util.logger.Logger;
import co.bywarp.lightkit.util.timings.Timings;
import co.bywarp.mapagent.MapAgent;
import co.bywarp.mapagent.parcel.prefs.ExtruderPreferences;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.function.Consumer;

import lombok.Getter;

public class JsonParcel implements Closable {

    private MapAgent plugin;
    private File file;
    private Logger logger;

    @Getter private ExtruderPreferences extruderPreferences;

    public JsonParcel(MapAgent plugin, File file) {
        Timings timings = new Timings("Prefs", "Load Parcel");

        this.plugin = plugin;
        this.file = file;
        this.logger = new Logger("Prefs");

        try {
            if (!file.exists()) {
                boolean result = file.createNewFile();
                if (!result) {
                    logger.severe("Error creating parcel.");
                    disable();
                    return;
                }

                logger.warning("Preference parcel is empty.");
                disable();
                return;
            }

            JSONObject store = JsonUtils.getFromFile(this.file);
            disableIfNotPresent(store, "extruder");

            JSONObject extruder = store.getJSONObject("extruder");
            this.extruderPreferences = new ExtruderPreferences(
                    new File(extruder.getString("mapRepository")),
                    extruder.getBoolean("shouldExtrude")
            );

            timings.complete("Parcel loaded in %c%tms%r.");
        } catch (Exception e) {
            logger.except(e, "Error parsing parcel");
        }
    }

    public void write(String node, String inner, Object obj, Consumer<Exception> failure) {
        try {
            JSONObject store = JsonUtils.getFromFile(this.file);
            store.getJSONObject(node).remove(inner);
            store.getJSONObject(node).put(inner, obj);

            FileWriter writer = new FileWriter(this.file);
            writer.write(store.toString(3));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            failure.accept(e);
            logger.except(e, "Failed to write to preferences");
        }
    }

    public void write(String node, String inner, Object obj, Consumer<Object> success, Consumer<Exception> failure) {
        try {
            JSONObject store = JsonUtils.getFromFile(this.file);
            store.getJSONObject(node).remove(inner);
            store.getJSONObject(node).put(inner, obj);

            FileWriter writer = new FileWriter(this.file);
            writer.write(store.toString(3));
            writer.flush();
            writer.close();

            success.accept(obj);
        } catch (Exception e) {
            failure.accept(e);
            logger.except(e, "Failed to write to preferences");
        }
    }

    private void disable() {
        if (this.plugin != null) {
            this.plugin
                    .getServer()
                    .getPluginManager()
                    .disablePlugin(this.plugin);
        }
    }

    private void disableIfNotPresent(JSONObject store, String... keys) {
        for (String s : keys) {
            if (store.isNull(s)) {
                logger.warning("Error reading " + s + " preferences.");
                this.disable();
            }
        }
    }

    @Override
    public void close() {
        this.file = null;
        this.logger = null;
        this.extruderPreferences = null;
    }

}
