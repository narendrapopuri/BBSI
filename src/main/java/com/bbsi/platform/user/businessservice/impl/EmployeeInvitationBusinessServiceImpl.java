package com.bbsi.platform.user.businessservice.impl;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.businessservice.intf.IntegrationBusinessService;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.enums.BoomiEnum;
import com.bbsi.platform.common.generic.BoomiHelper;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.JsonUtils;
import com.bbsi.platform.common.generic.RestClient;
import com.bbsi.platform.common.user.dto.EmployeeInvitationDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.user.businessservice.intf.EmployeeInvitationBusinessService;
import com.bbsi.platform.user.mapper.EmployeeInvitationMapper;
import com.bbsi.platform.user.model.AccessTermination;
import com.bbsi.platform.user.model.EmployeeInvitation;
import com.bbsi.platform.user.model.UserClients;
import com.bbsi.platform.user.repository.AccessTerminationRepository;
import com.bbsi.platform.user.repository.EmployeeInvitationRepository;
import com.bbsi.platform.user.repository.UserClientsRepository;
import com.bbsi.platform.user.utils.MethodNames;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Class for provides the behavior published by
 * {@link EmployeeInvitationBusinessService}
 * 
 * @author vpula
 *
 */
@Logging
@Service
public class EmployeeInvitationBusinessServiceImpl implements EmployeeInvitationBusinessService {

	@Autowired
	private EmployeeInvitationRepository employeeInvitationRepository;

	@Autowired
	private EmployeeInvitationMapper employeeNotificatoinMapper;
	
	@Autowired
	private IntegrationBusinessService integrationBusinessService;
	
	@Autowired
	private UserClientsRepository userClientRepository;

	@Autowired
	private BoomiHelper boomiHelper;

	@Autowired
	private RestClient restClient;
	
	@Autowired
	private AccessTerminationRepository accessTerminationRepository;
	
	@Value("${integration.check.employees}")
	private String empVerifyUrl;

	@Value("${get.integration.service.url}")
	private String getIntegrationUrl;

	private static final String STATUS = "status";
	
	private static final String EMP_T = "T";
	
	private static final String PERSONAL_EMAIL = "personal_email";

	private static final String MOBILE_NUMBER = "mobile_number";

