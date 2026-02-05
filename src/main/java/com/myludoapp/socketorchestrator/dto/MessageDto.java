package com.myludoapp.socketorchestrator.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "messages")
public class MessageDto {
    @Column(name = "id",columnDefinition = "VARCHAR(36)")
    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    @Column(name = "message_type")
    private String messageType;

    @Column(name = "content",columnDefinition = "VARCHAR(256)")
    private String content;
    @Column(name = "room")
    private int room;

    @Column(name = "user_id",columnDefinition = "VARCHAR(36)")
    private String userId;

//    @Column(name = "created_at")
//    private String createdAt;
//
//    @Column(name = "updated_at")
//    private Date updatedAt;

}