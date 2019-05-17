package com.devplayg.coffee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.devplayg.coffee.definition.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mbr_member")
@Getter
@ToString
@NoArgsConstructor
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private long id;

    @Column(name = "username", unique = true, length = 32)
    @NotBlank
    private String username;

    @Column(nullable = false, length = 254)
    @NotBlank
    private String email;

    @Column(name = "name", length = 64)
    @NotBlank
    private String name;

    @Column(length = 64)
    @JsonIgnore
    @ToString.Exclude
    private String password;

    @Column(name = "password_salt", length = 32)
    @JsonIgnore
    @ToString.Exclude
    private String passwordSalt = "";

    // 사용자 권한
    @OneToMany(fetch = FetchType.EAGER,
            orphanRemoval = true,
            cascade = {
                    CascadeType.PERSIST, // Child entities이 삭제 되도록 함
                    CascadeType.MERGE // Child entities를 Insert할 때, Parent ID를 기록한 후 Insert 함
            },
            mappedBy = "member")
    private List<MemberRole> roleList;

    @Column(nullable = false)
    private boolean enabled = false;

    // For use on view(Thymeleaf template)
    @JsonIgnore
    public List<RoleType.Role> getRoleEnumList() {
        List<RoleType.Role> list = new ArrayList<>();
        for (MemberRole role : roleList) {
            list.add(role.getRole());
        }
        return list;
    }
}