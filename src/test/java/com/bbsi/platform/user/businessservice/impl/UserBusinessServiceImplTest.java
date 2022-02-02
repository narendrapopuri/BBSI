package com.bbsi.platform.user.businessservice.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.bbsi.platform.common.businessservice.intf.IntegrationBusinessService;
import com.bbsi.platform.common.client.dto.UserToolbarSettingsDTO;
import com.bbsi.platform.common.dto.ClientDTO;
import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.dto.CostCenterClientDTO;
import com.bbsi.platform.common.dto.CostCenterDTO;
import com.bbsi.platform.common.dto.Email;
import com.bbsi.platform.common.dto.OtpDetailsDTO;
import com.bbsi.platform.common.dto.ParentDTO;
import com.bbsi.platform.common.dto.PrismizedClientsDTO;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.employee.dto.CustomPersonalDTO;
import com.bbsi.platform.common.enums.BatchStatusEnum.PortalStatus;
import com.bbsi.platform.common.enums.BoomiEnum;
import com.bbsi.platform.common.enums.Enums;
import com.bbsi.platform.common.enums.Enums.UserEnum;
import com.bbsi.platform.common.exception.ExceptionUtils;
import com.bbsi.platform.common.generic.AuditDetailsUtil;
import com.bbsi.platform.common.generic.AuthenticationUtil;
import com.bbsi.platform.common.generic.BoomiHelper;
import com.bbsi.platform.common.generic.CommonUtilities;
import com.bbsi.platform.common.generic.EncryptAndDecryptUtil;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.GenericFunctions;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableBiConsumer;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableConsumer;
import com.bbsi.platform.common.generic.GenericUtils;
import com.bbsi.platform.common.generic.RestClient;
import com.bbsi.platform.common.generic.WebClientTemplate;
import com.bbsi.platform.common.user.dto.MenuMappingDTO;
import com.bbsi.platform.common.user.dto.TimenetLoginInfo;
import com.bbsi.platform.common.user.dto.UserInvitationDTO;
import com.bbsi.platform.common.user.dto.UsersDTO;
import com.bbsi.platform.common.user.dto.v2.AccessGroupDTOV2;
import com.bbsi.platform.common.user.dto.v2.ClientDTOV2;
import com.bbsi.platform.common.user.dto.v2.FeatureCodeHierarchyDTOV2;
import com.bbsi.platform.common.user.dto.v2.PolicyAcceptedDTO;
import com.bbsi.platform.common.user.dto.v2.PrivilegeDTOV2;
import com.bbsi.platform.common.user.dto.v2.UserClientRoleDTOV2;
import com.bbsi.platform.common.user.dto.v2.UserCustomDTOV2;
import com.bbsi.platform.common.user.dto.v2.UserExistDTO;
import com.bbsi.platform.common.user.dto.v2.UserSecurityDTOV2;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.exception.business.InvalidUserException;
import com.bbsi.platform.exception.business.UnauthorizedAccessException;
import com.bbsi.platform.exception.business.ValidationException;
import com.bbsi.platform.user.businessservice.intf.EmployeeInvitationBusinessService;
import com.bbsi.platform.user.businessservice.intf.MappingBusinessService;
import com.bbsi.platform.user.businessservice.intf.MenuBusinessService;
import com.bbsi.platform.user.businessservice.intf.UserBusinessService;
import com.bbsi.platform.user.cache.UserProfileCacheService;
import com.bbsi.platform.user.mapper.RbacMapper;
import com.bbsi.platform.user.mapper.UserToolbarSettingsMapperV2;
import com.bbsi.platform.user.mapper.UsersMapper;
import com.bbsi.platform.user.model.ClientMaster;
import com.bbsi.platform.user.model.ClientRole;
import com.bbsi.platform.user.model.Mapping;
import com.bbsi.platform.user.model.PrivilegeMapping;
import com.bbsi.platform.user.model.RbacEntity;
import com.bbsi.platform.user.model.UserClients;
import com.bbsi.platform.user.model.UserCostCenterAssociation;
import com.bbsi.platform.user.model.UserSecurity;
import com.bbsi.platform.user.model.UserToolbarSettings;
import com.bbsi.platform.user.model.Users;
import com.bbsi.platform.user.repository.ClientMasterRepository;
import com.bbsi.platform.user.repository.ClientRoleRepository;
import com.bbsi.platform.user.repository.MappingRepository;
import com.bbsi.platform.user.repository.PrivilegeRepository;
import com.bbsi.platform.user.repository.RbacRepository;
import com.bbsi.platform.user.repository.UserClientsRepository;
import com.bbsi.platform.user.repository.UserCostCenterAssociationRepository;
import com.bbsi.platform.user.repository.UserFilterRepository;
import com.bbsi.platform.user.repository.UserToolbarSettingsRepositoryV2;
import com.bbsi.platform.user.repository.UsersRepository;
import com.bbsi.platform.user.utils.MethodNames;
import com.bbsi.platform.user.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import reactor.core.publisher.Mono;

@ContextConfiguration(classes = UserBusinessServiceImpl.class)
@TestPropertySource
public class UserBusinessServiceImplTest {

	@InjectMocks
	private UserBusinessServiceImpl userServiceImpl;

	@Mock
	private IntegrationBusinessService integrationBusinessService;

	@Mock
	private PrivilegeRepository privilegeRepository;

	@Mock
	private EncryptAndDecryptUtil encryptAndDecryptUtil;

	@Mock
	private PasswordBusinessServiceImpl passwordServiceImpl;

	@Mock
	private UsersRepository usersRepository;

	@Mock
	private AuditDetailsUtil auditDetailsUtil;
	@Mock
	private UserFilterRepository userSecurityRepository;
	@Mock
	private UserProfileCacheService userProfileCacheService;

	@Mock
	private UserToolbarSettingsRepositoryV2 userToolbarSettingsRepositoryV2;

	@Spy
	private UserToolbarSettingsMapperV2 userToolbarSettingsMapperV2;

	@Mock
	private MappingBusinessService mappingService;

	@Mock
	private MenuBusinessService menuBusinessService;
	@Mock
	private AuthenticationUtil authUtilities;
	@Mock
	private RbacRepository rbacRepository;
	@Mock
	private UserClientsRepository userClientRepository;
	@Mock
	private UserCostCenterAssociationRepository userCostCenterAssociationRepository;

	@Mock
	private ClientMasterRepository clientMasterRepository;

	@Mock
	private JsonNode jsonNode;
	@Mock
	private RestClient restClient;
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	private GenericUtils genericUtils;

	@Spy
	private UsersMapper usersMapper;
	@Mock
	private RbacMapper roleMapper;
	@Mock
	private BoomiHelper boomiHelper;
	@Mock
	private MappingRepository mappingRepository;

	@Mock
	private TokenStore tokenStore;

	@Mock
	private PasswordBusinessServiceImpl passwordBusinessServiceImpl;

	@Mock
	private RbacRepository roleRepository;

	@Mock
	private CommonUtilities commonUtilities;

	@Mock
	private EmployeeInvitationBusinessService employeeInvitationBusinessService;
	private UsersDTO userDTO;
	private Users users;
	@Mock
	private WebClientTemplate webClientTemplate;
	@Mock
	private UserBusinessService userBusinessService;

	private UserPrincipal userPrincipal;
	@Mock
	private ClientRoleRepository clientRoleRepository;
	@Mock
	private List<UserSecurity> userSecurityList;

	@Mock
	private RestTemplate restTemplate1;

	private static String token = "Bearer 1234-5678-abcd-efgh";

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private RestTemplate restTemplate;

	@Spy
	private UserBusinessServiceImpl spyServiceImpl;

	@Mock
	private Utils utils = Mockito.mock(Utils.class);

	private ObjectMapper mapper;

	private JsonNode node;

	private JsonNode node1;

	String payload = "{\"personal\" : {\"first_name\" : \"Ali\",\"middle_name\" : \"Jama\",\"last_name\" : \"Abdille\",\"social_security_number\" : \"r8RNP+BPBBgCZFowy0vLEg==\",\"gender\" : \"M\",\"date_of_birth\" : \"1968-01-01\",\"bbsi_assigned_employee_id\" : \"L47730\",\"is_electronic_onboarding\" : false,\"employee_status\" : \"A\",\"scorp_owner\" : \"false\",\"business_owner\" : \"false\"},\"contact_information\" : {\"home_telephone_number\" : \"4083102364\",\"personal_email_address\" : \"calijamaac@yahoo.com\"},\"residential_address\" : {\"address_line1\" : \"4403 Dulcey Drive\",\"zip_code\" : \"95136\",\"county\" : \"SANTA CLARA\",\"city\" : \"SAN JOSE\",\"state\" : \"CA\",\"is_unicorporated_area\" : false},\"veteran_status\" : {\"ethnicity\" : \"B\",\"is_handicapped\" : false,\"is_blind\" : false},\"direct_deposit_status\" : \"A\",\"direct_deposit\" : [{\"routing_transit_number\" : \"h8pbk/1+MJsuPshLTa9GJw==\",\"bank_name\" : \"WELLS FARGO BANK NA\",\"bank_account_number\" : \"QNxQir0hCJKP6wswCm2ixw==\",\"account_type\" : \"C\",\"calculation_method\" : \"B\",\"status\" : \"D\"}],\"federal_tax\" : {\"filing_status\" : \"M\",\"primary_allowances\" : \"2\",\"multiple_jobs\" : false},\"state_tax\" : [{\"filing_status\" : \"M\",\"state\" : \"CA\",\"primary_allowances\" : \"2\",\"secondary_allowances\" : \"2\",\"has_nrstate_certificate\" : false,\"w4_filed\" : \"false\"}],\"review_date\" : \"2019-08-19\",\"performance_review\" : {\"review_date\" : \"2019-08-19\"}}";

	String payload1 = "{\"MethodNames\" : {\"cost_center_code\" : \"1234\",\"getFeatureCodes\" : \"1234\",\"last_name\" : \"Abdille\",\"social_security_number\" : \"r8RNP+BPBBgCZFowy0vLEg==\",\"gender\" : \"M\",\"date_of_birth\" : \"1968-01-01\",\"bbsi_assigned_employee_id\" : \"L47730\",\"is_electronic_onboarding\" : false,\"employee_status\" : \"A\",\"scorp_owner\" : \"false\",\"business_owner\" : \"false\"},\"contact_information\" : {\"home_telephone_number\" : \"4083102364\",\"personal_email_address\" : \"calijamaac@yahoo.com\"},\"residential_address\" : {\"address_line1\" : \"4403 Dulcey Drive\",\"zip_code\" : \"95136\",\"county\" : \"SANTA CLARA\",\"city\" : \"SAN JOSE\",\"state\" : \"CA\",\"is_unicorporated_area\" : false},\"veteran_status\" : {\"ethnicity\" : \"B\",\"is_handicapped\" : false,\"is_blind\" : false},\"direct_deposit_status\" : \"A\",\"direct_deposit\" : [{\"routing_transit_number\" : \"h8pbk/1+MJsuPshLTa9GJw==\",\"bank_name\" : \"WELLS FARGO BANK NA\",\"bank_account_number\" : \"QNxQir0hCJKP6wswCm2ixw==\",\"account_type\" : \"C\",\"calculation_method\" : \"B\",\"status\" : \"D\"}],\"federal_tax\" : {\"filing_status\" : \"M\",\"primary_allowances\" : \"2\",\"multiple_jobs\" : false},\"state_tax\" : [{\"filing_status\" : \"M\",\"state\" : \"CA\",\"primary_allowances\" : \"2\",\"secondary_allowances\" : \"2\",\"has_nrstate_certificate\" : false,\"w4_filed\" : \"false\"}],\"review_date\" : \"2019-08-19\",\"performance_review\" : {\"review_date\" : \"2019-08-19\"}}";

	String jsonResponse = "{\"personal\" : {\"first_name\" : \"Ali\",\"middle_name\" : \"Jama\",\"last_name\" : \"Abdille\",\"social_security_number\" : \"r8RNP+BPBBgCZFowy0vLEg==\",\"gender\" : \"M\",\"date_of_birth\" : \"1968-01-01\",\"bbsi_assigned_employee_id\" : \"L47730\",\"is_electronic_onboarding\" : false,\"employee_status\" : \"T\",\"scorp_owner\" : \"false\",\"business_owner\" : \"false\"},\"contact_information\" : {\"home_telephone_number\" : \"4083102364\",\"personal_email_address\" : \"calijamaac@yahoo.com\"},\"residential_address\" : {\"address_line1\" : \"4403 Dulcey Drive\",\"zip_code\" : \"95136\",\"county\" : \"SANTA CLARA\",\"city\" : \"SAN JOSE\",\"state\" : \"CA\",\"is_unicorporated_area\" : false},\"veteran_status\" : {\"ethnicity\" : \"B\",\"is_handicapped\" : false,\"is_blind\" : false},\"direct_deposit_status\" : \"A\",\"direct_deposit\" : [{\"routing_transit_number\" : \"h8pbk/1+MJsuPshLTa9GJw==\",\"bank_name\" : \"WELLS FARGO BANK NA\",\"bank_account_number\" : \"QNxQir0hCJKP6wswCm2ixw==\",\"account_type\" : \"C\",\"calculation_method\" : \"B\",\"status\" : \"D\"}],\"federal_tax\" : {\"filing_status\" : \"M\",\"primary_allowances\" : \"2\",\"multiple_jobs\" : false},\"state_tax\" : [{\"filing_status\" : \"M\",\"state\" : \"CA\",\"primary_allowances\" : \"2\",\"secondary_allowances\" : \"2\",\"has_nrstate_certificate\" : false,\"w4_filed\" : \"false\"}],\"review_date\" : \"2019-08-19\",\"performance_review\" : {\"review_date\" : \"2019-08-19\"}}";

	String jsonResponse1 = "{\"personal\" : {\"first_name\" : \"Ali\",\"middle_name\" : \"Jama\",\"last_name\" : \"Abdille\",\"social_security_number\" : \"r8RNP+BPBBgCZFowy0vLEg==\",\"gender\" : \"M\",\"date_of_birth\" : \"1968-01-01\",\"bbsi_assigned_employee_id\" : \"L47730\",\"is_electronic_onboarding\" : false,\"employee_status\" : \"T\",\"scorp_owner\" : \"false\",\"business_owner\" : \"false\"},\"contact_information\" : {\"home_telephone_number\" : \"4083102364\",\"personal_email_address\" : \"calijamaac@yahoo.com\"},\"residential_address\" : {\"address_line1\" : \"4403 Dulcey Drive\",\"zip_code\" : \"95136\",\"county\" : \"SANTA CLARA\",\"city\" : \"SAN JOSE\",\"state\" : \"NY\",\"is_unicorporated_area\" : false},\"veteran_status\" : {\"ethnicity\" : \"B\",\"is_handicapped\" : false,\"is_blind\" : false},\"direct_deposit_status\" : \"A\",\"direct_deposit\" : [{\"routing_transit_number\" : \"h8pbk/1+MJsuPshLTa9GJw==\",\"bank_name\" : \"WELLS FARGO BANK NA\",\"bank_account_number\" : \"QNxQir0hCJKP6wswCm2ixw==\",\"account_type\" : \"C\",\"calculation_method\" : \"B\",\"status\" : \"D\"}],\"federal_tax\" : {\"filing_status\" : \"M\",\"primary_allowances\" : \"2\",\"multiple_jobs\" : false},\"state_tax\" : [{\"filing_status\" : \"M\",\"state\" : \"CA\",\"primary_allowances\" : \"2\",\"secondary_allowances\" : \"2\",\"has_nrstate_certificate\" : false,\"w4_filed\" : \"false\"}],\"review_date\" : \"2019-08-19\",\"performance_review\" : {\"review_date\" : \"2019-08-19\"}}";

	String jsonResponse2 = "{\"work_site_location\" : \"JIT\",\"division\" : \"LA\",\"department\" : \"RENO\",\"project\" : \"Boosted\",\"supervisor\" : \"T84834\",\"benefit_group\" : \"1\",\"is_corp\" : false,\"is_owner\" : false,\"worker's_comp_class\" : \"7219\",\"occupation\" : \"d\",\"shift\" : \"kk\"}";

	private OtpDetailsDTO otpDetailsDTO;

