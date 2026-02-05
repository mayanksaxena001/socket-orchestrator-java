package com.myludoapp.socketorchestrator.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.myludoapp.socketorchestrator.dto.MessageDto;
import com.myludoapp.socketorchestrator.model.*;
import com.myludoapp.socketorchestrator.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.myludoapp.socketorchestrator.model.MessageType.CLIENT;
import static com.myludoapp.socketorchestrator.model.MessageType.SERVER;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {
    private final MessageService messageService;
    private LudoGameService ludoGameService;
    private final CacheStorage cacheStorage;

    public void sendEventToOtherClients(EventType eventType, SocketIOClient senderClient, Object message, String room) {
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            client.sendEvent(String.valueOf(eventType),
                    message);
//            if (!client.getSessionId().equals(senderClient.getSessionId())) {
//            }
        }
    }

    public void saveInfoMessage(SocketIOClient senderClient, String message, String userId,String room) {
        MessageDto saveMessage = MessageDto.builder()
                .messageType(SERVER.toString())
                .content(message)
                .userId(userId)
                .room(Integer.parseInt(room))
//                .createdAt(new Date())
//                .updatedAt(new Date())
                .build();
//        MessageDto storedMessage = messageService.saveMessage(saveMessage);
//        sendEventToOtherClients(EventType.RECEIVED_MESSAGE, senderClient, Util.toMessage(saveMessage), room+"");
    }

    public void onSendMessage(SocketIOClient senderClient, MessageRequest data) {
        sendEventToOtherClients(EventType.RECEIVED_MESSAGE, senderClient, data, data.getRoom());
    }

    public void onJoinRoom(SocketIOClient senderClient, MessageRequest data) {
        if (data.getGameId() == null) return;
        //update player in cache if not present
        cacheStorage.updatePlayer(data.getRoom(), data.getGameId(), data.getUserId());
        //join the room
        senderClient.joinRoom(data.getRoom());
        GameData cacheData = cacheStorage.getGameCache(data.getGameId());
//        ludoGameService = new LudoGameService(cacheData);
        senderClient.sendEvent(String.valueOf(EventType.RECEIVED_MESSAGE),
                cacheData);
        sendEventToOtherClients(EventType.PLAYER_JOINED, senderClient, data.getUserId(), data.getRoom());
    }

    public void onDisconnection(SocketIOClient senderClient, MessageRequest data) {

    }

    public void startGame(SocketIOClient senderClient, MessageRequest data) {
        log.info("starting game..[{}]", data);
        GameData cacheData = cacheStorage.getGameCache(data.getGameId());
        ludoGameService = new LudoGameService(cacheData);
        boolean hasStarted = ludoGameService.startGame();
        if (hasStarted) {
            log.info("game has started..");
            sendUpdatedData(ludoGameService, data, senderClient);
        }else {
            log.info("Unable to start game..waiting for players..");
        }
    }

    public void diceRoll(SocketIOClient senderClient, MessageRequest data) {

        log.info("dice value received...[{}]", data.getDiceValue());
        if (data.getGameId() != null) {
            GameData cacheData = cacheStorage.getGameCache(data.getGameId());
            //reset
            cacheData.setMove_token(false);
            cacheData.setSelectedTokenId("");
            cacheData.setMoveTokenPositions(new String[]{});

            ludoGameService = new LudoGameService(cacheData);
            ludoGameService.setDiceValue(data.getDiceValue());
            ludoGameService.setPreviousDiceValues(data.getDiceValue());
            Map<String, Boolean> result = ludoGameService.enableTokens(data);
            if (result.get("retainPos") && !result.get("movedToken"))
                ludoGameService.setDiceCastComplete(true);
            else {
                ludoGameService.setDiceCastComplete(false);
                if (!result.get("retainPos") || !(result.get("retainPos") && result.get("movedToken"))) {
                    ludoGameService.setPlayerTurn();
                }
            }
            sendUpdatedData(ludoGameService, data, senderClient);
        }
    }

    public void timeOut(SocketIOClient senderClient, MessageRequest data) {
        log.info("time out..[{}]", data);
        if (data != null) {
            Boolean timeoutCache = cacheStorage.getTimeoutCache(data.getUserId());
            if (timeoutCache == null) {
                cacheStorage.updateTimeoutCache(data.getUserId(), true);
                GameData cacheData = cacheStorage.getGameCache(data.getGameId());
                if (cacheData.isDiceCastComplete()) {
                    //randomly select token
                    String tokenId = "";
                    Player player = cacheData.getPlayers().get(data.getUserId());
                    List<Token> tokens = player.getHouse().getTokens().stream().filter(Token::isActive).collect(Collectors.toList());
                    int randomNum = (int) (Math.floor(Math.random() * tokens.size()) + 1);

                    data.setTokenId(tokens.get(randomNum-1).getId());
                    selectedToken(senderClient,data);
                } else {
                    //randomly select dice value
                    int randomNum = (int) (Math.floor(Math.random() * 6) + 1);
                    data.setDiceValue(randomNum);
                    diceRoll(senderClient, data);
                }
//                cacheStorage.updateTimeoutCache(data.getUserId(), false);
                cacheStorage.clearTimeoutCache(data.getUserId());
            }
        }
    }

    public void selectedToken(SocketIOClient senderClient, MessageRequest data) {
        log.info("selected token...[{}]", data);
        GameData cacheData = cacheStorage.getGameCache(data.getGameId());
        ludoGameService = new LudoGameService(cacheData);
//        cacheData.setMove_token(true);
        //TODO ; updated position
        ludoGameService.setDiceCastComplete(false);
        boolean retainPos = ludoGameService.moveToken(data.getTokenId(), data.getUserId());
        if (!retainPos) ludoGameService.setPlayerTurn();
        sendUpdatedData(ludoGameService, data, senderClient);
    }

    private void sendUpdatedData(LudoGameService ludoGameService, MessageRequest data, SocketIOClient senderClient) {
        GameData gameData = ludoGameService.getGameData();
        senderClient.sendEvent(String.valueOf(EventType.RECEIVED_MESSAGE),
                gameData);
        sendEventToOtherClients(EventType.RECEIVED_MESSAGE, senderClient, gameData, data.getRoom());
        gameData.setMove_token(false);
        //save to cache db;
        if (data.getGameId() != null) cacheStorage.updateGameCache(data.getGameId(), gameData);
    }

    public void sendChatMessage(SocketIOClient senderClient, MessageRequest message) {
        MessageDto saveMessage = MessageDto.builder()
                .messageType(CLIENT.toString())
                .content(message.getContent())
                .room(Integer.parseInt(message.getRoom()))
                .userId(message.getUserId())
//                .createdAt(new Date())
//                .updatedAt(new Date())
                .build();
        MessageDto storedMessage = messageService.saveMessage(saveMessage);//save to db
        sendEventToOtherClients(EventType.CHAT_MESSAGE_RECEIVED, senderClient, Util.toMessage(storedMessage), message.getRoom());
    }

    public void onConnected(SocketIOClient client, String format, String userId) {
        MessageDto storedMessage = MessageDto.builder()
                .messageType(SERVER.toString())
                .content(format)
                .userId(userId)
                .build();
//        storedMessage = messageService.saveMessage(storedMessage);
        client.sendEvent(String.valueOf(EventType.RECEIVED_MESSAGE),
                Util.toMessage(storedMessage));
    }
}