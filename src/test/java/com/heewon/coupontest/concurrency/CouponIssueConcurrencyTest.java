package com.heewon.coupontest.concurrency;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.heewon.coupontest.coupon.domain.Coupon;
import com.heewon.coupontest.coupon.repository.CouponRepository;
import com.heewon.coupontest.coupon.service.CouponService;
import com.heewon.coupontest.issue.service.IssueService;

@SpringBootTest
public class CouponIssueConcurrencyTest {

	@Autowired
	private IssueService issueService;
	@Autowired
	private CouponRepository couponRepository;
	// 동시에 요청보내는 횟수 카운트
	final int threadCount = 100;
	final String lineDescriptor = ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
	@Test
	public void default100Test() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for(int i = 0; i < threadCount; i++){
			final int currentMemberId = i + 1;
			executorService.submit(() -> {
				try{
					LocalDateTime currentTime = LocalDateTime.of(2024,11,20,9,0,0);
					try {
						issueService.issueCoupon(currentTime, 1, 1, currentMemberId);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}finally {
					latch.countDown();
				}
			});
		}

		latch.await();





		Coupon coupon = couponRepository.findById(1L).orElse(null);
		if(coupon != null){
			System.out.println(lineDescriptor);
			System.out.println("remain Coupon: " +coupon.getCount() + "  Total Coupon: " + coupon.getTotalCount());
			System.out.println(lineDescriptor);

		}

	}

}
