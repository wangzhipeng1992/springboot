package com.qiyou.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class JenkinsController {

    @RequestMapping(path = "/jenkins", method = RequestMethod.POST)
    @ApiOperation(value = "jenkins 1.1", notes = "Jenkins自动部署测试一")
    public String JenkinsName() {
        return "JenkinsBuildAutoMatic";
    }
    
}
