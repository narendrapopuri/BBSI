package com.bbsi.platform.user.businessservice.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.doNothing;


import com.bbsi.platform.common.exception.ExceptionUtils;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableTriConsumer;
import com.bbsi.platform.common.user.dto.UserFilterConfigDTO;
import com.bbsi.platform.common.user.dto.UserFilterConfigValueDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.user.mapper.UserFilterConfigMapper;
import com.bbsi.platform.user.mapper.UserFilterConfigValueMapper;
import com.bbsi.platform.user.model.UserFilterConfig;
import com.bbsi.platform.user.model.UserFilterConfigValue;
import com.bbsi.platform.user.repository.UserFilterConfigRepository;
import com.bbsi.platform.user.repository.UserFilterConfigValueRepository;

public class UserFilterConfigBusinessServiceImplTest {
	
	@InjectMocks
	private UserFilterConfigBusinessServiceImpl userFilterConfigBusinessServiceImpl;
	
	@Mock
	private UserFilterConfigRepository userFilterConfigRepository;
	
	@Mock
    private UserFilterConfigValueRepository userFilterConfigValueRepository;
	
	@Mock
	private UserFilterConfigMapper  userFilterConfigMapper;
	
	@Mock
	private UserFilterConfigValueMapper  userFilterConfigValueMapper;
	
	private UserFilterConfigDTO userFilterConfigDTO;
	
	private UserFilterConfigValueDTO userFilterConfigValueDTO;
	
	private UserFilterConfig userFilterConfig;
	
	public List<UserFilterConfigDTO> userFilterConfigDTOList = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		userFilterConfigDTO = getUserFilterConfigDTO();
		userFilterConfig = getUserFilterConfig();
		ExceptionUtils.handleException = Mockito.mock(ThrowableTriConsumer.class);
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetAllUserFilterConfigsByClientCodeAndUserId() {
		List<UserFilterConfig> userFilterConfigs = new ArrayList<>();
		userFilterConfigDTOList.add(userFilterConfigDTO);
		doReturn(userFilterConfigs).when(userFilterConfigRepository).findByClientCodeAndUserId(Mockito.anyString(), Mockito.anyLong());
		doReturn(userFilterConfigDTOList).when(userFilterConfigMapper).userFilterConfigListToUserFilterConfigDTOList(Mockito.anyList());
		List<UserFilterConfigDTO> result = userFilterConfigBusinessServiceImpl.getAllUserFilterConfigsByClientCodeAndUserId("909464",5 );
		assertEquals(userFilterConfigDTOList.size(), result.size());
		
	}
	
	@Test
	public void testGetAllUserFilterConfigsByClientCodeAndUserIdForException() {
		userFilterConfigDTOList.add(userFilterConfigDTO);
		doThrow(BbsiException.class).when(userFilterConfigRepository).findByClientCodeAndUserId(null, 0l);
		doReturn(userFilterConfigDTOList).when(userFilterConfigMapper).userFilterConfigListToUserFilterConfigDTOList(Mockito.anyList());
		userFilterConfigBusinessServiceImpl.getAllUserFilterConfigsByClientCodeAndUserId(null, 0l);
		Mockito.verify(userFilterConfigRepository, Mockito.atLeastOnce()).findByClientCodeAndUserId(null, 0l);
		assertTrue(true);
		
	}
	
	@Test
	public void testGetAllUserFilterConfigsByClientCodeAndUserIdAndFilterId() {
		doReturn(userFilterConfig).when(userFilterConfigRepository).findByClientCodeAndUserIdAndId("909464", 5, 2);
		doReturn(userFilterConfigDTO).when(userFilterConfigMapper).userFilterConfigToUserFilterConfigDTO(userFilterConfig);
		String result = userFilterConfigBusinessServiceImpl.getAllUserFilterConfigsByClientCodeAndUserIdAndFilterId("909464", 5, 2);
		assertNotNull(result);
	}
	
	@Test
	public void testGetAllUserFilterConfigsByClientCodeAndUserIdAndFilterIdForIllegalException() {
		doReturn(null).when(userFilterConfigRepository).findByClientCodeAndUserIdAndId("909464", 5, 2);
		doReturn(null).when(userFilterConfigMapper).userFilterConfigToUserFilterConfigDTO(userFilterConfig);
		String result = userFilterConfigBusinessServiceImpl.getAllUserFilterConfigsByClientCodeAndUserIdAndFilterId("909464", 5, 2);
		assertNotNull(result);
	}
	
