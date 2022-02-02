package com.bbsi.platform.user.web.resource.v1;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.annotation.Secured;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.LogUtils;
import com.bbsi.platform.common.user.dto.AccessTerminationDTO;
import com.bbsi.platform.user.businessservice.intf.AccessTerminationBusinessService;
import com.bbsi.platform.user.utils.MethodNames;

/**
 * Class that exposes the methods to get the AccessTermination.
 * 
 * @author jkolla
 *
 */
@RestController
@RequestMapping("/v1/user/access/terminate/info")
@CrossOrigin
@Logging
public class AccessTerminationResource {
	
	private List<String> ignoreUserTypes = Arrays.asList(GenericConstants.USERTYPE_VANCOUVER, GenericConstants.USERTYPE_BRANCH);

	@Autowired
	private AccessTerminationBusinessService accessTerminationService;

	/**
	 * Method for saving the employee notification info.
	 * 
	 * @param accessTerminationDto
	 * @return Returns the {@link AccessTerminationDTO} object.
	 */
	@Secured
	@PostMapping
	public AccessTerminationDTO saveAccessTerminationInfo(@RequestBody AccessTerminationDTO accessTerminationDto,
			@AuthenticationPrincipal UserPrincipal principal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_ACCESS_TERMINATION_INFO),
					"Saving Access Termination details");

			if(null != principal && ignoreUserTypes.contains(principal.getUserType())) {
				accessTerminationDto = accessTerminationService.saveAccessTermination(accessTerminationDto);
			}

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_ACCESS_TERMINATION_INFO), e,
					"Error occured while saving accesstermination info Details");
		}
		return accessTerminationDto;
	}

	/**
	 * Method for updating the access termination dto.
	 * 
	 * @param accessTerminationDto
	 * @return
	 */
	@Secured
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public AccessTerminationDTO updateAccessTerminationInfo(@RequestBody AccessTerminationDTO accessTerminationDto) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_ACCESS_TERMINATION_INFO),
					"updating Access Termination details");

			accessTerminationDto = accessTerminationService.updateAccessTermination(accessTerminationDto);

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_ACCESS_TERMINATION_INFO), e,
					"Error occured while updating access termination info details");
		}
		return accessTerminationDto;
	}

	/**
	 *  This service will be schduled as a job. Its a nightly job.
	 *  Based on startDate, Terminated Employee role will be assigned to all terminated employees.
	 *  Based on endDate, portal acccess will be restricted for user types client, employee and external.
	 *
	 */
	@GetMapping("/run")
	public void updateStatusByEndDate(@AuthenticationPrincipal UserPrincipal principal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_ACCESS_TERMINATION_INFO),
					"updating AccessTerminations details");
			
			if(null != principal && principal.getEmail().equals(GenericConstants.INTEGRATION_ADMIN)) {
				this.accessTerminationService.updateStatusByEndDate();
			}
			
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_ACCESS_TERMINATION_INFO), e,
					"Error occured while updating access termination info");
		}
		
		return;
	}

	@Secured
	@GetMapping("/client/{clientCode}")
	public AccessTerminationDTO getTerminatedClientByClientCode(@PathVariable("clientCode") String clientCode,
			@AuthenticationPrincipal UserPrincipal principal) {

		AccessTerminationDTO accessTerminationDTO = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_TERMINATED_CLIENT_BY_CLIENT_CODE),
					"Getting terminated clients by client code");

			if(null != principal && ignoreUserTypes.contains(principal.getUserType())) {
				accessTerminationDTO = accessTerminationService.getTerminatedClientByClientCode(clientCode);
			}

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_TERMINATED_CLIENT_BY_CLIENT_CODE), e,
					"Error occured while getting terminated clients by client code");
		}

		return accessTerminationDTO;
	}

	@Secured
	@GetMapping("client/{clientCode}/employee/{employeeCode}")
	public AccessTerminationDTO getEmployeeAccessInfo(@PathVariable("clientCode") String clientCode, 
			@PathVariable("employeeCode") String employeeCode, @AuthenticationPrincipal UserPrincipal principal) {

		AccessTerminationDTO accessTerminationDTO = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_EMPLOYEE_ACCESS_INFO),
					String.format("Getting employee access termination info for employee code : %s of client :%s", employeeCode, clientCode));

			if(null != principal && ignoreUserTypes.contains(principal.getUserType())) {
				accessTerminationDTO = accessTerminationService.getEmployeeAccessInfo(clientCode, employeeCode);
			}

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_EMPLOYEE_ACCESS_INFO), e,
					"Error occured while getting access termination info of employee");
		}

		return accessTerminationDTO;
	}

}
