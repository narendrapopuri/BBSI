package com.bbsi.platform.user.web.resource.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.user.dto.UserFilterConfigDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.exception.business.UnauthorizedAccessException;
import com.bbsi.platform.user.businessservice.intf.UserFilterConfigBusinessService;
import com.beust.jcommander.internal.Lists;

public class UserFilterConfigResourceTest {
	
	@InjectMocks
	private UserFilterConfigResource userFilterConfigResource;
	
	@Mock
	private UserFilterConfigBusinessService userFilterConfigBusinessService;
	
	private UserPrincipal userPrincipal;
	
	private UserFilterConfigDTO userFilterConfigDTO;
	
	public List<UserFilterConfigDTO> userFilterConfigDTOList = new ArrayList<>();
	
	@Before
	public void setUp() throws Exception {
		userFilterConfigDTO = getUserFilterConfigDTO();
		userPrincipal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setEmail("integration@osius.com");
		userPrincipal.setClientCode(userFilterConfigDTO.getClientCode());
		userPrincipal.setUserId(userFilterConfigDTO.getUserId());
		MockitoAnnotations.initMocks(this);

	}
	
	@Test
	public void testGetAllUserFilterConfigsByClientCodeAndUserId() {
		userFilterConfigDTOList.add(userFilterConfigDTO);
		Mockito.doReturn(userFilterConfigDTOList).when(userFilterConfigBusinessService).getAllUserFilterConfigsByClientCodeAndUserId(userFilterConfigDTO.getClientCode(), userFilterConfigDTO.getUserId());
		List<UserFilterConfigDTO> result = userFilterConfigResource.getAllUserFilterConfigsByClientCodeAndUserId(userPrincipal);
		assertEquals("909464", result.get(0).getClientCode());
		assertEquals(5, result.get(0).getUserId());
	}
	
	@Test
	public void testGetAllUserFilterConfigsByClientCodeAndUserIdNotPresent() {
		Mockito.doThrow(BbsiException.class).when(userFilterConfigBusinessService).getAllUserFilterConfigsByClientCodeAndUserId(null, 01);
		userFilterConfigResource.getAllUserFilterConfigsByClientCodeAndUserId(null);
		assertTrue(true);
	}
	
	@Test
	public void testGetUserFilterConfigsByClientCodeAndUserIdAndFilterId() {
		userFilterConfigDTOList.add(userFilterConfigDTO);
		String userFilterConfigPayload = "test";
		Mockito.doReturn(userFilterConfigPayload).when(userFilterConfigBusinessService).getAllUserFilterConfigsByClientCodeAndUserIdAndFilterId(userFilterConfigDTO.getClientCode(), userFilterConfigDTO.getUserId(), userFilterConfigDTO.getId());
		String result = userFilterConfigResource.getUserFilterConfigsByClientCodeAndUserIdAndFilterId(userFilterConfigDTO.getId(), userPrincipal);
		assertNotNull(result);
	}
	
	@Test
	public void testGetUserFilterConfigsByClientCodeAndUserIdAndFilterIdThrowException() {
		Mockito.doThrow(BbsiException.class).when(userFilterConfigBusinessService).getAllUserFilterConfigsByClientCodeAndUserIdAndFilterId(null, 0l, 0l);
		userFilterConfigResource.getUserFilterConfigsByClientCodeAndUserIdAndFilterId(0l, null);
		assertTrue(true);
	}
	
	@Test
	public void testDeleteUserFilterConfigByClientCodeAndUserIdAndFilterId() {
		Mockito.doNothing().when(userFilterConfigBusinessService).deleteByClientCodeAndUserIdAndFilterId(userFilterConfigDTO.getClientCode(),userFilterConfigDTO.getUserId(), userFilterConfigDTO.getId());
		userFilterConfigResource.deleteUserFilterConfigByClientCodeAndUserIdAndFilterId(userFilterConfigDTO.getId(), userPrincipal);
		Mockito.verify(userFilterConfigBusinessService, Mockito.atLeastOnce()).deleteByClientCodeAndUserIdAndFilterId(userFilterConfigDTO.getClientCode(),userFilterConfigDTO.getUserId(), userFilterConfigDTO.getId());
		assertTrue(true);
	}
	
	@Test
	public void testDeleteUserFilterConfigByClientCodeAndUserIdAndFilterIdWithInvalidIds() {
		String clientCode = null;
		long userId = -1l;
		long id = -1l;
		Mockito.doThrow(BbsiException.class).when(userFilterConfigBusinessService).deleteByClientCodeAndUserIdAndFilterId(clientCode, userId, id);
		userFilterConfigResource.deleteUserFilterConfigByClientCodeAndUserIdAndFilterId(id, null);
		assertTrue(true);
	}
	
