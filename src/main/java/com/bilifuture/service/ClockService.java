package com.bilifuture.service;

import java.util.Map;

public interface ClockService {

	public Map<String,Object> clockIn(String content, String userId);

	public Map<String, Object> clockOut(String content, String userId);
}
