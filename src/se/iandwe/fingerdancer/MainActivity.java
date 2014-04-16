package se.iandwe.fingerdancer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;

import se.iandwe.fingerdancer.db.JSONHelper;
import se.iandwe.fingerdancer.db.JSONSharedPreferences;
import se.iandwe.fingerdancer.db.Settings;
import se.iandwe.fingerdancer.gameobjects.RoundObj;
import se.iandwe.fingerdancer.gameobjects.TapJava;
import se.iandwe.fingerdancer.interfaces.OnTouchButton;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnTouchButton {

	private ArrayList<TapJava> buttonArray;
	private ArrayList<TapJava> resetSquareListeners;
	private ArrayList<Integer> randomIntArray = new ArrayList<Integer>();
	private boolean simultaneousPushIsOpen = false;
	private boolean receivedPushFromThisRound = false;
	private TextView pointStatusTotal;
	private TextView pointStatusForPush;
	private int totalPointsForRound = 0;
	private int pointsForRound = 0;
	private boolean madeError = false;
	private static SoundPool soundPool;
	private static Map<Integer, Integer> soundPoolMap;
	public static final int S1 = R.raw.computererror;
	public static final int S2 = R.raw.robotblip;
	private int currentRoundGameboard = 0;
	private ArrayList<View> touchables;
	private ArrayList<RoundObj> roundObjects;
	private MediaPlayer mediaPlayer;
	/*private int[] activeColors = {
			0xffFF8AF5,
			0xff83C5F4
	};
	private int[] inactiveColors = {
			0xff50E3C2, 
			0xff7ED321
	};*/

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fetchRoundObjects();
		buttonArray = new ArrayList<TapJava>();
		resetSquareListeners = new ArrayList<TapJava>();
		initSounds(getApplicationContext());
		pointStatusTotal = (TextView)findViewById(R.id.pointStatusTotal);
		pointStatusTotal.setText("0 points");
		pointStatusForPush = (TextView)findViewById(R.id.pointStatusForPush);
		
		Button goagain = (Button)findViewById(R.id.goagain);
		goagain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				restartGame();
			}
		});
		LinearLayout gamebox = (LinearLayout)findViewById(R.id.gameBox);
		touchables = gamebox.getTouchables();
		int count = 0;
		for(View touchable : touchables) {
		    if(touchable instanceof TapJava) {
		    	randomIntArray.add(count);
		    	TapJava currentButton = (TapJava)touchable;
		    	buttonArray.add(currentButton);
		    	setResetSquareListener(currentButton);
		    	currentButton.setOnTouchListener(this);
		    	count++;
		    }
		}
		new Handler().postDelayed(new Runnable() {
	        @Override
	        public void run() {
	        	reset();
	        	playBackgroundMusic();
	        }
	    }, Settings.DELAY_BEFORE_STARTING_GAME);
	}
	
	/** Populate the SoundPool*/
	public static void initSounds(Context context) {
		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap(2);
		soundPoolMap.put( S1, soundPool.load(context, S1, 1) );
		soundPoolMap.put( S2, soundPool.load(context, S2, 2) );
	}
	
	/** Play a given sound in the soundPool */
	 public static void playSound(Context context, int soundID) {
		if(soundPool == null || soundPoolMap == null){
		   initSounds(context);
		}
	    float volume = 1;// whatever in the range = 0.0 to 1.0

	    // play sound with same right and left volume, with a priority of 1, 
	    // zero repeats (i.e play once), and a playback rate of 1f
	    soundPool.play(soundPoolMap.get(soundID), volume, volume, 1, 0, 1f);
	 }
	 
	 private void fetchRoundObjects()
	 {
		 //hämtar json data om levels settings från lokal jsonfil, skapar array med roundobjects.
		 
		 String jsondata = null;
	    	try {
				jsondata = JSONHelper.jsonToStringFromAssetFolder("roundsettings.json", this);
				try {
					JSONArray fetchedGamedata = new JSONArray(jsondata);
					roundObjects = new ArrayList<RoundObj>();
					for(int i = 0; i < fetchedGamedata.length(); i++)
					{
						RoundObj ro = new RoundObj(fetchedGamedata.getJSONObject(i));
						roundObjects.add(ro);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					//critical, try create again or quit app with error message?
				}
			} catch (IOException e) {
				e.printStackTrace();
				//critical, try fetch again or quit app with error message?
			}
	    	
	    	
	    	
	 }
	 
	 private void playBackgroundMusic()
	 {
		 mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.shortbeat);		
		 mediaPlayer.start();
	 }
	 
	 @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		 Log.i("special","onpause called");
		super.onPause();
	}
	
	 @Override
	protected void onStop() {
		// TODO Auto-generated method stub
		 Log.i("special","onstop called");
		super.onStop();
	}
	 
	 @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		 Log.i("special","onresume called");
		super.onResume();
	}
	 
	private int[] getArrayWithRandomCorrectAnswers(int amountOfCorrectAnswersNeeded)
    {
		int[] arrayToBeReturned = new int[(amountOfCorrectAnswersNeeded)];
		Collections.shuffle(randomIntArray);
		for(int i = 0; i < amountOfCorrectAnswersNeeded; i++)
		{
			arrayToBeReturned[i] = randomIntArray.get(i);
		}
		return arrayToBeReturned;
    }
	
	private void setResetSquareListener(TapJava _listener)
	{
		resetSquareListeners.add(_listener);
	}
	
	private void reset()
	{
		currentRoundGameboard += 1;
		emptyRoundPoints();
		receivedPushFromThisRound = false;
		Random r = new Random();
		int amountOfAnswers = currentRoundGameboard > 3 ? r.nextInt(5 - 2) + 2 : currentRoundGameboard;
		Log.i("AMOUNT OF ANSWERS", Integer.toString(amountOfAnswers));
		int[] correctAnswers = getArrayWithRandomCorrectAnswers(amountOfAnswers);
		for(int i = 0; i < resetSquareListeners.size(); i++){
			boolean foundMatch = false;
			for(int j = 0; j < correctAnswers.length; j++) {
				//int colorIndex = r.nextInt(currentRoundGameboard > 3 ? 4 : 1 - 1) + 1;
				if(correctAnswers[j] == i) {
					foundMatch = true;
					resetSquareListeners.get(i).onReset(true);
					break;
				}
			}
			if(!foundMatch)
			{
				resetSquareListeners.get(i).onReset(false);
			}	
		}
		madeError = false;
		if(currentRoundGameboard == Settings.LEVEL_ONE_SIZE)
		{
			showResultForFinishedLevel("");
		}
		else
		{
			startRound();
		}
		
	}
	
	private void restartGame()
	{
		hideInfoView();
		currentRoundGameboard = 0;
		totalPointsForRound = 0;
		setPointsTotal();
		playBackgroundMusic();
		startRound();
	}

	@Override
	public void onTouchButton(boolean correctAnswer, TapJava pushedButton) {
		if(!receivedPushFromThisRound)
		{
			simultaneousPushIsOpen = true;
			startSimultaneousPushBlocker();
		}
		receivedPushFromThisRound = true;
		
		if(simultaneousPushIsOpen)
		{
			if(correctAnswer && !madeError)
			{
				Log.i("special","rätt hann i tid");
				pointsForRound += 1;
				
				playSound(getApplicationContext(),S1);
			}
			else
			{
				madeError = true;
				Log.i("special","feeel hann i tid");
				pointsForRound = 0;
				playSound(getApplicationContext(),S2);
			}
		}
		else
		{
			Log.i("special","fick push men hann inte");
		}
	}
	
	private void setPointsTotal()
	{
		pointStatusTotal.setText(Integer.toString(totalPointsForRound));
	}
	
	private void setPointsForRound(int value)
	{
		totalPointsForRound += value;
		pointStatusForPush.setText(Integer.toString(value));
	}
	
	private void emptyRoundPoints()
	{
		pointStatusForPush.setText("");
	}
	
	private void startSimultaneousPushBlocker()
	{
		new Handler().postDelayed(new Runnable() {
	        @Override
	        public void run() {
	        	simultaneousPushIsOpen = false;
	        	setPointsForRound(pointsForRound);
	        	pointsForRound = 0;
	        }
	    }, Settings.SIMULTANEOUS_PUSH_FORGIVENESSTIME);
	}
	
	private void startRound()
	{
		new Handler().postDelayed(new Runnable() {
	        @Override
	        public void run() {
	        	setPointsTotal();
	        	reset();
	        }
	    }, Settings.ROUND_TIME);
	}

	private void showResultForFinishedLevel(String info)
	{
		RelativeLayout back = (RelativeLayout)findViewById(R.id.roundFinishedView);
		back.setVisibility(View.VISIBLE);
		TextView tv = (TextView)findViewById(R.id.roundFinishedInfo);
		int oldrec = JSONHelper.getCurrentRecord(getApplicationContext());
		if(JSONHelper.checkIfNewRecord(getApplicationContext(), totalPointsForRound))
		{
			tv.setText("Congratulations to your new highscore!\n\nOld record: " + oldrec +"\n\nNew record: " + totalPointsForRound);
		}
		else{
			tv.setText("The round is finished MF! And you didnt beat the highscore with your puny " + totalPointsForRound + "\n\nOld record: " + oldrec);
		}
	}
	
	private void hideInfoView()
	{
		RelativeLayout back = (RelativeLayout)findViewById(R.id.roundFinishedView);
		back.setVisibility(View.INVISIBLE);
	}
	

}
