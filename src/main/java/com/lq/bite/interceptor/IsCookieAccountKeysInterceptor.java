package com.lq.bite.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lq.bite.utils.CookieUtils;

/**
 * 未登录用户  cookie中是否存在对应public_key
 * @author l.q
 *
 */
public class IsCookieAccountKeysInterceptor implements HandlerInterceptor {
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
		Cookie cookie = CookieUtils.getCookieByName(req,"publicKey");
		logger.info("cookie========="+cookie);
		if(cookie == null){
			//cookie不存在
			res.sendRedirect(req.getContextPath()+"/page/toIndex");
			return false;
		}
		String publicKey = cookie.getValue();
		logger.info("publicKey========"+publicKey);
		if(StringUtils.isBlank(publicKey)){
			//publicKey不存在
			res.sendRedirect(req.getContextPath()+"/page/toIndex");
			return false;
		}
		req.setAttribute("publicKey", publicKey);
		return true;
	}

}
