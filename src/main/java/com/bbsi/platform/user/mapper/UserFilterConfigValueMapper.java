package com.bbsi.platform.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import com.bbsi.platform.common.user.dto.UserFilterConfigValueDTO;
import com.bbsi.platform.user.model.UserFilterConfigValue;


@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserFilterConfigValueMapper {
	
	public UserFilterConfigValueDTO userFilterConfigValueToUserFilterConfigValueDTO(UserFilterConfigValue userFilterConfigValue);

	public UserFilterConfigValue userFilterConfigValueDTOToUserFilterConfigValue(UserFilterConfigValueDTO userFilterConfigValueDTO);

}
