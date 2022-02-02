package com.bbsi.platform.user.utils;

public final class MethodNames {

    public static final String CHANGE_PASSWRD = "changePassword";
    
    public static final String COST_CENTER_CODE = "cost_center_code";
    
    public static final String GET_TERMINATED_CLIENT_BY_CLIENT_CODE = "getTerminatedClientByClientCode";

    public static final String GET_FEATURECODES = "getFeatureCodes";

    public static final String GET_FEATURECODE_BY_CLIENT_CODE_AND_FEATURE_GROUP_ID = "getFeatureCodeByClientCodeAndFeatureGroupId";

    public static final String SAVE_PRIVILEGES_GROUP = "savePrivilegeGroup";

    public static final String UPDATE_PRIVILEGES_GROUP = "updatePrivilegeGroup";

    public static final String GET_PRIVILEGES_BY_CLIENT_CODE = "getPrivilegeGroupByClientCode";

    public static final String GET_PRIVILEGES_BY_CLIENT_CODE_AND_NAME = "getPrivilegeGroupByClientCodeAndName";

    public static final String DELETE_PRIVILEGE_GROUP = "deletePrivilegeGroup";

    public static final String CREATE_ROLE = "createRole";

    public static final String SAVE_UPDATE_USER_ROLE = "saveOrUpdateUserRoles";

    public static final String GET_USER_PROFILE_BY_KEY = "getUserProfileByKey";

    public static final String GET_MENU_FOURITES_BY_USER_ID = "getMenuFavouritesByUserId";

    public static final String UPDATE_MENU_FAVOURITES = "updateMenuFavourite";

    public static final String FETCH_MENU = "fetchMenu";

    public static final String FETCH_MENU_BY_NAME = "fetchMenuByName";

    public static final String FETCH_MENU_BY_USER_EMAIL = "fetchMenuByUserEmail";

    public static final String GET_ROLE_BY_CLIENT_CODE = "getRoleByClientCode";

    public static final String UPDATE_ROLE = "updateRole";

    public static final String DELETE_ROLE = "deleteRoleById";

    public static final String GET_ROLE_BY_ID = "getRoleByById";
    
    public static final String GET_ROLES_BY_IDS = "getRolesByByIds";

    public static final String CREATE_USER_PROFILE_IN_CACHE = "createUserProfileIncache";

    public static final String GET_USER_PROFILE = "getUserProfile";

    public static final String DELETE_FROM_CACHE = "deleteFromCache";

    public static final String GET_USERS_BY_CLIENT_CODE_AND_EMPLOYEE_CODE = "getUsersByClientCodeAndEmployeeCode";

    public static final String ASSIGN_ROLES_TO_USER = "assignRolesToUser";

    public static final String CREATE_USER = "createUser";
    
    public static final String SEND_CREDENTIALS_TO_EMPLOYEES =  "sendCredentialsToEmployees";

    public static final String UPDATE_USER = "updateUser";
    
    public static final String UPDATE_USER_PERSONAL_INFO = "updateUserPersonalInfo";
    
    public static final String UPDATE_EMAIL = "updateEmail";

    public static final String GET_USER_BY_ID = "getUserById";

    public static final String GET_USER_PRINCIPAL = "getUserPrincipal";

    public static final String GET_MFA_DETAILS = "getMfaDetails";

    public static final String GET_ALL_USERS = "getAllUsers";

    public static final String GET_FEATURE_CODE_HIERARCHY = "getFeatureCodeHierarchy";

    public static final String GET_FEATURE_CODE_HIERARCHY_BY_PRIVILEGE_IDS = "getFeatureCodeHierarchyByPrivilegeIds";
    
    public static final String CREATE_ACCESS_GROUP = "createAccessGroup";

    public static final String UPDATE_ACCESS_GROUP = "updateAccessGroup";
    
    public static final String GETALL_ACCESS_GROUP = "getAllAccessGroups";
    
    public static final String GET_ACCESS_GROUP_BYID = "getAccessGroupById";    

    public static final String GET_ALL_ROLES = "getAllRoles";

    public static final String GET_ALL_CLIENT_ROLES   = "getAllClientRoles";
    
    public static final String DELETE_ACCESSGROUP_OF_ROLE = "deleteAccessGroupOfRole";
    
    public static final String CREATE_CLIENT_ROLE = "createClientRole";
    
    public static final String UPDATE_CLIENT_ROLE = "updateClientRole";
    
    public static final String CLIENT_ROLE_TYPE = "CLIENT";

    public static final String BRANCH_ROLE_TYPE = "BRANCH";
    
    public static final String CLIENT_ROLE_BY_CLIENT_CODE = "clientRoleByClientCode";

    public static final String  GET_CLIENT_ASSIGNED_ROLES = "getClientAssignedRoles";

