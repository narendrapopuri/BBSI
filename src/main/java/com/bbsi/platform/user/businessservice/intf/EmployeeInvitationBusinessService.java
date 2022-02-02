package com.bbsi.platform.user.businessservice.intf;

import java.util.List;

import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.user.dto.EmployeeInvitationDTO;
import com.bbsi.platform.common.user.dto.FeatureCodeDTO;

/**
 * Interface provides the behavior for managing the employee notification
 * information.
 * 
 * @author anandaluru
 *
 */
public interface EmployeeInvitationBusinessService {

	/**
	 * Method for getting all the feature list by client id.
	 * 
	 * @param clientCode
	 * @return Returns the {@link FeatureCodeDTO} list.
	 */
	public List<EmployeeInvitationDTO> getAllNotificatonInfoByClientCode(String clientCode, String token);

	/**
	 * Method for saving the {@link EmployeeInvitationDTO} information
	 * 
	 * @param employeeInvitationDto
	 * @return Returns the saved Entity.
	 */
	public EmployeeInvitationDTO saveEmployeeInvitationInfo(EmployeeInvitationDTO employeeInvitationDto, String token);
	
	/**
	 * Method for saving all the list.
	 * @param employeeInvitationDtoList
	 * @return Returns the list of saved {@link EmployeeInvitationDTO}
	 */
	public List<EmployeeInvitationDTO> saveAllEmployeeInvitationInfo(List<EmployeeInvitationDTO> employeeInvitationDtoList, String token);
	
	
	void saveAllByClientCodeAndEmployeeId(String clientCode,List<String> employeeCodes);
	
	public String getEmployeesForBulkInvitation(UserPrincipal userPrincipal);
}
