package com.cs.oswego.edu.dawcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Group {
	private Channel master;
	private Channel tmpMaster;
	
	private int groupID;
	
	private HashMap<ChannelController, Channel> channels;
	
	public Group() {
		channels = new HashMap<ChannelController, Channel>();
	}
	
	public boolean add(ChannelController cc, Channel chan) {
		if (channels.isEmpty()) {
			channels.put(cc, chan);
			return true;
		} else {
			if (isInGroup(chan)) {
				return false;
			}			
			channels.put(cc, chan);
			return true;
		}
	}
	
	public boolean remove(Channel chan) {
		if (channels.isEmpty()) {
			return false;
		} else {
			if (isInGroup(chan)) {
				for (ChannelController cc : channels.keySet()) {
					if (channels.get(cc) == chan) {
						channels.remove(cc);
						break;
					}
				}
			}
			return true;
		}
	}
	
	public boolean remove(ChannelController cc) {
		if (channels.isEmpty()) {
			return false;
		} else {
			if (isInGroup(cc)) {
				channels.remove(cc);
			}
			return true;
		}
		
	}
	
	public Channel getMasterFader() {		
		
		for (Channel c : channels.values()) {
			tmpMaster = c;
			if (master == null) {
				master = tmpMaster;
				continue;
			} 
			
			if (tmpMaster.getFade() > master.getFade()) {
				master = tmpMaster;
			}
		}
		
		master.setToMaster(true);
		
		return master;
	}
	
	public boolean isInGroup(Channel chan) {
		return channels.containsValue(chan);
	}
	
	public boolean isInGroup(ChannelController cc) {
		return channels.containsKey(cc);
	}
	
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	
	public int getGroupID() {
		return groupID;
	}
	
	public Collection<Channel> values() {
		return channels.values();
	}
	
	public Collection<ChannelController> keys() {
		return channels.keySet();
	}
}
