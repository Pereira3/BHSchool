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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    public String admins(Model model, HttpSession session) {
        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }
        List<Course> courses = courseRepository.findAll();

        // média de notas dos alunos em cada curso
        List<Double> avgGrades = new ArrayList<>();
        int counter = 0;
        for (Course course : courses) {
            List<Student> students = studentRepository.findStudentsByIdc(course.getIdc());
            double sum = 0;
            for (Student student : students) {
                if (student.getAverage() == 0) {
                    counter++;
                }
                sum += student.getAverage();
            }
            if (students.size() - counter == 0) {
                avgGrades.add(0.0);
                counter = 0;
                continue;
            }
            double avg = sum / (students.size() - counter);
            avgGrades.add(Math.round(avg * 100.0) / 100.0);
            counter = 0;
        }

        // media de idade dos alunos em cada curso
        List<Double> avgAges = new ArrayList<>();
        for (Course course : courses) {
            List<Student> students = studentRepository.findStudentsByIdc(course.getIdc());
            double sum = 0;
            for (Student student : students) {
                sum += student.getAge(student.getBirthdate());
            }
            double avg = sum / students.size();
            avgAges.add(Math.round(avg * 100.0) / 100.0);
        }


        model.addAttribute("avgAges", avgAges);
        model.addAttribute("list_course", courses);
        model.addAttribute("avgGrades", avgGrades);

        return "Admin/admin";
    }

    // ---------- TEACHERS ----------
    @GetMapping("/admin/teacher")
    public String admTeachers(Model model, HttpSession session){
        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }
        model.addAttribute("list_teacher", teacherRepository.findAll());
        return "/Admin/teacher";
    }
    @GetMapping("/admin/teacher/addTeacher")
    public String aProfessor(Model model, HttpSession session) {
        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }
        Teacher t = new Teacher();
        model.addAttribute("new_teacher", t);
        return "/Admin/addTeacher";
    }
    @PostMapping("/admin/teacher/saveTeacher")
    public String gProfessor (@ModelAttribute("new_teacher") Teacher t, RedirectAttributes rAttributes, HttpSession session) {

        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }

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
    public String eProfessor(@PathVariable(value = "idt") Integer idt, HttpSession session) {
        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }
        teacherRepository.deleteById(idt);
        return "redirect:/admin/teacher";
    }


    // ---------- STUDENT ----------
    @GetMapping("/admin/student")
    public String admStudent(Model model, HttpSession session){
        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }
        model.addAttribute("list_student", studentRepository.findAll());
        return "/Admin/student";
    }

    @GetMapping("/admin/student/addStudent")
    public String aStudent(Model model, HttpSession session) {
        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }
        Student s = new Student();
        model.addAttribute("new_student", s);
        return "/Admin/addStudent";
    }
    @PostMapping("/admin/student/saveStudent")
    public String gStudent (@ModelAttribute("new_student") Student s, RedirectAttributes rAttributes, HttpSession session) {

        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }

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
    public String eStudent(@PathVariable(value = "ids") Integer ids, HttpSession session) {
        studentRepository.deleteById(ids);
        return "redirect:/admin/student";
    }


    // ---------- COURSE ----------
    @GetMapping("/admin/course")
    public String admCourse(Model model, HttpSession session){
        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }
        List<Course> courses = courseRepository.findAll();
        List<Integer> enrolledStudents = new ArrayList<>();
        for (Course course : courses) {
            int enrolledCount = studentRepository.findStudentsByIdc(course.getIdc()).size();
            enrolledStudents.add(enrolledCount);
        }

        model.addAttribute("list_student", enrolledStudents);
        model.addAttribute("list_course", courses);
        return "/Admin/course";
    }
    @GetMapping("/admin/course/addCourse")
    public String aCourse(Model model, HttpSession session) {
        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }
        Course c = new Course();
        model.addAttribute("new_course", c);
        return "/Admin/addCourse";
    }
    @PostMapping("/admin/course/saveCourse")
    public String gCourse (@ModelAttribute("new_course") Course c, RedirectAttributes rAttributes, HttpSession session) {

        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }

        Course c_existence = courseRepository.findCourseByName(c.getName());

        if (c_existence != null) { //Já há um curso com o mesmo nome
            rAttributes.addFlashAttribute("error_aC", "Course already exists...");
            return "redirect:/admin/course";
        }

        courseRepository.save(c);
        return "redirect:/admin/course";
    }
    @GetMapping("admin/course/deleteCourse/{idc}")
    public String eCourse(@PathVariable(value = "idc") Integer idc, RedirectAttributes rAttributes, HttpSession session) {

        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }

        List<Teacher> t_existence = teacherRepository.findTeachersByIdc(idc);
        List<Student> s_existence = studentRepository.findStudentsByIdc(idc);

        if (!t_existence.isEmpty() || !s_existence.isEmpty()) { //Caso haja professores ou Alunos no curso -> Eliminar primeiro eles
            rAttributes.addFlashAttribute("error_C", "Course On Going...");
            return "redirect:/admin/course";
        }

        courseRepository.deleteById(idc);
        return "redirect:/admin/course";
    }

    // ---------- ALTERAR DADOS ----------
    @GetMapping("/admin/student/editStudent/{ids}")
    public String eStudent(@PathVariable(value = "ids") Integer ids, Model model, HttpSession session) {

        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }

        Student s = studentRepository.findStudentByIds(ids);
        model.addAttribute("student", s);
        return "/Admin/eStudent";
    }
    @PostMapping("/admin/student/saveEditedStudent/{ids}")
    public String saveEStudent(@PathVariable(value = "ids") Integer ids, @ModelAttribute Student student, RedirectAttributes rAttributes, HttpSession session) {

        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }

        Student s = studentRepository.findStudentByIds(ids);
        // Transformar pass em hash sha256
        String sha256hex = Hashing.sha256().hashString(student.getPassword(), StandardCharsets.UTF_8).toString();

        Teacher t_existence = teacherRepository.findTeacherByEmail(student.getEmail());
        Student s_existence = studentRepository.findStudentByEmail(student.getEmail());
        Admin a_existence = adminRepository.findAdminByEmail(student.getEmail());

        if (t_existence != null || (s_existence != null && !(s_existence.getEmail().equals(s.getEmail()))) || a_existence != null) { //Há alguém com o mesmo email
            rAttributes.addFlashAttribute("error_eS", "Email already exists...");
            return "redirect:/admin/student/editStudent/{ids}";
        }

        LocalDate bds = convertToLocalDateViaInstant(student.getBirthdate());
        if (bds != null) {
            Period age = Period.between(bds, LocalDate.now());
            if (age.getYears() < 16){
                rAttributes.addFlashAttribute("error_eS", "The student must be at least 16 years old.");
                return "redirect:/admin/student/editStudent/{ids}";
            }
        }

        s.setName(student.getName());
        s.setPassword(sha256hex);
        s.setEmail(student.getEmail());
        s.setBirthdate(student.getBirthdate());
        s.setAddress(student.getAddress());
        s.setGender(student.getGender());
        s.setIdc(student.getIdc());
        s.setAverage(student.getAverage());
        s.setState(student.getState());
        studentRepository.save(s);
        return "redirect:/admin/student";
    }


    // ---------- ALTERAR DADOS ----------
    @GetMapping("/admin/teacher/editTeacher/{idt}")
    public String eTeacher(@PathVariable(value = "idt") Integer idt, Model model, HttpSession session) {

        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }

        Teacher t = teacherRepository.findTeacherByIdt(idt);
        model.addAttribute("teacher", t);
        return "/Admin/eTeacher";
    }
    @PostMapping("/admin/teacher/saveEditedTeacher/{idt}")
    public String saveETeacher(@PathVariable(value = "idt") Integer idt, @ModelAttribute Teacher teacher, RedirectAttributes rAttributes, HttpSession session) {

        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }

        Teacher t = teacherRepository.findTeacherByIdt(idt);
        // Transformar pass em hash sha256
        String sha256hex = Hashing.sha256().hashString(teacher.getPassword(), StandardCharsets.UTF_8).toString();

        Teacher t_existence = teacherRepository.findTeacherByEmail(teacher.getEmail());
        Student s_existence = studentRepository.findStudentByEmail(teacher.getEmail());
        Admin a_existence = adminRepository.findAdminByEmail(teacher.getEmail());

        if ((t_existence != null && !(t_existence.getEmail().equals(t.getEmail()))) || s_existence != null || a_existence != null) { //Há alguém com o mesmo email
            rAttributes.addFlashAttribute("error_eT", "Email already exists...");
            return "redirect:/admin/teacher/editTeacher/{idt}";
        }

        LocalDate bds = convertToLocalDateViaInstant(teacher.getBirthdate());
        if (bds != null) {
            Period age = Period.between(bds, LocalDate.now());
            if (age.getYears() < 22){
                rAttributes.addFlashAttribute("error_eT", "The teacher must be at least 22 years old.");
                return "redirect:/admin/teacher/editTeacher/{idt}";
            }
        }

        t.setName(teacher.getName());
        t.setPassword(sha256hex);
        t.setEmail(teacher.getEmail());
        t.setBirthdate(teacher.getBirthdate());
        t.setAddress(teacher.getAddress());
        t.setGender(teacher.getGender());
        t.setIdc(teacher.getIdc());
        t.setSalary(teacher.getSalary());
        teacherRepository.save(t);
        return "redirect:/admin/teacher";
    }

    // ---------- ALTERAR DADOS ----------
    @GetMapping("/admin/course/editCourse/{idc}")
    public String eCourse(@PathVariable(value = "idc") Integer idc, Model model, HttpSession session) {
        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }

        Course c = courseRepository.findCourseByIdc(idc);
        model.addAttribute("curso", c);
        return "/Admin/eCourse";
    }
    @PostMapping("/admin/course/saveEditedCourse/{idc}")
    public String saveECourse(@PathVariable(value = "idc") Integer idc, @ModelAttribute Course course, RedirectAttributes rAttributes, HttpSession session) {

        Admin a = adminRepository.findAdminByEmail((String) session.getAttribute("loggedUser"));
        if(a == null){
            return "redirect:/error";
        }

        Course c = courseRepository.findCourseByIdc(idc);
        Course c_existence = courseRepository.findCourseByName(c.getName());

        if (c_existence != null && !(c_existence.getName().equals(course.getName()))) { //Já há um curso com o mesmo nome
            rAttributes.addFlashAttribute("error_C", "Course already exists...");
            return "redirect:/admin/course/editCourse/{idc}";
        }

        c.setName(course.getName());
        c.setDescription(course.getDescription());
        c.setDuration(course.getDuration());
        c.setCapacity(course.getCapacity());
        courseRepository.save(c);
        return "redirect:/admin/course";
    }

    // Site Spring -> https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
