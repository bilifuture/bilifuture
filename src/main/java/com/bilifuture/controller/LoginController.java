package com.bilifuture.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bilifuture.service.LoginService;

@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping("/login")
	@ResponseBody
	public Map<String,Object> login(String userId,String pwd){
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String remoteAddr = req.getRemoteAddr();
		
		Map<String, Object> map = loginService.login(userId,pwd,remoteAddr);
		logger.info("结果为：" + map);
		return map;
	}
	@RequestMapping("/registry")
	public String registry(String userId,String pwd,String tel,String email){
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String remoteAddr = req.getRemoteAddr();
		Map<String, Object> map = loginService.registry(userId,pwd,tel,email,remoteAddr);
		Integer i = (Integer) map.get("lines");
		if(1 == i) {
			return "redirect:/page/registerSuccess.html";
			
		}
		return "F";
	}
	
	@RequestMapping("/logout")
	public String logout(String userId) {
		loginService.logout(userId);
		return "redirect:/index.html";
	}
	
}
