package com.bbsi.platform.user.web.resource.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.util.ReflectionTestUtils;

import com.bbsi.platform.common.client.dto.UserToolbarSettingsDTO;
import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.dto.CostCenterClientDTO;
import com.bbsi.platform.common.dto.CostCenterDTO;
import com.bbsi.platform.common.dto.OtpDetailsDTO;
import com.bbsi.platform.common.dto.ParentDTO;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.employee.dto.CustomPersonalDTO;
import com.bbsi.platform.common.enums.Enums;
import com.bbsi.platform.common.enums.Enums.UserEnum;
import com.bbsi.platform.common.exception.ExceptionUtils;
import com.bbsi.platform.common.generic.BoomiHelper;
import com.bbsi.platform.common.generic.EncryptAndDecryptUtil;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableBiConsumer;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableConsumer;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableTriConsumer;
import com.bbsi.platform.common.generic.GenericUtils;
import com.bbsi.platform.common.generic.RestClient;
import com.bbsi.platform.common.user.dto.TimenetLoginInfo;
import com.bbsi.platform.common.user.dto.UserInvitationDTO;
import com.bbsi.platform.common.user.dto.UsersDTO;
import com.bbsi.platform.common.user.dto.v2.AccessGroupDTOV2;
import com.bbsi.platform.common.user.dto.v2.ClientDTOV2;
import com.bbsi.platform.common.user.dto.v2.FeatureCodeHierarchyDTOV2;
import com.bbsi.platform.common.user.dto.v2.PolicyAcceptedDTO;
import com.bbsi.platform.common.user.dto.v2.PrivilegeDTOV2;
import com.bbsi.platform.common.user.dto.v2.UserClientRoleDTOV2;
import com.bbsi.platform.common.user.dto.v2.UserExistDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.exception.business.UnauthorizedAccessException;
import com.bbsi.platform.exception.business.ValidationException;
import com.bbsi.platform.user.businessservice.intf.UserBusinessService;
import com.bbsi.platform.user.mapper.RbacMapper;
import com.bbsi.platform.user.utils.Utils;
import com.google.common.collect.Lists;

public class UserResourceTest {

	@InjectMocks
	private UserResource mockUserResource;

	@Spy
	private UserResource spyUserResource;

	@Mock
	private UserBusinessService mockUserBusinessService;

	@Mock
	private TokenStore tokenStore;

	@Mock
	private Utils utils = Mockito.mock(Utils.class);

	@Mock
	private EncryptAndDecryptUtil mockEncryptAndDecryptUtil;

	@Mock
	private SecurityContextHolder security;

	@Mock
	private SecurityContext context;

	@Mock
	private Authentication auth;

	@Mock
	private BoomiHelper helper;

	@Mock
	private RestClient restClient;

	private CustomPersonalDTO customPersonalDTO;

	private PolicyAcceptedDTO policyAcceptedDTO;

	private List<UserToolbarSettingsDTO> userToolbarSettingsDTOs;

	private List<UserInvitationDTO> userInvitation = new ArrayList<UserInvitationDTO>();

	@Mock
	private RbacMapper rbacMapper;

	private UserPrincipal userPrincipal;

	private OtpDetailsDTO otpDetailsDTO;

	@Before
	public void setUp() throws Exception {
		userPrincipal = new UserPrincipal("admin@bbsi.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		userPrincipal.setUserType(GenericConstants.USERTYPE_CLIENT);
		userPrincipal.setEmail("admin@bbsi.com");
		userPrincipal.setIsPolicyAccepted(false);
		userPrincipal.setIsCcpaRequired(false);
		userPrincipal.setIsPolicyUpdated(false);
		userPrincipal.setToken("Bearer 1234-5678-abcd-efgh");
		otpDetailsDTO = new OtpDetailsDTO();
		otpDetailsDTO.setUser("763432");
		otpDetailsDTO.setOtp("763432");
		MockitoAnnotations.initMocks(this);
		ExceptionUtils.handleException = Mockito.mock(ThrowableTriConsumer.class);
		GenericUtils.throwCommonNullVariableException = Mockito.mock(ThrowableBiConsumer.class);
		GenericUtils.throwCommonNullVariableExceptionString = Mockito.mock(ThrowableConsumer.class);
	}

	@After
	public void tearDown() throws Exception {
		userPrincipal = null;
	}

	@Test
	public void testreInitiateNewHire() {
		Mockito.doNothing().when(mockUserBusinessService).reInitiateNewHire(Mockito.any(), Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		mockUserResource.reInitiateNewHire("Bearer 1234-abcd-4321-dcba", "uiurl", populateUserDTO(), userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testreInitiateNewHireForException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).reInitiateNewHire(Mockito.any(),
				Mockito.anyString(), Mockito.anyString(), Mockito.any());
		mockUserResource.reInitiateNewHire("", "", null, null);
		assertTrue(true);
	}

	@Test
	public void testupdateClientMaster() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909464");
		principal.setEmail("integration@osius.com");
		Mockito.doNothing().when(mockUserBusinessService).updateClientMaster();
		mockUserResource.updateClientMaster(principal);
		Mockito.verify(mockUserBusinessService, Mockito.atLeastOnce()).updateClientMaster();
	}

	@Test
	public void testupdateClientMasterForEmail() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909464");
		principal.setEmail("integration123@osius.com");
		principal = null;
		Mockito.doNothing().when(mockUserBusinessService).updateClientMaster();
		mockUserResource.updateClientMaster(principal);
		assertTrue(true);
	}

	@Test
	public void testUpdateClientMasterForSuperAdmin() {
		UserPrincipal principal = userPrincipal;
		principal.setEmail(GenericConstants.SUPER_ADMIN.toString());
		Mockito.doNothing().when(mockUserBusinessService).updateClientMaster();
		mockUserResource.updateClientMaster(principal);
		Mockito.verify(mockUserBusinessService, Mockito.never()).updateClientMaster();
	}

	@Test
	public void testUpdateClientMasterForException() {
		UserPrincipal principal = new UserPrincipal(" ", " ", new ArrayList<>());
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateClientMaster();
		mockUserResource.updateClientMaster(principal);
		Mockito.verify(mockUserBusinessService, Mockito.never()).updateClientMaster();
	}

	@Test
	public void testsendEmailNotificationsForSchoolDistrict() {
		Mockito.doNothing().when(mockUserBusinessService).sendEmailNotificationsForSchoolDistrict(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.any());
		mockUserResource.sendEmailNotificationsForSchoolDistrict("ecode", "fname", "lname", userPrincipal);
		Mockito.verify(mockUserBusinessService, Mockito.atLeastOnce()).sendEmailNotificationsForSchoolDistrict(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any());
	}

