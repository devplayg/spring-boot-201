package com.devplayg.coffee.framework;

import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.util.SubnetUtils;
import com.mysema.commons.lang.Assert;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


/**
 * In-memory member manager
 */
@NoArgsConstructor
@Slf4j
public class InMemoryMemberManager implements UserDetailsManager, UserDetailsPasswordService, Serializable {
    private static final long serialVersionUID = 2L;

    private static InMemoryMemberManager imm;
    private final Map<String, Member> users = new HashMap<>();
    public static final String adminUsername = "admin";
    public static final long adminId = 1;

    public InMemoryMemberManager(List<Member> members) {
        for (Member member : members) {
            createUser(member);
        }
        imm = this;
        log.info("Loaded {} users and stored in memory", members.size());
    }

    public static InMemoryMemberManager getInstance() {
        return imm;
    }

    @Override
    public void createUser(UserDetails user) {
        Assert.isTrue(!userExists(user.getUsername()), "user should not exist");

        Member member = (Member) user;

        // Set Member accessible networks
        member.setSubnetUtils(
                member.getAccessibleIpList().stream()
                        .map(ip -> new SubnetUtils(ip.getIpCidr()))
                        .collect(Collectors.toList())
        );
        users.put(user.getUsername().toLowerCase(), member);
    }

    @Override
    public void updateUser(UserDetails user) {
        Assert.isTrue(userExists(user.getUsername()), "user should exist");

        Member member = (Member) user;

        // Set Member accessible networks
        member.setSubnetUtils(
                member.getAccessibleIpList().stream()
                        .map(ip -> new SubnetUtils(ip.getIpCidr()))
                        .collect(Collectors.toList())
        );
        users.put(user.getUsername().toLowerCase(), member);
    }

    @Override
    public void deleteUser(String username) {
        users.remove(username.toLowerCase());
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            // This would indicate bad coding somewhere
//            throw new AccessDeniedException(
//                    "Can't change password as no Authentication object found in context "
//                            + "for current user.");
        }

        String username = currentUser.getName();

        log.debug("Changing password for user '" + username + "'");

        // If an authentication manager has been set, re-authenticate the user with the
        // supplied password.
//        if (authenticationManager != null) {
//            logger.debug("Reauthenticating user '" + username
//                    + "' for password change request.");
//
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    username, oldPassword));
//        }
//        else {
//            logger.debug("No authentication manager set. Password won't be re-checked.");
//        }

        Member member = users.get(username);
        if (member == null) {
            throw new IllegalStateException("Current user doesn't exist in database.");
        }
        member.setPassword(newPassword);
    }

    @Override
    public boolean userExists(String username) {
        return users.containsKey(username.toLowerCase());
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserDetails user = users.get(username.toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        String username = user.getUsername();
//        UserDetails userDetails = users.get(username.toLowerCase());
//        Member member = (Member) userDetails;
//        member.setPassword(newPassword);
        Member member = users.get(username.toLowerCase());
        member.setPassword(newPassword);
        return member;
    }

    public static ZoneId getCurrentMemberTimezone() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return TimeZone.getDefault().toZoneId();
        }
        Member member = (Member) auth.getPrincipal();
        return member.getTimezoneId();
    }

    public static long getCurrentMemberId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return 0;
        }
        Member member = (Member) auth.getPrincipal();
        return member.getId();
    }

    public Collection<Member> getAllMembers() {
        return users.values();
    }

    public void informMemberHasChanged(String username) {
        Member member = users.get(username.toLowerCase());
        member.setChanged(true);
    }

    public Boolean isChanged(String username) {
        Member member = users.get(username.toLowerCase());
        if (member.getChanged() == null) {
            return false;
        }
        return member.getChanged();
    }

    public void gotNews(String username) {
        Member member = users.get(username.toLowerCase());
        member.setChanged(false);
    }
}
