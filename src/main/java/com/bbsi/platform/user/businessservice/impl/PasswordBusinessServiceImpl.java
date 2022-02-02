/**
 * 
 */
package com.bbsi.platform.user.businessservice.impl;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.io.Serializable;
import java.net.URI;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.dto.Email;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.enums.Enums.AuditDetailsEventEnum;
import com.bbsi.platform.common.enums.Enums.AuditDetailsSourceEntityEnum;
import com.bbsi.platform.common.enums.NotificationEventEnum;
import com.bbsi.platform.common.generic.CommonUtilities;
import com.bbsi.platform.common.generic.EncryptAndDecryptUtil;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.GenericUtils;
import com.bbsi.platform.common.generic.JwtTokenUtil;
import com.bbsi.platform.common.generic.LogUtils;
import com.bbsi.platform.common.generic.WebClientTemplate;
import com.bbsi.platform.common.user.dto.v2.PasswordDTOV2;
import com.bbsi.platform.exception.business.UnauthorizedAccessException;
import com.bbsi.platform.exception.business.ValidationException;
import com.bbsi.platform.exception.enums.ErrorCodes;
import com.bbsi.platform.exception.model.ErrorDetails;
import com.bbsi.platform.user.businessservice.intf.PasswordBusinessService;
import com.bbsi.platform.user.model.ResetPasswordRequest;
import com.bbsi.platform.user.model.UserClients;
import com.bbsi.platform.user.model.Users;
import com.bbsi.platform.user.model.UsersPasswords;
import com.bbsi.platform.user.repository.ResetPasswordRequestRepository;
import com.bbsi.platform.user.repository.UsersPasswordsRepository;
import com.bbsi.platform.user.repository.UsersRepository;
import com.bbsi.platform.user.utils.MethodNames;
import com.bbsi.platform.user.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

import io.jsonwebtoken.ExpiredJwtException;
import ua_parser.Client;
import ua_parser.Parser;

/**
 * @author rneelati
 *
 */
@Logging
@Service
public class PasswordBusinessServiceImpl implements PasswordBusinessService {
	
	private static final String RESET_PASSWRD_EMAIL_TEMPLATE_NAME = "reset_password_submit";
	private static final String RESET_PASSWRD_SUCCESS_TEMPLATE_NAME = "reset_password_success";
	private static final String CHANGE_PASSWRD_SUCCESS_TEMPLATE_NAME = "change_password_success";
	private static final String PASSWRD_EXPIRATION_TEMPLATE_NAME = "password_expiration";
	private static final String PASSWRD_EXPIRATION_ZERODAY_TEMPLATE_NAME = "password_expired_zeroday";
	private static final String PASSWRD_EXPIRATION_SUBJECT = "Password Expiration Soon";
	private static final String PASSWRD_EXPIRATION_ZERODAY_SUBJECT = "Change your Password Today";
	
	private static final String RESET_PASSWRD_EMAIL_SUBJECT = "Forgot Password";
	private static final String RESET_PASSWRD_SUCCESS_SUBJECT = "Your Password is reset successfully";
	private static final String CHANGE_PASSWRD_SUBJECT = "Your Password is changed successfully";
	private static final Integer PASSOWORD_INDEX = 1;
	private static final String NEW_HIRE = "NewHire";
	
	private static final String FORMAT_SS= "%s %s";

	@Autowired
	private UsersRepository userRepository;
	
	@Autowired
	private UsersPasswordsRepository usersPasswordsRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private WebClientTemplate webClientTemplate;

	@Value("${notification.email.url}")
	private String emailURL;

	@Value("${notification.email.forgot.password.url}")
	private String emailForgotPasswordURL;
	
	@Value("${password.expiration.days}")
	private Integer passwordExpirationDays;

	@Value("${employee.max-passwords}")
	private Integer maxPasswords;
	
	@Value("${bbsi.head.emails}")
	private String bbsiHeadEmails;

	@Resource(name = "customEncoder")
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CommonUtilities commonUtilities;
	
	@Autowired
	private EncryptAndDecryptUtil encryptAndDecryptUtil;
	
	@Autowired
	private ResetPasswordRequestRepository resetPasswordRequestRepository;
	
	@Value("${reset.password.captcha.request.duration}")
	private long resetPasswordCaptchaRequestDuration;
	
	@Value("${captcha.secret.key}")
	private String captchaSecret;
	
	private static final String INVALID_CAPTCHA = "Invalid CAPTCHA!!";
	
	protected void setEmailForgotPasswordURL(String emailForgotPasswordURL) {
		this.emailForgotPasswordURL = emailForgotPasswordURL;
	}

	protected void setMaxPassword(Integer maxPasswords) {
		this.maxPasswords = maxPasswords;
	}

	private UserDetails getByEmail(Users userData) throws UsernameNotFoundException {
		User user = new User(userData.getEmail(), "", new ArrayList<>());
		return user;
	}

	private Users findByEmailOrUsername(String email) {
		Users userData = userRepository.findByEmail(email);
		if (userData == null) {
			return null;
		}
		return userData;
	}

