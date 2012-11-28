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

package com.example.exampleauscroll;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.view.accessibility.AccessibilityEvent;

import com.appunite.scroll.ScrollableScalableView;

public class ExampleScrollableScalableView extends ScrollableScalableView {

	private static final int[] PRESSED_STATE = new int[] {android.R.attr.state_window_focused, android.R.attr.state_enabled, android.R.attr.state_pressed};

	private static final int[] NORMAL_STATE = new int[] {android.R.attr.state_window_focused, android.R.attr.state_enabled};
	
	private int mWorksheetWidth = 2000;
	private int mWorksheetHeight = 2000;

	private Paint mPaint;

	private Drawable mPattern;
	private Drawable mButtonDrawable;
	private Rect mButtonRect;

	private Runnable mClickRunnalbe;

	public ExampleScrollableScalableView(Context context) {
		this(context, null, 0);
	}

	public ExampleScrollableScalableView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ExampleScrollableScalableView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		Resources resources = context.getResources();
		mPattern = resources.getDrawable(R.drawable.pattern);
		mButtonDrawable = resources.getDrawable(R.drawable.btn_default_holo_dark);
		mButtonRect = new Rect(40, 40, 400, 200);
		mButtonDrawable.setBounds(mButtonRect);
		mButtonDrawable.setState(NORMAL_STATE);

		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(Color.RED);
	}

	protected void onDraw(Canvas canvas, int left, int top, int right,
			int bottom, float scaleFactorX, float scaleFactorY) {
		int restoreDrawing = canvas.save();
		int leftS = (int) (left / scaleFactorX);
		int topS = (int) (top / scaleFactorY);
		int rightS = (int) (right / scaleFactorX);
		int bottomS = (int) (bottom / scaleFactorY);
		canvas.scale(scaleFactorX, scaleFactorY);
		mPattern.setBounds(Math.max(10, leftS), Math.max(10, topS),
				Math.min(mWorksheetWidth - 11, rightS),
				Math.min(mWorksheetHeight - 11, bottomS));
		mPattern.draw(canvas);
		canvas.drawRect(10, 10, mWorksheetWidth - 11, mWorksheetHeight - 11,
				mPaint);
		mButtonDrawable.draw(canvas);
		canvas.restoreToCount(restoreDrawing);
	}
	
	@Override
	protected void onTouchCanceled(float x, float y) {
		mButtonDrawable.setState(NORMAL_STATE);
		invalidate();
	}
	
	@Override
	protected void onTouchClick(float x, float y) {
		mButtonDrawable.setState(NORMAL_STATE);
		invalidate();
		if (mClickRunnalbe == null) {
			mClickRunnalbe = new Runnable() {
				
				@Override
				public void run() {
					sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
					playSoundEffect(SoundEffectConstants.CLICK);
					new AlertDialog.Builder(getContext()).setTitle("krowa")
							.show();
				}
			};
		}
		if (!post(mClickRunnalbe)) {
			mClickRunnalbe.run();
		}
	}
	
	@Override
	protected boolean onTouchDown(float x, float y) {
		boolean touch = mButtonRect.contains((int)x, (int)y);
		if (touch) {
			mButtonDrawable.setState(PRESSED_STATE);
			invalidate();
		}
		return touch;
	}
	
	@Override
	protected boolean onTouchMove(float x, float y) {
		return mButtonRect.contains((int)x, (int)y);
	}

	protected int getWorksheetWidth() {
		return mWorksheetWidth;
	}

	protected int getWorksheetHeight() {
		return mWorksheetHeight;
	}

}
