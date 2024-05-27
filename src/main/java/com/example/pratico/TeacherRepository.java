package com.example.pratico;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeacherRepository extends CrudRepository<Teacher, Integer>{
    List<Teacher> findAll();
    Teacher findTeacherByEmail(String email);
}
