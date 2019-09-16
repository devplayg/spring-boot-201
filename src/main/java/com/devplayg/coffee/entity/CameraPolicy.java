package com.devplayg.coffee.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "pol_camera_vas")
@ToString
@NoArgsConstructor
public class CameraPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long policyId;
    private long deviceId;
    private long assetId;
    private String code;

    private int obstacle;
    private int abnormalCondition;
    private int crush;

    private boolean intrusion;
    private boolean noPpe;
    private boolean fire;
    private boolean fume;
    private boolean falling;
    private boolean spark;

    private boolean holiday;
    private int officeHourFrom;
    private int officeHourTo;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;
}