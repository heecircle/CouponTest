package com.heewon.coupontest.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

	@Value("${redis.host}")
	private String redisHost;

	@Value("${redis.port}")
	private int redisPort;

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();

		// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
		// System.out.println(redisHost + ":" + redisPort);
		// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
		config.useSingleServer()
			.setAddress(redisHost + ":" + redisPort);
		return Redisson.create(config);
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(redisHost, redisPort);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

		redisTemplate.setConnectionFactory(redisConnectionFactory());

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

		// Hash를 사용할 경우 Serializer
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new StringRedisSerializer());

		return redisTemplate;
	}

}
