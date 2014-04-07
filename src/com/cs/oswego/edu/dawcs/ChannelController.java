<<<<<<< HEAD
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

					panLvl.setText("" + val);

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

					NetHandler.getInstance().receivePacketFromChannel(new MIDIPacket(channelNum, 0xa, (int)((val / 300.0)*127)));


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
					NetHandler.getInstance().receivePacketFromChannel(new MIDIPacket(channelNum, 0x7, (int)(progress/((double)(seekBar.getMax()))*127)));

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
=======
package com.cs.oswego.edu.dawcs;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ChannelController extends LinearLayout {
	private Button close;
	private Dial pan;
	private Dial eq_high;
	private Dial eq_mid;
	private Dial eq_low;
	private TextView panLvl;
	private TextView gainLvl;
	private VerticalSlider gain;
	private Spinner chanNumSpinner;
	
	private final boolean show_eq;
	private final boolean show_pan;
	private final boolean show_gain;
	
	private int channelNum;
	
	private Channel chan;
	private Channels chans = DAWCS.chans;
	
	private HashMap<Integer, Group> group = DAWCS.groupsMap;
	
	private RadioGroup radioGroup;
	private RadioButton groupOne;
	private RadioButton groupTwo;
	private RadioButton groupThree;
	private RadioButton groupFour;
	
	public ChannelController(Context context, boolean show_eq, boolean show_pan, boolean show_gain, Channel chan) {
		super(context);
		this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
//		this.setBackgroundColor(getResources().getColor(R.color.Black));

		this.show_eq = show_eq;
		this.show_gain = show_gain;
		this.show_pan = show_pan;
		
		this.chan = chan;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.channel_layout, this);

		close = (Button)findViewById(R.id.x);
		
		List<String> chanStr = new ArrayList<String>();
        String c = "Chan# ";
	    chanStr.add(0, c + chan.getChanID());
	    
        /*for (int i = 0; i < chans.maxChannels(); i++) {
        	if (i == 0) {
        		chanStr.add(0, c + chan.getChanID());
        	} else if ((i + 1) == chan.getChanID()) {
        		continue;
        	} else {
        		chanStr.add(c + (i + 1));
        	}
        }*/
            
        chanNumSpinner = (Spinner) findViewById(R.id.chan_num);
        
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_item, chanStr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chanNumSpinner.setAdapter(dataAdapter);
        
        if (!DAWCS.availableChans.isEmpty()) {
        	for (Channel available : DAWCS.availableChans) {
        		chanStr.add(c + available.getChanID());
        	}
        	dataAdapter.notifyDataSetChanged();
        }
        
        chanNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	chans.getChan(pos);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
		if (show_eq) {
			eq_high = (Dial) findViewById(R.id.eq_high);
			eq_high.setImageResource(R.drawable.dial1);
			eq_mid = (Dial) findViewById(R.id.eq_mid);
			eq_mid.setImageResource(R.drawable.dial1);
			eq_low = (Dial) findViewById(R.id.eq_low);
			eq_low.setImageResource(R.drawable.dial1);
		}
		if (show_pan) {
			pan = (Dial) findViewById(R.id.pan);
			pan.setImageResource(R.drawable.dial1);
			panLvl = (TextView) findViewById(R.id.pan_lvl);
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
						panLvl.setText("L " + Math.abs(x));
					}else{
						double x = Math.floor((((val - 150)/150)*100));
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
					gainLvl.setText(String.format("%.1f",((gain.getProgress()/new Float(gain.getMax()))*100)));
					double fade = (double)(gain.getProgress()/new Float(gain.getMax()));
				}
			});
		}
		
		radioGroup = (RadioGroup) findViewById(R.id.radioGroups);
		groupOne = (RadioButton) findViewById(R.id.group1);
		groupTwo = (RadioButton) findViewById(R.id.group2);
		groupThree = (RadioButton) findViewById(R.id.group3);
		groupFour = (RadioButton) findViewById(R.id.group4);
		addRadioGroupListener();
	}
	
	private void addRadioGroupListener() {		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radGrp, int groupId) {
				switch (groupId){
					case R.id.nogroup:
						System.out.println(chan.getChanID() + " removed from group " + chan.getGroup());
						group.get(chan.getGroup()).remove(chan);
						chan.setGrouped(false);
						break;
					case R.id.group1:
						makeGroup(1);
						break;
					case R.id.group2:
						makeGroup(2);
						break;
					case R.id.group3:
						makeGroup(3);
						break;
					case R.id.group4:
						makeGroup(4);
						break;
				}				
			}
		});
	}

	protected void makeGroup(int i) {
		if (chan.isGrouped()) {
			group.get(chan.getGroup()).remove(chan);
			chan.setGrouped(false);
		}
		group.get(i).add(chan);
		chan.setGrouped(true);
		chan.setGroup(i);
		System.out.println(chan.getChanID() + " added to group " + chan.getGroup());
	}

	public void addCloseListener(OnClickListener ocl){
		close.setOnClickListener(ocl);
	}
	
	public int getChannelNum(){
		return chan.getChanID();
	}
	
	/*public void setChannelNum(int c){
		channelNum = c;
	}*/
}
>>>>>>> 3d1cb30cb35f9140162ba921bedbd1e9254eaaf2
