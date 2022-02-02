/**
 * 
 */
package com.bbsi.platform.user.web.resource.v1;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.annotation.Secured;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.LogUtils;
import com.bbsi.platform.common.user.dto.v2.PasswordDTOV2;
import com.bbsi.platform.exception.business.ValidationException;
import com.bbsi.platform.exception.enums.ErrorCodes;
import com.bbsi.platform.exception.model.ErrorDetails;
import com.bbsi.platform.user.businessservice.intf.PasswordBusinessService;
import com.bbsi.platform.user.utils.MethodNames;
import com.bbsi.platform.user.utils.Utils;

/**
 * @author rneelati
 *
 */
@RestController
@RequestMapping("/password")
@CrossOrigin
@Logging
public class PasswordResource {
	
	@Autowired
	private PasswordBusinessService passwordBusinessService;
	
	@Autowired
	private TokenStore tokenStore;

	@Value("${base.url}")
	private String baseUrl;
	
	@Value("${bbsi.head.emails}")
	private String bbsiHeadEmails;
	
	/**
	 * submitEmailForResetPassword method is invoked upon valid data and generates a JWT token to reset the password. 
	 * This token is send along with the email link.
	 * 
	 * @param uiUrl
	 * @param PasswordDTOV2
	 * @return PasswordDTOV2
	 */
	@GetMapping(path = "forgot/email/submit", produces = MediaType.APPLICATION_JSON_VALUE)
	public PasswordDTOV2 submitEmailForResetPassword(@RequestHeader(value = "user-id") String email, @RequestParam(name = "uiUrl", required = false) String uiUrl) {
		PasswordDTOV2 passwordDTOV2Response = null;
		try {
            LogUtils.debugLog.accept(
                    basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SUBMIT_EMAIL_FOR_RESET_PASSWRD),
                    String.format("Creating JWT Authententication Token for email: %s", email));
			uiUrl = baseUrl + GenericConstants.FORGOT_PWD_PATH;
            passwordDTOV2Response = passwordBusinessService.submitEmailForResetPassword(email, uiUrl);
            if(passwordDTOV2Response != null) {
            	LogUtils.debugLog.accept(
                        basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SUBMIT_EMAIL_FOR_RESET_PASSWRD),
                        String.format("Token created successfully for email: %s", passwordDTOV2Response.getEmail()));
            }
            

        } catch (Exception e) {
            handleException.accept(
                    basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SUBMIT_EMAIL_FOR_RESET_PASSWRD), e,
                    "Error occurred while submitting email in reset password");
        }
		return passwordDTOV2Response;
	}
	
	/**
	 * submitEmailForResetPassword method is invoked upon valid data and generates a JWT token to reset the password. 
	 * This token is send along with the email link.
	 * 
	 * @param uiUrl
	 * @param PasswordDTOV2
	 * @return PasswordDTOV2
	 */
	@GetMapping(path = "forgot/email/send", produces = MediaType.APPLICATION_JSON_VALUE)
	public void sendEmailForResetPassword( 
			@RequestParam(name = "uiUrl", required = false) String uiUrl,
			@RequestHeader(value = "user-id") String email) {
		try {
			if(email.isEmpty()) {
				throw new ValidationException(new ErrorDetails(ErrorCodes.V001,"email cant be null"));
			}
            LogUtils.debugLog.accept(
                    basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_EMAIL_FOR_RESET_PASSWRD),
                    String.format("Sending password reset email to: %s", email));
			uiUrl = baseUrl + GenericConstants.FORGOT_PWD_PATH;
			
            passwordBusinessService.sendEmailForResetPassword(email, uiUrl);
		} catch(ValidationException e) {
			throw e;
		} catch (Exception e) {
            handleException.accept(
                    basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_EMAIL_FOR_RESET_PASSWRD), e,
                    "Error occurred while creating Token");
        }
	}
	
	/**
	 * saveForgotPassword method persists the new password details with the updated password
	 * 
	 * return PasswordDTOV2
	 */
	@GetMapping(path = "forgot/save", produces = MediaType.APPLICATION_JSON_VALUE)
	public PasswordDTOV2 saveForgotPassword(@RequestHeader(value = "password") String password, @RequestHeader(value = "uid") String uid) {
		PasswordDTOV2 passwordDTOV2Response = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD),
					"SAVE_FORGOT_PASSWORD api method is invoked");
            passwordDTOV2Response = passwordBusinessService.saveForgotPassword(password, uid);
            passwordBusinessService.removeToken(tokenStore, false);
            LogUtils.debugLog.accept(
                    basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD),
                    String.format("Password reset successfully for email:: %s", passwordDTOV2Response.getEmail()));
            setPasswordDetails(passwordDTOV2Response);
			
        } catch (Exception e) {
            handleException.accept(
                    basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD), e,
                    "Error occurred while saving the data");
        }
		return passwordDTOV2Response;
	}

	private void setPasswordDetails(PasswordDTOV2 passwordDTOV2) {
		passwordDTOV2.setConfirmPassword(null);
		passwordDTOV2.setPassword(null);
		passwordDTOV2.setNewPassword(null);
		passwordDTOV2.setEmail(Base64.getEncoder().encodeToString(passwordDTOV2.getEmail().getBytes()));
	}
	
	/**
	* changePassword method validates the given details and persist the new details.
	* return PasswordDTOV2	
	*/
	@PostMapping(path = "change", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PasswordDTOV2 changePassword(@RequestBody PasswordDTOV2 passwordDTOV2, @RequestHeader(name = "Authorization", required = true) String token,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		PasswordDTOV2 passwordDTOV2Response = null;
		try {
			passwordDTOV2.setEmail(userPrincipal.getEmail());
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CHANGE_PASSWRD),
					String.format("CHANGE_PASSWORD api method is invoked for email:: %s", passwordDTOV2.getEmail()));
			String uiUrl = baseUrl + GenericConstants.LOGIN_PATH;
            passwordDTOV2Response = passwordBusinessService.changePassword(passwordDTOV2, token,userPrincipal,uiUrl);
            passwordBusinessService.removeToken(tokenStore, false);
            LogUtils.debugLog.accept(
                    basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CHANGE_PASSWRD),
                    String.format("Password changed  successfully : email :: %s", passwordDTOV2Response.getEmail()));
            setPasswordDetails(passwordDTOV2);

        } catch (Exception e) {
            handleException.accept(
                    basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CHANGE_PASSWRD), e,
                    "Error occurred in changepassword api");
        }
		return passwordDTOV2Response;
	}
	
	/**
	* sendFlagValueForExpiredPassword method validates the given details and persist the new details.
	* return PasswordDTOV2	
	*/
	@Secured
	@PostMapping(path = "change/expired", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public PasswordDTOV2 sendFlagValueForExpiredPassword(@RequestBody Map<String,String> emailMap,@AuthenticationPrincipal UserPrincipal principal) {
		PasswordDTOV2 passwordDTOV2Response = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_FLAG_VALUE_EXPIRED_PASSWRD),
					String.format("SEND_FLAG_VALUE_EXPIRED_PASSWORD api method is invoked for email:: %s", emailMap.get(GenericConstants.MEMBER_INFO_EMAIL)));
			if(emailMap.isEmpty() || !emailMap.containsKey(GenericConstants.MEMBER_INFO_EMAIL)) {
				throw new ValidationException(new ErrorDetails(ErrorCodes.V001,"email cant be null"));
			}
			passwordDTOV2Response = passwordBusinessService.sendFlagValueForExpiredPassword(emailMap.get(GenericConstants.MEMBER_INFO_EMAIL),principal);
			String exten[] = bbsiHeadEmails.split(",");
			if(null != passwordDTOV2Response && 
					(Utils.checkIfEmailHasExtension(principal.getEmail(), exten) 
							|| principal.getEmail().equals(GenericConstants.SUPER_ADMIN)
							|| principal.getEmail().equals(GenericConstants.INTEGRATION_ADMIN))) {
				passwordDTOV2Response.setPasswordExpired(false);
				LogUtils.debugLog.accept(
                    basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_FLAG_VALUE_EXPIRED_PASSWRD),
                    String.format("Password Expired details for : email :: %s", passwordDTOV2Response.getEmail()));
			}
			 

        } catch (Exception e) {
            handleException.accept(
                    basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_FLAG_VALUE_EXPIRED_PASSWRD), e,
                    "Error occurred in SEND_FLAG_VALUE_EXPIRED_PASSWORD api");
        }
		if(null != passwordDTOV2Response) {
			passwordDTOV2Response.setEmail(Base64.getEncoder().encodeToString(passwordDTOV2Response.getEmail().getBytes()));
		}
		return passwordDTOV2Response;
	}
	

	/**
	* sendFlagValueForExpiredPassword method validates the given details and persist the new details.
	* return PasswordDTOV2	
	*/
	@PostMapping(path = "forgot/token/expired", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public PasswordDTOV2 validateJwtToken(@RequestBody PasswordDTOV2 passwordDTOV2) {
		PasswordDTOV2 passwordDTOV2Response = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.VALIDATE_JWT_TOKEN),
					"VALIDATE_JWT_TOKEN api method is invoked to validate jwt token");
            passwordDTOV2Response = passwordBusinessService.validateJwtToken(passwordDTOV2);
            LogUtils.debugLog.accept(
                    basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.VALIDATE_JWT_TOKEN),
                    "VALIDATE_JWT_TOKEN api method is invoked to validate jwt token");

        } catch (Exception e) {
            handleException.accept(
                    basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.VALIDATE_JWT_TOKEN), e,
                    "Error occurred while validating jwt token");
        }
		return passwordDTOV2Response;
	}
	
	
	@GetMapping(path = "sendNotification/expired/passwords", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> sendNotificationsForExpiredPassword(@AuthenticationPrincipal UserPrincipal userPrincipal,
			@RequestHeader(name = "Authorization", required = true) String token) {
		List<String> notifications = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_FLAG_VALUE_EXPIRED_PASSWRD),
					"sendNotificationsForExpiredPassword");

			String uiUrl = baseUrl + GenericConstants.LOGIN_PATH;
			
			if (null != userPrincipal && userPrincipal.getEmail().equals(GenericConstants.INTEGRATION_ADMIN)) {
				notifications = passwordBusinessService.sendNotificationsForExpiredPassword(userPrincipal,uiUrl,token);
			}

			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_FLAG_VALUE_EXPIRED_PASSWRD),
					"Password Expired details");

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_FLAG_VALUE_EXPIRED_PASSWRD),
					e, "Error occurred in SEND_FLAG_VALUE_EXPIRED_PASSWORD api");
		}
		return notifications;

	}
}