    public static final String DELETE_ROLE_FROM_CLIENTROLE = "deleteRoleFromClientRole";

    public static final String DELETE_CLIENTROLE = "deleteClientRole";
    
    public static final String GET_CLIENT_ROLEBY_ID = "getclientRoleById";

    public static final String GET_ALL_CLIENT_ROLES_BY_CLIENT_CODE = "getAllClientRolesByClientCode";

    public static final String  GET_PRIVILEGES_OF_CLIENT_ROLE  = "getPrivlegesOfClientRole";

    public static final String CONSTRAINT_VIOLATION_EXCEPTION = "ConstraintViolationException";

    public static final String COPY_ROLE = "copyRole";

    public static final String UPDATE_COPY_ROLE = "updateCopyRole";
    
    public static final String DELETE_CLIENT_FORM_USER = "deleteClientFormUser";
    
    public static final String GET_CLIENT_ROLE_FOR_CLIENT_ADMIN = "getClientRoleForClientAdmin";
    
    public static final String GET_MENU_BY_USERID_AND_CLIENTCODE = "fetchMenuByUserIdAndClientCode";

	public static final String GET_FEATURES_BY_MENUITEMID_AND_USERID_CLIENTCODE = "getFeaturesByMenuItemIdAndUserIdAndClientCode";
	
	public static final String SUBMIT_EMAIL_FOR_RESET_PASSWRD ="submitEmailForResetPassword";
	
	public static final String SEND_EMAIL_FOR_RESET_PASSWRD ="sendEmailForResetPassword";
	
	public static final String SAVE_FORGOT_PASSWRD ="saveForgotPassword";

	public static final String VANCOUVER_OPERATIONS_CENTER = "Vancouver Operations Center";

    public static final String BRANCH = "Branch";

    public static final String CLIENT = "Client";
    
    public static final String GET_ALL_CLIENT_USERS_BY_CLIENTCODE ="getAllClientUsersByClientCode";
    
    public static final String GET_USERS_PASSWRD_EXPIRATION_NOTIFICATION = "getUsersPasswordExpireNotificationData";
    
    public static final String SEND_FLAG_VALUE_EXPIRED_PASSWRD = "sendFlagValueForExpiredPassword";
    
    public static final String VALIDATE_JWT_TOKEN = "validateJwtToken";
    
    public static final String SWITCH_BUSINESS ="switchBusiness";

    public static final String GET_ALL_BRANCH_USERS ="getAllBranchUsers";
    
    public static final String GET_FEATURECODE_HIERARCHYBY_FATURECODE = "getFeatureCodeHierarchyByFatureCode";

    public static final String UPDATE_IS_POLICY_ACCEPTED ="updateisPolicyAccepted";

    public static final String UPDATE_IS_CCPA_ACCEPTED ="updateIsCCPAAccepted";
    
    public static final String IS_CCPA_UPDATED="isCCPAUpdated";
    
    public static final String DISABLE_USER ="disableUser";
    
    public static final String CHECK_EXIST_USER ="checkExistUser";

	public static final String DELETE_NEWHIRE_USER_BY_ID = "deleteNewHireUser";
    
	public static final String UPDATE_NEWHIRE_USER_BY_ID = "updateNewHireUser";
    
	public static final String SAVE_USER_TOOLBAR_SETTINGS = "saveUserToolbarSettings";
    
	public static final String GET_USER_TOOLBAR_SETTINGS = "getUserToolBarSettings";
	
	public static final String GET_I9APPROVAL_EMPLOYEELIST = "getI9ApprovalEmployeeList";
	
	public static final String UPDATE_I9APPROVAL_DETAILS = "updateI9ApprovalDetails";
	
	public static final String GET_I9APPROVAL_USER = "geti9ApprovalUser";
	
	public static final String GET_USERBY_EMAIL = "getUserByEmail";

	public static final String UPDATE_USER_STATUS = "updateUserStatus";

	public static final String USER_CLIENT_FILTER ="getUserFilter";
	
	public static final String GET_EMAILBY_CLIENTCODE_AND_EMPLOYEECODE = "getEmailByClientCodeAndEmployeeCode";
	
	public static final String GET_MAPPING = "getMapping";
	
	public static final String SAVE_MAPPING = "saveMapping";
	
	public static final String GET_ALL_FEATURECODES = "getAllFeatureCodes";
		
	public static final String SAVE_ACCESS_TERMINATION_INFO = "saveAccessTerminationInfo";
	
	public static final String UPDATE_ACCESS_TERMINATION_INFO = "updateAccessTerminationInfo";
	
	public static final String GET_ALL_EMPLOYEE_NOTIFICATION_BY_CLIENT_CODE = "getAllEmployeeInvitationByClientCode";
	
	public static final String GET_CONFLUENCE_DETAILS = "getConfluenceDetails";
	
