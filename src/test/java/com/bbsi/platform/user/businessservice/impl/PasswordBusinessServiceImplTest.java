package com.bbsi.platform.user.businessservice.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.util.ReflectionTestUtils;

import com.bbsi.platform.common.businessservice.intf.IntegrationBusinessService;
import com.bbsi.platform.common.dto.Email;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.exception.ExceptionUtils;
import com.bbsi.platform.common.generic.CommonUtilities;
import com.bbsi.platform.common.generic.EncryptAndDecryptUtil;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.GenericFunctions;
import com.bbsi.platform.common.generic.JwtTokenUtil;
import com.bbsi.platform.common.generic.WebClientTemplate;
import com.bbsi.platform.common.user.dto.v2.PasswordDTOV2;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.exception.business.ValidationException;
import com.bbsi.platform.user.model.ClientMaster;
import com.bbsi.platform.user.model.RbacEntity;
import com.bbsi.platform.user.model.UserClients;
import com.bbsi.platform.user.model.Users;
import com.bbsi.platform.user.model.UsersPasswords;
import com.bbsi.platform.user.repository.UserClientsRepository;
import com.bbsi.platform.user.repository.UsersPasswordsRepository;
import com.bbsi.platform.user.repository.UsersRepository;
import com.bbsi.platform.user.utils.MethodNames;
import com.bbsi.platform.user.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import reactor.core.publisher.Mono;

public class PasswordBusinessServiceImplTest {

	@InjectMocks
	private PasswordBusinessServiceImpl passwordBusinessServiceImpl;

	@Mock
	private UsersRepository userRepository;

	@Mock
	private UsersPasswordsRepository usersPasswordsRepository;

	@Mock
	private IntegrationBusinessService integrationBusinessService;

	@Mock
	private JsonNode node;

	@Mock
	private WebClientTemplate webClientTemplate;

	@Mock
	private CommonUtilities commonUtilities;

	@Mock
	private UserClientsRepository userClientRepository;

	private UserPrincipal userPrincipal;

	@Mock
	private TokenStore tokenStore;

	@Mock
	private EncryptAndDecryptUtil encryptAndDecryptUtil;

	@Mock(name = "customEncoder")
	private PasswordEncoder passwordEncoder;

	@Mock
	private ObjectMapper mapper;

	private PasswordDTOV2 passwordDTOV2;

	private UsersPasswords userPwd;

	@Mock
	private Base64 decoder;

	private Users user;

	@Mock
	private JwtTokenUtil jwtTokenUtil;

	@Mock
	private OAuth2Authentication authentication;
	

