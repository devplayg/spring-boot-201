package com.devplayg.coffee.entity;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "adt_audit")
@Getter
public class Audit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 32)
    private String category;

    @Column
    private Integer ip;

    @Column(length = 256)
    private String message;

    @Column(length = 4096)
    private String detail;

    @Column
    private LocalDateTime created;
}