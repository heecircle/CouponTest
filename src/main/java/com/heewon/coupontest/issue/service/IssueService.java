package com.heewon.coupontest.issue.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.web.server.NotAcceptableStatusException;
import org.webjars.NotFoundException;

import com.heewon.coupontest.coupon.domain.Coupon;
import com.heewon.coupontest.coupon.repository.CouponLogRepository;
import com.heewon.coupontest.coupon.repository.CouponRepository;
import com.heewon.coupontest.event.domain.Event;
import com.heewon.coupontest.event.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueService {
	private final EventRepository eventRepository;
	private final CouponRepository couponRepository;
	private final CouponLogRepository couponLogRepository;

	public void issueCouponDefault(LocalDateTime localDateTime, long eventId, long couponId, long memberId) throws
		Exception {

		// 시간 검증 로직
		checkEventPeriodAndTime(eventId, localDateTime);

		// 발급 쿠폰 수 증가
		issueCouponStatus(couponId);

		//이력 저장
		issueCouponLog(memberId, eventId);

	}

	public void issueCouponSynchronize(LocalDateTime localDateTime, long eventId, long couponId, long memberId) throws
		Exception {
		// 시간 검증 로직
		synchronized (this) {
			issueCouponDefault(localDateTime, eventId, couponId, memberId);
		}

	}

	/**
	 * 시간 검증 로직
	 * @param eventId
	 * @param currentDateTime
	 * @throws Exception
	 */
	void checkEventPeriodAndTime(Long eventId, LocalDateTime currentDateTime) throws Exception {
		Event event = eventRepository.findById(eventId).orElse(null);
		if (event.getStartTime().isAfter(currentDateTime) || event.getEndTime().isBefore(currentDateTime)) {
			throw new NotAcceptableStatusException("예외 시간");
		}
	}

	void issueCouponStatus(Long couponId) {
		Coupon coupon = couponRepository.findById(couponId).orElse(null);

		if (coupon.getCount() > 0) {
			coupon.setCount(coupon.getCount() - 1);
			System.out.println("coupon decrease");
		} else {
			throw new NotFoundException("coupon");
		}

		couponRepository.save(coupon);

	}

	void issueCouponLog(Long couponId, Long userId) {
		couponLogRepository.insertLog(couponId, userId);
	}

}
