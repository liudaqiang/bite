package com.lq.bite.controller;

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
import com.lq.bite.entity.BiteOrders;
import com.lq.bite.entity.CleanBite;
import com.lq.bite.entity.IcoData;
import com.lq.bite.service.IcoDataService;
import com.lq.bite.threads.CoinEggBuyThread;
import com.lq.bite.utils.HttpClientUtil;
import com.lq.bite.utils.RedisAPI;

@Component
public class ScheduledTasks {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	private final String ticker = CoinEggInterface.TICKER;

	@Autowired
	private IcoDataService icoDataService;

	/**
	 * 每分钟监听一次当前行情 并入库
	 */
	@SuppressWarnings("unchecked")
	//@Scheduled(cron = "0/5 * * * * ?")
	public void executeFileDownLoadTask() {
		List<CleanBite> allIco = (List<CleanBite>) RedisAPI.getObj("allIco");
		long start = System.currentTimeMillis();
		try {
			int pagecount = 1;
			ExecutorService executors = Executors.newFixedThreadPool(1);
			CountDownLatch countDownLatch = new CountDownLatch(1);
			for (int i = 0; i < pagecount; i++) {
				HttpGet httpget = new HttpGet(ticker + "?coin=eth");
				HttpClientUtil.config(httpget);
				// 启动线程抓取
				executors.execute(new GetRunnable(ticker + "?coin=eth", countDownLatch,
						icoDataService,"eth"));
			}
			countDownLatch.await();
			executors.shutdown();
		} catch (InterruptedException e) {
			// logger.error(e.getMessage());
		} finally {
			logger.info(
					"行情入库线程结束！");
		}
		long end = System.currentTimeMillis();
		logger.info("行情入库consume -> " + (end - start));
	}

	static class GetRunnable implements Runnable {
		private CountDownLatch countDownLatch;
		private String url;
		private IcoDataService icoDataService;
		private String codeName;

		public GetRunnable(String url, CountDownLatch countDownLatch, IcoDataService icoDataService, String codeName) {
			// logger.info(url);
			this.url = url;
			this.countDownLatch = countDownLatch;
			this.icoDataService = icoDataService;
			this.codeName = codeName;
		}

		@Override
		public void run() {
			try {
				String json = HttpClientUtil.get(url);
				// logger.info(json);
				IcoData icoData = JSON.parseObject(json, IcoData.class);
				icoData.setCodeName(codeName);
				//如果(买1/卖1)>1.005
				logger.warn(icoData.toString());
				logger.warn("买1价格："+icoData.getBuy());
				logger.warn("卖1价格："+icoData.getSell());
				if(Float.parseFloat(icoData.getSell())/Float.parseFloat(icoData.getBuy()) >= 1.0045){
					if(RedisAPI.getStr("CoinEggBuy"+icoData.getCodeName()) != null && "true".equals(RedisAPI.getStr("CoinEggBuy"+icoData.getCodeName()))){
						logger.warn("正在撸羊毛");
					}else{
						logger.warn("存在撸羊毛机会");
						//启动抢买1挂单循环
						RedisAPI.setStr("CoinEggBuy"+icoData.getCodeName(), "true", 86400);
						new Thread(new CoinEggBuyThread(icoData)).start();;
					}
				}else{
				
					logger.warn("没有撸羊毛机会");
				}
				icoDataService.insert(icoData);
			} catch (Exception e) {
				// logger.error("error:"+e.getMessage());
			} finally {
				countDownLatch.countDown();
			}
		}
	}

	/**
	 * 每30秒钟监听一次全部币种 近100次成交
	 */
	@SuppressWarnings("unchecked")
	//@Scheduled(cron = "0/30 * * * * ?")
	public void listenerBiteDeal() {
		List<CleanBite> allIco = (List<CleanBite>) RedisAPI.getObj("allIco");
		long start = System.currentTimeMillis();
		try {
			int pagecount = allIco.size();
			ExecutorService executors = Executors.newFixedThreadPool(pagecount);
			CountDownLatch countDownLatch = new CountDownLatch(pagecount);
			for (int i = 0; i < pagecount; i++) {
				HttpGet httpget = new HttpGet(CoinEggInterface.ORDERS+"?coin="+allIco.get(i).getBiteName());
				HttpClientUtil.config(httpget);
				// 启动线程抓取
				executors.execute(new GetBiteOrders(allIco.get(i).getBiteName(),countDownLatch));
			}
			countDownLatch.await();
			executors.shutdown();
		} catch (InterruptedException e) {
			logger.error("error:"+e.getMessage());
		}finally {
			logger.info(
					"线程监控币种结束！");
		}
		long end = System.currentTimeMillis();
		logger.info("线程监控币种consume -> " + (end - start));
	}

	static class GetBiteOrders implements Runnable {
		private String biteName;
		private CountDownLatch countDownLatch;
		public GetBiteOrders(String biteName,CountDownLatch countDownLatch) {
			this.biteName = biteName;
			this.countDownLatch = countDownLatch;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			logger.info("this thread name"+Thread.currentThread().getName()+"is bite="+biteName+"执行开始");
			long start = System.currentTimeMillis();
			try {
				List<BiteOrders> tradeOrders = CoinEggAPI.tradeOrders(biteName);
				// 获取缓存中的某币的单子
				List<BiteOrders> dogeOrders = (List<BiteOrders>) RedisAPI.getObj(biteName + "Orders");
				if (dogeOrders != null && dogeOrders.size() !=0 && tradeOrders != null) {
					// 获取当前币的单子最后一个
					long date = dogeOrders.get(dogeOrders.size() - 1).getDate();
					int index = -1;
					// 哪缓存的最近的时间戳与新的比较 找到位置
					for (int i = 0; i < tradeOrders.size(); i++) {
						if (tradeOrders.get(i).getDate().compareTo(date) == 1) {
							index = i;
							break;
						}
					}
					if (index != -1) {
						tradeOrders = tradeOrders.subList(index, tradeOrders.size());
						dogeOrders.addAll(tradeOrders);
					}
					RedisAPI.setObj(biteName + "Orders", dogeOrders, 86400);
				} else if(tradeOrders != null){
					RedisAPI.setObj(biteName + "Orders", tradeOrders, 86400);
				}else{
					logger.info("this thread name"+Thread.currentThread().getName()+"is bite="+biteName+"出现了异常，已结束");
				}
			}catch(Exception e){
				logger.error("error:"+e.getMessage());
			}finally {
				countDownLatch.countDown();
			}
			logger.info("this thread name"+Thread.currentThread().getName()+"is bite="+biteName+"执行完毕");
			long end = System.currentTimeMillis();
			logger.info("线程  "+biteName+"->执行时间" + (end - start));
			
		}

	}
}
