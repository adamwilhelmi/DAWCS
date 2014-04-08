
package com.cs.oswego.edu.dawcs;

import java.util.ArrayList;
import java.util.HashMap;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DAWCS extends Activity {
    final ArrayList<ChannelController> channels = new ArrayList<ChannelController>();

    public static final int NUM_STREAMS = 1;
    
    public static final Channels chans = new Channels();
    public static ArrayList<Channel> availableChans = new ArrayList<Channel>();
    public static HashMap<Integer, Group> groupsMap = new HashMap<Integer, Group>();
    
    private Channel chan;
    
    private int numChans;
   
    
    HashMap<ChannelController, View> bufferMap = new HashMap<ChannelController, View>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_dawcs);
        final LinearLayout ll = (LinearLayout)findViewById(R.id.chanHolder);
//        ll.setBackgroundColor(0xffa9a9a9);
        
        NonFocusingHorizontalScrollView hsv = (NonFocusingHorizontalScrollView) findViewById(R.id.scroll1);
        
        for (int i = 0; i < 4; i++) {
        	Group group = new Group();
        	group.setGroupID(i + 1);
        	groupsMap.put(i + 1, group);
        }
        
        for (int i = 0; i < 10; i++) { //for debugging/testing only - adds 10 channels on load
        	chan = makeNewChan();
			addChannel(ll, chan);
		}

        NetHandler.getInstance().setWifiManager(((WifiManager) this.getSystemService(DAWCS.WIFI_SERVICE)));
       
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        NetHandler.getInstance().invokeListenerThread();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dawc, menu);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_button:
            	if (chans.isEmpty() || numChans == chans.size()) {
            		chan = makeNewChan();
            	} else {
            		for (int i = 1; i < chans.size(); i++) {
            			if (!chans.getChan(i).doesExist()) {
            				chan = chans.getChan(i);
            				chan.setExists(true);
            				break;
            			}
            		}
            	}
                addChannel((LinearLayout)findViewById(R.id.chanHolder), chan);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void addChannel(final LinearLayout ll, Channel chan){
    	
    	//Following commented code is old code
//        final ChannelController newChannel = new ChannelController(this, true, true, true, new Channel(1, false));
//        newChannel.addCloseListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                removeChannel(newChannel, ll);
//            }
//        });
//        
//        channels.add(newChannel);
//        ll.addView(newChannel);
//        genBufferView(newChannel);
//        ll.addView(bufferMap.get(newChannel));
    	
    	//Following code is new code from Jeremy
    	chans.add(chan);
        availableChans.remove(chan);
    	
    	final ChannelController newChannel = new ChannelController(this, true, true, true, chan);
        newChannel.addCloseListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                removeChannel(newChannel, ll);
            }
        });
        
//        newChannel.requestDisallowInterceptTouchEvent(true);
        ll.addView(newChannel);
        genBufferView(newChannel);
        ll.addView(bufferMap.get(newChannel));
        
        numChans++;
        
        
    }
    public void removeChannel(ChannelController cc, LinearLayout ll){
    	//Following commented code is old code
//        channels.remove(cc);
//        ll.removeView(cc);
//        ll.removeView(bufferMap.get(cc));
//        bufferMap.remove(cc);
    	
    	//Following code is new code from Jeremy
    	chans.getChan(cc.getChannelNum()).setExists(false);
    	if (availableChans.isEmpty()) {
    		availableChans.add(chans.getChan(cc.getChannelNum()));
    	} else {
    		availableChans.add(chans.getChan(cc.getChannelNum()));
    		sortAvailableChans();
    	}
        ll.removeView(cc);
        ll.removeView(bufferMap.get(cc));
        bufferMap.remove(cc);
        numChans--;
    }
    
    private void sortAvailableChans() {
    	for (int i = 0; i < availableChans.size(); i ++) {
    		for (int j = availableChans.size() - 1; j >= (i + 1); j--) {
    			if (availableChans.get(j).getChanID() < availableChans.get(j - 1).getChanID()) {
    				Channel tmp = availableChans.get(j);
    				availableChans.set(j, availableChans.get(j - 1));
    				availableChans.set(j - 1, tmp);
    			}
    		}
    	}
    }
    
    private Channel makeNewChan() {
    	if (chans.isEmpty()) {
    		chan = new Channel(1, false);
    	} else {
    		chan = new Channel(chans.size() + 1, false);
    	}
    	return chan;
    }
    
    private void genBufferView(ChannelController cc){
    	TextView t = new TextView(getBaseContext());
    	t.setText("  ");
    	
    	bufferMap.put(cc, t);
    }
}
    
