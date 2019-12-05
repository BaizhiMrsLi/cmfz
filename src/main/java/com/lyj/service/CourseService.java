package com.lyj.service;

import com.lyj.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> selCourseByChange(Course course);

    List<Course> queryAll();

    Course selCourseById(Course course);

    void save(Course course);

    void delete(Course course);
}
