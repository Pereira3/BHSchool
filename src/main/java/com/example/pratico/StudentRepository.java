package com.example.pratico;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student, Integer> {
    Student findStudentByEmail(String email);
    List<Student> findAll();
}
