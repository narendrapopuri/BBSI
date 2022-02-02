package com.bbsi.platform.user.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.bbsi.platform.common.dto.UserPrincipal;

public class UserProfileCacheServiceImplTest {
	
	@InjectMocks
	private UserProfileCacheServiceImpl userProfileCacheServiceImpl;
	
	@Mock
	private HashOperations<String, String, UserPrincipal> hashOperations;
	
	private RedisTemplate<String, Object> redisTemplate;

	@Before
	public void setUp() throws Exception {
		userProfileCacheServiceImpl = Mockito.mock(UserProfileCacheServiceImpl.class);
		userProfileCacheServiceImpl = new UserProfileCacheServiceImpl(redisTemplate);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSave() {
		Mockito.doNothing().when(hashOperations).put(Mockito.anyString(), Mockito.anyString(), Mockito.any());
		userProfileCacheServiceImpl.save(populateUserPrincipal());
		Mockito.verify(hashOperations, Mockito.atLeast(1)).put(Mockito.anyString(), Mockito.anyString(), Mockito.any());
		assertTrue(true);
	}
	
	@Test
	public void testUpdate() {
		Mockito.doNothing().when(hashOperations).put(Mockito.anyString(), Mockito.anyString(), Mockito.any());
		userProfileCacheServiceImpl.update(populateUserPrincipal());
		Mockito.verify(hashOperations, Mockito.atLeast(1)).put(Mockito.anyString(), Mockito.anyString(), Mockito.any());
		assertTrue(true);
	}
	
	@Test
	public void testDelete() {
		userProfileCacheServiceImpl.delete("code");
		Mockito.verify(hashOperations, Mockito.atLeastOnce()).delete(Mockito.anyString(), Mockito.any());
		assertTrue(true);
	}
	
	@Test
	public void testFind() {
		Mockito.doReturn(populateUserPrincipal()).when(hashOperations).get(Mockito.anyString(), Mockito.any());
		userProfileCacheServiceImpl.find(populateUserPrincipal().getToken());
		Mockito.verify(hashOperations, Mockito.atLeastOnce()).get(Mockito.anyString(), Mockito.any());
		assertTrue(true);
	}
	
	@Test
	public void testclearAll() {
		Mockito.doNothing().when(hashOperations).putAll(Mockito.anyString(), Mockito.any());
		userProfileCacheServiceImpl.clearAll();
		Mockito.verify(hashOperations, Mockito.atLeastOnce()).putAll(Mockito.anyString(), Mockito.any());
		assertTrue(true);
	}
	
	@Test
	public void testfindByKeys() {
		List<UserPrincipal> expected = new ArrayList<>();
		expected.add(populateUserPrincipal());
		List<String> input = new ArrayList<>();
		input.add(populateUserPrincipal().getToken());
		Mockito.doReturn(expected).when(hashOperations).multiGet(Mockito.anyString(), Mockito.any());
		userProfileCacheServiceImpl.findByKeys(input);
		assertTrue(true);
	}
	
	@Test
	public void testremoveExpiredAccessToken() {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		userProfileCacheServiceImpl.removeExpiredAccessToken(scheduler, populateUserPrincipal().getToken());
		Mockito.verify(hashOperations, Mockito.atLeastOnce()).delete(Mockito.anyString(), Mockito.any());
		assertTrue(true);
	}
	
	@Test
	public void testSaveAll() {
		Map<String, UserPrincipal> userProfileMap = new HashMap<>();
		userProfileMap.put(populateUserPrincipal().getToken(), populateUserPrincipal());
		Mockito.doNothing().when(hashOperations).putAll(Mockito.anyString(), Mockito.any());
		userProfileCacheServiceImpl.saveAll(userProfileMap);
		Mockito.verify(hashOperations, Mockito.atLeastOnce()).putAll(Mockito.anyString(), Mockito.any());
		assertTrue(true);
	}
	
	@Test
	public void testfindAll() {
		Map<String, UserPrincipal> expected = new HashMap<>();
		expected.put(populateUserPrincipal().getToken(), populateUserPrincipal());
		Mockito.doReturn(expected).when(hashOperations).entries(Mockito.anyString());
		Map<String, UserPrincipal> result = userProfileCacheServiceImpl.findAll();
		assertEquals(result.get(populateUserPrincipal().getToken()), expected.get(populateUserPrincipal().getToken()));
		Mockito.verify(hashOperations, Mockito.atLeastOnce()).entries(Mockito.anyString());
	}
	
	private UserPrincipal populateUserPrincipal() {
		UserPrincipal data = new UserPrincipal("abc@abc.com", "password",new ArrayList<>());
		data.setClientCode("clientCode");
		data.setClientName("clientName");
		data.setToken("Bearer abcd-1223-hdfe-4234");
		return data;
	}

}
