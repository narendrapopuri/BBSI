package com.bbsi.platform.user.web.resource.v1;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.bbsi.platform.common.dto.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.annotation.Secured;
import com.bbsi.platform.common.client.dto.UserToolbarSettingsDTO;
import com.bbsi.platform.common.employee.dto.CustomPersonalDTO;
import com.bbsi.platform.common.generic.EncryptAndDecryptUtil;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.LogUtils;
import com.bbsi.platform.common.user.dto.TimenetLoginInfo;
import com.bbsi.platform.common.user.dto.UserCredentials;
import com.bbsi.platform.common.user.dto.UserInvitationDTO;
import com.bbsi.platform.common.user.dto.UserMfaDTO;
import com.bbsi.platform.common.user.dto.UsersDTO;
import com.bbsi.platform.common.user.dto.v2.PolicyAcceptedDTO;
import com.bbsi.platform.common.user.dto.v2.UserExistDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.exception.business.UnauthorizedAccessException;
import com.bbsi.platform.exception.business.ValidationException;
import com.bbsi.platform.exception.enums.ErrorCodes;
import com.bbsi.platform.exception.model.ErrorDetails;
import com.bbsi.platform.user.businessservice.intf.UserBusinessService;
import com.bbsi.platform.user.utils.MethodNames;
import com.bbsi.platform.user.utils.Utils;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/v1/user")
@CrossOrigin
@Logging
public class UserResource {

	@Autowired
	private UserBusinessService userBusinessService;

	@Autowired
	private TokenStore tokenStore;
	
	@Autowired
	private EncryptAndDecryptUtil encryptAndDecryptUtil;
	
	private static final long USER_KEY_VALIDITY_DAYS = 30;
	
	@Value("${login.key}")
	private String secret;

	@Value("${base.url}")
	private String baseUrl;
	
	@Value("${bbsi.head.emails}")
	private String bbsiHeadEmails;
	
	private static final String UNAUTHORIZED_ACCESS = "Unauthorized access!!";

