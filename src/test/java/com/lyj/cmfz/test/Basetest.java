package com.lyj.cmfz.test;

import com.lyj.CmfzApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = CmfzApplication.class)
@RunWith(SpringRunner.class)
public class Basetest {
    @Test
    public void firstconn() {
        System.out.println("测试");
    }
}
