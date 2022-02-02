package com.bbsi.platform.user.businessservice.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.bbsi.platform.common.enums.Enums.UserEnum;
import com.bbsi.platform.common.exception.ExceptionUtils;
import com.bbsi.platform.common.generic.AuditDetailsUtil;
import com.bbsi.platform.common.generic.BoomiHelper;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableTriConsumer;
import com.bbsi.platform.common.generic.RestClient;
import com.bbsi.platform.common.user.dto.AccessTerminationDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.user.mapper.AccessTerminationMapper;
import com.bbsi.platform.user.model.AccessTermination;
import com.bbsi.platform.user.model.ClientMaster;
import com.bbsi.platform.user.model.ClientRole;
import com.bbsi.platform.user.model.Mapping;
import com.bbsi.platform.user.model.RbacEntity;
import com.bbsi.platform.user.model.UserClients;
import com.bbsi.platform.user.model.Users;
import com.bbsi.platform.user.repository.AccessTerminationRepository;
import com.bbsi.platform.user.repository.ClientMasterRepository;
import com.bbsi.platform.user.repository.ClientRoleRepository;
import com.bbsi.platform.user.repository.MappingRepository;
import com.bbsi.platform.user.repository.RbacRepository;
import com.bbsi.platform.user.repository.UserClientsRepository;
import com.bbsi.platform.user.utils.MethodNames;

/**
 * @author mprasad
 *
 */
public class AccessTerminationBusinessServiceImplTest {

	@InjectMocks
	private AccessTerminationBusinessServiceImpl accessTerminationBusinessServiceImpl;
	@Mock
	private AccessTerminationRepository accessTerminationRepository;

	@Spy
	private AccessTerminationMapper accessTerminationMapper;

	@Mock
	private RbacRepository rbacRepository;

	@Mock
	private UserClientsRepository userClientRepository;

	@Mock
	private ClientMasterRepository clientMasterRepository;

	@Mock
	private MappingRepository mappingRepository;

	@Mock
	private AuditDetailsUtil auditDetailsUtil;

	@Mock
	private RestClient restClient;

	@Mock
	private ClientRoleRepository clientRoleRepository;

	@Mock
	private BoomiHelper helper;

	private AccessTerminationDTO accessTerminationDto;
	private AccessTermination accessTermination;
	private List<AccessTermination> accessTerminationList;
	private List<UserClients> userClientsList;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		accessTermination = populateAccessTermination();
		accessTerminationList = populateAccessTerminationList();
		accessTerminationDto = populateAccessTerminationDto();
		userClientsList = populateUserClientsList();
		ExceptionUtils.handleException = Mockito.mock(ThrowableTriConsumer.class);
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testSaveAccessTermination() {

		when(accessTerminationMapper.accessTerminationToAccessTerminationDto(accessTerminationDto))
				.thenReturn(accessTerminationList);
		doReturn(accessTerminationList).when(accessTerminationRepository).saveAll(accessTerminationList);

		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.saveAccessTermination(accessTerminationDto);
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
		assertEquals(accessTerminationDto.getEmployeeCode(), actualAccessTerminationDto.getEmployeeCode());
		assertEquals(accessTerminationDto.getId(), actualAccessTerminationDto.getId());
	}

	@Test
	public void testSaveAccessTerminationThrowsException() {

		doThrow(BbsiException.class).when(accessTerminationMapper)
				.accessTerminationToAccessTerminationDto(accessTerminationDto);
		doReturn(accessTerminationList).when(accessTerminationRepository).saveAll(accessTerminationList);

		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.saveAccessTermination(accessTerminationDto);

		assertEquals(1, actualAccessTerminationDto.getId());

	}

	@Test
	public void TestUpdateAccessTerminationWhenUSERTYPE_CLIENTisClient() {
		accessTerminationList.add(accessTermination);
		doReturn(accessTerminationList).when(accessTerminationRepository).findByClientCode(anyString());
		doReturn(accessTerminationList).when(accessTerminationRepository).saveAll(any());
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.updateAccessTermination(accessTerminationDto);
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
		assertEquals(accessTerminationDto.getEmployeeCode(), actualAccessTerminationDto.getEmployeeCode());
		assertEquals(accessTerminationDto.getId(), actualAccessTerminationDto.getId());
	}

	@Test
	public void TestUpdateAccessTerminationWhenEmpCodeExist() {
		doReturn(null).when(accessTerminationRepository).findByClientCode(anyString());
		doReturn(null).when(accessTerminationRepository).saveAll(any());
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.updateAccessTermination(accessTerminationDto);
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
		assertEquals(accessTerminationDto.getEmployeeCode(), actualAccessTerminationDto.getEmployeeCode());
		assertEquals(accessTerminationDto.getId(), actualAccessTerminationDto.getId());
	}

	@Test
	public void TestUpdateAccessTerminationWhenUSERTYPE_CLIENTisClientWhenEmpCodeIsNull() {
		accessTerminationList.add(populateAccessTerminationWhenIsExternal());
		doReturn(accessTerminationList).when(accessTerminationRepository).findByClientCode(anyString());
		doReturn(accessTerminationList).when(accessTerminationRepository).saveAll(any());
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.updateAccessTermination(populateAccessTerminationDtoWhenEmpCodeIsNull());
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
	}

	@Test
	public void TestUpdateAccessTerminationWhenUSERTYPE_CLIENTisClientWhenEmpCodeIsNotNull() {
		accessTerminationList.add(populateAccessTerminationWhenIsExternal());
		doReturn(new ArrayList<>()).when(accessTerminationRepository).findByClientCode(anyString());
		doReturn(accessTerminationList).when(accessTerminationRepository).saveAll(any());
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.updateAccessTermination(populateAccessTerminationDtoWhenEmpCodeIsNull());
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
	}

	@Test
	public void TestUpdateAccessTerminationWhenUSERTYPE_CLIENTisClientWhenEmpCodeIsNullWhenTypeIsClient() {
		accessTerminationList.add(populateAccessTerminationWhenIsClient());
		doReturn(accessTerminationList).when(accessTerminationRepository).findByClientCode(anyString());
		doReturn(accessTerminationList).when(accessTerminationRepository).saveAll(any());
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.updateAccessTermination(populateAccessTerminationDtoWhenEmpCodeIsNull());
		assertEquals(1, actualAccessTerminationDto.getId());
	}

	@Test
	public void TestUpdateAccessTerminationWhenUSERTYPE_CLIENTisEmployee() {
		accessTermination.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		accessTerminationList.add(accessTermination);
		doReturn(accessTerminationList).when(accessTerminationRepository).findByClientCode(anyString());
		doReturn(accessTerminationList).when(accessTerminationRepository).saveAll(any());
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.updateAccessTermination(accessTerminationDto);
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
		assertEquals(accessTerminationDto.getEmployeeCode(), actualAccessTerminationDto.getEmployeeCode());
		assertEquals(accessTerminationDto.getId(), actualAccessTerminationDto.getId());
	}

	@Test
	public void TestUpdateAccessTerminationWhenUSERTYPE_EXTERNALisExternal() {
		accessTermination.setUserType(GenericConstants.USERTYPE_EXTERNAL.toString());
		accessTerminationList.add(accessTermination);
		doReturn(accessTerminationList).when(accessTerminationRepository).findByClientCode(anyString());
		doReturn(accessTerminationList).when(accessTerminationRepository).saveAll(any());
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.updateAccessTermination(accessTerminationDto);
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
		assertEquals(accessTerminationDto.getEmployeeCode(), actualAccessTerminationDto.getEmployeeCode());
		assertEquals(accessTerminationDto.getId(), actualAccessTerminationDto.getId());
	}

