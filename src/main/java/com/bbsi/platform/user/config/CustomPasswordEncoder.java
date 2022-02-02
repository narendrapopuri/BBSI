package com.bbsi.platform.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bbsi.platform.common.generic.EncryptAndDecryptUtil;

/**
 * CustomPasswordEncoder class to perform PasswordEncoder.
 * 
 * @author OSI
 */

@Component(value = "customEncoder")
public class CustomPasswordEncoder implements PasswordEncoder {
	
	@Value("${login.key}")
	private String loginSecret;
	
	@Autowired
	private EncryptAndDecryptUtil encryptUtil;
	
	@Override
	public String encode(CharSequence data) {
		return encryptUtil.encrypt(data.toString(), loginSecret);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equals(encryptUtil.encrypt(rawPassword.toString(), loginSecret));
	}
}
