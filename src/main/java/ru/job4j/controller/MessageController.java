package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
import ru.job4j.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<List<Message>> findMessagesByRoomId(@PathVariable int id) {
        return ResponseEntity.ok(messageService.findMessagesByRoomId(id));
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        return new ResponseEntity<>(messageService.createMessage(message), HttpStatus.CREATED);
    }
}
