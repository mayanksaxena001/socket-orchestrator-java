package com.myludoapp.socketorchestrator.model;

public enum EventType {

    //............SERVER EVENTS
    JOIN_ROOM("join_room"),
    DISCONNECT("disconnect"),
    START_GAME("start_game"),
    DICE_ROLL("dice_roll"),
    TIME_OUT("time_out"),
    SELECTED_TOKEN("selected_token"),
    SEND_CHAT_MESSAGE("send_chat_message"),
    SEND_MESSAGE("send_message"),


    //............CLIENT EVENTS,
    RECEIVED_MESSAGE("received_message"),

    CHAT_MESSAGE_RECEIVED("chat_message_recieved"),

    PLAYER_JOINED("player_joined");

    private final String value;

    EventType(String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
