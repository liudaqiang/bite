package com.lq.bite.controller;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lq.bite.CustomPropertiesConfig;
import com.lq.bite.entity.AccountKeys;
import com.lq.bite.entity.BiteOrders;
import com.lq.bite.threads.CoinEggBuyThread;
import com.lq.bite.utils.RedisAPI;
import com.lq.bite.utils.StringUtils;

/**
 * 币蛋币种监听器
 * 
 * @author l.q
 *
 */
@Component
@SuppressWarnings("unchecked")
public class CoinEggBiteListen {
	/**
	 * 初始化时直接创造10个线程的线程池
	 */
	private ExecutorService es = Executors.newFixedThreadPool(10);
	@Autowired
	private CustomPropertiesConfig customPropertiesConfig;
	
	private AccountKeys accountKeys = (AccountKeys)RedisAPI.getObj("accountKeys");
	/**
	 * 根据定时获得比特币缓存数据
	 */
	@Scheduled(cron = "0/10 * * * * ?")
	public void getBiteReidsByte() {
		if(accountKeys == null){
			accountKeys = (AccountKeys)RedisAPI.getObj("accountKeys");
		}
		//List<CleanBite> allIco = (List<CleanBite>) RedisAPI.getObj("allIco");
		//for (CleanBite cleanBite : allIco) {
			es.execute(new GetBiteOrders("ipt", customPropertiesConfig,accountKeys));
		//}
	}

	class GetBiteOrders implements Runnable {
		private Logger logger = LoggerFactory.getLogger(this.getClass());
		private String biteName;
		private CustomPropertiesConfig customPropertiesConfig;
		private AccountKeys accountKeys;
		public GetBiteOrders(String biteName, CustomPropertiesConfig customPropertiesConfig,AccountKeys accountKeys) {
			this.customPropertiesConfig = customPropertiesConfig;
			this.biteName = biteName;
		}

