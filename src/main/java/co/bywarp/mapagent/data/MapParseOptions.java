/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data;

import co.bywarp.mapagent.MapAgent;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MapParseOptions {

    private MapAgent plugin;
    private String map;
    private String author;
    private MapPoint center;
    private int radius;
    private int maxY;
    private int minY;

}
