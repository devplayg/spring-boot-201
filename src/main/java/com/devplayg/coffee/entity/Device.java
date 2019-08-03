package com.devplayg.coffee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ast_device")
@Getter
@ToString
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private long id;

    @Column(name = "asset_id")
    private long assetId;

    private int category;
    private int type;
    private int code;
    private String name;
    private String hostname;
    private int port1;
    private int port2;
    private int port3;
    private int port4;

    @Column(name="manufactured_by")
    private String manufacturedBy;

    private String model;
    private String version;
    private String firmware;
    private String username;

    @JsonIgnore
    private String password;
    private Boolean enabled;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;
}
