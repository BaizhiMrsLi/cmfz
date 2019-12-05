package com.lyj.controller;

import com.lyj.entity.Course;
import com.lyj.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
        //添加课程
        course.setCreate_date(new Date());
        courseService.save(course);

        //先查找所有属于该用户的课程
        Course c = new Course();
        c.setUser_id(course.getUser_id());
        List<Course> courses = courseService.selCourseByChange(c);

        //随后查找必修课
        c.setUser_id(null);
        c.setLevel("0");
        for (Course course1 : courseService.selCourseByChange(c)) {
            courses.add(course1);
        }

        map.put("status","200");
        map.put("courses",courses);
        return map;
    }

    @RequestMapping("delete")
    public Map delete(Course course){
        Map map = new HashMap();
        courseService.delete(course);

        Course c = new Course();
        c.setUser_id(course.getUser_id());
        //先查出所有属于该用户功课
        List<Course> courses = courseService.selCourseByChange(c);
        //随后查找必修课
        c.setUser_id(null);
        c.setLevel("0");
        for (Course course1 : courseService.selCourseByChange(c)) {
            courses.add(course1);
        }

        map.put("status","200");
        map.put("courses",courses);
        return map;
    }
}
