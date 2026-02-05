package com.myludoapp.socketorchestrator.service;

import com.myludoapp.socketorchestrator.model.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@NoArgsConstructor
@Slf4j
public class LudoGameService {
    public static String[] Colors = new String[]{"#ff0000", "#0000ff", "#008000", "#ffff00"};

    private GameData gameData;

    public LudoGameService(GameData gameData) {
        log.info("constructing a ludo game...");
        this.gameData = gameData;
    }

    public static String[] getRoute(int index) {
        String[] route = new String[58];
        if (index == 1) {
            route = new String[]{"1-17", "1-16", "1-15", "1-14", "1-13"
                    , "2-1", "2-2", "2-3", "2-4", "2-5", "2-6", "2-7", "2-18", "2-17", "2-16", "2-15", "2-14", "2-13"
                    , "3-1", "3-2", "3-3", "3-4", "3-5", "3-6", "3-7", "3-18", "3-17", "3-16", "3-15", "3-14", "3-13"
                    , "4-1", "4-2", "4-3", "4-4", "4-5", "4-6", "4-7", "4-18", "4-17", "4-16", "4-15", "4-14", "4-13"
                    , "1-1", "1-2", "1-3", "1-4", "1-5", "1-6", "1-7", "1-8", "1-9", "1-10", "1-11", "1-12", "home"};
        } else if (index == 2) {
            route = new String[]{"2-17", "2-16", "2-15", "2-14", "2-13"
                    , "3-1", "3-2", "3-3", "3-4", "3-5", "3-6", "3-7", "3-18", "3-17", "3-16", "3-15", "3-14", "3-13"
                    , "4-1", "4-2", "4-3", "4-4", "4-5", "4-6", "4-7", "4-18", "4-17", "4-16", "4-15", "4-14", "4-13"
                    , "1-1", "1-2", "1-3", "1-4", "1-5", "1-6", "1-7", "1-18", "1-17", "1-16", "1-15", "1-14", "1-13"
                    , "2-1", "2-2", "2-3", "2-4", "2-5", "2-6", "2-7", "2-8", "2-9", "2-10", "2-11", "2-12", "home"};
        } else if (index == 3) {
            route = new String[]{"3-17", "3-16", "3-15", "3-14", "3-13"
                    , "4-1", "4-2", "4-3", "4-4", "4-5", "4-6", "4-7", "4-18", "4-17", "4-16", "4-15", "4-14", "4-13"
                    , "1-1", "1-2", "1-3", "1-4", "1-5", "1-6", "1-7", "1-18", "1-17", "1-16", "1-15", "1-14", "1-13"
                    , "2-1", "2-2", "2-3", "2-4", "2-5", "2-6", "2-7", "2-18", "2-17", "2-16", "2-15", "2-14", "2-13"
                    , "3-1", "3-2", "3-3", "3-4", "3-5", "3-6", "3-7", "3-8", "3-9", "3-10", "3-11", "3-12", "home"};
        } else if (index == 4) {
            route = new String[]{"4-17", "4-16", "4-15", "4-14", "4-13"
                    , "1-1", "1-2", "1-3", "1-4", "1-5", "1-6", "1-7", "1-18", "1-17", "1-16", "1-15", "1-14", "1-13"
                    , "2-1", "2-2", "2-3", "2-4", "2-5", "2-6", "2-7", "2-18", "2-17", "2-16", "2-15", "2-14", "2-13"
                    , "3-1", "3-2", "3-3", "3-4", "3-5", "3-6", "3-7", "3-18", "3-17", "3-16", "3-15", "3-14", "3-13"
                    , "4-1", "4-2", "4-3", "4-4", "4-5", "4-6", "4-7", "4-8", "4-9", "4-10", "4-11", "4-12", "home"};
        }
        return route;
    }

    public void setDiceCastComplete(boolean b) {
        this.gameData.setDiceCastComplete(b);
    }

