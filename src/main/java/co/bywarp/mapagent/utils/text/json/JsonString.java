/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent.utils.text.json;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.stream.JsonWriter;

/**
 * Represents a JSON string value.
 * Writes by this object will not write name values nor begin/end objects in the JSON stream.
 * All writes merely write the represented string value.
 */
final class JsonString implements JsonRepresentedObject, ConfigurationSerializable {

	private String _value;

	public JsonString(CharSequence value) {
		_value = value == null ? null : value.toString();
	}

	@Override
	public void writeJson(JsonWriter writer) throws IOException {
		writer.value(getValue());
	}

	public String getValue() {
		return _value;
	}

	public Map<String, Object> serialize() {
		HashMap<String, Object> theSingleValue = new HashMap<String, Object>();
		theSingleValue.put("stringValue", _value);
		return theSingleValue;
	}

	public static JsonString deserialize(Map<String, Object> map) {
		return new JsonString(map.get("stringValue").toString());
	}

	@Override
	public String toString() {
		return _value;
	}

}
