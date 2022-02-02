package com.bbsi.platform.user.businessservice.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.exception.ExceptionUtils;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableTriConsumer;
import com.bbsi.platform.common.user.dto.MenuMappingDTO;
import com.bbsi.platform.user.businessservice.intf.MenuBusinessService;
import com.bbsi.platform.user.cache.FeatureCodeCacheService;
import com.bbsi.platform.user.cache.MenuCacheService;
import com.bbsi.platform.user.model.Menu;
import com.bbsi.platform.user.repository.MenuRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@RunWith(MockitoJUnitRunner.class)
//@RunWith(MockitoJUnitRunner.Silent.class)

//  This class contains fetch menu Test Cases related to Menu.

// @author anandaluru

public class MenuBusinessServiceImplTest {

	@InjectMocks
	private MenuBusinessServiceImpl menuBusinessServiceImpl;

	@Mock
	private MenuMappingDTO menuMappingDTO;

	@Mock
	private MenuBusinessService menuBusinessService;

	@Mock
	private CommonDTO commonDTO;

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private MenuCacheService menuCacheService;

	@Mock
	private FeatureCodeCacheService featureCacheService;

	@Mock
	private ObjectMapper mapper;

	private List<Menu> menu = new ArrayList<Menu>();
	private Map<Long, Menu> menuCache = new HashMap<Long, Menu>();
	private List<CommonDTO> userFeatures = new ArrayList<>();

	@SuppressWarnings("unchecked")

