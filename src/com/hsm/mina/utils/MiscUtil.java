package com.hsm.mina.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.hsm.mina.json.JSONArray;
import com.hsm.mina.json.JSONObject;

public class MiscUtil {
	public static Object LoadJSON(String fileBuff) throws IOException {
        fileBuff = fileBuff.trim();
        
        if(fileBuff.charAt(0) == '[') {
        	return (new JSONArray(fileBuff));
        }
        else if(fileBuff.charAt(0) == '{') {
        	return (new JSONObject(fileBuff));
        }
        else {
        	return null;
        }
	}
	
	public static String readTextFileContent(InputStream ins) throws Exception {
		InputStreamReader isr = null;
		
		try	{
			isr = new InputStreamReader(ins);

			int len = ins.available();
			byte bTemp[] = new byte[len];

			ins.read(bTemp);
			return new String(bTemp, isr.getEncoding());
		}
		finally	{
			if (isr != null) {
				isr.close();
				isr = null;
			}
		}
	}
}
