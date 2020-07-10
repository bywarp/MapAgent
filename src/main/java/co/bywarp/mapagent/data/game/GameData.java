/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data.game;

import co.bywarp.mapagent.data.game.marker.CornerMarker;
import co.bywarp.mapagent.data.game.team.TeamData;

import java.util.ArrayList;
import java.util.stream.Stream;

import lombok.Getter;

@Getter
public abstract class GameData {

    private String name;
    private GameDataType type;
    private ArrayList<TeamData> teams;
//    private ArrayList<GameDataAttribute<?>> attributes;
    private ArrayList<GameDataBlock> dataBlocks;

    public GameData(String name, GameDataType type, ArrayList<TeamData> teams, /*ArrayList<GameDataAttribute<?>> attributes,*/ GameDataBlock... dataBlock) {
        this.name = name;
        this.type = type;
//        this.attributes = attributes;
        this.teams = teams;
        this.dataBlocks = new ArrayList<>();

        dataBlocks.add(new CornerMarker());

        Stream<GameDataBlock> toRegister = Stream.of(dataBlock);
        toRegister.forEach(block -> dataBlocks.add(block));
    }

}

