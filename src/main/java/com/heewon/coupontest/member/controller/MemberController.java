package com.heewon.coupontest.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.heewon.coupontest.member.domain.Member;
import com.heewon.coupontest.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	@GetMapping("/get")
	public Member getMember(@RequestParam Long id) {
		return memberService.findMemberById(id);
	}

}
