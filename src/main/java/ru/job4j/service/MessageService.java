package ru.job4j.service;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Message;
import ru.job4j.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final PrettyTime p = new PrettyTime();

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    public List<Message> findMessagesByRoomId(int id) {
        return messageRepository.findMessagesByRoomId(id);
    }
    public Message createMessage(Message message) {
        message.setCreated(LocalDateTime.now());
        return messageRepository.save(message);
    }
    public List<Message> setCreatedTimeAgoForMessages(List<Message> messages) {
        if (!messages.isEmpty()) {
            messages.forEach(message -> message.setCreatedTimeAgo(p.format(message.getCreated())));
        }
        return messages;
    }
}
