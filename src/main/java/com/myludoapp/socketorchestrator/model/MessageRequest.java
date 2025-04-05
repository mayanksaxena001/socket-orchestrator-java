package com.myludoapp.socketorchestrator.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageRequest {

    private String messageType;
    private String content;
    private String room;
    private String gameId;
    private String userId;
    private String tokenId;
    private int diceValue=-1;
}