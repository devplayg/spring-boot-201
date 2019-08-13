package com.devplayg.coffee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name="ast_device")
@IdClass(DeviceId.class)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @Column(name = "asset_id")
    private long assetId;

    @Id
    private String uid;

    private int category;
    private int type;
    private long code;
    private String name;
    private String hostname;
    private int port1;
    private int port2;
    private int port3;
    private int port4;

    @Column(name = "manufactured_by")
    private String manufacturedBy;

    private String model;
    private String version;
    private String firmware;

    @JsonIgnore
    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String apiKey;
    private Boolean enabled;

    private String url;
    private String timezone;
    private String status;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;
}
