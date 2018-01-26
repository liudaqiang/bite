package com.lq.bite.controller;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lq.bite.allApi.CoinEggAPI;
import com.lq.bite.allInterface.CoinEggInterface;
import com.lq.bite.dao.AllIcoDao;
import com.lq.bite.entity.BiteOrders;
import com.lq.bite.entity.CleanBite;
import com.lq.bite.entity.IcoData;
import com.lq.bite.service.IcoDataService;
import com.lq.bite.utils.HttpClientUtil;
import com.lq.bite.utils.RedisAPI;

@Component
public class ScheduledTasks {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	private final String ticker = CoinEggInterface.TICKER;

	@Autowired
	private IcoDataService icoDataService;

	@Autowired
	private AllIcoDao allIcoDao;
	/**
	 * 每分钟监听一次当前行情  并入库
	 */
	@Scheduled(cron = "0 * * * * ?")
	public void executeFileDownLoadTask() {
		List<CleanBite> allIco = (List<CleanBite>)RedisAPI.getObj("allIco");
		long start = System.currentTimeMillis();
		try {
			int pagecount = allIco.size();
			ExecutorService executors = Executors.newFixedThreadPool(pagecount);
			CountDownLatch countDownLatch = new CountDownLatch(pagecount);
			for (int i = 0; i < pagecount; i++) {
				HttpGet httpget = new HttpGet(ticker+"?coin="+allIco.get(i).getBiteName());
				HttpClientUtil.config(httpget);
				// 启动线程抓取
				executors.execute(new GetRunnable(ticker+"?coin="+allIco.get(i).getBiteName(), countDownLatch,icoDataService, allIco.get(i).getBiteName()));
			}
			countDownLatch.await();
			executors.shutdown();
		} catch (InterruptedException e) {
			//logger.error(e.getMessage());
		} finally {
			logger.info(
					"线程" + Thread.currentThread().getName() + "," + System.currentTimeMillis() + ", 所有线程已完成，开始进入下一步！");
		}
		long end = System.currentTimeMillis();
		logger.info("consume -> " + (end - start));
		logger.info("定时任务事件");
	}

	static class GetRunnable implements Runnable {
		private CountDownLatch countDownLatch;
		private String url;
		private IcoDataService icoDataService;
		private String codeName;
		public GetRunnable(String url, CountDownLatch countDownLatch,IcoDataService icoDataService,String codeName) {
			//logger.info(url);
			this.url = url;
			this.countDownLatch = countDownLatch;
			this.icoDataService = icoDataService;
			this.codeName = codeName;
		}

		@Override
		public void run() {
			try {
				String json = HttpClientUtil.get(url);
				//logger.info(json);
				IcoData icoData = JSON.parseObject(json, IcoData.class);
				icoData.setCodeName(codeName);
				icoDataService.insert(icoData);
			} catch(Exception e){
				//logger.error("error:"+e.getMessage());
			}finally {
				countDownLatch.countDown();
			}
		}
	}
	
	/**
	 * 每5秒钟监听一次某币   近100次成交
	 */
	@Scheduled(cron="0/5 * * * * ?")
	public void listenerBiteDeal(){
		List<CleanBite> allIco  = (List<CleanBite>)RedisAPI.getObj("allIco");
		long start = System.currentTimeMillis();
		List<BiteOrders> tradeOrders = CoinEggAPI.tradeOrders("doge");
		//获取缓存中的某币的单子
		List<BiteOrders> dogeOrders = (List<BiteOrders>)RedisAPI.getObj("dogeOrders");
		if(dogeOrders != null){
			//获取当前币的单子最后一个
			long date = dogeOrders.get(dogeOrders.size()-1).getDate();
			int index = -1;
			//哪缓存的最近的时间戳与新的比较  找到位置
			for(int i=0;i<tradeOrders.size();i++){
				if(tradeOrders.get(i).getDate().compareTo(date) == 1){
					index = i;
					System.out.println("我来过这里i="+i);
					break;
				}
			}
			logger.info("index="+index);
			if(index != -1){
				tradeOrders = tradeOrders.subList(index, tradeOrders.size());
				dogeOrders.addAll(tradeOrders);
			}
			logger.info("tradeOrdersSize"+tradeOrders.size());
			RedisAPI.setObj("dogeOrders", dogeOrders, 86400);
		}else{
			RedisAPI.setObj("dogeOrders", tradeOrders, 86400);
		}
		logger.info("当前缓存数量"+dogeOrders.size());
		long end = System.currentTimeMillis();
		logger.info("consume -> " + (end - start));
	}
}
