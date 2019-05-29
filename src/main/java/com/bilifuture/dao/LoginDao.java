package com.bilifuture.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginDao {

	@Select("SELECT * FROM `user` WHERE userId=#{userId} and pwd=#{pwd}")
	public Map<String,Object> queryUser(String userId, String pwd);

	@Insert("INSERT INTO `user` (userId,pwd,tel,email,lastLoginIp,lastLoginDate,status,remark1,remark2) VALUES (#{userId},#{pwd},#{tel},#{email},#{remoteAddr},NOW(),'0','','')")
	public int insertUser(Map<String, Object> param);
}