	public static final String GET_USERSBY_CLIENTCODE_AND_TYPE_AND_CREATEDBY ="getUsersByClientCodeAndTypeAndCreatedBy";
	
	public static final String UPDATE_TIMENET_DETAILS = "updateTimeNetDetails";
	
	public static final String GET_TIMENET_DETAILS = "getTimeNetDetails";

	public static final String GET_TERMINATED_CLIENTS_OF_BRANCH = "getTerminatedClientsOfBranch";

    public static final String TERMINATED_STATUS_CODE = "T";

    public static final String TERMINATED_EMPLOYEE_ROLE_CODE = "TERMINATED EMPLOYEE";

    public static final String USERTYPE_EMPLOYEE = "Employee";

    public static final String USERTYPE_EXTERNAL = "External";

    public static final String GET_EMPLOYEE_ACCESS_INFO  = "getEmployeeAccessInfo";
    
    public static final String CREATE_REHIRE_USER = "createRehireUser";

    public static final String SAVE_ACCESS_TERMINATION = "saveAccessTermination";

    public static final String GET_ALL_NOTIFICATIONS_INFO_BY_CLIENTCODE = "getAllNotificatonInfoByClientCode";

    public static final String SAVE_EMPLOYEE_INVITATION_INFO = "saveEmployeeInvitationInfo";

    public static final String SAVE_ALL_EMPLOYEE_INVITATION_INFO = "saveAllEmployeeInvitationInfo";

    public static final String SAVE_ALL_CLIENTCODE_AND_EMPLOYEEID = "saveAllByClientCodeAndEmployeeId";

    public static final String INVALID_TOKEN_ERROR = "JWT Token Expired or Please send valid token";

    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access!!";

    public static final String COST_CENTER_CLIENTS = "COST_CENTER_CLIENTS";

    public static final String CLIENT_ID = "client_id";

    public static final String CLIENT_NAME = "client_name";

    public static final String MODIFIED_ON = "modifiedOn";

    public static final String PAY_TS_HOUR_SUBMIT_ALL = "PAY_TS_HOUR_SUBMIT.ALL";

    public static final String PAY_TS_HOUR_FINALIZE_ALL = "PAY_TS_HOUR_FINALIZE.ALL";

    public static final String COMPANY_DOING_BUSINESS_AS = "company_doing_business_as";

    public static final String STATUS = "status";

    public static final String UPDATE_PORTAL_ACCESS_FOR_NEW_CLIENTS = "updatePortalAccessForNewClients";
    
    public static final String UPDATE_CLIENT_MASTER_DETAILS = "updateClientMaster";

    public static final String INVALID_MFA = "Invalid Credentials, Required MFA !!";

    public static final String USER_AGENT = "userAgent";

    public static final String GET_EMAILS_BY_CLIENTCODE_TYPE_ENUM = "getEmailsByClientCodeAndTypeAndEnum";
    
    public static final String GET_COSTCENTER_PRISMIZED_CLIENTS = "getCostCenterPrismizedClients";

    public static final String GET_DISTINCT_CLIENTS = "getdistinctClients";

    public static final String PRIVILEGE_CHECK = "privilegeCheck";
    
    public static final String CHANGE_LOGIN = "changeLogin";
    public static final String GET_EMPLOYEES_FOR_BULKINVITATION = "getEmployeesForBulkInvitation";
    
    public static final String GET_AD_KEYS = "updateADKeys";
    
    public static final String SEND_EMAIL_NOTIFICATIONS_FOR_SCHOOLDISTRICT = "sendEmailNotificationsForSchoolDistrict";
    
    public static final String SAVE_USER_COST_CENTERS = "saveUserCostCenters";
    
    public static final String GET_ALL_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID = "getAllUserFilterConfigsByClientCodeAndUserId";
    
    public static final String GET_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID_AND_FILTER_ID = "getUserFilterConfigsByClientCodeAndUserIdAndFilterId";
    
    public static final String DELETE_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID_AND_FILTER_ID = "DeleteUserFilterConfigByClientCodeAndUserIdAndFilterId";
    
    public static final String CRATE_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID = "createUserFilterConfig";
    
    public static final String UPDATE_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID = "updateUserFilterConfig";
    
    public static final String REINITIATE_NEWHIRE = "reInitiateNewHire";
    
    public static final String AUTHENTICATE_USER = "authenticateUser";
    
    public static final String GET_EMPLOYEE_ENDDATE = "getEmployeeEndDate";
    
    public static final String UPDATE_EMPLOYEE_ENDDATE = "updateEmployeeEndDate";
    
	public static final String UPDATE_MFA_EMAIL = "updateMfaEmail";
	
	public static final String VALIDATE_MFA_EMAIL_FOR_BULK_UPLOAD = "validateMfaEmailForBulkUpload";
    
    
    private MethodNames() {
	}
}
