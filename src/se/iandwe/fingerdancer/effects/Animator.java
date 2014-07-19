package se.iandwe.fingerdancer.effects;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import android.view.View;
import android.view.animation.TranslateAnimation;

public class Animator {

	public Animator() {
		// TODO Auto-generated constructor stub
	}
	
	// To animate view slide out from left to right
	public void slideToRight(View view){
	TranslateAnimation animate = new TranslateAnimation(0,view.getWidth(),0,0);
	animate.setDuration(500);
	//animate.setFillAfter(true);
	view.startAnimation(animate);
	view.setVisibility(View.GONE);
	}
	// To animate view slide out from right to left
	public void slideToLeft(View view){
	TranslateAnimation animate = new TranslateAnimation(0,-view.getWidth(),0,0);
	animate.setDuration(500);
	//animate.setFillAfter(true);
	view.startAnimation(animate);
	view.setVisibility(View.GONE);
	}

	// To animate view slide out from top to bottom
	public void slideToBottom(View view){
	ViewHelper.setTranslationY(view, 0);
	ObjectAnimator.ofFloat(view, "translationY", view.getHeight()).start();
	}

	// To animate view slide out from bottom to top
	public void slideToTop(View view){
		ViewHelper.setTranslationY(view, view.getHeight());
		ObjectAnimator.ofFloat(view, "translationY", 0).start();
		view.setVisibility(View.VISIBLE);
	}

}
