package com.lq.bite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("first")
public class FirstController {
	@RequestMapping("/toIndex")
	public String toIndex(){
		return "first";
	}
}
