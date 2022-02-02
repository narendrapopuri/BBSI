package com.bbsi.platform.user.businessservice.intf;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.enums.Enums;
import com.bbsi.platform.common.enums.Enums.UserEnum;

/**
 * Class for managing the mappings.
 * 
 * @author OSI
 */
public interface MappingBusinessService {

	CommonDTO getMapping(String code, Enums.UserEnum type, boolean selected, boolean clearIds, UserPrincipal principal);
	
	CommonDTO getMapping(Long id, String type, boolean selected);
	
	List<CommonDTO> getMappings(List<String> codeList, Enums.UserEnum type, boolean selected, boolean clearIds, UserPrincipal principal);
	
	long saveMapping(CommonDTO mappingDTO, UserPrincipal userPrincipal);
	
	void deleteMapping(String code, Enums.UserEnum type, Long parentId);
	
	void deleteMapping(Long mappingId);
	
	List<CommonDTO> getPrivileges(Long id, Enums.UserEnum type);

	List<CommonDTO> getPrivilegesByRoleId(long id, UserEnum role);
	
	void updatePrivileges(String code, List<Long> parentIds, Set<String> codes);
	
	void updatePrivileges(String code, List<Long> parentIds, Map<String, List<String>> privilegeMap);
}

