package com.myludoapp.socketorchestrator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "game_infos")
public class GameInfoDto  {
    @Id
    @Column(name = "id",columnDefinition = "VARCHAR(36)")
    private String id;
    @Column(name = "game_id",columnDefinition = "VARCHAR(36)")
    private String gameId;
    @Column(name = "user_id",columnDefinition = "VARCHAR(36)")
    private String userId;
    @Column(name = "color")
    private String color;
    @Column(name = "active")
    private boolean active;


}
