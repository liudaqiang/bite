package com.lq.bite.allApi;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lq.bite.allEncode.CoinEggSha256;
import com.lq.bite.allInterface.CoinEggInterface;
import com.lq.bite.entity.AccountKeys;
import com.lq.bite.entity.BiteOrders;
import com.lq.bite.entity.CleanBite;
import com.lq.bite.entity.CoinEggEntity;
import com.lq.bite.entity.CoinEggTrade;
import com.lq.bite.utils.HttpClientUtil;
import com.lq.bite.utils.RedisAPI;

/**
 * 币蛋网站API
 * @author l.q
 *
 */
public class CoinEggAPI {
	private static Logger logger = LoggerFactory.getLogger(CoinEggAPI.class);
	/**
	 * 校验账号是否有效
	 * @return
	 */
	public static boolean accountValid(AccountKeys accountKeys){
		String signature = CoinEggSha256.personSign(accountKeys.getPublicKey(), accountKeys.getPrivateKey());
		String result = null;
		Map<String,Object> map = new LinkedHashMap<>();
		map.put("key", accountKeys.getPublicKey());
		map.put("signature", signature);
		map.put("nonce",123456);
		try {
			result = HttpClientUtil.post(CoinEggInterface.BALANCE, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		try{
			CoinEggEntity ce = JSON.parseObject(result,CoinEggEntity.class );
			if(ce != null && ce.result){
				return true;
			}
			return false;
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * 查询账号
	 * @param accountKeys
	 * @return
	 */
	public static CoinEggEntity account(AccountKeys accountKeys){
		String signature = CoinEggSha256.personSign(accountKeys.getPublicKey(), accountKeys.getPrivateKey());
		Map<String,Object> map = new LinkedHashMap<>();
		map.put("key", accountKeys.getPublicKey());
		map.put("signature", signature);
		map.put("nonce",123456);
		String result = null;
		try {
			result = HttpClientUtil.post(CoinEggInterface.BALANCE, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		try{
			return JSON.parseObject(result,CoinEggEntity.class );
			
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * 下单
	 * @param accountKeys
	 * @return
	 */
	public static CoinEggEntity tradeAdd(CoinEggTrade coinEggTrade){
		StringBuffer sbSign = new StringBuffer();
		sbSign.append("&amount=");
		sbSign.append(coinEggTrade.getAmount());
		sbSign.append("&price=");
		sbSign.append(coinEggTrade.getPrice());
		sbSign.append("&type=");
		sbSign.append(coinEggTrade.getType());
		sbSign.append("&coin=");
		sbSign.append(coinEggTrade.getCoin());
		System.out.println("sbSign---"+sbSign);
		String signature = CoinEggSha256.commonSign(coinEggTrade.getPublicKey(), coinEggTrade.getPrivateKey(),sbSign.toString());
		Map<String,Object> map = new LinkedHashMap<>();
		map.put("key", coinEggTrade.getPublicKey());
		map.put("signature", signature);
		map.put("nonce",123456);
		map.put("amount",coinEggTrade.getAmount());
		map.put("price",coinEggTrade.getPrice());
		map.put("type",coinEggTrade.getType());
		map.put("coin",coinEggTrade.getCoin());
		String result = null;
		try {
			result = HttpClientUtil.post(CoinEggInterface.TRADE_ADD, map);
			//result = HttpRequestUtils.sendPOSTRequest(CoinEggInterface.TRADE_ADD,sb.toString(),"utf-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("error:"+e.getMessage());
			return null;
		}
		try{
			return JSON.parseObject(result,CoinEggEntity.class );
		}catch(Exception e){
			logger.error("error:"+e.getMessage());
			return null;
		}
	}
	/**
	 * 委托列表
	 * @param coinEggTrade
	 * @param biteName
	 * @return
	 */
	public static CoinEggEntity tradeList(CoinEggTrade coinEggTrade,String biteName){
		StringBuffer sbSign = new StringBuffer();
		sbSign.append("&since=0");
		sbSign.append("&coin="+biteName);
		sbSign.append("&type=all");
		String signature = CoinEggSha256.commonSign(coinEggTrade.getPublicKey(), coinEggTrade.getPrivateKey(),sbSign.toString());
		
		Map<String,Object> map = new LinkedHashMap<>();
		map.put("key", coinEggTrade.getPublicKey());
		map.put("signature", signature);
		map.put("nonce",123456);
		map.put("since",0);
		map.put("coin",biteName);
		map.put("type","all");
		
		String result = null;
		try {
			result = HttpClientUtil.post(CoinEggInterface.TRADE_LIST, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("请求出现异常");
			return null;
		}
		try{
			return  JSON.parseObject(result, CoinEggEntity.class);
		}catch(Exception e){
			logger.error("请求结束，转换出现异常");
			return null;
		}
	}
	/**
	 * 取消委托
	 * @param accountKeys
	 * @param id
	 * @param biteName
	 * @return
	 */
	public static CoinEggEntity tradeCancel(AccountKeys accountKeys,String id,String biteName){
		StringBuffer sbSign = new StringBuffer();
		sbSign.append("&id="+id);
		sbSign.append("&coin="+biteName);
		String signature = CoinEggSha256.commonSign(accountKeys.getPublicKey(), accountKeys.getPrivateKey(),sbSign.toString());
		
		Map<String,Object> map = new LinkedHashMap<>();
		map.put("key", accountKeys.getPublicKey());
		map.put("signature", signature);
		map.put("nonce",123456);
		map.put("id",id);
		map.put("coin",biteName);
		
		String result = null;
		try {
			result = HttpClientUtil.post(CoinEggInterface.TRADE_CANCEL, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("请求出现异常");
			return null;
		}
		try{
			return  JSON.parseObject(result, CoinEggEntity.class);
		}catch(Exception e){
			logger.error("请求结束，转换出现异常");
			return null;
		}
	}
	/**
	 * 交易列表
	 * @param biteName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<BiteOrders> tradeOrders(String biteName){
		if(biteName == null){
			return null;
		}
		List<CleanBite> allIco  = (List<CleanBite>)RedisAPI.getObj("allIco");
		if(allIco == null || !allIco.stream().map(CleanBite::getBiteName).anyMatch(name -> name.equals(biteName))){
			return null;
		}
		String result = null;
		try {
			result = HttpClientUtil.get(CoinEggInterface.ORDERS+"?coin="+biteName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("请求结束，转换出现异常");
			return null;
		}
		try{
			return  JSON.parseArray(result, BiteOrders.class);
		}catch(Exception e){
			logger.error("请求结束，转换出现异常");
			return null;
		}
	}
}
