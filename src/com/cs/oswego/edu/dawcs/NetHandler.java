package com.cs.oswego.edu.dawcs;

import java.net.*;
import java.io.*;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

public class NetHandler{
	private static NetHandler instance = null;
	
	public static final int DEFAULT_PORT = 21928;
	public static final String DEFAULT_IP_ADDRESS = "192.168.56.1";
	
	public volatile MIDIStream[] streams;
	public volatile DatagramSocket[] sockets;
	private WifiManager wifiManager;
	private NetworkInitThread nit;
	private NetworkThread nt;
	
	protected NetHandler(int numStreams){
		this.streams = new MIDIStream[numStreams];
		for(int i = 0; i < numStreams; i++){
			streams[i] = new MIDIStream(i);
		}
		
		this.nit = new NetworkInitThread();
		nit.start();
/*		try{
			nit.join();	
		}catch(InterruptedException e){
			e.printStackTrace();
		}*/
		
		this.nt = new NetworkThread();
	}
	
	public void startRecord(){}

	public void stopRecord(){}

	public InetAddress getBroadcastAddress()throws IOException{
//		WifiManager wifi = (WifiManager) DAWCS.getSystemService(DAWCS.WIFI_SERVICE);
		if(this.wifiManager == null){
			return null;
		}else{
			DhcpInfo dhcp = wifiManager.getDhcpInfo();
			if(dhcp == null){
				return null;
			}else{
				int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
				byte[] quads = new byte[4];
				for(int k = 0; k < 4; k++){
					quads[k] = (byte)((broadcast >> (k * 8)) & 0xFF);
				}
				return InetAddress.getByAddress(quads);
			}	
		}
	}
/*	private void sendNextPackets(){
		for(int i = 0; i < streams.length; i++){
			while(!streams[i].queue.isEmpty()){
				byte[] buf = streams[i].queue.dequeue().getPacket().getBytes();
				(new NetworkThread(buf, sockets[i])).start();
			}
		}
	}*/
	
	public void receivePacketFromChannel(MIDIPacket packet){
		streams[packet.getStreamNumber()].add(packet);
	}
	
	public void setWifiManager(WifiManager wifi){
		this.wifiManager = wifi;
	}
	
	public WifiManager getWifiManager(){return this.wifiManager;}
	
	public synchronized static NetHandler getInstance(){
		if(instance == null){
			instance = new NetHandler(DAWCS.NUM_STREAMS);
		}
		return instance;
	}
	
	public void invokeListenerThread(){
		nt.start();
	}
}
