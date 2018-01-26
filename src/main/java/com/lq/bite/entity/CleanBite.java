package com.lq.bite.entity;
/**
 * 通过反射整合出来的所有币种
 * @author l.q
 *
 */
public class CleanBite implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String biteName;
	private String blance;
	private String lock;
	public String getBiteName() {
		return biteName;
	}
	public void setBiteName(String biteName) {
		this.biteName = biteName;
	}
	public String getBlance() {
		return blance;
	}
	public void setBlance(String blance) {
		this.blance = blance;
	}
	public String getLock() {
		return lock;
	}
	public void setLock(String lock) {
		this.lock = lock;
	}
	
	
}
