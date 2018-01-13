package com.lq.bite.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lq.bite.allApi.CoinEggAPI;
import com.lq.bite.base.BaseController;
import com.lq.bite.common.Constant;
import com.lq.bite.common.ReflectClass;
import com.lq.bite.entity.AccountKeys;
import com.lq.bite.entity.CleanBite;
import com.lq.bite.entity.CoinEggEntity;
import com.lq.bite.service.AccountKeysService;
import com.lq.bite.utils.CookieUtils;
import com.lq.bite.utils.StringUtils;

@RestController
@RequestMapping("account")
public class AccountKeysController extends BaseController {
	@Autowired
	private AccountKeysService accountKeysService;

	/**
	 * 保存未登录用户账号
	 * 
	 * @param accountKeys
	 * @return
	 */
	@RequestMapping("save")
	public Map<String, Object> save(AccountKeys accountKeys, HttpServletRequest request, HttpServletResponse response) {
		if (!StringUtils.isALLNotBlank(accountKeys.getPrivateKey(), accountKeys.getPublicKey())) {
			return returnFaild(Constant.FAILD_PARAM, Constant.FAILD_PARAM);
		}
		try {
			accountKeysService.deleteByPublicKey(accountKeys);
			accountKeys.setIsRight("0");
			accountKeysService.insert(accountKeys);
			// 未登录用户cookie保存1个小时
			CookieUtils.setCookie(response, "publicKey", accountKeys.getPublicKey(), 3600);
			request.getCookies();
			return returnSuccess(accountKeys, Constant.SUCCESS_INSERT);
		} catch (Exception e) {
			return returnFaild(e.getMessage(), Constant.FAILD_INSERT);
		}
	}

	/**
	 * 测试是否有效
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("checkAccount")
	public Map<String, Object> isUserCookie(AccountKeys accountKeys) {
		if (!StringUtils.isALLNotBlank(accountKeys.getPrivateKey(), accountKeys.getPublicKey())) {
			return returnFaild(Constant.FAILD_PARAM, Constant.FAILD_PARAM);
		}
		try {
			if (CoinEggAPI.accountValid(accountKeys)) {
				return returnSuccess(accountKeys, Constant.VALID_ACCOUNT_SUCCESS);
			}
			return returnFaild(Constant.VALID_ACCOUNT_FAILD, Constant.VALID_ACCOUNT_FAILD);
		} catch (Exception e) {
			return returnFaild(e.getMessage(), Constant.VALID_ACCOUNT_EXCEPTION);
		}
	}
	/**
	 * 获取用户信息
	 * @param accountKeys
	 * @param response
	 * @return
	 */
	@RequestMapping("getUserInfo")
	public Map<String,Object> getUserInfo(AccountKeys accountKeys,HttpServletResponse response){
		if (!StringUtils.isALLNotBlank(accountKeys.getPublicKey())) {
			return returnFaild(Constant.FAILD_PARAM, Constant.FAILD_PARAM);
		}
		//查询数据库中是否存在
		accountKeys.setIsRight("0");
		try{
			List<AccountKeys> accountKeysList = accountKeysService.get(accountKeys);
			if(accountKeysList == null || accountKeysList.size() == 0){
				//销毁该cookie
				CookieUtils.setCookie(response, "publicKey", null, 0);
			}
			CoinEggEntity cee = CoinEggAPI.account(accountKeysList.get(0));
			if(cee == null){
				System.out.println("失败");
				return returnFaild(Constant.ACCOUNT_MESSAGE_FAILD, Constant.ACCOUNT_MESSAGE_FAILD);
			}else{
				ReflectClass rc = new ReflectClass();
				List<CleanBite> cleanBiteList = rc.reflectCoinEgg(cee.getData());
				List<CleanBite> filterBiteList = cleanBiteList.stream().filter(cleanBite->Double.parseDouble(cleanBite.getBlance())>0 || Double.parseDouble(cleanBite.getLock())>0).collect(Collectors.toList());
				return returnSuccess(filterBiteList,Constant.ACCOUNT_MESSAGE_SUCCESS);
			}
		}catch(Exception e){
			return returnFaild(e.getMessage(),Constant.CONNECT_ADMIN);
		}
		
	}
}
