package com.lq.bite.jspPage;

import java.util.List;
import java.util.stream.Collectors;

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
		String userName = request.getSession().getAttribute("userName")+"";
		AccountKeys accountKeys = new AccountKeys();
		accountKeys.setUserName(userName);
		List<AccountKeys> list = accountKeysService.get(accountKeys);
		if(list.size() == 0){
			model.addAttribute("message", Constant.NOT_VALID_ACCOUNT_KEYS);
			return "saveAccount";
		}
		//连接测试是否有效
		if(!CoinEggAPI.accountValid(list.get(0))){
			//设置publicKey无效
			accountKeysService.delete(list.get(0).getId());
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
	
	@RequestMapping("toTradeList")
	public String toTradeList(Model model){
		return "tradeList";
	}
}
