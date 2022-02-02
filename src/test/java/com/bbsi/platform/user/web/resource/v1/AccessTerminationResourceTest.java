package com.bbsi.platform.user.web.resource.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.exception.ExceptionUtils;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableTriConsumer;
import com.bbsi.platform.common.user.dto.AccessTerminationDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.user.businessservice.intf.AccessTerminationBusinessService;
import com.google.common.collect.Lists;

/**
 * @author mprasad
 *
 */
public class AccessTerminationResourceTest {

	@InjectMocks
	private AccessTerminationResource accessTerminationResource;

	@Mock
	private AccessTerminationBusinessService accessTerminationService;

	private AccessTerminationDTO accessTerminationDto;

	private UserPrincipal userPrincipal;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		userPrincipal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setEmail("integration@osius.com");
		userPrincipal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		accessTerminationDto = populateAccessTerminationDto();
		ExceptionUtils.handleException = Mockito.mock(ThrowableTriConsumer.class);
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testSaveAccessTerminationInfo() {

		doReturn(accessTerminationDto).when(accessTerminationService).saveAccessTermination(accessTerminationDto);

		AccessTerminationDTO actualAccessTerminationDto = accessTerminationResource
				.saveAccessTerminationInfo(accessTerminationDto, userPrincipal);

		assertNotNull(actualAccessTerminationDto);
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
		assertEquals(accessTerminationDto.getEmployeeCode(), actualAccessTerminationDto.getEmployeeCode());
		assertEquals(accessTerminationDto.getId(), actualAccessTerminationDto.getId());

	}

	@Test
	public void testSaveAccessTerminationInfoThrowsException() {
		accessTerminationDto = null;
		Mockito.doThrow(new IllegalArgumentException()).when(accessTerminationService)
				.saveAccessTermination(accessTerminationDto);
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationResource
				.saveAccessTerminationInfo(accessTerminationDto, userPrincipal);
		assertNull(actualAccessTerminationDto);

	}

	@Test
	public void testSaveAccessTerminationInfoWhenDTOAndUserPrincipalNull() {
		doThrow(BbsiException.class).when(accessTerminationService).saveAccessTermination(Mockito.any());
		AccessTerminationDTO result = accessTerminationResource.saveAccessTerminationInfo(new AccessTerminationDTO(),
				null);
		assertNull(result.getClientCode());
	}

	@Test
	public void testSaveAccessTerminationInfoInvalidType() {
		userPrincipal.setUserType("InvalidUserType");
		doThrow(BbsiException.class).when(accessTerminationService).saveAccessTermination(Mockito.any());
		AccessTerminationDTO result = accessTerminationResource.saveAccessTerminationInfo(new AccessTerminationDTO(),
				userPrincipal);
		assertNull(result.getClientCode());
	}

	@Test
	public void testUpdateAccessTerminationInfo() {

		doReturn(accessTerminationDto).when(accessTerminationService).updateAccessTermination(accessTerminationDto);

		AccessTerminationDTO actualAccessTerminationDto = accessTerminationResource
				.updateAccessTerminationInfo(accessTerminationDto);

		assertNotNull(actualAccessTerminationDto);
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
		assertEquals(accessTerminationDto.getEmployeeCode(), actualAccessTerminationDto.getEmployeeCode());
		assertEquals(accessTerminationDto.getId(), actualAccessTerminationDto.getId());

	}

	@Test
	public void testUpdateAccessTerminationInfoThrowsException() {

		doThrow(BbsiException.class).when(accessTerminationService).updateAccessTermination(accessTerminationDto);

		AccessTerminationDTO actualAccessTerminationDto = accessTerminationResource
				.updateAccessTerminationInfo(accessTerminationDto);

		assertNotNull(actualAccessTerminationDto);
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
		assertEquals(accessTerminationDto.getEmployeeCode(), actualAccessTerminationDto.getEmployeeCode());
		assertEquals(accessTerminationDto.getId(), actualAccessTerminationDto.getId());

	}

