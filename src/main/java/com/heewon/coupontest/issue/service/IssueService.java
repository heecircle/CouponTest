package com.heewon.coupontest.issue.service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
	private final RedissonClient redissonClient;

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

	@Transactional
	public void issueCouponPessimisticLock(LocalDateTime localDateTime, long eventId, long couponId,
		long memberId) throws Exception {

		checkEventPeriodAndTime(eventId, localDateTime);

		issueCouponPessimisticLock(couponId);

		issueCouponLog(memberId, eventId);

	}

	/**
	 * 시간 검증 로직
	 * @param eventId
	 * @param currentDateTime
	 * @throws Exception
	 */
	public void checkEventPeriodAndTime(Long eventId, LocalDateTime currentDateTime) throws Exception {
		Event event = eventRepository.findById(eventId).orElse(null);
		if (event.getStartTime().isAfter(currentDateTime) || event.getEndTime().isBefore(currentDateTime)) {
			throw new NotAcceptableStatusException("예외 시간");
		}
	}

	public void issueCouponStatus(Long couponId) {
		Coupon coupon = couponRepository.findById(couponId).orElse(null);

		if (coupon.getCount() > 0) {
			coupon.setCount(coupon.getCount() - 1);
			// System.out.println("coupon decrease");
		} else {
			throw new NotFoundException("coupon");
		}

		couponRepository.save(coupon);

	}

	@Transactional
	public void issueCouponPessimisticLock(Long couponId) {
		Coupon coupon = couponRepository.findForUpdate(couponId);
		if (coupon.getCount() > 0) {
			coupon.setCount(coupon.getCount() - 1);
		}

		// couponRepository.save(coupon);
	}

	void issueCouponLog(Long couponId, Long userId) {
		couponLogRepository.insertLog(couponId, userId);
	}

	public void issueCouponRedisson(LocalDateTime localDateTime, long eventId, long couponId, long memberId) {
		RLock rLock = redissonClient.getLock(String.valueOf(couponId));
		try {
			if (!rLock.tryLock(15, 1, TimeUnit.SECONDS)) {
				throw new InterruptedException();
			}
			issueCouponDefault(localDateTime, eventId, couponId, memberId);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			rLock.unlock();
		}
	}

}
