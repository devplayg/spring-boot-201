package com.devplayg.coffee.entity;

import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.entity.listener.MemberListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "mbr_member")
@Getter @Setter
@ToString
@NoArgsConstructor
@EntityListeners(MemberListener.class)
public class Member implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable= false)
    private long id;

    @Column(name = "username", length = 32, nullable = false, updatable= false)
    @Pattern(regexp = "^[A-Za-z0-9_]{4,15}$")
    private String username;

    @Column(length = 254, nullable = false)
    @Email
    private String email;

    @Column(name = "name", length = 32, nullable = false)
    @Length(min = 4, max = 16)
    private String name;

    @Transient
    @JsonIgnore
    @ToString.Exclude
    @Length(min = 8, max = 16)
    private String inputPassword;

    @Column(length = 72, nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private String password;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(nullable = false)
    @JsonIgnore
    private int roles;

    @Column(nullable = false, length = 32)
    private String timezone;

    @Column(updatable= false)
    @CreationTimestamp
    private LocalDateTime created;

    @Column
    @UpdateTimestamp
    private LocalDateTime updated;

    @Transient
    @JsonIgnore
    private List<RoleType.Role> roleList = new ArrayList<>();

    @JsonProperty("roleList")
    public List<RoleType.Role> getRolesKeys() {
        List<RoleType.Role> roles = Arrays.stream(RoleType.Role.values())
                .filter(r -> (r.getValue() & this.roles) > 0)
                .collect(Collectors.toList());
        return roles;
    }
//    // 사용자 권한
//    @OneToMany(fetch = FetchType.EAGER,
//            orphanRemoval = true,
//            cascade = {
//                    CascadeType.PERSIST, // Child entities이 삭제 되도록 함
//                    CascadeType.MERGE // Child entities를 Insert할 때, Parent ID를 기록한 후 Insert 함
//            },
//            mappedBy = "member")
//    private List<MemberRole> roleList = new ArrayList<>();
}