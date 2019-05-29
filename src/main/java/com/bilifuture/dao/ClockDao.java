package com.bilifuture.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClockDao {

	@Insert("INSERT INTO user_clcok_content (userId,content,clockType,time) VALUES (#{userId},#{content},#{type},NOW())")
	int addContent(String content, String userId, String type);

}
