package com.bbsi.platform.user.businessservice.intf;

import java.util.List;

import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.enums.Enums;
import com.bbsi.platform.common.user.dto.RbacDTO;

/**
 * @author anandaluru
 *
 */
public interface RbacBusinessService {

	/**
	 * @param rbacDTO
	 * @return
	 * @throws Exception
	 */
	RbacDTO createRbac(RbacDTO rbacDTO, String type, UserPrincipal userPrincipal);
	
	List<RbacDTO> getAllRbacs(UserPrincipal userPrincipal, String type);

	/**
	 * @param rbacDTO
	 * @return
	 */
	RbacDTO updateRbac(RbacDTO rbacDTO, String type, UserPrincipal userPrincipal);
	
	RbacDTO getEntriesByCode(String code, Enums.UserEnum type, boolean selected, boolean clearIds, UserPrincipal userPrincipal);
	
	List<RbacDTO> getClientRoles(String clientCode, UserPrincipal userPrincipal);

    List<RbacDTO> getAllClientRoles(UserPrincipal userPrincipal);

	List<RbacDTO> getAllRoles(boolean clientOnly, String clientCode);
}
