package com.qiyou.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qiyou.basic.Result;
import com.qiyou.persistence.service.PeopleService;
import com.qiyou.util.JsonHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "pelple", produces = MediaType.APPLICATION_JSON_VALUE)
public class PeopleController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PeopleService peopleService;

	@RequestMapping(path = "/peopleInsert", method = RequestMethod.POST)
	@ApiOperation(value = "people1.1", notes = "peopleInsert")
	public Result peopleInsert() throws Exception{
		Result result = peopleService.peopleInsert();
		return result;
	}
	
	@RequestMapping(path = "peopleInsertXml", method = RequestMethod.POST)
	@ApiOperation(value = "people1.2", notes = "peoplerInsertXml")
	public Result peopleInserXml(@RequestParam("bizData") String bizData) throws Exception{
		logger.info("peopleInsertXml传入参数 bizData={}", bizData);
		Result result = peopleService.peopleInsertXml(JsonHelper.parseToMap(bizData));
		return result;
	}

}
