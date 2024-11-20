package com.heewon.coupontest.coupon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.heewon.coupontest.coupon.domain.Coupon;

import lombok.NonNull;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

	@NonNull Optional<Coupon> findById(@NonNull Long id);
}