	@Test
	public void TestUpdateAccessTerminationWheAccessTerminationListIsEmpty() {
		accessTermination.setUserType(GenericConstants.USERTYPE_EXTERNAL.toString());
		accessTerminationList.add(accessTermination);
		doReturn(null).when(accessTerminationRepository).findByClientCode(anyString());
		doReturn(accessTerminationList).when(accessTerminationRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.updateAccessTermination(accessTerminationDto);
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
		assertEquals(accessTerminationDto.getEmployeeCode(), actualAccessTerminationDto.getEmployeeCode());
		assertEquals(accessTerminationDto.getId(), actualAccessTerminationDto.getId());
	}

	@Test
	public void TestUpdateAccessTerminationThrowsException() {

		doThrow(BbsiException.class).when(accessTerminationRepository).findByClientCode(anyString());
		doThrow(BbsiException.class).when(accessTerminationRepository).saveAll(any());
		doThrow(BbsiException.class).when(accessTerminationRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.updateAccessTermination(accessTerminationDto);
		assertEquals(1, actualAccessTerminationDto.getId());
	}

	@Test
	public void testUpdateStatusByEndDate() {
		List<AccessTermination> result1 = new ArrayList<>();
		result1.add(populateAccessTermination());
		AccessTermination access = new AccessTermination();
		access.setClientCode("clientCode");
		access.setEmployeeCode("employeeCode");
		access.setStartDate(LocalDate.now());
		access.setEndDate(LocalDate.now());
		access.setId(2l);
		access.setStartDate(LocalDate.now());
		access.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		result1.add(access);
		List<AccessTermination> result2 = new ArrayList<>();
		result2.add(access);
		AccessTermination a = populateAccessTermination();
		a.setEmployeeCode("E1786F");
		a.setStartDate(LocalDate.now());
		a.setEndDate(LocalDate.now());
		a.setId(2l);
		result2.add(a);
		Optional<UserClients> optional = Optional.of(populateUserClient());

		String json = "[{\"employee_code\":\"E1786F\",\"first_name\":\"ROMEL\",\"start_date\":\"2019-08-19\",\"status_date\":\"2019-08-19\",\"status\":\"A\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",\"proj_cost_center\":\"PRO1\"},\"last_name\":\"FERNANDEZ\",\"middle_name\":\"ISRAEL\",\"name\":\"FERNANDEZ,ROMEL ISRAEL\",\"last_hire\":\"01-25-2019\"},{\"employee_code\":\"employeeCode\",\"first_name\":\"TYRIEK\",\"status\":\"T\",\"status_date\":\"2019-08-19\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",	\"proj_cost_center\":\"PRO1\"},\"last_name\":\"OSIRUPHEL\",\"name\":\"OSIRUPHEL, TYRIEK\",\"last_hire\":\"10-16-2017\"}]";
		String json2 = "{\"employment\":{\"employee_status\":\"T\",\"status_effective_date\":\"2019-08-19\",\"employee_type\":\"F\",\"type_effective_date\":\"2019-08-19\",\"company_employee_number\":\"3007\",\"hire_date\":\"2019-08-19\",\"last_hire_date\":\"2019-08-19\",\"original_hire_date\":\"2019-08-19\",\"first_name\":\"Ali\",\"last_name\":\"Abdille\",\"employee_code\":\"E1786F\",\"start_date\":\"2019-08-19\",\"middle_name\":\"Jama\"},\"is_client_timenet_enabled\":false}";
		Mockito.doReturn(result1).when(accessTerminationRepository).findByEndDate(Mockito.any());
		Mockito.doReturn(result2).when(accessTerminationRepository).findByStartDate(Mockito.any());
		Mockito.doReturn(populateUserClientsList()).when(userClientRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientsList().get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateRbacEntity(), populateRbacEntityForNonTerminated()).when(rbacRepository)
				.findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientArrayList()).when(userClientRepository).findAllEmployeesWithRoles();
		Mockito.doReturn("/url", "/url1").when(helper).getUrl(Mockito.any());
		Mockito.doReturn(json, json2).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doNothing().when(clientRoleRepository).updateRole(Mockito.anyLong(), Mockito.anyLong());
		Mockito.doReturn(optional).when(userClientRepository).findById(Mockito.anyLong());
		Mockito.doReturn(populateUserClientsList1()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndEmployeeCode("clientCode",
				"employeeCode");
	}

	@Test
	public void testUpdateStatusByEndDateForisTerminatedEqual() {
		List<AccessTermination> result1 = new ArrayList<>();
		result1.add(populateAccessTermination());
		AccessTermination access = new AccessTermination();
		access.setClientCode("clientCode");
		access.setEmployeeCode("employeeCode");
		access.setStartDate(LocalDate.now());
		access.setEndDate(LocalDate.now());
		access.setId(2l);
		access.setStartDate(LocalDate.now());
		access.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		result1.add(access);
		List<AccessTermination> result2 = new ArrayList<>();
		result2.add(access);
		AccessTermination a = populateAccessTermination();
		a.setEmployeeCode("E1786F");
		a.setStartDate(LocalDate.now());
		a.setEndDate(LocalDate.now());
		a.setId(2l);
		result2.add(a);
		Optional<UserClients> optional = Optional.of(populateUserClient());

		String json = "[{\"employee_code\":\"E1786F\",\"first_name\":\"ROMEL\",\"start_date\":\"2019-08-19\",\"status_date\":\"2019-08-19\",\"status\":\"A\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",\"proj_cost_center\":\"PRO1\"},\"last_name\":\"FERNANDEZ\",\"middle_name\":\"ISRAEL\",\"name\":\"FERNANDEZ,ROMEL ISRAEL\",\"last_hire\":\"01-25-2019\"},{\"employee_code\":\"employeeCode\",\"first_name\":\"TYRIEK\",\"status\":\"T\",\"status_date\":\"2019-08-19\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",	\"proj_cost_center\":\"PRO1\"},\"last_name\":\"OSIRUPHEL\",\"name\":\"OSIRUPHEL, TYRIEK\",\"last_hire\":\"10-16-2017\"}]";
		String json2 = "{\"employment\":{\"employee_status\":\"T\",\"status_effective_date\":\"2019-08-19\",\"employee_type\":\"F\",\"type_effective_date\":\"2019-08-19\",\"company_employee_number\":\"3007\",\"hire_date\":\"2019-08-19\",\"last_hire_date\":\"2019-08-19\",\"original_hire_date\":\"2019-08-19\",\"first_name\":\"Ali\",\"last_name\":\"Abdille\",\"employee_code\":\"E1786F\",\"start_date\":\"2019-08-19\",\"middle_name\":\"Jama\"},\"is_client_timenet_enabled\":false}";
		Mockito.doReturn(result1).when(accessTerminationRepository).findByEndDate(Mockito.any());
		Mockito.doReturn(result2).when(accessTerminationRepository).findByStartDate(Mockito.any());
		Mockito.doReturn(populateUserClientsList()).when(userClientRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientsList().get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateRbacEntity(), populateRbacEntityForNonTerminated()).when(rbacRepository)
				.findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientArrayList1()).when(userClientRepository).findAllEmployeesWithRoles();
		Mockito.doReturn("/url", "/url1").when(helper).getUrl(Mockito.any());
		Mockito.doReturn(json, json2).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doNothing().when(clientRoleRepository).updateRole(Mockito.anyLong(), Mockito.anyLong());
		Mockito.doReturn(optional).when(userClientRepository).findById(Mockito.anyLong());
		Mockito.doReturn(populateUserClientsList1()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndEmployeeCode("clientCode",
				"employeeCode");
	}

	@Test
	public void testUpdateStatusByEndDateForauditUserChangesClientNull() {
		List<AccessTermination> result1 = new ArrayList<>();
		result1.add(populateAccessTermination());
		AccessTermination access = new AccessTermination();
		access.setClientCode("clientCode");
		access.setEmployeeCode("employeeCode");
		access.setStartDate(LocalDate.now());
		access.setEndDate(LocalDate.now());
		access.setId(2l);
		access.setStartDate(LocalDate.now());
		access.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		result1.add(access);
		List<AccessTermination> result2 = new ArrayList<>();
		result2.add(access);
		AccessTermination a = populateAccessTermination();
		a.setEmployeeCode("E1786F");
		a.setStartDate(LocalDate.now());
		a.setEndDate(LocalDate.now());
		a.setId(2l);
		result2.add(a);
		Optional<UserClients> optional = Optional.of(populateUserClient());

		String json = "[{\"employee_code\":\"E1786F\",\"first_name\":\"ROMEL\",\"start_date\":\"2019-08-19\",\"status_date\":\"2019-08-19\",\"status\":\"A\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",\"proj_cost_center\":\"PRO1\"},\"last_name\":\"FERNANDEZ\",\"middle_name\":\"ISRAEL\",\"name\":\"FERNANDEZ,ROMEL ISRAEL\",\"last_hire\":\"01-25-2019\"},{\"employee_code\":\"employeeCode\",\"first_name\":\"TYRIEK\",\"status\":\"T\",\"status_date\":\"2019-08-19\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",	\"proj_cost_center\":\"PRO1\"},\"last_name\":\"OSIRUPHEL\",\"name\":\"OSIRUPHEL, TYRIEK\",\"last_hire\":\"10-16-2017\"}]";
		String json2 = "{\"employment\":{\"employee_status\":\"T\",\"status_effective_date\":\"2019-08-19\",\"employee_type\":\"F\",\"type_effective_date\":\"2019-08-19\",\"company_employee_number\":\"3007\",\"hire_date\":\"2019-08-19\",\"last_hire_date\":\"2019-08-19\",\"original_hire_date\":\"2019-08-19\",\"first_name\":\"Ali\",\"last_name\":\"Abdille\",\"employee_code\":\"E1786F\",\"start_date\":\"2019-08-19\",\"middle_name\":\"Jama\"},\"is_client_timenet_enabled\":false}";
		Mockito.doReturn(result1).when(accessTerminationRepository).findByEndDate(Mockito.any());
		Mockito.doReturn(result2).when(accessTerminationRepository).findByStartDate(Mockito.any());
		Mockito.doReturn(populateUserClientsList()).when(userClientRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientsList().get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateRbacEntity(), populateRbacEntityForNonTerminated()).when(rbacRepository)
				.findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientArrayList()).when(userClientRepository).findAllEmployeesWithRoles();
		Mockito.doReturn("/url", "/url1").when(helper).getUrl(Mockito.any());
		Mockito.doReturn(json, json2).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doNothing().when(clientRoleRepository).updateRole(Mockito.anyLong(), Mockito.anyLong());
		Mockito.doReturn(optional).when(userClientRepository).findById(Mockito.anyLong());
		Mockito.doReturn(populateUserClientsList4()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		when(clientMasterRepository.findByClientCode(Mockito.anyString())).thenReturn(null);
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndEmployeeCode("clientCode",
				"employeeCode");
	}

	@Test
	public void testUpdateStatusByEndDatesetClientIsActiveAndEndDate() {
		List<AccessTermination> result1 = new ArrayList<>();
		result1.add(populateAccessTermination());
		AccessTermination access = new AccessTermination();
		access.setClientCode("clientCode");
		access.setEmployeeCode("employeeCode");
		access.setStartDate(LocalDate.now());
		access.setEndDate(LocalDate.now());
		access.setId(2l);
		access.setStartDate(LocalDate.now());
		access.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		result1.add(access);
		List<AccessTermination> result2 = new ArrayList<>();
		result2.add(access);
		AccessTermination a = populateAccessTermination();
		a.setEmployeeCode("E1786F");
		a.setStartDate(LocalDate.now());
		a.setEndDate(LocalDate.now());
		a.setId(2l);
		result2.add(a);
		Optional<UserClients> optional = Optional.of(populateUserClient());

		String json = "[{\"employee_code\":\"E1786F\",\"first_name\":\"ROMEL\",\"start_date\":\"2019-08-19\",\"status_date\":\"2019-08-19\",\"status\":\"A\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",\"proj_cost_center\":\"PRO1\"},\"last_name\":\"FERNANDEZ\",\"middle_name\":\"ISRAEL\",\"name\":\"FERNANDEZ,ROMEL ISRAEL\",\"last_hire\":\"01-25-2019\"},{\"employee_code\":\"employeeCode\",\"first_name\":\"TYRIEK\",\"status\":\"T\",\"status_date\":\"2019-08-19\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",	\"proj_cost_center\":\"PRO1\"},\"last_name\":\"OSIRUPHEL\",\"name\":\"OSIRUPHEL, TYRIEK\",\"last_hire\":\"10-16-2017\"}]";
		String json2 = "{\"employment\":{\"employee_status\":\"T\",\"status_effective_date\":\"2019-08-19\",\"employee_type\":\"F\",\"type_effective_date\":\"2019-08-19\",\"company_employee_number\":\"3007\",\"hire_date\":\"2019-08-19\",\"last_hire_date\":\"2019-08-19\",\"original_hire_date\":\"2019-08-19\",\"first_name\":\"Ali\",\"last_name\":\"Abdille\",\"employee_code\":\"E1786F\",\"start_date\":\"2019-08-19\",\"middle_name\":\"Jama\"},\"is_client_timenet_enabled\":false}";
		Mockito.doReturn(result1).when(accessTerminationRepository).findByEndDate(Mockito.any());
		Mockito.doReturn(result2).when(accessTerminationRepository).findByStartDate(Mockito.any());
		Mockito.doReturn(populateUserClientsList()).when(userClientRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientsList().get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateRbacEntity(), populateRbacEntityForNonTerminated()).when(rbacRepository)
				.findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientArrayList()).when(userClientRepository).findAllEmployeesWithRoles();
		Mockito.doReturn("/url", "/url1").when(helper).getUrl(Mockito.any());
		Mockito.doReturn(json, json2).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doNothing().when(clientRoleRepository).updateRole(Mockito.anyLong(), Mockito.anyLong());
		Mockito.doReturn(optional).when(userClientRepository).findById(Mockito.anyLong());
		Mockito.doReturn(populateUserClientsList()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndEmployeeCode("clientCode",
				"employeeCode");
	}

	@Test
	public void testUpdateStatusByEndDatesetClientIsActiveAndEndDateClientCodeNotSame() {
		List<AccessTermination> result1 = new ArrayList<>();
		result1.add(populateAccessTermination());
		AccessTermination access = new AccessTermination();
		access.setClientCode("clientCode");
		access.setEmployeeCode("employeeCode");
		access.setStartDate(LocalDate.now());
		access.setEndDate(LocalDate.now());
		access.setId(2l);
		access.setStartDate(LocalDate.now());
		access.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		result1.add(access);
		List<AccessTermination> result2 = new ArrayList<>();
		result2.add(access);
		AccessTermination a = populateAccessTermination();
		a.setEmployeeCode("E1786F");
		a.setStartDate(LocalDate.now());
		a.setEndDate(LocalDate.now());
		a.setId(2l);
		result2.add(a);
		Optional<UserClients> optional = Optional.of(populateUserClient());

		String json = "[{\"employee_code\":\"E1786F\",\"first_name\":\"ROMEL\",\"start_date\":\"2019-08-19\",\"status_date\":\"2019-08-19\",\"status\":\"A\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",\"proj_cost_center\":\"PRO1\"},\"last_name\":\"FERNANDEZ\",\"middle_name\":\"ISRAEL\",\"name\":\"FERNANDEZ,ROMEL ISRAEL\",\"last_hire\":\"01-25-2019\"},{\"employee_code\":\"employeeCode\",\"first_name\":\"TYRIEK\",\"status\":\"T\",\"status_date\":\"2019-08-19\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",	\"proj_cost_center\":\"PRO1\"},\"last_name\":\"OSIRUPHEL\",\"name\":\"OSIRUPHEL, TYRIEK\",\"last_hire\":\"10-16-2017\"}]";
		String json2 = "{\"employment\":{\"employee_status\":\"T\",\"status_effective_date\":\"2019-08-19\",\"employee_type\":\"F\",\"type_effective_date\":\"2019-08-19\",\"company_employee_number\":\"3007\",\"hire_date\":\"2019-08-19\",\"last_hire_date\":\"2019-08-19\",\"original_hire_date\":\"2019-08-19\",\"first_name\":\"Ali\",\"last_name\":\"Abdille\",\"employee_code\":\"E1786F\",\"start_date\":\"2019-08-19\",\"middle_name\":\"Jama\"},\"is_client_timenet_enabled\":false}";
		Mockito.doReturn(result1).when(accessTerminationRepository).findByEndDate(Mockito.any());
		Mockito.doReturn(result2).when(accessTerminationRepository).findByStartDate(Mockito.any());
		Mockito.doReturn(populateUserClientsList()).when(userClientRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientsList().get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateRbacEntity(), populateRbacEntityForNonTerminated()).when(rbacRepository)
				.findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientArrayList()).when(userClientRepository).findAllEmployeesWithRoles();
		Mockito.doReturn("/url", "/url1").when(helper).getUrl(Mockito.any());
		Mockito.doReturn(json, json2).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doNothing().when(clientRoleRepository).updateRole(Mockito.anyLong(), Mockito.anyLong());
		Mockito.doReturn(optional).when(userClientRepository).findById(Mockito.anyLong());
		Mockito.doReturn(populateUserClientsList2()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndEmployeeCode("clientCode",
				"employeeCode");
	}

	@Test
	public void testUpdateStatusByEndDatesetClientIsActiveAndEndDateUserNull() {
		List<AccessTermination> result1 = new ArrayList<>();
		result1.add(populateAccessTermination());
		AccessTermination access = new AccessTermination();
		access.setClientCode("clientCode");
		access.setEmployeeCode("employeeCode");
		access.setStartDate(LocalDate.now());
		access.setEndDate(LocalDate.now());
		access.setId(2l);
		access.setStartDate(LocalDate.now());
		access.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		result1.add(access);
		List<AccessTermination> result2 = new ArrayList<>();
		result2.add(access);
		AccessTermination a = populateAccessTermination();
		a.setEmployeeCode("E1786F");
		a.setStartDate(LocalDate.now());
		a.setEndDate(LocalDate.now());
		a.setId(2l);
		result2.add(a);
		Optional<UserClients> optional = Optional.of(populateUserClient());

		String json = "[{\"employee_code\":\"E1786F\",\"first_name\":\"ROMEL\",\"start_date\":\"2019-08-19\",\"status_date\":\"2019-08-19\",\"status\":\"A\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",\"proj_cost_center\":\"PRO1\"},\"last_name\":\"FERNANDEZ\",\"middle_name\":\"ISRAEL\",\"name\":\"FERNANDEZ,ROMEL ISRAEL\",\"last_hire\":\"01-25-2019\"},{\"employee_code\":\"employeeCode\",\"first_name\":\"TYRIEK\",\"status\":\"T\",\"status_date\":\"2019-08-19\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",	\"proj_cost_center\":\"PRO1\"},\"last_name\":\"OSIRUPHEL\",\"name\":\"OSIRUPHEL, TYRIEK\",\"last_hire\":\"10-16-2017\"}]";
		String json2 = "{\"employment\":{\"employee_status\":\"T\",\"status_effective_date\":\"2019-08-19\",\"employee_type\":\"F\",\"type_effective_date\":\"2019-08-19\",\"company_employee_number\":\"3007\",\"hire_date\":\"2019-08-19\",\"last_hire_date\":\"2019-08-19\",\"original_hire_date\":\"2019-08-19\",\"first_name\":\"Ali\",\"last_name\":\"Abdille\",\"employee_code\":\"E1786F\",\"start_date\":\"2019-08-19\",\"middle_name\":\"Jama\"},\"is_client_timenet_enabled\":false}";
		Mockito.doReturn(result1).when(accessTerminationRepository).findByEndDate(Mockito.any());
		Mockito.doReturn(result2).when(accessTerminationRepository).findByStartDate(Mockito.any());
		Mockito.doReturn(populateUserClientsList()).when(userClientRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientsList().get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateRbacEntity(), populateRbacEntityForNonTerminated()).when(rbacRepository)
				.findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientArrayList()).when(userClientRepository).findAllEmployeesWithRoles();
		Mockito.doReturn("/url", "/url1").when(helper).getUrl(Mockito.any());
		Mockito.doReturn(json, json2).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doNothing().when(clientRoleRepository).updateRole(Mockito.anyLong(), Mockito.anyLong());
		Mockito.doReturn(optional).when(userClientRepository).findById(Mockito.anyLong());
		Mockito.doReturn(populateUserClientsList3()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndEmployeeCode("clientCode",
				"employeeCode");
	}

	@Test
	public void testUpdateStatusByEndDateForsetClientIsActiveAndEndDateUserClientsEmpty() {
		List<AccessTermination> result1 = new ArrayList<>();
		result1.add(populateAccessTermination());
		AccessTermination access = new AccessTermination();
		access.setClientCode("clientCode");
		access.setEmployeeCode("employeeCode");
		access.setStartDate(LocalDate.now());
		access.setEndDate(LocalDate.now());
		access.setId(2l);
		access.setStartDate(LocalDate.now());
		access.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		result1.add(access);
		List<AccessTermination> result2 = new ArrayList<>();
		result2.add(access);
		AccessTermination a = populateAccessTermination();
		a.setEmployeeCode("E1786F");
		a.setStartDate(LocalDate.now());
		a.setEndDate(LocalDate.now());
		a.setId(2l);
		result2.add(a);
		Optional<UserClients> optional = Optional.of(populateUserClient());

		String json = "[{\"employee_code\":\"E1786F\",\"first_name\":\"ROMEL\",\"start_date\":\"2019-08-19\",\"status_date\":\"2019-08-19\",\"status\":\"A\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",\"proj_cost_center\":\"PRO1\"},\"last_name\":\"FERNANDEZ\",\"middle_name\":\"ISRAEL\",\"name\":\"FERNANDEZ,ROMEL ISRAEL\",\"last_hire\":\"01-25-2019\"},{\"employee_code\":\"employeeCode\",\"first_name\":\"TYRIEK\",\"status\":\"T\",\"status_date\":\"2019-08-19\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",	\"proj_cost_center\":\"PRO1\"},\"last_name\":\"OSIRUPHEL\",\"name\":\"OSIRUPHEL, TYRIEK\",\"last_hire\":\"10-16-2017\"}]";
		String json2 = "{\"employment\":{\"employee_status\":\"T\",\"status_effective_date\":\"2019-08-19\",\"employee_type\":\"F\",\"type_effective_date\":\"2019-08-19\",\"company_employee_number\":\"3007\",\"hire_date\":\"2019-08-19\",\"last_hire_date\":\"2019-08-19\",\"original_hire_date\":\"2019-08-19\",\"first_name\":\"Ali\",\"last_name\":\"Abdille\",\"employee_code\":\"E1786F\",\"start_date\":\"2019-08-19\",\"middle_name\":\"Jama\"},\"is_client_timenet_enabled\":false}";
		Mockito.doReturn(result1).when(accessTerminationRepository).findByEndDate(Mockito.any());
		Mockito.doReturn(result2).when(accessTerminationRepository).findByStartDate(Mockito.any());
		Mockito.doReturn(populateUserClientsList()).when(userClientRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientsList().get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateRbacEntity(), populateRbacEntityForNonTerminated()).when(rbacRepository)
				.findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientArrayList()).when(userClientRepository).findAllEmployeesWithRoles();
		Mockito.doReturn("/url", "/url1").when(helper).getUrl(Mockito.any());
		Mockito.doReturn(json, json2).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doNothing().when(clientRoleRepository).updateRole(Mockito.anyLong(), Mockito.anyLong());
		Mockito.doReturn(optional).when(userClientRepository).findById(Mockito.anyLong());
		Mockito.doReturn(new ArrayList<>()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndEmployeeCode("clientCode",
				"employeeCode");
	}

	@Test
	public void testUpdateStatusByEndDateCase2() {
		List<AccessTermination> result1 = new ArrayList<>();
		result1.add(populateAccessTermination());
		AccessTermination access = new AccessTermination();
		access.setClientCode("clientCode");
		access.setEmployeeCode("employeeCode");
		access.setEndDate(LocalDate.now().plusMonths(10));
		access.setId(2l);
		access.setStartDate(LocalDate.now());
		access.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		result1.add(access);
		List<AccessTermination> result2 = new ArrayList<>();
		result2.add(access);
		AccessTermination a = populateAccessTermination();
		a.setEmployeeCode("E1786F");
		a.setId(2l);
		result2.add(a);

		List<UserClients> userClients = populateUserClientsList();
		userClients.get(0).setId(101L);
		userClients.get(0).setUserType("Not Vancouver Operations Center");

		String json = "[{\"employee_code\":\"E1786F\",\"first_name\":\"ROMEL\",\"status\":\"A\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",\"proj_cost_center\":\"PRO1\"},\"last_name\":\"FERNANDEZ\",\"middle_name\":\"ISRAEL\",name\":\"FERNANDEZ,ROMEL ISRAEL\",\"last_hire\":\"01-25-2019\"},{\"employee_code\":\"employeeCode\",\"first_name\":\"TYRIEK\",\"status\":\"T\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",	\"proj_cost_center\":\"PRO1\"},\"last_name\":\"OSIRUPHEL\",\"name\":\"OSIRUPHEL, TYRIEK\",\"last_hire\":\"10-16-2017\"}]";
		String json2 = "{\"employment\":{\"employee_status\":\"T\",\"status_effective_date\":\"2019-08-19\",\"employee_type\":\"F\",\"type_effective_date\":\"2019-08-19\",\"company_employee_number\":\"3007\",\"hire_date\":\"2019-08-19\",\"last_hire_date\":\"2019-08-19\",\"original_hire_date\":\"2019-08-19\",\"first_name\":\"Ali\",\"last_name\":\"Abdille\",\"middle_name\":\"Jama\"},\"is_client_timenet_enabled\":false}";
		Mockito.doReturn(result1).when(accessTerminationRepository).findByEndDate(Mockito.any());
		Mockito.doReturn(result2).when(accessTerminationRepository).findByStartDate(Mockito.any());
		Mockito.doReturn(userClients).when(userClientRepository).findByClientCodeAndUserType(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doReturn(userClients).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		Mockito.doReturn(userClients.get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateRbacEntity(), populateRbacEntityForNonTerminated()).when(rbacRepository)
				.findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateUserClientArrayList()).when(userClientRepository).findAllEmployeesWithRoles();
		Mockito.doReturn("/url", "/url1").when(helper).getUrl(Mockito.any());
		Mockito.doReturn(json, json2).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doNothing().when(clientRoleRepository).updateRole(Mockito.anyLong(), Mockito.anyLong());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndEmployeeCode("clientCode",
				"employeeCode");
	}

	@Test
	public void testUpdateStatusByEndDateCase3() {
		List<AccessTermination> result1 = new ArrayList<>();
		result1.add(populateAccessTermination());
		AccessTermination access = new AccessTermination();
		access.setClientCode("clientCode");
		access.setEmployeeCode("employeeCode");
		access.setEndDate(LocalDate.now().plusMonths(10));
		access.setId(2l);
		access.setStartDate(LocalDate.now());
		access.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		result1.add(access);
		List<AccessTermination> result2 = new ArrayList<>();
		result2.add(access);
		AccessTermination a = populateAccessTermination();
		a.setEmployeeCode("employeeCode");
		a.setId(2l);
		result2.add(a);

		List<UserClients> userClients = populateUserClientsList();
		userClients.get(0).setId(102L);
		userClients.get(0).getClientRoles().get(0).getRole().setId(1L);

		List<Object[]> userClientsWithActiveFalse = populateUserClientsListWithActive();
		userClientsWithActiveFalse.get(0)[0] = 103L;

		String json = "[{\"employee_code\":\"employeeCode\",\"first_name\":\"ROMEL\",\"status\":\"A\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",\"proj_cost_center\":\"PRO1\"},\"last_name\":\"FERNANDEZ\",\"middle_name\":\"ISRAEL\",name\":\"FERNANDEZ,ROMEL ISRAEL\",\"last_hire\":\"01-25-2019\"},{\"employee_code\":\"employeeCode\",\"first_name\":\"TYRIEK\",\"status\":\"T\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",	\"proj_cost_center\":\"PRO1\"},\"last_name\":\"OSIRUPHEL\",\"name\":\"OSIRUPHEL, TYRIEK\",\"last_hire\":\"10-16-2017\"}]";
		String json2 = "{\"employment\":{\"employee_status\":\"T\",\"status_effective_date\":\"2019-08-19\",\"employee_type\":\"F\",\"type_effective_date\":\"2019-08-19\",\"company_employee_number\":\"3007\",\"hire_date\":\"2019-08-19\",\"last_hire_date\":\"2019-08-19\",\"original_hire_date\":\"2019-08-19\",\"first_name\":\"Ali\",\"last_name\":\"Abdille\",\"middle_name\":\"Jama\"},\"is_client_timenet_enabled\":false}";
		Mockito.doReturn(result1).when(accessTerminationRepository).findByEndDate(Mockito.any());
		Mockito.doReturn(result2).when(accessTerminationRepository).findByStartDate(Mockito.any());
		Mockito.doReturn(userClients).when(userClientRepository).findByClientCodeAndUserType(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doReturn(userClients).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		Mockito.doReturn(userClients.get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateRbacEntity(), populateRbacEntityForNonTerminated()).when(rbacRepository)
				.findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(userClientsWithActiveFalse).when(userClientRepository).findAllEmployeesWithRoles();
		Mockito.doReturn("/url", "/url1").when(helper).getUrl(Mockito.any());
		Mockito.doReturn(json, json2).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doNothing().when(clientRoleRepository).updateRole(Mockito.anyLong(), Mockito.anyLong());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndEmployeeCode("clientCode",
				"employeeCode");
	}

	@Test
	public void testUpdateStatusByEndDateThrowsException() {
		doThrow(BbsiException.class).when(accessTerminationRepository).findByEndDate(any());
		doThrow(BbsiException.class).when(accessTerminationRepository).findByStartDate(any());
		doThrow(BbsiException.class).when(userClientRepository).findByEmployeeCode(any());
		doThrow(BbsiException.class).when(rbacRepository).findByCodeAndType(any(), any());
		doThrow(BbsiException.class).when(userClientRepository).findAllEmployeesWithRoles();
		doThrow(BbsiException.class).when(userClientRepository).findByClientCodeAndUserType(any(), any());
		doThrow(BbsiException.class).when(userClientRepository).saveAll(any());
		doThrow(BbsiException.class).when(userClientRepository).findByClientCodeAndEmployeeCode(any(), any());
		doThrow(BbsiException.class).when(auditDetailsUtil).buildAuditDetailsAndSave(any(), any(), any(), any(), any(),
				any(), any(), any(), any(), any());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		assertTrue(true);
	}

	@Test
	public void testUpdateStatusByEndDateForNullList() {
		Mockito.doReturn(null).when(accessTerminationRepository).findByEndDate(Mockito.any());
		Mockito.doReturn(null).when(accessTerminationRepository).findByStartDate(Mockito.any());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		assertTrue(true);
	}

	@Test
	public void testUpdateStatusByEndDateWhenupdateEmployeeAccessThrowsExceptionupdateClientAccess() {

		String json = "[{\"employeecodes\": [{\"status\": \"I\",\"employee_code\": \"1234\"},{\"status\": \"A\",\"employee_code\": \"5678\"}],\"employee_code\":\"D12345\",\"status\":\"A\"}]";
		AccessTermination accessTermination1 = new AccessTermination();
		accessTermination1.setClientCode("client1");
		accessTermination1.setEmployeeCode(null);
		accessTermination1.setId(1l);
		accessTermination1.setUserType("Employee");
		LocalDate endDate = LocalDate.now();
		accessTermination1.setEndDate(endDate);
		accessTerminationList.add(accessTermination1);
		RbacEntity rbacEntity = populateRbacEntity();

		doReturn(accessTerminationList).when(accessTerminationRepository).findByEndDate(any());
		doReturn(accessTerminationList).when(accessTerminationRepository).findByStartDate(any());
		doThrow(BbsiException.class).when(userClientRepository).findByEmployeeCode(any());
		doReturn(userClientsList).when(userClientRepository).saveAll(any());
		doReturn(userClientsList).when(userClientRepository).findByClientCodeAndUserType(Mockito.anyString(),
				Mockito.anyString());
		doReturn(rbacEntity).when(rbacRepository).findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		doReturn(populateUserClientsListWithActive()).when(userClientRepository).findAllEmployeesWithRoles();
		doReturn("uri").when(helper).getUrl(Mockito.any());
		doReturn(json).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		assertTrue(true);

	}

	@Test
	public void testUpdateStatusByEndDateWithoutClientCodeSetDbUserClient() {
		String json = "[{\"employeecodes\": [{\"status\": \"I\",\"employee_code\": \"1234\"},{\"status\": \"A\",\"employee_code\": \"5678\"}],\"employee_code\":\"employeeCode\",\"status\":\"A\"}]";
		AccessTermination accessTermination1 = new AccessTermination();
		accessTermination1.setEmployeeCode(null);
		accessTermination1.setId(1l);
		accessTermination1.setUserType("Employee");
		accessTerminationList.add(accessTermination1);
		RbacEntity rbacEntity = populateRbacEntity();
		Optional<UserClients> optional = Optional.of(populateUserClient());

		doReturn(accessTerminationList).when(accessTerminationRepository).findByEndDate(any());
		doReturn(accessTerminationList).when(accessTerminationRepository).findByStartDate(any());
		doThrow(BbsiException.class).when(userClientRepository).findByEmployeeCode(any());
		doReturn(userClientsList).when(userClientRepository).saveAll(any());
		doReturn(userClientsList).when(userClientRepository).findByClientCodeAndUserType(Mockito.anyString(),
				Mockito.anyString());
		doReturn(rbacEntity).when(rbacRepository).findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		doReturn(populateUserClientsListWithActive()).when(userClientRepository).findAllEmployeesWithRoles();
		doReturn("uri").when(helper).getUrl(Mockito.any());
		doReturn(json).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.doReturn(optional).when(userClientRepository).findById(Mockito.anyLong());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		assertTrue(true);

	}

	@Test
	public void testUpdateStatusByEndDateWithoutClientCodeSetDbUserClientNull() {

		String json = "[{\"employeecodes\": [{\"status\": \"I\",\"employee_code\": \"1234\"},{\"status\": \"A\",\"employee_code\": \"5678\"}],\"employee_code\":\"employeeCode\",\"status\":\"A\"}]";
		AccessTermination accessTermination1 = new AccessTermination();
		accessTermination1.setEmployeeCode(null);
		accessTermination1.setId(1l);
		accessTermination1.setUserType("Employee");
		accessTerminationList.add(accessTermination1);
		RbacEntity rbacEntity = populateRbacEntity();

		doReturn(accessTerminationList).when(accessTerminationRepository).findByEndDate(any());
		doReturn(accessTerminationList).when(accessTerminationRepository).findByStartDate(any());
		doThrow(BbsiException.class).when(userClientRepository).findByEmployeeCode(any());
		doReturn(userClientsList).when(userClientRepository).saveAll(any());
		doReturn(userClientsList).when(userClientRepository).findByClientCodeAndUserType(Mockito.anyString(),
				Mockito.anyString());
		doReturn(rbacEntity).when(rbacRepository).findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		doReturn(populateUserClientsListWithActive()).when(userClientRepository).findAllEmployeesWithRoles();
		doReturn("uri").when(helper).getUrl(Mockito.any());
		doReturn(json).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		assertTrue(true);

	}

	@Test
	public void testUpdateStatusByEndDateWithoutClientCodeSetDbUserClientNull1() {

		String json = "[{\"employeecodes\": [{\"status\": \"I\",\"employee_code\": \"1234\"},{\"status\": \"A\",\"employee_code\": \"5678\"}],\"employee_code\":\"employeeCode\",\"status\":\"A\"}]";
		AccessTermination accessTermination1 = new AccessTermination();
		accessTermination1.setEmployeeCode(null);
		accessTermination1.setId(1l);
		accessTermination1.setUserType("Employee");
		accessTerminationList.add(accessTermination1);
		RbacEntity rbacEntity = populateRbacEntity();
		Optional<UserClients> optional = Optional.of(populateUserClient3());

		doReturn(accessTerminationList).when(accessTerminationRepository).findByEndDate(any());
		doReturn(accessTerminationList).when(accessTerminationRepository).findByStartDate(any());
		doThrow(BbsiException.class).when(userClientRepository).findByEmployeeCode(any());
		doReturn(userClientsList).when(userClientRepository).saveAll(any());
		doReturn(userClientsList).when(userClientRepository).findByClientCodeAndUserType(Mockito.anyString(),
				Mockito.anyString());
		doReturn(rbacEntity).when(rbacRepository).findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		doReturn(populateUserClientsListWithActive()).when(userClientRepository).findAllEmployeesWithRoles();
		doReturn("uri").when(helper).getUrl(Mockito.any());
		doReturn(json).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.doReturn(optional).when(userClientRepository).findById(Mockito.anyLong());
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		assertTrue(true);

	}

	@Test
	public void testUpdateStatusByEndDateWhenUpdateClientAccessThrowsException() {

		AccessTermination accessTermination1 = new AccessTermination();
		accessTermination1.setClientCode("clientCode");
		accessTermination1.setEmployeeCode(null);
		accessTermination1.setId(1l);
		accessTermination1.setUserType("Employee");
		accessTerminationList.add(accessTermination1);

		doReturn(accessTerminationList).when(accessTerminationRepository).findByEndDate(any());
		doReturn(accessTerminationList).when(accessTerminationRepository).findByStartDate(any());
		doReturn(userClientsList).when(userClientRepository).findByEmployeeCode(any());
		doReturn(userClientsList).when(userClientRepository).saveAll(any());

		accessTerminationBusinessServiceImpl.updateStatusByEndDate();

		assertTrue(true);

	}

	@Test
	public void testGetTerminatedClientByClientCode() {

		doReturn(accessTerminationList).when(accessTerminationRepository).findByClientCodeAndEmployeeCodeIsNull(any());
		doReturn(accessTerminationDto).when(accessTerminationMapper).accessTerminationListToAccessTerminationDto(any());
		when(accessTerminationMapper.accessTerminationListToAccessTerminationDto(accessTerminationList))
				.thenReturn(accessTerminationDto);
		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.getTerminatedClientByClientCode("clientCode");
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
		assertEquals(accessTerminationDto.getEmployeeCode(), actualAccessTerminationDto.getEmployeeCode());
		assertEquals(accessTerminationDto.getId(), actualAccessTerminationDto.getId());
	}

	@Test
	public void testGetTerminatedClientByClientCodeForEmptyList() {
		Mockito.doReturn(new ArrayList<>()).when(accessTerminationRepository)
				.findByClientCodeAndEmployeeCodeIsNull(Mockito.anyString());
		AccessTerminationDTO result = accessTerminationBusinessServiceImpl
				.getTerminatedClientByClientCode("clientCode");
		assertNull(result);
	}

	@Test
	public void testGetTerminatedClientByClientCodeThrowsException() {

		doThrow(BbsiException.class).when(accessTerminationRepository).findByClientCodeAndEmployeeCodeIsNull(any());
		doReturn(accessTerminationDto).when(accessTerminationMapper).accessTerminationListToAccessTerminationDto(any());

		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.getTerminatedClientByClientCode("clientCode");
		assertNull(actualAccessTerminationDto);

	}

	@Test
	public void testGetEmployeeAccessInfo() {

		doReturn(accessTerminationList).when(accessTerminationRepository).findByClientCodeAndEmployeeCode(any(), any());
		doReturn(accessTerminationDto).when(accessTerminationMapper).accessTerminationListToAccessTerminationDto(any());

		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.getEmployeeAccessInfo("clientCode", "employeeCode");
		assertEquals(accessTerminationDto.getClientCode(), actualAccessTerminationDto.getClientCode());
		assertEquals(accessTerminationDto.getEmployeeCode(), actualAccessTerminationDto.getEmployeeCode());
		assertEquals(accessTerminationDto.getId(), actualAccessTerminationDto.getId());
	}

	@Test
	public void testGetEmployeeAccessInfoThrowsException() {

		doThrow(BbsiException.class).when(accessTerminationRepository).findByClientCodeAndEmployeeCode(any(), any());
		doReturn(accessTerminationDto).when(accessTerminationMapper).accessTerminationListToAccessTerminationDto(any());

		AccessTerminationDTO actualAccessTerminationDto = accessTerminationBusinessServiceImpl
				.getEmployeeAccessInfo("clientCode", "employeeCode");
		assertNull(actualAccessTerminationDto);

	}

	@Test
	public void testGetEmployeeAccessInfoForEmptyList() {
		Mockito.doReturn(new ArrayList<>()).when(accessTerminationRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		AccessTerminationDTO result = accessTerminationBusinessServiceImpl.getEmployeeAccessInfo("clientCode",
				"employeeCode");
		assertNull(result);
	}

	@Test
	public void testUpdateStatusByEndDateCase4() {
		List<AccessTermination> result1 = new ArrayList<>();
		result1.add(populateAccessTermination());
		AccessTermination access = new AccessTermination();
		access.setClientCode("clientCode");
		access.setEmployeeCode("employeeCode");
		access.setEndDate(LocalDate.now().plusMonths(10));
		access.setId(2l);
		access.setStartDate(LocalDate.now());
		access.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		result1.add(access);
		List<AccessTermination> result2 = new ArrayList<>();
		result2.add(access);
		AccessTermination a = populateAccessTermination();
		a.setEmployeeCode("E1786F");
		a.setId(2l);
		result2.add(a);

		List<UserClients> userClients = populateUserClientsList();
		userClients.get(0).setId(101L);
		userClients.get(0).setEndDate(null);

		List<UserClients> userClients2 = populateUserClientsList();
		userClients2.get(0).setId(101L);

		String json = "[{\"employee_code\":\"E1786F\",\"first_name\":\"ROMEL\",\"status\":\"A\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",\"proj_cost_center\":\"PRO1\"},\"last_name\":\"FERNANDEZ\",\"middle_name\":\"ISRAEL\",name\":\"FERNANDEZ,ROMEL ISRAEL\",\"last_hire\":\"01-25-2019\"},{\"employee_code\":\"employeeCode\",\"first_name\":\"TYRIEK\",\"status\":\"T\",\"employement_type\":\"F\",\"job_code\":\"9403\",\"assignment\":{\"home_location\":\"1\",\"home_division\":\"div124\",\"home_department\":\"DEPT_AKS\",    \"proj_cost_center\":\"PRO1\"},\"last_name\":\"OSIRUPHEL\",\"name\":\"OSIRUPHEL, TYRIEK\",\"last_hire\":\"10-16-2017\"}]";
		String json2 = "{\"employment\":{\"employee_status\":\"T\",\"status_effective_date\":\"2019-08-19\",\"employee_type\":\"F\",\"type_effective_date\":\"2019-08-19\",\"company_employee_number\":\"3007\",\"hire_date\":\"2019-08-19\",\"last_hire_date\":\"2019-08-19\",\"original_hire_date\":\"2019-08-19\",\"first_name\":\"Ali\",\"last_name\":\"Abdille\",\"middle_name\":\"Jama\"},\"is_client_timenet_enabled\":false}";
		Mockito.doReturn(result1).when(accessTerminationRepository).findByEndDate(Mockito.any());
		Mockito.doReturn(result2).when(accessTerminationRepository).findByStartDate(Mockito.any());
		Mockito.doReturn(userClients).when(userClientRepository).findByClientCodeAndUserType(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doReturn(userClients.get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateRbacEntity(), populateRbacEntityForNonTerminated()).when(rbacRepository)
				.findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(userClients).when(userClientRepository).findAllEmployeesWithRoles();
		Mockito.doReturn("/url", "/url1").when(helper).getUrl(Mockito.any());
		Mockito.doReturn(json, json2).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doNothing().when(clientRoleRepository).updateRole(Mockito.anyLong(), Mockito.anyLong());
		Mockito.doReturn(userClients2).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		userClients2.get(0).setEmployeeCode(null);
		userClients2.get(0).setUserType("Not Vancouver Operations Center");
		userClients2.get(0).setEndDate(null);
		accessTerminationBusinessServiceImpl.updateStatusByEndDate();
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndEmployeeCode("clientCode",
				"employeeCode");
	}

	private AccessTerminationDTO populateAccessTerminationDto() {

		AccessTerminationDTO accessTerminationDTO = new AccessTerminationDTO();
		accessTerminationDTO.setClientCode("clientCode");
		accessTerminationDTO.setEmployeeCode("employeeCode");
		accessTerminationDTO.setId(1l);

		return accessTerminationDTO;
	}

	private AccessTerminationDTO populateAccessTerminationDtoWhenEmpCodeIsNull() {

		AccessTerminationDTO accessTerminationDTO = new AccessTerminationDTO();
		accessTerminationDTO.setClientCode("clientCode");
		accessTerminationDTO.setId(1l);

		return accessTerminationDTO;
	}

	private AccessTermination populateAccessTermination() {

		AccessTermination accessTermination = new AccessTermination();
		accessTermination.setClientCode("clientCode");
		accessTermination.setEmployeeCode("employeeCode1");
		accessTermination.setId(1l);
		accessTermination.setUserType("Employee");
		return accessTermination;
	}

	private AccessTermination populateAccessTerminationWhenIsExternal() {

		AccessTermination accessTermination = new AccessTermination();
		accessTermination.setClientCode("clientCode");
		accessTermination.setEmployeeCode("employeeCode");
		accessTermination.setId(1l);
		accessTermination.setUserType("External");
		return accessTermination;
	}

	private AccessTermination populateAccessTerminationWhenIsClient() {

		AccessTermination accessTermination = new AccessTermination();
		accessTermination.setClientCode("clientCode");
		accessTermination.setEmployeeCode("employeeCode");
		accessTermination.setId(1l);
		accessTermination.setUserType("Client");
		return accessTermination;
	}

	private List<AccessTermination> populateAccessTerminationList() {

		List<AccessTermination> listOfAccessTermination = new ArrayList<>();

		listOfAccessTermination.add(populateAccessTermination());

		return listOfAccessTermination;
	}

	private UserClients populateUserClient() {

		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("clientCode");
		clientMaster.setClientName("clientName");

		UserClients userClients = new UserClients();

		userClients.setClientCode("clientCode");
		userClients.setClient(clientMaster);
		userClients.setEmployeeCode("employeeCode");
		userClients.setClientRoles(populateClientRoles());
		Users user = new Users();
		user.setAuthenticationType("Bearer");
		user.setEmail("test@test.com");
		user.setId(1l);
		userClients.setUser(user);
		userClients.setIsActive(true);
		return userClients;
	}

	private UserClients populateUserClient1() {

		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("clientCode");
		clientMaster.setClientName("clientName");

		UserClients userClients = new UserClients();

		userClients.setClientCode("clientCode");
		userClients.setClient(clientMaster);
		userClients.setEmployeeCode("");
		userClients.setClientRoles(populateClientRoles());
		Users user = new Users();
		user.setAuthenticationType("Bearer");
		user.setEmail("test@test.com");
		user.setId(1l);
		userClients.setUser(user);
		userClients.setIsActive(true);
		return userClients;
	}

	private UserClients populateUserClient4() {

		UserClients userClients = new UserClients();

		userClients.setClientCode("clientCode");
		userClients.setClient(null);
		userClients.setEmployeeCode("");
		userClients.setClientRoles(populateClientRoles());
		Users user = new Users();
		user.setAuthenticationType("Bearer");
		user.setEmail("test@test.com");
		user.setId(1l);
		userClients.setUser(user);
		userClients.setIsActive(true);
		return userClients;
	}

	private UserClients populateUserClient2() {

		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("clientCode");
		clientMaster.setClientName("clientName");

		UserClients userClients = new UserClients();

		userClients.setClientCode("909464");
		userClients.setClient(clientMaster);
		userClients.setEmployeeCode("");
		userClients.setClientRoles(populateClientRoles());
		Users user = new Users();
		user.setAuthenticationType("Bearer");
		user.setEmail("test@test.com");
		user.setId(1l);
		userClients.setUser(user);
		userClients.setIsActive(true);
		return userClients;
	}

	private UserClients populateUserClient3() {

		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("clientCode");
		clientMaster.setClientName("clientName");

		UserClients userClients = new UserClients();

		userClients.setClientCode("clientCode");
		userClients.setClient(clientMaster);
		userClients.setEmployeeCode("");
		userClients.setClientRoles(populateClientRoles());
		userClients.setUser(null);
		userClients.setIsActive(true);
		return userClients;
	}

	private List<ClientRole> populateClientRoles() {
		List<ClientRole> listOfUserClients = new ArrayList<>();
		ClientRole clientRole = new ClientRole();
		clientRole.setId(1l);
		clientRole.setRole(populateRole());
		listOfUserClients.add(clientRole);
		return listOfUserClients;
	}

	private Mapping populateRole() {
		Mapping mapping = new Mapping();
		mapping.setId(2l);
		return mapping;
	}

	private List<UserClients> populateUserClientsList() {

		List<UserClients> listOfUserClients = new ArrayList<>();
		listOfUserClients.add(populateUserClient());
		return listOfUserClients;
	}

	private List<UserClients> populateUserClientsList1() {

		List<UserClients> listOfUserClients = new ArrayList<>();
		listOfUserClients.add(populateUserClient1());
		return listOfUserClients;
	}

	private List<UserClients> populateUserClientsList4() {

		List<UserClients> listOfUserClients = new ArrayList<>();
		listOfUserClients.add(populateUserClient4());
		return listOfUserClients;
	}

	private List<UserClients> populateUserClientsList2() {

		List<UserClients> listOfUserClients = new ArrayList<>();
		listOfUserClients.add(populateUserClient2());
		return listOfUserClients;
	}

	private List<UserClients> populateUserClientsList3() {

		List<UserClients> listOfUserClients = new ArrayList<>();
		listOfUserClients.add(populateUserClient3());
		return listOfUserClients;
	}

	private Object[] populateUserClientArray() {
		Object[] objectArr = { BigInteger.ONE, "clientCode", "employeeCode", true, null, BigInteger.ONE };
		return objectArr;
	}

	private Object[] populateUserClientArray1() {
		Object[] objectArr = { BigInteger.ONE, "clientCode", "employeeCode", null, null, BigInteger.ONE };
		return objectArr;
	}

	private Object[] populateUserClientArray2() {
		Object[] objectArr = { BigInteger.ONE, "clientCode", "employeeCode", false, null, BigInteger.ONE };
		return objectArr;
	}

	private List<Object[]> populateUserClientArrayList() {

		List<Object[]> listOfUserClients = new ArrayList<>();
		listOfUserClients.add(populateUserClientArray());
		return listOfUserClients;
	}

	private List<Object[]> populateUserClientArrayList1() {

		List<Object[]> listOfUserClients = new ArrayList<>();
		listOfUserClients.add(populateUserClientArray1());
		listOfUserClients.add(populateUserClientArray2());
		return listOfUserClients;
	}

	private List<Object[]> populateUserClientsListWithActive() {

		List<Object[]> listOfUserClients = new ArrayList<>();
		listOfUserClients.add(populateUserClientWithActive());
		listOfUserClients.add(populateUserClientWithActive1());
		listOfUserClients.add(populateUserClientWithActive2());
		return listOfUserClients;
	}

	private Object[] populateUserClientWithActive() {

		Object[] objectArr = { BigInteger.ONE, "clientCode", "employeeCode", false, null, BigInteger.TWO };
		return objectArr;
	}

	private Object[] populateUserClientWithActive1() {

		Object[] objectArr = { BigInteger.ONE, "clientCode", "employeeCode", true, null, BigInteger.ONE };
		return objectArr;
	}

	private Object[] populateUserClientWithActive2() {

		Object[] objectArr = { BigInteger.ONE, "clientCode", "employeeCode", false, null, BigInteger.ONE };
		return objectArr;
	}

	private RbacEntity populateRbacEntity() {
		RbacEntity rbac = new RbacEntity();
		rbac.setCode(MethodNames.TERMINATED_EMPLOYEE_ROLE_CODE.toString());
		rbac.setMappingId(2l);
		rbac.setId(1l);
		rbac.setClientCode("clientCode");
		rbac.setName("name");
		rbac.setStatus(true);
		rbac.setType(UserEnum.ROLE.toString());
		return rbac;
	}

	private RbacEntity populateRbacEntityForNonTerminated() {
		RbacEntity rbac = new RbacEntity();
		rbac.setCode(GenericConstants.USERTYPE_EMPLOYEE.toString());
		rbac.setId(2l);
		rbac.setClientCode("clientCode");
		rbac.setName("name1");
		rbac.setStatus(true);
		rbac.setType(UserEnum.ROLE.toString());
		return rbac;
	}
}
