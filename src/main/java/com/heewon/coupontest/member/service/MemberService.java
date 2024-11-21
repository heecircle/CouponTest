package com.heewon.coupontest.member.service;

import org.springframework.stereotype.Service;

import com.heewon.coupontest.member.domain.Member;
import com.heewon.coupontest.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	public Member findMemberById(Long id) {
		return memberRepository.findMemberById(id).orElse(new Member());
	}

}
