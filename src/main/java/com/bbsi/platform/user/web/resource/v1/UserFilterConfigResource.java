package com.bbsi.platform.user.web.resource.v1;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.bbsi.platform.common.generic.LogUtils;
import com.bbsi.platform.common.user.dto.UserFilterConfigDTO;
import com.bbsi.platform.user.businessservice.intf.UserFilterConfigBusinessService;
import com.bbsi.platform.user.utils.MethodNames;


@RestController
@RequestMapping("/v1/user/filter")
@CrossOrigin
@Logging
public class UserFilterConfigResource {
	
	@Autowired 
	private UserFilterConfigBusinessService userFilterConfigBusinessService;
	
	/**
	 * 
	 * @param principal
	 * @return
	 */
	@Secured
	@GetMapping
	public List<UserFilterConfigDTO> getAllUserFilterConfigsByClientCodeAndUserId(@AuthenticationPrincipal UserPrincipal principal) {
		List<UserFilterConfigDTO> userFilterConfigDTOs = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID),
					String.format("Retriving user filter config details by user Id  ::%s and clientCode :: %s ",
							principal.getUserId(), principal.getClientCode()));
			userFilterConfigDTOs = userFilterConfigBusinessService.getAllUserFilterConfigsByClientCodeAndUserId(principal.getClientCode(),principal.getUserId());
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID),
					String.format("Retrived user filter config details %s", userFilterConfigDTOs));

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID), e,
					"Error occured while getting user filter config Details by");
		}
		return userFilterConfigDTOs;
	}
	
	/**
	 * This method is used to get the payload by client
	 * @param id
	 * @param principal
	 * @return
	 */
	@Secured
	@GetMapping(path = "filterId/{id}")
	public String getUserFilterConfigsByClientCodeAndUserIdAndFilterId(@PathVariable("id") long id,
			@AuthenticationPrincipal UserPrincipal principal) {
		String userFilterConfigPayload = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID_AND_FILTER_ID),
					String.format("Retriving user filter config details by user Id  ::%s and clientCode :: %s and filter id :: %s ",
							principal.getUserId(), principal.getClientCode(),id));
			userFilterConfigPayload = userFilterConfigBusinessService.getAllUserFilterConfigsByClientCodeAndUserIdAndFilterId(principal.getClientCode(),principal.getUserId(),id);
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID_AND_FILTER_ID),
					String.format("Retrived user filter config details %s", userFilterConfigPayload));

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID_AND_FILTER_ID), e,
					"Error occured while getting user filter config Details by");
		}
		return userFilterConfigPayload;
	}
	
	/**
	 * This method is used to delete the user selected config filter
	 * @param clientCode
	 * @param userId
	 * @param id
	 * @param principal
	 */
	@Secured
	@DeleteMapping(path = "filterId/{id}")
	public void deleteUserFilterConfigByClientCodeAndUserIdAndFilterId(@PathVariable("id") long id,
			@AuthenticationPrincipal UserPrincipal principal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.DELETE_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID_AND_FILTER_ID),
					String.format("Deleting user filter config details by user Id  ::%s and clientCode :: %s and filter id :: %s ",
							principal.getUserId(), principal.getClientCode(),id));
			userFilterConfigBusinessService.deleteByClientCodeAndUserIdAndFilterId(principal.getClientCode(), principal.getUserId(), id);

		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.DELETE_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID_AND_FILTER_ID), e,
					"Error occurred while deleting position Details");
		}

	}
	
	/**
	 * This method is used to create user filter config
	 * @param userFilterConfigDTO
	 * @param userPrincipal
	 * @return
	 */
	@Secured
	@PostMapping
	public UserFilterConfigDTO createUserFilterConfig(@RequestBody String userConfigPayload,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CRATE_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID),
					String.format("Creating user filter config  %s ",userConfigPayload));
			return userFilterConfigBusinessService.createUserFilterConfig(userConfigPayload,userPrincipal.getClientCode(),userPrincipal.getUserId());

		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CRATE_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID), e,
					"Error occurred while creating user filter config Details");
		}
		
		return null;

	}
	
	
	/**
	 * 
	 * @param id
	 * @param userConfigPayload
	 * @param userPrincipal
	 * @return
	 */
	@Secured
	@PutMapping(path = "filterId/{id}")
	public UserFilterConfigDTO updateUserFilterConfig(@PathVariable("id") long id,@RequestBody String userConfigPayload,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID),
					String.format("Updating user filter config  %s ",userConfigPayload));
			return userFilterConfigBusinessService.updateUserFilterConfig(id,userConfigPayload,userPrincipal.getClientCode(),userPrincipal.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID), e,
					"Error occurred while updating user filter config Details");
		}
		
		return null;

	}
	
}
