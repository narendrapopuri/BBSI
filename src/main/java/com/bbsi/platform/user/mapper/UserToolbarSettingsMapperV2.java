/**
 * 
 */
package com.bbsi.platform.user.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import com.bbsi.platform.common.client.dto.UserToolbarSettingsDTO;
import com.bbsi.platform.user.model.UserToolbarSettings;



/**
 * @author rneelati
 *
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserToolbarSettingsMapperV2 {
	
	
	public UserToolbarSettings userToolbarSettingsDTOToUserToolbarSettings(UserToolbarSettingsDTO userToolbarSettingsDTO);
	
	public UserToolbarSettingsDTO userToolbarSettingsToUserToolbarSettingsDTO(UserToolbarSettings userToolbarSettings);
	
	
	public default List<UserToolbarSettingsDTO> userToolbarSettingsListToUserToolbarSettingsDTOList(List<UserToolbarSettings> userToolbarSettings) {
		return CollectionUtils.isNotEmpty(userToolbarSettings) ? userToolbarSettings.stream()
				.map(m -> userToolbarSettingsToUserToolbarSettingsDTO(m)).collect(Collectors.toList())
				: Collections.emptyList();
	}

	public default List<UserToolbarSettings> userToolbarSettingsDTOListToUserToolbarSettingsList(List<UserToolbarSettingsDTO> userToolbarSettingDTOs) {
		return CollectionUtils.isNotEmpty(userToolbarSettingDTOs) ? userToolbarSettingDTOs.stream()
				.map(m -> userToolbarSettingsDTOToUserToolbarSettings(m)).collect(Collectors.toList())
				: Collections.emptyList();
	}


}
