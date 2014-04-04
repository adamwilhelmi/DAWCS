package com.cs.oswego.edu.dawcs;

public class MIDIPacket {
	public int channelNumber, controlID, controlValue;
	private byte[] packet;
	private int streamNumber;
	
	public MIDIPacket(int channelNumber, int controlID, int controlValue){
		this.channelNumber = channelNumber;
		this.controlID = controlID;
		this.controlValue = controlValue;
		
		this.streamNumber = channelNumber / 16;
		
		this.packet = new byte[3];
		packet[0] = (byte)(0xb0 + ((channelNumber - 1) & 0xf));
		packet[1] = (byte)controlID;
		packet[2] = (byte)controlValue;
	}
	
	public void update(){
		this.streamNumber = channelNumber / 16;
		
		this.packet = new byte[3];
		packet[0] = (byte)(0xb0 + ((channelNumber - 1) & 0xf));
		packet[1] = (byte)controlID;
		packet[2] = (byte)controlValue;
	}
	
	public int getStreamNumber(){return this.streamNumber;}
	
	public byte[] getBytes(){return this.packet;}
}
