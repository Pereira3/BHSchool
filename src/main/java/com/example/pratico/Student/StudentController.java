package com.example.pratico.Student;

import com.example.pratico.Admin.Admin;
import com.example.pratico.Course.Course;
import com.example.pratico.Course.CourseRepository;
import com.example.pratico.Teacher.TeacherRepository;
import com.google.common.hash.Hashing;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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
        model.addAttribute("student", s); //Para permitir usar o IDS na alteração do perfil
        int idc = getSessionCourseID(session);
        Course c = courseRepository.findCourseByIdc(idc);

        if(c == null || s == null){
            return "redirect:/error";
        }

        int maleCount = studentRepository.findStudentsByGenderAndIdc("Male", idc).size();
        int femaleCount = studentRepository.findStudentsByGenderAndIdc("Female", idc).size();
       // double averageGrade = studentRepository.findAverageGrade(); // Assuming you have a method for this
        int totalStudents = (int) studentRepository.count();
        int totalCourses = (int) courseRepository.count();

        int avg05 = studentRepository.findStudentsByAverageBetweenAndIdc(0.01f,5f, idc).size();
        int avg510 = studentRepository.findStudentsByAverageBetweenAndIdc(5.01f,10f, idc).size();
        int avg1015 = studentRepository.findStudentsByAverageBetweenAndIdc(10.01f,15f, idc).size();
        int avg1520 = studentRepository.findStudentsByAverageBetweenAndIdc(15.1f,20f, idc).size();

        int inCourse = studentRepository.findStudentsByIdcAndState(idc,1).size();
        int finished = studentRepository.findStudentsByIdcAndState(idc,2).size();
        int quit = studentRepository.findStudentsByIdcAndState(idc,0).size();

        model.addAttribute("maleCount", maleCount);
        model.addAttribute("femaleCount", femaleCount);
        //model.addAttribute("averageGrade", averageGrade);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("inCourse", inCourse);
        model.addAttribute("finished", finished);
        model.addAttribute("quit", quit);
        model.addAttribute("avg05", avg05);
        model.addAttribute("avg510", avg510);
        model.addAttribute("avg1015", avg1015);
        model.addAttribute("avg1520", avg1520);

        return "Student/student";
    }

    // ---------- ALTERAR DADOS ----------
    @GetMapping("/student/changeS/{ids}")
    public String eProfile(@PathVariable(value = "ids") Integer ids, Model model, HttpSession session) {
        Student sc = studentRepository.findStudentByEmail((String) session.getAttribute("loggedUser"));
        if(sc == null){
            return "error";
        }
        Student s = studentRepository.findStudentByIds(ids);
        model.addAttribute("student", s);
        return "/Student/eProfile";
    }
    @PostMapping("/student/changeS/eProfile/{ids}")
    public String saveEProfile(@PathVariable(value = "ids") Integer ids, @ModelAttribute Student student, HttpSession session) {

        Student sc = studentRepository.findStudentByEmail((String) session.getAttribute("loggedUser"));
        if(sc == null){
            return "error";
        }

        Student s = studentRepository.findStudentByIds(ids);
        // Transformar pass em hash sha256
        String sha256hex = Hashing.sha256().hashString(student.getPassword(), StandardCharsets.UTF_8).toString();

        s.setPassword(sha256hex);
        s.setAddress(student.getAddress());
        studentRepository.save(s);
        return "redirect:/student";
    }


    // ---------- CLASS ----------
    @GetMapping("/student/class")
    public String tStudents(Model model, HttpSession session){
        int idc = getSessionCourseID(session);
        model.addAttribute("list_student", studentRepository.findStudentsByIdcAndState(idc,1));
        model.addAttribute("teacher", teacherRepository.findTeachersByIdc(idc));

        Student s = studentRepository.findStudentByEmail((String) session.getAttribute("loggedUser"));
        model.addAttribute("student", s); //Para permitir usar o IDS na alteração do perfil

        return "/Student/class";
    }
}
