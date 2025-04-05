package com.myludoapp.socketorchestrator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "games")
public class GameDto {
    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;
    @Column(name = "room")
    private int room;
    @Column(name = "player_count")
    private int player_count;
    @Column(name = "token_count")
    private int token_count;
    @Column(name = "player_turn")
    private int player_turn;
    @Column(name = "dice_value")
    private int dice_value;
    @Column(name = "time_out")
    private int time_out;
    @Column(name = "created_by", columnDefinition = "VARCHAR(36)")
    private String created_by;
    @Column(name = "active")
    private boolean active;
}
