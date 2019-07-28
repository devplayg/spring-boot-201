package com.devplayg.coffee.service;

import com.devplayg.coffee.definition.AuditCategory;
import com.devplayg.coffee.definition.RoleType;
import com.devplayg.coffee.entity.Member;
import com.devplayg.coffee.entity.MemberNetwork;
import com.devplayg.coffee.exception.ResourceNotFoundException;
import com.devplayg.coffee.framework.InMemoryMemberManager;
import com.devplayg.coffee.repository.member.MemberRepository;
import com.devplayg.coffee.util.NetworkUtils;
import com.devplayg.coffee.vo.MemberPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Member service
 */
@Service
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuditService auditService;

    private final InMemoryMemberManager inMemoryMemberManager;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, AuditService auditService, InMemoryMemberManager inMemoryMemberManager) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
        this.inMemoryMemberManager = inMemoryMemberManager;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("member", "member.username", username));
    }

    public Member create(Member member) throws IllegalArgumentException {
        member.setRoles(member.getRoleList().stream().mapToInt(RoleType.Role::getValue).sum());
        member.setPassword(passwordEncoder.encode(member.getInputPassword()));
        member.setAccessibleIpList(this.getAccessibleIpList(member, member.getAccessibleIpListText()));
        log.debug("# post member converted: {}", member);

        Member newMember = memberRepository.save(member);
        inMemoryMemberManager.createUser(member);
        auditService.audit(AuditCategory.MEMBER_CREATE, newMember);
        return newMember;
    }

    private List<MemberNetwork> getAccessibleIpList(Member member, String text) {
        List<String> networks = NetworkUtils.splitNetworks(text, "\\s+|,\\s*|\\,\\s*");
        return networks.stream()
                .map(net -> {
                    MemberNetwork memberNetwork = new MemberNetwork(net);
                    memberNetwork.setMember(member);
                    return memberNetwork;
                })
                .collect(Collectors.toList());
    }

    public Member update(long id, Member input) throws IllegalArgumentException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("member", "id", id));
        member.setName(input.getName());
        member.setEmail(input.getEmail());
        member.setEnabled(input.isEnabled());
        member.setTimezone(input.getTimezone());
        member.setRoles(input.getRoleList().stream().mapToInt(i -> i.getValue()).sum());
        member.setAccessibleIpList(this.getAccessibleIpList(member, input.getAccessibleIpListText()));

        Member changed = memberRepository.save(member);
        log.debug("## member changed: {}", changed);
        log.debug("## member     now: {}", member);
        inMemoryMemberManager.updateUser(member);
        auditService.audit(AuditCategory.MEMBER_UPDATE, member);
        inMemoryMemberManager.informMemberHasChanged(member.getUsername());

        return changed;
    }

    public void updatePassword(long id, MemberPassword memberPassword) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("member", "id", id));

        member.setPassword(passwordEncoder.encode(memberPassword.getPassword()));
        member.setLastPasswordChange(LocalDateTime.now());
        memberRepository.save(member);

        // Update in-memory
        inMemoryMemberManager.updateUser(member);

        // Write audit log
		//
    }
}


