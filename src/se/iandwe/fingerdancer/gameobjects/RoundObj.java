package se.iandwe.fingerdancer.gameobjects;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class RoundObj implements Serializable {
	
	private static final long serialVersionUID = 1L;
	transient JSONObject roundObj;
	private String genericStringJson;
	private boolean allowedForStarMode = true;

	
	public RoundObj(JSONObject _roundObj) {
		this.roundObj = _roundObj;
		this.genericStringJson = _roundObj.toString();
	}
	
	public boolean getAllowedForStarmode()
	{
		return allowedForStarMode;
	}
	
	public void setAllowedForStarmode(boolean value)
	{
		allowedForStarMode = value;
	}
	
	public String getTitle()
	{
		return getValueForString("title");
	}
	
	public int getRoundTime()
	{
		return getValueForInt("roundTime");
	}
	
	public int getAmountRounds()
	{
		return getValueForInt("amountRounds");
	}
	public int getMusicLength()
	{
		return getValueForInt("musicLength");
	}
	public int getNumButtons()
	{
		return getValueForInt("numButtons");
	}
	
	
	
	private String getValueForString(String property)
	{
		if(roundObj == null)
		{
			try {
				roundObj = new JSONObject(genericStringJson);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			return roundObj.getString(property);
		} catch (JSONException e) {
			return "";
		}
	}
	
	private int getValueForInt(String property)
	{
		if(roundObj == null)
		{
			try {
				roundObj = new JSONObject(genericStringJson);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			return roundObj.getInt(property);
		} catch (JSONException e) {
			return 0;
		}
	}

}
