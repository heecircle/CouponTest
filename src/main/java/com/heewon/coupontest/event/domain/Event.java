package com.heewon.coupontest.event.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@EnableJpaAuditing
@Entity
@Getter
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	private String description;

	@CreatedDate
	@Column
	private LocalDateTime createTime;

	@Column
	private LocalDateTime startTime;

	@Column
	private LocalDateTime endTime;
}
