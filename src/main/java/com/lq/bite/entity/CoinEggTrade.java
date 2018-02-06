package com.lq.bite.entity;

import java.util.Date;

public class CoinEggTrade extends AccountKeys{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float amount;//购买数量
	private float price;//购买价格
	private String type;//买单或者卖单  buy  sell
	private String coin;//币种类型
	private Date dateTime;//交易时间
	private String status;//交易状态
	private Integer tradeId;//委托id
	private float amount_original;//下单时数量
	private float amount_outstanding;//当前数量
	
	
	public Integer getTradeId() {
		return tradeId;
	}
	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCoin() {
		return coin;
	}
	public void setCoin(String coin) {
		this.coin = coin;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public float getAmount_original() {
		return amount_original;
	}
	public void setAmount_original(float amount_original) {
		this.amount_original = amount_original;
	}
	public float getAmount_outstanding() {
		return amount_outstanding;
	}
	public void setAmount_outstanding(float amount_outstanding) {
		this.amount_outstanding = amount_outstanding;
	}
}
