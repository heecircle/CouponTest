package com.heewon.coupontest.coupon.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.heewon.coupontest.member.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CouponLog {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	Member member;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	Coupon coupon;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@CreatedDate
	private LocalDateTime createTime;

}
