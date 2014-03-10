package com.cs.oswego.edu.dawcs;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
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
	private VerticalSlider gain;
	private Spinner chanNum;
	
	private final boolean show_eq;
	private final boolean show_pan;
	private final boolean show_gain;
	
	private int channelNum;
	

	public ChannelController(Context context, boolean show_eq, boolean show_pan, boolean show_gain) {
		super(context);
		this.setLayoutParams(new LinearLayout.LayoutParams(100, LayoutParams.MATCH_PARENT));
		this.setBackgroundColor(getResources().getColor(R.color.Black));

		this.show_eq = show_eq;
		this.show_gain = show_gain;
		this.show_pan = show_pan;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.channel_layout, this);

		close = (Button)findViewById(R.id.x);
		
		eq_high = (Dial) findViewById(R.id.eq_high);
		eq_high.setImageResource(R.drawable.dial1);
		eq_mid = (Dial) findViewById(R.id.eq_mid);
		eq_mid.setImageResource(R.drawable.dial1);
		eq_low = (Dial) findViewById(R.id.eq_low);
		eq_low.setImageResource(R.drawable.dial1);

		pan = (Dial) findViewById(R.id.pan);
		pan.setImageResource(R.drawable.dial1);
		
		panLvl = (TextView) findViewById(R.id.pan_lvl);
		
		gain = (VerticalSlider) findViewById(R.id.gain);
		
		pan.setDialListener(new Dial.DialListener() {
			@Override
			public void onDialChanged(float delta, float val) {
//				panLvl.setText("" + val);
				if (delta > 0)
					; // rotate right 
				else
					; // rotate left 
				
				if(val<=150){
					double x = Math.floor((((val - 150)/150)*100));
					panLvl.setText("-" + Math.abs(x));
				}else{
					double x = Math.floor((((val - 150)/150)*100));
					panLvl.setText("+" + Math.abs(x));
				}
			}	
		});
		
		gain.setThumb(getResources().getDrawable(R.drawable.thumb1));

		gain.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}
		});
		
        
        List<String> channels = new ArrayList<String>();
        for(int i=0;i<10;i++){
        	String c = "Chan# ";
        	channels.add(c + (i+1));
        }
        
        chanNum = (Spinner) findViewById(R.id.chan_num);
        
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_item, channels);
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