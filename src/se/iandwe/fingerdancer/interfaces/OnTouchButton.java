package se.iandwe.fingerdancer.interfaces;

import se.iandwe.fingerdancer.gameobjects.TapJava;


public interface OnTouchButton {
	void onTouchButton(boolean correctAnswer, TapJava btnPushed);
}

