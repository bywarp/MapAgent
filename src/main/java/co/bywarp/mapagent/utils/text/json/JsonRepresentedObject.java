/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.utils.text.json;

import java.io.IOException;

import com.google.gson.stream.JsonWriter;

/**
 * Represents an object that can be serialized to a JSON writer instance.
 */
interface JsonRepresentedObject {

	/**
	 * Writes the JSON representation of this object to the specified writer.
	 * @param writer The JSON writer which will receive the object.
	 * @throws IOException If an error occurs writing to the stream.
	 */
	public void writeJson(JsonWriter writer) throws IOException;
	
}
