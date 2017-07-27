package com.lianlian.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class TestController {

	@RequestMapping("/hello")
	@ResponseBody
	public String hello() {
		return "hello spring mvc!";
	}
}
