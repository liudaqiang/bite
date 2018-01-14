package com.lq.bite.entity;

public class CoinEggTrade extends AccountKeys{
	private int amount;//购买数量
	private float price;//购买价格
	private String type;//买单或者卖单  buy  sell
	private String coin;//币种类型
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
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
	
	
}
