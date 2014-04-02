package se.iandwe.fingerdancer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import se.iandwe.fingerdancer.db.JSONHelper;
import se.iandwe.fingerdancer.db.Settings;
import se.iandwe.fingerdancer.gameobjects.ButtonDefault;
import se.iandwe.fingerdancer.interfaces.OnResetSquare;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnTouchButton {

	private ArrayList<ButtonDefault> buttonArray;
	private ArrayList<OnResetSquare> resetSquareListeners;
	private ArrayList<Integer> randomIntArray = new ArrayList<Integer>();
	private boolean simultaneousPushIsOpen = false;
	private boolean receivedPushFromThisRound = false;
	private TextView pointStatusTotal;
	private TextView pointStatusForPush;
	private int totalPointsForRound = 0;
	private int pointsForRound = 0;
	private boolean madeError = false;
	private static SoundPool soundPool;
	//private static HashMap soundPoolMap;
	private static Map<Integer, Integer> soundPoolMap;
	public static final int S1 = R.raw.computererror;
	public static final int S2 = R.raw.robotblip;
	//public static final int S3 = R.raw.diplo_oboy;
	private int currentRound = 1;
	private int currentRoundGameboard = 0;
	private MediaPlayer mediaPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttonArray = new ArrayList<ButtonDefault>();
		resetSquareListeners = new ArrayList<OnResetSquare>();
		initSounds(getApplicationContext());
		pointStatusTotal = (TextView)findViewById(R.id.pointStatusTotal);
		pointStatusTotal.setText("0 points");
		pointStatusForPush = (TextView)findViewById(R.id.pointStatusForPush);
		
		Button goagain = (Button)findViewById(R.id.goagain);
		goagain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				restartGame();
			}
		});
		
		ButtonDefault btn1 = (ButtonDefault)findViewById(R.id.btn1);
		
		buttonArray.add(btn1);
		
		ButtonDefault btn2 = (ButtonDefault)findViewById(R.id.btn2);
		buttonArray.add(btn2);
		ButtonDefault btn3 = (ButtonDefault)findViewById(R.id.btn3);
		buttonArray.add(btn3);
		ButtonDefault btn4 = (ButtonDefault)findViewById(R.id.btn4);
		buttonArray.add(btn4);
		ButtonDefault btn5 = (ButtonDefault)findViewById(R.id.btn5);
		buttonArray.add(btn5);
		ButtonDefault btn6 = (ButtonDefault)findViewById(R.id.btn6);
		buttonArray.add(btn6);
		ButtonDefault btn7 = (ButtonDefault)findViewById(R.id.btn7);
		buttonArray.add(btn7);
		ButtonDefault btn8 = (ButtonDefault)findViewById(R.id.btn8);
		buttonArray.add(btn8);
		ButtonDefault btn9 = (ButtonDefault)findViewById(R.id.btn9);
		buttonArray.add(btn9);
		
		for(int i = 0; i < buttonArray.size(); i++)
		{
			randomIntArray.add(i);
			setResetSquareListener(buttonArray.get(i));
			buttonArray.get(i).setOnTouchListener(this);
		}
		new Handler().postDelayed(new Runnable() {
	        @Override
	        public void run() {
	        	reset();
	        	//playSound(getApplicationContext(), S3);
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
	//soundPoolMap.put( S3, soundPool.load(context, S3, 1) );
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
	
	private void setResetSquareListener(OnResetSquare _listener)
	{
		resetSquareListeners.add(_listener);
	}
	
	private void reset()
	{
		currentRoundGameboard += 1;
		emptyRoundPoints();
		receivedPushFromThisRound = false;
		int[] correctAnswers = getArrayWithRandomCorrectAnswers(3);
		for(int i = 0; i < resetSquareListeners.size(); i++)
		{
			boolean foundMatch = false;
			for(int j = 0; j < correctAnswers.length; j++)
			{
				if(correctAnswers[j] == i)
				{
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
		if(currentRoundGameboard == Settings.ROUND_ONE_SIZE)
		{
			
			showRoundInfoView("");
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
	public void onTouchButton(boolean correctAnswer, ButtonDefault pushedButton) {
		// TODO Auto-generated method stub
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
				Log.i("special","rŠtt hann i tid");
				pointsForRound += 1;
				pushedButton.animateBecauseOfCorrectPush();
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

	private void showRoundInfoView(String info)
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
		//TextView tv = (TextView)findViewById(R.id.roundFinishedInfo);
		//tv.setText("Congratulations! you finished round " + currentRound + "\n\nTotalpoints: " + totalPointsForRound);
	}
	

}
