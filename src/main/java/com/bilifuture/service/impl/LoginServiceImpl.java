package com.bilifuture.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.bilifuture.dao.LoginDao;
import com.bilifuture.service.LoginService;
import com.bilifuture.utils.MailUtil;

import io.netty.util.internal.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Service(version="1.0.0")
@org.springframework.stereotype.Service("loginServiceImpl")
public class LoginServiceImpl implements LoginService {

	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Autowired
	private LoginDao loginDao;
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	/**
	 * 登录服务，从redis中获取，如果没有，使用双重检验锁
	 * */
	@Override
	public Map<String, Object> login(String userId, String pwd, String remoteAddr) {
		Map<String, Object> user = null;
		String userString = redisTemplate.opsForValue().get(userId);
		if (StringUtil.isNullOrEmpty(userString)) {
			synchronized (this) {
				userString = redisTemplate.opsForValue().get(userId);
				if (StringUtil.isNullOrEmpty(userString)) {
					user = loginDao.queryUser(userId,pwd);
					if (user != null){
						String json = JSON.toJSONString(user);
						redisTemplate.opsForValue().set(userId, json,10,TimeUnit.MINUTES);
						user.put("flag", "S");
						return user;
					}
				}
			}
		}else {
			logger.info("请求的是redis，走的缓存");
			user = JSON.parseObject(userString, HashMap.class);
		}
		
		if (user == null) {
			user = new HashMap<String,Object>();
			user.put("flag", "F");
		}else {
			user.put("flag", "S");
		}
		return user;
	}
	
	@Override
	public Map<String, Object> registry(String userId, String pwd, String tel, String email, String remoteAddr) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("pwd", pwd);
		param.put("tel", tel);
		param.put("email", email);
		param.put("remoteAddr", remoteAddr);
		
		int i = 0;
		try {
			i = loginDao.insertUser(param);
			MailUtil mailUtil = new MailUtil("smtp.163.com", "13838099126@163.com", "zhang1992");
			String subject = "bilifuture注册成功";
			String sendHtml = "恭喜注册成为bilifuture的一员。";
			String[] receiveUser = {email};
			String[] receiveUserCS = {"13838099126@163.com"};
			String[] receiveUserMS = {};
			File attachment = null;
			mailUtil.doSendHtmlEmail(subject , sendHtml , receiveUser, receiveUserCS, receiveUserMS , attachment );
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		Map<String,Object> re = new HashMap<String,Object>();
		re.put("lines", i);
		return re;
	}

	@Override
	public void logout(String userId) {
		Boolean delete = redisTemplate.delete(userId);
	}

}
