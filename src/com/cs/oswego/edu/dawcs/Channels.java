package com.cs.oswego.edu.dawcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Channels {
	private static final int MAX_CHANS = 16;
	private static HashMap<Integer, Channel> channels;
	private static ArrayList<Integer> availableChans = new ArrayList<Integer>();
	
	public Channels() {
		channels = new HashMap<Integer, Channel>();
	}
	
	public boolean add(Channel chan) {
		if (channels.containsValue(chan)) {
			return false;
		} else {
			channels.put(chan.getChanID(), chan);
			return true;
		}
	}
	
	public boolean add(int chanID) {
		if (channels.containsKey(chanID)) {
			return false;
		} else {
			Channel chan = new Channel(chanID, false);
			channels.put(chanID, chan);
			return true;
		}
	}
	
	public boolean remove(Channel chan) {
		if (!channels.containsValue(chan)) {
			return false;
		} else {
			channels.remove(chan.getChanID());
			return true;
		}
	}
	
	public boolean remove(int chanID) {
		if (!channels.containsKey(chanID)) {
			return false;
		} else {
			channels.remove(chanID);
			return true;
		}
	}
	
	public Channel getChan(int chanID) {
		return channels.get(chanID);
	}
	
	public boolean findChan(int chanID) {
		return channels.containsKey(chanID);
	}
	
	public boolean findChan(Channel chan) {
		return channels.containsValue(chan);
	}
	
	public int size() {
		return channels.size();
	}
	
	public int createChanID() {
		int nextID = 1;
		if (isEmpty()) {
			return nextID;
		} else {
			return channels.size() + 1;
		}
	}
	
	public int maxChannels() {
		return MAX_CHANS;
	}
	
	public boolean isEmpty() {
		return channels.isEmpty();
	}
	public List<Channel> getAllChannels(){
		return new ArrayList<Channel>(channels.values()); 
	}
}
