package com.lq.bite.allApi;

import com.alibaba.fastjson.JSON;
import com.lq.bite.allEncode.CoinEggSha256;
import com.lq.bite.allInterface.CoinEggInterface;
import com.lq.bite.entity.AccountKeys;
import com.lq.bite.entity.CoinEggEntity;
import com.lq.bite.entity.CoinEggTrade;
import com.lq.bite.utils.HttpRequestUtils;

/**
 * 币蛋网站API
 * @author l.q
 *
 */
public class CoinEggAPI {
	/**
	 * 校验账号是否有效
	 * @return
	 */
	public static boolean accountValid(AccountKeys accountKeys){
		String signature = CoinEggSha256.personSign(accountKeys.getPublicKey(), accountKeys.getPrivateKey());
		StringBuffer sb = new StringBuffer();
		sb.append("key=");
		sb.append(accountKeys.getPublicKey());
		sb.append("&signature=");
		sb.append(signature);
		sb.append("&nonce=");
		sb.append(123456);
		String result = null;
		try {
			result = HttpRequestUtils.sendPOSTRequest(CoinEggInterface.BALANCE,sb.toString(),"utf-8");
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
		StringBuffer sb = new StringBuffer();
		sb.append("key=");
		sb.append(accountKeys.getPublicKey());
		sb.append("&signature=");
		sb.append(signature);
		sb.append("&nonce=");
		sb.append(123456);
		String result = null;
		try {
			result = HttpRequestUtils.sendPOSTRequest(CoinEggInterface.BALANCE,sb.toString(),"utf-8");
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
		StringBuffer sb = new StringBuffer();
		sb.append("key=");
		sb.append(coinEggTrade.getPublicKey());
		sb.append("&signature=");
		sb.append(signature);
		sb.append("&nonce=");
		sb.append(123456);
		sb.append("&amount=");
		sb.append(coinEggTrade.getAmount());
		sb.append("&price=");
		sb.append(coinEggTrade.getPrice());
		sb.append("&type=");
		sb.append(coinEggTrade.getType());
		sb.append("&coin=");
		sb.append(coinEggTrade.getCoin());
		String result = null;
		try {
			result = HttpRequestUtils.sendPOSTRequest(CoinEggInterface.TRADE_ADD,sb.toString(),"utf-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("下单请求出现异常");
			return null;
		}
		try{
			return JSON.parseObject(result,CoinEggEntity.class );
		}catch(Exception e){
			System.out.println("下单请求结束，转换出现异常");
			return null;
		}
	}
}
