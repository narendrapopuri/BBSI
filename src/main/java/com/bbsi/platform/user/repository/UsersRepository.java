package com.bbsi.platform.user.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.user.model.Users;

/**
 * @author anandaluru
 *
 */
@Repository
public interface UsersRepository extends PagingAndSortingRepository<Users, Long> {
	
	@Lock(LockModeType.NONE)
	Optional<Users> findById(Long id);
	
	Users findByEmail(String email);

	@Query(value="select distinct u.id, u.email, u.mfa_email from users u, user_clients uc where u.mfa_email=?1", nativeQuery = true)
	List<Object[]> findUsersByMfaEmail(String email);

	@Lock(LockModeType.NONE)
	List<Users> findByMfaEmail(String mfaEmail);

	List<Users> findByCreatedBy(String createdBy);
	
	List<Users> findAll(Sort sort);
	
	@Modifying
	@Query(value = "update USERS set is_policy_accepted=?2,is_first_login=0 where email=?1", nativeQuery = true)
	int updateIsPolicyAccepted(final String email,final Boolean isPolicyAccepted);
	
	@Query(value = "select is_policy_accepted from USERS where email=?1", nativeQuery = true)
	Object getisPolicyAccepted(String email);
	
	@Query(value = "UPDATE users SET INVALID_ATTEMPTS = INVALID_ATTEMPTS + 1 OUTPUT INSERTED.INVALID_ATTEMPTS WHERE email=?1", nativeQuery = true)
	Object[] incrementInvalidAttempts(final String email);
	
	@Modifying
	@Query(value = "UPDATE users SET MFA_CANCEL_ATTEMPTS = MFA_CANCEL_ATTEMPTS + 1 WHERE id=?1", nativeQuery = true)
	void incrementMfaInvalidAttempts(long userId);
	
	@Modifying
	@Query(value = "update Users set invalidAttempts=0 where email=?1")
	int clearInvalidAttempts(final String email);
	
	@Query(value = "select distinct u.id, u.created_on,uc.first_name,uc.last_name,u.email from USERS u, user_clients uc where u.id=uc.user_id and uc.is_active=1", nativeQuery = true)
	List<Object[]> getUserIdsAndcreatedOn();
	
	@Query(value = "select uc.first_name,uc.last_name,uc.mobile,u.email,u.is_first_login,uc.user_type,uc.new_hire_id,uc.id,u.mfa_email, u.id as userId from users u, user_clients uc where uc.user_id=u.id and u.email=?1 and uc.client_code=?2", nativeQuery = true)
	List<Object[]> getUserDataByEmail(String email,String clientCode);
	
	@Modifying
	@Transactional
	@Query(value = "update USERS set token_value=?2 where email=?1", nativeQuery = true)
	int updateTokenValue(final String email,final String token);
	
	@Query(value = "SELECT DISTINCT client_code FROM user_clients", nativeQuery = true)
	List<String> getdistinctClients();
	
	@Query(value = "select uc.first_name,uc.last_name,uc.mobile,u.email,u.is_first_login,uc.employee_code,uc.user_type,uc.new_hire_id,uc.id from users u, user_clients uc where uc.user_id=u.id and uc.client_code=?1  and (uc.employee_code=?2 or (u.email=?3 and (uc.employee_code is not null or uc.new_hire_id > 0)))", nativeQuery = true)
	List<Object[]> getUserDataForInvitation(String clientCode,String employeeCode,String email);

	@Query(value = "select u.user_id from user_rbac_association u where u.role_id = ?1", nativeQuery = true)
	List<Object[]> findUsersByRoleId(final long roleId);
	
	@Query(value = "select created_on from users u where u.id = ?1", nativeQuery = true)
	Object[] findCreatedDateByUserId(final long userId);
	
	@Query(value = "select mobile, mfa_email from users u where u.id = ?1", nativeQuery = true)
	List<Object[]> findMobileByUserId(final long userId);
	
	@Modifying
	@Query(value = "update users set default_client=?2 where id=?1", nativeQuery = true)
	int updateDefaultClient(final long id, final String clientCode);
	
	void deleteByEmail(final String email);
	
	@Modifying
	@Transactional
	@Query(value = "delete from users WHERE id = ?1", nativeQuery = true)
	void deleteNewHireUser(long newHireUserId);
	
}
