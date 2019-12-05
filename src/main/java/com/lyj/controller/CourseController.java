package com.lyj.controller;

import com.lyj.entity.Course;
import com.lyj.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping("selCourseByUid")
    public Map selCourseByUid(String uid){
        Map map = new HashMap();
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setUser_id(uid);
        courses = courseService.selCourseByChange(course);
        course.setUser_id(null);
        course.setLevel("0");
        for (Course course1 : courseService.selCourseByChange(course)) {
            courses.add(course1);
        }
        map.put("status","200");
        map.put("courses",courses);
        return map;
    }


    @RequestMapping("save")
    public Map save(Course course){
        Map map = new HashMap();
        courseService.save(course);
        List<Course> courses = courseService.queryAll();
        map.put("status","200");
        map.put("courses",courses);
        return map;
    }

    @RequestMapping("delete")
    public Map delete(String uid,Course course){
        Map map = new HashMap();
        courseService.delete(course);
        List<Course> courses = courseService.queryAll();
        map.put("status","200");
        map.put("courses",courses);
        return map;
    }
}
