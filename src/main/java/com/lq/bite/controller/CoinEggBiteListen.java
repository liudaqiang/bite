package com.lq.bite.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lq.bite.entity.BiteOrders;
import com.lq.bite.entity.CleanBite;
import com.lq.bite.utils.RedisAPI;
import com.lq.bite.utils.StringUtils;
/**
 * 币蛋币种监听器
 * @author l.q
 *
 */
@Component
@SuppressWarnings("unchecked")
public class CoinEggBiteListen {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 初始化时直接创造10个线程的线程池
	 */
	private ExecutorService es = Executors.newFixedThreadPool(10);
	
	/**
	 * 根据定时获得比特币缓存数据
	 */
	@Scheduled(cron = "0/5 * * * * ?")
	public void getBiteReidsByte(){
		List<CleanBite> allIco = (List<CleanBite>) RedisAPI.getObj("allIco");
		for(CleanBite cleanBite:allIco){
			es.execute(new GetBiteOrders(cleanBite.getBiteName()));
		}
	}
	
	static class GetBiteOrders implements Runnable{
		private Logger logger = LoggerFactory.getLogger(this.getClass());
		private String biteName;
		
		public GetBiteOrders(String biteName){
			this.biteName = biteName;
		}
		
		@Override
		public void run() {
			List<BiteOrders> tradeOrders = (List<BiteOrders>)RedisAPI.getObj(biteName+"Orders");
			if(tradeOrders != null){
				//获取开始时间点
				String beginTime = RedisAPI.getStr(biteName+"beginTime");
				//获取结束时间点
				String endTime = RedisAPI.getStr(biteName+"endTime");
				//获取最高价格
				String highPrice = RedisAPI.getStr(biteName+"highPrice");
				//获取最低价格
				String lowPrice = RedisAPI.getStr(biteName+"lowPrice");
				Long ordersLastTime = tradeOrders.get(tradeOrders.size()-1).getDate();
				//开始时间不存在则插入
				if(StringUtils.isBlank(beginTime)){
					RedisAPI.setStr(biteName+"beginTime", String.valueOf(tradeOrders.get(0).getDate()), 86400);
				}
				//结束时间不存在则插入
				if(StringUtils.isBlank(endTime)){
					RedisAPI.setStr(biteName+"endTime", String.valueOf(tradeOrders.get(tradeOrders.size()-1).getDate()), 86400);
				}
				/**
				 * 如果时间上出现新值，那么比较原开始时间  是否已经超过了3个小时。如果超过了则动态更新
				 */
				if(!ordersLastTime.equals(Long.valueOf(endTime))){
					RedisAPI.setStr(biteName+"endTime", String.valueOf(ordersLastTime), 86400);
					Long newBeginTime = ordersLastTime - 60*60*3;
					if(Long.parseLong(beginTime) < newBeginTime){
						List<BiteOrders> newBiteOrdersList = tradeOrders.stream().filter(biteOrder->newBeginTime>biteOrder.getDate()).collect(Collectors.toList());
						RedisAPI.setObj(biteName+"Orders", newBiteOrdersList, 86400);
					}
					BiteOrders maxBite = tradeOrders.stream().collect(Collectors.maxBy(Comparator.comparing(BiteOrders::getPrice))).get();
					BiteOrders minBite = tradeOrders.stream().collect(Collectors.minBy(Comparator.comparing(BiteOrders::getPrice))).get();
					/*if(){
						
					}*/
				}
			}
		}
		
	}
}
