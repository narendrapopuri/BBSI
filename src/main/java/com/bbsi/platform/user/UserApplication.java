package com.bbsi.platform.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import com.bbsi.platform.audit.repository.AuditDetailsRepository;
import com.bbsi.platform.common.generic.LogUtils;
import com.microsoft.azure.spring.data.cosmosdb.repository.config.EnableCosmosRepositories;

/**
 * @author anandaluru
 *
 */
@SpringBootApplication
@ComponentScan({ "com.bbsi.*" })
@EnableCosmosRepositories(basePackageClasses = { AuditDetailsRepository.class})
@EnableAspectJAutoProxy
@EnableAsync
@EnableJpaRepositories
public class UserApplication {

	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	public static void displayAllBeans() {
		String[] allBeanNames = applicationContext.getBeanDefinitionNames();
		for (String beanName : allBeanNames) {
			LogUtils.basicDebugLog.accept(beanName);
		}
	}
}
