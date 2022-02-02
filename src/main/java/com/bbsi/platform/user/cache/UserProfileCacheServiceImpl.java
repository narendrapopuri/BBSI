package com.bbsi.platform.user.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bbsi.platform.common.dto.UserPrincipal;
import com.google.common.collect.Maps;

/**
 * @author veethakota
 *
 */
//@Logging
@Service
public class UserProfileCacheServiceImpl implements UserProfileCacheService {

	private static final String KEY = "UserProfile";

	@Autowired

	private RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, String, UserPrincipal> hashOperations;

	@Autowired
	public UserProfileCacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public UserPrincipal find(String code) {
		return hashOperations.get(KEY, code);
	}

	@Override
	public List<UserPrincipal> findByKeys(List<String> keys) {
		return hashOperations.multiGet(KEY, keys);
	}

	@Override
	public Map<String, UserPrincipal> findAll() {
		return hashOperations.entries(KEY);
	}

	@Override
	public void save(UserPrincipal userProfile) {
		hashOperations.put(KEY, userProfile.getToken(), userProfile);
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.schedule(() -> removeExpiredAccessToken(scheduler, userProfile.getToken()), 30l, TimeUnit.MINUTES);
	}

	@Override
	public void update(UserPrincipal userProfile) {
		// Calling save method as sonar reported identical method for update and save()
		save(userProfile);
	}

	@Override
	public void delete(String code) {
		hashOperations.delete(KEY, code);
	}
	
	@Override
	public void clearAll() {
		hashOperations.putAll(KEY, Maps.newHashMap());
	}

	@Override
	public void saveAll(Map<String, UserPrincipal> userProfileMap) {
		hashOperations.putAll(KEY, userProfileMap);
	}
	
	public void removeExpiredAccessToken(ScheduledExecutorService scheduler, String tokenValue) {
		delete(tokenValue);
		scheduler.shutdown();
	}
}
