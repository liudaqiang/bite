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
import com.lq.bite.common.ReflectClass;
import com.lq.bite.entity.AccountKeys;
import com.lq.bite.entity.CleanBite;
import com.lq.bite.entity.CoinEggEntity;
import com.lq.bite.service.AccountKeysService;
import com.lq.bite.utils.RedisAPI;
import com.lq.bite.utils.StringUtils;

@RestController
@RequestMapping("account")
public class AccountKeysController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());  
	
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
			return returnFaild(Constant.EXCEPTION_PARAM, Constant.FAILD_PARAM);
		}
		try {
			accountKeysService.deleteByPublicKey(accountKeys);
			accountKeys.setIsRight("0");
			accountKeys.setUserName(request.getSession().getAttribute("userName")+"");
			accountKeysService.insert(accountKeys);
			// 存入redis 保存10天
			logger.info("改用户keys 存入redis");
			RedisAPI.setObj(request.getSession().getAttribute("userName")+"", accountKeys, 864000);
			return returnSuccess(accountKeys, Constant.SUCCESS_INSERT);
		} catch (Exception e) {
			logger.error("error："+e.getMessage());
			return returnFaild(Constant.EXCEPTION_ERROR, Constant.FAILD_INSERT);
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
			logger.info("参数错误");
			return returnFaild(Constant.EXCEPTION_PARAM, Constant.FAILD_PARAM);
		}
		try {
			if (CoinEggAPI.accountValid(accountKeys)) {
				logger.info("校验成功");
				return returnSuccess(accountKeys, Constant.VALID_ACCOUNT_SUCCESS);
			}
			return returnFaild(Constant.EXCEPTION_VOLID, Constant.VALID_ACCOUNT_FAILD);
		} catch (Exception e) {
			logger.error("error:"+e.getMessage());
			return returnFaild(Constant.EXCEPTION_ERROR, Constant.VALID_ACCOUNT_EXCEPTION);
		}
	}
	/**
	 * 获取用户信息
	 * @param accountKeys
	 * @param response
	 * @return
	 */
	@RequestMapping("getUserInfo")
	public Map<String,Object> getUserInfo(AccountKeys accountKeys,HttpServletResponse response,HttpServletRequest request){
		//查询数据库中是否存在
		accountKeys.setIsRight("0");
		accountKeys.setUserName(request.getSession().getAttribute("userName")+"");
		try{
			List<AccountKeys> accountKeysList = accountKeysService.get(accountKeys);
			if(accountKeysList == null || accountKeysList.size() == 0){
				logger.info("该userName没有录入accountKeys");
				//销毁该cookie
				return returnFaild(Constant.EXCEPTION_NOT_ACCOUNT_KEYS, Constant.ACCOUNT_MESSAGE_FAILD);
			}
			CoinEggEntity cee = CoinEggAPI.account(accountKeysList.get(0));
			if(cee == null){
				logger.info("该用户userName录入了accountKeys但是已经失效");
				return returnFaild(Constant.EXCEPTION_NOT_ACCOUNT_KEYS, Constant.ACCOUNT_MESSAGE_FAILD);
			}else{
				ReflectClass rc = new ReflectClass();
				List<CleanBite> cleanBiteList = rc.reflectCoinEgg(cee.getData());
//				for(int i=0;i<cleanBiteList.size();i++){
//					allIcoDao.insert(cleanBiteList.get(i));
//				}
				List<CleanBite> filterBiteList = cleanBiteList.stream().filter(cleanBite->Double.parseDouble(cleanBite.getBlance())>0 || Double.parseDouble(cleanBite.getLock())>0).collect(Collectors.toList());
				return returnSuccess(filterBiteList,Constant.ACCOUNT_MESSAGE_SUCCESS);
			}
		}catch(Exception e){
			logger.error("error:"+e.getMessage());
			return returnFaild(Constant.EXCEPTION_ERROR,Constant.CONNECT_ADMIN);
		}
	}
	
}
