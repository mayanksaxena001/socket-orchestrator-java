package com.myludoapp.socketorchestrator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Player {
    private String id;
    private int player_turn;
    private String color;

    private House house;

    private boolean active;

    private String username;
    private boolean disabled;
}
