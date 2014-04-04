package com.cs.oswego.edu.dawcs;

import java.sql.Timestamp;
import java.util.HashMap;

public class MIDIStream {
	public HashMap<Timestamp, MIDIPacket> packets;
	public int streamID;
	public MIDIQueue queue;
	
	public MIDIStream(int streamID){
		this.packets = new HashMap<Timestamp, MIDIPacket>();
		this.streamID = streamID;
		this.queue = new MIDIQueue();
	}
	
	public MIDIStream(HashMap<Timestamp, MIDIPacket> packets, int streamID){
		this.packets = packets;
		this.streamID = streamID;
		this.queue = new MIDIQueue();
	}
	
	public MIDIStream(HashMap<Timestamp, MIDIPacket> packets, int streamID, MIDIQueue queue){
		this.packets = packets;
		this.streamID = streamID;
		this.queue = queue;
	}
	
	public void add(MIDIPacket packet){
		packets.put(new Timestamp(System.currentTimeMillis()), packet);
		queue.enqueue(packet);
	}
}
