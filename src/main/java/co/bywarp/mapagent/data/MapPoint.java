/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.data;

import org.bukkit.Location;
import org.bukkit.World;

import lombok.Data;

@Data
public class MapPoint {

	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;

	public MapPoint(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public MapPoint(double x, double y, double z) {
		this(x, y, z, 0, 0);
	}

	public Location toLocation(World world) {
		return new Location(world, x, y, z, yaw, pitch);
	}

}
