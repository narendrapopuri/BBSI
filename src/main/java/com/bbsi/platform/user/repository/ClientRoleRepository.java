package com.bbsi.platform.user.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.user.model.ClientRole;
import com.bbsi.platform.user.model.Mapping;

@Repository
public interface ClientRoleRepository extends JpaRepository<ClientRole, Long> {
	
	@Lock(LockModeType.NONE)
	List<ClientRole> findAllById(Iterable<Long> ids);
	
	List<ClientRole> findByClientCode(String clientCode,Sort sort);
	
	List<ClientRole> findByUserClient_IdAndRole_Id(long userClientId, long roleId);
	
	List<ClientRole> findByRole_Id(long roleId);
	
	List<ClientRole> findByIdIn(List<Long> ids);
	
	List<ClientRole> findByRole(Mapping role);
	
	@Modifying
	@Query(value = "DELETE FROM CLIENT_ROLE WHERE user_client_id IN ?1", nativeQuery = true)
	void deleteByUserClientIdIn(List<Long> userClientIds);
	
	@Modifying
	@Transactional
	@Query(value="DELETE FROM CLIENT_ROLE WHERE user_client_id=?1", nativeQuery = true)
	void deleteRole(long userClientId);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE CLIENT_ROLE set ROLE_ID = ?1 WHERE user_client_id=?2", nativeQuery = true)
	void updateRole(long roleId, long userClientId);

	boolean existsByRole_Id(final long roleId);
	
}
