package com.bbsi.platform.user.businessservice.intf;

import java.util.List;

import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.user.dto.MenuMappingDTO;

/**
 * @author anandaluru
 *
 */
public interface MenuBusinessService {
	
	List<MenuMappingDTO> fetchUserMenu(List<CommonDTO> userFeatures, List<CommonDTO> clientMappings);
}
