package com.lq.bite.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lq.bite.entity.AccountKeys;
import com.lq.bite.utils.RedisAPI;

/**
 * 未登录用户  cookie中是否存在对应public_key
 * @author l.q
 *
 */
@Component
public class IsAccountKeysInterceptor implements HandlerInterceptor {
	
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object obj) throws Exception {
		Object objKeys = RedisAPI.getObj(req.getSession().getAttribute("userName").toString());
		if(objKeys == null){
			logger.info("accountKeys不存在");
			//public Key不存在
			res.sendRedirect(req.getContextPath()+"/page/toIndex");
			return false;
		}
		if(!(objKeys instanceof AccountKeys)){
			logger.info("publicKey存在但是转换失败");
			res.sendRedirect(req.getContextPath()+"/page/toIndex");
			return false;
		}
		req.setAttribute("accountKeys", objKeys);
		return true;
	}

}
