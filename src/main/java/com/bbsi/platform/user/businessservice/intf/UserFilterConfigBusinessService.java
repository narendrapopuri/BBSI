package com.bbsi.platform.user.businessservice.intf;

import java.util.List;

import com.bbsi.platform.common.user.dto.UserFilterConfigDTO;


public interface UserFilterConfigBusinessService {
	
	List<UserFilterConfigDTO> getAllUserFilterConfigsByClientCodeAndUserId(String clientCode,long userId);
	
	String getAllUserFilterConfigsByClientCodeAndUserIdAndFilterId(String clientCode,long userId,long id);
	
	void deleteByClientCodeAndUserIdAndFilterId(String clientCode,long userId,long id);
	
	UserFilterConfigDTO createUserFilterConfig(String userFilterConfigDTO,String clientCode,long userId);
	
	UserFilterConfigDTO updateUserFilterConfig(long filterId,String userFilterConfigPayload,String clientCode,long userId);

}
