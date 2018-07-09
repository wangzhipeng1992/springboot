/**
 * Project Name:iask
 * File Name:AdminController.java
 * Package Name:com.crisis.controller
 * Date:2016-9-9下午1:40:59
 * Copyright (c) 2016, WONDERS INFORMATION CO., LTD All Rights Reserved.
 *
 */

package com.qiyou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// 页面跳转Controller
@Controller
@RequestMapping("/link")
public class LinkController {

	@RequestMapping("/index")
	public String link() {
		return "link/index";
	}

}