package com.myludoapp.socketorchestrator.service;

import com.myludoapp.socketorchestrator.dto.GameDto;
import com.myludoapp.socketorchestrator.dto.GameInfoDto;
import com.myludoapp.socketorchestrator.dto.LudoTokenDto;
import com.myludoapp.socketorchestrator.dto.UserDto;
import com.myludoapp.socketorchestrator.model.*;
import com.myludoapp.socketorchestrator.repository.GameInfoRepository;
import com.myludoapp.socketorchestrator.repository.GameRepository;
import com.myludoapp.socketorchestrator.repository.LudoTokenRepository;
import com.myludoapp.socketorchestrator.repository.UserRepository;
import com.myludoapp.socketorchestrator.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Component
@Slf4j
public class CacheStorage {

    ConcurrentHashMap<String, GameData> gameCache;

    ConcurrentHashMap<String, Boolean> timeoutCache;

    @Autowired
    ExecutorService executorService;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GameInfoRepository gameInfoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LudoTokenRepository ludoTokenRepository;

    public CacheStorage() {
        log.info("CacheStorage constructor..");
        gameCache = new ConcurrentHashMap<String, GameData>();
    }

    public GameData getGameCache(String key) {
        return gameCache.get(key);
    }

    public GameData updateGameCache(String key, GameData gameData) {
        //TODO update game;
        return gameCache.put(key, gameData);
    }

    public Boolean getTimeoutCache(String key) {
        return timeoutCache.get(key);
    }

    public void updateTimeoutCache(String key, Boolean timer) {
        //TODO update game;
      timeoutCache.put(key, timer);
    }

    public Boolean clearTimeoutCache(String key) {
        //TODO update game;
        return timeoutCache.remove(key);
    }


    @PostConstruct
    void postConstruct() {
        CompletableFuture.runAsync(this::startloop, executorService);
    }

    private void startloop() {
        try {
            log.info("initializing cache data....start");
            List<GameDto> games = gameRepository.findAll();
            for (GameDto gameDto : games) {
                log.info("initializing cache data....gameDto [{}]", gameDto);
                populateGameData(gameDto);
            }
            log.info("initializing cache data....done");
        } catch (Exception e) {
            log.info("initializing cache data....error : [{}]", e.toString());
        } finally {
        }
    }

    private void populateGameData(GameDto gameDto) {
        Game game = Util.toGame(gameDto);
        //TODO populate game
        GameData gameData = GameData.builder()
                .game(game)
                .dice_value(gameDto.getDice_value())
                .player_count(gameDto.getPlayer_count())
                .time_out(gameDto.getTime_out())
                .token_count(gameDto.getToken_count())
                .player_turn(gameDto.getPlayer_turn())
                .home(new String[gameDto.getPlayer_count()])
                .build();
        List<GameInfoDto> gameInfoDtos = gameInfoRepository.findAllByGameId(gameDto.getId());
        log.info("populateGameData [{}]", gameInfoDtos);
        extractData(gameInfoDtos, gameData);
        log.info("pushing into gameCache [{}]", gameData);
        gameCache.put(game.getId(), gameData);
    }

    private void extractData(List<GameInfoDto> gameInfoDtos, GameData gameData) {
        Map<String, Player> players = new HashMap<>();
        Map<Integer, String> turns = new HashMap<>();
        Map<Integer, Integer[]> previousDiceValues = new HashMap<>();
        int index = 0;
        for (GameInfoDto gameInfoDto : gameInfoDtos) {
            log.info("gameInfoDto [{}]", gameInfoDto);
            if (null != gameInfoDto) {
                int key = index + 1;
                String user_id = gameInfoDto.getUserId();
                UserDto userDto = userRepository.findById(user_id).orElse(null);
                String color = LudoGameService.Colors[index];
                List<Token> tokens = extractTokens(key, color, gameInfoDto.getId());
                House house = House.builder()
                        .id(gameInfoDto.getId())
                        .color(color)
                        .tokens(tokens)
                        .route(LudoGameService.getRoute(key))
                        .home(new String[gameData.getPlayer_count()])
                        .build();
                Player player = Player.builder().id(user_id)
                        .color(color)
                        .username(userDto.getUsername())
                        .player_turn(key)
                        .house(house)
                        .build();
//                if (gameInfoDto.isActive()) {
//                    player.getHouse().setActive(gameInfoDto.isActive());
//                    player.setActive(gameInfoDto.isActive());
//                }
                players.put(user_id, player);
                turns.put(key, player.getId());
                previousDiceValues.put(key,new Integer[]{0,0,0,0});
                index++;
            }
        }
        gameData.setPlayers(players);
        gameData.setTurns(turns);
        gameData.setPreviousDiceValues(previousDiceValues);
    }

    private List<Token> extractTokens(Integer key, String color, String id) {
        List<Token> tokens = new ArrayList<>();
        int count = 0;
        List<LudoTokenDto> ludoTokenDtos = ludoTokenRepository.findAllByHouseId(id);
        for (LudoTokenDto ludoTokenDto : ludoTokenDtos) {
            log.info("ludoTokenDto [{}]", ludoTokenDto);
            Token token = Token.builder()
                    .id(ludoTokenDto.getId())
                    .house_id(ludoTokenDto.getHouseId())
                    .token_id(key + ":" + (count + 1))
                    .color(color)
                    .position(ludoTokenDto.getPosition()).build();
//            if (ludoTokenDto.isActive()) {
//                token.setActive(ludoTokenDto.isActive());
////                token.setDisabled(!ludoTokenDto.isActive());
//            }
            if (null != token) tokens.add(token);
            count++;
        }
        return tokens;
    }

    public void updatePlayer(String room, String gameId, String userId) {
        GameData gameData = gameCache.get(gameId);
        Player playerObj = null;
        if (gameData == null) {
            GameDto gameDto = gameRepository.findById(gameId).orElse(null);
            if (Objects.nonNull(gameDto)) {
                populateGameData(gameDto);
                gameData = gameCache.get(gameId);
                playerObj = gameData.getPlayers().get(userId);
            }
        } else {
            Map<String, Player> players = gameData.getPlayers();
            playerObj = players.get(userId);
            if (playerObj == null) {
                GameDto gameDto = gameRepository.findById(gameId).orElse(null);
                if (Objects.nonNull(gameDto)) populateGameData(gameDto);
            }
        }
        if (playerObj != null) {
            playerObj.setActive(true);
            playerObj.setDisabled(false);
            playerObj.getHouse().setActive(true);
            playerObj.getHouse().setDisabled(false);
            Map<String, Player> players = gameData.getPlayers();
            players.put(userId, playerObj);
            gameData.setPlayers(players);
            updateGameCache(gameId, gameData);
        }
    }
}
