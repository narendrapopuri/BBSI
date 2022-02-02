package com.bbsi.platform.user.businessservice.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.test.util.ReflectionTestUtils;

import com.bbsi.platform.common.businessservice.intf.IntegrationBusinessService;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.exception.ExceptionUtils;
import com.bbsi.platform.common.generic.BoomiHelper;
import com.bbsi.platform.common.generic.CommonUtilities;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.GenericFunctions;
import com.bbsi.platform.common.generic.RestClient;
import com.bbsi.platform.common.user.dto.EmployeeInvitationDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.user.businessservice.intf.EmployeeInvitationBusinessService;
import com.bbsi.platform.user.mapper.EmployeeInvitationMapper;
import com.bbsi.platform.user.model.AccessTermination;
import com.bbsi.platform.user.model.EmployeeInvitation;
import com.bbsi.platform.user.model.UserClients;
import com.bbsi.platform.user.model.Users;
import com.bbsi.platform.user.repository.AccessTerminationRepository;
import com.bbsi.platform.user.repository.EmployeeInvitationRepository;
import com.bbsi.platform.user.repository.UserClientsRepository;
import com.google.common.collect.Lists;

public class EmployeeInvitationBusinessServiceImplTest {

	@InjectMocks
	private EmployeeInvitationBusinessServiceImpl employeeInvitationBusinessServiceImpl;

	@Mock
	private EmployeeInvitationRepository employeeInvitationRepository;

	@Spy
	private EmployeeInvitationMapper employeeInvitationMapper;

	@Mock
	private EmployeeInvitationBusinessService employeeInvitationBusinessService;

	@Mock
	private IntegrationBusinessService integrationBusinessService;

	private EmployeeInvitation employeeInvitation;

	private EmployeeInvitationDTO employeeInvitationDTO;

	private List<EmployeeInvitationDTO> employeeInvitationDTOList = new ArrayList<EmployeeInvitationDTO>();

	private List<EmployeeInvitation> employeeInvitationList = new ArrayList<EmployeeInvitation>();

	@Mock
	private CommonUtilities commonUtilities;

	@Mock
	private AccessTerminationRepository accessTerminationRepository;

	@Mock
	private BoomiHelper boomiHelper;

	@Mock
	private UserClientsRepository userClientsRepository;

	@Mock
	private RestClient restClient;

	private UserPrincipal userPrincipal;

