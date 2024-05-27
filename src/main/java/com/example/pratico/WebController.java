package com.example.pratico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebController {

    User loggedUser = null;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @GetMapping("/alunos")
    public String alunos(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "alunos";
    }

    @GetMapping("/course")
    public String cursos(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "course";
    }

    @GetMapping("/professores")
    public String professores(Model model) {

        // Obtém a lista de professores do repositório
        List<Teacher> professores = teacherRepository.findAll();

        // Log dos dados dos professores
        logger.info("Professores: {}", professores);

        // Adiciona a lista de professores ao modelo
        model.addAttribute("profs", professores);
        return "professores";
    }
}
