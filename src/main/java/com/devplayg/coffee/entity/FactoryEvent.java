package com.devplayg.coffee.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_factory")
@Getter
@Setter
@ToString
public class FactoryEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private long factoryId;

    private long cameraId;

    private int eventType;

    private int originEventType;

    private String path;

    private int archived;

    private String targetId;

    private String meta;

    @CreationTimestamp
    private LocalDateTime created;

    @Transient
    private MultipartFile file;

    @Transient
    private Asset asset;
}
