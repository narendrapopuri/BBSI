package com.bbsi.platform.user.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.user.model.AccessTermination;

/**
 * Interface that provides the data access operations for Access Termination
 * 
 * @author jkolla
 *
 */
@Repository
public interface AccessTerminationRepository extends JpaRepository<AccessTermination, Long> {

	/**
	 * Method for getting Access termination details by client code.
	 * @param clientCode
	 * @return Returns  {@link List} of {@link AccessTermination} objects.
	 */
	public List<AccessTermination> findByClientCode(String clientCode);

	public List<AccessTermination> findByClientCodeAndEmployeeCodeIsNull(String clientCode);
	
	public List<AccessTermination> findByClientCodeAndEmployeeCodeIsNotNullAndEndDateIsNotNull(String clientCode);
	
	/**
	 * Method for getting list of accesstermination object by status.
	 * @param endDate
	 * @return Returns {@link List} of {@link AccessTermination} objects.
	 */
	@Query(value = "select * from ACCESS_TERMINATION at where at.end_date <= ?1 and at.end_date_processed=0", nativeQuery = true)
	public List<AccessTermination> findByEndDate(LocalDate endDate);

	/**
	 * Method for getting terminated employee records.
	 *
	 * @param currentDate
	 * @return
	 */
	@Query(value = "select * from ACCESS_TERMINATION at where at.start_date <= ?1 and at.start_date_processed=0", nativeQuery = true)
	public List<AccessTermination> findByStartDate(LocalDate currentDate);
	
	public List<AccessTermination> findByClientCodeAndEmployeeCode(final String clientCode, final String employeeCode);

	@Query(value = "select * from ACCESS_TERMINATION at where at.client_code =?1 and at.employee_code =?2 and at.user_type=?3 and at.end_date_processed=0", nativeQuery = true)
	public List<AccessTermination> getEmployeeEndDate(final String clientCode, final String employeeCode,final String usertype);
	
	
	@Query(value = "select * from ACCESS_TERMINATION at where at.client_code =?1 and at.employee_code =?2 and at.user_type=?3 and at.start_date_processed=?4", nativeQuery = true)
	public List<AccessTermination> checkEmployeeEndDate(final String clientCode, final String employeeCode,final String usertype,final boolean startDateProcessed);
	
	
	@Modifying
	@Transactional
	@Query(value = "update ACCESS_TERMINATION set end_date=?4 where client_code =?1 and employee_code =?2 and user_type=?3", nativeQuery = true)
	public int updateEndDate(final String clientCode, final String employeeCode,final String usertype,final LocalDate enddate);
	
	@Modifying
	@Transactional
	@Query(value = "update ACCESS_TERMINATION set end_date=null where client_code =?1 and employee_code =?2 and user_type=?3", nativeQuery = true)
	public int updateEndDateNull(final String clientCode, final String employeeCode,final String usertype);
	
	
	
	

}