    public boolean moveToken(String tokenId, String userId) {
        AtomicBoolean retainPos = new AtomicBoolean(false);
        if (tokenId != null) this.gameData.setSelectedTokenId(tokenId);
        if (this.gameData.getPlayers() != null && userId != null) {
            Map<String, List<Token>> tokens = new HashMap<>();
            String[] route = null;
            AtomicReference<Token> playerToken = new AtomicReference<>(Token.builder().build());
            int dice_value = this.gameData.getDice_value();
            //fetch all tokens
            List<Token> tokensList = this.gameData.getPlayers().values().stream().flatMap(p -> p.getHouse().getTokens().stream()).collect(Collectors.toList());
            tokensList.forEach(t -> {
                        t.setActive(false);
                        if (t.getId().equals(tokenId)) {
                            playerToken.set(t);
                        }
                        List<Token> tokenList = tokens.get(t.getPosition());
                        if (tokenList == null) tokenList = new ArrayList<>();
                        tokenList.add(t);
                        tokens.put(t.getPosition(), tokenList);
                    }
            );
            //just for check
//            if (!playerToken.get().getHouse_id().equals(userId)) return false;
            route = this.gameData.getPlayers().get(userId).getHouse().getRoute();
            if (tokens != null && route.length > 0 && playerToken.get() != null) {
                int position = 0;
                if (dice_value == 6 && playerToken.get().getPosition().equals("base")) {
                    //if token on base positon
                    position = 0;
                } else {
                    //fetch new position;
                    for (int i = 0; i < route.length; i++) {
                        if (route[i].equals(playerToken.get().getPosition())) {
                            position = i + dice_value;
                        }
                    }
                }

                //change token position,
                if (position >= 0 && route.length > position) {
                    playerToken.get().setPosition(route[position]);
                    int homecount = 0;
                    List<Token> tokenList = this.gameData.getPlayers().get(userId).getHouse().getTokens();
                    for (Token token : tokenList) {
                        if (playerToken.get().getId().equals(token.getId())) {
                            token.setPosition(route[position]);
                            if(position>0){
                                //set token positions to be moved
                                String[] moveTokenPositions= moveTokenPositions(position,route,dice_value);
                                this.gameData.setMoveTokenPositions(moveTokenPositions);
                            }

                            if (token.getPosition().equals("home")) {
                                //TODO : retain player turn
                                String[] home = this.gameData.getPlayers().get(userId).getHouse().getHome();
                                if (home == null) home = new String[this.gameData.getToken_count()];
                                int c = 0;
                                for (int k = 0; k < home.length; k++) {
                                    if (home[k] != null) c++;
                                }

                                home[c] = token.getId();
                                this.gameData.getPlayers().get(userId).getHouse().setHome(home);
                                retainPos.set(true);
                            }
                        }
                        if (token.getPosition().equals("home")) {
                            homecount++;
                        }
                    }
                    if (homecount == this.gameData.getToken_count()) {
                        String[] home = this.gameData.getHome();
                        if (home == null) home = new String[this.gameData.getPlayer_count()];
                        int c = 0;
                        for (String s : home) {
                            if (s != null) c++;
                        }
                        home[c] = userId;
                        this.gameData.setHome(home);
                    }
                    this.finishGame();
                }
                //kill a token ,if any
                if (!playerToken.get().getPosition().equalsIgnoreCase("base") && !playerToken.get().getPosition().equalsIgnoreCase("home")) {
                    String position1 = playerToken.get().getPosition();
                    String[] splitPosition = position1.split("-");
                    if (Objects.nonNull(splitPosition[1])&& !splitPosition[1].equalsIgnoreCase("4") && !splitPosition[1].equalsIgnoreCase("17")) {
                        List<Token> _tokens = tokens.get(playerToken.get().getPosition());
                        if (_tokens != null && _tokens.size() > 0) {
                            _tokens.forEach(token -> {
                                if (!token.getPosition().equals("home") && !token.getHouse_id().equals(playerToken.get().getHouse_id())) {
                                    this.gameData.getPlayers().values().stream().flatMap(p -> p.getHouse().getTokens().stream()).collect(Collectors.toList()).forEach(t -> {
                                        if (t.getId().equals(token.getId())) {
                                            t.setPosition("base");
                                            retainPos.set(true);
                                        }
                                    });
                                    ;
                                }
                            });
                        }
                    }
                }

            }
//            if (retainPos.get()) this.gameData.setMove_token(true);
            return retainPos.get();
        }

        return false;
    }

    private String[] moveTokenPositions(int position, String[] route, int dice_value) {
        String[] positions=new String[dice_value];
        for (int i = 0; i < dice_value; i++) {
            positions[i]=route[position-dice_value-i];
        }
        return positions;
    }

    private void finishGame() {
        String[] home = this.gameData.getHome();
        int c = 0;
        for (String s : home) {
            if (s != null) c++;
        }
        //TODO :
        if (c == (this.gameData.getPlayer_count() - 1)) {
            log.info("finishing game...[{}]", this.gameData.getGame().getId());
            this.gameData.setHas_stopped(true);
            this.gameData.setHas_started(false);
        }

    }

    public void setPlayerTurn() {
        log.info("setting player turn");
        int dice_value = this.gameData.getDice_value();
        int player_turn = this.gameData.getPlayer_turn();
        if (dice_value != 6) {
            player_turn = player_turn + 1;
//            if (player_turn > this.gameData.getPlayer_count())
//                player_turn = 1; //rolling turns

            String[] home = this.gameData.getHome();
            if(home.length!=0){
                for(int i=player_turn;;){
                    if (i > this.gameData.getPlayer_count()) i=1;
                    String player = this.gameData.getTurns().get(i);
                    //check if next player is home
                    boolean isHome = isHome(home, player);
                    if(isHome) {
                        i++;
                    }
                    else {
                        player_turn=i;
                        break;
                    }
                }
            }
        }
        this.gameData.setPlayer_turn(player_turn);
    }

