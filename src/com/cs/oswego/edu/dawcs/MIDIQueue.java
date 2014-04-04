package com.cs.oswego.edu.dawcs;

public class MIDIQueue {
	static class MIDINode{
		private MIDIPacket packet;
		private MIDINode next;
		
		public MIDINode(MIDIPacket packet){
			this.packet = packet;
			this.next = null;
		}
		
		public MIDIPacket getPacket(){return this.packet;}
		
		public MIDINode getNext(){return this.next;}
		
		public void setNext(MIDINode next){this.next = next;}
	}
	
	private MIDINode head;
	private MIDINode tail;
	
	public MIDIQueue(){
		this.head = null;
		this.tail = null;
	}
	
	public MIDIQueue(MIDIPacket packet){
		this.head = new MIDINode(packet);
		this.tail = head;
	}
	
	public MIDIQueue(MIDINode head){
		this.head = head;
		this.tail = head;
	}
	
	public synchronized void enqueue(MIDINode node){
		if(head == null){
			head = node;
			tail = head;
		}
		tail.next = node;
		tail = tail.next;
	}
	
	public synchronized void enqueue(MIDIPacket packet){
		if(head == null){
			head = new MIDINode(packet);
			tail = head;
		}
		tail.next = new MIDINode(packet);
		tail = tail.next;
	}
	
	public synchronized MIDINode dequeue(){
		MIDINode a = head;
		head = head.next;
		return a;
	}
	
	public synchronized MIDINode first(){return this.head;}
	
	public synchronized MIDINode last(){return this.tail;}
	
	public synchronized boolean isEmpty(){return (this.head == null);}
}
