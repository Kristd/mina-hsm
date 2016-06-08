package com.hsm.mina.utils;

import com.hsm.mina.json.JSONArray;
import com.hsm.mina.json.JSONObject;

public class JSONUtil {
	public static JSONArray parserJSONArray(String filestream) {
		try {
			return (JSONArray) MiscUtil.LoadJSON(filestream);
		}
		catch(Exception ex) {
			return null;
		}
	}
	
	public static JSONObject parserJSONObject(String filestream) {
		try {
			return (JSONObject) MiscUtil.LoadJSON(filestream);
		}
		catch(Exception ex) {
			return null;
		}
	}
}