	@Test
	public void testGetAllUserFilterConfigsByClientCodeAndUserIdAndFilterIdForException() {
		List<UserFilterConfig> userFilterConfigs = new ArrayList<>();
		doThrow(BbsiException.class).when(userFilterConfigRepository).findByClientCodeAndUserIdAndId(null, 0l, 0l);
		doReturn(userFilterConfigs).when(userFilterConfigMapper).userFilterConfigListToUserFilterConfigDTOList(Mockito.anyList());
		String result = userFilterConfigBusinessServiceImpl.getAllUserFilterConfigsByClientCodeAndUserIdAndFilterId(null, 0l, 0l);
		assertTrue(result.isEmpty());
		Mockito.verify(userFilterConfigRepository, Mockito.atLeastOnce()).findByClientCodeAndUserIdAndId(null, 0l, 0l);

	}
	
	@Test
	public void testDeleteByClientCodeAndUserIdAndFilterId() {
		
		Mockito.doNothing().when(userFilterConfigRepository).deleteByClientCodeAndUserIdAndId(userFilterConfigDTO.getClientCode(), userFilterConfigDTO.getUserId(), userFilterConfigDTO.getId());
		userFilterConfigBusinessServiceImpl.deleteByClientCodeAndUserIdAndFilterId(userFilterConfigDTO.getClientCode(), userFilterConfigDTO.getUserId(), userFilterConfigDTO.getId());
		Mockito.verify(userFilterConfigRepository, Mockito.atLeastOnce()).deleteByClientCodeAndUserIdAndId(userFilterConfigDTO.getClientCode(), userFilterConfigDTO.getUserId(), userFilterConfigDTO.getId());
		assertTrue(true);
	}
	
	@Test
	public void testDeleteByClientCodeAndUserIdAndFilterIdForException() {
		Mockito.doThrow(BbsiException.class).when(userFilterConfigRepository).deleteByClientCodeAndUserIdAndId(null, 0l, 0l);
		userFilterConfigBusinessServiceImpl.deleteByClientCodeAndUserIdAndFilterId(null, 0l, 0l);
		Mockito.verify(userFilterConfigRepository, Mockito.atLeastOnce()).deleteByClientCodeAndUserIdAndId(null, 0l, 0l);
		assertTrue(true);
	}
	
	@Test
	public void testCreateUserFilterConfig() {
		String userConfigPayload = "{\r\n" + "  \"client_code\": \"909464\",\r\n" + "  \"user_id\": 100,\r\n"
				+ "  \"filter_name\": \"Division By 1\",\r\n" + "  \"user_filter_config_pay_load_dto\": {\r\n"
				+ "    \"STATUS_TYPE\": [\r\n" + "      \"A\",\r\n" + "      \"I\"\r\n" + "    ],\r\n"
				+ "    \"EMPLOYEE_TYPE\": [\r\n" + "      \"B\",\r\n" + "      \"F\"\r\n" + "    ],\r\n"
				+ "    \"DEPARTMENT\": [\r\n" + "      \"009\",\r\n" + "      \"1\"\r\n" + "    ]\r\n" + "  }\r\n"
				+ "}";
		doReturn(userFilterConfig).when(userFilterConfigMapper).userFilterConfigDTOToUserFilterConfig(Mockito.any());
		doReturn(userFilterConfig).when(userFilterConfigRepository).save(userFilterConfig);
		doReturn(userFilterConfigDTO).when(userFilterConfigMapper).userFilterConfigToUserFilterConfigDTO(Mockito.any());
		UserFilterConfigDTO result = userFilterConfigBusinessServiceImpl.createUserFilterConfig(userConfigPayload, userFilterConfigDTO.getClientCode(), userFilterConfigDTO.getUserId());
		assertEquals(userFilterConfigDTO.getClientCode(), result.getClientCode());
		assertEquals(userFilterConfigDTO.getId(), result.getId());
		assertEquals(userFilterConfigDTO.getUserId(), result.getUserId());
	}
	
