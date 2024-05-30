package com.example.pratico.Teacher;

import java.util.List;

import com.example.pratico.Course.Course;
import org.springframework.data.repository.CrudRepository;


public interface TeacherRepository extends CrudRepository<Teacher, Integer>{
    List<Teacher> findAll();
    Teacher findTeacherByEmail(String email);
    List<Teacher> findTeachersByIdc(int idc);
    Teacher findTeacherByEmailAndPassword(String email, String password);
}
