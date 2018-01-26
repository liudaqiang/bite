package com.lq.bite.entity;

import java.io.Serializable;
/**
 * 市场交易
 * @author l.q
 *
 */
public class BiteOrders implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String biteName;
	private Long date;
	private float price;
	private float amount;
	private String tid;
	private String type;// sell/buy
	public String getBiteName() {
		return biteName;
	}
	public void setBiteName(String biteName) {
		this.biteName = biteName;
	}
	
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
