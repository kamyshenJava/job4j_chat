package ru.job4j.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/signup")
    public String signup(Model model, HttpSession session) {
        addPersonToModel(model, session);
        return "signup";
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        addPersonToModel(model, session);
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/fail/{type}")
    public String fail(@PathVariable("type") String type, Model model, HttpSession session) {
        addPersonToModel(model, session);
        String message;
        switch (type) {
            case "signup" :
                message = "Failed to sign up. This name is already taken. Please, choose another name.";
                model.addAttribute("message", message);
                return "fail";
            case "login" :
                message = "Failed to log in. Please, enter correct name and password.";
                model.addAttribute("message", message);
                return "fail";
            default :
                message = "An error happened. Please, try again";
                model.addAttribute("message", message);
                return "fail";
        }
    }

    @PostMapping("/signup")
    public String signupPost(@ModelAttribute Person person, HttpServletRequest req) {
        Optional<Person> regPerson = personService.add(person);
        if (regPerson.isEmpty()) {
            return "redirect:/fail/signup";
        }
        HttpSession session = req.getSession();
        session.setAttribute("person", regPerson.get());
        return "redirect:/index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Person person, HttpServletRequest req) {
        Optional<Person> regPerson
                = personService.findPersonByUsernameAndPassword(person.getUsername(), person.getPassword());
        if (regPerson.isEmpty()) {
            return "redirect:/fail/login";
        }
        HttpSession session = req.getSession();
        session.setAttribute("person", regPerson.get());
        return "redirect:/index";
    }

    private void addPersonToModel(Model model, HttpSession session) {
        Person person = (Person) session.getAttribute("person");
        if (person == null) {
            person = new Person();
            person.setUsername("guest");
        }
        model.addAttribute("person", person);
    }
}