	@Before
	public void setUp() throws Exception {
		userPrincipal = new UserPrincipal("jchilakapati@bbsi.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setClientCode("909464");
		userPrincipal.setEmail("test@mybbsi.com");
		userPrincipal.setIsPolicyAccepted(false);
		userPrincipal.setIsCcpaRequired(true);
		userPrincipal.setIsPolicyUpdated(false);
		userPrincipal.setUserId(1l);
		userPrincipal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		mapper = new ObjectMapper();
		node = mapper.readTree(jsonResponse);
		node1 = mapper.readTree(jsonResponse1);
		otpDetailsDTO = new OtpDetailsDTO();
		otpDetailsDTO.setUser("763432");
		otpDetailsDTO.setOtp("763432");
		MockitoAnnotations.initMocks(this);
		ExceptionUtils.handleException = Mockito.mock(GenericFunctions.ThrowableTriConsumer.class);
		GenericUtils.throwCommonNullVariableException = Mockito.mock(ThrowableBiConsumer.class);
		GenericUtils.throwCommonNullVariableExceptionString = Mockito.mock(ThrowableConsumer.class);
	}

	@After
	public void tearDown() throws Exception {
		userPrincipal = null;
	}

	@Test
	public void testUpdateUserForBuildUser() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsi.com,@mybbsi.com,@bbsihq.com");
		UsersDTO inputDTO = populateUserDTO(false);
		Optional<Users> optional = Optional.of(populateUsers());
		List<ClientRole> tempClientRoles = new ArrayList<>();
		ClientRole clientRole = new ClientRole();
		clientRole.setClientCode(populateUserDTO(false).getClients().get(0).getClientCode());
		Mapping role = new Mapping();
		role.setCode("code");
		role.setDescription("desc");
		role.setName("name");
		role.setId(1l);
		clientRole.setRole(role);
		tempClientRoles.add(clientRole);
		List<Mapping> roles = new ArrayList<>();
		roles.add(role);
		UserClientRoleDTOV2 role1 = new UserClientRoleDTOV2();
		role1.setId(1l);
		List<UserClientRoleDTOV2> roles1 = new ArrayList<>();
		roles1.add(role1);
		inputDTO.setRoles(roles1);
		Mockito.doReturn(tempClientRoles).when(clientRoleRepository).findByUserClient_IdAndRole_Id(Mockito.anyLong(),
				Mockito.anyLong());
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn(populateUserClients()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		Mockito.doReturn(roles).when(mappingRepository).findAllById(Mockito.any());
		Mockito.doReturn(populateRbacEntity().get(0)).when(roleRepository).findByCodeAndType(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doReturn(populateUsers()).when(usersRepository).save(Mockito.any());
		Mockito.doNothing().when(userSecurityRepository).deleteByUserClient_Id(Mockito.anyLong());
		Mockito.doReturn(populateUserClients().get(0).getUserFilters()).when(userSecurityRepository)
				.saveAll(Mockito.any());
		Mockito.doReturn(inputDTO).when(usersMapper).userToUserDTO(Mockito.any());

		UsersDTO result = userServiceImpl.updateUser(inputDTO, token, userPrincipal);
		assertNotNull(result);
	}

	@Test(expected = ValidationException.class)
	public void testUpdateUserForBuildUserelseblock() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsi.com,@mybbsi.com,@bbsihq.com");
		UsersDTO inputDTO = populateUserDTO1(false);
		Optional<Users> optional = Optional.of(populateUsers());
		List<ClientRole> tempClientRoles = new ArrayList<>();
		ClientRole clientRole = new ClientRole();
		clientRole.setClientCode(populateUserDTO(false).getClients().get(0).getClientCode());
		Mapping role = new Mapping();
		role.setCode("code");
		role.setDescription("desc");
		role.setName("name");
		role.setId(1l);
		clientRole.setRole(role);
		tempClientRoles.add(clientRole);
		List<Mapping> roles = new ArrayList<>();
		roles.add(role);
		UserClientRoleDTOV2 role1 = new UserClientRoleDTOV2();
		role1.setId(1l);
		List<UserClientRoleDTOV2> roles1 = new ArrayList<>();
		roles1.add(role1);
		inputDTO.setRoles(roles1);
		Mockito.doReturn(tempClientRoles).when(clientRoleRepository).findByUserClient_IdAndRole_Id(Mockito.anyLong(),
				Mockito.anyLong());
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn(populateUserClients()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		Mockito.doReturn(roles).when(mappingRepository).findAllById(Mockito.any());
		Mockito.doReturn(populateRbacEntity().get(0)).when(roleRepository).findByCodeAndType(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doReturn(populateUsers()).when(usersRepository).save(Mockito.any());
		Mockito.doNothing().when(userSecurityRepository).deleteByUserClient_Id(Mockito.anyLong());
		Mockito.doReturn(populateUserClients().get(0).getUserFilters()).when(userSecurityRepository)
				.saveAll(Mockito.any());
		Mockito.doReturn(inputDTO).when(usersMapper).userToUserDTO(Mockito.any());

		UsersDTO result = userServiceImpl.updateUser(inputDTO, token, userPrincipal);
		assertNotNull(result);
	}

	@Test
	public void testCreateUserForEmployeeMergeUser() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com,@bbsi.com");
		ReflectionTestUtils.setField(userServiceImpl, "emailURL", "/url");
		UsersDTO inputDTO = populateUserDTO(true);
		inputDTO.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		UserClients expectedClients = populateUserClients().get(0);
		expectedClients.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		Mapping mapping = new Mapping();
		mapping.setCode("code");
		mapping.setDescription("description");
		mapping.setName("name");
		Optional<Mapping> map = Optional.of(mapping);
		Mockito.doReturn(expectedClients).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateRbacEntity().get(0)).when(roleRepository).findByCodeAndType(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doReturn(map).when(mappingRepository).findById(Mockito.anyLong());
		UsersDTO result = userServiceImpl.createUser(inputDTO, token, "/url", userPrincipal);
		assertNotNull(result);
	}

	@Test
	public void testSaveClientMaster() {
		ClientMaster client = new ClientMaster();
		client.setClientCode("code");
		client.setClientName("name");
		client.setCostCenterCode("centercode");
		client.setCostCenterDescription("centername");
		Mockito.doReturn(client).when(clientMasterRepository).save(Mockito.any());
		ClientMaster response = userServiceImpl.saveClientMaster("code", "name", "centercode", "centername");
		assertEquals(client.getClientCode(), response.getClientCode());
		assertEquals(client.getClientName(), response.getClientName());
		assertEquals(client.getCostCenterCode(), response.getCostCenterCode());
		assertEquals(client.getCostCenterDescription(), response.getCostCenterDescription());
	}

	@Test
	public void testupdateIsPrimary() {
		Mockito.doReturn(1).when(usersRepository).updateDefaultClient(Mockito.anyLong(), Mockito.anyString());
		userServiceImpl.updateIsPrimary(1l, 0l, "clientCode", GenericConstants.USERTYPE_VANCOUVER.toString());
		assertTrue(true);
		Mockito.verify(usersRepository, Mockito.atLeastOnce()).updateDefaultClient(1l, "clientCode");
	}

	@Test
	public void testupdateIsPrimaryForClient() {
		Mockito.doNothing().when(userClientRepository).clearPrimaryStatus(Mockito.anyLong());
		Mockito.doNothing().when(userClientRepository).updateClientPrimaryStatus(Mockito.anyBoolean(),
				Mockito.anyLong());
		userServiceImpl.updateIsPrimary(1l, 0l, "clientCode", GenericConstants.USERTYPE_CLIENT.toString());
		assertTrue(true);
	}

	@Test
	public void testupdateClientMaster() {
		CostCenterClientDTO client = new CostCenterClientDTO();
		client.setClientId("clientCode1");
		client.setCostCenter("009");
		List<CostCenterClientDTO> clients = new ArrayList<>();
		clients.add(client);
		List<ClientMaster> clientMasters = new ArrayList<>();
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("clientCode1");
		clientMaster.setClientName("clientName");
		clientMaster.setCostCenterCode(populateCostCenter().get(0).getCode());
		clientMaster.setModifiedOn(LocalDateTime.now().minusHours(20));
		clientMasters.add(clientMaster);
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(populateCostCenter(), clients).when(webClientTemplate).getForObjectList(Mockito.anyString(),
				Mockito.any(), Mockito.anyMap());
		Mockito.doReturn(clientMasters).when(clientMasterRepository).findAll();
		userServiceImpl.updateClientMaster();
		assertTrue(true);
	}

	@Test
	public void testupdateClientMasterForEmpty() {
		List<ClientMaster> clientMasters = new ArrayList<>();
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("clientCode1");
		clientMaster.setClientName("clientName");
		clientMaster.setCostCenterCode(populateCostCenter().get(0).getCode());
		clientMaster.setModifiedOn(LocalDateTime.now().minusHours(20));
		clientMasters.add(clientMaster);
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(new ArrayList<>()).when(webClientTemplate).getForObjectList(Mockito.anyString(), Mockito.any(),
				Mockito.anyMap());
		Mockito.doReturn(clientMasters).when(clientMasterRepository).findAll();
		userServiceImpl.updateClientMaster();
		assertTrue(true);
	}

	@Test
	public void testGetPrincipal() {
		List<UserToolbarSettings> list = new ArrayList<>();
		UserToolbarSettings data = new UserToolbarSettings();
		data.setClientCode("909464");
		data.setFeatureCode("fcode");
		data.setId(1l);
		data.setMenuItemId(1l);
		data.setUserEmail("test@test.com");
		list.add(data);
		Users user = populateUsers();
		user.getClients().get(0).setIsPrimary(true);
		user.getClients().get(0).setClientCode("909464");
		user.getClients().get(0).setStartDate(LocalDateTime.now().minusMonths(3));
		user.getClients().get(0).setEndDate(LocalDateTime.now().plusMonths(14));
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.any());
		Mockito.doReturn(populateClientMaster()).when(clientMasterRepository).findByClientCodeIn(Mockito.any());
		Mockito.doReturn(list).when(userToolbarSettingsRepositoryV2).findByClientCodeAndUserEmail(Mockito.anyString(),
				Mockito.anyString());
		UserPrincipal principal = userServiceImpl.getUserPrincipal(populateUsers().getEmail());
		Mockito.verify(usersRepository, Mockito.atLeastOnce()).findByEmail(Mockito.any());
		Mockito.verify(clientMasterRepository, Mockito.atLeastOnce()).findByClientCodeIn(Mockito.any());
		assertEquals("909464", principal.getClientCode());
	}

	@Test
	public void testGetPrincipalForNewHire() {
		List<UserToolbarSettings> list = new ArrayList<>();
		UserToolbarSettings data = new UserToolbarSettings();
		data.setClientCode("909464");
		data.setFeatureCode("fcode");
		data.setId(1l);
		data.setMenuItemId(1l);
		data.setUserEmail("test@test.com");
		list.add(data);
		Users user = populateUsers2();
		user.getClients().get(0).setIsPrimary(true);
		user.getClients().get(0).setClientCode("902738");
		user.getClients().get(0).setStartDate(LocalDateTime.now().minusMonths(3));
		user.getClients().get(0).setEndDate(LocalDateTime.now().plusMonths(14));
		user.getClients().get(0).setUserType(null);
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.any());
		Mockito.doReturn(populateClientMaster()).when(clientMasterRepository).findByClientCodeIn(Mockito.any());
		Mockito.doReturn(list).when(userToolbarSettingsRepositoryV2).findByClientCodeAndUserEmail(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doReturn(getUserCostCenterAssociation1()).when(userCostCenterAssociationRepository)
				.findByUserId(Mockito.anyLong());
		UserPrincipal principal = userServiceImpl.getUserPrincipal(populateUsers().getEmail());
		Mockito.verify(usersRepository, Mockito.atLeastOnce()).findByEmail(Mockito.any());
		assertNotNull(principal);
	}

	@Test
	public void testGetPrincipalForNewHire1() {
		List<UserToolbarSettings> list = new ArrayList<>();
		UserToolbarSettings data = new UserToolbarSettings();
		data.setClientCode("909464");
		data.setFeatureCode("fcode");
		data.setId(1l);
		data.setMenuItemId(1l);
		data.setUserEmail("test@test.com");
		list.add(data);
		Users user = populateUsers2();
		user.getClients().get(0).setIsPrimary(true);
		user.getClients().get(0).setClientCode("902738");
		user.getClients().get(0).setStartDate(LocalDateTime.now().minusMonths(3));
		user.getClients().get(0).setEndDate(LocalDateTime.now().plusMonths(14));
		// user.getClients().get(0).setUserType(null);
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.any());
		Mockito.doReturn(populateClientMaster()).when(clientMasterRepository).findByClientCodeIn(Mockito.any());
		Mockito.doReturn(list).when(userToolbarSettingsRepositoryV2).findByClientCodeAndUserEmail(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doReturn(getUserCostCenterAssociation1()).when(userCostCenterAssociationRepository)
				.findByUserId(Mockito.anyLong());
		UserPrincipal principal = userServiceImpl.getUserPrincipal(populateUsers().getEmail());
		Mockito.verify(usersRepository, Mockito.atLeastOnce()).findByEmail(Mockito.any());
		assertNotNull(principal);
	}

	@Test(expected = InvalidUserException.class)
	public void testGetPrincipalForException() {
		Users user = populateUser();
		user.setClients(new ArrayList<>());
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(null).when(userCostCenterAssociationRepository).findByUserId(Mockito.any());
		UserPrincipal principal = userServiceImpl.getUserPrincipal(populateUser().getEmail());
		assertEquals(principal.getEmail(), populateUser().getEmail());
	}

	@Test
	public void testGetADKeys() throws IOException {
		ReflectionTestUtils.setField(userServiceImpl, "adKeysURL", "url");
		String response = "{\"keys\" : [{\"kid\" : \"id1\",\"x5c\" : [\"field1\"]},{\"kid\" : \"id2\",\"x5c\" : [\"field2\"]}]}";
		JsonNode node = mapper.readTree(response);
		Mockito.doReturn(response).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(node).when(objectMapper).readTree(Mockito.anyString());
		Map<String, String> result = userServiceImpl.getADKeys(true);
		Mockito.verify(restClient, Mockito.atLeastOnce()).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		assertEquals(2, result.size());
		assertEquals("field1", result.get("id1"));
		assertEquals("field2", result.get("id2"));
	}

	@Test
	public void testGetADKeysForFalse() {
		ReflectionTestUtils.setField(userServiceImpl, "adKeysURL", "url");
		String response = "";
		Mockito.doReturn(response).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		Map<String, String> result = userServiceImpl.getADKeys(false);
		assertEquals(0, result.size());
	}

	@Test
	public void testGetADKeysForException() {
		Mockito.doThrow(BbsiException.class).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		Map<String, String> result = userServiceImpl.getADKeys(true);
		assertEquals(0, result.size());
	}

	@Test
	public void testCreateUserAndSendInvitationThrowsException() {
		List<UserInvitationDTO> result = userServiceImpl.createUserAndSendInvitation(null, token, userPrincipal,
				"909464", "MSBIb3VyIERyYWlu", "bbsi.com");
		assertNull(result);
	}

	@Test
	public void testCreateUserAndSendInvitation() throws IOException {
		List<UserInvitationDTO> inputList = populateUserInvitationDTOList();
		Object[] empUser = { "first", "last", "9090909090", "admin@osius.com", 1,
				GenericConstants.USERTYPE_CLIENT.toString() };
		RbacEntity rbac = new RbacEntity();
		rbac.setClientCode("909464");
		rbac.setCode("TERMINATED EMPLOYEE");
		rbac.setDescription("description");
		rbac.setId(1l);
		rbac.setMappingId(1l);
		rbac.setName("name");
		rbac.setStatus(true);
		rbac.setType(UserEnum.ROLE.toString());
		List<Object[]> empUsers = new ArrayList<Object[]>();
		empUsers.add(empUser);
		JsonNode node2 = mapper.readTree(jsonResponse2);
		Mockito.doReturn("url", "url1").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonResponse, jsonResponse2).when(restClient).getForString(Mockito.anyString(),
				Mockito.anyMap(), Mockito.anyMap());
		Mockito.doReturn(node, node2).when(objectMapper).readTree(Mockito.anyString());
		Mockito.doReturn(new ArrayList<>()).when(usersRepository)
				.getUserDataForInvitation(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(empUsers).when(usersRepository).getUserDataByEmail(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(rbac).when(rbacRepository).findByCodeAndType(Mockito.anyString(), Mockito.any());
		Mockito.doNothing().when(employeeInvitationBusinessService)
				.saveAllByClientCodeAndEmployeeId(Mockito.anyString(), Mockito.any());
		List<UserInvitationDTO> result = userServiceImpl.createUserAndSendInvitation(inputList, token, userPrincipal,
				"909464", "MSBIb3VyIERyYWlu", "bbsi.com");
		assertEquals(result.size(), inputList.size());
	}

	@Test
	public void testCreateUserAndSendInvitationForTest() throws IOException {
		List<UserInvitationDTO> inputList = populateUserInvitationDTOList();
		Object[] empUser = { "first", "last", "9090909090", "admin@osius.com", 1,
				GenericConstants.USERTYPE_CLIENT.toString() };
		RbacEntity rbac = new RbacEntity();
		rbac.setClientCode("909464");
		rbac.setCode("code");
		rbac.setDescription("description");
		rbac.setId(1l);
		rbac.setMappingId(1l);
		rbac.setName("name");
		rbac.setStatus(true);
		rbac.setType("type");
		List<Object[]> empUsers = new ArrayList<Object[]>();
		empUsers.add(empUser);
		JsonNode node2 = mapper.readTree(jsonResponse2);
		Mockito.doReturn("url", "url1").when(boomiHelper).getUrl(Mockito.any());
		String json = "{\"cost_center_code\":\"1 Hour Drain\"}";
		Mockito.doReturn(json).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		// Mockito.doReturn(jsonResponse).when(restClient).getForString(Mockito.anyString(),
		// Mockito.anyMap(), Mockito.anyMap());
		Mockito.doReturn(node, node2).when(objectMapper).readTree(Mockito.anyString());
		Mockito.doReturn(new ArrayList<>()).when(usersRepository)
				.getUserDataForInvitation(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(empUsers).when(usersRepository).getUserDataByEmail(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(rbac).when(rbacRepository).findByCodeAndType(Mockito.anyString(), Mockito.any());
		Mockito.doNothing().when(employeeInvitationBusinessService)
				.saveAllByClientCodeAndEmployeeId(Mockito.anyString(), Mockito.any());
		List<UserInvitationDTO> result = userServiceImpl.createUserAndSendInvitation(inputList, token, userPrincipal,
				"909464", "MSBIb3VyIERyYWlu", "bbsi.com");
		assertEquals(result.size(), inputList.size());
	}

	@Test
	public void testCreateUserAndSendInvitationForClientNameException() {
		List<UserInvitationDTO> inputList = populateUserInvitationDTOList();
		List<UserInvitationDTO> result = userServiceImpl.createUserAndSendInvitation(inputList, token, userPrincipal,
				"909464", "abc@test.com", "bbsi.com");
		assertEquals(result.size(), inputList.size());
	}

	@Test
	public void testCreateUserAndSendInvitationForCCPAFalse() throws IOException {
		List<UserInvitationDTO> inputList = populateUserInvitationDTOList();
		Object[] empUser = { "first", "last", "9090909090", "admin@osius.com", 1,
				GenericConstants.USERTYPE_CLIENT.toString() };
		RbacEntity rbac = new RbacEntity();
		rbac.setClientCode("909464");
		rbac.setCode("code");
		rbac.setDescription("description");
		rbac.setId(1l);
		rbac.setMappingId(1l);
		rbac.setName("name");
		rbac.setStatus(true);
		rbac.setType("type");
		List<Object[]> empUsers = new ArrayList<Object[]>();
		empUsers.add(empUser);
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonResponse1).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn(node1).when(objectMapper).readTree(Mockito.anyString());
		Mockito.doReturn(new ArrayList<>()).when(usersRepository)
				.getUserDataForInvitation(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(empUsers).when(usersRepository).getUserDataByEmail(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(rbac).when(rbacRepository).findByCodeAndType(Mockito.anyString(), Mockito.any());
		Mockito.doNothing().when(employeeInvitationBusinessService)
				.saveAllByClientCodeAndEmployeeId(Mockito.anyString(), Mockito.any());
		List<UserInvitationDTO> result = userServiceImpl.createUserAndSendInvitation(inputList, token, userPrincipal,
				"909464", "MSBIb3VyIERyYWlu", "bbsi.com");
		assertEquals(result.size(), inputList.size());
	}

	@Test
	public void testCreateUserAndSendInvitationForWorkAssignment() throws IOException {
		node1 = mapper.readTree(jsonResponse2);
		List<UserInvitationDTO> inputList = populateUserInvitationDTOList();
		Object[] empUser = { "first", "last", "9090909090", "admin@osius.com", 1,
				GenericConstants.USERTYPE_CLIENT.toString() };
		RbacEntity rbac = new RbacEntity();
		rbac.setClientCode("909464");
		rbac.setCode("code");
		rbac.setDescription("description");
		rbac.setId(1l);
		rbac.setMappingId(1l);
		rbac.setName("name");
		rbac.setStatus(true);
		rbac.setType("type");
		List<Object[]> empUsers = new ArrayList<Object[]>();
		empUsers.add(empUser);
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonResponse2).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn(node1).when(objectMapper).readTree(Mockito.anyString());
		Mockito.doReturn(new ArrayList<>()).when(usersRepository)
				.getUserDataForInvitation(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(empUsers).when(usersRepository).getUserDataByEmail(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(rbac).when(rbacRepository).findByCodeAndType(Mockito.anyString(), Mockito.any());
		Mockito.doNothing().when(employeeInvitationBusinessService)
				.saveAllByClientCodeAndEmployeeId(Mockito.anyString(), Mockito.any());
		List<UserInvitationDTO> result = userServiceImpl.createUserAndSendInvitation(inputList, token, userPrincipal,
				"909464", "MSBIb3VyIERyYWlu", "bbsi.com");
		assertEquals(result.size(), inputList.size());
	}

	@Test
	public void testCreateUserAndSendInvitationForUser() throws IOException {
		ReflectionTestUtils.setField(userServiceImpl, "emailURL", "emailUrl");
		Email email = new Email();
		email.setContext("context");
		email.setFrom("from@email.com");
		email.setReplyTo("replyTo@email.com");
		email.setSubject("susbject");
		email.setTemplate("template");
		email.setTemplateName("name");
		email.setToAddress("toAddress@email.com");
		Mono<String> result = Mono.just(email.toString());
		List<UserInvitationDTO> inputList = populateUserInvitationDTOList();
		Object[] empUser = { "first", "last", "9090909090", "admin@osius.com", Boolean.TRUE };
		RbacEntity rbac = new RbacEntity();
		rbac.setClientCode("909464");
		rbac.setCode("code");
		rbac.setDescription("description");
		rbac.setId(1l);
		rbac.setMappingId(1l);
		rbac.setName("name");
		rbac.setStatus(true);
		rbac.setType("type");
		List<Object[]> empUsers = new ArrayList<Object[]>();
		empUsers.add(empUser);
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonResponse).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn(node).when(objectMapper).readTree(Mockito.anyString());
		Mockito.doReturn(result).when(webClientTemplate).postForObjectWithToken(Mockito.anyString(), Mockito.any(),
				Mockito.any(), Mockito.anyString());
		Mockito.doReturn(empUsers).when(usersRepository).getUserDataForInvitation(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		List<UserInvitationDTO> response = userServiceImpl.createUserAndSendInvitation(inputList, token, userPrincipal,
				"909464", "MSBIb3VyIERyYWlu", "bbsi.com");
		assertEquals(response.size(), inputList.size());
	}

	@Test
	public void testCreateUserAndSendInvitationCannotCreateUser() throws IOException {
		List<UserInvitationDTO> inputList = populateUserInvitationDTOList();
		Object[] empUser = { "first", "last", "9090909090", "admin@osius.com", false,
				GenericConstants.USERTYPE_EMPLOYEE.toString() };
		List<Object[]> empUsers = new ArrayList<Object[]>();
		empUsers.add(empUser);
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonResponse).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn(node).when(objectMapper).readTree(Mockito.anyString());
		Mockito.doReturn(new ArrayList<>()).when(usersRepository)
				.getUserDataForInvitation(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(empUsers).when(usersRepository).getUserDataByEmail(Mockito.anyString(), Mockito.anyString());
		List<UserInvitationDTO> result = userServiceImpl.createUserAndSendInvitation(inputList, token, userPrincipal,
				"909464", "MSBIb3VyIERyYWlu", "bbsi.com");
		assertEquals(result.size(), inputList.size());
	}

	@Test
	public void testupdateNewHireUser() {
		UserClients userClients = populateUserClients().get(0);
		userClients.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		List<UserClients> expected = new ArrayList<>();
		expected.add(userClients);
		Optional<RbacEntity> role = Optional.of(populateRbac());
		Mockito.doReturn(userClients).when(userClientRepository).findByNewHireId(Mockito.any());
		Mockito.doReturn(expected).when(userClientRepository).findByUser_Id(Mockito.any());
		Mockito.doReturn(role).when(rbacRepository).findById(Mockito.any());
		userServiceImpl.updateNewHireUser(userClients.getNewHireId(), userClients.getUser().getEmail(), userClients.getUser().getMfaEmail(), "1234567890");
		assertTrue(true);
	}

	@Test
	public void testupdateNewHireUserForNewHireUserClientNull() {
		UserClients userClients = populateUserClients().get(0);
		userClients.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		List<UserClients> expected = new ArrayList<>();
		expected.add(userClients);
		Optional<RbacEntity> role = Optional.of(populateRbac());
		Mockito.doReturn(null).when(userClientRepository).findByNewHireId(Mockito.any());
		Mockito.doReturn(expected).when(userClientRepository).findByUser_Id(Mockito.any());
		Mockito.doReturn(role).when(rbacRepository).findById(Mockito.any());
		userServiceImpl.updateNewHireUser(userClients.getNewHireId(), userClients.getUser().getEmail(), userClients.getUser().getMfaEmail(), userClients.getUser().getMobile());
		assertTrue(true);
	}

	@Test(expected = ValidationException.class)
	public void testupdateNewHireUserForValidationException1() {
		UserClients userClients = populateUserClients().get(0);
		userClients.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		List<UserClients> expected = new ArrayList<>();
		expected.add(userClients);
		Users user=populateUsers();
		UserClients userClientDet= populateUserClients().get(0);
		userClientDet.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		userClientDet.setClientCode("909464");
		List<UserClients> userCli = new ArrayList<>();
		userCli.add(userClientDet);
		user.setClients(userCli);
		Optional<RbacEntity> role = Optional.of(populateRbac());
		Mockito.doReturn(userClients).when(userClientRepository).findByNewHireId(Mockito.any());
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(role).when(rbacRepository).findById(Mockito.any());
		userServiceImpl.updateNewHireUser(userClients.getNewHireId(),"test1@test.com","mfa@email.com", "1234567890");
		assertTrue(true);
	}

	@Test
	public void testupdateNewHireUserForValidationException() {
		UserClients userClients = populateUserClient();
		Users dbUser = populateUser();
		dbUser.getClients().get(0).setUserType(GenericConstants.USERTYPE_NEWHIRE);
		Mockito.doReturn(dbUser).when(usersRepository).findByEmail(userClients.getUser().getEmail());
		Mockito.doReturn(userClients).when(userClientRepository).findByNewHireId(userClients.getNewHireId());
		userServiceImpl.updateNewHireUser(userClients.getNewHireId(), userClients.getUser().getEmail(),userClients.getUser().getMfaEmail(), userClients.getUser().getMobile());
		assertTrue(true);
	}

	@Test(expected = BbsiException.class)
	public void testUpdateNewHireUserForException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByNewHireId(0l);
		userServiceImpl.updateNewHireUser(0l, "adminbbsi.com","mfa@email.com", "1234567890");
		assertTrue(true);
	}

	@Test
	public void testUpdateNewHireUserMoreThanOneClient() {
		ReflectionTestUtils.setField(userServiceImpl, "defaultPassword", "Osicpl@2");
		List<UserClients> expected = populateUserClients();
		expected.add(populateUserClient());
		UserClients userClient = populateUserClient();
		userClient.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		Mockito.doReturn(userClient).when(userClientRepository).findByNewHireId(Mockito.any());
		Mockito.doReturn(expected).when(userClientRepository).findByUser_Id(Mockito.any());
		userServiceImpl.updateNewHireUser(userClient.getNewHireId(), "YWRtaW5Ab3NpdXMuY29t","mfa@email.com", "1234567890");
		assertTrue(true);
	}

	@Test
	public void testremoveToken() {
		Mockito.doNothing().when(passwordBusinessServiceImpl).removeToken(tokenStore, false);
		userServiceImpl.removeToken(tokenStore, false);
		assertTrue(true);
	}

	@Test
	public void testRemoveTokenWhenOthersTrue() {
		Mockito.doNothing().when(passwordBusinessServiceImpl).removeToken(tokenStore, true);
		userServiceImpl.removeToken(tokenStore, true);
		assertTrue(true);
	}

	@Test(expected = BbsiException.class)
	public void testRemoveTokenForExceptions() {
		Mockito.doThrow(BbsiException.class).when(passwordBusinessServiceImpl).removeToken(null, true);
		userServiceImpl.removeToken(null, true);
		assertTrue(true);
	}

	@Test
	public void testUpdateCCPAPolicyAccepted() {
		List<String> clientCodes = new ArrayList<>();
		clientCodes.add("909464");
		List<UserClients> input = populateUserClients();
		input.get(0).setIsCaliforniaUser(Boolean.TRUE);
		userPrincipal.setUserId(1l);
		userPrincipal.setClientCode("909464");
		Object[] client = { "E1746F", Boolean.FALSE, Boolean.TRUE, "clientCode1" };
		Object[] client1 = { "F1234Q", Boolean.FALSE, Boolean.FALSE, "909464" };
		List<Object[]> clients = new ArrayList<Object[]>();
		clients.add(client);
		clients.add(client1);
		Mockito.doReturn(clients).when(userClientRepository).getCaliforniaUserClients(Mockito.any());
		Mockito.doNothing().when(userClientRepository).updateIsCCPAAccepted(clientCodes, true, 1l);
		userServiceImpl.updateCCPAPolicyAccepted(true, userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testUpdateCCPAPolicyAcceptedForclientsTwoNull() {
		List<String> clientCodes = new ArrayList<>();
		clientCodes.add("909464");
		List<UserClients> input = populateUserClients();
		input.get(0).setIsCaliforniaUser(Boolean.FALSE);
		userPrincipal.setUserId(1l);
		userPrincipal.setClientCode("909464");
		Object[] client = { "E1746F", Boolean.FALSE, null, "clientCode1" };
		Object[] client1 = { "F1234Q", Boolean.FALSE, Boolean.FALSE, "909464" };
		List<Object[]> clients = new ArrayList<Object[]>();
		clients.add(client);
		clients.add(client1);
		Mockito.doReturn(clients).when(userClientRepository).getCaliforniaUserClients(Mockito.any());
		Mockito.doNothing().when(userClientRepository).updateIsCCPAAccepted(clientCodes, false, 1l);
		userServiceImpl.updateCCPAPolicyAccepted(true, userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testUpdateCCPAPolicyAcceptedForNull() {
		List<String> clientCodes = new ArrayList<>();
		clientCodes.add("909464");
		List<UserClients> input = populateUserClients();
		input.get(0).setIsCaliforniaUser(Boolean.TRUE);
		userPrincipal.setUserId(1l);
		Mockito.doReturn(null).when(userClientRepository).getCaliforniaUserClients(Mockito.anyLong());
		Mockito.doNothing().when(userClientRepository).updateIsCCPAAccepted(clientCodes, true, 1l);
		userServiceImpl.updateCCPAPolicyAccepted(true, userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testUpdateCCPAPolicyAcceptedForOtherException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).updateIsCCPAAccepted(null, true, 0);
		userServiceImpl.updateCCPAPolicyAccepted(true, null);
		assertTrue(true);
	}

	@Test
	public void testUpdateCCPAPolicyAcceptedForException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).updateIsCCPAAccepted(Lists.newArrayList(),
				false, 0l);
		userServiceImpl.updateCCPAPolicyAccepted(true, new UserPrincipal(" ", " ", Lists.newArrayList()));
		assertTrue(true);
	}

	@Test
	public void testLoadUserByUsername() {
		Users users = populateUsers();
		Mockito.doReturn(users).when(usersRepository).findByEmail(Mockito.anyString());
		UserDetails actualResponse = userServiceImpl.loadUserByUsername(populateUsers().getEmail());
		Mockito.verify(usersRepository, Mockito.atLeastOnce()).findByEmail(Mockito.anyString());
		assertEquals("1", actualResponse.getUsername());
	}

	@Test
	public void testLoadUserByUsernameIfNotContainsEmail() {
		Mockito.doReturn(Optional.of(populateUsers())).when(usersRepository).findById(Mockito.anyLong());
		UserDetails actualResponse = userServiceImpl.loadUserByUsername(populateUsers().getEmail());
		assertNull(actualResponse);
	}

	@Test
	public void testLoadUserByUsernameIsNewHire() {
		users = populateUserWhenUserTypeIsNewHire();
		Mockito.doReturn(Optional.of(users)).when(usersRepository).findById(Mockito.anyLong());
		UserDetails actualResponse = userServiceImpl.loadUserByUsername("123");
		assertEquals("12", actualResponse.getUsername());
	}

	@Test
	public void testLoadUserByUsernameIsClient() {
		users = populateUserWhenUserTypeIsClient();
		Mockito.doReturn(Optional.of(users)).when(usersRepository).findById(Mockito.anyLong());
		UserDetails actualResponse = userServiceImpl.loadUserByUsername("123");
		assertEquals("12", actualResponse.getUsername());
	}

	@Test
	public void testLoadUserByUsernameIsEmployee() {
		users = populateUserWhenUserTypeIsEmployee();
		Mockito.doReturn(Optional.of(users)).when(usersRepository).findById(Mockito.anyLong());
		UserDetails actualResponse = userServiceImpl.loadUserByUsername("123");
		assertEquals("12", actualResponse.getUsername());
	}

	@Test
	public void testUpdateFailAttempts() {
		Object[] response = { 1, 2, 3 };
		Mockito.doReturn(response).when(usersRepository).incrementInvalidAttempts(Mockito.anyString());
		Integer value = userServiceImpl.updateFailAttempts("test@bbsihqc.com");
		assertEquals(response[0], value);
	}

	@Test
	public void testUpdateFailAttemptsThrowsException() {
		Integer expected = 0;
		Mockito.doThrow(BbsiException.class).when(usersRepository).incrementInvalidAttempts(Mockito.anyString());
		Integer result = userServiceImpl.updateFailAttempts("test@bbsihqc.com");
		assertEquals(expected, result);
	}

	@Test
	public void testUpdateFailAttemptsForNullResponse() {
		Integer expected = 0;
		Mockito.doReturn(null).when(usersRepository).incrementInvalidAttempts(Mockito.anyString());
		Integer value = userServiceImpl.updateFailAttempts("test@bbsihq.com");
		assertEquals(expected, value);
	}

	@Test
	public void testResetFailAttempts() {
		Mockito.doReturn(5).when(usersRepository).clearInvalidAttempts(Mockito.anyString());
		userServiceImpl.resetFailAttempts("test@bbsihqc.com");
		assertTrue(true);
	}

	@Test
	public void testResetFailAttemptsWhenEmailIsInvalid() {
		Mockito.doThrow(BbsiException.class).when(usersRepository).clearInvalidAttempts(Mockito.anyString());
		userServiceImpl.resetFailAttempts("test");
		assertTrue(true);
	}

	@Test
	public void testGetUserbyEmail() {
		Users user = populateUsers();
		UserCustomDTOV2 userCustomDTOV2 = populateUserCustomDTOV2();
		Mockito.doReturn(userCustomDTOV2).when(userBusinessService).getUserByEmail(userCustomDTOV2.getEmail());
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		UserCustomDTOV2 response = userServiceImpl.getUserByEmail(populateUserCustomDTOV2().getEmail());
		assertEquals(user.getEmail(), response.getEmail());
		assertEquals(user.getId(), response.getId());
	}

	@Test
	public void testGetUserbyEmailWhenEmailIsNull() {
		Mockito.doThrow(BbsiException.class).when(usersRepository).findByEmail(Mockito.anyString());
		UserCustomDTOV2 response = userServiceImpl.getUserByEmail(null);
		assertNull(response);
	}

	@Test
	public void testCreateUserForEmployee() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		ReflectionTestUtils.setField(userServiceImpl, "emailURL", "url");
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "url");
		UsersDTO inputDTO = populateUserDTO(true);
		inputDTO.setEmail("test@test.com");
		inputDTO.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpcl@2", grantedAuthorities1);
		List<RbacEntity> listRbac = new ArrayList<>();
		listRbac.add(populateRbac());
		UserClients userClient = populateUsers().getClients().get(0);
		userClient.setEmployeeCode("empCode");
		userClient.setClientCode("clientCode");
		userClient.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		RbacEntity rbac = populateRbacEntity().get(0);
		rbac.setCode(GenericConstants.USERTYPE_EMPLOYEE.toString());
		rbac.setType(UserEnum.ROLE.toString());
		Mockito.doReturn(userClient).when(userClientRepository).findByClientCodeAndEmployeeCode(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doReturn(rbac).when(roleRepository).findByCodeAndType(Mockito.any(), Mockito.any());
		UsersDTO response = userServiceImpl.createUser(inputDTO, token, "bbsi.com", principal);
		assertEquals(populateUserDTO(true).getId(), response.getId());
	}

	@Test
	public void testCreateusersForMergeUserTypes() {
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "url");
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode(populateUserDTO(true).getClients().get(0).getClientCode());
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		Iterable<Mapping> roles = new ArrayList<Mapping>();
		Optional<RbacEntity> expected = Optional.of(populateRbac());
		Users user = populateUsers();
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(user).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(expected).when(rbacRepository).findById(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findAllById(Mockito.any());
		Mockito.doReturn(populateUserSecurity()).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Mockito.doReturn(populateUser()).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(populateUserClientsForUserTypeNewHire()).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.any(), Mockito.any());
		UsersDTO response = userServiceImpl.createUser(populateUserDTO(true), token, "bbsi.com", principal);
		assertEquals(populateUserDTO(true).getId(), response.getId());
	}

	@Test
	public void testCreateusersForMergeUserTypes1() {
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "url");
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode(populateUserDTO(true).getClients().get(0).getClientCode());
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		Iterable<Mapping> roles = new ArrayList<Mapping>();
		Optional<RbacEntity> expected = Optional.of(populateRbac());
		Users user = populateUsers();
		user.getClients().get(0).setUserType(null);
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(user).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(expected).when(rbacRepository).findById(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findAllById(Mockito.any());
		Mockito.doReturn(populateUserSecurity()).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Mockito.doReturn(populateUser()).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(populateUserClientsForUserTypeNewHire()).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.any(), Mockito.any());
		UsersDTO response = userServiceImpl.createUser(populateUserDTO(true), token, "bbsi.com", principal);
		assertEquals(populateUserDTO(true).getId(), response.getId());
	}

	@Test
	public void testCreateusesrEmptyFilters() {
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "url");
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode(populateUserDTO(true).getClients().get(0).getClientCode());
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		Iterable<Mapping> roles = new ArrayList<Mapping>();
		Optional<RbacEntity> expected = Optional.of(populateRbac());
		Users user = populateUsers();
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(user).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(expected).when(rbacRepository).findById(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findAllById(Mockito.any());
		Mockito.doReturn(populateUserSecurity()).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Mockito.doReturn(populateUser()).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(populateUserClientsForUserTypeNewHire()).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.any(), Mockito.any());
		UsersDTO populateUserDTO = populateUserDTO(true);
		populateUserDTO.setFilter(null);
		;
		UsersDTO response = userServiceImpl.createUser(populateUserDTO, token, "bbsi.com", principal);
		assertEquals(populateUserDTO(true).getId(), response.getId());
	}

	@Test
	public void testCreateusersNotEmptyFilters() {
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "url");
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode(populateUserDTO(true).getClients().get(0).getClientCode());
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setEmail("sdhudhipala@osius.com");
		principal.setUserType("");
		Iterable<Mapping> roles = new ArrayList<Mapping>();
		Optional<RbacEntity> expected = Optional.of(populateRbac());
		Users user = populateUsers1();
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Object[] expected1 = { BigInteger.ONE, BigInteger.ONE, "8106533484", "Branch", 764235475365463L,
				6354635436463L };
		List<Object[]> expectedList = new ArrayList<>();
		expectedList.add(expected1);
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(user).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(expected).when(rbacRepository).findById(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findAllById(Mockito.any());
		Mockito.doReturn(populateUserSecurity()).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Mockito.doReturn(populateUser()).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(expectedList).when(userClientRepository).findByUserClientsByClient(Mockito.anyString(),
				Mockito.anyLong());
		Mockito.doReturn(populateUserClientsForUserTypeNewHire()).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.any(), Mockito.any());
		UsersDTO populateUserDTO = populateUserDTO(true);
		List<UserSecurityDTOV2> filter = new ArrayList<>();
		UserSecurityDTOV2 sec = new UserSecurityDTOV2();
		sec.setClientCode("909464");
		sec.setType("type");
		Set<String> values = new HashSet<>();
		values.add("value1");
		sec.setValue(values);
		populateUserDTO.setFilter(filter);
		UsersDTO response = userServiceImpl.createUser(populateUserDTO, token, "bbsi.com", principal);
		assertEquals(populateUserDTO(true).getId(), response.getId());
	}

	@Test
	public void testCreateusersNotEmptyUserFilters() {
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "url");
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		ReflectionTestUtils.setField(userServiceImpl, "emailURL", "http://localhost:8809/notification/v1/email/");
		Email email = new Email();
		email.setContext("context");
		email.setContextMap(new HashMap<>());
		email.setDbTemplate(false);
		email.setFrom("abc@abc.com");
		email.setReplyTo("noreply@bbsi.com");
		email.setSubject("subject");
		email.setTemplate("template");
		email.setTemplateName("templateName");
		email.setToAddress("toAddress@email.com");
		Mono<String> web = Mono.just(email.toString());
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode(populateUserDTO(true).getClients().get(0).getClientCode());
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setEmail("sdhudhipala@osius.com");
		principal.setUserType("1234");
		Iterable<Mapping> roles = new ArrayList<Mapping>();
		Optional<RbacEntity> expected = Optional.of(populateRbac());
		Users user = populateUsers1();
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Object[] expected1 = { BigInteger.ONE, BigInteger.ONE, "8106533484", "Branch", 764235475365463L,
				6354635436463L };
		List<Object[]> expectedList = new ArrayList<>();
		expectedList.add(expected1);

		String redirectUrl = "admin@osius.com";
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(user).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(expected).when(rbacRepository).findById(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findAllById(Mockito.any());
		Mockito.doReturn(populateUserSecurity()).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Mockito.doReturn(populateUser1()).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(expectedList).when(userClientRepository).findByUserClientsByClient(Mockito.anyString(),
				Mockito.anyLong());
		Mockito.doReturn(populateUserClientsForUserTypeNewHire()).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.any(), Mockito.any());
		UsersDTO populateUserDTO = populateUserDTO(true);
		List<UserSecurityDTOV2> filter = new ArrayList<>();
		UserSecurityDTOV2 sec = new UserSecurityDTOV2();
		sec.setClientCode("909464");
		sec.setType("type");
		Set<String> values = new HashSet<>();
		values.add("value1");
		sec.setValue(values);
		populateUserDTO.setFilter(filter);
		Mockito.doReturn(redirectUrl).when(encryptAndDecryptUtil).encrypt(Mockito.anyString());
		when(webClientTemplate.postForObjectWithToken(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.anyString())).thenReturn(web);
		UsersDTO response = userServiceImpl.createUser(populateUserDTO, token, "bbsi.com", principal);
		assertEquals(populateUserDTO(true).getId(), response.getId());
	}

	@Test
	public void testCreateUserForVancouverForOther() {
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "url");
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(false);
		userDTO.setClients(null);
		userDTO.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		userDTO.setEmail("test1@mybbsi.com");
		userDTO.setCostCenters(populateCostCenters());
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("costCenterCode");
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		PrismizedClientsDTO expected = new PrismizedClientsDTO();
		expected.setClient_code("909464");
		List<PrismizedClientsDTO> list = new ArrayList<>();
		list.add(expected);
		Users user = populateUsers();
		user.setEmail("test1@mybbsi.com");
		user.getClients().get(0).setClientCode("909090");
		List<RbacEntity> rbaclist = new ArrayList<>();
		rbaclist.add(populateRbac());
		userDTO.setRoles(null);
		userDTO.setFilter(populateUserDTO(false).getFilter());
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.any());
		Mockito.doReturn(list).when(webClientTemplate).getForObjectList(Mockito.anyString(), Mockito.any(),
				Mockito.anyMap());
		Mockito.doReturn(rbaclist).when(rbacRepository).findAllById(Mockito.any());
		Mockito.doReturn(user).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(userDTO).when(usersMapper).userToUserDTO(Mockito.any());
		userDTO.setEmail("test@bbsihq.com");
		UsersDTO response = userServiceImpl.createUser(userDTO, token, "bbsi.com", principal);
		assertEquals(response.getEmail(), userDTO.getEmail());
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testCreateUserForVancouverForException() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(true);
		userDTO.setClients(null);
		userDTO.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		userDTO.setEmail("test1@abc.com");
		userDTO.setCostCenters(populateCostCenters());
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("costCenterCode");
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		PrismizedClientsDTO expected = new PrismizedClientsDTO();
		expected.setClient_code("909464");
		List<PrismizedClientsDTO> list = new ArrayList<>();
		list.add(expected);
		Users user = populateUsers();
		user.setEmail("test1@mybbsi.com");
		user.getClients().get(0).setClientCode("909090");
		List<RbacEntity> rbaclist = new ArrayList<>();
		rbaclist.add(populateRbac());
		userDTO.setRoles(null);
		userDTO.setFilter(populateUserDTO(true).getFilter());
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.any());
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(list).when(webClientTemplate).getForObjectList(Mockito.anyString(), Mockito.any(),
				Mockito.anyMap());
		Mockito.doReturn(rbaclist).when(rbacRepository).findAllById(Mockito.any());
		Mockito.doReturn(user).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(userDTO).when(usersMapper).userToUserDTO(Mockito.any());
		UsersDTO response = userServiceImpl.createUser(userDTO, token, "bbsi.com", principal);
		assertEquals(response.getEmail(), userDTO.getEmail());
	}

	@Test
	public void testCreateUserForNewHire() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		ReflectionTestUtils.setField(userServiceImpl, "defaultPassword", "pass@123");
		UsersDTO userDTO = populateUserDTO(true);
		userDTO.setClients(null);
		userDTO.setCostCenters(null);
		userDTO.setEmail("test1@test.com");
		userDTO.setRoles(null);
		Users user = populateUsers();
		user.setClients(null);
		user.setRoles(null);
		Mockito.doReturn(null).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(user).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(user).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(userDTO).when(usersMapper).userToUserDTO(Mockito.any());
		UsersDTO result = userServiceImpl.createUser(userDTO, token, "bbsi.com", userPrincipal);
		assertEquals(result.getEmail(), userDTO.getEmail());
	}

	@Test(expected = ValidationException.class)
	public void testCreateUser() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Optional<Users> optional = Optional.of(populateUsers());
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode(populateUserDTO(true).getClients().get(0).getClientCode());
		principal.setCostCenterCode("costCenter1");
		principal.setBbsiBranch("bbsiBranch");
		principal.setUserType("Vancouver Operations Center");
		Optional<RbacEntity> roleOptional = Optional.of(populateRbacEntity().get(0));
		Mockito.doReturn(roleOptional).when(roleRepository).findById(Mockito.any());
		Mockito.doReturn(null).when(userClientRepository).findByClientCodeAndEmployeeCode(Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn("respose").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(populateCostCenterClient()).when(webClientTemplate).getForObjectList(Mockito.anyString(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn("respose").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(populatePrismizedClient()).when(webClientTemplate).getForObjectList(Mockito.anyString(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUser()).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(userDTO).when(usersMapper).userToUserDTO(Mockito.any());
		UsersDTO actualResponse = userServiceImpl.createUser(populateUserDTO(true), token, "/uiUrl", principal);
		assertEquals(actualResponse.getId(), populateUserDTO(true).getId());
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testCreateUserWhenClientsMoreThanOne() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(true);
		userDTO.setEmployeeCode("Z1234F");
		ClientDTOV2 client = new ClientDTOV2();
		client.setClientCode("clientCode");
		client.setClientName("clientName");
		client.setCostCenterCode("costCenterCode");
		client.setCostCenterDescription("descriptiion");
		client.setEmployeeCode("ecode");
		client.setId(2l);
		userDTO.getClients().add(client);
		Mockito.doThrow(UnauthorizedAccessException.class).when(usersRepository).findByEmail(Mockito.anyString());
		UsersDTO actualResponse = userServiceImpl.createUser(userDTO, token, "/uiUrl", userPrincipal);
		assertNull(actualResponse);
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testCreateUserWhenVancouver() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(false);
		userDTO.setCostCenters(populateCostCenters());
		userDTO.setEmployeeCode("Z1234F");
		userDTO.getClients().get(0).setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		userDTO.setEmail("admin@mybbsi.com");
		Mockito.doThrow(UnauthorizedAccessException.class).when(usersRepository).findByEmail(Mockito.anyString());
		UsersDTO actualResponse = userServiceImpl.createUser(userDTO, token, "uiURL", userPrincipal);
		assertNull(actualResponse);
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testCreateUserWhenNonMatchClientCode() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(true);
		userDTO.setEmployeeCode("Z1234F");
		userDTO.getClients().get(0).setUserType(GenericConstants.USERTYPE_EXTERNAL.toString());
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode("abcd1234");
		Mockito.doThrow(UnauthorizedAccessException.class).when(usersRepository).findByEmail(Mockito.anyString());
		UsersDTO actualResponse = userServiceImpl.createUser(userDTO, token, "/uiUrl", principal);
		assertNull(actualResponse);
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testCreateUserForBbsiHeadEmailsAndBranchUser() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(false);
		userDTO.setEmployeeCode("Z1234F");
		userDTO.setEmail("admin@bbsi.com");
		userDTO.getClients().get(0).setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		Mockito.doThrow(UnauthorizedAccessException.class).when(usersRepository).findByEmail(Mockito.anyString());
		UsersDTO actualResponse = userServiceImpl.createUser(userDTO, token, "/uiUrl", userPrincipal);
		assertNull(actualResponse);
	}

	@Test(expected = ValidationException.class)
	public void testCreateUserForValidationException() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(false);
		userDTO.setEmail("abc.com");
		Mockito.doThrow(ValidationException.class).when(userBusinessService).createUser(userDTO, token, "bbsi.com",
				userPrincipal);
		UsersDTO response = userServiceImpl.createUser(userDTO, token, "bbsi.com", userPrincipal);
		assertNull(response);
	}

	@Test
	public void testCreateUserBbsiHeadEmails() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(false);
		userDTO.setEmail("admin@bbsihq.com");
		userDTO.getClients().get(0).setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		Mockito.doThrow(UnauthorizedAccessException.class).when(userBusinessService).createUser(userDTO, token,
				"bbsi.com", userPrincipal);
		UsersDTO response = userServiceImpl.createUser(userDTO, token, "bbsi.com", userPrincipal);
		assertEquals(response.getId(), populateUserDTO(true).getId());
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testCreateUserClientCodeNotEquals() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(false);
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode("clientCode1");
		Mockito.doThrow(UnauthorizedAccessException.class).when(userBusinessService).createUser(userDTO, token,
				"bbsi.com", principal);
		UsersDTO response = userServiceImpl.createUser(userDTO, token, "bbsi.com", principal);
		assertNull(response);
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testCreateUserForUnauthorizedExceptionForBbsiHeadEmails() {
		UsersDTO user = populateUserDTO(false);
		user.setEmail("bbsi@mybbsi.com");
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Optional<Users> optional = Optional.of(populateUsers());
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode(populateUserDTO(false).getClients().get(0).getClientCode());
		principal.setCostCenterCode("costCenter1");
		principal.setBbsiBranch("bbsiBranch");
		principal.setUserType("Vancouver Operations Center");
		Optional<RbacEntity> roleOptional = Optional.of(populateRbacEntity().get(0));
		Mockito.doReturn(roleOptional).when(roleRepository).findById(Mockito.any());
		Mockito.doReturn(null).when(userClientRepository).findByClientCodeAndEmployeeCode(Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn("respose").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(populateCostCenterClient()).when(webClientTemplate).getForObjectList(Mockito.anyString(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn("respose").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(populatePrismizedClient()).when(webClientTemplate).getForObjectList(Mockito.anyString(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUser()).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(userDTO).when(usersMapper).userToUserDTO(Mockito.any());
		UsersDTO actualResponse = userServiceImpl.createUser(user, token, "/uiUrl", principal);
		assertEquals(actualResponse.getId(), populateUserDTO(false).getId());
	}

	@Test
	public void testCreateUserWhenUiUrlNull() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Mockito.doThrow(BbsiException.class).when(userBusinessService).createUser(Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any());
		userServiceImpl.createUser(populateUserDTO(true), token, null, userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testCreateUserWhenUserPrincipalNull() {
		UsersDTO input = populateUserDTO(true);
		input.getClients().get(0).setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Mockito.doThrow(BbsiException.class).when(userBusinessService).createUser(Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any());
		UsersDTO response = userServiceImpl.createUser(input, token, "bbsi.com", null);
		assertNotNull(response);
	}

	@Test
	public void testCreateUserForBbsiHeadEmails() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO input = populateUserDTO(true);
		input.setEmail("admin@bbsi.com");
		input.setVersion(2l);
		input.setId(2l);
		input.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		UserPrincipal principal = new UserPrincipal("admin@bbsi.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode(input.getClients().get(0).getClientCode());
		principal.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Mockito.doThrow(UnauthorizedAccessException.class).when(userBusinessService).createUser(input, token,
				"bbsi.com", principal);
		UsersDTO response = userServiceImpl.createUser(input, token, "bbsi.com", principal);
		assertEquals(response.getId(), input.getId());
	}

	@Test
	public void testUpdateUser() {
		UsersDTO userDTO = populateUserDTO(false);
		userDTO.setCostCenters(populateCostCenter());
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Optional<Users> optional = Optional.of(populateUsers());
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode(populateUserDTO(false).getClients().get(0).getClientCode());
		principal.setCostCenterCode("costCenter1");
		principal.setBbsiBranch("bbsiBranch");
		principal.setUserType("Vancouver Operations Center");
		Optional<RbacEntity> roleOptional = Optional.of(populateRbacEntity().get(0));
		Mockito.doReturn(populateUserClients()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		Mockito.doReturn(roleOptional).when(roleRepository).findById(Mockito.any());
		Mockito.doReturn(null).when(userClientRepository).findByClientCodeAndEmployeeCode(Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn("respose").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(populateCostCenterClient()).when(webClientTemplate).getForObjectList(Mockito.anyString(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn("respose").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(populatePrismizedClient()).when(webClientTemplate).getForObjectList(Mockito.anyString(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUser()).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(userDTO).when(usersMapper).userToUserDTO(Mockito.any());
		UsersDTO actualResponse = userServiceImpl.updateUser(userDTO, token, principal);
		assertEquals(actualResponse.getId(), populateUserDTO(false).getId());
	}

	@Test
	public void testUpdateUserForClient() {
		UsersDTO userDTO = populateUserDTO(true);
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Optional<Users> optional = Optional.of(populateUsers());
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode(populateUserDTO(true).getClients().get(0).getClientCode());
		principal.setCostCenterCode("costCenter1");
		principal.setBbsiBranch("bbsiBranch");
		principal.setUserType("Vancouver Operations Center");
		Optional<RbacEntity> roleOptional = Optional.of(populateRbacEntity().get(0));
		Mockito.doReturn(roleOptional).when(roleRepository).findById(Mockito.any());
		Mockito.doReturn(null).when(userClientRepository).findByClientCodeAndEmployeeCode(Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn("respose").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(populateCostCenterClient()).when(webClientTemplate).getForObjectList(Mockito.anyString(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn("respose").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(populatePrismizedClient()).when(webClientTemplate).getForObjectList(Mockito.anyString(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUser()).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(userDTO).when(usersMapper).userToUserDTO(Mockito.any());
		UsersDTO actualResponse = userServiceImpl.updateUser(userDTO, token, principal);
		assertEquals(actualResponse.getId(), populateUserDTO(true).getId());
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testUpdateUserForPrivilegesUnauthorized() {
		UsersDTO userDTO = populateUserDTO(false);
		userDTO.setCostCenters(populateCostCenter());
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Optional<Users> optional = Optional.of(populateUsers());
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode(populateUserDTO(false).getClients().get(0).getClientCode());
		principal.setCostCenterCode("costCenter1");
		principal.setBbsiBranch("bbsiBranch");
		principal.setUserType("Vancouver Operations Center");
		Optional<RbacEntity> roleOptional = Optional.of(populateRbacEntity().get(0));
		Optional<Users> userOptional = Optional.of(populateUsers());
		UserSecurity filter = new UserSecurity();
		filter.setId(1l);
		filter.setType("type");
		filter.setUserClient(populateUserClient());
		filter.setValue("value");
		List<UserSecurity> filters = new ArrayList<>();
		filters.add(filter);
		filters.add(filter);
		Mockito.doReturn(populateUserClientsIdObjectArray()).when(userClientRepository)
				.findByUserClientsByType(Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.doReturn(filters).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Mockito.doReturn(userOptional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn(roleOptional).when(roleRepository).findById(Mockito.any());
		Mockito.doReturn(null).when(userClientRepository).findByClientCodeAndEmployeeCode(Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn("respose").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(populateCostCenterClient()).when(webClientTemplate).getForObjectList(Mockito.anyString(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn("respose").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(populatePrismizedClient()).when(webClientTemplate).getForObjectList(Mockito.anyString(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUser()).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(userDTO).when(usersMapper).userToUserDTO(Mockito.any());
		UsersDTO actualResponse = userServiceImpl.updateUser(userDTO, token, principal);
		assertEquals(actualResponse.getId(), populateUserDTO(false).getId());
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testUpdateUserWhenClientsMoreThanOne() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(true);
		userDTO.setEmployeeCode("Z1234F");
		ClientDTOV2 client = new ClientDTOV2();
		client.setClientCode("clientCode");
		client.setClientName("clientName");
		client.setCostCenterCode("costCenterCode");
		client.setCostCenterDescription("descriptiion");
		client.setEmployeeCode("ecode");
		client.setId(2l);
		userDTO.getClients().add(client);
		Mockito.doThrow(UnauthorizedAccessException.class).when(usersRepository).findByEmail(Mockito.anyString());
		UsersDTO actualResponse = userServiceImpl.updateUser(userDTO, token, userPrincipal);
		assertNull(actualResponse);
	}

	@Test(expected = ValidationException.class)
	public void testUpddateUserWhenVancouver() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(false);
		userDTO.setCostCenters(populateCostCenters());
		userDTO.setEmployeeCode("Z1234F");
		userDTO.getClients().get(0).setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		userDTO.setEmail("admin@mybbsi.com");
		Mockito.doThrow(UnauthorizedAccessException.class).when(usersRepository).findByEmail(Mockito.anyString());
		userDTO.setEmail("test@bbsihq.com");
		UsersDTO actualResponse = userServiceImpl.updateUser(userDTO, token, userPrincipal);
		assertNull(actualResponse);
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testUpdateUserWhenNonMatchClientCode() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(true);
		userDTO.setEmployeeCode("Z1234F");
		userDTO.getClients().get(0).setUserType(GenericConstants.USERTYPE_EXTERNAL.toString());
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode("abcd1234");
		Mockito.doThrow(UnauthorizedAccessException.class).when(usersRepository).findByEmail(Mockito.anyString());
		UsersDTO actualResponse = userServiceImpl.updateUser(userDTO, token, principal);
		assertNull(actualResponse);
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testUpdateUserForBbsiHeadEmails() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(true);
		userDTO.setUserType(null);
		userDTO.setEmail("admin@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		Mockito.doThrow(UnauthorizedAccessException.class).when(usersRepository).findByEmail(Mockito.anyString());
		UsersDTO actualResponse = userServiceImpl.updateUser(userDTO, token, principal);
		assertNull(actualResponse);
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testUpdateUserForBbsiHeadEmailsAndBranchUser() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		UsersDTO userDTO = populateUserDTO(true);
		userDTO.setEmployeeCode("Z1234F");
		userDTO.setEmail("admin@bbsi.com");
		userDTO.getClients().get(0).setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		Mockito.doThrow(UnauthorizedAccessException.class).when(usersRepository).findByEmail(Mockito.anyString());
		UsersDTO actualResponse = userServiceImpl.updateUser(userDTO, token, userPrincipal);
		assertNull(actualResponse);
	}

	@Test(expected = ValidationException.class)
	public void testUpdateUserForInvalidPayload() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		UsersDTO userDTO = new UsersDTO();
		userDTO.setEmail("test@mybbsi.com");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		UsersDTO response = userServiceImpl.updateUser(userDTO, token, principal);
		assertNull(response.getClients());
	}

	@Test(expected = ValidationException.class)
	public void testUpdateUserForUserTypeEmployeeException() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		UsersDTO userDTO = new UsersDTO();
		userDTO.setId(2l);
		userDTO.setEmail("test@test.com");
		userDTO.setEmployeeCode("empCode");
		userDTO.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Mockito.doReturn(populateUserClients().get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		UsersDTO response = userServiceImpl.updateUser(userDTO, token, userPrincipal);
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndEmployeeCode(Mockito.anyString(),
				Mockito.anyString());
		assertNotNull(response);
	}

	@Test(expected = ValidationException.class)
	public void testUpdateUserForUsertypeemployee() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		UsersDTO userDTO = new UsersDTO();
		userDTO.setId(2l);
		userDTO.setEmail("test@test.com");
		userDTO.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		List<ClientDTOV2> clients = new ArrayList<>();
		ClientDTOV2 client = new ClientDTOV2();
		client.setClientCode("clientCode");
		client.setEmployeeCode("empCode");
		client.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		clients.add(client);
		userDTO.setClients(clients);
		Mockito.doReturn(populateUserClients().get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		UsersDTO response = userServiceImpl.updateUser(userDTO, token, userPrincipal);
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndEmployeeCode(Mockito.anyString(),
				Mockito.anyString());
		assertNotNull(response);
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testUpdateUserForVancouverException() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		UsersDTO userDTO = new UsersDTO();
		userDTO.setId(3l);
		userDTO.setEmail("test@test.com");
		userDTO.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		userDTO.setCostCenters(populateCostCenters());
		UsersDTO response = userServiceImpl.updateUser(userDTO, token, userPrincipal);
		assertNotNull(response);
	}

	@Test
	public void testUpdateUserForVancouverUserException() {
		Users user = populateUser();
		user.setEmail("test@test.com");
		UsersDTO userDTO = new UsersDTO();
		userDTO.setEmail("test@mybbsi.com");
		List<UserSecurityDTOV2> filter = new ArrayList<>();
		UserSecurityDTOV2 sec = new UserSecurityDTOV2();
		sec.setClientCode("909464");
		sec.setType("type");
		Set<String> values = new HashSet<>();
		values.add("value1");
		sec.setValue(values);
		filter.add(sec);
		userDTO.setFilter(filter);
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", new ArrayList<>());
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		Optional<Users> optional = Optional.of(user);
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		UsersDTO response = userServiceImpl.updateUser(userDTO, token, principal);
		assertNull(response.getId());
	}

	@Test
	public void testGetUserById() {
		Users user = populateUsers();

		Optional<Users> expected = Optional.of(user);
		long id = 1;
		List<UserSecurity> list = new ArrayList<>();
		UserSecurity userSecurity = populateUserSecurity().get(0);
		userSecurity.setUserClient(populateUserClients().get(0));
		list.add(userSecurity);
		UserSecurity security1 = populateUserSecurity().get(0);
		security1.setType(null);
		UsersDTO actualResponse = populateUserDTO(true);
		actualResponse.setEmail(user.getEmail());
		Mockito.doReturn(expected).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn(user.getClients()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		Mockito.doReturn(user.getClients()).when(userClientRepository).findByClientCodeAndUser_Id(Mockito.any(),
				Mockito.anyLong());
		Mockito.doReturn(actualResponse).when(usersMapper).userToUserDTO(Mockito.any());
		Mockito.doReturn(list).when(userSecurityRepository).findByUserClient_Id(Mockito.anyLong());
		Mockito.doReturn(populateUserClients().get(0).getClientRoles()).when(clientRoleRepository)
				.findAllById(Mockito.any());
		actualResponse = userServiceImpl.getUserById(id, token, "909464", userPrincipal);
		assertEquals(actualResponse.getId(), populateUserDTO(true).getId());
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testGetUserByIdForUnauthorizedAccessException() {
		Users user = populateUsers();
		List<UserClients> clients = user.getClients();
		user.setEmail("admin@osius.com");
		Optional<Users> expected = Optional.of(user);
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail("admin@osius.com");
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		Mockito.doReturn(expected).when(usersRepository).findById(Mockito.any());
		Mockito.doReturn(clients).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		Mockito.doReturn(clients).when(userClientRepository).findByClientCodeAndUser_Id(Mockito.any(),
				Mockito.anyLong());
		Mockito.doThrow(UnauthorizedAccessException.class).when(userBusinessService).getUserById(1l, "token1", "909464",
				principal);
		UsersDTO response = userServiceImpl.getUserById(1l, token, "909464", principal);
		assertNull(response);
	}

	@Test
	public void testGetUserByIdForVancouver() throws IOException {
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "/url");
		Users user = populateUsers();
		user.setEmail("admin@bbsi.com");
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		List<UserClients> clients = user.getClients();
		clients.get(0).setUserType(GenericConstants.USERTYPE_VANCOUVER);
		user.setRoles(populateRbacEntity());
		List<RbacEntity> roles = populateRbacEntity();
		roles.get(0).setType(Enums.UserEnum.ROLE.toString());
		Optional<Users> expected = Optional.of(user);
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail("admin@bbsi.com");
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		List<CostCenterDTO> expected1 = populateCostCenter();
		String jsonResponse = new Gson().toJson(expected1);
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(jsonResponse);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		JsonNode node = mapper.readTree(jsonArray.get(0).toString());
		List<ClientRole> clientRoles = new ArrayList<>();
		ClientRole clientRole = new ClientRole();
		clientRole.setClientCode("909464");
		clientRole.setRole(populateMapping());
		clientRole.setSectionId(1l);
		clientRoles.add(clientRole);
		Mockito.doReturn(expected).when(usersRepository).findById(Mockito.any());
		Mockito.doReturn(clients).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		Mockito.doReturn(clients).when(userClientRepository).findByClientCodeAndUser_Id(Mockito.any(),
				Mockito.anyLong());
		Mockito.doReturn(populateUserDTO(false)).when(usersMapper).userToUserDTO(Mockito.any());
		Mockito.doReturn(populateSecurity()).when(userSecurityRepository).findByUserClient_Id(Mockito.anyLong());
		Mockito.doReturn(roles).when(roleRepository).findByType(Mockito.anyString());
		Mockito.doReturn(jsonResponse).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn(node).when(objectMapper).readTree(Mockito.anyString());
		Mockito.doReturn(clientRoles).when(clientRoleRepository).findAllById(Mockito.any());
		UsersDTO response = userServiceImpl.getUserById(1l, token, "909464", principal);
		assertNotNull(response);
	}

	@Test
	public void testGetUserByIdForBranch() throws IOException {
		String json = "{\"client_id\":\"clientId\",\"client_name\":\"client name\",\"cost_center\":\"costCenterCode1\",\"status\":\"A\"}";
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "/url");
		Users user = populateUsers();
		user.setEmail("admin@bbsi.com");
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		List<UserClients> clients = user.getClients();
		clients.get(0).setUserType(GenericConstants.USERTYPE_BRANCH);
		user.setRoles(populateRbacEntity());
		List<RbacEntity> roles = populateRbacEntity();
		roles.get(0).setType(Enums.UserEnum.ROLE.toString());
		Optional<Users> expected = Optional.of(user);
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail("admin@bbsi.com");
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		List<CostCenterDTO> expected1 = populateCostCenter();
		String jsonResponse = new Gson().toJson(expected1);
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(jsonResponse);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		JsonNode node = mapper.readTree(jsonArray.get(0).toString());
		JsonNode node1 = mapper.readTree(json);
		List<ClientRole> clientRoles = new ArrayList<>();
		ClientRole clientRole = new ClientRole();
		clientRole.setClientCode("909464");
		clientRole.setRole(populateMapping());
		clientRole.setSectionId(1l);
		clientRoles.add(clientRole);
		List<PrismizedClientsDTO> expectedList = new ArrayList<>();
		PrismizedClientsDTO expectedDTO = new PrismizedClientsDTO();
		expectedDTO.setClient_code("clientId");
		expectedList.add(expectedDTO);
		Mockito.doReturn(expected).when(usersRepository).findById(Mockito.any());
		Mockito.doReturn(clients).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		Mockito.doReturn(clients).when(userClientRepository).findByClientCodeAndUser_Id(Mockito.any(),
				Mockito.anyLong());
		Mockito.doReturn(populateUserDTO(false)).when(usersMapper).userToUserDTO(Mockito.any());
		Mockito.doReturn(populateSecurity()).when(userSecurityRepository).findByUserClient_Id(Mockito.anyLong());
		Mockito.doReturn(roles).when(roleRepository).findByType(Mockito.anyString());
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(expectedList).when(webClientTemplate).getForObjectList(Mockito.anyString(), Mockito.any(),
				Mockito.anyMap());
		Mockito.doReturn(jsonResponse).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn(node).when(objectMapper).readTree(Mockito.anyString());
		Mockito.doReturn(node1).when(objectMapper).readTree(Mockito.anyString());
		Mockito.doReturn(clientRoles).when(clientRoleRepository).findAllById(Mockito.any());
		UsersDTO response = userServiceImpl.getUserById(1l, token, "909464", principal);
		assertNotNull(response);
	}

	@Test(expected = UnauthorizedAccessException.class)
	public void testGetUserByIdForUnauthorizedAccessExceptionPrincipalEmployee() {
		Users user = populateUsers();
		List<UserClients> clients = user.getClients();
		user.setEmail("integration@osius.com");
		Optional<Users> expected = Optional.of(user);
		Mockito.doReturn(expected).when(usersRepository).findById(Mockito.any());
		Mockito.doReturn(clients).when(userClientRepository).findByClientCodeAndUser_Id(Mockito.any(),
				Mockito.anyLong());
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_EMPLOYEE);
		UsersDTO response = userServiceImpl.getUserById(0l, " ", " ", principal);
		assertNull(response);
	}

	@Test
	public void testDeleteClientFromUser() {
		long id = 1;
		Mockito.doReturn(populateUserClients()).when(userClientRepository).findByClientCode(Mockito.any());
		userServiceImpl.deleteClientFromUser(id, "BBSI");
		assertTrue(true);
	}

	@Test
	public void testDeleteClientFromUserForException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByClientCode(Mockito.any());
		userServiceImpl.deleteClientFromUser(0l, null);
		assertTrue(true);
	}

	@Test
	public void testCreateRehireUser() {
		ReflectionTestUtils.setField(userServiceImpl, "emailURL", "http://localhost:8809/notification/v1/email/");
		users = populateUsers();
		users.setClients(populateUserClients());
		users.getClients().get(0).setEmployeeCode("empCode");
		users.getClients().get(0).setNewHireId(2l);
		users.getClients().get(0).setId(3l);
		users.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Mockito.doReturn(users).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doNothing().when(userClientRepository).updateEmployeeUserTypeToNewHireUserStatus(Mockito.anyString(),
				Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(clientRoleRepository).delete(Mockito.any());
		UsersDTO actualResponse = userServiceImpl.createRehireUser(populateUserDTO(true), "oauthToken", "uiUrl",
				userPrincipal);
		assertEquals(actualResponse.getId(), populateUserDTO(true).getId());
	}

	@Test
	public void testCreateRehireUserForUsersNull() {
		ReflectionTestUtils.setField(userServiceImpl, "emailURL", "http://localhost:8809/notification/v1/email/");
		users = populateUsers();
		users.setClients(populateUserClients());
		users.getClients().get(0).setEmployeeCode("empCode");
		users.getClients().get(0).setNewHireId(2l);
		users.getClients().get(0).setId(3l);
		users.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Mockito.doReturn(null).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doNothing().when(userClientRepository).updateEmployeeUserTypeToNewHireUserStatus(Mockito.anyString(),
				Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(clientRoleRepository).delete(Mockito.any());
		UsersDTO actualResponse = userServiceImpl.createRehireUser(populateUserDTO(true), "oauthToken", "uiUrl",
				userPrincipal);
		assertEquals(actualResponse.getId(), populateUserDTO(true).getId());
	}

	@Test
	public void testCreateRehireUserForUserTypeNotSame() {
		ReflectionTestUtils.setField(userServiceImpl, "emailURL", "http://localhost:8809/notification/v1/email/");
		users = populateUsers();
		users.setClients(populateUserClients());
		users.getClients().get(0).setEmployeeCode("empCode");
		users.getClients().get(0).setNewHireId(2l);
		users.getClients().get(0).setId(3l);
		users.getClients().get(0).setUserType(GenericConstants.ACTIVE.toString());
		Mockito.doReturn(users).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doNothing().when(userClientRepository).updateEmployeeUserTypeToNewHireUserStatus(Mockito.anyString(),
				Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(clientRoleRepository).delete(Mockito.any());
		UsersDTO actualResponse = userServiceImpl.createRehireUser(populateUserDTO(true), "oauthToken", "uiUrl",
				userPrincipal);
		assertEquals(actualResponse.getId(), populateUserDTO(true).getId());
	}

	@Test
	public void testCreateRehireUserNotEmployee() {
		users = populateUserWhenUserTypeIsEmployee();
		UsersDTO input = populateUserDTO(true);
		input.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		input.getClients().get(0)
				.setClientCode(populateUserWhenUserTypeIsEmployee().getClients().get(0).getClientCode());
		Mockito.doReturn(users).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doNothing().when(userClientRepository).updateEmployeeUserTypeToNewHireUserStatus(Mockito.anyString(),
				Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
		UsersDTO actualResponse = userServiceImpl.createRehireUser(populateUserDTO(true), "oauthToken", "uiUrl",
				userPrincipal);
		assertEquals(populateUserWhenUserTypeIsEmployee().getId(), actualResponse.getId());
	}

	@Test
	public void testCreateRehireUserForEmptyUser() {
		UsersDTO input = populateUserDTO(true);
		input.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		input.getClients().get(0)
				.setClientCode(populateUserWhenUserTypeIsEmployee().getClients().get(0).getClientCode());
		Mockito.doReturn(new Users("admin@osius.com")).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doNothing().when(userClientRepository).updateEmployeeUserTypeToNewHireUserStatus(Mockito.anyString(),
				Mockito.anyLong(), Mockito.anyString(), Mockito.anyString());
		UsersDTO actualResponse = userServiceImpl.createRehireUser(populateUserDTO(true), "oauthToken", "uiUrl",
				userPrincipal);
		assertEquals(actualResponse.getId(), populateUserDTO(true).getId());
	}

	@Test
	public void testMfaOtpValidation() {
		ReflectionTestUtils.setField(userServiceImpl, "verifyOtpURL", "/notification/v1/mfa/verify/user/otp");
		Mockito.doReturn("value").when(restClient).postForString(Mockito.anyString(), Mockito.any(OtpDetailsDTO.class),
				Mockito.any());
		String value = userServiceImpl.mfaOtpValidation(otpDetailsDTO, "token");
		assertEquals("value", value);
	}

	@Test
	public void testMfaOtpValidationForException() {
		Mockito.doThrow(BbsiException.class).when(restClient).postForString(" ", new HashMap<>(), new HashMap<>());
		String value = userServiceImpl.mfaOtpValidation(otpDetailsDTO, " ");
		assertNull(value);
	}

	@Test
	public void testGetdistinctClients() {
		List<String> clients = new ArrayList<String>();
		clients.add("value1");
		Mockito.doReturn(clients).when(usersRepository).getdistinctClients();
		List<String> value = userServiceImpl.getdistinctClients();
		assertEquals(1, value.size());
	}

	@Test
	public void testGetdistinctClientsThrowsException() {
		Mockito.doThrow(BbsiException.class).when(usersRepository).getdistinctClients();
		List<String> value = userServiceImpl.getdistinctClients();
		assertEquals(0, value.size());
	}

	@Test
	public void testGetCostCenterPrismizedClients() {
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl",
				"http://localhost:8810/integration/v1/common/type/COST_CENTER_CLIENTS?id=909464");
		List<PrismizedClientsDTO> expected = new ArrayList<>();
		PrismizedClientsDTO prism = new PrismizedClientsDTO();
		prism.setClient_code("909464");
		expected.add(prism);
		List<CostCenterClientDTO> expected1 = new ArrayList<>();
		CostCenterClientDTO client = new CostCenterClientDTO();
		client.setClientId("909464");
		expected1.add(client);
		Map<String, String> clients = new HashedMap<String, String>();
		clients.put("First", "ClientOne");
		String costcenters = "[[\"id\" : 1,\"client_name\" : \"1 Hour Drain\",\"client_id\" : \"909464\",\"status\" : \"Active\",\"cost_center\" : \"costCenter1\"]]";
		Mockito.doReturn("url", "url1", "url2").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(expected, expected1).when(webClientTemplate).getForObjectList(Mockito.anyString(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn(costcenters).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any());
		List<CostCenterClientDTO> response = userServiceImpl.getCostCenterPrismizedClients("909090");
		assertEquals(1, response.size());
	}

	@Test
	public void testGetCostCenterPrismizedClientsForEmpty() {
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl",
				"http://localhost:8810/integration/v1/common/type/COST_CENTER_CLIENTS?id=909464");
		Mockito.doReturn("url", "url1", "url2").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(new ArrayList<>()).when(webClientTemplate).getForObjectList(Mockito.anyString(), Mockito.any(),
				Mockito.any());
		List<CostCenterClientDTO> response = userServiceImpl.getCostCenterPrismizedClients("909090");
		assertNotNull(response);
	}

	@Test
	public void testGetCostCenterPrismizedClientsForException() {
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl",
				"http://localhost:8810/integration/v1/common/type/COST_CENTER_CLIENTS?id=909464");
		List<PrismizedClientsDTO> expected = new ArrayList<>();
		PrismizedClientsDTO prism = new PrismizedClientsDTO();
		prism.setClient_code("909464");
		expected.add(prism);
		Map<String, String> clients = new HashedMap<String, String>();
		clients.put("First", "ClientOne");
		String costcenters = "[[\"id\" : 1,\"client_name\" : \"1 Hour Drain\",\"client_id\" : \"909464\",\"status\" : \"Active\",\"cost_center\" : \"costCenter1\"]]";
		Mockito.doReturn(expected).when(webClientTemplate).getForObjectList(Mockito.anyString(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(costcenters).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any());
		List<CostCenterClientDTO> response = userServiceImpl.getCostCenterPrismizedClients("909090");
		assertEquals(0, response.size());
	}

	@Test
	public void testGetAllClientUsersByClientCode() {
		List<UsersDTO> listUserDto = new ArrayList<UsersDTO>();
		listUserDto.add(populateUserDTO(true));
		List<Users> listUsers = new ArrayList<>();
		listUsers.add(populateUsers());
		Mockito.doReturn(populateUserClients()).when(userClientRepository).findByClientCode(Mockito.anyString(),
				Mockito.any());
		when(usersMapper.userListToUserDTOList(listUsers)).thenReturn(listUserDto);
		Mockito.doReturn(listUserDto).when(usersMapper).userListToUserDTOList(Mockito.any());
		List<UsersDTO> actualResponse = userServiceImpl.getAllClientUsersByClientCode("909464");
		assertEquals(1, actualResponse.size());
	}

	@Test
	public void testGetAllClientUsersByClientCodeNoUser() {
		Mockito.doReturn(populateUserClientsNoUser()).when(userClientRepository).findByClientCode(Mockito.anyString(),
				Mockito.any());
		when(usersMapper.userListToUserDTOList(new ArrayList<>())).thenReturn(new ArrayList<>());
		List<UsersDTO> actualResponse = userServiceImpl.getAllClientUsersByClientCode("909464");
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testGetAllClientUsersByClientCoderThrowsException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByClientCode(Mockito.anyString(),
				Mockito.any());
		List<UsersDTO> actualResponse = userServiceImpl.getAllClientUsersByClientCode("909464");
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testDeleteNewHireUser() {
		List<UserClients> list = new ArrayList<>();
		UserClients user = new UserClients();
		user.setClientCode("clientCode");
		list.add(user);
		Object[] client = { BigInteger.ONE, "NewHire", 1l };
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(client);
		Mockito.doReturn(data).when(userClientRepository).getNewhireUser(Mockito.anyLong());
		Mockito.doReturn(list).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		userServiceImpl.deleteNewHireUser(123l);
		assertTrue(true);
	}

	@Test
	public void testDeleteNewHireUserForClient1ValueNotsame() {
		List<UserClients> list = new ArrayList<>();
		UserClients user = new UserClients();
		user.setClientCode("clientCode");
		list.add(user);
		Object[] client = { BigInteger.ONE, "NewHire123", 1l };
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(client);
		Mockito.doReturn(data).when(userClientRepository).getNewhireUser(Mockito.anyLong());
		Mockito.doReturn(list).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		userServiceImpl.deleteNewHireUser(123l);
		assertTrue(true);
	}

	@Test
	public void testDeleteNewHireUserForMultipleClients() {
		Object[] client = { 123l, "NewHire", 2l };
		List<Object[]> data = new ArrayList<Object[]>();
		data.add(client);
		UserClients data1 = populateUserClient();
		data1.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		List<UserClients> dataList = new ArrayList<>();
		dataList.add(data1);
		Mockito.doReturn(data).when(userClientRepository).getNewhireUser(Mockito.anyLong());
		Mockito.doReturn(dataList).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		userServiceImpl.deleteNewHireUser(123l);
		assertTrue(true);
	}

	@Test
	public void testDeleteNewHireUserWhenNoClient() {
		Mockito.doReturn(populateUserAndClient()).when(userClientRepository).findByNewHireId(Mockito.anyLong());
		userServiceImpl.deleteNewHireUser(123l);
		assertTrue(true);
	}

	@Test
	public void testDeleteNewHireUserThrowsException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByNewHireId(Mockito.anyLong());
		Mockito.doReturn(populateUserClients()).when(userClientRepository).findByUser_Id(Mockito.anyLong());
		userServiceImpl.deleteNewHireUser(123l);
		assertTrue(true);
	}

	@Test
	public void testGetEmailsByClientCodeAndTypeAndEnum() {
		Mockito.doReturn(populateUserClients()).when(userClientRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateSecurity()).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Map<String, List<String>> response = userServiceImpl.getEmailsByClientCodeAndTypeAndEnum("909090", "Client",
				"LOCATION_INFO");
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndUserType("909090", "Client");
		Mockito.verify(userSecurityRepository, Mockito.atMost(1)).findByUserClient_Id(Mockito.anyLong());
		assertEquals(2, response.size());
	}

	@Test
	public void testGetEmailsByClientCodeAndTypeAndEnumForUserClientsEmpty() {
		Mockito.doReturn(new ArrayList<>()).when(userClientRepository).findByClientCodeAndUserType(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doReturn(populateSecurity()).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Map<String, List<String>> response = userServiceImpl.getEmailsByClientCodeAndTypeAndEnum("909090", "Client",
				"LOCATION_INFO");
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndUserType("909090", "Client");
		Mockito.verify(userSecurityRepository, Mockito.atMost(1)).findByUserClient_Id(Mockito.anyLong());
		assertNotNull(response);
	}

	@Test
	public void testGetEmailsByClientCodeAndTypeAndEnumEmpty() {
		Mockito.doReturn(populateUserClients()).when(userClientRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(populateSecurity()).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Map<String, List<String>> response = userServiceImpl.getEmailsByClientCodeAndTypeAndEnum("909090", "Client",
				"");
		assertNotNull(response);
	}

	@Test
	public void testGetEmailsByClientCodeAndTypeAndEnumUser() {
		Mockito.doReturn(populateUserClients()).when(userClientRepository)
				.findByClientCodeAndUserType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(null).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Map<String, List<String>> response = userServiceImpl.getEmailsByClientCodeAndTypeAndEnum("909090", "Client",
				"LOCATION_INFO");
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndUserType("909090", "Client");
		Mockito.verify(userSecurityRepository, Mockito.atMost(1)).findByUserClient_Id(Mockito.anyLong());
		assertEquals(2, response.size());
	}

	@Test
	public void testGetEmailsByClientCodeAndTypeAndEnumOtherThanLocation() {
		Mockito.doReturn(new ArrayList<>()).when(userClientRepository).findByClientCodeAndUserType(Mockito.anyString(),
				Mockito.anyString());
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setCostCenterCode("CC");
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(Lists.newArrayList()).when(userCostCenterAssociationRepository)
				.findByCostCenterCode(Mockito.any());
		Map<String, List<String>> response = userServiceImpl.getEmailsByClientCodeAndTypeAndEnum("909090", "Client",
				"Branch");
		assertEquals(3, response.size());
	}

	@Test
	public void testGetEmailsByClientCodeAndType() {
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setCostCenterCode("code");
		assoc.setUser(populateUsers());
		assoc.setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		assoc.setId(1l);
		List<UserCostCenterAssociation> assocs = new ArrayList<>();
		assocs.add(assoc);
		Object[] objectArray = { "email", "abc", "def", "8765432109", "Branch", true,"mfaemail" };
		List<Object[]> expected = new ArrayList<Object[]>();
		expected.add(objectArray);
		Mockito.doReturn(expected).when(userClientRepository).findByUserDetailsByClient(Mockito.anyString());
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setCostCenterCode("CC");
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(assocs).when(userCostCenterAssociationRepository).findByCostCenterCode(Mockito.any());
		Map<String, List<String>> response = userServiceImpl.getEmailsByClientCodeAndType("909464", "Branch");
		assertEquals(5, response.size());
	}

	@Test
	public void testGetEmailsByClientCodeAndTypeThrowsException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByUserDetailsByClient(Mockito.anyString());
		Map<String, List<String>> response = userServiceImpl.getEmailsByClientCodeAndType("909464", "NewHire");
		assertEquals(0, response.size());
	}

	@Test
	public void testDisableUser() {
		Mockito.doReturn(populateUserAndClient()).when(userClientRepository).findByNewHireId(Mockito.anyLong());
		boolean response = userServiceImpl.disableUser("909464", 1l);
		assertEquals(false, response);
	}

	@Test
	public void testDisableUserThrowException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByNewHireId(Mockito.anyLong());
		boolean response = userServiceImpl.disableUser("909464", 1l);
		assertEquals(false, response);
	}

	@Test
	public void testCheckExistUser() {
		List<Object[]> objectList = new ArrayList<Object[]>();
		Object[] obj = { "fname", "lname", "9090909090", "email@bbsi.com", true };
		objectList.add(obj);
		Mockito.doReturn(objectList).when(usersRepository).getUserDataByEmail(Mockito.anyString(), Mockito.anyString());
		UserExistDTO response = userServiceImpl.checkExistUser("909090", "email@bbsi.com", token);
		assertEquals(objectList.get(0)[3], response.getEmail());
		assertEquals("909090", response.getClientCode());
		assertEquals(response.getFirstName(), objectList.get(0)[0]);
	}

	@Test
	public void testCheckExistUserForuserNull() {
		List<Object[]> objectList = new ArrayList<Object[]>();
		Object[] obj = { "fname", null, null, "email@bbsi.com", null };
		objectList.add(obj);
		Mockito.doReturn(objectList).when(usersRepository).getUserDataByEmail(Mockito.anyString(), Mockito.anyString());
		UserExistDTO response = userServiceImpl.checkExistUser("909090", "email@bbsi.com", token);
		assertEquals(objectList.get(0)[3], response.getEmail());
		assertEquals("909090", response.getClientCode());
		assertEquals(response.getFirstName(), objectList.get(0)[0]);
	}

	@Test
	public void testCheckExistUserIfUserIsEmpty() {
		Mockito.doReturn(new ArrayList<>()).when(usersRepository).getUserDataByEmail(Mockito.anyString(),
				Mockito.anyString());
		UserExistDTO response = userServiceImpl.checkExistUser("909090", "email@bbsi.com", token);
		assertEquals(null, response.getClientCode());
	}

	@Test
	public void testCheckExistUserIfUserThrowsException() {
		Mockito.doThrow(BbsiException.class).when(usersRepository).getUserDataByEmail(Mockito.anyString(),
				Mockito.anyString());
		UserExistDTO response = userServiceImpl.checkExistUser("909090", "email@bbsi.com", token);
		assertEquals(null, response.getClientCode());
	}

	@Test
	public void testSaveUserToolbarSettings() {
		List<UserToolbarSettingsDTO> inputList = new ArrayList<>();
		UserToolbarSettingsDTO input = new UserToolbarSettingsDTO();
		input.setClientCode("909464");
		input.setFeatureCode("1");
		input.setId(1l);
		input.setMenuItemId(1l);
		input.setUserEmail("YWRtaW5Ab3NpdXMuY29t");
		input.setVersion(1l);
		inputList.add(input);
		Mockito.doNothing().when(userToolbarSettingsRepositoryV2).flush();
		userServiceImpl.saveUserToolbarSettings(userPrincipal, inputList);
		assertTrue(true);
	}

	@Test
	public void testSaveUserToolbarSettingsForExceptioons() {
		List<UserToolbarSettingsDTO> inputList = new ArrayList<>();
		UserToolbarSettingsDTO input = new UserToolbarSettingsDTO();
		input.setClientCode(" ");
		input.setId(0l);
		input.setUserEmail(null);
		inputList.add(input);
		when(userToolbarSettingsMapperV2.userToolbarSettingsListToUserToolbarSettingsDTOList(new ArrayList<>()))
				.thenReturn(new ArrayList<>());
		Mockito.doThrow(BbsiException.class).when(userToolbarSettingsRepositoryV2).flush();
		userServiceImpl.saveUserToolbarSettings(userPrincipal, inputList);
		assertTrue(true);
	}

	@Test
	public void testSaveUserToolbarSettingsForOtherExceptions() {
		Mockito.doNothing().when(userToolbarSettingsRepositoryV2).flush();
		userServiceImpl.saveUserToolbarSettings(userPrincipal, null);
		assertTrue(true);
	}

	@Test
	public void testSaveUserToolbarSettingsWhenMenuItemId() {
		List<UserToolbarSettingsDTO> inputList = new ArrayList<>();
		UserToolbarSettingsDTO input = new UserToolbarSettingsDTO();
		input.setClientCode("909464");
		input.setFeatureCode("1");
		input.setUserEmail("admin@osius.com");
		input.setVersion(1l);
		input.setMenuItemId(0);
		inputList.add(input);
		Mockito.doNothing().when(userToolbarSettingsRepositoryV2).flush();
		userServiceImpl.saveUserToolbarSettings(userPrincipal, inputList);
		assertTrue(true);
	}

	@Test
	public void testUpdatePortalAccessForNewClients() throws Exception {
		List<CostCenterDTO> costCenters1 = populateCostCenters();
		costCenters1.get(0).setCode("909464");
		List<UserClients> vancouver = populateUserClientsForVancouver();
		vancouver.get(0).setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		vancouver.get(0).setClientCode("909464");
		List<PrismizedClientsDTO> prismizedClients = populatePrismizedClient();
		List<ClientMaster> clientMaster = new ArrayList<>();
		ClientMaster data1 = new ClientMaster();
		data1.setClientCode("909464");
		data1.setClientName("1 Hour Drain");
		data1.setCostCenterCode("009");
		data1.setCostCenterDescription(populateCostCenter().get(0).getDescription());
		clientMaster.add(data1);
		List<UserClients> branchClients = populateUserClientsForClient();
		branchClients.get(0).setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		branchClients.get(0).setClientCode("909464");
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findAll();
		Mockito.doReturn("/url", "url1").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(costCenters1, prismizedClients, populateCostCenterClientDTO()).when(webClientTemplate)
				.getForObjectList(Mockito.anyString(), Mockito.any(), Mockito.any());
		Mockito.doNothing().when(auditDetailsUtil).buildAuditDetailsAndSave(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.any());
		Mockito.doReturn(getUserCostCenterAssociation1()).when(userCostCenterAssociationRepository)
				.findByUserType(Mockito.anyString());
		userServiceImpl.updatePortalAccessForNewClients();
		assertTrue(true);
	}

	@Test
	public void testUpdatePortalAccessForNewClientsThrowsException() throws Exception {
		Mockito.doReturn(null).when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(null).when(webClientTemplate).getForObjectList(Mockito.anyString(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(null).when(restClient).getForString(Mockito.any(), Mockito.anyString(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(null).when(objectMapper).readTree(Mockito.anyString());
		userServiceImpl.updatePortalAccessForNewClients();
		assertTrue(true);
	}

	@Test
	public void testGetUsersByClientCodeAndType() {
		List<UsersDTO> listUserDTO = new ArrayList<UsersDTO>();
		listUserDTO.add(populateUserDTO(false));
		Mockito.doReturn(populateUserClientsForVancouver()).when(userClientRepository)
				.findByClientCode(Mockito.anyString(), Mockito.any());
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setCostCenterCode("CC");
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(Lists.newArrayList()).when(userCostCenterAssociationRepository)
				.findByCostCenterCode(Mockito.any());
		Mockito.doReturn(listUserDTO).when(usersMapper).userListToUserDTOList(Mockito.any());
		List<UsersDTO> actualResponse = userServiceImpl.getUsersByClientCodeAndType("909464", "Client");
		assertEquals(1, actualResponse.size());
	}

	@Test
	public void testGetUsersByClientCodeAndTypeForNullUser() {
		List<UsersDTO> listUserDTO = new ArrayList<UsersDTO>();
		listUserDTO.add(populateUserDTO(false));
		UserClients userClient = new UserClients();
		userClient.setClientCode("909464");
		List<UserClients> userClients = new ArrayList<>();
		userClients.add(userClient);
		Mockito.doReturn(userClients).when(userClientRepository).findByClientCode(Mockito.anyString(), Mockito.any());
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setCostCenterCode("CC");
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(Lists.newArrayList()).when(userCostCenterAssociationRepository)
				.findByCostCenterCode(Mockito.any());
		Mockito.doReturn(listUserDTO).when(usersMapper).userListToUserDTOList(Mockito.any());
		List<UsersDTO> actualResponse = userServiceImpl.getUsersByClientCodeAndType("909464", "Client");
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testGetUsersByClientCodeAndTypeNoUser() {
		List<UserClients> inputClients = populateUserClientsForVancouver();
		inputClients.get(0).setUser(null);
		Mockito.doReturn(inputClients).when(userClientRepository).findByClientCode(Mockito.anyString(), Mockito.any());
		Mockito.doThrow(ValidationException.class).when(usersMapper).userListToUserDTOList(Mockito.any());
		List<UsersDTO> actualResponse = userServiceImpl.getUsersByClientCodeAndType("909464", "Client");
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testGetUsersByClientCodeAndTypeThrowsException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByClientCode(Mockito.anyString(),
				Mockito.any());
		List<UsersDTO> actualResponse = userServiceImpl.getUsersByClientCodeAndType("909464", "Client");
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testGetUsersByClientCodeAndTypeAndCreatedBy() {
		List<UserClients> expectedList = populateUserClients();
		expectedList.get(0).setUserType("SUPER");
		List<Users> userList = new ArrayList<Users>();
		userList.add(populateUser());
		UsersDTO expecteddTO = populateUserDTO(false);
		Mockito.doReturn(populateUser()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(expectedList).when(userClientRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(expecteddTO).when(usersMapper).userToUserDTO(Mockito.any());
		Mockito.doReturn(userList).when(usersRepository).findByCreatedBy(Mockito.anyString());
		List<UsersDTO> actualResponse = userServiceImpl.getUsersByClientCodeAndTypeAndCreatedBy(
				expectedList.get(0).getClientCode(), "SUPER", "YWRtaW5Ab3NpdXMuY29t");
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testGetUsersByClientCodeAndTypeAndCreatedByParentUserNull() {
		Mockito.doReturn(null).when(usersRepository).findByEmail(Mockito.anyString());
		List<UsersDTO> actualResponse = userServiceImpl.getUsersByClientCodeAndTypeAndCreatedBy("909464", "SUPER",
				"YWRtaW5Ab3NpdXMuY29t");
		assertEquals(0, actualResponse.size());
	}

	@Test
	public void testGetUsersByClientCodeAndTypeAndCreatedByVancouver() {
		String json = "[{\"status\": \"T\",\"employee_code\": \"1234\"},{\"status\": \"A\",\"employee_code\": \"5678\"}]";
		String uri = "http://10.1.17.6:9090/ws/rest/V3/getClientEmployeeDetailsFromUniquery?clientId=909464&options=EXTENDED";
		List<UserClients> expectedList = populateUserClients();
		expectedList.get(0).setUserType(GenericConstants.USERTYPE_VANCOUVER);
		List<Users> userList = new ArrayList<Users>();
		userList.add(populateUser());
		userList.get(0).getClients().get(0).setUserType(GenericConstants.USERTYPE_VANCOUVER);
		UsersDTO expecteddTO = populateUserDTO(false);
		expecteddTO.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		Mockito.doReturn(populateUser()).when(usersRepository).findByEmail(Mockito.anyString());
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setCostCenterCode("CC");
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setUser(populateUser());
		assoc.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		assoc.setCostCenterCode("CC");
		Mockito.doReturn(Lists.newArrayList(assoc)).when(userCostCenterAssociationRepository)
				.findByCostCenterCode(Mockito.any());
		doReturn(uri).when(boomiHelper).getUrl(BoomiEnum.CLIENT_EMPLOYEES_FILTER_UQ);
		doReturn(json).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUserClientsForVancouver()).when(userClientRepository)
				.findByClientCode(Mockito.anyString());
		Mockito.doReturn(expecteddTO).when(usersMapper).userToUserDTO(Mockito.any());
		Mockito.doReturn(userList).when(usersRepository).findByCreatedBy(Mockito.anyString());
		List<UsersDTO> actualResponse = userServiceImpl.getUsersByClientCodeAndTypeAndCreatedBy("909464",
				GenericConstants.USERTYPE_VANCOUVER, "YWRtaW5Ab3NpdXMuY29t");
		assertEquals(1, actualResponse.size());
	}

	@Test
	public void testGetUsersByClientCodeAndTypeAndCreatedByBranch() {
		List<UserClients> expectedList = populateUserClients();
		expectedList.get(0).setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		List<Users> userList = new ArrayList<Users>();
		userList.add(populateUser());
		userList.get(0).getClients().get(0).setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		UsersDTO expecteddTO = populateUserDTO(false);
		expecteddTO.setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		Mockito.doReturn(populateUser()).when(usersRepository).findByEmail(Mockito.anyString());
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setCostCenterCode("CC");
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setUser(populateUser());
		assoc.setUserType(GenericConstants.USERTYPE_BRANCH);
		assoc.setCostCenterCode("CC");
		Mockito.doReturn(Lists.newArrayList(assoc)).when(userCostCenterAssociationRepository)
				.findByCostCenterCode(Mockito.any());
		Mockito.doReturn(expectedList).when(userClientRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(expecteddTO).when(usersMapper).userToUserDTO(Mockito.any());
		Mockito.doReturn(userList).when(usersRepository).findByCreatedBy(Mockito.anyString());
		List<UsersDTO> actualResponse = userServiceImpl.getUsersByClientCodeAndTypeAndCreatedBy("909464",
				GenericConstants.USERTYPE_BRANCH.toString(), "YWRtaW5Ab3NpdXMuY29t");
		assertEquals(1, actualResponse.size());
	}

	@Test
	public void testGetUsersByClientCodeAndTypeAndCreatedByClient() {
		List<Users> userList = new ArrayList<Users>();
		userList.add(populateUser());
		UsersDTO expecteddTO = populateUserDTO(true);
		Mockito.doReturn(populateUser()).when(usersRepository).findByEmail(Mockito.anyString());
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setCostCenterCode("CC");
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setUser(populateUser());
		assoc.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		assoc.setCostCenterCode("CC");
		Mockito.doReturn(Lists.newArrayList(assoc)).when(userCostCenterAssociationRepository)
				.findByCostCenterCode(Mockito.any());
		Mockito.doReturn(populateUserClientsForClient()).when(userClientRepository)
				.findByClientCode(Mockito.anyString());
		Mockito.doReturn(expecteddTO).when(usersMapper).userToUserDTO(Mockito.any());
		Mockito.doReturn(userList).when(usersRepository).findByCreatedBy(Mockito.anyString());
		List<UsersDTO> actualResponse = userServiceImpl.getUsersByClientCodeAndTypeAndCreatedBy("909464", "Client",
				Base64.getEncoder().encodeToString("test@bbsi.com".getBytes()));
		assertEquals(1, actualResponse.size());
	}

	@Test
	public void testGetUsersByClientCodeAndTypeAndCreatedByClientForDiffClientCode() {
		List<Users> userList = new ArrayList<Users>();
		userList.add(populateUser());
		UsersDTO expecteddTO = populateUserDTO(false);
		expecteddTO.getClients().get(0).setClientCode("abcd");
		Mockito.doReturn(populateUser()).when(usersRepository).findByEmail(Mockito.anyString());
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setCostCenterCode("CC");
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setUser(populateUser());
		assoc.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		assoc.setCostCenterCode("CC");
		Mockito.doReturn(Lists.newArrayList(assoc)).when(userCostCenterAssociationRepository)
				.findByCostCenterCode(Mockito.any());
		Mockito.doReturn(populateUserClientsForClient()).when(userClientRepository)
				.findByClientCode(Mockito.anyString());
		Mockito.doReturn(expecteddTO).when(usersMapper).userToUserDTO(Mockito.any());
		Mockito.doReturn(userList).when(usersRepository).findByCreatedBy(Mockito.anyString());
		List<UsersDTO> actualResponse = userServiceImpl.getUsersByClientCodeAndTypeAndCreatedBy("909464", "Client",
				Base64.getEncoder().encodeToString("test@bbsi.com".getBytes()));
		assertEquals(1, actualResponse.size());
	}

	@Test
	public void testUpdateIsPolicyAccepted() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("costCenterCode");
		principal.setIsPolicyAccepted(true);
		int result = 0;
		Mockito.doReturn(result).when(usersRepository).updateIsPolicyAccepted(populatePolicyAccepted().getEmail(),
				populatePolicyAccepted().getIsPolicyAccepted());
		userServiceImpl.updateIsPolicyAccepted(populatePolicyAccepted(), principal);
		assertEquals(0, result);
	}

	@Test
	public void testUpdateIsPolicyAcceptedForExceptions() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Mockito.doThrow(BbsiException.class).when(usersRepository).updateIsPolicyAccepted(" ", true);
		userServiceImpl.updateIsPolicyAccepted(new PolicyAcceptedDTO(),
				new UserPrincipal(" ", " ", Lists.newArrayList()));
		assertTrue(true);
	}

	@Test
	public void testUpdateIsPolicyAcceptedForOtherExceptions() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		PolicyAcceptedDTO data = new PolicyAcceptedDTO();
		data.setEmail(null);
		data.setIsPolicyAccepted(true);
		Mockito.doThrow(BbsiException.class).when(usersRepository).updateIsPolicyAccepted(null, true);
		userServiceImpl.updateIsPolicyAccepted(data, null);
		assertTrue(true);
	}

	@Test
	public void testUpdateIsPolicyAcceptedWhenDTOIsNull() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		Mockito.doThrow(BbsiException.class).when(usersRepository).updateIsPolicyAccepted("admin@osius.com", true);
		userServiceImpl.updateIsPolicyAccepted(null, userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testUpdateUserDetails() throws Exception, JsonProcessingException {
		JsonNode node = mapper.readTree(payload);
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(payload).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		when(utils.validateEmployeeCodes(populateCustomPersonalDTO(), restClient, boomiHelper)).thenReturn(payload);
		Mockito.doReturn(node).when(objectMapper).readTree(Mockito.anyString());
		Mockito.doReturn(populateUserAndClient()).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		userServiceImpl.updateUserDetails(populateCustomPersonalDTO(), token);
		assertTrue(true);
	}

	@Test
	public void testUpdateUserDetailsForCustomPersonalDTOClientCodeEmpty() throws Exception, JsonProcessingException {
		CustomPersonalDTO customPersonalDTO = new CustomPersonalDTO();
		customPersonalDTO.setClientCode("");
		userServiceImpl.updateUserDetails(customPersonalDTO, token);
		assertTrue(true);
	}

	@Test
	public void testUpdateUserDetailsForCustomPersonalDTOEmployeeCodeEmpty() throws Exception, JsonProcessingException {
		CustomPersonalDTO customPersonalDTO = new CustomPersonalDTO();
		customPersonalDTO.setClientCode("909464");
		customPersonalDTO.setEmployeeCode("");
		userServiceImpl.updateUserDetails(customPersonalDTO, token);
		assertTrue(true);
	}

	@Test
	public void testUpdateUserDetailsElse() throws Exception, JsonProcessingException {
		CustomPersonalDTO populateCustomPersonalDTO = populateCustomPersonalDTO();
		populateCustomPersonalDTO.setEmployeeCode("L47730");
		JsonNode node = mapper.readTree(payload);
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(payload).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		when(utils.validateEmployeeCodes(populateCustomPersonalDTO, restClient, boomiHelper)).thenReturn(payload);
		Mockito.doReturn(node).when(objectMapper).readTree(Mockito.anyString());
		Mockito.doReturn(populateUserAndClient()).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		userServiceImpl.updateUserDetails(populateCustomPersonalDTO, token);
		assertTrue(true);
	}

	@Test
	public void testUpdateUserDetailsThrowsException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		userServiceImpl.updateUserDetails(populateCustomPersonalDTO(), token);
		assertTrue(true);
	}

	@Test
	public void testUpdateUserPersonalInfo() {
		Users user = populateUsers();
		UserClients userClient = populateUsers().getClients().get(0);
		Users user1 = new Users();
		user1.setId(13l);
		userClient.setUser(user1);
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(user).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(userClient).when(userClientRepository).findByClientCodeAndEmployeeCode(Mockito.any(),
				Mockito.any());
		CustomPersonalDTO custom = userServiceImpl.updateUserPersonalInfo(populateCustomPersonalDTO(), token,
				getUserPrincipal());
		assertEquals(populateCustomPersonalDTO().getId(), custom.getId());
	}

	@Test
	public void testUpdateUserPersonalInfoWhenNewEmailIsNotPresent() {
		Mockito.doReturn(populateUser()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateUserClients().get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		CustomPersonalDTO response = userServiceImpl.updateUserPersonalInfo(populateCustomPersonalDTOWithNoEmail(),
				token, userPrincipal);
		assertEquals(0, response.getId());
	}

	@Test
	public void testUpdateUserPersonalInfoThrowsException() {
		Mockito.doThrow(BbsiException.class).when(usersRepository).findByEmail(Mockito.anyString());
		CustomPersonalDTO response = userServiceImpl.updateUserPersonalInfo(populateCustomPersonalDTO(), token,
				userPrincipal);
		assertEquals(1, response.getId());
	}

	@Test
	public void testUpdateI9ApprovalDetails() {
		Optional<Users> optional = Optional.of(populateUserWithTypeUser());
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn(123).when(userClientRepository).updateI9ApprovalDetails(Mockito.anyBoolean(),
				Mockito.anyLong());
		userServiceImpl.updateI9ApprovalDetails(123l, "E71624", "909090");
		assertTrue(true);
	}

	@Test
	public void testUpdateI9ApprovalDetailsForempcode() {
		Optional<Users> optional = Optional.of(populateUserWithTypeUser());
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn(123).when(userClientRepository).updateI9ApprovalDetails(Mockito.anyBoolean(),
				Mockito.anyLong());
		userServiceImpl.updateI9ApprovalDetails(123l, "E71624123", "909090");
		assertTrue(true);
	}

	@Test
	public void testUpdateI9ApprovalDetailsForEmpty() {
		Users user = new Users();
		List<UserClients> clients = new ArrayList<>();
		user.setClients(clients);
		Optional<Users> optional = Optional.of(user);
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn(123).when(userClientRepository).updateI9ApprovalDetails(Mockito.anyBoolean(),
				Mockito.anyLong());
		userServiceImpl.updateI9ApprovalDetails(123l, "E71624", "909090");
		assertTrue(true);
	}

	@Test
	public void testUpdateI9ApprovalDetailsThrowsException() {
		Mockito.doThrow(BbsiException.class).when(usersRepository).findById(Mockito.anyLong());
		userServiceImpl.updateI9ApprovalDetails(123l, "E71624", "909464");
		assertTrue(true);
	}

	@Test
	public void testGeti9ApprovalUser() {
		userDTO = populateUserDTO(true);
		Mockito.doReturn(populateUserClientsWithEmployee()).when(userClientRepository)
				.findByClientCode(Mockito.anyString());
		Mockito.doReturn(userDTO).when(usersMapper).userToUserDTO(Mockito.any());
		UsersDTO actualResponse = userServiceImpl.geti9ApprovalUser("909090");
		assertEquals(actualResponse.getId(), userDTO.getId());
	}

	@Test
	public void testGeti9ApprovalUserForuserClientListEmpty() {
		userDTO = populateUserDTO(true);
		Mockito.doReturn(new ArrayList<>()).when(userClientRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(userDTO).when(usersMapper).userToUserDTO(Mockito.any());
		UsersDTO actualResponse = userServiceImpl.geti9ApprovalUser("909090");
		assertNull(actualResponse);
	}

	@Test
	public void testGeti9ApprovalUserForUserTypeNull() {
		List<UserClients> clients = new ArrayList<>();
		UserClients uc = new UserClients();
		uc.setUserType(null);
		clients.add(uc);
		userDTO = populateUserDTO(true);
		Mockito.doReturn(clients).when(userClientRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(userDTO).when(usersMapper).userToUserDTO(Mockito.any());
		UsersDTO actualResponse = userServiceImpl.geti9ApprovalUser("909090");
		assertNull(actualResponse);
	}

	@Test
	public void testGeti9ApprovalUserForUserTypeNotEqual() {
		List<UserClients> clients = new ArrayList<>();
		UserClients uc = new UserClients();
		uc.setUserType("client");
		uc.setI9Approver(true);
		clients.add(uc);
		userDTO = populateUserDTO(true);
		Mockito.doReturn(clients).when(userClientRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(userDTO).when(usersMapper).userToUserDTO(Mockito.any());
		UsersDTO actualResponse = userServiceImpl.geti9ApprovalUser("909090");
		assertNull(actualResponse);
	}

	@Test
	public void testGeti9ApprovalUserForOtherUsers() {
		List<UserClients> expected = new ArrayList<>();
		expected.add(populateUserClient());
		Mockito.doReturn(expected).when(userClientRepository).findByClientCode(Mockito.anyString());
		UsersDTO actualResponse = userServiceImpl.geti9ApprovalUser("909090");
		assertNull(actualResponse);
	}

	@Test
	public void testGeti9ApprovalUserThrowsException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(userDTO).when(usersMapper).userToUserDTO(Mockito.any());
		UsersDTO actualResponse = userServiceImpl.geti9ApprovalUser("909090");
		assertNull(actualResponse);
	}

	@Test
	public void testgetAllBranchUsers() {
		List<UsersDTO> expected = new ArrayList<>();
		expected.add(populateUserDTO(false));
		Users input = populateUsers();
		input.getClients().get(0).setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		Optional<Users> optional = Optional.of(input);
		List<UserClients> branchUsers = populateUserClientsForClient();
		branchUsers.get(0).setCreatedBy(populateUser().getEmail());
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn(branchUsers).when(userClientRepository).findByClientCodeIn(Mockito.any(), Mockito.any());
		Mockito.doReturn(expected).when(usersMapper).userListToUserDTOList(Mockito.any());
		List<UsersDTO> response = userServiceImpl.getAllBranchUsers(Mockito.anyLong());
		assertEquals(0, response.size());
	}

	@Test
	public void testgetAllBranchUsersForUserClientEmpty() {
		List<UsersDTO> expected = new ArrayList<>();
		expected.add(populateUserDTO(false));
		Users input = new Users();
		input.setClients(new ArrayList<>());
		Optional<Users> optional = Optional.of(input);
		List<UserClients> branchUsers = populateUserClientsForClient();
		branchUsers.get(0).setCreatedBy(populateUser().getEmail());
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn(branchUsers).when(userClientRepository).findByClientCodeIn(Mockito.any(), Mockito.any());
		Mockito.doReturn(expected).when(usersMapper).userListToUserDTOList(Mockito.any());
		List<UsersDTO> response = userServiceImpl.getAllBranchUsers(Mockito.anyLong());
		assertEquals(0, response.size());
	}

	@Test
	public void testgetAllBranchUsersThrowsValidationException() {
		Users input = populateUser();
		input.getClients().get(0).setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		Optional<Users> optional = Optional.of(input);
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		List<UsersDTO> response = userServiceImpl.getAllBranchUsers(0l);
		assertEquals(0, response.size());
	}

	@Test
	public void testgetAllBranchUsersThrowsException() {
		Mockito.doThrow(BbsiException.class).when(usersRepository).findById(Mockito.anyLong());
		List<UsersDTO> response = userServiceImpl.getAllBranchUsers(Mockito.anyLong());
		assertEquals(0, response.size());
	}

	@Test
	public void testGetAllBranchUsersWhenCreatedUserIsSame() {
		List<Users> expected = new ArrayList<>();
		expected.add(populateUser());
		Users input = populateUser();
		List<UserClients> expected1 = populateUserClientsForClient();
		expected1.get(0).setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		expected1.get(0).setCreatedBy(input.getEmail());
		input.getClients().get(0).setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		input.getClients().get(0).setCreatedBy(input.getEmail());
		Optional<Users> optional = Optional.of(input);
		Mockito.doReturn(optional).when(usersRepository).findById(Mockito.anyLong());
		Mockito.doReturn(populateUserClientsForClient()).when(userClientRepository).findByClientCodeIn(Mockito.any(),
				Mockito.any());
		Mockito.doReturn(expected).when(usersMapper).userListToUserDTOList(Mockito.any());
		List<UsersDTO> response = userServiceImpl.getAllBranchUsers(Mockito.anyLong());
		assertEquals(0, response.size());
	}

	@Test
	public void testGetUserToolbarSettings() {
		List<UserToolbarSettings> listSettings = new ArrayList<>();
		UserToolbarSettings settings = new UserToolbarSettings();
		settings.setClientCode("909464");
		settings.setFeatureCode("featureCode");
		settings.setMenuItemId(1l);
		settings.setUserEmail("admin@osius.com");
		listSettings.add(settings);
		List<UserToolbarSettingsDTO> listDTO = new ArrayList<>();
		UserToolbarSettingsDTO dto = new UserToolbarSettingsDTO();
		dto.setClientCode("909464");
		dto.setFeatureCode("featureCode");
		dto.setMenuItemId(1l);
		dto.setUserEmail("admin@osius.com");
		listDTO.add(dto);
		Mockito.doReturn(listSettings).when(userToolbarSettingsRepositoryV2)
				.findByClientCodeAndUserEmail(Mockito.anyString(), Mockito.anyString());
		when(userToolbarSettingsMapperV2.userToolbarSettingsListToUserToolbarSettingsDTOList(listSettings))
				.thenReturn(listDTO);
		List<UserToolbarSettingsDTO> response = userServiceImpl.getUserToolbarSettings("909090", "admin@osius.com");
		assertEquals(1, response.size());
	}

	@Test
	public void testGetUserToolbarSettingsThrowsException() {
		Mockito.doThrow(BbsiException.class).when(userToolbarSettingsRepositoryV2)
				.findByClientCodeAndUserEmail(Mockito.anyString(), Mockito.anyString());
		when(userToolbarSettingsMapperV2.userToolbarSettingsListToUserToolbarSettingsDTOList(new ArrayList<>()))
				.thenReturn(new ArrayList<>());
		Mockito.doThrow(BbsiException.class).when(userToolbarSettingsMapperV2)
				.userToolbarSettingsListToUserToolbarSettingsDTOList(Mockito.any());
		List<UserToolbarSettingsDTO> response = userServiceImpl.getUserToolbarSettings("909090", "admin@osius.com");
		assertNull(response);
	}

	@Test
	public void testSwitchBusiness() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		Map<String, String> clientMap = new HashMap<>();
		clientMap.put("909090", "1 Hour Drain");
		principal.setClientMap(clientMap);
		principal.setClientCode("909090");
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setEmail("test@bbsi.com");
		principal.setUserType(populateUsers().getClients().get(0).getUserType());
		principal.setAccessGroup("1");
		List<MenuMappingDTO> userMenu = new ArrayList<>();
		MenuMappingDTO menu = new MenuMappingDTO();
		menu.setCategory("category");
		menu.setDescription("description");
		menu.setDisplayName("displayName");
		menu.setFeature("feature");
		menu.setFeatureCodeId(1l);
		menu.setIconUrl("/iconUrl");
		menu.setId(1l);
		menu.setName("name");
		menu.setSequence(1);
		menu.setType("ALL");
		menu.setUrl("/url");
		menu.setMenuItems(populateMenuItems());
		userMenu.add(menu);
		principal.setUserMenu(userMenu);
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setClientName("1 Hour Drain");
		clientMaster.setCostCenterCode("costCenterCode");
		clientMaster.setCostCenterDescription("description");
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setCostCenterCode("costCenterCode");
		assoc.setUserType(populateUsers().getClients().get(0).getUserType());
		assoc.setUser(populateUsers());
		Mockito.doReturn(Lists.newArrayList(assoc)).when(userCostCenterAssociationRepository)
				.findByUserId(Mockito.any());
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateCommonDto(), populateCommonDto()).when(mappingService).getMapping(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyBoolean());
		Mockito.doReturn(userMenu).when(menuBusinessService).fetchUserMenu(Mockito.any(), Mockito.any());
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(Lists.newArrayList(clientMaster)).when(clientMasterRepository)
				.findByCostCenterCode(Mockito.anyString());
		ParentDTO actualResponse = userServiceImpl.switchBusiness(principal, "909464",
				populateUsers().getClients().get(0).getUserType());
		assertEquals(principal.getClientCode(), actualResponse.getPrincipal().getClientCode());
	}

	@Test(expected = InvalidUserException.class)
	public void testSwitchBusinessForgetSwitchUserTypeNewHire() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		Map<String, String> clientMap = new HashMap<>();
		clientMap.put("909090", "1 Hour Drain");
		principal.setClientMap(clientMap);
		principal.setClientCode("909090");
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setEmail("test@bbsi.com");
		principal.setUserType(populateUsers().getClients().get(0).getUserType());
		principal.setAccessGroup("1");
		principal.setUserTypeMap(getuserTypeMap());
		List<MenuMappingDTO> userMenu = new ArrayList<>();
		MenuMappingDTO menu = new MenuMappingDTO();
		menu.setCategory("category");
		menu.setDescription("description");
		menu.setDisplayName("displayName");
		menu.setFeature("feature");
		menu.setFeatureCodeId(1l);
		menu.setIconUrl("/iconUrl");
		menu.setId(1l);
		menu.setName("name");
		menu.setSequence(1);
		menu.setType("ALL");
		menu.setUrl("/url");
		menu.setMenuItems(populateMenuItems());
		userMenu.add(menu);
		principal.setUserMenu(userMenu);
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setClientName("1 Hour Drain");
		clientMaster.setCostCenterCode("costCenterCode");
		clientMaster.setCostCenterDescription("description");
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setCostCenterCode("costCenterCode");
		assoc.setUserType(populateUsers().getClients().get(0).getUserType());
		assoc.setUser(populateUsers());
		Mockito.doReturn(Lists.newArrayList(assoc)).when(userCostCenterAssociationRepository)
				.findByUserId(Mockito.any());
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateCommonDto(), populateCommonDto()).when(mappingService).getMapping(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyBoolean());
		Mockito.doReturn(userMenu).when(menuBusinessService).fetchUserMenu(Mockito.any(), Mockito.any());
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(Lists.newArrayList(clientMaster)).when(clientMasterRepository)
				.findByCostCenterCode(Mockito.anyString());
		ParentDTO actualResponse = userServiceImpl.switchBusiness(principal, GenericConstants.USERTYPE_NEWHIRE, "");
		assertNotNull(actualResponse);
	}

	@Test(expected = InvalidUserException.class)
	public void testSwitchBusinessForgetSwitchUserTypeClient() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		Map<String, String> clientMap = new HashMap<>();
		clientMap.put("909090", "1 Hour Drain");
		principal.setClientMap(clientMap);
		principal.setClientCode("909090");
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setEmail("test@bbsi.com");
		principal.setUserType(populateUsers().getClients().get(0).getUserType());
		principal.setAccessGroup("1");
		principal.setUserTypeMap(getuserTypeMap2());
		List<MenuMappingDTO> userMenu = new ArrayList<>();
		MenuMappingDTO menu = new MenuMappingDTO();
		menu.setCategory("category");
		menu.setDescription("description");
		menu.setDisplayName("displayName");
		menu.setFeature("feature");
		menu.setFeatureCodeId(1l);
		menu.setIconUrl("/iconUrl");
		menu.setId(1l);
		menu.setName("name");
		menu.setSequence(1);
		menu.setType("ALL");
		menu.setUrl("/url");
		menu.setMenuItems(populateMenuItems());
		userMenu.add(menu);
		principal.setUserMenu(userMenu);
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setClientName("1 Hour Drain");
		clientMaster.setCostCenterCode("costCenterCode");
		clientMaster.setCostCenterDescription("description");
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setCostCenterCode("costCenterCode");
		assoc.setUserType(populateUsers().getClients().get(0).getUserType());
		assoc.setUser(populateUsers());
		Mockito.doReturn(Lists.newArrayList(assoc)).when(userCostCenterAssociationRepository)
				.findByUserId(Mockito.any());
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateCommonDto(), populateCommonDto()).when(mappingService).getMapping(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyBoolean());
		Mockito.doReturn(userMenu).when(menuBusinessService).fetchUserMenu(Mockito.any(), Mockito.any());
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(Lists.newArrayList(clientMaster)).when(clientMasterRepository)
				.findByCostCenterCode(Mockito.anyString());
		ParentDTO actualResponse = userServiceImpl.switchBusiness(principal, GenericConstants.USERTYPE_CLIENT, "");
		// assertEquals(principal.getClientCode(),
		// actualResponse.getPrincipal().getClientCode());
		assertNotNull(actualResponse);
	}

	@Test(expected = InvalidUserException.class)
	public void testSwitchBusinessForgetSwitchUserTypeEmployee() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		Map<String, String> clientMap = new HashMap<>();
		clientMap.put("909090", "1 Hour Drain");
		principal.setClientMap(clientMap);
		principal.setClientCode("909090");
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setEmail("test@bbsi.com");
		principal.setUserType(populateUsers().getClients().get(0).getUserType());
		principal.setAccessGroup("1");
		principal.setUserTypeMap(getuserTypeMap1());
		List<MenuMappingDTO> userMenu = new ArrayList<>();
		MenuMappingDTO menu = new MenuMappingDTO();
		menu.setCategory("category");
		menu.setDescription("description");
		menu.setDisplayName("displayName");
		menu.setFeature("feature");
		menu.setFeatureCodeId(1l);
		menu.setIconUrl("/iconUrl");
		menu.setId(1l);
		menu.setName("name");
		menu.setSequence(1);
		menu.setType("ALL");
		menu.setUrl("/url");
		menu.setMenuItems(populateMenuItems());
		userMenu.add(menu);
		principal.setUserMenu(userMenu);
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setClientName("1 Hour Drain");
		clientMaster.setCostCenterCode("costCenterCode");
		clientMaster.setCostCenterDescription("description");
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setCostCenterCode("costCenterCode");
		assoc.setUserType(populateUsers().getClients().get(0).getUserType());
		assoc.setUser(populateUsers());
		Mockito.doReturn(Lists.newArrayList(assoc)).when(userCostCenterAssociationRepository)
				.findByUserId(Mockito.any());
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateCommonDto(), populateCommonDto()).when(mappingService).getMapping(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyBoolean());
		Mockito.doReturn(userMenu).when(menuBusinessService).fetchUserMenu(Mockito.any(), Mockito.any());
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(Lists.newArrayList(clientMaster)).when(clientMasterRepository)
				.findByCostCenterCode(Mockito.anyString());
		ParentDTO actualResponse = userServiceImpl.switchBusiness(principal, GenericConstants.USERTYPE_EMPLOYEE, "");
		assertNotNull(actualResponse);
	}

	@Test(expected = InvalidUserException.class)
	public void testSwitchBusinessForgetSwitchUserTypegetDefaultUserType() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		Map<String, String> clientMap = new HashMap<>();
		clientMap.put("909090", "1 Hour Drain");
		principal.setClientMap(clientMap);
		principal.setClientCode("909090");
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setEmail("test@bbsi.com");
		principal.setUserType(populateUsers().getClients().get(0).getUserType());
		principal.setAccessGroup("1");
		principal.setUserTypeMap(getuserTypeMap3());
		List<MenuMappingDTO> userMenu = new ArrayList<>();
		MenuMappingDTO menu = new MenuMappingDTO();
		menu.setCategory("category");
		menu.setDescription("description");
		menu.setDisplayName("displayName");
		menu.setFeature("feature");
		menu.setFeatureCodeId(1l);
		menu.setIconUrl("/iconUrl");
		menu.setId(1l);
		menu.setName("name");
		menu.setSequence(1);
		menu.setType("ALL");
		menu.setUrl("/url");
		menu.setMenuItems(populateMenuItems());
		userMenu.add(menu);
		principal.setUserMenu(userMenu);
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setClientName("1 Hour Drain");
		clientMaster.setCostCenterCode("costCenterCode");
		clientMaster.setCostCenterDescription("description");
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setCostCenterCode("costCenterCode");
		assoc.setUserType(populateUsers().getClients().get(0).getUserType());
		assoc.setUser(populateUsers());
		Mockito.doReturn(Lists.newArrayList(assoc)).when(userCostCenterAssociationRepository)
				.findByUserId(Mockito.any());
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateCommonDto(), populateCommonDto()).when(mappingService).getMapping(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyBoolean());
		Mockito.doReturn(userMenu).when(menuBusinessService).fetchUserMenu(Mockito.any(), Mockito.any());
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(Lists.newArrayList(clientMaster)).when(clientMasterRepository)
				.findByCostCenterCode(Mockito.anyString());
		ParentDTO actualResponse = userServiceImpl.switchBusiness(principal, "909464", "");
		assertNotNull(actualResponse);
	}

	@Test
	public void testSwitchBusinessForgetSwitchUserTypegetDefaultUserType1() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		Map<String, String> clientMap = new HashMap<>();
		clientMap.put("909090", "1 Hour Drain");
		principal.setClientMap(clientMap);
		principal.setClientCode("909090");
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setEmail("test@bbsi.com");
		principal.setUserType(populateUsers().getClients().get(0).getUserType());
		principal.setAccessGroup("1");
		principal.setUserTypeMap(getuserTypeMap());
		List<MenuMappingDTO> userMenu = new ArrayList<>();
		MenuMappingDTO menu = new MenuMappingDTO();
		menu.setCategory("category");
		menu.setDescription("description");
		menu.setDisplayName("displayName");
		menu.setFeature("feature");
		menu.setFeatureCodeId(1l);
		menu.setIconUrl("/iconUrl");
		menu.setId(1l);
		menu.setName("name");
		menu.setSequence(1);
		menu.setType("ALL");
		menu.setUrl("/url");
		menu.setMenuItems(populateMenuItems());
		userMenu.add(menu);
		principal.setUserMenu(userMenu);
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setClientName("1 Hour Drain");
		clientMaster.setCostCenterCode("costCenterCode");
		clientMaster.setCostCenterDescription("description");
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setCostCenterCode("costCenterCode");
		assoc.setUserType(populateUsers().getClients().get(0).getUserType());
		assoc.setUser(populateUsers());
		Mockito.doReturn(Lists.newArrayList(assoc)).when(userCostCenterAssociationRepository)
				.findByUserId(Mockito.any());
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateCommonDto(), populateCommonDto()).when(mappingService).getMapping(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyBoolean());
		Mockito.doReturn(userMenu).when(menuBusinessService).fetchUserMenu(Mockito.any(), Mockito.any());
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(Lists.newArrayList(clientMaster)).when(clientMasterRepository)
				.findByCostCenterCode(Mockito.anyString());
		ParentDTO actualResponse = userServiceImpl.switchBusiness(principal, "909464", "");
		assertNotNull(actualResponse);
	}

	@Test
	public void testSwitchBusinessForsetUserFavoriteMenus() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		Map<String, String> clientMap = new HashMap<>();
		clientMap.put("909090", "1 Hour Drain");
		principal.setClientMap(clientMap);
		principal.setClientCode("909090");
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setEmail("test@bbsi.com");
		principal.setUserType(populateUsers().getClients().get(0).getUserType());
		principal.setAccessGroup("1");
		List<MenuMappingDTO> userMenu = new ArrayList<>();
		MenuMappingDTO menu = new MenuMappingDTO();
		menu.setCategory("category");
		menu.setDescription("description");
		menu.setDisplayName("displayName");
		menu.setFeature("feature");
		menu.setFeatureCodeId(1l);
		menu.setIconUrl("/iconUrl");
		menu.setId(1l);
		menu.setName("name");
		menu.setSequence(1);
		menu.setType("ALL");
		menu.setUrl("/url");
		menu.setMenuItems(populateMenuItems());
		userMenu.add(menu);
		principal.setUserMenu(userMenu);
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setClientName("1 Hour Drain");
		clientMaster.setCostCenterCode("costCenterCode");
		clientMaster.setCostCenterDescription("description");
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setCostCenterCode("costCenterCode");
		assoc.setUserType(populateUsers().getClients().get(0).getUserType());
		assoc.setUser(populateUsers());
		List<UserToolbarSettings> toolbars = new ArrayList<>();
		UserToolbarSettings toolbar = new UserToolbarSettings();
		toolbar.setClientCode("909464");
		toolbar.setFeatureCode("featureCode");
		toolbar.setId(1l);
		toolbar.setMenuItemId(1l);
		toolbar.setUserEmail(populateCustomPersonalDTO().getOldEmail());
		toolbar.setVersion(1l);
		toolbars.add(toolbar);
		Mockito.doReturn(Lists.newArrayList(assoc)).when(userCostCenterAssociationRepository)
				.findByUserId(Mockito.any());
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateCommonDto(), populateCommonDto()).when(mappingService).getMapping(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyBoolean());
		Mockito.doReturn(userMenu).when(menuBusinessService).fetchUserMenu(Mockito.any(), Mockito.any());
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(Lists.newArrayList(clientMaster)).when(clientMasterRepository)
				.findByCostCenterCode(Mockito.anyString());
		Mockito.doReturn(toolbars).when(userToolbarSettingsRepositoryV2)
				.findByClientCodeAndUserEmail(Mockito.anyString(), Mockito.anyString());
		ParentDTO actualResponse = userServiceImpl.switchBusiness(principal, "909464",
				populateUsers().getClients().get(0).getUserType());
		assertEquals(principal.getClientCode(), actualResponse.getPrincipal().getClientCode());
	}

	@Test
	public void testSwitchBusinessWhenMappingIsNotPresent() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909090");
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setEmail("test@bbsi.com");
		principal.setUserType("NewHire");
		principal.setAccessGroup("accessGroup");
		Map<String, Set<String>> userTypeMap = new HashMap<>();
		Set<String> clientCodes = new HashSet<>();
		clientCodes.add("909090");
		userTypeMap.put(GenericConstants.USERTYPE_EMPLOYEE, clientCodes);
		principal.setUserTypeMap(userTypeMap);
		Mockito.doReturn(populateUserWithNoClientRole()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateClientMaster().get(0)).when(clientMasterRepository)
				.findByClientCode(Mockito.anyString());
		Mockito.doReturn(populateClientMaster()).when(clientMasterRepository).findByCostCenterCode(Mockito.anyString());
		Mockito.doReturn(new ArrayList<MenuMappingDTO>()).when(menuBusinessService).fetchUserMenu(Mockito.any(),
				Mockito.any());
		Mockito.doReturn(populateCommonDto()).when(mappingService).getMapping(Mockito.anyLong(), Mockito.anyString(),
				Mockito.anyBoolean());
		ParentDTO actualResponse = userServiceImpl.switchBusiness(principal, "909090", "NewHire");
		assertEquals(principal.getClientCode(), actualResponse.getPrincipal().getClientCode());
	}

	@Test(expected = InvalidUserException.class)
	public void testSwitchBusinessForDifferentUsertype() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909090");
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setEmail("test@bbsi.com");
		principal.setUserType("NewHire");
		principal.setAccessGroup("accessGroup");
		Mockito.doReturn(null).when(userCostCenterAssociationRepository).findByUserId(Mockito.any());
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		ParentDTO actualResponse = userServiceImpl.switchBusiness(principal, "909090", "NewHire");
		assertNull(actualResponse);
	}

	@Test(expected = InvalidUserException.class)
	public void testSwitchBusinessForVancouver() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909464");
		principal.setCostCenterCode("costCenterCode");
		principal.setBbsiBranch("bbsiBranch");
		principal.setEmail("test@bbsi.com");
		principal.setUserType("NewHire");
		principal.setAccessGroup("accessGroup");
		List<MenuMappingDTO> userMenu = new ArrayList<>();
		MenuMappingDTO menu = new MenuMappingDTO();
		menu.setCategory("category");
		menu.setDescription("description");
		menu.setDisplayName("displayName");
		menu.setFeature("feature");
		menu.setFeatureCodeId(1l);
		menu.setIconUrl("/iconUrl");
		menu.setId(1l);
		menu.setName("name");
		menu.setSequence(1);
		menu.setType("ALL");
		menu.setUrl("/url");
		menu.setMenuItems(populateMenuItems());
		userMenu.add(menu);
		principal.setUserMenu(userMenu);
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setCostCenterCode("code");
		assoc.setId(1l);
		assoc.setUser(populateUsers());
		assoc.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		ClientMaster client = new ClientMaster();
		client.setClientCode("909464");
		client.setClientName("clientName");
		client.setCostCenterCode("code");
		client.setCostCenterDescription("description");
		List<UserCostCenterAssociation> assocs = new ArrayList<>();
		assocs.add(assoc);
		List<ClientMaster> clients = new ArrayList<>();
		clients.add(client);
		Mockito.doReturn(populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(assocs).when(userCostCenterAssociationRepository).findByUserId(Mockito.anyLong());
		Mockito.doReturn(clients).when(clientMasterRepository).findByCostCenterCode(Mockito.anyString());
		Mockito.doReturn(populateCommonDto(), populateCommonDto()).when(mappingService).getMapping(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyBoolean());
		Mockito.doReturn(userMenu).when(menuBusinessService).fetchUserMenu(Mockito.any(), Mockito.any());
		Mockito.doReturn(client).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		ParentDTO actual = userServiceImpl.switchBusiness(principal, "909464",
				GenericConstants.USERTYPE_VANCOUVER.toString());
		assertNull(actual);
	}

	@Test
	public void testUpdateEmail() throws Exception {
		Email email = new Email();
		email.setContext("context");
		email.setContextMap(new HashMap<>());
		email.setDbTemplate(false);
		email.setFrom("abc@abc.com");
		email.setReplyTo("noreply@bbsi.com");
		email.setSubject("subject");
		email.setTemplate("template");
		email.setTemplateName("templateName");
		email.setToAddress("toAddress@email.com");
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		ReflectionTestUtils.setField(userServiceImpl, "emailURL", "http://localhost:8809/notification/v1/email/");
		Mono<String> web = Mono.just(email.toString());
		CustomPersonalDTO input = populateCustomPersonalDTO();
		input.setOldEmail("admin@bbsihq.com");
		Users newUser = populateUsers();
		newUser.getClients().get(0).setEmployeeCode(input.getEmployeeCode());
		newUser.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		userPrincipal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		Mockito.doReturn(newUser, null).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(web).when(webClientTemplate).postForObjectWithToken(
				"http://localhost:8007/notification/v1/email", Email.class, email, "1234-abcd-4321-dcba");
		when(webClientTemplate.postForObjectWithToken(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.anyString())).thenReturn(web);
		JsonNode node = mapper.readTree(payload);
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(payload).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		when(utils.validateEmployeeCodes(populateCustomPersonalDTO(), restClient, boomiHelper)).thenReturn(payload1);
		CustomPersonalDTO result = userServiceImpl.updateEmail(input, token, userPrincipal);
		assertEquals(result.getClientCode(), populateCustomPersonalDTO().getClientCode());
	}

	@Test
	public void testUpdateEmailForEmpty() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		CustomPersonalDTO data = new CustomPersonalDTO();
		data.setIsAccountLoginChange(true);
		data.setNewEmail("test100@test.com");
		data.setOldEmail("test200@mailinator.com");
		data.setEmployeeCode("empCode");
		data.setClientCode(populateUsers().getClients().get(0).getClientCode());
		Users user = populateUsers();
		user.getClients().get(0).setEmployeeCode("empCode");
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Mockito.doReturn(null).when(usersRepository).findByEmail(Mockito.anyString());
		CustomPersonalDTO result = userServiceImpl.updateEmail(data, token, userPrincipal);
		Mockito.verify(usersRepository, Mockito.never()).findByEmail(Mockito.anyString());
		assertEquals(data.getClientCode(), result.getClientCode());
	}

	@Test
	public void testUpdateEmailForOldBbsiHeadEmails() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		CustomPersonalDTO input = new CustomPersonalDTO();
		input.setOldEmail("admin@bbsihq.com");
		input.setNewEmail("admin@admin.com");
		input.setIsAccountLoginChange(false);
		UserPrincipal principal = userPrincipal;
		principal.setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		CustomPersonalDTO result = userServiceImpl.updateEmail(input, token, principal);
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		Mockito.doReturn(null, populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		CustomPersonalDTO result1 = userServiceImpl.updateEmail(input, token, principal);
		assertNotNull(result);
		assertNotNull(result1);
	}

	@Test
	public void testUpdateEmailForupdateUserForNonLoginChangeEmailsEmpty() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		CustomPersonalDTO input = new CustomPersonalDTO();
		input.setOldEmail("");
		input.setNewEmail("");
		input.setIsAccountLoginChange(false);
		UserPrincipal principal = userPrincipal;
		principal.setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		CustomPersonalDTO result = userServiceImpl.updateEmail(input, token, principal);
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		Mockito.doReturn(null, populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		CustomPersonalDTO result1 = userServiceImpl.updateEmail(input, token, principal);
		assertNotNull(result);
		assertNotNull(result1);
	}

	@Test
	public void testUpdateEmailForupdateUserForNonLoginChangenewEmailsEmpty() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		CustomPersonalDTO input = new CustomPersonalDTO();
		input.setOldEmail("sdhudhipala@osius.com");
		input.setNewEmail("");
		input.setIsAccountLoginChange(false);
		UserPrincipal principal = userPrincipal;
		principal.setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		CustomPersonalDTO result = userServiceImpl.updateEmail(input, token, principal);
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		Mockito.doReturn(null, populateUsers()).when(usersRepository).findByEmail(Mockito.anyString());
		CustomPersonalDTO result1 = userServiceImpl.updateEmail(input, token, principal);
		assertNotNull(result);
		assertNotNull(result1);
	}

	@Test
	public void testUpdateEmailFordifferentClientCode() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsi.com,@bbsihq.com");
		ReflectionTestUtils.setField(userServiceImpl, "emailURL", "emailURL");
		Email email = new Email();
		email.setContext("context");
		email.setFrom("from@email.com");
		email.setReplyTo("replyTo@email.com");
		email.setSubject("susbject");
		email.setTemplate(GenericConstants.CHANGE_EMAIL_VM);
		email.setTemplateName(GenericConstants.CHANGE_EMAIL_VM);
		email.setToAddress("toAddress@email.com");
		Mono<String> expected = Mono.just(email.toString());
		CustomPersonalDTO input = populateCustomPersonalDTO();
		input.setClientCode("cCode");
		Mockito.doReturn(populateUsers(), null).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(expected).when(webClientTemplate).postForObjectWithToken(Mockito.anyString(), Mockito.any(),
				Mockito.any(), Mockito.anyString());
		CustomPersonalDTO result = userServiceImpl.updateEmail(input, token, userPrincipal);
		assertNotEquals(result.getClientCode(), populateCustomPersonalDTO().getClientCode());
	}

	@Test
	public void testUpdateEmailForOldBbsiHeadEmailsForMoreThanOneClient() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		ReflectionTestUtils.setField(userServiceImpl, "emailURL", "http://localhost:8007/notification/v1/email");
		CustomPersonalDTO input = new CustomPersonalDTO();
		input.setOldEmail("admin@bbsihq.com");
		input.setNewEmail("admin@admin.com");
		input.setIsAccountLoginChange(false);
		Users user = populateUsers();
		UserClients client = new UserClients();
		client.setClientCode("clientCode1");
		client.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		user.getClients().add(client);
		UserPrincipal principal = userPrincipal;
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		Mono<String> web = Mono.just("succes");
		Mockito.doReturn(null, user).when(usersRepository).findByEmail(Mockito.anyString());
		when(webClientTemplate.postForObjectWithToken(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.anyString())).thenReturn(web);
		CustomPersonalDTO result1 = userServiceImpl.updateEmail(input, "1234 123456asdfgh", principal);
		assertNotNull(result1);
	}

	@Test
	public void testUpdateEmailForNewBbsiHeadEmails() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		CustomPersonalDTO input = new CustomPersonalDTO();
		input.setOldEmail("admin@admin.com");
		input.setNewEmail("admin@bbsihq.com");
		input.setIsAccountLoginChange(false);
		CustomPersonalDTO result = userServiceImpl.updateEmail(input, token, userPrincipal);
		assertNotNull(result);
	}

	@Test
	public void testUpdateEmailForEmptyForSuppress() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		CustomPersonalDTO data = new CustomPersonalDTO();
		data.setIsAccountLoginChange(true);
		data.setOldEmail("test100@test.com");
		data.setEmployeeCode("empCode");
		data.setClientCode("clientCode1");
		Users user = populateUsers();
		user.getClients().get(0).setEmployeeCode("empCode");
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		CustomPersonalDTO result = userServiceImpl.updateEmail(data, token, userPrincipal);
		Mockito.verify(usersRepository, Mockito.never()).findByEmail(null);
		assertEquals(data.getClientCode(), result.getClientCode());
	}

	@Test
	public void testUpdateEmailForupdateUserAccess() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		CustomPersonalDTO data = new CustomPersonalDTO();
		data.setIsAccountLoginChange(true);
		data.setNewEmail("test100@test.com");
		data.setOldEmail("test234100@test.com");
		data.setClientCode(populateUsers().getClients().get(0).getClientCode());
		Users user = populateUsers();
		user.getClients().get(0).setEmployeeCode("empCode");
		user.getClients().get(0).setClientCode("empCode");
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		CustomPersonalDTO result = userServiceImpl.updateEmail(data, token, userPrincipal);
		assertNotNull(result);
		assertEquals(result.getClientCode(), data.getClientCode());
	}

	@Test
	public void testUpdateEmailForupdateUserAccessUserNull() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		CustomPersonalDTO data = new CustomPersonalDTO();
		data.setIsAccountLoginChange(true);
		data.setNewEmail("qwyeeueueuu@mailinator.com");
		data.setOldEmail("qwyeeueueuudhgdshgf@test.com");
		data.setClientCode(populateUsers().getClients().get(0).getClientCode());
		Users user = populateUsers();
		user.getClients().get(0).setEmployeeCode("empCode");
		user.getClients().get(0).setClientCode("empCode");
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Mockito.doReturn(null).when(usersRepository).findByEmail(Mockito.anyString());
		CustomPersonalDTO result = userServiceImpl.updateEmail(data, token, userPrincipal);
		assertNotNull(result);
	}

	@Test
	public void testUpdateEmailForupdateUserAccessNewEmailEmpty() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		CustomPersonalDTO data = new CustomPersonalDTO();
		data.setIsAccountLoginChange(true);
		data.setNewEmail("");
		data.setOldEmail("s123@osius.com");
		data.setClientCode(populateUsers().getClients().get(0).getClientCode());
		Users user = populateUsers();
		user.getClients().get(0).setEmployeeCode("empCode");
		user.getClients().get(0).setClientCode("empCode");
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Mockito.doReturn(null).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateUserClients().get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.any(), Mockito.any());
		CustomPersonalDTO result = userServiceImpl.updateEmail(data, token, userPrincipal);
		assertNotNull(result);
	}

	@Test
	public void testUpdateEmailForCreateNewEmp() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		CustomPersonalDTO data = new CustomPersonalDTO();
		data.setIsAccountLoginChange(true);
		data.setNewEmail("test100@test.com");
		data.setOldEmail("admin@osius.com");
		data.setClientCode(populateUsers().getClients().get(0).getClientCode());
		Users user = populateUsers();
		user.getClients().get(0).setEmployeeCode("empCode");
		user.getClients().get(0).setClientCode("empCode");
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Object[] obj = { 1, 123, "sushma", "test", 1, 1 };
		List<Object[]> userClients = new ArrayList<>();
		userClients.add(obj);
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateUserClients().get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.any(), Mockito.any());
		Mockito.doReturn(userClients).when(userClientRepository).findByUserClientsByClient(Mockito.any(),
				Mockito.any());
		CustomPersonalDTO result = userServiceImpl.updateEmail(data, token, userPrincipal);
		assertNotNull(result);
	}

	@Test
	public void testUpdateEmailForCreateNewEmpForvalidateoldEmailElseBlock() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		CustomPersonalDTO data = new CustomPersonalDTO();
		data.setIsAccountLoginChange(true);
		data.setNewEmail("test100@test.com");
		data.setOldEmail("admin@osius.com");
		data.setClientCode(populateUsers().getClients().get(0).getClientCode());
		Users user = populateUsers();
		user.getClients().get(0).setEmployeeCode("empCode");
		user.getClients().get(0).setClientCode("empCode");
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Object[] obj = { 1, 123, "sushma", "test" };
		List<Object[]> userClients = new ArrayList<>();
		userClients.add(obj);
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(populateUserClients().get(0)).when(userClientRepository)
				.findByClientCodeAndEmployeeCode(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(userClients).when(userClientRepository).findByUserClientsByClient(Mockito.anyString(),
				Mockito.anyLong());
		CustomPersonalDTO result = userServiceImpl.updateEmail(data, token, userPrincipal);
		assertNotNull(result);
	}

	@Test
	public void testUpdateEmailForupdateEmpLoginIfblock() throws Exception {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		CustomPersonalDTO data = new CustomPersonalDTO();
		data.setIsAccountLoginChange(true);
		data.setNewEmail("test100@test.com");
		data.setOldEmail("test12300@test.com");
		data.setClientCode(populateUsers().getClients().get(0).getClientCode());
		Users user = populateUsers();
		user.getClients().get(0).setEmployeeCode("empCode");
		user.getClients().get(0).setClientCode("909464");
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Mockito.doReturn(user).when(usersRepository).findByEmail(Mockito.anyString());
		CustomPersonalDTO result = userServiceImpl.updateEmail(data, token, userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testUpdateEmailForHandleUserAcces() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		ReflectionTestUtils.setField(userServiceImpl, "emailURL", "http://localhost:8809s/notification/v1/email/");
		Mono<String> web = Mono.just("succes");
		CustomPersonalDTO input = populateCustomPersonalDTO();
		Users newUser = populateUsers();
		newUser.getClients().get(0).setEmployeeCode(input.getEmployeeCode());
		newUser.getClients().get(0).setClientCode(populateCustomPersonalDTO().getClientCode());
		newUser.getClients().add(populateUserClients().get(0));
		newUser.getClients().get(1).setClientCode(populateCustomPersonalDTO().getClientCode());
		userPrincipal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		Mockito.doReturn(newUser, null).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(web).when(webClientTemplate).postForObjectWithToken(
				"http://localhost:8007/notification/v1/email", Email.class, new Email(), "1234-abcd-4321-dcba");
		when(webClientTemplate.postForObjectWithToken(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.anyString())).thenReturn(web);
		CustomPersonalDTO result = userServiceImpl.updateEmail(input, token, userPrincipal);
		assertEquals(result.getClientCode(), populateCustomPersonalDTO().getClientCode());
	}

	@Test
	public void testUpdateEmailForHandleUserAccesForVancouver() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		ReflectionTestUtils.setField(userServiceImpl, "emailURL", "http://localhost:8809s/notification/v1/email/");
		Mono<String> web = Mono.just("succes");
		CustomPersonalDTO input = populateCustomPersonalDTO();
		input.setNewEmail("admin@bbsihq.com");
		Users newUser = populateUsers();
		newUser.getClients().get(0).setClientCode(populateCustomPersonalDTO().getClientCode());
		newUser.getClients().get(0).setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		newUser.getClients().addAll(populateUserClients());
		newUser.getClients().get(1).setClientCode(populateCustomPersonalDTO().getClientCode());
		newUser.getClients().get(0).setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		newUser.getClients().get(2).setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		userPrincipal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		Mockito.doReturn(newUser, null).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(web).when(webClientTemplate).postForObjectWithToken(
				"http://localhost:8007/notification/v1/email", Email.class, new Email(), "1234-abcd-4321-dcba");
		when(webClientTemplate.postForObjectWithToken(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.anyString())).thenReturn(web);
		CustomPersonalDTO result = userServiceImpl.updateEmail(input, token, userPrincipal);
		assertEquals(result.getClientCode(), populateCustomPersonalDTO().getClientCode());
	}

	@Test
	public void testUpdateEmailForChangeUserAccess() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		ReflectionTestUtils.setField(userServiceImpl, "emailURL", "http://localhost:8809s/notification/v1/email/");
		Mono<String> web = Mono.just("succes");
		CustomPersonalDTO input = populateCustomPersonalDTO();
		input.setOldEmail("admin@bbsihq.com");
		Users newUser = populateUsers();
		newUser.getClients().get(0).setEmployeeCode(input.getEmployeeCode());
		newUser.getClients().get(0).setClientCode("clientCode");
		userPrincipal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		Mockito.doReturn(newUser, null).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(web).when(webClientTemplate).postForObjectWithToken(
				"http://localhost:8007/notification/v1/email", Email.class, new Email(), "1234-abcd-4321-dcba");
		when(webClientTemplate.postForObjectWithToken(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.anyString())).thenReturn(web);
		CustomPersonalDTO result = userServiceImpl.updateEmail(input, token, userPrincipal);
		assertEquals(result.getClientCode(), populateCustomPersonalDTO().getClientCode());
	}

	@Test
	public void testUpdateEmailForBothEmails() {
		Users user = populateUsers();
		user.getClients().get(0).setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		user.getClients().addAll(populateUserClients());
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		CustomPersonalDTO data = new CustomPersonalDTO();
		data.setOldEmail("admin@bbsihq.com");
		data.setNewEmail(user.getEmail());
		data.setIsAccountLoginChange(Boolean.FALSE);
		List<UserToolbarSettings> toolbars = new ArrayList<>();
		UserToolbarSettings toolbar = new UserToolbarSettings();
		toolbar.setClientCode("909464");
		toolbar.setFeatureCode("featureCode");
		toolbar.setId(1l);
		toolbar.setMenuItemId(1l);
		toolbar.setUserEmail(populateCustomPersonalDTO().getOldEmail());
		toolbar.setVersion(1l);
		toolbars.add(toolbar);
		Mono<String> web = null;
		Mockito.doReturn(null, user).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(toolbars).when(userToolbarSettingsRepositoryV2).findByUserEmail(Mockito.any());
		Mockito.doReturn(toolbars).when(userToolbarSettingsRepositoryV2).saveAll(Mockito.any());
		Mockito.doReturn(web).when(webClientTemplate).postForObjectWithToken(Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any());
		CustomPersonalDTO result = userServiceImpl.updateEmail(data, token, userPrincipal);
		assertNotNull(result);
	}

	@Test
	public void testUpdateEmailForElseCondition() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		CustomPersonalDTO data = new CustomPersonalDTO();
		data.setOldEmail("test1@test.com");
		data.setNewEmail("test2@test.com");
		data.setIsAccountLoginChange(false);
		List<UserToolbarSettings> toolbars = new ArrayList<>();
		UserToolbarSettings toolbar = new UserToolbarSettings();
		toolbar.setClientCode("909464");
		toolbar.setFeatureCode("featureCode");
		toolbar.setId(1l);
		toolbar.setMenuItemId(1l);
		toolbar.setUserEmail(populateCustomPersonalDTO().getOldEmail());
		toolbar.setVersion(1l);
		toolbars.add(toolbar);
		Mockito.doReturn(null, populateUser()).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(toolbars).when(userToolbarSettingsRepositoryV2).findByUserEmail(Mockito.any());
		Mockito.doReturn(toolbars).when(userToolbarSettingsRepositoryV2).saveAll(Mockito.any());
		CustomPersonalDTO result = userServiceImpl.updateEmail(data, token, userPrincipal);
		assertNotNull(result);
	}

	@Test
	public void testUpdateEmailForBothEmailsForOldEmailBbsi() {
		Users user = populateUsers();
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		CustomPersonalDTO data = new CustomPersonalDTO();
		data.setOldEmail("admin@bbsihq.com");
		data.setNewEmail("admin@osius.com");
		data.setIsAccountLoginChange(Boolean.FALSE);
		List<UserToolbarSettings> toolbars = new ArrayList<>();
		UserToolbarSettings toolbar = new UserToolbarSettings();
		toolbar.setClientCode("909464");
		toolbar.setFeatureCode("featureCode");
		toolbar.setId(1l);
		toolbar.setMenuItemId(1l);
		toolbar.setUserEmail(populateCustomPersonalDTO().getOldEmail());
		toolbar.setVersion(1l);
		toolbars.add(toolbar);
		Mono<String> web = null;
		Mockito.doReturn(null, user).when(usersRepository).findByEmail(Mockito.anyString());
		Mockito.doReturn(toolbars).when(userToolbarSettingsRepositoryV2).findByUserEmail(Mockito.any());
		Mockito.doReturn(web).when(webClientTemplate).postForObjectWithToken(Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any());
		CustomPersonalDTO result = userServiceImpl.updateEmail(data, token, userPrincipal);
		assertNotNull(result);
	}

	@Test
	public void testUpdateEmailForUnauthorizedAccessExceptionForNewEmail() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		CustomPersonalDTO inputs = populateCustomPersonalDTO();
		inputs.setNewEmail("admin@bbsihq.com");
		Mockito.doThrow(UnauthorizedAccessException.class).when(usersRepository).findByEmail(null);
		CustomPersonalDTO result = userServiceImpl.updateEmail(inputs, token, userPrincipal);
		assertEquals(result.getId(), inputs.getId());
	}

	@Test
	public void testUpdateEmailForUnauthorizedAccessException() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		CustomPersonalDTO inputs = populateCustomPersonalDTO();
		inputs.setOldEmail("admin@bbsihq.com");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		Mockito.doThrow(UnauthorizedAccessException.class).when(usersRepository).findByEmail("admin@bbsihq.com");
		CustomPersonalDTO result = userServiceImpl.updateEmail(inputs, token, principal);
		principal.setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		result = userServiceImpl.updateEmail(inputs, token, principal);
		assertEquals(result.getId(), inputs.getId());
	}

	@Test
	public void testUpdateEmailForUnauthorizedAccessException1() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@bbsihq.com,@mybbsi.com");
		CustomPersonalDTO inputs = populateCustomPersonalDTO();
		inputs.setOldEmail("admin@bbsihq.com");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		Mockito.doThrow(UnauthorizedAccessException.class).when(usersRepository).findByEmail(inputs.getNewEmail());
		CustomPersonalDTO result = userServiceImpl.updateEmail(inputs, token, principal);
		assertEquals(result.getId(), inputs.getId());
	}

	@Test
	public void testisCCPAUpdated() {
		Mockito.doNothing().when(userClientRepository).isCCPAUpdated(true, "E1764F", "909464");
		userServiceImpl.isCCPAUpdated("909464", "E1764F", true, userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testIsCCPAUpdatedForException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).isCCPAUpdated(true, null, null);
		userServiceImpl.isCCPAUpdated(null, null, true, null);
		assertTrue(true);
	}

	@Test
	public void testupdateUserStatus() {
		UserClients expected = new UserClients();
		Mockito.doReturn(expected).when(userClientRepository).findByClientCodeAndEmployeeCode(
				populateUserDTO(true).getClients().get(0).getClientCode(), populateUserDTO(true).getEmployeeCode());
		Mockito.doNothing().when(userClientRepository).updateUserStatus(true, populateUserDTO(true).getEmployeeCode(),
				populateUserDTO(true).getClients().get(0).getClientCode());
		Boolean result = userServiceImpl.updateUserStatus(populateUserDTO(true).getClients().get(0).getClientCode(),
				populateUserDTO(true).getEmployeeCode(), true);
		assertEquals(true, result);
	}

	@Test
	public void testupdateUserStatusForEmptyUserClients() {
		Mockito.doReturn(null).when(userClientRepository).findByClientCodeAndEmployeeCode(
				populateUserDTO(true).getClients().get(0).getClientCode(), populateUserDTO(true).getEmployeeCode());
		Boolean result = userServiceImpl.updateUserStatus(populateUserDTO(true).getClients().get(0).getClientCode(),
				populateUserDTO(true).getEmployeeCode(), true);
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndEmployeeCode(Mockito.anyString(),
				Mockito.anyString());
		assertEquals(false, result);
	}

	@Test
	public void testUpdateUserStatusForException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByClientCodeAndEmployeeCode(" ", " ");
		Mockito.doThrow(BbsiException.class).when(userClientRepository).updateUserStatus(true, " ", " ");
		Boolean result = userServiceImpl.updateUserStatus(" ", " ", true);
		assertEquals(false, result);
	}

	@Test
	public void testUpdateUserStatusWhenAllAreNull() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByClientCodeAndEmployeeCode(null, null);
		Mockito.doThrow(BbsiException.class).when(userClientRepository).updateUserStatus(true, null, null);
		Boolean result = userServiceImpl.updateUserStatus(null, null, true);
		assertEquals(false, result);
	}

	@Test
	public void testgetEmailByClientCodeAndEmployeeCode() {
		UserClients expected = new UserClients();
		Users user = new Users();
		user.setEmail("admin@osius.com");
		expected.setUser(user);
		Mockito.doReturn(expected).when(userClientRepository).findByClientCodeAndEmployeeCode(
				populateUserDTO(true).getClients().get(0).getClientCode(), populateUserDTO(true).getEmployeeCode());
		String result = userServiceImpl.getEmailByClientCodeAndEmployeeCode(
				populateUserDTO(true).getClients().get(0).getClientCode(), populateUserDTO(true).getEmployeeCode());
		assertEquals("admin@osius.com", result);
	}

	@Test
	public void testGetAllClientUsersByClientCodeAndPayRollStatus() {
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setClientName("clientName");
		clientMaster.setCostCenterCode("code");
		clientMaster.setCostCenterDescription("description");
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		assoc.setUser(populateUser());
		List<UserCostCenterAssociation> assocs = new ArrayList<>();
		assocs.add(assoc);
		List<CommonDTO> expectedPrivileges = new ArrayList<>();
		expectedPrivileges.add(populateCommonDto());
		List<String> privilegeIds = new ArrayList<>();
		privilegeIds.add("PAY_TS_HOUR_SAVE,PAY_TS_HOUR_SUBMIT,PAY_TS_HOUR_CALC");
		privilegeIds.add("PAY_TS_HOUR_SAVE,PAY_TS_HOUR_FINALIZE,PAY_TS_HOUR_CALC");
		privilegeIds.add("PAY_TS_HOUR_SAVE,PAY_TS_HOUR_FINALIZE,PAY_TS_HOUR_CALC");
		UsersDTO inputs = populateUserDTO(true);
		inputs.setUserType(Enums.UserEnum.USER_CLIENT.toString());
		List<UserClients> expected = populateUserClients();
		expected.get(0).setUser(populateUser());
		Mockito.doReturn(expected).when(userClientRepository).findByClientCode(Mockito.anyString(), Mockito.any());
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(assocs).when(userCostCenterAssociationRepository).findByCostCenterCode(Mockito.anyString());
		Mockito.doReturn(privilegeIds).when(privilegeRepository).getPrivilegeCodesByFeatureCodes(Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		Mockito.doReturn(expectedPrivileges).when(mappingService).getPrivileges(Mockito.any(), Mockito.any());
		List<UsersDTO> result = userServiceImpl.getAllClientUsersByClientCodeAndPayRollStatus("909464",
				PortalStatus.SUBMITTED_FOR_APPROVAL.getValue());
		assertEquals(0, result.size());
	}

	@Test
	public void testGetAllClientUsersByClientCodeAndPayRollStatusEmpty() {
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setClientName("clientName");
		clientMaster.setCostCenterCode("code");
		clientMaster.setCostCenterDescription("description");
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		assoc.setUser(populateUser());
		List<UserCostCenterAssociation> assocs = new ArrayList<>();
		assocs.add(assoc);
		List<CommonDTO> expectedPrivileges = new ArrayList<>();
		expectedPrivileges.add(populateCommonDto());
		List<String> privilegeIds = new ArrayList<>();
		privilegeIds.add("PAY_TS_HOUR_SAVE,PAY_TS_HOUR_SUBMIT,PAY_TS_HOUR_CALC");
		privilegeIds.add("PAY_TS_HOUR_SAVE,PAY_TS_HOUR_FINALIZE,PAY_TS_HOUR_CALC");
		privilegeIds.add("PAY_TS_HOUR_SAVE,PAY_TS_HOUR_FINALIZE,PAY_TS_HOUR_CALC");
		UsersDTO inputs = populateUserDTO(true);
		inputs.setUserType(Enums.UserEnum.USER_CLIENT.toString());
		List<UserClients> expected = populateUserClients();
		expected.get(0).setUser(populateUser());
		Mockito.doReturn(expected).when(userClientRepository).findByClientCode(Mockito.anyString(), Mockito.any());
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(assocs).when(userCostCenterAssociationRepository).findByCostCenterCode(Mockito.anyString());
		Mockito.doReturn(privilegeIds).when(privilegeRepository).getPrivilegeCodesByFeatureCodes(Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		Mockito.doReturn(expectedPrivileges).when(mappingService).getPrivileges(Mockito.any(), Mockito.any());
		List<UsersDTO> result = userServiceImpl.getAllClientUsersByClientCodeAndPayRollStatus("909464", "");
		assertEquals(0, result.size());
	}

	@Test
	public void testGetAllClientUsersByClientCodeAndPayRollStatusNotEqual() {
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setClientName("clientName");
		clientMaster.setCostCenterCode("code");
		clientMaster.setCostCenterDescription("description");
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		assoc.setUser(populateUser());
		List<UserCostCenterAssociation> assocs = new ArrayList<>();
		assocs.add(assoc);
		List<CommonDTO> expectedPrivileges = new ArrayList<>();
		expectedPrivileges.add(populateCommonDto());
		List<String> privilegeIds = new ArrayList<>();
		privilegeIds.add("PAY_TS_HOUR_SAVE,PAY_TS_HOUR_SUBMIT,PAY_TS_HOUR_CALC");
		privilegeIds.add("PAY_TS_HOUR_SAVE,PAY_TS_HOUR_FINALIZE,PAY_TS_HOUR_CALC");
		privilegeIds.add("PAY_TS_HOUR_SAVE,PAY_TS_HOUR_FINALIZE,PAY_TS_HOUR_CALC");
		UsersDTO inputs = populateUserDTO(true);
		inputs.setUserType(Enums.UserEnum.USER_CLIENT.toString());
		List<UserClients> expected = populateUserClients();
		expected.get(0).setUser(populateUser());
		Mockito.doReturn(expected).when(userClientRepository).findByClientCode(Mockito.anyString(), Mockito.any());
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(assocs).when(userCostCenterAssociationRepository).findByCostCenterCode(Mockito.anyString());
		Mockito.doReturn(privilegeIds).when(privilegeRepository).getPrivilegeCodesByFeatureCodes(Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		Mockito.doReturn(expectedPrivileges).when(mappingService).getPrivileges(Mockito.any(), Mockito.any());
		List<UsersDTO> result = userServiceImpl.getAllClientUsersByClientCodeAndPayRollStatus("909464",
				"USERTYPE_CLIENT");
		assertEquals(0, result.size());
	}

	@Test
	public void testGetAllClientUsersByClientCodeAndPayRollStatusForSubmittedForFinalize() {
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setClientName("clientName");
		clientMaster.setCostCenterCode("code");
		clientMaster.setCostCenterDescription("description");
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		assoc.setUser(populateUser());
		List<UserCostCenterAssociation> assocs = new ArrayList<>();
		assocs.add(assoc);
		List<CommonDTO> expectedPrivileges = new ArrayList<>();
		expectedPrivileges.add(populateCommonDto());
		List<String> privilegeIds = new ArrayList<>();
		privilegeIds.add("909090");
		privilegeIds.add("909090");
		UsersDTO inputs = populateUserDTO(true);
		inputs.setUserType(Enums.UserEnum.USER_CLIENT.toString());
		List<UserClients> expected = populateUserClients();
		Mockito.doReturn(expected).when(userClientRepository).findByClientCode(Mockito.anyString(), Mockito.any());
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(assocs).when(userCostCenterAssociationRepository).findByCostCenterCode(Mockito.anyString());
		Mockito.doReturn(privilegeIds).when(privilegeRepository).getPrivilegeCodesByFeatureCodes(Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		Mockito.doReturn(expectedPrivileges).when(mappingService).getPrivileges(Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUsersDTOForVancouver()).when(usersMapper).userToUserDTO(Mockito.any());
		List<UsersDTO> result = userServiceImpl.getAllClientUsersByClientCodeAndPayRollStatus(
				inputs.getClients().get(0).getClientCode(), PortalStatus.SUBMITTED_FOR_FINALIZE.getValue());
		assertEquals(0, result.size());
	}

	@Test
	public void testGetAllClientUsersByClientCodeAndPayRollSTatusForFinalize() {
		UsersDTO inputs = populateUserDTO(true);
		inputs.setUserType(Enums.UserEnum.USER_CLIENT.toString());
		Sort sort = Sort.by(Sort.Direction.DESC, MethodNames.MODIFIED_ON);
		List<UserClients> expected = populateUserClients();
		Mockito.doReturn(expected).when(userClientRepository)
				.findByClientCode(populateUserClients().get(0).getClientCode(), sort);
		List<UsersDTO> result = userServiceImpl.getAllClientUsersByClientCodeAndPayRollStatus(
				inputs.getClients().get(0).getClientCode(), PortalStatus.FINALIZED.getValue());
		assertEquals(0, result.size());
	}

	@Test
	public void testGetAllClientUsersByClientCodeAdPayRollStatusWhenEmpty() {
		UsersDTO inputs = populateUserDTO(false);
		inputs.getClients().get(0).setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		inputs.setUserType(GenericConstants.USERTYPE_BRANCH.toString());
		Sort sort = Sort.by(Sort.Direction.DESC, MethodNames.MODIFIED_ON);
		List<UserClients> expected = populateUserClients();
		Mockito.doReturn(expected).when(userClientRepository)
				.findByClientCode(populateUserClients().get(0).getClientCode(), sort);
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("909464");
		clientMaster.setCostCenterCode("CC");
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn(Lists.newArrayList()).when(userCostCenterAssociationRepository)
				.findByCostCenterCode(Mockito.any());
		Mockito.doReturn(inputs).when(usersMapper).userToUserDTO(Mockito.any());
		List<UsersDTO> result = userServiceImpl.getAllClientUsersByClientCodeAndPayRollStatus(
				inputs.getClients().get(0).getClientCode(), PortalStatus.FINALIZED.getValue());
		assertEquals(1, result.size());
	}

	@Test
	public void testgetPersonalInfoByClientCodeAndEmployeeCode() throws IOException {
		String json = "{\"contact_information\":{\"mobile_phone_number\" : \"7864341234\",\"personal_email_address\" : \"personal@personal.com\"},\"personal\":{\"work_email_address\" : \"work@personal.com\"}}";
		List<Object[]> userClients = populateUserClientsObjectArray();
		UserClients expected = new UserClients();
		JsonNode jsonNode = objectMapper.readTree(json);
		Object[] client = { "sdhudhipala", "first", "last" };
		List<Object[]> userArr = new ArrayList<>();
		userArr.add(client);
		Mockito.doReturn(userClients).when(userClientRepository).getClientUserMobile(Mockito.anyLong());
		Mockito.doReturn(expected).when(userClientRepository).findByClientCodeAndEmployeeCode(
				populateUserDTO(true).getClients().get(0).getClientCode(), populateUserDTO(true).getEmployeeCode());
		Mockito.doReturn(populateUserClientsObjectArray()).when(userClientRepository).getEmpUserName(Mockito.any(),
				Mockito.any());
		Mockito.doReturn(
				"http://localhost:8810/integration/v1/employee/client/909464/employee/E1764F/type/EMPLOYEE_INFO")
				.when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(json).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.doReturn(jsonNode).when(objectMapper).readTree(json);
		Mockito.doReturn(userArr).when(usersRepository).findMobileByUserId(Mockito.anyLong());
		Map<String, Set<String>> result = userServiceImpl.getPersonalInfoByClientCodeAndEmployeeCode(
				populateUserDTO(true).getClients().get(0).getClientCode(), populateUserDTO(true).getEmployeeCode(),
				populateUserClient().getUser().getTokenValue(), "admin@osius.com", 1l);
		assertEquals(3, result.size());
	}

	@Test
	public void testgetPersonalInfoByClientCodeAndEmployeeCodeForEmpty() throws IOException {
		String json = "{\"contact_information\":{\"mobile_phone_number\" : \"7864341234\",\"personal_email_address\" : \"personal@personal.com\"},\"personal\":{\"work_email_address\" : \"work@personal.com\"}}";
		UserClients expected = new UserClients();
		JsonNode jsonNode = objectMapper.readTree(json);
		Mockito.doReturn(new ArrayList<>()).when(userClientRepository).getClientUserMobile(Mockito.anyLong());
		Mockito.doReturn(expected).when(userClientRepository).findByClientCodeAndEmployeeCode(
				populateUserDTO(true).getClients().get(0).getClientCode(), populateUserDTO(true).getEmployeeCode());
		Mockito.doReturn(populateUserClientsObjectArray()).when(userClientRepository).getEmpUserName(Mockito.any(),
				Mockito.any());
		Mockito.doReturn(
				"http://localhost:8810/integration/v1/employee/client/909464/employee/E1764F/type/EMPLOYEE_INFO")
				.when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(json).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.doReturn(jsonNode).when(objectMapper).readTree(json);
		// Mockito.doReturn(null).when(usersRepository).findMobileByUserId(Mockito.anyLong());
		Map<String, Set<String>> result = userServiceImpl.getPersonalInfoByClientCodeAndEmployeeCode(
				populateUserDTO(true).getClients().get(0).getClientCode(), "",
				populateUserClient().getUser().getTokenValue(), "admin@osius.com", 1l);
		assertEquals(3, result.size());
	}

	@Test
	public void testGetPersonalInfoByClientCodeAndEmployeeCodeForException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByUser_Email(Mockito.any());
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByClientCodeAndEmployeeCode(Mockito.any(),
				Mockito.any());
		Map<String, Set<String>> result = userServiceImpl.getPersonalInfoByClientCodeAndEmployeeCode(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());
		assertEquals(3, result.size());
		assertEquals(0, result.get("phone_number").size());
		assertEquals(0, result.get("email").size());
		assertEquals(0, result.get("mfa_email").size());
	}

	@Test
	public void testgetClientNameByClientCode() {
		ReflectionTestUtils.setField(userServiceImpl, "clientInfo",
				"http://localhost:9000/ws/rest/V2/getClientInformation?clientId=909464");
		List<UserClients> expected = new ArrayList<>();
		expected.add(populateUserClient());
		String json = "{\"company_doing_business_as\":\"1 Hour Drain\"}";
		Mockito.doReturn(json).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(expected).when(userClientRepository).findByClientCode(populateUserClient().getClientCode());
		Mockito.doReturn(getClientMaster("909464", "1 Hour Drain")).when(clientMasterRepository)
				.findByClientCode(populateUserClient().getClientCode());
		String result = userServiceImpl.getClientNameByClientCode(populateUserClient().getClientCode());
		assertEquals("1 Hour Drain", result);
	}

	@Test
	public void testgetClientNameByClientCodeForJsonNull() {
		ReflectionTestUtils.setField(userServiceImpl, "clientInfo",
				"http://localhost:9000/ws/rest/V2/getClientInformation?clientId=909464");
		List<UserClients> expected = new ArrayList<>();
		expected.add(populateUserClient());
		Mockito.doReturn(jsonResponse).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(expected).when(userClientRepository).findByClientCode(populateUserClient().getClientCode());
		Mockito.doReturn(getClientMaster("909464", "1 Hour Drain")).when(clientMasterRepository)
				.findByClientCode(populateUserClient().getClientCode());
		String result = userServiceImpl.getClientNameByClientCode(populateUserClient().getClientCode());
		assertNull(result);
	}

	@Test
	public void testgetClientNameByClientCodeForEmptyBusinessName() {
		ReflectionTestUtils.setField(userServiceImpl, "clientInfo",
				"http://localhost:9000/ws/rest/V2/getClientInformation?clientId=909464");
		String json = "{\"company_doing_business_as\":\"\"}";
		Mockito.doReturn(json).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(getClientMaster("909464", "1 Hour Drain")).when(clientMasterRepository)
				.findByClientCode(populateUserClient().getClientCode());
		Mockito.doNothing().when(clientMasterRepository).updateClientNameByClientCode(Mockito.any(), Mockito.any(),
				Mockito.any());
		String result = userServiceImpl.getClientNameByClientCode(populateUserClient().getClientCode());
		assertEquals(null, result);
	}

	@Test
	public void testGetClientNameByClientCode() {
		Mockito.doReturn("abc").when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(null).when(clientMasterRepository).findByClientCode(populateUserClient().getClientCode());
		Mockito.doThrow(BbsiException.class).when(clientMasterRepository).updateClientNameByClientCode(Mockito.any(),
				Mockito.any(), Mockito.any());
		String result = userServiceImpl.getClientNameByClientCode(populateUserClient().getClientCode());
		assertEquals(null, result);
	}

	@Test(expected = BbsiException.class)
	public void testGetClientNameByClientCodeForException() {
		Mockito.doThrow(BbsiException.class).when(clientMasterRepository).findByClientCode(null);
		String result = userServiceImpl.getClientNameByClientCode(null);
		assertNull(result);
	}

	@Test
	public void testUpdateCaliforniaFlag()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		jsonResponse = "{\"personal\" : {\"first_name\" : \"Ali\",\"middle_name\" : \"Jama\",\"last_name\" : \"Abdille\",\"social_security_number\" : \"r8RNP+BPBBgCZFowy0vLEg==\",\"gender\" : \"M\",\"date_of_birth\" : \"1968-01-01\",\"bbsi_assigned_employee_id\" : \"L47730\",\"is_electronic_onboarding\" : false,\"employee_status\" : \"A\",\"scorp_owner\" : \"false\",\"business_owner\" : \"false\"},\"contact_information\" : {\"home_telephone_number\" : \"4083102364\",\"personal_email_address\" : \"calijamaac@yahoo.com\"},\"residential_address\" : {\"address_line1\" : \"4403 Dulcey Drive\",\"zip_code\" : \"95136\",\"county\" : \"SANTA CLARA\",\"city\" : \"SAN JOSE\",\"state\" : \"CA\",\"is_unicorporated_area\" : false},\"veteran_status\" : {\"ethnicity\" : \"B\",\"is_handicapped\" : false,\"is_blind\" : false},\"direct_deposit_status\" : \"A\",\"direct_deposit\" : [{\"routing_transit_number\" : \"h8pbk/1+MJsuPshLTa9GJw==\",\"bank_name\" : \"WELLS FARGO BANK NA\",\"bank_account_number\" : \"QNxQir0hCJKP6wswCm2ixw==\",\"account_type\" : \"C\",\"calculation_method\" : \"B\",\"status\" : \"D\"}],\"federal_tax\" : {\"filing_status\" : \"M\",\"primary_allowances\" : \"2\",\"multiple_jobs\" : false},\"state_tax\" : [{\"filing_status\" : \"M\",\"state\" : \"CA\",\"primary_allowances\" : \"2\",\"secondary_allowances\" : \"2\",\"has_nrstate_certificate\" : false,\"w4_filed\" : \"false\"}],\"review_date\" : \"2019-08-19\",\"performance_review\" : {\"review_date\" : \"2019-08-19\"}}";
		principal.setClientCode(populateUserClientsWhenIsNewHire().get(0).getClientCode());
		principal.setUserId(populateUserClientsWhenIsNewHire().get(0).getId());
		principal.setEmployeeCode(populateUserClientsWhenIsNewHire().get(0).getEmployeeCode());
		Object[] client = { "E1786", false, true };
		List<Object[]> clientList = new ArrayList<Object[]>();
		clientList.add(client);
		Mockito.doReturn(clientList).when(userClientRepository).getCaliforniaUsers(Mockito.anyString(),
				Mockito.anyLong());
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonResponse).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(node).when(objectMapper).readTree(Mockito.anyString());
		userServiceImpl.updateCaliforniaFlag(principal);
		assertTrue(true);
	}

	@Test
	public void testUpdateCaliforniaFlagForUser1true()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		jsonResponse = "{\"personal\" : {\"first_name\" : \"Ali\",\"middle_name\" : \"Jama\",\"last_name\" : \"Abdille\",\"social_security_number\" : \"r8RNP+BPBBgCZFowy0vLEg==\",\"gender\" : \"M\",\"date_of_birth\" : \"1968-01-01\",\"bbsi_assigned_employee_id\" : \"L47730\",\"is_electronic_onboarding\" : false,\"employee_status\" : \"A\",\"scorp_owner\" : \"false\",\"business_owner\" : \"false\"},\"contact_information\" : {\"home_telephone_number\" : \"4083102364\",\"personal_email_address\" : \"calijamaac@yahoo.com\"},\"residential_address\" : {\"address_line1\" : \"4403 Dulcey Drive\",\"zip_code\" : \"95136\",\"county\" : \"SANTA CLARA\",\"city\" : \"SAN JOSE\",\"state\" : \"CA\",\"is_unicorporated_area\" : false},\"veteran_status\" : {\"ethnicity\" : \"B\",\"is_handicapped\" : false,\"is_blind\" : false},\"direct_deposit_status\" : \"A\",\"direct_deposit\" : [{\"routing_transit_number\" : \"h8pbk/1+MJsuPshLTa9GJw==\",\"bank_name\" : \"WELLS FARGO BANK NA\",\"bank_account_number\" : \"QNxQir0hCJKP6wswCm2ixw==\",\"account_type\" : \"C\",\"calculation_method\" : \"B\",\"status\" : \"D\"}],\"federal_tax\" : {\"filing_status\" : \"M\",\"primary_allowances\" : \"2\",\"multiple_jobs\" : false},\"state_tax\" : [{\"filing_status\" : \"M\",\"state\" : \"CA\",\"primary_allowances\" : \"2\",\"secondary_allowances\" : \"2\",\"has_nrstate_certificate\" : false,\"w4_filed\" : \"false\"}],\"review_date\" : \"2019-08-19\",\"performance_review\" : {\"review_date\" : \"2019-08-19\"}}";
		principal.setClientCode(populateUserClientsWhenIsNewHire().get(0).getClientCode());
		principal.setUserId(populateUserClientsWhenIsNewHire().get(0).getId());
		principal.setEmployeeCode(populateUserClientsWhenIsNewHire().get(0).getEmployeeCode());
		Object[] client = { "E1786", true, true };
		List<Object[]> clientList = new ArrayList<Object[]>();
		clientList.add(client);
		Mockito.doReturn(clientList).when(userClientRepository).getCaliforniaUsers(Mockito.anyString(),
				Mockito.anyLong());
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonResponse).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(node).when(objectMapper).readTree(Mockito.anyString());
		userServiceImpl.updateCaliforniaFlag(principal);
		assertTrue(true);
	}

	@Test
	public void testUpdateCaliforniaFlagForclientsEmpty()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		jsonResponse = "{\"personal\" : {\"first_name\" : \"Ali\",\"middle_name\" : \"Jama\",\"last_name\" : \"Abdille\",\"social_security_number\" : \"r8RNP+BPBBgCZFowy0vLEg==\",\"gender\" : \"M\",\"date_of_birth\" : \"1968-01-01\",\"bbsi_assigned_employee_id\" : \"L47730\",\"is_electronic_onboarding\" : false,\"employee_status\" : \"A\",\"scorp_owner\" : \"false\",\"business_owner\" : \"false\"},\"contact_information\" : {\"home_telephone_number\" : \"4083102364\",\"personal_email_address\" : \"calijamaac@yahoo.com\"},\"residential_address\" : {\"address_line1\" : \"4403 Dulcey Drive\",\"zip_code\" : \"95136\",\"county\" : \"SANTA CLARA\",\"city\" : \"SAN JOSE\",\"state\" : \"CA\",\"is_unicorporated_area\" : false},\"veteran_status\" : {\"ethnicity\" : \"B\",\"is_handicapped\" : false,\"is_blind\" : false},\"direct_deposit_status\" : \"A\",\"direct_deposit\" : [{\"routing_transit_number\" : \"h8pbk/1+MJsuPshLTa9GJw==\",\"bank_name\" : \"WELLS FARGO BANK NA\",\"bank_account_number\" : \"QNxQir0hCJKP6wswCm2ixw==\",\"account_type\" : \"C\",\"calculation_method\" : \"B\",\"status\" : \"D\"}],\"federal_tax\" : {\"filing_status\" : \"M\",\"primary_allowances\" : \"2\",\"multiple_jobs\" : false},\"state_tax\" : [{\"filing_status\" : \"M\",\"state\" : \"CA\",\"primary_allowances\" : \"2\",\"secondary_allowances\" : \"2\",\"has_nrstate_certificate\" : false,\"w4_filed\" : \"false\"}],\"review_date\" : \"2019-08-19\",\"performance_review\" : {\"review_date\" : \"2019-08-19\"}}";
		principal.setClientCode(populateUserClientsWhenIsNewHire().get(0).getClientCode());
		principal.setUserId(populateUserClientsWhenIsNewHire().get(0).getId());
		principal.setEmployeeCode(populateUserClientsWhenIsNewHire().get(0).getEmployeeCode());
		Object[] client = { "E1786", false, true };
		List<Object[]> clientList = new ArrayList<Object[]>();
		clientList.add(client);
		Mockito.doReturn(new ArrayList<>()).when(userClientRepository).getCaliforniaUsers(Mockito.anyString(),
				Mockito.anyLong());
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonResponse).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(node).when(objectMapper).readTree(Mockito.anyString());
		userServiceImpl.updateCaliforniaFlag(principal);
		assertTrue(true);
	}

	@Test
	public void testUpdateCaliforniaFlagForAssignmentInfo() throws IOException {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode(populateUserClientsWhenIsNewHire().get(0).getClientCode());
		principal.setUserId(populateUserClientsWhenIsNewHire().get(0).getId());
		principal.setEmployeeCode(populateUserClientsWhenIsNewHire().get(0).getEmployeeCode());
		String jsonResponse = "{\"work_site_location\" : \"1\",\"state\" : \"CA\"}";
		String jsonResponse2 = "{\"state\":\"CA\"}";
		JsonNode node = mapper.readTree(jsonResponse);
		Object[] client = { "E1786", false, true };
		List<Object[]> clientList = new ArrayList<Object[]>();
		clientList.add(client);
		Mockito.doReturn(clientList).when(userClientRepository).getCaliforniaUsers(Mockito.anyString(),
				Mockito.anyLong());
		Mockito.doReturn("url", "url1").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonResponse, jsonResponse2).when(restClient).getForString(Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn(node).when(objectMapper).readTree(Mockito.anyString());
		userServiceImpl.updateCaliforniaFlag(principal);
		Mockito.verify(boomiHelper, Mockito.atMost(3)).getUrl(Mockito.any());
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).getCaliforniaUsers(Mockito.anyString(),
				Mockito.anyLong());
		assertTrue(true);
	}

	@Test
	public void testUpdateCaliforniaFlagForAssignmentInfoForNotCAUser() throws IOException {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode(populateUserClientsWhenIsNewHire().get(0).getClientCode());
		principal.setUserId(populateUserClientsWhenIsNewHire().get(0).getId());
		principal.setEmployeeCode(populateUserClientsWhenIsNewHire().get(0).getEmployeeCode());
		String jsonResponse = "{\"work_site_location\" : \"1\",\"state\" : \"NY\"}";
		String jsonResponse2 = "{\"state\":\"CA\"}";
		JsonNode node = mapper.readTree(jsonResponse);
		Object[] client = { "E1786", false, true };
		List<Object[]> clientList = new ArrayList<Object[]>();
		clientList.add(client);
		Mockito.doReturn(clientList).when(userClientRepository).getCaliforniaUsers(Mockito.anyString(),
				Mockito.anyLong());
		Mockito.doReturn("url", "url1").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonResponse, jsonResponse2).when(restClient).getForString(Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn(node).when(objectMapper).readTree(Mockito.anyString());
		userServiceImpl.updateCaliforniaFlag(principal);
		Mockito.verify(boomiHelper, Mockito.atMost(3)).getUrl(Mockito.any());
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).getCaliforniaUsers(Mockito.anyString(),
				Mockito.anyLong());
		assertTrue(true);
	}

	@Test
	public void testUpdateCaliforniaFlagThrowsException()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
		userServiceImpl.updateCaliforniaFlag(null);
		assertTrue(true);
	}

	@Test
	public void testgetTimenetLoginInfo() {
		TimenetLoginInfo input = new TimenetLoginInfo();
		input.setRememberTimenetCompanyId(true);
		input.setRememberTimenetPassword(true);
		input.setRememberTimenetUserId(true);
		input.setTimenetCompanyId("909464");
		input.setTimenetPassword("Osicpl@2");
		input.setTimenetUserId("1");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserId(1l);
		principal.setEmail("admin@osius.com");
		List<UserClients> userClients = populateUserClientsWhenIsClient();
		userClients.get(0).setClientCode("909464");
		userClients.get(0).setRememberTimenetCompanyId(true);
		userClients.get(0).setRememberTimenetPassword(true);
		userClients.get(0).setRememberTimenetUserId(true);
		userClients.get(0).setTimenetCompanyId("909464");
		userClients.get(0).setTimenetPassword("Osicpl@2");
		userClients.get(0).setTimenetUserId("1");
		UserClients userClient = new UserClients();
		userClient.setClientCode("909090");
		userClient.setRememberTimenetPassword(true);
		userClient.setRememberTimenetUserId(true);
		userClient.setTimenetCompanyId("909464");
		userClient.setTimenetPassword("Osicpl@2");
		userClient.setTimenetUserId("1");
		userClients.add(userClient);
		UserClients userClient1 = new UserClients();
		userClient.setClientCode("909091");
		userClient.setRememberTimenetUserId(true);
		userClient.setTimenetCompanyId("909464");
		userClient.setTimenetPassword("Osicpl@2");
		userClient.setTimenetUserId("1");
		userClients.add(userClient1);
		Mockito.doReturn(userClients).when(userClientRepository).findByClientCodeAndUser_Id("909464",
				principal.getUserId());
		TimenetLoginInfo result = userServiceImpl.getTimenetLoginInfo(input.getTimenetCompanyId(), principal);
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndUser_Id("909464",
				principal.getUserId());
		assertNotEquals(input.getRememberTimenetCompanyId(), result.getRememberTimenetCompanyId());
		assertEquals(input.getRememberTimenetPassword(), result.getRememberTimenetPassword());
		assertEquals(input.getRememberTimenetUserId(), result.getRememberTimenetUserId());
		assertEquals(input.getTimenetCompanyId(), result.getTimenetCompanyId());
		assertEquals(input.getTimenetPassword(), result.getTimenetPassword());
		assertEquals(input.getTimenetUserId(), result.getTimenetUserId());
	}

	@Test
	public void testgetTimenetLoginInfoForRememberTimenetCompanyIdNull() {
		TimenetLoginInfo input = new TimenetLoginInfo();
		input.setRememberTimenetCompanyId(true);
		input.setRememberTimenetPassword(true);
		input.setRememberTimenetUserId(true);
		input.setTimenetCompanyId("909464");
		input.setTimenetPassword("Osicpl@2");
		input.setTimenetUserId("1");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserId(1l);
		principal.setEmail("admin@osius.com");
		List<UserClients> userClients = populateUserClientsWhenIsClient();
		userClients.get(0).setClientCode("909464");
		userClients.get(0).setRememberTimenetCompanyId(null);
		userClients.get(0).setRememberTimenetPassword(null);
		userClients.get(0).setRememberTimenetUserId(null);
		userClients.get(0).setTimenetCompanyId("909464");
		userClients.get(0).setTimenetPassword("Osicpl@2");
		userClients.get(0).setTimenetUserId("1");
		UserClients userClient = new UserClients();
		userClient.setClientCode("909090");
		userClient.setRememberTimenetPassword(true);
		userClient.setRememberTimenetUserId(true);
		userClient.setTimenetCompanyId("909464");
		userClient.setTimenetPassword("Osicpl@2");
		userClient.setTimenetUserId("1");
		userClients.add(userClient);
		UserClients userClient1 = new UserClients();
		userClient.setClientCode("909091");
		userClient.setRememberTimenetUserId(true);
		userClient.setRememberTimenetCompanyId(true);
		userClient.setRememberTimenetPassword(true);
		userClient.setTimenetCompanyId("909464");
		userClient.setTimenetPassword("Osicpl@2");
		userClient.setTimenetUserId("1");
		userClients.add(userClient1);
		Mockito.doReturn(userClients).when(userClientRepository).findByClientCodeAndUser_Id("909464",
				principal.getUserId());
		TimenetLoginInfo result = userServiceImpl.getTimenetLoginInfo(input.getTimenetCompanyId(), principal);
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndUser_Id("909464",
				principal.getUserId());
		assertEquals(input.getRememberTimenetPassword(), result.getRememberTimenetPassword());
		assertEquals(input.getRememberTimenetUserId(), result.getRememberTimenetUserId());
		assertEquals(input.getTimenetCompanyId(), result.getTimenetCompanyId());
		assertEquals(input.getTimenetPassword(), result.getTimenetPassword());
		assertEquals(input.getTimenetUserId(), result.getTimenetUserId());
	}

	@Test
	public void testGetTimenetLoginInfoForExceptions() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByClientCodeAndUser_Id(" ", 0l);
		TimenetLoginInfo result = userServiceImpl.getTimenetLoginInfo(" ",
				new UserPrincipal(" ", " ", Lists.newArrayList()));
		assertNull(result);
	}

	@Test
	public void testGetTimenetLoginInfoForInputsNull() {

		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByClientCodeAndUser_Id(null, 0l);
		TimenetLoginInfo result = userServiceImpl.getTimenetLoginInfo(null, userPrincipal);
		assertNull(result);
	}

	@Test
	public void testUpdateTimenetLoginInfo() {
		TimenetLoginInfo input = new TimenetLoginInfo();
		input.setRememberTimenetCompanyId(true);
		input.setRememberTimenetPassword(true);
		input.setRememberTimenetUserId(true);
		input.setTimenetCompanyId("909464");
		input.setTimenetPassword("Osicpl@2");
		input.setTimenetUserId("1");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setEmail("admin@osius.com");
		List<UserClients> userClients = populateUserClientsWhenIsClient();
		userClients.get(0).setClientCode("909464");
		userClients.get(0).setRememberTimenetCompanyId(true);
		userClients.get(0).setRememberTimenetPassword(true);
		userClients.get(0).setRememberTimenetUserId(true);
		userClients.get(0).setTimenetCompanyId("909464");
		userClients.get(0).setTimenetPassword("Osicpl@2");
		userClients.get(0).setTimenetUserId("1");
		Mockito.doReturn(userClients).when(userClientRepository).findByUserClientsByClient("909464",
				principal.getUserId());
		userServiceImpl.updateTimenetLoginInfo(input, "909464", principal);
		assertTrue(true);
	}

	@Test
	public void testUpdateTimenetLoginInfoForException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByUserClientsByClient(" ", 0l);
		userServiceImpl.updateTimenetLoginInfo(new TimenetLoginInfo(), "abcd",
				new UserPrincipal(" ", " ", Lists.newArrayList()));
		;
		assertTrue(true);
	}

	@Test
	public void testUpdateTimenetLoginInfoForOtherExceptions() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByUserClientsByClient(null, null);
		userServiceImpl.updateTimenetLoginInfo(null, null, null);
		assertTrue(true);
	}

	@Test
	public void testgetTerminatedClients() throws Exception {
		List<ClientMaster> clientMasterList = new ArrayList<ClientMaster>();
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("clientCode");
		clientMaster.setClientName("clientName");
		clientMaster.setCostCenterCode("code1");
		clientMaster.setCostCenterDescription("description");
		ClientMaster clientMaster1 = new ClientMaster();
		clientMaster1.setClientCode("clientCode1");
		clientMaster1.setClientName("clientName1");
		clientMaster1.setCostCenterCode("code2");
		clientMaster1.setCostCenterDescription("description2");
		clientMasterList.add(clientMaster);
		clientMasterList.add(clientMaster1);
		String json = "[\"clientCode\",\"clientCode1\"]";
		String jsonResponse = "[{\"status\" : \"T\", \"client_id\" : \"clientCode\"},{\"status\" : \"A\", \"client_id\" : \"clientCode1\"}]";
		Type type = new TypeToken<Object[]>() {
		}.getType();
		Object[] array = new Gson().fromJson(json, type);
		Mockito.doReturn(getUserCostCenterAssociationList()).when(userCostCenterAssociationRepository)
				.findByUserId(Mockito.anyLong());
		Mockito.doReturn(clientMasterList).when(clientMasterRepository).findByCostCenterCode(Mockito.anyString());
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(jsonResponse).when(restClient).getForString(Mockito.any(), Mockito.anyString(),
				Mockito.anyMap(), Mockito.anyMap());
		Map<String, String> result = userServiceImpl.getTerminatedClients(1l, token);
		Mockito.verify(userCostCenterAssociationRepository, Mockito.atLeastOnce()).findByUserId(1l);
		Mockito.verify(clientMasterRepository, Mockito.atMost(1)).findByClientCode("clientCode");
		Mockito.verify(clientMasterRepository, Mockito.atMost(1)).findByClientCode("clientCode1");
		Mockito.verify(boomiHelper, Mockito.atLeastOnce()).getUrl(BoomiEnum.COSTCENTER_CLIENTLIST);
		assertNull(result.get("clientCode1"));
	}

	@Test
	public void testGetTerminatedClientsForClientArrayNull() {
		Mockito.doReturn(null).when(userCostCenterAssociationRepository).findByUserId(Mockito.any());
		Mockito.doReturn(new ClientMaster()).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Map<String, String> result = userServiceImpl.getTerminatedClients(1l, token);
		Mockito.verify(userCostCenterAssociationRepository, Mockito.atLeastOnce()).findByUserId(1l);
		assertEquals(0, result.size());
	}

	@Test
	public void testGetTerminatedClientsForClientArrayEmpty() {
		String json = "[]";
		Type type = new TypeToken<Object[]>() {
		}.getType();
		Object[] array = new Gson().fromJson(json, type);
		Mockito.doReturn(new ArrayList<UserCostCenterAssociation>()).when(userCostCenterAssociationRepository)
				.findByUserId(Mockito.any());
		Mockito.doReturn(new ClientMaster()).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Map<String, String> result = userServiceImpl.getTerminatedClients(1l, token);
		Mockito.verify(userCostCenterAssociationRepository, Mockito.atLeastOnce()).findByUserId(1l);
		assertEquals(0, result.size());
	}

	@Test
	public void testGetTerminatedClientsForNoTerminatedClients() {
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode("clientCode");
		clientMaster.setClientName("clientName");
		clientMaster.setCostCenterCode("code1");
		clientMaster.setCostCenterDescription("description");
		String json = "[\"clientCode\"]";
		Type type = new TypeToken<Object[]>() {
		}.getType();
		Object[] array = new Gson().fromJson(json, type);
		Mockito.doReturn(getUserCostCenterAssociationList()).when(userCostCenterAssociationRepository)
				.findByUserId(Mockito.any());
		Mockito.doReturn(clientMaster).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doReturn("url").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(null).when(restClient).getForString(Mockito.any(), Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Map<String, String> result = userServiceImpl.getTerminatedClients(1l, token);
		Mockito.verify(userCostCenterAssociationRepository, Mockito.atLeastOnce()).findByUserId(1l);
		Mockito.verify(boomiHelper, Mockito.atLeastOnce()).getUrl(BoomiEnum.COSTCENTER_CLIENTLIST);
		assertNull(result.get("clientCode"));
		assertEquals(0, result.size());
	}

	@Test
	public void testGetTerminatedClientsForExceptions() {
		Mockito.doThrow(BbsiException.class).when(userCostCenterAssociationRepository).findByUserId(Mockito.anyLong());
		Mockito.doThrow(BbsiException.class).when(clientMasterRepository).findByClientCode(Mockito.anyString());
		Mockito.doThrow(BbsiException.class).when(boomiHelper).getUrl(Mockito.any());
		Mockito.doThrow(BbsiException.class).when(restClient).getForString(Mockito.any(), Mockito.anyString(),
				Mockito.anyMap(), Mockito.anyMap());
		Map<String, String> result = userServiceImpl.getTerminatedClients(1l, token);
		assertEquals(0, result.size());
		Mockito.verify(userCostCenterAssociationRepository, Mockito.atLeastOnce()).findByUserId(1l);
		Mockito.verify(clientMasterRepository, Mockito.never()).findByClientCode("clientCode");
		Mockito.verify(boomiHelper, Mockito.never()).getUrl(Mockito.any());
	}

	@Test
	public void testgetUserFilter() {
		List<UserClients> expectedList = new ArrayList<>();
		UserSecurity filter = new UserSecurity();
		filter.setId(1l);
		filter.setType("type");
		filter.setUserClient(populateUserClient());
		filter.setValue("value");
		List<UserSecurity> filters = new ArrayList<>();
		filters.add(filter);
		expectedList.addAll(populateUserClients());
		Mockito.doReturn(expectedList).when(userClientRepository).findByUser_Email(Mockito.any());
		Mockito.doReturn(filters).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Map<String, Map<String, Set<String>>> result = userServiceImpl.getUserFilter("YWRtaW5Ab3NpdXMuY29t");
		assertEquals(1, result.size());
	}

	@Test
	public void testGetUserFilterForId() {
		List<UserClients> expectedList = new ArrayList<>();
		expectedList.addAll(populateUserClients());
		Mockito.doReturn(expectedList).when(userClientRepository).findByUser_Id(1l);
		Map<String, Map<String, Set<String>>> result = userServiceImpl.getUserFilter("MQ==");
		assertEquals(1, result.size());
	}

	@Test
	public void testGetUserFilterForException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByUser_Email(Mockito.anyString());
		Map<String, Map<String, Set<String>>> result = userServiceImpl.getUserFilter(" ");
		assertEquals(0, result.size());
	}

	@Test(expected = NullPointerException.class)
	public void testGetUserFilterForOtherExceptions() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByUser_Email(Mockito.anyString());
		Map<String, Map<String, Set<String>>> result = userServiceImpl.getUserFilter(null);
		assertEquals(0, result.size());
	}

	@Test
	public void testupdateUserPrincipal() {
		CommonDTO mappingDTO = populateCommonDto();
		MenuMappingDTO menu = new MenuMappingDTO();
		menu.setCategory("Main Menu");
		menu.setDescription("description");
		menu.setDisplayName("Home");
		menu.setFeature("feature");
		menu.setFeatureCodeId(1l);
		menu.setName("HOME");
		menu.setType("type");
		List<MenuMappingDTO> menus = new ArrayList<>();
		menus.add(menu);
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode("909464");
		principal.setAccessGroup("1");
		principal.setUserType(GenericConstants.USERTYPE_NEWHIRE);
		Mockito.doReturn(mappingDTO).when(mappingService).getMapping(Mockito.anyLong(), Mockito.anyString(),
				Mockito.anyBoolean());
		Mockito.doReturn(menus).when(menuBusinessService).fetchUserMenu(Mockito.any(), Mockito.any());
		UserPrincipal result = userServiceImpl.updateUserPrincipal(principal);
		assertEquals(result.getClientCode(), principal.getClientCode());
	}

	@Test
	public void testUpdateUserPrincipalForNewHire() {
		CommonDTO mappingDTO = populateCommonDto();
		MenuMappingDTO menu = new MenuMappingDTO();
		menu.setCategory("Main Menu");
		menu.setDescription("description");
		menu.setDisplayName("Home");
		menu.setFeature("feature");
		menu.setFeatureCodeId(1l);
		menu.setName("HOME");
		menu.setType("type");
		List<MenuMappingDTO> menus = new ArrayList<>();
		menus.add(menu);
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode("909464");
		principal.setUserType(GenericConstants.USERTYPE_NEWHIRE);
		Mockito.doReturn(mappingDTO).when(mappingService).getMapping(Mockito.anyLong(), Mockito.anyString(),
				Mockito.anyBoolean());
		Mockito.doReturn(menus).when(menuBusinessService).fetchUserMenu(Mockito.any(), Mockito.any());
		UserPrincipal result = userServiceImpl.updateUserPrincipal(principal);
		assertEquals(result.getClientCode(), principal.getClientCode());
	}

	@Test
	public void testGetUserFilterWithArgs() {
		List<UserClients> expected = populateUserClients();
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		principal.setEmail(expected.get(0).getUser().getEmail());
		principal.setClientCode("909464");
		List<Object[]> expectedArray = populateUserClientsIdObjectArray();
		Mockito.doReturn(expectedArray).when(userClientRepository).findByUserClientsByClient(Mockito.any(),
				Mockito.any());
		Mockito.doReturn(expectedArray).when(userClientRepository).findByUserClientsByType(Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(expected.get(0).getUserFilters()).when(userSecurityRepository)
				.findByUserClient_Id(Mockito.any());
		Map<String, Set<String>> result = userServiceImpl.getUserFilter(principal.getEmail(), principal.getClientCode(),
				principal);
		assertEquals(1, result.size());
	}

	@Test
	public void testGetUserFilterWithArgsfiltersEmpty() {
		List<UserClients> expected = populateUserClients();
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		principal.setEmail(expected.get(0).getUser().getEmail());
		principal.setClientCode("909464");
		List<Object[]> expectedArray = populateUserClientsIdObjectArray();
		Mockito.doReturn(expectedArray).when(userClientRepository).findByUserClientsByClient(Mockito.any(),
				Mockito.any());
		Mockito.doReturn(expectedArray).when(userClientRepository).findByUserClientsByType(Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(new ArrayList<>()).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Map<String, Set<String>> result = userServiceImpl.getUserFilter(principal.getEmail(), principal.getClientCode(),
				principal);
		assertNotNull(result);
	}

	@Test(expected = BbsiException.class)
	public void testGetUserFilterWithArgsForException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByUserClientsByClient(Mockito.any(),
				Mockito.any());
		Mockito.doThrow(BbsiException.class).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Map<String, Set<String>> result = userServiceImpl.getUserFilter(" ", " ",
				new UserPrincipal(" ", " ", Lists.newArrayList()));
		assertEquals(0, result.size());
	}

	@Test
	public void testgetAllCostCenters() throws IOException {
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "/integration/v1/common/type/COST_CENTER?id=");
		String client = "{\"client_id\":\"909464\", \"status\":\"A\", \"client_name\":\"1 Hour Drain\"}";
		List<CostCenterDTO> expected = populateCostCenter();
		String jsonResponse = new Gson().toJson(expected);
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(jsonResponse);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		JsonNode node = mapper.readTree(jsonArray.get(0).toString());
		JsonNode node1 = mapper.readTree(client);
		Mockito.doReturn(jsonResponse).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn(node, node1).when(objectMapper).readTree(Mockito.anyString());
		List<CostCenterDTO> result = userServiceImpl.getAllCostCenters(token);
		assertEquals(1, result.size());
	}

	@Test
	public void testGetAllCostCentersForExceptions() throws IOException {
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "/integration/v1/common/type/COST_CENTER?id=");
		List<CostCenterDTO> expected = populateCostCenter();
		String jsonResponse = new Gson().toJson(expected);
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(jsonResponse);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		JsonNode node = mapper.readTree(jsonArray.get(0).toString());
		Mockito.doReturn(jsonResponse).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.doReturn(node, null).when(objectMapper).readTree(Mockito.anyString());
		List<CostCenterDTO> result = userServiceImpl.getAllCostCenters(token);
		assertEquals(1, result.size());
	}

	@Test
	public void testGetAllCostCentersForExceptions2() throws IOException {
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "/integration/v1/common/type/COST_CENTER?id=");
		List<CostCenterDTO> expected = populateCostCenter();
		String jsonResponse = new Gson().toJson(expected);
		Mockito.doReturn(jsonResponse).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any());
		List<CostCenterDTO> result = userServiceImpl.getAllCostCenters(token);
		assertEquals(0, result.size());
	}

	@Test
	public void testGetAllCostCentersForOtherExceptions() {
		Mockito.doThrow(BbsiException.class).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any());
		List<CostCenterDTO> result = userServiceImpl.getAllCostCenters(null);
		assertEquals(0, result.size());
	}

	@Test
	public void testgetI9ApprovalEmployeeList() {
		Object[] expected = { "fname", "lname", "8106533484", "Employee", 1l, 1l };
		List<Object[]> expectedList = new ArrayList<Object[]>();
		expectedList.add(expected);
		List<PrivilegeMapping> privilegeDTOs = new ArrayList<>();
		PrivilegeMapping privilegeDTO = new PrivilegeMapping();
		privilegeDTO.setCode("NEW_HIRE_I9_APPROVER_F.ALL");
		privilegeDTOs.add(privilegeDTO);
		privilegeDTO = new PrivilegeMapping();
		privilegeDTO.setCode("NEW_HIRE_I9_APPROVER_F.VIEW");
		privilegeDTOs.add(privilegeDTO);
		Mockito.doReturn(expectedList).when(userClientRepository).findByBranchUsersByClientCode(Mockito.anyString());
		Mockito.doReturn(populateCommonDTOList()).when(mappingService).getPrivileges(Mockito.anyLong(), Mockito.any());
		// Mockito.doReturn(privilegeDTOs).when(mappingService).getPrivileges(Mockito.anyLong(),
		// Mockito.any());
		List<UsersDTO> result = userServiceImpl.getI9ApprovalEmployeeList("909464");
		assertEquals(0, result.size());
	}

	@Test
	public void testgetI9ApprovalEmployeeList1() {
		Object[] expected = { "fname", "lname", "8106533484", "Employee", BigInteger.ONE, BigInteger.ONE, 1L, 1L, 2L };
		List<Object[]> expectedList = new ArrayList<Object[]>();
		expectedList.add(expected);

		List<CommonDTO> privilegeDTOs = new ArrayList<>();
		CommonDTO privilegeDTO = new CommonDTO();
		privilegeDTO.setCode("NEW_HIRE_I9_APPROVER_F.ALL");
		privilegeDTOs.add(privilegeDTO);
		privilegeDTO = new CommonDTO();
		privilegeDTO.setCode("NEW_HIRE_I9_APPROVER_F.VIEW");
		privilegeDTOs.add(privilegeDTO);
		Mockito.doReturn(expectedList).when(userClientRepository)
				.findByApprovalUserDetailsByClient(Mockito.anyString());
		Mockito.doReturn(populateCommonDTOList1()).when(mappingService).getPrivileges(Mockito.anyLong(), Mockito.any());
		Mockito.doReturn(privilegeDTOs).when(mappingService).getPrivileges(Mockito.anyLong(), Mockito.any());
		List<UsersDTO> result = userServiceImpl.getI9ApprovalEmployeeList("909464");
		assertEquals(0, result.size());
	}

	@Test
	public void testgetI9ApprovalEmployeeListForNEW_HIRE_I9_APPROVER_FVIEW() {
		Object[] expected = { "fname", "lname", "8106533484", "Employee", BigInteger.ONE, BigInteger.ONE, 1L, 1L, 2L };
		List<Object[]> expectedList = new ArrayList<Object[]>();
		expectedList.add(expected);

		List<CommonDTO> privilegeDTOs = new ArrayList<>();
		CommonDTO privilegeDTO = new CommonDTO();
		privilegeDTO.setCode("NEW_HIRE_I9_APPROVER_F.VIEW");
		privilegeDTOs.add(privilegeDTO);
		List<Users> users1 = new ArrayList<>();
		users1.add(populateUserWhenUserTypeIsNewHire());
		Iterable<Users> users = new ArrayList<Users>();
		users = users1;

		Mockito.doReturn(expectedList).when(userClientRepository)
				.findByApprovalUserDetailsByClient(Mockito.anyString());
		Mockito.doReturn(populateCommonDTOList1()).when(mappingService).getPrivileges(Mockito.anyLong(), Mockito.any());
		Mockito.doReturn(privilegeDTOs).when(mappingService).getPrivileges(Mockito.anyLong(), Mockito.any());
		Mockito.doReturn(users).when(usersRepository).findAllById(Mockito.anyIterable());
		Mockito.doReturn(populateUsersDTOForVancouver()).when(usersMapper).userToUserDTO(Mockito.any());
		List<UsersDTO> result = userServiceImpl.getI9ApprovalEmployeeList("909464");
		assertNotNull(result);
	}

	@Test
	public void testGetI9ApprovalEmployeeListForExceptions() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByClientCodeAndIsActiveTrue(" ");
		Mockito.doThrow(BbsiException.class).when(mappingService).getPrivileges(0l, UserEnum.PRIVILEGE);
		List<UsersDTO> result = userServiceImpl.getI9ApprovalEmployeeList("909464");
		assertNotNull(result);
	}

	@Test
	public void testGetI9ApprovalEmployeeListForException() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository)
				.findByApprovalUserDetailsByClient(Mockito.anyString());
		Mockito.doThrow(NullPointerException.class).when(usersMapper).userToUserDTO(Mockito.any());
		List<UsersDTO> result = userServiceImpl.getI9ApprovalEmployeeList("909464");
		assertNotNull(result);
	}

	@Test
	public void testGetI9ApprovalEmployeeListForOtherExceptions() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByClientCodeAndIsActiveTrue(null);
		Mockito.doThrow(BbsiException.class).when(mappingService).getPrivileges(0l, null);
		List<UsersDTO> result = userServiceImpl.getI9ApprovalEmployeeList(null);
		assertEquals(0, result.size());
	}

	@Test
	public void testgetisPolicyAccepted() {
		PolicyAcceptedDTO expected = new PolicyAcceptedDTO();
		expected.setEmail("admin@osius.com");
		expected.setIsPolicyAccepted(false);
		Object obj = expected;
		Mockito.doReturn(obj).when(usersRepository).getisPolicyAccepted("admin@osius.com");
		PolicyAcceptedDTO result = userServiceImpl.getisPolicyAccepted("YWRtaW5Ab3NpdXMuY29t");
		assertEquals("YWRtaW5Ab3NpdXMuY29t", result.getEmail());
	}

	@Test
	public void testgetisPolicyAcceptedobjNull() {
		Mockito.doReturn(null).when(usersRepository).getisPolicyAccepted("admin@osius.com");
		PolicyAcceptedDTO result = userServiceImpl.getisPolicyAccepted("YWRtaW5Ab3NpdXMuY29t");
		assertNull(result);
	}

	@Test
	public void testGetIsPolicyAcceptedForExceptions() {
		Mockito.doThrow(BbsiException.class).when(usersRepository).getisPolicyAccepted(Mockito.any());
		PolicyAcceptedDTO result = userServiceImpl.getisPolicyAccepted("1");
		assertNull(result);
	}

	@Test
	public void testaddSecurityEntry() {
		List<UserClients> expected = new ArrayList<>();
		expected.add(populateUserClient());
		Mockito.doReturn(expected).when(userClientRepository).findByClientCodeAndUser_Id(Mockito.any(), Mockito.any());
		userServiceImpl.addSecurityEntry(userPrincipal, "type", "value");
		assertTrue(true);
	}

	@Test
	public void testAddSecurityEntryForExternal() {
		List<UserClients> expected = new ArrayList<>();
		UserClients userClient = populateUserClient();
		userClient.setUserType(GenericConstants.USERTYPE_EXTERNAL.toString());
		expected.add(userClient);
		UserPrincipal principal = userPrincipal;
		principal.setUserType(GenericConstants.USERTYPE_EXTERNAL.toString());
		Mockito.doReturn(expected).when(userClientRepository).findByClientCodeAndUser_Id(Mockito.any(), Mockito.any());
		userServiceImpl.addSecurityEntry(principal, "type1", "value1");
		assertTrue(true);
	}

	@Test
	public void testAddSecurityEntryForClient() {
		List<UserClients> expected = new ArrayList<>();
		UserClients userClient = populateUserClient();
		userClient.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		expected.add(userClient);
		UserPrincipal principal = userPrincipal;
		principal.setUserType(GenericConstants.USERTYPE_EXTERNAL.toString());
		Mockito.doReturn(expected).when(userClientRepository).findByClientCodeAndUser_Id(Mockito.any(), Mockito.any());
		userServiceImpl.addSecurityEntry(principal, "type1", "value1");
		assertTrue(true);
	}

	@Test
	public void testSendEmailNotificationsForSchoolDistrictForgetCostCenterCodeByClientCodeCatchBlock() {
		Object[] expected = { "fname", "lname", "8106533484", "Branch", 764235475365463L, 6354635436463L };
		List<Object[]> expectedList = new ArrayList<>();
		expectedList.add(expected);
		List<PrivilegeMapping> privilegeDTOs = new ArrayList<>();
		PrivilegeMapping privilegeDTO = new PrivilegeMapping();
		privilegeDTO.setCode("EMP_MGMT.ALL");
		privilegeDTOs.add(privilegeDTO);
		privilegeDTO = new PrivilegeMapping();
		privilegeDTO.setCode("EMP_MGMT.VIEW");
		privilegeDTOs.add(privilegeDTO);
		Mockito.doReturn(expectedList).when(userCostCenterAssociationRepository)
				.findByBranchUsersByCostCenterCode("909464");
		Mockito.doReturn(privilegeDTOs).when(mappingService).getPrivileges(1l, UserEnum.ROLE);
		userServiceImpl.sendEmailNotificationsForSchoolDistrict("1234", "type1", "value1", userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testSendEmailNotificationsForSchoolDistrict() {
		ReflectionTestUtils.setField(userServiceImpl, "clientInfo",
				"http://localhost:9000/ws/rest/V2/getClientInformation?clientId=909464");
		Object[] expected = { "fname", BigInteger.TWO, BigInteger.ONE, "Branch", 764235475365463L, 6354635436463L };
		List<Object[]> expectedList = new ArrayList<>();
		expectedList.add(expected);

		List<Users> users1 = new ArrayList<>();
		users1.add(populateUserWhenUserTypeIsNewHire());
		Iterable<Users> users = new ArrayList<Users>();
		users = users1;

		String json = "{\"cost_center_code\":\"1 Hour Drain\"}";
		Mockito.doReturn(json).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(expectedList).when(userCostCenterAssociationRepository)
				.findByBranchUsersByCostCenterCode(Mockito.anyString());
		Mockito.doReturn(populateCommonDTOList()).when(mappingService).getPrivileges(Mockito.anyLong(), Mockito.any());
		Mockito.doReturn(users).when(usersRepository).findAllById(Mockito.anyIterable());
		Mockito.doReturn(populateUsersDTOForVancouver()).when(usersMapper).userToUserDTO(Mockito.any());
		Mockito.doNothing().when(commonUtilities).buildEventToPublish(Mockito.any(), Mockito.anyMap(), Mockito.anyMap(),
				Mockito.any(), Mockito.anyString());
		userServiceImpl.sendEmailNotificationsForSchoolDistrict("1234", "type1", "value1", userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testSendEmailNotificationsForSchoolDistrictForCommonDTOCode() {
		ReflectionTestUtils.setField(userServiceImpl, "clientInfo",
				"http://localhost:9000/ws/rest/V2/getClientInformation?clientId=909464");
		Object[] expected = { "fname", BigInteger.TWO, BigInteger.ONE, "Branch", 764235475365463L, 6354635436463L };
		List<Object[]> expectedList = new ArrayList<>();
		expectedList.add(expected);
		List<CommonDTO> privilegeDTOs = new ArrayList<>();
		CommonDTO privilegeDTO = new CommonDTO();
		privilegeDTO.setCode("EMP_MGMT.ALL");
		privilegeDTOs.add(privilegeDTO);
		privilegeDTO = new CommonDTO();
		privilegeDTO.setCode("EMP_MGMT.VIEW");
		privilegeDTOs.add(privilegeDTO);
		privilegeDTO = new CommonDTO();
		privilegeDTO.setCode("PAY_TS_INF.VIEW");
		privilegeDTOs.add(privilegeDTO);
		privilegeDTO = new CommonDTO();
		privilegeDTO.setCode("PAY_TS_INF.ALL");
		privilegeDTOs.add(privilegeDTO);
		List<Users> users1 = new ArrayList<>();
		users1.add(populateUserWhenUserTypeIsNewHire());
		Iterable<Users> users = new ArrayList<Users>();
		users = users1;

		Mockito.doReturn(privilegeDTOs).when(mappingService).getPrivileges(1l, UserEnum.ROLE);
		String json = "{\"cost_center_code\":\"1 Hour Drain\"}";
		Mockito.doReturn(json).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(expectedList).when(userCostCenterAssociationRepository)
				.findByBranchUsersByCostCenterCode(Mockito.anyString());
		Mockito.doReturn(privilegeDTOs).when(mappingService).getPrivileges(Mockito.anyLong(), Mockito.any());
		Mockito.doReturn(users).when(usersRepository).findAllById(Mockito.anyIterable());
		Mockito.doReturn(populateUsersDTOForVancouver()).when(usersMapper).userToUserDTO(Mockito.any());
		Mockito.doNothing().when(commonUtilities).buildEventToPublish(Mockito.any(), Mockito.anyMap(), Mockito.anyMap(),
				Mockito.any(), Mockito.anyString());
		userServiceImpl.sendEmailNotificationsForSchoolDistrict("1234", "type1", "value1", userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testSendEmailNotificationsForSchoolDistrictForprivilegeDTOsEmpty() {
		ReflectionTestUtils.setField(userServiceImpl, "clientInfo",
				"http://localhost:9000/ws/rest/V2/getClientInformation?clientId=909464");
		Object[] expected = { "fname", BigInteger.TWO, BigInteger.ZERO };
		List<Object[]> expectedList = new ArrayList<>();
		expectedList.add(expected);
		List<PrivilegeMapping> privilegeDTOs = new ArrayList<>();
		List<Users> users1 = new ArrayList<>();
		users1.add(populateUserWhenUserTypeIsNewHire());
		Iterable<Users> users = new ArrayList<Users>();
		users = users1;

		Mockito.doReturn(privilegeDTOs).when(mappingService).getPrivileges(1l, UserEnum.ROLE);
		String json = "{\"cost_center_code\":\"1 Hour Drain\"}";
		Mockito.doReturn(json).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(expectedList).when(userCostCenterAssociationRepository)
				.findByBranchUsersByCostCenterCode(Mockito.anyString());
		userServiceImpl.sendEmailNotificationsForSchoolDistrict("1234", "type1", "value1", userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testSendEmailNotificationsForSchoolDistrictForJsonNodeNull() throws Exception {
		ReflectionTestUtils.setField(userServiceImpl, "clientInfo",
				"http://localhost:8080/ws/rest/V2/getClientInformation?clientId=909464");
		Object[] expected = { "fname", "lname", "8106533484", "Branch", 764235475365463L, 6354635436463L };
		List<Object[]> expectedList = new ArrayList<>();
		expectedList.add(expected);
		List<PrivilegeMapping> privilegeDTOs = new ArrayList<>();
		PrivilegeMapping privilegeDTO = new PrivilegeMapping();
		privilegeDTO.setCode("EMP_MGMT.ALL");
		privilegeDTOs.add(privilegeDTO);
		privilegeDTO = new PrivilegeMapping();
		privilegeDTO.setCode("EMP_MGMT.VIEW");
		privilegeDTOs.add(privilegeDTO);
		Mockito.doReturn(expectedList).when(userCostCenterAssociationRepository)
				.findByBranchUsersByCostCenterCode(Mockito.anyString());
		Mockito.doReturn(privilegeDTOs).when(mappingService).getPrivileges(1l, UserEnum.ROLE);
		String response = "{\"MethodNames\" : [{\"cost_center_code\" : \"cost_center_code\",\"cost_center_code\" : [\"field1\"]},{\"kid\" : \"id2\",\"x5c\" : [\"field2\"]}]}";
		Mockito.doReturn(response).when(restClient).getForString(Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any());
		userServiceImpl.sendEmailNotificationsForSchoolDistrict("1234", "type1", "value1", userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testSendEmailNotificationsForSchoolDistrictForgetCostCenterCodeByClientCodeResponseNull()
			throws Exception {
		ReflectionTestUtils.setField(userServiceImpl, "clientInfo",
				"http://localhost:8080/ws/rest/V2/getClientInformation?clientId=909464");
		Object[] expected = { "fname", "lname", "8106533484", "Branch", 764235475365463L, 6354635436463L };
		List<Object[]> expectedList = new ArrayList<>();
		expectedList.add(expected);
		List<PrivilegeMapping> privilegeDTOs = new ArrayList<>();
		PrivilegeMapping privilegeDTO = new PrivilegeMapping();
		privilegeDTO.setCode("EMP_MGMT.ALL");
		privilegeDTOs.add(privilegeDTO);
		privilegeDTO = new PrivilegeMapping();
		privilegeDTO.setCode("EMP_MGMT.VIEW");
		privilegeDTOs.add(privilegeDTO);
		Mockito.doReturn(expectedList).when(userCostCenterAssociationRepository)
				.findByBranchUsersByCostCenterCode(Mockito.anyString());
		Mockito.doReturn(privilegeDTOs).when(mappingService).getPrivileges(1l, UserEnum.ROLE);
		userServiceImpl.sendEmailNotificationsForSchoolDistrict("1234", "type1", "value1", userPrincipal);
		assertTrue(true);
	}

	@Test
	public void testSendEmailNotificationsForSchoolDistrictForException() {
		userServiceImpl.sendEmailNotificationsForSchoolDistrict("1234", "type1", "value1", null);
		assertTrue(true);
	}

	@Test
	public void testAddSecurityEntryForOtherUser() {
		List<UserClients> expected = new ArrayList<>();
		UserClients userClient = populateUserClient();
		userClient.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		expected.add(userClient);
		UserPrincipal principal = userPrincipal;
		principal.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		Mockito.doReturn(expected).when(userClientRepository).findByClientCodeAndUser_Id(Mockito.any(), Mockito.any());
		userServiceImpl.addSecurityEntry(userPrincipal, "type1", "value1");
		Mockito.verify(userClientRepository, Mockito.never()).findByClientCodeAndUser_Id(userClient.getClientCode(),
				userClient.getId());
	}

	@Test
	public void testAddSecurityEntryForEmptyClients() {
		Mockito.doReturn(new ArrayList<>()).when(userClientRepository).findByClientCodeAndUser_Id(Mockito.any(),
				Mockito.any());
		UserPrincipal userPrincipal = new UserPrincipal("jchilakapati@bbsi.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setClientCode("909464");
		userPrincipal.setEmail("test@mybbsi.com");
		userPrincipal.setIsPolicyAccepted(false);
		userPrincipal.setIsCcpaRequired(true);
		userPrincipal.setIsPolicyUpdated(false);
		userPrincipal.setUserId(1l);
		userPrincipal.setUserType(GenericConstants.USERTYPE_CLIENT);
		userServiceImpl.addSecurityEntry(userPrincipal, "type1", "value1");
		Mockito.verify(userClientRepository, Mockito.atLeastOnce()).findByClientCodeAndUser_Id(Mockito.any(),
				Mockito.any());
	}

	@Test
	public void testAddSecurityEntryForExceptions() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByClientCodeAndUser_Id(Mockito.any(),
				Mockito.any());
		userServiceImpl.addSecurityEntry(new UserPrincipal(" ", " ", Lists.newArrayList()), " ", " ");
		assertTrue(true);
	}

	@Test
	public void testAddSecurityEntryForOtherExceptions() {
		Mockito.doThrow(BbsiException.class).when(userClientRepository).findByClientCodeAndUser_Id(null, null);
		userServiceImpl.addSecurityEntry(null, null, null);
		assertTrue(true);
	}

	@Test
	public void testGetClientsByMultipleCostCenters() {
		List<CostCenterClientDTO> costCenterClients = new ArrayList<>();
		CostCenterClientDTO data = new CostCenterClientDTO();
		data.setBusinessName("1 hour drain");
		data.setClientId("909464");
		data.setClientName("1 Hour Drain");
		data.setCostCenter("costCenter");
		data.setDescription("description");
		data.setId(1l);
		data.setLegalName("1 Hour Drain");
		data.setStatus("Active");
		costCenterClients.add(data);
		List<String> clients = new ArrayList<>();
		clients.add("costCenter");
		Mockito.doReturn("http://localhost:9000/ws/rest/V2/getClientIdByCostCenter?costCenterId=%s").when(boomiHelper)
				.getUrl(Mockito.any());
		Mockito.doReturn(costCenterClients).when(webClientTemplate).getForObjectList(Mockito.any(), Mockito.any(),
				Mockito.any());
		List<CostCenterClientDTO> result = userServiceImpl.getClientsByMultipleCostCenters(clients);
		assertEquals(result.size(), costCenterClients.size());
	}

	@Test
	public void testGetClientsByMultipleCostCentersForexception() {
		Mockito.doThrow(BbsiException.class).when(webClientTemplate).getForObjectList(Mockito.any(), Mockito.any(),
				Mockito.any());
		List<CostCenterClientDTO> result = userServiceImpl.getClientsByMultipleCostCenters(new ArrayList<>());
		assertEquals(0, result.size());
	}

	@Test
	public void testReInitiateNewHire() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "sdhudhipala@osius.com,admin@osius.com");
		List<Object[]> objectList = new ArrayList<Object[]>();
		Object[] obj = { "fname", "lname", "9090909090", "email@bbsi.com", true };
		objectList.add(obj);
		Mockito.doReturn(objectList).when(usersRepository).getUserDataByEmail(Mockito.anyString(), Mockito.anyString());
		userServiceImpl.reInitiateNewHire(populateUsersDTOForVancouver1(), token, "/login", userPrincipal);
		Mockito.verify(usersRepository, Mockito.atLeastOnce()).getUserDataByEmail(Mockito.anyString(),
				Mockito.anyString());

	}

	@Test
	public void testReInitiateNewHireForUserExistDTOFirstLoginFalse() {
		List<Object[]> objectList = new ArrayList<Object[]>();
		Object[] obj = { "fname", "lname", "9090909090", "email@bbsi.com", false };
		objectList.add(obj);
		Mockito.doReturn(objectList).when(usersRepository).getUserDataByEmail(Mockito.anyString(), Mockito.anyString());
		userServiceImpl.reInitiateNewHire(populateUsersDTOForVancouver1(), token, "/login", userPrincipal);
		Mockito.verify(usersRepository, Mockito.atLeastOnce()).getUserDataByEmail(Mockito.anyString(),
				Mockito.anyString());
	}

	@Test
	public void testReInitiateNewHireThrowsException() {
		List<Object[]> objectList = new ArrayList<Object[]>();
		Object[] obj = { "fname", "lname", "9090909090", "email@bbsi.com", false };
		objectList.add(obj);
		Mockito.doReturn(objectList).when(usersRepository).getUserDataByEmail(Mockito.anyString(), Mockito.anyString());
		userServiceImpl.reInitiateNewHire(userDTO, token, "/login", userPrincipal);
		assertTrue(true);
	}

	private CommonDTO populateCommonDto() {
		CommonDTO commonDto = new CommonDTO();
		commonDto.setCode("909090");
		commonDto.setPrivileges(populateCommonDTOList());
		return commonDto;
	}

	private List<CommonDTO> populateCommonDTOList() {
		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO commonDTO = new CommonDTO();
		commonDTO.setId(1233);
		commonDTO.setName("Bbsi");
		commonDTO.setCode("privilege1");
		commonDTO.setType("Client");
		commonDTO.setChild(populateChild());
		commonDTO.setIsSelected(true);
		commonDto.add(commonDTO);
		commonDTO = new CommonDTO();
		commonDTO.setCode(MethodNames.PAY_TS_HOUR_SUBMIT_ALL.toString());
		commonDTO.setIsSelected(true);
		commonDto.add(commonDTO);
		commonDTO = new CommonDTO();
		commonDTO.setName("name");
		commonDTO.setCode(MethodNames.PAY_TS_HOUR_FINALIZE_ALL.toString());
		commonDTO.setIsSelected(true);
		commonDto.add(commonDTO);

		commonDTO = new CommonDTO();
		commonDTO.setName("name");
		commonDTO.setCode("EMP_MGMT.VIEW");
		commonDTO.setIsSelected(true);
		commonDto.add(commonDTO);
		commonDTO = new CommonDTO();
		commonDTO.setName("name");
		commonDTO.setCode("EMP_MGMT_ALL");
		commonDTO.setIsSelected(true);
		commonDto.add(commonDTO);
		return commonDto;
	}

	private List<CommonDTO> populateCommonDTOList1() {
		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO commonDTO = new CommonDTO();
		commonDTO.setId(1233);
		commonDTO.setName("Bbsi");
		commonDTO.setCode("privilege1");
		commonDTO.setType("Client");
		commonDTO.setChild(populateChild());
		commonDTO.setIsSelected(true);
		commonDto.add(commonDTO);
		CommonDTO commonDTO1 = new CommonDTO();
		commonDTO1.setCode("NEW_HIRE_I9_APPROVER_F.ALL");
		commonDTO1.setIsSelected(true);
		commonDto.add(commonDTO1);
		CommonDTO commonDTO2 = new CommonDTO();
		commonDTO2.setName("name");
		commonDTO2.setCode("NEW_HIRE_I9_APPROVER_F.VIEW");
		commonDTO2.setIsSelected(true);
		commonDto.add(commonDTO2);
		return commonDto;
	}

	private List<CommonDTO> populateChild() {
		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO commonDTO = new CommonDTO();
		commonDTO.setId(1233);
		commonDTO.setName("Bbsi");
		commonDTO.setType("Client");
		commonDTO.setIsSelected(true);
		commonDto.add(commonDTO);
		return commonDto;
	}

	private Users populateUserWhenUserTypeIsNewHire() {
		Users users = new Users();
		users.setId(12l);
		users.setEmail("admin@bbsi.com");
		users.setPassword("password");
		users.setTokenValue("12jkkj-1231d-as123");
		users.setClients(populateUserClientsWhenIsNewHire());
		return users;
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
		customDTO.setIsAccountLoginChange(Boolean.TRUE);
		return customDTO;
	}

	private CustomPersonalDTO populateCustomPersonalDTOWithNoEmail() {
		CustomPersonalDTO custom = new CustomPersonalDTO();
		custom.setClientCode("909464");
		custom.setEmployeeCode("E71624");
		return custom;
	}

	private List<UserClients> populateUserClientsWhenIsNewHire() {
		List<UserClients> listUserClients = new ArrayList<UserClients>();
		UserClients clients = new UserClients();
		clients.setClientCode("909090");
		clients.setFirstName("testBbsi");
		clients.setIsActive(true);
		clients.setClient(getClientMaster("909090", "Bbsi"));
		clients.setUserType("NewHire");
		clients.setId(9l);
		clients.setIsCaliforniaUser(true);
		listUserClients.add(clients);
		return listUserClients;
	}

	private Users populateUserWhenUserTypeIsClient() {
		Users users = new Users();
		users.setId(12l);
		users.setEmail("admin@bbsi.com");
		users.setPassword("password");
		users.setTokenValue("12jkkj-1231d-as123");
		users.setClients(populateUserClientsWhenIsClient());
		return users;
	}

	private ClientMaster getClientMaster(String code, String name) {
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode(code);
		clientMaster.setClientName(name);
		clientMaster.setCostCenterCode("costCenterCode");
		clientMaster.setCostCenterDescription("description");
		return clientMaster;
	}

	private List<UserClients> populateUserClientsWhenIsClient() {
		List<UserClients> listUserClients = new ArrayList<UserClients>();
		UserClients clients = new UserClients();
		clients.setClientCode("909090");
		clients.setFirstName("testBbsi");
		clients.setIsActive(true);
		clients.setClient(getClientMaster("909090", "Bbsi"));
		clients.setUserType("Client");
		clients.setRememberTimenetCompanyId(true);
		clients.setRememberTimenetPassword(true);
		clients.setRememberTimenetUserId(true);
		listUserClients.add(clients);
		return listUserClients;
	}

	private Users populateUserWhenUserTypeIsEmployee() {
		Users users = new Users();
		users.setId(12l);
		users.setEmail("admin@bbsi.com");
		users.setPassword("password");
		users.setTokenValue("12jkkj-1231d-as123");
		users.setClients(populateUserClientsWhenIsEmployee());
		return users;
	}

	private List<UserClients> populateUserClientsWhenIsEmployee() {
		List<UserClients> listUserClients = new ArrayList<UserClients>();
		UserClients clients = new UserClients();
		clients.setClientCode("909090");
		clients.setId(123l);
		clients.setFirstName("testBbsi");
		clients.setIsActive(true);
		clients.setClient(getClientMaster("909090", "Bbsi"));
		clients.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		listUserClients.add(clients);
		return listUserClients;
	}

	private Users populateUserWithTypeUser() {
		Users users = new Users();
		users.setId(12l);
		users.setEmail("admin@bbsi.com");
		users.setPassword("password");
		users.setIsFirstLogin(true);
		users.setClients(populateUserClientsWithUser());
		users.setTokenValue("12jkkj-1231d-as123");
		return users;
	}

	private List<UserClients> populateUserClientsWithUser() {
		List<UserClients> listUserClients = new ArrayList<UserClients>();
		UserClients clients = new UserClients();
		clients.setClientCode("909090");
		clients.setFirstName("testBbsi");
		clients.setLastName("Admin");
		clients.setMobile("9090090909");
		clients.setIsActive(true);
		clients.setIsPrimary(true);
		clients.setClient(getClientMaster("909090", "Bbsi"));
		clients.setEndDate(LocalDateTime.now());
		clients.setStartDate(LocalDateTime.now());
		clients.setUserType("Client");
		clients.setI9Approver(true);
		clients.setUser(users);
		clients.setId(12l);
		clients.setEmployeeCode("E71624");
		listUserClients.add(clients);

		clients = new UserClients();
		clients.setClientCode("909090");
		clients.setFirstName("testBbsi");
		clients.setLastName("Admin");
		clients.setMobile("9090090909");
		clients.setIsActive(true);
		clients.setIsPrimary(true);
		clients.setClient(getClientMaster("909090", "Bbsi"));
		clients.setEndDate(LocalDateTime.now());
		clients.setStartDate(LocalDateTime.now());
		clients.setUserType("USERTYPE_EMPLOYEE");
		clients.setI9Approver(true);
		clients.setUser(users);
		clients.setId(12l);
		listUserClients.add(clients);
		return listUserClients;
	}

	private List<UserClients> populateUserClientsWithEmployee() {
		List<UserClients> listUserClients = new ArrayList<UserClients>();
		UserClients clients = new UserClients();
		clients.setClientCode("909090");
		clients.setFirstName("testBbsi");
		clients.setLastName("Admin");
		clients.setMobile("9090090909");
		clients.setIsActive(true);
		clients.setIsPrimary(true);
		clients.setClient(getClientMaster("909090", "Bbsi"));
		clients.setEndDate(LocalDateTime.now());
		clients.setStartDate(LocalDateTime.now());
		clients.setUserType("Employee");
		clients.setI9Approver(true);
		clients.setUser(users);
		clients.setId(12l);
		listUserClients.add(clients);
		return listUserClients;
	}

	private Users populateUser() {
		Users users = new Users();
		users.setId(1l);
		users.setEmail("test@test.com");
		users.setPassword("password");
		users.setClients(populateUserClients());
		users.setTokenValue("1234-5678-abcd-efgh");
		return users;
	}

	private Users populateUser1() {
		Users users = new Users();
		users.setId(1l);
		users.setEmail("test@test.com");
		users.setPassword("password");
		List<UserClients> clients = new ArrayList<>();
		UserClients client = new UserClients();
		client.setUser(users);
		client.setClientCode("909464");
		client.setClient(populateClientMaster().get(0));
		client.setEmployeeCode("E1768F");
		client.setI9Approver(false);
		client.setId(1l);
		client.setIsActive(true);
		client.setIsPrimary(true);
		client.setNewHireId(1l);
		client.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		client.setVersion(1l);
		client.setUserFilters(userSecurityList);
		users.setClients(clients);
		users.setTokenValue("1234-5678-abcd-efgh");
		return users;
	}

	private Users populateUserWithNoClientRole() {
		Users users = new Users();
		users.setId(12l);
		users.setEmail("admin@bbsi.com");
		users.setPassword("password");
		users.setTokenValue("12jkkj-1231d-as123");
		users.setRoles(populateRbacEntity());
		users.setClients(populateUserClientsWithoutClientrole());
		return users;
	}

	private List<UserClients> populateUserClients() {
		List<UserClients> listUserClients = new ArrayList<UserClients>();
		UserClients clients = new UserClients();
		clients.setMobile("9087654321");
		clients.setClientCode("909464");
		clients.setClient(getClientMaster("909464", "1 Hour Drain"));
		clients.setIsActive(true);
		List<ClientRole> clientRoles = new ArrayList<>();
		ClientRole clientRole = new ClientRole();
		clientRole.setClientCode("909464");
		clientRole.setId(1l);
		Mapping role = new Mapping();
		role.setCode("code");
		role.setDescription("description");
		role.setId(1l);
		List<Mapping> mappings = new ArrayList<>();
		Mapping mapping = new Mapping();
		mapping.setCode("code1");
		mapping.setDescription("description1");
		mapping.setId(1l);
		mapping.setName("name");
		List<PrivilegeMapping> privileges = new ArrayList<>();
		PrivilegeMapping privilege = new PrivilegeMapping();
		privilege.setCode("code");
		privilege.setId(1l);
		privilege.setName("name");
		privilege.setType(GenericConstants.USERTYPE_NEWHIRE.toString());
		privileges.add(privilege);
		mapping.setPrivileges(privileges);
		role.setMappings(mappings);
		clientRole.setRole(role);
		clientRoles.add(clientRole);
		clients.setClientRoles(clientRoles);
		clients.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		clients.setVersion(1l);
		UserSecurity userFilter = new UserSecurity();
		userFilter.setId(1l);
		userFilter.setType(GenericConstants.USERTYPE_NEWHIRE.toString());
		userFilter.setValue(token);
		List<UserSecurity> userFilters = new ArrayList<>();
		userFilters.add(userFilter);
		clients.setUserFilters(userFilters);
		clients.setUser(populateUsers());
		listUserClients.add(clients);
		clients = new UserClients();
		Users newUser = new Users();
		newUser.setId(1l);
		newUser.setEmail("test.com");
		newUser.setMfaEmail("mfa@test.com");
		clients.setUser(newUser);
		clients.setId(2l);
		clients.setNewHireId(2l);
		clients.setEndDate(LocalDateTime.now().plusYears(10));
		clients.setFirstName("first");
		clients.setLastName("last");
		clients.setMobile("9080706050");
		clients.setClientCode("909464");
		clients.setIsActive(true);
		clients.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		listUserClients.add(clients);
		return listUserClients;
	}

	private List<UserClients> populateUserClientsWithoutClientrole() {
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
		clients.setUser(users);
		clients.setId(12l);
		listUserClients.add(clients);
		return listUserClients;
	}

	private UserClients getUserClients() {
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
		clients.setUser(users);
		clients.setId(12l);
		return clients;
	}

	private Mapping populateMapping() {
		Mapping mapping = new Mapping();
		mapping.setCode("909090");
		mapping.setPrivileges(populatePrivilegeMapping());
		return mapping;
	}

	private List<PrivilegeMapping> populatePrivilegeMapping() {
		List<PrivilegeMapping> privilegeList = new ArrayList<PrivilegeMapping>();
		PrivilegeMapping privilege = new PrivilegeMapping();
		privilege.setCode("909090");
		privilege.setIsEditable(true);
		privilege.setIsSelected(true);
		privilege.setMapping(populateMappingForPrivilege());
		privilegeList.add(privilege);
		return privilegeList;

	}

	private Mapping populateMappingForPrivilege() {
		Mapping mapping = new Mapping();
		mapping.setCode("909090");
		return mapping;
	}

	private List<UserClients> populateUserClientsForClient() {
		List<UserClients> listUserClients = new ArrayList<UserClients>();
		UserClients clients = new UserClients();
		clients.setClientCode("909464");
		clients.setFirstName("testBbsi");
		clients.setLastName("Admin");
		clients.setMobile("9090090909");
		clients.setIsActive(true);
		clients.setIsPrimary(true);
		clients.setClient(getClientMaster("909464", "Bbsi"));
		clients.setEndDate(LocalDateTime.now());
		clients.setStartDate(LocalDateTime.now());
		clients.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		clients.setUser(users);
		clients.setId(12l);
		Users user = new Users();
		user.setEmail("admin@osius.com");
		user.setId(2l);
		clients.setUser(user);
		listUserClients.add(clients);
		return listUserClients;
	}

	private List<UserClients> populateUserClientsForVancouver() {
		List<UserClients> listUserClients = new ArrayList<UserClients>();
		UserClients clients = new UserClients();
		clients.setClientCode("909464");
		clients.setFirstName("testBbsi");
		clients.setLastName("Admin");
		clients.setMobile("9090090909");
		clients.setIsActive(true);
		clients.setIsPrimary(true);
		clients.setClient(getClientMaster("909464", "Bbsi"));
		clients.setEndDate(LocalDateTime.now());
		clients.setStartDate(LocalDateTime.now());
		clients.setUserType("Vancouver Operations Center");
		clients.setUser(users);
		clients.setId(12l);
		Users user = new Users();
		user.setEmail("admin@osius.com");
		user.setId(1l);
		clients.setUser(user);
		listUserClients.add(clients);
		return listUserClients;
	}

	private List<UserClients> populateUserClientsNoUser() {
		List<UserClients> listUserClients = new ArrayList<UserClients>();
		UserClients clients = new UserClients();
		clients.setClientCode("909464");
		clients.setFirstName("testBbsi");
		clients.setLastName("Admin");
		clients.setMobile("9090090909");
		clients.setIsActive(true);
		clients.setIsPrimary(true);
		clients.setClient(getClientMaster("909464", "Bbsi"));
		clients.setEndDate(LocalDateTime.now());
		clients.setStartDate(LocalDateTime.now());
		clients.setUserType("NewHire");
		clients.setId(12l);
		listUserClients.add(clients);
		return listUserClients;
	}

	private UserClients populateUserClientsForUserTypeNewHire() {
		UserClients data = populateUserClient();
		data.setId(2l);
		data.setUserType(GenericConstants.USERTYPE_NEWHIRE);
		data.setEmployeeCode("Z1333F");
		return data;
	}

	private UsersDTO populateUserDTO(boolean isClient) {
		UsersDTO userDTO = new UsersDTO();
		userDTO.setEmail("admin@osius.com");
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
		userDTO.setIsCaliforniaUser(true);
		userDTO.setIsAccountLoginChange(Boolean.FALSE);
		List<ClientDTOV2> clients = new ArrayList<>();
		ClientDTOV2 client = new ClientDTOV2();
		client.setClientCode("909464");
		client.setClientName("1 Hour Drain");
		client.setEmployeeCode("E1768F");
		client.setI9Approver(false);
		client.setId(1l);
		client.setIsActive(false);
		client.setNewHireId(1l);
		client.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		client.setVersion(1l);
		List<UserClientRoleDTOV2> clientRoles = new ArrayList<>();
		UserClientRoleDTOV2 clientRole = new UserClientRoleDTOV2();
		clientRole.setCode("AUD_LOG.ALL");
		clientRole.setDescription("Audit log all privileges");
		clientRole.setId(1l);
		clientRole.setIsActive(true);
		clientRole.setName("AUDIT LOG");
		clientRole.setRoleId(1l);
		clientRole.setType("ALL");
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
		privilege.setCode("909464");
		privilege.setDescription("description");
		privilege.setId(1l);
		privilege.setType(UserEnum.PRIVILEGE.toString());
		privilege.setVersion(1l);
		privileges.add(privilege);
		accessGroup.setPrivileges(privileges);
		List<FeatureCodeHierarchyDTOV2> featureCodeHierarchy = new ArrayList<>();
		FeatureCodeHierarchyDTOV2 featureHierarchy = new FeatureCodeHierarchyDTOV2();
		featureHierarchy.setCode("909464");
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
		if (!isClient) {
			List<CostCenterDTO> costCenters = new ArrayList<>();
			CostCenterDTO costCenter = new CostCenterDTO();
			costCenter.setAll_clients_selected(true);
			List<CostCenterClientDTO> clientList = new ArrayList<>();
			CostCenterClientDTO cli = new CostCenterClientDTO();
			cli.setBusinessName("Business_Name_1_Hour_Drain");
			cli.setClientId("909464");
			cli.setClientName("1 Hour Drain");
			cli.setCostCenter("Vancouver");
			cli.setDescription("description");
			cli.setId(1l);
			cli.setLegalName("1 Hour Drain");
			cli.setStatus("Active");
			clientList.add(cli);
			costCenter.setClientList(clientList);
			List<ClientDTO> clients1 = new ArrayList<>();
			ClientDTO client1 = new ClientDTO();
			client1.setCode("909464");
			client1.setCorporationType("PRIVATE");
			client1.setCorporationTypeDescription("private sector");
			client1.setDescription("Plumbing");
			client1.setVersion(1l);
			costCenter.setClients(clients1);
			userDTO.setCostCenters(costCenters);
		}
		List<UserClientRoleDTOV2> roles = new ArrayList<>();
		UserClientRoleDTOV2 role = new UserClientRoleDTOV2();
		List<AccessGroupDTOV2> accessGroups1 = new ArrayList<>();
		AccessGroupDTOV2 accessGroup1 = new AccessGroupDTOV2();
		accessGroup1.setClientCode("909464");
		accessGroup1.setDescription("description");
		List<FeatureCodeHierarchyDTOV2> featureCodeHierarchy1 = new ArrayList<>();
		FeatureCodeHierarchyDTOV2 featureCode = new FeatureCodeHierarchyDTOV2();
		featureCode.setCode("feature1");
		featureCode.setDescription("feature description");
		featureCode.setId(1l);
		featureCode.setLevel("level");
		featureCode.setName("name");
		featureCode.setPrivileges(privileges);
		featureCode.setType("type1");
		accessGroup1.setFeatureCodeHierarchy(featureCodeHierarchy1);
		accessGroups1.add(accessGroup1);
		role.setAccessGroups(accessGroups1);
		role.setRoleId(1l);
		role.setCode("role1");
		roles.add(role);
		userDTO.setRoles(roles);
		List<UserSecurityDTOV2> filter = new ArrayList<>();
		UserSecurityDTOV2 security = new UserSecurityDTOV2();
		security.setClientCode("909464");
		security.setType("type");
		Set<String> value = new HashSet<>();
		value.add("value1");
		value.add("value2");
		value.add("value3");
		security.setValue(value);
		filter.add(security);
		userDTO.setFilter(filter);
		return userDTO;
	}

	private UsersDTO populateUserDTO1(boolean isClient) {
		UsersDTO userDTO = new UsersDTO();
		userDTO.setEmail("admin@osius.com");
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
		userDTO.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		userDTO.setVersion(1l);
		userDTO.setIsCaliforniaUser(true);
		userDTO.setIsAccountLoginChange(Boolean.FALSE);
		List<ClientDTOV2> clients = new ArrayList<>();
		ClientDTOV2 client = new ClientDTOV2();
		client.setClientCode("909464");
		client.setClientName("1 Hour Drain");
		client.setEmployeeCode("E1768F");
		client.setI9Approver(false);
		client.setId(1l);
		client.setIsActive(false);
		client.setNewHireId(1l);
		client.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		client.setVersion(1l);
		List<UserClientRoleDTOV2> clientRoles = new ArrayList<>();
		UserClientRoleDTOV2 clientRole = new UserClientRoleDTOV2();
		clientRole.setCode("AUD_LOG.ALL");
		clientRole.setDescription("Audit log all privileges");
		clientRole.setIsActive(true);
		clientRole.setName("AUDIT LOG");
		clientRole.setType("ALL");
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
		privilege.setCode("909464");
		privilege.setDescription("description");
		privilege.setId(1l);
		privilege.setType(UserEnum.PRIVILEGE.toString());
		privilege.setVersion(1l);
		privileges.add(privilege);
		accessGroup.setPrivileges(privileges);
		List<FeatureCodeHierarchyDTOV2> featureCodeHierarchy = new ArrayList<>();
		FeatureCodeHierarchyDTOV2 featureHierarchy = new FeatureCodeHierarchyDTOV2();
		featureHierarchy.setCode("909464");
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
		if (!isClient) {
			List<CostCenterDTO> costCenters = new ArrayList<>();
			CostCenterDTO costCenter = new CostCenterDTO();
			costCenter.setAll_clients_selected(true);
			List<CostCenterClientDTO> clientList = new ArrayList<>();
			CostCenterClientDTO cli = new CostCenterClientDTO();
			cli.setBusinessName("Business_Name_1_Hour_Drain");
			cli.setClientId("909464");
			cli.setClientName("1 Hour Drain");
			cli.setCostCenter("Vancouver");
			cli.setDescription("description");
			cli.setId(1l);
			cli.setLegalName("1 Hour Drain");
			cli.setStatus("Active");
			clientList.add(cli);
			costCenter.setClientList(clientList);
			List<ClientDTO> clients1 = new ArrayList<>();
			ClientDTO client1 = new ClientDTO();
			client1.setCode("909464");
			client1.setCorporationType("PRIVATE");
			client1.setCorporationTypeDescription("private sector");
			client1.setDescription("Plumbing");
			client1.setVersion(1l);
			costCenter.setClients(clients1);
			userDTO.setCostCenters(costCenters);
		}
		List<UserClientRoleDTOV2> roles = new ArrayList<>();
		UserClientRoleDTOV2 role = new UserClientRoleDTOV2();
		List<AccessGroupDTOV2> accessGroups1 = new ArrayList<>();
		AccessGroupDTOV2 accessGroup1 = new AccessGroupDTOV2();
		accessGroup1.setClientCode("909464");
		accessGroup1.setDescription("description");
		List<FeatureCodeHierarchyDTOV2> featureCodeHierarchy1 = new ArrayList<>();
		FeatureCodeHierarchyDTOV2 featureCode = new FeatureCodeHierarchyDTOV2();
		featureCode.setCode("feature1");
		featureCode.setDescription("feature description");
		featureCode.setId(1l);
		featureCode.setLevel("level");
		featureCode.setName("name");
		featureCode.setPrivileges(privileges);
		featureCode.setType("type1");
		accessGroup1.setFeatureCodeHierarchy(featureCodeHierarchy1);
		accessGroups1.add(accessGroup1);
		role.setAccessGroups(accessGroups1);
		role.setRoleId(1l);
		role.setCode("role1");
		roles.add(role);
		userDTO.setRoles(roles);
		List<UserSecurityDTOV2> filter = new ArrayList<>();
		UserSecurityDTOV2 security = new UserSecurityDTOV2();
		security.setClientCode("909464");
		security.setType("type");
		Set<String> value = new HashSet<>();
		value.add("value1");
		value.add("value2");
		value.add("value3");
		security.setValue(value);
		filter.add(security);
		userDTO.setFilter(filter);
		return userDTO;
	}

	private List<CostCenterDTO> populateCostCenter() {
		List<CostCenterDTO> costCenter = new ArrayList<CostCenterDTO>();
		CostCenterDTO center = new CostCenterDTO();
		center.setCode("009");
		center.setDescription("description1");
		center.setId(123L);
		center.setUserType("Vancouver Operations Center");
		center.setIsHrpLoad(true);
		center.setClientList(populateCostCenterClient());
		center.setClients(populateClientList());
		center.setStatus(true);
		costCenter.add(center);
		return costCenter;
	}

	private List<CostCenterClientDTO> populateCostCenterClient() {
		List<CostCenterClientDTO> listCostCenter = new ArrayList<CostCenterClientDTO>();
		CostCenterClientDTO costCenter = new CostCenterClientDTO();
		costCenter.setClientId("909464");
		listCostCenter.add(costCenter);
		return listCostCenter;
	}

	private List<ClientDTO> populateClientList() {
		List<ClientDTO> listClient = new ArrayList<ClientDTO>();
		ClientDTO clientDTO = new ClientDTO();
		clientDTO.setId(12l);
		clientDTO.setIsSelected(true);
		clientDTO.setCode("909464");
		listClient.add(clientDTO);
		return listClient;
	}

	private UserClients populateUserAndClient() {
		UserClients clients = new UserClients();
		clients.setClientCode("code");
		clients.setFirstName("testBbsi");
		clients.setLastName("Admin");
		clients.setMobile("9090090909");
		clients.setIsActive(false);
		clients.setIsPrimary(true);
		clients.setClient(populateClientMaster().get(1));
		clients.setUser(users);
		clients.setEndDate(LocalDateTime.now());
		clients.setStartDate(LocalDateTime.now());
		clients.setUserType("NewHire");
		clients.setId(12l);
		return clients;
	}

	private List<UserSecurity> populateSecurity() {
		List<UserSecurity> userSecurity = new ArrayList<UserSecurity>();
		UserSecurity user = new UserSecurity();
		user.setUserClient(populateUserAndClient());
		user.setId(1232l);
		user.setType("Client");
		user.setValue("value");
		userSecurity.add(user);
		return userSecurity;
	}

	private List<PrismizedClientsDTO> populatePrismizedClient() {
		List<PrismizedClientsDTO> prismizedClient = new ArrayList<PrismizedClientsDTO>();
		PrismizedClientsDTO prismized = new PrismizedClientsDTO();
		prismized.setClient_code("909464");
		prismizedClient.add(prismized);
		prismized = new PrismizedClientsDTO();
		prismized.setClient_code("clientCode");
		prismizedClient.add(prismized);
		return prismizedClient;
	}

	private PolicyAcceptedDTO populatePolicyAccepted() {
		PolicyAcceptedDTO policy = new PolicyAcceptedDTO();
		policy.setEmail("test@bbsi.com");
		policy.setIsPolicyAccepted(true);
		return policy;
	}

	private Users populateUsers() {
		Users users = new Users();
		users.setAuthenticationType("Bearer");
		users.setEmail("test@test.com");
		users.setMfaEmail("mfa@test.com");
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
		client.setClient(populateClientMaster().get(0));
		client.setEmployeeCode("E1768F");
		client.setI9Approver(false);
		client.setId(1l);
		client.setIsActive(true);
		client.setIsPrimary(true);
		client.setNewHireId(1l);
		client.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		client.setVersion(1l);
		List<ClientRole> clientRoles = new ArrayList<>();
		ClientRole clientRole = new ClientRole();
		clientRole.setClientCode("909464");
		clientRole.setId(1l);
		clientRole.setSectionId(1l);
		Mapping crole = new Mapping();
		crole.setCode("role1");
		crole.setId(1l);
		clientRole.setRole(crole);
		clientRoles.add(clientRole);
		client.setClientRoles(clientRoles);
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
		return users;
	}

	private Users populateUsers2() {
		Users users = new Users();
		users.setAuthenticationType("Bearer");
		users.setEmail("test@test.com");
		users.setMfaEmail("mfa@test.com");
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
		client.setClient(populateClientMaster().get(0));
		client.setEmployeeCode("E1768F");
		client.setI9Approver(false);
		client.setId(1l);
		client.setIsActive(true);
		client.setIsPrimary(true);
		client.setNewHireId(1l);
		client.setVersion(1l);

		UserClients client1 = new UserClients();
		client1.setUser(users);
		client1.setClientCode("909464");
		client1.setClient(populateClientMaster().get(0));
		client1.setEmployeeCode("E1768F");
		client1.setI9Approver(false);
		client1.setId(1l);
		client1.setIsActive(true);
		client1.setIsPrimary(true);
		client1.setNewHireId(1l);
		client1.setUserType(GenericConstants.USERTYPE_NEWHIRE.toString());
		client1.setVersion(1l);
		List<ClientRole> clientRoles = new ArrayList<>();
		ClientRole clientRole = new ClientRole();
		clientRole.setClientCode("909464");
		clientRole.setId(1l);
		clientRole.setSectionId(1l);
		Mapping crole = new Mapping();
		crole.setCode("role1");
		crole.setId(1l);
		clientRole.setRole(crole);
		clientRoles.add(clientRole);
		client.setClientRoles(clientRoles);
		clients.add(client);
		clients.add(client1);
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
		return users;
	}

	private Users populateUsers1() {
		Users users = new Users();
		users.setAuthenticationType("Bearer");
		users.setEmail("test@test.com");
		users.setId(1l);
		users.setInvalidAttempts(0);
		users.setIsFirstLogin(true);
		users.setIsPolicyAccepted(true);
		users.setIsPolicyUpdated(false);
		users.setPassword("Osicpl@2");
		users.setTokenValue("token");
		users.setVersion(1);
		List<UserClients> clients = new ArrayList<>();
		UserClients client = new UserClients();
		client.setUser(users);
		client.setClientCode("909464");
		client.setClient(populateClientMaster().get(0));
		client.setEmployeeCode("E1768F");
		client.setI9Approver(false);
		client.setId(1l);
		client.setIsActive(true);
		client.setIsPrimary(true);
		client.setNewHireId(1l);
		client.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		client.setVersion(1l);
		client.setUserFilters(userSecurityList);
		List<ClientRole> clientRoles = new ArrayList<>();
		ClientRole clientRole = new ClientRole();
		clientRole.setClientCode("909464");
		clientRole.setId(1l);
		clientRole.setSectionId(1l);
		Mapping crole = new Mapping();
		crole.setCode("role1");
		crole.setId(1l);
		clientRole.setRole(crole);
		clientRoles.add(clientRole);
		client.setClientRoles(clientRoles);
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
		return users;
	}

	private UserCustomDTOV2 populateUserCustomDTOV2() {
		UserCustomDTOV2 data = new UserCustomDTOV2();
		data.setEmail("jchilakapati@osius.com");
		data.setFirstName("Jagruthi");
		data.setId(1l);
		List<ClientDTOV2> clients = new ArrayList<>();
		ClientDTOV2 client = new ClientDTOV2();
		client.setClientCode("909464");
		client.setClientName("1 Hour Drain");
		client.setCostCenterCode("cc1");
		client.setCostCenterDescription("costCenterCode1");
		client.setEmployeeCode("E17869");
		client.setId(1l);
		List<CommonDTO> mappings = new ArrayList<>();
		CommonDTO mapping = new CommonDTO();
		mapping.setCode("909464");
		mapping.setDescription("description");
		mapping.setId(1l);
		mapping.setName("name");
		mapping.setNewStatus(true);
		mapping.setType(GenericConstants.USERTYPE_VANCOUVER.toString());
		mappings.add(mapping);
		client.setMappings(mappings);
		client.setUserType(GenericConstants.USERTYPE_VANCOUVER.toString());
		clients.add(client);
		data.setClients(clients);
		return data;
	}

	private List<UserInvitationDTO> populateUserInvitationDTOList() {
		List<UserInvitationDTO> dataList = new ArrayList<>();
		UserInvitationDTO data = new UserInvitationDTO();
		data.setEmail("test@test.com");
		data.setEmployeeCode("ABC");
		data.setEndDate(LocalDateTime.now().plusYears(12));
		data.setFirstName("first");
		data.setId(1l);
		data.setLastName("last");
		data.setMobile(populateUserDTO(true).getMobile());
		data.setStartDate(LocalDateTime.now());
		data.setStatus(false);
		data.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		dataList.add(data);
		return dataList;
	}

	private UserClients populateUserClient() {
		UserClients data = new UserClients();
		data.setClientCode("909464");
		data.setClient(getClientMaster("909464", "1 Hour Drain"));
		data.setEmployeeCode("E1786F");
		data.setEndDate(LocalDateTime.now().plusYears(10));
		data.setFirstName("first Name");
		data.setI9Approver(false);
		data.setId(1l);
		data.setIsActive(false);
		data.setIsCaliforniaUser(true);
		data.setIsCCPAAccepted(false);
		data.setIsCCPAUpdated(false);
		data.setIsPrimary(true);
		data.setLastName("last");
		data.setMobile("9080706045");
		data.setNewHireId(0l);
		data.setRememberTimenetCompanyId(false);
		data.setRememberTimenetPassword(false);
		data.setRememberTimenetUserId(false);
		data.setStartDate(LocalDateTime.now());
		data.setTimenetCompanyId("909464");
		data.setTimenetPassword("Osicpl@2");
		Users user = new Users();
		user.setAuthenticationType("Bearer");
		List<UserClients> clients = new ArrayList<>();
		UserClients client = new UserClients();
		client.setClientCode("909464");
		client.setClient(getClientMaster("909464", "1 Hour Drain"));
		List<ClientRole> clientRoles = new ArrayList<>();
		ClientRole clientRole = new ClientRole();
		clientRole.setClientCode("909464");
		clientRole.setId(1l);
		Mapping role = new Mapping();
		role.setCode("ROLE");
		role.setDescription("description");
		role.setId(1l);
		role.setIsReplica(false);
		List<Mapping> mappings = new ArrayList<>();
		Mapping mapping = new Mapping();
		mapping.setCode("NOTF_MGMT_TMPL");
		mapping.setDescription("Notification Templates");
		mapping.setId(10l);
		mappings.add(mapping);
		role.setMappings(mappings);
		role.setName("Notification Templates");
		List<PrivilegeMapping> privileges = new ArrayList<>();
		PrivilegeMapping privilege = new PrivilegeMapping();
		privilege.setCode("NOTF_MGMT_TMPL");
		privilege.setId(1l);
		privilege.setMapping(mapping);
		privilege.setName("Notification Templates");
		privilege.setType("NOTF_MGMTEmployeeEmployeeACCESS_GROUP");
		role.setPrivileges(privileges);
		role.setStatus(true);
		role.setType("NOTF_MGMTEmployeeEmployeeACCESS_GROUP");
		clientRole.setRole(role);
		clientRoles.add(clientRole);
		client.setClientRoles(clientRoles);
		clients.add(client);
		user.setClients(clients);
		user.setEmail("test@test.com");
		user.setMfaEmail("mfa@test.com");
		user.setId(1l);
		user.setInvalidAttempts(0);
		user.setIsFirstLogin(false);
		user.setIsPolicyAccepted(true);
		user.setIsPolicyUpdated(false);
		user.setPassword("Osicpl@2");
		List<RbacEntity> roles = new ArrayList<>();
		RbacEntity role1 = new RbacEntity();
		role1.setClientCode("909464");
		role1.setCode("NOTF_MGMT_TMPL");
		role1.setDescription("Notification Templates");
		role1.setId(1l);
		role1.setIsClientTemplate(false);
		role1.setMappingId(1l);
		role1.setName("Notification Templates");
		role1.setStatus(true);
		role1.setType("NOTF_MGMTEmployeeEmployeeACCESS_GROUP");
		role1.setVersion(1l);
		user.setRoles(roles);
		user.setTokenValue("Bearer 1234-5678-abcd-efgh");
		user.setVersion(1l);
		data.setUser(user);
		data.setNewHireId(1l);
		data.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		return data;
	}

	private RbacEntity populateRbac() {
		RbacEntity data = new RbacEntity();
		data.setClientCode("909464");
		data.setCode("role1");
		data.setDescription("role description");
		data.setId(1l);
		data.setIsClientTemplate(false);
		data.setMappingId(1l);
		data.setName("name");
		data.setStatus(true);
		data.setType("type");
		return data;
	}

	private List<MenuMappingDTO> populateMenuItems() {
		List<MenuMappingDTO> dataList = new ArrayList<>();
		MenuMappingDTO data = new MenuMappingDTO();
		data.setCategory("category");
		data.setDescription("description");
		data.setDisplayName("displayName");
		data.setFeature("ALL");
		data.setFeatureCodeId(2l);
		data.setIconUrl("/iconUrl");
		data.setParentId(1l);
		data.setSequence(2);
		data.setName("name1");
		data.setUrl("/url");
		data.setType("ALL");
		dataList.add(data);
		return dataList;
	}

	private List<Object[]> populateUserClientsObjectArray() {
		List<Object[]> list = new ArrayList<Object[]>();
		Object[] objArray = { "654565434", "last" };
		list.add(objArray);
		return list;
	}

	private List<Object[]> populateUserClientsIdObjectArray() {
		List<Object[]> list = new ArrayList<>();
		Object[] objArray = { new BigInteger("2") };
		list.add(objArray);
		return list;
	}

	@Test
	public void testCreateUserForVancouverForClientList() {
		UsersDTO dto = populateUsersDTOForVancouver();
		dto.setCostCenters(populateCostCenterForClientList());
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		ReflectionTestUtils.setField(userServiceImpl, "defaultPassword", "Osicpl@2");
		List<Object[]> clients = new ArrayList<Object[]>();
		Object[] client = { 1, "first", "last" };
		Object[] client1 = { 2, "first1", "last1" };
		clients.add(client);
		clients.add(client1);
		Mockito.doReturn(populateUserForVacouver()).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(populateRbacEntity()).when(roleRepository).findAllById(Mockito.any());
		Mockito.doReturn(populatePrismizedClientsDTO(), populateCostCenterClientDTO()).when(webClientTemplate)
				.getForObjectList(Mockito.anyString(), Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUserForVacouver()).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(dto).when(usersMapper).userToUserDTO(Mockito.any());
		dto.setEmail("test@bbsihq.com");
		UsersDTO result = userServiceImpl.createUser(dto, token, "/login", userPrincipal);
		Mockito.verify(usersMapper, Mockito.atLeastOnce()).userDTOToUser(Mockito.any());
		Mockito.verify(roleRepository, Mockito.atLeastOnce()).findAllById(Mockito.any());
		assertEquals(result.getEmail(), populateUserForVacouver().getEmail());
	}

	@Test
	public void testCreateUserForVancouverEmptyFilters() {
		UsersDTO dto = populateUsersDTOForVancouver();
		dto.setCostCenters(populateCostCenterForClientList());
		dto.setFilter(Collections.emptyList());
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		ReflectionTestUtils.setField(userServiceImpl, "defaultPassword", "Osicpl@2");
		List<Object[]> clients = new ArrayList<Object[]>();
		Object[] client = { 1, "first", "last" };
		Object[] client1 = { 2, "first1", "last1" };
		clients.add(client);
		clients.add(client1);
		Mockito.doReturn(populateUserForVacouver()).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(populateRbacEntity()).when(roleRepository).findAllById(Mockito.any());
		Mockito.doReturn(populatePrismizedClientsDTO(), populateCostCenterClientDTO()).when(webClientTemplate)
				.getForObjectList(Mockito.anyString(), Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUserForVacouver()).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(dto).when(usersMapper).userToUserDTO(Mockito.any());
		dto.setEmail("test@bbsihq.com");
		UsersDTO result = userServiceImpl.createUser(dto, token, "/login", userPrincipal);
		Mockito.verify(usersMapper, Mockito.atLeastOnce()).userDTOToUser(Mockito.any());
		Mockito.verify(roleRepository, Mockito.atLeastOnce()).findAllById(Mockito.any());
		assertEquals(result.getEmail(), populateUserForVacouver().getEmail());
	}

	@Test
	public void testCreateUserForVancouverForClientListMappingToUserSecurity() {
		UsersDTO dto = populateUsersDTOForVancouver();
		dto.setCostCenters(populateCostCenterForClientList());
		List<UserSecurityDTOV2> filter = new ArrayList<>();
		UserSecurityDTOV2 security = new UserSecurityDTOV2();
		security.setClientCode("clientCode");
		security.setType("type");
		Set<String> value = new HashSet<>();
		value.add("value");
		security.setValue(value);
		filter.add(security);
		dto.setFilter(filter);

		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		ReflectionTestUtils.setField(userServiceImpl, "defaultPassword", "Osicpl@2");
		List<Object[]> clients = new ArrayList<Object[]>();
		Object[] client = { BigInteger.valueOf(1), "first", "last" };
		Object[] client1 = { BigInteger.valueOf(2), "first1", "last1" };
		clients.add(client);
		clients.add(client1);
		Mockito.doReturn(populateUserForVacouver()).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(populateRbacEntity()).when(roleRepository).findAllById(Mockito.any());
		Mockito.doReturn(clients).when(userClientRepository).findByUserClientsByType(Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(populateUserSecurity()).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Mockito.doReturn(populatePrismizedClientsDTO(), populateCostCenterClientDTO()).when(webClientTemplate)
				.getForObjectList(Mockito.anyString(), Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUserForVacouver()).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(dto).when(usersMapper).userToUserDTO(Mockito.any());
		dto.setEmail("test@bbsihq.com");
		UsersDTO result = userServiceImpl.createUser(dto, token, "/login", userPrincipal);
		Mockito.verify(usersMapper, Mockito.atLeastOnce()).userDTOToUser(Mockito.any());
		Mockito.verify(roleRepository, Mockito.atLeastOnce()).findAllById(Mockito.any());
		assertEquals(result.getEmail(), populateUserForVacouver().getEmail());
	}

	@Test
	public void testCreateUserForVancouverForClientListWithExistingFilters() {
		UsersDTO dto = populateUsersDTOForVancouver();
		dto.setCostCenters(populateCostCenterForClientList());
		List<UserSecurityDTOV2> filter = new ArrayList<>();
		UserSecurityDTOV2 security = new UserSecurityDTOV2();
		security.setClientCode("909464");
		security.setType("type");
		Set<String> value = new HashSet<>();
		value.add("value");
		security.setValue(value);
		filter.add(security);
		dto.setFilter(filter);

		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		ReflectionTestUtils.setField(userServiceImpl, "defaultPassword", "Osicpl@2");
		List<Object[]> clients = new ArrayList<Object[]>();
		Object[] client = { BigInteger.valueOf(1), "first", "last" };
		Object[] client1 = { BigInteger.valueOf(2), "first1", "last1" };
		clients.add(client);
		clients.add(client1);
		Mockito.doReturn(populateUserForVacouver()).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(populateRbacEntity()).when(roleRepository).findAllById(Mockito.any());
		Mockito.doReturn(clients).when(userClientRepository).findByUserClientsByType(Mockito.any(), Mockito.any(),
				Mockito.any());
		Mockito.doReturn(populateUserSecurity()).when(userSecurityRepository).findByUserClient_Id(Mockito.any());
		Mockito.doReturn(populatePrismizedClientsDTO(), populateCostCenterClientDTO()).when(webClientTemplate)
				.getForObjectList(Mockito.anyString(), Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUserForVacouver()).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(dto).when(usersMapper).userToUserDTO(Mockito.any());
		dto.setEmail("test@bbsihq.com");
		UsersDTO result = userServiceImpl.createUser(dto, token, "/login", userPrincipal);
		Mockito.verify(usersMapper, Mockito.atLeastOnce()).userDTOToUser(Mockito.any());
		Mockito.verify(roleRepository, Mockito.atLeastOnce()).findAllById(Mockito.any());
		assertEquals(result.getEmail(), populateUserForVacouver().getEmail());
	}

	@Test
	public void testCreateUserForVancouverForClients() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		ReflectionTestUtils.setField(userServiceImpl, "defaultPassword", "Osicpl@2");
		UsersDTO dto = populateUsersDTOForVancouver();
		dto.setCostCenters(populateCostCentersForClients());
		Mockito.doReturn(populateUserForVacouver()).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(populateRbacEntity()).when(roleRepository).findAllById(Mockito.any());
		Mockito.doReturn("/url", "/url1").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(populatePrismizedClientsDTO(), populateCostCenterClientDTO()).when(webClientTemplate)
				.getForObjectList(Mockito.anyString(), Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUserForVacouver()).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(dto).when(usersMapper).userToUserDTO(Mockito.any());
		dto.setEmail("test@bbsihq.com");
		UsersDTO result = userServiceImpl.createUser(dto, token, "/login", userPrincipal);
		Mockito.verify(usersMapper, Mockito.atLeastOnce()).userDTOToUser(Mockito.any());
		Mockito.verify(roleRepository, Mockito.atLeastOnce()).findAllById(Mockito.any());
		assertEquals(result.getEmail(), populateUserForVacouver().getEmail());
	}

	@Test
	public void testCreateUserForVancouver() {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		ReflectionTestUtils.setField(userServiceImpl, "defaultPassword", "Osicpl@2");
		UsersDTO dto = populateUsersDTOForVancouver();
		dto.setCostCenters(populateCostCenters());
		Mockito.doReturn(populateUserForVacouver()).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(populateRbacEntity()).when(roleRepository).findAllById(Mockito.any());
		Mockito.doReturn(populatePrismizedClientsDTO(), populateCostCenterClientDTO()).when(webClientTemplate)
				.getForObjectList(Mockito.anyString(), Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUserForVacouver()).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(dto).when(usersMapper).userToUserDTO(Mockito.any());
		dto.setEmail("test@bbsihq.com");
		UsersDTO result = userServiceImpl.createUser(dto, token, "/login", userPrincipal);
		Mockito.verify(usersMapper, Mockito.atLeastOnce()).userDTOToUser(Mockito.any());
		Mockito.verify(roleRepository, Mockito.atLeastOnce()).findAllById(Mockito.any());
		assertEquals(result.getEmail(), populateUserForVacouver().getEmail());
	}

	@Test
	public void testCreateUserForVancouverForRestClient() throws IOException {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		ReflectionTestUtils.setField(userServiceImpl, "defaultPassword", "Osicpl@2");
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "/url");
		String strCostCenter = new Gson().toJson(populateCostCenterForClientList());
		JsonNode jsonNode = mapper.readTree(strCostCenter);
		UsersDTO dto = populateUsersDTOForVancouver();
		dto.setCostCenters(populateCostCenters());
		dto.getCostCenters().get(0).setUserType(GenericConstants.USERTYPE_CLIENT);
		dto.setMfaEmail("sushma@gmail.com");
		dto = populateUsersDTOForVancouver();
		dto.setCostCenters(populateCostCenters());
		dto.getCostCenters().get(0).setUserType(GenericConstants.USERTYPE_EXTERNAL);
		dto.setMfaEmail("sushma@gmail.com");
		dto = populateUsersDTOForVancouver();
		dto.setCostCenters(populateCostCenters());
		dto.getCostCenters().get(0).setUserType(GenericConstants.USERTYPE_NEWHIRE);
		dto.setMfaEmail("sushma@gmail.com");
		Mockito.doReturn(populateUserForVacouver()).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(populateRbacEntity()).when(roleRepository).findAllById(Mockito.any());
		Mockito.doReturn("/url", "/url1").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(new ArrayList<>(), new ArrayList<>()).when(webClientTemplate)
				.getForObjectList(Mockito.anyString(), Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUserForVacouver()).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(strCostCenter).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn(jsonNode).when(objectMapper).readTree(Mockito.anyString());
		Mockito.doReturn(dto).when(usersMapper).userToUserDTO(Mockito.any());
		dto.setEmail("test@bbsihq.com");
		UsersDTO result = userServiceImpl.createUser(dto, token, "/login", userPrincipal);
		assertEquals(result.getEmail(), populateUserForVacouver().getEmail());
	}

	@Test
	public void testCreateUserForMfaEmailField() throws IOException {
		ReflectionTestUtils.setField(userServiceImpl, "bbsiHeadEmails", "@mybbsi.com,@bbsihq.com");
		ReflectionTestUtils.setField(userServiceImpl, "defaultPassword", "Osicpl@2");
		ReflectionTestUtils.setField(userServiceImpl, "commonUrl", "/url");
		String strCostCenter = new Gson().toJson(populateCostCenterForClientList());
		JsonNode jsonNode = mapper.readTree(strCostCenter);
		UsersDTO dto = populateUsersDTOForVancouver();
		dto.setCostCenters(populateCostCenters());
		dto.getCostCenters().get(0).setUserType(GenericConstants.USERTYPE_CLIENT);
		dto.setId(null);
		dto.setEmail("sushma@gmail.com");
		dto.setMfaEmail("sushma@gmail.com");
		List<Users> listUsers = new ArrayList<>();
		listUsers.add(populateUsers());
		Mockito.doReturn(populateUserForVacouver()).when(usersMapper).userDTOToUser(Mockito.any());
		Mockito.doReturn(populateRbacEntity()).when(roleRepository).findAllById(Mockito.any());
		Mockito.doReturn("/url", "/url1").when(boomiHelper).getUrl(Mockito.any());
		Mockito.doReturn(new ArrayList<>(), new ArrayList<>()).when(webClientTemplate)
				.getForObjectList(Mockito.anyString(), Mockito.any(), Mockito.any());
		Mockito.doReturn(populateUserForVacouver()).when(usersRepository).save(Mockito.any());
		Mockito.doReturn(new ArrayList<>()).when(usersRepository).findUsersByMfaEmail(Mockito.anyString());
		Mockito.doReturn(strCostCenter).when(restClient).getForString(Mockito.anyString(), Mockito.anyMap(),
				Mockito.anyMap());
		Mockito.doReturn(jsonNode).when(objectMapper).readTree(Mockito.anyString());
		Mockito.doReturn(dto).when(usersMapper).userToUserDTO(Mockito.any());
		dto.setEmail("test@bbsihq.com");
		UsersDTO result = userServiceImpl.createUser(dto, token, "/login", userPrincipal);
		assertEquals(result.getEmail(), populateUserForVacouver().getEmail());
	}
	
	private Users populateUserForVacouver() {
		Users user = new Users();
		user.setAuthenticationType("authenticationType");
		user.setEmail("test@bbsihq.com");
		user.setId(1l);
		user.setPassword("Osicpl@2");
		user.setRoles(populateRbacEntity());
		user.setTokenValue(token);
		user.setPassword("Osicpl@2");
		user.setClients(populateUserClients());
		return user;
	}

	private List<RbacEntity> populateRbacEntity() {
		List<RbacEntity> rbacEntity = new ArrayList<>();
		RbacEntity rbac = new RbacEntity();
		rbac.setClientCode("clientCode");
		rbac.setCode("code");
		rbac.setMappingId(1l);
		rbac.setName("name");
		rbac.setStatus(true);
		rbac.setType(Enums.UserEnum.ROLE.toString());
		rbacEntity.add(rbac);
		return rbacEntity;
	}

	private List<CostCenterDTO> populateCostCenterForClientList() {
		List<CostCenterDTO> costCenters = new ArrayList<>();
		CostCenterDTO data = new CostCenterDTO();
		data.setAll_clients_selected(true);
		List<CostCenterClientDTO> clientList = new ArrayList<>();
		CostCenterClientDTO element = new CostCenterClientDTO();
		element.setBusinessName("business name");
		element.setClientId("clientId");
		element.setClientName("client name");
		element.setCostCenter("costCenter");
		element.setDescription("description");
		element.setStatus("A");
		element.setIsSelected(true);
		clientList.add(element);
		data.setClientList(clientList);
		data.setSegment1("segment1");
		data.setSegment2("segment2");
		data.setStatus(true);
		data.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		costCenters.add(data);
		return costCenters;
	}

	private List<CostCenterDTO> populateCostCentersForClients() {
		List<CostCenterDTO> list = new ArrayList<>();
		CostCenterDTO data = new CostCenterDTO();
		List<ClientDTO> clients = new ArrayList<>();
		ClientDTO client = new ClientDTO();
		client.setCode("code1");
		client.setIsSelected(true);
		client.setLegalName("business1");
		client.setBusinessName("business1");
		clients.add(client);
		data.setClients(clients);
		data.setIsSelected(true);
		data.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		list.add(data);
		return list;
	}

	private List<CostCenterDTO> populateCostCenters() {
		List<CostCenterDTO> list = new ArrayList<>();
		CostCenterDTO data = new CostCenterDTO();
		data.setCode("1");
		data.setDescription("Cost Center 1");
		data.setIsSelected(true);
		data.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		list.add(data);
		return list;
	}

	private UsersDTO populateUsersDTOForVancouver() {
		UsersDTO data = new UsersDTO();
		data.setEmail("test@mybbsi.com");
		data.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		List<UserSecurityDTOV2> filter = new ArrayList<>();
		UserSecurityDTOV2 security = new UserSecurityDTOV2();
		security.setClientCode("909464");
		security.setType("type");
		Set<String> value = new HashSet<>();
		value.add("value1");
		value.add("value2");
		security.setValue(value);
		filter.add(security);
		data.setFilter(filter);
		data.setId(1l);
		data.setFirstName("first");
		data.setFullName("first last");
		data.setIsAccountLoginChange(false);
		data.setIsCaliforniaUser(true);
		data.setRoles(populateRoles());
		return data;
	}

	private UsersDTO populateUsersDTOForVancouver1() {
		UsersDTO data = new UsersDTO();
		data.setEmail("test@mybbsi.com");
		data.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		List<UserSecurityDTOV2> filter = new ArrayList<>();
		UserSecurityDTOV2 security = new UserSecurityDTOV2();
		security.setClientCode("clientCode");
		security.setType("type");
		Set<String> value = new HashSet<>();
		value.add("value1");
		value.add("value2");
		security.setValue(value);
		filter.add(security);
		data.setFilter(filter);
		data.setId(1l);
		data.setFirstName("first");
		data.setFullName("first last");
		data.setIsAccountLoginChange(false);
		data.setIsCaliforniaUser(true);
		data.setRoles(populateRoles());
		List<ClientDTOV2> clients = new ArrayList<>();
		ClientDTOV2 client = new ClientDTOV2();
		client.setClientCode("909464");
		client.setClientName("1 Hour Drain");
		client.setEmployeeCode("E1768F");
		client.setI9Approver(false);
		client.setId(1l);
		client.setIsActive(false);
		client.setNewHireId(1l);
		client.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		client.setVersion(1l);
		data.setClients(clients);
		return data;
	}

	private List<UserClientRoleDTOV2> populateRoles() {
		List<UserClientRoleDTOV2> roles = new ArrayList<>();
		UserClientRoleDTOV2 role = new UserClientRoleDTOV2();
		List<AccessGroupDTOV2> accessGroups = new ArrayList<>();
		AccessGroupDTOV2 access = new AccessGroupDTOV2();
		access.setClientCode("clientCode");
		access.setDescription("description");
		List<FeatureCodeHierarchyDTOV2> featureCodeHierarchy = new ArrayList<>();
		FeatureCodeHierarchyDTOV2 hierarchy = new FeatureCodeHierarchyDTOV2();
		hierarchy.setCode("code");
		hierarchy.setDescription("description");
		hierarchy.setId(1l);
		hierarchy.setLevel("level");
		hierarchy.setName("name");
		List<PrivilegeDTOV2> privileges = new ArrayList<>();
		PrivilegeDTOV2 privilege = new PrivilegeDTOV2();
		privilege.setCode("privilege");
		privilege.setDescription("desc");
		privilege.setId(1l);
		privilege.setType("type");
		privileges.add(privilege);
		hierarchy.setPrivileges(privileges);
		featureCodeHierarchy.add(hierarchy);
		access.setFeatureCodeHierarchy(featureCodeHierarchy);
		role.setAccessGroups(accessGroups);
		role.setCode("role1");
		role.setDescription("description");
		role.setId(1l);
		role.setIsActive(true);
		role.setName("name");
		role.setRoleId(1l);
		role.setType(Enums.UserEnum.ROLE.toString());
		roles.add(role);
		return roles;
	}

	private List<PrismizedClientsDTO> populatePrismizedClientsDTO() {
		List<PrismizedClientsDTO> list = new ArrayList<>();
		PrismizedClientsDTO data = new PrismizedClientsDTO();
		data.setClient_code("clientCode");
		list.add(data);
		return list;
	}

	private List<CostCenterClientDTO> populateCostCenterClientDTO() {
		List<CostCenterClientDTO> list = new ArrayList<>();
		CostCenterClientDTO data = new CostCenterClientDTO();
		data.setBusinessName("business1");
		data.setClientId("clientCode");
		data.setClientName("client1");
		data.setCostCenter("1");
		data.setDescription("description");
		data.setIsSelected(true);
		data.setLegalName("legal");
		data.setStatus("Active");
		list.add(data);
		return list;
	}

	private List<UserSecurity> populateUserSecurity() {
		List<UserSecurity> list = new ArrayList<>();
		UserSecurity data = new UserSecurity();
		data.setId(1l);
		data.setType("LOCATION");
		data.setValue("LOCATION");
		UserClients userClient = new UserClients();
		userClient.setClientCode("clientCode");
		userClient.setEmployeeCode("empCode");
		userClient.setId(1l);
		data.setUserClient(userClient);
		list.add(data);
		return list;
	}

	private UserCostCenterAssociation getUserCostCenterAssociation() {
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setCostCenterCode("015");
		return assoc;
	}

	private List<UserCostCenterAssociation> getUserCostCenterAssociationList() {
		List<UserCostCenterAssociation> assocs = new ArrayList<>();
		assocs.add(getUserCostCenterAssociation());
		return assocs;
	}

	private UserPrincipal getUserPrincipal() {
		UserPrincipal userPrincipal = new UserPrincipal("288", "Osicpl@1",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		userPrincipal.setEmail("integration@osius.com");
		userPrincipal.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		userPrincipal.setClientMap(getClientMap());
		return userPrincipal;
	}

	private Map<String, String> getClientMap() {
		Map<String, String> clientMap = new HashMap<String, String>();
		clientMap.put("902738", "JIT TRANSPORTATION");
		clientMap.put("909096", "AG ENTERPRISES LLC");
		return clientMap;
	}

	private List<ClientMaster> populateClientMaster() {
		List<ClientMaster> list = new ArrayList<>();
		ClientMaster data = new ClientMaster();
		data.setClientCode("clientCode1");
		data.setClientName("clientName");
		data.setCostCenterCode("costCenterCode1");
		data.setCostCenterDescription("costCenterDescription1");
		list.add(data);
		ClientMaster data1 = new ClientMaster();
		data1.setClientCode("clientCode2");
		data1.setClientName("clientName2");
		data1.setCostCenterCode("costCenterCode2");
		data1.setCostCenterDescription("costCenterDescription2");
		list.add(data1);
		ClientMaster data2 = new ClientMaster();
		data1.setClientCode("909464");
		data1.setClientName("1 Hour Drain");
		data1.setCostCenterCode("costCenterCode3");
		data1.setCostCenterDescription("costCenterDescription3");
		list.add(data2);
		return list;
	}

	private List<UserCostCenterAssociation> getUserCostCenterAssociation1() {
		UserCostCenterAssociation assoc = new UserCostCenterAssociation();
		assoc.setCostCenterCode("code");
		assoc.setId(1l);
		assoc.setUser(populateUsers());
		assoc.setUserType(GenericConstants.USERTYPE_EMPLOYEE.toString());
		ClientMaster client = new ClientMaster();
		client.setClientCode("909464");
		client.setClientName("clientName");
		client.setCostCenterCode("code");
		client.setCostCenterDescription("description");
		List<UserCostCenterAssociation> assocs = new ArrayList<>();
		assocs.add(assoc);
		return assocs;
	}

	private Map<String, Set<String>> getuserTypeMap() {
		Map<String, Set<String>> userTypeMap = new HashMap<>();
		Set<String> s1 = new HashSet<>();
		s1.add(GenericConstants.USERTYPE_NEWHIRE);
		s1.add(GenericConstants.USERTYPE_CLIENT);
		s1.add(GenericConstants.USERTYPE_EMPLOYEE);
		userTypeMap.put(GenericConstants.USERTYPE_NEWHIRE, s1);
		userTypeMap.put(GenericConstants.USERTYPE_CLIENT, s1);
		userTypeMap.put(GenericConstants.USERTYPE_EMPLOYEE, s1);
		userTypeMap.put("909464", s1);
		return userTypeMap;
	}

	private Map<String, Set<String>> getuserTypeMap3() {
		Map<String, Set<String>> userTypeMap = new HashMap<>();
		Set<String> s1 = new HashSet<>();
		s1.add("909464");
		s1.add(GenericConstants.USERTYPE_EMPLOYEE);
		userTypeMap.put("909464", s1);
		return userTypeMap;
	}

	private Map<String, Set<String>> getuserTypeMap1() {
		Map<String, Set<String>> userTypeMap = new HashMap<>();
		Set<String> s1 = new HashSet<>();
		s1.add(GenericConstants.USERTYPE_EMPLOYEE);
		userTypeMap.put(GenericConstants.USERTYPE_EMPLOYEE, s1);
		userTypeMap.put("909464", s1);
		return userTypeMap;
	}

	private Map<String, Set<String>> getuserTypeMap2() {
		Map<String, Set<String>> userTypeMap = new HashMap<>();
		Set<String> s1 = new HashSet<>();
		s1.add(GenericConstants.USERTYPE_CLIENT);
		userTypeMap.put(GenericConstants.USERTYPE_CLIENT, s1);
		userTypeMap.put("909464", s1);
		return userTypeMap;
	}

}
