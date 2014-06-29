package com.drift.util;

import java.util.Map;

import org.json.simple.JSONObject;

public class JSONUtil {
	
	public static String toJSONString(Map<String, Object> map) {
		
		JSONObject obj = new JSONObject(map);
		//System.out.println(obj.toJSONString());
		return obj.toJSONString();
		
	}

}
