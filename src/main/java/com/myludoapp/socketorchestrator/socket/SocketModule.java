package com.myludoapp.socketorchestrator.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.myludoapp.socketorchestrator.model.MessageRequest;
import com.myludoapp.socketorchestrator.service.SocketService;
import com.myludoapp.socketorchestrator.util.Constants;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static com.myludoapp.socketorchestrator.model.EventType.*;

@Component
@Slf4j
public class SocketModule {


    private final SocketIOServer server;
    private final SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());


        server.addEventListener((String.valueOf(SEND_MESSAGE)), MessageRequest.class, onSendMessage());
        server.addEventListener(String.valueOf(JOIN_ROOM), MessageRequest.class, join_room());
        server.addEventListener(String.valueOf(DISCONNECT), MessageRequest.class, onDisconnection());
        server.addEventListener(String.valueOf(START_GAME), MessageRequest.class, startGame());
        server.addEventListener(String.valueOf(DICE_ROLL), MessageRequest.class, diceRoll());
        server.addEventListener(String.valueOf(TIME_OUT), MessageRequest.class, timeOut());
        server.addEventListener(String.valueOf(SELECTED_TOKEN), MessageRequest.class, selectedToken());
        server.addEventListener(String.valueOf(SEND_CHAT_MESSAGE), MessageRequest.class, onChatReceived());

    }

    private DataListener<MessageRequest>  onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info("Received chat message ..[{}]", data);
            socketService.sendChatMessage(senderClient, data);
        };
    }

    private DataListener<MessageRequest>  selectedToken() {
        return (senderClient, data, ackSender) -> {
            log.info("Select token request ..[{}]", data);
            socketService.selectedToken(senderClient, data);
        };
    }

    private DataListener<MessageRequest>  timeOut() {
        return (senderClient, data, ackSender) -> {
            log.info("timeout request ..[{}]", data);
            socketService.timeOut(senderClient, data);
        };
    }

    private DataListener<MessageRequest> diceRoll() {
        return (senderClient, data, ackSender) -> {
            log.info("dice roll request ..[{}]", data);
            socketService.diceRoll(senderClient, data);
        };
    }

    private DataListener<MessageRequest>   startGame() {
        return (senderClient, data, ackSender) -> {
            log.info("Start game request ..[{}]", data);
            socketService.startGame(senderClient, data);
        };
    }

    private DataListener<MessageRequest>  onDisconnection() {
        return (senderClient, data, ackSender) -> {
            log.info("Disconnected ..[{}]", data);
            socketService.onDisconnection(senderClient, data);
        };
    }

    private DataListener<MessageRequest> join_room() {
        return (senderClient, data, ackSender) -> {
            log.info("Joining room request ..[{}]", data);
            socketService.onJoinRoom(senderClient, data);
        };
    }


    private DataListener<MessageRequest> onSendMessage() {
        return (senderClient, data, ackSender) -> {
            log.info("Data Received at Server [{}]", data.toString());
            socketService.onSendMessage(senderClient, data);
        };
    }


    private ConnectListener onConnected() {
        return (client) -> {
//            String room = client.getHandshakeData().getSingleUrlParam("room");
//            String userId = client.getHandshakeData().getSingleUrlParam("room");
            var params = client.getHandshakeData().getUrlParams();
            String userId = params.get("userId").stream().collect(Collectors.joining());
//            String room = params.get("room").stream().collect(Collectors.joining());
            //TODO : validate client cred
//            client.joinRoom(room);
            socketService.onConnected(client, String.format(Constants.WELCOME_MESSAGE, userId),userId);
            log.info("Socket ID[{}] - userId [{}]  Connected to server", client.getSessionId().toString(), userId);
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
//            String room = params.get("room").stream().collect(Collectors.joining());
            String userId = params.get("userId").stream().collect(Collectors.joining());

            //TODO : sedn to other clients
            socketService.saveInfoMessage(client, String.format(Constants.DISCONNECT_MESSAGE, userId), userId);
            log.info("Socket ID[{}] - userId [{}]  disconnected from server", client.getSessionId().toString(), userId);
        };
    }


}