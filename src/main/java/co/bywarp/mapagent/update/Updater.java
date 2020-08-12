/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.update;

import co.bywarp.lightkit.util.Closable;
import co.bywarp.mapagent.MapAgent;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;

public class Updater implements Closable {

    private MapAgent plugin;
    @Getter private BukkitTask updater;

    public Updater(MapAgent plugin) {
        this.plugin = plugin;
        this.updater = new BukkitRunnable() {
            @Override
            public void run() {
                if (plugin.getCurrentParse() == null) {
                    close();
                    return;
                }

//                for (int i = 0; i < 15; i++) {
//                    if (plugin.getCurrentParse() == null) {
//                        close();
//                        break;
//                    }
//                    plugin.getServer().getPluginManager().callEvent(new UpdateEvent());
//                }
                plugin.getServer().getPluginManager().callEvent(new UpdateEvent());
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    @Override
    public void close() {
        updater.cancel();
    }

}

