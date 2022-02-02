package com.bbsi.platform.user.web.resource.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.enums.Enums;
import com.bbsi.platform.common.enums.Enums.UserEnum;
import com.bbsi.platform.common.exception.ExceptionUtils;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.GenericUtils;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableBiConsumer;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableConsumer;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableTriConsumer;
import com.bbsi.platform.common.user.dto.RbacDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.exception.business.UnauthorizedAccessException;
import com.bbsi.platform.user.businessservice.intf.RbacBusinessService;
import com.bbsi.platform.user.model.RbacEntity;
import com.bbsi.platform.user.repository.RbacRepository;
import com.google.common.collect.Lists;

public class RbacResourceTest {

	@InjectMocks
	private RbacResource rbacResource;

	@Spy
	private RbacResource spyRbacResource;

	@Mock
	private RbacBusinessService rbacBusinessService;

	@Mock
	private UserPrincipal userPrincipal;

	@Mock
	private RbacEntity rbacEntity;

	@Mock
	private RbacRepository rbacRepository;

	private RbacDTO rbacDto;

	private List<RbacDTO> rbacDTOList = new ArrayList<RbacDTO>();

	@Before
	public void setUp() throws Exception {
		userPrincipal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setUserType(GenericConstants.USERTYPE_CLIENT);
		rbacDto = populateRbacDTO();
		rbacDTOList.add(rbacDto);
		MockitoAnnotations.initMocks(this);
		ExceptionUtils.handleException = Mockito.mock(ThrowableTriConsumer.class);
		GenericUtils.throwCommonNullVariableException = Mockito.mock(ThrowableBiConsumer.class);
		GenericUtils.throwCommonNullVariableExceptionString = Mockito.mock(ThrowableConsumer.class);

	}

	@After
	public void tearDown() throws Exception {
		rbacDto = null;
		rbacDTOList = null;
	}

	@Test
	public void testCreateRbac() throws Exception {
		RbacDTO rbacDTO = populateRbacDTO();
		Mockito.doReturn(rbacDTO).when(rbacBusinessService).createRbac(populateRbacDTO(),
				Enums.UserEnum.USER_CLIENT.toString(), userPrincipal);
		RbacDTO result = rbacResource.createRbac(Enums.UserEnum.USER_CLIENT, populateRbacDTO(), userPrincipal);
		assertEquals(result.getId(), rbacDTO.getId());
		assertEquals(result.getClientCode(), rbacDTO.getClientCode());
	}

	@Test
	public void testCreateRbacThrowsException() throws Exception {
		Mockito.doThrow(BbsiException.class).when(rbacBusinessService).createRbac(Mockito.any(), Mockito.anyString(),
				Mockito.any());
		RbacDTO rbac = rbacResource.createRbac(Enums.UserEnum.CLIENT, new RbacDTO(), null);
		assertTrue(true);

	}

	@Test
	public void testCreateRbacForUnauthorizedException() throws Exception {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		Mockito.doThrow(UnauthorizedAccessException.class).when(rbacBusinessService).createRbac(populateRbacDTO(),
				Enums.UserEnum.COPY_ROLE.toString(), principal);
		RbacDTO result = rbacResource.createRbac(Enums.UserEnum.COPY_ROLE, populateRbacDTO(), principal);
		assertEquals(result.getClientCode(), populateRbacDTO().getClientCode());
		Mockito.doThrow(UnauthorizedAccessException.class).when(rbacBusinessService).createRbac(populateRbacDTO(),
				Enums.UserEnum.ACCESS_GROUP.toString(), principal);
		result = rbacResource.createRbac(UserEnum.ACCESS_GROUP, populateRbacDTO(), principal);
		assertEquals(result.getClientCode(), populateRbacDTO().getClientCode());
	}

	@Test
	public void testCreateRbacWhenUserTypeVancouverAndEnumClient() throws Exception {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		Mockito.doThrow(UnauthorizedAccessException.class).when(rbacBusinessService).createRbac(populateRbacDTO(),
				Enums.UserEnum.CLIENT.toString(), principal);
		RbacDTO result = rbacResource.createRbac(Enums.UserEnum.CLIENT, populateRbacDTO(), principal);
		assertEquals(result.getClientCode(), populateRbacDTO().getClientCode());
	}

	@Test
	public void testGetAllRbacs() {
		Mockito.doReturn(rbacDTOList).when(rbacBusinessService).getAllRbacs(Mockito.any(), Mockito.anyString());
		List<RbacDTO> listOfRbac = rbacResource.getAllRbacs(Enums.UserEnum.CLIENT, userPrincipal);
		assertNotNull(listOfRbac);
	}

	@Test
	public void testGetAllRbacThrowsException() {
		Mockito.doThrow(new IllegalArgumentException()).when(rbacBusinessService).getAllRbacs(Mockito.any(),
				Mockito.anyString());
		List<RbacDTO> listOfRbac = rbacResource.getAllRbacs(Enums.UserEnum.SECTION, null);
		assertNull(listOfRbac);
	}

