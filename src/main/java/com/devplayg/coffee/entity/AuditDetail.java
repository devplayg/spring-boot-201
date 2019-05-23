package com.devplayg.coffee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "adt_audit_detail")
@Getter
public class AuditDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    @JsonIgnore
    private long id;

    @Column(name = "audit_id")
    private long auditID;

    private String detail;
}