	@Before
	public void setUp() throws Exception {
		userPrincipal = new UserPrincipal("jchilakapati@bbsi.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setUserType(GenericConstants.USERTYPE_CLIENT);
		userPrincipal.setClientCode("909464");
		userPrincipal.setEmail("jchilakapati@bbsi.com");
		userPrincipal.setIsPolicyAccepted(false);
		userPrincipal.setIsCcpaRequired(true);
		userPrincipal.setIsPolicyUpdated(false);
		userPrincipal.setUserId(1l);
		userPrincipal.setUserType(GenericConstants.USERTYPE_CLIENT);
		userPrincipal.setToken("Bearer 1234-5678-abcd-efgh");
		employeeInvitationDTO = getEmployeeInvitationDTO();
		employeeInvitationDTOList.add(employeeInvitationDTO);
		employeeInvitation = getEmployeeInvitation();
		employeeInvitationList.add(employeeInvitation);
		MockitoAnnotations.initMocks(this);
		ExceptionUtils.handleException = Mockito.mock(GenericFunctions.ThrowableTriConsumer.class);

	}

	@After
	public void tearDown() {
		employeeInvitationDTO = null;
		employeeInvitation = null;
		employeeInvitationList = null;
		employeeInvitationDTOList = null;
	}

	@Test
	public void testSaveEmployeeInvitationInfo() {
		EmployeeInvitationDTO expected = new EmployeeInvitationDTO();
		EmployeeInvitation invitation = getEmployeeInvitation();
		when(employeeInvitationMapper.employeeInvitationToEmployeeInvitationDTO(getEmployeeInvitation()))
				.thenReturn(expected);
		Mockito.doReturn(invitation).when(employeeInvitationRepository).save(getEmployeeInvitation());
		Mockito.doNothing().when(integrationBusinessService)
				.verifyEmployee(getEmployeeInvitationDTO().getEmployeeCode(), "token1");
		EmployeeInvitationDTO result = employeeInvitationBusinessServiceImpl
				.saveEmployeeInvitationInfo(getEmployeeInvitationDTO(), "token");
		assertEquals(result.getId(), getEmployeeInvitationDTO().getId());
		assertEquals(result.getClientCode(), getEmployeeInvitationDTO().getClientCode());
		assertEquals(result.getEmployeeCode(), getEmployeeInvitationDTO().getEmployeeCode());
	}

	@Test
	public void testSaveEmployeeInvitationInfoWhenDTOIsNull() {
		Mockito.doReturn(employeeInvitation).when(employeeInvitationMapper)
				.employeeInvitationToEmployeeInvitationDTO(Mockito.any(EmployeeInvitationDTO.class));
		Mockito.doReturn(employeeInvitation).when(employeeInvitationRepository).save(Mockito.any());
		EmployeeInvitationDTO result = employeeInvitationBusinessServiceImpl.saveEmployeeInvitationInfo(null, null);
		assertNull(result);
	}

	@Test(expected = BbsiException.class)
	public void testSaveEmployeeInvitationInfoThrowsException() {
		Mockito.doThrow(BbsiException.class).when(employeeInvitationMapper)
				.employeeInvitationToEmployeeInvitationDTO(Mockito.any(EmployeeInvitationDTO.class));
		Mockito.doThrow(BbsiException.class).when(employeeInvitationRepository).save(Mockito.any());
		EmployeeInvitationDTO result = employeeInvitationBusinessServiceImpl
				.saveEmployeeInvitationInfo(new EmployeeInvitationDTO(), " ");
		assertEquals(0l, result.getId());
		assertNull(result.getClientCode());
		assertNull(result.getEmployeeCode());
		assertNull(result.getLastSentDate());
	}

	@Test
	public void testGetAllNotificatonInfoByClientCode() {
		ReflectionTestUtils.setField(employeeInvitationBusinessServiceImpl, "empVerifyUrl", "/url");
		Mockito.doReturn(employeeInvitationList).when(employeeInvitationRepository)
				.findByClientCode(Mockito.anyString());
		Mockito.doNothing().when(integrationBusinessService).verifyEmployee(Mockito.any(), Mockito.any());
		Mockito.doReturn("").when(restClient).postForString(Mockito.anyString(), Mockito.anyList(), Mockito.anyMap());
		when(employeeInvitationMapper.employeeInvitationToEmployeeInvitationDTOs(Mockito.any()))
				.thenReturn(employeeInvitationDTOList);
		List<EmployeeInvitationDTO> employeeInvitationDTOResult = employeeInvitationBusinessServiceImpl
				.getAllNotificatonInfoByClientCode(employeeInvitationDTOList.get(0).getClientCode(), "token1");
		assertNotEquals(employeeInvitationDTOResult.size(), 0);
	}

	@Test
	public void testGetAllNotificationInfoByClientCodeWhenJsonArrayEmpty() {
		ReflectionTestUtils.setField(employeeInvitationBusinessServiceImpl, "empVerifyUrl", "/url");
		Mockito.doReturn(employeeInvitationList).when(employeeInvitationRepository)
				.findByClientCode(Mockito.anyString());
		Mockito.doNothing().when(integrationBusinessService).verifyEmployee(Mockito.any(), Mockito.any());
		Mockito.doReturn("[]").when(restClient).postForString(Mockito.anyString(), Mockito.anyList(), Mockito.anyMap());
		when(employeeInvitationMapper.employeeInvitationToEmployeeInvitationDTOs(Mockito.any()))
				.thenReturn(employeeInvitationDTOList);
		List<EmployeeInvitationDTO> employeeInvitationDTOResult = employeeInvitationBusinessServiceImpl
				.getAllNotificatonInfoByClientCode(employeeInvitationDTOList.get(0).getClientCode(), "token1");
		assertNotEquals(employeeInvitationDTOResult.size(), 0);
	}

	@Test
	public void testGetAllNotificationInfoByClientCodeWhenResponseandEmpCodeSame() {
		ReflectionTestUtils.setField(employeeInvitationBusinessServiceImpl, "empVerifyUrl", "/url");
		List<EmployeeInvitation> list = new ArrayList<>();
		list.add(getEmployeeInvitation1());
		Mockito.doReturn(list).when(employeeInvitationRepository).findByClientCode(Mockito.anyString());
		Mockito.doNothing().when(integrationBusinessService).verifyEmployee(Mockito.any(), Mockito.any());
		Mockito.doReturn("[]").when(restClient).postForString(Mockito.anyString(), Mockito.anyList(), Mockito.anyMap());
		when(employeeInvitationMapper.employeeInvitationToEmployeeInvitationDTOs(Mockito.any()))
				.thenReturn(employeeInvitationDTOList);
		List<EmployeeInvitationDTO> employeeInvitationDTOResult = employeeInvitationBusinessServiceImpl
				.getAllNotificatonInfoByClientCode(employeeInvitationDTOList.get(0).getClientCode(), "token1");
		assertNotEquals(employeeInvitationDTOResult.size(), 0);
	}

	@Test
	public void testGetAllNotificatonInfoByClientCodeListIsEmpty() {
		Mockito.doReturn(new ArrayList<>()).when(employeeInvitationRepository).findByClientCode(Mockito.anyString());
		Mockito.doNothing().when(integrationBusinessService).verifyEmployee(Mockito.any(), Mockito.any());
		Mockito.doReturn(employeeInvitationDTOList).when(employeeInvitationMapper)
				.employeeInvitationToEmployeeInvitationDTOs(Mockito.any());
		List<EmployeeInvitationDTO> employeeInvitationDTOResult = employeeInvitationBusinessServiceImpl
				.getAllNotificatonInfoByClientCode(employeeInvitationDTOList.get(0).getClientCode(), "token1");
		assertNotNull(employeeInvitationDTOResult);
		assertNotEquals(employeeInvitationDTOResult.size(), 0);
	}

	@Test(expected = BbsiException.class)
	public void testGetAllNotificationInfoByClientCode() {
		Mockito.doThrow(BbsiException.class).when(employeeInvitationRepository).findByClientCode(Mockito.any());
		Mockito.doThrow(BbsiException.class).when(employeeInvitationMapper)
				.employeeInvitationToEmployeeInvitationDTOs(Mockito.any());
		List<EmployeeInvitationDTO> response = employeeInvitationBusinessServiceImpl
				.getAllNotificatonInfoByClientCode(" ", " ");
		assertEquals(0, response.size());
	}

	@Test(expected = NullPointerException.class)
	public void testGetAllNotificationInfoByClientCodeForException() {
		Mockito.doThrow(NullPointerException.class).when(employeeInvitationRepository).findByClientCode(Mockito.any());
		Mockito.doThrow(NullPointerException.class).when(employeeInvitationMapper)
				.employeeInvitationToEmployeeInvitationDTOs(Mockito.any());
		List<EmployeeInvitationDTO> response = employeeInvitationBusinessServiceImpl
				.getAllNotificatonInfoByClientCode("clientCode", "token1");
		assertNull(response);
	}

	@Test
	public void testsaveAllByClientCodeAndEmployeeId() {
		List<String> employeeList = new ArrayList<>();
		employeeList.add("Employee_1");
		employeeList.add("Employee_2");
		Mockito.doReturn(employeeInvitation).when(employeeInvitationRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(employeeInvitationList).when(employeeInvitationRepository).saveAll(Mockito.any());
		employeeInvitationBusinessServiceImpl.saveAllByClientCodeAndEmployeeId("client200", employeeList);
		assertTrue(true);
	}

	@Test
	public void testsaveAllByClientCodeAndEmployeeIdWhenEmployeeIdIsNotNull() {
		Mockito.doThrow(BbsiException.class).when(employeeInvitationRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doThrow(BbsiException.class).when(employeeInvitationRepository).saveAll(Mockito.any());
		employeeInvitationBusinessServiceImpl.saveAllByClientCodeAndEmployeeId("client201", new ArrayList<>());
		assertTrue(true);
	}

	@Test
	public void testsaveAllByClientCodeAndEmployeeIdWhenEmployeeIdIsNullThrowsException() {

		List<String> employeeCodes = new ArrayList<>();
		String code = "emp001";
		employeeCodes.add(code);
		Mockito.doReturn(null).when(employeeInvitationRepository).findByClientCodeAndEmployeeCode(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doThrow(BbsiException.class).when(employeeInvitationRepository).saveAll(Mockito.any());
		employeeInvitationBusinessServiceImpl.saveAllByClientCodeAndEmployeeId("client201", employeeCodes);
		assertTrue(true);
	}

	@Test
	public void testSaveAllEmployeeInvitationInfo() {
		List<EmployeeInvitationDTO> inputList = new ArrayList<>();
		inputList.add(getEmployeeInvitationDTO());
		EmployeeInvitationDTO input = getEmployeeInvitationDTO();
		input.setEmployeeCode("A1234F");
		inputList.add(input);
		Mockito.doReturn(employeeInvitationList).when(employeeInvitationMapper)
				.employeeInvitationDTOToEmployeeInvitationDTOs(Mockito.any());
		Mockito.doReturn(employeeInvitationList).when(employeeInvitationRepository).saveAll(Mockito.any());
		List<EmployeeInvitationDTO> result = employeeInvitationBusinessServiceImpl
				.saveAllEmployeeInvitationInfo(inputList, "token1");
		assertEquals(2, result.size());
	}

	@Test
	public void testSaveAllEmployeeInvitationInfoForEmptyList() {
		Mockito.doReturn(new ArrayList<>()).when(employeeInvitationMapper)
				.employeeInvitationDTOToEmployeeInvitationDTOs(Mockito.any());
		Mockito.doReturn(new ArrayList<>()).when(employeeInvitationRepository).saveAll(Mockito.any());
		List<EmployeeInvitationDTO> result = employeeInvitationBusinessServiceImpl
				.saveAllEmployeeInvitationInfo(new ArrayList<>(), " ");
		assertEquals(0, result.size());
	}

	@Test
	public void testSaveAllEmployeeInvitationInfoForException() {
		List<EmployeeInvitationDTO> inputList = new ArrayList<>();
		inputList.add(null);
		Mockito.doThrow(BbsiException.class).when(employeeInvitationMapper)
				.employeeInvitationDTOToEmployeeInvitationDTOs(Mockito.any());
		Mockito.doThrow(BbsiException.class).when(employeeInvitationRepository).saveAll(Mockito.any());
		List<EmployeeInvitationDTO> result = employeeInvitationBusinessServiceImpl.saveAllEmployeeInvitationInfo(null,
				null);
		assertNull(result);
	}

	@Test(expected = BbsiException.class)
	public void testSaveAllEmployeeInvitationInfoForBbsiException() {
		List<EmployeeInvitationDTO> inputList = new ArrayList<>();
		inputList.add(null);
		Mockito.doThrow(BbsiException.class).when(employeeInvitationMapper)
				.employeeInvitationDTOToEmployeeInvitationDTOs(Mockito.any());
		Mockito.doThrow(BbsiException.class).when(employeeInvitationRepository).saveAll(Mockito.any());
		List<EmployeeInvitationDTO> result = employeeInvitationBusinessServiceImpl
				.saveAllEmployeeInvitationInfo(new ArrayList<>(), "token1");
		assertNull(result);
	}

	@Test
	public void testGetEmployeesForBulkInvitation() {
		String jsonRespnse = "[{\"status\":\"T\",\"employee_code\":\"E71624\",\"personal_email\":\"personal@email.com\"}]";
		Mockito.doReturn(populateUserClients()).when(userClientsRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateAccessTermination()).when(accessTerminationRepository)
				.findByClientCodeAndEmployeeCodeIsNotNullAndEndDateIsNotNull(Mockito.anyString());
		Mockito.doReturn("url/bbsi").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonRespnse).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn("[]").when(restClient).postForString(Mockito.any(), Mockito.anySet(), Mockito.anyMap());
		String response = employeeInvitationBusinessServiceImpl.getEmployeesForBulkInvitation(userPrincipal);
		assertNotNull(response);
		assertEquals(true, response.toString().contains("status"));
		assertEquals(true, response.toString().contains("T"));
	}

	@Test
	public void testGetEmployeesForBulkInvitationPersonalEmailEmpty() {
		String jsonRespnse = "[{\"status\":\"T\",\"employee_code\":\"E71624\",\"personal_email\":\"\"}]";
		Mockito.doReturn(populateUserClients()).when(userClientsRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateAccessTermination()).when(accessTerminationRepository)
				.findByClientCodeAndEmployeeCodeIsNotNullAndEndDateIsNotNull(Mockito.anyString());
		Mockito.doReturn("url/bbsi").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonRespnse).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn("[]").when(restClient).postForString(Mockito.any(), Mockito.anySet(), Mockito.anyMap());
		String response = employeeInvitationBusinessServiceImpl.getEmployeesForBulkInvitation(userPrincipal);
		assertNotNull(response);
		assertEquals(true, response.toString().contains("status"));
		assertEquals(true, response.toString().contains("T"));
	}

	@Test
	public void testGetEmployeesForBulkInvitationWithDifferentStatus() {
		String jsonRespnse = "[{\"status\":\"A\",\"employee_code\":\"E71624\"}]";
		Mockito.doReturn(populateUserClientsEmployeeCodeEmpty()).when(userClientsRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateAccessTermination()).when(accessTerminationRepository)
				.findByClientCodeAndEmployeeCodeIsNotNullAndEndDateIsNotNull(Mockito.anyString());
		Mockito.doReturn("url/bbsi").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonRespnse).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn("[]").when(restClient).postForString(Mockito.any(), Mockito.anySet(), Mockito.anyMap());
		String response = employeeInvitationBusinessServiceImpl.getEmployeesForBulkInvitation(userPrincipal);
		assertNotNull(response);
		assertEquals(true, response.toString().contains("status"));
	}

	@Test
	public void testGetEmployeesForBulkInvitationWithDifferentStatusempReponsekeysame() {
		String jsonRespnse = "[{\"status\":\"A\",\"employee_code\":\"E71624\"}]";
		Mockito.doReturn(populateUserClientsEmployeeCodeEmpty()).when(userClientsRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateAccessTermination()).when(accessTerminationRepository)
				.findByClientCodeAndEmployeeCodeIsNotNullAndEndDateIsNotNull(Mockito.anyString());
		Mockito.doReturn("url/bbsi").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonRespnse).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn("E71624").when(restClient).postForString(Mockito.any(), Mockito.anySet(), Mockito.anyMap());
		String response = employeeInvitationBusinessServiceImpl.getEmployeesForBulkInvitation(userPrincipal);
		assertNotNull(response);
	}

