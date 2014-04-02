package se.iandwe.fingerdancer.db;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;


public class JSONHelper {
   
	

    public static void setPushMessageUnreadTo(Context c, String value) {
    	JSONObject push;
		try {
			push = JSONSharedPreferences.loadJSONObject(c, "pushmessage", "push");
			push.put("unread", value);
			JSONSharedPreferences.saveJSONObject(c, "pushmessage", "push", push);
		} catch (JSONException e) {
		}
    }
    
    public static boolean checkIfNewRecord(Context c, int value)
    {
    	try {
			JSONObject oldRecordObj = JSONSharedPreferences.loadJSONObject(c, "record", "record");
			int oldrecord = oldRecordObj.getInt("record");
			if(value > oldrecord)
			{
				oldRecordObj.put("record", value);
				JSONSharedPreferences.saveJSONObject(c, "record", "record", oldRecordObj);
				return true;
			}
			else
			{
				return false;
			}
		} catch (JSONException e1) {
			JSONObject newObj = new JSONObject();
			try {
				newObj.put("record", value);
				JSONSharedPreferences.saveJSONObject(c, "record", "record", newObj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
    }
    
    public static int getCurrentRecord(Context c)
    {
    	JSONObject oldRecordObj;
		try {
			oldRecordObj = JSONSharedPreferences.loadJSONObject(c, "record", "record");
			int oldrecord = oldRecordObj.getInt("record");
			return oldrecord;
		} catch (JSONException e) {
			return 0;
		}
		
    }
    
    public static boolean getHelsingborgPushtokenUploaded(Context c)
    {
    	JSONObject obj;
		try {
			obj = JSONSharedPreferences.loadJSONObject(c, "helpush", "helpush");
			boolean uploaded = obj.getBoolean("uploaded");
			return uploaded;
		} catch (JSONException e1) {
			return false;
		}
    	
    }
    
    
    
   /* public static Me getMe(Context context)
    {
    	JSONObject mejson;
		try {
			mejson = JSONSharedPreferences.loadJSONObject(context, "me", "meinfo");
			return new Me(mejson);
		} catch (JSONException e) 
		{
			return null;
		}
    }
    
    public static void saveMe(Context context, JSONObject obj)
    {
    	JSONSharedPreferences.saveJSONObject(context, "me", "meinfo", obj);
    }*/
    
  
}
