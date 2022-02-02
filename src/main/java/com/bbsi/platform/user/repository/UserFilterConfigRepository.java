package com.bbsi.platform.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.user.model.UserFilterConfig;

@Repository
public interface UserFilterConfigRepository extends JpaRepository<UserFilterConfig, Long> {


	List<UserFilterConfig> findByClientCodeAndUserId(String clientCode, long userId);
	
	UserFilterConfig findByClientCodeAndUserIdAndId(String clientCode, long userId,long id);
	
	@Modifying
	@Transactional
	public void deleteByClientCodeAndUserIdAndId(String clientCode, long userId,long id);
	
}
