package com.heewon.coupontest.coupon.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heewon.coupontest.coupon.dto.CouponDecreaseRequestDto;
import com.heewon.coupontest.coupon.service.CouponService;
import com.heewon.coupontest.issue.service.IssueService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {
	private final CouponService couponService;
	private final IssueService issueService;

	@GetMapping("/decreaseCoupon")
	public String decreaseCoupon(@RequestBody CouponDecreaseRequestDto couponDecreaseRequestDto) throws Exception {
		issueService.issueCouponRedisson(LocalDateTime.now(), 1L, 1L, couponDecreaseRequestDto.getUserId());
		return "success";
	}

}
