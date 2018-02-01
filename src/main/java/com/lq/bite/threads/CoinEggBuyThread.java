package com.lq.bite.threads;

import java.util.List;

import com.lq.bite.CustomPropertiesConfig;
import com.lq.bite.allApi.CoinEggAPI;
import com.lq.bite.common.Constant;
import com.lq.bite.entity.AccountKeys;
import com.lq.bite.entity.CoinEggEntity;
import com.lq.bite.utils.RedisAPI;

public class CoinEggBuyThread implements Runnable{
	
	private String biteName;
	private Float price;
	private CustomPropertiesConfig customPropertiesConfig;
	public CoinEggBuyThread(String biteName,Float price,CustomPropertiesConfig customPropertiesConfig){
		this.biteName = biteName;
		this.price = price;
		this.customPropertiesConfig = customPropertiesConfig;
	}
	
	@Override
	public void run() {
		Long beginTime = System.currentTimeMillis();
		boolean flag = true;
		//当前剩余金额 小于0.003所以无法交易
		if(price<0.003){
			flag = false;
		}else{
		
		}
		
		while(flag){
			
		}
	}

}
