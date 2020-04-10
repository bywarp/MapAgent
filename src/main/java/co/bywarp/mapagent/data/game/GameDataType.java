/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data.game;

public enum GameDataType {

    CANNONS,
    FLOWER_POWER,
    DEATH_RUN,
    INFECTED,
    BINGO;

    public static GameDataType match(String input) {
        for (GameDataType type : values()) {
            if (type.name().equalsIgnoreCase(input)) {
                return type;
            }
        }
        return null;
    }

}
