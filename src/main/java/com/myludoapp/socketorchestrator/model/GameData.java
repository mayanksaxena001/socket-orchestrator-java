package com.myludoapp.socketorchestrator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GameData {
    private Game game;

    private boolean has_started;
    private boolean has_stopped;
    private int player_count=-1;
    private int token_count=-1;
    private int time_out=-1;

    private int dice_value=-1;

    private int player_turn=-1;

    private boolean diceCastComplete;

    private boolean move_token;

    private String selectedTokenId;

    private Map<String, Player> players;

    private Map<Integer, String> turns;

    private String[] home;

    private String[] moveTokenPositions;

    private Map<Integer, Integer[]> previousDiceValues;

}
