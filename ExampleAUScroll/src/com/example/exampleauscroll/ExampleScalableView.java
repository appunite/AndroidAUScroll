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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.appunite.scroll.ScrollableView;

public class ExampleScalableView extends ScrollableView {

	private int mWorksheetWidth = 2000;
	private int mWorksheetHeight = 2000;

	private Paint mPaint;

	private Drawable mPattern;

	public ExampleScalableView(Context context) {
		this(context, null, 0);
	}

	public ExampleScalableView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ExampleScalableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		Resources resources = context.getResources();
		mPattern = resources.getDrawable(R.drawable.pattern);

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
		canvas.restoreToCount(restoreDrawing);
	}

	protected int getWorksheetWidth() {
		return mWorksheetWidth;
	}

	protected int getWorksheetHeight() {
		return mWorksheetHeight;
	}

	@Override
	protected void onDraw(Canvas canvas, int left, int top, int right,
			int bottom) {
		mPattern.setBounds(Math.max(10, left), Math.max(10, top),
				Math.min(mWorksheetWidth - 11, right),
				Math.min(mWorksheetHeight - 11, bottom));
		mPattern.draw(canvas);
		canvas.drawRect(10, 10, mWorksheetWidth - 11, mWorksheetHeight - 11,
				mPaint);
	}

}
