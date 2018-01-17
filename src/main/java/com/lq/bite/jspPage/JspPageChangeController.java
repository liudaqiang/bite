package com.lq.bite.jspPage;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lq.bite.allApi.CoinEggAPI;
import com.lq.bite.common.Constant;
import com.lq.bite.dao.AllIcoDao;
import com.lq.bite.entity.AccountKeys;
import com.lq.bite.entity.CleanBite;
import com.lq.bite.service.AccountKeysService;
import com.lq.bite.utils.CookieUtils;
import com.lq.bite.utils.StringUtils;

/**
 * 页面跳转Controller
 * @author l.q
 *
 */
@RequestMapping("page")
@Controller
public class JspPageChangeController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());  
	
	@Autowired
	private AccountKeysService accountKeysService;
	@Autowired
	private AllIcoDao allIcoDao;
	
	@RequestMapping("/toLogin")
	public String toLogin(){
		return "login";
	}
	@RequestMapping("/toIndex")
	public String toIndex(HttpServletRequest request,Model model,HttpServletResponse response){
		//校验cookie是否存在 或是否有效
		Cookie cookie = CookieUtils.getCookieByName(request, "publicKey");
		if(cookie == null){
			model.addAttribute("message", Constant.NO_ACCOUNT_KEYS);
			return "saveAccount"; 
		}
		String publicKey = cookie.getValue();
		if(StringUtils.isBlank(publicKey)){
			model.addAttribute("message", Constant.NO_ACCOUNT_KEYS);
			return "saveAccount";
		}
		//查询数据库中是否存在
		AccountKeys accountKeys = new AccountKeys();
		accountKeys.setPublicKey(publicKey);
		accountKeys.setIsRight("0");
		List<AccountKeys> accountKeysList = accountKeysService.get(accountKeys);
		if(accountKeysList == null || accountKeysList.size() == 0){
			//销毁该cookie
			CookieUtils.setCookie(response, "publicKey", null, 0);
			model.addAttribute("message", Constant.NO_SQL_ACCOUNT_KEYS);
			return "saveAccount";
		}
		//连接测试是否有效
		if(!CoinEggAPI.accountValid(accountKeysList.get(0))){
			//销毁该cookie
			CookieUtils.setCookie(response, "publicKey", null, 0);
			//设置publicKey无效
			accountKeysService.delete(accountKeysList.get(0).getId());
			model.addAttribute("message", Constant.NOT_VALID_ACCOUNT_KEYS);
			return "saveAccount";
		}
		return "accountInfo";
	}
	
	@RequestMapping("toRegister")
	public String toRegister(){
		return "register";
	}
	@RequestMapping("toAccountInfo")
	public String toAccountInfo(AccountKeys accountKeys,Model model){
		model.addAttribute("accountKeys", accountKeys);
		return "accountInfo";
	}
	
	@RequestMapping("toBuy")
	public String toBuy(Model model){
		List<CleanBite> all = allIcoDao.getAll();
		logger.info("bite length----"+all.size());
		List<String> biteNames = all.stream().map(CleanBite::getBiteName).collect(Collectors.toList());
		model.addAttribute("biteNames", biteNames);
		return "buy";
	}
}
