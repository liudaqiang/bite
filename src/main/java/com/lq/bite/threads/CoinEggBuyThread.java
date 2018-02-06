package com.lq.bite.threads;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lq.bite.allApi.CoinEggAPI;
import com.lq.bite.allInterface.CoinEggInterface;
import com.lq.bite.entity.CoinEggEntity;
import com.lq.bite.entity.CoinEggTrade;
import com.lq.bite.entity.IcoData;
import com.lq.bite.utils.HttpClientUtil;
import com.lq.bite.utils.RedisAPI;

public class CoinEggBuyThread implements Runnable {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private IcoData icoData;

	public CoinEggBuyThread(IcoData icoData) {
		this.icoData = icoData;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// 买1单
		//BigDecimal   decimalBuy   =   new   BigDecimal(Double.parseDouble(icoData.getBuy()));  
		//logger.warn(icoData.getBuy()+"买1价格");
		//Float buy   =   Float.parseFloat(decimalBuy.setScale(7,   BigDecimal.ROUND_HALF_UP).doubleValue()+"");
		Double buy = Double.parseDouble(icoData.getBuy());
		buy += 0.00000001;
		String buyStr = String.format(" %.8f  ",buy); 
		logger.warn(buyStr+"我的买价格");
		
		BigDecimal   b   =   new   BigDecimal(0.0022 / buy);  
		Float amount   =   Float.parseFloat(b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue()+"");  
		CoinEggTrade cet = new CoinEggTrade();
		cet.setAmount(amount);
		cet.setPrice(Float.parseFloat(buyStr));
		cet.setType("buy");
		cet.setCoin(icoData.getCodeName());
		logger.warn("正在进行ETH挂单买操作---");
		logger.warn("ETH购买单价："+buy);
		logger.warn("ETH购买数量："+amount);
		CoinEggEntity cee = tradeAdd(cet);
		logger.warn(cee.toString());
		boolean tradeFlag = true;
		while (tradeFlag) {
			logger.warn(cee.toString());
			if (cee.isResult()) {
				logger.warn("挂单成功，将监控挂单是否完成操作");
				tradeFlag = false;
				logger.warn("您应该挂的卖单为"+(Float.parseFloat(icoData.getBuy())*1.0045-0.00000001));
				boolean robBuy1 = true;
				while(robBuy1){
					String json = HttpClientUtil.get(CoinEggInterface.TICKER+"?coin=eth");
					IcoData icoData1 = JSON.parseObject(json, IcoData.class);
					icoData1.setCodeName(icoData.getCodeName());
					logger.warn("最新的价格:"+icoData1.getBuy()+"    我的价格:"+buyStr);
					if(!icoData1.getBuy().equals(buyStr)){
						CoinEggEntity tradeCancel = tradeCancel(cee.getId(),icoData1.getCodeName());
						while(!tradeCancel.isResult()){
							logger.warn("取消委托失败");
							tradeCancel = tradeCancel(cee.getId(),icoData1.getCodeName());
							logger.warn("1秒后继续取消委托");
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						logger.warn("取消委托成功");
						robBuy1 = false;
						RedisAPI.setStr("CoinEggBuy"+icoData.getCodeName(), "false", 86400);
					}else{
						logger.warn("2秒后继续检查    买1单是否被顶掉");
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}else{
				logger.warn("挂单失败");
				tradeFlag = false;
			}
		}
	}

	public CoinEggEntity tradeAdd(CoinEggTrade cet) {
		return CoinEggAPI.tradeAdd(cet);
	}
	public CoinEggEntity tradeCancel(int id,String biteName){
		return CoinEggAPI.tradeCancel(id+"", biteName);
	}
}
