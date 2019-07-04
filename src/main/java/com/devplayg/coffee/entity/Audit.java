package com.devplayg.coffee.entity;

import com.devplayg.coffee.converter.AuditCategoryConverter;
import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.entity.view.AuditView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "adt_audit")
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Audit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    @JsonView(AuditView.Normal.class)
    private long id;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    @JsonView(AuditView.Normal.class)
    private Member member;

    @Column(length = 16, nullable = false)
    @Convert(converter = AuditCategoryConverter.class)
    @JsonView(AuditView.Normal.class)
    private AuditCategory category;

    @Column(nullable = false)
    @JsonView(AuditView.Normal.class)
    private int ip;

    @Column(length = 4096)
    @JsonView(AuditView.Normal.class)
    private String message;

    @Column
    @CreationTimestamp
    @JsonView(AuditView.Normal.class)
    private LocalDateTime created;
}