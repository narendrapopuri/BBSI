package com.bbsi.platform.user.businessservice.intf;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bbsi.platform.common.dto.*;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.bbsi.platform.common.client.dto.UserToolbarSettingsDTO;
import com.bbsi.platform.common.employee.dto.CustomPersonalDTO;
import com.bbsi.platform.common.user.dto.TimenetLoginInfo;
import com.bbsi.platform.common.user.dto.UserCredentials;
import com.bbsi.platform.common.user.dto.UserInvitationDTO;
import com.bbsi.platform.common.user.dto.UserMfaDTO;
import com.bbsi.platform.common.user.dto.UsersDTO;
import com.bbsi.platform.common.user.dto.v2.PolicyAcceptedDTO;
import com.bbsi.platform.common.user.dto.v2.UserCustomDTOV2;
import com.bbsi.platform.common.user.dto.v2.UserExistDTO;

/**
 * @author veethakota
 *
 */
public interface UserBusinessService {

	UsersDTO createUser(UsersDTO usersDTO, String oauthToken, String uiUrl, UserPrincipal userPrincipal);

	UsersDTO updateUser(UsersDTO usersDTO, String token, UserPrincipal userPrincipal);

	/**
	 * Update userPersonal Information.
	 * 
	 * @param usersDTO
	 * @param token
	 * @return Returns the {@link CustomPersonalDTO} Object.
	 */
	public CustomPersonalDTO updateUserPersonalInfo(CustomPersonalDTO usersDTO, String token, UserPrincipal userPrincipal);

	UsersDTO getUserById(Long userId, String token , String clientCode, UserPrincipal userPrincipal);

	void deleteClientFromUser(Long userId, String clientCode);

	List<UsersDTO> getAllClientUsersByClientCode(String clientCode);

	UserPrincipal getUserPrincipal(String userName);

	ParentDTO switchBusiness(UserPrincipal principal, String clientCode, String userType);
	
	void updateIsPrimary(Long userId, Long userClientId, String clientCode, String userType);

	Map<String,List<String>> getEmailsByClientCodeAndType(String clientCode, String userType);

	List<UsersDTO> getAllBranchUsers(Long userId);

	void updateIsPolicyAccepted(PolicyAcceptedDTO policyAcceptedDTO, UserPrincipal principal);

	void updateCCPAPolicyAccepted(boolean isAccepted, UserPrincipal principal);

	boolean disableUser(String clientCode, long newHireId);

	UserExistDTO checkExistUser(String clientCode, String email, String token);

	void updateUserDetails(CustomPersonalDTO customPersonalDTO, String token);

	void deleteNewHireUser(Long id);

	void updateNewHireUser(Long newHireId, String newEmail, String mfaEmail, String mobile);

	void saveUserToolbarSettings(UserPrincipal principal, List<UserToolbarSettingsDTO> userToolbarSettingTOs);

	List<UserToolbarSettingsDTO> getUserToolbarSettings(String clientCode, String userEmail);

	List<UsersDTO> getI9ApprovalEmployeeList(String clientCode);

	void updateI9ApprovalDetails(long userId, String employeeCode, String clientCode);

	UsersDTO geti9ApprovalUser(String clientCode);

	UserCustomDTOV2 getUserByEmail(String email);

	boolean updateUserStatus(String clientCode, String employeeCode, Boolean status);

	List<UsersDTO> getAllClientUsersByClientCodeAndPayRollStatus(String clientCode, String payrollStatus);

	PolicyAcceptedDTO getisPolicyAccepted(String email);

	Map<String, Set<String>> getUserFilter(String email, String clientCode, UserPrincipal principal);

	Map<String, Map<String, Set<String>>> getUserFilter(String email);

	String getEmailByClientCodeAndEmployeeCode(String clientCode, String empCode);

	List<UsersDTO> getUsersByClientCodeAndType(String clientCode, String userType);

	List<CostCenterDTO> getAllCostCenters(String token);

	List<UsersDTO> getUsersByClientCodeAndTypeAndCreatedBy(String clientCode, String userType, String email);

	Map<String,List<String>> getEmailsByClientCodeAndTypeAndEnum(String clientCode, String userType,String enumType);

	/**
	 * Method for getting personal info of employee by client code and employee code
	 * @param clientCode
	 * @param empCode
	 * @return
	 */
	public Map<String,Set<String>> getPersonalInfoByClientCodeAndEmployeeCode(String clientCode, String empCode,String token,String email,Long userId);
	
	CustomPersonalDTO updateEmail(CustomPersonalDTO customPersonalDTO, String token, UserPrincipal userPrincipal);

	/**
	 * Method for creating bbsi user and sending invitations to each created user.
	 * @param userInvitationDetails
	 * @return Returns the {@link List} of {@link UserInvitationDTO} object.
	 */
	public List<UserInvitationDTO>  createUserAndSendInvitation(List<UserInvitationDTO> userInvitationDetails,String token,UserPrincipal userPrincipal
			,String clientCode,String clientName,String uiUrl);
	
	void updateTimenetLoginInfo(TimenetLoginInfo timenetLoginInfo, String clientCode, UserPrincipal principal);

	TimenetLoginInfo getTimenetLoginInfo(String clientCode, UserPrincipal principal);
	
	String getClientNameByClientCode(String clientCode);

	Map<String, String> getTerminatedClients(Long userId, String token);

	void updatePortalAccessForNewClients();
	
	void updateClientMaster();

	List<CostCenterClientDTO> getCostCenterPrismizedClients(String code);
	
	UserPrincipal updateUserPrincipal(UserPrincipal principal);

	public List<String> getdistinctClients();
	

	String mfaOtpValidation(OtpDetailsDTO otpDetailsDTO, String token);
	
	UsersDTO createRehireUser(UsersDTO usersDTO, String oauthToken, String uiUrl, UserPrincipal userPrincipal);
	
	void removeToken(TokenStore tokenStore, boolean removeOtherUsers);

	void addSecurityEntry(UserPrincipal principal, String type, String value);

	void isCCPAUpdated(String clientCode, String employeeCode, Boolean isCCPAUpdated, UserPrincipal principal);
	
	void updateCaliforniaFlag(UserPrincipal principal);
	
	void sendEmailNotificationsForSchoolDistrict(String employeeCode,String firstName,String lastName,UserPrincipal principal);
	
	void reInitiateNewHire(UsersDTO userDTO, String token, String uiUrl, UserPrincipal userPrincipal);

	String authenticateUser(UserCredentials userCredentials, String token);

	String getEmployeeEndDate(String clientCode, String employeeCode);

	void updateEmployeeEndDate(String clientCode, String empCode, String enddate);
	
	String validateMfaCancelAttempts(UserPrincipal principal);

	UserMfaDTO updateMfaEmail(UserMfaDTO userMfaDTO, UserPrincipal principal, String token);
	
	List<BulkUploadMailDTO> validateMfaEmails(List<BulkUploadMailDTO> emailList, UserPrincipal userPrincipal);

	Map<String, Set<String>> getMfaDetails(String uid);
}