	@Test
	public void testCreateUserFilterConfigForException() {
		String userConfigPayload = null;
		doThrow(BbsiException.class).when(userFilterConfigMapper).userFilterConfigDTOToUserFilterConfig(Mockito.any());
		doThrow(BbsiException.class).when(userFilterConfigRepository).save(userFilterConfig);
		doThrow(BbsiException.class).when(userFilterConfigMapper).userFilterConfigToUserFilterConfigDTO(Mockito.any());
		UserFilterConfigDTO result = userFilterConfigBusinessServiceImpl.createUserFilterConfig(userConfigPayload, null, 0l);
		assertNull(result);
	}
	
	
	@Test
	public void testUpdateUserFilterConfig() {
		String userConfigPayload = "{\r\n" + "  \"client_code\": \"909464\",\r\n" + "  \"user_id\": 5,\r\n"
				+ "  \"filter_name\": \"Division By 1\",\r\n" + "  \"user_filter_config_pay_load_dto\": {\r\n"
				+ "    \"testKey\": [\r\n" + "      \"testValue\"\r\n" + "    ]\r\n" + "  }\r\n" + "}";
		UserFilterConfig userFilterConfigData = userFilterConfig;
		UserFilterConfigDTO userFilterConfigDTOData = userFilterConfigDTO;
		UserFilterConfigValueDTO userFilterConfigValueDTOData = userFilterConfigValueDTO;
		doNothing().when(userFilterConfigValueRepository).deleteUserFilterConfigValueByFilterId(2l);
		doReturn(userFilterConfigData).when(userFilterConfigRepository).findByClientCodeAndUserIdAndId("909464",5l,2l);
		doReturn(userFilterConfigData).when(userFilterConfigRepository).save(userFilterConfigData);
		doReturn(userFilterConfigDTOData).when(userFilterConfigMapper).userFilterConfigToUserFilterConfigDTO(Mockito.any());
		doReturn(userFilterConfigValueDTOData).when(userFilterConfigValueMapper).userFilterConfigValueDTOToUserFilterConfigValue(Mockito.any());
		UserFilterConfigDTO result = userFilterConfigBusinessServiceImpl.updateUserFilterConfig(2, userConfigPayload, "909464", 5l);
		assertEquals(userFilterConfigDTO.getClientCode(), result.getClientCode());
		assertEquals(userFilterConfigDTO.getId(), result.getId());
		assertEquals(userFilterConfigDTO.getUserId(), result.getUserId());
	}
	
	@Test
	public void testUpdateUserFilterConfigForException() {
		String userConfigPayload = null;
		doThrow(BbsiException.class).when(userFilterConfigMapper).userFilterConfigDTOToUserFilterConfig(Mockito.any());
		doThrow(BbsiException.class).when(userFilterConfigRepository).save(userFilterConfig);
		doThrow(BbsiException.class).when(userFilterConfigMapper).userFilterConfigToUserFilterConfigDTO(Mockito.any());
		UserFilterConfigDTO result = userFilterConfigBusinessServiceImpl.updateUserFilterConfig(2, userConfigPayload, "909464", 5l);
		assertNull(result);
	}
	
	public UserFilterConfigDTO getUserFilterConfigDTO() {
		
		List<UserFilterConfigValueDTO> userFilterConfigValuesDTO = new ArrayList<>();
		UserFilterConfigValueDTO userFilterConfigValueDTO = new UserFilterConfigValueDTO();
		userFilterConfigValueDTO.setId(1);
		userFilterConfigValueDTO.setJsonKey("testKey");
		userFilterConfigValueDTO.setJsonValue("testValue");
		userFilterConfigValuesDTO.add(userFilterConfigValueDTO);
		
		UserFilterConfigDTO userFilterConfigDto = new UserFilterConfigDTO();
		userFilterConfigDto.setId(2);
		userFilterConfigDto.setClientCode("909464");
		userFilterConfigDto.setModifiedBy("1");
		userFilterConfigDto.setUserId(5);
		userFilterConfigDto.setIsHrpLoad(false);
		userFilterConfigDto.setUserFilterConfigValues(userFilterConfigValuesDTO);
		return userFilterConfigDto;
	}
	
	public UserFilterConfig getUserFilterConfig() {
		List<UserFilterConfigValue> userFilterConfigValues = new ArrayList<>();
		UserFilterConfigValue userFilterConfigValue = new UserFilterConfigValue();
		userFilterConfigValue.setId(1);
		userFilterConfigValue.setJsonKey("testKey");
		userFilterConfigValue.setJsonValue("testValue");
		userFilterConfigValues.add(userFilterConfigValue);
		
		UserFilterConfig userFilterConfig = new UserFilterConfig();
		userFilterConfig.setId(2);
		userFilterConfig.setClientCode("909464");
		userFilterConfig.setModifiedBy("1");
		userFilterConfig.setUserId(5);
		userFilterConfig.setUserFilterConfigValues(userFilterConfigValues);
		return userFilterConfig;
	}
}
