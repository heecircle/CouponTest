package com.heewon.coupontest.coupon.domain;

import com.heewon.coupontest.event.domain.Event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Coupon {

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Setter
	@Getter
	@Column(nullable = false)
	private int count;

	@Getter
	@Column(updatable = false)
	private int totalCount;

	@JoinColumn
	@ManyToOne(fetch = FetchType.LAZY)
	private Event event;

	public void setCouponCountDecrease() {
		this.count--;
	}

}
