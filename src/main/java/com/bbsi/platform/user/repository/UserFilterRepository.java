package com.bbsi.platform.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.user.model.UserSecurity;

@Repository
public interface UserFilterRepository extends JpaRepository<UserSecurity, Long> {
	
	List<UserSecurity> findByUserClient_Id(final Long id);
	
	@Modifying
	@Transactional
	void deleteByUserClient_Id(final Long id);
	
	@Modifying
	@Transactional
	void deleteByUserClient_IdIn(final List<Long> ids);
}
