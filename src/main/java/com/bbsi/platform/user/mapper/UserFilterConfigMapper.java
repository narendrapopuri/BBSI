package com.bbsi.platform.user.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import com.bbsi.platform.common.user.dto.UserFilterConfigDTO;
import com.bbsi.platform.user.model.UserFilterConfig;


@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {UserFilterConfigValueMapper.class})
public interface UserFilterConfigMapper {

	
	public UserFilterConfigDTO userFilterConfigToUserFilterConfigDTO(UserFilterConfig userFilterConfig);
	
	public UserFilterConfig userFilterConfigDTOToUserFilterConfig(UserFilterConfigDTO userFilterConfigDTO);

	public default List<UserFilterConfigDTO> userFilterConfigListToUserFilterConfigDTOList(List<UserFilterConfig> userFilterConfigs) {
		return CollectionUtils.isNotEmpty(userFilterConfigs)
				? userFilterConfigs.stream().map(m -> userFilterConfigToUserFilterConfigDTO(m)).collect(Collectors.toList())
				: Collections.emptyList();
	}	
}
