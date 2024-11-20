package com.heewon.coupontest.coupon.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.heewon.coupontest.coupon.domain.CouponLog;

public interface CouponLogRepository extends JpaRepository<CouponLog, Long> {
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into coupon_log(`member_id`, `coupon_id`, `create_time`) values (:id1, :id2, now())")
	void insertLog(@Param("id1") Long id1, @Param("id2") Long id2);
}
