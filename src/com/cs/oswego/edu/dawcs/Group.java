package com.cs.oswego.edu.dawcs;

import java.util.HashMap;

public class Group {
	private int groupID;
	private HashMap<Integer, Channel> channels;
	
	public Group() {
		channels = new HashMap<Integer, Channel>();
	}
	
	public boolean add(Channel chan) {
		if (channels.isEmpty()) {
			channels.put(0, chan);
			return true;
		} else {
			if (isInGroup(chan)) {
				return false;
			}
			
			int channelID = chan.getChanID();
			
			for (int i = 0; i < channels.size(); i++) {
				if (channelID < channels.get(i).getChanID()) {
					Channel tempChan = channels.get(i);
					channels.put(i, chan);
					add(tempChan);
				} else {
					channels.put(i, chan);
				}
			}
			return true;
		}
	}
	
	public boolean remove(Channel chan) {
		if (channels.isEmpty()) {
			return false;
		} else {
			if (isInGroup(chan)) {
				for (int i = 0; i < channels.size(); i++) {
					if (chan == channels.get(i)) {
						channels.remove(i);
						return true;
					}					
				}
			}
			return false;
		}
	}
	
	public boolean isInGroup(Channel chan) {
		return channels.containsValue(chan);
	}
	
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	
	public int getGroupID() {
		return groupID;
	}

}
