/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.utils;

import co.bywarp.mapagent.data.MapPoint;

import org.bukkit.Location;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

public class DataUtils {

    public static <T> ArrayList<T> inlineList(T... entries) {
        ArrayList<T> ret = new ArrayList<>();
        Stream<T> stream = Stream.of(entries);
        stream.forEach(ret::add);

        return ret;
    }

    public static <K, T> void getAndInsert(HashMap<K, ArrayList<T>> map, K key, T entry) {
        ArrayList<T> result = map.getOrDefault(key, new ArrayList<>());
        result.add(entry);
        map.put(key, result);
    }

    public static MapPoint fromLocation(Location location) {
        return new MapPoint(location.getX(),
                location.getY(),
                location.getZ(),
                location.getPitch(),
                location.getYaw());
    }

    public static MapPoint fromJson(JSONObject object) {
        if (object.isNull("x")
                || object.isNull("y")
                || object.isNull("z")) {
            return null;
        }

        MapPoint point = new MapPoint(object.getDouble("x"),
                object.getDouble("y"),
                object.getDouble("z"));

        if (!object.isNull("pitch")) {
            point.setPitch((float) object.getDouble("pitch"));
        }

        if (!object.isNull("yaw")) {
            point.setYaw((float) object.getDouble("yaw"));
        }

        return point;
    }

    public static void writeJson(File file, JSONObject object) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(object.toString(3));
        writer.flush();
        writer.close();
    }

}
