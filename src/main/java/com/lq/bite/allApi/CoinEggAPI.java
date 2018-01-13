package com.lq.bite.allApi;

import com.alibaba.fastjson.JSON;
import com.lq.bite.allEncode.CoinEggSha256;
import com.lq.bite.allInterface.CoinEggInterface;
import com.lq.bite.entity.AccountKeys;
import com.lq.bite.entity.CoinEggEntity;
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
		String signature = CoinEggSha256.encode(accountKeys.getPublicKey(), accountKeys.getPrivateKey());
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
		String signature = CoinEggSha256.encode(accountKeys.getPublicKey(), accountKeys.getPrivateKey());
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
}
