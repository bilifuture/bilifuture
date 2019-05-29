package com.bilifuture.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bilifuture.service.ClockService;

/**
 * @author ForWx
 *	
 */
@Controller
public class ClockController {

	@Autowired
	private ClockService clockService;
	
	@RequestMapping("/clockIn")
	@ResponseBody
	public Map<String,Object> clockIn(String content,String userId){
		Map<String, Object> re = clockService.clockIn(content,userId);
		return re;
	}
	
	@RequestMapping("/clockOut")
	@ResponseBody
	public Map<String,Object> clockOut(String content,String userId){
		Map<String, Object> re = clockService.clockOut(content,userId);
		return re;
	}
	
}
