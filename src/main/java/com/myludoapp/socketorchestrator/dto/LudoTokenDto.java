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
@Table(name = "ludo_tokens")
public class LudoTokenDto {
    @Id
    @Column(name = "id",columnDefinition = "VARCHAR(36)")
    private String id;
    @Column(name = "house_id",columnDefinition = "VARCHAR(36)")
    private String houseId;
    @Column(name = "token_id")
    private String tokenId;
    @Column(name = "color")
    private String color;
    @Column(name = "position")
    private String position;
    @Column(name = "active")
    private boolean active;
}
