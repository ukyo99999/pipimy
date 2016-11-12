package com.macrowell.pipimy.utility;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtility {

	/* 取得JsonObject */
	protected static JSONObject getJSONObject(JSONObject json, String key) {
		if (json.has(key)) {
			if (json.isNull(key) == false) {
				return json.optJSONObject(key);
			}
		}
		return null;
	}

	/* 取得JsonArray */
	public static JSONArray getJSONArray(JSONObject json, String key) {
		if (json.has(key)) {
			if (json.isNull(key) == false) {
				return json.optJSONArray(key);
			}
		}
		return null;
	}

	/* 取得String */
	public static String getString(JSONObject json, String key) {
		if (json.has(key)) {
			return json.optString(key, "");
		}
		return null;
	}

	/*取得Int*/
	public static int getInt(JSONObject json, String key){
		if(json.has(key)){
				return json.optInt(key, 0);
		}
		return 0;
	}
	
	/*取得Long*/
	public static long getLong(JSONObject json, String key){
		if(json.has(key)){
			return json.optLong(key);
		}
		return 0;
	}
	
	/*取得Double*/
	protected static double getDouble(JSONObject json, String key){
		if(json.has(key)){
			return json.optDouble(key);
		}
		return 0;
	}
	
	/*取得boolean*/
	protected static boolean getBoolean(JSONObject json, String key){
		if(json.has(key)){
			return json.optBoolean(key, false);
		}
		return false;
	}
	
	
	
}
