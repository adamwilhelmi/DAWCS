package com.cs.oswego.edu.dawcs;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

public class VerticalSlider extends SeekBar{
	public VerticalSlider(Context context) {
		super(context);
	}

	public VerticalSlider(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public VerticalSlider(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldh, oldw);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}

	protected void onDraw(Canvas c) {
		c.rotate(-90);
		c.translate(-getHeight(), 0);

		super.onDraw(c);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {
			return false;
		}
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			NonFocusingHorizontalScrollView.mScrollable = false;
//			System.out.println("Disabling scrolling...");
		case MotionEvent.ACTION_MOVE:
			setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
			onSizeChanged(getWidth(), getHeight(), 0, 0);
			break;
		case MotionEvent.ACTION_UP:
			setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
			onSizeChanged(getWidth(), getHeight(), 0, 0);
			NonFocusingHorizontalScrollView.mScrollable = true;
//			System.out.println("Re-enabling scrolling...");
			break;
		}

		return true;
	}
}
