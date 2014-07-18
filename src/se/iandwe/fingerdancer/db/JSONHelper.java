package se.iandwe.fingerdancer.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;


public class JSONHelper {
   
	

	 public static String jsonToStringFromAssetFolder(String fileName,Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(fileName);

        byte[] data = new byte[file.available()];
        file.read(data);
        file.close();
        return new String(data, "UTF-8");
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
