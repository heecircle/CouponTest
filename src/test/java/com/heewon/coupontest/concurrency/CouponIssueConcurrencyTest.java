package com.heewon.coupontest.concurrency;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.heewon.coupontest.coupon.domain.Coupon;
import com.heewon.coupontest.coupon.repository.CouponRepository;
import com.heewon.coupontest.issue.service.IssueService;

@SpringBootTest
public class CouponIssueConcurrencyTest {

	// 동시에 요청보내는 횟수 카운트
	final int threadCount = 10000;
	final String lineDescriptor = ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";

	@Autowired
	private IssueService issueService;
	@Autowired
	private CouponRepository couponRepository;

	@Test
	public void default100Test() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			final int currentMemberId = i + 1;
			executorService.submit(() -> {
				try {
					LocalDateTime currentTime = LocalDateTime.of(2024, 11, 20, 9, 0, 0);
					try {
						issueService.issueCouponDefault(currentTime, 1, 1, currentMemberId);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		Coupon coupon = couponRepository.findById(1L).orElse(null);
		if (coupon != null) {
			System.out.println(lineDescriptor);
			System.out.println("remain Coupon: " + coupon.getCount() + "  Total Coupon: " + coupon.getTotalCount());
			System.out.println(lineDescriptor);

		}
		assert Objects.requireNonNull(coupon).getCount() + 10000 == coupon.getTotalCount();
	}

	@Test
	public void synchronize100Test() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			final int currentMemberId = i + 1;
			executorService.submit(() -> {
				try {
					LocalDateTime currentTime = LocalDateTime.of(2024, 11, 20, 9, 0, 0);
					try {
						issueService.issueCouponSynchronize(currentTime, 1, 4, currentMemberId);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		Coupon coupon = couponRepository.findById(4L).orElse(null);
		if (coupon != null) {
			System.out.println(lineDescriptor);
			System.out.println("remain Coupon: " + coupon.getCount() + "  Total Coupon: " + coupon.getTotalCount());
			System.out.println(lineDescriptor);

		}
		assert Objects.requireNonNull(coupon).getCount() + 10000 == coupon.getTotalCount();
	}

	@Test
	public void PessimisticLockTest() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			final int currentMemberId = i + 1;
			executorService.submit(() -> {
				try {
					LocalDateTime currentTime = LocalDateTime.of(2024, 11, 20, 9, 0, 0);
					try {
						issueService.issueCouponPessimisticLock(currentTime, 1, 3, currentMemberId);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		Coupon coupon = couponRepository.findById(3L).orElse(null);
		if (coupon != null) {
			System.out.println(lineDescriptor);
			System.out.println("remain Coupon: " + coupon.getCount() + "  Total Coupon: " + coupon.getTotalCount());
			System.out.println(lineDescriptor);

		}
		assert Objects.requireNonNull(coupon).getCount() + 10000 == coupon.getTotalCount();
	}

	@Test
	public void ReddisonTest() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			final int currentMemberId = i + 1;
			executorService.submit(() -> {
				try {
					LocalDateTime currentTime = LocalDateTime.of(2024, 11, 20, 9, 0, 0);
					try {
						issueService.issueCouponRedisson(currentTime, 1, 6, currentMemberId);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		Coupon coupon = couponRepository.findById(6L).orElse(null);
		if (coupon != null) {
			System.out.println(lineDescriptor);
			System.out.println("remain Coupon: " + coupon.getCount() + "  Total Coupon: " + coupon.getTotalCount());
			System.out.println(lineDescriptor);

		}
		assert coupon.getCount() + 10000 == coupon.getTotalCount();
	}

}
