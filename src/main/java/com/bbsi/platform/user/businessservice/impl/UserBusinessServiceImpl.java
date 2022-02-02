package com.bbsi.platform.user.businessservice.impl;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.businessservice.intf.IntegrationBusinessService;
import com.bbsi.platform.common.client.dto.UserToolbarSettingsDTO;
import com.bbsi.platform.common.dto.BulkUploadMailDTO;
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
import com.bbsi.platform.common.enums.Enums.NotifyTo;
import com.bbsi.platform.common.enums.Enums.UserEnum;
import com.bbsi.platform.common.enums.NotificationEventEnum;
import com.bbsi.platform.common.generic.AuditDetailsUtil;
import com.bbsi.platform.common.generic.BoomiHelper;
import com.bbsi.platform.common.generic.CommonUtilities;
import com.bbsi.platform.common.generic.EncryptAndDecryptUtil;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.GenericUtils;
import com.bbsi.platform.common.generic.JsonUtils;
import com.bbsi.platform.common.generic.JwtTokenUtil;
import com.bbsi.platform.common.generic.LogUtils;
import com.bbsi.platform.common.generic.RestClient;
import com.bbsi.platform.common.generic.WebClientTemplate;
import com.bbsi.platform.common.integration.dto.LocationInfo;
import com.bbsi.platform.common.user.dto.MenuMappingDTO;
import com.bbsi.platform.common.user.dto.RbacDTO;
import com.bbsi.platform.common.user.dto.TimenetLoginInfo;
import com.bbsi.platform.common.user.dto.UserCredentials;
import com.bbsi.platform.common.user.dto.UserInvitationDTO;
import com.bbsi.platform.common.user.dto.UserMfaDTO;
import com.bbsi.platform.common.user.dto.UsersDTO;
import com.bbsi.platform.common.user.dto.v2.ClientDTOV2;
import com.bbsi.platform.common.user.dto.v2.ClientRoleDTOV2;
import com.bbsi.platform.common.user.dto.v2.PolicyAcceptedDTO;
import com.bbsi.platform.common.user.dto.v2.UserClientRoleDTOV2;
import com.bbsi.platform.common.user.dto.v2.UserCustomDTOV2;
import com.bbsi.platform.common.user.dto.v2.UserExistDTO;
import com.bbsi.platform.common.user.dto.v2.UserSecurityDTOV2;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.exception.business.InvalidTokenException;
import com.bbsi.platform.exception.business.InvalidUserException;
import com.bbsi.platform.exception.business.UnauthorizedAccessException;
import com.bbsi.platform.exception.business.ValidationException;
import com.bbsi.platform.exception.enums.ErrorCodes;
import com.bbsi.platform.exception.model.ErrorDetails;
import com.bbsi.platform.oauth.config.CustomUserDetailsService;
import com.bbsi.platform.user.businessservice.intf.EmployeeInvitationBusinessService;
import com.bbsi.platform.user.businessservice.intf.MappingBusinessService;
import com.bbsi.platform.user.businessservice.intf.MenuBusinessService;
import com.bbsi.platform.user.businessservice.intf.UserBusinessService;
import com.bbsi.platform.user.mapper.UserToolbarSettingsMapperV2;
import com.bbsi.platform.user.mapper.UsersMapper;
import com.bbsi.platform.user.model.AccessTermination;
import com.bbsi.platform.user.model.ClientMaster;
import com.bbsi.platform.user.model.ClientRole;
import com.bbsi.platform.user.model.Mapping;
import com.bbsi.platform.user.model.RbacEntity;
import com.bbsi.platform.user.model.UserClients;
import com.bbsi.platform.user.model.UserCostCenterAssociation;
import com.bbsi.platform.user.model.UserSecurity;
import com.bbsi.platform.user.model.UserToolbarSettings;
import com.bbsi.platform.user.model.Users;
import com.bbsi.platform.user.repository.AccessTerminationRepository;
import com.bbsi.platform.user.repository.ClientMasterRepository;
import com.bbsi.platform.user.repository.ClientRoleRepository;
import com.bbsi.platform.user.repository.EmployeeInvitationRepository;
import com.bbsi.platform.user.repository.MappingRepository;
import com.bbsi.platform.user.repository.PrivilegeRepository;
import com.bbsi.platform.user.repository.RbacRepository;
import com.bbsi.platform.user.repository.ResetPasswordRequestRepository;
import com.bbsi.platform.user.repository.UserClientsRepository;
import com.bbsi.platform.user.repository.UserCostCenterAssociationRepository;
import com.bbsi.platform.user.repository.UserFilterRepository;
import com.bbsi.platform.user.repository.UserToolbarSettingsRepositoryV2;
import com.bbsi.platform.user.repository.UsersRepository;
import com.bbsi.platform.user.utils.MethodNames;
import com.bbsi.platform.user.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


/**
 * @author veethakota
 *
 */

@Logging
@Service(value = "userService")
public class UserBusinessServiceImpl implements UserBusinessService, CustomUserDetailsService {

	private static final String EMAIL = "email";

	private static final String MFA_EMAIL = "mfa_email";

	private static final String PHONE_NUMBER = "phone_number";

	@Value("${common.integration.get.url}")
	private String commonUrl;

	@Value("${user.default.password}")
	private String defaultPassword;
	
	@Value("${user.default.mfaDate}")
	private String mfaDate;

	@Value("${notification.email.url}")
	private String emailURL;

	@Value("${emp.code.by.email.and.clientcode}")
	private String getEmployeeCodeUrl;

	@Value("${get.integration.v1.client.info}")
	private String clientInfo;

	@Value("${employee.integration.empinfo}")
	private String employeePersonalIntegrationUrl;

	@Value("${terminated.clients.url}")
	private String terminatedClientsUrl;

	@Value("${notification.mfa.verify}")
	private String verifyOtpURL;

	@Value("${bbsi.head.emails}")
	private String bbsiHeadEmails;
	
	@Value("${oauth.access.token.validity}")
	private long accessTokenValidity;

	@Value("${ad.keys.url}")
	private String adKeysURL;
	
	@Value("${newhire.check.service.url}")
	private String existNewhireUrl;
	
	@Value("${check.emp.eligibility.clientcode.and.empcode}")
	private String getEmpEligibilityUrl;

	private static Map<String, String> adPublicKeys = Maps.newHashMap();

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private WebClientTemplate webClientTemplate;

	@Autowired
	private UserToolbarSettingsRepositoryV2 userToolbarSettingsRepositoryV2;

	@Autowired
	private UserFilterRepository userSecurityRepository;

	@Autowired
	private UserClientsRepository userClientRepository;
	
	@Autowired
	private UserCostCenterAssociationRepository userCostCenterAssociationRepository;

	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private ClientRoleRepository clientRoleRepository;

	@Autowired
	private RbacRepository roleRepository;

	@Autowired
	private MappingRepository mappingRepository;

	@Autowired
	private UsersRepository userRepository;

	@Autowired
	private UserToolbarSettingsMapperV2 userToolbarSettingsMapperV2;

	@Autowired
	private UsersMapper userMapper;

	@Autowired
	private MappingBusinessService mappingService;

	@Autowired
	private MenuBusinessService menuBusinessService;

	@Autowired
	private EmployeeInvitationBusinessService employeeInvitationBusinessService;

	@Autowired
	private PasswordBusinessServiceImpl passwordBusinessServiceImpl;

	@Autowired
	private IntegrationBusinessService integrationBusinessService;

	@Autowired
	private RestClient restClient;

	@Autowired
	private BoomiHelper boomiHelper;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private final String PERSONAL_EMAIL_PATH = "personal.work_email_address";

	private final String CONTACT_INFO_EMAIL_ADDRESS = "contact_information.personal_email_address";

	private final String CONTACT_INFO_MOBILE_NUMBER = "contact_information.mobile_phone_number";

	private final String EMPLOYEE_STATUS = "personal.employee_status";

	private static final String PRIVILEGE_CODE_NEW_HIRE_I9_APPROVER_F_ALL = "NEW_HIRE_I9_APPROVER_F.ALL";

	private static final String PRIVILEGE_CODE_NEW_HIRE_I9_APPROVER_F_VIEW = "NEW_HIRE_I9_APPROVER_F.VIEW";
	
	private static final String EMP_MGMT_VIEW = "EMP_MGMT.VIEW";

	private static final String EMP_MGMT_ALL = "EMP_MGMT.ALL";
	
	private static final String PAY_TS_INF_VIEW = "PAY_TS_INF.VIEW";

	private static final String PAY_TS_INF_ALL = "PAY_TS_INF.ALL";

	private static final String FORMAT = "format";

	private static final String STATE = "state";

	private static final String UPDATE_CALIFORNIA_FLAG = "updateCaliforniaFlag";

	private static final String WORK_SITE_LOCATION = "work_site_location";
	
	private static final String COST_CENTERS_MSG = "Cost Centers: %s";
	
	private static final String INVALID_PAYLOAD_MSG = "Invalid Request Payload!";
	
	private static final String EXTEN_HQ = "@bbsihq.com";
	
	private static final String ROLE_USER  = "ROLE_USER";
	
	private static final String ROLE_ADMIN  = "ADMIN";
	
	private static final String MOBILE_PHONE_NUMBER = "mobile_phone_number";
	
	private static final String CONTACT_INFORMATION = "contact_information";

	@Autowired
	private AuditDetailsUtil auditDetailsUtil;
	
	@Autowired
	private CommonUtilities commonUtilities;
	
	@Autowired
	private EncryptAndDecryptUtil encryptAndDecryptUtil;
	
	@Autowired
	private EmployeeInvitationRepository employeeInvitationRepository;

	@Value("${user.token.url}")
	private String defaultTokenUrl;
	
	@Autowired
	private AccessTerminationRepository accessTerminationRepository;
	
