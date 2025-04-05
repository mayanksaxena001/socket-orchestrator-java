package com.myludoapp.socketorchestrator.model;

public enum MessageType {
    CLIENT("client"), SERVER("server");

    private final String value;

    MessageType(String s){
        this.value=s;
    }
}
