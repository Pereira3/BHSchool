package com.example.pratico.Admin;

import com.example.pratico.Course.Course;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdminRepository extends CrudRepository<Course, Integer> {
    List<Course> findAll();
}
