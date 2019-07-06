package com.devplayg.coffee.framework;

import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Hashtable;

@Slf4j
// 실시간 사용자 권한관리 Singleton 패턴 클래스
public class MembershipCenter {
    private static MembershipCenter mc = new MembershipCenter();
    private static final String systemUsername = "system";
    private static final long systemUserId = 1;

    // 사용자 정보변경 유무 DB
    private Hashtable<String, Boolean> memberNews;

    // 변경된 사용자 정보 DB
    private Hashtable<String, UserDetails> membership;

    private MembershipCenter() {
        log.info("Initializing membership Center");
        memberNews = new Hashtable<>();
        membership = new Hashtable<>();
        this.createSystemUser();
    }

    public static MembershipCenter getInstance() {
        return mc;
    }

    // 관리자 계정 생성
    private void createSystemUser() {
        Member m = new Member();
        m.setId(systemUserId);
        m.setUsername(systemUsername);
        m.setRoles(RoleType.Role.ADMIN.getValue());
        this.put(MembershipCenter.systemUsername, m);
    }

    // 정보 조회
    private UserDetails get(String username) {
        return this.membership.get(username);
    }

    // 정보 입력
    private void put(String username, UserDetails member) {
        this.membership.put(username, member);
    }

    // 변경 알림
    public static void notifyChanges(UserDetails member) {
        mc.put(member.getUsername(), member);
        mc.memberNews.put(member.getUsername(), true);
    }

    // 변경사항 조회
    public static UserDetails readNews(String username) {
        mc.memberNews.remove(username);
        UserDetails member = mc.get(username);
        return member;
    }

    // 변경사항 삭제
    public static void clearNews(String username) {
        mc.membership.remove(username);
    }

    // 시스템 계정 조히
    public static UserDetails getSystemAccount() {
        return mc.get(systemUsername);
    }

    // 뉴스 확인
    public static Boolean anyNews(String username) {
        Boolean any = mc.memberNews.get(username);
        return any == null ? false : any;
    }

    public static Hashtable<String, UserDetails> getMembership() {
        return mc.membership;
    }

    public static Hashtable<String, Boolean> getMemberNews() {
        return mc.memberNews;
    }
}
