package com.heewon.coupontest.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@AllArgsConstructor
public class CouponDecreaseRequestDto {
	Long userId;
	Long couponId;
}
