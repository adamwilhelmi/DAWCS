package com.cs.oswego.edu.dawcs;

//import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import android.net.wifi.WifiManager;

//import android.os.AsyncTask;

public class NetworkThread extends Thread{

	public void run(){
		//UDP Loop
//        WifiManager.MulticastLock lock = NetHandler.getInstance().getWifiManager().createMulticastLock("Log_Tag");
//        lock.acquire();
		for(;;){
			for(int i = 0; i < NetHandler.getInstance().streams.length; i++){
				long startTime = System.nanoTime();
				while(!NetHandler.getInstance().streams[i].queue.isEmpty()){
					try{
//						byte[] buf = Arrays.copyOf(NetHandler.getInstance().streams[i].queue.dequeue().getPacket().getBytes(), 1024);
						byte[] buf = NetHandler.getInstance().streams[i].queue.dequeue().getPacket().getBytes();
						InetAddress address = InetAddress.getByName(NetHandler.DEFAULT_IP_ADDRESS);
						DatagramPacket packet = new DatagramPacket(buf, buf.length, address, NetHandler.DEFAULT_PORT);
						NetHandler.getInstance().sockets[i].send(packet);
					}catch(UnknownHostException uhe){
						uhe.printStackTrace();
					}catch(IOException ioe){
						ioe.printStackTrace();
					}
					
					if((System.nanoTime() - startTime) <= 5000){
						continue;
					}else{
						try{
							Thread.sleep(5);							
						}catch(InterruptedException e){
							e.printStackTrace();
						}
						startTime = System.nanoTime();
						break;
					}
				}
			}
		}
		
		//TCP Init
/*		Socket[] sockets = new Socket[DAWCS.netHandler.streams.length];
		for(int i = 0; i < DAWCS.netHandler.streams.length; i++){
			try{
				sockets[i] = new Socket(DAWCS.netHandler.DEFAULT_IP_ADDRESS, DAWCS.netHandler.DEFAULT_PORT);
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
		//TCP Loop
		for(;;){
			for(int i = 0; i < DAWCS.netHandler.streams.length; i++){
				long startTime = System.nanoTime();
				while(!DAWCS.netHandler.streams[i].queue.isEmpty()){
					try{
						byte[] buf = DAWCS.netHandler.streams[i].queue.dequeue().getPacket().getBytes();
						DataOutputStream out = new DataOutputStream(sockets[i].getOutputStream());
						out.write(buf, 0, buf.length);
					}catch(UnknownHostException uhe){
						uhe.printStackTrace();
					}catch(IOException ioe){
						ioe.printStackTrace();
					}
					
					if((System.nanoTime() - startTime) <= 5000){
						continue;
					}else{
						try{
							Thread.sleep(5);							
						}catch(InterruptedException e){
							e.printStackTrace();
						}
						startTime = System.nanoTime();
						break;
					}
				}
			}
		}*/
	}

}
