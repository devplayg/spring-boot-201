package com.devplayg.coffee.vo;

import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Hashtable;
import java.util.List;

@Slf4j
public class MembershipCenter {
    private static MembershipCenter instance = new MembershipCenter();
    private static final String systemUsername = "system";
    private Hashtable<String, UserDetails> membership;
    private Hashtable<String, Boolean> memberNews;

    // 생성자
    private MembershipCenter() {
        log.debug("### Initializing Membership Center");
        memberNews = new Hashtable<>();
        membership = new Hashtable<>();

        Member m = new Member();
        m.setId(1);
        m.setUsername(MembershipCenter.systemUsername);
        m.setRoles(1024);
        membership.put(MembershipCenter.systemUsername, m);



    }

    public static MembershipCenter getInstance() {
        return instance;
    }

    public void init(List<Member> list) {
//                for (Member m : list) {
//            membership.put(m.getUsername(), m);
//            memberNews.put(m.getUsername(), false);
//        }
    }

    public UserDetails get(String username) {
        return this.membership.get(username);
    }

    public void put(String username, UserDetails member) {
        this.membership.put(username, member);
    }
    public void update(String username, UserDetails member) {
        this.put(username, member);
        this.memberNews.put(username, true);
    }


    public UserDetails getSystemAccount() {
        return this.membership.get(systemUsername);
    }

    public Boolean anyNews(String username) {
        Boolean any = memberNews.get(username);
        return any == null ? false : any;
    }

    public Hashtable<String, UserDetails> getMembership() {
        return this.membership;
    }

    public Hashtable<String, Boolean> getMemberNews() {
        return this.memberNews;
    }
}
