package com.devplayg.coffee.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "msg_message")
@Getter
@Setter
@ToString
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private long id;

    @CreationTimestamp
    private LocalDateTime created;

    private int priority;
    private long receiverId;
    private long senderId;
    private String category;
    private String message;
    private boolean read;
    private String url;
}
