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

    BINGO,
    CANNONS,
    DEATHMATCH,
    DEATH_RUN,
    DRAGON_RUN,
    EGG_HUNT,
    ICE_BALL,
    INFECTED,
    MELON_WARS,
    OITQ,
    SNOWFIGHT,
    SPLEEF,
    TEAM_DEATHMATCH;

    public static GameDataType match(String input) {
        for (GameDataType type : values()) {
            if (type.name().equalsIgnoreCase(input)) {
                return type;
            }
        }
        return null;
    }

}
