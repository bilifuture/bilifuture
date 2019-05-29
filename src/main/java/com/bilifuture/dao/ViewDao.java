package com.bilifuture.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ViewDao {

	@Select("SELECT * FROM `user_clcok_content` WHERE userId = #{userId}")
	List<Map<String, Object>> queryClockContentByUserId(String userId);

}
