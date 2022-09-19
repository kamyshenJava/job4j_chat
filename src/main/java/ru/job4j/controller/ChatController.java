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
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.Room;
import ru.job4j.service.MessageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ChatController {
    private static final String API_MESSAGE = "http://localhost:8080/message/";
    private static final String API_MESSAGE_ID = "http://localhost:8080/message/{id}";
    private static final String API_MESSAGE_ROOM = "http://localhost:8080/message/room/{id}";
    private static final String API_ID = "http://localhost:8080/room/{id}";
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/chatroom/{id}")
    public String getChatroom(@PathVariable("id") int id, Model model, HttpSession session) {
        addUserToModel(model, session);
        Room room = restTemplate.getForObject(API_ID, Room.class, id);
        List<Message> messages = restTemplate.exchange(API_MESSAGE_ROOM, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Message>>() { }, id
        ).getBody();
        model.addAttribute("room", room);
        model.addAttribute("messages", messages);
        return "room";
    }

    @PostMapping("/save_message")
    public String saveMessage(@ModelAttribute Message message, HttpServletRequest req, HttpSession session) {
        int roomId = Integer.parseInt(req.getParameter("room_id"));
        Person person = (Person) (session.getAttribute("person"));
        message.setRoom(Room.of(roomId));
        message.setPerson(person);
        restTemplate.postForObject(API_MESSAGE, message, Message.class);
        return String.format("redirect:/chatroom/%d", roomId);
    }

    @PostMapping("/delete_message/{id}")
    public String deleteMessage(@PathVariable("id") int id, HttpServletRequest req) {
        int roomId = Integer.parseInt(req.getParameter("room_id"));
        restTemplate.delete(API_MESSAGE_ID, id);
        return String.format("redirect:/chatroom/%d", roomId);
    }

    @PostMapping("edit_message")
    public String editMessage(@ModelAttribute Message message, HttpServletRequest req, HttpSession session) {
        int roomId = Integer.parseInt(req.getParameter("room_id"));
        Person person = Person.of(Integer.parseInt(req.getParameter("person_id")));
        LocalDateTime created = LocalDateTime.parse(req.getParameter("created1"));
        message.setRoom(Room.of(roomId));
        message.setPerson(person);
        message.setCreated(created);
        restTemplate.put(API_MESSAGE, message);
        return String.format("redirect:/chatroom/%d", roomId);
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
