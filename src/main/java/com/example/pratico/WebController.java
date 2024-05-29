package com.example.pratico;

import com.example.pratico.Course.CourseRepository;
import com.example.pratico.Student.StudentRepository;
import com.example.pratico.Teacher.Teacher;
import com.example.pratico.Teacher.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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


    @GetMapping("/adicionarProfessor")
    public String adicionarProfessor(Model model) {
        Teacher t = new Teacher();
        model.addAttribute("new_teacher", t);
        return "adicionarProfessor";
    }
    @PostMapping("/guardarProfessor")
    public String guardarProfessor (@ModelAttribute("new_teacher") Teacher t ) {
        teacherRepository.save(t);
        return "redirect:/admin";
    }
}
