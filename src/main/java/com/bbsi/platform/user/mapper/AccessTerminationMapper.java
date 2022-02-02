package com.bbsi.platform.user.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.util.CollectionUtils;

import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.user.dto.AccessTerminationDTO;
import com.bbsi.platform.user.model.AccessTermination;

/**
 * @author anandaluru
 *
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AccessTerminationMapper {

	public default List<AccessTermination> accessTerminationToAccessTerminationDto(
			AccessTerminationDTO accessTerminationDto) {

		List<AccessTermination> accessTerminationList = new ArrayList<>();

		if (accessTerminationDto == null) {
			return new ArrayList<>();
		}
		if(null != accessTerminationDto.getClientEndDate()) {
            AccessTermination clientTermination = new AccessTermination();
            clientTermination.setClientCode(accessTerminationDto.getClientCode());
            clientTermination.setUserType(GenericConstants.USERTYPE_CLIENT);
            clientTermination.setEndDate(accessTerminationDto.getClientEndDate());
            clientTermination.setLimited(accessTerminationDto.isLimited());
            clientTermination.setStartDate(LocalDate.now());
            accessTerminationList.add(clientTermination);
        }

		if(null != accessTerminationDto.getEmployeeEndDate()) {
            AccessTermination employeeTermination = new AccessTermination();
            employeeTermination.setUserType(GenericConstants.USERTYPE_EMPLOYEE);
            employeeTermination.setClientCode(accessTerminationDto.getClientCode());
            employeeTermination.setEndDate(accessTerminationDto.getEmployeeEndDate());
            employeeTermination.setLimited(accessTerminationDto.isLimited());
            employeeTermination.setStartDate(null == accessTerminationDto.getStartDate() ? LocalDate.now() : accessTerminationDto.getStartDate());
            employeeTermination.setEmployeeCode(accessTerminationDto.getEmployeeCode());
            accessTerminationList.add(employeeTermination);
        }

		if(null != accessTerminationDto.getExternalEndDate()) {
            AccessTermination externalUserTermination = new AccessTermination();
            externalUserTermination.setUserType(GenericConstants.USERTYPE_EXTERNAL);
            externalUserTermination.setClientCode(accessTerminationDto.getClientCode());
            externalUserTermination.setEndDate(accessTerminationDto.getExternalEndDate());
            externalUserTermination.setStartDate(LocalDate.now());
            externalUserTermination.setLimited(accessTerminationDto.isLimited());
            accessTerminationList.add(externalUserTermination);
        }
		return accessTerminationList;
	}

	public default AccessTerminationDTO accessTerminationListToAccessTerminationDto(
			List<AccessTermination> accessTerminationList) {
		AccessTerminationDTO accessTerminationDto = new AccessTerminationDTO();

		if (!CollectionUtils.isEmpty(accessTerminationList)) {
			for (AccessTermination accessTermination : accessTerminationList) {

				accessTerminationDto.setClientCode(accessTermination.getClientCode());
				accessTerminationDto.setLimited(accessTermination.isLimited());

				if (GenericConstants.USERTYPE_CLIENT.equalsIgnoreCase(accessTermination.getUserType())) {
					accessTerminationDto.setClientEndDate(accessTermination.getEndDate());
				}

				if (GenericConstants.USERTYPE_EMPLOYEE.equalsIgnoreCase(accessTermination.getUserType())) {
					accessTerminationDto.setEmployeeEndDate(accessTermination.getEndDate());
					accessTerminationDto.setEmployeeCode(accessTermination.getEmployeeCode());
				}

				if (GenericConstants.USERTYPE_EXTERNAL.equalsIgnoreCase(accessTermination.getUserType())) {
					accessTerminationDto.setExternalEndDate(accessTermination.getEndDate());
				}

			}

		}

		return accessTerminationDto;
	}

}
