package com.cs.oswego.edu.dawcs;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ChannelController extends LinearLayout{
	private Button close;
	private Dial pan;
	private Dial eq_high;
	private Dial eq_mid;
	private Dial eq_low;
	private TextView panLvl;
	private TextView gainLvl;
	private VerticalSlider gain;
	private Spinner chanNum;
	
	private final boolean show_eq;
	private final boolean show_pan;
	private final boolean show_gain;
	
	private int channelNum;
	

	public ChannelController(Context context, boolean show_eq, boolean show_pan, boolean show_gain) {
		super(context);
		this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
//		this.setBackgroundColor(getResources().getColor(R.color.Black));

		this.show_eq = show_eq;
		this.show_gain = show_gain;
		this.show_pan = show_pan;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.channel_layout, this);

		close = (Button)findViewById(R.id.x);
		
		if (show_eq) {
			eq_high = (Dial) findViewById(R.id.eq_high);
			eq_high.setImageResource(R.drawable.bnb);
			eq_mid = (Dial) findViewById(R.id.eq_mid);
			eq_mid.setImageResource(R.drawable.bnb);
			eq_low = (Dial) findViewById(R.id.eq_low);
			eq_low.setImageResource(R.drawable.bnb);
		}
		if (show_pan) {
			pan = (Dial) findViewById(R.id.pan);
			pan.setImageResource(R.drawable.bnb);
			panLvl = (TextView) findViewById(R.id.pan_lvl);
			pan.setDialListener(new Dial.DialListener() {
				@Override
				public void onDialChanged(float delta, float val) {
					System.out.println("Changing dial value...");
//					panLvl.setText("" + val);
					if (delta > 0)
						; // rotate right 
					else
						; // rotate left 
					double x = Math.floor((((val - 150)/150)*100));
					if(val<=150){
						panLvl.setText("L " + Math.abs(x));
					}else{
						panLvl.setText("R " + Math.abs(x));
					}
				}
			});
		}
		
		
		
		
		
		if (show_gain) {
			gain = (VerticalSlider) findViewById(R.id.gain);
			gain.setMax(Byte.MAX_VALUE);
			gain.setThumb(getResources().getDrawable(R.drawable.thumb1));
			gainLvl = (TextView) findViewById(R.id.gain_lvl);
			gain.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					gainLvl.setText(String.format("%.1f",((gain.getProgress()/Float.valueOf(gain.getMax()))*100)));
				}
			});
		}
		List<Integer> channels = new ArrayList<Integer>();
        for(int i=0;i<10;i++){
        	channels.add(i+1);
        }
        
        chanNum = (Spinner) findViewById(R.id.chan_num);
        
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this.getContext(), R.layout.spinner_item, channels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chanNum.setAdapter(dataAdapter);
        
        chanNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	channelNum = pos;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
	}
	
	public void addCloseListener(OnClickListener ocl){
		close.setOnClickListener(ocl);
	}
	
	public int getChannelNum(){
		return channelNum;
	}
	
	public void setChannelNum(int c){
		channelNum = c;
	}

}