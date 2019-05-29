package com.bilifuture.service;

import java.util.Map;

public interface LoginService {
	public Map<String,Object> login(String userId, String pwd, String remoteAddr);

	public Map<String, Object> registry(String userId, String pwd, String tel, String email, String remoteAddr);

	public void logout(String userId);
}