	private List<UsersPasswords> userPassword = new ArrayList<UsersPasswords>();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ExceptionUtils.handleException = Mockito.mock(GenericFunctions.ThrowableTriConsumer.class);
		passwordDTOV2 = populatePasswordDTOV2();
		user = populateUser();
		userPwd = populateUserPassword();
		userPassword.add(userPwd);
	}

	@After
	public void tearDown() throws Exception {

		passwordDTOV2 = null;
		user = null;
		userPwd = null;
		userPassword = null;
	}

	@Test
	public void testSetEmailForgotPasswordURL() {
		passwordBusinessServiceImpl.setEmailForgotPasswordURL("pwd");
		assertTrue(true);
	}

	@Test
	public void testSetMaxPassword() {
		passwordBusinessServiceImpl.setMaxPassword(8);
		assertTrue(true);
	}

	@Test
	public void testisNewPasswordPreviouslyUsed() {
		List<UsersPasswords> inputList = new ArrayList<>();
		UsersPasswords input = new UsersPasswords();
		input.setClientCode("909464");
		input.setId(1l);
		input.setPassword("Osicpl@2");
		input.setPasswordIndex(1);
		input.setUserId(1l);
		input.setVersion(1);
		inputList.add(input);
		input = new UsersPasswords();
		input.setClientCode("909464");
		input.setId(2l);
		input.setPassword("Osicpl@0");
		input.setPasswordIndex(2);
		input.setUserId(1l);
		input.setVersion(1);
		inputList.add(input);
		Mockito.doReturn(true).when(passwordEncoder).matches(Mockito.any(), Mockito.any());
		Boolean result = passwordBusinessServiceImpl
				.isNewPasswordPreviouslyUsed("$2a$04$XqPNNJX52MaLwNLBWHEBN.bUjH4fEHgn.U.Ix035xzZ9yCjzyfXzW", inputList);
		assertEquals(true, result);
	}

	@Test
	public void testisNewPasswordPreviouslyUsedNotInList() {
		List<UsersPasswords> inputList = new ArrayList<>();
		UsersPasswords input = new UsersPasswords();
		input.setClientCode("909464");
		input.setId(1l);
		input.setPassword("Osicpl@2");
		input.setPasswordIndex(1);
		input.setUserId(1l);
		input.setVersion(1);
		inputList.add(input);
		input = new UsersPasswords();
		input.setClientCode("909464");
		input.setId(2l);
		input.setPassword("Osicpl@0");
		input.setPasswordIndex(2);
		input.setUserId(1l);
		input.setVersion(1);
		inputList.add(input);
		Mockito.doReturn(false).when(passwordEncoder).matches(Mockito.any(), Mockito.any());
		Boolean result = passwordBusinessServiceImpl.isNewPasswordPreviouslyUsed("Osicpl@1", inputList);
		assertEquals(false, result);
	}

	@Test
	public void testremoveWhenSizeIsGreateEqualMaxPasswords() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "maxPasswords", 2);
		List<UsersPasswords> inputList = new ArrayList<>();
		UsersPasswords input = new UsersPasswords();
		input.setClientCode("909464");
		input.setId(1l);
		input.setPassword("Osicpl@0");
		input.setPasswordIndex(1);
		input.setUserId(1l);
		input.setVersion(1l);
		inputList.add(input);
		input = new UsersPasswords();
		input.setClientCode("909464");
		input.setId(2l);
		input.setPassword("Osicpl@1");
		input.setPasswordIndex(2);
		input.setVersion(1l);
		inputList.add(input);
		input = new UsersPasswords();
		input.setClientCode("909464");
		input.setId(3l);
		input.setPassword("Osicpl@2");
		input.setPasswordIndex(3);
		input.setId(1l);
		input.setVersion(1l);
		List<UsersPasswords> result = passwordBusinessServiceImpl.removeWhenSizeIsGreateEqualMaxPasswords(inputList);
		assertEquals(result.size(), inputList.size());
	}

	@Test
	public void testremoveWhenSizeIsGreateEqualMaxPasswordsForExceptions() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "maxPasswords", 2);
		List<UsersPasswords> result = passwordBusinessServiceImpl
				.removeWhenSizeIsGreateEqualMaxPasswords(new ArrayList<>());
		assertEquals(0, result.size());
	}

	@Test(expected = NullPointerException.class)
	public void testremoveWhenSizeIsGreateEqualMaxPasswordsForOtherExceptions() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "maxPasswords", 2);
		List<UsersPasswords> result = passwordBusinessServiceImpl.removeWhenSizeIsGreateEqualMaxPasswords(null);
		assertNull(result);
	}

	@Test
	public void testSubmitEmailForResetPassword() throws Exception {
		Mockito.doReturn("email@email.com").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn(user).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateUserClients()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		Mockito.doReturn("response").when(integrationBusinessService).getEmployeeInfo(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(node).when(mapper).readTree(Mockito.anyString());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.submitEmailForResetPassword("pFQXvbh2QOSn2dWBfj+UZw==", "bbsi.com");
		Mockito.verify(userRepository, Mockito.atLeastOnce()).findByEmail(Mockito.anyString());
	}

	@Test
	public void testSubmitEmailForResetPasswordForEmptyClients() throws Exception {
		PasswordDTOV2 input = populatePasswordDTOV2();
		Mockito.doReturn("email@email.com").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn(populateUserForCilentsEmpty()).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateUserClients()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		Mockito.doReturn("response").when(integrationBusinessService).getEmployeeInfo(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(node).when(mapper).readTree(Mockito.anyString());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.submitEmailForResetPassword("pFQXvbh2QOSn2dWBfj+UZw==", "bbsi.com");
		assertEquals(null, actualResponse);
	}

	@Test
	public void testSubmitEmailForResetPasswordForInactiveClient() throws Exception {
		PasswordDTOV2 input = populatePasswordDTOV2();
		Users data = user;
		data.getClients().get(0).setIsActive(false);
		Mockito.doReturn("email@email.com").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn(user).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateUserClients()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		Mockito.doReturn("response").when(integrationBusinessService).getEmployeeInfo(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(node).when(mapper).readTree(Mockito.anyString());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.submitEmailForResetPassword("pFQXvbh2QOSn2dWBfj+UZw==", "bbsi.com");
		assertNull(actualResponse);
	}

	@Test
	public void testSubmitEmailForResetPasswordIfUserIsNull() {

		Mockito.doReturn(null).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(null).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.submitEmailForResetPassword("email@email.com",
				"bbsi.com");
		assertNull(actualResponse);
	}

	@Test
	public void testSubmitEmailForResetPasswordIfUserThrowsException() {

		Mockito.doThrow(BbsiException.class).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.submitEmailForResetPassword("email@email.com",
				"bbsi.com");
		assertNull(actualResponse);
	}

	@Test
	public void testSendEmailForResetPassword() {
		Email data = new Email();
		data.setContext("context");
		data.setDbTemplate(false);
		data.setFrom("no-reply@mybbsi.com");
		data.setReplyTo("admin@mybbsi.com");
		data.setSubject("subject");
		data.setTemplate(MethodNames.SUBMIT_EMAIL_FOR_RESET_PASSWRD.toString());
		data.setTemplateName("submit email for reset password");
		data.setToAddress("admin@osius.com");
		Mono<Email> result = Mono.just(data);
		String redirectUrl = "admin@osius.com";
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailForgotPasswordURL",
				"http://localhost:8807/notification/v1/email/forgot/");
		Mockito.doReturn("email@email.com").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn(user).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(redirectUrl).when(encryptAndDecryptUtil).encrypt(Mockito.anyString());
		Mockito.doReturn(result).when(webClientTemplate).postForObjectMono(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		passwordBusinessServiceImpl.sendEmailForResetPassword("YWRtaW5Ab3NpdXMuY29t", "/resetPassword");
		assertTrue(true);
	}

	@Test
	public void testSendEmailForResetPasswordForEmptyClients() {
		Email data = new Email();
		data.setContext("context");
		data.setDbTemplate(false);
		data.setFrom("no-reply@mybbsi.com");
		data.setReplyTo("admin@mybbsi.com");
		data.setSubject("subject");
		data.setTemplate(MethodNames.SUBMIT_EMAIL_FOR_RESET_PASSWRD.toString());
		data.setTemplateName("submit email for reset password");
		data.setToAddress("admin@osius.com");
		Mono<Email> result = Mono.just(data);
		String redirectUrl = "admin@osius.com";
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailForgotPasswordURL",
				"http://localhost:8807/notification/v1/email/forgot/");
		Mockito.doReturn("email@email.com").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn(populateUserForCilentsEmpty()).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(redirectUrl).when(encryptAndDecryptUtil).encrypt(Mockito.anyString());
		Mockito.doReturn(result).when(webClientTemplate).postForObjectMono(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		passwordBusinessServiceImpl.sendEmailForResetPassword("YWRtaW5Ab3NpdXMuY29t", "/resetPassword");
		assertTrue(true);
	}

	@Test
	public void testSendEmailForResetPasswordIfUserIsNull() {
		Mockito.doThrow(BbsiException.class).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doThrow(BbsiException.class).when(webClientTemplate).postForObjectMono(Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any(), Mockito.any());
		passwordBusinessServiceImpl.sendEmailForResetPassword("YWRtaW5Ab3NpdXMuY29t", "");
		assertTrue(true);
	}

	@Test(expected = ValidationException.class)
	public void testSendEmailForResetPasswordInvalid() {
		Mockito.doThrow(ValidationException.class).when(userRepository).findByEmail(Mockito.any());
		Mockito.doThrow(ValidationException.class).when(webClientTemplate).postForObjectMono(Mockito.any(),
				Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		passwordBusinessServiceImpl.sendEmailForResetPassword("YWRtaW5Ab3NpdXMuY29t", null);
		assertTrue(true);
	}

	@Test
	public void testSendEmailForResetPasswordForNull() {
		Mockito.doReturn(null).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(null).when(webClientTemplate).postForObjectMono(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any());
		passwordBusinessServiceImpl.sendEmailForResetPassword("YWRtaW5Ab3NpdXMuY29t", "/url");
		Mockito.verify(userRepository, Mockito.atLeastOnce()).findByEmail(null);
		Mockito.verify(webClientTemplate, Mockito.never()).postForObjectMono(Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any(), Mockito.any());
		assertTrue(true);
	}

	@Test
	public void testChangePassword() {
		Email data = new Email();
		data.setContext("context");
		data.setDbTemplate(false);
		data.setFrom("no-reply@mybbsi.com");
		data.setReplyTo("admin@mybbsi.com");
		data.setSubject("subject");
		data.setTemplate(MethodNames.SUBMIT_EMAIL_FOR_RESET_PASSWRD.toString());
		data.setTemplateName("submit email for reset password");
		data.setToAddress("admin@osius.com");
		Mono<Email> result = Mono.just(data);
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "maxPasswords", 10);
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailURL", "/url");
		List<UsersPasswords> userPasswords = new ArrayList<>();
		userPasswords.add(populateUserPassword());
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail(populateUser().getEmail());
		Users user = populateUsers();
		user.setPassword("Osicpl@2");
		user.setModifiedOn(LocalDateTime.now().minusDays(91));
		user.getClients().get(0).setUserType("NewHire");
		when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
		Mockito.doReturn("password1, password2").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn(true, false).when(passwordEncoder).matches(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(userPasswords).when(usersPasswordsRepository).findByUserId(Mockito.anyLong());
		Mockito.doReturn(user).when(userRepository).save(Mockito.any());
		Mockito.doReturn(userPasswords).when(usersPasswordsRepository).saveAll(Mockito.any());
		Mockito.doReturn(result).when(webClientTemplate).postForObjectMono(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.changePassword(populatePasswordDTOV2(),
				"Bearer 12jkkj-1231d-as123", principal, "/changePassword");
		assertEquals(actualResponse.getPassword(), populatePasswordDTOV2().getPassword());
	}

	@Test
	public void testChangePasswordWhenPasswordListIsEmpty() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "maxPasswords", 10);
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailURL", "/url");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail(populateUser().getEmail());
		Users user = populateUsers();
		user.setPassword("Osicpl@2");
		user.setModifiedOn(LocalDateTime.now().minusDays(91));
		when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
		Mockito.doReturn("password1, password2").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn(true, false).when(passwordEncoder).matches(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(new ArrayList<>()).when(usersPasswordsRepository).findByUserId(Mockito.anyLong());
		Mockito.doReturn(user).when(userRepository).save(Mockito.any());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.changePassword(populatePasswordDTOV2(),
				"Bearer 12jkkj-1231d-as123", principal, "/changePassword");
		assertEquals(actualResponse.getPassword(), populatePasswordDTOV2().getPassword());
	}

	@Test
	public void testChangePasswordForValidationException() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "maxPasswords", 10);
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailURL", "/url");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail(populateUser().getEmail());
		Users user = populateUsers();
		user.setPassword("Osicpl@2");
		user.setModifiedOn(LocalDateTime.now().minusDays(91));
		user.setClients(null);
		when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
		Mockito.doReturn("password1, password2").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn(false).when(passwordEncoder).matches(Mockito.anyString(), Mockito.anyString());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.changePassword(populatePasswordDTOV2(),
				"Bearer 12jkkj-1231d-as123", principal, "/changePassword");
		assertEquals(actualResponse.getPassword(), populatePasswordDTOV2().getPassword());
	}

	@Test
	public void testChangePasswordForValidationExceptionWhenUserNull() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "maxPasswords", 10);
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailURL", "/url");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail(populateUser().getEmail());
		Users user = populateUsers();
		user.setPassword("Osicpl@2");
		user.setModifiedOn(LocalDateTime.now().minusDays(91));
		user.setClients(null);
		when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);
		Mockito.doReturn("password1, password2").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn(false).when(passwordEncoder).matches(Mockito.anyString(), Mockito.anyString());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.changePassword(populatePasswordDTOV2(),
				"Bearer 12jkkj-1231d-as123", principal, "/changePassword");
		assertEquals(actualResponse.getPassword(), populatePasswordDTOV2().getPassword());
	}

	@Test
	public void testChangePasswordMaxPasswordExceeds() {
		List<UsersPasswords> userPasswords = new ArrayList<>();
		userPasswords.add(populateUserPassword());
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "maxPasswords", 1);
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailURL", "/url");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail(populateUser().getEmail());
		Users user = populateUsers();
		user.setPassword("Osicpl@2");
		user.setModifiedOn(LocalDateTime.now().minusDays(91));
		when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
		Mockito.doReturn("password1, password2").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn(true, false).when(passwordEncoder).matches(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(userPasswords).when(usersPasswordsRepository).findByUserId(Mockito.anyLong());
		Mockito.doReturn(user).when(userRepository).save(Mockito.any());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.changePassword(populatePasswordDTOV2(),
				"Bearer 12jkkj-1231d-as123", principal, "/changePassword");
		assertEquals(actualResponse.getPassword(), populatePasswordDTOV2().getPassword());
	}

	@Test
	public void testSaveForgotPassword() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailForgotPasswordURL", "url");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "maxPasswords", 2);
		Users users = populateUser();
		List<UsersPasswords> expected = new ArrayList<>();
		expected.add(populateUserPassword());
		Mockito.doReturn("abcdfer","password").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn("UserName").when(jwtTokenUtil).getUsernameFromToken(Mockito.anyString());
		Mockito.doReturn(users).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(expected).when(usersPasswordsRepository).findByUserId(Mockito.anyLong());
		Mockito.doReturn(false).when(passwordEncoder).matches(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateEmailResponse()).when(webClientTemplate).postForObjectMono(Mockito.anyString(),
				Mockito.any(), Mockito.any(), Mockito.anyMap());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.saveForgotPassword("D1k/18XJq8q4cEWeidztsA==", "VXsLGV/c7nmN1rj5j4CegNjxXRy8qqonGtiEiNa+0JpHlxuC1Q0ZpKG9yJCIMkSyv6sZ0E/kCArzjIVisF+tg5DeIbgufGxNoxyZGBIAojSTfAHULD8NOUpgV1Cm6HPqyyhMAhumpz5H3LUDQg9lKCHaiKZ5uBD9VW9hgj3BmTKKndIFWuauqfziyzm/ln6OtPFrG9DW+F2WkXMlMFnO/w8jqEfxMSis0VM+8j/PXQNzIlRkiqe43FqmbxvrSGFh");
		Mockito.verify(encryptAndDecryptUtil, Mockito.atLeastOnce()).decrypt(Mockito.anyString());
	}

	@Test
	public void testSaveForgotPasswordForVallidationException() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailForgotPasswordURL", "url");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "maxPasswords", 2);
		Users users = populateUser();
		List<UsersPasswords> expected = new ArrayList<>();
		expected.add(populateUserPassword());
		Mockito.doReturn("abcdef","password").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn("UserName").when(jwtTokenUtil).getUsernameFromToken(Mockito.anyString());
		Mockito.doReturn(users).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(expected).when(usersPasswordsRepository).findByUserId(Mockito.anyLong());
		Mockito.doReturn(true).when(passwordEncoder).matches(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateEmailResponse()).when(webClientTemplate).postForObjectMono(Mockito.anyString(),
				Mockito.any(), Mockito.any(), Mockito.anyMap());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.saveForgotPassword("String", "String");
		Mockito.verify(jwtTokenUtil,Mockito.atLeastOnce()).getUsernameFromToken(Mockito.anyString());
	}

	@Test
	public void testSaveForgotPasswordWhenPasswordListIsEmpty() {
		PasswordDTOV2 inputDTO = populatePasswordDTOV2();
		List<UsersPasswords> expected = new ArrayList<>();
		inputDTO.setPasswordExpired(true);
		Mockito.doReturn("username","password").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn(populatePasswordDTOV2().getEmail()).when(jwtTokenUtil)
				.getUsernameFromToken(inputDTO.getToken());
		Mockito.doReturn(user).when(userRepository).findByEmail(inputDTO.getEmail());
		Mockito.doReturn(expected).when(usersPasswordsRepository).findByUserId(1l);
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.saveForgotPassword("String", "String");
		Mockito.verify(jwtTokenUtil,Mockito.atLeastOnce()).getUsernameFromToken(Mockito.anyString());
	}

	@Test
	public void testSaveForgotPasswordWhenPwdTokenIsNull() {
		List<UsersPasswords> expected = new ArrayList<>();
		expected.add(populateUserPassword());
		PasswordDTOV2 passwordDTOV2 = new PasswordDTOV2();
		passwordDTOV2.setConfirmPassword("Osicpl@2");
		passwordDTOV2.setEmail("admin@osius.com");
		passwordDTOV2.setToken(null);
		Mockito.doReturn("username","password").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn("username").when(jwtTokenUtil).getUsernameFromToken(Mockito.anyString());
		Mockito.doReturn(populateUsers()).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(expected).when(usersPasswordsRepository).findByUserId(Mockito.anyLong());
		Mockito.doReturn(populateEmailResponse()).when(webClientTemplate).postForObjectMono(Mockito.anyString(),
				Mockito.any(), Mockito.any(), Mockito.anyMap());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.saveForgotPassword("String", "String");
		Mockito.verify(jwtTokenUtil,Mockito.atLeastOnce()).getUsernameFromToken(Mockito.anyString());
	}

	@Test
	public void testSaveForgotPasswordWhenDTOIsNull() {
		passwordDTOV2.setToken("abcd");
		Users user = new Users();
		user.setTokenValue("Bearer abcd-efgh-1234-5678");
		Mockito.doReturn("username","password").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn("username").when(jwtTokenUtil).getUsernameFromToken(Mockito.anyString());
		Mockito.doReturn(null).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doThrow(ValidationException.class).when(usersPasswordsRepository).findByUserId(Mockito.anyLong());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.saveForgotPassword("String", "String");
		Mockito.verify(jwtTokenUtil,Mockito.atLeastOnce()).getUsernameFromToken(Mockito.anyString());
	}

	@Test
	public void testSaveForgotPasswordWhenDTOIsNullWhenUserPasswordIsEmpty() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailForgotPasswordURL", "url");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "maxPasswords", 2);
		Users users = populateUser();
		List<UsersPasswords> expected = new ArrayList<>();
		expected.add(populateUserPassword());
		expected.add(populateUserPassword());
		Mockito.doReturn("username","password").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn("UserName").when(jwtTokenUtil).getUsernameFromToken(Mockito.anyString());
		Mockito.doReturn(users).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(expected).when(usersPasswordsRepository).findByUserId(Mockito.anyLong());
		Mockito.doReturn(false).when(passwordEncoder).matches(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateEmailResponse()).when(webClientTemplate).postForObjectMono(Mockito.anyString(),
				Mockito.any(), Mockito.any(), Mockito.anyMap());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.saveForgotPassword("String", "String");
		Mockito.verify(jwtTokenUtil,Mockito.atLeastOnce()).getUsernameFromToken(Mockito.anyString());
	}

	@Test
	public void testSaveForgotPasswordWhenPasswordIsNull() {
		Mockito.doThrow(BbsiException.class).when(jwtTokenUtil).getUsernameFromToken(Mockito.anyString());
		Mockito.doThrow(BbsiException.class).when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doThrow(BbsiException.class).when(userRepository).findByEmail(Mockito.anyString());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.saveForgotPassword("null", "null");
		Mockito.verify(jwtTokenUtil,Mockito.never()).getUsernameFromToken(Mockito.anyString());
	}

	@Test
	public void testSaveForgotPasswordForValidationException() {
		Users users = populateUser();
		List<UsersPasswords> expected = new ArrayList<>();
		expected.add(populateUserPassword());
		Mockito.doReturn("username","password").when(encryptAndDecryptUtil).decrypt(Mockito.anyString());
		Mockito.doReturn("UserName").when(jwtTokenUtil).getUsernameFromToken(Mockito.anyString());
		Mockito.doReturn(users).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(userPassword).when(usersPasswordsRepository).findByUserId(Mockito.anyLong());
		Mockito.doReturn(true).when(jwtTokenUtil).isTokenExpired(Mockito.anyString());
		Mockito.doReturn(false).when(passwordEncoder).matches(Mockito.any(), Mockito.any());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.saveForgotPassword("String","String");
		Mockito.verify(jwtTokenUtil,Mockito.atLeastOnce()).getUsernameFromToken(Mockito.anyString());
	}

	@Test
	public void testSendFlagValueForExpiredPasswordWhenEmailIsEqual() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "passwordExpirationDays", 91);
		UsersPasswords expected = new UsersPasswords();
		expected.setClientCode("909464");
		expected.setId(1l);
		expected.setModifiedOn(LocalDateTime.now());
		expected.setCreatedOn(LocalDateTime.now().minusDays(50));
		expected.setPassword("Osicpl@2");
		expected.setPasswordIndex(1);
		expected.setUserId(1l);
		expected.setVersion(1l);
		List<UsersPasswords> expectedList = new ArrayList<>();
		expectedList.add(expected);
		PasswordDTOV2 input = new PasswordDTOV2();
		input.setEmail("admin@osius.com");
		input.setConfirmPassword("Osicpl@2");
		input.setNewPassword("Osicpl@2");
		input.setPassword("Osicpl@1");
		input.setDescription("description");
		input.setEmployeeCode("E1786F");
		input.setPasswordExpired(false);
		input.setToken("Bearer 1234-5678-abcd-efgh");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setEmail("admin@osius.com");
		principal.setClientCode(expected.getClientCode());
		principal.setUserId(user.getId());
		user.setEmail("admin@osius.com");
		List<Object[]> clients = populateUserClientsObjectArray();
		Object[] user = { new Timestamp(System.currentTimeMillis()) };
		Mockito.doReturn(user).when(userRepository).findCreatedDateByUserId(Mockito.anyLong());
		Mockito.doReturn(clients).when(userClientRepository).getClientUserName(Mockito.anyString(), Mockito.anyLong());
		Mockito.doReturn(expectedList).when(usersPasswordsRepository).findByUserIdAndPasswordIndex(Mockito.any(),
				Mockito.any());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl
				.sendFlagValueForExpiredPassword("YWRtaW5Ab3NpdXMuY29t", principal);
		assertEquals("admin@osius.com", actualResponse.getEmail());
	}

	@Test
	public void testSendFlagValueForExpiredPasswordWhenUsersPasswordsEmpty() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "passwordExpirationDays", 91);
		UsersPasswords expected = new UsersPasswords();
		expected.setClientCode("909464");
		expected.setId(1l);
		expected.setModifiedOn(LocalDateTime.now());
		expected.setCreatedOn(LocalDateTime.now().minusDays(50));
		expected.setPassword("Osicpl@2");
		expected.setPasswordIndex(1);
		expected.setUserId(1l);
		expected.setVersion(1l);
		List<UsersPasswords> expectedList = new ArrayList<>();
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setEmail("admin@osius.com");
		principal.setClientCode(expected.getClientCode());
		principal.setUserId(user.getId());
		user.setEmail("admin@osius.com");
		List<Object[]> clients = populateUserClientsObjectArray();
		Object[] user = { new Timestamp(System.currentTimeMillis()) };
		Mockito.doReturn(user).when(userRepository).findCreatedDateByUserId(Mockito.anyLong());
		Mockito.doReturn(clients).when(userClientRepository).getClientUserName(Mockito.anyString(), Mockito.anyLong());
		Mockito.doReturn(expectedList).when(usersPasswordsRepository).findByUserIdAndPasswordIndex(Mockito.any(),
				Mockito.any());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl
				.sendFlagValueForExpiredPassword("YWRtaW5Ab3NpdXMuY29t", principal);
		assertEquals("admin@osius.com", actualResponse.getEmail());
	}

	@Test
	public void testSendFlagValueForExpiredPasswordWhenObject() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "passwordExpirationDays", 91);
		UsersPasswords expected = new UsersPasswords();
		expected.setClientCode("909464");
		expected.setId(1l);
		expected.setModifiedOn(LocalDateTime.now());
		expected.setCreatedOn(LocalDateTime.now().minusDays(50));
		expected.setPassword("Osicpl@2");
		expected.setPasswordIndex(1);
		expected.setUserId(1l);
		expected.setVersion(1l);
		List<UsersPasswords> expectedList = new ArrayList<>();
		expectedList.add(expected);
		PasswordDTOV2 input = new PasswordDTOV2();
		input.setEmail("admin@osius.com");
		input.setConfirmPassword("Osicpl@2");
		input.setNewPassword("Osicpl@2");
		input.setPassword("Osicpl@1");
		input.setDescription("description");
		input.setEmployeeCode("E1786F");
		input.setPasswordExpired(false);
		input.setToken("Bearer 1234-5678-abcd-efgh");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setEmail("admin@osius.com");
		principal.setClientCode(expected.getClientCode());
		principal.setUserId(user.getId());
		user.setEmail("admin@osius.com");
		List<Object[]> clients = populateUserClientsObjectArray();
		Object[] user = { null, BigInteger.ONE };
		Mockito.doReturn(user).when(userRepository).findCreatedDateByUserId(Mockito.anyLong());
		Mockito.doReturn(clients).when(userClientRepository).getClientUserName(Mockito.anyString(), Mockito.anyLong());
		Mockito.doReturn(expectedList).when(usersPasswordsRepository).findByUserIdAndPasswordIndex(Mockito.any(),
				Mockito.any());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl
				.sendFlagValueForExpiredPassword("YWRtaW5Ab3NpdXMuY29t", principal);
		assertEquals("admin@osius.com", actualResponse.getEmail());
	}

	@Test
	public void testSendFlagValueForExpiredPasswordWhenUserPrincipalNull() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "passwordExpirationDays", 91);
		List<Object[]> clients = populateUserClientsObjectArray();
		Object[] user = { null, BigInteger.ONE };
		Mockito.doReturn(user).when(userRepository).findCreatedDateByUserId(Mockito.anyLong());
		Mockito.doReturn(clients).when(userClientRepository).getClientUserName(Mockito.anyString(), Mockito.anyLong());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl
				.sendFlagValueForExpiredPassword("YWRtaW5Ab3NpdXMuY29t", null);
		assertNull(actualResponse);
	}

	@Test
	public void testSendFlagValueForExpiredPasswordWhenUserNull() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "passwordExpirationDays", 91);
		List<Object[]> clients = populateUserClientsObjectArray();
		UsersPasswords expected = new UsersPasswords();
		expected.setClientCode("909464");
		expected.setId(1l);
		expected.setModifiedOn(LocalDateTime.now());
		expected.setCreatedOn(LocalDateTime.now().minusDays(50));
		expected.setPassword("Osicpl@2");
		expected.setPasswordIndex(1);
		expected.setUserId(1l);
		expected.setVersion(1l);
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setEmail("admin@osius.com");
		principal.setClientCode(expected.getClientCode());
		Mockito.doReturn(null).when(userRepository).findCreatedDateByUserId(Mockito.anyLong());
		Mockito.doReturn(clients).when(userClientRepository).getClientUserName(Mockito.anyString(), Mockito.anyLong());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl
				.sendFlagValueForExpiredPassword("YWRtaW5Ab3NpdXMuY29t", principal);
		assertNotNull(actualResponse);
	}

	@Test
	public void testSendFlagValueForExpiredPasswordTrue() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "passwordExpirationDays", 91);
		UsersPasswords expected = new UsersPasswords();
		expected.setClientCode("909464");
		expected.setId(1l);
		expected.setModifiedOn(LocalDateTime.now().minusDays(92));
		expected.setCreatedOn(LocalDateTime.now().minusDays(400));
		expected.setPassword("Osicpl@2");
		expected.setPasswordIndex(1);
		expected.setUserId(1l);
		expected.setVersion(1l);
		List<UsersPasswords> expectedList = new ArrayList<>();
		expectedList.add(expected);
		PasswordDTOV2 input = new PasswordDTOV2();
		input.setEmail("admin@osius.com");
		input.setConfirmPassword("Osicpl@2");
		input.setNewPassword("Osicpl@2");
		input.setPassword("Osicpl@1");
		input.setDescription("description");
		input.setEmployeeCode("E1786F");
		input.setPasswordExpired(false);
		input.setToken("Bearer 1234-5678-abcd-efgh");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setEmail("admin@osius.com");
		principal.setClientCode(expected.getClientCode());
		principal.setUserId(user.getId());
		user.setEmail("admin@osius.com");
		Object[] objArray = { Timestamp.valueOf(LocalDateTime.now()) };
		Mockito.doReturn(objArray).when(userRepository).findCreatedDateByUserId(Mockito.anyLong());
		Mockito.doReturn(expectedList).when(usersPasswordsRepository).findByUserIdAndPasswordIndex(Mockito.any(),
				Mockito.any());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl
				.sendFlagValueForExpiredPassword("YWRtaW5Ab3NpdXMuY29t", principal);
		assertEquals("admin@osius.com", actualResponse.getEmail());
	}

	@Test
	public void testSendFlagVallueForExpiredPasswordWhenUserClientForEmpty() {
		UserPrincipal principal = new UserPrincipal(" ", " ", new ArrayList<>());
		principal.setEmail("admin@osius.com");
		principal.setClientCode("909464");
		principal.setUserId(user.getId());
		Object[] objArray = {};
		Mockito.doReturn(objArray).when(userRepository).findCreatedDateByUserId(Mockito.anyLong());
		PasswordDTOV2 response = passwordBusinessServiceImpl.sendFlagValueForExpiredPassword("YWRtaW5Ab3NpdXMuY29t",
				principal);
		assertNull(response.getDescription());
	}

	@Test
	public void testSendFlagValueForExpiredPasswordWhenEmailDiffer() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setEmail(null);
		Mockito.doReturn(null).when(userRepository).findByEmail(Mockito.anyString());
		Mockito.doThrow(ValidationException.class).when(usersPasswordsRepository)
				.findByUserIdAndPasswordIndex(Mockito.any(), Mockito.any());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.sendFlagValueForExpiredPassword("MQ==", principal);
		assertNull(actualResponse);
	}

	@Test
	public void testSendFlagValueForExpiredPasswordForValidation() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setEmail("admin@osius.com");
		Mockito.doThrow(ValidationException.class).when(userRepository).findByEmail(null);
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.sendFlagValueForExpiredPassword("MQ==", principal);
		assertNull(actualResponse);
	}

	@Test
	public void testSendNotificationsForExpiredPassword() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailURL", "/url");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailForgotPasswordURL",
				"http://localhost:8807/notification/v1/email/forgot/");
		userPrincipal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setEmail("test@test.com");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "bbsiHeadEmails", "bbsihq.com,mybbsi.com,bbsi.com");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "passwordExpirationDays", 1);
		List<Users> userDetails = new ArrayList<>();
		userDetails.add(populateUser());
		Object[] JavaObjectArray = { 12l, LocalDateTime.now(), "BBSI", "last", "admin@bbsi.com" };
		Object[] object1 = { 11l, LocalDateTime.now(), "BBSI", "last", "admin@osius.com" };
		List<Object[]> userDeatils = new ArrayList<Object[]>();
		userDeatils.add(JavaObjectArray);
		userDeatils.add(object1);
		List<UsersPasswords> usersPasswords = new ArrayList<>();
		usersPasswords.add(populateUserPassword());
		usersPasswords.get(0).setModifiedOn(LocalDateTime.now().plusDays(2));
		Mockito.doReturn(userDeatils).when(userRepository).getUserIdsAndcreatedOn();
		Mockito.doReturn(usersPasswords).when(usersPasswordsRepository).findByUserIdAndPasswordIndex(Mockito.anyLong(),
				Mockito.any());
		Mockito.doReturn(populateEmailResponse()).when(webClientTemplate).postForObjectMono(Mockito.anyString(),
				Mockito.any(), Mockito.any(), Mockito.anyMap());
		List<String> actualResponse = passwordBusinessServiceImpl.sendNotificationsForExpiredPassword(userPrincipal,
				"/expiredPassword", "Bearer 1234-abcd-4321-dcba");
		assertNotNull(actualResponse);
	}
	
	@Test
	public void testSendNotificationsForExpiredPasswordForsendNotificationsToEmailsIfBlock() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailURL", "/url");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "emailForgotPasswordURL",
				"http://localhost:8807/notification/v1/email/forgot/");
		userPrincipal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setEmail("test@test.com");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "bbsiHeadEmails", "bbsihq.com,mybbsi.com,bbsi.com");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "passwordExpirationDays", 0);
		List<Users> userDetails = new ArrayList<>();
		userDetails.add(populateUser());
		Object[] JavaObjectArray = { 12l, LocalDateTime.now(), "BBSI", "last", "admin@bbsi.com" };
		Object[] object1 = { 11l, LocalDateTime.now(), "BBSI", "last", "admin@osius.com" };
		List<Object[]> userDeatils = new ArrayList<Object[]>();
		userDeatils.add(JavaObjectArray);
		userDeatils.add(object1);
		List<UsersPasswords> usersPasswords = new ArrayList<>();
		usersPasswords.add(populateUserPassword());
		//usersPasswords.get(0).setModifiedOn(LocalDateTime.now());
		Mockito.doReturn(userDeatils).when(userRepository).getUserIdsAndcreatedOn();
		Mockito.doReturn(usersPasswords).when(usersPasswordsRepository).findByUserIdAndPasswordIndex(Mockito.anyLong(),
				Mockito.any());
		Mockito.doReturn(populateEmailResponse()).when(webClientTemplate).postForObjectMono(Mockito.anyString(),
				Mockito.any(), Mockito.any(), Mockito.anyMap());
		List<String> actualResponse = passwordBusinessServiceImpl.sendNotificationsForExpiredPassword(userPrincipal,
				"/expiredPassword", "Bearer 1234-abcd-4321-dcba");
		assertNotNull(actualResponse);
	}


	@Test
	public void testSendNotificationsForExpiredPasswordUserObjectsNull() {
		userPrincipal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setEmail("test@test.com");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "bbsiHeadEmails", "bbsihq.com,mybbsi.com,bbsi.com");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "passwordExpirationDays", 1);
		List<Users> userDetails = new ArrayList<>();
		userDetails.add(populateUser());
		Object[] JavaObjectArray = { 123, null, null, null, null };
		Object[] object1 = { 1, LocalDateTime.now(), "BBSI", "last", "admin@osius.com" };
		List<Object[]> userDeatils = new ArrayList<Object[]>();
		userDeatils.add(JavaObjectArray);
		userDeatils.add(object1);
		List<UsersPasswords> usersPasswords = new ArrayList<>();
		usersPasswords.add(populateUserPassword());
		usersPasswords.get(0).setModifiedOn(LocalDateTime.now().plusDays(2));
		Mockito.doReturn(userDeatils).when(userRepository).getUserIdsAndcreatedOn();
		Mockito.doReturn(usersPasswords).when(usersPasswordsRepository).findByUserIdAndPasswordIndex(Mockito.anyLong(),
				Mockito.any());
		Mockito.doReturn(populateEmailResponse()).when(webClientTemplate).postForObjectMono(Mockito.anyString(),
				Mockito.any(), Mockito.any(), Mockito.any());
		List<String> actualResponse = passwordBusinessServiceImpl.sendNotificationsForExpiredPassword(userPrincipal,
				"/expiredPassword", "Bearer 1234-abcd-4321-dcba");
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testSendNotificationsForExpiredPasswordWhenUserDeatilsEmpty() {
		userPrincipal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setEmail("test@test.com");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "bbsiHeadEmails", "bbsihq.com,mybbsi.com,bbsi.com");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "passwordExpirationDays", 1);
		List<Users> userDetails = new ArrayList<>();
		userDetails.add(populateUser());
		List<Object[]> userDeatils = new ArrayList<Object[]>();
		List<UsersPasswords> usersPasswords = new ArrayList<>();
		usersPasswords.add(populateUserPassword());
		usersPasswords.get(0).setModifiedOn(LocalDateTime.now().plusDays(2));
		Mockito.doReturn(userDeatils).when(userRepository).getUserIdsAndcreatedOn();
		Mockito.doReturn(usersPasswords).when(usersPasswordsRepository).findByUserIdAndPasswordIndex(Mockito.anyLong(),
				Mockito.any());
		Mockito.doReturn(populateEmailResponse()).when(webClientTemplate).postForObjectMono(Mockito.anyString(),
				Mockito.any(), Mockito.any(), Mockito.any());
		List<String> actualResponse = passwordBusinessServiceImpl.sendNotificationsForExpiredPassword(userPrincipal,
				"/expiredPassword", "Bearer 1234-abcd-4321-dcba");
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testSendNotificationsForExpiredPasswordForParseException() {
		userPrincipal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setEmail("test@test.com");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "bbsiHeadEmails", "bbsihq.com,mybbsi.com,bbsi.com");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "passwordExpirationDays", 1);
		List<Users> userDetails = new ArrayList<>();
		userDetails.add(populateUser());
		Object[] JavaObjectArray = { 12l, "abcde", "BBSI", "last", "admin@bbsi.com" };
		Object[] object1 = { 11l, "1234", "BBSI", "last", "admin@osius.com" };
		List<Object[]> userDeatils = new ArrayList<Object[]>();
		userDeatils.add(JavaObjectArray);
		userDeatils.add(object1);
		List<UsersPasswords> usersPasswords = new ArrayList<>();
		usersPasswords.add(populateUserPassword());
		usersPasswords.get(0).setModifiedOn(LocalDateTime.now().plusDays(2));
		Mockito.doReturn(userDeatils).when(userRepository).getUserIdsAndcreatedOn();
		List<String> actualResponse = passwordBusinessServiceImpl.sendNotificationsForExpiredPassword(userPrincipal,
				"/expiredPassword", "token");
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testSendNotificationsForExpiredPasswordWhenTokenEmpty() {
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "bbsiHeadEmails", "bbsihq.com,mybbsi.com,bbsi.com");
		ReflectionTestUtils.setField(passwordBusinessServiceImpl, "passwordExpirationDays", 1);
		List<Users> userDetails = new ArrayList<>();
		userDetails.add(populateUser());
		Object[] JavaObjectArray = { 12l, LocalDateTime.now(), "BBSI", "last", "admin@bbsi.com" };
		Object[] object1 = { 11l, LocalDateTime.now(), "BBSI", "last", "admin@osius.com" };
		List<Object[]> userDeatils = new ArrayList<Object[]>();
		userDeatils.add(JavaObjectArray);
		userDeatils.add(object1);
		List<UsersPasswords> usersPasswords = new ArrayList<>();
		usersPasswords.add(populateUserPassword());
		usersPasswords.get(0).setModifiedOn(LocalDateTime.now().plusDays(2));
		Mockito.doReturn(userDeatils).when(userRepository).getUserIdsAndcreatedOn();
		Mockito.doReturn(usersPasswords).when(usersPasswordsRepository).findByUserIdAndPasswordIndex(Mockito.anyLong(),
				Mockito.any());
		Mockito.doReturn(populateEmailResponse()).when(webClientTemplate).postForObjectMono(Mockito.anyString(),
				Mockito.any(), Mockito.any(), Mockito.any());
		List<String> actualResponse = passwordBusinessServiceImpl.sendNotificationsForExpiredPassword(userPrincipal,
				"/expiredPassword", "");
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testValidateJwtToken() {
		PasswordDTOV2 input = populatePasswordDTOV2();
		input.setPasswordExpired(true);
		input.setToken("token1");
		Mockito.doReturn(populatePasswordDTOV2().getEmail()).when(jwtTokenUtil).getUsernameFromToken(Mockito.any());
		Mockito.doReturn(populateUser()).when(userRepository).findByEmail(Mockito.any());
		Mockito.doReturn(false).when(jwtTokenUtil).isTokenExpired(Mockito.any());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.validateJwtToken(input);
		assertEquals(actualResponse.getConfirmPassword(), input.getConfirmPassword());
	}

	@Test
	public void testValidateJwtTokenWhenTrue() {
		Mockito.doReturn(true).when(jwtTokenUtil).isTokenExpired(Mockito.anyString());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.validateJwtToken(populatePasswordDTOV2());
		assertEquals(actualResponse.getConfirmPassword(), populatePasswordDTOV2().getConfirmPassword());
	}

	@Test
	public void testValidateJwtTokenForExceptions() {
		Mockito.doReturn(populatePasswordDTOV2().getEmail()).when(jwtTokenUtil).getUsernameFromToken(Mockito.any());
		Mockito.doReturn(null).when(userRepository).findByEmail(Mockito.any());
		Mockito.doReturn(true).when(jwtTokenUtil).isTokenExpired(Mockito.any());
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.validateJwtToken(populatePasswordDTOV2());
		assertEquals(actualResponse.getConfirmPassword(), populatePasswordDTOV2().getConfirmPassword());
	}

	@Test
	public void testValidateJwtTokenForOtherExceptions() {
		PasswordDTOV2 actualResponse = passwordBusinessServiceImpl.validateJwtToken(null);
		assertNull(actualResponse);
	}

	@Test
	public void testremoveToken() {
		OAuth2AccessToken token = null;
		tokenStore.readAccessToken("1234-5678-abcd-efgh");
		tokenStore.readAuthentication((OAuth2AccessToken) token);
		tokenStore.readAuthenticationForRefreshToken((OAuth2RefreshToken) token);
		tokenStore.readRefreshToken("2345-6789-bcde-fghi");
		tokenStore.readAuthentication("1234-5678-abcd-efgh");
		passwordBusinessServiceImpl.removeToken(tokenStore, false);
		assertTrue(true);
	}
	

	@Test
	public void testgetUserToken() {
		String jwtTokenData = " ";
		Users userData = new Users();
		userData.setAuthenticationType("Bearer");
		userData.setEmail("admin@bbsi.com");
		userData.setId(1l);
		userData.setInvalidAttempts(0);
		userData.setIsFirstLogin(false);
		userData.setIsPolicyAccepted(false);
		userData.setIsPolicyUpdated(false);
		userData.setPassword("Osicpl@2");
		userData.setTokenValue("1234-5678-abcd-efgh");
		User user = new User(userData.getEmail(), "", new ArrayList<>());
		Mockito.doReturn(jwtTokenData).when(jwtTokenUtil).generateToken(user, true, 1l);
		Mockito.doReturn(userData).when(userRepository).findByEmail(Mockito.any());
		String result = passwordBusinessServiceImpl.getUserToken("admin@bbsi.com");
		assertEquals(null, result);
	}

	@Test
	public void testGetUserTokenForEmailEmpty() {
		Users userData = new Users();
		userData.setAuthenticationType("Bearer");
		userData.setEmail(" ");
		userData.setId(1l);
		userData.setInvalidAttempts(0);
		userData.setIsFirstLogin(false);
		userData.setIsPolicyAccepted(false);
		userData.setIsPolicyUpdated(false);
		userData.setPassword("Osicpl@2");
		userData.setTokenValue("1234-5678-abcd-efgh");
		User user = new User(userData.getEmail(), "", new ArrayList<>());
		Mockito.doThrow(BbsiException.class).when(jwtTokenUtil).generateToken(user, true, 0l);
		String result = passwordBusinessServiceImpl.getUserToken(" ");
		assertEquals(null, result);
	}

	@Test
	public void testGetUserTokenForNullEmail() {
		Mockito.doThrow(BbsiException.class).when(jwtTokenUtil).generateToken(null, false, 0l);
		String result = passwordBusinessServiceImpl.getUserToken(null);
		assertEquals(null, result);
	}

	private PasswordDTOV2 populatePasswordDTOV2() {
		PasswordDTOV2 data = new PasswordDTOV2();
		data.setConfirmPassword("Osicpl@0");
		data.setDescription("Password");
		data.setEmail(populateUsers().getEmail());
		Set<String> emails = new HashSet<>();
		emails.add(populateUsers().getEmail());
		Map<String, Set<String>> emailMap = new HashMap<>();
		emailMap.put(GenericConstants.MEMBER_INFO_EMAIL.toString(), emails);
		data.setEmailMap(emailMap);
		data.setEmployeeCode("E1786F");
		data.setFirstName("first");
		data.setLastName("last");
		data.setName("first last");
		data.setNewPassword("Osicpl@0");
		data.setPassword("Osicpl@2");
		data.setPasswordExpired(false);
		data.setToken("Bearer 12jkkj-1231d-as123");
		data.setUserId(1l);
		return data;
	}

	private Users populateUser() {
		Users users = new Users();
		users.setId(12l);
		users.setEmail("test@test.com");
		users.setPassword("Osicpl@2");
		users.setClients(populateUserClients());
		users.setTokenValue("Bearer 12jkkj-1231d-as123");
		return users;
	}

	private Users populateUserForCilentsEmpty() {
		Users users = new Users();
		users.setId(12l);
		users.setEmail("test@test.com");
		users.setPassword("Osicpl@2");
		List<UserClients> listUserClients = new ArrayList<UserClients>();
		users.setClients(listUserClients);
		users.setTokenValue("Bearer 12jkkj-1231d-as123");
		return users;
	}

	private List<UserClients> populateUserClients() {
		List<UserClients> listUserClients = new ArrayList<UserClients>();
		UserClients clients = new UserClients();
		clients.setClientCode("909090");
		clients.setFirstName("testBbsi");
		clients.setLastName("last");
		clients.setMobile("9090909909");
		clients.setIsActive(true);
		clients.setEmployeeCode("E71624");
		clients.setIsPrimary(true);
		Users user = new Users();
		user.setEmail("admin@osius.com");
		user.setPassword("Osicpl@2");
		clients.setUser(user);
		clients.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		listUserClients.add(clients);
		return listUserClients;
	}

	private List<Object[]> populateUserClientsObjectArray() {
		List<Object[]> list = new ArrayList<>();
		Object[] objArray = { "first", "last" };
		list.add(objArray);
		return list;
	}

	private UsersPasswords populateUserPassword() {
		UsersPasswords userPass = new UsersPasswords();
		userPass.setModifiedOn(LocalDateTime.now());
		userPass.setId(1l);
		userPass.setPassword("Osicpl@2");
		userPass.setClientCode("909464");
		userPass.setUserId(12l);
		userPass.setPasswordIndex(6);
		return userPass;
	}

	private Users populateUsers() {
		Users users = new Users();
		users.setAuthenticationType("Bearer");
		users.setEmail("test@test.com");
		users.setId(1l);
		users.setInvalidAttempts(0);
		users.setIsFirstLogin(false);
		users.setIsPolicyAccepted(true);
		users.setIsPolicyUpdated(false);
		users.setPassword("Osicpl@2");
		users.setTokenValue("token");
		users.setVersion(1);
		List<UserClients> clients = new ArrayList<>();
		UserClients client = new UserClients();
		client.setUser(users);
		client.setClientCode("909464");
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setClientName("1 Hour Drain");
		clientMaster.setCostCenterCode("costCenterCode1");
		clientMaster.setCostCenterDescription("description");
		client.setClient(clientMaster);
		client.setEmployeeCode("E1768F");
		client.setI9Approver(false);
		client.setId(1l);
		client.setIsActive(true);
		client.setIsPrimary(true);
		client.setNewHireId(1l);
		client.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		client.setVersion(1l);
		clients.add(client);
		users.setClients(clients);
		List<RbacEntity> roles = new ArrayList<>();
		RbacEntity role = new RbacEntity();
		role.setClientCode("909464");
		role.setCode("code");
		role.setDescription("description");
		role.setIsClientTemplate(true);
		role.setMappingId(1);
		role.setName("name");
		role.setParentId(0l);
		role.setStatus(true);
		role.setType(GenericConstants.USERTYPE_CLIENT.toString());
		role.setVersion(1l);
		users.setRoles(roles);
		users.setPassword("password");
		return users;
	}

	private Mono<Email> populateEmailResponse() {
		Email data = new Email();
		data.setContext("context");
		data.setDbTemplate(false);
		data.setFrom("no-reply@mybbsi.com");
		data.setReplyTo("admin@mybbsi.com");
		data.setSubject("subject");
		data.setTemplate(MethodNames.SUBMIT_EMAIL_FOR_RESET_PASSWRD.toString());
		data.setTemplateName("submit email for reset password");
		data.setToAddress("admin@osius.com");
		Mono<Email> result = Mono.just(data);
		return result;
	}
}
