package com.bbsi.platform.user.repository;

import java.time.LocalDateTime;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.user.model.ResetPasswordRequest;


@Repository
public interface ResetPasswordRequestRepository extends JpaRepository<ResetPasswordRequest, Long> {

	@Lock(LockModeType.NONE)
	@Query(value = "select count(1) from ResetPasswordRequest where userId=?1")
	Object findByUserEmail(Long userId);

	void deleteByUserId(Long id);

	@Lock(LockModeType.NONE)
	@Query(value = "select count(1) from ResetPasswordRequest where userId=?1 and requestedOn is null and  submittedOn between ?2  and ?3")
	Object findByUserEmailAndSubmittedOnRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);

	@Modifying
	@Query(value = "delete from reset_password_request where user_id=?1 and requested_on is null and  submitted_on is not null",nativeQuery = true)
	void deleteSubmittedCallRecords(Long userId);
	
	@Lock(LockModeType.NONE)
	@Query(value = "select count(1) from ResetPasswordRequest where userId=?1 and submittedOn is null and  requestedOn between ?2  and ?3")
	Object findByUserEmailAndRequestedOnRange(Long id, LocalDateTime startTime, LocalDateTime endTime);

}
