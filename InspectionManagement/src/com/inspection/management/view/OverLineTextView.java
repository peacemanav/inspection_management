package com.inspection.management.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.TextView;

public class OverLineTextView extends TextView {

	private int mLineColor = 0xffa3bbdc;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	public OverLineTextView(Context context) {
		super(context);
		
		init();
	}

	public OverLineTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		init();
	}


	public OverLineTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init();
	}


	private void init() {
		mPaint.setStyle(Style.FILL_AND_STROKE);
		mPaint.setStrokeWidth(4);
		mPaint.setColor(mLineColor);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawLine(0, 0, getMeasuredWidth(), 0, mPaint);
		canvas.drawLine(0, 0, 0, getMeasuredHeight(), mPaint);
		canvas.drawLine(getMeasuredWidth(), 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
		super.onDraw(canvas);
	}
}
