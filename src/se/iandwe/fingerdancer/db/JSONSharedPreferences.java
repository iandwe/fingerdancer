package se.iandwe.fingerdancer.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class JSONSharedPreferences {
    private static final String PREFIX = "json";
    public static void saveJSONObject(Context c, String prefName, String key, JSONObject object) {
    
		SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(JSONSharedPreferences.PREFIX+key, object.toString());
        editor.commit();
    	
    }

    public static void saveJSONArray(Context c, String prefName, String key, JSONArray array)
    {
	    
		SharedPreferences settings = c.getSharedPreferences(prefName, 0);
		if(settings != null)
		{
			SharedPreferences.Editor editor = settings.edit();
            editor.putString(JSONSharedPreferences.PREFIX+key, array.toString());
            editor.commit();
		}
    }

    public static JSONObject loadJSONObject(Context c, String prefName, String key) throws JSONException {
    	
		  SharedPreferences settings = c.getSharedPreferences(prefName, 0);
	      return new JSONObject(settings.getString(JSONSharedPreferences.PREFIX+key, "{}"));      
    }

    public static JSONArray loadJSONArray(Context c, String prefName, String key) throws JSONException {
    	
		SharedPreferences settings = c.getSharedPreferences(prefName, 0);
        return new JSONArray(settings.getString(JSONSharedPreferences.PREFIX+key, "[]"));
    }

    public static void remove(Context c, String prefName, String key) {
    	if(c != null)
    	{
    		SharedPreferences settings = c.getSharedPreferences(prefName, 0);
            if (settings.contains(JSONSharedPreferences.PREFIX+key)) {
                SharedPreferences.Editor editor = settings.edit();
                editor.remove(JSONSharedPreferences.PREFIX+key);
                editor.commit();
            }
    	}
        
    }
    
    
}