	@Test
	public void testCreateUserFilterConfig() {
		String userConfigPayload = "{\r\n" + "  \"client_code\": \"909464\",\r\n" + "  \"user_id\": 5,\r\n"
				+ "  \"filter_name\": \"Division By 1\",\r\n" + "  \"user_filter_config_pay_load_dto\": {\r\n"
				+ "    \"STATUS_TYPE\": [\r\n" + "      \"A\",\r\n" + "      \"I\"\r\n" + "    ],\r\n"
				+ "    \"EMPLOYEE_TYPE\": [\r\n" + "      \"B\",\r\n" + "      \"F\"\r\n" + "    ],\r\n"
				+ "    \"DEPARTMENT\": [\r\n" + "      \"009\",\r\n" + "      \"1\"\r\n" + "    ]\r\n" + "  },\r\n"
				+ "  \"user_filter_config_values\": [\r\n" + "    \r\n" + "  ]\r\n" + "}";
		userPrincipal.setClientCode("909464");
		Mockito.doReturn(userFilterConfigDTO).when(userFilterConfigBusinessService).createUserFilterConfig(userConfigPayload, userPrincipal.getClientCode(), userPrincipal.getUserId());
		userFilterConfigResource.createUserFilterConfig(userConfigPayload, userPrincipal);
		assertTrue(true);
	}
	
	@Test
	public void testCreateUserFilterConfigThrowsException() {
		String userConfigPayload = "{\r\n" + "  \"client_code\": \"909464\",\r\n" + "  \"user_id\": 5,\r\n"
				+ "  \"filter_name\": \"Division By 1\",\r\n" + "  \"user_filter_config_pay_load_dto\": {\r\n"
				+ "    \"STATUS_TYPE\": [\r\n" + "      \"A\",\r\n" + "      \"I\"\r\n" + "    ],\r\n"
				+ "    \"EMPLOYEE_TYPE\": [\r\n" + "      \"B\",\r\n" + "      \"F\"\r\n" + "    ],\r\n"
				+ "    \"DEPARTMENT\": [\r\n" + "      \"009\",\r\n" + "      \"1\"\r\n" + "    ]\r\n" + "  },\r\n"
				+ "  \"user_filter_config_values\": [\r\n" + "    \r\n" + "  ]\r\n" + "}";
		Mockito.doThrow(UnauthorizedAccessException.class).when(userFilterConfigBusinessService).createUserFilterConfig(userConfigPayload, userPrincipal.getClientCode(), userPrincipal.getUserId());
		userFilterConfigResource.createUserFilterConfig(userConfigPayload, userPrincipal);
		assertTrue(true);
	}
	
	@Test
    public void testUpdateUserFilterConfig() {
        String userConfigPayload = "{\r\n" + "  \"client_code\": \"909464\",\r\n" + "  \"user_id\": 5,\r\n"
                + "  \"filter_name\": \"Division By 1\",\r\n" + "  \"user_filter_config_pay_load_dto\": {\r\n"
                + "    \"STATUS_TYPE\": [\r\n" + "      \"A\",\r\n" + "      \"I\"\r\n" + "    ],\r\n"
                + "    \"EMPLOYEE_TYPE\": [\r\n" + "      \"B\",\r\n" + "      \"F\"\r\n" + "    ],\r\n"
                + "    \"DEPARTMENT\": [\r\n" + "      \"009\",\r\n" + "      \"1\"\r\n" + "    ]\r\n" + "  },\r\n"
                + "  \"user_filter_config_values\": [\r\n" + "    \r\n" + "  ]\r\n" + "}";
        userPrincipal.setClientCode("909464");
        Mockito.doReturn(userFilterConfigDTO).when(userFilterConfigBusinessService).updateUserFilterConfig(2l,userConfigPayload, userPrincipal.getClientCode(), userPrincipal.getUserId());
        UserFilterConfigDTO result = userFilterConfigResource.updateUserFilterConfig(2l,userConfigPayload, userPrincipal);
        assertNotNull(result);
    }
   
    @Test
    public void testUpdateUserFilterConfigThrowsException() {
        Mockito.doThrow(BbsiException.class).when(userFilterConfigBusinessService).updateUserFilterConfig(0l, null, null, 0l);
        userFilterConfigResource.updateUserFilterConfig(0l, null, null);
        assertTrue(true);
    }
	
	public UserFilterConfigDTO getUserFilterConfigDTO() {
		UserFilterConfigDTO userFilterConfigDto = new UserFilterConfigDTO();
		userFilterConfigDto.setId(2);
		userFilterConfigDto.setClientCode("909464");
		userFilterConfigDto.setModifiedBy("1");
		userFilterConfigDto.setUserId(5);
		userFilterConfigDto.setIsHrpLoad(false);;
		return userFilterConfigDto;
	}

}
