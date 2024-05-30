package com.example.pratico.Student;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface StudentRepository extends CrudRepository<Student, Integer> {
    List<Student> findAll();
    Student findStudentByEmail(String email);
    List<Student> findStudentsByIdc(int idc);
    Student findStudentByEmailAndPassword(String email, String password);
}
