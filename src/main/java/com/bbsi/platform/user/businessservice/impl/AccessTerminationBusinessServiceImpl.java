package com.bbsi.platform.user.businessservice.impl;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.enums.BoomiEnum;
import com.bbsi.platform.common.enums.Enums.AuditDetailsSourceEntityEnum;
import com.bbsi.platform.common.enums.Enums.UserEnum;
import com.bbsi.platform.common.generic.AuditDetailsUtil;
import com.bbsi.platform.common.generic.BoomiHelper;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.GenericUtils;
import com.bbsi.platform.common.generic.LogUtils;
import com.bbsi.platform.common.generic.RestClient;
import com.bbsi.platform.common.user.dto.AccessTerminationDTO;
import com.bbsi.platform.common.user.dto.RbacDTO;
import com.bbsi.platform.user.businessservice.intf.AccessTerminationBusinessService;
import com.bbsi.platform.user.mapper.AccessTerminationMapper;
import com.bbsi.platform.user.model.AccessTermination;
import com.bbsi.platform.user.model.RbacEntity;
import com.bbsi.platform.user.model.UserClients;
import com.bbsi.platform.user.repository.AccessTerminationRepository;
import com.bbsi.platform.user.repository.ClientMasterRepository;
import com.bbsi.platform.user.repository.ClientRoleRepository;
import com.bbsi.platform.user.repository.RbacRepository;
import com.bbsi.platform.user.repository.UserClientsRepository;
import com.bbsi.platform.user.utils.MethodNames;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Class for provides the behavior published by
 * {@link AccessTerminationBusinessService}
 * 
 * @author vpula
 *
 */
@Logging
@Service
public class AccessTerminationBusinessServiceImpl implements AccessTerminationBusinessService {

	@Autowired
	private AccessTerminationRepository accessTerminationRepository;

	@Autowired
	private AccessTerminationMapper accessTerminationMapper;

	@Autowired
	private UserClientsRepository userClientRepository;
	
	@Autowired
	private ClientMasterRepository clientMasterRepository;

	@Autowired
	private RbacRepository roleRepository;

	@Value("${get.integration.service.url}")
	private String getIntegrationUrl;

	@Value("${get.service.payrate.url}")
	private String getEmployeeIntegrationUrl;
	
	@Autowired
	private BoomiHelper helper;

	@Autowired
	private RestClient restClient;
	
	@Autowired
	private AuditDetailsUtil auditDetailsUtil;

	@Autowired
	private ClientRoleRepository clientRoleRepository;
	
	private static final String STATUS = "status";
	
	private static final String END_DATE = "endDate";
	
	private static final String DATE_FORMATTE = "MM-dd-yyyy";