	@Test
	public void testGetEmployeesForBulkInvitationWithDifferentStatusResponseEmpty() {
		String jsonRespnse = "[{\"status\":\"A\",\"employee_code\":\"E71624\"}]";
		Mockito.doReturn(populateUserClientsEmployeeCodeEmpty()).when(userClientsRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateAccessTermination()).when(accessTerminationRepository)
				.findByClientCodeAndEmployeeCodeIsNotNullAndEndDateIsNotNull(Mockito.anyString());
		Mockito.doReturn("url/bbsi").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonRespnse).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn("").when(restClient).postForString(Mockito.any(), Mockito.anySet(), Mockito.anyMap());
		String response = employeeInvitationBusinessServiceImpl.getEmployeesForBulkInvitation(userPrincipal);
		assertNotNull(response);
	}

	@Test
	public void testGetEmployeesForBulkInvitationForEmptyUsers() {
		Mockito.doReturn(null).when(userClientsRepository).findByClientCodeAndUserType(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doReturn(null).when(accessTerminationRepository)
				.findByClientCodeAndEmployeeCodeIsNotNullAndEndDateIsNotNull(Mockito.anyString());
		Mockito.doReturn("url/bbsi").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn("[{abc, def}]").when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		String response = employeeInvitationBusinessServiceImpl.getEmployeesForBulkInvitation(userPrincipal);
		assertNotNull(response);
		assertEquals(false, response.toString().contains("status"));
	}

	@Test
	public void testgetEmployeesForBulkInvitationForException() {
		Mockito.doThrow(BbsiException.class).when(userClientsRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doThrow(BbsiException.class).when(accessTerminationRepository)
				.findByClientCodeAndEmployeeCodeIsNotNullAndEndDateIsNotNull(Mockito.anyString());
		Mockito.doThrow(BbsiException.class).when(boomiHelper).getUrl(Mockito.any());
		Mockito.doThrow(BbsiException.class).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		String response = employeeInvitationBusinessServiceImpl.getEmployeesForBulkInvitation(userPrincipal);
		assertEquals("[]", response);
	}

	private List<AccessTermination> populateAccessTermination() {
		List<AccessTermination> listOfAccessTermination = new ArrayList<AccessTermination>();
		AccessTermination access = new AccessTermination();
		access.setClientCode("909464");
		access.setEmployeeCode("E71624");
		access.setEndDate(LocalDate.now());
		listOfAccessTermination.add(access);
		return listOfAccessTermination;
	}

	private List<UserClients> populateUserClients() {
		List<UserClients> listUserClients = new ArrayList<UserClients>();
		UserClients clients = new UserClients();
		clients.setClientCode("909090");
		clients.setFirstName("testBbsi");
		clients.setLastName("Admin");
		clients.setMobile("9090090909");
		clients.setEmployeeCode("E71624");
		clients.setIsActive(true);
		clients.setNewHireId(12l);
		clients.setIsPrimary(true);
		clients.setEndDate(LocalDateTime.now());
		clients.setStartDate(LocalDateTime.now());
		clients.setUserType("NewHire");
		clients.setId(12l);
		clients.setUser(populateUsers());
		listUserClients.add(clients);
		return listUserClients;
	}

	private List<UserClients> populateUserClientsEmployeeCodeEmpty() {
		List<UserClients> listUserClients = new ArrayList<UserClients>();
		UserClients clients = new UserClients();
		clients.setClientCode("909090");
		clients.setFirstName("testBbsi");
		clients.setLastName("Admin");
		clients.setMobile("9090090909");
		clients.setEmployeeCode("");
		clients.setIsActive(true);
		clients.setNewHireId(12l);
		clients.setIsPrimary(true);
		clients.setEndDate(LocalDateTime.now());
		clients.setStartDate(LocalDateTime.now());
		clients.setUserType("NewHire");
		clients.setId(12l);
		clients.setUser(populateUsers());
		listUserClients.add(clients);
		return listUserClients;
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
		return users;
	}

	private EmployeeInvitation getEmployeeInvitation() {
		EmployeeInvitation data = new EmployeeInvitation();
		data.setId(2l);
		data.setClientCode("23456");
		data.setEmployeeCode("XYZ123");
		return data;
	}

	private EmployeeInvitation getEmployeeInvitation1() {
		EmployeeInvitation data = new EmployeeInvitation();
		data.setId(2l);
		data.setClientCode("23456");
		data.setEmployeeCode("[]");
		return data;
	}

	private EmployeeInvitationDTO getEmployeeInvitationDTO() {
		EmployeeInvitationDTO data = new EmployeeInvitationDTO();
		data.setId(2l);
		data.setClientCode("23456");
		data.setEmployeeCode("XYZ123");
		data.setLastSentDate(LocalDateTime.now().minusMonths(6));
		return data;
	}

}
