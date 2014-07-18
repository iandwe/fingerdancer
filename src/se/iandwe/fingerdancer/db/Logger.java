package se.iandwe.fingerdancer.db;

import android.util.Log;




public class Logger {

	// logtags
	public static final String LOG_STATES								= "logStates";
	public static final String GAME_DETAILS								= "gameDetails";
	
	//turn off if disable all logging
	private static final boolean loggingIsOn = true;
	
	public static void logThis(String logTag, String value)
	{
		if(loggingIsOn){
			Log.i(logTag,value);
		}
	}
}
