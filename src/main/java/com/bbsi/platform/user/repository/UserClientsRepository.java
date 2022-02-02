package com.bbsi.platform.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.user.model.UserClients;

@Repository
public interface UserClientsRepository extends JpaRepository<UserClients, Long> {
	
	@Lock(LockModeType.NONE)
	Optional<UserClients> findById(final long id);
	
	List<UserClients> findByClientCode(final String clientCode);
	
	List<UserClients> findByClientCodeAndIsActiveTrue(final String clientCode);
	
	List<UserClients> findByClientCode(final String clientCode, Sort sort);
	
	List<UserClients> findByClientCodeAndUserType(final String clientCode, String userType);
	
	List<UserClients> findByClientCodeAndUserTypeAndIsActiveTrue(final String clientCode, String userType);
	
	List<UserClients> findByClientCodeIn(final Set<String> clientCodes, Sort sort);
	
	List<UserClients> findByUser_IdIn(final List<Long> ids);
	
	@Lock(LockModeType.NONE)
	List<UserClients> findByUser_Id(final Long id);
	
	UserClients findByNewHireId(final Long newHireId);
	
	@Query(value = "select c.id,c.user_type,c.user_id from user_clients c where c.new_hire_id=?1", nativeQuery = true)
	List<Object[]> getNewhireUser(final Long newhireId);

	@Modifying
	@Transactional
	void deleteById(long id);
	
	@Modifying
	@Query(value = "DELETE FROM UserClients uc WHERE uc.id IN ?1")
	void deleteByIdIn(List<Long> ids);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE UserClients uc set uc.isI9Approver=?1 where uc.id=?2")
	int updateI9ApprovalDetails(final Boolean isI9Approver, final long userClientId);
	
	@Lock(LockModeType.NONE)
	List<UserClients> findByClientCodeAndUser_Id(final String clientCode, final Long id);
	
	@Query(value = "select c.first_name, c.last_name from user_clients c where c.client_code=?1 and c.user_id=?2", nativeQuery = true)
	List<Object[]> getClientUserName(final String clientCode, final Long userId);
	
	@Query(value = "select c.mobile,c.id from user_clients c where c.user_id=?1", nativeQuery = true)
	List<Object[]> getClientUserMobile(final Long userId);
	
	@Query(value = "select c.first_name, c.last_name, u.mfa_email from users u, user_clients c where u.id = c.user_id and c.client_code=?1 and c.employee_code=?2", nativeQuery = true)
	List<Object[]> getEmpUserName(final String clientCode, final String empCode);
	
	@Query(value = "select c.employee_code, c.IS_CCPA_ACCEPTED, c.IS_CALIFORNIA_USER from user_clients c where c.client_code=?1 and c.user_id=?2 and c.user_type='Employee'", nativeQuery = true)
	List<Object[]> getCaliforniaUsers(final String clientCode, final Long userId);

	@Query(value = "select c.employee_code, c.IS_CCPA_ACCEPTED, c.IS_CALIFORNIA_USER, c.client_code from user_clients c where c.user_id=?1 and c.user_type in ('Employee','NewHire')", nativeQuery = true)
	List<Object[]> getCaliforniaUserClients(final Long userId);
	
	List<UserClients> findByUser_Email(final String email);
	
	@Query(value = "select c.id, c.first_name, c.last_name, c.employee_code from user_clients c where c.client_code=?1 and c.user_id=?2", nativeQuery = true)
	List<Object[]> findByUserClientsByClient(final String clientCode, final Long userId);
	
	@Query(value = "select c.id, c.first_name, c.last_name from user_clients c where c.client_code=?1 and c.user_id=?3 and c.user_type=?2", nativeQuery = true)
	List<Object[]> findByUserClientsByType(final String clientCode, String userType, final Long userId);
	
	@Query(value = "select u.email, uc.first_name, uc.last_name, uc.mobile, uc.user_type, uc.is_active, u.mfa_email from user_clients uc, users u where uc.user_id=u.id and uc.client_code=?1", nativeQuery = true)
	List<Object[]> findByUserDetailsByClient(final String clientCode);
	
	@Query(value = "select uc.first_name, uc.last_name, uc.mobile, uc.user_type, uc.user_id, cr.role_id from user_clients uc, client_role cr where uc.id=cr.user_client_id and uc.client_code=?1 and uc.is_active=1", nativeQuery = true)
	List<Object[]> findByApprovalUserDetailsByClient(final String clientCode);
	
	
	 @Query(value ="select uc.first_name, uc.last_name, uc.mobile, uc.user_type, uc.user_id, re.mapping_id from user_clients uc, user_rbac_association ur, rbac_entity re where uc.user_id=ur.user_id and uc.client_code=?1 and uc.user_type='Branch' and uc.is_active=1 and re.id=ur.role_id", nativeQuery = true)
	 List<Object[]> findByBranchUsersByClientCode(final String clientCode);
	 

