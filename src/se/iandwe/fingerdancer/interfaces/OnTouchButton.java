package se.iandwe.fingerdancer.interfaces;

import se.iandwe.fingerdancer.gameobjects.ButtonDefault;


public interface OnTouchButton {
	void onTouchButton(boolean correctAnswer, ButtonDefault btnPushed);
}

