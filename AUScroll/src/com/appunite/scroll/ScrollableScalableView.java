/*
 * Copyright (C) 2012 Jacek Marchwicki <jacek.marchwicki@gmail.com>
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

package com.appunite.scroll;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;

public abstract class ScrollableScalableView extends ScalableView implements OnScaleGestureListener {

	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactorX = 1.0f;
	private float mScaleFactorY = 1.0f;
	private float mMinScaleFactorX = 0.1f;
	private float mMinSacleFactorY = 0.1f;
	private float mMaxScaleFactorX = 5.0f;
	private float mMaxScaleFactorY = 5.0f;

	public ScrollableScalableView(Context context) {
		this(context, null, 0);
	}

	public ScrollableScalableView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScrollableScalableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mScaleDetector = new ScaleGestureDetector(context, this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mScaleDetector.onTouchEvent(event);

		final int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			if (mScaleDetector.isInProgress()) {
				return true;
			}
			break;
		}
		}

		return super.onTouchEvent(event);
	}


	@Override
	protected int computeHorizontalScrollRange() {
		return (int) (super.computeHorizontalScrollRange()*mScaleFactorX);
	}
	@Override
	protected int computeVerticalScrollRange() {
		return (int) (super.computeVerticalScrollRange() * mScaleFactorY);
	}

	protected abstract void onDraw(Canvas canvas, int left, int top, int right, int bottom, float scaleFactorX, float scaleFactorY);
	
	protected final void onDraw(Canvas canvas, int left, int top, int right, int bottom) {
		onDraw(canvas, left, top, right, bottom, mScaleFactorX, mScaleFactorY);
	}
	
	public void setMinScaleFactor(float minScaleFactor) {
		mMinScaleFactorX = minScaleFactor;
		mMinSacleFactorY = minScaleFactor;
		validateScaleFactors();
		invalidate();
	}
	
	public void setScaleFactor(float scaleFactor) {
		mScaleFactorX = scaleFactor;
		mScaleFactorY = scaleFactor;
		validateScaleFactors();
		invalidate();
	}
	
	public void setMaxScaleFactor(float maxScaleFactor) {
		mMaxScaleFactorX = maxScaleFactor;
		mMaxScaleFactorY = maxScaleFactor;
		validateScaleFactors();
		invalidate();
	}
	
	private void validateScaleFactors() {
		if (mScaleFactorX > mMaxScaleFactorX) {
			mScaleFactorX = mMaxScaleFactorX;
		}
		if (mScaleFactorY > mMaxScaleFactorY) {
			mScaleFactorY = mMaxScaleFactorY;
		}
		if (mScaleFactorX < mMinScaleFactorX) {
			mScaleFactorX = mMinScaleFactorX;
		}
		if (mScaleFactorY < mMinSacleFactorY) {
			mScaleFactorY = mMinSacleFactorY;
		}
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		mScaleFactorX *= detector.getScaleFactor();
		mScaleFactorY *= detector.getScaleFactor();
		validateScaleFactors();
		ViewCompat.postInvalidateOnAnimation(this);
		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
	}
}
