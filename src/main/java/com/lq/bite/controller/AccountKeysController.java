package com.lq.bite.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lq.bite.base.BaseController;
import com.lq.bite.common.Constant;
import com.lq.bite.entity.AccountKeys;
import com.lq.bite.service.AccountKeysService;
import com.lq.bite.utils.CookieUtils;
import com.lq.bite.utils.StringUtils;

@Controller
public class AccountKeysController extends BaseController{
	@Autowired
	private AccountKeysService accountKeysService;
	/**
	 * 保存未登录用户账号
	 * @param accountKeys
	 * @return
	 */
	@RequestMapping("save")
	@ResponseBody
	public Map<String,Object> save(AccountKeys accountKeys,HttpServletRequest request,HttpServletResponse response){
		if(!StringUtils.isALLNotBlank(accountKeys.getPrivateKey(),accountKeys.getPublicKey())){
			return returnFaild(Constant.FAILD_PARAM,Constant.FAILD_PARAM);
		}
		try{
			accountKeysService.deleteByPublicKey(accountKeys);
			accountKeys.setIsRight("0");
			accountKeysService.insert(accountKeys);
			//未登录用户cookie保存1个小时
			CookieUtils.setCookie(response, "publicKey", accountKeys.getPublicKey(), 3600);
			request.getCookies();
			return returnSuccess(accountKeys,Constant.SUCCESS_INSERT); 
		}catch(Exception e){
			return returnFaild(e.getMessage(),Constant.FAILD_INSERT);
		}
	}
	/**
	 * 测试是否有效
	 * @param request
	 * @return
	 */
	@RequestMapping("isUserCookie")
	@ResponseBody
	public Map<String,Object> isUserCookie(HttpServletRequest request){
		Cookie cookie = CookieUtils.getCookieByName(request, "publicKey");
		String publicKey = cookie.getValue();
		//根据publicKey查询密码
		return null;
	}
	
}
