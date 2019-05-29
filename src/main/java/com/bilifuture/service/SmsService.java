package com.bilifuture.service;

import java.util.Map;

public interface SmsService {

	public Map<String,Object> tenecentSmsSend(String phoneNum);
}
