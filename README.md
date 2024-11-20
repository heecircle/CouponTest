## 쿠폰 동시성 처리

### 공통 로직

#### 500개의 쿠폰 수량에 대해 100개의 쿠폰을 동시에 발행하는 테스트 케이스

⚠️ 동시성 체크를 위한 함수 작성으로, 빠져있는 검증 로직이 있을 수 있음.

### checkEventPeriodAndTime

이벤트 시간 내에 요청된 건인지 판별

```Java
void checkEventPeriodAndTime(Long eventId, LocalDateTime currentDateTime) throws Exception {

	Event event = eventRepository.findById(eventId).orElse(null);

	if (event.getStartTime().isAfter(currentDateTime) || event.getEndTime().isBefore(currentDateTime)) {
		throw new NotAcceptableStatusException("예외 시간");
	}
}
```

### issueCouponStatus

쿠폰 개수 줄이는 메소드

```Java
// transactional 관련 내용 작성
@Transactional
void issueCouponStatus(Long couponId) {
	Coupon coupon = couponRepository.findById(couponId).orElse(null);

	if (coupon.getCount() > 0) {
		coupon.setCouponCountDecrease();
		System.out.println("coupon decrease");
	} else {
		throw new NotFoundException("coupon");
	}
}
```

### issueCouponLog

쿠폰 발급 로그 설정

```Java
void issueCouponLog(Long couponId, Long userId) {
	couponLogRepository.insertLog(couponId, userId);
}
```

### Case 1 : Transactional 여부에 따른 결과값 다른 경우

두가지 케이스로 issueCouponStatus 함수를 작성하였을 때, 결과값이 다르게 나온 것을 알 수 있었다.

```Java
// transactional 관련 내용 작성
@Transactional
void issueCouponStatus(Long couponId) {
	Coupon coupon = couponRepository.findById(couponId).orElse(null);

	if (coupon.getCount() > 0) {
		coupon.setCouponCountDecrease();
		System.out.println("coupon decrease");
	} else {
		throw new NotFoundException("coupon");
	}
}
```

```Java
// transactional 관련 내용 작성
void issueCouponStatus(Long couponId) {
	Coupon coupon = couponRepository.findById(couponId).orElse(null);

	if (coupon.getCount() > 0) {
		coupon.setCouponCountDecrease();
		System.out.println("coupon decrease");
	} else {
		throw new NotFoundException("coupon");
	}

	couponRepository.save(coupon);
}
```

[@Transactional 어노테이션에 대해서 좀 더 살펴보자.](#transactional)

- Dirty Checking 을 통해 coupon에 대한 값을 업데이트 하는 케이스 before 500, after 500 -> 642ms
    - 이 경우는, 트랜잭션 종료 시점에 반영돼서 여러 트랜잭션이 같은 엔티티를 동시에 수정하면, 나중에 커밋된 트랜잭션이 이전 트랜잭션의 결과를 덮어 쓸 수 있기 때문
      ![img_1.png](IMG/img_1.png)
- .save 사용 시 before 500, after 488 -> 605ms
    - .save는 호출 즉시 db에 값을 저장하기 떄문에, 트랜잭션 격리 수준을 통해 결과의 정합성 유지 필요
    - ![img.png](IMG/img.png)

### Transactional?