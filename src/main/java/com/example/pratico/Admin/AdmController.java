package com.example.pratico.Admin;
import com.example.pratico.Course.Course;
import com.example.pratico.Course.CourseRepository;
import com.example.pratico.Student.Student;
import com.example.pratico.Student.StudentRepository;
import com.example.pratico.Teacher.Teacher;
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
public class AdmController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AdminRepository adminRepository;

    // ---------- PÁGINA INICIAL ----------
    @GetMapping("/admin")
    public String admins(HttpSession session) {
        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }
        return "Admin/admin";
    }

    // ---------- TEACHERS ----------
    @GetMapping("/admin/teacher")
    public String admTeachers(Model model){
        model.addAttribute("list_teacher", teacherRepository.findAll());
        return "/Admin/teacher";
    }
    @GetMapping("/admin/teacher/addTeacher")
    public String aProfessor(Model model) {
        Teacher t = new Teacher();
        model.addAttribute("new_teacher", t);
        return "/Admin/addTeacher";
    }
    @PostMapping("/admin/teacher/saveTeacher")
    public String gProfessor (@ModelAttribute("new_teacher") Teacher t, RedirectAttributes rAttributes) {

        Teacher t_existence = teacherRepository.findTeacherByEmail(t.getEmail());
        Student s_existence = studentRepository.findStudentByEmail(t.getEmail());
        Admin a_existence = adminRepository.findAdminByEmail(t.getEmail());

        if (t_existence != null || s_existence != null || a_existence != null) { //Há alguém com o mesmo email
            rAttributes.addFlashAttribute("error_aT", "Email already exists...");
            return "redirect:/admin/teacher/addTeacher";
        }

        // Transformar pass em hash sha256
        String sha256hex = Hashing.sha256().hashString(t.getPassword(), StandardCharsets.UTF_8).toString();

        t.setPassword(sha256hex);

        LocalDate bdt = convertToLocalDateViaInstant(t.getBirthdate());
        if (bdt != null) {
            Period age = Period.between(bdt, LocalDate.now());
            if (age.getYears() < 22){
                rAttributes.addFlashAttribute("error_aT", "The teacher must be at least 22 years old.");
                return "redirect:/admin/teacher/addTeacher";
            }
        }

        teacherRepository.save(t);
        return "redirect:/admin/teacher";
    }
    @GetMapping("admin/teacher/deleteTeacher/{idt}")
    public String eProfessor(@PathVariable(value = "idt") Integer idt) {
        teacherRepository.deleteById(idt);
        return "redirect:/admin/teacher";
    }


    // ---------- STUDENT ----------
    @GetMapping("/admin/student")
    public String admStudent(Model model){
        model.addAttribute("list_student", studentRepository.findAll());
        return "/Admin/student";
    }

    @GetMapping("/admin/student/addStudent")
    public String aStudent(Model model) {
        Student s = new Student();
        model.addAttribute("new_student", s);
        return "/Admin/addStudent";
    }
    @PostMapping("/admin/student/saveStudent")
    public String gStudent (@ModelAttribute("new_student") Student s, RedirectAttributes rAttributes) {

        Teacher t_existence = teacherRepository.findTeacherByEmail(s.getEmail());
        Student s_existence = studentRepository.findStudentByEmail(s.getEmail());
        Admin a_existence = adminRepository.findAdminByEmail(s.getEmail());

        // Transformar pass em hash sha256
        String sha256hex = Hashing.sha256().hashString(s.getPassword(), StandardCharsets.UTF_8).toString();

        s.setPassword(sha256hex);

        if (t_existence != null || s_existence != null || a_existence != null) { //Há alguém com o mesmo email
            rAttributes.addFlashAttribute("error_aS", "Email already exists...");
            return "redirect:/admin/student/addStudent";
        }

        LocalDate bds = convertToLocalDateViaInstant(s.getBirthdate());
        if (bds != null) {
            Period age = Period.between(bds, LocalDate.now());
            if (age.getYears() < 16){
                rAttributes.addFlashAttribute("error_aS", "The student must be at least 16 years old.");
                return "redirect:/admin/student/addStudent";
            }
        }
        studentRepository.save(s);
        return "redirect:/admin/student";
    }
    @GetMapping("admin/student/deleteStudent/{ids}")
    public String eStudent(@PathVariable(value = "ids") Integer ids) {
        studentRepository.deleteById(ids);
        return "redirect:/admin/student";
    }


    // ---------- COURSE ----------
    @GetMapping("/admin/course")
    public String admCourse(Model model){
        model.addAttribute("list_course", courseRepository.findAll());
        return "/Admin/course";
    }
    @GetMapping("/admin/course/addCourse")
    public String aCourse(Model model) {
        Course c = new Course();
        model.addAttribute("new_course", c);
        return "/Admin/addCourse";
    }
    @PostMapping("/admin/course/saveCourse")
    public String gCourse (@ModelAttribute("new_course") Course c, RedirectAttributes rAttributes) {

        Course c_existence = courseRepository.findCourseByName(c.getName());

        if (c_existence != null) { //Já há um curso com o mesmo nome
            rAttributes.addFlashAttribute("error_aC", "Course already exists...");
            return "redirect:/admin/course";
        }

        courseRepository.save(c);
        return "redirect:/admin/course";
    }
    @GetMapping("admin/course/deleteCourse/{idc}")
    public String eCourse(@PathVariable(value = "idc") Integer idc, RedirectAttributes rAttributes) {

        List<Teacher> t_existence = teacherRepository.findTeachersByIdc(idc);
        List<Student> s_existence = studentRepository.findStudentsByIdc(idc);

        if (!t_existence.isEmpty() || !s_existence.isEmpty()) { //Caso haja professores ou Alunos no curso -> Eliminar primeiro eles
            rAttributes.addFlashAttribute("error_dC", "Course On Going...");
            return "redirect:/admin/course";
        }

        courseRepository.deleteById(idc);
        return "redirect:/admin/course";
    }

    // Site Spring -> https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
