package com.bilifuture.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bilifuture.service.ViewService;

@Controller
public class ViewController {

	@Autowired
	private ViewService viewService;
	
	@RequestMapping("/viewByUserId")
	@ResponseBody
	public List<Map<String,Object>> viewByUserId(String userId){
		
		List<Map<String,Object>> list = viewService.viewByUserId(userId);
		
		return list;
	}
}