	/*
	 * Method for saving access termination.
	 */
	@Override
	public AccessTerminationDTO saveAccessTermination(AccessTerminationDTO accessTerminationDTO) {
		List<AccessTermination> accessTerminationList = new ArrayList<>();

		try {

			accessTerminationList = accessTerminationMapper
					.accessTerminationToAccessTerminationDto(accessTerminationDTO);

			accessTerminationRepository.saveAll(accessTerminationList);

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_ACCESS_TERMINATION), e,
					"Error occured while saving the access termination info ");
		}

		return accessTerminationDTO;
	}

	@Override
	public AccessTerminationDTO updateAccessTermination(AccessTerminationDTO accessTerminationDTO) {
		List<AccessTermination> accessTerminationList = null;
		try {
			// if employeeCode is null, then update should happen for the client.
			if (null == accessTerminationDTO.getEmployeeCode()) {
				accessTerminationList = accessTerminationRepository
						.findByClientCode(accessTerminationDTO.getClientCode());

				if (!CollectionUtils.isEmpty(accessTerminationList)) {
					setAccessTerminationEndDateAndLimited(accessTerminationDTO, accessTerminationList);
				}
			} else {
				// if employeeCode is present, then update should happen for the employee.
				accessTerminationList = accessTerminationRepository.findByClientCodeAndEmployeeCode(
						accessTerminationDTO.getClientCode(), accessTerminationDTO.getEmployeeCode());
				if (CollectionUtils.isEmpty(accessTerminationList)) {
					accessTerminationList = new ArrayList<>();
					AccessTermination accessTermination = new AccessTermination();
					accessTermination.setLimited(accessTerminationDTO.isLimited());
					accessTermination.setEndDate(accessTerminationDTO.getEmployeeEndDate());
					accessTermination.setClientCode(accessTerminationDTO.getClientCode());
					accessTermination.setEmployeeCode(accessTerminationDTO.getEmployeeCode());
					accessTermination.setStartDate(accessTerminationDTO.getStartDate());
					accessTermination.setUserType(GenericConstants.USERTYPE_EMPLOYEE);
					accessTerminationList.add(accessTermination);
				} else {
					accessTerminationList.forEach(accessTermination -> {
						accessTermination.setEndDate(accessTerminationDTO.getEmployeeEndDate());
						accessTermination.setStartDate(accessTerminationDTO.getStartDate());
					});
				}
			}
			accessTerminationRepository.saveAll(accessTerminationList);

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_ACCESS_TERMINATION), e,
					"Error occured while updating the access termination info ");
		}finally {
			GenericUtils.clearForGC(accessTerminationList);
		}

		return accessTerminationDTO;
	}

	private void setAccessTerminationEndDateAndLimited(AccessTerminationDTO accessTerminationDTO,
			List<AccessTermination> accessTerminationList) {
		for (AccessTermination accessTermination : accessTerminationList) {

			if (GenericConstants.USERTYPE_CLIENT.equalsIgnoreCase(accessTermination.getUserType())) {
				accessTermination.setEndDate(accessTerminationDTO.getClientEndDate());
				accessTermination.setLimited(accessTerminationDTO.isLimited());
			}

			if (GenericConstants.USERTYPE_EMPLOYEE.equalsIgnoreCase(accessTermination.getUserType())) {
				accessTermination.setEndDate(accessTerminationDTO.getEmployeeEndDate());
				accessTermination.setLimited(accessTerminationDTO.isLimited());
			}

			if (GenericConstants.USERTYPE_EXTERNAL.equalsIgnoreCase(accessTermination.getUserType())) {
				accessTermination.setEndDate(accessTerminationDTO.getExternalEndDate());
				accessTermination.setLimited(accessTerminationDTO.isLimited());
			}

		}
	}

	@Transactional
	@Override
	public void updateStatusByEndDate() {
		List<AccessTermination> accessTerminationList = null;
		List<AccessTermination> teminatedEmployeeList = null;
		Map<String, LocalDate> dateMap = null;
		try {
			accessTerminationList = accessTerminationRepository.findByEndDate(LocalDate.now().minusDays(1));
			teminatedEmployeeList = accessTerminationRepository.findByStartDate(LocalDate.now());
			/*
			 * *fetch user clients by client code and user types. * update their status by
			 * end date and start date.
			 *
			 */
			Set<String> terminatedClients = updatePortalAccess(accessTerminationList);
			/*
			 * upated terminated role to the employee.
			 */
			dateMap = updateTerminateRole(teminatedEmployeeList);

			// Terminate employees who has been terminated in Hrp End (Not through portal
			// UI)
			terminateEmployeeBasedonUserAccess(dateMap, terminatedClients);
			//assign terminated role to clients
			updateTerminateRoleToClients(teminatedEmployeeList);
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_ACCESS_TERMINATION), e,
					"Error occured while updating the access termination info ");
		}finally {
			GenericUtils.clearForGC(accessTerminationList,teminatedEmployeeList,dateMap);
		}
	}

	/* Helper method for updating is active to db */
	private Set<String> updatePortalAccess(List<AccessTermination> accessTerminationList) {
		Set<String> clientCodes = Sets.newHashSet();
		List<AccessTermination> batchUpdateList = null;
		try {
			// In two scenarios, we insert data into AccessTermination table. From Branch
			// administration and employee container.
			// In Branch administration flow, whole client and all its employees , external
			// users will be terminated. So we don't have employee code gets inserted into
			// DB.
			// From employee container, Terminatation will be for a particular employee. In
			// this case, we instert employeeCode in DB
			batchUpdateList = accessTerminationList.stream()
					.filter(accessTermination -> StringUtils.isEmpty(accessTermination.getEmployeeCode()))
					.collect(Collectors.toList());
			
			if (!CollectionUtils.isEmpty(batchUpdateList)) {
				clientCodes = updateClientAccess(batchUpdateList);
			}
			if (!CollectionUtils.isEmpty(accessTerminationList)) {
				accessTerminationList.forEach(accessTermination -> accessTermination.setEndDateProcessed(Boolean.TRUE));
				accessTerminationRepository.saveAll(accessTerminationList);
			}
		} catch (Exception ex) {
			LogUtils.errorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "updateUserClients"),
					ExceptionUtils.getRootCauseMessage(ex), ex);
		}finally {
			GenericUtils.clearForGC(batchUpdateList);
		}
		return clientCodes;
	}

	private void terminateEmployeeBasedonUserAccess(Map<String, LocalDate> dateMap, Set<String> terminatedClients) {

		String statusField = STATUS;
		String endDateField = END_DATE;
		RbacEntity role = roleRepository.findByCodeAndType(MethodNames.TERMINATED_EMPLOYEE_ROLE_CODE,
				UserEnum.ROLE.toString());
		
		RbacEntity empRole = roleRepository.findByCodeAndType(GenericConstants.USERTYPE_EMPLOYEE,
				UserEnum.ROLE.toString());

		long terminatedRoleId = role.getMappingId();
		List<Object[]> userClients = userClientRepository.findAllEmployeesWithRoles();

		Map<String, List<Object[]>> userClientMap = userClients.parallelStream()
				.collect(Collectors.groupingBy(uc -> String.valueOf(uc[1])));

		userClientMap.forEach((clientCode, userClientList) -> {

			Set<String> terminatedEmployeeList = new LinkedHashSet<>();
			Set<String> activeEmployeeList = new LinkedHashSet<>();
        try {
			String uri = helper.getUrl(BoomiEnum.CLIENT_EMPLOYEES_FILTER_UQ);
			uri = String.format(uri, clientCode,"EXTWITHSTATUS");
			String response = restClient.getForString(uri, Collections.emptyMap(), helper.getHeaders());
			
			JsonElement jsonElement = JsonParser.parseString(response);
			JsonArray jsonArray = jsonElement.getAsJsonArray();

			Map<String,String> statusDateMap = buildActiveAndTerminatedEmpList(terminatedClients, clientCode, terminatedEmployeeList, activeEmployeeList,
					jsonArray);
			userClientList.forEach(userClient -> {
				if (terminatedEmployeeList.contains(String.valueOf(userClient[2])) && !(isTerminated.test(userClient[5], terminatedRoleId))
						&& null != userClient[3] && Boolean.TRUE.equals(userClient[3])) {
					try {
						terminateEmployeeBasedonStatus(dateMap, statusDateMap, String.valueOf(userClient[2]), clientCode, role, userClient);
					} catch (Exception e) {
						LogUtils.errorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "removeIfPresentDoc"),
								"Exception while updating the terminated status for userClientId %s and error :: %s "+ userClient[0], e);
					}
				} else if (activeEmployeeList.contains(String.valueOf(userClient[2])) && ((isTerminated.test(userClient[5], terminatedRoleId))
						|| (null != userClient[3] && Boolean.FALSE.equals(userClient[3])))) {
					clientRoleRepository.updateRole(empRole.getMappingId(), ((BigInteger)userClient[0]).longValue());
					Map<String, Object> oldMap = Maps.newHashMap();
					oldMap.put(UserEnum.ROLE.toString(), MethodNames.TERMINATED_EMPLOYEE_ROLE_CODE);
					oldMap.put(statusField, userClient[3]);
					oldMap.put(endDateField, userClient[4]);
					String oldDataString = GenericUtils.gsonInstance.get().toJson(oldMap);
					UserClients dbUserClient = userClientRepository.findById(((BigInteger)userClient[0]).longValue()).orElse(null);
					setDbUserClient(dbUserClient, oldDataString);
				}
			});
        } catch (Exception e) {
			LogUtils.basicErrorLog.accept(
					"AccessTerminationBusinessServiceImpl.terminateEmployeeBasedonUserAccess() :: Error occurred while fetching employee's for client "
							+ clientCode, e.getMessage());
		}
		});
	}
	private void updateTerminateRoleToClients(List<AccessTermination> teminatedEmployeeList) {

		try {
			if(!CollectionUtils.isEmpty(teminatedEmployeeList)) {
				Map<String, LocalDate> dateMap = Maps.newHashMap();
	        	List<AccessTermination> clientTerminationList = teminatedEmployeeList.stream()
						.filter(accessTermination -> (null != accessTermination.getUserType() && (GenericConstants.USERTYPE_CLIENT.equalsIgnoreCase(accessTermination.getUserType()) || GenericConstants.USERTYPE_EXTERNAL.equalsIgnoreCase(accessTermination.getUserType()))))
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(clientTerminationList)) {
					for(AccessTermination access : clientTerminationList) {
						dateMap.put(access.getClientCode()+"-"+access.getUserType(), access.getEndDate());
					}
				}
						
				if(!CollectionUtils.isEmpty(dateMap)) {
					RbacEntity role = roleRepository.findByCodeAndType(GenericConstants.TERMINATED_CLIENT_ROLE_CODE,UserEnum.ROLE.toString());
					long terminatedRoleId = role.getMappingId();
					List<Object[]> userClients = userClientRepository.findAllClientUsersWithRoles();
					if(!CollectionUtils.isEmpty(userClients)) {
						userClients.forEach(userClient -> {
							assignTerminateRoleToClientAndExternalUsers(dateMap, terminatedRoleId, userClient); 
						});
					}
				}
			}
			
        } catch (Exception e) {
			LogUtils.basicErrorLog.accept(
					"AccessTerminationBusinessServiceImpl.updateTerminateRoleToClients() :: Error occurred while fetching  client and external users ", e.getMessage());
		}
	}

	private void assignTerminateRoleToClientAndExternalUsers(Map<String, LocalDate> dateMap, long terminatedRoleId,
			Object[] userClient) {
		if (dateMap.containsKey(String.valueOf(userClient[1]+"-"+userClient[5])) && !(isTerminated.test(userClient[4], terminatedRoleId)) && null != userClient[2] && Boolean.TRUE.equals(userClient[2])) {
			try {
				LocalDate endDate = null;
				if(dateMap.containsKey(userClient[1]+"-"+userClient[5])	&& null != dateMap.get(userClient[1]+"-"+userClient[5])) {
					endDate = dateMap.get(userClient[1]+"-"+userClient[5]).atStartOfDay().toLocalDate();
				}
				UserClients dbUserClient = userClientRepository.findById(((BigInteger)userClient[0]).longValue()).orElse(null);
				if(null != dbUserClient) {
					Map<String, Object> oldMap = Maps.newHashMap();
					oldMap.put(UserEnum.ROLE.toString(), userClient[6]);
					String oldDataString = GenericUtils.gsonInstance.get().toJson(oldMap);
					clientRoleRepository.updateRole(terminatedRoleId, ((BigInteger)userClient[0]).longValue());
					if(null == dbUserClient.getEndDate()) {
						dbUserClient.setEndDate(endDate.atStartOfDay());
						userClientRepository.save(dbUserClient);
					}
					Map<String, Object> newMap = Maps.newHashMap();
					newMap.put(UserEnum.ROLE.toString(), GenericConstants.TERMINATED_CLIENT_ROLE_CODE);
					String newDataString = GenericUtils.gsonInstance.get().toJson(newMap);
					buildAuditUserChanges(dbUserClient, oldDataString, newDataString);
				}
			} catch (Exception e) {
				LogUtils.errorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "assignTerminateRoleToClientAndExternalUsers"),
						"Exception while updating the terminated status for userClientId %s and error :: %s "+ userClient[0], e);
			}
		}
	}

	
	private void setDbUserClient(UserClients dbUserClient, String oldDataString) {
		if(null != dbUserClient) {
			dbUserClient.setIsActive(true);
			dbUserClient.setEndDate(null);
			Map<String, Object> newMap = Maps.newHashMap();
			newMap.put(UserEnum.ROLE.toString(), GenericConstants.USERTYPE_EMPLOYEE);
			newMap.put("STATUS", dbUserClient.getIsActive());
			newMap.put("END_DATE", dbUserClient.getEndDate());
			String newDataString = GenericUtils.gsonInstance.get().toJson(newMap);
			buildAuditUserChanges(dbUserClient, oldDataString, newDataString);
			userClientRepository.save(dbUserClient);
		}
	}

	private void buildAuditUserChanges(UserClients userClient, String oldDataString, String newDataString) {
		if(null != userClient.getUser()) {
			auditUserChanges(userClient, oldDataString, newDataString);
		}
	}

	private Map<String,String> buildActiveAndTerminatedEmpList(Set<String> terminatedClients, String clientCode,
			Set<String> terminatedEmployeeList, Set<String> activeEmployeeList, JsonArray jsonArray) {
		Map<String,String> statusDateMap = Maps.newHashMap();
		String statusDate = "status_date";
		for (int i = 0; i < jsonArray.size(); i++) {
			String status = (null != jsonArray.get(i).getAsJsonObject().get(STATUS)) ? jsonArray.get(i).getAsJsonObject().get(STATUS).getAsString():"";
			String employeeCode = jsonArray.get(i).getAsJsonObject().get("employee_code").getAsString();
			String strDate = (null != jsonArray.get(i).getAsJsonObject().get(statusDate)) ? jsonArray.get(i).getAsJsonObject().get(statusDate).getAsString():null;
			if ("T".equals(status) || terminatedClients.contains(clientCode)) {
				terminatedEmployeeList.add(employeeCode);
			} else if ("A".equals(status)) {
				activeEmployeeList.add(employeeCode);
			}
			statusDateMap.put(employeeCode, strDate);
		}
		return statusDateMap;
	}

	/**
	 * @param employeeCode
	 * @param clientCode
	 * @param role
	 * @param userClient
	 * 
	 *                     Call EmployementInfo and if already terminated in Hrp
	 *                     update the status here in portal
	 */
	private void terminateEmployeeBasedonStatus(Map<String, LocalDate> dateMap, Map<String,String> statusDateMap, String employeeCode, String clientCode, 
			RbacEntity role, Object[] userClientArray) {
		
		String statusField = STATUS;
		String endDateField = END_DATE;
		LocalDate endDate = null;
		if(dateMap.containsKey(clientCode+"-"+employeeCode)
				&& null != dateMap.get(clientCode+"-"+employeeCode)) {
			endDate = dateMap.get(clientCode+"-"+employeeCode).atStartOfDay().toLocalDate();
		}
		
		String statusEffectiveDate = statusDateMap.get(employeeCode);

		if (StringUtils.isNotEmpty(statusEffectiveDate)) {
			LocalDate effectiveDate = null;
			try {
			 effectiveDate = LocalDate.parse(statusEffectiveDate);
			}catch(DateTimeParseException e) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTE);
				effectiveDate = LocalDate.parse(statusEffectiveDate, formatter);
			}
			// Terminate if the effective date is previous date
			if (effectiveDate.isBefore(LocalDate.now())) {
				if(null == endDate) {
					endDate = effectiveDate.plusMonths(18);
				}
				UserClients dbUserClient = userClientRepository.findById(((BigInteger)userClientArray[0]).longValue()).orElse(null);
				if(null != dbUserClient) {
					Map<String, Object> oldMap = Maps.newHashMap();
					oldMap.put(UserEnum.ROLE.toString(), GenericConstants.USERTYPE_EMPLOYEE);
					String oldDataString = GenericUtils.gsonInstance.get().toJson(oldMap);
					clientRoleRepository.updateRole(role.getMappingId(), ((BigInteger)userClientArray[0]).longValue());
					if(null == dbUserClient.getEndDate()) {
						dbUserClient.setEndDate(endDate.atStartOfDay());
						userClientRepository.save(dbUserClient);
					}
					Map<String, Object> newMap = Maps.newHashMap();
					newMap.put(UserEnum.ROLE.toString(), MethodNames.TERMINATED_EMPLOYEE_ROLE_CODE);
					String newDataString = GenericUtils.gsonInstance.get().toJson(newMap);
					buildAuditUserChanges(dbUserClient, oldDataString, newDataString);
					List<UserClients> clientsList = userClientRepository.findByUser_Id(dbUserClient.getUser().getId());
					setClientIsActiveAndEndDate(clientCode, statusField, endDateField, effectiveDate, clientsList);
				}
			}
		}
	}

	private void setClientIsActiveAndEndDate(String clientCode, String statusField, String endDateField,
			LocalDate effectiveDate, List<UserClients> clientsList) {
		if(!CollectionUtils.isEmpty(clientsList)) {
			for (UserClients client : clientsList) {
				if(client.getClientCode().equals(clientCode) 
						&& StringUtils.isEmpty(client.getEmployeeCode())) {
					Map<String, Object> oldMap = Maps.newHashMap();
					oldMap.put(statusField, client.getIsActive());
					oldMap.put(endDateField, client.getEndDate());
					String oldAuditString = GenericUtils.gsonInstance.get().toJson(oldMap);
					client.setIsActive(Boolean.FALSE);
					client.setEndDate(effectiveDate.atStartOfDay());
					userClientRepository.save(client);
					Map<String, Object> newMap = Maps.newHashMap();
					newMap.put(statusField, client.getIsActive());
					newMap.put(endDateField, client.getEndDate());
					String newAuditString = GenericUtils.gsonInstance.get().toJson(newMap);
					if(null != client.getUser()) {
						auditUserChanges(client, oldAuditString, newAuditString);
					}
				}
			}
		}
	}
	
	private void auditUserChanges(UserClients userClient, String oldDataString, String newDataString) {
		if(null != userClient.getUser()) {
			UserPrincipal userPrincipal = new UserPrincipal(GenericConstants.INTEGRATION_ADMIN, "******", Lists.newArrayList());
			userPrincipal.setClientCode(userClient.getClientCode());
			if(null == userClient.getClient()) {
				userClient.setClient(clientMasterRepository.findByClientCode(userClient.getClientCode()));
			}
			userPrincipal.setClientName(userClient.getClient().getClientName());
			userPrincipal.setCostCenterCode(userClient.getClient().getCostCenterCode());
			userPrincipal.setBbsiBranch(userClient.getClient().getCostCenterDescription());
			userPrincipal.setUserType(userClient.getUserType());
			userPrincipal.setEmail(GenericConstants.INTEGRATION_ADMIN);
			userPrincipal.setFirstName(userClient.getFirstName());
			userPrincipal.setLastName(userClient.getLastName());
			
			RbacDTO entity = new RbacDTO();
			entity.setCreatedBy(userClient.getCreatedBy());
			entity.setCreatedOn(userClient.getCreatedOn());
			entity.setModifiedOn(LocalDateTime.now());
			entity.setModifiedBy(GenericConstants.INTEGRATION_ADMIN);
			auditDetailsUtil.buildAuditDetailsAndSave(AuditDetailsSourceEntityEnum.USER.toString(), "email",
					userClient.getUser().getEmail(), AuditDetailsSourceEntityEnum.USER.toString(), null, userPrincipal, oldDataString, newDataString,
                com.bbsi.platform.common.enums.Enums.AuditDetailsEventEnum.UPDATE.toString(), entity);
		}
	}

	/**
	 * Check for employee already terminated
	 */
	BiPredicate<Object, Long> isTerminated = (roleId, id) ->
		(null != roleId  && ((BigInteger)roleId).longValue() == id);

	/**
	 * This will update isActive to false, foe those employees who are terminated.
	 *
	 * @param employeeTerminationList
	 */
	private void updateEmployeeAccess(List<AccessTermination> employeeTerminationList) {
		try {
			if (!CollectionUtils.isEmpty(employeeTerminationList)) {
				List<UserClients> userClientsList = Lists.newArrayList();
				for(AccessTermination access : employeeTerminationList) {
					UserClients userClients = userClientRepository.findByClientCodeAndEmployeeCode(access.getClientCode(), access.getEmployeeCode());
					if(null != userClients) {
						String status = STATUS;
						String endDate = END_DATE;
						Map<String, Object> oldMap = Maps.newHashMap();
						oldMap.put(status, userClients.getIsActive());
						oldMap.put(endDate, userClients.getEndDate());
						String oldDataString = GenericUtils.gsonInstance.get().toJson(oldMap);
						userClients.setIsActive(Boolean.TRUE);
						if (null != access.getEndDate()) {
							userClients.setEndDate(access.getEndDate().atStartOfDay());
						}
						userClientsList.add(userClients);
						Map<String, Object> newMap = Maps.newHashMap();
						newMap.put(status, userClients.getIsActive());
						newMap.put(endDate, userClients.getEndDate());
						String newDataString = GenericUtils.gsonInstance.get().toJson(newMap);
						if(null != userClients.getUser()) {
							auditUserChanges(userClients, oldDataString, newDataString);
						}
					}
				}
				// update to db.
				this.userClientRepository.saveAll(userClientsList);
			}
		} catch (Exception ex) {
			LogUtils.errorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "updateEmployeeAccess"),
					ExceptionUtils.getRootCauseMessage(ex), ex);
		}
	}

	/**
	 * This will update the isActive flag to false, for userTypes employee, external
	 * and client.
	 *
	 * @param batchUpdateList
	 */
	private Set<String> updateClientAccess(List<AccessTermination> batchUpdateList) {
		Set<String> clientCodes = Sets.newHashSet();
		try {
			if (!CollectionUtils.isEmpty(batchUpdateList)) {
				/* get client codes and user types */
				String status = STATUS;
				String endDateField = END_DATE;
				for(AccessTermination access : batchUpdateList) {
					LocalDate endDate = access.getEndDate();
					List<UserClients> userClientsList = this.userClientRepository.findByClientCodeAndUserType(access.getClientCode(),
							access.getUserType());
					clientCodes.add(access.getClientCode());
					if (!CollectionUtils.isEmpty(userClientsList)) {
						for (UserClients userClients : userClientsList) {
							Map<String, Object> oldMap = Maps.newHashMap();
							oldMap.put(status, userClients.getIsActive());
							oldMap.put(endDateField, userClients.getEndDate());
							String oldDataString = GenericUtils.gsonInstance.get().toJson(oldMap);
							userClients.setIsActive(Boolean.FALSE);
							setUserClientEndDate(endDate, userClients);
							Map<String, Object> newMap = Maps.newHashMap();
							newMap.put(status, userClients.getIsActive());
							newMap.put(endDateField, userClients.getEndDate());
							String newDataString = GenericUtils.gsonInstance.get().toJson(newMap);
							buildAuditUserChanges(userClients, oldDataString, newDataString);
						}
						// update to db.
						this.userClientRepository.saveAll(userClientsList);
					}
				}
			}
		} catch (Exception ex) {
			LogUtils.errorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "updateClientAccess"),
					ExceptionUtils.getRootCauseMessage(ex), ex);
		}
		return clientCodes;
	}

	private void setUserClientEndDate(LocalDate endDate, UserClients userClients) {
		if (null != endDate) {
			userClients.setEndDate(endDate.atStartOfDay());
		}
	}

	private Map<String, LocalDate> updateTerminateRole(List<AccessTermination> accessTerminationList) {
		Map<String, LocalDate> dateMap = Maps.newHashMap();
		try {
			List<AccessTermination> employeeTerminationList = accessTerminationList.stream()
					.filter(accessTermination -> null != accessTermination.getEmployeeCode())
					.collect(Collectors.toList());
			
			if (!CollectionUtils.isEmpty(employeeTerminationList)) {
				for(AccessTermination access : employeeTerminationList) {
					dateMap.put(access.getClientCode()+"-"+access.getEmployeeCode(), access.getEndDate());
				}
				updateEmployeeAccess(employeeTerminationList);
			}
			if (!CollectionUtils.isEmpty(accessTerminationList)) {
				accessTerminationList.forEach(terminatedList -> 
					terminatedList.setStartDateProcessed(Boolean.TRUE)
				);
				accessTerminationRepository.saveAll(accessTerminationList);
			}
		} catch (Exception ex) {
			LogUtils.errorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "updateTerminateRole"),
					ExceptionUtils.getRootCauseMessage(ex), ex);
		}
		return dateMap;
	}

	@Override
	public AccessTerminationDTO getTerminatedClientByClientCode(String clientCode) {
		AccessTerminationDTO accessTerminationDTO = null;
		List<AccessTermination> accessTerminationList = null;
		try {
			accessTerminationList = accessTerminationRepository
					.findByClientCodeAndEmployeeCodeIsNull(clientCode);
			if (!CollectionUtils.isEmpty(accessTerminationList)) {
				accessTerminationDTO = accessTerminationMapper
						.accessTerminationListToAccessTerminationDto(accessTerminationList);
			}

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), "getTerminatedClientByClientCode"), e,
					"Error occured while getting the access termination info ");
		}finally {
			GenericUtils.clearForGC(accessTerminationList);
		}
		return accessTerminationDTO;
	}

	@Override
	public AccessTerminationDTO getEmployeeAccessInfo(String clientCode, String employeeCode) {
		AccessTerminationDTO accessTerminationDTO = null;
		List<AccessTermination> accessTerminationList = null;
		try {
			accessTerminationList = accessTerminationRepository
					.findByClientCodeAndEmployeeCode(clientCode, employeeCode);
			if (!CollectionUtils.isEmpty(accessTerminationList)) {
				accessTerminationDTO = accessTerminationMapper
						.accessTerminationListToAccessTerminationDto(accessTerminationList);
			}

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_EMPLOYEE_ACCESS_INFO), e,
					"Error occured while getting the access termination info of employee ");
		}finally {
			GenericUtils.clearForGC(accessTerminationList);
		}
		return accessTerminationDTO;
	}



}