	UserClients findByClientCodeAndEmployeeCode(final String clientCode, final String employeeCode);
	
	List<UserClients> findByEmployeeCode(final String employeeCode);
	
	@Query(value = "select uc.id, uc.client_code, uc.employee_code, uc.is_active, uc.end_date, cr.role_id from user_clients uc, client_role cr where employee_code is not null and cr.user_client_id=uc.id", nativeQuery = true)
	List<Object[]> findAllEmployeesWithRoles();
	
	List<UserClients> findByEmployeeCodeNotNullAndClientCodeAndUser_Id(final String clientCode, final long userId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE UserClients uc set uc.isActive=?1 where uc.employeeCode=?2 and uc.clientCode=?3")
	void updateUserStatus(final Boolean isActive, final String employeeCode, final String clientCode);
	
	@Modifying
	@Transactional
	@Query(value = "update user_clients set is_primary=0 where user_id=?1", nativeQuery = true)
	void clearPrimaryStatus(final long userId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE UserClients set isPrimary=?1 where id=?2")
	void updateClientPrimaryStatus(final Boolean isPrimary, final long id);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE UserClients uc set uc.isI9Approver=?1 where uc.clientCode=?2")
	int updateI9ApprovalDetails(final Boolean isI9Approver, final String clientCode);
	
	@Modifying
	@Transactional
	@Query(value="DELETE FROM UserClients WHERE newHireId=?1")
	void deleteNewHireId(long newHireId);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE UserClients uc set uc.rememberTimenetCompanyId=?1,uc.rememberTimenetUserId=?2,uc.rememberTimenetPassword=?3,uc.timenetCompanyId=?4,uc.timenetUserId=?5,uc.timenetPassword=?6 where uc.user.id=?7 and uc.clientCode=?8")
	void updateTimeNetInfo(Boolean rememberTimeNetCompanyId, Boolean rememberTimeNetUserId,
			Boolean rememberTimeNetPassword, String timeNetCompanyId, String timeNetUserId, String timeNetPassword,
			long userId, String clientCode);

	@Query(value = "select distinct c.client_code from user_clients c where c.user_id=?1", nativeQuery = true)
	Object[] getClientsOfBranch(final Long userId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE UserClients SET userType=?1, newHireId=?2, employeeCode=null WHERE employeeCode=?3 and clientCode=?4")
	void updateEmployeeUserTypeToNewHireUserStatus(final String userType, final long newHireId, final String employeeCode, final String clientCode);

	@Modifying
	@Transactional
	@Query(value = "update user_clients set is_ccpa_accepted=:isCCPAAccepted where user_id=:userId and client_code in (:clientCodes)", nativeQuery = true)
	void updateIsCCPAAccepted(final List<String> clientCodes, final Boolean isCCPAAccepted,final long userId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE UserClients SET isCaliforniaUser=?1 WHERE employeeCode=?2 and clientCode=?3")
	void isCCPAUpdated(final Boolean isCaliforniaUser,final String employeeCode, final String clientCode);
	
	@Query(value = "select uc.id, uc.client_code, uc.is_active, uc.end_date, cr.role_id,uc.user_type, mp.code from user_clients uc, client_role cr left  join mapping mp on cr.role_id = mp.id where (uc.user_type ='Client' or uc.user_type ='External')  and cr.user_client_id=uc.id", nativeQuery = true)
	List<Object[]> findAllClientUsersWithRoles();

	
	UserClients findByClientCodeAndUserTypeAndEmployeeCode(final String clientCode,final String userType, String employeeCode);
	
	@Modifying
	@Transactional
	@Query(value = "update user_clients set end_date=?4 where client_code=?1 and employee_code=?2 and user_type=?3", nativeQuery = true)
	void updateEmployeeEndDate(final String clientCode, final String employeeCode,final String userType,final LocalDateTime endDate);
	
	@Modifying
	@Transactional
	@Query(value = "update user_clients set end_date=null where client_code=?1 and employee_code=?2 and user_type=?3", nativeQuery = true)
	void updateEmployeeEndDateNull(final String clientCode, final String employeeCode,final String userType);

	@Query(value = "select count(1) from user_clients uc where uc.user_id =?1 ",nativeQuery = true)
	long getNoOfUserClients(long newHireUserId);

	@Modifying
	@Transactional
	@Query(value = "update user_clients set user_id=?2 where id=?1", nativeQuery = true)
	void updateNewHireUser(Long userClientId, Long userId);

}