	@Secured
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public UsersDTO createUser(@RequestHeader(name = "Authorization", required = true) String token,
			@RequestParam(name = "uiUrl", required = false) String uiUrl, @RequestBody UsersDTO userDTO,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_USER),
					String.format("Creating user with details: %s", userDTO));

			userDTO = userBusinessService.createUser(userDTO, token, baseUrl + GenericConstants.LOGIN_PATH, userPrincipal);

			buildWarnings(userDTO);
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_USER),
					String.format("Created user with details: %s", userDTO));
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_USER), e,
					"Error occured while creating user Details");
		}
		return userDTO;
	}
	
	@Secured
	@PutMapping(value = "/updateMfaEmail", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public UserMfaDTO updateMfaEmail(@RequestHeader(name = "Authorization", required = true) String token,
			@RequestBody UserMfaDTO userMfaDTO,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_MFA_EMAIL),
					String.format("Updating MFA Email and Mobile Number: %s", userMfaDTO));

			if(!userPrincipal.getClientCode().equalsIgnoreCase(userMfaDTO.getClientCode())) {
				throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA001, "Unauthorized access!!"));
			}
			
			if((!(userPrincipal.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER))
					&& !(userPrincipal.getUserType().equals(GenericConstants.USERTYPE_BRANCH)))
					|| userMfaDTO.getIsConfactInfoUpdate()) {
				
				userMfaDTO = userBusinessService.updateMfaEmail(userMfaDTO, userPrincipal, token);
				userPrincipal.setHasMfaInfo(true);
				setUserPrincipal(userPrincipal, token);
			}
			
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_MFA_EMAIL),
					String.format("Updated MFA Email and Mobile Number: %s", userMfaDTO));
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_MFA_EMAIL), e,
					"Error occured while updating MFA Email and Mobile Number");
		}
		return userMfaDTO;
	}
	
	


	@Secured
	@PutMapping
	public UsersDTO updateUser(@RequestHeader(name = "Authorization", required = true) String token,
			@RequestBody UsersDTO userDTO, @AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER),
					"Updating user");
			userDTO = userBusinessService.updateUser(userDTO, token, userPrincipal);
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER),
					"User updated successfully");
			buildWarnings(userDTO);
		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER), e,
					"Error occurred while updating user");
		}
		return userDTO;
	}

	/**
	 * Method for updating user personal info like email, first name and last name.
	 * 
	 * @param token
	 * @param userPersonalInfoDto
	 * @return
	 */
	@Secured
	@PutMapping(value = "/personalinfo",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public CustomPersonalDTO updateUserPersonalInfo(
			@RequestHeader(name = "Authorization", required = true) String token,
			@RequestBody CustomPersonalDTO userPersonalInfoDto,@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_PERSONAL_INFO),
					"Updating user's personal info");

			userPersonalInfoDto = userBusinessService.updateUserPersonalInfo(userPersonalInfoDto, token, userPrincipal);

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_PERSONAL_INFO),
					"UserPersonal Information updated successfully");

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_PERSONAL_INFO), e,
					"Error occurred while updating user personal Information");
		}

		return userPersonalInfoDto;
	}
	
	/**
	 * Used to verify the oauth token
	 *
	 * @param principal
	 * @return
	 */
	@GetMapping(value = "/verify", produces = MediaType.APPLICATION_JSON_VALUE)
	public ParentDTO verifyUser(@AuthenticationPrincipal UserPrincipal principal) {
		LogUtils.basicDebugLog.accept("START :: getUser ::");
		ParentDTO principalDTO = new ParentDTO();
		principalDTO.setPrincipal(principal);
		return filterPrincipal(principalDTO);
	}
	
	@GetMapping(value = "/chkmfa")
	public Map<String, String> checkMFA(@AuthenticationPrincipal UserPrincipal principal,
			@RequestHeader(value = "user-key", required = false) String userKey) {
		Map<String, String> mapMFA = Maps.newHashMap();
		List<String> ignoreUserTypes = Arrays.asList(GenericConstants.USERTYPE_VANCOUVER, GenericConstants.USERTYPE_BRANCH);
		String exten[] = bbsiHeadEmails.split(",");
		
		if (principal.getMfaAuthorizationRequired() 
				&& (ignoreUserTypes.contains(principal.getUserType())
						|| Utils.checkIfEmailHasExtension(principal.getEmail(),exten))) {
			mapMFA.put("mfa", "N");
		} else if (principal.getMfaAuthorizationRequired()) {
			if (StringUtils.isEmpty(userKey)) {
				mapMFA.put("mfa", "Y");
			} else {
				String key = encryptAndDecryptUtil.decrypt(userKey,secret);

				String[] keyArray = key.split("<<>>");
				if (keyArray.length != 2)
					throw new UnauthorizedAccessException(
							new ErrorDetails(ErrorCodes.UA004, MethodNames.INVALID_MFA));

				String userName = keyArray[0];
				LocalDate date = LocalDate.parse(keyArray[1]);

				if ((!principal.getEmail().equals(userName))
						&& date.plusDays(USER_KEY_VALIDITY_DAYS).isAfter(LocalDate.now())) {
					throw new UnauthorizedAccessException(
							new ErrorDetails(ErrorCodes.UA004, MethodNames.INVALID_MFA));
				}
				mapMFA.put("mfa", "N");
			}
		}
		return mapMFA;
	}
	
	/**
	 * Used to fetch the user principal
	 *
	 * @param principal
	 * @return
	 */
	@GetMapping(value = "/principal",produces = MediaType.APPLICATION_JSON_VALUE)
	public ParentDTO principal(@AuthenticationPrincipal UserPrincipal principal,
			@RequestHeader(value = GenericConstants.AUTH_TOKEN_HDR, required = false) String authToken,
			@RequestHeader(value = "user-key", required = false) String userKey) {
		LogUtils.basicDebugLog.accept("START :: getUserPrincipal ::");
		
		List<String> ignoreUserTypes = Arrays.asList(GenericConstants.USERTYPE_VANCOUVER, GenericConstants.USERTYPE_BRANCH);
		
		String exten[] = bbsiHeadEmails.split(",");
		
		if (principal.getMfaAuthorizationRequired() 
				&& (ignoreUserTypes.contains(principal.getUserType())
						|| Utils.checkIfEmailHasExtension(principal.getEmail(),exten))) {
			principal.setMfaAuthorizationRequired(false);
			setUserPrincipal(principal, authToken);
		} else if (principal.getMfaAuthorizationRequired()) {
			if (StringUtils.isEmpty(userKey)) {
				principal.setMfaAuthorizationRequired(true);
				setUserPrincipal(principal, authToken);
			} else {
				String key = encryptAndDecryptUtil.decrypt(userKey,secret);

				String[] keyArray = key.split("<<>>");
				if (keyArray.length != 2)
					throw new UnauthorizedAccessException(
							new ErrorDetails(ErrorCodes.UA004, MethodNames.INVALID_MFA));

				String userName = keyArray[0];
				LocalDate date = LocalDate.parse(keyArray[1]);

				checkInvalidMFAValidation(principal,date,userName);
				
				principal.setMfaAuthorizationRequired(false);
				setUserPrincipal(principal, authToken);
			}
		}

		ParentDTO principalDTO = new ParentDTO();
		principal.setEmail(Base64.getEncoder().encodeToString(principal.getEmail().getBytes()));
		principalDTO.setPrincipal(principal);
		if (StringUtils.isNotEmpty(principal.getClientCode()) && GenericConstants.USERTYPE_CLIENT.equals(principal.getUserType())) {
			userBusinessService.getClientNameByClientCode(principal.getClientCode());
		}

		return filterPrincipal(principalDTO);
	}
	
	private void checkInvalidMFAValidation(UserPrincipal principal,LocalDate date,String userName) {
		if ((!principal.getEmail().equals(userName))
				&& date.plusDays(USER_KEY_VALIDITY_DAYS).isAfter(LocalDate.now())) {
			throw new UnauthorizedAccessException(
					new ErrorDetails(ErrorCodes.UA004, MethodNames.INVALID_MFA));
		}
	}
	
	private ParentDTO filterPrincipal(ParentDTO principalDTO) {
		if(null == principalDTO.getPrincipal().getMfaAuthorizationRequired()
				|| principalDTO.getPrincipal().getMfaAuthorizationRequired().equals(Boolean.TRUE)
				|| null == principalDTO.getPrincipal().getIsPolicyAccepted()
				|| principalDTO.getPrincipal().getIsPolicyAccepted().equals(Boolean.FALSE)
		        || null == principalDTO.getPrincipal().getIsCcpaRequired()
		        || principalDTO.getPrincipal().getIsCcpaRequired().equals(Boolean.TRUE)) {
			principalDTO.getPrincipal().setAccessGroup(null);
			principalDTO.getPrincipal().setBbsiBranch(null);
			principalDTO.getPrincipal().setClientMap(null);
			principalDTO.getPrincipal().setClientName(null);
			principalDTO.getPrincipal().setCostCenterCode(null);
			principalDTO.getPrincipal().setPrivilegeCodes(null);
			principalDTO.getPrincipal().setPrivilegeMap(null);
			principalDTO.getPrincipal().setPrivileges(null);
			principalDTO.getPrincipal().setUserMenu(null);
			principalDTO.getPrincipal().setFeature(null);
		}
		principalDTO.getPrincipal().setUserTypeMap(null);
		if(null != principalDTO.getPrincipal().getCreatedDate()) {
			principalDTO.getPrincipal().setValidity(principalDTO.getPrincipal().getValidity() - ChronoUnit.SECONDS.between(principalDTO.getPrincipal().getCreatedDate(), LocalDateTime.now()));
		}
		return principalDTO;
	}
	
	
	@PostMapping(path = "mfa/verify/user/otp", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verifyOtp(@RequestBody OtpDetailsDTO otpDetailsDTO,
			@RequestHeader(value = GenericConstants.AUTH_TOKEN_HDR, required = false) String token,
			@AuthenticationPrincipal UserPrincipal principal,HttpServletResponse serletResponse) {
		
		if (principal.getMfaFailedAttempts() >= 3) {
			
			return new ResponseEntity<ErrorDetails>(new ErrorDetails(ErrorCodes.MFA001),ErrorCodes.MFA001.getHttpStatus());
		}
		String response = userBusinessService.mfaOtpValidation(otpDetailsDTO, token);
		HttpHeaders responseHeaders = new HttpHeaders();
		if(StringUtils.isNotEmpty(response) && response.contains("isValidToken")) {
			String key = String.format("%s<<>>%s", principal.getEmail() , LocalDate.now().toString());
			String encryptedKey = encryptAndDecryptUtil.encrypt(key,secret);
	        responseHeaders.add("mfa-user", encryptedKey);
			
	        principal.setMfaFailedAttempts(0);
	        principal.setMfaAuthorizationRequired(false);
			setUserPrincipal(principal, token);
			
			return ResponseEntity.ok().headers(responseHeaders).body(response);
		} else {
			int attemtpts = principal.getMfaFailedAttempts()+1;
			principal.setMfaFailedAttempts(attemtpts);
			setUserPrincipal(principal, token);
			if (attemtpts >= 3) {
				return new ResponseEntity<ErrorDetails>(new ErrorDetails(ErrorCodes.MFA001),ErrorCodes.MFA001.getHttpStatus());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Invalid OTP. You have %s attempt(s) left", (3-attemtpts)));
		}
		
	}
	
	@GetMapping(value = "/saveMfaAttempts/{attempts}" , produces = MediaType.APPLICATION_JSON_VALUE)
	public String saveMfaAttempts(@AuthenticationPrincipal UserPrincipal principal,@PathVariable("attempts") int attempts,
			@RequestHeader(value = GenericConstants.AUTH_TOKEN_HDR, required = false) String authToken) {
		LogUtils.basicDebugLog.accept("START :: getUser ::");
		if(attempts > 3) {
			userBusinessService.removeToken(tokenStore, false);
		} else {
			principal.setMfaFailedAttempts(attempts);
			setUserPrincipal(principal, authToken);
		}
		return "success";
	}
	
	@GetMapping(value = "/mfaCancelAttempts" , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> cancelMfaAttempts(@AuthenticationPrincipal UserPrincipal principal,
			@RequestHeader(value = GenericConstants.AUTH_TOKEN_HDR, required = false) String authToken) {
		
		String response = null;
		if(!(principal.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER)) && !(principal.getUserType().equals(GenericConstants.USERTYPE_BRANCH))) {
			response = userBusinessService.validateMfaCancelAttempts(principal);
			if("Success".equals(response)) {
				return ResponseEntity.ok().body("success");
			} else {
				return new ResponseEntity<ErrorDetails>(new ErrorDetails(ErrorCodes.MFA002),ErrorCodes.MFA002.getHttpStatus());
			}
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void setUserPrincipal(UserPrincipal principal, String authToken) {
		principal.setToken(authToken.split(" ")[1]);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(null != auth) {
			Authentication newAuth = new UsernamePasswordAuthenticationToken(principal, auth.getCredentials(), auth.getAuthorities());
			String tokenValue = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
			if (null != tokenValue) {
				OAuth2AccessToken token = tokenStore.readAccessToken(tokenValue);
				if(token != null && tokenStore.readAuthentication(token) != null) {
					OAuth2Authentication oauthAuthentication = new OAuth2Authentication(tokenStore.readAuthentication(token).getOAuth2Request(), newAuth);
					LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SWITCH_BUSINESS),
							String.format("Updated principal object for client code: %s and userType %s : ",
									principal.getClientCode(), principal.getUserType()));
					tokenStore.removeAccessToken(token);
					tokenStore.storeAccessToken(token, oauthAuthentication);
					SecurityContextHolder.getContext().setAuthentication(newAuth);
				}
			}
		}
	}


	/**
	 * This API will provide the client user list by client code and user type
	 * 
	 * @param clientCode
	 * @param type
	 * @return
	 */
	@Secured
	@GetMapping(value = "/client/{clientCode}/type/{type}")
	public List<UsersDTO> getUsersByClientCodeAndType(@PathVariable("clientCode") String clientCode,
			@PathVariable("type") String type) {
		List<UsersDTO> userDTOList = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_ALL_CLIENT_USERS_BY_CLIENTCODE),
					String.format("retriving client user list by clientcode %s : ", clientCode));

			// service call to get the client user list
			userDTOList = userBusinessService.getUsersByClientCodeAndType(clientCode, type);

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_ALL_CLIENT_USERS_BY_CLIENTCODE),
					String.format("retrived client user list by clientcode %s : ", clientCode));
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_ALL_CLIENT_USERS_BY_CLIENTCODE),
					exception,
					String.format("Error occurred while retriving client user list for clientcode is %s", clientCode));
		}
		return userDTOList;
	}

	@Secured
	@GetMapping(value = "id/{id}")
	public UsersDTO getUserById(@PathVariable("id") Long id,
			@RequestHeader(name = "Authorization", required = true) String token, @AuthenticationPrincipal UserPrincipal userPrincipal) {
		UsersDTO usersDTO = null;
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_BY_ID),
					String.format("Retrieving user details by Id %s", id));
			usersDTO = userBusinessService.getUserById(id, token , userPrincipal.getClientCode(), userPrincipal);
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_BY_ID),
					"Retrieved user details by Id");

		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_BY_ID), e,
					"Error occurred while getting user by Id");
		}
		return usersDTO;
	}

	@Secured
	@GetMapping(value = "costcenters")
	public List<CostCenterDTO> getCostCenters(@RequestHeader(name = "Authorization", required = true) String token,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		List<CostCenterDTO> costCenters = null;
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_BY_ID),
					"Retrieving all cost centers");
			if(null != userPrincipal && userPrincipal.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER)) {
				costCenters = userBusinessService.getAllCostCenters(token);
			}
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_BY_ID),
					"Retrieving all cost centers");

		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_BY_ID), e,
					"Error occurred while getting cost center details");
		}
		return costCenters;
	}

	/**
	 * Switch business api implementation
	 * 
	 * @param principal
	 * @param clientCode
	 * @param userType
	 * @return
	 */
	@Secured
	@GetMapping(value = "switchBusiness/client/{clientId}/userType/{userType}")
	public ParentDTO switchBusiness(@PathVariable("clientId") String clientId, @PathVariable("userType") String userType,
			@RequestHeader(name = "Authorization", required = true) String authToken, @AuthenticationPrincipal UserPrincipal principal
			, @RequestHeader(value = "user-key", required = false) String userKey) {
		ParentDTO userPrincipal = null;
		try {
			boolean mfaRequired = principal.getMfaAuthorizationRequired();

			if (mfaRequired) {

				List<String> ignoreUserTypes = Arrays.asList(GenericConstants.USERTYPE_VANCOUVER, GenericConstants.USERTYPE_BRANCH);
				
				String exten[] = bbsiHeadEmails.split(",");

				if (principal.getMfaAuthorizationRequired() && (ignoreUserTypes.contains(principal.getUserType())
						|| Utils.checkIfEmailHasExtension(principal.getEmail(),exten))) {

					mfaRequired = false;

				} else {
					if (StringUtils.isEmpty(userKey)) {
						mfaRequired = true;
					} else {
						String key = encryptAndDecryptUtil.decrypt(userKey, secret);

						String[] keyArray = key.split("<<>>");
						validateKeyLength(keyArray);

						String userName = keyArray[0];
						LocalDate date = LocalDate.parse(keyArray[1]);

						verifyUsernameAndKeyValidityDays(principal, userName, date);
						mfaRequired = false;
					}
				}

			}

			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SWITCH_BUSINESS),
					String.format("retriving user principal details based on client code: %s and userType %s : ",
							clientId, userType));

			// service call to get the user principal based on client code and user types
			userPrincipal = userBusinessService.switchBusiness(principal, clientId, userType);
			userBusinessService.updateIsPrimary(principal.getUserId(), userPrincipal.getUserClientId(), clientId, userPrincipal.getPrincipal().getUserType());

			if(GenericConstants.USERTYPE_CLIENT.equals(userPrincipal.getPrincipal().getUserType())) {
				userBusinessService.getClientNameByClientCode(clientId);
			}
			userBusinessService.updateCaliforniaFlag(principal);
			userPrincipal.getPrincipal().setMfaAuthorizationRequired(mfaRequired);

			// Change the Principal
			updatePrincipalObject(clientId, userPrincipal.getPrincipal().getUserType(), authToken, userPrincipal);
			userPrincipal.getPrincipal().setEmail(Base64.getEncoder().encodeToString(userPrincipal.getPrincipal().getEmail().getBytes()));
			userPrincipal.getPrincipal().setUserTypeMap(null);
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SWITCH_BUSINESS),
					String.format("retrived user principal details based on client code: %s and userType %s : ",
							clientId, userPrincipal.getPrincipal().getUserType()));
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SWITCH_BUSINESS),
					exception, String.format(
							"Error occurred while retriving client admin user list for clientcode is %s", clientId));
		}
		return userPrincipal;
	}


	private void verifyUsernameAndKeyValidityDays(UserPrincipal principal, String userName, LocalDate date) {
		if ((!principal.getEmail().equals(userName))
				&& date.plusDays(USER_KEY_VALIDITY_DAYS).isAfter(LocalDate.now())) {
			throw new UnauthorizedAccessException(
					new ErrorDetails(ErrorCodes.UA004, MethodNames.INVALID_MFA));
		}
	}

	private void validateKeyLength(String[] keyArray) {
		if (keyArray.length != 2)
			throw new UnauthorizedAccessException(
					new ErrorDetails(ErrorCodes.UA004, MethodNames.INVALID_MFA));
	}

	private void updatePrincipalObject(String clientId, String userType, String authToken, ParentDTO userPrincipal) {
		userPrincipal.getPrincipal().setToken(authToken.split(" ")[1]);
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (null != auth) {
				Authentication newAuth = new UsernamePasswordAuthenticationToken(userPrincipal.getPrincipal(),
						auth.getCredentials(), auth.getAuthorities());
				String tokenValue = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
				if (null != tokenValue) {
					OAuth2AccessToken token = tokenStore.readAccessToken(tokenValue);
					if (token != null && tokenStore.readAuthentication(token) != null) {
						OAuth2Authentication oauthAuthentication = new OAuth2Authentication(
								tokenStore.readAuthentication(token).getOAuth2Request(), newAuth);
						LogUtils.debugLog.accept(
								basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SWITCH_BUSINESS),
								String.format("Updated principal object for client code: %s and userType %s : ",
										clientId, userType));
						tokenStore.removeAccessToken(token);
						tokenStore.storeAccessToken(token, oauthAuthentication);
						SecurityContextHolder.getContext().setAuthentication(newAuth);
					}
				}
			}
		} catch (Exception exception) {
			LogUtils.errorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SWITCH_BUSINESS),
					ExceptionUtils.getRootCauseMessage(exception), exception);
		}
	}

	@Secured
	@GetMapping(value = "/policyaccepted/email/{email}")
	public PolicyAcceptedDTO getisPolicyAccepted(@PathVariable("email") String email) {
		PolicyAcceptedDTO policyAcceptedDTO = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_IS_POLICY_ACCEPTED),
					"Updating isPolicy Accepted");
			policyAcceptedDTO = userBusinessService.getisPolicyAccepted(email);
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_IS_POLICY_ACCEPTED),
					"Updating isPolicy Accepted successfully");

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_IS_POLICY_ACCEPTED), e,
					"Error occurred while Updating isPolicy Accepted");
		}
		return policyAcceptedDTO;
	}

	@DeleteMapping("/logout")
	public void revokeToken(@RequestHeader(name = "Authorization", required = true) String authToken) {
		userBusinessService.removeToken(tokenStore, true);
	}

	/**
	 * This API will provide the user list by client code and user type and email
	 * 
	 * @param clientCode
	 * @param type
	 * @param email
	 * @return
	 */
	@Secured
	@GetMapping(value = "/client/{clientCode}/type/{type}/email/{email}",produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UsersDTO> getUsersByClientCodeAndTypeAndCreatedBy(@PathVariable("clientCode") String clientCode,
			@PathVariable("type") String type, @PathVariable("email") String email) {
		List<UsersDTO> userDTOList = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_USERSBY_CLIENTCODE_AND_TYPE_AND_CREATEDBY),
					String.format("retriving user list by clientcode : %s user type : %s ", clientCode,
							type));

			// service call to get the client user list
			userDTOList = userBusinessService.getUsersByClientCodeAndTypeAndCreatedBy(clientCode, type, email);

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_USERSBY_CLIENTCODE_AND_TYPE_AND_CREATEDBY),
					String.format("retriving user list by clientcode : %s user type : %s ", clientCode,
							type));
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(
							getClass().getCanonicalName(), MethodNames.GET_USERSBY_CLIENTCODE_AND_TYPE_AND_CREATEDBY),
					exception,
					String.format(
							"Error occurred while retriving user list for clientcode : %s and usertype: %s and email : %s",
							clientCode, type, email));
		}
		if(null != userDTOList) {
			userDTOList.forEach(user->user.setEmail(Base64.getEncoder().encodeToString(user.getEmail().getBytes())));
		}
		return userDTOList;
	}

	@Secured
	@GetMapping(value = "/client/{clientCode}/payrollstatus/{status}",produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UsersDTO> getAllClientUsersByClientCodeAndPrivileges(@PathVariable("clientCode") String clientCode,
			@PathVariable("status") String payrollStatus) {
		List<UsersDTO> userDTOList = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_ALL_CLIENT_USERS_BY_CLIENTCODE),
					String.format("retriving client admin user list by clientcode %s : ", clientCode));

			// service call to get the client admin user list
			userDTOList = userBusinessService.getAllClientUsersByClientCodeAndPayRollStatus(clientCode, payrollStatus);

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_ALL_CLIENT_USERS_BY_CLIENTCODE),
					String.format("retrived client admin user list by clientcode %s : ", clientCode));
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_ALL_CLIENT_USERS_BY_CLIENTCODE),
					exception, String.format(
							"Error occurred while retriving client admin user list for clientcode is %s", clientCode));
		}
		return userDTOList;
	}


	@Secured
	@GetMapping(path = "update/newhire/{id}/{onboardEmail}")
	public void updateNewHireUser(@PathVariable("id") Long id, @PathVariable("onboardEmail") String onboardEmail,
			@RequestHeader(name = GenericConstants.MFA_EMAIL) String mfaEmail, @RequestHeader(name = GenericConstants.MOBILE) String mobile) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_NEWHIRE_USER_BY_ID),
					"update the new hire user");

			userBusinessService.updateNewHireUser(id, onboardEmail,mfaEmail, mobile);

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_NEWHIRE_USER_BY_ID),
					"updated the new hire user");
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_NEWHIRE_USER_BY_ID),
					exception, "Error occurred while updating the new hire user");
		}
	}

	@Secured
	@GetMapping(value = "/email/client/{clientCode}/type/{type}",produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,List<String>> getEmailsByClientCodeAndType(@PathVariable("clientCode") String clientCode,
																 @PathVariable("type") String userType) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_PRINCIPAL),
					String.format("Retrieving user details by client code %s", clientCode));
			return userBusinessService.getEmailsByClientCodeAndType(clientCode, userType);
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_PRINCIPAL),
					e, "Error occurred while getting user's emails");
		}
		return null;
	}

	@GetMapping(value = "/email/client/{clientCode}/type/{type}/integration/{enumtype}",produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,List<String>> getEmailsByClientCodeAndTypeAndEnum(@PathVariable("clientCode") String clientCode,
			@PathVariable("type") String userType, @PathVariable("enumtype") String enumType) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_EMAILS_BY_CLIENTCODE_TYPE_ENUM),
					String.format("Retrieving user details by client code %s", clientCode));
			return userBusinessService.getEmailsByClientCodeAndTypeAndEnum(clientCode, userType, enumType);
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_EMAILS_BY_CLIENTCODE_TYPE_ENUM), e,
					"Error occurred while getting user's emails info");
		}
		return null;
	}

	@Secured
	@GetMapping(value = "/user-exist/{clientCode}/{userEmail}")
	public UserExistDTO checkExistUser(@RequestHeader(name = "Authorization", required = true) String token,
			@PathVariable("clientCode") String clientCode, @PathVariable("userEmail") String userEmail) {
		UserExistDTO userExistDTO = null;

		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CHECK_EXIST_USER),
					"check Exist the User by email");
			byte[] decodedBytes = Base64.getDecoder().decode(userEmail);
			String decodedEmail = encryptAndDecryptUtil.decrypt(new String(decodedBytes));
			userExistDTO = userBusinessService.checkExistUser(clientCode, decodedEmail, token);

		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CHECK_EXIST_USER),
					e, "Error occurred while check Exist the User by email");
		}
		return userExistDTO;

	}

	/**
	 * API to retrieve the i9 approval list to sign the document.
	 * 
	 * @param clientCode
	 * @return
	 */
	@Secured
	@GetMapping(path = "i9ApprovalList/clientCode/{clientCode}")
	public List<UsersDTO> getI9ApprovalEmployeeList(@PathVariable("clientCode") String clientCode) {
		List<UsersDTO> userList = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_I9APPROVAL_EMPLOYEELIST),
					String.format("Get i9 approval employee list  for clientCode :: %s ", clientCode));
			// service call to the i9 approval list
			userList = userBusinessService.getI9ApprovalEmployeeList(clientCode);
			userList.forEach(user -> user.setEmail(Base64.getEncoder().encodeToString(user.getEmail().getBytes())));
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_I9APPROVAL_EMPLOYEELIST),
					String.format("retrieved i9 approval employee list  for clientCode :: %s ", clientCode));

		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_I9APPROVAL_EMPLOYEELIST),
					exception, String.format("Error occurred while getting the i9 approval list  for clientCode :: %s",
							clientCode));
		}
		
		return userList;
	}

	@Secured
	@PutMapping(value = "/policyaccepted")
	public void updateIsPolicyAccepted(@RequestBody PolicyAcceptedDTO policyAcceptedDTO,
			@AuthenticationPrincipal UserPrincipal principal, @RequestHeader(value = GenericConstants.AUTH_TOKEN_HDR, required = false) String authToken) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_IS_POLICY_ACCEPTED),
					"Updating isPolicy Accepted");
			userBusinessService.updateIsPolicyAccepted(policyAcceptedDTO, principal);
			setUserPrincipal(principal, authToken);
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_IS_POLICY_ACCEPTED),
					"Updating isPolicy Accepted successfully");

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_IS_POLICY_ACCEPTED), e,
					"Error occurred while Updating isPolicy Accepted");
		}
	}

	@Secured
	@PostMapping(path = "toolbar/settings")
	public void saveUserToolbarSettings(@AuthenticationPrincipal UserPrincipal principal,
			@RequestBody List<UserToolbarSettingsDTO> userToolbarSettingDTOs) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_USER_TOOLBAR_SETTINGS),
					"save user tool bar setting details");
			userBusinessService.saveUserToolbarSettings(principal, userToolbarSettingDTOs);
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_USER_TOOLBAR_SETTINGS),
					"save user tool bar setting details");
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_USER_TOOLBAR_SETTINGS),
					exception, "Error occurred while saving user tool bar setting details");
		}
	}

	@Secured
	@GetMapping(value = "/filter/{key}/{value}")
	public void addUserFilter(@PathVariable("key") String key, 
			@PathVariable("value") String value, @AuthenticationPrincipal UserPrincipal principal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.USER_CLIENT_FILTER),
					"Add user client filter");
			if (null != principal 
					&& StringUtils.isNotEmpty(principal.getUserType())
					&& (principal.getUserType().equals(GenericConstants.USERTYPE_CLIENT)
							|| principal.getUserType().equals(GenericConstants.USERTYPE_EXTERNAL))) {
				userBusinessService.addSecurityEntry(principal, key, value);
			}
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.USER_CLIENT_FILTER),
					e, "Error occurred while adding user filter");
		}
	}
	
	@Secured
	@GetMapping(value = "/filter/client/{clientCode}/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Set<String>> getUserClientFilter(@PathVariable("clientCode") String clientCode,
			@PathVariable("email") String email, @AuthenticationPrincipal UserPrincipal principal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.USER_CLIENT_FILTER),
					"Fetch user client filter");
			if (null != principal 
					&& StringUtils.isNotEmpty(principal.getUserType())
					&& (principal.getUserType().equals(GenericConstants.USERTYPE_CLIENT)
							|| principal.getUserType().equals(GenericConstants.USERTYPE_EXTERNAL))) {
				return userBusinessService.getUserFilter(email, clientCode, principal);
			} else {
				return Maps.newHashMap();
			}
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.USER_CLIENT_FILTER),
					e, "Error occurred while fetching user filter");
		}
		return null;
	}
	
	@Secured
	@GetMapping(value = "/filter/email/{email}",produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Map<String, Set<String>>> getUserFilter(@PathVariable("email") String email,
			@AuthenticationPrincipal UserPrincipal principal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.USER_CLIENT_FILTER),
					"Fetch user client filter");
			if (null != principal 
					&& StringUtils.isNotEmpty(principal.getUserType())
					&& (principal.getUserType().equals(GenericConstants.USERTYPE_CLIENT)
							|| principal.getUserType().equals(GenericConstants.USERTYPE_EXTERNAL))) {
				return userBusinessService.getUserFilter(email);
			} else {
				return Maps.newHashMap();
			}
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.USER_CLIENT_FILTER),
					e, "Error occurred while fetching user filter");
		}
		return null;
	}

	/**
	 * API to get the email based on the client code and employee code
	 * 
	 * @param clientCode
	 * @param empCode
	 * @return
	 */
	@Secured
	@GetMapping(value = "/email/client/{clientCode}/employee/{empCode}",produces = MediaType.APPLICATION_JSON_VALUE)
	public String getEmailByClientCodeAndEmployeeCode(@PathVariable("clientCode") String clientCode,
			@PathVariable("empCode") String empCode) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_EMAILBY_CLIENTCODE_AND_EMPLOYEECODE),
					String.format("Retrieving user details by client code %s and employee code", clientCode));
			// service call
			return userBusinessService.getEmailByClientCodeAndEmployeeCode(clientCode, empCode);
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_EMAILBY_CLIENTCODE_AND_EMPLOYEECODE),
					e, String.format("Error Occured while Retrieving user details by client code %s and employee code",
							clientCode));
		}
		return null;
	}

	@Secured
	@GetMapping(value = "/disable-user/{clientCode}/{newHireId}",produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean disableUser(@PathVariable("clientCode") String clientCode,
			@PathVariable("newHireId") long newHireId) {
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.DISABLE_USER),
					"Disable the User by NewHireId");
			return userBusinessService.disableUser(clientCode, newHireId);

		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.DISABLE_USER), e,
					"Error occurred while Disable the User by NewHireId");
		}
		return false;
	}

	@Secured
	@DeleteMapping(path = "delete/newhire/{id}")
	public void deleteNewHireUser(@PathVariable("id") Long id) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.DELETE_NEWHIRE_USER_BY_ID),
					"deleting the new hire user");

			userBusinessService.deleteNewHireUser(id);

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.DELETE_NEWHIRE_USER_BY_ID),
					"deleted the new hire user");
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.DELETE_NEWHIRE_USER_BY_ID),
					exception, "Error occurred while deleting the new hire user");
		}
	}

	@Secured
	@PutMapping(path = "/update",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String updateUserDetails(@RequestBody CustomPersonalDTO customPersonalDTO,
			@RequestHeader(name = "Authorization", required = true) String token,@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER),
					"Updating user details");
			if(Utils.isValidClientCode(userPrincipal, customPersonalDTO.getClientCode())) {
				throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA001, UNAUTHORIZED_ACCESS));
			}
			userBusinessService.updateUserDetails(customPersonalDTO, token);
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER),
					"User updated successfully");

		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER), e,
					"Error occurred while updating user");
		}
		return "";
	}

	/**
	 * Method is to update status of the user when employee status gets updated.
	 */
	@Secured
	@GetMapping(path = "updatestatus/client/{clientCode}/employee/{employeeCode}/status/{status}",produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean updateUserStatus(@PathVariable("clientCode") String clientCode,
			@PathVariable("employeeCode") String employeeCode, @PathVariable("status") Boolean status) {
		boolean isStatusUpdated = false;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_STATUS), String.format(
							"Update user status for employee code: %s and clientCode: %s", employeeCode, clientCode));
			isStatusUpdated = userBusinessService.updateUserStatus(clientCode, employeeCode, status);
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_STATUS), String.format(
							"Updated user status for employee code: %s and clientCode: %s", employeeCode, clientCode));
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_STATUS),
					exception,
					String.format("Error occurred while updating user status for employee: %s and clientCode: %s",
							employeeCode, clientCode));
		}
		return isStatusUpdated;
	}

	/**
	 * update the i_9 details in the user table.
	 * 
	 * @param userId
	 */
	@Secured
	@PutMapping(path = "update/i9ApprovalDetails/userId/{userId}/employeeCode/{empCode}/client/{clientCode}",consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateI9ApprovalDetails(@PathVariable("userId") long userId,
			@PathVariable("empCode") String empCode, @PathVariable("clientCode") String clientCode) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_I9APPROVAL_DETAILS),
					String.format("update i9 approval employee details  for user id with ::%s and clientCode :: %s ",
							userId, clientCode));
			if(StringUtils.isNotEmpty(empCode) && empCode.equals(GenericConstants.NOT_APPLICABLE)) {
				empCode = null;
			}
			// service call to update the i9 approval details.
			userBusinessService.updateI9ApprovalDetails(userId, empCode, clientCode);

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_I9APPROVAL_DETAILS),
					String.format("updated i9 approval employee details  for user id with ::%s and clientCode :: %s ",
							userId, clientCode));
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_I9APPROVAL_EMPLOYEELIST),
					exception,
					String.format(
							"Error occured while updating the  i9 approval employee details  for user id  ::%s and clientCode :: %s ",
							userId, clientCode));
		}
	}

	@Secured
	@GetMapping(value = "/personaldetails/client/{clientCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Set<String>> getEmployeePersonaDetailsByClientCodeAndEmployeeCode(
			@PathVariable("clientCode") String clientCode, @RequestParam(value = "employeeCode",required = false) String employeeCode,
			@RequestHeader(value = GenericConstants.AUTH_TOKEN_HDR,required = true) String token,@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_EMAILBY_CLIENTCODE_AND_EMPLOYEECODE),
					String.format("Retrieving user details by client code %s and employee code %s", clientCode,
							employeeCode));
			// service call
			return userBusinessService.getPersonalInfoByClientCodeAndEmployeeCode(clientCode, employeeCode, token,
					userPrincipal.getEmail(), userPrincipal.getUserId());

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(),
							MethodNames.GET_EMAILBY_CLIENTCODE_AND_EMPLOYEECODE),
					e, String.format("Error Occured while Retrieving user details by client code %s and employee code %s",
							clientCode, employeeCode));
		}
		return null;
	}

	/**
	 * Method for updating user email.
	 * 
	 * @param token
	 * @param updateEmail
	 * @return
	 */
	@Secured
	@PutMapping(value = "/changemail", produces = MediaType.APPLICATION_JSON_VALUE)
	public CustomPersonalDTO updateEmail(@RequestHeader(name = "Authorization", required = true) String token,
			@RequestBody CustomPersonalDTO emailDto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
		CustomPersonalDTO customDTO = new CustomPersonalDTO();
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_EMAIL),
					"Updating user email");
			emailDto.setIsAccountLoginChange(Boolean.TRUE);
			if(StringUtils.isEmpty(emailDto.getEmployeeCode())){
				if((!userPrincipal.getEmail().equals(emailDto.getOldEmail()))) {
					throw new UnauthorizedAccessException(
							new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
				}
				emailDto.setOldEmail(userPrincipal.getEmail());	
				emailDto.setIsAccountLoginChange(Boolean.FALSE);
			}
			emailDto = userBusinessService.updateEmail(emailDto, token, userPrincipal);
			customDTO.setMfaEmail(emailDto.getMfaEmail());
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_EMAIL),
					"Email Information updated successfully");

		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_EMAIL), e,
					"Error occurred while updating user email Information");
		}

		return customDTO;
	}

	/**
	 * Method for sending credentials to Users
	 * 
	 * @param token
	 * @param userInvitationList
	 * @return Returns the {@link UsersDTO}
	 */
	@Secured
	@PostMapping("/client/{clientCode}/{base64ClientName}")
	public List<UserInvitationDTO> sendCredentialsToEmployees(
			@RequestHeader(name = "Authorization", required = true) String token,
			@RequestBody List<UserInvitationDTO> userInvitationList,
			@PathVariable("clientCode") String clientCode,
			@PathVariable("base64ClientName") String base64ClientName,
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@RequestParam(name = "uiUrl", required = false) String uiUrl) {

		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_CREDENTIALS_TO_EMPLOYEES),
					String.format("Sending Credentilas to the user list %s", userInvitationList));

			userBusinessService.createUserAndSendInvitation(userInvitationList,token,userPrincipal,clientCode,base64ClientName,baseUrl + GenericConstants.LOGIN_PATH);

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_CREDENTIALS_TO_EMPLOYEES),
					String.format("Sent Credentilas to the user list %s", userInvitationList));
		} catch (BbsiException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_CREDENTIALS_TO_EMPLOYEES), e,
					"Error occured while sending credentials to user Details");
		}

		return userInvitationList;
	}

	@Secured
	@PutMapping(path = "update/timeNetDetails/client/{clientCode}",consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateTimeNetDetails(@RequestBody TimenetLoginInfo timenetLoginInfo,@PathVariable("clientCode") String clientCode,@RequestHeader(name = "Authorization", required = true) String token,@AuthenticationPrincipal UserPrincipal principal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_TIMENET_DETAILS),
					String.format("update timenet login info by userid::%s and client code :: %s",
							principal.getUserId(), clientCode));
			//service call to update the timenetlogin details
			userBusinessService.updateTimenetLoginInfo(timenetLoginInfo, clientCode,principal);

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_TIMENET_DETAILS),
					String.format("updated timenet login info by userid::%s  and client code :: %s ",
							principal.getUserId(),clientCode));
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_TIMENET_DETAILS),
					exception,
					String.format(
							"Error occured while updating the timenet login info by userid::%s  and client code :: %s",
							principal.getUserId(),clientCode));
		}
	}
	
	@Secured
	@GetMapping(path = "timeNetDetails/client/{clientCode}")
	public TimenetLoginInfo getTimeNetDetails(@PathVariable("clientCode") String clientCode,@RequestHeader(name = "Authorization", required = true) String token,@AuthenticationPrincipal UserPrincipal principal) {
		TimenetLoginInfo timenetLoginInfo=null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_TIMENET_DETAILS),
					String.format("get timenet login info by userid::%s and client code :: %s",
							principal.getUserId(), clientCode));
			//service call to get timenetlogin details
			timenetLoginInfo=userBusinessService.getTimenetLoginInfo(clientCode,principal);

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_TIMENET_DETAILS),
					String.format("get timenet login info by userid::%s  and client code :: %s ",
							principal.getUserId(),clientCode));
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_TIMENET_DETAILS),
					exception,
					String.format(
							"Error occured while getting the timenet login info by userid::%s  and client code :: %s",
							principal.getUserId(),clientCode));
		}
		return timenetLoginInfo;
	}


	@Secured
	@GetMapping(path = "/terminated/clients")
	public ResponseEntity<Map<String, String>> getTerminatedClientsOfBranch(@AuthenticationPrincipal UserPrincipal principal, 
			@RequestHeader(name = "Authorization", required = true) String token) {
		Map<String, String> clientList = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_TERMINATED_CLIENTS_OF_BRANCH),
					String.format("Retrieve terminated clients of branch user's id::%s",
							principal.getUserId()));
			List<String> ignoreUserTypes = Arrays.asList(GenericConstants.USERTYPE_VANCOUVER, GenericConstants.USERTYPE_BRANCH);
			if(ignoreUserTypes.contains(principal.getUserType())) {
				clientList = userBusinessService.getTerminatedClients(principal.getUserId(), token);
			}

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_TERMINATED_CLIENTS_OF_BRANCH),
					String.format("Retrieved terminated clients of branch user's id::%s",
							principal.getUserId()));
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_TERMINATED_CLIENTS_OF_BRANCH),
					exception,
					String.format(
							"Error occurred while getting terminated clients of branch user's id::%s",
							principal.getUserId()));
		}
		return new ResponseEntity<Map<String, String>>(clientList, HttpStatus.OK);
	}
	
	@GetMapping(path = "update/access/newclients")
	public void updatePortalAccessForNewClients(@AuthenticationPrincipal UserPrincipal principal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_PORTAL_ACCESS_FOR_NEW_CLIENTS),
					"update Portal Access For New Clients ");

			if(null != principal && principal.getEmail().equals(GenericConstants.INTEGRATION_ADMIN)) {
				userBusinessService.updatePortalAccessForNewClients();
			}

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_PORTAL_ACCESS_FOR_NEW_CLIENTS),
					"updated Portal Access For New Clients");
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_PORTAL_ACCESS_FOR_NEW_CLIENTS), exception,
					"Error occured while updating  Portal Access For NewClients");
		}
		return;
	}
	
	@GetMapping(path = "update/clientdtls")
	public void updateClientMaster(@AuthenticationPrincipal UserPrincipal principal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_CLIENT_MASTER_DETAILS),
					"update client details");
			if(null != principal && principal.getEmail().equals(GenericConstants.INTEGRATION_ADMIN)) {
				userBusinessService.updateClientMaster();
			}
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_CLIENT_MASTER_DETAILS),
					"updated client details");
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_CLIENT_MASTER_DETAILS), exception,
					"Error occured while updating client details");
		}
		return;
	}
	
	@Secured
	//Dont change the path param code to clientcode it is the costcenter code
	@GetMapping(path = "costcenter/prismized/clients/{code}")
	public List<CostCenterClientDTO> getPrismizedClientsByCostCenterCode(
			@RequestHeader(name = "Authorization", required = true) String token,
			@PathVariable("code") String code, @AuthenticationPrincipal UserPrincipal principal) {
		List<CostCenterClientDTO> costcenterClientList = null;
		
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_COSTCENTER_PRISMIZED_CLIENTS),
					String.format("Get Prismized Clients of costcenter code::%s ",code));
			if(null != principal && (principal.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER)
					|| (principal.getUserType().equals(GenericConstants.USERTYPE_BRANCH) 
							&& (StringUtils.isEmpty(code) || (StringUtils.isNotEmpty(code) && code.equals(principal.getCostCenterCode())))))) {
				costcenterClientList = userBusinessService.getCostCenterPrismizedClients(code);
			}

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_COSTCENTER_PRISMIZED_CLIENTS),
					String.format("Retrived Prismized Clients of costcenter code::%s ",code));
		} catch (Exception exception) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_COSTCENTER_PRISMIZED_CLIENTS), exception,
					"Error occured while Get Prismized Clients of costcenter code");
		}
		return costcenterClientList;
	}
	
	
	@GetMapping(path = "get/distinct/clients",produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getdistinctClients(@AuthenticationPrincipal UserPrincipal principal) {
		List<String> clients = null;

		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_DISTINCT_CLIENTS),
					"Get distinct Clients ");

			if(null != principal && principal.getEmail().equals(GenericConstants.INTEGRATION_ADMIN)) {
				clients = userBusinessService.getdistinctClients();
			}

			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_DISTINCT_CLIENTS),
					"Retrived distinct Clients ");
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_DISTINCT_CLIENTS),
					exception, "Error occured while Getting distinct Clients ");
		}
		return clients;
	}
	
	private void buildWarnings(UsersDTO userDTO) {
		if (CollectionUtils.isNotEmpty(userDTO.getNonPrismizedCostCenters())) {
			StringBuilder sb = new StringBuilder();
			// Appends cost centers one by one
			for (String str : userDTO.getNonPrismizedCostCenters()) {
				sb.append(str);
			}
			sb.append(" " + " These Cost Centers doesn't have any Prismized clients");
			throw new ValidationException(new ErrorDetails(ErrorCodes.UR0016, sb.toString()));
		}
	}
	
	/**
	 * API to create the employee status from terminated to new-hire [re-hire] functionality
	 * 
	 * @param token
	 * @param uiUrl
	 * @param userDTO
	 * @param userPrincipal
	 * @return
	 */
	@Secured
	@PostMapping(path="/create/rehire/user",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public UsersDTO createRehireUser(@RequestHeader(name = "Authorization", required = true) String token,
			@RequestParam(name = "uiUrl", required = false) String uiUrl, @RequestBody UsersDTO userDTO,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_USER),
					String.format("Creating rehire user with details %s", userDTO));

			uiUrl = baseUrl + GenericConstants.LOGIN_PATH;
			userDTO = userBusinessService.createRehireUser(userDTO, token, uiUrl, userPrincipal);

			buildWarnings(userDTO);
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_USER),
					String.format("Created rehire user with details %s", userDTO));
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CREATE_USER), e,
					"Error occured while creating user Details");
		}
		return userDTO;
		}

	@GetMapping(value = "/prechks")
	public Map<String, String> preChecks(@AuthenticationPrincipal UserPrincipal principal,
										@RequestHeader(value = "user-key", required = false) String userKey) {
		Map<String, String> checksMap = Maps.newHashMap();
		List<String> ignoreUserTypes = Arrays.asList(GenericConstants.USERTYPE_VANCOUVER, GenericConstants.USERTYPE_BRANCH);
		String exten[] = bbsiHeadEmails.split(",");

		checkMFA(checksMap, exten, ignoreUserTypes, principal,userKey);
        checksMap.put("isPolicyAccepted", principal.getIsPolicyAccepted() ? "Y" : "N");
        checksMap.put("isPolicyUpdated", principal.getIsPolicyUpdated() ? "Y" : "N");
        checksMap.put("isAdUser", Utils.checkIfEmailHasExtension(principal.getEmail(), exten) ? "Y" : "N");
        checksMap.put("isCCPARequired", principal.getIsCcpaRequired() ? "Y" : "N");
        userBusinessService.updateCaliforniaFlag(principal);
		return checksMap;
	}

	private void checkMFA(Map<String, String> checksMap, String[] exten, List<String> ignoreUserTypes, UserPrincipal principal, String userKey) {
		if (principal.getMfaAuthorizationRequired()
				&& (ignoreUserTypes.contains(principal.getUserType())
				|| Utils.checkIfEmailHasExtension(principal.getEmail(), exten))) {
			checksMap.put("mfa", "N");
		} else if (principal.getMfaAuthorizationRequired()) {
			if (StringUtils.isEmpty(userKey)) {
				checksMap.put("mfa", "Y");
			} else {
				String key = encryptAndDecryptUtil.decrypt(userKey, secret);

				String[] keyArray = key.split("<<>>");
				if (keyArray.length != 2)
					throw new UnauthorizedAccessException(
							new ErrorDetails(ErrorCodes.UA004, MethodNames.INVALID_MFA));

				String userName = keyArray[0];
				LocalDate date = LocalDate.parse(keyArray[1]);

				if ((!principal.getEmail().equals(userName))
						&& date.plusDays(USER_KEY_VALIDITY_DAYS).isAfter(LocalDate.now())) {
					throw new UnauthorizedAccessException(
							new ErrorDetails(ErrorCodes.UA004, MethodNames.INVALID_MFA));
				}
				checksMap.put("mfa", "N");
			}
		}
	}

	@Secured
	@PutMapping(value = "/ccpaaccepted/{isaccepted}")
	public void updateIsCCPAAccepted(@PathVariable("isaccepted") boolean isaccepted,
									 @AuthenticationPrincipal UserPrincipal principal,
									 @RequestHeader(value = GenericConstants.AUTH_TOKEN_HDR, required = false) String authToken) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_IS_CCPA_ACCEPTED),
					"Updating CCPA Policy Accepted");
			userBusinessService.updateCCPAPolicyAccepted(isaccepted, principal);
			setUserPrincipal(principal, authToken);
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_IS_CCPA_ACCEPTED),
					"Updating CCPA policy Accepted successfully");

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_IS_CCPA_ACCEPTED), e,
					"Error occurred while Updating CCPA policy Accepted");
		}
	}
	
	
	@Secured
	@PutMapping(value = "/ccpaupdated/client/{clientCode}/employee/{empCode}/isCCPAUpdated/{isCCPAUpdated}",consumes = MediaType.APPLICATION_JSON_VALUE)
	public void  isCCPAUpdated(@PathVariable("clientCode") String clientCode,@PathVariable("empCode") String empCode, @PathVariable("isCCPAUpdated") Boolean isCCPAUpdated,
									 @AuthenticationPrincipal UserPrincipal principal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.IS_CCPA_UPDATED),
					"Updating CCPA Policy Updated");
			userBusinessService.isCCPAUpdated(clientCode,empCode,isCCPAUpdated,principal);
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.IS_CCPA_UPDATED),
					"Updating CCPA policy Updated successfully");

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.IS_CCPA_UPDATED), e,
					"Error occurred while Updating CCPA policy pdated");
		}
	}
	
	@Secured
	@GetMapping(value = "/employee/{empCode}/{firstName}/{lastName}")
	public void sendEmailNotificationsForSchoolDistrict(@PathVariable("empCode") String empCode,@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName,
									 @AuthenticationPrincipal UserPrincipal principal) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_EMAIL_NOTIFICATIONS_FOR_SCHOOLDISTRICT),
					"Sending Email NotificationsForSchoolDistict");
			userBusinessService.sendEmailNotificationsForSchoolDistrict(empCode,firstName,lastName,principal);
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_EMAIL_NOTIFICATIONS_FOR_SCHOOLDISTRICT),
					"Sent Email NotificationsForSchoolDistict");

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_EMAIL_NOTIFICATIONS_FOR_SCHOOLDISTRICT), e,
					"Error occurred while Sending Email NotificationsForSchoolDistict");
		}
	}
	
	@Secured
	@PostMapping(value="/reinitiate",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void reInitiateNewHire(@RequestHeader(name = "Authorization", required = true) String token,
			@RequestParam(name = "uiUrl", required = false) String uiUrl, @RequestBody UsersDTO userDTO,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.REINITIATE_NEWHIRE),
					String.format("Reinitiate newhire with details: %s", userDTO));

			userBusinessService.reInitiateNewHire(userDTO, token, baseUrl + GenericConstants.LOGIN_PATH, userPrincipal);

			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.REINITIATE_NEWHIRE),
					String.format("Reinitiated newhire with details: %s", userDTO));
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.REINITIATE_NEWHIRE), e,
					"Error occured while reinitiating newhire Details");
		}
	}
	
	@PostMapping(value="/authenticateuser",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String authenticateUser(@RequestHeader(name = "Authorization", required = true) String token, @RequestBody UserCredentials userCredentials) {
		String authRes=null;
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.AUTHENTICATE_USER),
					"Start");

			authRes= userBusinessService.authenticateUser(userCredentials, token);

			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.AUTHENTICATE_USER),
					"End");
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.AUTHENTICATE_USER), e,
					"Error occured while Authenticate user");
		}
		return authRes;
	}
	
	@Secured
	@GetMapping(value="/enddate/client/{clientCode}/employee/{empCode}")
	public String getEmployeeEndDate(@PathVariable("clientCode") String clientCode, @PathVariable("empCode") String empCode) {
		try {
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_EMPLOYEE_ENDDATE),
					"Start");
		return userBusinessService.getEmployeeEndDate(clientCode, empCode);
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_EMPLOYEE_ENDDATE), e,
					"Error occured while get user end date");
		}
		return null;
	}
	
	@Secured
	@PutMapping(value = "/client/{clientCode}/employee/{empCode}/enddate/{enddate}",consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateEmployeeEndDate(@PathVariable("clientCode") String clientCode,
			@PathVariable("empCode") String empCode, @PathVariable("enddate") String enddate) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_EMPLOYEE_ENDDATE), "Start");
			userBusinessService.updateEmployeeEndDate(clientCode, empCode,enddate);
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_EMPLOYEE_ENDDATE),
					e, "Error occured while Update user end date");
		}
		
	}
	
	@Secured
	@PostMapping(value = "/bulkupload/mfaemail/validate", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BulkUploadMailDTO> validateMfaEmails(@RequestBody List<BulkUploadMailDTO> emailList,
																 @AuthenticationPrincipal UserPrincipal userPrincipal) {
		List<BulkUploadMailDTO> validatedEmailList = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.VALIDATE_MFA_EMAIL_FOR_BULK_UPLOAD), "Start");
			validatedEmailList = userBusinessService.validateMfaEmails(emailList, userPrincipal);
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.VALIDATE_MFA_EMAIL_FOR_BULK_UPLOAD),
					e, "Error occured while validating emails for Bulk Upload");
		}
		return validatedEmailList;
	}

	@GetMapping(value = "/email/client/mfa",produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Set<String>> getMfaDetails(@RequestHeader(value = "uid") String uid) {
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_MFA_DETAILS),
					String.format("Retrieving MFA details"));
			return userBusinessService.getMfaDetails(uid);
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_MFA_DETAILS),
					e, "Error occurred while getting MFA details");
		}
		return null;
	}
}
