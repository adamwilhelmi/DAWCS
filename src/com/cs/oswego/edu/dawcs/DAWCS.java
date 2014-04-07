
package com.cs.oswego.edu.dawcs;

import java.util.ArrayList;
import java.util.HashMap;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DAWCS extends Activity {
    final ArrayList<ChannelController> channels = new ArrayList<ChannelController>();

    public static final int NUM_STREAMS = 1;
    
    HashMap<ChannelController, View> bufferMap = new HashMap<ChannelController, View>();
    ArrayList<Integer> unavailChannels = new ArrayList<Integer>();
    
    Button addChannel;
    Button toggleScroll;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_dawcs);
        final LinearLayout ll = (LinearLayout)findViewById(R.id.chanHolder);
        ll.setBackgroundColor(0xffa9a9a9);
        
        NonFocusingHorizontalScrollView hsv = (NonFocusingHorizontalScrollView) findViewById(R.id.scroll1);
        
        for (int i = 0; i < 10; i++) { //for debugging/testing only - adds 10 channels on load
			addChannel(ll);
		}
		addChannel = (Button) findViewById(R.id.add_channel);
        addChannel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                addChannel(ll);
            }
        });

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
    
    public void addChannel(final LinearLayout ll){
        final ChannelController newChannel = new ChannelController(this, true, true, true, new Channel(1, false));
        newChannel.addCloseListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                removeChannel(newChannel, ll);
            }
        });
        
//        newChannel.requestDisallowInterceptTouchEvent(true);
        
        channels.add(newChannel);
        ll.addView(newChannel);
        genBufferView(newChannel);
        ll.addView(bufferMap.get(newChannel));
        
        
    }
    public void removeChannel(ChannelController cc, LinearLayout ll){
        channels.remove(cc);
        ll.removeView(cc);
        ll.removeView(bufferMap.get(cc));
        bufferMap.remove(cc);
    }
    
    private void genBufferView(ChannelController cc){
    	TextView t = new TextView(getBaseContext());
    	t.setText("  ");
    	
    	bufferMap.put(cc, t);
    }
}
    
