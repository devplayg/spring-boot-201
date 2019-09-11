package com.devplayg.coffee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name="ast_device")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long deviceId;

    private long assetId;
    private int category;
    private int type;
    private long code;
    private String name;
    private String hostname;
    private int port1;
    private int port2;
    private int port3;
    private int port4;
    private String manufacturedBy;
    private String model;
    private String version;
    private String firmware;

    @JsonIgnore
    private String username;

    @JsonIgnore
    private String password;

    private String apiKey;
    private Boolean enabled;
    private String url;
    private String timezone;
    private String status;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    // serial, cpu_usage, memory_usage, disk_usage, memory_comment, disk_comment

    @Transient
    private Object policy;
}
