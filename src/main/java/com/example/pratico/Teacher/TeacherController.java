package com.example.pratico.Teacher;

import com.example.pratico.Course.Course;
import com.example.pratico.Course.CourseRepository;
import com.example.pratico.Student.Student;
import com.example.pratico.Student.StudentRepository;
import com.google.common.hash.Hashing;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


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

        if(t == null){
            return 0;
        }

        return t.getIdc();
    }

    // ---------- PÁGINA INICIAL ----------
    @GetMapping("/teacher")
    public String teachers(Model model, HttpSession session) {

        Teacher t = teacherRepository.findTeacherByEmail((String) session.getAttribute("loggedUser"));
        model.addAttribute("teacher", t); //Para permitir usar o IDT na alteração do perfil

        int idc = getSessionCourseID(session);
        Course c = courseRepository.findCourseByIdc(idc);

        int maleCount = studentRepository.findStudentsByGenderAndIdc("Male", idc).size();
        int femaleCount = studentRepository.findStudentsByGenderAndIdc("Female", idc).size();

        List<Student> students = studentRepository.findStudentsByIdc(idc);
        List<Integer> ages = new ArrayList<>();
        for (Student s : students) {
            int age = s.getAge(s.getBirthdate());

            ages.add(age);
        }

        int avg05 = studentRepository.findStudentsByAverageBetweenAndIdc(0.01f,5f, idc).size();
        int avg510 = studentRepository.findStudentsByAverageBetweenAndIdc(5.01f,10f, idc).size();
        int avg1015 = studentRepository.findStudentsByAverageBetweenAndIdc(10.01f,15f, idc).size();
        int avg1520 = studentRepository.findStudentsByAverageBetweenAndIdc(15.1f,20f, idc).size();

        if(c == null || t == null){
            return "redirect:/error";
        }

        model.addAttribute("course", c.getName());
        model.addAttribute("maleCount", maleCount);
        model.addAttribute("femaleCount", femaleCount);
        model.addAttribute("ages", ages);
        model.addAttribute("avg05", avg05);
        model.addAttribute("avg510", avg510);
        model.addAttribute("avg1015", avg1015);
        model.addAttribute("avg1520", avg1520);
        return "Teacher/teacher";
    }

    // ---------- ALTERAR DADOS ----------
    @GetMapping("/teacher/changeT/{idt}")
    public String eProfile(@PathVariable(value = "idt") Integer idt, Model model, HttpSession session) {
        Teacher tc = teacherRepository.findTeacherByEmail((String) session.getAttribute("loggedUser"));
        if(tc == null){
            return "error";
        }

        Teacher t = teacherRepository.findTeacherByIdt(idt);
        model.addAttribute("teacher", t);
        return "/Teacher/eProfile";
    }
    @PostMapping("/teacher/changeT/eProfile/{idt}")
    public String saveEProfile(@PathVariable(value = "idt") Integer idt, @ModelAttribute Teacher teacher, HttpSession session) {

        Teacher tc = teacherRepository.findTeacherByEmail((String) session.getAttribute("loggedUser"));
        if(tc == null){
            return "error";
        }

        Teacher t = teacherRepository.findTeacherByIdt(idt);
        // Transformar pass em hash sha256
        String sha256hex = Hashing.sha256().hashString(teacher.getPassword(), StandardCharsets.UTF_8).toString();

        t.setPassword(sha256hex);
        t.setAddress(teacher.getAddress());
        teacherRepository.save(t);
        return "redirect:/teacher";
    }


    // ---------- STUDENTS ----------
    @GetMapping("/teacher/student")
    public String tStudenst(Model model, HttpSession session){
        int idc = getSessionCourseID(session);
        model.addAttribute("list_student", studentRepository.findStudentsByIdc(idc));

        Teacher t = teacherRepository.findTeacherByEmail((String) session.getAttribute("loggedUser"));
        model.addAttribute("teacher", t); //Para permitir usar o IDT na alteração do perfil

        return "/Teacher/student";
    }
    @GetMapping("/teacher/student/in")
    public String tStudentsIn(Model model, HttpSession session){
        int idc = getSessionCourseID(session);
        model.addAttribute("list_student_in", studentRepository.findStudentsByIdcAndState(idc, 1));

        Teacher t = teacherRepository.findTeacherByEmail((String) session.getAttribute("loggedUser"));
        model.addAttribute("teacher", t); //Para permitir usar o IDT na alteração do perfil

        return "/Teacher/in";
    }
    @GetMapping("/teacher/student/out")
    public String tStudentsOut(Model model, HttpSession session){
        int idc = getSessionCourseID(session);
        model.addAttribute("list_student_exit", studentRepository.findStudentsByIdcAndState(idc, 0));
        model.addAttribute("list_student_fin", studentRepository.findStudentsByIdcAndState(idc, 2));

        Teacher t = teacherRepository.findTeacherByEmail((String) session.getAttribute("loggedUser"));
        model.addAttribute("teacher", t); //Para permitir usar o IDT na alteração do perfil

        return "/Teacher/out";
    }

    @GetMapping("/teacher/student/in/cAverage/{ids}")
    public String cAStudent(@PathVariable(value = "ids") Integer ids, Model model, HttpSession session) {
        Student s = studentRepository.findStudentByIds(ids);
        model.addAttribute("student", s);

        Teacher t = teacherRepository.findTeacherByEmail((String) session.getAttribute("loggedUser"));
        model.addAttribute("teacher", t); //Para permitir usar o IDT na alteração do perfil
        return "/Teacher/inAverage";
    }
    @PostMapping("/teacher/student/in/saveAverage/{ids}")
    public String saveAverage(@PathVariable(value = "ids") Integer ids, @RequestParam("average") Float average, @RequestParam("state") Integer state, HttpSession session) {

        Teacher tc = teacherRepository.findTeacherByEmail((String) session.getAttribute("loggedUser"));
        if(tc == null){
            return "error";
        }

        Student s = studentRepository.findStudentByIds(ids);
        s.setAverage(average);
        s.setState(state);
        studentRepository.save(s);
        return "redirect:/teacher/student/in";
    }

}
