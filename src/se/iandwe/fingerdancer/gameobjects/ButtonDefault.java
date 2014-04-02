package se.iandwe.fingerdancer.gameobjects;

import java.util.Random;

import se.iandwe.fingerdancer.R;
import se.iandwe.fingerdancer.db.Settings;
import se.iandwe.fingerdancer.interfaces.OnResetSquare;
import se.iandwe.fingerdancer.interfaces.OnTouchButton;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class ButtonDefault extends ImageButton implements OnResetSquare, View.OnClickListener {
	
	private boolean wasTouched = false;
	private TypedArray colors; 
	private Random random;
	private int currentColor;
	private String[] answers; 
	private boolean isCorrectAnswer = false;
	private OnTouchButton listenerForClick;
	private boolean shouldTransitionBackOnReset = false;
	//private int id;

	public ButtonDefault(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ButtonDefault(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		setOnClickListener(this);
		// TODO Auto-generated constructor stub
	}

	public ButtonDefault(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
    public void onClick(View v) {
        // Do something
		Log.i("special", "was clicked color: " + currentColor);
		checkIfRightAnswer();
    }
	
	public void setOnTouchListener(OnTouchButton _listenerForClick)
	{
		listenerForClick = _listenerForClick;
	}
	
	private void checkIfRightAnswer()
	{
		
		if(!isCorrectAnswer)
		{
			Log.i("special","feel");
			listenerForClick.onTouchButton(false, this);
			showErrorColor();
		}
		else
		{
			Log.i("special","correct");
			listenerForClick.onTouchButton(true, this);
			
		}
		
	}
	
	public void animateBecauseOfCorrectPush()
	{
		shouldTransitionBackOnReset = true;
		TransitionDrawable td = (TransitionDrawable) this.getBackground();
		td.startTransition(Settings.BUTTON_ANIMATIONTIME_WHEN_CORRECT_PUSH);
	}
	
	private void animateBack()
	{
		shouldTransitionBackOnReset = false;
		TransitionDrawable td = (TransitionDrawable) this.getBackground();
		td.reverseTransition(Settings.BUTTON_ANIMATIONTIME_WHEN_CORRECT_PUSH);
	}
	
	private void showErrorColor()
	{
		this.setBackgroundResource(R.drawable.error);
		
	}
	
	public void setColor(int color)
	{
		//TransitionDrawable td = (TransitionDrawable) this.getBackground();
		//Log.i("special","innan sätter color");
		this.setBackgroundResource(color);
		//Log.i("special","efter sätter color");
	}
	
	private void resetColor()
	{
		if(colors == null)
		{
			colors = getResources().obtainTypedArray(R.array.background_drawables);
		}
		if(random == null)
		{
			random = new Random();
		}
		int randomNumber = random.nextInt(colors.length() - 0) + 0;
		int color = colors.getResourceId(randomNumber, -1);
		currentColor = color;
		setColor(color);
		if(isCorrectAnswer)
		{
			setSelected(true);
		}
		else
		{
			setSelected(false);
		}
	}
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		if (getLayoutParams() != null && w != h) {
	        getLayoutParams().width = h;
	        setLayoutParams(getLayoutParams());
	    }
	}

	@Override
	public void onReset(boolean _isCorrectAnswer) {
		// TODO Auto-generated method stub
		if(shouldTransitionBackOnReset)
		{
			animateBack();
		}
		isCorrectAnswer = _isCorrectAnswer;
		resetColor();
	}
}
