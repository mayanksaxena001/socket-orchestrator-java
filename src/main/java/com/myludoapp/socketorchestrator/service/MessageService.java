package com.myludoapp.socketorchestrator.service;

import com.myludoapp.socketorchestrator.dto.MessageDto;
import com.myludoapp.socketorchestrator.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;


    public List<MessageDto> getMessages(String room) {
        return messageRepository.findAllByRoom(room);
    }

    public MessageDto saveMessage(MessageDto message) {
        return messageRepository.save(message);
    }

}