		@Override
		public void run() {
			logger.warn("当前操作的是" + biteName);
			List<BiteOrders> tradeOrders = (List<BiteOrders>) RedisAPI.getObj(biteName + "Orders");
			if (tradeOrders != null && tradeOrders.size() != 0) {
				logger.warn("当前tradeOrders长度为" + tradeOrders.size());
				// 获取开始时间点
				String beginTime = RedisAPI.getStr(biteName + "beginTime");
				// 获取结束时间点
				String endTime = RedisAPI.getStr(biteName + "endTime");

				Long ordersLastTime = tradeOrders.get(tradeOrders.size() - 1).getDate();
				// 开始时间不存在则插入
				if (StringUtils.isBlank(beginTime)) {
					logger.warn("不存在开始时间，进行插入操作");
					RedisAPI.setStr(biteName + "beginTime", String.valueOf(tradeOrders.get(0).getDate()),
							customPropertiesConfig.getRedisSaveTime());
					beginTime = RedisAPI.getStr(biteName + "beginTime");
				}
				// 结束时间不存在则插入
				if (StringUtils.isBlank(endTime)) {
					logger.warn("不存在结束时间，进行插入操作");
					RedisAPI.setStr(biteName + "endTime",
							String.valueOf(tradeOrders.get(tradeOrders.size() - 1).getDate()),
							customPropertiesConfig.getRedisSaveTime());
					endTime = RedisAPI.getStr(biteName + "endTime");
				}
				/**
				 * 如果时间上出现新值，那么比较原开始时间 是否已经超过了3个小时。如果超过了则动态更新
				 */
				logger.warn("我在if前边我现在的begintime是："+beginTime);
				if (!ordersLastTime.equals(Long.valueOf(endTime))) {
					logger.warn("出现了新值，准备校验");
					RedisAPI.setStr(biteName + "endTime", String.valueOf(ordersLastTime),
							customPropertiesConfig.getRedisSaveTime());
					Long newBeginTime = ordersLastTime - customPropertiesConfig.getOrderSaveTime();
					if (Long.parseLong(beginTime) < newBeginTime) {
						logger.warn("正在动态更新  缓存时间");
						List<BiteOrders> newBiteOrdersList = tradeOrders.stream()
								.filter(biteOrder -> newBeginTime < biteOrder.getDate()).collect(Collectors.toList());
						logger.warn("更新后，list长度：" + newBiteOrdersList.size());
						RedisAPI.setObj(biteName + "Orders", newBiteOrdersList,
								customPropertiesConfig.getRedisSaveTime());
						List<BiteOrders> oldBiteOrdersList = tradeOrders.stream()
								.filter(biteOrder -> newBeginTime >= biteOrder.getDate()).collect(Collectors.toList());
						List<BiteOrders> redisOldBiteOrders = (List<BiteOrders>) RedisAPI
								.getObj(biteName + "OldOrders");
						if (redisOldBiteOrders != null) {
							redisOldBiteOrders.addAll(oldBiteOrdersList);
							RedisAPI.setObj(biteName + "OldOrders", redisOldBiteOrders,
									customPropertiesConfig.getRedisSaveTime());
						} else {
							RedisAPI.setObj(biteName + "OldOrders", oldBiteOrdersList,
									customPropertiesConfig.getRedisSaveTime());
						}
						logger.warn("缓存动态更新结束");
					}

					// 再次获得缓存中的orders（如果时间没有动态更新那么还是原样，如果动态更新了 就改新值）
					List<BiteOrders> newOrders = (List<BiteOrders>) RedisAPI.getObj(biteName + "Orders");
					BiteOrders maxBite = newOrders.stream()
							.collect(Collectors.maxBy(Comparator.comparing(BiteOrders::getPrice))).get();
					BiteOrders minBite = newOrders.stream()
							.collect(Collectors.minBy(Comparator.comparing(BiteOrders::getPrice))).get();
					RedisAPI.setStr(biteName + "highPrice", maxBite.getPrice() + "",
							customPropertiesConfig.getRedisSaveTime());
					RedisAPI.setStr(biteName + "highPriceTime", maxBite.getDate() + "",
							customPropertiesConfig.getRedisSaveTime());
					RedisAPI.setStr(biteName + "lowPrice", minBite.getPrice() + "",
							customPropertiesConfig.getRedisSaveTime());
					RedisAPI.setStr(biteName + "lowPriceTime", minBite.getDate() + "",
							customPropertiesConfig.getRedisSaveTime());
					float divisionPrice = Float.parseFloat(RedisAPI.getStr(biteName + "highPrice"))
							/ Float.parseFloat(RedisAPI.getStr(biteName + "lowPrice"));
					// 三小时跌幅8个点
					if (divisionPrice > customPropertiesConfig.getDropRange()) {
						synchronized (accountKeys) {
							logger.warn("########################################################################");
							logger.warn("币名：" + biteName + "highPrice" + RedisAPI.getStr(biteName + "highPrice"));
							logger.warn("币名：" + biteName + "lowPrice" + RedisAPI.getStr(biteName + "lowPrice"));
							logger.warn("即将进行购买"+biteName+"的操作。。。购买金额为"+RedisAPI.getStr(biteName + "lowPrice"));
							logger.warn("启动购买并监听线程");
							new Thread(new CoinEggBuyThread(biteName,Float.parseFloat(RedisAPI.getStr(biteName + "lowPrice")),customPropertiesConfig,accountKeys)).start();
							//先把开始时间设置成   最低值得的最后时间
							Long addEndTime =Long.parseLong(endTime) - Long.parseLong(RedisAPI.getStr(biteName + "lowPriceTime"));
							RedisAPI.setStr(biteName + "beginTime",
									(Long.parseLong(RedisAPI.getStr(biteName + "lowPriceTime"))
											- customPropertiesConfig.getOrderSaveTime()+addEndTime) + "",
									customPropertiesConfig.getRedisSaveTime());
							logger.warn("########################################################################");
						}
					}
				}
			}
		}

	}
}
