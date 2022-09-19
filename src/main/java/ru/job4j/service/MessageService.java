package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.domain.Message;
import ru.job4j.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    public List<Message> findMessagesByRoomId(int id) {
        return messageRepository.findMessagesByRoomIdOrderByCreated(id);
    }
    public Message createMessage(Message message) {
        message.setCreated(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public void deleteMessage(int id) {
        messageRepository.deleteById(id);
    }

    public Message updateMessage(Message message) {
        return messageRepository.save(message);
    }
}
