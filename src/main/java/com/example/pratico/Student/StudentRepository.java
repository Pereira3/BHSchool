package com.example.pratico.Student;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface StudentRepository extends CrudRepository<Student, Integer> {
    List<Student> findAll();
    Student findStudentByEmail(String email);
    Student findStudentByIds(int ids);
    List<Student> findStudentsByIdc(int idc);
    List<Student> findStudentsByIdcAndState(int idc, int state);
    Student findStudentByEmailAndPassword(String email, String password);
}
