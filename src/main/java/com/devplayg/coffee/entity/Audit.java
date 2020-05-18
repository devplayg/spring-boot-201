package com.devplayg.coffee.entity;

import com.devplayg.coffee.converter.AuditCategoryConverter;
import com.devplayg.coffee.definition.AuditCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
    private long id;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Member member;

    @Column(length = 16, nullable = false)
    @Convert(converter = AuditCategoryConverter.class)
    @NotFound(action = NotFoundAction.IGNORE)
    private AuditCategory category;

    @Column(nullable = false)
    private long ip;

    @Column(length = 4096)
    private String message;

    @Column
    @CreationTimestamp
    private LocalDateTime created;
}
