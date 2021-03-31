package com.lq.bite.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lq.bite.allApi.GateIoApi;
import com.lq.bite.entity.gateIo.GateIoTicks;

@Component
public class GateIoTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Scheduled(cron = "*/50 * * * * ?")
	/**
	 * 50秒调用1次5分线
	 * @throws Exception
	 */
	public void execute5M() throws Exception{
		List<GateIoTicks> list = GateIoApi.getCandlesticks5M("BTC_USDT", "1","5m");
		if(list == null){
			logger.warn("5分钟线无反馈");
		}else if(list.size()>0){
			logger.warn(list.get(0).toString());
		}
	}
	@Scheduled(cron = "*/10 * * * * ?")
	/**
	 * 10秒调用1次1分线
	 * @throws Exception
	 */
	public void execute1M() throws Exception{
		List<GateIoTicks> list = GateIoApi.getCandlesticks1M("BTC_USDT", "1","1m");
		if(list == null){
			logger.warn("1分钟线无反馈");
		}else if(list.size()>0){
			logger.warn(list.get(0).toString());
		}
		
	}
}
