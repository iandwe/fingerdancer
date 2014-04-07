package se.iandwe.fingerdancer.gameobjects;
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


import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.view.View;

/**
 * A data structure that holds a Shape and various properties that can be used to define
 * how the shape is drawn.
 */
public class ShapeHolder {
    private float x = 0, y = 0;
    private ShapeDrawable shape;
    private int color;
    private RadialGradient gradient;
    private float alpha = 1f;
    private Paint paint;
	private int areaWidth;
	private int areaHeight;
	private float percentHeight = 100.0f;
	private float percentWidth = 100.0f;

    public void setPaint(Paint value) {
        paint = value;
    }
    public Paint getPaint() {
        return paint;
    }

    public void setX(float value) {
        x = value;
    }
    public float getX() {
        return x;
    }
    public void setY(float value) {
        y = value;
    }
    public float getY() {
        return y;
    }
    public void setShape(ShapeDrawable value) {
        shape = value;
    }
    public ShapeDrawable getShape() {
        return shape;
    }
    public int getColor() {
        return color;
    }
    public void setColor(int value) {
        shape.getPaint().setColor(value);
        color = value;
    }
    public void setGradient(RadialGradient value) {
        gradient = value;
    }
    public RadialGradient getGradient() {
        return gradient;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        shape.setAlpha((int)((alpha * 255f) + .5f));
    }

    public float getWidth() {
        return shape.getShape().getWidth();
    }
    public void setWidth(float width) {
        Shape s = shape.getShape();
        s.resize(width, s.getHeight());
    }

    public float getHeight() {
        return shape.getShape().getHeight();
    }
    
    public void setHeight(float height) {
        Shape s = shape.getShape();
        s.resize(s.getWidth(), height);
    }
    
    public void setAreaWidth(int pixels) {
    	this.areaWidth = pixels;
    	this.setPercentWidth(this.percentWidth);
    }
    public int getAreaWidth() {
    	return this.areaWidth;
    }
    
    public void setAreaHeight(int pixels) {
    	this.areaHeight = pixels;
    	this.setPercentHeight(this.percentHeight);   	
    }
    public int getAreaHeight() {
    	return this.areaHeight;
    }
    
    public void setPercentWidth(float percent) {
    	this.percentWidth = percent;
    	Shape s = shape.getShape();
        s.resize(this.areaWidth*(this.percentWidth/100.0f), s.getHeight());
    }
    
    public void setPercentHeight(float percent) {
    	this.percentHeight = percent;
    	Shape s = shape.getShape();
        s.resize(s.getWidth(), this.areaHeight*(this.percentHeight/100.0f));
    }
    public float getPercentHeight(){
    	return this.percentHeight;
    }
    public float getPercentWidth() {
    	return this.percentWidth;
    }
    

    public ShapeHolder(ShapeDrawable s) {
        shape = s;
    }
}
