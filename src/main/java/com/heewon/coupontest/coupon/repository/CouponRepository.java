package com.heewon.coupontest.coupon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.heewon.coupontest.coupon.domain.Coupon;

import jakarta.persistence.LockModeType;
import lombok.NonNull;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

	@NonNull
	Optional<Coupon> findById(@NonNull Long id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select c from Coupon c where c.id = :id")
	Coupon findForUpdate(@NonNull Long id);
}
