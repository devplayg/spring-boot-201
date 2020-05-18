package com.devplayg.coffee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ast_asset")
@ToString
@NoArgsConstructor
public class Asset {
    public final static int ALL = 0;
    public final static int ORG = 1;
    public final static int GROUP = 2;

    public final static long ROOT_ID = -1;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private long id;

    private long parentId;
    private int type;

    @JsonIgnore
    private int category;

    private String name;
    private String code;

    @JsonIgnore
    private int seq;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime created;

    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updated;

    @Transient
    @JsonIgnore
    private Asset parent;

    @Transient
    private List<Asset> children;
}
