package com.myludoapp.socketorchestrator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Game {
    private String id;
    private int room;
    private String created_by;

    private boolean active;

    private String createdAt;
    private String updatedAt;
}
