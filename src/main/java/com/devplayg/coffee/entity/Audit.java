package com.devplayg.coffee.entity;

import com.devplayg.coffee.converter.AuditCategoryConverter;
import com.devplayg.coffee.definition.AuditCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adt_audit")
@Getter
public class Audit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private long id;

//    @ManyToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
//    @JoinColumn(name = "member_id")
//    private Member member;

//    @Column(length = 16, nullable = false)
//    @Convert(converter = AuditCategoryConverter.class)
//    private AuditCategory category;
//

    @Column(length = 16, nullable = false)
    @Convert(converter = AuditCategoryConverter.class)
    private AuditCategory category;

    @Column(length = 16, nullable = false)
    private String action;

    @Column(nullable = false)
    private Integer ip;

    @Column(length = 512)
    private String message;

//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name="audit_id")
//    private List<AuditDetail> details;

    @Column
    @CreationTimestamp
    private LocalDateTime created;

}