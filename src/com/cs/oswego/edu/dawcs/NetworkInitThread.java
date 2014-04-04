package com.cs.oswego.edu.dawcs;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import android.net.wifi.WifiManager;

public class NetworkInitThread extends Thread {
	public void run(){
		NetHandler.getInstance().sockets = new DatagramSocket[NetHandler.getInstance().streams.length];
		for(int i = 0; i < NetHandler.getInstance().sockets.length; i++){
//	        WifiManager.MulticastLock lock = NetHandler.getInstance().getWifiManager().createMulticastLock("Log_Tag");
//	        lock.acquire();
			try{
				NetHandler.getInstance().sockets[i] = new DatagramSocket(NetHandler.DEFAULT_PORT);
//				NetHandler.getInstance().sockets[i].setBroadcast(true);
				try{
//					NetHandler.getInstance().sockets[i].connect(new InetSocketAddress(NetHandler.getInstance().getBroadcastAddress(), NetHandler.DEFAULT_PORT));
					NetHandler.getInstance().sockets[i].connect(new InetSocketAddress(NetHandler.DEFAULT_IP_ADDRESS, NetHandler.DEFAULT_PORT));
				}catch(IOException e){
					e.printStackTrace();
				}
			}catch(SocketException e){
				e.printStackTrace();
			}
//			lock.release();
		}
	}
}
