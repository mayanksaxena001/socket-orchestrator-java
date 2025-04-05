package com.myludoapp.socketorchestrator.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "messages")
public class MessageDto {
    @Id
    @Column(name = "id",columnDefinition = "VARCHAR(36)")
    private String id;
    private String messageType;

    private String content;
    private String room;

    private String userId;

}