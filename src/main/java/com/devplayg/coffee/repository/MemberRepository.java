package com.devplayg.coffee.repository;

import com.devplayg.coffee.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
