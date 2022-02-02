package com.bbsi.platform.user.web.resource.v1;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.annotation.Secured;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.enums.Enums;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.LogUtils;
import com.bbsi.platform.common.user.dto.RbacDTO;
import com.bbsi.platform.exception.business.UnauthorizedAccessException;
import com.bbsi.platform.exception.enums.ErrorCodes;
import com.bbsi.platform.exception.model.ErrorDetails;
import com.bbsi.platform.user.businessservice.intf.RbacBusinessService;
import com.bbsi.platform.user.utils.MethodNames;

/**
 * @author anandaluru
 *
 */
@RestController
@RequestMapping("/v1/rbac")
@CrossOrigin
@Logging
public class RbacResource {

	
	  @Autowired private RbacBusinessService rbacBusinessService;
	  
	  private static final String UNAUTHORIZED_ACCESS = "Unauthorized access!!";
	 

	/**
	 * @param rbacDTO
	 * @return
	 */
	@Secured
	@PostMapping(path = "/{type}")
	public RbacDTO createRbac(@PathVariable(name = "type") Enums.UserEnum type, @RequestBody RbacDTO rbacDTO,
														@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_ROLE),
					String.format("Creating rbac with details %s", rbacDTO));
			privilegeCheck(userPrincipal, type);
			rbacDTO = rbacBusinessService.createRbac(rbacDTO, type.toString(), userPrincipal);
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_ROLE),
					String.format("Created rbac with details %s", rbacDTO));
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_ROLE), e,
					"Error occured while creating rbac Details");
		}
		return rbacDTO;
	}
	
	/**
	 * get all rbacs.
	 * 
	 * @return
	 */
	@Secured
	@GetMapping(path = "/all/{type}")
	public List<RbacDTO> getAllRbacs(@PathVariable(name = "type") Enums.UserEnum type,@AuthenticationPrincipal UserPrincipal userPrincipal) {
		List<RbacDTO> rbacDTOList = null;
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_ROLES),
					"getting All Rbacs");

			rbacDTOList = rbacBusinessService.getAllRbacs(userPrincipal, type.toString());

			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_ROLES),
					"got All Rbacs");
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_ROLES),
					exception, "Error occured while getting all rbacs");
		}
		return rbacDTOList;
	}

	/**
	 * get all rbacs.
	 *
	 * @return
	 */
	@Secured
	@GetMapping(path = "/client")
	public List<RbacDTO> getAllClientRoles(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		List<RbacDTO> rbacDTOList = null;
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_CLIENT_ROLES),
					"getting All client template roles");

			rbacDTOList = rbacBusinessService.getAllClientRoles(userPrincipal);

			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_CLIENT_ROLES),
					"got All client specific roles");
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_CLIENT_ROLES),
					exception, "Error occured while getting All client template roles");
		}
		return rbacDTOList;
	}


	/**
	 * get all rbacs.
	 *
	 * @return
	 */
	@Secured
	@GetMapping(path = "/roles/{clientOnly}")
	public List<RbacDTO> getAllRoles(@PathVariable(name = "clientOnly", required = true) boolean clientOnly, @AuthenticationPrincipal UserPrincipal userPrincipal) {
		List<RbacDTO> rbacDTOList = null;
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_ROLES),
					"getting All client specific roles");

			rbacDTOList = rbacBusinessService.getAllRoles(clientOnly, userPrincipal.getClientCode());

			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_ROLES),
					"got All client specific roles");
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_ROLES),
					exception, "Error occured while getting All client specific roles");
		}
		return rbacDTOList;
	}
	
	/**
	 * get all client roles.
	 * 
	 * @return
	 */
	@Secured
	@GetMapping(path = "/role/client/{clientCode}")
	public List<RbacDTO> getAllClientRoles(@PathVariable(name = "clientCode") String clientCode, @AuthenticationPrincipal UserPrincipal userPrincipal) {
		List<RbacDTO> rbacDTOList = null;
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_ROLES),
					"getting All client roles");

			rbacDTOList = rbacBusinessService.getClientRoles(clientCode, userPrincipal);

			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_ROLES),
					"got client roles");
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_ROLES),
					exception, "Error occured while getting all client roles");
		}
		return rbacDTOList;
	}

	/**
	 * @param code
	 * @return
	 */
	@Secured
	@GetMapping(path = "/{type}/client/{clientCode}")
	public RbacDTO getRbacByClientCode(@PathVariable(name = "type") Enums.UserEnum type, 
			@PathVariable("clientCode") String clientCode, @RequestParam(name = "selected", required = false) boolean selected,
			@RequestParam(name = "flag", required = false) boolean flag, @AuthenticationPrincipal UserPrincipal userPrincipal) {
		RbacDTO rbacDTO = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ROLE_BY_CLIENT_CODE),
					String.format("Retrieving Rbac details with client code %s", clientCode));
			List<String> ignoreUserTypes = Arrays.asList(GenericConstants.USERTYPE_VANCOUVER, GenericConstants.USERTYPE_BRANCH, GenericConstants.USERTYPE_CLIENT, GenericConstants.USERTYPE_EXTERNAL);
			if (ignoreUserTypes.contains(userPrincipal.getUserType())) {
				throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA001, UNAUTHORIZED_ACCESS));
			}
			rbacDTO = rbacBusinessService.getEntriesByCode(clientCode, type, selected, flag, userPrincipal);
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ROLE_BY_CLIENT_CODE),
					String.format("Retrieved Rbac details with client code %s, %s", clientCode, rbacDTO));
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ROLE_BY_CLIENT_CODE), e,
					"Error occured while getting rbac Details by");
		}
		return rbacDTO;
	}

	/**
	 * @param rbacDTO
	 * @return
	 */
	@Secured
	@PutMapping(path = "/{type}")
	public RbacDTO updateRbac(@PathVariable(name = "type") Enums.UserEnum type, @RequestBody RbacDTO rbacDTO,
														@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_ROLE),
					String.format("Updating Rbac with details %s", rbacDTO));
			privilegeCheck(userPrincipal, type);
			rbacDTO = rbacBusinessService.updateRbac(rbacDTO, type.toString(), userPrincipal);
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(),  MethodNames.UPDATE_ROLE),
					String.format("Updated Rbac with details %s", rbacDTO));
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(),  MethodNames.UPDATE_ROLE), e,
					"Error occured while updating rbac Details");
		}
		return rbacDTO;
	}


	/**
	 * This method throws an Unauthorized exception if Branch, client or external user is trying to create/edit access group and role.
	 *
	 * @param userPrincipal
	 * @param type
	 */
	private void privilegeCheck(UserPrincipal userPrincipal, Enums.UserEnum type) {
		String userType = userPrincipal.getUserType();
		List<String> ignoreUserTypes = Arrays.asList(GenericConstants.USERTYPE_BRANCH, GenericConstants.USERTYPE_CLIENT, GenericConstants.USERTYPE_EXTERNAL);
		
		if (type.equals(Enums.UserEnum.CLIENT)) {
			throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA001, UNAUTHORIZED_ACCESS));
		} else if (ignoreUserTypes.contains(userType)) {
			if ((type.equals(Enums.UserEnum.ACCESS_GROUP) || type.equals(Enums.UserEnum.ROLE))
					|| (GenericConstants.USERTYPE_BRANCH.equalsIgnoreCase(userType)
							&& type.equals(Enums.UserEnum.COPY_ROLE))) {
				throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA001, UNAUTHORIZED_ACCESS));
			}
		}	
	}
}
