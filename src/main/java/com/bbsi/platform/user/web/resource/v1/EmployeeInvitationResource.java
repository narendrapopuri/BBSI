package com.bbsi.platform.user.web.resource.v1;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.annotation.Secured;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.LogUtils;
import com.bbsi.platform.common.user.dto.EmployeeInvitationDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.user.businessservice.intf.EmployeeInvitationBusinessService;
import com.bbsi.platform.user.utils.MethodNames;

/**
 * Class that exposes the methods to get the employeeInvitation.
 * 
 * @author jkolla
 *
 */
@RestController
@RequestMapping("/v1/user/invitation/info")
@CrossOrigin
@Logging
public class EmployeeInvitationResource {

	@Autowired
	private EmployeeInvitationBusinessService employeeInvitationService;

	/**
	 * Method for getting employee notifications by client code.
	 * 
	 * @param clientCode
	 * @return Returns {@link List} of {@link EmployeeInvitationDTO} objects.
	 */
	@Secured
	@GetMapping("/client/{clientCode}")
	public List<EmployeeInvitationDTO> getAllEmployeeInvitationByClientCode(
			@PathVariable("clientCode") String clientCode, 
			@RequestHeader(value = GenericConstants.AUTH_TOKEN_HDR) String token) {
		List<EmployeeInvitationDTO> employeeInvitationinfo = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_ALL_EMPLOYEE_NOTIFICATION_BY_CLIENT_CODE),
					"Retrieving all employees invitations details");

			employeeInvitationinfo = employeeInvitationService.getAllNotificatonInfoByClientCode(clientCode, token);

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_ALL_EMPLOYEE_NOTIFICATION_BY_CLIENT_CODE),
					String.format("Retrived featureCode details %s", employeeInvitationinfo));

		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_ALL_EMPLOYEE_NOTIFICATION_BY_CLIENT_CODE),
					e, "Error occured while getting notification info Details by");
		}
		return employeeInvitationinfo;
	}

	/**
	 * Method for saving the employee notification info.
	 * 
	 * @param employeeInvitationDto
	 * @return Returns the {@link EmployeeInvitationDTO} object.
	 */
	@Secured
	@PostMapping
	public EmployeeInvitationDTO saveEmployeeInvitationInfo(
			@RequestBody EmployeeInvitationDTO employeeInvitationDto, 
			@RequestHeader(value = GenericConstants.AUTH_TOKEN_HDR) String token,
			@AuthenticationPrincipal UserPrincipal principal) {
		EmployeeInvitationDTO employeeInvitationinfo = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_EMPLOYEE_INVITATION_INFO),
					"Persisting employee invitations details");

			employeeInvitationDto.setClientCode(principal.getClientCode());
			employeeInvitationinfo = employeeInvitationService
					.saveEmployeeInvitationInfo(employeeInvitationDto, token);

		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_EMPLOYEE_INVITATION_INFO),
					e, "Error occured while saving notification info Details");
		}
		return employeeInvitationinfo;
	}
	
	/**
	 * Method for saving all employee notification information.
	 * @param employeeInvitationDto
	 * @return
	 */
	@Secured
	@PostMapping("/all")
	public List<EmployeeInvitationDTO> saveAllEmployeeInvitationInfo(
			@RequestBody List<EmployeeInvitationDTO> employeeInvitationDto, 
			@RequestHeader(value = GenericConstants.AUTH_TOKEN_HDR) String token,
			@AuthenticationPrincipal UserPrincipal principal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_ALL_EMPLOYEE_INVITATION_INFO),
					"Persisting all employees nvitations details");

			if(CollectionUtils.isNotEmpty(employeeInvitationDto)) {
				employeeInvitationDto.forEach(dto -> dto.setClientCode(principal.getClientCode()));
			}
			employeeInvitationDto = employeeInvitationService
					.saveAllEmployeeInvitationInfo(employeeInvitationDto, token);

		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_ALL_EMPLOYEE_INVITATION_INFO),
					e, "Error occured while saving all employees invitation details");
		}
		return employeeInvitationDto;
	}
	
	
	/**
	 * Method for getting employee ForBulkInvitation.
	 * 
	 * @param userPrincipal
	 * @return
	 */
	@Secured
	@GetMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getEmployeesForBulkInvitation(@AuthenticationPrincipal UserPrincipal userPrincipal, 
			@RequestHeader(value = GenericConstants.AUTH_TOKEN_HDR) String token) {
		String response = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_EMPLOYEES_FOR_BULKINVITATION),
					"Retrieving all employees invitations details");
			userPrincipal.setToken(token);
			response = employeeInvitationService.getEmployeesForBulkInvitation(userPrincipal);
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_EMPLOYEES_FOR_BULKINVITATION),
					"Retrived Employees for  BulkInvitation");
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_ALL_EMPLOYEE_NOTIFICATION_BY_CLIENT_CODE),
					e, "Error occured while getting Employees For BulkInvitation");
		}
		return response;
	}


}
