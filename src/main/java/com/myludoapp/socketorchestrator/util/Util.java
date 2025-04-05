package com.myludoapp.socketorchestrator.util;

import com.myludoapp.socketorchestrator.dto.GameDto;
import com.myludoapp.socketorchestrator.dto.MessageDto;
import com.myludoapp.socketorchestrator.model.Game;
import com.myludoapp.socketorchestrator.model.MessageRequest;

public class Util {

    public static MessageRequest toMessage(MessageDto dto) {
        return MessageRequest.builder()
                .messageType(dto.getMessageType())
                .content(dto.getContent())
                .room(dto.getRoom())
                .userId(dto.getUserId())
                .build();
    }

    public static MessageDto toMessageDto(MessageRequest message) {
        return MessageDto.builder()
                .messageType(message.getMessageType().toString())
                .content(message.getContent())
                .room(message.getRoom())
                .userId(message.getUserId())
                .build();
    }

    public static Game toGame(GameDto gameDto) {
        return Game.builder()
                .id(String.valueOf(gameDto.getId()))
                .active(gameDto.isActive())
                .room(gameDto.getRoom())
                .created_by(String.valueOf(gameDto.getCreated_by()))
                .build();
    }
}
