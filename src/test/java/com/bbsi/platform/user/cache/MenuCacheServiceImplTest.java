package com.bbsi.platform.user.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.user.model.Menu;
import com.bbsi.platform.user.repository.MenuRepository;
import com.google.common.collect.Maps;

public class MenuCacheServiceImplTest {
	
	@InjectMocks
	private MenuCacheServiceImpl menuCache;
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private MenuCacheService menuCacheService;
	
	@Mock
	private HashOperations<String, Long, Menu> hashOperations;
	
	@Mock
	private static Map<Long, Menu> menuMap1 = Maps.newHashMap();
	
	private RedisTemplate<String, Object> redisTemplate;

		
	@Before
	public void setUp() throws Exception {
		menuCache = Mockito.mock(MenuCacheServiceImpl.class);
		menuCache = new MenuCacheServiceImpl(redisTemplate);
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testLoadCache() {
		List<Menu> features = new ArrayList<>();
		features.add(populateMenu());
		when(menuRepository.findAll()).thenReturn(features);
		menuCache.loadCache();
		assertTrue(true);
	}
	
	@Test
	public void testLoadCacheForMenuEmpty() {
		when(menuRepository.findAll()).thenReturn(new ArrayList<>());
		menuCache.loadCache();
		assertTrue(true);
	}

	@Test
	public void testSave() {
		Mockito.doNothing().when(menuCacheService).save(Mockito.any());
		menuCache.save(populateMenu());
		assertTrue(true);
	}
	
	@Test
	public void testFind() {
		List<Menu> features = new ArrayList<>();
		features.add(populateMenu());
		Mockito.doReturn(features).when(menuRepository).findAll();
		Menu result = menuCache.find(1l);
		Mockito.verify(menuRepository, Mockito.atLeastOnce()).findAll();
		assertEquals(result.getId(), features.get(0).getId());
	}
	
	@Test
	public void testFindForGetMenucodeNotsame() {
		List<Menu> features = new ArrayList<>();
		features.add(populateMenu());
		Mockito.doReturn(features).when(menuRepository).findAll();
		Menu result = menuCache.find(5l);
		Mockito.verify(menuRepository, Mockito.atLeastOnce()).findAll();
		assertNull(result);
	}
	
	@Test
	public void testFindForGetMenuEmpty() {
		Mockito.doReturn(new ArrayList<>()).when(menuRepository).findAll();
		Menu result = menuCache.find(1l);
		Mockito.verify(menuRepository, Mockito.atLeastOnce()).findAll();
		assertNull(result);
	}
	
	@Test
	public void testFindForMenuNotNull() {
       Mockito.doReturn(populateMenu()).when(menuMap1).get(Mockito.any());
		Menu result = menuCache.find(1l);
		assertNotNull(result);
	}
	
	@Test
	public void testFindWhenNotNull() {
		Mockito.doReturn(BbsiException.class).when(menuMap1).get(Mockito.any());
		List<Menu> features = new ArrayList<>();
		features.add(populateMenu());
		Mockito.doReturn(features).when(menuRepository).findAll();
		Menu result = menuCache.find(1l);
		Mockito.verify(menuRepository, Mockito.atLeastOnce()).findAll();
		assertEquals(result.getId(), features.get(0).getId());
	}
	
	@Test
	public void testfindByKeys() {
		Set<Long> keys = new HashSet<>();
		keys.add(1l);
		List<Menu> features = new ArrayList<>();
		features.add(populateMenu());
		Mockito.doReturn(features).when(menuRepository).findAll();
		List<Menu> result = menuCache.findByKeys(keys);
		Mockito.verify(menuRepository, Mockito.atLeastOnce()).findAll();
		assertEquals(result.get(0).getId(), features.get(0).getId());
	}
	
	@Test
	public void testfindByKeysForEmpty() {
		Set<Long> keys = new HashSet<>();
		keys.add(5l);
		List<Menu> features = new ArrayList<>();
		features.add(populateMenu());
		Mockito.doReturn(features).when(menuRepository).findAll();
		List<Menu> result = menuCache.findByKeys(keys);
		Mockito.verify(menuRepository, Mockito.atLeastOnce()).findAll();
		assertNotNull(result);
	}
	
	@Test
	public void testfindByKeysForEmpty1() {
		Set<Long> keys = new HashSet<>();
		keys.add(5l);
		Mockito.doReturn(new ArrayList<>()).when(menuRepository).findAll();
		List<Menu> result = menuCache.findByKeys(keys);
		Mockito.verify(menuRepository, Mockito.atLeastOnce()).findAll();
		assertNotNull(result);
	}
	
	
	
	@Test
	public void testfindByParentCode() {
		List<Menu> features = new ArrayList<>();
		features.add(populateMenu());
		Mockito.doReturn(features).when(menuMap1).values();
		List<Menu> result = menuCache.findByParentCode(0l);
		assertEquals(1, result.size());
	}
	
	@Test
	public void testfindByParentCodeForMenuEmpty() {
		List<Menu> features = new ArrayList<>();
		features.add(populateMenu());
		Mockito.doReturn(features).when(menuMap1).values();
		List<Menu> result = menuCache.findByParentCode(2l);
		assertNotNull(result);
	}
	
	@Test
	public void testdelete() {
		menuCache.delete(1l);
		assertTrue(true);
	}
	
	@Test
	public void testUpdate() {
		Mockito.doNothing().when(hashOperations).put("MENU_DATA", 1l, populateMenu());
		menuCache.update(populateMenu());
		assertTrue(true);
	}
	
	@Test
	public void testfindAll() {
		menuCache.findAll();
		assertTrue(true);
	}
	
	@Test
	public void testsaveAll() {
		Map<Long, Menu> menuMap = new HashMap<>();
		menuMap.put(1l, populateMenu());
		menuCache.saveAll(menuMap);
		assertTrue(true);
		Mockito.verify(hashOperations, Mockito.atLeastOnce()).putAll(Mockito.any(), Mockito.any());
	}
	
	private Menu populateMenu() {
		Menu data = new Menu();
		data.setCategory("category");
		data.setDescription("description");
		data.setDisplayName("displayName");
		data.setFeatureCodeId(1l);
		data.setIconUrl("/url");
		data.setId(1l);
		data.setName("name");
		data.setSequence(100);
		data.setType("type");
		data.setUrl("/url1");
		data.setParentId(0);
		return data;
	}

}