	/**
	 * submitEmailForResetPassword method is used to generates JWTToken and encloses
	 * the token in an email with UI redirect url.
	 * 
	 * return PasswordDTOV2
	 */
	@Override
	@Transactional
	public PasswordDTOV2 submitEmailForResetPassword(String email, String uiUrl) {
		PasswordDTOV2 passwordDTOV2Response = null;
		try {
			email = encryptAndDecryptUtil.decrypt(email);
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SUBMIT_EMAIL_FOR_RESET_PASSWRD),
					String.format("Submit email for the respective email :: %s ", email));
			Users userData = findByEmailOrUsername(email);
			// If user doesn' have any active client, we should not allow the user to reset his password.
			if (userData != null) {
				Object resetPasswordRequestCount=0L;
				synchronized (this) {
					if(resetPasswordCaptchaRequestDuration>0) {
						LocalDateTime dateTime=LocalDateTime.now(ZoneOffset.systemDefault());
						resetPasswordRequestCount=resetPasswordRequestRepository.findByUserEmailAndSubmittedOnRange(userData.getId(),dateTime.minusMinutes(resetPasswordCaptchaRequestDuration),dateTime.plusMinutes(1L));
					}
					else {
						resetPasswordRequestCount=resetPasswordRequestRepository.findByUserEmail(userData.getId());
					}
					
					validateRequestBasedOnCount(resetPasswordRequestCount);
					ResetPasswordRequest resetPasswordReq= new ResetPasswordRequest();
					resetPasswordReq.setUserId(userData.getId());
					resetPasswordReq.setSubmittedOn(LocalDateTime.now(ZoneOffset.systemDefault()));
					resetPasswordRequestRepository.save(resetPasswordReq);
				}
				
				if (CollectionUtils.isNotEmpty(userData.getClients())) {
					boolean clientIsActive = false;
					for (UserClients client : userData.getClients()) {
						if (client.getIsActive()) {
							clientIsActive = true;
							break;
						}
					}
					if (!clientIsActive) {
						return passwordDTOV2Response;
					}
				}

				UserDetails userDetails = getByEmail(userData);
				
				if(null == userData.getTokenValue() || !jwtTokenUtil.validateToken(userData.getTokenValue(),userDetails)){
					String jwtTokenData = jwtTokenUtil.generateToken(userDetails, false, 14400);
					// update the Jwt Token with in DB
					userData.setTokenValue(jwtTokenData);
					userRepository.save(userData);
				}
				passwordDTOV2Response = constructResponse();
				commonUtilities.saveAudit(passwordDTOV2Response, AuditDetailsSourceEntityEnum.UPDATE_PASSWORD, AuditDetailsEventEnum.UPDATE);
			} else {
				LogUtils.basicErrorLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(),
								MethodNames.SUBMIT_EMAIL_FOR_RESET_PASSWRD),
						String.format("Users_Passwords entity do not contains records for Email : %s",
								email));
			}
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SUBMIT_EMAIL_FOR_RESET_PASSWRD),
					e, "Error occured generation of JWT token");
		}
		return passwordDTOV2Response;
	}
	private void captchaValidation(String response) {
		try {
			URI verifyUri = URI.create(String.format(
					"https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
					captchaSecret, response, "127.0.0.1"));

			String googleResponse = GenericUtils.getRestTemplate(10000).getForObject(verifyUri, String.class);
			ObjectMapper mapper = GenericUtils.getObjectMapperInstance.get();
			JsonNode jsonNode = mapper.readTree(googleResponse);

			if (null == jsonNode.get("success") || Boolean.FALSE.equals(jsonNode.get("success").asBoolean())) {
				throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA004, INVALID_CAPTCHA));
			}
		} catch(Exception e) {
			throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA004, INVALID_CAPTCHA));
		}
	}

	
	public String getUserToken(String email) {
		String jwtTokenData = null;
		Users userData = findByEmailOrUsername(email);
		if (userData != null) {
			UserDetails userDetails = getByEmail(userData);
			jwtTokenData = jwtTokenUtil.generateToken(userDetails, true, 0);
		}
		return jwtTokenData;
	}
	
	
	/**
	 * submitEmailForResetPassword method is used to generates JWTToken and encloses
	 * the token in an email with UI redirect url.
	 * 
	 * return PasswordDTOV2
	 */
	@Override
	@Transactional
	public void sendEmailForResetPassword(String userEmail, String uiUrl) {
		try {
			Map<String, String> headers = new HashMap<>();
			
				userEmail = encryptAndDecryptUtil.decrypt(userEmail);
				Users userData = findByEmailOrUsername(userEmail);
				if (userData != null && CollectionUtils.isNotEmpty(userData.getClients())) {
					Object resetPasswordRequestCount=0L;
					synchronized (this) {
						if(resetPasswordCaptchaRequestDuration>0) {
							LocalDateTime dateTime=LocalDateTime.now(ZoneOffset.systemDefault());
							resetPasswordRequestCount=resetPasswordRequestRepository.findByUserEmailAndRequestedOnRange(userData.getId(),dateTime.minusMinutes(resetPasswordCaptchaRequestDuration),dateTime.plusMinutes(1L));
						}
						else {
							resetPasswordRequestCount=resetPasswordRequestRepository.findByUserEmail(userData.getId());
						}
						validateRequestBasedOnCount(resetPasswordRequestCount);
						ResetPasswordRequest resetPasswordReq= new ResetPasswordRequest();
						resetPasswordReq.setUserId(userData.getId());
						resetPasswordReq.setRequestedOn(LocalDateTime.now(ZoneOffset.systemDefault()));
						resetPasswordRequestRepository.save(resetPasswordReq);
					}
					resetPasswordRequestRepository.deleteSubmittedCallRecords(userData.getId());
					
					String jwtTokenData = userData.getTokenValue();
					if(StringUtils.isNotEmpty(jwtTokenData)) {
						String redirectUrl="email=" + userEmail + "&token=" + jwtTokenData;
						redirectUrl=encryptAndDecryptUtil.encrypt(redirectUrl);
						redirectUrl= new String (Base64.getEncoder().encode(redirectUrl.getBytes()));
						uiUrl = uiUrl + "?qs="+redirectUrl;
						String firstName = userData.getFirstName();
						String lastName = userData.getLastName();
						if(CollectionUtils.isNotEmpty(userData.getClients())) {
							firstName = userData.getClients().get(0).getFirstName();
							lastName = userData.getClients().get(0).getLastName();
						}
						Email email = buildEmail(userEmail, firstName, lastName,
								RESET_PASSWRD_EMAIL_TEMPLATE_NAME, jwtTokenData, RESET_PASSWRD_EMAIL_SUBJECT, uiUrl,"","");
		
						webClientTemplate.postForObjectMono(emailForgotPasswordURL, Email.class, email, headers)
								.subscribe(emailResponse -> {
									LogUtils.debugLog.accept(
											basicMethodInfo.apply(getClass().getCanonicalName(),
													MethodNames.SUBMIT_EMAIL_FOR_RESET_PASSWRD),
											String.format("Email status: %s", emailResponse));
								});
					}
					else {
						LogUtils.basicErrorLog.accept(
								basicMethodInfo.apply(getClass().getCanonicalName(),
										MethodNames.SUBMIT_EMAIL_FOR_RESET_PASSWRD),
								String.format("Token is not available for Email : %s",
										userEmail));
					}
					
				} else {
					LogUtils.basicErrorLog.accept(
							basicMethodInfo.apply(getClass().getCanonicalName(),
									MethodNames.SUBMIT_EMAIL_FOR_RESET_PASSWRD),
							String.format("Users_Passwords entity do not contains records for Email : %s",
									userEmail));
				}
		} catch(ValidationException e) {
			throw e;
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_EMAIL_FOR_RESET_PASSWRD),
					e, "Error occured while sending email");
		}
	}

	private void validateRequestBasedOnCount(Object resetPasswordRequestCount) {
		if(null!=resetPasswordRequestCount && Long.parseLong(resetPasswordRequestCount.toString())>=3) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String response = request.getHeader("cresponse");
			if(StringUtils.isNotEmpty(response)) {
				captchaValidation(response);
			}
			else {
				throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA004, "Unauthorized Access - Provide a valid captcha"));
			}
		}
	}

	private Email buildEmail(String toAddress, String firstName, String lastName,String templateName, String jwt_token, String subject,
			String link,String numberOfDaysForPasswordExpiry,String passwordExpirationDate) {
		Map<String, String> contextMap = new HashMap<String, String>();

		Email email = new Email();
		email.setToAddress(toAddress);
		email.setSubject(subject);
		email.setTemplateName(templateName);
		email.setContext(""); // What is the use of this?
		contextMap.put("link", link);
		contextMap.put("first_name", firstName);
		contextMap.put("last_name", lastName);
		contextMap.put("name", String.format(FORMAT_SS, firstName, lastName));
		contextMap.put("jwt_token", jwt_token);
		contextMap.put("days_count", numberOfDaysForPasswordExpiry);
		contextMap.put("expiration_date", passwordExpirationDate);
		email.setContextMap(contextMap);
		return email;
	}

	private PasswordDTOV2 constructResponse() {

		PasswordDTOV2 passwordDTOV2 = new PasswordDTOV2();
		passwordDTOV2.setDescription("Reset Password Email sent successfully ");
		
		return passwordDTOV2;
	}

	/**
	 * saveForgotPassword method persists the new password details
	 * 
	 * return PasswordDTOV2
	 */
	@Transactional
	@Override
	public PasswordDTOV2 saveForgotPassword(String password, String uid) {
		List<UsersPasswords> usersPasswordsV2SaveList = new ArrayList<>();
		List<UsersPasswords> usersPasswordsV2List = new ArrayList<>();
		PasswordDTOV2 passwordDTOV2 = new PasswordDTOV2();
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD),
					"saveForgotPassword api method is invoked");
			
			checkPasswordGotExpiredValidation(uid);
			uid = encryptAndDecryptUtil.decrypt(uid);
			String userName = jwtTokenUtil.getUsernameFromToken(uid);
			String newPassword = encryptAndDecryptUtil.decrypt(password);
			passwordDTOV2.setEmail(userName);
			passwordDTOV2.setToken(uid);
			Users users = userRepository.findByEmail(passwordDTOV2.getEmail());
			
			checkUserEmailNotFoundOrPasswordGotExpiredValidation(users, passwordDTOV2);
			
			// get the new password and persist/ update into the Users entity with new
			// password.
			String existingPasswordFromDB = users.getPassword();

				List<UsersPasswords> userPasswordsList = usersPasswordsRepository.findByUserId(users.getId());
				passwordDTOV2.setPassword(passwordEncoder.encode(newPassword));

				if (CollectionUtils.isEmpty(userPasswordsList)) {
					LogUtils.debugLog.accept(
							basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD),
							String.format("Users_Passwords entity do not contains records for UserID: %s",
									users.getId()));
				}
				// list is empty and then check whether the given new password is same then
				// throw error
				// OR convert the new password as encrypted one and pass to compare with the
				// users passwords list

				checkUserNewPasswordShouldNotMatchWithPreviousPasswordValidation(newPassword, existingPasswordFromDB, userPasswordsList);

				users.setPassword(passwordDTOV2.getPassword());
				users.setInvalidAttempts(0);

				// set empty value to token as the user already used this token
				users.setTokenValue(null);
				users.setIsFirstLogin(Boolean.FALSE);
				userRepository.save(users);

				if (CollectionUtils.isEmpty(userPasswordsList) || userPasswordsList.size() < maxPasswords) {
					// add the new object to the existing list
					usersPasswordsV2List.add(buildUsersPasswordsV2(users.getId(),users.getDefaultClient(), existingPasswordFromDB));
					if (CollectionUtils.isNotEmpty(userPasswordsList)) {
						usersPasswordsV2List.addAll(userPasswordsList);
					}
				} else {
					userPasswordsList = removeWhenSizeIsGreateEqualMaxPasswords(userPasswordsList);
					// insert new object to the existing list
					usersPasswordsV2List.add(buildUsersPasswordsV2(users.getId(),users.getDefaultClient(), existingPasswordFromDB));
					usersPasswordsV2List.addAll(userPasswordsList);
				}

				for (UsersPasswords userPwds : usersPasswordsV2List) {
					userPwds.setPasswordIndex(userPwds.getPasswordIndex() + 1);
					usersPasswordsV2SaveList.add(userPwds);
				}
				usersPasswordsRepository.saveAll(usersPasswordsV2SaveList);
				resetPasswordRequestRepository.deleteByUserId(users.getId());
				LogUtils.debugLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD),
						"New Passwords is saved successfully.");
				// reset password has no login and hence empty token value
				sendEmail(users, passwordDTOV2.getEmail(), RESET_PASSWRD_SUCCESS_TEMPLATE_NAME,
						RESET_PASSWRD_SUCCESS_SUBJECT, MethodNames.SAVE_FORGOT_PASSWRD, "","","","");
				
				commonUtilities.saveAudit(users, AuditDetailsSourceEntityEnum.UPDATE_PASSWORD, AuditDetailsEventEnum.UPDATE);


		} catch (ExpiredJwtException e1) {
				LogUtils.basicErrorLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD), MethodNames.INVALID_TOKEN_ERROR);
				throw new ValidationException(new ErrorDetails(ErrorCodes.USR003));
		} catch(Exception e2) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD), e2,
					"Error occurred while saving the password details");
		}
		return passwordDTOV2;
	}
	
	private void checkPasswordGotExpiredValidation(String uid) {
		if (isTokenNull(uid)) {
			LogUtils.basicErrorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD), MethodNames.INVALID_TOKEN_ERROR);
			throw new ValidationException(new ErrorDetails(ErrorCodes.USR003));
		}
	}
	
	private void checkUserEmailNotFoundOrPasswordGotExpiredValidation(Users users,PasswordDTOV2 passwordDTOV2) {
		if (null == users) {
			LogUtils.basicErrorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD),
					String.format("User email is invalid or do not exist::  %s", passwordDTOV2.getEmail()));
			throw new ValidationException(new ErrorDetails(ErrorCodes.USR001));
		} else if ((!isTokenValid(passwordDTOV2.getToken(), users))
				|| jwtTokenUtil.isTokenExpired(passwordDTOV2.getToken())) {
			LogUtils.basicErrorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD), MethodNames.INVALID_TOKEN_ERROR);
			throw new ValidationException(new ErrorDetails(ErrorCodes.USR003));
		}
	}
	
	private void checkUserNewPasswordShouldNotMatchWithPreviousPasswordValidation(String newPassword, String existingPasswordFromDB,List<UsersPasswords> userPasswordsList) {
		if (passwordEncoder.matches(newPassword, existingPasswordFromDB)
				|| (isNewPasswordPreviouslyUsed(newPassword, userPasswordsList))) {
			LogUtils.basicErrorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD),
					"New password should not match with the previously used 24 passwords.");
			throw new ValidationException(new ErrorDetails(ErrorCodes.PWD004));
		}
	}

	private void sendEmail(Users users, String emailOrUsername, String templateName, String subject, String methodName,
			String token,String uiUrl,String numberOfDaysForPasswordExpiry,String passwordExpirationDate) {
		// build email: email, firstName, templateName, token, subject, link
		String firstName = users.getFirstName();
		String lastName = users.getLastName();
		if(CollectionUtils.isNotEmpty(users.getClients())) {
			firstName = users.getClients().get(0).getFirstName();
			lastName = users.getClients().get(0).getLastName();
		}
		Email email = buildEmail(emailOrUsername, firstName, lastName, templateName, "", subject, uiUrl,
				numberOfDaysForPasswordExpiry,passwordExpirationDate);
		Map<String, String> headers = new HashMap<>();
		headers.put(GenericConstants.AUTH_TOKEN_HDR, token);
		if (StringUtils.isNotBlank(token)) {
			webClientTemplate.postForObjectMono(emailURL, Email.class, email, headers).subscribe(emailResponse -> {
				LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), methodName),
						String.format("Email status:: %s", emailResponse));
			});
		} else {
			// Reset/Forgot Password action is performed before login, token is null or not
			// required.
			webClientTemplate.postForObjectMono(emailForgotPasswordURL, Email.class, email, Collections.emptyMap())
					.subscribe(emailResponse -> {
						LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), methodName),
								String.format("Email status : %s", emailResponse));
					});
		}
	}

	/* buildUsersPasswordsV2 builds a new object for persistence */
	private UsersPasswords buildUsersPasswordsV2(long userId, String clientCode, String existingPasswordFromDB) {
		UsersPasswords usersPasswordsV2 = new UsersPasswords();
		usersPasswordsV2.setClientCode(clientCode);
		usersPasswordsV2.setPassword(existingPasswordFromDB);
		usersPasswordsV2.setUserId(userId);
		usersPasswordsV2.setPasswordIndex(0);
		return usersPasswordsV2;
	}

	public Boolean isNewPasswordPreviouslyUsed(String newPassword,
			List<UsersPasswords> userPasswordsList) {
		if (CollectionUtils.isNotEmpty(userPasswordsList)) {
			for (UsersPasswords usersPasswords : userPasswordsList) {
				if (passwordEncoder.matches(newPassword, usersPasswords.getPassword())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * changePassword method validates the given details and persist the new
	 * details. return PasswordDTOV2
	 */
	@Transactional
	@Override
	public PasswordDTOV2 changePassword(PasswordDTOV2 passwordDTOV2, String authToken,UserPrincipal userPrincipal,String uiUrl) {
		List<UsersPasswords> usersPasswordsV2SaveList = new ArrayList<>();
		List<UsersPasswords> usersPasswordsV2List = new ArrayList<>();
		try {
			passwordDTOV2.setEmail(userPrincipal.getEmail());
			LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CHANGE_PASSWRD),
					String.format("changePassword api method is invoked for this email :: %s",
							passwordDTOV2.getEmail()));
			Users users = userRepository.findByEmail(passwordDTOV2.getEmail());

			checkUserEmailNotFoundValidation(users, passwordDTOV2);
			
			String oldPassword = encryptAndDecryptUtil.decrypt(passwordDTOV2.getPassword());
			String newPassword = encryptAndDecryptUtil.decrypt(passwordDTOV2.getNewPassword());
			// get the new password and persist/ update into the Users entity with new
			// password.
			String existingPasswordFromDB = users.getPassword();
		
			checkPasswordNotMatchingWithCurrentPasswordValidation(oldPassword, existingPasswordFromDB);

				List<UsersPasswords> userPasswordsList = usersPasswordsRepository.findByUserId(users.getId());
				passwordDTOV2.setNewPassword(passwordEncoder.encode(newPassword));
				if (CollectionUtils.isEmpty(userPasswordsList)) {
					LogUtils.debugLog.accept(
							basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CHANGE_PASSWRD),
							String.format("Users_Passwords entity do not contains records for UserID: %s",
									users.getId()));
				}
				// list is empty and then check whether the given new password is same then
				// throw error
				// OR convert the new password as encrypted one and pass to compare with the
				// users passwords list
				
				checkUserNewPasswordShouldNotMatchWithPreviousPasswordValidation(newPassword, existingPasswordFromDB, userPasswordsList);
			
				users.setPassword(passwordDTOV2.getNewPassword());
				users.setInvalidAttempts(0);
				users.setIsFirstLogin(Boolean.FALSE);
				userRepository.save(users);
				if (CollectionUtils.isEmpty(userPasswordsList) || userPasswordsList.size() < maxPasswords) {
					// add the new object to the existing list
					usersPasswordsV2List.add(buildUsersPasswordsV2(users.getId(),userPrincipal.getClientCode(), existingPasswordFromDB));
					if (CollectionUtils.isNotEmpty(userPasswordsList)) {
						usersPasswordsV2List.addAll(userPasswordsList);
					}
				} else {
					userPasswordsList = removeWhenSizeIsGreateEqualMaxPasswords(userPasswordsList);
					// insert new object to the existing list
					usersPasswordsV2List.add(buildUsersPasswordsV2(users.getId(), userPrincipal.getClientCode(), existingPasswordFromDB));
					usersPasswordsV2List.addAll(userPasswordsList);
				}
				buildUsersPasswordsV2SaveList(usersPasswordsV2SaveList, usersPasswordsV2List);

				usersPasswordsRepository.saveAll(usersPasswordsV2SaveList);
				LogUtils.debugLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CHANGE_PASSWRD),
						"New Password is changed/updated successfully.");

				sendEmail(users, passwordDTOV2.getEmail(), CHANGE_PASSWRD_SUCCESS_TEMPLATE_NAME,
						CHANGE_PASSWRD_SUBJECT, MethodNames.CHANGE_PASSWRD, authToken,uiUrl,"","");
				
				if(!CollectionUtils.isEmpty(users.getClients()) && users.getClients().get(0).getUserType().equalsIgnoreCase(NEW_HIRE)){
					Map<String, String> contextMap = new HashMap<>();
					contextMap.put("employee_name", users.getClients().get(0).getFirstName());
					contextMap.put("name", users.getClients().get(0).getFirstName());
					commonUtilities.buildEventToPublish(userPrincipal, NotificationEventEnum.NEWHIRE_ONBOARDING_STARTED, contextMap);
				}
				
				commonUtilities.saveAudit(users, AuditDetailsSourceEntityEnum.UPDATE_PASSWORD, AuditDetailsEventEnum.UPDATE, userPrincipal.getClientCode());

		} catch (Exception e) {
			e.printStackTrace();
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CHANGE_PASSWRD), e,
					"Error occurred while changing the password details");
		}
		return passwordDTOV2;
	}

	private void buildUsersPasswordsV2SaveList(List<UsersPasswords> usersPasswordsV2SaveList,
			List<UsersPasswords> usersPasswordsV2List) {
		for (UsersPasswords userPwds : usersPasswordsV2List) {
			if (userPwds.getPasswordIndex() < maxPasswords) {
				userPwds.setPasswordIndex(userPwds.getPasswordIndex() + 1);
				usersPasswordsV2SaveList.add(userPwds);
			}
		}
	}
	
	private void checkUserEmailNotFoundValidation(Users users,PasswordDTOV2 passwordDTOV2) {
		if (null == users) {
			LogUtils.basicErrorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CHANGE_PASSWRD),
					String.format("User email is invalid or do not exist :  %s", passwordDTOV2.getEmail()));
			throw new ValidationException(new ErrorDetails(ErrorCodes.USR001));
		}
	}
	
	private void checkPasswordNotMatchingWithCurrentPasswordValidation(String oldPassword, String existingPasswordFromDB) {
		if (!passwordEncoder.matches(oldPassword, existingPasswordFromDB)) {
			LogUtils.basicErrorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CHANGE_PASSWRD),
					"Password is not matching with the Current Password.");
			throw new ValidationException(new ErrorDetails(ErrorCodes.PWD005));
		}
	}

	

	private boolean isTokenValid(String token, Users users) {
		String tokenValueFromDB = users.getTokenValue();
		if (!StringUtils.isEmpty(tokenValueFromDB) && tokenValueFromDB.equals(token)) {
			return true;
		}
		return false;
	}

	private boolean isTokenNull(String token) {
		return StringUtils.isEmpty(token) ? true : false;
	}

	/**
	 * sendFlagValueForExpiredPassword method gets the details of the expired user
	 * password with respective to the current date
	 * 
	 * return PasswordDTOV2
	 */
	@Override
	@Transactional(readOnly = true)
	public PasswordDTOV2 sendFlagValueForExpiredPassword(String email,UserPrincipal principal) {
		PasswordDTOV2 passwordDTOV2Response = null;
		try {
			byte[] decodedArray = org.apache.commons.codec.binary.Base64.decodeBase64(email);
			String decodedEmail = new String(decodedArray);
			
			if (null == principal || !principal.getEmail().equalsIgnoreCase(decodedEmail)) {
				throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA001, "Unauthorized access!!"));
			}
			
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_FLAG_VALUE_EXPIRED_PASSWRD),
					String.format("SEND_FLAG_VALUE_EXPIRED_PASSWORD api method is invoked with email :: %s", decodedEmail));
			passwordDTOV2Response = new PasswordDTOV2();
			LocalDateTime modifiedOn = null;
			Object[] user = userRepository.findCreatedDateByUserId(principal.getUserId());
			if (null != user && user.length > 0) {
				Timestamp userCreatedOn = (Timestamp) user[0];
				if(null == userCreatedOn) {
					userCreatedOn = new Timestamp(System.currentTimeMillis());
				}
				passwordDTOV2Response.setEmail(decodedEmail);
				passwordDTOV2Response.setUserId(principal.getUserId());
				List<UsersPasswords> userPasswordsV2 = usersPasswordsRepository.findByUserIdAndPasswordIndex(principal.getUserId(), PASSOWORD_INDEX);
				modifiedOn = (CollectionUtils.isNotEmpty(userPasswordsV2) && userPasswordsV2.size() == 1 && null != userPasswordsV2.get(0).getModifiedOn())
						? userPasswordsV2.get(0).getModifiedOn()
						: userCreatedOn.toLocalDateTime();
						
				LocalDate modifiedOnDate = modifiedOn.toLocalDate();
				LocalDate passwordExpiredDate = modifiedOnDate.plusDays(passwordExpirationDays);

				long days_count = ChronoUnit.DAYS.between(LocalDate.now(), passwordExpiredDate);
				if (days_count < 0) {
					passwordDTOV2Response.setPasswordExpired(true);
				}
			} else {
				LogUtils.basicErrorLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(),
								MethodNames.SEND_FLAG_VALUE_EXPIRED_PASSWRD),
						String.format("User email is invalid or do not exist ::  %s", decodedEmail));
				throw new ValidationException(new ErrorDetails(ErrorCodes.USR001));
			}

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SEND_FLAG_VALUE_EXPIRED_PASSWRD),
					e, "Error occurred while retrieving  the users or users passwords expired data details");
		}
		return passwordDTOV2Response;
	}

	/**
	 * validateJwtToken method verifies for the Jwt Token validity and if token got
	 * expired it throws error.
	 * 
	 * return passwordDTOV2
	 */
	@Override
	public PasswordDTOV2 validateJwtToken(PasswordDTOV2 passwordDTOV2) {

		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.VALIDATE_JWT_TOKEN),
					"VALIDATE_JWT_TOKEN api method is invoked with ");
			if (StringUtils.isEmpty(passwordDTOV2.getToken()) || isTokenAlreadyUsed(passwordDTOV2.getToken())
					|| jwtTokenUtil.isTokenExpired(passwordDTOV2.getToken())) {
				LogUtils.basicErrorLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.VALIDATE_JWT_TOKEN),
						"JWT Token Expired or  Token is used already. Please reset your password again.");
				throw new ValidationException(new ErrorDetails(ErrorCodes.USR003));
			}
		} catch (ExpiredJwtException e1) {
				LogUtils.basicErrorLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.SAVE_FORGOT_PASSWRD), MethodNames.INVALID_TOKEN_ERROR);
				throw new ValidationException(new ErrorDetails(ErrorCodes.USR003));
		} catch(Exception e2) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.VALIDATE_JWT_TOKEN),
					e2, " verifies for the Jwt Token validity and if token got expired it throws error.");
		}
		return passwordDTOV2;
	}

	private boolean isTokenAlreadyUsed(String token) {
		try {
			String userName = jwtTokenUtil.getUsernameFromToken(token);
			Users users = userRepository.findByEmail(userName);
			// if token is already used, users.getTokenValue() == null
			if (null == users || StringUtils.isEmpty(users.getTokenValue()) || !token.equals(users.getTokenValue())) {
				return true;
			}
		} catch (ExpiredJwtException e1) {
				LogUtils.basicErrorLog.accept(
						basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.VALIDATE_JWT_TOKEN), MethodNames.INVALID_TOKEN_ERROR);
				throw new ValidationException(new ErrorDetails(ErrorCodes.USR003));
		} catch(Exception e2) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.VALIDATE_JWT_TOKEN),
					e2, " verifies for the Jwt Token validity and if token got expired it throws error.");
		}
		return false;
	}

	public List<UsersPasswords> removeWhenSizeIsGreateEqualMaxPasswords(List<UsersPasswords> userPasswordsList) {
		Iterator<UsersPasswords> userPwdsIterator = userPasswordsList.iterator();
		List<Long> removeIds = new ArrayList<>();
		while (userPwdsIterator.hasNext()) {
			UsersPasswords userPasswordsV2 = userPwdsIterator.next();
			if (userPasswordsV2.getPasswordIndex() >= maxPasswords) {
				// add the ids to the list when passwordIndex >= maxPasswords
				removeIds.add(userPasswordsV2.getId());
				// remove the entry from the list when passwordIndex= maxPasswords
				userPwdsIterator.remove();
			}
		}
		if (CollectionUtils.isNotEmpty(removeIds)) {
			// to allow new entry delete the existing record from DB when passwordIndex >=
			// maxPasswords
			usersPasswordsRepository.deleteByIdIn(removeIds);
			usersPasswordsRepository.flush();
		}
		return userPasswordsList;
	}
	
	
	@Override
	public List<String> sendNotificationsForExpiredPassword(UserPrincipal userPrincipal,String uiUrl,String token) {
		List<Object[]> userDeatils = null;
		List<Users> users= new ArrayList<>();
		List<String> notifications = new ArrayList<>();
		
		try {
			userDeatils = userRepository.getUserIdsAndcreatedOn();
			if (CollectionUtils.isNotEmpty(userDeatils)) {
				
				buildUsersListForNotifications(userDeatils, users);
				
				if (CollectionUtils.isNotEmpty(users)) {
					String exten[] = bbsiHeadEmails.split(",");
					users.forEach(user -> {
						if(!Utils.checkIfEmailHasExtension(user.getEmail(), exten)) {
							String message = sendNotificationsToEmails(userPrincipal, user, uiUrl, token);
							if (null != message) {
								notifications.add(message);
							}
						}
					});
				}
			}
		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), "sendNotificationsForExpiredPassword"), e,
					"Error occurred while retrieving  the users or users passwords expired data details");
		}
		return notifications;

	}

	private void buildUsersListForNotifications(List<Object[]> userDeatils, List<Users> users) {
		Set<Long> userSet = Sets.newHashSet();
		for (Object[] userObj : userDeatils) {
			if(!userSet.contains(Long.valueOf(userObj[0]==null?"":userObj[0].toString()))) {
				List<UserClients> userClientList = new ArrayList<>();
				Users user = new Users();
				UserClients userClient = new UserClients();
				setUserIdForBuildUsersListForNotifications(userObj, user);
				user.setCreatedOn(getLocalDateTime(userObj[1]==null?"":userObj[1].toString()));
				setFirstNameForBuildUsersListForNotifications(userObj, userClient);
				setLastNameForBuildUsersListForNotifications(userObj, userClient);
				user.setEmail(userObj[4]==null?"":userObj[4].toString());
				userClientList.add(userClient);
				user.setClients(userClientList);
				users.add(user);
				userSet.add(user.getId());
			}
		}
	}

	private void setLastNameForBuildUsersListForNotifications(Object[] userObj, UserClients userClient) {
		userClient.setLastName(userObj[3]==null?"":userObj[3].toString());
	}

	private void setFirstNameForBuildUsersListForNotifications(Object[] userObj, UserClients userClient) {
		userClient.setFirstName(userObj[2]==null?"":userObj[2].toString());
	}

	private void setUserIdForBuildUsersListForNotifications(Object[] userObj, Users user) {
		user.setId(Long.valueOf(userObj[0]==null?"":userObj[0].toString()));
	}

	private LocalDateTime getLocalDateTime(String dateString) {
		LocalDateTime localDateTime= null;
		Date date;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
			localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		} catch (ParseException e) {
			LogUtils.basicErrorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "getLocalDateTime"),
					e.getMessage());
		}
		return localDateTime;
	}

	private String sendNotificationsToEmails(UserPrincipal userPrincipal, Users user, String uiUrl,String token) {
		LocalDateTime modifiedOn = null;
		String massage = null;
		List<UsersPasswords> userPasswordsV2 = usersPasswordsRepository.findByUserIdAndPasswordIndex(user.getId(),
				PASSOWORD_INDEX);
		modifiedOn = (CollectionUtils.isNotEmpty(userPasswordsV2) && userPasswordsV2.size() == 1 && null != userPasswordsV2.get(0).getModifiedOn())
				? userPasswordsV2.get(0).getModifiedOn()
				: user.getCreatedOn();
		LocalDate modifiedOnDate = modifiedOn.toLocalDate();
		LocalDate passwordExpiredDate = modifiedOnDate.plusDays(passwordExpirationDays);
		long daysCount = ChronoUnit.DAYS.between(LocalDate.now(), passwordExpiredDate);
		// NOTE: in 90 days remove 7 days as we have to sent 7 days before.
		if ((passwordExpiredDate.isEqual(LocalDate.now())
				|| passwordExpiredDate.isAfter(LocalDate.now()))
				&& (daysCount >= 0 && daysCount <= 7)) {
			
			Map<String, String> contextMap = new HashMap<>();
			userPrincipal.setEmail(user.getEmail());
			String firstName = user.getFirstName();
			if(CollectionUtils.isNotEmpty(user.getClients())) {
				firstName = user.getClients().get(0).getFirstName();
			}
			contextMap.put("employee_name", firstName);
			contextMap.put("days", daysCount+"");
			
			commonUtilities.buildEventToPublish(userPrincipal, NotificationEventEnum.PASSWORDS_EXPIRE, contextMap);
			massage = "Notifications Sent" + "" + firstName;

			if (passwordExpiredDate.isEqual(LocalDate.now())) {
				sendEmail(user, user.getEmail(), PASSWRD_EXPIRATION_ZERODAY_TEMPLATE_NAME,
						PASSWRD_EXPIRATION_ZERODAY_SUBJECT, MethodNames.SEND_FLAG_VALUE_EXPIRED_PASSWRD,
						token, uiUrl, String.valueOf(daysCount), passwordExpiredDate.toString());
			} else {
				sendEmail(user, user.getEmail(), PASSWRD_EXPIRATION_TEMPLATE_NAME, PASSWRD_EXPIRATION_SUBJECT,
						MethodNames.SEND_FLAG_VALUE_EXPIRED_PASSWRD, token, uiUrl,
						String.valueOf(daysCount), passwordExpiredDate.toString());
			}

		}
		return massage;

	}
	
	public void removeToken(TokenStore tokenStore, boolean removeOtherUsers) {
		if (null != SecurityContextHolder.getContext().getAuthentication()
				&& (SecurityContextHolder.getContext().getAuthentication().getDetails() instanceof OAuth2AuthenticationDetails)) {
			String tokenValue = ((OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication()
					.getDetails()).getTokenValue();
			if (null != tokenValue) {
				UserPrincipal userPrincipal = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				OAuth2AccessToken token = tokenStore.readAccessToken(tokenValue);
				String useragent = null;
				Map<String, Serializable> extensions = tokenStore.readAuthentication(token).getOAuth2Request().getExtensions();
				if (MapUtils.isNotEmpty(extensions)) {
					useragent = getUserAgentValue(extensions);
				}
				String clientId = tokenStore.readAuthentication(token).getOAuth2Request().getClientId();
				String username = String.valueOf(userPrincipal.getUserId());
				Collection<OAuth2AccessToken> existingTokens = tokenStore.findTokensByClientIdAndUserName(clientId, username);
				tokenStore.removeAccessToken(token);
				if (removeOtherUsers && CollectionUtils.isNotEmpty(existingTokens)) {
					removeAccessTokenFromTokenStore(tokenStore, useragent, existingTokens);
				}
			}
		}
	}

	private String getUserAgentValue(Map<String, Serializable> extensions) {
		String useragent;
		useragent = (null != extensions.get(MethodNames.USER_AGENT)) ? extensions.get(MethodNames.USER_AGENT).toString() : null;
		if(StringUtils.isNotEmpty(useragent)) {
			try {
				Parser uaParser = new Parser();
				Client c = uaParser.parse(useragent);
				useragent = c.userAgent.family;
			} catch (Exception e1) { 
				  LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "removeToken"), e1.getMessage());
					
			}
		}
		return useragent;
	}

	private void removeAccessTokenFromTokenStore(TokenStore tokenStore, String useragent,
			Collection<OAuth2AccessToken> existingTokens) {
		for (OAuth2AccessToken oauthToken : existingTokens) {
			if (null != tokenStore.readAuthentication(oauthToken) && null != tokenStore.readAuthentication(oauthToken).getOAuth2Request()) {
				Map<String, Serializable> tokenExtensions = tokenStore.readAuthentication(oauthToken).getOAuth2Request().getExtensions();
				if (MapUtils.isNotEmpty(tokenExtensions) && null != useragent) {
					removeAccessToken(tokenStore, useragent, oauthToken, tokenExtensions);
				}
			}
		}
		
	}

	private void removeAccessToken(TokenStore tokenStore, String useragent,
			OAuth2AccessToken oauthToken, Map<String, Serializable> tokenExtensions) {
		String otherAgent = (null != tokenExtensions.get(MethodNames.USER_AGENT)) ? tokenExtensions.get(MethodNames.USER_AGENT).toString() : null;
		if(StringUtils.isNotEmpty(otherAgent)) {
			try {
				Parser uaParser = new Parser();
				Client c = uaParser.parse(otherAgent);
				otherAgent = c.userAgent.family;
			} catch (Exception e1) {
				LogUtils.debugLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "removeToken"), e1.getMessage());

			}
			if(useragent.equals(otherAgent)) {
				tokenStore.removeAccessToken(oauthToken);
			}
		}
	}

}