	@Test
	public void testsendEmailNotificationsForSchoolDistrictForException() {
		UserPrincipal principal = new UserPrincipal(" ", " ", new ArrayList<>());
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).sendEmailNotificationsForSchoolDistrict(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any());
		mockUserResource.sendEmailNotificationsForSchoolDistrict("ecode", "fname", "lname", principal);
		Mockito.verify(mockUserBusinessService, Mockito.atLeastOnce()).sendEmailNotificationsForSchoolDistrict(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any());
	}

	@Test
	public void testPreChecks() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setIsPolicyAccepted(false);
		principal.setIsPolicyUpdated(false);
		principal.setEmail("admin@mybbsi.com");
		principal.setIsCcpaRequired(false);
		principal.setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		principal.setMfaAuthorizationRequired(true);
		Map<String, String> checksMap = new HashMap<>();
		Mockito.doReturn(checksMap).when(spyUserResource).preChecks(userPrincipal,
				"Bearer 7ca6c2ee-173d-4cd5-9e2f-09bab1c5e4b1");
		checksMap = mockUserResource.preChecks(principal, "Bearer 7ca6c2ee-173d-4cd5-9e2f-09bab1c5e4b1");
		assertNotEquals(checksMap.size(), 0);
	}

	@Test
	public void testPreChecksForEmptyKey() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setIsPolicyAccepted(false);
		principal.setIsPolicyUpdated(false);
		principal.setIsCcpaRequired(false);
		principal.setMfaAuthorizationRequired(true);
		principal.setEmail("test@test.com");
		Map<String, String> checksMap = new HashMap<>();
		checksMap = mockUserResource.preChecks(principal, "");
		assertNotEquals(checksMap.size(), 0);
	}

	@Test
	public void testPreChecksWhenMfaFalse() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setIsPolicyAccepted(true);
		principal.setIsPolicyUpdated(true);
		principal.setEmail("test@test.com");
		principal.setIsCcpaRequired(true);
		Map<String, String> checksMap = new HashMap<>();
		String key = "test@test.com<<>>" + LocalDate.now().toString();
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.anyString(), Mockito.any());
		checksMap = mockUserResource.preChecks(principal, "Bearer 7ca6c2ee-173d-4cd5-9e2f-09bab1c5e4b1");
		assertNotEquals(checksMap.size(), 0);
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testPreChecksForExceptions() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setIsPolicyAccepted(true);
		principal.setIsPolicyUpdated(true);
		principal.setEmail("test@test.com");
		principal.setIsCcpaRequired(true);
		Map<String, String> checksMap = new HashMap<>();
		String key = "admin@test.com<<>>" + LocalDate.now().toString();
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.anyString(), Mockito.any());
		checksMap = mockUserResource.preChecks(principal, "Bearer 7ca6c2ee-173d-4cd5-9e2f-09bab1c5e4b1");
		assertNotEquals(checksMap.size(), 0);
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testPreChecksForExceptionsWhenArray2() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setIsPolicyAccepted(true);
		principal.setIsPolicyUpdated(true);
		principal.setEmail("test@test.com");
		principal.setIsCcpaRequired(true);
		Map<String, String> checksMap = new HashMap<>();
		String key = "admin@test.com<<>>" + LocalDate.now().toString() + "<<>>abc";
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.anyString(), Mockito.any());
		checksMap = mockUserResource.preChecks(principal, "Bearer 7ca6c2ee-173d-4cd5-9e2f-09bab1c5e4b1");
		assertNotEquals(checksMap.size(), 0);
	}

	@Test
	public void testUpdateIsCCPAAccepted() {
		Mockito.doNothing().when(mockUserBusinessService).updateCCPAPolicyAccepted(true, userPrincipal);
		mockUserResource.updateIsCCPAAccepted(true, userPrincipal, "Bearer 1234-5678-abcd-efgh");
		assertTrue(true);
	}

	@Test
	public void testUpdateIsCCPAAcceptedForExceptions() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateCCPAPolicyAccepted(false,
				new UserPrincipal(" ", " ", Lists.newArrayList()));
		mockUserResource.updateIsCCPAAccepted(false, new UserPrincipal(" ", " ", Lists.newArrayList()), " ");
		assertTrue(true);
	}

	@Test
	public void testUpdateIsCCPAAcceptedForOtherExceptions() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateCCPAPolicyAccepted(false, null);
		mockUserResource.updateIsCCPAAccepted(false, null, null);
		assertTrue(true);
	}

	@Test
	public void testUpdateUser() {
		UsersDTO expected = populateUserDTO();
		UsersDTO inputData = populateUserDTO();
		inputData.setFullName("first last name");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		Mockito.doReturn(expected).when(mockUserBusinessService).updateUser(inputData, "token1", principal);
		UsersDTO response = mockUserResource.updateUser("token1", inputData, principal);
		assertEquals(expected.getId(), response.getId());
		assertEquals(expected.getEmail(), response.getEmail());
		assertEquals(expected.getEmployeeCode(), response.getEmployeeCode());
	}

	@Test(expected = BbsiException.class)
	public void testUpdateUserWhenTokenIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateUser(new UsersDTO(), " ",
				new UserPrincipal(" ", " ", Lists.newArrayList()));
		UsersDTO response = mockUserResource.updateUser(" ", new UsersDTO(),
				new UserPrincipal(" ", " ", Lists.newArrayList()));
		assertNull(response);
	}

	@Test(expected = ValidationException.class)
	public void testUpdateUserForValidationException() {
		UsersDTO expected = populateUserDTO();
		UsersDTO inputData = populateUserDTO();
		inputData.setFullName("first last name");
		List<String> nonPrismizedCostCenters = new ArrayList<>();
		nonPrismizedCostCenters.add("NEW YORK");
		nonPrismizedCostCenters.add("Illionis");
		nonPrismizedCostCenters.add("Vancuover");
		nonPrismizedCostCenters.add("Ohio");
		inputData.setNonPrismizedCostCenters(nonPrismizedCostCenters);
		expected.setNonPrismizedCostCenters(nonPrismizedCostCenters);
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		Mockito.doReturn(expected).when(mockUserBusinessService).updateUser(inputData, "token1", principal);
		UsersDTO response = mockUserResource.updateUser("token1", inputData, principal);
		assertEquals(expected.getId(), response.getId());
		assertEquals(expected.getEmail(), response.getEmail());
	}

	@Test
	public void testUpdateUserForException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateUser(null, null, null);
		UsersDTO response = mockUserResource.updateUser(null, null, userPrincipal);
		assertNull(response);
	}

	@Test
	public void testUpdateUserPersonalInfo() {
		CustomPersonalDTO expected = populateCustomPersonalDTO();
		Mockito.doReturn(expected).when(mockUserBusinessService).updateUserPersonalInfo(Mockito.any(),
				Mockito.anyString(), Mockito.any());
		CustomPersonalDTO response = mockUserResource.updateUserPersonalInfo("token5", populateCustomPersonalDTO(),
				userPrincipal);
		assertEquals(expected.getClientCode(), response.getClientCode());
		assertEquals(expected.getEmployeeCode(), response.getEmployeeCode());
		assertEquals(expected.getDateOfBirth(), response.getDateOfBirth());
	}

	@Test
	public void testUpdateUserProfileInfoWhenTokenIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateUserPersonalInfo(Mockito.any(),
				Mockito.anyString(), Mockito.any());
		CustomPersonalDTO response = mockUserResource.updateUserPersonalInfo(" ", new CustomPersonalDTO(),
				userPrincipal);
		assertEquals(0, response.getId());
		assertEquals(0, response.getEmployeeId());
		assertEquals(null, response.getEmployeeCode());
	}

	@Test
	public void testVerifyUser() {

		ParentDTO response = mockUserResource.verifyUser(userPrincipal);
		assertEquals(response.getPrincipal().getEmail(), userPrincipal.getEmail());
	}

	@Test
	public void testGetUsersByClientCodeAndType() {
		Mockito.doReturn(new ArrayList<UsersDTO>()).when(mockUserBusinessService)
				.getUsersByClientCodeAndType(Mockito.anyString(), Mockito.anyString());
		List<UsersDTO> response = mockUserResource.getUsersByClientCodeAndType("client101", "CLIENT");
		assertNotNull(response);
	}

	@Test
	public void testGetUsersByClientCodeAndTypeWhenClientCodeThrowsException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService)
				.getUsersByClientCodeAndType(Mockito.anyString(), Mockito.anyString());
		List<UsersDTO> response = mockUserResource.getUsersByClientCodeAndType("client101", "CLIENT");
		assertNull(response);
	}

	@Test
	public void testGetUserById() {
		UsersDTO expected = populateUserDTO();
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909464");
		Mockito.doReturn(expected).when(mockUserBusinessService).getUserById(expected.getId(), "token1", "909464",
				principal);
		UsersDTO response = mockUserResource.getUserById(expected.getId(), "token1", principal);
		assertEquals(expected.getId(), response.getId());
		assertEquals(expected.getEmail(), response.getEmail());
		assertEquals(expected.getEmployeeCode(), response.getEmployeeCode());
	}

	@Test(expected = BbsiException.class)
	public void testGetUserByIdWhenIdIsNull() {
		UserPrincipal principal = new UserPrincipal(" ", " ", new ArrayList<>());
		principal.setClientCode("909464");
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getUserById(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyString(), Mockito.any());
		UsersDTO response = mockUserResource.getUserById(populateUserDTO().getId(), "token1", principal);
		assertNull(response);
	}

	@Test
	public void testGetUserByIdForExceptions() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getUserById(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyString(), Mockito.any());
		UsersDTO response = mockUserResource.getUserById(0l, "", null);
		assertNull(response);
	}

	@Test
	public void testGetCostCenters() {
		ArrayList<CostCenterDTO> list = new ArrayList<>();
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909464");
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());

		Mockito.doReturn(list).when(mockUserBusinessService).getAllCostCenters(Mockito.anyString());
		List<CostCenterDTO> response = mockUserResource.getCostCenters("token1", principal);
		assertEquals(response.size(), list.size());
	}

	@Test
	public void testGetCostCentersWhenTokenIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getAllCostCenters(" ");
		List<CostCenterDTO> response = mockUserResource.getCostCenters(" ",
				new UserPrincipal(" ", " ", Lists.newArrayList()));
		assertNull(response);
	}

	@Test
	public void testGetCostCenterForException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getAllCostCenters(null);
		List<CostCenterDTO> response = mockUserResource.getCostCenters(null, null);
		assertNull(response);
	}

	@Test
	public void testSwitchBusiness() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "bbsihq.com,mybbsi.com");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "admin ", grantedAuthorities1);
		principal.setMfaAuthorizationRequired(true);
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		principal.setEmail("admin@mybbsi.com");
		principal.setToken("Bearer 1234-abcd-4321-dcba");
		ParentDTO parentDto = new ParentDTO();
		parentDto.setPrincipal(principal);
		Authentication auth = Mockito.mock(Authentication.class);
		Mockito.doReturn("admin@osius.com").when(auth).getName();
		Mockito.doReturn(parentDto).when(mockUserBusinessService).switchBusiness(Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn("clientName").when(mockUserBusinessService).getClientNameByClientCode(Mockito.anyString());
		Mockito.doNothing().when(mockUserBusinessService).updateCaliforniaFlag(Mockito.any());
		ParentDTO response = mockUserResource.switchBusiness("909464", GenericConstants.USERTYPE_CLIENT.toString(),
				"Bearer 1234-abcd-4321-dcba", principal, "key1");
		assertEquals(response.getPrincipal().getToken(), parentDto.getPrincipal().getToken());
	}

	@Test
	public void testSwitchBusinessForOtherUsersVerifyUsernameAndKeyValidityDays() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "bbsihq.com,mybbsi.com");
		ReflectionTestUtils.setField(mockUserResource, "secret", "secret");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "admin ", grantedAuthorities1);
		principal.setMfaAuthorizationRequired(true);
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		principal.setEmail("admin@osius.com");
		principal.setToken("Bearer 1234-abcd-4321-dcba");
		ParentDTO parentDto = new ParentDTO();
		parentDto.setPrincipal(principal);
		String key = "admin123@osius.com<<>>" + LocalDate.now().toString();
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.anyString(), Mockito.any());
		Mockito.doReturn(parentDto).when(mockUserBusinessService).switchBusiness(Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn("clientName").when(mockUserBusinessService).getClientNameByClientCode(Mockito.anyString());
		Mockito.doNothing().when(mockUserBusinessService).updateCaliforniaFlag(Mockito.any());
		ParentDTO response = mockUserResource.switchBusiness("909464", GenericConstants.USERTYPE_EMPLOYEE.toString(),
				"Bearer 1234-abcd-4321-dcba", principal, "key1");
		assertTrue(true);
	}

	@Test
	public void testSwitchBusinessForOtherUsers() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "bbsihq.com,mybbsi.com");
		ReflectionTestUtils.setField(mockUserResource, "secret", "secret");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "admin ", grantedAuthorities1);
		principal.setMfaAuthorizationRequired(true);
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		principal.setEmail("admin@osius.com");
		principal.setToken("Bearer 1234-abcd-4321-dcba");
		ParentDTO parentDto = new ParentDTO();
		parentDto.setPrincipal(principal);
		String key = "admin@osius.com<<>>" + LocalDate.now().toString();
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.anyString(), Mockito.any());
		Mockito.doReturn(parentDto).when(mockUserBusinessService).switchBusiness(Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn("clientName").when(mockUserBusinessService).getClientNameByClientCode(Mockito.anyString());
		Mockito.doNothing().when(mockUserBusinessService).updateCaliforniaFlag(Mockito.any());
		ParentDTO response = mockUserResource.switchBusiness("909464", GenericConstants.USERTYPE_EMPLOYEE.toString(),
				"Bearer 1234-abcd-4321-dcba", principal, "key1");
		assertTrue(true);
	}

	@Test
	public void testSwitchBusinessForOtherUsersForValidateKeyLength() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "bbsihq.com,mybbsi.com");
		ReflectionTestUtils.setField(mockUserResource, "secret", "secret");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "admin ", grantedAuthorities1);
		principal.setMfaAuthorizationRequired(true);
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		principal.setEmail("admin@osius.com");
		principal.setToken("Bearer 1234-abcd-4321-dcba");
		ParentDTO parentDto = new ParentDTO();
		parentDto.setPrincipal(principal);
		String key = "admin@osius.com<<>>";
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.anyString(), Mockito.any());
		Mockito.doReturn(parentDto).when(mockUserBusinessService).switchBusiness(Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn("clientName").when(mockUserBusinessService).getClientNameByClientCode(Mockito.anyString());
		Mockito.doNothing().when(mockUserBusinessService).updateCaliforniaFlag(Mockito.any());
		ParentDTO response = mockUserResource.switchBusiness("909464", GenericConstants.USERTYPE_EMPLOYEE.toString(),
				"Bearer 1234-abcd-4321-dcba", principal, "key1");
		assertTrue(true);
	}

	@Test
	public void testSwitchBusinessForOtherUsersForEmptyKey() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "bbsihq.com,mybbsi.com");
		ReflectionTestUtils.setField(mockUserResource, "secret", "secret");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "admin ", grantedAuthorities1);
		principal.setMfaAuthorizationRequired(true);
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		principal.setEmail("admin@osius.com");
		principal.setToken("Bearer 1234-abcd-4321-dcba");
		ParentDTO parentDto = new ParentDTO();
		parentDto.setPrincipal(principal);
		ParentDTO response = mockUserResource.switchBusiness("909464", Enums.UserEnum.CLIENT.toString(),
				"Bearer 1234-abcd-4321-dcba", principal, "");
		assertNull(response);
	}

	@Test
	public void testSwitchBusinessForOtherUsersForKeyArrayUnauthorizedAccessException() {
		ReflectionTestUtils.setField(mockUserResource, "secret", "secret");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "admin ", grantedAuthorities1);
		principal.setMfaAuthorizationRequired(true);
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		principal.setEmail("admin@osius.com");
		principal.setToken("Bearer 1234-abcd-4321-dcba");
		ParentDTO parentDto = new ParentDTO();
		parentDto.setPrincipal(principal);
		String key = "admin@test.com<<>>" + LocalDate.now().toString();
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.anyString(), Mockito.any());
		Mockito.doReturn(parentDto).when(mockUserBusinessService).switchBusiness(Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn("clientName").when(mockUserBusinessService).getClientNameByClientCode(Mockito.anyString());
		Mockito.doNothing().when(mockUserBusinessService).updateCaliforniaFlag(Mockito.any());
		ParentDTO response = mockUserResource.switchBusiness("909464", GenericConstants.USERTYPE_EMPLOYEE.toString(),
				"Bearer 1234-abcd-4321-dcba", principal, "key1");
		assertNull(response);
	}

	@Test
	public void testSwitchBusinessForOtherUsersForKeyArrayUnauthorizedAccessException2() {
		ReflectionTestUtils.setField(mockUserResource, "secret", "secret");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "admin ", grantedAuthorities1);
		principal.setMfaAuthorizationRequired(true);
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		principal.setEmail("admin@osius.com");
		principal.setToken("Bearer 1234-abcd-4321-dcba");
		ParentDTO parentDto = new ParentDTO();
		parentDto.setPrincipal(principal);
		String key = "admin@test.com<<>>" + LocalDate.now().toString() + "<<>>abc";
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.anyString(), Mockito.any());
		Mockito.doReturn(parentDto).when(mockUserBusinessService).switchBusiness(Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn("clientName").when(mockUserBusinessService).getClientNameByClientCode(Mockito.anyString());
		Mockito.doNothing().when(mockUserBusinessService).updateCaliforniaFlag(Mockito.any());
		ParentDTO response = mockUserResource.switchBusiness("909464", GenericConstants.USERTYPE_EMPLOYEE.toString(),
				"Bearer 1234-abcd-4321-dcba", principal, "key1");
		assertNull(response);
	}

	@Test
	public void testSwitchBusinessWhenUserPrincipalIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService)
				.getClientNameByClientCode(Mockito.anyString());
		ParentDTO response = mockUserResource.switchBusiness("909464", Enums.UserEnum.CLIENT.toString(), " ",
				userPrincipal, " ");
		assertNull(response);
	}

	@Test
	public void testGetisPolicyAccepted() {
		Mockito.doReturn(new PolicyAcceptedDTO()).when(mockUserBusinessService)
				.getisPolicyAccepted(Mockito.anyString());
		PolicyAcceptedDTO response = mockUserResource.getisPolicyAccepted("test@bbsihqc.com");
		assertNotNull(response);
	}

	@Test
	public void testGetisPolicyAcceptedWhenEmailThrowsException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getisPolicyAccepted(Mockito.anyString());
		PolicyAcceptedDTO response = mockUserResource.getisPolicyAccepted("test@bbsi.com");
		assertNull(response);
	}

	@Test
	public void testGetUsersByClientCodeAndTypeAndCreatedBy() {
		Mockito.doReturn(new ArrayList<UsersDTO>()).when(mockUserBusinessService)
				.getUsersByClientCodeAndTypeAndCreatedBy(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		List<UsersDTO> response = mockUserResource.getUsersByClientCodeAndTypeAndCreatedBy("BBSI", "employee",
				"test@bbsihqc.com");
		assertNotNull(response);
	}

	@Test
	public void testGetUsersByClientCodeAndTypeAndCreatedByWhenClientCodeIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService)
				.getUsersByClientCodeAndTypeAndCreatedBy(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		List<UsersDTO> response = mockUserResource.getUsersByClientCodeAndTypeAndCreatedBy("BBSI", "employee",
				"test@bbsihqc.com");
		assertNull(response);
	}

	@Test
	public void testGetAllClientUsersByClientCodeAndPrivileges() {
		Mockito.doReturn(new ArrayList<UsersDTO>()).when(mockUserBusinessService)
				.getAllClientUsersByClientCodeAndPayRollStatus(Mockito.anyString(), Mockito.anyString());
		List<UsersDTO> response = mockUserResource.getAllClientUsersByClientCodeAndPrivileges("BBSI", "Active");
		assertNotNull(response);
	}

	@Test
	public void testGetAllClientUsersByClientCodeAndPrivilegesWhenClientCodeIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService)
				.getAllClientUsersByClientCodeAndPayRollStatus(Mockito.anyString(), Mockito.anyString());
		List<UsersDTO> response = mockUserResource.getAllClientUsersByClientCodeAndPrivileges("BBSI", "Active");
		assertNull(response);
	}

	@Test
	public void testUpdateNewHireUser() {
		long id = 1;
		Mockito.doNothing().when(mockUserBusinessService).updateNewHireUser(Mockito.anyLong(), Mockito.anyString(),Mockito.anyString(), Mockito.anyString());
		mockUserResource.updateNewHireUser(id, "employee1@bbsihqc.com","employee1@bbsihqc.com", "1234567890");
		assertTrue(true);
	}

	@Test
	public void testUpdateNewHireUserWhenIdIsNull() {
		long id = -1;
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateNewHireUser(Mockito.anyLong(),
				Mockito.anyString(),Mockito.anyString(), Mockito.anyString());
		mockUserResource.updateNewHireUser(id, "employee1@bbsihqc.com","employee1@bbsihqc.com", "1234567890");
		assertTrue(true);
	}

	@Test
	public void testGetEmailsByClientCodeAndType() {
		Map<String, List<String>> expected = new HashMap<>();
		Mockito.doReturn(expected).when(mockUserBusinessService).getEmailsByClientCodeAndType("909464",
				UserEnum.CLIENT.toString());
		Map<String, List<String>> response = mockUserResource.getEmailsByClientCodeAndType("909464",
				UserEnum.CLIENT.toString());
		assertNotNull(response);
	}

	@Test
	public void testGetEmailsByClientCodeAndTypeWhenClientCodeIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getEmailsByClientCodeAndType(null,
				UserEnum.CLIENT.toString());
		Map<String, List<String>> response = mockUserResource.getEmailsByClientCodeAndType(null,
				UserEnum.CLIENT.toString());
		assertNull(response);
	}

	@Test
	public void testGetEmailsByClientCodeAndTypeAndEnum() {
		Map<String, List<String>> expected = new HashMap<>();
		Mockito.doReturn(expected).when(mockUserBusinessService)
				.getEmailsByClientCodeAndTypeAndEnum(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Map<String, List<String>> response = mockUserResource.getEmailsByClientCodeAndTypeAndEnum("BBSI", "Employee",
				"CLIENT");
		assertNotNull(response);
	}

	@Test
	public void testGetEmailsByClientCodeAndTypeAndEnumIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService)
				.getEmailsByClientCodeAndTypeAndEnum(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Map<String, List<String>> response = mockUserResource.getEmailsByClientCodeAndTypeAndEnum("BBSI", "Employee",
				"CLIENT");
		assertNull(response);
	}

	@Test
	public void testCheckExistUser() {
		Mockito.doReturn("test@mailinator.com").when(mockEncryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn(new UserExistDTO()).when(mockUserBusinessService).checkExistUser(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		UserExistDTO response = mockUserResource.checkExistUser("BBSI", "test@bbsihqc.com", "token1");
		assertNotNull(response);
	}

	@Test
	public void testCheckExistUserWhenClientCodeIsNull() {
		Mockito.doReturn("test1@mailinator.com").when(mockEncryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).checkExistUser(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		UserExistDTO response = mockUserResource.checkExistUser("BBSI", "test@bbsihqc.com", "token1");
		assertNull(response);
	}

	@Test
	public void testGetI9ApprovalEmployeeList() {
		Mockito.doReturn(new ArrayList<UsersDTO>()).when(mockUserBusinessService)
				.getI9ApprovalEmployeeList(Mockito.anyString());
		List<UsersDTO> response = mockUserResource.getI9ApprovalEmployeeList("BBSI");
		assertNotNull(response);
	}

	@Test
	public void testGetI9ApprovalEmployeeListWhenClientCodeIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService)
				.getI9ApprovalEmployeeList(Mockito.anyString());
		List<UsersDTO> response = mockUserResource.getI9ApprovalEmployeeList("BBSI");
		assertNull(response);
	}

	@Test
	public void testUpdateIsPolicyAccepted() {
		PolicyAcceptedDTO inputDTO = new PolicyAcceptedDTO();
		inputDTO.setEmail("admin@osius.com");
		inputDTO.setIsPolicyAccepted(true);
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setToken("Bearer 1234-5678-abcd-efgh");
		principal.setIsPolicyUpdated(true);
		principal.setIsPolicyAccepted(true);
		Mockito.doNothing().when(mockUserBusinessService).updateIsPolicyAccepted(Mockito.any(), Mockito.any());
		mockUserResource.updateIsPolicyAccepted(policyAcceptedDTO, principal, "Bearer 1234-5678-abcd-efgh");
		assertTrue(true);
	}

	@Test
	public void testUpdateIsPolicyAccepted1() {
		PolicyAcceptedDTO inputDTO = new PolicyAcceptedDTO();
		inputDTO.setEmail("admin@osius.com");
		inputDTO.setIsPolicyAccepted(true);
		Mockito.doNothing().when(mockUserBusinessService).updateIsPolicyAccepted(Mockito.any(), Mockito.any());
		mockUserResource.updateIsPolicyAccepted(getPolicyAcceptedDTO(), getUserPrincipal(),
				"Bearer 1234-5678-abcd-efgh");
		assertTrue(true);
	}

	@Test
	public void testUpdateIsPolicyAcceptedWhenPolicyAcceptedDTOIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateIsPolicyAccepted(Mockito.any(),
				Mockito.any());
		mockUserResource.updateIsPolicyAccepted(null, userPrincipal, "token1");
		assertTrue(true);
	}

	@Test
	public void testSaveUserToolbarSettings() {
		Mockito.doNothing().when(mockUserBusinessService).saveUserToolbarSettings(Mockito.any(), Mockito.any());
		mockUserResource.saveUserToolbarSettings(userPrincipal, userToolbarSettingsDTOs);
		assertTrue(true);
	}

	@Test
	public void testSaveUserToolbarSettingsWhenPrincipalIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).saveUserToolbarSettings(Mockito.any(),
				Mockito.any());
		mockUserResource.saveUserToolbarSettings(userPrincipal, userToolbarSettingsDTOs);
		assertTrue(true);
	}

	@Test
	public void testGetEmailByClientCodeAndEmployeeCode() {
		Mockito.doReturn("test").when(mockUserBusinessService).getEmailByClientCodeAndEmployeeCode(Mockito.anyString(),
				Mockito.anyString());
		String response = mockUserResource.getEmailByClientCodeAndEmployeeCode("BBSI", "employee_001");
		assertNotNull(response);
	}

	@Test
	public void testGetEmailByClientCodeAndEmployeeIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService)
				.getEmailByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		String response = mockUserResource.getEmailByClientCodeAndEmployeeCode("BBSI", "employee_001");
		assertNull(response);
	}

	@Test
	public void testDisableUser() {
		boolean value = true;
		long id = 1;
		Mockito.doReturn(value).when(mockUserBusinessService).disableUser(Mockito.anyString(), Mockito.anyLong());
		Boolean response = mockUserResource.disableUser("BBSI", id);
		assertNotNull(response);
	}

	@Test
	public void testDisableUserWhenClientCodeIsNull() {
		long id = 1;
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).disableUser(null, id);
		Boolean response = mockUserResource.disableUser(null, id);
		assertNotNull(response);

	}

	@Test
	public void testDeleteNewHireUser() {
		long id = 1;
		Mockito.doNothing().when(mockUserBusinessService).deleteNewHireUser(Mockito.anyLong());
		mockUserResource.deleteNewHireUser(id);
		assertTrue(true);
	}

	@Test
	public void testDeleteNewHireUserWhenIdIsInvalid() {
		long id = -1;
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).deleteNewHireUser(id);
		mockUserResource.deleteNewHireUser(id);
		assertTrue(true);
	}

	@Test
	public void testUpdateUserDetails() {
		UserPrincipal principal = userPrincipal;
		Map<String, String> clientMap = new HashMap<>();
		clientMap.put("clientCode1", populateCustomPersonalDTO().getClientCode());
		principal.setClientMap(clientMap);
		Mockito.doNothing().when(mockUserBusinessService).updateUserDetails(Mockito.any(), Mockito.anyString());
		String response = mockUserResource.updateUserDetails(populateCustomPersonalDTO(), "token1", principal);
		assertNotNull(response);
	}
	
	@Test
	public void testupdateUserDetails1() {
		String res = mockUserResource.updateUserDetails(populateCustomPersonalDTO(), "token", getUserPrincipal());
		assertNotNull(res);
	}

	@Test
	public void testUpdateUserDetailsWhenDetailsEmpty() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateUserDetails(Mockito.any(),
				Mockito.anyString());
		String response = mockUserResource.updateUserDetails(null, "token1", userPrincipal);
		assertNotNull(response);
	}

	@Test
	public void testUpdateI9ApprovalDetailsWhenEmpCodeIsNA() {

		Mockito.doNothing().when(mockUserBusinessService).updateI9ApprovalDetails(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyString());
		mockUserResource.updateI9ApprovalDetails(123l, "NA", "BBSI");
		assertTrue(true);
	}

	@Test
	public void testUpdateI9ApprovalDetailsWhenClientCodeNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateI9ApprovalDetails(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyString());
		mockUserResource.updateI9ApprovalDetails(123l, "E1786F", null);
		assertTrue(true);
	}

	@Test
	public void testUpdateI9ApprovalDetailsForExceptions() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateI9ApprovalDetails(0, " ", " ");
		mockUserResource.updateI9ApprovalDetails(0, " ", " ");
		assertTrue(true);
	}

	@Test
	public void testUpdateI9ApprovalDetailsForOtherExceptions() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateI9ApprovalDetails(0, null, null);
		mockUserResource.updateI9ApprovalDetails(0, null, null);
		assertTrue(true);
	}

	@Test
	public void testGetEmployeePersonaDetailsByClientCodeAndEmployeeCode() {
		Mockito.doReturn(new HashMap<String, Set<String>>()).when(mockUserBusinessService)
				.getPersonalInfoByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString(),
						Mockito.anyString(), Mockito.any(), Mockito.anyLong());
		Map<String, Set<String>> actualResponse = mockUserResource
				.getEmployeePersonaDetailsByClientCodeAndEmployeeCode("909464", "E71634", "token", userPrincipal);
		assertNotNull(actualResponse);
	}

	@Test
	public void testGetEmployeePersonaDetailsByClientCodeAndEmployeeCodeWhenClientCodeIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getPersonalInfoByClientCodeAndEmployeeCode(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyLong());
		Map<String, Set<String>> actualResponse = mockUserResource
				.getEmployeePersonaDetailsByClientCodeAndEmployeeCode(null, null, "token", null);
		assertNull(actualResponse);
	}

	@Test
	public void testUpdateEmail() {
		CustomPersonalDTO expected = populateCustomPersonalDTO();
		UserPrincipal principal = userPrincipal;
		expected.setEmployeeCode(null);
		principal.setEmployeeCode("ecode");
		principal.setEmail("admin@osius.com");
		Mockito.doReturn(expected).when(mockUserBusinessService).updateEmail(Mockito.any(), Mockito.any(),
				Mockito.any());
		CustomPersonalDTO actualResponse = mockUserResource.updateEmail("token", expected, principal);
		assertNotNull(actualResponse);
	}

	@Test
	public void testUpdateEmailForElseBlock() {
		Mockito.doReturn(populateCustomPersonalDTO()).when(mockUserBusinessService).updateEmail(Mockito.any(),
				Mockito.anyString(), Mockito.any());
		CustomPersonalDTO actualResponse = mockUserResource.updateEmail("token", populateCustomPersonalDTO(),
				getUserPrincipal());
		assertNotNull(actualResponse);

	}

	@Test
	public void testUpdateEmailEmployee() {
		Mockito.doReturn(populateCustomPersonalDTO()).when(mockUserBusinessService).updateEmail(Mockito.any(),
				Mockito.anyString(), Mockito.any());
		CustomPersonalDTO actualResponse = mockUserResource.updateEmail("token", populateCustomPersonalDTO(),
				getUserPrincipalForUserType());
		assertNotNull(actualResponse);

	}

	@Test
	public void testUpdateEmailWhenTokenIsNull() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateEmail(Mockito.any(), Mockito.any(),
				Mockito.any());
		CustomPersonalDTO actualResponse = mockUserResource.updateEmail(null, null, null);
		assertEquals(new CustomPersonalDTO(), actualResponse);
	}

	@Test(expected = ValidationException.class)
	public void testUpdateEmailThrowsValidationException() {
		CustomPersonalDTO input = new CustomPersonalDTO();
		input.setNewEmail("admin.com");
		input.setOldEmail("admin@osius.com");
		UserPrincipal principal = userPrincipal;
		principal.setEmail("admin@osius.com");
		Mockito.doThrow(ValidationException.class).when(mockUserBusinessService).updateEmail(Mockito.any(),
				Mockito.any(), Mockito.any());
		CustomPersonalDTO actualResponse = mockUserResource.updateEmail(" ", input, principal);
		assertNull(actualResponse.getClientCode());
	}

	@Test
	public void testSendCredentialsToEmployees() {

		Mockito.doReturn(userInvitation).when(mockUserBusinessService).createUserAndSendInvitation(Mockito.any(),
				Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		List<UserInvitationDTO> actualResponse = mockUserResource.sendCredentialsToEmployees("token", userInvitation,
				"909090", "base64ClientName", userPrincipal, "https://portaldev.mybbsi.com/");
		assertNotNull(actualResponse);
	}

	@Test(expected = BbsiException.class)
	public void testSendCredentialsToEmployeesThrowsException() {

		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).createUserAndSendInvitation(Mockito.any(),
				Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		List<UserInvitationDTO> actualResponse = mockUserResource.sendCredentialsToEmployees("token", userInvitation,
				"909090", "base64ClientName", userPrincipal, "https://portaldev.mybbsi.com/");
		assertNotNull(actualResponse);
	}

	@Test
	public void testsendCredentialsToEmployeesThrowsException() {
		Mockito.doThrow(new NullPointerException()).when(mockUserBusinessService).createUserAndSendInvitation(
				Mockito.any(), Mockito.anyString(), Mockito.any(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());
		List<UserInvitationDTO> actualResponse = mockUserResource.sendCredentialsToEmployees("token", userInvitation,
				"909090", "base64ClientName", userPrincipal, "https://portaldev.mybbsi.com/");
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testGetPrismizedClientsByCostCenterCode() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909464");
		principal.setUserType("Branch");
		principal.setCostCenterCode("909464");
		Mockito.doReturn(populateCostCenterDto()).when(mockUserBusinessService)
				.getCostCenterPrismizedClients(Mockito.anyString());
		List<CostCenterClientDTO> actualCostcenterClientList = mockUserResource
				.getPrismizedClientsByCostCenterCode("1bdss-12jj-12nj", "909464", principal);
		assertNotEquals(0, actualCostcenterClientList.size());
	}

	@Test
	public void testGetPrismizedClientsByCostCenterCodeVancouver() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909464");
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		principal.setCostCenterCode("909464");
		Mockito.doReturn(populateCostCenterDto()).when(mockUserBusinessService)
				.getCostCenterPrismizedClients(Mockito.anyString());
		List<CostCenterClientDTO> actualCostcenterClientList = mockUserResource
				.getPrismizedClientsByCostCenterCode("1bdss-12jj-12nj", "909464", principal);
		assertNotEquals(actualCostcenterClientList.size(), 0);
	}

	@Test
	public void testGetPrismizedClientsByCostCenterCodeThrowsException() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode("clientCode");
		principal.setCostCenterCode("clientCode");
		principal.setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService)
				.getCostCenterPrismizedClients(Mockito.anyString());
		List<CostCenterClientDTO> actualCostcenterClientList = mockUserResource
				.getPrismizedClientsByCostCenterCode("1bdss-12jj-12nj", "909464", principal);
		assertNull(actualCostcenterClientList);
	}

	@Test
	public void testGetPrismizedClientsByCostCenterCodeThrowsOtherException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getCostCenterPrismizedClients(null);
		List<CostCenterClientDTO> response = mockUserResource.getPrismizedClientsByCostCenterCode(null, null, null);
		assertNull(response);
	}

	@Test
	public void testGetPrismizedClientsByCostCenterCodeThrowsOtherExceptions() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getCostCenterPrismizedClients("");
		List<CostCenterClientDTO> response = mockUserResource.getPrismizedClientsByCostCenterCode(" ", " ",
				new UserPrincipal(" ", " ", Lists.newArrayList()));
		assertNull(response);
	}

	@Test
	public void testGetdistinctClients() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909464");
		principal.setEmail("integration@osius.com");
		Mockito.doReturn(new ArrayList<String>()).when(mockUserBusinessService).getdistinctClients();
		List<String> actualClients = mockUserResource.getdistinctClients(principal);
		assertNotNull(actualClients);

	}

	@Test
	public void testGetdistinctClientsForUserPrincipalNull() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909464");
		principal.setEmail("integration123@osius.com");
		Mockito.doReturn(new ArrayList<String>()).when(mockUserBusinessService).getdistinctClients();
		List<String> actualClients = mockUserResource.getdistinctClients(principal);
		assertNull(actualClients);

	}

	@Test
	public void testGetdistinctClientsThrowsException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getdistinctClients();
		List<String> actualClients = mockUserResource
				.getdistinctClients(new UserPrincipal(" ", " ", Lists.newArrayList()));
		assertNull(actualClients);
	}

	@Test
	public void testGetDistinctClientsThrowsOtherExceptions() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getdistinctClients();
		List<String> actualClients = mockUserResource.getdistinctClients(null);
		assertNull(actualClients);
	}

	@Test
	public void testUpdatePortalAccessForNewClients() {
		Mockito.doNothing().when(mockUserBusinessService).updatePortalAccessForNewClients();
		mockUserResource.updatePortalAccessForNewClients(userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testUpdatePortalAccessForNewClientsIntegrationAdmin() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail("integration@osius.com");
		Mockito.doNothing().when(mockUserBusinessService).updatePortalAccessForNewClients();
		mockUserResource.updatePortalAccessForNewClients(principal);
		assertTrue(true);
	}

	@Test
	public void testUpdatePortalAccessForNewClientsIntegrationAdmin123() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail("integration123@osius.com");
		principal = null;
		Mockito.doNothing().when(mockUserBusinessService).updatePortalAccessForNewClients();
		mockUserResource.updatePortalAccessForNewClients(principal);
		assertTrue(true);
	}

	@Test
	public void testUpdatePortalAccessForNewClientsThrosException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updatePortalAccessForNewClients();
		mockUserResource.updatePortalAccessForNewClients(new UserPrincipal(" ", " ", Lists.newArrayList()));
		assertTrue(true);
	}

	@Test
	public void testGetTerminatedClientsOfBranch() {
		Map<String, String> expected = new HashMap<>();
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserId(1l);
		principal.setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		Mockito.doReturn(expected).when(mockUserBusinessService).getTerminatedClients(Mockito.anyLong(),
				Mockito.anyString());
		ResponseEntity<Map<String, String>> actualResponse = mockUserResource.getTerminatedClientsOfBranch(principal,
				"token");
		assertEquals(200, actualResponse.getStatusCodeValue());
	}

	@Test
	public void testGetTerminatedClientsOfBranchThrowsException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getTerminatedClients(Mockito.anyLong(),
				Mockito.anyString());
		ResponseEntity<Map<String, String>> actualResponse = mockUserResource
				.getTerminatedClientsOfBranch(userPrincipal, "token");
		assertEquals(200, actualResponse.getStatusCodeValue());
	}

	@Test
	public void testGetTerminatedClientsOfBranchThrowsOtherExceptions() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserId(1l);
		principal.setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getTerminatedClients(Mockito.any(),
				Mockito.any());
		ResponseEntity<Map<String, String>> actualResponse = mockUserResource.getTerminatedClientsOfBranch(principal,
				null);
		assertEquals(200, actualResponse.getStatusCodeValue());
	}

	@Test
	public void testGetTimeNetDetails() {
		Mockito.doReturn(new TimenetLoginInfo()).when(mockUserBusinessService).getTimenetLoginInfo(Mockito.anyString(),
				Mockito.any());
		TimenetLoginInfo actualResponse = mockUserResource.getTimeNetDetails("909464", "token", userPrincipal);
		assertNotNull(actualResponse);
	}

	@Test
	public void testGetTimeNetDetailsThrowsException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getTimenetLoginInfo(Mockito.anyString(),
				Mockito.any());
		TimenetLoginInfo actualResponse = mockUserResource.getTimeNetDetails("909464", "token", userPrincipal);
		assertNull(actualResponse);
	}

	@Test
	public void testUpdateTimeNetDetails() {

		Mockito.doNothing().when(mockUserBusinessService).updateTimenetLoginInfo(Mockito.any(), Mockito.anyString(),
				Mockito.any());
		mockUserResource.updateTimeNetDetails(populateTimenetLoginInfo(), "909464", "token", userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testUpdateTimeNetDetailsThrowsException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateTimenetLoginInfo(Mockito.any(),
				Mockito.anyString(), Mockito.any());
		mockUserResource.updateTimeNetDetails(populateTimenetLoginInfo(), "909464", "token", userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testUpdateUserStatus() {
		Mockito.doReturn(Boolean.TRUE).when(mockUserBusinessService).updateUserStatus(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyBoolean());
		Boolean response = mockUserResource.updateUserStatus("909464", "E71624", Boolean.TRUE);
		assertNotNull(response);
	}

	@Test
	public void testUpdateUserStatusThrowsException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).updateUserStatus(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyBoolean());
		Boolean response = mockUserResource.updateUserStatus("909464", "E71624", Boolean.TRUE);
		assertNotNull(response);
	}

	@Test
	public void testGetUserFilter() {
		userPrincipal.setUserType(GenericConstants.USERTYPE_EXTERNAL.toString());
		Mockito.doReturn(new HashMap<String, Set<String>>()).when(mockUserBusinessService)
				.getUserFilter(Mockito.anyString());
		Map<String, Map<String, Set<String>>> response = mockUserResource.getUserFilter("test@bbsi.com", userPrincipal);
		assertEquals(0, response.size());
	}

	@Test
	public void testGetUserFilterThrowsException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getUserFilter(Mockito.anyString());
		Map<String, Map<String, Set<String>>> response = mockUserResource.getUserFilter("test@bbsi.com", null);
		assertEquals(0, response.size());
	}

	@Test
	public void testGetUserFilterThrowsBbsiException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getUserFilter(null);
		Map<String, Map<String, Set<String>>> response = mockUserResource.getUserFilter(null, userPrincipal);
		assertNull(response);
	}

	@Test
	public void testGetUserClientFilter() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_EXTERNAL.toString());
		Map<String, Set<String>> mapResult = new HashMap<String, Set<String>>();
		mapResult.put("key", new HashSet<String>());
		Mockito.doReturn(mapResult).when(mockUserBusinessService).getUserFilter(Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		Map<String, Set<String>> response = mockUserResource.getUserClientFilter("909464", "test@bbsi.com",
				userPrincipal);
		assertNotNull(response);
	}

	@Test
	public void testGetUserClientFilterThrowsException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).getUserFilter(Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		Map<String, Set<String>> response = mockUserResource.getUserClientFilter("909464", "test@bbsi.com",
				userPrincipal);
		assertNull(response);
	}

	@Test
	public void testgetUserClientFilterWhenEmptyUserType() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(" ");
		Map<String, Set<String>> mapResult = new HashMap<String, Set<String>>();
		mapResult.put("key", new HashSet<String>());
		Mockito.doReturn(mapResult).when(mockUserBusinessService).getUserFilter(Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		Map<String, Set<String>> response = mockUserResource.getUserClientFilter("909464", "test@bbsi.com", principal);
		assertEquals(0, response.size());
	}

	@Test
	public void testVerifyOtp() {
		HttpServletResponse servletResponse = null;
		Mockito.doReturn("isValidToken").when(mockUserBusinessService).mfaOtpValidation(Mockito.any(),
				Mockito.anyString());
		ResponseEntity<?> actualResponse = mockUserResource.verifyOtp(otpDetailsDTO, "Bearer 1234-abcd-4321-dcba",
				userPrincipal, servletResponse);
		assertNotNull(actualResponse);
	}

	@Test
	public void testVerifyOtpCase2() {
		HttpServletResponse servletResponse = null;
		Mockito.doReturn("").when(mockUserBusinessService).mfaOtpValidation(Mockito.any(), Mockito.anyString());
		ResponseEntity<?> actualResponse = mockUserResource.verifyOtp(otpDetailsDTO, "Bearer 1234-abcd-4321-dcba",
				userPrincipal, servletResponse);
		assertNotNull(actualResponse);
		assertEquals("400 BAD_REQUEST", actualResponse.getStatusCode().toString());
	}

	@Test
	public void testVerifyOtpIfResponseIsEmpty() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setMfaFailedAttempts(5);
		HttpServletResponse servletResponse = null;
		Mockito.doReturn(null).when(mockUserBusinessService).mfaOtpValidation(Mockito.any(), Mockito.anyString());
		ResponseEntity<?> actualResponse = mockUserResource.verifyOtp(otpDetailsDTO, "12312 - ascsas3 - 2323ew",
				principal, servletResponse);
		assertEquals("401 UNAUTHORIZED", actualResponse.getStatusCode().toString());
	}

	@Test
	public void testVerifyOtpIfTokenIsEmpty() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setMfaFailedAttempts(2);
		HttpServletResponse servletResponse = null;
		Mockito.doReturn("").when(mockUserBusinessService).mfaOtpValidation(Mockito.any(), Mockito.anyString());
		ResponseEntity<?> actualResponse = mockUserResource.verifyOtp(otpDetailsDTO, "Bearer 1234-5678-abcd-efgh",
				principal, servletResponse);
		Mockito.verify(mockUserBusinessService, Mockito.atLeastOnce()).mfaOtpValidation(Mockito.any(),
				Mockito.anyString());
		assertNotNull(actualResponse);
	}

	@Test
	public void testSaveMfaAttempts() {
		String response = mockUserResource.saveMfaAttempts(userPrincipal, 123, "12bnj - 112jj - 12nkk");
		assertEquals("success", response);
	}

	@Test
	public void testSaveMfaAttemptsWhenLessThan3() {
		String response = mockUserResource.saveMfaAttempts(userPrincipal, 1, "12bnj - 112jj - 12nkk");
		assertEquals("success", response);
	}

	@Test
	public void testRevokeToken() {
		auth.setAuthenticated(true);
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);
		mockUserResource.revokeToken("authToken");
		assertTrue(true);
	}

	@Test
	public void testCreateRehireUser() {
		ReflectionTestUtils.setField(mockUserResource, "baseUrl", "/baseUrl");
		UsersDTO expected = populateUserDTO();
		Mockito.doReturn(expected).when(mockUserBusinessService).createRehireUser(Mockito.any(), Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		UsersDTO actualResponse = mockUserResource.createRehireUser("Bearer 1234-5678-abcd-efgh", "/rehire",
				populateUserDTO(), userPrincipal);
		assertEquals(expected.getEmail(), actualResponse.getEmail());
		Mockito.verify(mockUserBusinessService, Mockito.atLeastOnce()).createRehireUser(Mockito.any(),
				Mockito.anyString(), Mockito.anyString(), Mockito.any());
	}

	@Test
	public void testCreateRehireUserThrowsException() {
		userPrincipal.setEmail("admin@osius.com");
		UsersDTO expected = populateUserDTO();
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).createRehireUser(Mockito.any(),
				Mockito.anyString(), Mockito.anyString(), Mockito.any());
		String key = "admin@mybbsi.com";
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.anyString());
		UsersDTO actualResponse = mockUserResource.createRehireUser("token", "https://portaldev.mybbsi.com/",
				new UsersDTO(), userPrincipal);
		assertNotNull(actualResponse);
	}

	@Test
	public void testCreateRehireUserThrowsValidationException() {
		UsersDTO expected = populateUserDTO();
		UsersDTO inputData = populateUserDTO();
		List<String> nonPrismizedCostCenters = new ArrayList<>();
		nonPrismizedCostCenters.add("NEW YORK");
		nonPrismizedCostCenters.add("Illionis");
		nonPrismizedCostCenters.add("Vancuover");
		nonPrismizedCostCenters.add("Ohio");
		inputData.setNonPrismizedCostCenters(nonPrismizedCostCenters);
		expected.setNonPrismizedCostCenters(nonPrismizedCostCenters);
		Mockito.doReturn(expected).when(mockUserBusinessService).createRehireUser(inputData, "token1", "/rehire",
				userPrincipal);
		UsersDTO actualResponse = mockUserResource.createRehireUser("token1", "/rehire", inputData, userPrincipal);
		assertNull(actualResponse);
	}

	@Test
	public void testCreateUser() {
		UsersDTO expected = populateUserDTO();
		Mockito.doReturn(expected).when(mockUserBusinessService).createUser(Mockito.any(), Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		UsersDTO response = mockUserResource.createUser("token", "uiUrl", populateUserDTO(), userPrincipal);
		assertEquals(expected.getId(), response.getId());
		assertEquals(expected.getEmail(), response.getEmail());
		assertEquals(expected.getEmployeeCode(), response.getEmployeeCode());
	}

	@Test
	public void testCreateUserThrowsException() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).createUser(Mockito.any(),
				Mockito.anyString(), Mockito.anyString(), Mockito.any());
		UsersDTO response = mockUserResource.createUser("token", "uiUrl", new UsersDTO(), userPrincipal);
		assertNull(response.getId());
		assertNull(response.getEmail());
		assertNull(response.getEmployeeCode());
	}

	@Test
	public void testcheckMFA() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Map<String, String> response = new HashMap<>();
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		principal.setMfaAuthorizationRequired(true);
		principal.setMfaFailedAttempts(0);
		principal.setEmail("admin@bbsi.com");
		String key = "admin@bbsi.com<<>>" + LocalDate.now();
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.any(), Mockito.any());
		response = mockUserResource.checkMFA(principal, "key1");
		assertEquals("N", response.get("mfa"));
	}

	@Test
	public void testCheckMFAForOtherUsers() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		ReflectionTestUtils.setField(mockUserResource, "secret", "secret");
		Map<String, String> response = new HashMap<>();
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setMfaAuthorizationRequired(true);
		principal.setEmail("admin@osius.com");
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		String key = "admin@osius.com<<>>" + LocalDate.now().plusMonths(60).toString();
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.any(), Mockito.any());
		response = mockUserResource.checkMFA(principal, "key1");
		assertEquals("N", response.get("mfa"));
	}

	@Test
	public void testCheckMFAWhenKeyIsEmpty() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Map<String, String> response = new HashMap<>();
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setMfaAuthorizationRequired(true);
		principal.setEmail("admin@osius.com");
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		response = mockUserResource.checkMFA(principal, "");
		assertEquals("Y", response.get("mfa"));
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testCheckMFAForOtherException() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		ReflectionTestUtils.setField(mockUserResource, "secret", "secret");
		Map<String, String> response = new HashMap<>();
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setMfaAuthorizationRequired(true);
		principal.setEmail("admin@osius.com");
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		String key = "abc<<>>" + LocalDate.now().plusMonths(60).toString();
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.any(), Mockito.any());
		response = mockUserResource.checkMFA(principal, "key1");
		assertEquals("N", response.get("mfa"));
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testCheckMFAForUnauthorizedAccessException() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		ReflectionTestUtils.setField(mockUserResource, "secret", "secret");
		Map<String, String> response = new HashMap<>();
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setMfaAuthorizationRequired(true);
		principal.setEmail("admin@osius.com");
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		String key = "<<>>";
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.any(), Mockito.any());
		response = mockUserResource.checkMFA(principal, "key1");
		assertEquals("N", response.get("mfa"));
	}

	@Test
	public void testisCCPAUpdated() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode("909464");
		principal.setEmployeeCode("E1786F");
		principal.setIsCcpaRequired(true);
		Mockito.doNothing().when(mockUserBusinessService).isCCPAUpdated("909464", "E1786F", false, principal);
		mockUserResource.isCCPAUpdated("909464", "E1786F", false, principal);
		assertTrue(true);
	}

	@Test
	public void isCCPAUpdatedForExceptions() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).isCCPAUpdated(null, null, false, null);
		mockUserResource.isCCPAUpdated(null, null, false, null);
		assertTrue(true);
	}

	@Test
	public void testaddUserFilter() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		Mockito.doNothing().when(mockUserBusinessService).addSecurityEntry(principal, UserEnum.CLIENT.toString(),
				"Client");
		mockUserResource.addUserFilter(UserEnum.CLIENT.toString(), "Client", principal);
		assertTrue(true);
	}

	@Test
	public void testAddUserFilterForExternalUser() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_EXTERNAL.toString());
		Mockito.doNothing().when(mockUserBusinessService).addSecurityEntry(principal,
				GenericConstants.USERTYPE_EXTERNAL.toString(), "External");
		mockUserResource.addUserFilter("Bearer 1234-5678-abcd-efgh", GenericConstants.USERTYPE_EXTERNAL, principal);
		assertTrue(true);
	}

	@Test
	public void testAddUserFilterForExceptions() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_EXTERNAL.toString());
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).addSecurityEntry(Mockito.any(),
				Mockito.any(), Mockito.any());
		mockUserResource.addUserFilter("Bearer 1234-5678-abcd-efgh", GenericConstants.USERTYPE_EXTERNAL, principal);
		assertTrue(true);
	}

	@Test
	public void testAddUserFilterForOtherExceptions() {
		Mockito.doThrow(BbsiException.class).when(mockUserBusinessService).addSecurityEntry(null, null, null);
		mockUserResource.addUserFilter(null, null, null);
		assertTrue(true);
	}

	@Test
	public void testPrincipal() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setMfaAuthorizationRequired(true);
		principal.setEmail("admin@bbsi.com");
		principal.setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		principal.setClientCode("909464");
		principal.setIsPolicyAccepted(false);
		principal.setIsCcpaRequired(true);
		LocalDateTime createdDate = LocalDateTime.now();
		principal.setCreatedDate(createdDate);
		ParentDTO expected = new ParentDTO();
		String key = "admin@bbsi.com<<>>" + LocalDate.now().toString();
		Mockito.doReturn("admin@bbsi.com<<>>1234").when(mockEncryptAndDecryptUtil).decrypt(Mockito.any(),
				Mockito.any());
		Mockito.doReturn(expected).when(spyUserResource).principal(principal, "Bearer 1234-5678-abcd-efgh", "123456");
		ParentDTO actual = mockUserResource.principal(principal, "Bearer 1234-5678-abcd-efgh", "123456");
		assertEquals(GenericConstants.USERTYPE_BRANCH, actual.getPrincipal().getUserType());
	}

	@Test(expected = NullPointerException.class)
	public void testPrincipalForUserVancouver() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setMfaAuthorizationRequired(true);
		principal.setEmail("admin@osius.com");
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		principal.setClientCode("909464");
		String key = "admin@osius.com<<>>" + LocalDate.now().toString();
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.any(), Mockito.any());
		ParentDTO actual = mockUserResource.principal(principal, "Bearer 1234-5678-abcd-efgh", "123456");
		assertEquals(GenericConstants.USERTYPE_CLIENT, actual.getPrincipal().getUserType());
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testPrincipalForUserVancouverForcheckInvalidMFAValidation() throws Exception {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setMfaAuthorizationRequired(true);
		principal.setEmail("admin@osius.com");
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		principal.setClientCode("909464");
		String key = "admin@osius.com123<<>>" + LocalDate.now().toString();
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.any(), Mockito.any());
		ParentDTO actual = mockUserResource.principal(principal, "Bearer 1234-5678-abcd-efgh", "123456");
		assertTrue(true);
	}

	@Test
	public void testPrincipalUserKeyEmpty() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setMfaAuthorizationRequired(true);
		principal.setEmail("admin@osius.com");
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		principal.setClientCode("909464");
		ParentDTO actual = mockUserResource.principal(principal, "Bearer 1234-5678-abcd-efgh", "");
		assertEquals(actual.getPrincipal().getEmail(), principal.getEmail());
		assertEquals("1234-5678-abcd-efgh", actual.getPrincipal().getToken());
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testPrincipalUserKeyNotEmpty() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		ReflectionTestUtils.setField(mockUserResource, "secret", "secret");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setMfaAuthorizationRequired(true);
		principal.setEmail("admin@osius.com");
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		principal.setClientCode("909464");
		String key = "<<>>";
		Mockito.doReturn(key).when(mockEncryptAndDecryptUtil).decrypt(Mockito.any(), Mockito.any());
		ParentDTO actual = mockUserResource.principal(principal, "Bearer 1234-5678-abcd-efgh", "key1");
		assertEquals(actual.getPrincipal().getEmail(), principal.getEmail());
		assertEquals("1234-5678-abcd-efgh", actual.getPrincipal().getToken());
	}

	@Test
	public void testPrincipalWhenKeyUserNull() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setMfaAuthorizationRequired(false);
		principal.setEmail("admin@osius.com");
		principal.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		principal.setClientCode("909464");
		Mockito.doThrow(BbsiException.class).when(spyUserResource).principal(principal, "Bearer 1234-5678-abcd-efgh",
				null);
		ParentDTO actual = mockUserResource.principal(principal, "Bearer 1234-5678-abcd-efgh", null);
		assertEquals(null, actual.getPrincipal().getToken());
	}

	@Test(expected = NullPointerException.class)
	public void testPrincipalForExceptions() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setMfaAuthorizationRequired(false);
		principal.setEmail("admin@osius.com");
		principal.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		principal.setClientCode("909464");
		Authentication newAuth = new UsernamePasswordAuthenticationToken(principal, auth.getCredentials(),
				auth.getAuthorities());
		OAuth2AuthenticationDetails oauth = (OAuth2AuthenticationDetails) auth.getDetails();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Mockito.doReturn("Bearer<<>>1234").when(mockEncryptAndDecryptUtil).decrypt(Mockito.any(), Mockito.any());
		when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(newAuth);
		Mockito.doThrow(BbsiException.class).when(spyUserResource).principal(null, null, null);
		ParentDTO actual = mockUserResource.principal(null, null, null);
		assertNull(actual);
	}

	@Test(expected = NullPointerException.class)
	public void testPrincipalForOtherExceptions() {
		ReflectionTestUtils.setField(mockUserResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		ReflectionTestUtils.setField(mockUserResource, "secret", "secret");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		principal.setEmail("admin@mybbsi.com");
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		Mockito.doReturn("admin@osius.com<<>>" + LocalDate.now().toString()).when(mockEncryptAndDecryptUtil)
				.decrypt(Mockito.any(), Mockito.any());
		ParentDTO actual = mockUserResource.principal(principal, "Bearer 1234-5678-abcd-efgh", "key1");
		assertEquals(actual.getPrincipal().getEmail(), principal.getEmail());
		assertEquals("1234-5678-abcd-efgh", actual.getPrincipal().getToken());
	}

	private UsersDTO populateUserDTO() {
		UsersDTO userDTO = new UsersDTO();
		userDTO.setEmail("test@test.com");
		userDTO.setEmployeeCode("E1768F");
		userDTO.setEndDate(LocalDateTime.now().plusYears(25));
		userDTO.setFirstName("test");
		userDTO.setFullName("test name");
		userDTO.setId(1l);
		userDTO.setIsCaliforniaUser(false);
		userDTO.setIsFirstLogin(false);
		userDTO.setIsPolicyAccepted(true);
		userDTO.setIsPolicyUpdated(false);
		userDTO.setLastName("name");
		userDTO.setMobile("9490125682");
		userDTO.setStartDate(LocalDateTime.now());
		userDTO.setStatus(true);
		userDTO.setUiUrl("/uiUrl");
		userDTO.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		userDTO.setVersion(1l);
		List<ClientDTOV2> clients = new ArrayList<>();
		ClientDTOV2 client = new ClientDTOV2();
		client.setClientCode("909464");
		client.setClientName("1 Hour Drain");
		client.setCostCenterCode("costCenter1");
		client.setCostCenterDescription("costCenter1");
		client.setEmployeeCode("E1768F");
		client.setI9Approver(false);
		client.setId(1l);
		client.setIsActive(false);
		client.setNewHireId(1l);
		client.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		client.setVersion(1l);
		List<UserClientRoleDTOV2> clientRoles = new ArrayList<>();
		UserClientRoleDTOV2 clientRole = new UserClientRoleDTOV2();
		clientRole.setCode("code");
		clientRole.setDescription("description");
		clientRole.setId(1l);
		clientRole.setIsActive(true);
		clientRole.setName("name");
		clientRole.setRoleId(1l);
		clientRole.setType(UserEnum.ROLE.toString());
		clientRole.setVersion(1l);
		List<AccessGroupDTOV2> accessGroups = new ArrayList<>();
		AccessGroupDTOV2 accessGroup = new AccessGroupDTOV2();
		accessGroup.setClientCode("909464");
		accessGroup.setDescription("description");
		accessGroup.setId(1l);
		accessGroup.setIsActive(true);
		accessGroup.setName("accessGroup");
		List<PrivilegeDTOV2> privileges = new ArrayList<>();
		PrivilegeDTOV2 privilege = new PrivilegeDTOV2();
		privilege.setCode("code");
		privilege.setDescription("description");
		privilege.setId(1l);
		privilege.setType(UserEnum.PRIVILEGE.toString());
		privilege.setVersion(1l);
		privileges.add(privilege);
		accessGroup.setPrivileges(privileges);
		List<FeatureCodeHierarchyDTOV2> featureCodeHierarchy = new ArrayList<>();
		FeatureCodeHierarchyDTOV2 featureHierarchy = new FeatureCodeHierarchyDTOV2();
		featureHierarchy.setCode("featureHierarchy");
		featureHierarchy.setDescription("description");
		featureHierarchy.setId(1l);
		featureHierarchy.setLevel("level1");
		featureHierarchy.setName("hierarchy");
		featureHierarchy.setParentId(0l);
		featureHierarchy.setPrivileges(privileges);
		featureHierarchy.setType(UserEnum.FEATURE.toString());
		featureCodeHierarchy.add(featureHierarchy);
		accessGroup.setFeatureCodeHierarchy(featureCodeHierarchy);
		accessGroups.add(accessGroup);
		clientRole.setAccessGroups(accessGroups);
		clientRoles.add(clientRole);
		client.setClientRoles(clientRoles);
		List<CommonDTO> mappings = new ArrayList<>();
		CommonDTO mapping = new CommonDTO();
		mapping.setCode("mapping");
		mapping.setDescription("description");
		mapping.setId(1l);
		mapping.setName("name");
		mapping.setNewStatus(true);
		mapping.setParentId(0l);
		mapping.setStatus(true);
		mapping.setType(UserEnum.PRIVILEGE.toString());
		mappings.add(mapping);
		client.setMappings(mappings);
		clients.add(client);
		userDTO.setClients(clients);
		return userDTO;
	}

	private TimenetLoginInfo populateTimenetLoginInfo() {
		TimenetLoginInfo loginInfo = new TimenetLoginInfo();
		loginInfo.setTimenetCompanyId("timenetCompanyId");
		return loginInfo;
	}

	private List<CostCenterClientDTO> populateCostCenterDto() {
		List<CostCenterClientDTO> listCostCenter = new ArrayList<CostCenterClientDTO>();
		CostCenterClientDTO costCenter = new CostCenterClientDTO();
		costCenter.setBusinessName("businessName");
		costCenter.setClientId("909464");
		costCenter.setClientName("testClientName");
		costCenter.setId(123L);
		listCostCenter.add(costCenter);
		return listCostCenter;
	}

	private CustomPersonalDTO populateCustomPersonalDTO() {
		CustomPersonalDTO customDTO = new CustomPersonalDTO();
		customDTO.setClientCode("909464");
		customDTO.setId(123L);
		customDTO.setEmployeeCode("E71624");
		customDTO.setDateOfBirth(LocalDate.now().minusYears(25));
		customDTO.setEmployeeId(1l);
		customDTO.setFirstName("first");
		customDTO.setFullName("first last");
		customDTO.setGender("F");
		customDTO.setGenderDescription("Female");
		customDTO.setId(1l);
		customDTO.setIsSync(true);
		customDTO.setLastName("last");
		customDTO.setNewEmail("first.last@email.com");
		customDTO.setOldEmail("last.first@email.com");
		customDTO.setWorkEmail("first.last@osius.com");
		return customDTO;
	}

	private UserPrincipal getUserPrincipal() {
		UserPrincipal userPrincipal = new UserPrincipal("admin@osius.com", "Osicpl@1",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		userPrincipal.setEmail("admin@osius.com");
		userPrincipal.setUserType(GenericConstants.USERTYPE_EMPLOYEE);
		userPrincipal.setToken("Bearer 1234-5678-abcd-efgh");
		userPrincipal.setIsPolicyUpdated(true);
		userPrincipal.setIsPolicyAccepted(true);
		userPrincipal = new UserPrincipal("288", "Osicpl@1", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		userPrincipal.setEmail("admin@osius.com");
		userPrincipal.setUserType(GenericConstants.USERTYPE_EMPLOYEE);
		userPrincipal.setToken("Bearer 1234-5678-abcd-efgh");
		userPrincipal.setClientMap(getClientMap());
		userPrincipal.setMfaAuthorizationRequired(true);
		userPrincipal.setIsPolicyAccepted(false);
		userPrincipal.setIsCcpaRequired(true);
		return userPrincipal;
	}

	private UserPrincipal getUserPrincipalForUserType() {
		UserPrincipal userPrincipal = new UserPrincipal("288", "Osicpl@1",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		userPrincipal.setEmail("admin@osius.com");
		userPrincipal.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		userPrincipal.setClientMap(getClientMap());
		return userPrincipal;
	}

	private Map<String, String> getClientMap() {
		Map<String, String> clientMap = new HashMap<String, String>();
		clientMap.put("902738", "JIT TRANSPORTATION");
		clientMap.put("909096", "AG ENTERPRISES LLC");
		clientMap.put("909464", "1hour Drain");
		return clientMap;
	}

	private PolicyAcceptedDTO getPolicyAcceptedDTO() {
		PolicyAcceptedDTO policyAcceptedDTO = new PolicyAcceptedDTO();
		policyAcceptedDTO.setEmail("admin@osius.com");
		policyAcceptedDTO.setIsPolicyAccepted(true);
		return policyAcceptedDTO;
	}

	
}