	@Test
	public void testUpdateStatusByEndDate() {

		doNothing().when(accessTerminationService).updateStatusByEndDate();

		accessTerminationResource.updateStatusByEndDate(userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testUpdateStatusByEndDateWhenEmailIsNotIntegration() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail("admin@osius.com");
		doThrow(BbsiException.class).when(accessTerminationService).updateStatusByEndDate();
		accessTerminationResource.updateStatusByEndDate(principal);
		assertTrue(true);
	}

	@Test
	public void testUpdateStatusByEndDateForExceptions() {
		doThrow(BbsiException.class).when(accessTerminationService).updateStatusByEndDate();
		accessTerminationResource.updateStatusByEndDate(null);
		assertTrue(true);
	}

	@Test
	public void testUpdateStatusByEndDateForOtherExceptions() {
		doThrow(BbsiException.class).when(accessTerminationService).updateStatusByEndDate();
		accessTerminationResource.updateStatusByEndDate(new UserPrincipal(" ", " ", Lists.newArrayList()));
		assertTrue(true);
	}

	@Test
	public void testGetTerminatedClientByClientCode() {

		doReturn(accessTerminationDto).when(accessTerminationService).getTerminatedClientByClientCode("ClientCode");

		AccessTerminationDTO actualAccessTerminationDto = accessTerminationResource
				.getTerminatedClientByClientCode("ClientCode", userPrincipal);

		assertNotNull(actualAccessTerminationDto);
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
		assertEquals(accessTerminationDto.getEmployeeCode(), actualAccessTerminationDto.getEmployeeCode());
		assertEquals(accessTerminationDto.getId(), actualAccessTerminationDto.getId());

	}

	@Test
	public void testGetTerminatedClientByClientCodePrincipalNull() {
		doReturn(new AccessTerminationDTO()).when(accessTerminationService)
				.getTerminatedClientByClientCode("ClientCode");
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationResource
				.getTerminatedClientByClientCode("ClientCode", null);
		assertNull(actualAccessTerminationDto);

	}

	@Test
	public void testGetTerminatedClientByClientCodeInvalidUserType() {
		userPrincipal.setUserType("InvalidUserType");
		doReturn(new AccessTerminationDTO()).when(accessTerminationService)
				.getTerminatedClientByClientCode("ClientCode");
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationResource
				.getTerminatedClientByClientCode("ClientCode", userPrincipal);
		assertNull(actualAccessTerminationDto);

	}

	@Test
	public void testGetTerminatedClientByClientCodeThrowsException() {
		doThrow(BbsiException.class).when(accessTerminationService)
				.getTerminatedClientByClientCode(Mockito.anyString());
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationResource
				.getTerminatedClientByClientCode("ClientCode", userPrincipal);
		assertNull(actualAccessTerminationDto);

	}

	@Test
	public void testGetEmployeeAccessInfo() {
		doReturn(accessTerminationDto).when(accessTerminationService).getEmployeeAccessInfo("ClientCode",
				"EmployeeCode");
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationResource.getEmployeeAccessInfo("ClientCode",
				"EmployeeCode", userPrincipal);
		assertNotNull(actualAccessTerminationDto);
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
		assertEquals(accessTerminationDto.getEmployeeCode(), actualAccessTerminationDto.getEmployeeCode());
		assertEquals(accessTerminationDto.getId(), actualAccessTerminationDto.getId());
	}

	@Test
	public void testGetEmployeeAccessInfoUserPrincipalNull() {
		doReturn(new AccessTerminationDTO()).when(accessTerminationService).getEmployeeAccessInfo("ClientCode",
				"EmployeeCode");
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationResource.getEmployeeAccessInfo("ClientCode",
				"EmployeeCode", null);
		assertNull(actualAccessTerminationDto);
	}

	@Test
	public void testGetEmployeeAccessInfoInvalidUserTypes() {
		userPrincipal.setUserType("InvalidUserType");
		doReturn(new AccessTerminationDTO()).when(accessTerminationService).getEmployeeAccessInfo("ClientCode",
				"EmployeeCode");
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationResource.getEmployeeAccessInfo("ClientCode",
				"EmployeeCode", userPrincipal);
		assertNull(actualAccessTerminationDto);
	}

	@Test
	public void testGetEmployeeAccessInfoThrowsException() {
		doThrow(BbsiException.class).when(accessTerminationService).getEmployeeAccessInfo(Mockito.anyString(),
				Mockito.anyString());
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationResource.getEmployeeAccessInfo("904064",
				"Emp123", userPrincipal);

		assertNull(actualAccessTerminationDto);

	}

	private AccessTerminationDTO populateAccessTerminationDto() {

		AccessTerminationDTO accessTerminationDTO = new AccessTerminationDTO();
		accessTerminationDTO.setClientCode("clientCode");
		accessTerminationDTO.setEmployeeCode("employeeCode");
		accessTerminationDTO.setId(1l);

		return accessTerminationDTO;
	}
}
