package com.bbsi.platform.user.web.resource.v1;

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
import org.springframework.test.util.ReflectionTestUtils;

import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.user.dto.v2.PasswordDTOV2;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.exception.business.ValidationException;
import com.bbsi.platform.user.businessservice.intf.PasswordBusinessService;
import com.google.common.collect.Lists;

public class PasswordResourceTest {

	@InjectMocks
	private PasswordResource passwordResource;

	@Mock
	private PasswordDTOV2 passwordDTOV2;

	@Mock
	private PasswordBusinessService passwordBusinessService;

	private UserPrincipal userPrincipal;

	@Before
	public void setUp() throws Exception {
		passwordDTOV2 = new PasswordDTOV2();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSubmitEmailForResetPassword() {
		Mockito.doReturn(passwordDTOV2).when(passwordBusinessService).submitEmailForResetPassword(Mockito.any(),
				Mockito.anyString());
		PasswordDTOV2 actualResponse = passwordResource.submitEmailForResetPassword("email@email.com",
				"https://portaldev.mybbsi.com/");
		assertNotNull(actualResponse);
	}
	
	@Test
	public void testSubmitEmailForResetPasswordExpectingNull() {
		Mockito.doReturn(null).when(passwordBusinessService).submitEmailForResetPassword(Mockito.any(),
				Mockito.anyString());
		PasswordDTOV2 actualResponse = passwordResource.submitEmailForResetPassword("email@email.com",
				"https://portaldev.mybbsi.com/");
		assertNull(actualResponse);
	}

	@Test
	public void testSubmitEmailForResetPasswordThrowsException() {
		Mockito.doThrow(BbsiException.class).when(passwordBusinessService).submitEmailForResetPassword(Mockito.any(),
				Mockito.anyString());
		PasswordDTOV2 actualResponse = passwordResource.submitEmailForResetPassword("email@email.com",
				"https://portaldev.mybbsi.com/");
		assertNull(actualResponse);
	}
	
	@Test
	public void testSendEmailForResetPassword()
	{
		Mockito.doNothing().when(passwordBusinessService).sendEmailForResetPassword(Mockito.anyString(), Mockito.anyString());
		passwordResource.sendEmailForResetPassword("https://portaldev.mybbsi.com/", "email@email.com");
		assertTrue(true);
	}
	
	@Test(expected=ValidationException.class)
	public void testSendEmailForResetPasswordThrowsException()
	{
		Mockito.doThrow(BbsiException.class).when(passwordBusinessService).sendEmailForResetPassword(null, null);
		passwordResource.sendEmailForResetPassword(null, "");
		assertTrue(true);
	}
	
	@Test
	public void testSendEmailForResetPasswordThrowsBbsiException()
	{
		Mockito.doThrow(ValidationException.class).when(passwordBusinessService).sendEmailForResetPassword(null, null);
		passwordResource.sendEmailForResetPassword(null, null);
		assertTrue(true);
	}

	@Test
	public void testSaveForgotPassword() {
		PasswordDTOV2 input = new PasswordDTOV2();
		input.setEmail("admin@osius.com");
		Mockito.doReturn(input).when(passwordBusinessService).saveForgotPassword(Mockito.anyString(), Mockito.anyString());
		PasswordDTOV2 actualResponse = passwordResource.saveForgotPassword("D1k/18XJq8q4cEWeidztsA==", "VXsLGV/c7nmN1rj5j4CegNjxXRy8qqonGtiEiNa+0JpHlxuC1Q0ZpKG9yJCIMkSyv6sZ0E/kCArzjIVisF+tg5DeIbgufGxNoxyZGBIAojSTfAHULD8NOUpgV1Cm6HPqyyhMAhumpz5H3LUDQg9lKCHaiKZ5uBD9VW9hgj3BmTKKndIFWuauqfziyzm/ln6OtPFrG9DW+F2WkXMlMFnO/w8jqEfxMSis0VM+8j/PXQNzIlRkiqe43FqmbxvrSGFh");
		assertNotNull(actualResponse);
	}

	@Test
	public void testSaveForgotPasswordWhenPasswordIsNull() {
		Mockito.doThrow(BbsiException.class).when(passwordBusinessService).saveForgotPassword(Mockito.anyString(), Mockito.anyString());
		PasswordDTOV2 actualResponse = passwordResource.saveForgotPassword("D1k/18XJq8q4cEWeidztsA==", "abc");
		assertNull(actualResponse);
	}

	@Test
	public void testChangePassword() {
		PasswordDTOV2 expected = getPasswordDTOV2();
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		Mockito.doReturn(expected).when(passwordBusinessService).changePassword(Mockito.any(), Mockito.anyString(), Mockito.any(), Mockito.anyString());
		Mockito.doNothing().when(passwordBusinessService).removeToken(Mockito.any(), Mockito.anyBoolean());
		PasswordDTOV2 actualResponse = passwordResource.changePassword(getPasswordDTOV2(), "token1", principal);
		assertNotNull(actualResponse);
		Mockito.verify(passwordBusinessService, Mockito.atLeastOnce()).removeToken(Mockito.any(), Mockito.anyBoolean());
	}

	@Test
	public void testChangePasswordWhenDTOIsNull() {
		Mockito.doThrow(BbsiException.class).when(passwordBusinessService).changePassword(new PasswordDTOV2(), " ", new UserPrincipal(" ", " ", Lists.newArrayList())," ");
		PasswordDTOV2 actualResponse = passwordResource.changePassword(new PasswordDTOV2(), "token1", userPrincipal);
		assertNull(actualResponse);
	}
	
	@Test
	public void testValidateJwtToken() {
		PasswordDTOV2 passwordv2 = new PasswordDTOV2();
		PasswordDTOV2 result = new PasswordDTOV2();
		passwordv2.setToken("Bearer 8973-d5d2d-8497d-derdd");
		Mockito.doReturn(result).when(passwordBusinessService).validateJwtToken(Mockito.any());
		PasswordDTOV2 actualResponse = passwordResource.validateJwtToken(passwordv2);
		assertNotNull(actualResponse);
	}

	@Test
	public void testValidateJwtTokenPasswordDTOIsNull() {
		Mockito.doThrow(BbsiException.class).when(passwordBusinessService).validateJwtToken(null);
		PasswordDTOV2 actualResponse = passwordResource.validateJwtToken(null);
		assertNull(actualResponse);
	}

	@Test
	public void testSendNotificationsForExpiredPassword() {
		List<String> result = new ArrayList<String>();
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail("integration@osius.com");
		Mockito.doReturn(result).when(passwordBusinessService).sendNotificationsForExpiredPassword(principal, "/expiredPassword ", "token1");
		List<String> actualResponse = passwordResource.sendNotificationsForExpiredPassword(principal, "token1");
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testSendNotificationForExpiredPasswordForException() {
		Mockito.doThrow(BbsiException.class).when(passwordBusinessService).sendNotificationsForExpiredPassword(null, " ", " ");
		List<String> actualResponse = passwordResource.sendNotificationsForExpiredPassword(null, " ");
		assertNull(actualResponse);
	}
	
	@Test
	public void testSendNotificationForExpiredPasswordForUnauthorizedAccessException() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail("admin@osius.com");
		Mockito.doThrow(BbsiException.class).when(passwordBusinessService).sendNotificationsForExpiredPassword(principal, "/expiredPassword", "token1");
		List<String> actualResponse = passwordResource.sendNotificationsForExpiredPassword(principal, "token1");
		assertNull(actualResponse);
	}
	
	@Test
	public void testSendNotificationForExpiredPasswordForExceptions() {
		UserPrincipal principal = new UserPrincipal(" ", " ", Lists.newArrayList());
		Mockito.doThrow(BbsiException.class).when(passwordBusinessService).sendNotificationsForExpiredPassword(principal, " ", " ");
		List<String> actualResponse = passwordResource.sendNotificationsForExpiredPassword(principal, " ");
		assertNull(actualResponse);
	}

	@Test
	public void testSendFlagValueForExpiredPassword() {
		ReflectionTestUtils.setField(passwordResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@mybbsi.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail("admin@mybbsi.com");
		Map<String, String> emailMap = new HashMap<String, String>();
		emailMap.put(GenericConstants.MEMBER_INFO_EMAIL.toString(), "admin@mybbsi.com");
		PasswordDTOV2 passv2 = new PasswordDTOV2();
		passv2.setConfirmPassword("pass1");
		passv2.setDescription("description");
		passv2.setEmail("admin@osius.com");
		Mockito.doReturn(passv2).when(passwordBusinessService).sendFlagValueForExpiredPassword(Mockito.anyString(),
				Mockito.any());
		PasswordDTOV2 actualResponse = passwordResource.sendFlagValueForExpiredPassword(emailMap, principal);
		assertEquals(actualResponse.getEmployeeCode(), passv2.getEmployeeCode());
		assertEquals(actualResponse.getEmail(), passv2.getEmail());
	}

	@Test
	public void testSendFlagValueForExpiredPasswordExpectingNull() {
		ReflectionTestUtils.setField(passwordResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@mybbsi.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail(GenericConstants.SUPER_ADMIN.toString());
		Map<String, String> emailMap = new HashMap<String, String>();
		Mockito.doReturn(null).when(passwordBusinessService).sendFlagValueForExpiredPassword(Mockito.anyString(),
				Mockito.any());
		PasswordDTOV2 actualResponse = passwordResource.sendFlagValueForExpiredPassword(emailMap, principal);
		assertNull(actualResponse);
	}

	@Test
	public void testSendFlagValueForExpiredPasswordForException() {
		userPrincipal = new UserPrincipal("test@test.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setEmail("test@test.com");
		Map<String, String> emailMap = new HashMap<String, String>();
		emailMap.put("", "");
		Mockito.doReturn(null).when(passwordBusinessService)
				.sendFlagValueForExpiredPassword(Mockito.anyString(), Mockito.any());
		PasswordDTOV2 actualResponse = passwordResource.sendFlagValueForExpiredPassword(emailMap, userPrincipal);
		assertNull(actualResponse);
	}
	
	@Test(expected=NullPointerException.class)
	public void testSendFlagValueForExpiredPasswordForIntegration() {
		ReflectionTestUtils.setField(passwordResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail("integration@osius.com");
		Map<String, String> emailMap = new HashMap<String, String>();
		emailMap.put(GenericConstants.MEMBER_INFO_EMAIL.toString(), "admin@bbsi.com");
		PasswordDTOV2 passv2 = new PasswordDTOV2();
		Mockito.doReturn(passv2).when(passwordBusinessService).sendFlagValueForExpiredPassword(Mockito.anyString(),
				Mockito.any());
		PasswordDTOV2 actualResponse = passwordResource.sendFlagValueForExpiredPassword(emailMap, principal);
		assertEquals(actualResponse.getEmployeeCode(), passv2.getEmployeeCode());
		assertEquals(actualResponse.getEmail(), passv2.getEmail());
	}
	
	@Test(expected=NullPointerException.class)
	public void testSendFlagValueForExpiredPasswordForAdmin() {
		ReflectionTestUtils.setField(passwordResource, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail("admin@osius.com");
		Map<String, String> emailMap = new HashMap<String, String>();
		emailMap.put(GenericConstants.MEMBER_INFO_EMAIL.toString(), "admin@bbsi.com");
		PasswordDTOV2 passv2 = new PasswordDTOV2();
		Mockito.doReturn(passv2).when(passwordBusinessService).sendFlagValueForExpiredPassword(Mockito.anyString(),
				Mockito.any());
		PasswordDTOV2 actualResponse = passwordResource.sendFlagValueForExpiredPassword(emailMap, principal);
		assertEquals(actualResponse.getEmployeeCode(), passv2.getEmployeeCode());
		assertEquals(actualResponse.getEmail(), passv2.getEmail());
	}
	
	private PasswordDTOV2 getPasswordDTOV2() {
		PasswordDTOV2 data = new PasswordDTOV2();
		data.setConfirmPassword("Osicpl@0");
		data.setDescription("Password");
		data.setEmail("admin@osius.com");
		Map<String, Set<String>> emailMap = new HashMap<>();
		Set<String> emails = new HashSet<>();
		emails.add("admin@osius.com");
		emailMap.put(GenericConstants.MEMBER_INFO_EMAIL.toString(), emails);
		data.setEmailMap(emailMap);
		data.setEmployeeCode("E1786F");
		data.setFirstName("first");
		data.setLastName("last");
		data.setName("first last");
		data.setNewPassword("Osicpl@0");
		data.setPassword("Osicpl@2");
		data.setPasswordExpired(false);
		data.setToken("token1");
		data.setUserId(1l);
		return data;
	}
}
