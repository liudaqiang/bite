package com.lq.bite.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lq.bite.allApi.CoinEggAPI;
import com.lq.bite.base.BaseController;
import com.lq.bite.common.Constant;
import com.lq.bite.dao.AllIcoDao;
import com.lq.bite.entity.AccountKeys;
import com.lq.bite.entity.CleanBite;
import com.lq.bite.entity.CoinEggEntity;
import com.lq.bite.entity.CoinEggTrade;
import com.lq.bite.service.AccountKeysService;
import com.lq.bite.utils.RedisAPI;
import com.lq.bite.utils.StringUtils;

/**
 * 委托 买单/卖单
 * 
 * @author l.q
 *
 */
@RestController
@RequestMapping("entrust")
public class EntrustController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AllIcoDao allIcoDao;
	@Autowired
	private AccountKeysService accountKeysService;

	/**
	 * 委托下单 (买单/卖单)
	 * 
	 * @return
	 */
	@RequestMapping("entrustDown")
	public Map<String, Object> entrustDown(HttpServletResponse response, CoinEggTrade coinEggTrade,
			HttpServletRequest request) {
		if (!StringUtils.isALLNotBlank(coinEggTrade.getAmount() + "", coinEggTrade.getCoin(),
				coinEggTrade.getPrice() + "", coinEggTrade.getType())) {
			logger.info("参数不正确");
			return returnFaild(Constant.EXCEPTION_PARAM, Constant.FAILD_PARAM);
		}
		if (coinEggTrade.getPrice() < 0.0000000001 || coinEggTrade.getAmount() < 0.01) {
			logger.info("price 和amount 传入有问题");
			return returnFaild(Constant.EXCEPTION_VOLID, Constant.ENTRUST_NUM_LESS);
		}
		if (coinEggTrade.getPrice() * coinEggTrade.getAmount() < 0.001) {
			return returnFaild(Constant.EXCEPTION_VOLID, Constant.ENTRUST_ALL_PRICE_LESS);
		}
		if (!"buy".equals(coinEggTrade.getType()) && !"sell".equals(coinEggTrade.getType())) {
			logger.info("type 传入有问题");
			return returnFaild(Constant.EXCEPTION_VOLID, Constant.ENTRUST_TYPE_ERROR);
		}
		List<CleanBite> all = allIcoDao.getAll();
		List<String> bitrStrs = all.stream().map(CleanBite::getBiteName).collect(Collectors.toList());
		if (!bitrStrs.contains(coinEggTrade.getCoin())) {
			logger.info("coin 不存在");
			return returnFaild(Constant.ENTRUST_COIN_TYPE_NOT_EXISTS, Constant.ENTRUST_COIN_TYPE_NOT_EXISTS);
		}
		AccountKeys accountKeys = (AccountKeys) RedisAPI.getObj(request.getSession().getAttribute("userName") + "");
		String publicKey = accountKeys.getPublicKey();
		logger.info("publicKey-------" + publicKey);
		try {
			List<AccountKeys> accountKeysList = accountKeysService.get(accountKeys);
			if (accountKeysList == null || accountKeysList.size() == 0) {
				logger.info("publicKey  失效");
				return returnFaild(Constant.EXCEPTION_NOT_ACCOUNT_KEYS, Constant.NO_SQL_ACCOUNT_KEYS);
			}
			CoinEggEntity cee = CoinEggAPI.account(accountKeysList.get(0));
			if (cee == null) {
				logger.info("publicKey不正确或用户失效");
				return returnFaild(Constant.EXCEPTION_NOT_ACCOUNT_KEYS, Constant.ACCOUNT_MESSAGE_FAILD);
			} else {
				coinEggTrade.setPublicKey(accountKeysList.get(0).getPublicKey());
				coinEggTrade.setPrivateKey(accountKeysList.get(0).getPrivateKey());
				CoinEggEntity coinEggEntity = CoinEggAPI.tradeAdd(coinEggTrade);
				logger.info(coinEggEntity.toString());
				return returnSuccess(coinEggEntity, Constant.ENTRUST_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("error:" + e.getMessage());
			return returnFaild(Constant.EXCEPTION_ERROR, Constant.CONNECT_ADMIN);
		}
	}
	/**
	 * 挂单查询
	 * @param coin
	 * @param request
	 * @return
	 */
	@RequestMapping("entrustTradeList")
	public Map<String, Object> entrustTradeList(String coin,HttpServletRequest request) {
		
		return null;
	}
}
