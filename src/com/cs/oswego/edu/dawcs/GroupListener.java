package com.cs.oswego.edu.dawcs;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class GroupListener extends Thread implements OnSeekBarChangeListener {
	private List<OnSeekBarChangeListener> listeners = new ArrayList<OnSeekBarChangeListener>();
	private Group group;
	
	public GroupListener () { }
	
	public void registerListener(Group group) {
		this.group = group;	
	}
	
	public void addChangeListener(OnSeekBarChangeListener sbc) {
		listeners.add(sbc);
	}
	
	public void removeChangeListener(OnSeekBarChangeListener sbc) {
		for (int i = 0; i < listeners.size(); i++) {
			if (listeners.get(i) == sbc) {
				listeners.remove(i);
				break;
			}
		}
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		ListIterator<OnSeekBarChangeListener> sit = listeners.listIterator();
		
		int i = 0;
		while (sit.hasNext() && (i <= listeners.size() - 1)) {
			listeners.get(i).onProgressChanged(seekBar, progress, fromUser);
			i++;
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

}
