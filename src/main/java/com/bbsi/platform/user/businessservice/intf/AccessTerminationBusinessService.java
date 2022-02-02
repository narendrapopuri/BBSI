package com.bbsi.platform.user.businessservice.intf;

import com.bbsi.platform.common.user.dto.AccessTerminationDTO;

/**
 * Interface provides the behavior for managing the employee notification
 * information.
 * 
 * @author anandaluru
 *
 */
public interface AccessTerminationBusinessService {

	/**
	 * Method for saving the accesstermination details.
	 * 
	 * @param accessTerminationDTO
	 * @return
	 */
	public AccessTerminationDTO saveAccessTermination(AccessTerminationDTO accessTerminationDTO);

	/**
	 * Method for Updating the accesstermination details.
	 * 
	 * @param accessTerminationDTO
	 * @return
	 */
	public AccessTerminationDTO updateAccessTermination(AccessTerminationDTO accessTerminationDTO);

	/**
	 * Method for updating status of user_clients by date.
	 */
	public void updateStatusByEndDate();
	
	/**
	 * Method to get the terminated Client by clientCode
	 * 
	 * @param clientCode
	 * @return
	 */
	public AccessTerminationDTO getTerminatedClientByClientCode(String clientCode);

	/**
	 * Method to get portal access information of employee
	 *
	 * @param employeeCode
	 * @return
	 */
	public AccessTerminationDTO getEmployeeAccessInfo(String clientCode, String employeeCode);

}
