package com.heewon.coupontest.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heewon.coupontest.member.domain.Member;

import jakarta.persistence.Id;

@Repository
public interface MemberRepository extends JpaRepository<Member, Id> {
	Optional<Member> findMemberById(Long id);
}
