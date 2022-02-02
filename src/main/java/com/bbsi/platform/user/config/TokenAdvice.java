package com.bbsi.platform.user.config;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bbsi.platform.common.generic.GenericUtils;
import com.bbsi.platform.exception.business.UnauthorizedAccessException;
import com.bbsi.platform.exception.enums.ErrorCodes;
import com.bbsi.platform.exception.model.ErrorDetails;
import com.bbsi.platform.user.businessservice.intf.UserBusinessService;
import com.bbsi.platform.user.model.Users;
import com.bbsi.platform.user.repository.UsersRepository;
import com.bbsi.platform.user.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Configuration
public class TokenAdvice {
	
	@Value("${captcha.secret.key}")
	private String captchaSecret;
	
	@Autowired
	private UsersRepository userRepository;
	
	@Value("${bbsi.head.emails}")
	private String bbsiHeadEmails;
	
	private static final String USER_NAME = "username";
	
	private static final String INVALID_CAPTCHA = "Invalid CAPTCHA!!";

	@Around("execution(* com.bbsi.platform.user.businessservice.impl.UserBusinessServiceImpl.loadUserByUsername(..))")
	public Object methodBeforeAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String exten[] = bbsiHeadEmails.split(",");
		if(StringUtils.isNotEmpty(request.getParameter(USER_NAME))
				&& request.getParameter(USER_NAME).contains("@")) {
			String username = request.getParameter(USER_NAME);
			if(!Utils.checkIfEmailHasExtension(username,exten)) {
				Users user = userRepository.findByEmail(username.trim().toLowerCase());
				if(user.getInvalidAttempts() >= 3) {
					String response = request.getHeader("cresponse");
					if(StringUtils.isNotEmpty(response)) {
						verifyUriForMethodBeforeAdvice(response);
					} else {
						throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA004, INVALID_CAPTCHA));
					}
				}
			}
		}
		return joinPoint.proceed(joinPoint.getArgs());
	}
	

	private void verifyUriForMethodBeforeAdvice(String response) {
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
}
