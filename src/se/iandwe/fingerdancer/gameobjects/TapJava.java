/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.iandwe.fingerdancer.gameobjects;


import android.animation.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;

import se.iandwe.fingerdancer.interfaces.OnResetSquare;
import se.iandwe.fingerdancer.interfaces.OnTouchButton;

    @SuppressLint("NewApi")
	public class TapJava extends View implements ValueAnimator.AnimatorUpdateListener, OnResetSquare, View.OnClickListener {

        public final ArrayList<ShapeHolder> shapes = new ArrayList<ShapeHolder>();
        
        private OnTouchButton listenerForClick;
        private boolean isCorrectAnswer = false;
        private int activeColor = 0xffFF8AF5;
        private int inactiveColor = 0xff50E3C2;
        private int activeShadow = 0xffAE5EB1;
        private ShapeHolder front;
        private ShapeHolder shadow;
        

        public TapJava(Context context) {
            super(context);
            this.init();
        }
        
        public TapJava(Context context, AttributeSet attrs) {
    		super(context, attrs);
    		this.init();
    		// TODO Auto-generated constructor stub
    	}

    	public TapJava(Context context, AttributeSet attrs, int defStyleAttr) {
    		super(context, attrs, defStyleAttr);
            this.init();
    		// TODO Auto-generated constructor stub
    	}
    	
    	private void init() {
    		this.shadow = addSquare(0, 25f, this.activeShadow);
            this.front = addSquare(0, 25f, this.activeColor);
            setOnClickListener(this);
    	}

        @SuppressLint("NewApi")
		private void downAnimation() {
        	AnimatorSet animation = null;
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(shapes.get(1), "y",
            		shapes.get(1).getY(), 5).setDuration(100);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(shapes.get(1), "x",
            		shapes.get(1).getX(), 5).setDuration(100);
            
            anim1.addUpdateListener(this);
            anim2.addUpdateListener(this);
            animation = new AnimatorSet();
            animation.play(anim1).with(anim2);
            animation.start();
        }
        private void upAnimation() {
        	AnimatorSet animation = null;
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(
            		this.front, 
            		"y",
            		5, 
            		17).setDuration(100);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(
            		this.front, 
            		"x",
            		5, 
            		17).setDuration(100);
            
            anim1.addUpdateListener(this);
            anim2.addUpdateListener(this);
            animation = new AnimatorSet();
            animation.play(anim1).with(anim2);
            animation.start();
            
        }

        private ShapeHolder addSquare(float x, float y, int color) {
        	float[] outerR = new float[] { 25, 25, 25, 25, 25, 25, 25, 25 };
        	RoundRectShape box = new RoundRectShape(outerR, null, null);
        	box.resize(310, 310);
            ShapeDrawable drawable = new ShapeDrawable(box);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            shapeHolder.setX(5);
            shapeHolder.setY(5);
            /*
            int red = (int)(100 + Math.random() * 155);
            int green = (int)(100 + Math.random() * 155);
            int blue = (int)(100 + Math.random() * 155);
            */
            //int color = 0xff000000 | red << 16 | green << 8 | blue;
            Paint paint = drawable.getPaint(); //new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(color);
            shapeHolder.setPaint(paint);
            shapes.add(shapeHolder);
            return shapeHolder;
        }
        
        private ShapeHolder createCircle(float x, float y, int color) {
        	OvalShape circle = new OvalShape();
        	
            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            shapeHolder.setX(15);
            shapeHolder.setY(15);
            Paint paint = drawable.getPaint(); //new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(color);
            shapeHolder.setPaint(paint);
            shapes.add(shapeHolder);
            return shapeHolder;
        }
        
        public void setOnTouchListener(OnTouchButton _listenerForClick){
    		listenerForClick = _listenerForClick;
    	}
        private void checkIfRightAnswer() {
    		if(!isCorrectAnswer) {
    			Log.i("special","feel");
    			listenerForClick.onTouchButton(false, this);
    			this.front.setColor(0xff000000);
    		}
    		else {
    			this.downAnimation();
    			this.front.setColor(this.activeShadow);
    			listenerForClick.onTouchButton(true, this);
    		}
    	}

        @Override
        protected void onDraw(Canvas canvas) {
            for (int i = 0; i < shapes.size(); ++i) {
                ShapeHolder shapeHolder = shapes.get(i);
                canvas.save();
                canvas.translate(shapeHolder.getX(), shapeHolder.getY());
                shapeHolder.getShape().draw(canvas);
                canvas.restore();
            }
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
        }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.i("tapjava", "clicked");
			this.checkIfRightAnswer();
		}

		@Override
		public void onReset(boolean _isCorrectAnswer) {
			// TODO Auto-generated method stub
			Log.i("Reset", Boolean.toString(_isCorrectAnswer));
			
			if (_isCorrectAnswer) {
				front.getPaint().setColor(this.activeColor);
				this.upAnimation();
			}
			else {
				front.getPaint().setColor(this.inactiveColor);
				this.downAnimation();
			}
			this.isCorrectAnswer = _isCorrectAnswer;
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

    }
