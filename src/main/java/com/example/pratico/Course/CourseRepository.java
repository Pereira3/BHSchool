package com.example.pratico.Course;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Integer> {
    List<Course> findAll();
    Course findCourseByName(String name);
}
