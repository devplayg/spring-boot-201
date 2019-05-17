package com.devplayg.coffee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.devplayg.coffee.definition.RoleType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "mbr_role")
@Getter
@EqualsAndHashCode(of = {"role"})
@NoArgsConstructor
@ToString(exclude = "member")  // 순환참조 차단
public class MemberRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long id;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "member_id")
    @JsonIgnore
    @Setter
    private Member member;

    @Column
    @Enumerated(EnumType.STRING)
    @Setter
    private RoleType.Role role;

    @Column
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime created;
}
