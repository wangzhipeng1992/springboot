package com.qiyou.persistence.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qiyou.basic.Result;
import com.qiyou.persistence.mapper.PeopleMapper;
import com.qiyou.persistence.model.People;
import com.qiyou.util.JsonHelper;

@Service
public class PeopleService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PeopleMapper peopleMapper;
	
	@Transactional(rollbackFor = Exception.class)
	public Result peopleInsert() throws Exception {
		Result result = new Result();
		People arg0 = new People();
		arg0.setName("徐慧娟");
		arg0.setSex(0);
		arg0.setAge(25);
		arg0.setHeight(168.3);
		arg0.setWeight(55.2);
		try {
			logger.debug("存储信息参数 peopleInfo={}", JsonHelper.parseToJson(arg0));
			int insert = peopleMapper.insert(arg0);
			if (insert>0) {
				result.setResultData(arg0);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return Result.result("0101", "保存人员信息出现异常");
		}
		return result;
	}
	
	public Result peopleInsertXml(Map<String, Object> peopleInfo) throws Exception {
		Result result = new Result();
		try {
			int num = peopleMapper.peopleInsertXml(peopleInfo);
			if(num>0){
				result.setResultData(peopleInfo);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("XML保存人员信息有误 e={}", e);
			return Result.result("0101", "保存人员信息出现异常");
		}
		return result;
	}

}
