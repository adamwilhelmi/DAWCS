package com.cs.oswego.edu.dawcs;

import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Based on:
 * http://go-lambda.blogspot.fr/2012/02/rotary-knob-widget-on-android.html
 * and:
 * https://gist.github.com/anonymous/4281823
 */
public class Dial extends ImageView {

	private final boolean CLICK = true;
	private final int CLICK_NUM = 15;
	
	private float angle = 0f;
	private float theta_old = 0f;
	private float angle_old=0f;
	private int width;
	private int height;
	private float val = 0;
	
	private DialListener listener;
	private Direction direction;
	
	private MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.click);

	public interface DialListener {
		public void onDialChanged(float delta, float angle);
	}

	public void setDialListener(DialListener l) {
		listener = l;
	}

	public Dial(Context context) {
		super(context);
	}

	public Dial(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public Dial(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w;
		height = h;
		initialize();
	}

	private float getTheta(float x, float y) {
		float sx = x - (width / 2.0f);
		float sy = y - (height / 2.0f);

		float length = (float) Math.sqrt(sx * sx + sy * sy);
		float nx = sx / length;
		float ny = sy / length;
		float theta = (float) Math.atan2(ny, nx);

		final float rad2deg = (float) (180.0 / Math.PI);
		float theta2 = theta * rad2deg;
		
		if(CLICK){
			float rounded = CLICK_NUM*(Math.round(theta2/CLICK_NUM));
			theta2 = rounded;
		}
		
		if((theta2 > 60)&&(theta2 <= 90)){
			theta2 = 60;
		}else if((theta2 >= 90)&&(theta2 < 120)){
			theta2 = 120;
		}
				
		return (theta2 < 0) ? theta2 + 360.0f : theta2;
	}

	public void initialize() {

//		this.setImageResource(R.drawable.dial1);
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				angle_old = angle;
				int action = event.getAction();
				int actionCode = action & MotionEvent.ACTION_MASK;
				if (actionCode == MotionEvent.ACTION_POINTER_DOWN) {
					float x = event.getX(0);
					float y = event.getY(0);
					theta_old = getTheta(x, y);
				} else if (actionCode == MotionEvent.ACTION_MOVE) {
					invalidate();

					float x = event.getX(0);
					float y = event.getY(0);

					float theta = getTheta(x, y);
					float delta_theta = theta - theta_old;
					theta_old = theta;

//					direction = (delta_theta > 0) ? 1 : -1;
					angle = theta - 270;

					if(angle_old != angle && CLICK){
//						System.out.println("Play sound now...");
						mediaPlayer.start();
					}
					if((((theta + 90) % 360) <= 360) && (((theta + 90) % 360) >= 210)){
						val = ((theta + 90) % 360)-210;
					}else{
						val = ((theta + 90) % 360)+150;
					}
					
					
					
//					notifyListener(delta_theta, (theta + 90) % 360);
					notifyListener(delta_theta, val);
				}
				return true;
			}
		});
	}

	private void notifyListener(float delta, float angle) {
		if (null != listener) {
			listener.onDialChanged(delta, angle);
		}
	}

	protected void onDraw(Canvas c) {
		c.rotate(angle, width / 2, height / 2);
		super.onDraw(c);
	}
	
	public float getVal(){
		return val;
	}
	private enum Direction{
		left,
		right;
	}
}
