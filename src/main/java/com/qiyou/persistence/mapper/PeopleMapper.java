package com.qiyou.persistence.mapper;

import java.util.Map;

import com.qiyou.persistence.model.People;
import com.qiyou.util.MyMapper;

public interface PeopleMapper extends MyMapper<People> {

	int peopleInsertXml(Map<String, Object> peopleInfo);
	
}
