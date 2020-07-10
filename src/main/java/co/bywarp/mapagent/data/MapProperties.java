/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data;

import co.bywarp.mapagent.data.game.team.TeamData;

import org.json.JSONObject;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MapProperties {

	private String name;
	private String author;
	private String description;
	private JSONObject data;
	private MapPoint center;
	private HashMap<TeamData, MapPoint> spawns;
	private HashMap<String, MapPoint> customLocations;

}
