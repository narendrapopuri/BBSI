package com.bbsi.platform.user.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.enums.Enums.UserEnum;
import com.bbsi.platform.common.user.dto.FeatureCodeDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.user.businessservice.intf.FeatureCodeBusinessService;
import com.bbsi.platform.user.model.Menu;
import com.google.common.collect.Maps;

public class FeatureCodeCacheServiceImplTest {
	
	@InjectMocks
	private FeatureCodeCacheServiceImpl serviceImpl;
	
	@Mock
	private HashOperations<String, String, CommonDTO> hashOperations;
	
	@Mock
	private static Map<Long, Menu> menuMap1 = Maps.newHashMap();
	
	@Mock
	private Map<String, CommonDTO> featureMap = Maps.newHashMap();
	
	
	@Mock
	private FeatureCodeBusinessService featureCodeBusinessService;
	
	private RedisTemplate<String, Object> redisTemplate;


	@Before
	public void setUp() throws Exception {
		serviceImpl = Mockito.mock(FeatureCodeCacheServiceImpl.class);
		serviceImpl = new FeatureCodeCacheServiceImpl(redisTemplate);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSave() {
		serviceImpl.save(populateCommonDTO());
		Mockito.verify(hashOperations, Mockito.atLeastOnce()).put(Mockito.any(), Mockito.any(), Mockito.any());
		assertTrue(true);
	}
	
	@Test
	public void testfindByKeys() {
		Set<String> keys = new HashSet<>();
		keys.add("mapping");
		List<CommonDTO> features = new ArrayList<>();
		features.add(populateCommonDTO());
		Mockito.doReturn(features).when(featureCodeBusinessService).loadAllFeatureCodes();
		List<CommonDTO> result = serviceImpl.findByKeys(keys);
		assertEquals(result.size(), features.size());
		assertEquals(result.get(0).getCode(), features.get(0).getCode());
		Mockito.verify(featureCodeBusinessService, Mockito.atLeastOnce()).loadAllFeatureCodes();
	}
	
	@Test
	public void testfindByKeys1() {
		Set<String> keys = new HashSet<>();
		keys.add("mapping");
		Mockito.doReturn(new ArrayList<>()).when(featureCodeBusinessService).loadAllFeatureCodes();
		List<CommonDTO> result = serviceImpl.findByKeys(keys);
		Mockito.verify(featureCodeBusinessService, Mockito.atLeastOnce()).loadAllFeatureCodes();
		assertNotNull(result);
	}
	
	@Test
	public void testfindByKeys2() {
		Set<String> keys = new HashSet<>();
		keys.add("mapping123");
		List<CommonDTO> features = new ArrayList<>();
		features.add(populateCommonDTO());
		Mockito.doReturn(features).when(featureCodeBusinessService).loadAllFeatureCodes();
		List<CommonDTO> result = serviceImpl.findByKeys(keys);
		assertNotNull(result);
	}
	
	@Test
	public void testgetFeatureCodes() {
		List<CommonDTO> result = serviceImpl.getFeatureCodes();
		assertEquals(null, result);
	}
	
	@Test
	public void testfindAll() {
		Map<String, CommonDTO> result = serviceImpl.findAll();
		assertEquals(0, result.size());
	}
	
	@Test
	public void testloadCache() {
		List<CommonDTO> input = new ArrayList<>();
		input.add(populateCommonDTO());
		Mockito.doReturn(input).when(featureCodeBusinessService).loadAllFeatureCodes();
		serviceImpl.loadCache();
		Mockito.verify(featureCodeBusinessService, Mockito.atLeastOnce()).loadAllFeatureCodes();
		assertTrue(true);
	}
	
	@Test
	public void testLoadCacheForOtherValues() {
		List<FeatureCodeDTO> input = new ArrayList<>();
		FeatureCodeDTO dto = new FeatureCodeDTO();
		dto.setCode("code1");
		dto.setDescription("description");
		dto.setId(1l);
		dto.setModule("module");
		dto.setName("namem");
		input.add(dto);
		Mockito.doReturn(null).when(featureCodeBusinessService).loadAllFeatureCodes();
		Mockito.doReturn(input).when(featureCodeBusinessService).getAllFeatureCodes();
		serviceImpl.loadCache();
		Mockito.verify(featureCodeBusinessService, Mockito.never()).getAllFeatureCodes();
		assertTrue(true);
	}
	
	@Test
	public void testSaveAll() {
		Map<String, CommonDTO> input = new HashMap<>();
		input.put("mapping", populateCommonDTO());
		serviceImpl.saveAll(input);
		Mockito.verify(hashOperations, Mockito.never()).put(Mockito.anyString(), Mockito.anyString(), Mockito.any());
		assertTrue(true);
		
	}
	
	@Test
	public void testupdate() {
		Mockito.doNothing().when(hashOperations).put(Mockito.anyString(), Mockito.anyString(), Mockito.any());
		serviceImpl.update(populateCommonDTO());
		Mockito.verify(hashOperations, Mockito.atLeastOnce()).put(Mockito.anyString(), Mockito.anyString(), Mockito.any());
		assertTrue(true);
	}
	
	@Test
	public void testdelete() {
		serviceImpl.delete("mapping");
		Mockito.verify(hashOperations, Mockito.atLeastOnce()).delete(Mockito.anyString(), Mockito.any());
		assertTrue(true);
	}
	
	@Test
	public void testFind() {
		Mockito.doReturn(populateCommonDTO()).when(featureCodeBusinessService).getFeature(Mockito.anyString());
		CommonDTO result = serviceImpl.find("mapping");
		assertEquals(result.getCode(), populateCommonDTO().getCode());
		Mockito.verify(featureCodeBusinessService, Mockito.atLeastOnce()).getFeature(Mockito.anyString());
	}
	
	@Test
	public void testFindForEmpty() {
		Mockito.doReturn(populateCommonDTO()).when(featureMap).get(Mockito.any());
		CommonDTO result = serviceImpl.find("mapping");
		assertNotNull(result);
	}
	
	@Test
	public void testFindForThrowsException() {
		Mockito.doThrow(BbsiException.class).when(featureMap).get(Mockito.any());
		CommonDTO result = serviceImpl.find("mapping");
		assertNull(result);
	}
	
	@Test
	public void testSetFeatureCodes() {
		List<CommonDTO> features = new ArrayList<>();
		features.add(populateCommonDTO());
		serviceImpl.setFeatureCodes(features);
		List<CommonDTO> result = serviceImpl.getFeatureCodes();
		assertNotNull(result);
	}
	
	private CommonDTO populateCommonDTO()
	{
	  CommonDTO mapping = new CommonDTO();
		mapping.setCode("mapping");
		mapping.setDescription("description");
		mapping.setId(1l);
		mapping.setName("name");
		mapping.setNewStatus(true);
		mapping.setParentId(0l);
		mapping.setStatus(true);
		mapping.setType(UserEnum.PRIVILEGE.toString());
		List<CommonDTO> privileges = new ArrayList<>();
		CommonDTO privilege = new CommonDTO();
		privilege.setCode("pcode");
		privileges.add(privilege);
		mapping.setPrivileges(privileges);
		List<CommonDTO> child = new ArrayList<>();
		CommonDTO child1 = new CommonDTO();
		child1.setCode("ccode");
		child.add(child1);
		mapping.setChild(child);
		return mapping;
	}

}
