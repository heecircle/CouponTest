package com.heewon.coupontest.coupon.service;

import org.springframework.stereotype.Service;

import com.heewon.coupontest.coupon.domain.Coupon;
import com.heewon.coupontest.coupon.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {
	private final CouponRepository couponRepository;

	public Long createCoupon(Coupon coupon) {
		couponRepository.save(coupon);
		return coupon.getId();
	}

	public Coupon getCoupon(Long id) {
		return couponRepository.findById(id).orElse(null);
	}

	public void updateCoupon(Long id) {
		Coupon coupon = getCoupon(id);
		coupon.setCouponCountDecrease();
		couponRepository.save(coupon);
	}

}
