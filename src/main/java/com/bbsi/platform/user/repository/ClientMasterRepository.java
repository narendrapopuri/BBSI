package com.bbsi.platform.user.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.user.model.ClientMaster;

@Repository
public interface ClientMasterRepository extends JpaRepository<ClientMaster, String> {
	
	@Lock(LockModeType.NONE)
	ClientMaster findByClientCode(final String clientCode);
	
	@Lock(LockModeType.NONE)
	List<ClientMaster> findByClientCodeIn(final List<String> clientCodes);
	
	@Lock(LockModeType.NONE)
	List<ClientMaster> findByModifiedOnGreaterThan(LocalDateTime modifiedOn);
	
	@Lock(LockModeType.NONE)
	List<ClientMaster> findByCostCenterCode(final String costCenterCode);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE ClientMaster uc set uc.clientName=?1, uc.modifiedOn=?2 where uc.clientCode=?3")
	void updateClientNameByClientCode(final String companyName, final LocalDateTime modifiedOn, final String clientCode);

}