	@Before
	public void setUp() throws Exception {
		ExceptionUtils.handleException = Mockito.mock(ThrowableTriConsumer.class);
		menu = populateMenu();
		menuCache = populateMenuCache();
		userFeatures = populateUserFeatures();
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() {
		menu = null;
		menuCache = null;
		userFeatures = null;
	}

	// This Test case is used to test get the All Menus

	@Test
	public void testFetchUserMenu() throws JsonProcessingException {
		byte[] byteArray = new byte[] { 1, 3, 5 };
		Mockito.doReturn(menu).when(menuRepository).findAll();
		Mockito.doReturn(menuCache).when(menuCacheService).findAll();
		Mockito.doReturn(userFeatures).when(featureCacheService).findByKeys(Mockito.any());
		Mockito.doReturn(menu).when(menuCacheService).findByParentCode(Mockito.anyLong());
		Mockito.doReturn(byteArray).when(mapper).writeValueAsBytes(Mockito.any());
		List<MenuMappingDTO> actualResponse = menuBusinessServiceImpl.fetchUserMenu(populateUserFeatures(),
				populaClientMappingsWhenPrivilegeIsEmpty());
		assertEquals(1, actualResponse.size());
	}

	@Test
	public void testFetchUserMenuForPrivillagesIsEmpty() throws JsonProcessingException {
		byte[] byteArray = new byte[] { 1, 3, 5 };
		Mockito.doReturn(menu).when(menuRepository).findAll();
		Mockito.doReturn(menuCache).when(menuCacheService).findAll();
		Mockito.doReturn(populateUserFeaturesFormenuMapping()).when(featureCacheService).findByKeys(Mockito.any());
		Mockito.doReturn(menu).when(menuCacheService).findByParentCode(Mockito.anyLong());
		Mockito.doReturn(byteArray).when(mapper).writeValueAsBytes(Mockito.any());
		List<MenuMappingDTO> actualResponse = menuBusinessServiceImpl
				.fetchUserMenu(populateUserFeaturesFormenuMapping(), populaClientMappingsWhenPrivilegeIsEmpty());
		assertNotNull(actualResponse);
	}

	@Test
	public void testFetchUserMenuFormenuMappingPrivilegesIsEmpty() throws JsonProcessingException {
		byte[] byteArray = new byte[] { 1, 3, 5 };
		Mockito.doReturn(menu).when(menuRepository).findAll();
		Mockito.doReturn(menuCache).when(menuCacheService).findAll();
		Mockito.doReturn(populateUserFeaturesWhenPrivilegesIsEmpty()).when(featureCacheService)
				.findByKeys(Mockito.any());
		Mockito.doReturn(menu).when(menuCacheService).findByParentCode(Mockito.anyLong());
		Mockito.doReturn(byteArray).when(mapper).writeValueAsBytes(Mockito.any());
		List<MenuMappingDTO> actualResponse = menuBusinessServiceImpl
				.fetchUserMenu(populateUserFeaturesWhenPrivilegesIsEmpty(), populaClientMappingsWhenPrivilegeIsEmpty());
		assertNotNull(actualResponse);
	}

	@Test
	public void testFetchUserMenuForIsSelectedFalse() throws JsonProcessingException {
		byte[] byteArray = new byte[] { 1, 3, 5 };
		Mockito.doReturn(menu).when(menuRepository).findAll();
		Mockito.doReturn(menuCache).when(menuCacheService).findAll();
		Mockito.doReturn(populatePrivilegeForCodeEmpty()).when(featureCacheService).findByKeys(Mockito.any());
		Mockito.doReturn(menu).when(menuCacheService).findByParentCode(Mockito.anyLong());
		Mockito.doReturn(byteArray).when(mapper).writeValueAsBytes(Mockito.any());
		List<MenuMappingDTO> actualResponse = menuBusinessServiceImpl.fetchUserMenu(populatePrivilegeForCodeEmpty(),
				populaClientMappingsWhenPrivilegeIsEmpty());
		assertNotNull(actualResponse);
	}
	

	@Test
	public void testFetchUserMenuWhenUserFeatureIsEmpty() throws JsonProcessingException {
		byte[] byteArray = new byte[] { 1, 3, 5 };
		Mockito.doReturn(new ArrayList<>()).when(menuRepository).findAll();
		Mockito.doReturn(new HashMap<>()).when(menuCacheService).findAll();
		Mockito.doReturn(new ArrayList<>()).when(featureCacheService).findByKeys(Mockito.any());
		Mockito.doReturn(new ArrayList<>()).when(menuCacheService).findByParentCode(Mockito.anyLong());
		Mockito.doReturn(byteArray).when(mapper).writeValueAsBytes(Mockito.any());
		List<MenuMappingDTO> actualResponse = menuBusinessServiceImpl.fetchUserMenu(new ArrayList<CommonDTO>(),
				populaClientMappingsWhenPrivilegeIsEmpty());
		assertNotNull(actualResponse);
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testFetchUserMenuWhenMenuCacheIsEmpty() throws JsonProcessingException {
		byte[] byteArray = new byte[] { 1, 3, 5 };
		Mockito.doReturn(menu).when(menuRepository).findAll();
		Mockito.doReturn(new HashMap<Long, Menu>()).when(menuCacheService).findAll();
		Mockito.doReturn(new ArrayList<CommonDTO>()).when(featureCacheService).findByKeys(Mockito.any());
		Mockito.doReturn(menu).when(menuCacheService).findByParentCode(Mockito.anyLong());
		Mockito.doReturn(byteArray).when(mapper).writeValueAsBytes(Mockito.any());
		List<MenuMappingDTO> actualResponse = menuBusinessServiceImpl.fetchUserMenu(populateUserFeatures(),
				new ArrayList<CommonDTO>());
		assertNotNull(actualResponse);
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testFetchUserMenuWhenFeatureIsEmpty() throws JsonProcessingException {
		byte[] byteArray = new byte[] { 1, 3, 5 };
		Mockito.doReturn(menu).when(menuRepository).findAll();
		Mockito.doReturn(menuCache).when(menuCacheService).findAll();
		Mockito.doReturn(new ArrayList<CommonDTO>()).when(featureCacheService).findByKeys(Mockito.any());
		Mockito.doReturn(menu).when(menuCacheService).findByParentCode(Mockito.anyLong());
		Mockito.doReturn(byteArray).when(mapper).writeValueAsBytes(Mockito.any());
		List<MenuMappingDTO> actualResponse = menuBusinessServiceImpl.fetchUserMenu(populateUserFeatures(),
				new ArrayList<CommonDTO>());
		assertNotNull(actualResponse);
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testFetchUserMenuWithPrivilege() throws JsonProcessingException {

		byte[] byteArray = new byte[] { 1, 3, 5 };
		Mockito.doReturn(menu).when(menuRepository).findAll();
		Mockito.doReturn(menuCache).when(menuCacheService).findAll();
		Mockito.doReturn(userFeatures).when(featureCacheService).findByKeys(Mockito.any());
		Mockito.doReturn(menu).when(menuCacheService).findByParentCode(Mockito.anyLong());
		Mockito.doReturn(byteArray).when(mapper).writeValueAsBytes(Mockito.any());
		List<MenuMappingDTO> actualResponse = menuBusinessServiceImpl.fetchUserMenu(populateUserFeatures(),
				populaClientMappings());
		assertNotNull(actualResponse);
		assertEquals(1, actualResponse.size());
	}

	@Test
	public void testFetchUserMenuWhenListIsEmpty() throws JsonProcessingException {

		byte[] byteArray = new byte[] { 1, 3, 5 };
		Mockito.doReturn(menu).when(menuRepository).findAll();
		Mockito.doReturn(menuCache).when(menuCacheService).findAll();
		Mockito.doReturn(userFeatures).when(featureCacheService).findByKeys(Mockito.any());
		Mockito.doReturn(new ArrayList<>()).when(menuCacheService).findByParentCode(Mockito.anyLong());
		Mockito.doReturn(byteArray).when(mapper).writeValueAsBytes(Mockito.any());
		Mockito.doReturn(populateMenuRecord()).when(menuCacheService).find(Mockito.anyLong());
		List<MenuMappingDTO> actualResponse = menuBusinessServiceImpl.fetchUserMenu(populateUserFeatures(),
				populaClientMappings());
		assertNotNull(actualResponse);
		assertEquals(1, actualResponse.size());
	}

	@Test
	public void testFetchUserMenuWhenParentMenuIsNull() throws JsonProcessingException {

		byte[] byteArray = new byte[] { 1, 3, 5 };
		Mockito.doReturn(menu).when(menuRepository).findAll();
		Mockito.doReturn(menuCache).when(menuCacheService).findAll();
		Mockito.doReturn(userFeatures).when(featureCacheService).findByKeys(Mockito.any());
		Mockito.doReturn(new ArrayList<>()).when(menuCacheService).findByParentCode(Mockito.anyLong());
		Mockito.doReturn(byteArray).when(mapper).writeValueAsBytes(Mockito.any());
		Mockito.doReturn(null).when(menuCacheService).find(Mockito.anyLong());
		List<MenuMappingDTO> actualResponse = menuBusinessServiceImpl.fetchUserMenu(populateUserFeatures(),
				populaClientMappings());
		assertNotNull(actualResponse);
	}

	@Test
	public void testFetchUserMenuWhenParentIdIsNotPresent() throws JsonProcessingException {

		byte[] byteArray = new byte[] { 1, 3, 5 };
		Mockito.doReturn(menu).when(menuRepository).findAll();
		Mockito.doReturn(populateMenuCacheWithoutParentId()).when(menuCacheService).findAll();
		Mockito.doReturn(userFeatures).when(featureCacheService).findByKeys(Mockito.any());
		Mockito.doReturn(new ArrayList<>()).when(menuCacheService).findByParentCode(Mockito.anyLong());
		Mockito.doReturn(byteArray).when(mapper).writeValueAsBytes(Mockito.any());
		Mockito.doReturn(populateMenuRecord()).when(menuCacheService).find(Mockito.anyLong());
		List<MenuMappingDTO> actualResponse = menuBusinessServiceImpl.fetchUserMenu(populateUserFeatures(),
				populaClientMappings());
		assertNotNull(actualResponse);
		assertEquals(1, actualResponse.size());
	}

	@Test
	public void testFetchUserMenuWhenNull() {
		Mockito.doReturn(null).when(menuRepository).findAll();
		Mockito.doReturn(menuCache).when(menuCacheService).findAll();
		Mockito.doReturn(null).when(featureCacheService).findByKeys(Mockito.any());
		Mockito.doReturn(null).when(menuCacheService).findByParentCode(Mockito.anyLong());
		Mockito.doReturn(null).when(menuCacheService).find(Mockito.anyLong());
		List<MenuMappingDTO> actualResponse = menuBusinessServiceImpl.fetchUserMenu(populateUserFeatures(),
				populaClientMappings());
		assertEquals(0, actualResponse.size());
	}

	private Map<Long, Menu> populateMenuCache() {
		Map<Long, Menu> menuCache = new HashMap<Long, Menu>();
		Menu menu = new Menu();
		menu.setId(123l);
		menu.setIsActive(true);
		menu.setFeatureCodeId(123l);
		menu.setParentId(123);
		menuCache.put(1l, menu);
		return menuCache;
	}

	private Map<Long, Menu> populateMenuCacheWithoutParentId() {
		Map<Long, Menu> menuCache = new HashMap<Long, Menu>();
		Menu menu = new Menu();
		menu.setId(123l);
		menu.setIsActive(true);
		menu.setFeatureCodeId(123l);
		menuCache.put(1l, menu);
		return menuCache;
	}

	private List<Menu> populateMenu() {
		List<Menu> menuList = new ArrayList<Menu>();
		Menu menu = new Menu();
		menu.setId(123l);
		menu.setIsActive(true);
		menu.setFeatureCodeId(123l);
		menu.setParentId(123);
		Menu menu1 = new Menu();
		menu1.setId(123l);
		menu1.setIsActive(true);
		menu1.setFeatureCodeId(123l);
		menu1.setParentId(123);
		menuList.add(menu);
		menuList.add(menu1);
		return menuList;
	}

	private Menu populateMenuRecord() {
		Menu menu = new Menu();
		menu.setId(123l);
		menu.setIsActive(true);
		menu.setFeatureCodeId(123l);
		menu.setParentId(1);
		return menu;
	}

	private List<CommonDTO> populateUserFeatures() {
		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO value = new CommonDTO();
		value.setCode("code12");
		value.setIsSelected(true);
		value.setId(123l);
		value.setName("Bbsi");
		value.setParentId(12l);
		value.setPrivileges(populatePrivilege());
		value.setDescription("Description");
		value.setNewStatus(true);
		value.setStatus(true);
		value.setChild(populatePrivilege());
		value.setType("ALL");
		CommonDTO data = new CommonDTO();
		data.setCode("code");
		data.setDescription("description");
		data.setId(1l);
		data.setName("name");
		data.setParentId(123l);
		data.setStatus(true);
		data.setType("type");
		List<CommonDTO> dataList = new ArrayList<>();
		dataList.add(data);
		commonDto.add(value);
		commonDto.add(data);
		commonDto.add(value);
		return commonDto;
	}

	private List<CommonDTO> populatePrivilege() {

		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO value = new CommonDTO();
		value.setCode("code12");
		value.setIsSelected(true);
		value.setId(123l);
		value.setName("Bbsi");
		value.setParentId(12l);
		commonDto.add(value);
		return commonDto;
	}

	private List<CommonDTO> populatePrivilegeForCodeEmpty() {

		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO value = new CommonDTO();
		value.setCode("12");
		value.setIsSelected(false);
		value.setId(123l);
		value.setName("Bbsi");
		value.setParentId(12l);
		value.setPrivileges(commonDto);
		value.setChild(commonDto);
		commonDto.add(value);

		return commonDto;
	}
	

	private List<CommonDTO> populaClientMappings() {

		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO value = new CommonDTO();
		value.setCode("code12");
		value.setIsSelected(true);
		value.setId(123l);
		value.setName("Bbsi");
		value.setParentId(12l);
		value.setPrivileges(populatePrivilegeForCommon());
		value.setChild(populateChild());
		commonDto.add(value);
		return commonDto;
	}

	private List<CommonDTO> populaClientMappingsWhenPrivilegeIsEmpty() {

		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO value = new CommonDTO();
		value.setCode("code12");
		value.setIsSelected(true);
		value.setId(123l);
		value.setName("Bbsi");
		value.setParentId(12l);
		value.setChild(populateChild());
		commonDto.add(value);
		commonDto.add(value);
		return commonDto;
	}

	private List<CommonDTO> populateChild() {

		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO value = new CommonDTO();
		value.setCode("code12");
		value.setIsSelected(true);
		value.setId(123l);
		value.setName("Bbsi");
		value.setParentId(12l);
		value.setPrivileges(populatePrivilegeForCommon());
		value.setChild(populatePrivilege());
		commonDto.add(value);
		return commonDto;
	}

	private List<CommonDTO> populatePrivilegeForCommon() {

		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO value = new CommonDTO();
		value.setCode("code12");
		value.setIsSelected(true);
		value.setId(123l);
		value.setName("Bbsi");
		value.setParentId(12l);
		commonDto.add(value);
		return commonDto;
	}

	private List<CommonDTO> populateUserFeaturesFormenuMapping() {
		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO value = new CommonDTO();
		value.setCode("");
		value.setIsSelected(true);
		value.setId(123l);
		value.setName("Bbsi");
		value.setParentId(12l);
		value.setPrivileges(new ArrayList<>());
		value.setDescription("Description");
		value.setNewStatus(true);
		value.setStatus(true);
		value.setChild(new ArrayList<>());
		value.setType("ALL");

		CommonDTO value1 = new CommonDTO();
		value1.setCode("");
		value1.setIsSelected(true);
		value1.setId(123l);
		value1.setName("Bbsi");
		value1.setParentId(12l);
		value1.setPrivileges(userFeatures);
		;
		value1.setDescription("Description");
		value1.setNewStatus(true);
		value1.setStatus(true);
		value1.setChild(userFeatures);
		value1.setType("ALL");
		commonDto.add(value);
		commonDto.add(value1);
		return commonDto;
	}

	private List<CommonDTO> populateUserFeaturesWhenPrivilegesIsEmpty() {
		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO value = new CommonDTO();
		value.setCode("");
		value.setIsSelected(true);
		value.setId(123l);
		value.setName("Bbsi");
		value.setParentId(12l);
		value.setPrivileges(new ArrayList<>());
		value.setDescription("Description");
		value.setNewStatus(true);
		value.setStatus(true);
		value.setChild(new ArrayList<>());
		value.setType("ALL");
		commonDto.add(value);
		return commonDto;
	}
}
