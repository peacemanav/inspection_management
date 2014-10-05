package com.inspection.management.view;

import com.inspection.management.R;
import com.inspection.management.R.color;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class MultiFiniteProgressView extends View {

	private float mAccepted;
	private float mRejected;
	private float mTentative;

	private Paint mPaint;
	private static final int RECT_WIDTH = 20;

	private int mBarGreen;
	private int mBarYello;
	private int mBarRed;

	public MultiFiniteProgressView(Context context) {
		super(context);
		init();
	}

	public MultiFiniteProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MultiFiniteProgressView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Style.FILL);
	}

	public void setIndicatorValues(int accepted, int rejected, int tentative) {
		mAccepted = accepted;
		mRejected = rejected;
		mTentative = tentative;

		mBarGreen = getContext().getResources().getColor(
				R.color.bar_green_color);
		mBarYello = getContext().getResources().getColor(
				R.color.bar_yello_color);
		mBarRed = getContext().getResources().getColor(R.color.bar_red_color);

		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// get largest number
		float largest = getLargestOfThree();

		// decide scale on this.
		float scale = getWidth() / largest;

		int top = (getHeight()-(RECT_WIDTH*3))/2;
		
		// draw accepted
		float acceptedWidth = mAccepted * scale;
		mPaint.setColor(mBarGreen);
		canvas.drawRect(getWidth() - acceptedWidth, top, getWidth(), top
				+ RECT_WIDTH, mPaint);

		// increment top
		top += RECT_WIDTH;

		// draw rejected
		float rejectedWidth = mRejected * scale;
		mPaint.setColor(mBarYello);
		canvas.drawRect(getWidth() - rejectedWidth, top, getWidth(), top
				+ RECT_WIDTH, mPaint);

		// increment top
		top += RECT_WIDTH;

		// draw rejected
		float tentativeWidth = mTentative * scale;
		mPaint.setColor(mBarRed);
		canvas.drawRect(getWidth() - tentativeWidth, top, getWidth(), top
				+ RECT_WIDTH, mPaint);
	}

	private float getLargestOfThree() {

		float largest = Math.max(mAccepted, mRejected);
		largest = Math.max(largest, mTentative);

		return largest;
	}
}
