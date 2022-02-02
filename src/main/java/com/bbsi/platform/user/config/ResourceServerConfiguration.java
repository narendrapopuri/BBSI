package com.bbsi.platform.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer config) {
		config.resourceId("bbsi-client-service").stateless(true);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.cors().and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.antMatchers("/swagger-ui/**","/swagger**","/v3/**","/oauth/**","/password/forgot/**", "/v1/user/email/client/**", "/management/health/**","/v1/user/authenticateuser").permitAll()
			.antMatchers("/management/metrics**", "/management/loggers**").hasAuthority("ADMIN")
			.antMatchers("/**").authenticated();
		
	}
}
