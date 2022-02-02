package com.bbsi.platform.user.businessservice.intf;

import java.util.List;

import org.springframework.security.oauth2.provider.token.TokenStore;

import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.user.dto.v2.PasswordDTOV2;

public interface PasswordBusinessService {
	
	public PasswordDTOV2 submitEmailForResetPassword(String email, String uiUrl);
	
	public void sendEmailForResetPassword(String userEmail, String uiUrl);
	
	public PasswordDTOV2 saveForgotPassword(String password, String userName);

	public PasswordDTOV2 changePassword(PasswordDTOV2 passwordDTOV2, String token,UserPrincipal userPrincipal, String uiUrl);
	
	public PasswordDTOV2 sendFlagValueForExpiredPassword(String email,UserPrincipal principal);
	
	public PasswordDTOV2 validateJwtToken(PasswordDTOV2 passwordDTOV2);

	List<String> sendNotificationsForExpiredPassword(UserPrincipal userPrincipal, String uiUrl,String token);
	
	void removeToken(TokenStore tokenStore, boolean removeOtherUsers);
}
