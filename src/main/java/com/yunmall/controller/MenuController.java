package com.yunmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yunmall.bean.EsFunctionResourceMenuBean;
import com.yunmall.service.EsFunctionResourceService;

import java.util.List;

import javax.servlet.http.HttpSession;

@Controller
public class MenuController {
	
	@Reference
	private EsFunctionResourceService esFunctionResourceService;

	@RequestMapping("/queryInfo")
	public String queryInfo(Model model, HttpSession session, @PathVariable("userid") String userid) {
		List<EsFunctionResourceMenuBean> firstMenuList = esFunctionResourceService.selectAllMenu();
		session.setAttribute("menuList", firstMenuList);
		return "user/index";
	}
}