package com.heewon.coupontest.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heewon.coupontest.event.domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
	Event findEventById(Long id);
}
