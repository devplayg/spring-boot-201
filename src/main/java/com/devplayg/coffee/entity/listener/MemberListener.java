package com.devplayg.coffee.entity.listener;

import com.devplayg.coffee.entity.Member;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
public class MemberListener {
//    @PrePersist
//    void onPrePersist(Member member) {
//        log.info("MemberListener.onPrePersist(): {}", member.toString());
//    }

//    @PostPersist
//    void onPostPersist(Member member) {
//        log.info("MemberListener.onPostPersist(): {}", member.toString());
//    }

//    @PostLoad
//    void onPostLoad(Member member) {
//        log.info("MemberListener.onPostLoad(): {}", member.toString());
//    }

//    @PreUpdate
//    void onPreUpdate(Member member) {
//        log.info("MemberListener.onPreUpdate(): {}", member.toString());
//    }

    @PostUpdate
    void onPostUpdate(Member member) {
        log.info("MemberListener.onPostUpdate(): {}", member.toString());
    }

//    @PreRemove
//    void onPreRemove(Member member) {
//        log.info("MemberListener.onPreRemove(): {}", member.toString());
//    }

//    @PostRemove
//    void onPostRemove(Member member) {
//        log.info("MemberListener.onPostRemove(): {}", member.toString());
//    }
}
