package com.bbsi.platform.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bbsi.platform.user.model.UserToolbarSettings;



@Repository
public interface UserToolbarSettingsRepositoryV2 extends JpaRepository<UserToolbarSettings, Long>{
	
	
	List<UserToolbarSettings> findByClientCodeAndUserEmail(String clientCode, String userEmail);
	
	void deleteByClientCodeAndUserEmail(String clientCode, String userEmail);

	List<UserToolbarSettings> findByUserEmail(final String email);

}
