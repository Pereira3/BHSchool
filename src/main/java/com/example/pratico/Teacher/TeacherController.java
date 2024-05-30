package com.example.pratico.Teacher;

import com.example.pratico.Course.Course;
import com.example.pratico.Course.CourseRepository;
import com.example.pratico.Student.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeacherController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;


    // ---------- GET ID SESSION ----------
    public int getSessionCourseID(HttpSession session){
        // Curso do Professor Logado
        Teacher t = teacherRepository.findTeacherByEmail((String) session.getAttribute("loggedUser"));
        return t.getIdc();
    }

    // ---------- PÁGINA INICIAL ----------
    @GetMapping("/teacher")
    public String teachers(Model model, HttpSession session) {
        int idc = getSessionCourseID(session);
        Course c = courseRepository.findCourseByIdc(idc);
        model.addAttribute("course", c.getName());
        return "Teacher/teacher";
    }


    // ---------- STUDENTS ----------
    @GetMapping("/teacher/student")
    public String tStudenst(Model model, HttpSession session){
        int idc = getSessionCourseID(session);
        model.addAttribute("list_student", studentRepository.findStudentsByIdc(idc));
        return "/Teacher/student";
    }
    @GetMapping("/teacher/student/in")
    public String tStudentsIn(Model model, HttpSession session){
        int idc = getSessionCourseID(session);
        model.addAttribute("list_student_in", studentRepository.findStudentsByIdcAndState(idc, 1));
        return "/Teacher/in";
    }
    @GetMapping("/teacher/student/out")
    public String tStudentsOut(Model model, HttpSession session){
        int idc = getSessionCourseID(session);
        model.addAttribute("list_student_exit", studentRepository.findStudentsByIdcAndState(idc, 0));
        model.addAttribute("list_student_fin", studentRepository.findStudentsByIdcAndState(idc, 2));
        return "/Teacher/out";
    }
}
