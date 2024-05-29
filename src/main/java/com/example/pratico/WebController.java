package com.example.pratico;

import com.example.pratico.Course.CourseRepository;
import com.example.pratico.Student.StudentRepository;
import com.example.pratico.Teacher.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/admin")
    public String admins(Model model) {
        model.addAttribute("list_teacher", teacherRepository.findAll());
        model.addAttribute("list_student", studentRepository.findAll());
        model.addAttribute("list_course", courseRepository.findAll());
        return "admin";
    }
    @GetMapping("/student")
    public String alunos(Model model) {
        model.addAttribute("list_student", studentRepository.findAll());
        return "student";
    }
    @GetMapping("/course")
    public String cursos(Model model) {
        model.addAttribute("list_course", courseRepository.findAll());
        return "course";
    }
    @GetMapping("/teacher")
    public String professores(Model model) {
        model.addAttribute("list_teacher", teacherRepository.findAll());
        return "teacher";
    }
}
