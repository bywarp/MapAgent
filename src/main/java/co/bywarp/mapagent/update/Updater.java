/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.update;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;

public class Updater {

    private JavaPlugin plugin;
    @Getter private BukkitTask updater;

    public Updater(JavaPlugin plugin) {
        this.plugin = plugin;
        this.updater = new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    plugin.getServer().getPluginManager().callEvent(new UpdateEvent());
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

}

