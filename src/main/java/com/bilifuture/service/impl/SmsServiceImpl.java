package com.bilifuture.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.bilifuture.service.SmsService;

@Service
public class SmsServiceImpl implements SmsService{

	@Override
	public Map<String, Object> tenecentSmsSend(String phoneNum) {
		
		DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI28BbjqXnefhR", "XvYNg9fuvr4dYIt3Fhsp15QMHr0QtL");
        IAcsClient client = new DefaultAcsClient(profile);
        
        
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", "13838099126");
        request.putQueryParameter("TemplateCode", "SMS_166486751");
        request.putQueryParameter("SignName", "bilifuture个人");
        request.putQueryParameter("TemplateParam", "{\"code\":\"765567\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI28BbjqXnefhR", "XvYNg9fuvr4dYIt3Fhsp15QMHr0QtL");
        IAcsClient client = new DefaultAcsClient(profile);
        
        
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", "13838099126");
        request.putQueryParameter("TemplateCode", "SMS_166486751");
        request.putQueryParameter("SignName", "bilifuture个人");
        request.putQueryParameter("TemplateParam", "{\"code\":\"765567\"}");
        CommonResponse response = client.getCommonResponse(request);
        System.out.println(response.getData());
	}
	
}
