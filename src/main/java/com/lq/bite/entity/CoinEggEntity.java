package com.lq.bite.entity;
/**
 * 币蛋请求结果
 * @author l.q
 *
 */
public class CoinEggEntity {
	public boolean result;
	public String code;
	public CoinEggUserBite data;
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public CoinEggUserBite getData() {
		return data;
	}
	public void setData(CoinEggUserBite data) {
		this.data = data;
	}
	
	
}
