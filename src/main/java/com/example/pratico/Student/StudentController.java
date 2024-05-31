package com.example.pratico.Student;

import com.example.pratico.Admin.Admin;
import com.example.pratico.Course.Course;
import com.example.pratico.Course.CourseRepository;
import com.example.pratico.Teacher.TeacherRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;


    // ---------- GET ID SESSION ----------
    public int getSessionCourseID(HttpSession session){
        // Curso do Professor Logado
        Student s = studentRepository.findStudentByEmail((String) session.getAttribute("loggedUser"));

        if(s == null){
            return 0;
        }

        return s.getIdc();
    }

    // ---------- PÁGINA INICIAL ----------
    @GetMapping("/student")
    public String students(Model model, HttpSession session) {

        Student s = studentRepository.findStudentByEmail((String) session.getAttribute("loggedUser"));

        int idc = getSessionCourseID(session);
        Course c = courseRepository.findCourseByIdc(idc);

        if(c == null || s == null){
            return "redirect:/error";
        }

        model.addAttribute("course", c.getName());
        return "Student/student";
    }


    // ---------- CLASS ----------
    @GetMapping("/student/class")
    public String tStudents(Model model, HttpSession session){
        int idc = getSessionCourseID(session);
        model.addAttribute("list_student", studentRepository.findStudentsByIdcAndState(idc,1));
        model.addAttribute("teacher", teacherRepository.findTeachersByIdc(idc));
        return "/Student/class";
    }
}
