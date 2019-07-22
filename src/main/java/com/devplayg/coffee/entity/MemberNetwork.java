package com.devplayg.coffee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="mbr_allowed_ip")
@ToString(exclude = "member")
@NoArgsConstructor
public class MemberNetwork implements Serializable {

    private static final long serialVersionUID = 2L;

    public MemberNetwork(String ipCidr) {
        this.ipCidr = ipCidr;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "network_id")
    @JsonIgnore
    private long networkId;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    private String ipCidr;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime created;

    public void setMember(Member member) {
        this.member = member;
    }
}
