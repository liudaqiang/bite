package com.lq.bite.jspPage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转Controller
 * @author l.q
 *
 */
@RequestMapping("page")
@Controller
public class JspPageChangeController {
	@RequestMapping("/toIndex")
	public String toIndex(){
		return "first";
	}
}
