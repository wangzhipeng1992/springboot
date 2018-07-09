package com.qiyou.persistence.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.qiyou.basic.Result;
import com.qiyou.util.HttpClientHelper;

@Service
public class HttpTelnetService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String URL = "http://218.22.14.66:8082/yzt/toHandleQuery.do?id=ZGVncG9xcXBvcnlxd2h2anVK&uniScID=amJqcG91cXBvenFwcmhzY3JP&jumpFlag=false";
    
    public Result httpTelnet() throws Exception{
        Result result = new Result();
        String reStr = HttpClientHelper.get(URL);
        logger.info("调用结果返回     reStr={}", reStr);
        result.setResultData(reStr);
        return result;
     }
}
