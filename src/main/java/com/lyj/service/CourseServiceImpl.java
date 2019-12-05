package com.lyj.service;

import com.lyj.dao.CourseDao;
import com.lyj.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService{
    @Autowired
    private CourseDao courseDao;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Course> selCourseByChange(Course course) {
        return courseDao.select(course);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Course> queryAll() {
        return courseDao.selectAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Course selCourseById(Course course) {
        return courseDao.selectByPrimaryKey(course);
    }

    @Override
    public void save(Course course) {
        courseDao.insert(course);
    }

    @Override
    public void delete(Course course) {
        courseDao.delete(course);
    }
}
