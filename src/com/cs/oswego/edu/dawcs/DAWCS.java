package com.cs.oswego.edu.dawcs;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public class DAWCS extends Activity {
    final ArrayList<ChannelController> channels = new ArrayList<ChannelController>();
    
    HashMap<ChannelController, View> bufferMap = new HashMap<ChannelController, View>();
    ArrayList<Integer> unavailChannels = new ArrayList<Integer>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dawcs);
        
        setContentView(R.layout.activity_dawcs);
        final LinearLayout ll = (LinearLayout)findViewById(R.id.LinearLayout1);
        ll.setBackgroundColor(0xffa9a9a9);
        
        addChannel(ll);
        
        Button addChannel = (Button) findViewById(R.id.add_channel);
        addChannel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                addChannel(ll);
            }
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dawc, menu);
        
        return true;
    }
    
    public void addChannel(final LinearLayout ll){
        final ChannelController newChannel = new ChannelController(this, true, true, true);
        newChannel.addCloseListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                removeChannel(newChannel, ll);
            }
        });
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
    