	@Autowired
	private ResetPasswordRequestRepository resetPasswordRequestRepository;
	
	
	/**
	 * This method is used to load the users by username and also provide the
	 * user details by roles
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserPrincipal userDetails = getUserPrincipal(userName);
		return userDetails;
	}

	/**
	 * Method to update the invalid login attempts
	 *
	 * @param email
	 */
	@Override
	@Transactional
	public Integer updateFailAttempts(String email) {
		try {
			Object[] response = userRepository.incrementInvalidAttempts(email);
			if (null != response && response.length > 0) {
				return Integer.parseInt(response[0].toString());
			}
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_USER),
					exception, "Error occured while updateFailAttempts");
		}
		return 0;
	}

	/**
	 * Method to reset the invalid login attempts to zero
	 *
	 * @param email
	 */
	@Override
	@Transactional
	@Async
	public void resetFailAttempts(String email) {
		try {
			userRepository.clearInvalidAttempts(email);
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "updateFailAttempts"),
					exception, "Error occured while resetFailAttempts");
		}
	}

	@Override
	@Transactional
	public UsersDTO createUser(UsersDTO userDTO, String oauthToken, String uiUrl, UserPrincipal userPrincipal) {
		Boolean isAccountLoginChange = userDTO.getIsAccountLoginChange();
		try {
			String emailError = "The Email Address should be in" + " " + bbsiHeadEmails + " " + FORMAT;
			String otherEmailError = "The Email Address should not be in" + " " + bbsiHeadEmails + " " + FORMAT;
			String exten[] = bbsiHeadEmails.split(",");

			if (!GenericUtils.validateEmail(userDTO.getEmail().trim().toLowerCase())) {
				throw new ValidationException(new ErrorDetails(ErrorCodes.V006));
			}
			List<String> newClients = Lists.newArrayList();
			userDTO.setEmail(userDTO.getEmail().trim().toLowerCase());
			if(userDTO.getMfaEmail() != null) {
				userDTO.setMfaEmail(userDTO.getMfaEmail().trim().toLowerCase());
			}
			String userType = "";
			if (CollectionUtils.isNotEmpty(userDTO.getClients())) {
				userType = userDTO.getClients().get(0).getUserType();
				validateUserDTO(userDTO, userPrincipal.getClientCode(), emailError, otherEmailError, exten);
				userDTO.getClients().forEach(clientDTO -> {
					newClients.add(clientDTO.getClientCode());
					if (StringUtils.isEmpty(clientDTO.getCostCenterCode()) && userPrincipal != null
							&& StringUtils.isNotEmpty(userPrincipal.getCostCenterCode())) {
						clientDTO.setCostCenterCode(userPrincipal.getCostCenterCode());
					}
					if (StringUtils.isEmpty(clientDTO.getCostCenterDescription()) && userPrincipal != null
							&& StringUtils.isNotEmpty(userPrincipal.getBbsiBranch())) {
						clientDTO.setCostCenterDescription(userPrincipal.getBbsiBranch());
					}
					if (StringUtils.isNotEmpty(clientDTO.getUserType())
							&& clientDTO.getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE)
							&& StringUtils.isNotEmpty(clientDTO.getEmployeeCode())) {
						UserClients userClients = userClientRepository.findByClientCodeAndEmployeeCode(
								clientDTO.getClientCode(), clientDTO.getEmployeeCode());
						if (userClients != null
								&& !userClients.getUserType().equals(GenericConstants.USERTYPE_NEWHIRE)) {
							throw new ValidationException(new ErrorDetails(ErrorCodes.UR0015));
						}
					}
				});
				if (userDTO.getClients().size() == 1
						&& StringUtils.isNotEmpty(userDTO.getClients().get(0).getUserType())
						&& userDTO.getClients().get(0).getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE)
						&& StringUtils.isNotEmpty(userDTO.getClients().get(0).getEmployeeCode())) {
					UserClients userClients = userClientRepository.findByClientCodeAndEmployeeCode(
							userDTO.getClients().get(0).getClientCode(), userDTO.getClients().get(0).getEmployeeCode());
					if (userClients != null && userClients.getUserType().equals(GenericConstants.USERTYPE_NEWHIRE)) {
						userClients.setUserType(GenericConstants.USERTYPE_EMPLOYEE);
						userClients.setNewHireId(0);

						setRoleForEmployee(userClients);

						userClientRepository.save(userClients);
						addEmployeeInvitation(userDTO, userPrincipal);
						initiateEmail(userDTO, oauthToken, uiUrl, GenericConstants.REINITIATE_DOCS_VM, false,
								userPrincipal);
						return userDTO;

					}
				}

			} else if (CollectionUtils.isNotEmpty(userDTO.getCostCenters())
					&& !Utils.checkIfEmailHasExtension(userDTO.getEmail(), new String[] {EXTEN_HQ})) {
				ErrorDetails errorDetails = new ErrorDetails(ErrorCodes.UR008, emailError);
				errorDetails.setErrorMessage(emailError);
				throw new UnauthorizedAccessException(errorDetails);
			} else if (CollectionUtils.isNotEmpty(userDTO.getCostCenters())) {
				userType = userDTO.getCostCenters().get(0).getUserType();
			}
			verifyUserType(userPrincipal, userType);

			// mfa email, mandatory any one field, user type Client/External/Exployee/NewHire
			validateMfaEmail(userType, userDTO, otherEmailError, exten);

			if (CollectionUtils.isNotEmpty(userDTO.getFilter())) {
				Map<String, Set<String>> superUserFilters = getUserFilter(userPrincipal.getEmail(),
						userPrincipal.getClientCode(), userPrincipal);
				for (UserSecurityDTOV2 userFilter : userDTO.getFilter()) {
					if (superUserFilters.containsKey(userFilter.getType())
							&& !superUserFilters.get(userFilter.getType()).containsAll(userFilter.getValue())) {
						throw new UnauthorizedAccessException(
								new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
					}
				}
			}
			Users dbUser = userRepository.findByEmail(userDTO.getEmail());
			List<UserSecurityDTOV2> userFilters = userDTO.getFilter();
			Users newUser = null;
			if (null != dbUser && StringUtils.isNotEmpty(dbUser.getEmail())) {
				dbUser.setEmail(userDTO.getEmail());
				String oldDataString = buildAuditObj(userType, userPrincipal, dbUser);

				mergeUserTypes(dbUser, userDTO);
				validateAndAssignRoles(dbUser, userDTO);
				newUser = userRepository.save(dbUser);
				userDTO.setMfaEmail(dbUser.getMfaEmail());
				if (CollectionUtils.isNotEmpty(userDTO.getFilter())) {

					for (UserClients userClients : newUser.getClients()) {
						if (userClients.getClientCode().equals(userPrincipal.getClientCode())
								&& userClients.getUserType().equals(userType)) {
							userSecurityRepository.deleteByUserClient_Id(userClients.getId());
							userClients.setUserFilters(Lists.newArrayList());
						}
						for (UserSecurityDTOV2 userFilter : userDTO.getFilter()) {
							if (StringUtils.isNotEmpty(userFilter.getClientCode())
									&& userFilter.getClientCode().equals(userClients.getClientCode())
									&& CollectionUtils.isNotEmpty(userFilter.getValue())
									&& (userClients.getUserType().equals(GenericConstants.USERTYPE_CLIENT)
									|| userClients.getUserType().equals(GenericConstants.USERTYPE_EXTERNAL))) {
								for (String value : userFilter.getValue()) {
									UserSecurity userSecurity = new UserSecurity();
									userSecurity.setType(userFilter.getType());
									userSecurity.setValue(value);
									userSecurity.setUserClient(userClients);
									userClients.getUserFilters().add(userSecurity);
								}
								/* update security filters */
								List<UserSecurity> finalFilters = getSuperUserFilters(userClients.getUserFilters(),
										userPrincipal.getEmail(), userClients.getClientCode(), userPrincipal);
								if (CollectionUtils.isNotEmpty(finalFilters)) {
									for (UserSecurity security : finalFilters) {
										security.setUserClient(userClients);
									}
									userClients.setUserFilters(finalFilters);
									userSecurityRepository.saveAll(userClients.getUserFilters());
								}
							}
						}
					}
				} else {
					for (UserClients userClient : newUser.getClients()) {
						/* update security filters */
						if (newClients.contains(userClient.getClientCode())
								&& userClient.getUserType().equalsIgnoreCase(userType)) {
							userClient.setUserFilters(Lists.newArrayList());
							List<UserSecurity> finalFilters = getSuperUserFilters(userClient.getUserFilters(),
									userPrincipal.getEmail(), userClient.getClientCode(), userPrincipal);
							if (CollectionUtils.isNotEmpty(finalFilters)) {
								userClient.setUserFilters(finalFilters);
								userSecurityRepository.saveAll(userClient.getUserFilters());
							}
						}
					}
				}

				// initiate email
				if (newUser.getIsFirstLogin()) {
					initiateEmail(userDTO, oauthToken, uiUrl, GenericConstants.USER_LOGIN_VM, true, userPrincipal);
				} else {
					initiateEmail(userDTO, oauthToken, uiUrl, GenericConstants.REINITIATE_DOCS_VM, false,
							userPrincipal);
				}

				RbacDTO entity = new RbacDTO();
				entity.setCreatedBy(userPrincipal.getEmail());
				entity.setCreatedOn(LocalDateTime.now());
				entity.setModifiedOn(LocalDateTime.now());
				entity.setModifiedBy(userPrincipal.getEmail());

				addEmployeeInvitation(userDTO,userPrincipal);
//				Audit for update
				String newDataString = buildAuditObj(userType, userPrincipal, newUser);				

				auditDetailsUtil.buildAuditDetailsAndSave(Enums.AuditDetailsSourceEntityEnum.USER.toString(), EMAIL,
						userDTO.getEmail(), null, Enums.AuditDetailsSourceEntityEnum.USER.toString(), userPrincipal, oldDataString,
						newDataString, Enums.AuditDetailsEventEnum.UPDATE.toString(), entity);
			} else {
				String firstName = userDTO.getFirstName();
				String lastName = userDTO.getLastName();
				newUser = userMapper.userDTOToUser(userDTO);
				newUser.setPassword(passwordEncoder.encode(defaultPassword));
				validateAndAssignRoles(newUser, userDTO);
				newUser.setClients(Lists.newArrayList());
				buildUserTypes(newUser, userDTO, false, userPrincipal.getClientCode());
				assignIsPrimaryFlag(newUser);
				newUser.setIsFirstLogin(Boolean.TRUE);
				newUser = userRepository.save(newUser);
				if (CollectionUtils.isNotEmpty(userDTO.getFilter())) {

					for (UserClients userClients : newUser.getClients()) {
						userClients.setUserFilters(Lists.newArrayList());
						for (UserSecurityDTOV2 userFilter : userDTO.getFilter()) {
							if (StringUtils.isNotEmpty(userFilter.getClientCode())
									&& userFilter.getClientCode().equals(userClients.getClientCode())
									&& CollectionUtils.isNotEmpty(userFilter.getValue())) {
								for (String value : userFilter.getValue()) {
									UserSecurity userSecurity = new UserSecurity();
									userSecurity.setType(userFilter.getType());
									userSecurity.setValue(value);
									userSecurity.setUserClient(userClients);
									userClients.getUserFilters().add(userSecurity);
								}
							}
						}

						/* update security filters */
						List<UserSecurity> finalFilters = getSuperUserFilters(userClients.getUserFilters(),
								userPrincipal.getEmail(), userClients.getClientCode(), userPrincipal);
						if (CollectionUtils.isNotEmpty(finalFilters)) {
							for (UserSecurity security : finalFilters) {
								security.setUserClient(userClients);
							}
							userClients.setUserFilters(finalFilters);
							userSecurityRepository.saveAll(userClients.getUserFilters());
						}

					}
				} else {

					if (CollectionUtils.isNotEmpty(newUser.getClients()) &&
							!GenericConstants.USERTYPE_VANCOUVER.equalsIgnoreCase(newUser.getClients().get(0).getUserType())
							&& !GenericConstants.USERTYPE_BRANCH
							.equalsIgnoreCase(newUser.getClients().get(0).getUserType())
							&& !GenericConstants.USERTYPE_EMPLOYEE
							.equalsIgnoreCase(newUser.getClients().get(0).getUserType())) {
						for (UserClients userClient : newUser.getClients()) {
							userClient.setUserFilters(Lists.newArrayList());
							/* update security filters */
							List<UserSecurity> finalFilters = getSuperUserFilters(userClient.getUserFilters(),
									userPrincipal.getEmail(), userClient.getClientCode(), userPrincipal);
							if (CollectionUtils.isNotEmpty(finalFilters)) {
								userClient.setUserFilters(finalFilters);
								userSecurityRepository.saveAll(userClient.getUserFilters());
							}

						}

					}

				}
				boolean isElectronic = userDTO.getIsElectronicOnboard();
				List<CostCenterDTO> costCenters = userDTO.getCostCenters();
				userDTO = userMapper.userToUserDTO(newUser);
				userDTO.setIsAccountLoginChange(isAccountLoginChange);
				userDTO.setIsElectronicOnboard(isElectronic);
				userDTO.setCostCenters(costCenters);
				userDTO.setFilter(userFilters);
				userDTO.setFirstName(firstName);
				userDTO.setLastName(lastName);

				// initiate email for new user.
				if (!isAccountLoginChange) {
					initiateEmail(userDTO, oauthToken, uiUrl, GenericConstants.USER_LOGIN_VM, true, userPrincipal);
				}
//				Audit For insert
				RbacDTO entity = new RbacDTO();
				entity.setCreatedBy(userPrincipal.getEmail());
				entity.setCreatedOn(LocalDateTime.now());
				entity.setModifiedOn(LocalDateTime.now());
				entity.setModifiedBy(userPrincipal.getEmail());

				addEmployeeInvitation(userDTO,userPrincipal);
				String newDataString = buildAuditObj(userType, userPrincipal, newUser);

				auditDetailsUtil.buildAuditDetailsAndSave(Enums.AuditDetailsSourceEntityEnum.USER.toString(), EMAIL,
						userDTO.getEmail(), null, Enums.AuditDetailsSourceEntityEnum.USER.toString(), userPrincipal, null,
						newDataString, Enums.AuditDetailsEventEnum.INSERT.toString(), entity);
			}

		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_USER), e,
					"Error occurred during user creation");
		}
		return userDTO;
	}

	private void updateEmployeeConfig(Users dbUser, String mfaEmail, String token) {
		List<UserClients> userClients = dbUser.getClients();
		if(CollectionUtils.isNotEmpty(userClients)) {
			for(UserClients userClient : userClients) {
				if(GenericConstants.USERTYPE_EMPLOYEE.equalsIgnoreCase(userClient.getUserType())) {
					invokeEmpEligibility(userClient.getClientCode(), userClient.getEmployeeCode(), mfaEmail, token);
				}
			}
		}
	}

	private void validateMfaEmail(String userType, UsersDTO userDTO, String otherEmailError, String[] exten) {
		if(((userType.equals(GenericConstants.USERTYPE_CLIENT)
				|| userType.equals(GenericConstants.USERTYPE_EXTERNAL)
				|| userType.equals(GenericConstants.USERTYPE_EMPLOYEE)
				|| userType.equals(GenericConstants.USERTYPE_NEWHIRE)))) {
			if(userDTO.getIsAccountLoginChange()) {
				if (!GenericUtils.validateEmail(userDTO.getEmail().trim().toLowerCase())) {
					throw new ValidationException(new ErrorDetails(ErrorCodes.V006));
				}
				if (Utils.checkIfEmailHasExtension(userDTO.getEmail(), exten)) {
					ErrorDetails errorDetails = new ErrorDetails(ErrorCodes.UR008, otherEmailError);
					errorDetails.setErrorMessage(otherEmailError);
					throw new UnauthorizedAccessException(errorDetails);
				}
				validateEmailWithMfaEmail(userDTO.getEmail(), userDTO.getClients().get(0).getClientCode(), userDTO.getId(), userType);
			} else if(userDTO.getMfaEmail() != null && StringUtils.isNotEmpty(userDTO.getMfaEmail())) {
				if (!GenericUtils.validateEmail(userDTO.getMfaEmail().trim().toLowerCase())) {
					throw new ValidationException(new ErrorDetails(ErrorCodes.V006));
				}
				if (userDTO.getEmail().equalsIgnoreCase(userDTO.getMfaEmail())) {
					throw new ValidationException(new ErrorDetails(ErrorCodes.UR0017));
				}
				if (Utils.checkIfEmailHasExtension(userDTO.getMfaEmail(), exten)) {
					ErrorDetails errorDetails = new ErrorDetails(ErrorCodes.UR008, otherEmailError);
					errorDetails.setErrorMessage(otherEmailError);
					throw new UnauthorizedAccessException(errorDetails);
				}
				validateUniqueMfaEmail(userDTO.getEmail(), userDTO.getMfaEmail(), userDTO.getId());
				validateEmailWithMfaEmail(userDTO.getEmail(), userDTO.getClients().get(0).getClientCode(), userDTO.getId(), userType);
			} else if(StringUtils.isEmpty(userDTO.getMobile()) && !userType.equals(GenericConstants.USERTYPE_NEWHIRE)  && !userType.equals(GenericConstants.USERTYPE_EMPLOYEE)) {
				throw new ValidationException(new ErrorDetails(ErrorCodes.UR0019));
			}
		}
	}
	
	private void validateUniqueMfaEmail(String email, String mfaEmail, Long id) {
		if (StringUtils.isEmpty(mfaEmail)) {
			return;
		}
		List<Users> users = userRepository.findByMfaEmail(mfaEmail.trim().toLowerCase());
		if ((id == null || id.equals(0l)) && (CollectionUtils.isNotEmpty(users)
				&& (users.size() > 1 || (users.size() == 1 && !users.get(0).getEmail().equalsIgnoreCase(email))))) {
			throw new ValidationException(new ErrorDetails(ErrorCodes.UR0018));
		} else if (id != null && id > 0l && CollectionUtils.isNotEmpty(users)
				&& (users.size() > 1 || !users.get(0).getId().equals(id))) {
			throw new ValidationException(new ErrorDetails(ErrorCodes.UR0018));
		}
		Users user = userRepository.findByEmail(mfaEmail.trim().toLowerCase());
		if (user != null) {
			throw new ValidationException(new ErrorDetails(ErrorCodes.UR0018));
		}
	}
	
	private void validateChangeLoginMfaEmail(String email, String mfaEmail) {
		if (StringUtils.isEmpty(mfaEmail)) {
			return;
		}
		if (email.equalsIgnoreCase(mfaEmail)) {
			throw new ValidationException(new ErrorDetails(ErrorCodes.UR0024));
		}
		
		List<Users> users = userRepository.findByMfaEmail(mfaEmail.trim().toLowerCase());
		if (CollectionUtils.isNotEmpty(users)
				&& users.size() > 1) {
			throw new ValidationException(new ErrorDetails(ErrorCodes.UR0028));
		} else if (CollectionUtils.isNotEmpty(users)
				&& users.size() == 1 && !users.get(0).getEmail().equalsIgnoreCase(email)) {
			throw new ValidationException(new ErrorDetails(ErrorCodes.UR0028));
		}
	}

	private void validateEmailWithMfaEmail(String email, String clientCode, Long id, String userType) {
		if(StringUtils.isEmpty(email)) {
			return;
		}
		List<Object[]> users = userRepository.findUsersByMfaEmail(email.trim().toLowerCase());
		if(CollectionUtils.isNotEmpty(users)) {
			throw new ValidationException(new ErrorDetails(ErrorCodes.UR0022));
		}

		if(GenericConstants.USERTYPE_EMPLOYEE.equalsIgnoreCase(userType)) {
			List<Object[]> empUsers = userRepository.getUserDataByEmail(email.trim().toLowerCase(), clientCode);
			if (CollectionUtils.isNotEmpty(empUsers)) {
				for (Object[] user : empUsers) {
					if (user.length > 5 && StringUtils.isNotEmpty((String) user[5])
							&& user[5].equals(GenericConstants.USERTYPE_EMPLOYEE)
							&& (id == null || (BigInteger.valueOf(id).compareTo((BigInteger) user[9]) != 0))) {
						throw new ValidationException(new ErrorDetails(ErrorCodes.UR0029));
					}
				}
			}
		}
	}
	
	private void validateMfaEmail(String userType, UserMfaDTO userMfaDTO, String otherEmailError, String[] exten) {
		if(((userType.equals(GenericConstants.USERTYPE_CLIENT)
				|| userType.equals(GenericConstants.USERTYPE_EXTERNAL)
				|| userType.equals(GenericConstants.USERTYPE_EMPLOYEE)
				|| userType.equals(GenericConstants.USERTYPE_NEWHIRE)))) {
			if(StringUtils.isNotEmpty(userMfaDTO.getMfaEmail())) {
				if (!GenericUtils.validateEmail(userMfaDTO.getMfaEmail().trim().toLowerCase())) {
					throw new ValidationException(new ErrorDetails(ErrorCodes.V006));
				}
				if (userMfaDTO.getMfaEmail().equalsIgnoreCase(userMfaDTO.getEmail())) {
					if(userMfaDTO.getIsConfactInfoUpdate()) {
						throw new ValidationException(new ErrorDetails(ErrorCodes.UR0020));
					} else {
						throw new ValidationException(new ErrorDetails(ErrorCodes.UR0021));
					}
				}
				if (Utils.checkIfEmailHasExtension(userMfaDTO.getMfaEmail(), exten)) {
					ErrorDetails errorDetails = new ErrorDetails(ErrorCodes.UR008, otherEmailError);
					errorDetails.setErrorMessage(otherEmailError);
					throw new UnauthorizedAccessException(errorDetails);
				}
				validateUniqueMfaEmail(userMfaDTO.getEmail(), userMfaDTO.getMfaEmail(), userMfaDTO.getId());
				validateEmailWithMfaEmail(userMfaDTO.getEmail(), userMfaDTO.getClientCode(), userMfaDTO.getId(), userType);
			} else if(StringUtils.isEmpty(userMfaDTO.getMobile())) {
				throw new ValidationException(new ErrorDetails(ErrorCodes.UR0019));
			}
		}
	}

	public void addEmployeeInvitation(UsersDTO userDTO, UserPrincipal userPrincipal) {
		if (!userDTO.getIsAccountLoginChange()) {
			if (CollectionUtils.isNotEmpty(userDTO.getClients())
					&& (null != userDTO.getClients().get(0).getEmployeeCode())
					&& GenericConstants.USERTYPE_EMPLOYEE.equals(userDTO.getClients().get(0).getUserType())) {
				employeeInvitationBusinessService.saveAllByClientCodeAndEmployeeId(userPrincipal.getClientCode(),
						Lists.newArrayList(userDTO.getClients().get(0).getEmployeeCode()));
			}
		}
	}
	
	
	private void validateUserDTO(UsersDTO userDTO, String clientCode, String emailError, String otherEmailError, String exten[]) {
		if (userDTO.getClients().size() > 1) {
			throw new UnauthorizedAccessException(
					new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
		}
		if (userDTO.getClients().size() == 1
				&& (userDTO.getClients().get(0).getUserType().equals(GenericConstants.USERTYPE_VANCOUVER)
				|| userDTO.getClients().get(0).getUserType().equals(GenericConstants.USERTYPE_BRANCH))
				&& !Utils.checkIfEmailHasExtension(userDTO.getEmail(),  new String[] {EXTEN_HQ})) {
			ErrorDetails errorDetails = new ErrorDetails(ErrorCodes.UR008, emailError);
			errorDetails.setErrorMessage(emailError);
			throw new UnauthorizedAccessException(errorDetails);
		}
		if (userDTO.getClients().size() == 1 && (userDTO.getClients().get(0).getUserType()
				.equals(GenericConstants.USERTYPE_CLIENT)
				|| userDTO.getClients().get(0).getUserType().equals(GenericConstants.USERTYPE_EXTERNAL))) {
			if (!userDTO.getClients().get(0).getClientCode().equals(clientCode)) {
				throw new UnauthorizedAccessException(
						new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
			}
			if (Utils.checkIfEmailHasExtension(userDTO.getEmail(), exten)) {
				ErrorDetails errorDetails = new ErrorDetails(ErrorCodes.UR008, otherEmailError);
				errorDetails.setErrorMessage(otherEmailError);
				throw new UnauthorizedAccessException(errorDetails);
			}
			if (CollectionUtils.isNotEmpty(userDTO.getRoles())) {
				Optional<RbacEntity> roleOptional = roleRepository.findById(userDTO.getRoles().get(0).getId());
				if (roleOptional.isPresent()) {
					RbacEntity role = roleOptional.get();
					userDTO.getClients().get(0).setClientRoles(Lists.newArrayList());
					UserClientRoleDTOV2 clientRoleDTOV2 = new UserClientRoleDTOV2();
					clientRoleDTOV2.setRoleId(role.getMappingId());
					userDTO.getClients().get(0).getClientRoles().add(clientRoleDTOV2);
				}
				userDTO.setRoles(null);
			}
		}
		employeeEmailExtensionCheck(userDTO, exten);
	}
	
	private void employeeEmailExtensionCheck(UsersDTO userDTO, String exten[]) {
		if (userDTO.getClients().size() == 1
				&& userDTO.getClients().get(0).getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE)
				&& Utils.checkIfEmailHasExtension(userDTO.getEmail(), exten)) {
			throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
		}
	}
	
	private void verifyUserType(UserPrincipal userPrincipal, String userType) {
		if((!userPrincipal.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER)
				&& userType.equals(GenericConstants.USERTYPE_VANCOUVER)) 
				|| (!userPrincipal.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER)
						&& !userPrincipal.getUserType().equals(GenericConstants.USERTYPE_BRANCH)
						&& userType.equals(GenericConstants.USERTYPE_BRANCH))) {
			throw new UnauthorizedAccessException(
					new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));		
		}
	}

	/*
	 * Method for getting user security
	 */
	private List<UserSecurity> getSuperUserFilters(List<UserSecurity> existingFilters, String superUsername,
												   String clientCode, UserPrincipal userPrincipal) {

		Map<String, Set<String>> superUserFilters = this.getUserFilter(superUsername, clientCode, userPrincipal);

		/*
		 * if super user filters empty return existing filters.
		 */
		if (MapUtils.isEmpty(superUserFilters)) {
			return existingFilters;
		} else {

			if (CollectionUtils.isNotEmpty(existingFilters)) {
				combineWithExistingFilters(existingFilters, superUserFilters);
			} else {
				return mapToUserSecurity(superUserFilters);
			}
		}

		return new ArrayList<>();
	}

	private List<UserSecurity> combineWithExistingFilters(List<UserSecurity> existingFilters,
														  Map<String, Set<String>> superUserFilters) {
		List<UserSecurity> filters = new ArrayList<>();
		boolean locationFilter = hasFilter(existingFilters, Enums.AuditDetailsSourceEntityEnum.LOCATION.toString());
		if (!locationFilter) {
			filters.addAll(fetchTypes(superUserFilters, Enums.AuditDetailsSourceEntityEnum.LOCATION.toString()));
		}

		locationFilter = hasFilter(existingFilters, Enums.AuditDetailsSourceEntityEnum.DIVISION.toString());
		if (!locationFilter) {
			filters.addAll(fetchTypes(superUserFilters, Enums.AuditDetailsSourceEntityEnum.DIVISION.toString()));
		}

		locationFilter = hasFilter(existingFilters, Enums.AuditDetailsSourceEntityEnum.DEPARTMENT.toString());
		if (!locationFilter) {
			filters.addAll(fetchTypes(superUserFilters, Enums.AuditDetailsSourceEntityEnum.DEPARTMENT.toString()));
		}

		locationFilter = hasFilter(existingFilters, Enums.AuditDetailsSourceEntityEnum.SHIFT.toString());
		if (!locationFilter) {
			filters.addAll(fetchTypes(superUserFilters, Enums.AuditDetailsSourceEntityEnum.SHIFT.toString()));
		}

		locationFilter = hasFilter(existingFilters, Enums.AuditDetailsSourceEntityEnum.PROJECT.toString());
		if (!locationFilter) {
			filters.addAll(fetchTypes(superUserFilters, Enums.AuditDetailsSourceEntityEnum.PROJECT.toString()));
		}
		
		locationFilter = hasFilter(existingFilters, Enums.AuditDetailsSourceEntityEnum.WORK_GROUP.toString());
		if (!locationFilter) {
			filters.addAll(fetchTypes(superUserFilters, Enums.AuditDetailsSourceEntityEnum.WORK_GROUP.toString()));
		}

		return filters;
	}

	private boolean hasFilter(List<UserSecurity> existingFilters, String filter) {
		for (UserSecurity userSecurity : existingFilters) {
			if (filter.equalsIgnoreCase(userSecurity.getType())) {
				return true;
			}
		}
		return false;
	}

	private List<UserSecurity> mapToUserSecurity(Map<String, Set<String>> existingFilters) {
		List<UserSecurity> newUserSecurity = new ArrayList<>();

		if (MapUtils.isNotEmpty(existingFilters)) {
			newUserSecurity.addAll(fetchTypes(existingFilters, Enums.AuditDetailsSourceEntityEnum.LOCATION.toString()));
			newUserSecurity.addAll(fetchTypes(existingFilters, Enums.AuditDetailsSourceEntityEnum.DIVISION.toString()));
			newUserSecurity
					.addAll(fetchTypes(existingFilters, Enums.AuditDetailsSourceEntityEnum.DEPARTMENT.toString()));
			newUserSecurity.addAll(fetchTypes(existingFilters, Enums.AuditDetailsSourceEntityEnum.SHIFT.toString()));
			newUserSecurity.addAll(fetchTypes(existingFilters, Enums.AuditDetailsSourceEntityEnum.PROJECT.toString()));
			newUserSecurity.addAll(fetchTypes(existingFilters, Enums.AuditDetailsSourceEntityEnum.WORK_GROUP.toString()));
		}
		return newUserSecurity;
	}

	private List<UserSecurity> fetchTypes(Map<String, Set<String>> existingFilters, String type) {

		List<UserSecurity> userSecurity = new ArrayList<>();
		if (MapUtils.isNotEmpty(existingFilters)) {

			Set<String> typeCodes = existingFilters.get(type);
			if (CollectionUtils.isNotEmpty(typeCodes)) {
				for (String code : typeCodes) {
					UserSecurity security = new UserSecurity();
					security.setValue(code);
					security.setType(type);
					userSecurity.add(security);
				}
			}
		}

		return userSecurity;
	}

	@Override
	@Transactional
	public UsersDTO updateUser(UsersDTO userDTO, String token, UserPrincipal userPrincipal) {
		Users user = null;
		List<String> nonPrismisedCostCenters = Lists.newArrayList();
		String firstName = userDTO.getFirstName();
		String lastName = userDTO.getLastName();
		String mobile = userDTO.getMobile();
		String userType = "";
		try {

			String emailError = "The Email Address should be in" + " " + bbsiHeadEmails + " " + FORMAT;
			String otherEmailError = "The Email Address should not be in" + " " + bbsiHeadEmails + " " + FORMAT;

			String exten[] = bbsiHeadEmails.split(",");

			userDTO.setEmail(userDTO.getEmail().trim().toLowerCase());
			if(userDTO.getMfaEmail() != null) {
				userDTO.setMfaEmail(userDTO.getMfaEmail().trim().toLowerCase());
			}

			if (CollectionUtils.isNotEmpty(userDTO.getClients())) {
				userType = userDTO.getClients().get(0).getUserType();
				validateUserDTO(userDTO, userPrincipal.getClientCode(), emailError, otherEmailError, exten);

				userDTO.getClients().forEach(clientDTO -> {
					clientDTO = populateCostCenterDetails(clientDTO, userPrincipal);
					validateForEmployee(clientDTO);
					
				});
			} else if (CollectionUtils.isNotEmpty(userDTO.getCostCenters())
					&& !Utils.checkIfEmailHasExtension(userDTO.getEmail(), new String[] {EXTEN_HQ})) {
				ErrorDetails errorDetails = new ErrorDetails(ErrorCodes.UR008, emailError);
				errorDetails.setErrorMessage(emailError);
				throw new UnauthorizedAccessException(errorDetails);
			} else if (CollectionUtils.isNotEmpty(userDTO.getCostCenters())) {
				userType = userDTO.getCostCenters().get(0).getUserType();
			}
			verifyUserType(userPrincipal, userType);
			if (GenericConstants.USERTYPE_VANCOUVER.equals(userPrincipal.getUserType())
					||(GenericConstants.USERTYPE_CLIENT.equals(userPrincipal.getUserType()) 
							&& userPrincipal.getUserId() == userDTO.getId())
					||(GenericConstants.USERTYPE_EXTERNAL.equals(userPrincipal.getUserType())
							&& userPrincipal.getUserId() == userDTO.getId())) {
				validateMfaEmail(userType, userDTO, otherEmailError, exten);
			}
					
			// Request payload validation
			checkInvalidRequestPayloadValidation(userDTO);

			checkUserDoesNotHavePrivilegesValidation(userDTO, userPrincipal);

			Optional<Users> existingUserOptional = userRepository.findById(userDTO.getId());
			if (existingUserOptional.isPresent()) {
				user = existingUserOptional.get();
				if (GenericConstants.USERTYPE_BRANCH.equals(userPrincipal.getUserType())
						||(GenericConstants.USERTYPE_CLIENT.equals(userPrincipal.getUserType()) 
								&& userPrincipal.getUserId() != userDTO.getId())
						||(GenericConstants.USERTYPE_EXTERNAL.equals(userPrincipal.getUserType())
								&& userPrincipal.getUserId() != userDTO.getId())) {
					userDTO.setMfaEmail(user.getMfaEmail());
					userDTO.setMobile(user.getMobile());
				}
				String oldDataString = buildAuditObj(userType, userPrincipal, user);

				if (!userPrincipal.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER)
						&& !user.getEmail().equalsIgnoreCase(userDTO.getEmail())) {
					throw new UnauthorizedAccessException(
							new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
				}

				String password = user.getPassword();
				copyUpdatedDetails(user, userDTO, userType);
				if(StringUtils.isNotEmpty(userDTO.getMfaEmail())) {
					updateEmployeeConfig(user, userDTO.getMfaEmail(), token);
				}
				
				if(StringUtils.isNotEmpty(mobile)) {
					for(UserClients userClient : user.getClients()) {
						if(GenericConstants.USERTYPE_EMPLOYEE.equals(userClient.getUserType()) 
								&& userPrincipal.getClientMap().containsKey(userClient.getClientCode())) {
							updateContactInfoForEmployee(userDTO.getMobile(), userClient.getClientCode(), userClient.getEmployeeCode(), userPrincipal, token);
						}
					}
				}
				buildUserTypes(user, userDTO, true, userPrincipal.getClientCode());

				// validate and set roles to user
				validateAndAssignRoles(user, userDTO);

				assignIsPrimaryFlag(user);
				user.setPassword(password);
				user = userRepository.save(user);

				if (CollectionUtils.isNotEmpty(userDTO.getFilter())) {
					saveUserFilters(userDTO, user,userPrincipal.getClientCode(), userType);
				}

				String newDataString = buildAuditObj(userType, userPrincipal, user);

				userDTO = userMapper.userToUserDTO(user);
				userDTO.setFirstName(firstName);
				userDTO.setLastName(lastName);
				userDTO.setMobile(mobile);

				RbacDTO entity = new RbacDTO();
				entity.setCreatedBy(user.getCreatedBy());
				entity.setCreatedOn(user.getCreatedOn());
				entity.setModifiedOn(LocalDateTime.now());
				entity.setModifiedBy(userPrincipal.getEmail());
				auditDetailsUtil.buildAuditDetailsAndSave(Enums.AuditDetailsSourceEntityEnum.USER.toString(), EMAIL,
						user.getEmail(), null, Enums.AuditDetailsSourceEntityEnum.USER.toString(), userPrincipal,
						oldDataString, newDataString, Enums.AuditDetailsEventEnum.UPDATE.toString(), entity);

				userDTO.setNonPrismizedCostCenters(nonPrismisedCostCenters);
			} else {
				LogUtils.basicErrorLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER),
						String.format("No Record Found for user Id  : %s", userDTO.getId()));

				throw new ValidationException(new ErrorDetails(ErrorCodes.UR003));
			}

		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER), e,
					"Error occurred during user creation");
		}
		return userDTO;
	}
	
	private ClientDTOV2 populateCostCenterDetails(ClientDTOV2 clientDTO, UserPrincipal userPrincipal) {
		if (StringUtils.isEmpty(clientDTO.getCostCenterCode()) && userPrincipal != null
				&& StringUtils.isNotEmpty(userPrincipal.getCostCenterCode())) {
			clientDTO.setCostCenterCode(userPrincipal.getCostCenterCode());
		}
		if (StringUtils.isEmpty(clientDTO.getCostCenterDescription()) && userPrincipal != null
				&& StringUtils.isNotEmpty(userPrincipal.getBbsiBranch())) {
			clientDTO.setCostCenterDescription(userPrincipal.getBbsiBranch());
		}
		
		return clientDTO;
	}
	
	private void validateForEmployee(ClientDTOV2 clientDTO) {
		if (StringUtils.isNotEmpty(clientDTO.getUserType())
				&& clientDTO.getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE)
				&& StringUtils.isNotEmpty(clientDTO.getEmployeeCode())) {
			UserClients userClients = userClientRepository.findByClientCodeAndEmployeeCode(
					clientDTO.getClientCode(), clientDTO.getEmployeeCode());
			if (userClients != null) {
				throw new ValidationException(new ErrorDetails(ErrorCodes.UR0015));
			}
		}
	}

	private String buildAuditObj(String userType, UserPrincipal userPrincipal, Users user) {

		List<UserClients> clientList = user.getClients();
		if(CollectionUtils.isEmpty(clientList)) {
			clientList = fetchCostCenterUserClients(user);
		}
		UserClients userClient = null;
		Optional<UserClients> userClientOpt = getUserClient(userType, userPrincipal, clientList);

		String clients = clientList.parallelStream().map(UserClients::getClientCode).sorted().collect(Collectors.joining(";"));
		
		if(userClientOpt.isPresent())
			userClient = userClientOpt.get();
		else if(CollectionUtils.isNotEmpty(clientList))
			userClient = clientList.get(0);
		else 
			return null;

		Map<String,Object> map = Maps.newHashMap();

		map.put("Email", user.getEmail());
		if(CollectionUtils.isNotEmpty(user.getRoles())) {
			map.put("Role", user.getRoles().get(0).getName());
		} else if(CollectionUtils.isNotEmpty(userClient.getClientRoles())) {
			map.put("Role", userClient.getClientRoles().get(0).getRole().getName());
		}
		map.put("Status", (userClient.getIsActive() == null || userClient.getIsActive().equals(Boolean.TRUE)) ? "Active" : "Inactive");
		map.put("First Name",userClient.getFirstName());
		map.put("Last Name",userClient.getLastName());
		map.put("Mobile",userClient.getMobile());
		map.put("Start Date",userClient.getStartDate());
		map.put("End Date",userClient.getEndDate());
		map.put("Type",userClient.getUserType());
		map.put("Clients",clients);
		if(null != userClient.getId() 
				&& (userType.equals(GenericConstants.USERTYPE_CLIENT) || userType.equals(GenericConstants.USERTYPE_EXTERNAL))) {
			Map<String, Set<String>> filterMap = Maps.newTreeMap();
			List<UserSecurity> filters = userSecurityRepository.findByUserClient_Id(userClient.getId());
			if (CollectionUtils.isNotEmpty(filters)) {
				constructFilterMapByUserSecurityFilters(filters, filterMap);
				map.put("Data Security",filterMap);
			}
		}
		return GenericUtils.gsonInstance.get().toJson(map);
	}

	private Optional<UserClients> getUserClient(String userType, UserPrincipal userPrincipal, List<UserClients> uc1) {

		Optional<UserClients> userClientOpt = uc1.parallelStream().filter(uc-> uc.getClientCode().equals(userPrincipal.getClientCode())
				&& uc.getUserType().equals(userType)).findFirst();
		if(userClientOpt.isPresent()) {
			return userClientOpt;
		}else {
			userClientOpt = uc1.parallelStream().filter(uc -> uc.getClientCode().equals(userPrincipal.getClientCode())).findFirst();

			return userClientOpt.isPresent() ? userClientOpt
					: uc1.parallelStream().filter(uc -> uc.getUserType().equals(userType)).findFirst();
		}
	}

	private void saveUserFilters(UsersDTO userDTO, Users user, String clientCode, String userType) {
		for (UserClients userClients : user.getClients()) {
			if (clientCode.equals(userClients.getClientCode())
					&& userType.equals(userClients.getUserType())) {
				userClients.setUserFilters(Lists.newArrayList());
				userSecurityRepository.deleteByUserClient_Id(userClients.getId());
			}
			for (UserSecurityDTOV2 userFilter : userDTO.getFilter()) {
				if (StringUtils.isNotEmpty(userFilter.getClientCode())
						&& userFilter.getClientCode().equals(userClients.getClientCode())
						&& userType.equals(userClients.getUserType())
						&& CollectionUtils.isNotEmpty(userFilter.getValue())) {
					for (String value : userFilter.getValue()) {
						UserSecurity userSecurity = new UserSecurity();
						userSecurity.setType(userFilter.getType());
						userSecurity.setValue(value);
						userSecurity.setUserClient(userClients);
						userClients.getUserFilters().add(userSecurity);
					}
				}
			}
			if(CollectionUtils.isNotEmpty(userClients.getUserFilters())) {
			userSecurityRepository.saveAll(userClients.getUserFilters());
			}
		}
	}

	private void checkUserDoesNotHavePrivilegesValidation(UsersDTO userDTO,UserPrincipal userPrincipal) {
		if (CollectionUtils.isNotEmpty(userDTO.getFilter())) {
			Map<String, Set<String>> superUserFilters = getUserFilter(userPrincipal.getEmail(),
					userPrincipal.getClientCode(), userPrincipal);
			for (UserSecurityDTOV2 userFilter : userDTO.getFilter()) {
				if (superUserFilters.containsKey(userFilter.getType())
						&& !superUserFilters.get(userFilter.getType()).containsAll(userFilter.getValue())) {
					throw new UnauthorizedAccessException(
							new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
				}
			}
		}
	}
	private void checkInvalidRequestPayloadValidation(UsersDTO userDTO) {
		if (ObjectUtils.isEmpty(userDTO) || ObjectUtils.isEmpty(userDTO.getId())) {
			LogUtils.basicErrorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER),
					INVALID_PAYLOAD_MSG);

			throw new ValidationException(new ErrorDetails(ErrorCodes.UR002));
		}
	}

	private void copyUpdatedDetails(Users user, UsersDTO userDTOV2, String userType) {
		user.setId(userDTOV2.getId());
		user.setEmail(userDTOV2.getEmail());
		user.setIsFirstLogin(userDTOV2.getIsFirstLogin());
		user.setVersion(userDTOV2.getVersion());
		user.setMobile(userDTOV2.getMobile());
		if(GenericConstants.USERTYPE_CLIENT.equalsIgnoreCase(userType)
				|| GenericConstants.USERTYPE_EXTERNAL.equalsIgnoreCase(userType)
				|| GenericConstants.USERTYPE_EMPLOYEE.equalsIgnoreCase(userType)
				|| GenericConstants.USERTYPE_NEWHIRE.equalsIgnoreCase(userType)) {
			user.setMfaEmail(userDTOV2.getMfaEmail());
		}
		if(GenericConstants.USERTYPE_VANCOUVER.equalsIgnoreCase(userType)
				|| GenericConstants.USERTYPE_BRANCH.equalsIgnoreCase(userType)) {
			user.setStatus(userDTOV2.getStatus());
		}

	}

	private void mergeUserTypes(Users newUser, UsersDTO userDTO) {
		if (CollectionUtils.isEmpty(newUser.getClients())) {
			List<UserClients> userClients = userClientRepository.findByUser_Id(newUser.getId());
			if (userClients == null) {
				userClients = Lists.newArrayList();
			}
			newUser.setClients(userClients);
		}
		Map<String, Set<String>> userTypeMap = Maps.newHashMap();
		Set<String> clientList = Sets.newHashSet();
		newUser.getClients().forEach(userClientsV2 -> {
			if (userTypeMap.containsKey(userClientsV2.getUserType())) {
				userTypeMap.get(userClientsV2.getUserType()).add(userClientsV2.getClientCode());
			} else {
				Set<String> clients = Sets.newHashSet();
				clients.add(userClientsV2.getClientCode());
				userTypeMap.put(userClientsV2.getUserType(), clients);
			}
			clientList.add(userClientsV2.getClientCode());
		});

		// bug fix changes- 17077
		if (CollectionUtils.isNotEmpty(userDTO.getClients())
				&& userDTO.getClients().get(0).getUserType().equals(GenericConstants.USERTYPE_NEWHIRE)
				&& clientList.contains(userDTO.getClients().get(0).getClientCode())) {
			// already employee to one client and trying do on-board new hire to
			// the same client throwing error
			newUser.getClients().forEach(userClientsV2 -> {
				if (userClientsV2.getClientCode().equals(userDTO.getClients().get(0).getClientCode())
						&& userClientsV2.getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE)) {
					throw new ValidationException(new ErrorDetails(ErrorCodes.UR0010));
				}
			});
		}

		if (CollectionUtils.isNotEmpty(userDTO.getClients())
				&& userDTO.getClients().get(0).getUserType().equals(GenericConstants.USERTYPE_CLIENT)
				&& clientList.contains(userDTO.getClients().get(0).getClientCode())) {
			newUser.getClients().forEach(userClientsV2 -> {
				if (userClientsV2.getClientCode().equals(userDTO.getClients().get(0).getClientCode())
						&& userClientsV2.getUserType().equals(GenericConstants.USERTYPE_CLIENT)) {
					throw new ValidationException(new ErrorDetails(ErrorCodes.UR0010));
				}
			});
		}

		if (CollectionUtils.isNotEmpty(userDTO.getClients())
				&& userDTO.getClients().get(0).getUserType().equals(GenericConstants.USERTYPE_EXTERNAL)
				&& clientList.contains(userDTO.getClients().get(0).getClientCode())) {
			newUser.getClients().forEach(userClientsV2 -> {
				if (userClientsV2.getClientCode().equals(userDTO.getClients().get(0).getClientCode())
						&& userClientsV2.getUserType().equals(GenericConstants.USERTYPE_EXTERNAL)) {
					throw new ValidationException(new ErrorDetails(ErrorCodes.UR0010));
				}
			});
		}

		if (CollectionUtils.isNotEmpty(userDTO.getClients()) && userDTO.getClients().size() == 1
				&& userDTO.getClients().get(0).getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE)
				&& StringUtils.isNotEmpty(userDTO.getClients().get(0).getEmployeeCode())
				&& CollectionUtils.isEmpty(userDTO.getClients().get(0).getClientRoles())) {
			RbacEntity role = roleRepository.findByCodeAndType(GenericConstants.USERTYPE_EMPLOYEE,
					UserEnum.ROLE.toString());
			if (role != null) {
				UserClientRoleDTOV2 clientRoleV2 = new UserClientRoleDTOV2();
				clientRoleV2.setRoleId(role.getMappingId());
				List<UserClientRoleDTOV2> clientRoles = Lists.newArrayList();
				clientRoles.add(clientRoleV2);
				userDTO.getClients().get(0).setClientRoles(clientRoles);
			}
		}

		if (!CollectionUtils.isEmpty(userDTO.getCostCenters())) {
			newUser.setFirstName(userDTO.getFirstName());
			newUser.setLastName(userDTO.getLastName());
			newUser.setMobile(userDTO.getMobile());
			newUser.setStatus(userDTO.getStatus());
			saveUserCostCenters(newUser, userDTO.getCostCenters());
		} else if (CollectionUtils.isNotEmpty(userDTO.getClients())) {
			List<Long> clientRoleIdList = new ArrayList<>();
			List<Long> roleIdList = new ArrayList<>();
			userDTO.getClients().forEach(userClient -> {
				if (CollectionUtils.isNotEmpty(userClient.getClientRoles())) {
					userClient.getClientRoles().forEach(clientRole -> {
						if (null != clientRole.getId()) {
							clientRoleIdList.add(clientRole.getId());
						}
						if (null != clientRole.getRoleId()) {
							roleIdList.add(clientRole.getRoleId());
						}
					});
				}
			});

			if (CollectionUtils.isNotEmpty(clientRoleIdList) || CollectionUtils.isNotEmpty(roleIdList)) {
				List<ClientRole> clientRoles = clientRoleRepository.findByIdIn(clientRoleIdList);
				if (CollectionUtils.isEmpty(clientRoles) && CollectionUtils.isNotEmpty(roleIdList)) {
					List<Long> filteredRoleIds = Lists.newArrayList();
					for (Long roleId : roleIdList) {
						List<ClientRole> tempClientRoles = null;
						if (null != userDTO.getClients().get(0).getId() && userDTO.getClients().get(0).getId() > 0) {
							tempClientRoles = clientRoleRepository
									.findByUserClient_IdAndRole_Id(userDTO.getClients().get(0).getId(), roleId);
						}
						if (CollectionUtils.isNotEmpty(tempClientRoles)) {
							clientRoles.addAll(tempClientRoles);
						} else {
							filteredRoleIds.add(roleId);
						}
					}
					Iterable<Mapping> roles = mappingRepository.findAllById(filteredRoleIds);
					for (Mapping role : roles) {
						ClientRole clientRoleV2 = new ClientRole();
						clientRoleV2.setRole(role);
						clientRoles.add(clientRoleV2);
					}
				}
				userDTO.getClients().forEach(clientDTO -> {
					if (!userTypeMap.containsKey(clientDTO.getUserType())) {
						UserClients userClients = new UserClients();
						userClients.setClientCode(clientDTO.getClientCode());
						userClients.setEmployeeCode(clientDTO.getEmployeeCode());
						userClients.setStartDate(userDTO.getStartDate());
						userClients.setEndDate(userDTO.getEndDate());
						userClients.setVersion(clientDTO.getVersion());
						userClients.setIsActive(userDTO.getStatus());
						userClients.setFirstName(userDTO.getFirstName());
						userClients.setLastName(userDTO.getLastName());
						userClients.setMobile(userDTO.getMobile());
						userClients.setIsCaliforniaUser(userDTO.getIsCaliforniaUser());
						List<ClientRole> filteredClientRoles = Lists.newArrayList();
						for (ClientRole clientRole : clientRoles) {
							if (clientRole.getId() == null) {
								clientRole.setClientCode(clientDTO.getClientCode());
								clientRole.setUserClient(userClients);
								filteredClientRoles.add(clientRole);
							} else if (StringUtils.isNotEmpty(clientRole.getClientCode())
									&& clientRole.getClientCode().equals(clientDTO.getClientCode())) {
								filteredClientRoles.add(clientRole);
							}
						}
						userClients.setClientRoles(filteredClientRoles);
						userClients.setUserType(clientDTO.getUserType());
						userClients.setNewHireId(clientDTO.getNewHireId());
						userClients.setUser(newUser);
						newUser.getClients().add(userClients);
					} else if (userTypeMap.containsKey(clientDTO.getUserType())
							&& !userTypeMap.get(clientDTO.getUserType()).contains(clientDTO.getClientCode())) {
						UserClients userClients = new UserClients();
						userClients.setClientCode(clientDTO.getClientCode());
						userClients.setEmployeeCode(clientDTO.getEmployeeCode());
						userClients.setStartDate(userDTO.getStartDate());
						userClients.setEndDate(userDTO.getEndDate());
						userClients.setFirstName(userDTO.getFirstName());
						userClients.setLastName(userDTO.getLastName());
						userClients.setMobile(userDTO.getMobile());
						userClients.setVersion(clientDTO.getVersion());
						userClients.setIsActive(userDTO.getStatus());
						userClients.setIsCaliforniaUser(userDTO.getIsCaliforniaUser());
						List<ClientRole> filteredClientRoles = Lists.newArrayList();
						for (ClientRole clientRole : clientRoles) {
							if (clientRole.getId() == null) {
								clientRole.setClientCode(clientDTO.getClientCode());
								clientRole.setUserClient(userClients);
								filteredClientRoles.add(clientRole);
							} else if (StringUtils.isNotEmpty(clientRole.getClientCode())
									&& clientRole.getClientCode().equals(clientDTO.getClientCode())) {
								filteredClientRoles.add(clientRole);
							}
						}
						userClients.setClientRoles(filteredClientRoles);
						userClients.setUserType(clientDTO.getUserType());
						userClients.setNewHireId(clientDTO.getNewHireId());
						userClients.setUser(newUser);
						newUser.getClients().add(userClients);
					}
				});
			} else {
				userDTO.getClients().forEach(clientDTO -> {
					if ((!userTypeMap.containsKey(clientDTO.getUserType())) || (userTypeMap.containsKey(clientDTO.getUserType())
							&& !userTypeMap.get(clientDTO.getUserType()).contains(clientDTO.getClientCode()))) {
						UserClients userClients = new UserClients();
						userClients.setClientCode(clientDTO.getClientCode());
						userClients.setIsActive(userDTO.getStatus());
						userClients.setEmployeeCode(clientDTO.getEmployeeCode());
						userClients.setUserType(clientDTO.getUserType());
						userClients.setStartDate(userDTO.getStartDate());
						userClients.setEndDate(userDTO.getEndDate());
						userClients.setFirstName(userDTO.getFirstName());
						userClients.setLastName(userDTO.getLastName());
						userClients.setMobile(userDTO.getMobile());
						userClients.setNewHireId(clientDTO.getNewHireId());
						userClients.setIsCaliforniaUser(userDTO.getIsCaliforniaUser());
						userClients.setUser(newUser);
						newUser.getClients().add(userClients);
					}
				});
			}
		}
	}

	private void buildUserTypes(Users newUser, UsersDTO userDTO, boolean replaceTypes, String currentClient) {
		List<CostCenterDTO> costCenters = userDTO.getCostCenters();
		Map<String, String> clientMap = Maps.newHashMap();
		List<String> existingClientList = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(userDTO.getClients())) {
			userDTO.getClients().forEach(clientDTO -> {
				clientMap.put(clientDTO.getClientCode(), clientDTO.getClientName());
			});
		}
		if (CollectionUtils.isNotEmpty(newUser.getClients())) {
			newUser.getClients().forEach(client -> {
				existingClientList.add(client.getClientCode());
			});
		}
		if (replaceTypes) {
			if (CollectionUtils.isEmpty(userDTO.getClients()) && CollectionUtils.isEmpty(costCenters)) {
				return;
			}
			if (!CollectionUtils.isEmpty(costCenters)) {
				userCostCenterAssociationRepository.deleteByUserId(newUser.getId());
			} else {
				List<UserClients> filteredUserTypes = Lists.newArrayList();
				List<UserClients> userClients = userClientRepository.findByUser_Id(newUser.getId());
				if (CollectionUtils.isNotEmpty(userClients)) {
					List<Long> removalIds = Lists.newArrayList();
					userClients.forEach(userClientV1 -> {
						// filtering the existing user types
						if (!currentClient.equals(userClientV1.getClientCode()) || 
								userClientV1.getUserType().equals(GenericConstants.USERTYPE_NEWHIRE) 
								|| userClientV1.getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE)) {
							filteredUserTypes.add(userClientV1);
						} else {
							removalIds.add(userClientV1.getId());
						}
					});
					deleteRecursiveUserClients(removalIds);
				}
				newUser.setClients(filteredUserTypes);
			}
		} else {
			if (newUser.getClients() == null) {
				newUser.setClients(Lists.newArrayList());
			} else if (userDTO.getClients() != null && userDTO.getClients().size() == 1
					&& StringUtils.isNotEmpty(userDTO.getClients().get(0).getUserType())
					&& StringUtils.isNotEmpty(userDTO.getClients().get(0).getClientCode())
					&& existingClientList.contains(userDTO.getClients().get(0).getClientCode())
					&& newUser.getId() > 0) {
				List<UserClients> userClients = userClientRepository.findByUser_Id(newUser.getId());
				if (CollectionUtils.isNotEmpty(userClients)) {
					userClients.forEach(userClientV1 -> {
						if (StringUtils.isNotEmpty(userClientV1.getUserType())
								&& userClientV1.getUserType().equals(userDTO.getClients().get(0).getUserType())) {
							return;
						}
					});
				}
			}
		}

		if (!CollectionUtils.isEmpty(costCenters)) {
			if (!replaceTypes) {
				newUser.setClients(Lists.newArrayList());
			}
			saveUserCostCenters(newUser, costCenters);
		} else if (CollectionUtils.isNotEmpty(userDTO.getClients())) {
			List<Long> clientRoleIdList = new ArrayList<>();
			List<Long> roleIdList = new ArrayList<>();
			userDTO.getClients().forEach(userClientDTO -> {
				if (CollectionUtils.isNotEmpty(userClientDTO.getClientRoles())) {
					userClientDTO.getClientRoles().forEach(clientRole -> {
						if (null != clientRole.getId() && clientRole.getId() > 0) {
							clientRoleIdList.add(clientRole.getId());
						}
						if (null != clientRole.getRoleId() && clientRole.getRoleId() > 0) {
							roleIdList.add(clientRole.getRoleId());
						}
					});
				}
			});

			if (CollectionUtils.isNotEmpty(clientRoleIdList) || CollectionUtils.isNotEmpty(roleIdList)) {
				List<ClientRole> clientRoles = Lists.newArrayList();
				if (CollectionUtils.isNotEmpty(clientRoleIdList)) {
					clientRoles = clientRoleRepository.findByIdIn(clientRoleIdList);
				}
				if (CollectionUtils.isEmpty(clientRoles) && CollectionUtils.isNotEmpty(roleIdList)) {
					List<Long> filteredRoleIds = Lists.newArrayList();
					for (Long roleId : roleIdList) {
						List<ClientRole> tempClientRoles = null;
						if (null != userDTO.getClients().get(0).getId() && userDTO.getClients().get(0).getId() > 0) {
							tempClientRoles = clientRoleRepository
									.findByUserClient_IdAndRole_Id(userDTO.getClients().get(0).getId(), roleId);
						}
						if (CollectionUtils.isNotEmpty(tempClientRoles)) {
							clientRoles.addAll(tempClientRoles);
						} else {
							filteredRoleIds.add(roleId);
						}
					}
					Iterable<Mapping> roles = mappingRepository.findAllById(filteredRoleIds);
					for (Mapping role : roles) {
						ClientRole clientRoleV2 = new ClientRole();
						clientRoleV2.setRole(role);
						clientRoles.add(clientRoleV2);
					}
				}
				for (ClientDTOV2 clientDTO : userDTO.getClients()) {
					UserClients userClient = new UserClients();
					userClient.setClientCode(clientDTO.getClientCode());
					userClient.setVersion(clientDTO.getVersion());
					userClient.setIsActive(userDTO.getStatus());
					userClient.setIsCaliforniaUser(userDTO.getIsCaliforniaUser());
					List<ClientRole> filteredClientRoles = Lists.newArrayList();
					for (ClientRole clientRole : clientRoles) {
						if (clientRole.getId() == null) {
							clientRole.setClientCode(clientDTO.getClientCode());
							clientRole.setUserClient(userClient);
							filteredClientRoles.add(clientRole);
						} else if (StringUtils.isNotEmpty(clientRole.getClientCode())
								&& clientRole.getClientCode().equals(clientDTO.getClientCode())) {
							filteredClientRoles.add(clientRole);
						}
					}
					userClient.setClientRoles(filteredClientRoles);
					userClient.setEmployeeCode(clientDTO.getEmployeeCode());
					userClient.setUserType(clientDTO.getUserType());
					userClient.setStartDate(userDTO.getStartDate());
					userClient.setEndDate(userDTO.getEndDate());
					userClient.setNewHireId(clientDTO.getNewHireId());
					userClient.setFirstName(userDTO.getFirstName());
					userClient.setLastName(userDTO.getLastName());
					userClient.setMobile(userDTO.getMobile());
					userClient.setUser(newUser);
					newUser.getClients().add(userClient);
				}
			} else {
				for (ClientDTOV2 clientDTO : userDTO.getClients()) {
					UserClients userClient = new UserClients();
					userClient.setClientCode(clientDTO.getClientCode());
					userClient.setEmployeeCode(clientDTO.getEmployeeCode());
					userClient.setIsActive(userDTO.getStatus());
					userClient.setUserType(clientDTO.getUserType());
					userClient.setStartDate(userDTO.getStartDate());
					userClient.setEndDate(userDTO.getEndDate());
					userClient.setFirstName(userDTO.getFirstName());
					userClient.setLastName(userDTO.getLastName());
					userClient.setMobile(userDTO.getMobile());
					userClient.setNewHireId(clientDTO.getNewHireId());
					userClient.setIsCaliforniaUser(userDTO.getIsCaliforniaUser());
					userClient.setUser(newUser);
					newUser.getClients().add(userClient);
				}
			}
		}

		if (CollectionUtils.isNotEmpty(newUser.getClients())) {
			// if the user type is client make an user type entry as client and
			// employee in user types and user clients tables.
			List<UserClients> newUserTypes = newUser.getClients();

			if (newUserTypes.size() == 1 && newUserTypes.get(0).getUserType().equals(GenericConstants.USERTYPE_CLIENT)
					&& CollectionUtils.isNotEmpty(userDTO.getClients())
					&& StringUtils.isNotEmpty(userDTO.getClients().get(0).getEmployeeCode())) {
				RbacEntity role = roleRepository.findByCodeAndType(GenericConstants.USERTYPE_EMPLOYEE,
						UserEnum.ROLE.toString());
				Mapping roleMapping = null;
				if (role.getMappingId() > 0) {
					Optional<Mapping> optional = mappingRepository.findById(role.getMappingId());
					if (optional.isPresent()) {
						roleMapping = optional.get();
					}
				}
				for (String clientCode : clientMap.keySet()) {
					UserClients empUserClient = new UserClients();
					empUserClient.setUserType(GenericConstants.USERTYPE_EMPLOYEE);
					empUserClient.setClientCode(clientCode);
					empUserClient.setIsActive(true);
					empUserClient.setEmployeeCode(userDTO.getClients().get(0).getEmployeeCode());
					empUserClient.setUser(newUser);
					empUserClient.setIsCaliforniaUser(userDTO.getIsCaliforniaUser());
					ClientRole clientRoleV2 = new ClientRole();
					clientRoleV2.setClientCode(clientCode);
					clientRoleV2.setRole(roleMapping);
					clientRoleV2.setUserClient(empUserClient);
					List<ClientRole> clientRoles = Lists.newArrayList();
					clientRoles.add(clientRoleV2);
					empUserClient.setClientRoles(clientRoles);
					newUserTypes.add(empUserClient);
				}
			} else if (newUserTypes.size() == 1
					&& newUserTypes.get(0).getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE)
					&& StringUtils.isNotEmpty(newUserTypes.get(0).getEmployeeCode())
					&& CollectionUtils.isEmpty(newUserTypes.get(0).getClientRoles())) {
				RbacEntity role = roleRepository.findByCodeAndType(GenericConstants.USERTYPE_EMPLOYEE,
						UserEnum.ROLE.toString());
				if (role != null) {
					Mapping roleMapping = null;
					if (role.getMappingId() > 0) {
						Optional<Mapping> optional = mappingRepository.findById(role.getMappingId());
						if (optional.isPresent()) {
							roleMapping = optional.get();
						}
					}
					ClientRole clientRoleV2 = new ClientRole();
					clientRoleV2.setClientCode(newUserTypes.get(0).getClientCode());
					clientRoleV2.setRole(roleMapping);
					clientRoleV2.setUserClient(newUserTypes.get(0));
					List<ClientRole> clientRoles = Lists.newArrayList();
					clientRoles.add(clientRoleV2);
					newUserTypes.get(0).setStartDate(userDTO.getStartDate());
					newUserTypes.get(0).setEndDate(userDTO.getEndDate());
					newUserTypes.get(0).setIsActive(true);
					newUserTypes.get(0).setClientRoles(clientRoles);
					newUserTypes.get(0).setFirstName(userDTO.getFirstName());
					newUserTypes.get(0).setLastName(userDTO.getLastName());
					newUserTypes.get(0).setMobile(userDTO.getMobile());
				}
			}
			newUser.setClients(newUserTypes);
		}
	}
	
	private void saveUserCostCenters(Users user, List<CostCenterDTO> costCenters) {
		String url = boomiHelper.getUrl(BoomiEnum.PRISMIZED_CLIENTS);
		List<PrismizedClientsDTO> prismizedClientsDTOs = webClientTemplate.getForObjectList(url,
				PrismizedClientsDTO.class, boomiHelper.getHeaders());
		LogUtils.infoLog.accept(
				basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_USER_COST_CENTERS),
				String.format("Prismized Clients: %s", prismizedClientsDTOs));
		String costCenterUrl = boomiHelper.getUrl(BoomiEnum.COST_CENTER);
		List<CostCenterDTO> costcenterDTOs = webClientTemplate.getForObjectList(costCenterUrl, CostCenterDTO.class,
				boomiHelper.getHeaders());
		LogUtils.infoLog.accept(
				basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_USER_COST_CENTERS),
				String.format(COST_CENTERS_MSG, costcenterDTOs));
		Map<String, String> costCenterMap = costcenterDTOs.stream()
				.filter(cc -> StringUtils.isNotEmpty(cc.getCode()))
				.collect(Collectors.toMap(CostCenterDTO::getCode, CostCenterDTO::getDescription));
		
		if (CollectionUtils.isNotEmpty(prismizedClientsDTOs)) {
			List<String> prismizedClients = prismizedClientsDTOs.stream()
					.map(PrismizedClientsDTO::getClient_code).collect(Collectors.toList());
			List<String> costCenterCodes = costCenters.stream()
					.filter(cc -> (null != cc.getIsSelected() && cc.getIsSelected().equals(Boolean.TRUE)))
					.map(CostCenterDTO::getCode).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(prismizedClients)) {
				List<CostCenterClientDTO> clientDTOs = getClientsByMultipleCostCenters(Lists.newArrayList());
				updateClientMasterForNewClients(costCenters, clientDTOs, costCenterMap, prismizedClients, costCenterCodes);
			}
		}
		
		List<String> costCenterCodes = Lists.newArrayList();
		if(null != user.getId()) {
			List<UserCostCenterAssociation> costCenterAssocsList = userCostCenterAssociationRepository.findByUserId(user.getId());
			for(UserCostCenterAssociation userCostCenterAssoc : costCenterAssocsList) {
				costCenterCodes.add(userCostCenterAssoc.getCostCenterCode());
			}
		}
		List<UserCostCenterAssociation> costCenterAssocs = Lists.newArrayList();
		costCenters.forEach(costCenter -> {
			if(null != costCenter.getIsSelected() && costCenter.getIsSelected().equals(Boolean.TRUE) && !costCenterCodes.contains(costCenter.getCode())) {
				UserCostCenterAssociation assoc = new UserCostCenterAssociation();
				assoc.setCostCenterCode(costCenter.getCode());
				assoc.setUserType(costCenter.getUserType());
				assoc.setUser(user);
				costCenterAssocs.add(assoc);
			}
		});
		userCostCenterAssociationRepository.saveAll(costCenterAssocs);
	}
	
	private void updateClientMasterForNewClients(List<CostCenterDTO> costCenters, List<CostCenterClientDTO> clientDTOs, 
			Map<String, String> costCenterMap, List<String> prismizedClients, List<String> costCenterCodes) {
		Map<String, Set<String>> costCenterClientMap = clientDTOs.stream()
				.collect(Collectors.groupingBy(CostCenterClientDTO::getCostCenter, 
						Collectors.mapping(CostCenterClientDTO::getClientId, Collectors.toSet())));
		List<ClientMaster> clientMasters = clientMasterRepository.findAll();
		Map<String, Set<String>> dbCostCenterMap = clientMasters.stream()
				.collect(Collectors.groupingBy(ClientMaster::getCostCenterCode, 
						Collectors.mapping(ClientMaster::getClientCode, Collectors.toSet())));
		for(String costCenterCode : costCenterCodes) {
			if(costCenterClientMap.containsKey(costCenterCode)) {
				Set<String> prismizedClientsInCostCenter = costCenterClientMap.get(costCenterCode).stream()
						.filter(cc -> prismizedClients.contains(cc)).collect(Collectors.toSet());
				if(CollectionUtils.isEmpty(prismizedClientsInCostCenter)) {
					costCenters.forEach(costCenter -> {
						if(costCenter.getCode().equals(costCenterCode)) {
							costCenter.setIsSelected(false);
						}
					});
				} else if(dbCostCenterMap.containsKey(costCenterCode)) {
					saveNewClientInCostCenter(dbCostCenterMap, prismizedClientsInCostCenter, clientDTOs, costCenterMap, costCenterCode);
				} else if(!dbCostCenterMap.containsKey(costCenterCode)) {
					saveNewCostCenter(clientDTOs, prismizedClientsInCostCenter, costCenterMap, costCenterCode);
				}
			}
		}
	}
	
	private void saveNewClientInCostCenter(Map<String, Set<String>> dbCostCenterMap, Set<String> prismizedClientsInCostCenter, 
			List<CostCenterClientDTO> clientDTOs, Map<String, String> costCenterMap, String costCenterCode) {
		if(!dbCostCenterMap.get(costCenterCode).containsAll(prismizedClientsInCostCenter)) {
			for(CostCenterClientDTO clientDTO : clientDTOs) {
				if(prismizedClientsInCostCenter.contains(clientDTO.getClientId())
						&& !dbCostCenterMap.get(costCenterCode).contains(clientDTO.getClientId())) {
					saveClientMaster(clientDTO.getClientId(), clientDTO.getClientName(), costCenterCode, costCenterMap.get(costCenterCode));
				}
			}
		}
	}
	
	private void saveNewCostCenter(List<CostCenterClientDTO> clientDTOs, Set<String> prismizedClientsInCostCenter, Map<String, String> costCenterMap, String costCenterCode) {
		List<ClientMaster> clientMasterList = Lists.newArrayList();
		for(CostCenterClientDTO clientDTO : clientDTOs) {
			if(prismizedClientsInCostCenter.contains(clientDTO.getClientId())) {
				ClientMaster clientMaster = new ClientMaster();
				clientMaster.setClientCode(clientDTO.getClientId());
				clientMaster.setClientName(clientDTO.getClientName());
				clientMaster.setCostCenterCode(costCenterCode);
				clientMaster.setCostCenterDescription(costCenterMap.get(costCenterCode));
				clientMaster.setModifiedOn(LocalDateTime.now());
				clientMasterList.add(clientMaster);
			}
		}
		saveClientMaster(clientMasterList);
	}

	public ClientMaster getClientMaster(String code) {
		return clientMasterRepository.findByClientCode(code);
	}

	public ClientMaster saveClientMaster(String code, String name, String centerCode, String centerName) {
		ClientMaster clientMaster = new ClientMaster();
		clientMaster.setClientCode(code);
		clientMaster.setClientName(name);
		clientMaster.setCostCenterCode(centerCode);
		clientMaster.setCostCenterDescription(centerName);
		return clientMasterRepository.save(clientMaster);
	}

	private void deleteRecursiveUserClients(List<Long> removalIds) {
		if(CollectionUtils.isNotEmpty(removalIds)) {
			while(removalIds.size() > 2000) {
				List<Long> removalSubSet = removalIds.subList(0, 2000);
				deleteExistingUserClients(removalSubSet);
				removalIds = removalIds.subList(2000, removalIds.size());
			}
			deleteExistingUserClients(removalIds);
		}
	}
	
	private void deleteExistingUserClients(List<Long> removalIds) {
		userSecurityRepository.deleteByUserClient_IdIn(removalIds);
		clientRoleRepository.deleteByUserClientIdIn(removalIds);
		userClientRepository.deleteByIdIn(removalIds);
	}

	private void validateAndAssignRoles(Users newUser, UsersDTO userDTOV2) {
		if (!CollectionUtils.isEmpty(userDTOV2.getRoles())) {
			List<Long> roleIds = new ArrayList<>();
			userDTOV2.getRoles().forEach(role -> {
				roleIds.add(role.getId());
			});
			List<RbacEntity> roleList = roleRepository.findAllById(roleIds);
			if (CollectionUtils.isEmpty(roleList)) {
				LogUtils.basicErrorLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_USER),
						String.format("Unable to create user due to invalid roles : %s", roleIds));

				throw new ValidationException(new ErrorDetails(ErrorCodes.SQL002));
			}
			newUser.setRoles(roleList);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UsersDTO getUserById(Long userId, String token, String clientCode, UserPrincipal userPrincipal) {
		UsersDTO userDTOV2 = null;
		try {
			List<UserClients> userClientList = null;
			if (userPrincipal.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER)
					|| userPrincipal.getUserType().equals(GenericConstants.USERTYPE_BRANCH)) {
				Users user = userRepository.findById(userId).orElse(null);
				List<UserClients> userClients = userClientRepository.findByUser_Id(user.getId());
				user.setClients(userClients);
				populateCostCenterUsers(user);
				userClientList = user.getClients();
			} else {
				userClientList = userClientRepository.findByClientCodeAndUser_Id(clientCode, userId);
			}
			Set<String> clients = Sets.newHashSet();
			if (CollectionUtils.isNotEmpty(userClientList)) {
				Users user = userClientList.get(0).getUser();
				if (user.getEmail().equals(GenericConstants.SUPER_ADMIN)
						|| user.getEmail().equals(GenericConstants.INTEGRATION_ADMIN)
						|| userPrincipal.getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE)
						|| userPrincipal.getUserType().equals(GenericConstants.USERTYPE_EXTERNAL)
						|| userPrincipal.getUserType().equals(GenericConstants.USERTYPE_NEWHIRE)) {
					throw new UnauthorizedAccessException(
							new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
				}
				userDTOV2 = userMapper.userToUserDTO(user);
				user.setClients(userClientList);
				Map<String, Map<String, Set<String>>> filterMap = Maps.newHashMap();
				List<UserSecurityDTOV2> filterDTOs = Lists.newArrayList();
				boolean updateNameFlag=false;
				String userType = null;
				for (UserClients userClients : userClientList) {
					clients.add(userClients.getClientCode());
					if (userClients.getClientCode().equals(clientCode) && (!updateNameFlag
							|| (updateNameFlag && !userClients.getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE)
									&& !userClients.getUserType().equals(GenericConstants.USERTYPE_NEWHIRE)))) {
						if (StringUtils.isEmpty(userType)) {
							userType = userClients.getUserType();
						}
						updateNameFlag=true;
						userDTOV2.setFirstName(userClients.getFirstName());
						userDTOV2.setLastName(userClients.getLastName());
						userDTOV2.setFullName(userClients.getFirstName() + " " + userClients.getLastName());
						userDTOV2.setMobile(userClients.getMobile());

					}
					if (userClients.getUserType().equals(GenericConstants.USERTYPE_CLIENT)
							|| userClients.getUserType().equals(GenericConstants.USERTYPE_EXTERNAL)) {
						List<UserSecurity> userFilters = userSecurityRepository.findByUserClient_Id(userClients.getId());
						if (CollectionUtils.isNotEmpty(userFilters)) {
							for (UserSecurity filter : userFilters) {
								if (filterMap.containsKey(userClients.getClientCode())) {
									if (filterMap.get(userClients.getClientCode()) != null
											&& filterMap.get(userClients.getClientCode()).containsKey(filter.getType())) {
										filterMap.get(userClients.getClientCode()).get(filter.getType())
												.add(filter.getValue());
									} else {
										Set<String> values = Sets.newHashSet();
										values.add(filter.getValue());
										filterMap.get(userClients.getClientCode()).put(filter.getType(), values);
									}
								} else {
									Set<String> values = Sets.newHashSet();
									values.add(filter.getValue());
									Map<String, Set<String>> clientMap = Maps.newHashMap();
									clientMap.put(filter.getType(), values);
									filterMap.put(userClients.getClientCode(), clientMap);
								}
							}
						}
					}
				}
				if (!clients.contains(clientCode)) {
					throw new UnauthorizedAccessException(
							new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
				}
				for (Entry<String, Map<String, Set<String>>> key : filterMap.entrySet()) {
					for (String type : filterMap.get(key.getKey()).keySet()) {
						UserSecurityDTOV2 filter = new UserSecurityDTOV2();
						filter.setClientCode(key.getKey());
						filter.setType(type);
						filter.setValue(filterMap.get(key.getKey()).get(type));
						filterDTOs.add(filter);
					}
				}
				userDTOV2.setFilter(filterDTOs);
				userDTOV2.setStatus(false);
				List<String> userTypeList = getUserTypes();
				// filter the user type
				List<ClientDTOV2> filteredUserTypesList = Lists.newArrayList();
				if (null != userDTOV2) {
					if (CollectionUtils.isNotEmpty(userDTOV2.getClients())) {
						if (CollectionUtils.isNotEmpty(user.getClients())) {
							List<UserClients> filteredUserClientList = Lists.newArrayList();
							for (UserClients client : user.getClients()) {
								if (userTypeList.contains(client.getUserType())
										&& ((userPrincipal.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER))
										|| (userPrincipal.getUserType().equals(GenericConstants.USERTYPE_CLIENT)
										&& getClientUserNotShownUserTypes(client.getUserType()))
										|| (userPrincipal.getUserType().equals(GenericConstants.USERTYPE_BRANCH)
										&& getBranchUserNotShownUserTypes(client.getUserType())))) {
									filteredUserClientList.add(client);
									if (client.getClientCode().equals(clientCode)) {
										if (client.getEndDate() != null) {
											userDTOV2.setEndDate(client.getEndDate());
										}
										if (client.getStartDate() != null) {
											userDTOV2.setStartDate(client.getStartDate());
										}
									}
								}
								for (ClientDTOV2 clientDTO : userDTOV2.getClients()) {
									if (null != client.getId() && client.getId().equals(clientDTO.getId())) {
										clientDTO.setUserType(client.getUserType());
										break;
									}
								}
							}
							user.setClients(filteredUserClientList);
							Set<String> filterClients = filteredUserClientList.stream().map(UserClients::getClientCode)
									.collect(Collectors.toSet());
							if (!filterClients.contains(clientCode)) {
								throw new UnauthorizedAccessException(
										new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
							}
						}

						userDTOV2.setStatus(userDTOV2.getClients().get(0).getIsActive());
						for (ClientDTOV2 client : userDTOV2.getClients()) {
							if (userTypeList.contains(client.getUserType())
									&& ((userPrincipal.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER))
									|| (userPrincipal.getUserType().equals(GenericConstants.USERTYPE_CLIENT)
									&& getClientUserNotShownUserTypes(client.getUserType()))
									|| (userPrincipal.getUserType().equals(GenericConstants.USERTYPE_BRANCH)
									&& getBranchUserNotShownUserTypes(client.getUserType())))) {
								filteredUserTypesList.add(client);
								if (clientCode.equals(client.getClientCode())) {
									userDTOV2.setStatus((client.getIsActive()));
								}
							}
						}
					}
					userDTOV2.setClients(filteredUserTypesList);
				}
				if (CollectionUtils.isNotEmpty(userDTOV2.getRoles())) {
					userDTOV2.getRoles().forEach(role -> {
						role.setIsSelected(Boolean.TRUE);
					});
				}
				boolean isVocOrBranchUser = checkUserType(userDTOV2);
				if (isVocOrBranchUser) {
					buildAllRoles(user, userDTOV2);
					if (StringUtils.isEmpty(userType)) {
						userType = user.getClients().get(0).getUserType();
					}
					populateUserCostCenters(user, userDTOV2, userType);
				}
				if(CollectionUtils.isEmpty(userDTOV2.getCostCenters())) {
					buildClientRoles(user, userDTOV2);
				}
			}
		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_BY_ID), e,
					"Error occurred while getting user by Id");
		}
		String email =encryptAndDecryptUtil.encrypt(userDTOV2.getEmail());
		userDTOV2.setEmail(email);
		return userDTOV2;
	}
	
	private void populateUserCostCenters(Users user, UsersDTO userDTOV2, String userType) {
		if ((userType.equals(GenericConstants.USERTYPE_VANCOUVER) || userType.equals(GenericConstants.USERTYPE_BRANCH)) 
				&& CollectionUtils.isNotEmpty(user.getClients())) {
			List<CostCenterDTO> costCenters = Lists.newArrayList();
			String url = boomiHelper.getUrl(BoomiEnum.PRISMIZED_CLIENTS);
			List<PrismizedClientsDTO> prismizedClientsDTOs = webClientTemplate.getForObjectList(url,
					PrismizedClientsDTO.class, boomiHelper.getHeaders());
			LogUtils.infoLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							"populateUserCostCenters"),
					String.format(COST_CENTERS_MSG, prismizedClientsDTOs));
			List<String> prismisedClientCodes = prismizedClientsDTOs.stream()
					.map(PrismizedClientsDTO::getClient_code).collect(Collectors.toList());
			Map<String, Set<String>> dbCostCenterMap = user.getClients().stream()
					.filter(cc -> null != cc.getClient())
					.collect(Collectors.groupingBy(cc -> cc.getClient().getCostCenterCode(), 
							Collectors.mapping(cc -> cc.getClient().getClientCode(), Collectors.toSet())));
			Map<String, String> costCenterMap = Maps.newHashMap();
			for(UserClients uc : user.getClients()) {
				if(null != uc.getClient()) {
					costCenterMap.put(uc.getClient().getCostCenterCode(), uc.getClient().getCostCenterDescription());
				}
			}
			populateUserCostCenters(userType, costCenters, prismisedClientCodes, dbCostCenterMap, costCenterMap);
			userDTOV2.setCostCenters(costCenters);
			List<ClientDTOV2> filteredList = userDTOV2.getClients().stream()
					.filter(cc -> (!cc.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER) && !cc.getUserType().equals(GenericConstants.USERTYPE_BRANCH)))
					.collect(Collectors.toList());
			userDTOV2.setClients(filteredList);
		}
	}

	private void populateUserCostCenters(String userType, List<CostCenterDTO> costCenters,
			List<String> prismisedClientCodes, Map<String, Set<String>> dbCostCenterMap,
			Map<String, String> costCenterMap) {
		List<CostCenterClientDTO> allClientDTOs = getClientsByMultipleCostCenters(Lists.newArrayList());
		String costCenterUrl = boomiHelper.getUrl(BoomiEnum.COST_CENTER);
		List<CostCenterDTO> costcenterDTOs = webClientTemplate.getForObjectList(costCenterUrl, CostCenterDTO.class,
				boomiHelper.getHeaders());
		costcenterDTOs.forEach(dto->{
			CostCenterDTO costCenterDTO = new CostCenterDTO();
			costCenterDTO.setCode(dto.getCode());
			costCenterDTO.setDescription(dto.getDescription());
			costCenterDTO.setIsSelected(costCenterMap.containsKey(dto.getCode()));
			costCenterDTO.setUserType(userType);
			if(userType.equals(GenericConstants.USERTYPE_BRANCH)) {
			List<CostCenterClientDTO> clientDTOs = Lists.newArrayList();
			for(CostCenterClientDTO clientDTO : allClientDTOs) {
				if(clientDTO.getCostCenter().equals(dto.getCode()) && prismisedClientCodes.contains(clientDTO.getClientId())) {
					if (null != dbCostCenterMap.get(dto.getCode())) {
						clientDTO.setIsSelected(dbCostCenterMap.get(dto.getCode()).contains(clientDTO.getClientId()));
					}
					clientDTOs.add(clientDTO);
				}
			}
			costCenterDTO.setClientList(clientDTOs);
			}
			
			costCenters.add(costCenterDTO);
		});
	}

	private List<String> getUserTypes() {
		List<String> userTypeList = new ArrayList<>();
		userTypeList.add(GenericConstants.USERTYPE_VANCOUVER);
		userTypeList.add(GenericConstants.USERTYPE_BRANCH);
		userTypeList.add(GenericConstants.USERTYPE_CLIENT);
		userTypeList.add(GenericConstants.USERTYPE_EXTERNAL);
		return userTypeList;
	}

	private boolean getNotShownUserTypes(String userType) {
		if (null != userType && !userType.equals(GenericConstants.USERTYPE_NEWHIRE)
				&& !userType.equals(GenericConstants.USERTYPE_EMPLOYEE)) {
			return true;
		}
		return false;
	}

	private boolean getBranchUserNotShownUserTypes(String buserType) {
		return (null != buserType && !buserType.equals(GenericConstants.USERTYPE_VANCOUVER)
				&& !buserType.equals(GenericConstants.USERTYPE_BRANCH)
				&& !buserType.equals(GenericConstants.USERTYPE_NEWHIRE)
				&& !buserType.equals(GenericConstants.USERTYPE_EMPLOYEE));
	}

	private boolean getClientUserNotShownUserTypes(String cuserType) {
		return (null != cuserType && !cuserType.equals(GenericConstants.USERTYPE_VANCOUVER)
				&& !cuserType.equals(GenericConstants.USERTYPE_BRANCH)
				&& !cuserType.equals(GenericConstants.USERTYPE_NEWHIRE)
				&& !cuserType.equals(GenericConstants.USERTYPE_EMPLOYEE));
	}

	/**
	 * Sets Client Role Objects to UserDTO by setting isSelected flag.
	 *
	 * @param user
	 * @param userDTOV2
	 */
	private void buildClientRoles(Users user, UsersDTO userDTOV2) {
		// Its a list object, but at any point of time, only one Client details
		// will be get.
		if (CollectionUtils.isNotEmpty(user.getClients())) {
			Set<Long> clientRoleIdSet = new HashSet<>();
			Map<String, Set<Long>> clientRoleIdMap = Maps.newHashMap();
			userDTOV2.setClients(new ArrayList<>());
			List<UserClients> userClients = user.getClients();
			if (CollectionUtils.isNotEmpty(userClients)) {
				userClients.forEach(userClient -> {
					ClientDTOV2 clientDTOV2 = new ClientDTOV2();
					clientDTOV2.setClientCode(userClient.getClientCode());
					clientDTOV2.setClientName(userClient.getClient().getClientName());
					clientDTOV2.setCostCenterCode(userClient.getClient().getCostCenterCode());
					clientDTOV2.setCostCenterDescription(userClient.getClient().getCostCenterDescription());
					clientDTOV2.setIsActive(userClient.getIsActive());
					clientDTOV2.setId(userClient.getId());
					userDTOV2.getClients().add(clientDTOV2);
					if (CollectionUtils.isNotEmpty(userClient.getClientRoles())) {
						userClient.getClientRoles().forEach(clientRole -> {
							clientRoleIdSet.add(clientRole.getId());
							if (clientRoleIdMap.containsKey(userClient.getClientCode())) {
								clientRoleIdMap.get(userClient.getClientCode()).add(clientRole.getId());
							} else {
								Set<Long> idSet = Sets.newHashSet();
								idSet.add(clientRole.getId());
								clientRoleIdMap.put(userClient.getClientCode(), idSet);
							}
						});
					}
				});
				// Invoke ClientRole service to get all client roles of a client
				// by passing client code.
				Map<String, List<ClientRoleDTOV2>> clientRoleMap = Maps.newHashMap();

				getAllClientRolesByClientCode(clientRoleIdMap, clientRoleMap);

				// to assign client roles to userDTO and to set isSelected flag
				// value
				assignUserClientRole(clientRoleMap, userDTOV2, clientRoleIdSet);
			}
		}
	}

	private void getAllClientRolesByClientCode(Map<String, Set<Long>> clientRoleIdMap,Map<String, List<ClientRoleDTOV2>> clientRoleMap) {
		for (Entry<String, Set<Long>> clientCode : clientRoleIdMap.entrySet()) {
			List<ClientRole> roles = clientRoleRepository.findAllById(clientRoleIdMap.get(clientCode.getKey()));
			if (CollectionUtils.isNotEmpty(roles)) {
				List<ClientRoleDTOV2> roleDTOs = Lists.newArrayList();
				for (ClientRole role : roles) {
					ClientRoleDTOV2 clientRoleDTO = new ClientRoleDTOV2();
					clientRoleDTO.setId(role.getId());
					RbacEntity rbacEntity = roleRepository.findByMappingId(role.getRole().getId());
					if(null != rbacEntity) {
						clientRoleDTO.setRoleId(rbacEntity.getId());
					}
					clientRoleDTO.setCode(role.getRole().getCode());
					clientRoleDTO.setName(role.getRole().getName());
					clientRoleDTO.setIsActive(role.getRole().getStatus());
					roleDTOs.add(clientRoleDTO);
				}
				clientRoleMap.put(clientCode.getKey(), roleDTOs);
			}
		}
	}

	/**
	 * Builds UserClientRolesDTO object
	 *
	 * @param clientRoleMap
	 * @param userDTOV2
	 * @param clientRoleIdSet
	 */
	private void assignUserClientRole(Map<String, List<ClientRoleDTOV2>> clientRoleMap, UsersDTO userDTOV2,
									  Set<Long> clientRoleIdSet) {
		if (null != clientRoleMap && !clientRoleMap.isEmpty()) {
			for (ClientDTOV2 userClient : userDTOV2.getClients()) {
				if (CollectionUtils.isNotEmpty(clientRoleMap.get(userClient.getClientCode()))) {
					List<UserClientRoleDTOV2> userClientRoles = new ArrayList<>();
					clientRoleMap.get(userClient.getClientCode()).forEach(clientRole -> {
						UserClientRoleDTOV2 userClientRoleDTO = new UserClientRoleDTOV2();
						userClientRoleDTO.setId(clientRole.getId());
						userClientRoleDTO.setRoleId(clientRole.getRoleId());
						userClientRoleDTO.setCode(clientRole.getCode());
						userClientRoleDTO.setName(clientRole.getName());
						if (clientRoleIdSet.contains(clientRole.getId())) {
							userClientRoleDTO.setIsSelected(Boolean.TRUE);
						}
						userClientRoles.add(userClientRoleDTO);
					});
					userClient.setClientRoles(userClientRoles);
				}
			}
		}
	}

	/**
	 * Make a rest service call to Client module to get all cost centers
	 *
	 * @param token
	 * @return
	 */
	public List<CostCenterDTO> getAllCostCenters(String token) {
		List<CostCenterDTO> costCenters = Lists.newArrayList();
		try {
			Map<String, String> headers = new HashMap<>();
			headers.put(GenericConstants.AUTH_TOKEN_HDR, token);
			String costCentersUrl = String.format(commonUrl, "COST_CENTER", "");
			String strCostCenters = restClient.getForString(costCentersUrl, Collections.emptyMap(), headers);
			if (StringUtils.isNotEmpty(strCostCenters)) {
				JsonElement jsonElement = JsonParser.parseString(strCostCenters);
				if (jsonElement.isJsonArray()) {
					JsonArray jsonArray = jsonElement.getAsJsonArray();
					jsonArray.forEach(jsonObj -> {
						try {
							JsonNode jsonNode = mapper.readTree(jsonObj.toString());
							String code = jsonNode.get("code").asText();
							String description = jsonNode.get("description").asText();
							CostCenterDTO costCenterDTO = new CostCenterDTO();
							costCenterDTO.setCode(code);
							costCenterDTO.setDescription(description);
							costCenters.add(costCenterDTO);
							String costCenterUrl = String.format(commonUrl, MethodNames.COST_CENTER_CLIENTS, code);
							String strCostCenter = restClient.getForString(costCenterUrl, Collections.emptyMap(),
									headers);
							if (StringUtils.isNotEmpty(strCostCenter)) {
								addClientsToCostCenter(costCenterDTO, strCostCenters, code);
							}
						} catch (Exception e) {
							LogUtils.basicErrorLog.accept(
									basicMethodInfo.apply(getClass().getCanonicalName(), "getAllCostCenters"),
									"Error occurred while getting all cost centers");
						}
					});
				}
			}
		} catch (Exception ex) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_BY_ID), ex,
					"Error occurred while getting all the cost centers");
		}
		return costCenters;
	}

	private void addClientsToCostCenter(CostCenterDTO costCenterDTO,String strCostCenter,String code) {

		costCenterDTO.setClients(Lists.newArrayList());
		JsonElement centerElement = JsonParser.parseString(strCostCenter);
		if (centerElement.isJsonArray()) {
			JsonArray centerArray = centerElement.getAsJsonArray();
			centerArray.forEach(centerObj -> {
				try {
					JsonNode centerNode = mapper.readTree(centerObj.toString());
					if (centerNode.get(MethodNames.STATUS) != null
							&& centerNode.get(MethodNames.STATUS).asText().equals("A")) {
						ClientDTO clientDTO = new ClientDTO();
						clientDTO.setCode(centerNode.get(MethodNames.CLIENT_ID).asText());
						clientDTO
								.setLegalName(centerNode.get(MethodNames.CLIENT_NAME).asText());
						costCenterDTO.getClients().add(clientDTO);
					}
				} catch (Exception e) {
					LogUtils.basicErrorLog.accept(
							basicMethodInfo.apply(getClass().getCanonicalName(),
									"getAllCostCenters"),
							String.format(
									"Error occurred while getting cost center for code %s",
									code));
				}
			});
		}

	}

	/**
	 * During user creation, user assigned roles will only get persisted in DB.
	 * While giving User information, we need to give all existing roles with
	 * flag isSelected(which means its already been assigned to user)
	 *
	 * @param user
	 * @param userDTOV2
	 */
	private void buildAllRoles(Users user, UsersDTO userDTOV2) {
		if (CollectionUtils.isNotEmpty(user.getRoles())) {
			Set<Long> roleIdSet = new HashSet<>();
			userDTOV2.setRoles(new ArrayList<>());
			user.getRoles().forEach(role -> {
				roleIdSet.add(role.getId());
			});
			List<RbacEntity> allRoleList = roleRepository.findByType(UserEnum.ROLE.toString());
			if (CollectionUtils.isNotEmpty(allRoleList)) {
				allRoleList.forEach(role -> {
					UserClientRoleDTOV2 roleDTO = new UserClientRoleDTOV2();
					roleDTO.setId(role.getId());
					roleDTO.setCode(role.getCode());
					roleDTO.setName(role.getName());
					roleDTO.setDescription(role.getDescription());
					if (roleIdSet.contains(role.getId())) {
						roleDTO.setIsSelected(Boolean.TRUE);
					}
					userDTOV2.getRoles().add(roleDTO);
				});
			}
		}
	}

	@Override
	@Transactional
	public void deleteClientFromUser(Long userId, String clientCode) {
		try {
			List<UserClients> userClients = userClientRepository.findByClientCode(clientCode);
			for (UserClients userClientsV2 : userClients) {
				if (userClientsV2.getUserType() != null && userClientsV2.getUser() != null
						&& userClientsV2.getUser().getId().equals(userId)) {
					userClientRepository.delete(userClientsV2);
				}
			}
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_USER),
					exception,
					String.format(
							"Error occurred while deleting the client form user with userId is: %s and clientCode is : %s ",
							userId, clientCode));
		}
	}

	/**
	 * This API will give client admin user list
	 */
	@Override
	public List<UsersDTO> getAllClientUsersByClientCode(String clientCode) {
		List<UsersDTO> userDTOList = null;
		List<UsersDTO> userClientDTOList = Lists.newArrayList();
		List<UsersDTO> filtertedList = Lists.newArrayList();
		try {
			Sort sort = Sort.by(Sort.Direction.DESC, MethodNames.MODIFIED_ON);
			List<Users> userList = Lists.newArrayList();
			List<UserClients> userClients = userClientRepository.findByClientCode(clientCode, sort);
			for (UserClients userClient : userClients) {
				if (userClient.getUser() != null) {
					userList.add(userClient.getUser());
				}
			}
			if (CollectionUtils.isEmpty(userList)) {
				LogUtils.basicErrorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(),
						MethodNames.GET_ALL_CLIENT_USERS_BY_CLIENTCODE), "No Client user records found");

				throw new ValidationException(new ErrorDetails(ErrorCodes.UR003));
			}

			userDTOList = userMapper.userListToUserDTOList(userList);
			// adding first name and last name to full name property
			userDTOList.forEach(user -> {
				user.setFullName(user.getFirstName() + " " + user.getLastName());
				// returning client and external user in the response
				List<ClientDTOV2> userTypes = Lists.newArrayList();
				user.setStatus(false);
				user.getClients().forEach(client -> {
					if (client.getUserType().equals(GenericConstants.USERTYPE_CLIENT)
							|| client.getUserType().equals(GenericConstants.USERTYPE_EXTERNAL)) {
						userTypes.add(client);
						user.setStatus(client.getIsActive());
					}

				});
				if (CollectionUtils.isNotEmpty(userTypes)) {
					user.setClients(userTypes);
					userClientDTOList.add(user);
				}
			});

			// If user is assigned for multiple clients, userClientDTOList will
			// contains duplicate user list.below will filter user list object.
			Set<Long> idSet = new HashSet<>();
			if (CollectionUtils.isNotEmpty(userClientDTOList)) {
				filtertedList = userClientDTOList.stream().filter(e -> idSet.add(e.getId()))
						.collect(Collectors.toList());
			}

		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_USERS),
					exception, "Error occured while retrieving all client admin users by client code");
		}

		return filtertedList;
	}

	/**
	 * This API will give client user list of particular user type
	 */
	@Override
	public List<UsersDTO> getUsersByClientCodeAndType(String clientCode, String userType) {
		List<UsersDTO> userDTOList = null;
		List<UsersDTO> userClientDTOList = Lists.newArrayList();
		List<UsersDTO> filteredList = Lists.newArrayList();
		try {
			Sort sort = Sort.by(Sort.Direction.DESC, MethodNames.MODIFIED_ON);
			List<Users> userList = Lists.newArrayList();
			List<UserClients> userClients = userClientRepository.findByClientCode(clientCode, sort);
			userClients.addAll(populateCostCenterList(clientCode));
			for (UserClients userClient : userClients) {
				if (userClient.getUser() != null) {
					userList.add(userClient.getUser());
				}
			}
			if (CollectionUtils.isEmpty(userList)) {
				LogUtils.basicErrorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(),
						MethodNames.GET_ALL_CLIENT_USERS_BY_CLIENTCODE), "No user records found");

				throw new ValidationException(new ErrorDetails(ErrorCodes.UR003));
			}

			userDTOList = userMapper.userListToUserDTOList(userList);
			// adding first name and last name to full name property
			userDTOList.forEach(user -> {
				user.setFullName(user.getFirstName() + " " + user.getLastName());
				// returning client and external user in the response
				List<ClientDTOV2> userTypes = Lists.newArrayList();
				user.setStatus(false);
				user.getClients().forEach(client -> {
					if (client.getUserType().equals(userType)) {
						userTypes.add(client);
						user.setUserType(userType);
						user.setStatus(client.getIsActive());
					}

				});
				if (CollectionUtils.isNotEmpty(userTypes)) {
					user.setClients(userTypes);
					userClientDTOList.add(user);
				}
			});

			// If user is assigned for multiple clients, userClientDTOList will
			// contains duplicate user list.below will filter user list object.
			Set<Long> idSet = new HashSet<>();
			if (CollectionUtils.isNotEmpty(userClientDTOList)) {
				filteredList = userClientDTOList.stream().filter(e -> idSet.add(e.getId()))
						.collect(Collectors.toList());
			}
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_USERS),
					exception, "Error occured while retrieving all client admin users by client code and type");
		}
		return filteredList;
	}

	@Override
	public List<UsersDTO> getUsersByClientCodeAndTypeAndCreatedBy(String clientCode, String userType, String email) {
		List<UsersDTO> userDTOList = Lists.newArrayList();
		List<UsersDTO> userDTOs = Lists.newArrayList();
		List<UsersDTO> filteredList = Lists.newArrayList();

		byte[] decodedArray = org.apache.commons.codec.binary.Base64.decodeBase64(email);
		String decodedEmail = new String(decodedArray);

		try {
			Users parentUser = userRepository.findByEmail(decodedEmail);
			if (parentUser == null) {
				return filteredList;
			}
			List<UserClients> userClients = userClientRepository.findByClientCode(clientCode);
			userClients.addAll(populateCostCenterList(clientCode));

			switch (userType) {
				case GenericConstants.USERTYPE_VANCOUVER:
					addVancouverUsers(userDTOs, userClients);
					break;
				case GenericConstants.USERTYPE_BRANCH:
					addBranchUsers(userDTOs, userClients, userType);
					break;
				case GenericConstants.USERTYPE_CLIENT:
					addClientUsers(userDTOs, userClients);
					break;
				default:
					break;
			}

			checkNoUsersFoundValidation(userDTOs);

			// adding first name and last name to full name property
			if (CollectionUtils.isNotEmpty(userDTOs)) {
				constructUserDTOFullName(userDTOs, clientCode, userDTOList);
			}

			// If user is assigned for multiple clients, userClientDTOList will
			// contains duplicate user list.below will filter user list object.
			Set<Long> idSet = new HashSet<>();
			if (CollectionUtils.isNotEmpty(userDTOList)) {
				filteredList = userDTOList.stream().filter(e -> idSet.add(e.getId())).collect(Collectors.toList());
			}
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_USERSBY_CLIENTCODE_AND_TYPE_AND_CREATEDBY),
					exception, "Error occured while retriving all client users");
		}
		return filteredList;
	}
	
	private List<UserClients> populateCostCenterList(String clientCode) {
		List<UserClients> userClientList = Lists.newArrayList();
		ClientMaster clientMaster = clientMasterRepository.findByClientCode(clientCode);
		List<UserCostCenterAssociation> costCenterAssocs = userCostCenterAssociationRepository.findByCostCenterCode(clientMaster.getCostCenterCode());
		for(UserCostCenterAssociation assoc : costCenterAssocs) {
			Users user = assoc.getUser();
			if(null != user) {
				UserClients userClient = new UserClients();
				userClient.setUser(user);
				userClient.setUserType(assoc.getUserType());
				userClient.setClientCode(clientMaster.getClientCode());
				if(StringUtils.isNotEmpty(user.getDefaultClient()) 
						&& clientMaster.getClientCode().equals(user.getDefaultClient())) {
					userClient.setIsPrimary(true);
				}
				userClient.setIsActive(user.getStatus());
				userClient.setFirstName(user.getFirstName());
				userClient.setLastName(user.getLastName());
				userClient.setMobile(user.getMobile());
				userClient.setClient(clientMaster);
				userClientList.add(userClient);
			}
		}
		return userClientList;
	}

	private void addVancouverUsers(List<UsersDTO> userDTOs,List<UserClients> userClients) {
		for (UserClients userClient : userClients) {
			if (userClient.getUser() != null && getNotShownUserTypes(userClient.getUserType())) {
				Users user = userClient.getUser();
				user.setClients(Lists.newArrayList(userClient));
				UsersDTO userDTO = userMapper.userToUserDTO(user);
				userDTO.setFirstName(userClient.getFirstName());
				userDTO.setLastName(userClient.getLastName());
				userDTO.setMobile(userClient.getMobile());
				userDTOs.add(userDTO);
			}
		}
	}

	private void addBranchUsers(List<UsersDTO> userDTOs,List<UserClients> userClients,String userType) {
		for (UserClients userClient : userClients) {
			if (userClient.getUser() != null && userType.equals(GenericConstants.USERTYPE_BRANCH.toString())
					&& (getBranchUserNotShownUserTypes(userClient.getUserType()))) {
				Users user = userClient.getUser();
				user.setClients(Lists.newArrayList(userClient));
				UsersDTO userDTO = userMapper.userToUserDTO(user);
				userDTO.setFirstName(userClient.getFirstName());
				userDTO.setLastName(userClient.getLastName());
				userDTO.setMobile(userClient.getMobile());
				userDTOs.add(userDTO);
			}
		}
	}

	private void addClientUsers(List<UsersDTO> userDTOs,List<UserClients> userClients) {
		for (UserClients userClient : userClients) {
			if (userClient.getUser() != null && getClientUserNotShownUserTypes(userClient.getUserType())) {
				Users user = userClient.getUser();
				user.setClients(Lists.newArrayList(userClient));
				UsersDTO userDTO = userMapper.userToUserDTO(user);
				userDTO.setFirstName(userClient.getFirstName());
				userDTO.setLastName(userClient.getLastName());
				userDTO.setMobile(userClient.getMobile());
				userDTOs.add(userDTO);
			}
		}
	}

	private void constructUserDTOFullName(List<UsersDTO> userDTOs,String clientCode,List<UsersDTO> userDTOList) {

		for (UsersDTO user : userDTOs) {
			user.setFullName(user.getFirstName() + " " + user.getLastName());
			boolean hasClient = false;
			if (CollectionUtils.isNotEmpty(user.getClients())) {
				for (ClientDTOV2 client : user.getClients()) {
					if (clientCode.equals(client.getClientCode())) {
						user.setStatus(client.getIsActive());
						user.setUserType(client.getUserType());
						hasClient = true;
						break;
					}
				}
			}
			if (!hasClient) {
				user.setStatus(user.getClients().get(0).getIsActive());
				user.setUserType(user.getClients().get(0).getUserType());
			}

			userDTOList.add(user);
		}

	}

	private void checkNoUsersFoundValidation(List<UsersDTO> userDTOs) {
		if (CollectionUtils.isEmpty(userDTOs)) {
			LogUtils.basicErrorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(),
					MethodNames.GET_ALL_CLIENT_USERS_BY_CLIENTCODE), "No User records found");

			throw new ValidationException(new ErrorDetails(ErrorCodes.UR003));
		}
	}
	
	private List<ClientMaster> populateCostCenterUsers(Users user) {
		List<ClientMaster> clientMasterEntries = Lists.newArrayList();
		List<UserCostCenterAssociation> costCenterAssocs = userCostCenterAssociationRepository.findByUserId(user.getId());
		if (CollectionUtils.isNotEmpty(costCenterAssocs)) {
			List<UserClients> userClientList = Lists.newArrayList();
			Set<String> clientCodeSet = Sets.newHashSet();
			for(UserCostCenterAssociation assoc : costCenterAssocs) {
				List<ClientMaster> clientMasters = clientMasterRepository.findByCostCenterCode(assoc.getCostCenterCode());
				clientMasters.forEach(cm -> {
					clientMasterEntries.add(cm);
					clientCodeSet.add(cm.getClientCode());
				});
				for(ClientMaster clientMaster : clientMasters) {
					UserClients userClient = populateCostCenterUserClient(user, clientMaster, assoc.getUserType());
					userClientList.add(userClient);
				}
			}
			if(CollectionUtils.isNotEmpty(user.getClients())) {
				populateClientsForCostCenterUsers(user, clientMasterEntries, userClientList, clientCodeSet);
			}
			user.setClients(userClientList);
		}
		return clientMasterEntries;
	}

	private void populateClientsForCostCenterUsers(Users user, List<ClientMaster> clientMasterEntries,
			List<UserClients> userClientList, Set<String> clientCodeSet) {
		user.getClients().forEach(uc -> {
			if(null != uc.getClient() && !clientCodeSet.contains(uc.getClient().getClientCode())) {
				clientCodeSet.add(uc.getClient().getClientCode());
				clientMasterEntries.add(uc.getClient());
			}
			if(StringUtils.isNotEmpty(user.getDefaultClient())) {
				uc.setIsPrimary(false);
			}
			userClientList.add(uc);
		});
	}
	
	private UserClients populateCostCenterUserClient(Users user, ClientMaster clientMaster, String userType) {
		UserClients userClient = new UserClients();
		userClient.setUser(user);
		userClient.setUserType(userType);
		userClient.setClientCode(clientMaster.getClientCode());
		if(StringUtils.isNotEmpty(user.getDefaultClient()) 
				&& clientMaster.getClientCode().equals(user.getDefaultClient())) {
			userClient.setIsPrimary(true);
		}
		userClient.setIsActive(user.getStatus());
		userClient.setFirstName(user.getFirstName());
		userClient.setLastName(user.getLastName());
		userClient.setMobile(user.getMobile());
		userClient.setClient(clientMaster);
		return userClient;
	}
	
	private List<UserClients> fetchCostCenterUserClients(Users user) {
		List<UserClients> userClientList = Lists.newArrayList();
		List<UserCostCenterAssociation> costCenterAssocs = userCostCenterAssociationRepository.findByUserId(user.getId());
		if (CollectionUtils.isNotEmpty(costCenterAssocs)) {
			for(UserCostCenterAssociation assoc : costCenterAssocs) {
				List<ClientMaster> clientMasters = clientMasterRepository.findByCostCenterCode(assoc.getCostCenterCode());
				for(ClientMaster clientMaster : clientMasters) {
					UserClients userClient = populateCostCenterUserClient(user, clientMaster, assoc.getUserType());
					userClientList.add(userClient);
				}
			}
		}
		return userClientList;
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public UserPrincipal getUserPrincipal(String userName) {
		Users user = null;
		long userClientId = 0;
		if (userName.contains("@")) {
			user = userRepository.findByEmail(userName.trim().toLowerCase());
		} else {
			Optional<Users> userOptional = userRepository.findById(Long.valueOf(userName));
			user = userOptional.get();
		}
		if (null != user) {
			List<ClientMaster> clientMasterEntries = populateCostCenterUsers(user);
			if (CollectionUtils.isEmpty(user.getClients()) || (CollectionUtils.isNotEmpty(user.getClients())
					&& user.getClients().size() == 1 && user.getClients().get(0).getIsActive().equals(Boolean.FALSE))) {
				throw new InvalidUserException(ErrorCodes.UR0013.getDescription());
			}

			List<RbacEntity> roles = user.getRoles();
			UserPrincipal principal = new UserPrincipal(user.getId() + "", user.getPassword(), true, true, true, true,
					getAuthority(userName));
			principal.setUserId(user.getId());
			principal.setIsFirstLogin(user.getIsFirstLogin());
			principal.setIsPolicyAccepted(user.getIsPolicyAccepted());
			principal.setIsPolicyUpdated(user.getIsPolicyUpdated());
			principal.setEmail(user.getEmail().trim().toLowerCase());
			principal.setMfaCancelAttempts(user.getMfaCancelAttempts());
			try {
				// Add client list
				if (CollectionUtils.isNotEmpty(user.getClients())) {
					Map<String, String> clientMap = Maps.newHashMap();
					Map<String, Set<String>> userTypeMap = Maps.newHashMap();
					
					List<UserClients> userClients = user.getClients();
					if (CollectionUtils.isNotEmpty(userClients)) {
						List<UserClients> activeUserClientList = userClients.stream()
								.filter(userClientsV2 -> (null != userClientsV2.getIsActive()
										&& userClientsV2.getIsActive()
										&& (null == userClientsV2.getEndDate() || userClientsV2.getEndDate()
										.toLocalDate().compareTo(LocalDateTime.now().toLocalDate()) >= 0)))
								.collect(Collectors.toList());
						List<String> clientCodes = Lists.newArrayList();
						for (UserClients userClientsV2 : activeUserClientList) {
							if(!clientCodes.contains(userClientsV2.getClientCode())) {
								clientCodes.add(userClientsV2.getClientCode());
							}
							if (null != userClientsV2.getIsPrimary() && userClientsV2.getIsPrimary()) {
								principal.setUserType(userClientsV2.getUserType());
							}
							if (userTypeMap.containsKey(userClientsV2.getUserType())) {
								userTypeMap.get(userClientsV2.getUserType()).add(userClientsV2.getClientCode());
							} else {
								Set<String> clients = Sets.newHashSet();
								clients.add(userClientsV2.getClientCode());
								userTypeMap.put(userClientsV2.getUserType(), clients);
							}
						}
						if(CollectionUtils.isEmpty(clientMasterEntries)) {
							clientMasterEntries = invokeMaster(clientCodes);
						}
						clientMap = clientMasterEntries.stream().filter(cc -> StringUtils.isNotEmpty(cc.getClientCode()))
								.collect(Collectors.toMap(ClientMaster::getClientCode, ClientMaster::getClientName));
						user.setClients(activeUserClientList);
					}
					if (MapUtils.isEmpty(clientMap)) {
						LogUtils.basicErrorLog.accept(
								basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_PRINCIPAL),
								String.format("No active clients found for user %s and client code %s",
										principal.getEmail(), principal.getClientCode()));

						throw new ValidationException(ErrorCodes.UR0013.getDescription());
					}
					principal.setUserTypeMap(userTypeMap);
					ZoneId zoneId = ZoneId.systemDefault();

					if (CollectionUtils.isNotEmpty(userTypeMap.keySet())) {
						if (StringUtils.isEmpty(principal.getUserType())) {
							if (userTypeMap.containsKey(GenericConstants.USERTYPE_NEWHIRE)) {
								principal.setUserType(GenericConstants.USERTYPE_NEWHIRE);
							} else if (userTypeMap.containsKey(GenericConstants.USERTYPE_CLIENT)) {
								principal.setUserType(GenericConstants.USERTYPE_CLIENT);
							} else if (userTypeMap.containsKey(GenericConstants.USERTYPE_EMPLOYEE)) {
								principal.setUserType(GenericConstants.USERTYPE_EMPLOYEE);
							} else {
								principal.setUserType((String) userTypeMap.keySet().toArray()[0]);
							}
						} else if (userTypeMap.containsKey(GenericConstants.USERTYPE_NEWHIRE)) {
							principal.setUserType(GenericConstants.USERTYPE_NEWHIRE);
						}

						if (CollectionUtils.isNotEmpty(userTypeMap.get(principal.getUserType()))) {
							principal.setClientCode((String) userTypeMap.get(principal.getUserType()).toArray()[0]);
							principal.setClientName(clientMap.get(principal.getClientCode()));
							List<ClientRole> clientRoles = null;
							for (UserClients userClient : user.getClients()) {
								if (null != userClient.getIsPrimary() && userClient.getIsPrimary()
										&& !userClient.getClientCode().equals(principal.getClientCode())) {
									principal.setClientCode(userClient.getClientCode());
									principal.setClientName(clientMap.get(userClient.getClientCode()));
									principal.setUserType(userClient.getUserType());
									principal.setEmployeeCode("");
									principal.setFirstName(userClient.getFirstName());
									principal.setLastName(userClient.getLastName());
									principal.setName(userClient.getFirstName() + " " + userClient.getLastName());
									principal.setMobile(userClient.getMobile());
								}
								if (userClient.getUserType().equals(principal.getUserType())
										&& userClient.getClientCode().equals(principal.getClientCode())) {
									if (StringUtils.isNotEmpty(userClient.getEmployeeCode())) {
										principal.setEmployeeCode(userClient.getEmployeeCode());
										principal.setI9Approver(userClient.isI9Approver());
									}
									if(null != userClient.getId()) {
										userClientId = userClient.getId();
									}
									clientRoles = userClient.getClientRoles();
									principal.setFirstName(userClient.getFirstName());
									principal.setLastName(userClient.getLastName());
									principal.setName(userClient.getFirstName() + " " + userClient.getLastName());
									principal.setMobile(userClient.getMobile());
									principal.setClientCode(userClient.getClientCode());
									for(ClientMaster clientMaster : clientMasterEntries) {
										if(clientMaster.getClientCode().equals(userClient.getClientCode())) {
											userClient.setClient(clientMaster);
											break;
										}
									}
									principal.setClientName(userClient.getClient().getClientName());
									principal.setCostCenterCode(userClient.getClient().getCostCenterCode());
									principal.setBbsiBranch(userClient.getClient().getCostCenterDescription());
									if (null != userClient.getStartDate())
										principal.setPortalAccessStartDate(
												userClient.getStartDate().atZone(zoneId).toEpochSecond());
									if (null != userClient.getEndDate())
										principal.setPortalAccessEndDate(
												userClient.getEndDate().atZone(zoneId).toEpochSecond());
									if (null != userClient.getIsActive())
										principal.setPortalAccessStatus(String.valueOf(userClient.getIsActive()));
									principal.setNewHireId(userClient.getNewHireId());
								}
								if (userClient.getIsCaliforniaUser() && !userClient.getIsCCPAAccepted()) {
									principal.setIsCcpaRequired(Boolean.TRUE);
								}
							}
							
							if(null != principal && StringUtils.isNotEmpty(principal.getUserType())
									&& (principal.getUserType().equals(GenericConstants.USERTYPE_CLIENT)
											|| principal.getUserType().equals(GenericConstants.USERTYPE_EXTERNAL) 
											|| principal.getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE))) {
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								LocalDate mfaLocalDate = LocalDate.parse(mfaDate, formatter);
								principal.setMfaDate(mfaLocalDate);
								List<UserClients> userClientList = userClients.stream().filter(userClientsMfa -> StringUtils.isEmpty(userClientsMfa.getMobile()))
										.collect(Collectors.toList());
								if (!CollectionUtils.isEmpty(userClientList) && StringUtils.isEmpty(user.getMobile())
										&& StringUtils.isEmpty(user.getMfaEmail())) {
									if (StringUtils.isEmpty(principal.getEmployeeCode())
											|| (StringUtils.isNotEmpty(principal.getEmployeeCode())
													&& StringUtils.isEmpty(getEmployeeMobileNumber(
															principal.getClientCode(), principal.getEmployeeCode())))) {
										principal.setHasMfaInfo(Boolean.FALSE);
									}
								}
							}

							if (CollectionUtils.isNotEmpty(clientRoles)) {
								// privilege list call
								for (ClientRole clientRole : clientRoles) {
									if (clientRole.getRole() != null) {
										principal.setAccessGroup(clientRole.getRole().getId() + "");
									}
								}
							}
						}
					}
					if (CollectionUtils.isNotEmpty(roles) && StringUtils.isEmpty(principal.getAccessGroup())) {
						for (RbacEntity role : roles) {
							principal.setAccessGroup(role.getMappingId() + "");
							principal.setRole(role.getName());
						}
					}
					principal.setClientMap(clientMap);
					principal = updateUserPrincipal(principal);
					setDataSecurityFlag(userClientId, principal);
					updateAddlPrincipalFields(principal);
				}
			} catch (Exception e) {
				LogUtils.basicErrorLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_PRINCIPAL),
						e.getMessage());
			}
			return principal;
		}
		return null;
	}
	
	private void updateAddlPrincipalFields(UserPrincipal principal) {
		List<String> assignedTypes = Lists.newArrayList();
		if(null != principal.getUserTypeMap()) {
			for (Entry<String, Set<String>> entry : principal.getUserTypeMap().entrySet()) {
				if(principal.getUserTypeMap().get(entry.getKey()).contains(principal.getClientCode())) {
					assignedTypes.add(entry.getKey());
				}
			}
		}
		principal.setAssignedUserType(assignedTypes);
		if(null == principal.getCreatedDate()) {
			principal.setCreatedDate(LocalDateTime.now());
			principal.setValidity(accessTokenValidity);
		}
	}

	private void setDataSecurityFlag(long userClientId, UserPrincipal principal) {
		List<String> ignoreUserTypes = Arrays.asList(GenericConstants.USERTYPE_BRANCH,GenericConstants.USERTYPE_VANCOUVER);
		if(!ignoreUserTypes.contains(principal.getUserType())) {
			List<UserSecurity> filters = userSecurityRepository.findByUserClient_Id(userClientId);
			if (CollectionUtils.isNotEmpty(filters)) {
				principal.setHasSecurity(Boolean.TRUE); 
			}
		}
	}

	private List<ClientMaster> invokeMaster(List<String> clientCodes) {
		List<ClientMaster> dbEntries = new ArrayList<>();
		while(clientCodes.size() > 2000) {
			List<String> temp = clientCodes.subList(0, 2000);

			dbEntries.addAll(clientMasterRepository.findByClientCodeIn(temp));
			clientCodes = clientCodes.subList(2000, clientCodes.size());
		}
		dbEntries.addAll(clientMasterRepository.findByClientCodeIn(clientCodes));

		return dbEntries;
	}

	public UserPrincipal updateUserPrincipal(UserPrincipal principal) {
		List<CommonDTO> clientMappings = Lists.newArrayList();
		principal.setPrivilegeCodes(Sets.newHashSet());
		if (StringUtils.isNotEmpty(principal.getAccessGroup())) {
			CommonDTO mappingDTO = mappingService.getMapping(Long.parseLong(principal.getAccessGroup()),
					UserEnum.CLIENT.toString(), true);
			if (null != mappingDTO) {
				updateUserPrincipal(mappingDTO, clientMappings, principal);
			}
		} else if (principal.getUserType().equals(GenericConstants.USERTYPE_NEWHIRE)) {
			principal.setPrivilegeCodes(Sets.newHashSet("NEW_HIRE_USER.ALL"));
		}
		return principal;
	}

	private void updateUserPrincipal(CommonDTO mappingDTO,List<CommonDTO> clientMappings,UserPrincipal principal) {

		Set<String> privileges = Sets.newHashSet();
		Map<String, List<String>> privilegeMap = Maps.newHashMap();
		List<CommonDTO> featurePrivilegeList = Lists.newArrayList();
		clientMappings.add(mappingDTO);
		updateFeatureMappings(featurePrivilegeList, privilegeMap, privileges, clientMappings);
		List<MenuMappingDTO> menu = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(featurePrivilegeList)) {
			menu = menuBusinessService.fetchUserMenu(featurePrivilegeList, clientMappings);
			// set isFavorite attribute true on user selection
			setUserFavoriteMenus(principal.getClientCode(), principal.getEmail(), menu);
		}
		List<CommonDTO> accessGroups = mappingDTO.getChild();
		if (StringUtils.isNotEmpty(principal.getRole())) {
			principal.setAccessGroup(mappingDTO.getName());
		} else {
			principal.setRole(mappingDTO.getName());
			List<String> accessGroupNames = new ArrayList<>();
			for (CommonDTO accessGroup : accessGroups) {
				accessGroupNames.add(accessGroup.getName());
			}
			principal.setAccessGroup(String.join(",", accessGroupNames));
		}
		principal.setUserMenu(menu);
		if (privileges.contains(MethodNames.PAY_TS_HOUR_FINALIZE_ALL)
				&& privileges.contains(MethodNames.PAY_TS_HOUR_SUBMIT_ALL)) {
			privileges.remove(MethodNames.PAY_TS_HOUR_SUBMIT_ALL);
		}
		principal.setPrivilegeCodes(privileges);

	}
	private void updateFeatureMappings(List<CommonDTO> featurePrivilegeList, Map<String, List<String>> privilegeMap,
									   Set<String> privileges, List<CommonDTO> mappings) {
		for (CommonDTO clientMapping : mappings) {
			if (CollectionUtils.isNotEmpty(clientMapping.getPrivileges())) {
				for (CommonDTO clientPrivilegesV2 : clientMapping.getPrivileges()) {
					constructPrivilegeMap(clientPrivilegesV2, privileges, privilegeMap);
				}
				featurePrivilegeList.add(clientMapping);
			}
			if (CollectionUtils.isNotEmpty(clientMapping.getChild())) {
				updateFeatureMappings(featurePrivilegeList, privilegeMap, privileges, clientMapping.getChild());
			}
		}
	}

	private void constructPrivilegeMap(CommonDTO clientPrivilegesV2,Set<String> privileges,Map<String, List<String>> privilegeMap) {

		if (null != clientPrivilegesV2.getIsSelected() && clientPrivilegesV2.getIsSelected()) {
			privileges.add(clientPrivilegesV2.getCode());
			if (privilegeMap.containsKey(clientPrivilegesV2.getCode())) {
				privilegeMap.get(clientPrivilegesV2.getCode()).add(clientPrivilegesV2.getType());
			} else {
				List<String> privList = Lists.newArrayList();
				privList.add(clientPrivilegesV2.getType());
				privilegeMap.put(clientPrivilegesV2.getCode(), privList);
			}
		}

	}
	
	private String getSwitchUserType(UserPrincipal principal, String clientCode, String userType) {
		if (StringUtils.isEmpty(userType) || userType.equals(GenericConstants.NOT_APPLICABLE)) {
			if (principal.getUserTypeMap().containsKey(GenericConstants.USERTYPE_NEWHIRE)
					&& principal.getUserTypeMap().get(GenericConstants.USERTYPE_NEWHIRE).contains(clientCode)) {
				return GenericConstants.USERTYPE_NEWHIRE;
			} else if (principal.getUserTypeMap().containsKey(GenericConstants.USERTYPE_CLIENT)
					&& principal.getUserTypeMap().get(GenericConstants.USERTYPE_CLIENT).contains(clientCode)) {
				return GenericConstants.USERTYPE_CLIENT;
			} else if (principal.getUserTypeMap().containsKey(GenericConstants.USERTYPE_EMPLOYEE)
					&& principal.getUserTypeMap().get(GenericConstants.USERTYPE_EMPLOYEE).contains(clientCode)) {
				return GenericConstants.USERTYPE_EMPLOYEE;
			} else {
				return getDefaultUserType(principal, clientCode);
			}
		}
		return userType;
	}
	
	private String getDefaultUserType(UserPrincipal principal, String clientCode) {
		for (Entry<String, Set<String>> entry : principal.getUserTypeMap().entrySet()) {
			if(principal.getUserTypeMap().get(entry.getKey()).contains(clientCode)) {
				return entry.getKey();
			}
		}
		return principal.getUserType();
	}

	@Override
	@Transactional(readOnly = true)
	public ParentDTO switchBusiness(UserPrincipal principal, String clientCode, String userType) {
		ParentDTO parentDTO = new ParentDTO();
		long userClientId = 0;
		Users user = userRepository.findByEmail(principal.getEmail());
		if (null != user) {
			principal.setPrivilegeCodes(Sets.newHashSet());
			principal.setClientCode(clientCode);
			principal.setIsPolicyAccepted(user.getIsPolicyAccepted());
			principal.setIsPolicyUpdated(user.getIsPolicyUpdated());
			principal.setEmployeeCode("");
			principal.setI9Approver(false);
			principal.setNewHireId(0);
			if (null != principal.getClientMap()) {
				principal.setClientName(principal.getClientMap().get(clientCode));
			}
			userType = getSwitchUserType(principal, clientCode, userType);
			principal.setUserType(userType);
			Map<String, List<String>> privilegeMap = Maps.newHashMap();
			List<CommonDTO> featurePrivilegeList = Lists.newArrayList();

			// Add client list
			Set<String> privileges = null;
			boolean isValid = false;
			if(userType.equals(GenericConstants.USERTYPE_VANCOUVER) 
					|| userType.equals(GenericConstants.USERTYPE_BRANCH)) {
				populateCostCenterUsers(user);
			}
			if (CollectionUtils.isNotEmpty(user.getClients())) {
				privileges = Sets.newHashSet();
				Map<String, List<CommonDTO>> clientFeatureMap = Maps.newHashMap();
				List<CommonDTO> clientMappings = Lists.newArrayList();
				for (UserClients userClient : user.getClients()) {
					if (userClient.getUserType().equals(principal.getUserType())
							&& userClient.getClientCode().equals(principal.getClientCode())) {
						isValid = true;
						if (null == userClient.getClient()) {
							ClientMaster clientMaster = getClientMaster(userClient.getClientCode());
							userClient.setClient(clientMaster);
						}
						if (StringUtils.isNotEmpty(userClient.getEmployeeCode())) {
							principal.setEmployeeCode(userClient.getEmployeeCode());
							principal.setI9Approver(userClient.isI9Approver());
						}
						if (userClient.getNewHireId() > 0) {
							principal.setNewHireId(userClient.getNewHireId());
						}
						if(null != userClient.getId()) {
							userClientId = userClient.getId();
							parentDTO.setUserClientId(userClientId);
						}
						principal.setCostCenterCode(userClient.getClient().getCostCenterCode());
						principal.setBbsiBranch(userClient.getClient().getCostCenterDescription());
						principal.setFirstName(userClient.getFirstName());
						principal.setLastName(userClient.getLastName());
						principal.setName(userClient.getFirstName() + " " + userClient.getLastName());
						principal.setMobile(userClient.getMobile());
						if (null != userClient.getIsActive())
							principal.setPortalAccessStatus(String.valueOf(userClient.getIsActive()));
						principal.setNewHireId(userClient.getNewHireId());
						if (null != userClient.getId() && CollectionUtils.isNotEmpty(userClient.getClientRoles())) {
							for (ClientRole clientRole : userClient.getClientRoles()) {
								if (clientRole.getRole() != null) {
									CommonDTO mappingDTO = mappingService.getMapping(clientRole.getRole().getId(),
											UserEnum.CLIENT.toString(), true);
									clientMappings.add(mappingDTO);
								}
							}
							if (CollectionUtils.isNotEmpty(clientMappings)) {
								updateFeatureMappings(featurePrivilegeList, privilegeMap, privileges, clientMappings);
							}
						} else {
							privileges = Sets.newHashSet();
							if (CollectionUtils.isNotEmpty(user.getRoles())) {
								for (RbacEntity role : user.getRoles()) {
									CommonDTO mappingDTO = mappingService.getMapping(role.getMappingId(),
											UserEnum.CLIENT.toString(), true);
									clientMappings.add(mappingDTO);
								}
								if (CollectionUtils.isNotEmpty(clientMappings)) {
									updateFeatureMappings(featurePrivilegeList, privilegeMap, privileges,
											clientMappings);
								}
								clientFeatureMap.put(userClient.getClientCode(), clientMappings);
							}
						}
						break;
					}
				}
				if (privileges.contains(MethodNames.PAY_TS_HOUR_FINALIZE_ALL)
						&& privileges.contains(MethodNames.PAY_TS_HOUR_SUBMIT_ALL)) {
					privileges.remove(MethodNames.PAY_TS_HOUR_SUBMIT_ALL);
				}
				if (privilegeMap.containsKey(MethodNames.PAY_TS_HOUR_FINALIZE_ALL)
						&& privilegeMap.containsKey(MethodNames.PAY_TS_HOUR_SUBMIT_ALL)) {
					privilegeMap.remove(MethodNames.PAY_TS_HOUR_SUBMIT_ALL);
				}
				principal.setPrivilegeMap(privilegeMap);
				principal.setPrivilegeCodes(privileges);
				List<MenuMappingDTO> menu = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(featurePrivilegeList)) {
					menu = menuBusinessService.fetchUserMenu(featurePrivilegeList, clientMappings);
				}

				if (clientMappings.size() > 0 && null != clientMappings.get(0)) {
					principal.setRole(clientMappings.get(0).getName());

					List<CommonDTO> accessGroups = clientMappings.get(0).getChild();
					List<String> accessGroupNames = new ArrayList<>();
					for (CommonDTO accessGroup : accessGroups) {
						accessGroupNames.add(accessGroup.getName());
					}
					principal.setAccessGroup(String.join(",", accessGroupNames));
				}
				principal.setPrivileges(String.join(",", privileges));
				principal.setUserMenu(menu);
				setUserToolBarSettingsData(principal);
				if (principal.getUserType().equals(GenericConstants.USERTYPE_NEWHIRE)) {
					principal.setPrivilegeCodes(Sets.newHashSet("NEW_HIRE_USER.ALL"));
				}
				setDataSecurityFlag(userClientId, principal);
				updateAddlPrincipalFields(principal);
			}
			if (!isValid) {
				throw new InvalidUserException(new ErrorDetails(ErrorCodes.UR008));
			}
		}
		parentDTO.setPrincipal(principal);
		return parentDTO;
	}

	/**
	 * As part of story #21684, user has to go back to his previous logged in
	 * client. So when user switches to any client, we update is primary flag
	 * with True for that particular user Client and update existing is primary
	 * user client record with False. So next time, when user logs in, by
	 * default user should land on his last logged in client.
	 *
	 */
	@Async
	@Transactional
	public void updateIsPrimary(Long userId, Long userClientId, String clientCode, String userType) {
		if(userType.equals(GenericConstants.USERTYPE_VANCOUVER) || userType.equals(GenericConstants.USERTYPE_BRANCH)) {
			userRepository.updateDefaultClient(userId, clientCode);
		} else if (null != userClientId) {
			userClientRepository.clearPrimaryStatus(userId);
			userClientRepository.updateClientPrimaryStatus(true, userClientId);
		}
	}

	private void setUserToolBarSettingsData(UserPrincipal principal) {
		if (CollectionUtils.isNotEmpty(principal.getUserMenu())) {
			// manually disabling is favourite value false
			principal.getUserMenu().forEach(menu -> {
				menu.setIsFavourite(Boolean.FALSE);
				if (CollectionUtils.isNotEmpty(menu.getMenuItems())) {
					menu.getMenuItems().forEach(menuMap -> {
						if (null != menuMap) {
							menuMap.setIsFavourite(Boolean.FALSE);
						}
					});
				}
			});
			// set isFavorite attribute true on user selection
			setUserFavoriteMenus(principal.getClientCode(), principal.getEmail(), principal.getUserMenu());
		}
	}

	private List<SimpleGrantedAuthority> getAuthority(String userName) {
		if (userName.equals(GenericConstants.SUPER_ADMIN)) {
			return Arrays.asList(new SimpleGrantedAuthority(ROLE_ADMIN), new SimpleGrantedAuthority(ROLE_USER));
		}
		return Arrays.asList(new SimpleGrantedAuthority(ROLE_USER));
	}


	/**
	 * User can be created for multiple clients.There should be a default one.
	 * For this, we are setting isPrimary flag value by sorting clients by
	 * clientName and setting isPrimary for first client after sorting.
	 *
	 * @param user
	 */
	private void assignIsPrimaryFlag(Users user) {
		if (CollectionUtils.isNotEmpty(user.getClients())) {
			boolean isPrimaryClientExist = user.getClients().stream()
					.anyMatch(client -> null != client.getIsPrimary() && client.getIsPrimary());
			if (!isPrimaryClientExist) {
				user.getClients().get(0).setIsPrimary(Boolean.TRUE);
			}
		}
	}

	private Email buildEmail(UsersDTO userDTO, String uiUrl, String template, boolean includeToken,
							 UserPrincipal userPrincipal) {
		String exten[] = bbsiHeadEmails.split(",");
		Email emailObj = new Email();
		Map<String, String> context = new HashMap<String, String>();
		context.put("name", String.format("%s %s", userDTO.getFirstName(), userDTO.getLastName()));
		emailObj.setSubject(GenericConstants.INVITATION_TO_BBSI);
		emailObj.setToAddress(userDTO.getEmail());
		emailObj.setReplyTo(userPrincipal.getEmail());
		if (StringUtils.isNotEmpty(template)) {
			emailObj.setTemplateName(template);
		} else {
			emailObj.setTemplateName(GenericConstants.USER_LOGIN_VM);
		}
		// NOTE: Sending user name for both new and existing user.
		context.put("userName", userDTO.getEmail());
		context.put("clientName", userPrincipal.getClientName());

		checkUserTypeForEmail(userDTO,context);

		if (StringUtils.isNotEmpty(uiUrl) && !uiUrl.contains("#")) {
			uiUrl = uiUrl + "#/login";
		}
		String redirectUrl="email=" + userDTO.getEmail();
		if (includeToken && !Utils.checkIfEmailHasExtension(userDTO.getEmail(), exten)) {
			String jwtTokenData = passwordBusinessServiceImpl.getUserToken(userDTO.getEmail());
			redirectUrl = redirectUrl + "&token=" + jwtTokenData;
			userRepository.updateTokenValue(userDTO.getEmail(), jwtTokenData);
		}
		
		redirectUrl=encryptAndDecryptUtil.encrypt(redirectUrl);
		redirectUrl= new String (Base64.getEncoder().encode(redirectUrl.getBytes()));
		context.put("uiURL", uiUrl+"?qs="+redirectUrl);
		context.put("client", "BBSI");
		emailObj.setContextMap(context);
		return emailObj;
	}

	private void checkUserTypeForEmail(UsersDTO userDTO,Map<String, String> context) {
		if (CollectionUtils.isNotEmpty(userDTO.getClients()) && userDTO.getClients().size() == 1
				&& (userDTO.getClients().get(0).getUserType().equalsIgnoreCase(GenericConstants.USERTYPE_CLIENT)
				|| userDTO.getClients().get(0).getUserType()
				.equalsIgnoreCase(GenericConstants.USERTYPE_EXTERNAL))) {
			context.put(GenericConstants.IS_CLIENT, "Y");
		} else if (CollectionUtils.isNotEmpty(userDTO.getClients()) && userDTO.getClients().size() == 1
				&& userDTO.getClients().get(0).getUserType().equalsIgnoreCase(GenericConstants.USERTYPE_VANCOUVER)) {
			context.put(GenericConstants.IS_VOC, "Y");
		} else if (CollectionUtils.isNotEmpty(userDTO.getClients()) && userDTO.getClients().size() == 1
				&& userDTO.getClients().get(0).getUserType().equalsIgnoreCase(GenericConstants.USERTYPE_BRANCH)) {
			context.put(GenericConstants.IS_BRANCH, "Y");
		} else if (CollectionUtils.isNotEmpty(userDTO.getClients()) && userDTO.getClients().size() == 1
				&& userDTO.getClients().get(0).getUserType().equalsIgnoreCase(GenericConstants.USERTYPE_EMPLOYEE)) {
			context.put(GenericConstants.IS_EMPLOYEE, "Y");
		} else if (CollectionUtils.isNotEmpty(userDTO.getClients()) && userDTO.getClients().size() == 1
				&& userDTO.getClients().get(0).getUserType().equalsIgnoreCase(GenericConstants.USERTYPE_NEWHIRE)) {
			if (userDTO.getIsElectronicOnboard()) {
				context.put(GenericConstants.IS_NEWHIRE_ELECTRONIC, "Y");
			} else {
				context.put(GenericConstants.IS_NEWHIRE, "Y");
			}
		}
	}

	private boolean checkUserType(UsersDTO userDTOV2) {
		boolean isVOCorBranch = false;
		if (CollectionUtils.isNotEmpty(userDTOV2.getClients())) {
			for (ClientDTOV2 userType : userDTOV2.getClients()) {
				if (userType.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER)
						|| userType.getUserType().equals(GenericConstants.USERTYPE_BRANCH)) {
					isVOCorBranch = true;
					break;
				}
			}
		}
		return isVOCorBranch;
	}

	@Override
	public Map<String, List<String>> getEmailsByClientCodeAndType(String clientCode, String userType) {
		Set<String> userEmailList = Sets.newHashSet();
		Set<String> userMfaEmailList = Sets.newHashSet();
		Map<String, List<String>> userDataMap = Maps.newHashMap();
		List<String> mobileList = Lists.newArrayList();
		try {
			
			List<Object[]> userClients = userClientRepository.findByUserDetailsByClient(clientCode);
			for (Object[] userClient : userClients) {
				if (!Objects.isNull(userClient[4]) && userType.equalsIgnoreCase(String.valueOf(userClient[4]))
						&& !Objects.isNull(userClient[5]) && (Boolean.TRUE).equals(userClient[5])) {
					List<String> nameList = Lists.newArrayList();
					nameList.add(userClient[1] + " " + userClient[2]);
					userDataMap.put(String.valueOf(userClient[0]), nameList);
					userEmailList.add(String.valueOf(userClient[0]));
					if (!Objects.isNull(userClient[6]))
						userMfaEmailList.add(String.valueOf(userClient[6]));
					if (!Objects.isNull(userClient[3]))
						mobileList.add(String.valueOf(userClient[3]));
				}
			}
			
			if (userType.equalsIgnoreCase(GenericConstants.USERTYPE_BRANCH)
					|| userType.equals(GenericConstants.USERTYPE_VANCOUVER))
				updateCostCenterUsers(clientCode, userDataMap, userEmailList, userMfaEmailList, mobileList,userType);

			userDataMap.put(GenericConstants.EMAIL, Lists.newArrayList(userEmailList));
			userDataMap.put(GenericConstants.MFA_EMAIL, Lists.newArrayList(userMfaEmailList));
			userDataMap.put(GenericConstants.TEXT, mobileList);
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_USERS),
					exception, "Error occured while retriving all users");
		}
		return userDataMap;
	}
	
	private void updateCostCenterUsers(String clientCode, Map<String, List<String>> userDataMap, Set<String> userEmailList,Set<String> userMfaEmailList, List<String> mobileList,String userType) {
		ClientMaster clientMaster = clientMasterRepository.findByClientCode(clientCode);
		List<UserCostCenterAssociation> costCenterAssocs = userCostCenterAssociationRepository.findByCostCenterCode(clientMaster.getCostCenterCode());
		Map<Long, Users> costCenterUserMap = Maps.newHashMap();
		for(UserCostCenterAssociation assoc : costCenterAssocs) {
			if (userType.equalsIgnoreCase(assoc.getUserType())) {
				Users user = assoc.getUser();
				if (null != user && !costCenterUserMap.containsKey(user.getId())) {
					costCenterUserMap.put(user.getId(), user);
				}
			}
		}
		for(Users user : costCenterUserMap.values()) {
			List<String> nameList = Lists.newArrayList();
			nameList.add(user.getFirstName() + " " + user.getLastName());
			userDataMap.put(user.getEmail(), nameList);
			userEmailList.add(user.getEmail());
			if(!StringUtils.isEmpty(user.getMfaEmail())) {
				userMfaEmailList.add(user.getMfaEmail());
			}
			if (!StringUtils.isEmpty(user.getMobile())) {
				mobileList.add(user.getMobile());
			}
		}
	}

	/**
	 * Method to get All Branch users list
	 */

	@Override
	public List<UsersDTO> getAllBranchUsers(Long userId) {
		List<UsersDTO> userDTOList = new ArrayList<>();
		try {
			Optional<Users> optionalUser = userRepository.findById(userId);
			if (optionalUser.isPresent()) {
				Set<String> clientCodeSet = Sets.newHashSet();
				Users user = optionalUser.get();
				if (CollectionUtils.isNotEmpty(user.getClients())) {
					user.getClients().forEach(userClientsV2 -> {
						clientCodeSet.add(userClientsV2.getClientCode());
					});
					if (CollectionUtils.isNotEmpty(clientCodeSet)) {
						Map<Long, Users> branchUsersList = Maps.newHashMap();
						// branch user should see only users of type branch and
						// client
						constructBranchUserList(clientCodeSet, branchUsersList, user);
						constructUserDTOList(branchUsersList, userDTOList);
					}
				}
			}
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_BRANCH_USERS), exception,
					"Error occured while retriving all branch admin users");
		}
		return userDTOList;
	}

	private void constructUserDTOList(Map<Long, Users> branchUsersList,List<UsersDTO> userDTOList) {
		if (CollectionUtils.isNotEmpty(branchUsersList.keySet())) {
			// If user is assigned for multiple clients,
			// branchUserList will contains duplicate user
			// list.below will filter user list object.
			List<Users> filteredList = Lists.newArrayList(branchUsersList.values());
			// below will sort the filtered user list based in
			// ModifiedOn field in descending order.
			Collections.sort(filteredList, new Comparator<Users>() {
				public int compare(Users u1, Users u2) {
					return u2.getModifiedOn().compareTo(u1.getModifiedOn());
				}
			});
			userDTOList = userMapper.userListToUserDTOList(filteredList);
			// adding first name and last name to full name
			// property
			userDTOList.forEach(userdto -> {
				userdto.setFullName(userdto.getFirstName() + " " + userdto.getLastName());
				if (CollectionUtils.isNotEmpty(userdto.getClients())) {
					userdto.setUserType(userdto.getClients().get(0).getUserType());
				}
			});
		}
	}

	private void constructBranchUserList(Set<String> clientCodeSet,Map<Long, Users> branchUsersList,Users user) {
		List<String> userTypeList = new ArrayList<>();
		userTypeList.add(GenericConstants.USERTYPE_BRANCH);
		userTypeList.add(GenericConstants.USERTYPE_CLIENT);
		userTypeList.add(GenericConstants.USERTYPE_EXTERNAL);

		Sort sort = Sort.by(Sort.Direction.DESC, MethodNames.MODIFIED_ON);
		List<UserClients> branchUsers = userClientRepository.findByClientCodeIn(clientCodeSet, sort);
		if (CollectionUtils.isEmpty(branchUsers)) {
			LogUtils.basicErrorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(),
					MethodNames.GET_ALL_BRANCH_USERS), "No Branch user records found");

			throw new ValidationException(new ErrorDetails(ErrorCodes.UR003));
		}
		branchUsers.forEach(branchUser -> {
			if (null != branchUser.getUserType() && userTypeList.contains(branchUser.getUserType())
					&& user.getEmail().equals(branchUser.getCreatedBy())) {
				// Branch user created list
				List<UserClients> userClients = Lists.newArrayList();
				userClients.add(branchUser);
				Users user1 = branchUser.getUser();
				if (null != user1) {
					user1.setClients(userClients);
					if (branchUsersList.containsKey(user1.getId())) {
						branchUsersList.get(user1.getId()).getClients().add(branchUser);
					} else {
						branchUsersList.put(user1.getId(), user1);
					}
				}
			}
		});
	}

	@Transactional
	@Override
	public void updateIsPolicyAccepted(PolicyAcceptedDTO policyAcceptedDTO, UserPrincipal principal) {
		try {
			if (null != policyAcceptedDTO) {
				userRepository.updateIsPolicyAccepted(policyAcceptedDTO.getEmail(),
						policyAcceptedDTO.getIsPolicyAccepted());
				principal.setIsPolicyAccepted(policyAcceptedDTO.getIsPolicyAccepted());
			}
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_IS_POLICY_ACCEPTED),
					exception, "Error occured while Updating isPolicy Accepted");
		}
		return;
	}

	@Transactional
	@Override
	public void updateCCPAPolicyAccepted(boolean isAccepted, UserPrincipal principal) {
		try {
			List<String> clientCodes = new ArrayList<>();
			List<Object[]> clients = userClientRepository.getCaliforniaUserClients(principal.getUserId());
			if (CollectionUtils.isNotEmpty(clients)) {
				clients.forEach(client -> {
					if (null != client[2] && ((Boolean)client[2]).equals(Boolean.TRUE)) {
						clientCodes.add(String.valueOf(client[3]));
					}
				});
				userClientRepository.updateIsCCPAAccepted(clientCodes, isAccepted, principal.getUserId());
				principal.setIsCcpaRequired(!isAccepted ? Boolean.TRUE : Boolean.FALSE);
			}
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_IS_CCPA_ACCEPTED),
					exception, "Error occured while Updating CCPA Accepted");
		}
	}

	@Override
	public boolean disableUser(String clientCode, long newHireId) {
		boolean deleteParent = false;
		try {
			UserClients userClient = userClientRepository.findByNewHireId(newHireId);
			if (null != userClient && userClient.getUserType().equals(GenericConstants.USERTYPE_NEWHIRE)) {
				long userId = userClient.getUser().getId();
				int profileCount = userClientRepository.findByUser_Id(userId).size();
				userSecurityRepository.deleteByUserClient_Id(userClient.getId());
				userClientRepository.deleteNewHireId(newHireId);
				if (profileCount == 1) {
					userRepository.deleteNewHireUser(userId);
				}
				deleteParent = true;
			}
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), "disableUser"),
					exception, "Error occured while deleting newhire user");
		}
		return deleteParent;
	}

	@Override
	public UserExistDTO checkExistUser(String clientCode, String email, String token) {

		UserExistDTO userExistDTO = new UserExistDTO();
		try {
			List<Object[]> users = userRepository.getUserDataByEmail(email, clientCode);
			if (CollectionUtils.isNotEmpty(users)) {
				for (Object[] user : users) {
					userExistDTO.setClientCode(clientCode);
					userExistDTO.setEmail(user[3].toString());
					userExistDTO.setFirstName(user[0].toString());
					userExistDTO.setLastName(user[1] == null ? "" : user[1].toString());
					userExistDTO.setMobile(user[2] == null ? "" : user[2].toString());
					userExistDTO.setIsFirstLogin(Boolean.valueOf(user[4] == null ? "" : user[4].toString()));
					userExistDTO.setIsUserExist(true);
					if(user.length >= 8) {
						userExistDTO.setMfaEmail(user[8] == null ? "" : user[8].toString());
					}
				}
			} else {
				userExistDTO.setIsEmailNotExist(true);
			}
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CHECK_EXIST_USER),
					exception, "Error occured while check Exist the User by email");
		}
		return userExistDTO;
	}

	@Transactional
	@Override
	public void updateUserDetails(CustomPersonalDTO customPersonalDTO, String token) {
		try {
			if (StringUtils.isNotEmpty(customPersonalDTO.getClientCode())
					&& StringUtils.isNotEmpty(customPersonalDTO.getEmployeeCode())) {
				String jsonResponse = Utils.validateEmployeeCodes(customPersonalDTO, restClient, boomiHelper);
				validateJsonResponse(customPersonalDTO, jsonResponse);
				UserClients userClient = userClientRepository.findByClientCodeAndEmployeeCode(
						customPersonalDTO.getClientCode(), customPersonalDTO.getEmployeeCode());
				userClient.setFirstName(customPersonalDTO.getFirstName());
				userClient.setLastName(customPersonalDTO.getLastName());
				userClientRepository.save(userClient);
			}
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER), e,
					"Error occurred while updating user details");
		}
	}

	/*
	 * Method for updating employee email or first and last name.
	 *
	 */
	@Transactional
	@Override
	public CustomPersonalDTO updateUserPersonalInfo(CustomPersonalDTO customPersonalDTO, String token, UserPrincipal userPrincipal) {
		try {
			if(Utils.isValidClientCode(userPrincipal, customPersonalDTO.getClientCode())) {
				throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
			}
			String jsonResponse = Utils.validateEmployeeCodes(customPersonalDTO, restClient, boomiHelper);
			validateJsonResponse(customPersonalDTO, jsonResponse);
			/* Get existing users by email */
			if (StringUtils.isNotEmpty(customPersonalDTO.getOldEmail())
					&& StringUtils.isNotEmpty(customPersonalDTO.getNewEmail())) {

				Users users = userRepository.findByEmail(customPersonalDTO.getOldEmail());
				/* Never update if he has assigned to more than one client */
				if (users != null && (CollectionUtils.isEmpty(users.getClients()) || users.getClients().size() == 1)) {

					users.setEmail(customPersonalDTO.getNewEmail());
					userRepository.save(users);
				}

			} else if (StringUtils.isNotEmpty(customPersonalDTO.getClientCode())) {
				// Code to update first name and last name.
				UserClients userClient = userClientRepository.findByClientCodeAndEmployeeCode(
						customPersonalDTO.getClientCode(), customPersonalDTO.getEmployeeCode());

				if (userClient != null && userClient.getUser() != null) {
					userClient.setFirstName(customPersonalDTO.getFirstName());
					userClient.setLastName(customPersonalDTO.getLastName());
					userClientRepository.save(userClient);
				}

			}
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_PERSONAL_INFO), e,
					"Error occurred during user personal details update");
		}

		return customPersonalDTO;
	}

	/*
	 * Method for updating user email.
	 */
	@Transactional
	public CustomPersonalDTO updateEmail(CustomPersonalDTO customPersonalDTO, String token,
			UserPrincipal userPrincipal) {

		validateChangeLoginMfaEmail(customPersonalDTO.getOldEmail(), customPersonalDTO.getNewEmail());
		try {
			String exten[] = bbsiHeadEmails.split(",");
			if (GenericConstants.USERTYPE_EMPLOYEE.equals(userPrincipal.getUserType()) && 
					GenericConstants.TERMINATED_EMPLOYEE_ROLE_CODE.equalsIgnoreCase(userPrincipal.getRole())) {
				updateUserForNonLoginChange(customPersonalDTO, token, userPrincipal, exten);
			} else if (customPersonalDTO.getIsAccountLoginChange().equals(Boolean.TRUE)) {
				if (StringUtils.isNotEmpty(customPersonalDTO.getEmployeeCode())) {
					String jsonResponse = Utils.validateEmployeeCodes(customPersonalDTO, restClient, boomiHelper);
					validateJsonResponse(customPersonalDTO, jsonResponse);
				}
				updateUserAccess(customPersonalDTO, token, userPrincipal, exten);
			} else {
				updateUserForNonLoginChange(customPersonalDTO, token, userPrincipal, exten);
			}
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_PERSONAL_INFO), e,
					"Error occurred during user details update");
		}
		return customPersonalDTO;
	}

	private void updateUserForNonLoginChange(CustomPersonalDTO customPersonalDTO, String token,
																					 UserPrincipal userPrincipal, String[] exten) {
		/* Get existing users by email */
		if (StringUtils.isNotEmpty(customPersonalDTO.getOldEmail())
				&& StringUtils.isNotEmpty(customPersonalDTO.getNewEmail())) {
			Users newUser = userRepository.findByEmail(customPersonalDTO.getNewEmail());

			isNewUserAlreadyExistsValidation(newUser);

			Users users = userRepository.findByEmail(customPersonalDTO.getOldEmail());
			if(users != null && StringUtils.isNotEmpty(users.getMfaEmail())
					&& users.getMfaEmail().equalsIgnoreCase(customPersonalDTO.getNewEmail())) {
				throw new ValidationException(new ErrorDetails(ErrorCodes.UR0027));
			}
			checkUserTypeInUpdateEmail(customPersonalDTO, userPrincipal, exten);
			if (Utils.checkIfEmailHasExtension(customPersonalDTO.getOldEmail(), exten)) {
				buildUserInUpdateEmail(customPersonalDTO, users);
			} else {
				users.setEmail(customPersonalDTO.getNewEmail());
				customPersonalDTO.setMfaEmail(users.getMfaEmail());
			}
			userRepository.save(users);
			List<UserToolbarSettings> toolbarSettings = userToolbarSettingsRepositoryV2
					.findByUserEmail(customPersonalDTO.getOldEmail());
			if (CollectionUtils.isNotEmpty(toolbarSettings)) {
				toolbarSettings.forEach(setting -> setting.setUserEmail(customPersonalDTO.getNewEmail()));
				userToolbarSettingsRepositoryV2.saveAll(toolbarSettings);
			}
			sendEmailInUpdateEmail(customPersonalDTO, token, users);
		}
	}

	private void isNewUserAlreadyExistsValidation(Users newUser) {
		if (null != newUser) {
			throw new ValidationException(new ErrorDetails(ErrorCodes.UR0010));
		}
	}

	private void buildUserInUpdateEmail(CustomPersonalDTO customPersonalDTO, Users users) {
		List<UserClients> userClients = users.getClients();
		if (CollectionUtils.isEmpty(users.getCostCenterAssociations()) || CollectionUtils.isEmpty(userClients)) {
			users.setEmail(customPersonalDTO.getNewEmail());
			customPersonalDTO.setMfaEmail(users.getMfaEmail());
		} else if (CollectionUtils.isNotEmpty(userClients)) {
			createAnotherUser(customPersonalDTO, userClients, users);
			users.setClients(Lists.newArrayList());
		}
	}

	private void sendEmailInUpdateEmail(CustomPersonalDTO customPersonalDTO, String token, Users users) {
		String firstName = CollectionUtils.isNotEmpty(users.getClients()) ? users.getClients().get(0).getFirstName() : users.getFirstName();
		String lastName = CollectionUtils.isNotEmpty(users.getClients()) ? users.getClients().get(0).getLastName() : users.getLastName();
		Email emailNewObj = buildEmail(firstName, lastName, customPersonalDTO.getNewEmail(),
				GenericConstants.CHANGE_EMAIL_VM);
		webClientTemplate.postForObjectWithToken(emailURL, Email.class, emailNewObj, token.split(" ")[1])
				.subscribe(emailResponse -> {
					LogUtils.debugLog.accept(
							basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_EMAIL),
							String.format("New email status: %s", emailResponse));
				});

		Email emailOldObj = buildEmail(firstName, lastName, customPersonalDTO.getOldEmail(),
				GenericConstants.CHANGE_EMAIL_VM);
		webClientTemplate.postForObjectWithToken(emailURL, Email.class, emailOldObj, token.split(" ")[1])
				.subscribe(emailResponse -> {
					LogUtils.debugLog.accept(
							basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_EMAIL),
							String.format("Old email status: %s", emailResponse));
				});
	}

	private void checkUserTypeInUpdateEmail(CustomPersonalDTO customPersonalDTO, UserPrincipal userPrincipal,
											String[] exten) {
		if (Utils.checkIfEmailHasExtension(customPersonalDTO.getNewEmail(), exten)) {
			String emailError = "The Email Address should not be in " + bbsiHeadEmails + " format";
			ErrorDetails errorDetails = new ErrorDetails(ErrorCodes.UR008, emailError);
			errorDetails.setErrorMessage(emailError);
			throw new UnauthorizedAccessException(errorDetails);
		}
		if (Utils.checkIfEmailHasExtension(customPersonalDTO.getOldEmail(), exten)
				&& (userPrincipal.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER)
				|| userPrincipal.getUserType().equals(GenericConstants.USERTYPE_BRANCH))) {
			String emailError = "The Email Address should be in " + bbsiHeadEmails + " format";
			ErrorDetails errorDetails = new ErrorDetails(ErrorCodes.UR008, emailError);
			errorDetails.setErrorMessage(emailError);
			throw new UnauthorizedAccessException(errorDetails);
		}
	}

	private void updateUserAccess(CustomPersonalDTO customPersonalDTO, String token, UserPrincipal userPrincipal,
								  String[] exten) {
		if (StringUtils.isNotEmpty(customPersonalDTO.getNewEmail())) {
			UserClients userClients = userClientRepository.findByClientCodeAndEmployeeCode(userPrincipal.getClientCode(),customPersonalDTO.getEmployeeCode());
			if(null != userClients) {
				userClients.setFirstName(customPersonalDTO.getFirstName());
				userClients.setLastName(customPersonalDTO.getLastName());
			}
			Users oldusers = validateoldEmail(customPersonalDTO, userClients);
			if(oldusers != null && StringUtils.isNotEmpty(oldusers.getMfaEmail())
					&& oldusers.getMfaEmail().equalsIgnoreCase(customPersonalDTO.getNewEmail())) {
				throw new ValidationException(new ErrorDetails(ErrorCodes.UR0027));
			}

			Users newEmailData = userRepository.findByEmail(customPersonalDTO.getNewEmail());
			if(null != newEmailData && null != oldusers) {
				mergeEmpLoginWithExistingUser(userPrincipal, oldusers, newEmailData);
				customPersonalDTO.setMfaEmail(newEmailData.getMfaEmail());
			} else if(null != oldusers) {
				changeLogin(customPersonalDTO, exten, oldusers);
			} else {
				if (null != newEmailData) {
					updateEmpLogin(customPersonalDTO, exten, newEmailData);
				} else {
					createEmpOfNewClient(customPersonalDTO, token, userPrincipal);
				}
			}
		} else if (StringUtils.isNotEmpty(customPersonalDTO.getOldEmail())){
			if(StringUtils.isEmpty(customPersonalDTO.getNewEmail())) {
				verifyAndDeleteExistingUserClients(customPersonalDTO, userPrincipal);
			} else {
				Users oldUser = userRepository.findByEmail(customPersonalDTO.getOldEmail());
				if(null != oldUser.getClients() && oldUser.getClients().size()==1 && oldUser.getClients().get(0).getClientCode().equals(customPersonalDTO.getClientCode())) {
					userRepository.delete(oldUser);
				}else {
					updateEmpLogin(customPersonalDTO, exten, oldUser);
				}
			}
		}
	}

	private void verifyAndDeleteExistingUserClients(CustomPersonalDTO customPersonalDTO, UserPrincipal userPrincipal) {
		UserClients userClients = userClientRepository.findByClientCodeAndEmployeeCode(userPrincipal.getClientCode(),customPersonalDTO.getEmployeeCode());
		if(null !=userClients ) {
			List<Long> removalIds = Lists.newArrayList();
			removalIds.add(userClients.getId());
			deleteExistingUserClients(removalIds);
		}
	}

	private Users validateoldEmail(CustomPersonalDTO customPersonalDTO, UserClients userClients) {
		Users oldusers = null;
		if(userClients != null) {
			oldusers = userClients.getUser();
			if(!oldusers.getEmail().equals(customPersonalDTO.getOldEmail())) {
				throw new UnauthorizedAccessException(
						new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
			}
			List<Object[]> userClientsList = userClientRepository.findByUserClientsByClient(userClients.getClientCode(),
					oldusers.getId());
			if(CollectionUtils.isNotEmpty(userClientsList)){
				for (Object[] userClient : userClientsList) {
					if (null != userClient[3] && StringUtils.isNotEmpty(userClient[3].toString())
							&& !customPersonalDTO.getEmployeeCode().equals(userClient[3].toString())) {
						throw new UnauthorizedAccessException(
								new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
					}
				}
			}
		}else if(null == userClients && StringUtils.isNotEmpty(customPersonalDTO.getOldEmail())) {
			throw new UnauthorizedAccessException(
					new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
		}
		return oldusers;
	}
	
	private void mergeEmpLoginWithExistingUser(UserPrincipal userPrincipal, Users oldUser, Users newUser) {
		if (CollectionUtils.isNotEmpty(oldUser.getClients())) {
			for (UserClients uc : oldUser.getClients()) {
				if (userPrincipal.getClientCode().equals(uc.getClientCode())) {
					if (CollectionUtils
							.isEmpty(newUser.getClients().stream()
									.filter(e -> (e.getClientCode().equals(uc.getClientCode())
											&& e.getUserType().equals(uc.getUserType())))
									.collect(Collectors.toList()))) {
						newUser.getClients().add(uc);
						uc.setUser(newUser);
					}
				}
			}
			userRepository.save(newUser);
		}
	}

	private void updateEmpLogin(CustomPersonalDTO customPersonalDTO, String[] exten, Users newUser) {
		Map<String, List<UserClients>> portalLoginMap = newUser.getClients().stream().collect(Collectors.groupingBy(UserClients::getClientCode));
		if (portalLoginMap.containsKey(customPersonalDTO.getClientCode())) {
			List<UserClients> existingClients = portalLoginMap.get(customPersonalDTO.getClientCode());
			boolean isAlreadyEmployee = false;
			for(UserClients uc : existingClients) {
				if(StringUtils.isEmpty(customPersonalDTO.getEmployeeCode()) || StringUtils.isNotEmpty(uc.getEmployeeCode())) {
					isAlreadyEmployee = true;
				}
			}
			if(isAlreadyEmployee) {
				throw new ValidationException(new ErrorDetails(ErrorCodes.UR0010));
			} else {
				handleMultiUser(customPersonalDTO, newUser);
			}
		} else {
			handleMultiUser(customPersonalDTO, newUser);
		}
	}
	
	private void createEmpOfNewClient(CustomPersonalDTO customPersonalDTO, String token, UserPrincipal userPrincipal) {
		UsersDTO userDTO = new UsersDTO();
		userDTO.setFirstName(customPersonalDTO.getFirstName());
		userDTO.setLastName(customPersonalDTO.getLastName());
		userDTO.setEmail(customPersonalDTO.getNewEmail());
		userDTO.setIsAccountLoginChange(Boolean.TRUE);
		userDTO.setMfaEmail(customPersonalDTO.getMfaEmail());
		userDTO.setMobile(customPersonalDTO.getMobileNumber());
		userDTO.setStatus(Boolean.TRUE);
		List<ClientDTOV2> clientDTOList = Lists.newArrayList();
		ClientDTOV2 clientDTOV2 = new ClientDTOV2();
		clientDTOV2.setClientName(userPrincipal.getClientName());
		clientDTOV2.setClientCode(userPrincipal.getClientCode());
		clientDTOV2.setEmployeeCode(customPersonalDTO.getEmployeeCode());
		clientDTOV2.setUserType(GenericConstants.USERTYPE_EMPLOYEE);
		clientDTOV2.setNewHireId(0l);
		clientDTOList.add(clientDTOV2);
		userDTO.setClients(clientDTOList);
		
		createUser(userDTO, token, null, userPrincipal);
	}

	private void setRoleForEmployee(UserClients userClients) {
		RbacEntity role = roleRepository.findByCodeAndType(GenericConstants.USERTYPE_EMPLOYEE,
				UserEnum.ROLE.toString());
		if (null != role) {
			Mapping roleMapping = null;
			if (role.getMappingId() > 0) {
				Optional<Mapping> optional = mappingRepository.findById(role.getMappingId());
				if (optional.isPresent()) {
					roleMapping = optional.get();
					ClientRole clientRole = new ClientRole();
					clientRole.setRole(roleMapping);
					clientRole.setUserClient(userClients);
					clientRole.setClientCode(userClients.getClientCode());
					List<ClientRole> clientRoles = Lists.newArrayList();
					clientRoles.add(clientRole);
					userClients.setClientRoles(clientRoles);
				}
			}

		}
	}

	private void changeLogin(CustomPersonalDTO customPersonalDTO, String[] exten, Users users) {
		List<UserClients> userClients = Lists.newArrayList();
		List<UserClients> diffClients = Lists.newArrayList();
		if(StringUtils.isNotEmpty(customPersonalDTO.getEmployeeCode())
				&& (CollectionUtils.isEmpty(users.getClients()) || users.getClients().size() > 1)) {
			handleMultiUser(customPersonalDTO, users);
		}else if(CollectionUtils.isNotEmpty(users.getClients())) {
			if ((users.getClients().size() == 1)
					&& (users.getClients().get(0).getClientCode().equals(customPersonalDTO.getClientCode())) && CollectionUtils.isEmpty(users.getCostCenterAssociations())) {
				updateUserEmail(customPersonalDTO, users);
			} else if((users.getClients().size() == 1)
					&& (users.getClients().get(0).getClientCode().equals(customPersonalDTO.getClientCode())) && CollectionUtils.isNotEmpty(users.getCostCenterAssociations())){
				insertAnotherUser(customPersonalDTO, users, userClients);
			} else {
				handleUserAccsForMutipleClients(customPersonalDTO, exten, users, userClients, diffClients);
			}
		}
		
	}

	private void handleMultiUser(CustomPersonalDTO customPersonalDTO, Users users) {
		UserClients userClient = null;
		for (UserClients uc : users.getClients()) {
			if (uc.getClientCode().equalsIgnoreCase(customPersonalDTO.getClientCode())
					&& StringUtils.isNotEmpty(uc.getEmployeeCode())
					&& uc.getEmployeeCode().equalsIgnoreCase(customPersonalDTO.getEmployeeCode())) {
				userClient = uc;
			}
		}
		if (null == userClient) {
			userClient = new UserClients();
			userClient.setUserType(GenericConstants.USERTYPE_EMPLOYEE);
			userClient.setNewHireId(0);
			userClient.setIsActive(Boolean.TRUE);
			userClient.setEmployeeCode(customPersonalDTO.getEmployeeCode());
			userClient.setFirstName(customPersonalDTO.getFirstName());
			userClient.setLastName(customPersonalDTO.getLastName());
			userClient.setClientCode(customPersonalDTO.getClientCode());
			setRoleForEmployee(userClient);
			userClient.setUser(users);
			userClientRepository.save(userClient);
			customPersonalDTO.setMfaEmail(users.getMfaEmail());
		} else {
			Users newUser = new Users();
			newUser.setEmail(customPersonalDTO.getNewEmail());
			newUser.setInvalidAttempts(0);
			newUser.setFirstName(customPersonalDTO.getFirstName());
			newUser.setLastName(customPersonalDTO.getLastName());
			newUser.setStatus(Boolean.TRUE);
			newUser.setPassword(passwordEncoder.encode(defaultPassword));
			userClient.setFirstName(customPersonalDTO.getFirstName());
			userClient.setLastName(customPersonalDTO.getLastName());
			newUser.setClients(Lists.newArrayList(userClient));
			newUser.setIsFirstLogin(Boolean.TRUE);
			userRepository.save(newUser);
			customPersonalDTO.setMfaEmail(null);
		}
	}

	private void insertAnotherUser(CustomPersonalDTO customPersonalDTO, Users users, List<UserClients> userClients) {
		if (users.getClients().get(0).getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE)) {
			userClients.addAll(users.getClients());
			createAnotherUser(customPersonalDTO, userClients, users);
		}
	}

	private void updateUserEmail(CustomPersonalDTO customPersonalDTO, Users users) {
		if (users.getClients().get(0).getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE)) {
			users.setEmail(customPersonalDTO.getNewEmail());
			userRepository.save(users);
			customPersonalDTO.setMfaEmail(users.getMfaEmail());
		}
	}

	private void handleUserAccsForMutipleClients(CustomPersonalDTO customPersonalDTO, String[] exten,
												 Users users, List<UserClients> userClients, List<UserClients> diffClients) {
		for (UserClients userClient : users.getClients()) {
			if (userClient.getClientCode().equals(customPersonalDTO.getClientCode())) {
				if (userClient.getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE)
						|| userClient.getUserType().equals(GenericConstants.USERTYPE_EXTERNAL)
						|| userClient.getUserType().equals(GenericConstants.USERTYPE_CLIENT)) {
					userClients.add(userClient);
				} else if (userClient.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER)
						|| userClient.getUserType().equals(GenericConstants.USERTYPE_BRANCH)) {
					if (Utils.checkIfEmailHasExtension(customPersonalDTO.getNewEmail(), exten)) {
						userClients.add(userClient);
					} else {
						diffClients.add(userClient);
					}
				}
			} else {
				diffClients.add(userClient);
			}
		}
		changeUserAccess(customPersonalDTO, userClients, users, diffClients);
	}

	private void changeUserAccess(CustomPersonalDTO customPersonalDTO,List<UserClients> userClients,
								  Users users, List<UserClients> diffClients) {
		if (CollectionUtils.isEmpty(diffClients)) {
			users.setEmail(customPersonalDTO.getNewEmail());
			userRepository.save(users);
			customPersonalDTO.setMfaEmail(users.getMfaEmail());
		} else {
			createAnotherUser(customPersonalDTO, userClients, users);
		}
	}

	private void createAnotherUser(CustomPersonalDTO customPersonalDTO, List<UserClients> userClients, Users users) {
		Users dbUser = new Users();
		dbUser.setEmail(customPersonalDTO.getNewEmail());
		dbUser.setInvalidAttempts(0);
		dbUser.setFirstName(users.getFirstName());
		dbUser.setLastName(users.getLastName());
		dbUser.setIsFirstLogin(users.getIsFirstLogin());
		dbUser.setIsPolicyAccepted(users.getIsPolicyAccepted());
		dbUser.setIsPolicyUpdated(users.getIsPolicyUpdated());
		dbUser.setPassword(users.getPassword());
		dbUser.setTokenValue(users.getTokenValue());
		dbUser.setClients(userClients);
		userClients.forEach(client -> client.setUser(dbUser));
		userRepository.save(dbUser);
		customPersonalDTO.setMfaEmail(null);
	}

	private Email buildEmail(String firstName, String lastName, String email, String template) {
		Email emailObj = new Email();
		Map<String, String> context = new HashMap<>();
		context.put("first_name", firstName);
		context.put("last_name", lastName);
		context.put("name", String.format("%s %s", firstName, lastName));
		emailObj.setSubject("Login Email Address Change");
		emailObj.setToAddress(email);
		emailObj.setReplyTo(email);
		emailObj.setTemplateName(template);
		context.put("client", "BBSI");
		emailObj.setContextMap(context);
		return emailObj;
	}

	@Override
	public void deleteNewHireUser(Long id) {
		try {
			List<Object[]> clients = userClientRepository.getNewhireUser(id);
			if (CollectionUtils.isNotEmpty(clients)) {
				Object[] client = clients.get(0);
				if (GenericConstants.USERTYPE_NEWHIRE.equals(String.valueOf(client[1]))) {
					userSecurityRepository.deleteByUserClient_Id(((BigInteger) client[0]).longValue());
					userClientRepository.deleteNewHireId(id);
				}
			}
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.DELETE_NEWHIRE_USER_BY_ID),
					exception, "Error occured while Deleting newhire user");
		}
	}

	@Transactional
	public void updateNewHireUser(Long newHireId, String newEmail,String mfaEmail, String mobile) {
		byte[] decodedArray;
		String decodedEmail = "";
		String decodedMfaEmail = "";

		if (!StringUtils.isEmpty(newEmail) && !newEmail.contains("@")) {
			decodedArray = org.apache.commons.codec.binary.Base64.decodeBase64(newEmail);
			decodedEmail = new String(decodedArray);
		} else if(!StringUtils.isEmpty(newEmail)) {
			decodedEmail = newEmail.toLowerCase();
		}
		
		if (!StringUtils.isEmpty(mfaEmail) && !mfaEmail.contains("@")) {
			decodedArray = org.apache.commons.codec.binary.Base64.decodeBase64(mfaEmail);
			decodedMfaEmail = new String(decodedArray);
		} else if(!StringUtils.isEmpty(mfaEmail)) {
			decodedMfaEmail = mfaEmail.toLowerCase();
		}
		
		UserClients userClient = userClientRepository.findByNewHireId(newHireId);
		if (isNewhire(userClient)) {
			Users dbUser = userRepository.findByEmail(decodedEmail);
			if (dbUser != null) {
				if(null!=userClient.getUser() && null!=userClient.getUser().getEmail() && !userClient.getUser().getEmail().equalsIgnoreCase(decodedEmail)) {
					if(checkIfDbUserContainsNewHire(dbUser,userClient.getClientCode()))
						throw new ValidationException(new ErrorDetails(ErrorCodes.UR006));
					else {
						updateNewHireUserDetails(userClient, dbUser);
						userRepository.save(dbUser);
					}
				} else if(null!=userClient.getUser() && CollectionUtils.isNotEmpty(dbUser.getClients()) && dbUser.getClients().size() == 1 
						&& userClient.getUser().getEmail().equalsIgnoreCase(decodedEmail)
						&& (StringUtils.isNotEmpty(decodedMfaEmail) || StringUtils.isNotEmpty(mobile))) {
					dbUser.setMfaEmail(decodedMfaEmail);
					dbUser.setMobile(mobile);
					userRepository.save(dbUser);
				}
			}
			else {
				Users user = userClient.getUser();
				if (user != null) {
					List<UserClients> newUserClients = userClientRepository.findByUser_Id(user.getId());
					if (newUserClients.size() == 1) {
						user.setEmail(decodedEmail);
						user.setMfaEmail(decodedMfaEmail);
						user.setMobile(mobile);
						userRepository.save(user);
					} else if (newUserClients.size() > 1) {
						Users newUser = new Users();
						newUser.setEmail(decodedEmail);
						if(null == user.getMfaEmail() || !user.getMfaEmail().equalsIgnoreCase(decodedMfaEmail)) {
							newUser.setMfaEmail(decodedMfaEmail);
						}
						newUser.setMobile(mobile);
						for (UserClients userClients : newUserClients) {
							userClients.setFirstName(userClient.getFirstName());
							userClients.setLastName(userClient.getLastName());
							userClients.setMobile(userClient.getMobile());
						}
						newUser.setPassword(passwordEncoder.encode(defaultPassword));
						newUser.setIsFirstLogin(Boolean.TRUE);
						userClient.setIsActive(Boolean.TRUE);
						userClient.setUser(newUser);
						List<UserClients> list = Lists.newArrayList();
						list.add(userClient);
						newUser.setClients(list);
						userRepository.save(newUser);
					}
				}
			}
			
		}
	}

	private void updateNewHireUserDetails(UserClients userClient, Users dbUser) {
		Users user=userClient.getUser();
		long newHireUserId=null!=user?user.getId():0L;
		userClientRepository.updateNewHireUser(userClient.getId(), dbUser.getId());
		long count=userClientRepository.getNoOfUserClients(newHireUserId);
		if(count<1 && CollectionUtils.isEmpty(user.getCostCenterAssociations())) {
			userRepository.deleteNewHireUser(newHireUserId);
		}
	}

	private boolean checkIfDbUserContainsNewHire(Users dbUser,String clientCode) {
		boolean isUserContainsNewHire=false;
		if(!CollectionUtils.isEmpty(dbUser.getClients())) {
			for(UserClients userClient: dbUser.getClients()) {
				if(clientCode.equalsIgnoreCase(userClient.getClientCode()) && (isNewhire(userClient) || isEmployee(userClient))) {
					isUserContainsNewHire=true;
					break;
				}
			}
		}
		return isUserContainsNewHire;
	}

	private boolean isNewhire(UserClients userClient) {
		return null != userClient && userClient.getUserType().equals(GenericConstants.USERTYPE_NEWHIRE);
	}
	
	private boolean isEmployee(UserClients userClient) {
		return null != userClient && userClient.getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE);
	}

	private void initiateEmail(UsersDTO userDTOV2, String oauthToken, String uiUrl, String templateName,
							   boolean includeToken, UserPrincipal userPrincipal) {

		// new hire id is empty case only we are sending the email to the user.
		if (null != userDTOV2 && CollectionUtils.isNotEmpty(userDTOV2.getClients())) {
			// After successful user creation, send an email to user with user
			// name and
			// default password.
			Email emailObj = buildEmail(userDTOV2, uiUrl, templateName, includeToken, userPrincipal);
			webClientTemplate.postForObjectWithToken(emailURL, Email.class, emailObj, oauthToken.split(" ")[1])
					.subscribe(emailResponse -> {
						LogUtils.debugLog.accept(
								basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_USER),
								String.format("Email status: %s", emailResponse));
					});
		}
	}

	@Transactional
	@Override
	public void saveUserToolbarSettings(UserPrincipal principal, List<UserToolbarSettingsDTO> userToolbarSettingDTOs) {

		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_USER_TOOLBAR_SETTINGS), "");
			
			if (CollectionUtils.isNotEmpty(userToolbarSettingDTOs)) {
				userToolbarSettingDTOs
				.forEach(uts -> uts.setUserEmail(new String(Base64.getDecoder().decode(uts.getUserEmail()))));
				
				UserToolbarSettingsDTO userToolbarSettingDTO = userToolbarSettingDTOs.get(0);
				if (null != userToolbarSettingDTO) {
					
					userToolbarSettingsRepositoryV2.deleteByClientCodeAndUserEmail(
							userToolbarSettingDTO.getClientCode(), userToolbarSettingDTO.getUserEmail());
					userToolbarSettingsRepositoryV2.flush();
				}

				// when user toolbar settings reset then menuItemId is ZERO
				// when menuItemSettings is set then menuItemId greater than
				// zero
				if (userToolbarSettingDTO != null && userToolbarSettingDTO.getMenuItemId() > 0) {
					List<UserToolbarSettings> userToolBarSettings = userToolbarSettingsMapperV2
							.userToolbarSettingsDTOListToUserToolbarSettingsList(userToolbarSettingDTOs);
					userToolbarSettingsRepositoryV2.saveAll(userToolBarSettings);
				}
			}
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_USER_TOOLBAR_SETTINGS), e,
					"Error occurred while saving user tool bar setting details");
		}
	}

	@Override
	public List<UserToolbarSettingsDTO> getUserToolbarSettings(String clientCode, String userEmail) {
		List<UserToolbarSettingsDTO> userToolbarSettingDTOs = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_TOOLBAR_SETTINGS),
					String.format("Get toolbar settings data for clientCode :: %s, userEmail :: %s", clientCode,
							userEmail));

			List<UserToolbarSettings> userToolBarSettings = userToolbarSettingsRepositoryV2
					.findByClientCodeAndUserEmail(clientCode, userEmail);

			userToolbarSettingDTOs = userToolbarSettingsMapperV2
					.userToolbarSettingsListToUserToolbarSettingsDTOList(userToolBarSettings);

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_TOOLBAR_SETTINGS), e,
					String.format(
							"Error occurred while getting the user tool bar setting details for clientCode :: %s, userEmail :: %s",
							clientCode, userEmail));
		}
		return userToolbarSettingDTOs;
	}

	/*
	 * setUserFavoriteMenus method sets the isFavourite attribute to true based
	 * on user profile menu settings
	 */
	private void setUserFavoriteMenus(String clientCode, String userName, List<MenuMappingDTO> menu) {
		List<UserToolbarSettings> userToolBarSettings = userToolbarSettingsRepositoryV2
				.findByClientCodeAndUserEmail(clientCode, userName);
		menu.forEach(menuDTO -> {
			if (CollectionUtils.isNotEmpty(userToolBarSettings)) {
				userToolBarSettings.forEach(userTBS -> {
					if (userTBS.getMenuItemId() == menuDTO.getId()) {
						menuDTO.setIsFavourite(Boolean.TRUE);
					}
				});
			}
			if (CollectionUtils.isNotEmpty(menuDTO.getMenuItems())) {
				updateMenuItems(menuDTO, userToolBarSettings);
			}
		});
	}
	
	private void updateMenuItems(MenuMappingDTO menuDTO, List<UserToolbarSettings> userToolBarSettings) {
		menuDTO.getMenuItems().forEach(menuItem -> {
			if (CollectionUtils.isNotEmpty(userToolBarSettings)) {
				userToolBarSettings.forEach(userTBS -> {
					if (userTBS.getMenuItemId() == menuItem.getId()) {
						menuItem.setIsFavourite(Boolean.TRUE);
					}
				});
			}
		});
	}

	/**
	 * Method to provide the i-9 approval details to sign the document.
	 */
	@Override
	@Transactional
	public List<UsersDTO> getI9ApprovalEmployeeList(String clientCode) {
		Map<Long, UsersDTO> map = Maps.newHashMap();
		List<UsersDTO> userDTOList = Lists.newArrayList();
		List<Object[]> userClients = Lists.newArrayList();
		try {
			userClients = userClientRepository.findByApprovalUserDetailsByClient(clientCode);
			if (CollectionUtils.isNotEmpty(userClients)) {
				Map<Long, List<CommonDTO>> privilegeMap = Maps.newHashMap();
				userClients.forEach(userClient -> {
					if (null != userClient && userClient.length>=5) {
						Long clientRole = ((BigInteger) userClient[5]).longValue();
						List<CommonDTO> privilegeDTOs = null;
						if (privilegeMap.containsKey(clientRole)) {
							privilegeDTOs = privilegeMap.get(clientRole);
						} else {
							privilegeDTOs = mappingService.getPrivileges(clientRole,UserEnum.ROLE);
							privilegeMap.put(clientRole, privilegeDTOs);
						}
						if (CollectionUtils.isNotEmpty(privilegeDTOs)) {
							privilegeDTOs.forEach(clientPrivilege -> {
								 if ((PRIVILEGE_CODE_NEW_HIRE_I9_APPROVER_F_ALL.equals(clientPrivilege.getCode())
												|| PRIVILEGE_CODE_NEW_HIRE_I9_APPROVER_F_VIEW.equals(clientPrivilege.getCode()))) {
									getUserDTO(map, userClient);
								}
							});
						}
					}
				});
			}
			buildUserDTOs(map, userDTOList);
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_I9APPROVAL_EMPLOYEELIST),
					exception, String.format("Error occurred while getting the i_9 approval list for client code:: %s",
							clientCode));
		}
		return userDTOList;
	}

	private void buildUserDTOs(Map<Long, UsersDTO> map, List<UsersDTO> userDTOList) {
		if (null != map && map.size() > 0) {
			Iterable<Users> users = userRepository.findAllById(map.keySet());
			users.forEach(user -> {
				UsersDTO userDTO = userMapper.userToUserDTO(user);
				userDTO.setFirstName(map.get(userDTO.getId()).getFirstName());
				userDTO.setLastName(map.get(userDTO.getId()).getLastName());
				userDTO.setMobile(map.get(userDTO.getId()).getMobile());
				userDTO.setFullName(userDTO.getFirstName() + " " + userDTO.getLastName());
				userDTOList.add(userDTO);
			});
		}
	}


	private void getUserDTO(Map<Long, UsersDTO> map, Object[] userClient) {
		UsersDTO userDTO = new UsersDTO();
		userDTO.setFirstName(String.valueOf(userClient[0]));
		userDTO.setLastName(String.valueOf(userClient[1]));
		userDTO.setMobile(String.valueOf(userClient[2]));
		if(userClient[4] != null) {
			map.put(((BigInteger) userClient[4]).longValue(), userDTO);
		}
	}

	/**
	 * update the i9 approval details in user_client table.
	 */
	@Override
	@Transactional
	public void updateI9ApprovalDetails(long userId, String employeeCode, String clientCode) {
		try {
			Optional<Users> userV2Optional = userRepository.findById(userId);
			if (userV2Optional.isPresent()) {
				userClientRepository.updateI9ApprovalDetails(false, clientCode);
				Users userV2 = userV2Optional.get();
				if (CollectionUtils.isNotEmpty(userV2.getClients())) {
					userV2.getClients().forEach(userClient -> {
						if ((userClient.getUserType().equalsIgnoreCase(GenericConstants.USERTYPE_EMPLOYEE)
								|| userClient.getUserType().equalsIgnoreCase(GenericConstants.USERTYPE_CLIENT)) &&
								(userClient.getClientCode().equals(clientCode) && (StringUtils.isEmpty(employeeCode)
										|| (StringUtils.isNotEmpty(userClient.getEmployeeCode())
										&& employeeCode.equals(userClient.getEmployeeCode()))))) {
							userClientRepository.updateI9ApprovalDetails(true, userClient.getId());
						}
					});
				}
			}
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_I9APPROVAL_DETAILS),
					exception,
					String.format(
							"Error occurred while updating the i_9 approval details with employee_code :: %s  client code:: %s",
							employeeCode, clientCode));
		}
	}

	@Override
	@Transactional
	public UsersDTO geti9ApprovalUser(String clientCode) {
		try {
			List<UserClients> userClientList = userClientRepository.findByClientCode(clientCode);
			if (CollectionUtils.isNotEmpty(userClientList)) {
				for (UserClients userClient : userClientList) {
					if (null != userClient.getUserType() && userClient.isI9Approver()
							&& userClient.getUserType().equalsIgnoreCase(GenericConstants.USERTYPE_EMPLOYEE)) {
						return userMapper.userToUserDTO(userClient.getUser());
					}
				}
			}
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_I9APPROVAL_USER), exception,
					String.format("Error occurred while getting  the i_9 approval details with  client code:: %s",
							clientCode));
		}
		return null;
	}

	/**
	 * get the user details based on the email : customized response
	 */
	@Override
	public UserCustomDTOV2 getUserByEmail(String email) {
		UserCustomDTOV2 userCustomerDTO = null;
		try {
			Users userV2 = userRepository.findByEmail(email);
			if (null != userV2) {
				userCustomerDTO = new UserCustomDTOV2();
				userCustomerDTO.setId(userV2.getId());
				userCustomerDTO.setFirstName(userV2.getClients().get(0).getFirstName());
				userCustomerDTO.setLastName(userV2.getClients().get(0).getLastName());
				userCustomerDTO.setEmail(userV2.getEmail());
				List<ClientDTOV2> userClientList = Lists.newArrayList();
				if (CollectionUtils.isNotEmpty(userV2.getClients())) {
					userV2.getClients().forEach(userClient -> {
						ClientDTOV2 clientDTO = new ClientDTOV2();
						clientDTO.setClientCode(userClient.getClientCode());
						clientDTO.setClientName(userClient.getClient().getClientName());
						userClientList.add(clientDTO);
					});
				}
				userCustomerDTO.setClients(userClientList);
			} else {
				LogUtils.basicErrorLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USERBY_EMAIL),
						"No user record found for given email");
				throw new ValidationException(new ErrorDetails(ErrorCodes.UR003));
			}

		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USERBY_EMAIL),
					exception,
					String.format("Error occurred while getting  user details with email_id is:: %s", email));
		}

		return userCustomerDTO;
	}

	@Override
	@Transactional
	public boolean updateUserStatus(String clientCode, String employeeCode, Boolean status) {
		boolean isUserStatusUpdated = false;
		try {
			UserClients userClient = userClientRepository.findByClientCodeAndEmployeeCode(clientCode, employeeCode);
			if (null != userClient) {
				userClientRepository.updateUserStatus(status, employeeCode, clientCode);
				isUserStatusUpdated = true;
			}
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_STATUS),
					e, "Error occurred while updating user status");
		}
		return isUserStatusUpdated;
	}

	@Override
	public List<UsersDTO> getAllClientUsersByClientCodeAndPayRollStatus(String clientCode, String payrollStatus) {
		List<UsersDTO> userDTOList = Lists.newArrayList();
		List<UsersDTO> userClientDTOList = Lists.newArrayList();
		List<UsersDTO> filtertedList = Lists.newArrayList();
		try {
			List<List<String>> privilegesListByFeatures = new ArrayList<>();
			List<String> privilegeIds = null;
			List<String> features = null;
			Sort sort = Sort.by(Sort.Direction.DESC, MethodNames.MODIFIED_ON);
			List<UserClients> userClients = userClientRepository.findByClientCode(clientCode, sort);
			userClients.addAll(populateCostCenterList(clientCode));
			
			String userTypeVal = GenericConstants.USERTYPE_CLIENT;
			if (StringUtils.isNotEmpty(payrollStatus)) {
				if (!PortalStatus.FINALIZED.getValue().equalsIgnoreCase(payrollStatus)) {
					userTypeVal = GenericConstants.USERTYPE_CLIENT;
					if (PortalStatus.SUBMITTED_FOR_APPROVAL.getValue().equalsIgnoreCase(payrollStatus)) {
						features = new ArrayList<>();
						features.add("PAY_TS_HOUR_SAVE,PAY_TS_HOUR_SUBMIT,PAY_TS_HOUR_CALC");
						features.add("PAY_TS_HOUR_SAVE,PAY_TS_HOUR_FINALIZE,PAY_TS_HOUR_CALC");
					} else if (PortalStatus.SUBMITTED_FOR_FINALIZE.getValue().equalsIgnoreCase(payrollStatus)) {
						features = new ArrayList<>();
						features.add("PAY_TS_HOUR_SAVE,PAY_TS_HOUR_FINALIZE,PAY_TS_HOUR_CALC");
					}
					if (CollectionUtils.isNotEmpty(features)) {
						for (String feature : features) {
							privilegeIds = privilegeRepository.getPrivilegeCodesByFeatureCodes("ALL", "PAY_TS_INF",
									Arrays.asList(feature.split(",")));
							if (CollectionUtils.isNotEmpty(privilegeIds)) {
								privilegesListByFeatures.add(privilegeIds);
							}
						}
					}
					for (UserClients userClient : userClients) {
						List<String> clientPrivileges = new ArrayList<>();
						if (CollectionUtils.isNotEmpty(userClient.getClientRoles())) {
							for (ClientRole clientRoleV2 : userClient.getClientRoles()) {
								if (null != clientRoleV2.getRole()) {
									List<CommonDTO> privilegeDTOs = mappingService
											.getPrivileges(clientRoleV2.getRole().getId(), UserEnum.ROLE);
									if (CollectionUtils.isNotEmpty(privilegeDTOs)) {
										List<String> clientPrivs = privilegeDTOs.stream()
												.map(clientPriv -> clientPriv.getCode()).collect(Collectors.toList());
										if (CollectionUtils.isNotEmpty(clientPrivs)) {
											clientPrivileges.addAll(clientPrivs);
										}
									}
								}
							}
						}
						if (CollectionUtils.isNotEmpty(privilegesListByFeatures)
								&& CollectionUtils.isNotEmpty(clientPrivileges)) {
							for (List<String> featPriv : privilegesListByFeatures) {
								if (clientPrivileges.containsAll(featPriv) && null != userClient.getUser()) {
									UsersDTO userDTO = userMapper.userToUserDTO(userClient.getUser());
									userDTO.setFirstName(userClient.getFirstName());
									userDTO.setLastName(userClient.getLastName());
									userDTO.setFullName(userDTO.getFirstName() + " " + userDTO.getLastName());
									userDTO.setUserType(userClient.getUserType());
									userDTOList.add(userDTO);
								}
							}
						}
					}
				} else if (PortalStatus.FINALIZED.getValue().equalsIgnoreCase(payrollStatus)) {
					userTypeVal = GenericConstants.USERTYPE_BRANCH;
					for (UserClients userClient : userClients) {
						if (null != userClient.getUser()) {
							UsersDTO userDTO = userMapper.userToUserDTO(userClient.getUser());
							userDTO.setFirstName(userClient.getFirstName());
							userDTO.setLastName(userClient.getLastName());
							userDTO.setFullName(userDTO.getFirstName() + " " + userDTO.getLastName());
							userDTO.setUserType(userClient.getUserType());
							userDTOList.add(userDTO);
						}
					}
				}
			}

			// adding first name and last name to full name property
			if (null != userDTOList && CollectionUtils.isNotEmpty(userDTOList)) {
				setUserTypeAndFullName(userDTOList, userClientDTOList, userTypeVal);
			}
			// If user is assigned for multiple clients, userClientDTOList will
			// contains duplicate user list.below will filter user list object.
			Set<Long> idSet = new HashSet<>();
			if (null != userDTOList && CollectionUtils.isNotEmpty(userClientDTOList)) {
				filtertedList = userClientDTOList.stream().filter(e -> idSet.add(e.getId()))
						.collect(Collectors.toList());
			}

		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_USERS),
					exception,
					"Error occured while retrieving all client admin users by client code and payroll status");
		}

		return filtertedList;
	}

	private void setUserTypeAndFullName(List<UsersDTO> userDTOList, List<UsersDTO> userClientDTOList,
										String userTypeVal) {
		userDTOList.forEach(user -> {
			// returning client and external user in the response
			List<ClientDTOV2> userTypes = Lists.newArrayList();
			user.setStatus(false);
			if (CollectionUtils.isNotEmpty(user.getClients())) {
				user.getClients().forEach(client -> {
					if (client.getUserType().equals(userTypeVal)) {
						userTypes.add(client);
						if (null != client.getIsActive()) {
							user.setStatus(client.getIsActive());
						}
					}
				});
			}
			if (CollectionUtils.isNotEmpty(userTypes)) {
				user.setClients(userTypes);
				userClientDTOList.add(user);
			}
		});
	}

	@Override
	public PolicyAcceptedDTO getisPolicyAccepted(String email) {
		Object obj = null;
		PolicyAcceptedDTO policyAcceptedDTO = null;

		byte[] decodedArray = org.apache.commons.codec.binary.Base64.decodeBase64(email);
		String decodedEmail = new String(decodedArray);

		try {
			obj = userRepository.getisPolicyAccepted(decodedEmail);
			if (obj != null) {
				policyAcceptedDTO = new PolicyAcceptedDTO();
				policyAcceptedDTO.setEmail(email);
				policyAcceptedDTO.setIsPolicyAccepted(Boolean.valueOf(obj.toString()));
			}

		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER), e,
					"Error occurred while updating policy accepted flag");
		}
		return policyAcceptedDTO;
	}

	@Transactional
	public void addSecurityEntry(UserPrincipal principal, String type, String value) {
		if (null != principal && StringUtils.isNotEmpty(principal.getUserType())
				&& (principal.getUserType().equals(GenericConstants.USERTYPE_CLIENT)
				|| principal.getUserType().equals(GenericConstants.USERTYPE_EXTERNAL))) {
			List<UserClients> clients = userClientRepository.findByClientCodeAndUser_Id(
					principal.getClientCode(), principal.getUserId());
			if (CollectionUtils.isNotEmpty(clients)) {
				for (UserClients client : clients) {
					if(client.getUserType().equals(GenericConstants.USERTYPE_CLIENT)
							|| client.getUserType().equals(GenericConstants.USERTYPE_EXTERNAL)) {
						UserSecurity filter = new UserSecurity();
						filter.setType(type);
						filter.setValue(value);
						filter.setUserClient(client);
						userSecurityRepository.save(filter);
					}
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Set<String>> getUserFilter(String email, String clientCode, UserPrincipal principal) {
		Map<String, Set<String>> filterMap = Maps.newHashMap();
		List<Object[]> clients = findUserClientsByType(clientCode, principal);

		if (CollectionUtils.isNotEmpty(clients)) {
			for (Object[] client : clients) {
				List<UserSecurity> filters = userSecurityRepository.findByUserClient_Id(((BigInteger) client[0]).longValue());
				if (CollectionUtils.isNotEmpty(filters)) {
					constructFilterMapByUserSecurityFilters(filters, filterMap);
				}
			}
		}
		return filterMap;
	}

	private List<Object[]> findUserClientsByType(String clientCode,UserPrincipal principal) {
		if (StringUtils.isNotEmpty(principal.getUserType())) {
			return  userClientRepository.findByUserClientsByType(clientCode, principal.getUserType(),
					principal.getUserId());
		} else {
			return userClientRepository.findByUserClientsByClient(clientCode, principal.getUserId());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Map<String, Set<String>>> getUserFilter(String email) {
		Map<String, Map<String, Set<String>>> clientFilterMap = Maps.newHashMap();

		byte[] decodedArray = org.apache.commons.codec.binary.Base64.decodeBase64(email);
		String decodedEmail = new String(decodedArray);

		List<UserClients> clients = getUserClientsByEmail(decodedEmail);

		if (CollectionUtils.isNotEmpty(clients)) {
			for (UserClients client : clients) {
				Map<String, Set<String>> filterMap = Maps.newHashMap();
				if (clientFilterMap.containsKey(client.getClientCode())) {
					filterMap = clientFilterMap.get(client.getClientCode());
				} else {
					clientFilterMap.put(client.getClientCode(), filterMap);
				}
				List<UserSecurity> filters = userSecurityRepository.findByUserClient_Id(client.getId());
				if (CollectionUtils.isNotEmpty(filters)) {
					constructFilterMapByUserSecurityFilters(filters, filterMap);
				}
			}
		}
		return clientFilterMap;
	}

	private void constructFilterMapByUserSecurityFilters(List<UserSecurity> filters,Map<String, Set<String>> filterMap) {
		for (UserSecurity filter : filters) {
			if (StringUtils.isNotEmpty(filter.getType()) && !filterMap.containsKey(filter.getType())) {
				Set<String> values = Sets.newHashSet();
				values.add(filter.getValue());
				filterMap.put(filter.getType(), values);
			} else if (StringUtils.isNotEmpty(filter.getType())) {
				filterMap.get(filter.getType()).add(filter.getValue());
			}
		}
	}

	private List<UserClients> getUserClientsByEmail(String decodedEmail) {
		if (StringUtils.isNotEmpty(decodedEmail)) {
			if (decodedEmail.contains("@")) {
				return  userClientRepository.findByUser_Email(decodedEmail);
			} else {
				return userClientRepository.findByUser_Id(Long.parseLong(decodedEmail));
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Method to get the email based on the client code and employee code
	 */
	@Override
	public String getEmailByClientCodeAndEmployeeCode(String clientCode, String empCode) {
		String email = null;
		UserClients userClient = userClientRepository.findByClientCodeAndEmployeeCode(clientCode, empCode);
		if (null != userClient && null != userClient.getUser()) {
			email = userClient.getUser().getEmail();
		}
		return email;
	}

	private static String encodeString(String text) throws UnsupportedEncodingException {
		return new String(Base64.getEncoder().encode(text.getBytes("utf-8")));
	}

	/*
	 * Method for getting personal info by client code and employee code.
	 */
	@Override
	public Map<String, Set<String>> getPersonalInfoByClientCodeAndEmployeeCode(String clientCode, String empCode,
	                                                         String token, String emailId, Long userId) {
	   Map<String, Set<String>> response = new HashMap<>();
	   response.put(PHONE_NUMBER, new HashSet<>());
	   response.put(EMAIL, new HashSet<>());
	   response.put(MFA_EMAIL, new HashSet<>());
		String mfaEmail = "";

		try {
	      /*
	       * Get user clients by employee code and client code.
	       */
		   List<Object[]> userArr = userRepository.findMobileByUserId(userId);
		   if(CollectionUtils.isNotEmpty(userArr)) {
			   mfaEmail = (String) userArr.get(0)[1];
		   }

		   if (null != userArr && userArr.size() > 0) {
			   addMobile(response, (String) userArr.get(0)[0]);
		   } 
			List<Object[]> userClients = userClientRepository.getClientUserMobile(userId);
			if (CollectionUtils.isNotEmpty(userClients)) {
				for (Object[] client : userClients) {
					addMobile(response, (String) client[0]);
				}
			}
		  if (StringUtils.isNotEmpty(empCode)) {
	         constructEmployeeInfo(clientCode, empCode, response);
	      }
		  if (StringUtils.isNotEmpty(mfaEmail)) {
			   response.get(MFA_EMAIL).add(encodeString(mfaEmail));
		   } else if (CollectionUtils.isEmpty(response.get(PHONE_NUMBER))) {
			   response.get(EMAIL).add(encodeString(emailId));
		   }
	   } catch (Exception ex) {
	      LogUtils.basicDebugLog.accept(String.format(
	            "error while fetching the phone numbers and emails for client code %s and employee code %s",
	            clientCode, empCode));
	   }
	   return response;
	}
	
	private void addMobile(Map<String, Set<String>> response, String mobile) {
		if (StringUtils.isNotEmpty(mobile)) {
			try {
				response.get(PHONE_NUMBER).add(encodeString(mobile));
			} catch (Exception e) {
				LogUtils.basicDebugLog.accept(String.format(
						"Error while encoding mobile %s", mobile));
			}
		}
	}

	private void constructEmployeeInfo(String clientCode,String empCode,Map<String, Set<String>> response) {

		try {
			String jsonResponse = null;
			String email = null;
			String mobileNumber = null;
			String mfaEmail = null;

			List<Object[]> empUsers = userClientRepository.getEmpUserName(clientCode, empCode);
			if (CollectionUtils.isNotEmpty(empUsers)) {
				String uri = boomiHelper.getUrl(BoomiEnum.EMPLOYEE_INFO);
				uri = String.format(uri, clientCode, empCode, "");
				jsonResponse = restClient.getForString(uri, Collections.emptyMap(), boomiHelper.getHeaders());

				mobileNumber = fetchFieldByPath(jsonResponse, this.CONTACT_INFO_MOBILE_NUMBER);

				if (StringUtils.isNotEmpty(mobileNumber)) {
					response.get(PHONE_NUMBER).add(encodeString(mobileNumber));
				}

				mfaEmail = (String) empUsers.get(0)[2];
				if(StringUtils.isNotEmpty(mfaEmail)) {
					response.get(MFA_EMAIL).add(encodeString(mfaEmail));
				} else if (CollectionUtils.isEmpty(response.get(PHONE_NUMBER))) {
					email = fetchFieldByPath(jsonResponse, this.CONTACT_INFO_EMAIL_ADDRESS);
					if (StringUtils.isNotEmpty(email)) {
						response.get(EMAIL).add(encodeString(email));
					}
					email = fetchFieldByPath(jsonResponse, this.PERSONAL_EMAIL_PATH);

					if (StringUtils.isNotEmpty(email)) {
						response.get(EMAIL).add(encodeString(email));
					}
				}
			}
		} catch (Exception ex) {
			LogUtils.basicDebugLog.accept(String.format(
					"error while fetching the phone numbers and emails for client code %s and employee code %s",
					clientCode, empCode));
		}

	}

	private String fetchFieldByPath(String json, String path) {
		String result = null;
		JsonNode node = null;

		try {

			node = mapper.readTree(json);
			JsonNode parentNode = node.get(path.split("\\.")[0]);

			if (parentNode != null) {
				JsonNode childNode = parentNode.get(path.split("\\.")[1]);
				/* if it has personal email add */
				if (childNode != null) {
					result = childNode.textValue().toString();
				}

			}
		} catch (Exception ex) {
			LogUtils.basicDebugLog.accept(String.format("Error while Fetching from path %s, in json %s", path, json));
		}
		return result;

	}

	@Override
	public Map<String, List<String>> getEmailsByClientCodeAndTypeAndEnum(String clientCode, String userType,
																		 String enumType) {
		if (StringUtils.isNotEmpty(enumType) && enumType.equals("LOCATION_INFO")) {
			Map<String, List<String>> userDataMap = Maps.newHashMap();
			List<String> emailList = Lists.newArrayList();
			List<String> mobileList = Lists.newArrayList();
			List<UserClients> userClients = userClientRepository.findByClientCodeAndUserType(clientCode, userType);
			if (CollectionUtils.isNotEmpty(userClients)) {
				getUserDataMap(userDataMap, emailList, mobileList, userClients);
			}
			return userDataMap;
		}
		return getEmailsByClientCodeAndType(clientCode, userType);
	}

	private void getUserDataMap(Map<String, List<String>> userDataMap, List<String> emailList, List<String> mobileList,
								List<UserClients> userClients) {
		for (UserClients userClient : userClients) {
			if(null != userClient.getIsActive() && userClient.getIsActive().equals(Boolean.TRUE)) {
				List<UserSecurity> filters = userSecurityRepository.findByUserClient_Id(userClient.getId());
				if (CollectionUtils.isNotEmpty(filters)) {
					getUserFilterData(emailList, mobileList, userClient, filters);
				} else if (null != userClient.getUser()) {
					emailList.add(userClient.getUser().getEmail());
					if (StringUtils.isNotEmpty(userClient.getMobile()))
						mobileList.add(userClient.getMobile());
				}
			}
		}
		userDataMap.put(GenericConstants.EMAIL, emailList);
		userDataMap.put(GenericConstants.TEXT, emailList);
	}

	private void getUserFilterData(List<String> emailList, List<String> mobileList, UserClients userClient,
								   List<UserSecurity> filters) {
		Map<String, Set<String>> filterMap = Maps.newHashMap();
		for (UserSecurity filter : filters) {
			if (StringUtils.isNotEmpty(filter.getType()) && !filterMap.containsKey(filter.getType())) {
				Set<String> values = Sets.newHashSet();
				values.add(filter.getValue());
				filterMap.put(filter.getType(), values);
			} else if (StringUtils.isNotEmpty(filter.getType())) {
				filterMap.get(filter.getType()).add(filter.getValue());
			}
		}
		if (CollectionUtils.isEmpty(filterMap.get("LOCATION")) && null != userClient.getUser()) {
			emailList.add(userClient.getUser().getEmail());
			if (StringUtils.isNotEmpty(userClient.getMobile()))
				mobileList.add(userClient.getMobile());
		}
	}

	/*
	 * Method for creating users and sending invitation.
	 */
	@Override
	@Transactional
	public List<UserInvitationDTO> createUserAndSendInvitation(List<UserInvitationDTO> userInvitationList, String token,
															   UserPrincipal userPrincipal, String clientCode, String base64ClientName, String uiUrl) {
		List<UsersDTO> userList = null;
		String existingEmp = "Employee already exists for the given email!";
		Map<String, String> headers = new HashMap<>();

		try {
			List<String> empList = userInvitationList.stream()
					.filter(obj -> StringUtils.isNotEmpty(obj.getEmployeeCode()))
					.map(UserInvitationDTO::getEmployeeCode).collect(Collectors.toList());
			integrationBusinessService.verifyEmployee(String.join(",", empList), token);

			userList = userInvitationList.stream().map(e -> {
				return buildUsersDtoFromUserInvitition(clientCode, base64ClientName, e);
			}).collect(Collectors.toList());

			/* check empty or not */
			if (CollectionUtils.isNotEmpty(userList)) {
				for (UsersDTO dto : userList) {
					try {
						String uri = boomiHelper.getUrl(BoomiEnum.EMPLOYEE_INFO);
						uri = String.format(uri, clientCode, dto.getEmployeeCode(), "");
						String jsonResponse = restClient.getForString(uri, Collections.emptyMap(),
								boomiHelper.getHeaders());
						String status = fetchFieldByPath(jsonResponse, EMPLOYEE_STATUS);
						/*Setting end date to empty for employees other than terminated*/
						if (null != status && !status.equals("T")) {
							dto.setEndDate(null);
						}
						if (StringUtils.isNotEmpty(status)) {
							List<Object[]> empUsers = userRepository
									.getUserDataForInvitation(clientCode, dto.getEmployeeCode(), dto.getEmail());
							if (CollectionUtils.isNotEmpty(empUsers)) {
								boolean isFirstLogin = false;
								boolean isExistingUser = false;
								for (Object[] user : empUsers) {
									isFirstLogin = Boolean.valueOf(user[4] == null ? "" : user[4].toString());
									dto.setEmail(user[3] == null ? "" : user[3].toString());
									if(null != user[5] && !user[5].toString().equals(dto.getEmployeeCode())) {
										isExistingUser = true;
										break;
									}
								}
								Object[] newhireUser = empUsers.stream().filter(emp->
								(null != emp[6] ? GenericConstants.USERTYPE_NEWHIRE.equalsIgnoreCase(emp[6].toString()):false)).findAny().orElse(null);
								if(null !=newhireUser) {
									Long newhireid= null != newhireUser[7] ? Long.parseLong(newhireUser[7].toString()) : null;
									if(null != newhireid) {
										headers.put(GenericConstants.AUTH_TOKEN_HDR, token);
										String newhireUrl = String.format(existNewhireUrl,newhireid);
										Boolean isNewhire = restClient.getForObject(newhireUrl, Collections.emptyMap(), Boolean.class, headers);
										if(!isNewhire) {
											if(null != newhireUser[8]) {
											userSecurityRepository.deleteByUserClient_Id(Long.parseLong(newhireUser[8].toString()));
											userClientRepository.deleteNewHireId(newhireid);
											}
										}
									}
								}
								
								if(!isExistingUser) {
									if (isFirstLogin) {
										initiateEmail(dto, token, uiUrl, GenericConstants.USER_LOGIN_VM, true,
												userPrincipal);
									} else {
										initiateEmail(dto, token, uiUrl, GenericConstants.REINITIATE_DOCS_VM, false,
												userPrincipal);
									}
								}
								for (UserInvitationDTO user : userInvitationList) {
									if (null != user && user.getEmployeeCode().equals(dto.getEmployeeCode())) {
										user.setStatus(!isExistingUser);
										user.setUserType(existingEmp);
									}
								}
							} else {
								List<Object[]> users = userRepository.getUserDataByEmail(dto.getEmail(), clientCode);
								boolean present = false;
								if (CollectionUtils.isNotEmpty(users)) {
									boolean isFirstLogin = false;
									for (Object[] user : users) {
										isFirstLogin = Boolean.valueOf(user[4] == null ? "" : user[4].toString());
										if (null != user[5]
												&& user[5].toString().equals(GenericConstants.USERTYPE_EMPLOYEE)) {
											present = true;
											userInvitationList.forEach(invite -> {
												if (null != invite
														&& invite.getEmployeeCode().equals(dto.getEmployeeCode())) {
													invite.setStatus(false);
													invite.setUserType(existingEmp);
												}
											});
										}
									}
									Object[] newhireUser = users.stream().filter(emp->
									(null != emp[5] ? GenericConstants.USERTYPE_NEWHIRE.equalsIgnoreCase(emp[5].toString()):false)).findAny().orElse(null);
									if(null !=newhireUser) {
										Long newhireid= null != newhireUser[6] ? Long.parseLong(newhireUser[6].toString()) : null;
										if(null != newhireid) {
											headers.put(GenericConstants.AUTH_TOKEN_HDR, token);
											String newhireUrl = String.format(existNewhireUrl,newhireid);
											Boolean isNewhire = restClient.getForObject(newhireUrl, Collections.emptyMap(), Boolean.class, headers);
											if(!isNewhire) {
												if(null != newhireUser[7]) {
												userSecurityRepository.deleteByUserClient_Id(Long.parseLong(newhireUser[7].toString()));
												userClientRepository.deleteNewHireId(newhireid);
												}
											}
										}
									}
									if (present) {
										if (isFirstLogin) {
											initiateEmail(dto, token, uiUrl, GenericConstants.USER_LOGIN_VM, true,
													userPrincipal);
										} else {
											initiateEmail(dto, token, uiUrl, GenericConstants.REINITIATE_DOCS_VM, false,
													userPrincipal);
										}
									}
								}
								if (!present) {
									if (status.equals("T")) {
										RbacEntity role = roleRepository.findByCodeAndType(
												MethodNames.TERMINATED_EMPLOYEE_ROLE_CODE, UserEnum.ROLE.toString());
										if (null != role) {
											dto.getClients().get(0).setClientRoles(Lists.newArrayList());
											UserClientRoleDTOV2 clientRoleDTOV2 = new UserClientRoleDTOV2();
											clientRoleDTOV2.setRoleId(role.getMappingId());
											dto.getClients().get(0).getClientRoles().add(clientRoleDTOV2);
										}
									}
									String oldMfaEmail =dto.getMfaEmail();
									UsersDTO newUserDTO = this.createUser(dto, token, uiUrl, userPrincipal);
									userInvitationList.forEach(user -> {
										if (null != user && user.getEmployeeCode().equals(dto.getEmployeeCode())) {
											user.setStatus(true);
											user.setUserType("Success!");
											if ((oldMfaEmail != null && !oldMfaEmail.equalsIgnoreCase(newUserDTO.getMfaEmail()))
													|| (newUserDTO.getMfaEmail() != null && !newUserDTO.getMfaEmail().equalsIgnoreCase(oldMfaEmail))) {
												invokeEmpEligibility(clientCode, newUserDTO.getEmployeeCode(), newUserDTO.getMfaEmail(), token);
											}
										}
									});
								}
							}
							updatedisCCPAforEmployee(userPrincipal, clientCode, dto, jsonResponse);
						} else {
							userInvitationList.forEach(invite -> {
								if (null != invite && invite.getEmployeeCode().equals(dto.getEmployeeCode())) {
									invite.setStatus(false);
									invite.setUserType("Invalid Employee!");
								}
							});
						}

					} catch (Exception ex) {
						/* log and supress */
						LogUtils.errorLog.accept(
								basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_STATUS),
								"Error occured while creating user info %s ", ex);
						userInvitationList.forEach(invite -> {
							if (null != invite && invite.getEmployeeCode().equals(dto.getEmployeeCode())) {
								invite.setStatus(false);
								if(ex instanceof ValidationException && null != ((ValidationException)ex).getErrorResponse()) {
									invite.setUserType(((ValidationException)ex).getErrorResponse().getErrorMessage());
								} else {
									invite.setUserType("Unable to create Employee!");
								}
							}
						});
					}
				}
			}
			/* Update last sent date */
			List<String> empCodes = userInvitationList.parallelStream().map(UserInvitationDTO::getEmployeeCode)
					.collect(Collectors.toList());
			/* Save all client code and employee codes List */
			employeeInvitationBusinessService.saveAllByClientCodeAndEmployeeId(clientCode, empCodes);

		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_STATUS),
					e, "Error occurred while sending invitation to the user");
		}
		return userInvitationList;
	}

	private void updatedisCCPAforEmployee(UserPrincipal userPrincipal, String clientCode, UsersDTO dto,
										  String jsonResponse) {
		if (null != jsonResponse) {
			JSONObject jsonObject = new JSONObject(jsonResponse);
			JSONObject residentialAddress = jsonObject.getJSONObject("residential_address");
			if (null != residentialAddress) {
				String state = residentialAddress.getString(STATE);
				if (null != state && state.equalsIgnoreCase(GenericConstants.CA)) {
					isCCPAUpdated(clientCode, dto.getEmployeeCode(), Boolean.TRUE, userPrincipal);
				} else {
					isCCPAUpdated(clientCode, dto.getEmployeeCode(), Boolean.FALSE, userPrincipal);
				}
			} else {
				String uri = boomiHelper.getUrl(BoomiEnum.EMPLOYEE_WORK_ASSIGNMENT);
				uri = String.format(uri, clientCode, dto.getEmployeeCode(), "");
				String response = restClient.getForString(uri, Collections.emptyMap(), boomiHelper.getHeaders());
				JSONObject assing = new JSONObject(response);
				String wclocation = assing.getString(WORK_SITE_LOCATION);
				if (null != wclocation) {
					LocationInfo workSiteLocations = null;
					String url = boomiHelper.getUrl(BoomiEnum.LOCATION_DETAILS);
					String strResponse = restClient.getForString(String.format(url, clientCode, wclocation),
							Collections.emptyMap(), boomiHelper.getHeaders());
					if (null != strResponse) {
						TypeToken<LocationInfo> tokenType = new TypeToken<LocationInfo>() {
						};
						workSiteLocations = new Gson().fromJson(strResponse, tokenType.getType());
						if (null != workSiteLocations.getState()
								&& workSiteLocations.getState().equalsIgnoreCase(GenericConstants.CA)) {
							isCCPAUpdated(clientCode, dto.getEmployeeCode(), Boolean.TRUE, userPrincipal);
						} else{
							isCCPAUpdated(clientCode, dto.getEmployeeCode(), Boolean.FALSE, userPrincipal);
						}
					}
				}

			}
		}
	}

	/**
	 * Method for building users from user invitition.
	 *
	 * @param clientCode
	 * @param base64ClientName
	 * @param e
	 * @return Returns {@link UsersDTO}.
	 */
	private UsersDTO buildUsersDtoFromUserInvitition(String clientCode, String base64ClientName, UserInvitationDTO e) {
		UsersDTO user = new UsersDTO();
		user.setFirstName(e.getFirstName());
		user.setLastName(e.getLastName());
		user.setEmail(e.getEmail().trim().toLowerCase());
		user.setEmployeeCode(e.getEmployeeCode());
		user.setMobile(e.getMobile());
		user.setStatus(true);
		user.setMfaEmail(e.getMfaEmail());
		if(null != e.getStatusDate()) {
			user.setEndDate(e.getStatusDate().plusMonths(18));
		}
		List<ClientDTOV2> clientDTOList = Lists.newArrayList();
		ClientDTOV2 clientDTOV2 = new ClientDTOV2();
		/* decode base 64 name */
		if (StringUtils.isNotEmpty(base64ClientName)) {

			try {
				base64ClientName = URLDecoder.decode(base64ClientName, "UTF-8");
				clientDTOV2.setClientName(new String(Base64.getDecoder().decode(base64ClientName)));
			} catch (Exception ex) {
				LogUtils.errorLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(), "buildUsersDtoFromUserInvitition"),
						"Error while decoding client name info %s ", ex);
			}
		}
		clientDTOV2.setClientCode(clientCode);
		clientDTOV2.setEmployeeCode(e.getEmployeeCode());
		clientDTOV2.setUserType(GenericConstants.USERTYPE_EMPLOYEE);
		clientDTOV2.setNewHireId(0l);
		clientDTOV2.setIsActive(true);
		clientDTOList.add(clientDTOV2);
		user.setClients(clientDTOList);
		return user;
	}

	public void updateTimenetLoginInfo(TimenetLoginInfo timenetLoginInfo, String clientCode, UserPrincipal principal) {
		try {
			List<Object[]> userClients = userClientRepository.findByUserClientsByClient(clientCode,
					principal.getUserId());
			if (CollectionUtils.isNotEmpty(userClients)) {
				userClientRepository.updateTimeNetInfo(timenetLoginInfo.getRememberTimenetCompanyId(),
						timenetLoginInfo.getRememberTimenetUserId(),
						timenetLoginInfo.getRememberTimenetPassword(), timenetLoginInfo.getTimenetCompanyId(),
						timenetLoginInfo.getTimenetUserId(), timenetLoginInfo.getTimenetPassword(),
						principal.getUserId(), clientCode);
			}
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_TIMENET_DETAILS), e,
					"Error occurred while updating timenetlogin deatils");
		}
	}

	@Override
	public TimenetLoginInfo getTimenetLoginInfo(String clientCode, UserPrincipal principal) {
		TimenetLoginInfo timenetLoginInfo = null;
		try {
			List<UserClients> userClients = userClientRepository.findByClientCodeAndUser_Id(clientCode,
					principal.getUserId());
			if (CollectionUtils.isNotEmpty(userClients)) {
				for (UserClients userClient : userClients) {
					if ((null != userClient.getRememberTimenetCompanyId()
							&& userClient.getRememberTimenetCompanyId().equals(Boolean.TRUE))
							|| (null != userClient.getRememberTimenetUserId()
							&& userClient.getRememberTimenetUserId().equals(Boolean.TRUE))
							|| (null != userClient.getRememberTimenetPassword()
							&& userClient.getRememberTimenetPassword().equals(Boolean.TRUE))) {
						timenetLoginInfo = new TimenetLoginInfo();
						timenetLoginInfo.setRememberTimenetCompanyId(userClient.getRememberTimenetCompanyId());
						timenetLoginInfo.setRememberTimenetUserId(userClient.getRememberTimenetUserId());
						timenetLoginInfo.setRememberTimenetPassword(userClient.getRememberTimenetPassword());
						timenetLoginInfo.setTimenetCompanyId(userClient.getTimenetCompanyId());
						timenetLoginInfo.setTimenetUserId(userClient.getTimenetUserId());
						timenetLoginInfo.setTimenetPassword(userClient.getTimenetPassword());
					}
				}
			}
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_TIMENET_DETAILS), e,
					"Error occurred while getting timenetlogin deatils");
		}
		return timenetLoginInfo;
	}

	@Async
	@Override
	public String getClientNameByClientCode(String clientCode) {
		String response = null;
		ClientMaster clientMaster = getClientMaster(clientCode);

		if (null != clientMaster) {
			Map<String, String> headers = boomiHelper.getHeaders();
			try {
				RestTemplate restTemplate = GenericUtils.getRestTemplate(120000);
				response = restClient.getForString(restTemplate, String.format(clientInfo, clientCode), new HashMap<>(),
						headers);
				ObjectMapper res = GenericUtils.getObjectMapperInstance.get();
				JsonNode node = res.readTree(response);

				if (node.get(MethodNames.COMPANY_DOING_BUSINESS_AS) != null) {
					String companyName = node.get(MethodNames.COMPANY_DOING_BUSINESS_AS).textValue();
					if (companyName.trim().equals(clientMaster.getClientName())) {
						return companyName;
					} else {
						clientMasterRepository.updateClientNameByClientCode(companyName, LocalDateTime.now(), clientCode);
					}
				}
			} catch (Exception e) {
				LogUtils.basicErrorLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(), "getClientNameByClientCode"),
						"Error occurred while getting client name by client code");
			}
		}
		return null;
	}

	@Override
	public Map<String, String> getTerminatedClients(Long userId, String token) {
		Map<String, String> terminatedClients = new HashMap<>();
		Set<String> costCenterCodeSet = new HashSet<>();
		try {
			List<UserCostCenterAssociation> costCenterAssocs = userCostCenterAssociationRepository.findByUserId(userId);
			if(CollectionUtils.isNotEmpty(costCenterAssocs)) {
                for (UserCostCenterAssociation userCostCenterAssociation : costCenterAssocs) {
                    List<ClientMaster> clientMasters = clientMasterRepository.findByCostCenterCode(userCostCenterAssociation.getCostCenterCode());
                    for(ClientMaster clientMaster : clientMasters) {
                        terminatedClients.put(clientMaster.getClientCode(), clientMaster.getClientName());
                    }
                    costCenterCodeSet.add(userCostCenterAssociation.getCostCenterCode());
				}
				String url = boomiHelper.getUrl(BoomiEnum.COSTCENTER_CLIENTLIST);
				Map<String, String> headers = boomiHelper.getHeaders();
				Set<String> terminatedClientCodes = getTerminatedClientCodes(costCenterCodeSet, url, headers);
				if (CollectionUtils.isEmpty(terminatedClientCodes)) {
					return new HashMap<>();
				}
				terminatedClients.entrySet().removeIf(entry -> (!terminatedClientCodes.contains(entry.getKey())));

			}
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_TERMINATED_CLIENTS_OF_BRANCH),
					e, "Error occurred while retrieving terminated clients of a branch");
		}
		return terminatedClients;
	}

	/**
	 * This will get the terminated cliencodes
	 *
	 * @param costCenterCodeSet
	 * @param url
	 * @param headers
	 * @return
	 */
	private Set<String> getTerminatedClientCodes(Set<String> costCenterCodeSet, String url,
												 Map<String, String> headers) {
		Set<String> clientCodeSet = new HashSet<>();
		try {
			if (CollectionUtils.isNotEmpty(costCenterCodeSet)) {
				RestTemplate restTemplate = GenericUtils.getRestTemplate(120000);
				costCenterCodeSet.forEach(costCenterCode -> {
					String jsonResponse = restClient.getForString(restTemplate, String.format(url, costCenterCode),
							new HashMap<>(), headers);
					if (null != jsonResponse) {
						JSONArray jsonarray = new JSONArray(jsonResponse);
						for (int i = 0; i < jsonarray.length(); i++) {
							JSONObject jsonobject = jsonarray.getJSONObject(i);
							if (jsonobject.has(MethodNames.STATUS) && null != jsonobject.getString(MethodNames.STATUS)
									&& "T".equalsIgnoreCase(jsonobject.getString(MethodNames.STATUS).toString())) {
								clientCodeSet.add(jsonobject.getString(MethodNames.CLIENT_ID).toString());
							}
						}
					}

				});
			}
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "getTerminatedClientCodes"), e,
					"Error occurred while getting codes of terminated clients");
		}
		return clientCodeSet;
	}

	Function<UserClients, String> getClientCode = uc -> {
		return uc.getClientCode();

	};

	@Override
	public void updatePortalAccessForNewClients() {
		try {
			List<ClientMaster> clients = clientMasterRepository.findAll();
			Map<String, String> clientCostCenterMap = clients.stream()
					.filter(cc -> StringUtils.isNotEmpty(cc.getClientCode()))
					.collect(Collectors.toMap(ClientMaster::getClientCode, ClientMaster::getCostCenterCode));			

			String costCenterUrl = boomiHelper.getUrl(BoomiEnum.COST_CENTER);
			List<CostCenterDTO> costcenterDTOs = webClientTemplate.getForObjectList(costCenterUrl, CostCenterDTO.class,
					boomiHelper.getHeaders());
			LogUtils.infoLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.UPDATE_PORTAL_ACCESS_FOR_NEW_CLIENTS),
					String.format(COST_CENTERS_MSG, costcenterDTOs));
			Map<String, String> costCenterMap = costcenterDTOs.stream()
					.filter(cc -> StringUtils.isNotEmpty(cc.getCode()))
					.collect(Collectors.toMap(CostCenterDTO::getCode, CostCenterDTO::getDescription));

			String url = boomiHelper.getUrl(BoomiEnum.PRISMIZED_CLIENTS);
			List<PrismizedClientsDTO> prismizedClientsDTOs = webClientTemplate.getForObjectList(url,
					PrismizedClientsDTO.class, boomiHelper.getHeaders());
			LogUtils.infoLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.UPDATE_PORTAL_ACCESS_FOR_NEW_CLIENTS),
					String.format("Prismized Clients: %s", prismizedClientsDTOs));
			
			populateDetailsForPortalAccess(prismizedClientsDTOs,costCenterMap, clientCostCenterMap);
			
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.UPDATE_PORTAL_ACCESS_FOR_NEW_CLIENTS),
					e, "Error occurred while updating update PortalAccess For NewClients details");
		}
	}
	
	private void populateDetailsForPortalAccess(List<PrismizedClientsDTO> prismizedClientsDTOs, Map<String, String> costCenterMap, Map<String, String> clientCostCenterMap) {
		if (CollectionUtils.isNotEmpty(prismizedClientsDTOs)) {
			List<String> prisamisedclientCodes = prismizedClientsDTOs.stream()
					.map(PrismizedClientsDTO::getClient_code).collect(Collectors.toList());
			
			populateNewCostCenters(prisamisedclientCodes, costCenterMap, clientCostCenterMap);
		}
	}
	
	private void populateNewCostCenters(List<String> prisamisedclientCodes, Map<String, String> costCenterMap, Map<String, String> clientCostCenterMap) {
		Set<String> newCostCenters = Sets.newHashSet();
		if (CollectionUtils.isNotEmpty(prisamisedclientCodes)) {
			List<CostCenterClientDTO> clientDTOs = getClientsByMultipleCostCenters(Lists.newArrayList());
			
			prisamisedclientCodes.forEach(client -> {
				if(!clientCostCenterMap.containsKey(client)) {
					String costCenter = addNewClient(client, costCenterMap, clientDTOs);
					if(StringUtils.isNotEmpty(costCenter) && !clientCostCenterMap.containsValue(costCenter)) {
						newCostCenters.add(costCenter);
					}
				}
			});
			if(!newCostCenters.isEmpty()) {
				Map<String, Set<Long>> newUsersMap = addNewCostCenters(newCostCenters);
				buildAndSaveAudit(newUsersMap);
			}
		}
	}
	
	private Map<String, Set<Long>> addNewCostCenters(Set<String> newCostCenters) {
		Map<String, Set<Long>> newUsersMap = Maps.newHashMap();
		List<UserCostCenterAssociation> userCostCenters = userCostCenterAssociationRepository.findByUserType(GenericConstants.USERTYPE_VANCOUVER);
		Map<Long, Set<String>> userCostCenterMap = userCostCenters.stream()
				.collect(Collectors.groupingBy(uc -> uc.getUser().getId(), 
						Collectors.mapping(UserCostCenterAssociation::getCostCenterCode, Collectors.toSet())));
		Map<Long, Users> userMap = Maps.newHashMap();
		userCostCenters.stream()
				.filter(uc -> !userMap.containsKey(uc.getUser().getId()))
				.collect(Collectors.toMap(uc -> uc.getUser().getId(), uc -> uc.getUser()));
		List<UserCostCenterAssociation> newUserCostCenters = Lists.newArrayList();
		newCostCenters.forEach(costCenterCode -> {
			newUsersMap.put(costCenterCode, Sets.newHashSet());
			for(Long userId : userCostCenterMap.keySet()) {
				if(userCostCenterMap.get(userId).contains(costCenterCode)) {
					UserCostCenterAssociation assoc = new UserCostCenterAssociation();
					assoc.setCostCenterCode(costCenterCode);
					assoc.setUserType(GenericConstants.USERTYPE_VANCOUVER);
					assoc.setUser(userMap.get(userId));
					newUserCostCenters.add(assoc);
					newUsersMap.get(costCenterCode).add(userId);
				}
			}
		});
		userCostCenterAssociationRepository.saveAll(newUserCostCenters);
		return newUsersMap;
	}
	
	private String addNewClient(String clientCode, Map<String, String> costCenterMap, List<CostCenterClientDTO> clientDTOs) {
		for(CostCenterClientDTO dto : clientDTOs) {
			if(clientCode.equals(dto.getClientId())) {
				String costCenterCode = dto.getCostCenter();
				saveClientMaster(dto.getClientId(), dto.getClientName(), costCenterCode,
							costCenterMap.get(costCenterCode));
				return costCenterCode;
			}
		}
		return null;
	}
	
	@Override
	public void updateClientMaster() {
		Map<String, String> headers = boomiHelper.getHeaders();
		String costCenterUrl = boomiHelper.getUrl(BoomiEnum.COST_CENTER);
		List<CostCenterDTO> costcenterDTOs = webClientTemplate.getForObjectList(costCenterUrl, CostCenterDTO.class,
				headers);
		LogUtils.infoLog.accept(
				basicMethodInfo.apply(getClass().getCanonicalName(),
						MethodNames.UPDATE_CLIENT_MASTER_DETAILS),
				String.format(COST_CENTERS_MSG, costcenterDTOs));
		Map<String, String> costCenterMap = costcenterDTOs.stream()
				.filter(cc -> StringUtils.isNotEmpty(cc.getCode()))
				.collect(Collectors.toMap(CostCenterDTO::getCode, CostCenterDTO::getDescription));
	
		List<CostCenterClientDTO> clientDTOs = getClientsByMultipleCostCenters(Lists.newArrayList());
		List<ClientMaster> clients = clientMasterRepository.findAll();
		for(ClientMaster client : clients) {
			for(CostCenterClientDTO clientDTO : clientDTOs) {
				if(client.getClientCode().equals(clientDTO.getClientId())) {
					client.setClientName(clientDTO.getClientName());
					client.setCostCenterCode(clientDTO.getCostCenter());
					client.setCostCenterDescription(costCenterMap.get(clientDTO.getCostCenter()));
					client.setModifiedOn(LocalDateTime.now());
					break;
				}
			}
		}
		saveClientMaster(clients);
	}
	
	@Transactional
	public void saveClientMaster(List<ClientMaster> clients) {
		clientMasterRepository.saveAll(clients);
	}

	private void buildAndSaveAudit(Map<String, Set<Long>> newUsersMap) {
		String newDataString = GenericUtils.gsonInstance.get().toJson(newUsersMap);
		UserPrincipal userPrincipal = new UserPrincipal(GenericConstants.INTEGRATION_ADMIN, "******", Lists.newArrayList());
		userPrincipal.setClientCode("1234");
		userPrincipal.setUserType(GenericConstants.USERTYPE_VANCOUVER);
		userPrincipal.setEmail(GenericConstants.INTEGRATION_ADMIN);
		userPrincipal.setFirstName("Integration");
		userPrincipal.setLastName("User");

		auditDetailsUtil.buildAuditDetailsAndSave(Enums.UserEnum.CLIENT.toString(), EMAIL,
				GenericConstants.INTEGRATION_ADMIN, null, Enums.UserEnum.CLIENT.toString(), userPrincipal,
				null, newDataString, Enums.AuditDetailsEventEnum.INSERT.toString(), null);
	}

	@Override
	public List<CostCenterClientDTO> getCostCenterPrismizedClients(String code) {
		List<CostCenterClientDTO> costCenterClientList = Lists.newArrayList();
		try {
			String url = boomiHelper.getUrl(BoomiEnum.PRISMIZED_CLIENTS);
			List<PrismizedClientsDTO> prismizedClientsDTOs = webClientTemplate.getForObjectList(url,
					PrismizedClientsDTO.class, boomiHelper.getHeaders());
			if (CollectionUtils.isNotEmpty(prismizedClientsDTOs)) {
				List<String> prisamisedclientCodes = prismizedClientsDTOs.stream()
						.map(PrismizedClientsDTO::getClient_code).collect(Collectors.toList());
				List<CostCenterClientDTO> clientDTOs = getClientsByMultipleCostCenters(Lists.newArrayList(code));
				for(CostCenterClientDTO clientDTO : clientDTOs) {
					if(prisamisedclientCodes.contains(clientDTO.getClientId())) {
						clientDTO.setIsSelected(Boolean.TRUE);
						costCenterClientList.add(clientDTO);
					}
				}
			}
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), "getCostCenterPrismizedClients"), e,
					"Error occurred while getting prismized clients of cost center");
		}
		return costCenterClientList;
	}

	@Override
	public List<String> getdistinctClients() {
		List<String> clients = new ArrayList<>();
		try {
			clients = userRepository.getdistinctClients();
		} catch (Exception ex) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "getdistinctClients"), ex,
					"Error occurred while getting distinct Clients ");
		}
		return clients;
	}

	@Override
	public String mfaOtpValidation(OtpDetailsDTO otpDetailsDTO, String token) {
		// TODO Auto-generated method stub
		Map<String, String> headers = new HashMap<>();
		String response = null;
		headers.put(GenericConstants.AUTH_TOKEN_HDR, token);
		try {
			response = restClient.postForString(verifyOtpURL, otpDetailsDTO,headers);
		} catch (Exception e) {
			return "";
		}
		return response;
	}

	public List<CostCenterClientDTO> getClientsByMultipleCostCenters(List<String> costcentercodes) {
		List<CostCenterClientDTO> clients = Lists.newArrayList();
		try {
			String url = boomiHelper.getUrl(BoomiEnum.GET_CLIENTS_BY_MULTIPLE_COSTCENTERS);
			clients = webClientTemplate.getForObjectList(
					String.format(url, costcentercodes.stream().collect(Collectors.joining(","))),
					CostCenterClientDTO.class, boomiHelper.getHeaders());
		} catch (Exception ex) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), "getClientsByMultipleCostCenters"), ex,
					"Error occurred while getting all clients by Cost Centers");
		}
		return clients;
	}

	/**
	 * Employee status terminated to active[re-hire] functionality
	 */
	@Override
	@Transactional
	public UsersDTO createRehireUser(UsersDTO usersDTO, String oauthToken, String uiUrl, UserPrincipal userPrincipal) {
		try {
			usersDTO.setEmail(usersDTO.getEmail().trim().toLowerCase());
			Users dbUser = userRepository.findByEmail(usersDTO.getEmail());
			if (null != dbUser && CollectionUtils.isNotEmpty(dbUser.getClients())) {
				boolean isEmploee = false;
				for (UserClients userClient : dbUser.getClients()) {
					// client code and employee code validation
					if (userClient.getClientCode().equals(usersDTO.getClients().get(0).getClientCode())
							&& userClient.getUserType().equals(GenericConstants.USERTYPE_EMPLOYEE.toString())) {

						userClientRepository.updateEmployeeUserTypeToNewHireUserStatus(
								GenericConstants.USERTYPE_NEWHIRE.toString(),
								usersDTO.getClients().get(0).getNewHireId(), userClient.getEmployeeCode(),
								userClient.getClientCode());
						clientRoleRepository.deleteRole(userClient.getId());

						// re-initiate the email
						initiateEmail(usersDTO, oauthToken, uiUrl, GenericConstants.REINITIATE_DOCS_VM, false,
								userPrincipal);
						isEmploee = true;
						break;

					}
				}
				if (!isEmploee) {
					// user is available in db and user type employee is not
					// existed for this client code case
					usersDTO.setId(dbUser.getId());
					createUser(usersDTO, oauthToken, uiUrl, userPrincipal);
				}

			} else {
				createUser(usersDTO, oauthToken, uiUrl, userPrincipal);
			}
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_REHIRE_USER),
					e, "Error occurred during terminated to rehire-user creation");
		}
		return usersDTO;
	}

	public void removeToken(TokenStore tokenStore, boolean removeOtherUsers) {
		passwordBusinessServiceImpl.removeToken(tokenStore, removeOtherUsers);
	}

	@Override
	public void isCCPAUpdated(String clientCode, String employeeCode, Boolean isCCPAUpdated, UserPrincipal principal) {
		try {
			userClientRepository.isCCPAUpdated(isCCPAUpdated, employeeCode, clientCode);
		} catch (Exception ex) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), "isCCPAUpdated"), ex,
					"Error occurred while getting all clients by Cost Centers");
		}

	}

	@Async
	@Override
	public void updateCaliforniaFlag(UserPrincipal principal) {
		try {

			List<Object[]> clients = userClientRepository
					.getCaliforniaUsers(principal.getClientCode(), principal.getUserId());
			if (CollectionUtils.isNotEmpty(clients)) {

				clients.forEach(client -> {
					if (null != client && (null == client[1]
							|| ((Boolean)client[1]).equals(Boolean.FALSE))) {
						String empCode = (String)client[0];
						String infoUri = boomiHelper.getUrl(BoomiEnum.EMPLOYEE_INFO);
						infoUri = String.format(infoUri, principal.getClientCode(), empCode, "");
						RestTemplate restTemplate = GenericUtils.getRestTemplate(120000);
						String jsonResponse = restClient.getForString(restTemplate, infoUri, Collections.emptyMap(),
								boomiHelper.getHeaders());
						String state = fetchFieldByPath(jsonResponse, "residential_address.state");
						boolean isCAUser = Boolean.FALSE;
						if (StringUtils.isNotEmpty(state)
								&& (state.equals(GenericConstants.CA) || state.toUpperCase().equals("CALIFORNIA"))) {
							isCAUser = Boolean.TRUE;
							userClientRepository.isCCPAUpdated(isCAUser, empCode,
									principal.getClientCode());
						} else {
							String assignmentUri = boomiHelper.getUrl(BoomiEnum.EMPLOYEE_WORK_ASSIGNMENT);
							assignmentUri = String.format(assignmentUri, principal.getClientCode(),
									empCode);
							String assignmentResponse = restClient.getForString(restTemplate, assignmentUri,
									Collections.emptyMap(), boomiHelper.getHeaders());
							if (!StringUtils.isEmpty(assignmentResponse)) {
								JsonNode jsonNode = null;
								try {
									jsonNode = mapper.readTree(assignmentResponse);
								} catch (IOException e) {
									LogUtils.basicErrorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(),
											UPDATE_CALIFORNIA_FLAG), e.getMessage());
								}
								if (null != jsonNode.get(WORK_SITE_LOCATION)) {
									String locationCode = jsonNode.get(WORK_SITE_LOCATION).asText();
									if (!StringUtils.isEmpty(locationCode)) {
										String url = boomiHelper.getUrl(BoomiEnum.LOCATION_DETAILS);
										String strResponse = restClient.getForString(restTemplate,
												String.format(url, principal.getClientCode(), locationCode),
												Collections.emptyMap(), boomiHelper.getHeaders());
										if (!StringUtils.isEmpty(strResponse) && strResponse.contains(STATE)) {
											JsonNode locNode = null;
											try {
												locNode = mapper.readTree(strResponse);
											} catch (IOException e) {
												LogUtils.basicErrorLog.accept(basicMethodInfo
																.apply(getClass().getCanonicalName(), UPDATE_CALIFORNIA_FLAG),
														e.getMessage());
											}
											if (locNode.get(STATE) != null) {
												String workLocationState = locNode.get(STATE).asText();
												if (StringUtils.isNotEmpty(workLocationState) && (workLocationState
														.equals(GenericConstants.CA)
														|| workLocationState.toUpperCase().equals("CALIFORNIA"))) {
													isCAUser = Boolean.TRUE;
												}
												userClientRepository.isCCPAUpdated(isCAUser, empCode,
														principal.getClientCode());
											}
										}
									}
								}
							}
						}
						if (null != client[2] && ((Boolean)client[2]).equals(Boolean.TRUE)
								&& !isCAUser) {
							userClientRepository.isCCPAUpdated(isCAUser, empCode,
									principal.getClientCode());
						}
					}
				});

			}

		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), UPDATE_CALIFORNIA_FLAG), e,
					"Error occurred while updateCaliforniaFlag");
		}
	}

	public Map<String, String> getADKeys(boolean reload) {
		if (reload) {
			updateADKeys();
		}
		return adPublicKeys;
	}

	@PostConstruct
	private void updateADKeys() {
		String strKeys = "keys";
		String strKeyId = "kid";
		String strKeyField = "x5c";
		try {
			RestTemplate restTemplate = GenericUtils.getRestTemplate(GenericConstants.DEFAULT_TIMEOUT);
			String response = restClient.getForString(restTemplate, adKeysURL, Maps.newHashMap(), Maps.newHashMap());
			LogUtils.infoLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_AD_KEYS),
					String.format("AD Response: %s", response));
			if (StringUtils.isNotEmpty(response)) {
				JsonNode jsonNode = mapper.readTree(response);
				if (null != jsonNode.get(strKeys) && jsonNode.get(strKeys).isArray()) {
					JsonNode jsonKeys = jsonNode.get(strKeys);
					Map<String, String> localPublicKeys = Maps.newHashMap();
					for (int index = 0; index < jsonKeys.size(); index++) {
						String keyId = jsonKeys.get(index).get(strKeyId).asText();
						String publicKey = jsonKeys.get(index).get(strKeyField).get(0).asText();
						localPublicKeys.put(keyId, publicKey);
					}
					adPublicKeys = localPublicKeys;
				}
			}
		} catch (Exception e) {
			LogUtils.basicErrorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_AD_KEYS),
					INVALID_PAYLOAD_MSG);
		}
	}
	
	@Override
	public void sendEmailNotificationsForSchoolDistrict(String employeeCode,String firstName,String lastName,UserPrincipal principal) {
		List<UsersDTO> userDTOList = Lists.newArrayList();
		List<Object[]> userClients = Lists.newArrayList();
		List<Long> map = new ArrayList<>();
		try {
			String costcentercode = getCostCenterCodeByClientCode(principal.getClientCode());
			userClients = userCostCenterAssociationRepository.findByBranchUsersByCostCenterCode(costcentercode);
			if (CollectionUtils.isNotEmpty(userClients)) {
				Map<Long, List<CommonDTO>> privilegeMap = Maps.newHashMap();
				userClients.forEach(userClient -> {
					if (null != userClient && userClient.length>=2) {
						Long clientRole = ((BigInteger) userClient[2]).longValue();
						List<CommonDTO> privilegeDTOs = null;
						if (privilegeMap.containsKey(clientRole)) {
							privilegeDTOs = privilegeMap.get(clientRole);
						} else {
							privilegeDTOs = mappingService.getPrivileges(clientRole,
									UserEnum.ROLE);
							privilegeMap.put(clientRole, privilegeDTOs);
						}
						if (CollectionUtils.isNotEmpty(privilegeDTOs)) {
							privilegeDTOs.forEach(clientPrivilege -> {
								if ((EMP_MGMT_VIEW.equals(clientPrivilege.getCode())
										|| EMP_MGMT_ALL.equals(clientPrivilege.getCode())
										|| PAY_TS_INF_VIEW.equals(clientPrivilege.getCode())
										|| PAY_TS_INF_ALL.equals(clientPrivilege.getCode()))) {
									getUserIdsDTO(map, userClient);
								} 
							});
						}
					}
				});
			}
			buildBranchUserDTOs(map, userDTOList);
			sendEmailsForBranchUsers(employeeCode, firstName, lastName, principal, userDTOList);
		} catch (Exception e) {
			LogUtils.basicErrorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(),MethodNames.SEND_EMAIL_NOTIFICATIONS_FOR_SCHOOLDISTRICT),
					INVALID_PAYLOAD_MSG);
		}
	}
	
	private void getUserIdsDTO(List<Long> map, Object[] userClient) {
		map.add(((BigInteger) userClient[1]).longValue());
	}
	
	private void buildBranchUserDTOs(List<Long> map, List<UsersDTO> userDTOList) {
		if (null != map && map.size() > 0) {
			Iterable<Users> users = userRepository.findAllById(map);
			users.forEach(user -> {
				UsersDTO userDTO = userMapper.userToUserDTO(user);
				userDTOList.add(userDTO);
			});
		}
	}
	public String getCostCenterCodeByClientCode(String clientCode) {
		String response = null;
		Map<String, String> headers = boomiHelper.getHeaders();
		try {
			RestTemplate restTemplate = GenericUtils.getRestTemplate(120000);
			response = restClient.getForString(restTemplate, String.format(clientInfo, clientCode), new HashMap<>(),
					headers);
			if (null != response) {
				ObjectMapper res = GenericUtils.getObjectMapperInstance.get();
				JsonNode node = res.readTree(response);
				if (node.get(MethodNames.COST_CENTER_CODE) != null) {
					String costcentercode = node.get(MethodNames.COST_CENTER_CODE).textValue();
					return costcentercode;
				}
			}
		} catch (Exception e) {
			LogUtils.basicErrorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), "getClientNameByClientCode"),
					"Error occurred while getting client name by client code");
		}
		return null;
	}

	private void sendEmailsForBranchUsers(String employeeCode, String firstName, String lastName,
			UserPrincipal principal, List<UsersDTO> userDTOList) {
		if(CollectionUtils.isNotEmpty(userDTOList)) {
				Map<String, Set<String>> emailMap = buildEmailMap(userDTOList);
				Map<String, String> contextMap = buildContextMap(employeeCode,firstName,lastName,principal);
				commonUtilities.buildEventToPublish(principal, emailMap, contextMap,
						NotificationEventEnum.SCHOOL_DISTRICT_NOT_SUBMITTED,null);
			
		}
	}
	
	private Map<String, Set<String>> buildEmailMap(List<UsersDTO> usersList) {
		Map<String, Set<String>> emailMap = new HashMap<>();
		emailMap.put(NotifyTo.BRANCH.toString(), usersList.stream().map(UsersDTO::getEmail).collect(Collectors.toSet()));
		return emailMap;
	}

	private Map<String, String> buildContextMap(String employeeCode,String firstName,String lastName,UserPrincipal principal) {
		Map<String, String> contextMap = new HashMap<>();
		contextMap.put("last_name",lastName);
		contextMap.put("first_name", firstName);
		contextMap.put("employee_code", employeeCode);
		contextMap.put("client_code", principal.getClientCode());
		return contextMap;
	}
	
	private void validateJsonResponse(CustomPersonalDTO customPersonalDTO, String jsonResponse) {
		String bbsiEmpoyeeCode = fetchFieldByPath(jsonResponse, "personal.bbsi_assigned_employee_id");
		if(!customPersonalDTO.getEmployeeCode().equals(bbsiEmpoyeeCode)) {
			throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
		}
	}
	
	@Override
	public void reInitiateNewHire(UsersDTO userDTO, String token, String uiUrl,UserPrincipal userPrincipal) {
		try {
			UserExistDTO userExistDTO=checkExistUser(userPrincipal.getClientCode(), userDTO.getEmail(), token);
				if (userExistDTO.getIsFirstLogin()) {
					initiateEmail(userDTO, token, uiUrl, GenericConstants.USER_LOGIN_VM, true, userPrincipal);
				} else {
					initiateEmail(userDTO, token, uiUrl, GenericConstants.REINITIATE_DOCS_VM, false, userPrincipal);
				}

		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.REINITIATE_NEWHIRE), e,
					"Error occurred while reinitiating newhire");
		}
	}

	@Override
	public String authenticateUser(UserCredentials userCredentials, String token) {
		try {
			return getToken(userCredentials, token);
		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.AUTHENTICATE_USER), e,
					"Error occurred while authenticating user");
		}
		return null;
	}
	
	private String getToken(UserCredentials userCredentials, String token) {
		Map<String, String> headers = new HashMap<>();
		headers.put(GenericConstants.AUTH_TOKEN_HDR, token);
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString());
		
		String requestQuery = String.format("username=%s&password=%s&grant_type=%s",userCredentials.getUsername(),userCredentials.getPassword(),userCredentials.getGrant_type());
		RestTemplate restTemplate = GenericUtils.getRestTemplate(600000);
		String response = restClient.postForString(restTemplate, defaultTokenUrl, requestQuery,headers);
		if(StringUtils.isEmpty(response)) {
			throw new InvalidTokenException(new ErrorDetails(ErrorCodes.TOO1));
		}
		
		return response;
	}

	@Override
	public String getEmployeeEndDate(String clientCode, String employeeCode) {
		try {
			List<AccessTermination> accessTerminationList = accessTerminationRepository
					.getEmployeeEndDate(clientCode, employeeCode, GenericConstants.USERTYPE_EMPLOYEE);
			if (!CollectionUtils.isEmpty(accessTerminationList)
					&& null != accessTerminationList.get(0).getEndDate()) {
				return accessTerminationList.get(0).getEndDate().toString();
			} else {
				UserClients userClients = userClientRepository.findByClientCodeAndUserTypeAndEmployeeCode(clientCode,GenericConstants.USERTYPE_EMPLOYEE,employeeCode);
				if (null != userClients && null != userClients.getEndDate()) {
					return userClients.getEndDate().toLocalDate().toString();
				}
			}
		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.AUTHENTICATE_USER),
					e, "Error occurred  while get user end date");
		}
		return null;
	}

	@Override
	public void updateEmployeeEndDate(String clientCode, String empCode, String enddate) {
		try {
			List<AccessTermination> terminateEmployessNotProcess = accessTerminationRepository
					.checkEmployeeEndDate(clientCode, empCode, GenericConstants.USERTYPE_EMPLOYEE, false);
			if (!CollectionUtils.isEmpty(terminateEmployessNotProcess)) {
				if(StringUtils.isNotEmpty(enddate) && !"null".equalsIgnoreCase(enddate)) {
					accessTerminationRepository.updateEndDate(clientCode, empCode,GenericConstants.USERTYPE_EMPLOYEE, LocalDate.parse(enddate));
				} else {
					accessTerminationRepository.updateEndDateNull(clientCode, empCode,GenericConstants.USERTYPE_EMPLOYEE);
				}
				
			} else {
				updateProcessedEmpEndDate(clientCode, empCode, enddate);
				
			}
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_EMPLOYEE_ENDDATE),
					e, "Error occurred while Update user end date");
		}
	}

	private void updateProcessedEmpEndDate(String clientCode, String empCode, String enddate) {
		List<AccessTermination> terminateEmployessProcessed = accessTerminationRepository
				.checkEmployeeEndDate(clientCode, empCode, GenericConstants.USERTYPE_EMPLOYEE, true);
		UserClients userclients = userClientRepository.findByClientCodeAndUserTypeAndEmployeeCode(clientCode,GenericConstants.USERTYPE_EMPLOYEE,empCode);
		if(StringUtils.isNotEmpty(enddate) && !"null".equalsIgnoreCase(enddate)) {
			if (!CollectionUtils.isEmpty(terminateEmployessProcessed)) {
				accessTerminationRepository.updateEndDate(clientCode, empCode, GenericConstants.USERTYPE_EMPLOYEE, LocalDate.parse(enddate));
				userClientRepository.updateEmployeeEndDate(clientCode, empCode, GenericConstants.USERTYPE_EMPLOYEE,LocalDate.parse(enddate).atStartOfDay());
			}else if(null !=userclients) {
				userClientRepository.updateEmployeeEndDate(clientCode, empCode, GenericConstants.USERTYPE_EMPLOYEE,LocalDate.parse(enddate).atStartOfDay());
			}
		} else {
			if (!CollectionUtils.isEmpty(terminateEmployessProcessed)) {
				accessTerminationRepository.updateEndDateNull(clientCode, empCode, GenericConstants.USERTYPE_EMPLOYEE);
				userClientRepository.updateEmployeeEndDateNull(clientCode, empCode, GenericConstants.USERTYPE_EMPLOYEE);
			}else if(null !=userclients) {
				userClientRepository.updateEmployeeEndDateNull(clientCode, empCode, GenericConstants.USERTYPE_EMPLOYEE);
			}
		}
	}

	private boolean isMfaValidationRequired() {
		LocalDate pstDate = ZonedDateTime.ofInstant(LocalDateTime.now(ZoneId.of("Z")), ZoneOffset.UTC,
				ZoneId.of("America/Los_Angeles")).toLocalDate();
	    LocalDate mfaValidationDate= LocalDate.parse(mfaDate);
	    return (pstDate.compareTo(mfaValidationDate) > 0);
	}
	
	private void updateInvalidMfaAttempts(UserPrincipal principal) {
		userRepository.incrementMfaInvalidAttempts(principal.getUserId());
	}
	
	@Transactional
	@Override
	public String validateMfaCancelAttempts(UserPrincipal principal) {
		if (isMfaValidationRequired()) {
			if (principal.getMfaCancelAttempts() == 4) {
				return "AttemptsExceeded";
			}
			updateInvalidMfaAttempts(principal);
		}
		return "Success";
	}

	@Override
	public UserMfaDTO updateMfaEmail(UserMfaDTO userMfaDTO, UserPrincipal principal, String token) {
		
		String otherEmailError = "The Email Address should not be in" + " " + bbsiHeadEmails + " " + FORMAT;
		String exten[] = bbsiHeadEmails.split(",");
		Users users = null;

		if(StringUtils.isNotEmpty(userMfaDTO.getMfaEmail())) {
			userMfaDTO.setMfaEmail(encryptAndDecryptUtil.decrypt(userMfaDTO.getMfaEmail().trim()).toLowerCase());
		}
		if(StringUtils.isNotEmpty(userMfaDTO.getEmail())) {
			userMfaDTO.setEmail(encryptAndDecryptUtil.decrypt(userMfaDTO.getEmail().trim()).toLowerCase());
		}

		String userType = principal.getUserType();
		// Verify Employee
		if(StringUtils.isNotEmpty(userMfaDTO.getEmployeeCode())) {
			integrationBusinessService.verifyEmployee(userMfaDTO.getEmployeeCode(), token);
		}

		if (!userMfaDTO.getIsConfactInfoUpdate()) {
			if(StringUtils.isEmpty(userMfaDTO.getMfaEmail())) {
				userMfaDTO.setMfaEmail(null);
			}
			if (StringUtils.isNotEmpty(userMfaDTO.getEmail())) {
				if (!userMfaDTO.getEmail().equalsIgnoreCase(principal.getEmail())) {
					throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
				}
			}
			users = userRepository.findByEmail(userMfaDTO.getEmail());
			if(StringUtils.isNotEmpty(userMfaDTO.getMobile()) && CollectionUtils.isNotEmpty(users.getClients())) {
				for(UserClients userClient : users.getClients()) {
					if(GenericConstants.USERTYPE_EMPLOYEE.equals(userClient.getUserType()) 
							&& principal.getClientMap().containsKey(userClient.getClientCode())) {
						updateContactInfoForEmployee(userMfaDTO.getMobile(), userClient.getClientCode(), userClient.getEmployeeCode(), principal, token);
					}
				}
			}
			mfaAudit(userMfaDTO, principal);
		} else {
			userType = GenericConstants.USERTYPE_EMPLOYEE;
			UserClients userClients = userClientRepository.findByClientCodeAndEmployeeCode(userMfaDTO.getClientCode(), userMfaDTO.getEmployeeCode());
			if(userClients != null) {
				users = userClients.getUser();
			}
			if(users != null) {
				userMfaDTO.setId(users.getId());
				userClients.setMobile(userMfaDTO.getMobile());
				if(StringUtils.isNotEmpty(users.getMobile()) && StringUtils.isEmpty(userMfaDTO.getMobile()) && users.getClients().size() > 1) {
					userMfaDTO.setMobile(users.getMobile());
				}
			} else {
				validateUniqueMfaEmail("",userMfaDTO.getMfaEmail(), null);
				return userMfaDTO;
			}
		}
		validateMfaEmail(userType, userMfaDTO, otherEmailError, exten);

		if(users != null) {
			users.setMobile(userMfaDTO.getMobile());
			users.setMfaEmail(userMfaDTO.getMfaEmail());
			userRepository.save(users);
		}

		if(userMfaDTO.getMfaEmail() != null && CollectionUtils.isNotEmpty(users.getClients())) {
			for(UserClients userClient : users.getClients()) {
				if(StringUtils.isNotEmpty(userClient.getEmployeeCode())) {
					invokeEmpEligibility(userClient.getClientCode(), userClient.getEmployeeCode(), userMfaDTO.getMfaEmail(), token);
				}
			}
		}
		
		return userMfaDTO;
	}

	private void mfaAudit(UserMfaDTO userMfaDTO, UserPrincipal principal) {
		try {
			Map<String,String> requestMap = new HashMap<>();
			requestMap.put(MFA_EMAIL, userMfaDTO.getMfaEmail());
			requestMap.put(PHONE_NUMBER, userMfaDTO.getMobile());
			auditDetailsUtil.buildAuditDetailsAndSave(BoomiEnum.CONTACT_INFO.toString(), BoomiEnum.CONTACT_INFO.toString(),
					userMfaDTO.getEmail(), BoomiEnum.CONTACT_INFO.toString(), null, 
					principal, null, mapper.writeValueAsString(requestMap), Enums.AuditDetailsEventEnum.INSERT.toString(), null);
		} catch(Exception e) {
			LogUtils.basicErrorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(),
					"mfaAudit"), e.getMessage());
		}
	}
	
	private String getEmployeeMobileNumber(String clientCode, String employeeCode) {
		String mobileNumber = null;
		String infoUri = boomiHelper.getUrl(BoomiEnum.EMPLOYEE_INFO);
		infoUri = String.format(infoUri, clientCode, employeeCode, "");
		RestTemplate restTemplate = GenericUtils.getRestTemplate(120000);
		String jsonResponse = restClient.getForString(restTemplate, infoUri, Collections.emptyMap(),
				boomiHelper.getHeaders());
		try {
			JsonElement jsonElement = JsonParser.parseString(jsonResponse);
			if (jsonElement.getAsJsonObject().get(CONTACT_INFORMATION) != null) {
				JsonElement contactInfo = jsonElement.getAsJsonObject().get(CONTACT_INFORMATION);
				if (contactInfo != null) {
					mobileNumber = contactInfo.getAsJsonObject().get(MOBILE_PHONE_NUMBER).getAsString();
				}

			}
		} catch (Exception e) {
			LogUtils.basicErrorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), "getEmployeeMobileNumber"), e.getMessage());
		}
		return mobileNumber;

	}

	private void updateContactInfoForEmployee(String mobile, String clientCode, String employeeCode, UserPrincipal principal, String token) {
		String infoUri = boomiHelper.getUrl(BoomiEnum.EMPLOYEE_INFO);
		infoUri = String.format(infoUri, clientCode, employeeCode, "");
		RestTemplate restTemplate = GenericUtils.getRestTemplate(120000);
		String jsonResponse = restClient.getForString(restTemplate, infoUri, Collections.emptyMap(),
				boomiHelper.getHeaders());
		try {
			JsonNode jsonNode = mapper.readTree(jsonResponse);
			if (null != jsonNode.get("contact_information")) {
				String contactInfo = jsonNode.get("contact_information").toString();
				Map<String,String> requestMap = new HashMap<>();
				requestMap.put("$.client_code", clientCode);
				requestMap.put("$.bbsi_assigned_employee_id", employeeCode);
				contactInfo = JsonUtils.insertJsonField(contactInfo, requestMap);
				requestMap.clear();
				requestMap.put("$.mobile_phone_number", mobile);
				String contactNewInfo = JsonUtils.insertJsonField(contactInfo, requestMap);
				if(StringUtils.isNotEmpty(contactNewInfo)) {
				
					String contactInfoUrl = boomiHelper.getUrl(BoomiEnum.CONTACT_INFO);
					
					restClient.putForObjectString(contactInfoUrl, contactNewInfo, boomiHelper.getHeaders());
					auditDetailsUtil.buildAuditDetailsAndSave(BoomiEnum.CONTACT_INFO.toString(), BoomiEnum.CONTACT_INFO.toString(),
							clientCode+"::"+employeeCode , BoomiEnum.EMPLOYEE_INFO.toString(), BoomiEnum.CONTACT_INFO.toString(), 
							principal, contactInfo, contactNewInfo, Enums.AuditDetailsEventEnum.UPDATE.toString(), null);
				}
			} else {
				Map<String,String> requestMap = new HashMap<>();
				requestMap.put("client_code", clientCode);
				requestMap.put("bbsi_assigned_employee_id", employeeCode);
				requestMap.put("mobile_phone_number", mobile);
				String contactInfoUrl = boomiHelper.getUrl(BoomiEnum.CONTACT_INFO);
				
				restClient.putForObjectString(contactInfoUrl, mapper.writeValueAsString(requestMap), boomiHelper.getHeaders());
				auditDetailsUtil.buildAuditDetailsAndSave(BoomiEnum.CONTACT_INFO.toString(), BoomiEnum.CONTACT_INFO.toString(),
						clientCode+"::"+employeeCode , BoomiEnum.EMPLOYEE_INFO.toString(), BoomiEnum.CONTACT_INFO.toString(), 
						principal, null, mapper.writeValueAsString(requestMap), Enums.AuditDetailsEventEnum.INSERT.toString(), null);
			}
			
		} catch (BbsiException e) {
			if(null != e.getErrorResponse()) {
				e.getErrorResponse().setErrorDesc(e.getErrorResponse().getErrorDesc()+" Please contact your myBBSI administrator.");
				e.getErrorResponse().setErrorMessage(e.getErrorResponse().getErrorMessage()+" Please contact your myBBSI administrator.");
			}
			throw e;
		} catch (Exception e) {
			LogUtils.basicErrorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(),
					"updateContactInfoForEmployee"), e.getMessage());
		}
	}

	private void invokeEmpEligibility(String clientCode, String employeeCode, String mfaEmail, String token) {
		Map<String, String> headers = new HashMap<>();
		headers.put(GenericConstants.AUTH_TOKEN_HDR, token);
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			String url = String.format(getEmpEligibilityUrl, clientCode,
					employeeCode);
			Map<String, String> payloadMap = Maps.newHashMap();
			payloadMap.put("mfaEmail", mfaEmail);
			payloadMap.put("clientCode", clientCode);
			
				LogUtils.basicDebugLog.accept(String.format("Started emp eligibility call for  Employee code, email  :%s, %s", employeeCode, mfaEmail));
				try {
					String response = restClient.postForString(url, payloadMap, headers);
					LogUtils.basicDebugLog.accept(String.format("Completed emp eligibility response: %s", response));
				} catch (Exception e) {
					LogUtils.basicErrorLog.accept(
							basicMethodInfo.apply(getClass().getCanonicalName(), "invokeEmpEligibility"),
							String.format("Error occurred in emp eligibility call for employee %s", ExceptionUtils.getRootCauseMessage(e)));
				}
	}
	
	@Override
	public List<BulkUploadMailDTO> validateMfaEmails(List<BulkUploadMailDTO> emailList, UserPrincipal userPrincipal) {
		for(BulkUploadMailDTO eMailDTO : emailList) {
			try {
				validateUniqueMfaEmail(eMailDTO.getPersonalEmail(), eMailDTO.getMfaEmail(), null);
				validateEmailWithMfaEmail(eMailDTO.getPersonalEmail(), userPrincipal.getClientCode(), null, GenericConstants.USERTYPE_EMPLOYEE);
			} catch (ValidationException ex) {
				if(ex.getErrorResponse() != null) {
					eMailDTO.setErrorMessage(ex.getErrorResponse().getErrorMessage());
				} else {
					eMailDTO.setErrorMessage(ErrorCodes.UR0010.getDescription());
				}
			}
		}
		return emailList;
	}

	@Override
	public Map<String, Set<String>> getMfaDetails(String uid) {

		uid = encryptAndDecryptUtil.decrypt(uid);
		validateToken(uid);
		String userName = jwtTokenUtil.getUsernameFromToken(uid);
		Users users = userRepository.findByEmail(userName);

		if(users != null) {
			return getPersonalInfoByClientCodeAndEmployeeCode("", "", null, userName, users.getId());
		}
		return Maps.newHashMap();
	}

	private void validateToken(String uid) {
		if (jwtTokenUtil.isTokenExpired(uid)) {
			LogUtils.basicErrorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD), MethodNames.INVALID_TOKEN_ERROR);
			throw new ValidationException(new ErrorDetails(ErrorCodes.USR003));
		}
	}
}
