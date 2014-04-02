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
    	if(c != null)
    	{
    		SharedPreferences settings = c.getSharedPreferences(prefName, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(JSONSharedPreferences.PREFIX+key, object.toString());
            editor.commit();
    	}
    	else
    	{
    		Log.i("special","nope kunde inte spara josnobject för context varnull jsonsharedpref");
    	}
    }

    public static void saveJSONArray(Context c, String prefName, String key, JSONArray array)
    {
	    if(c != null)
	    {
	    	if(prefName != null)
	    	{
	    		Log.i("qnekt","innan SharedPreferences");
	    		SharedPreferences settings = c.getSharedPreferences(prefName, 0);
	    		Log.i("qnekt","innan settings");
	    		if(settings != null)
	    		{
	    			Log.i("qnekt","innan Editor");
	    			SharedPreferences.Editor editor = settings.edit();
	    			Log.i("qnekt","innan PREFIX");
	                editor.putString(JSONSharedPreferences.PREFIX+key, array.toString());
	                Log.i("qnekt","innan commit");
	                editor.commit();
	    		}
	    	}
	    	else
	    	{
	    		Log.i("qnekt","prefName = null");
	    	}
	    }
	    else
	    {
	    	Log.i("qnekt","context var null så hoppas inte kraschar numera");
	    }
    }

    public static JSONObject loadJSONObject(Context c, String prefName, String key) throws JSONException {
    	if(c == null)
    	{
    		return new JSONObject();
    	}
    	else
    	{
    		  SharedPreferences settings = c.getSharedPreferences(prefName, 0);
    	        return new JSONObject(settings.getString(JSONSharedPreferences.PREFIX+key, "{}"));
    	}
      
    }

    public static JSONArray loadJSONArray(Context c, String prefName, String key) throws JSONException {
    	if(c == null)
    	{
    		Log.i("special", "context null i loadjsonarray, returnar tom array");
    		return new JSONArray();
    	}
    	else
    	{
    		SharedPreferences settings = c.getSharedPreferences(prefName, 0);
            return new JSONArray(settings.getString(JSONSharedPreferences.PREFIX+key, "[]"));
    	}
        
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
    public static JSONArray sort(JSONArray array, Comparator c){
    	ArrayList<Object> asList = new ArrayList<Object>(array.length());
        for (int i=0; i<array.length(); i++){
       //   asList[i] = array.opt(i);
          asList.add(array.opt(i));
        		  //.add(array.opt(i));
        }
        
        Collections.sort(asList, c);
        Collections.reverse(asList);
        JSONArray  res = new JSONArray();
        for (Object o : asList){
          res.put(o);
        }
        return res;
    }
    
    public static JSONArray removeJsonObj(final int idx, final JSONArray from) {
    	
        final List<JSONObject> objs = asList(from);
        objs.remove(idx);

        final JSONArray ja = new JSONArray();
        for (final JSONObject obj : objs) {
            ja.put(obj);
        }
        return ja;
    }

    public static List<JSONObject> asList(final JSONArray ja) {
        final int len = ja.length();
        final ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
        for (int i = 0; i < len; i++) {
            final JSONObject obj = ja.optJSONObject(i);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }
}
