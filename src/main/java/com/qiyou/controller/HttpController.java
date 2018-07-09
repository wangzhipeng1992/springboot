package com.qiyou.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qiyou.basic.Result;
import com.qiyou.persistence.service.HttpTelnetService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "http", produces = MediaType.APPLICATION_JSON_VALUE)
public class HttpController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpTelnetService httpTelnetService;

    @RequestMapping(path = "/httpTelnet", method = RequestMethod.POST)
    @ApiOperation(value = "http1.1", notes = "httpTelnet")
    public Result httpTelnet() throws Exception {
        logger.info("httpTelnet 进行访问url测试");
        Result result = httpTelnetService.httpTelnet();
        return result;
    }

}
