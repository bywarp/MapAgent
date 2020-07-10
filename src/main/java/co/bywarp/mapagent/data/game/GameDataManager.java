/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data.game;

import co.bywarp.mapagent.MapAgent;
import co.bywarp.mapagent.parcel.prefs.ExtruderPreferences;

import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.stream.Stream;

public class GameDataManager {

    private MapAgent plugin;
    private ExtruderPreferences preferences;
    private ArrayList<GameData> registeredGames;

    public GameDataManager(MapAgent plugin, ExtruderPreferences preferences, GameData... toRegister) {
        this.plugin = plugin;
        this.preferences = preferences;
        this.registeredGames = new ArrayList<>();

        Stream<GameData> stream = Stream.of(toRegister);
        stream.forEach(gameData -> registeredGames.add(gameData));
    }

    public GameData getAgentForType(String unsafe) {
        String realUnsafe = unsafe.replaceAll("_", "");
        return registeredGames
                .stream()
                .filter(game -> {
                    String gameName = game.getName()
                            .replaceAll("_", "")
                            .replaceAll(" ", "");
                    return gameName.equalsIgnoreCase(realUnsafe);
                })
                .findFirst()
                .orElse(null);
    }

    public GameData getAgentForWorld(World world) {
        String name = world.getName();
        if (!name.contains("_")) {
            return null;
        }

        String gameName = name.split("_")[0];
        return getAgentForType(gameName);
    }

    public GameDataBlock getHandler(GameData data, Block block) {
        return getAllBlocks(data)
                .stream()
                .filter(dataBlock -> dataBlock.getMaterial() == block.getType() && dataBlock.getData() == block.getData())
                .findFirst()
                .orElse(null);
    }

    public ArrayList<GameDataBlock> getHandlers(String unsafe) {
        GameData data = getAgentForType(unsafe);
        if (data == null) {
            return new ArrayList<>();
        }

        return getAllBlocks(data);
    }

    public ArrayList<GameDataBlock> getHandlers(World unsafe) {
        GameData data = getAgentForWorld(unsafe);
        if (data == null) {
            return new ArrayList<>();
        }

        return getAllBlocks(data);
    }

    private ArrayList<GameDataBlock> getAllBlocks(GameData data) {
        ArrayList<GameDataBlock> blocks = new ArrayList<>(data.getDataBlocks());
        blocks.addAll(data.getTeams());
        return blocks;
    }

}
