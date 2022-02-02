package com.bbsi.platform.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bbsi.platform.user.model.EmployeeInvitation;

/**
 * Interface that provides methods for managing the {@link EmployeeInvitation}
 * details.
 * 
 * @author jkolla
 *
 */
@Repository
public interface EmployeeInvitationRepository extends JpaRepository<EmployeeInvitation, Long> {

	/**
	 * Method for getting employee notification list by client code.
	 * 
	 * @param clientCode
	 * @return Returns the {@link List} of {@link EmployeeInvitation} objects.
	 */
	public List<EmployeeInvitation> findByClientCode(String clientCode);
	
	/**
	 * Method for getting client code and employee code.
	 * @param clientCode
	 * @param employeeCode
	 * @return Returns the {@link EmployeeInvitation} object.
	 */
	public EmployeeInvitation findByClientCodeAndEmployeeCode(String clientCode,String employeeCode);

}
