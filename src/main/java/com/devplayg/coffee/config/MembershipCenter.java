package com.devplayg.coffee.config;

import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Hashtable;

public class MembershipCenter {
    private static final long systemId = 1;

    @Autowired
    private MemberRepository memberRepository;

    private Hashtable<Long, Member> membership;

    public MembershipCenter(Hashtable<Long, Member> membership) {
        this.membership = membership;
    }
    public Member get(Long memberId) {
        return this.membership.get(memberId);
    }
    public void put(Long memberId, Member member) {
        this.membership.put(memberId, member);
    }
    public Member getSystemAccount() {
        return this.membership.get(systemId);
    }
}
