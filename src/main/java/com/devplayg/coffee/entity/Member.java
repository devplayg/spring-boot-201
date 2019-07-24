package com.devplayg.coffee.entity;

import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.entity.view.AuditView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Member entity which implements UserDetails, CredentialsContainer.
 */
@Entity
@Table(name = "mbr_member")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"roleList", "updated"})
public class Member implements UserDetails, CredentialsContainer, Serializable {
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @JsonView({AuditView.Normal.class})
    private long id;

    @Column(name = "username", length = 32, nullable = false, updatable = false)
    @Pattern(regexp = "^[a-zA-Z]{1}[a-zA-Z0-9_]{2,16}$")
    @JsonView({AuditView.Normal.class})
    private String username;

    @Column(length = 254, nullable = false)
    @Email
    @JsonView({AuditView.Normal.class})
    private String email;

    @Column(name = "name", length = 32, nullable = false)
    @Length(min = 4, max = 16)
    @JsonView({AuditView.Normal.class})
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
    private boolean enabled ;

    @Column(nullable = false)
    @JsonIgnore
    private int roles;

    @Column(nullable = false, length = 32)
    private String timezone;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @Transient
    private Boolean changed;

    @Transient
    @JsonIgnore
    private List<RoleType.Role> roleList = new ArrayList<>();

    @Transient
    @JsonProperty("roleList")
    private List<RoleType.Role> getRoleNameList() {
        List<RoleType.Role> roles = Arrays.stream(RoleType.Role.values())
                .filter(r -> (r.getValue() & this.roles) > 0)
                .collect(Collectors.toList());
        return roles;
    }

    // Accessible IP list
    @OneToMany(fetch = FetchType.EAGER,
            orphanRemoval = true,
            cascade = {
                    CascadeType.PERSIST, // Child entities이 삭제 되도록 함
                    CascadeType.MERGE // Child entities를 Insert할 때, Parent ID를 기록한 후 Insert 함
            },

            mappedBy = "member")
    private List<MemberNetwork> accessibleIpList = new ArrayList<>();

    // Text to accessible IP list
    @Transient
    @JsonIgnore
    private String accessibleIpListText;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] roles = this.getRoleNameList().stream().map(role -> "ROLE_" + role).toArray(String[]::new);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);
        return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public void eraseCredentials() {
        // password = null;
    }

    @JsonIgnore
    public ZoneId getTimezoneId() {
        return ZoneId.of(getTimezone());
    }


    private LocalDateTime lastPasswordChange;
}
