package com.bilifuture.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.bilifuture.dao.ClockDao;
import com.bilifuture.service.ClockService;

import io.netty.util.internal.StringUtil;

@Service
public class ClockServiceImpl implements ClockService {

	private static final Logger logger = LoggerFactory.getLogger(ClockServiceImpl.class);
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private ClockDao clockDao;
	
	@Override
	public Map<String, Object> clockIn(String content,String userId) {
		//1 clockIn  2 clockOut
		String type = "1";
		Map<String,Object> re = new HashMap<String, Object>();
		logger.info("content" + content);
		logger.info("userId" + userId);
		if (StringUtil.isNullOrEmpty(userId)) {
			re.put("flag", "E");
			re.put("message", "对不起,需要登录后进行打卡操作!!!");
			return re;
		}
		String userString = redisTemplate.opsForValue().get(userId);
		if (StringUtil.isNullOrEmpty(userString)) {
			re.put("flag", "E");
			re.put("message", "对不起"+userId+"的登录信息已失效，请重新登录!!!");
			return re;
		}
		
		try {
			int i = clockDao.addContent(content,userId,type);
			re.put("flag", "S");
			re.put("message", "打卡成功");
			re.put("loginUser", userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return re;
	}

	@Override
	public Map<String, Object> clockOut(String content, String userId) {
		//1 clockIn  2 clockOut
		String type = "2";
		Map<String,Object> re = new HashMap<String, Object>();
		logger.info("content" + content);
		logger.info("userId" + userId);
		if (StringUtil.isNullOrEmpty(userId)) {
			re.put("flag", "E");
			re.put("message", "对不起,需要登录后进行打卡操作!!!");
			return re;
		}
		String userString = redisTemplate.opsForValue().get(userId);
		if (StringUtil.isNullOrEmpty(userString)) {
			re.put("flag", "E");
			re.put("message", "对不起"+userId+"的登录信息已失效，请重新登录!!!");
			return re;
		}
		
		try {
			int i = clockDao.addContent(content,userId,type);
			re.put("flag", "S");
			re.put("message", "结束打卡成功！！！");
			re.put("loginUser", userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return re;
	}

}
