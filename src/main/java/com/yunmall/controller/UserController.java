package com.yunmall.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yunmall.bean.EsFunctionResourceMenuBean;
import com.yunmall.bean.User;
import com.yunmall.jwt.JWTResponseData;
import com.yunmall.jwt.JWTSubject;
import com.yunmall.jwt.JWTUtils;
import com.yunmall.service.EsFunctionResourceService;
import com.yunmall.service.UserService;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@Controller
//@RestController
public class UserController {
    @Reference
    private UserService userService;
    @Reference
	private EsFunctionResourceService esFunctionResourceService;
    @RequestMapping("/user")
    @ResponseBody
	public ModelAndView insertUser(String username,String password) {
    	ModelAndView modelAndView=new ModelAndView();
		User user = userService.findUser(username,password);
		if(user==null){
			modelAndView.setViewName("error");
			return modelAndView;
		}
		modelAndView.setViewName("login");
		return modelAndView;
	}
    
    @RequestMapping("/login")
	public ModelAndView selectUser(String username,String password,HttpSession session) {
    	List<EsFunctionResourceMenuBean> firstMenuList = esFunctionResourceService.selectAllMenu();
		session.setAttribute("menuList", firstMenuList);
		ModelAndView modelAndView=new ModelAndView();
		JWTResponseData jWTResponseData=new JWTResponseData();
		User user = userService.findUser(username,password);
		if(user!=null) {
			JWTSubject subject=new JWTSubject(username);
			String token = JWTUtils.createJWT(UUID.randomUUID().toString(),"jwt_secert", JWTUtils.generalSubject(subject), 60*1000);
			jWTResponseData.setCode(200);
			jWTResponseData.setData(null);
			jWTResponseData.setToken(token);
			modelAndView.addObject("jWTResponseData",jWTResponseData);
		}else {
			
		}
				
		modelAndView.setViewName("user/index");
		return modelAndView;
	}
   
    
    
    
}
