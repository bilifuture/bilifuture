package com.bilifuture.service;

import java.util.List;
import java.util.Map;

public interface ViewService {

	public List<Map<String,Object>> viewByUserId(String userId);
}
