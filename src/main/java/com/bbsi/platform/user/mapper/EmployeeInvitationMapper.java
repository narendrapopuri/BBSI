package com.bbsi.platform.user.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import com.bbsi.platform.common.user.dto.EmployeeInvitationDTO;
import com.bbsi.platform.user.model.EmployeeInvitation;

/**
 * @author anandaluru
 *
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EmployeeInvitationMapper {

	/**
	 * Method for mapping from {@link EmployeeInvitation} to
	 * {@link EmployeeInvitationDTO} Object.
	 * 
	 * @param employeeInvitation
	 * @return Returns {@link EmployeeInvitationDTO}
	 */
	public EmployeeInvitationDTO employeeInvitationToEmployeeInvitationDTO(
			EmployeeInvitation employeeInvitation);

	/**
	 * Method for mapping list of {@link EmployeeInvitation} to List of
	 * {@link EmployeeInvitationDTO} Objects.
	 * 
	 * @param employeeInvitation
	 * @return Returns the {@link List} of {@link EmployeeInvitationDTO} objects.
	 */
	public List<EmployeeInvitationDTO> employeeInvitationToEmployeeInvitationDTOs(
			List<EmployeeInvitation> employeeInvitation);

	/**
	 * Method for mapping from {@link EmployeeInvitation} to
	 * {@link EmployeeInvitationDTO} object.
	 * 
	 * @param employeeInvitation
	 * @return Returns the {@link EmployeeInvitation}
	 */
	public EmployeeInvitation employeeInvitationToEmployeeInvitationDTO(
			EmployeeInvitationDTO employeeInvitation);

	/**
	 * 
	 * @param employeeInvitation
	 * @return
	 */
	public List<EmployeeInvitation> employeeInvitationDTOToEmployeeInvitationDTOs(
			List<EmployeeInvitationDTO> employeeInvitation);

}
