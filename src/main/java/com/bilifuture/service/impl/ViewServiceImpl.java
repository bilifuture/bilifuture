package com.bilifuture.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bilifuture.dao.ViewDao;
import com.bilifuture.service.ViewService;

@Service
public class ViewServiceImpl implements ViewService {

	private static final Logger logger = LoggerFactory.getLogger(ViewServiceImpl.class);
	
	@Autowired
	private ViewDao viewDao;
	
	@Override
	public List<Map<String, Object>> viewByUserId(String userId) {
		
		List<Map<String,Object>> re = viewDao.queryClockContentByUserId(userId);
		logger.info(re.toString());
		return re;
	}

}
