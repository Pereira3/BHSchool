package com.example.pratico;

import com.example.pratico.Admin.Admin;
import com.example.pratico.Admin.AdminRepository;
import com.example.pratico.Student.Student;
import com.example.pratico.Student.StudentRepository;
import com.example.pratico.Teacher.Teacher;
import com.example.pratico.Teacher.TeacherRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GeneralController {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AdminRepository adminRepository;

    // ---------- PÁGINA INICIAL ----------
    @GetMapping("/index") //Para dar return à index page quando ela é chamada - Caso contrário dá Erro
    public String index() {
        return "index";
    }

    @PostMapping("index")
    public String login(@RequestParam String email, @RequestParam String password, RedirectAttributes rAttributes, HttpSession session){

        Teacher t_existence = teacherRepository.findTeacherByEmailAndPassword(email, password);
        Student s_existence = studentRepository.findStudentByEmailAndPassword(email, password);
        Admin a_existence = adminRepository.findAdminByEmailAndPassword(email, password);

        if (t_existence != null) { // Há um professor com as credenciais postas
            session.setAttribute("loggedUser", email);
            return "redirect:/teacher";
        } else if (s_existence != null){
            session.setAttribute("loggedUser", email);
            return "redirect:/student";
        }else if (a_existence != null){
            session.setAttribute("loggedUser", email);
            return "redirect:/admin";
        }else{
            rAttributes.addFlashAttribute("error_login", "Invalid email or password...");
            return "redirect:/index";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }
}