    private static boolean isHome(String[] home, String player) {
        if(Objects.isNull(player)) return  false;
        boolean isHome=false;
        if(home.length==0)  return false;
        for(String s: home){
            if(Objects.nonNull(s)&& s.equalsIgnoreCase(player)) {
                isHome = true;
                break;
            }
        }
        return isHome;
    }

    public GameData getGameData() {
        return this.gameData;
    }

    public void setPreviousDiceValues(Integer dice_value){
        if(this.gameData.getPreviousDiceValues()!=null) {
            int player_turn=this.gameData.getPlayer_turn();
            Integer[] previousDiceValues = this.gameData.getPreviousDiceValues().get(player_turn);
            if(previousDiceValues!=null){
                previousDiceValues[3]=previousDiceValues[2];
                previousDiceValues[2]=previousDiceValues[1];
                previousDiceValues[1]=previousDiceValues[0];
                previousDiceValues[0]=dice_value;
            }
        }
    }
    public void setDiceValue(int diceValue) {
        if (diceValue > 0) {
            this.gameData.setDice_value(diceValue);
        }
    }

    public Map<String, Boolean> enableTokens(MessageRequest data) {
        List<Token> tokensActive = new ArrayList<>();
        String userId = data.getUserId();
        int diceValue = data.getDiceValue();

        if (this.gameData.getPlayers() != null && userId != null) {
            Player player = this.gameData.getPlayers().get(userId);
            if (player != null) {
                House house = player.getHouse();
                if (house != null) {
                    String[] routes = house.getRoute();
                    house.getTokens().
                            forEach(token -> {
                                if (!token.getPosition().equals("home")) {
                                    int newPosition = 0;
                                    for (int i = 0; i < routes.length; i++) {
                                        if (Objects.equals(routes[i], token.getPosition())) {
                                            newPosition = i + diceValue;
                                        }
                                    }
                                    //TODO
                                    if (diceValue == 6 && token.getPosition().equals("base")) {
                                        token.setActive(true);
                                        tokensActive.add(token);
                                    } else if (!Objects.equals(token.getPosition(), "base") && newPosition >= 0 && routes.length > newPosition) {
                                        token.setActive(true);
                                        tokensActive.add(token);
                                    }
                                }
                            });

                }
            }
        }
        Map<String, Boolean> results = new HashMap<>();
        boolean retainPos = false;
        boolean movedToken = false;
        String selectedTokenId = null;
        if (tokensActive.size() == 1) {
            //move token;
            Token token = tokensActive.get(0);
            if (token != null) {
                selectedTokenId = token.getId();
                retainPos = this.moveToken(token.getId(), userId);
                movedToken = true;
            }
        } else if (tokensActive.size() > 0) {
            retainPos = true;
        }
        results.put("retainPos", retainPos);
        results.put("movedToken", movedToken);
        if (movedToken) {
            this.gameData.setMove_token(true);
            if (selectedTokenId != null) this.gameData.setSelectedTokenId(selectedTokenId);
        }
        return results;
    }

    public boolean startGame() {
        int playerCount = 0;
        for (String key : this.gameData.getPlayers().keySet()) {
            Player player = this.gameData.getPlayers().get(key);
            if (player != null && player.isActive()) {//only active players
                playerCount++;
            }
        }
        if (this.gameData != null && !this.gameData.isHas_started() && playerCount == this.gameData.getPlayer_count()) {
            this.gameData.setPlayer_turn((int) (Math.floor(Math.random() * this.gameData.getPlayer_count()) + 1));
            this.gameData.setHas_started(true);
            this.gameData.setHas_stopped(false);
        }
//        if (this.gameData.isHas_started()) {
//            log.info("setting all ludo tokens to inactive .....");
//            List<Token> tokens = this.getGameData().getPlayers().values().stream().flatMap(p -> p.getHouse().getTokens().stream()).collect(Collectors.toList());
//            tokens.forEach(t -> t.setActive(false));
//        }
        //TODO to be removed
        // this.gameData.player_turn = Math.floor(Math.random() * this.gameData.player_count) + 1;
        // this.gameData.has_started = true;
        // this.gameData.has_stopped = false;
        return this.gameData.isHas_started();
    }

//    public void timeOut() {
//        int randomNum = (int) (Math.floor(Math.random() * 6) + 1);
//        this.gameData.setDice_value(randomNum);
//        this.setPlayerTurn();
//    }
}
