package com.qiyou;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.qiyou.basic.Result;
import com.qiyou.persistence.service.PeopleService;
import com.qiyou.util.JsonHelper;

public class PeopleServiceTest extends SpringBootAdminApplicationTests {

    @Autowired
    private PeopleService peopleService;

    @Test
    public void getPeopleInfo() {
        try {
            Result pResult = peopleService.getPeopleInfoFromCache("1");
            System.out.println(JsonHelper.parseToJson(pResult));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