	/*
	 * Method for fetching employee notification info by client code.
	 */
	@Override
	public List<EmployeeInvitationDTO> getAllNotificatonInfoByClientCode(String clientCode, String token) {
		List<EmployeeInvitation> employeeInvitationList = Lists.newArrayList();
		try {
			List<EmployeeInvitation> employeeInvitationInfo = employeeInvitationRepository.findByClientCode(clientCode);
			
			if(CollectionUtils.isNotEmpty(employeeInvitationInfo)) {
				List<String> empList = employeeInvitationInfo.stream()
						.filter(obj -> StringUtils.isNotEmpty(obj.getEmployeeCode()))
						.map(EmployeeInvitation::getEmployeeCode).collect(Collectors.toList());
				Map<String, String> headers = new HashMap<>();
				headers.put(GenericConstants.AUTH_TOKEN_HDR, token);
				headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
				String empReponse = restClient.postForString(empVerifyUrl, empList, headers);
				if(StringUtils.isNotEmpty(empReponse) && empReponse.equals("[]")) {
					for (EmployeeInvitation empInfo : employeeInvitationInfo) {
						if(!empReponse.contains(empInfo.getEmployeeCode())) {
							employeeInvitationList.add(empInfo);
						}
					}
				} else {
					employeeInvitationList = employeeInvitationInfo;
				}
			}
		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_NOTIFICATIONS_INFO_BY_CLIENTCODE), e,
					String.format("Error occured while getting employee notification info for client %s", clientCode));
		}

		return employeeNotificatoinMapper.employeeInvitationToEmployeeInvitationDTOs(employeeInvitationList);
	}

	/*
	 * Method for saving employuee notification information.
	 */
	@Override
	public EmployeeInvitationDTO saveEmployeeInvitationInfo(EmployeeInvitationDTO employeeInvitationDto, String token) {
		EmployeeInvitation employeeInvitation = null;

		try {
			integrationBusinessService.verifyEmployee(employeeInvitationDto.getEmployeeCode(), token);
			employeeInvitation = employeeNotificatoinMapper
					.employeeInvitationToEmployeeInvitationDTO(employeeInvitationDto);

			employeeInvitationRepository.save(employeeInvitation);

		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_EMPLOYEE_INVITATION_INFO), e,
					"Error occured while saving the employee notification info");
		}
		/* No need of saved entity */
		return employeeInvitationDto;
	}

	/*
	 * Method for saving all employee info.
	 */
	@Override
	public List<EmployeeInvitationDTO> saveAllEmployeeInvitationInfo(
			List<EmployeeInvitationDTO> employeeInvitationDtoList, String token) {
		List<EmployeeInvitation> employeeInvitationList = null;

		try {
			List<String> empList = employeeInvitationDtoList.stream()
					.filter(obj -> StringUtils.isNotEmpty(obj.getEmployeeCode()))
					.map(EmployeeInvitationDTO::getEmployeeCode).collect(Collectors.toList());
			integrationBusinessService.verifyEmployee(String.join(",", empList), token);
			
			employeeInvitationList = employeeNotificatoinMapper
					.employeeInvitationDTOToEmployeeInvitationDTOs(employeeInvitationDtoList);
			/* Update last sent date*/
			employeeInvitationList.parallelStream()
								.forEach(e->e.setLastSentDate(LocalDateTime.now()));
			
			employeeInvitationRepository.saveAll(employeeInvitationList);

		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_ALL_EMPLOYEE_INVITATION_INFO), e,
					"Error occured while saving all the employees notification info ");
		}
		return employeeInvitationDtoList;
	}
	
	/*
	 * Method for saving all the employee invitation details by client code.
	 * 
	 */
	@Override
	public void saveAllByClientCodeAndEmployeeId(String clientCode,List<String> employeeCodes) {
		List<EmployeeInvitation> employeeInvitationList = new ArrayList<>();
		
		try {
			if (CollectionUtils.isNotEmpty(employeeCodes)) {
				for(String code: employeeCodes) {
					EmployeeInvitation invitation = null;
					/* Find existing invitation details*/
					invitation = this.employeeInvitationRepository
							.findByClientCodeAndEmployeeCode(clientCode, code);
					
					if (invitation == null) {
						invitation = new EmployeeInvitation();
						invitation.setClientCode(clientCode);
						invitation.setEmployeeCode(code);
					}
					invitation.setLastSentDate(LocalDateTime.now());
					
					employeeInvitationList.add(invitation);
				}
				
				/* Save or update*/
			employeeInvitationRepository.saveAll(employeeInvitationList);
			}
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_ALL_CLIENTCODE_AND_EMPLOYEEID), e,
					"Error occured while saving the employee invitation info by clientcode and employeeId");
		}
	}
	
	@Override
	public String getEmployeesForBulkInvitation(UserPrincipal userPrincipal) {
		List<Object> temp = new ArrayList<>();
		Map<String, List<UserClients>> portalLoginMap = Maps.newHashMap();
		Map<String, List<AccessTermination>> terminateEmpMap = Maps.newHashMap();
		try {
			List<UserClients> userClients = userClientRepository
					.findByClientCodeAndUserType(userPrincipal.getClientCode(), GenericConstants.USERTYPE_EMPLOYEE);
			List<AccessTermination> terminatedEmpList = accessTerminationRepository
					.findByClientCodeAndEmployeeCodeIsNotNullAndEndDateIsNotNull(userPrincipal.getClientCode());
			
			portalLoginMap = userClients.stream().collect(Collectors.groupingBy(UserClients::getEmployeeCode));
			terminateEmpMap = terminatedEmpList.stream().collect(Collectors.groupingBy(AccessTermination::getEmployeeCode));

			String response = getEmployeeResponse(userPrincipal.getClientCode(), userPrincipal.getToken());
			getEmployees(response, portalLoginMap,terminateEmpMap,temp, userPrincipal.getToken());

		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_EMPLOYEES_FOR_BULKINVITATION),
					e, "Error occurred while getting Employees For BulkInvitation");
		}
		return temp.toString();
	}

	private String getEmployeeResponse(String clientCode, String token) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", token);
		Map<String, String> uriParams = new HashMap<>();
		uriParams.put("code", clientCode);
		uriParams.put("type", "CLIENT_EMPLOYEES_FILTER_UQ");
		UriComponentsBuilder builder = null;
		String employeeUrl =null;
		builder = UriComponentsBuilder.fromUriString(getIntegrationUrl+"?id=EXTWITHSTATUS");
		employeeUrl = builder.buildAndExpand(uriParams).toUriString();
		return restClient.getForString(employeeUrl, new HashMap<>(), headers);
	}

	@SuppressWarnings("deprecation")
	private void getEmployees(String response, Map<String, List<UserClients>> portalLoginMap,Map<String, List<AccessTermination>> terminateEmpMap, List<Object> temp, String token) {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(response);
		Map<String, JsonElement> employeeMap = Maps.newHashMap();
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		populateEmployeeMap(jsonArray, portalLoginMap, terminateEmpMap, employeeMap);
		Map<String, String> headers = new HashMap<>();
		headers.put(GenericConstants.AUTH_TOKEN_HDR, token);
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		String empReponse = restClient.postForString(empVerifyUrl, employeeMap.keySet(), headers);
		if(StringUtils.isNotEmpty(empReponse)) {
			for (Entry<String, JsonElement> emp : employeeMap.entrySet()) {
				if(!empReponse.contains(emp.getKey())) {
					temp.add(emp.getValue());
				}
			}
		}
	}
	
	private void populateEmployeeMap(JsonArray jsonArray,  Map<String, List<UserClients>> portalLoginMap, Map<String, List<AccessTermination>> terminateEmpMap, Map<String, JsonElement> employeeMap) {
		for (int i = 0; i < jsonArray.size(); i++) {
			if(null !=jsonArray.get(i).getAsJsonObject().get(PERSONAL_EMAIL)){
				String personalEmail = jsonArray.get(i).getAsJsonObject().get(PERSONAL_EMAIL).getAsString();
				if(StringUtils.isNotEmpty(personalEmail)){
					jsonArray.get(i).getAsJsonObject().addProperty(PERSONAL_EMAIL, Base64.getEncoder().encodeToString(personalEmail.getBytes()));
				}
			}
			if(null !=jsonArray.get(i).getAsJsonObject().get(MOBILE_NUMBER)){
				String mobileNumber = jsonArray.get(i).getAsJsonObject().get(MOBILE_NUMBER).getAsString();
				if(StringUtils.isNotEmpty(mobileNumber)){
					jsonArray.get(i).getAsJsonObject().addProperty(MOBILE_NUMBER, mobileNumber);
				}
			}
			String status = null;
			if(null !=jsonArray.get(i).getAsJsonObject().get(STATUS)){
			 status = jsonArray.get(i).getAsJsonObject().get(STATUS).getAsString();
			}
			String employeeCode = jsonArray.get(i).getAsJsonObject().get("employee_code").getAsString();
			String value = jsonArray.get(i).getAsJsonObject().toString();
			value = fetchEmpValue(jsonArray, portalLoginMap, terminateEmpMap, i, status, employeeCode, value);
			employeeMap.put(employeeCode, JsonParser.parseString(value));
		}
	}

	private String fetchEmpValue(JsonArray jsonArray, Map<String, List<UserClients>> portalLoginMap,
			Map<String, List<AccessTermination>> terminateEmpMap, int i, String status, String employeeCode,
			String value) {
		if (portalLoginMap.containsKey(employeeCode) && !portalLoginMap.get(employeeCode).isEmpty()){
			UserClients uc = portalLoginMap.get(employeeCode).get(0);
			Map<String, String> requestIdMap = new HashMap<>();
			requestIdMap.put("$.portal_login_id", (null != uc.getUser()) ? Base64.getEncoder().encodeToString(uc.getUser().getEmail().getBytes()) : "");
			if(terminateEmpMap.containsKey(employeeCode) && (null != status &&  EMP_T.equals(status))){
				AccessTermination te = terminateEmpMap.get(employeeCode).get(0);
				requestIdMap.put("$.end_date", te.getEndDate().toString());
			}
			value = JsonUtils.insertJsonField(jsonArray.get(i).getAsJsonObject().toString(), requestIdMap);
		}
		return value;
	}
}
