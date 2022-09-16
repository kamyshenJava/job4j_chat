package ru.job4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {

    private static final String API = "http://localhost:8080/room/";
    private static final String API_ID = "http://localhost:8080/room/{id}";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping({"/", "/index"})
    public String index(Model model, HttpSession session) {
        addUserToModel(model, session);
        List<Room> rooms = restTemplate.exchange(API, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Room>>() { }
        ).getBody();
        model.addAttribute("rooms", rooms);
        return "index";
    }

    @PostMapping("/add_room")
    public String addRoom(@ModelAttribute Room room) {
        restTemplate.postForObject(API, room, Room.class);
        return "redirect:/index";
    }

    @PostMapping("/delete_room/{id}")
    public String deleteRoom(@PathVariable("id") int id) {
        restTemplate.delete(API_ID, id);
        return "redirect:/index";
    }

    private void addUserToModel(Model model, HttpSession session) {
        Person person = (Person) session.getAttribute("person");
        if (person == null) {
            person = new Person();
            person.setUsername("guest");
        }
        model.addAttribute("person", person);
    }

}
