package com.cs.oswego.edu.dawcs;

public class Channel {
	private boolean doesExist = false;
	private boolean isGrouped = false;
	private boolean isMaster = false;
	
	private int chanID,
				groupID;
	
	private double heq,
		leq,
		meq,
		pan,
		fade;
	
	private Group group;
	
	public Channel() { }
	
	public Channel(int chanID, boolean isGrouped) {
		this.chanID = chanID;
		this.isGrouped = isGrouped;
		doesExist = true;
	}
	
	public void updateAll(double heq, double leq, double meq, double pan, double fade) {
		this.setHighEQ(heq);
		this.setLowEQ(leq);
		this.setMidEQ(meq);
		this.setPan(pan);
		this.setFade(fade);
	}
	
	public void getAll() {
		this.getHeq();
		this.getLeq();
		this.getMeq();
		this.getPan();
		this.getFade();
	}
	
	public void killChan() {
		doesExist = false;
	}
	
	public void setHighEQ(double heq) {
		this.heq = heq;
	}
	
	public double getHeq() {
		return heq;
	}
	
	public void setLowEQ(double leq) {
		this.leq = leq;
	}
	
	public double getLeq() {
		return leq;
	}
	
	public void setMidEQ(double meq) {
		this.meq = meq;
	}
	
	public double getMeq() {
		return meq;
	}
	
	public void setPan(double pan) {
		this.pan = pan;
	}
	
	public double getPan() {
		return pan;
	}
	
	public void setFade(double fade) {
		this.fade = fade;
	}
	
	public double getFade() {
		return fade;
	}
	
	public int getChanID() {
		return chanID;
	}
	
	public boolean doesExist() {
		return doesExist;
	}
	
	public void setExists(boolean doesExist) {
		this.doesExist = doesExist;
	}
	
	public boolean isGrouped() {
		return isGrouped;
	}
	
	public void setGrouped(boolean isGrouped) {
		this.isGrouped = isGrouped;
	}
	
	public int getGroup() {
		return groupID;
	}
	
	public void setGroup(int groupID) {
		this.groupID = groupID;
	}
	
	public void setToMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}
	
	public boolean isMaster() {
		return isMaster;
	}
}
