package com.lyj.controller;

import com.lyj.entity.Counter;
import com.lyj.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("counter")
public class CounterController {
    @Autowired
    private CounterService counterService;

    @RequestMapping("selCounterByUid")
    public Map selCourseByUid(String uid){
        Map map = new HashMap();
        Counter course = new Counter();
        course.setUser_id(uid);
        List<Counter> counters = counterService.selCounterByChange(course);
        map.put("status","200");
        map.put("counters",counters);
        return map;
    }


    @RequestMapping("save")
    public Map save(Counter counter){
        Map map = new HashMap();
        counterService.save(counter);
        List<Counter> counters = counterService.queryAll();
        map.put("status","200");
        map.put("counters",counters);
        return map;
    }

    @RequestMapping("delete")
    public Map delete(String uid,Counter counter){
        Map map = new HashMap();
        counterService.delete(counter);
        List<Counter> counters = counterService.queryAll();
        map.put("status","200");
        map.put("counters",counters);
        return map;
    }

    @RequestMapping("update")
    public Map update(Counter counter){
        Map map = new HashMap();
        counterService.update(counter);
        List<Counter> counters = counterService.queryAll();
        map.put("status","200");
        map.put("counters",counters);
        return map;
    }
}
