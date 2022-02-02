package com.bbsi.platform.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.user.model.UserFilterConfigValue;

@Repository
public interface UserFilterConfigValueRepository extends JpaRepository<UserFilterConfigValue, Long> {
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM USER_FILTER_CONFIG_VALUE WHERE USER_FILTER_CONFIG_ID = ?1", nativeQuery = true)
	void deleteUserFilterConfigValueByFilterId(long filterId);
}
