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
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;



import se.iandwe.fingerdancer.interfaces.OnTouchButton;

    @SuppressLint("NewApi")
	public class TapJava extends View implements ValueAnimator.AnimatorUpdateListener, View.OnClickListener{

        public final ArrayList<ShapeHolder> shapes = new ArrayList<ShapeHolder>();
        
        private OnTouchButton listenerForClick;
        private boolean isCorrectAnswer = false;
        public int activeColor = 0xffFF8AF5;
        private int inactiveColor = 0xff50E3C2;
        private int activeShadow = 0xffAE5EB1;
        private int errorColor = 0xffff0000;
        private ShapeHolder front;
        private ShapeHolder shadow;
        private ShapeHolder circle;
        private int upY = 5;
        private int upX = 10;
        private int downY = 15;
        private int downX = 0;
		private int viewWidth  = 1;
		private int viewHeight = 1;

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
            this.front = addSquare(0, 25f, this.inactiveColor);
            this.circle = createCircle();
            setOnClickListener(this);
    	}
    	

        @SuppressLint("NewApi")
		private void downAnimation() {

        	AnimatorSet animation = null;
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(shapes.get(1), "y",
            		this.front.getY(), this.downY).setDuration(100);
            
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(shapes.get(1), "x",
            		this.front.getX(), this.downX).setDuration(100);
            
            ObjectAnimator ci = ObjectAnimator.ofFloat(this.circle, "width",
            		this.circle.getWidth(), 300).setDuration(100);
            ObjectAnimator ch = ObjectAnimator.ofFloat(this.circle, "height",
            		this.circle.getHeight(), 300).setDuration(100);
            
            anim1.addUpdateListener(this);
            anim2.addUpdateListener(this);
            ci.addUpdateListener(this);
            ch.addUpdateListener(this);
            animation = new AnimatorSet();
            animation.play(anim1).with(anim2).with(ci).with(ch);
            animation.start();
        }
        private void upAnimation() {
        	
        	AnimatorSet animation = null;
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(
            		this.front, 
            		"y",
            		this.front.getY(), 
            		this.upY).setDuration(100);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(
            		this.front, 
            		"x",
            		this.front.getX(), 
            		this.upX).setDuration(100);
            
            anim1.addUpdateListener(this);
            anim2.addUpdateListener(this);
            animation = new AnimatorSet();
            animation.play(anim1).with(anim2);
            animation.start();
            
        }

        private ShapeHolder addSquare(float x, float y, int color) {
        	float[] outerR = new float[] { 25, 25, 25, 25, 25, 25, 25, 25 };
        	RoundRectShape box = new RoundRectShape(outerR, null, null);
        	Log.i("THIS HEIGHT", Integer.toString(this.getHeight()));
        	box.resize(290, 290);
        	
            ShapeDrawable drawable = new ShapeDrawable(box);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            shapeHolder.setX(this.downX);
            shapeHolder.setY(this.downY);
            shapeHolder.setPercentHeight(90);
            shapeHolder.setPercentWidth(90);
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
        
        private ShapeHolder createCircle() {
        	Log.i("Tap", "CREATE CIRCLE: " + Integer.toString(this.viewWidth) + " " + Integer.toString(this.viewHeight));
        	OvalShape c = new OvalShape();
        	c.resize(50, 50);
            ShapeDrawable drawable = new ShapeDrawable(c);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            shapeHolder.setX(80);
            shapeHolder.setY(80);
            shapeHolder.setPercentHeight(10);
            shapeHolder.setPercentWidth(10);
            shapeHolder.setAlpha((float)0.5);
            Paint paint = drawable.getPaint(); //new Paint(Paint.ANTI_ALIAS_FLAG);
            
            int red = (int)(100 + Math.random() * 155);
            int green = (int)(100 + Math.random() * 155);
            int blue = (int)(100 + Math.random() * 155);
            paint.setColor(0xffffffff);
            shapeHolder.setPaint(paint);
            shapes.add(shapeHolder);
            return shapeHolder;
        }
        
        public void setOnTouchListener(OnTouchButton _listenerForClick){
    		listenerForClick = _listenerForClick;
    	}
        
        public void onLateTap(){
        	Log.i("Tap", "Late");
        }
        public void onErrorTap() {
        	Log.i("Tap", "Error");
        	this.front.getPaint().setColor(this.errorColor);
        	this.downAnimation();
        }
        public void onCorrectTap() {
        	Log.i("Tap", "Correct");
        	this.front.setColor(this.activeShadow);
        	this.downAnimation();
        	
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
        
	    protected void onLayout(boolean changed, int l, int t, int r, int b) {
	        super.onLayout(changed, l, t, r, b);
	        //here you have the size of the view and you can do stuff
	        for (int i = 0; i < this.shapes.size(); i++) {
	        	this.shapes.get(i).setAreaHeight(this.getHeight());
	        	this.shapes.get(i).setAreaWidth(this.getHeight());
	        }
	        invalidate();
        }

		@Override
		public void onClick(View v) {
			listenerForClick.onTouchButton(this.isCorrectAnswer, this);
		}
		

		
		public void onReset(boolean _isCorrectAnswer) {
			// TODO Auto-generated method stub
			
			if (_isCorrectAnswer) {
				this.front.setColor(this.activeColor);
				this.upAnimation();
			}
			else {
				this.front.setColor(this.inactiveColor);
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
