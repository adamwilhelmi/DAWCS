
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
	private int currentProgress;
	
	private Channel chan;
	private Channels chans = DAWCS.chans;
	
	private HashMap<Integer, Group> group = DAWCS.groupsMap;
	private GroupListener gl;
	private GroupListener masterListener;
	
	private RadioGroup radioGroup;
	private RadioButton groupOne;
	private RadioButton groupTwo;
	private RadioButton groupThree;
	private RadioButton groupFour;
	
	public int masterProg;
	public Channel masterChan;
	
	public ChannelController(Context context, boolean show_eq, boolean show_pan, boolean show_gain, Channel chanIn) {
		super(context);
		this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
//		this.setBackgroundColor(getResources().getColor(R.color.Black));

		this.show_eq = show_eq;
		this.show_gain = show_gain;
		this.show_pan = show_pan;
		
		this.chan = chanIn;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.channel_layout, this);

		close = (Button)findViewById(R.id.x);
		
		List<Integer> avalChans = new ArrayList<Integer>();
	    avalChans.add(0, chan.getChanID());
	    
        /*for (int i = 0; i < chans.maxChannels(); i++) {
        	if (i == 0) {
        		chanStr.add(0, c + chan.getChanID());
        	} else if ((i + 1) == chan.getChanID()) {
        		continue;
        	} else {
        		chanStr.add(c + (i + 1));
        	}
        }*/
	    
	    if (!DAWCS.availableChans.isEmpty()) {
        	for (Channel available : DAWCS.availableChans) {
        		avalChans.add(available.getChanID());
        	}
        }
            
        chanNumSpinner = (Spinner) findViewById(R.id.chan_num);
        
        final ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this.getContext(), R.layout.spinner_item, avalChans);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chanNumSpinner.setAdapter(dataAdapter);
        
        chanNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	int newChan = Integer.parseInt(parent.getItemAtPosition(pos).toString());            	
            	
            	if (id > 0) {
            		DAWCS.availableChans.add(chan);
            		DAWCS.availableChans.remove(pos);
            		
            		/*if (!DAWCS.availableChans.isEmpty()) {
            			avalChans.clear();
                    	for (Channel available : DAWCS.availableChans) {
                    		avalChans.add(available.getChanID());
                    	}
                    }*/
            		
            		dataAdapter.notifyDataSetChanged();
            		System.out.println("Old Channel: " + chan.getChanID());
            	}            	
   
            	//dataAdapter.notifyDataSetChanged();
            	chan = chans.getChan(newChan);
            	
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
		if (show_eq) {
			eq_high = (Dial) findViewById(R.id.eq_high);
			eq_high.setImageResource(R.drawable.bnb_knob);
			eq_mid = (Dial) findViewById(R.id.eq_mid);
			eq_mid.setImageResource(R.drawable.bnb_knob);
			eq_low = (Dial) findViewById(R.id.eq_low);
			eq_low.setImageResource(R.drawable.bnb_knob);
		}
		if (show_pan) {
			pan = (Dial) findViewById(R.id.pan);
			pan.setImageResource(R.drawable.bnb_knob);
			panLvl = (TextView) findViewById(R.id.pan_lvl);
			pan.setDialListener(new Dial.DialListener() {
				@Override
				public void onDialChanged(float delta, float val) {

					if(val<=150){
						//rotating left
						double x = Math.ceil((((val - 150)/150)*100));
						panLvl.setText("L " + Math.abs(x));
					}else{
						//rotating right
						double x = Math.floor((((val - 150)/150)*100));
						panLvl.setText("R " + Math.abs(x));
					}
				}	
			});
		}
		
		if (show_gain) {
			gain = (VerticalSlider) findViewById(R.id.gain);
			gain.setMax(Byte.MAX_VALUE);
			gain.setThumb(getResources().getDrawable(R.drawable.bnb_thumb));
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
					if (chan.isGrouped()) {
						gl = new GroupListener();
						gl.registerListener(group.get(chan.getGroup()));
						
						
						gain.setOnSeekBarChangeListener(gl);
						
						masterChan = group.get(chan.getGroup()).getMasterFader();
						
						//System.out.println("I'm your master..." + masterChan.getChanID());
						
						/*for (Channel c : group.get(chan.getGroup()).values()) {
							System.out.println(c.getChanID());
						}*/
						
						//System.out.println("Grouped");
						for (Channel c : group.get(chan.getGroup()).values()) {
							gl.addChangeListener(new OnSeekBarChangeListener() {
	
								@Override
								public void onProgressChanged(SeekBar seekBar,
										int progress, boolean fromUser) {
									if (chan == masterChan) {
										gainLvl.setText(String.format("%.1f",((gain.getProgress()/new Float(gain.getMax()))*100)));
										double fade = (double)(gain.getProgress()/new Float(gain.getMax()));
										chan.setFade(fade);
										for (ChannelController cc : group.get(chan.getGroup()).keys()) {
											if (cc.getChannelNum() == masterChan.getChanID()) {
												continue;
											}
											cc.gain.setProgress(gain.getProgress() - cc.currentProgress);
											cc.gain.updateThumb();
											cc.gainLvl.setText(String.format("%.1f",((cc.gain.getProgress()/new Float(cc.gain.getMax()))*100)));
											fade = (double)(cc.gain.getProgress()/new Float(cc.gain.getMax()));
										}
									} /*else {
										//gainLvl.setText(String.format("%.1f",((gain.getProgress()/new Float(gain.getMax()))*100)));
										//double fade = (double)(gain.getProgress()/new Float(gain.getMax()));
										//chan.setFade(fade);
										for (ChannelController cc : group.get(chan.getGroup()).keys()) {
											if (cc.getChannelNum() == chan.getChanID()) {
												continue;
											}
											if (cc.currentProgress < gain.getProgress()) {
												cc.gain.setProgress(gain.getProgress() - cc.currentProgress);
												cc.gainLvl.setText(String.format("%.1f",((cc.gain.getProgress()/new Float(cc.gain.getMax()))*100)));
												//fade = (double)(cc.gain.getProgress()/new Float(cc.gain.getMax()));
											} else {
												cc.gain.setProgress(cc.currentProgress - gain.getProgress());
												cc.gainLvl.setText(String.format("%.1f",((cc.gain.getProgress()/new Float(cc.gain.getMax()))*100)));
												//fade = (double)(cc.gain.getProgress()/new Float(cc.gain.getMax()));
											}
											
										}
									}*/
								}
	
								@Override
								public void onStartTrackingTouch(SeekBar seekBar) {						
								}
	
								@Override
								public void onStopTrackingTouch(SeekBar seekBar) {							
								}
								
							});
						}
					} else {
						//System.out.println("Not Grouped");
						gainLvl.setText(String.format("%.1f",((gain.getProgress()/new Float(gain.getMax()))*100)));
						double fade = (double)(gain.getProgress()/new Float(gain.getMax()));
						currentProgress = gain.getProgress();
						chan.setFade(fade);
					}
				}
			});
		}
		
		radioGroup = (RadioGroup) findViewById(R.id.radioGroups);
		groupOne = (RadioButton) findViewById(R.id.group1);
		groupTwo = (RadioButton) findViewById(R.id.group2);
		groupThree = (RadioButton) findViewById(R.id.group3);
		groupFour = (RadioButton) findViewById(R.id.group4);
		
		groupOne.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(groupOne.isSelected()) {
                    groupOne.setSelected(false);
					radioGroup.clearCheck();
					System.out.println(chan.getChanID() + " removed from group " + chan.getGroup());
					group.get(chan.getGroup()).remove(chan);
					chan.setGrouped(false);
					
					System.out.println(chan.getChanID() + " Grouped: " + chan.isGrouped());
                }
                else{
                	makeGroup(1);
                	groupOne.setSelected(true);
                	groupTwo.setSelected(false);
                	groupThree.setSelected(false);
                	groupFour.setSelected(false);
                	radioGroup.check(R.id.group1);
                	System.out.println("Checking group1");
                }
			}
		});
		groupTwo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(groupTwo.isSelected()) {
                    groupTwo.setSelected(false);
					radioGroup.clearCheck();
					System.out.println(chan.getChanID() + " removed from group " + chan.getGroup());
					group.get(chan.getGroup()).remove(chan);
					chan.setGrouped(false);
                }
                else{
                	makeGroup(2);
                	groupOne.setSelected(false);
                	groupTwo.setSelected(true);
                	groupThree.setSelected(false);
                	groupFour.setSelected(false);
                	radioGroup.check(R.id.group2);
                	System.out.println("Checking group2");
                }
			}
		});
		groupThree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(groupThree.isSelected()) {
					groupThree.setSelected(false);
					radioGroup.clearCheck();
					System.out.println(chan.getChanID() + " removed from group " + chan.getGroup());
					group.get(chan.getGroup()).remove(chan);
					chan.setGrouped(false);
                }
                else{
                	makeGroup(3);
                	groupTwo.setSelected(false);
                	groupOne.setSelected(false);
                	groupThree.setSelected(true);
                	groupFour.setSelected(false);
                	radioGroup.check(R.id.group3);
                	System.out.println("Checking group3");
                }
			}
		});
		groupFour.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(groupFour.isSelected()) {
					groupFour.setSelected(false);
					radioGroup.clearCheck();
					System.out.println(chan.getChanID() + " removed from group " + chan.getGroup());
					group.get(chan.getGroup()).remove(chan);
					chan.setGrouped(false);
                }
                else{
                	makeGroup(4);
                	groupTwo.setSelected(false);
                	groupOne.setSelected(false);
                	groupThree.setSelected(false);
                	groupFour.setSelected(true);
                	radioGroup.check(R.id.group4);
                	System.out.println("Checking group4");
                }
			}
		});
	}
	
	protected void makeGroup(int i) {
		if (chan.isGrouped()) {
			group.get(chan.getGroup()).remove(chan);
			chan.setGrouped(false);
		}
		
		group.get(i).add(this, chan);
		chan.setGrouped(true);
		chan.setGroup(i);
		
		System.out.println(chan.getChanID() + " added to group " + chan.getGroup() + " " + chan.isGrouped());
	}

	public void addCloseListener(OnClickListener ocl){
		close.setOnClickListener(ocl);
	}
	
	public int getChannelNum(){
		return chan.getChanID();
	}
	
	public VerticalSlider getGain() {
		return gain;
	}
	
	public TextView getGainLvl() {
		return gainLvl;
	}
}