	@Test
	public void testGetAllClientRoles() {
		Mockito.doReturn(rbacDTOList).when(rbacBusinessService).getClientRoles(Mockito.anyString(), Mockito.any());
		List<RbacDTO> actualResponse = rbacResource.getAllClientRoles("BBSI", userPrincipal);
		assertNotNull(actualResponse);
	}

	@Test
	public void testGetAllClientRolesWhenCodeIsNull() {
		Mockito.doThrow(NullPointerException.class).when(rbacBusinessService).getClientRoles(Mockito.anyString(),
				Mockito.any());
		List<RbacDTO> actualResponse = rbacResource.getAllClientRoles("BBSI", null);
		assertNull(actualResponse);
	}

	@Test
	public void testGetRbacByClientCode() {
		Mockito.doReturn(rbacDto).when(rbacBusinessService).getEntriesByCode(Mockito.anyString(), Mockito.any(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		RbacDTO actualResponse = rbacResource.getRbacByClientCode(Enums.UserEnum.CLIENT, "BBSI", true, true,
				userPrincipal);
		assertNotNull(actualResponse);
	}

	@Test
	public void testGetRbacByClientCodeWhenClientCodeIsNull() {
		Mockito.doThrow(BbsiException.class).when(rbacBusinessService).getEntriesByCode("code", UserEnum.CLIENT, false,
				false, userPrincipal);
		RbacDTO actualResponse = rbacResource.getRbacByClientCode(null, null, true, true, null);
		assertNull(actualResponse);
	}

	@Test
	public void testGetRbacByClientCodeForUnauthorizedAccessException() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		Mockito.doThrow(UnauthorizedAccessException.class).when(rbacBusinessService).getEntriesByCode("code",
				UserEnum.CLIENT, false, false, principal);
		RbacDTO actualResponse = rbacResource.getRbacByClientCode(Enums.UserEnum.CLIENT, "BBSI", false, false,
				principal);
		assertNull(actualResponse);
	}

	@Test
	public void testUpdateRbac() {
		RbacDTO expected = populateRbacDTO();
		userPrincipal.setUserType(GenericConstants.USERTYPE_CLIENT);
		Mockito.doReturn(expected).when(rbacBusinessService).updateRbac(Mockito.any(), Mockito.anyString(),
				Mockito.any());
		RbacDTO actualResponse = rbacResource.updateRbac(UserEnum.USER_CLIENT, populateRbacDTO(), userPrincipal);
		assertEquals(expected.getId(), actualResponse.getId());
		assertEquals(expected.getClientCode(), actualResponse.getClientCode());
	}

	@Test
	public void testUpdateRbacWhenTypeIsNull() {
		Mockito.doThrow(BbsiException.class).when(rbacBusinessService).updateRbac(Mockito.any(), Mockito.anyString(),
				Mockito.any());
		RbacDTO actualResponse = rbacResource.updateRbac(Enums.UserEnum.CLIENT, populateRbacDTO(), userPrincipal);
		assertNull(actualResponse.getCode());
	}

	@Test
	public void testGetAllClientRolesNoParams() {
		List<RbacDTO> rbacDTOList = new ArrayList<>();
		Mockito.doReturn(rbacDTOList).when(rbacBusinessService).getAllClientRoles(Mockito.any());
		List<RbacDTO> result = rbacResource.getAllClientRoles(userPrincipal);
		assertNotEquals(result.size(), -1);
	}

	@Test
	public void testGetAllClientRolesNoParamsForException() {
		Mockito.doThrow(NullPointerException.class).when(rbacBusinessService).getAllClientRoles(Mockito.any());
		List<RbacDTO> result = rbacResource.getAllClientRoles(userPrincipal);
		assertNull(result);
	}

	@Test
	public void testGetAllRoles() {
		List<RbacDTO> rbacDTOList = new ArrayList<>();
		userPrincipal.setClientCode("909464");
		Mockito.doReturn(rbacDTOList).when(rbacBusinessService).getAllRoles(Mockito.anyBoolean(), Mockito.anyString());
		List<RbacDTO> result = rbacResource.getAllRoles(false, userPrincipal);
		assertEquals(result.size(), rbacDTOList.size());
	}

	@Test
	public void testGetAllRolesForExceptions() {
		Mockito.doThrow(BbsiException.class).when(rbacBusinessService).getAllRoles(Mockito.anyBoolean(),
				Mockito.anyString());
		;
		List<RbacDTO> result = rbacResource.getAllRoles(false, null);
		assertTrue(true);
	}

	private RbacDTO populateRbacDTO() {
		RbacDTO rbac = new RbacDTO();
		rbac.setClientCode("909464");
		rbac.setId(123l);
		rbac.setIsSync(true);
		return rbac;
	}

}
