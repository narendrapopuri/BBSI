

-- MENU



INSERT INTO menu_v1
(id, created_by, created_on, modified_by, modified_on, version, description, icon_url, is_active, name, parent_id, [sequence])
VALUES(1, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Administration related menu items', NULL, NULL, 'Administration', 0, 7);
INSERT INTO menu_v1
(id, created_by, created_on, modified_by, modified_on, version, description, icon_url, is_active, name, parent_id, [sequence])
VALUES(2, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Company management related menu items', NULL, NULL, 'Company', 0, 1);
INSERT INTO menu_v1
(id, created_by, created_on, modified_by, modified_on, version, description, icon_url, is_active, name, parent_id, [sequence])
VALUES(3, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Employee management related menu items', NULL, NULL, 'Employee', 0, 3);
INSERT INTO menu_v1
(id, created_by, created_on, modified_by, modified_on, version, description, icon_url, is_active, name, parent_id, [sequence])
VALUES(4, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Payroll management related menu items', NULL, NULL, 'Payroll', 0, 4);
INSERT INTO menu_v1
(id, created_by, created_on, modified_by, modified_on, version, description, icon_url, is_active, name, parent_id, [sequence])
VALUES(5, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Reporting related menu items', NULL, NULL, 'Reporting', 0, 5);
INSERT INTO menu_v1
(id, created_by, created_on, modified_by, modified_on, version, description, icon_url, is_active, name, parent_id, [sequence])
VALUES(6, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Company management related menu items', NULL, NULL, 'Benefits', 0, 2);
INSERT INTO menu_v1
(id, created_by, created_on, modified_by, modified_on, version, description, icon_url, is_active, name, parent_id, [sequence])
VALUES(7, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Company management related menu items', NULL, NULL, 'Utilities', 0, 6);
INSERT INTO menu_v1
(id, created_by, created_on, modified_by, modified_on, version, description, icon_url, is_active, name, parent_id, [sequence])
VALUES(8, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'Pay Calculators related menu items', NULL, NULL, 'PayCalculators', 0, 1);



-- MENU_ITEM


INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(2, 'anonymousUser', '2019-07-16 08:56:22.277', 'anonymousUser', '2019-07-16 08:56:22.277', 0, 'Manage Audit Log Information', 'Audit Log', NULL, 'assets/images/icon-audit-log-light.svg', NULL, 'AuditLog', NULL, 14);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(3, 'anonymousUser', '2019-07-16 08:56:22.277', 'anonymousUser', '2019-07-16 08:56:22.277', 0, 'Define Role and User Access controls', 'User Access', NULL, 'assets/images/icon-user-access-light.svg', NULL, 'RoleUserAccess', NULL, 15);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(4, 'anonymousUser', '2019-07-16 08:56:22.277', 'anonymousUser', '2019-07-16 08:56:22.277', 0, 'Define Notification templates and rules', 'Notification Management', NULL, 'assets/images/icon-notification-management-light.svg', NULL, 'Notification', NULL, 20);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(5, 'anonymousUser', '2019-07-16 08:56:22.277', 'anonymousUser', '2019-07-16 08:56:22.277', 0, 'Configure the Company Announcements', 'Announcements', NULL, 'assets/images/icon-announcements-light.svg', NULL, 'Announce', NULL, 23);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(7, 'anonymousUser', '2019-07-16 08:56:22.277', 'anonymousUser', '2019-07-16 08:56:22.277', 0, 'Manage the employee leave of absence status', 'Leave of Absence', NULL, 'assets/images/icon-leave-of-absence-light.svg', NULL, 'LOA', NULL, 24);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(8, 'anonymousUser', '2019-07-16 08:56:22.277', 'anonymousUser', '2019-07-16 08:56:22.277', 0, 'Manage the employee record either by HR or through self-service', 'Employee Management', NULL, 'assets/images/icon-employee-management-light.svg', NULL, 'Employee', NULL, 28);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(9, 'anonymousUser', '2019-07-16 08:56:22.277', 'anonymousUser', '2019-07-16 08:56:22.277', 0, 'Manage the onboarding of new employees of a client', 'New Hire Onboarding', NULL, 'assets/images/icon-new-hire-onboarding-light.svg', NULL, 'NewHire', NULL, 37);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(10, 'anonymousUser', '2019-07-16 08:56:22.277', 'anonymousUser', '2019-07-16 08:56:22.277', 0, 'Create and Manage the company employees loans', 'Loan Overview', NULL, 'assets/images/icon-loan-overview-light.svg', NULL, 'Loan', NULL, 45);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(11, 'anonymousUser', '2019-07-16 08:56:22.277', 'anonymousUser', '2019-07-16 08:56:22.277', 0, 'Create company employees by uploading a file', 'Bulk Upload', NULL, 'assets/images/icon-bulk-upload-light.svg', NULL, 'BulkUpload', NULL, 46);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(12, 'anonymousUser', '2019-07-16 08:56:22.277', 'anonymousUser', '2019-07-16 08:56:22.277', 0, 'Fill and submit the employee work hours for a payroll batch', 'Payroll Processing', NULL, 'assets/images/icon-payroll-processing-light.svg', NULL, 'Timesheet', NULL, 47);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(14, 'anonymousUser', '2019-07-16 08:56:22.277', 'anonymousUser', '2019-07-16 08:56:22.277', 0, 'List of Reports available to execute', 'Reports', NULL, 'assets/images/icon-reports-light.svg', NULL, 'Reports', NULL, 50);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(15, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'On-Boarding Document Management', 'On-Boarding Document Management', NULL, 'assets/images/icon-document-management.svg', NULL, 'On-Boarding Document Management', NULL, 51);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(16, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'Manage the Pay Calculators', 'Pay Calculators', NULL, 'assets/images/icon-pay-calender-light.svg', NULL, 'PayCalculators', NULL, 52);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(17, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'Manage the Company and its information', 'Company Management', NULL, 'assets/images/icon-company-management-light.svg', NULL, 'CompanyMgmt', NULL, 54);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(18, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'Report Library to build Reports', 'Report Library', NULL, 'assets/images/icon-reports-library-light.svg', NULL, 'ReportLibrary', NULL, 60);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(19, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'Message Center', 'Message Center', NULL, 'assets/images/icon-notification-management-light.svg', NULL, 'MessageCenter', NULL, 63);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(20, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'Configure holidays and payroll schedule', 'Calendar', NULL, 'assets/images/icon-calendar.svg', NULL, 'Calendar Settings', NULL, 68);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(21, 'anonymousUser', '2019-09-26 08:56:22.000', 'anonymousUser', '2019-09-26 08:56:22.000', 0, 'Payroll Settings management', 'Payroll Settings', NULL, 'assets/images/icon-payroll-setings-light.svg', NULL, 'Payroll Setting Management', NULL, 69);
INSERT INTO menu_item_v1
(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date, webform_id)
VALUES(22, 'anonymousUser', '2019-10-10 15:00:00.000', 'anonymousUser', '2019-10-10 15:00:00.000', 0, 'Configure Help & Training', 'Help & Training', NULL, 'assets/images/icon-help.svg', NULL, 'Help & Training', NULL, 70);



-- MENU_MAP


INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(1, 1, 1, 3);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(2, 2, 1, 2);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(7, 1, 3, 8);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(8, 2, 3, 9);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(9, 3, 3, 7);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(10, 1, 4, 12);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(11, 1, 5, 14);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(12, 1, 7, 11);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(14, 5, 3, 15);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(15, 1, 4, 16);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(16, 1, 2, 17);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(17, 2, 2, 10);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(18, 3, 2, 4);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(19, 4, 2, 5);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(20, 2, 5, 18);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(21, 5, 2, 19);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(22, 3, 1, 20);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(23, 3, 4, 21);
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id) VALUES(24, 4, 1, 22);


-- WEBFORM


INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(14, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Client Audit Log', 'audit_log', 'PAGE', '/home/auditlog', 26);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(15, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Role and User Management', 'role_user_mgmt', 'PAGE', '/home/rollmgmt', 4);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(16, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Access Groups Management', 'access_group', 'PAGE', '/home/rollmgmt/accessgroup', 27);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(17, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Roles Management', 'role_mgmt', 'PAGE', '/home/rollmgmt/roles', 28);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(18, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Client Roles Management', 'client_roles', 'PAGE', '/home/rollmgmt/client', 29);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(19, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Users Management', 'user_mgmt', 'PAGE', '/home/rollmgmt/users', 30);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(20, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Notification Management', 'notif_mgmt', 'PAGE', '/home/notif', 5);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(21, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Notification Templates Management', 'notif_template', 'PAGE', '/home/notif/notificationtemplates', 31);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(22, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Notification Rules Management', 'notif_rule', 'PAGE', '/home/notif/notificationrule', 32);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(23, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Announcements Information', 'announcement', 'PAGE', '/home/announcements', 33);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(24, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Leave Of Absence', 'leave_of_absence', 'PAGE', '/home/loa', 7);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(25, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Employee Leave Requests', 'leave_requests', 'PAGE', '/home/loa/employeeleaverequests', 34);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(26, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Leave of Absence Setup', 'loa_setup', 'PAGE', '/home/loa/leaveofabsencesetup', 35);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(27, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Archived Requests', 'archived_requests', 'PAGE', '/home/loa/archivedrequests', 36);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(28, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Employee Management', 'employee_management', 'PAGE', '/home/employeemanagement', 8);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(29, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Employee Personal Info', 'employee_management#personal', 'POPUP', '/home/employeemanagement#personal', 37);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(30, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Employee Job Info', 'employee_management#job', 'POPUP', '/home/employeemanagement#job', 38);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(31, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Employee Pay & Deductions Info', 'employee_management#pay', 'POPUP', '/home/employeemanagement#pay', 39);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(32, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Employee Payment history Info', 'employee_management#history', 'POPUP', '/home/employeemanagement#history', 40);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(33, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Employee Benefit Info', 'employee_management#benefit', 'POPUP', '/home/employeemanagement#benefit', 41);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(34, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Employee Pay TimeOff Info', 'employee_management#pto', 'POPUP', '/home/employeemanagement#pto', 42);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(35, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Employee Development Info', 'employee_management#dev', 'POPUP', '/home/employeemanagement#dev', 43);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(36, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Employee Documents Info', 'employee_management#doc', 'POPUP', '/home/employeemanagement#doc', 44);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(37, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'New Hire Onboarding list', 'new_hire', 'PAGE', '/home/client/new-hire-list', 9);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(38, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'New Hire Onboarding', 'new_hire_add', 'PAGE', '/home/client/new-hire', 45);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(39, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'New Hire Personal Info', 'new_hire_personal', 'PAGE', '/home/new-hire/manual-eletronic/personal', 46);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(40, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'New Hire Job & Pay Info', 'new_hire_job', 'PAGE', '/home/new-hire/manual-eletronic/jobPay', 47);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(41, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'New Hire Tax Info', 'new_hire_tax', 'PAGE', '/home/new-hire/manual-eletronic/tax', 48);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(42, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'New Hire I9 Details Info', 'new_hire_i9', 'PAGE', '/home/new-hire/manual-eletronic/detailsi9', 49);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(43, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'New Hire Documents Info', 'new_hire_docs', 'PAGE', '/home/new-hire/manual-eletronic/documents', 50);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(44, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'New Hire Review & Submit', 'new_hire_review', 'PAGE', '/home/new-hire/manual-eletronic/review', 51);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(45, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Employee Loans', 'employee_loans', 'PAGE', '/home/employeeloans', 52);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(46, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Bulk Employee Onboarding processs', 'bulk_upload', 'PAGE', '/home/bulkupload', 53);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(47, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'List of payroll batches', 'payroll_batch', 'PAGE', '/home/payroll/list', 54);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(48, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Timesheet Entry Hours', 'manual_timesheet', 'PAGE', '/home/payroll/manual-timesheet', 55);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(50, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'Reports', 'reports', 'PAGE', '/home/reports', 112);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(51, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'On-Boarding Document Management', 'on_boarding_document_management', 'PAGE', '/home/onboarding-docs', 114);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(52, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'Client Management', 'pay_calculators', 'PAGE', '/home/payroll/pay-cal', 119);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(53, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'Client Workers Comp Class', 'gross_to_net_calculator', 'PAGE', '/home/payroll/pay-cal/#gtncal', 120);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(54, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'Client Management', 'company_management', 'PAGE', '/home/companymanagement', 130);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(55, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'Client Management', 'company_management_info', 'PAGE', '/home/companymanagement/info', 131);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(56, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'Client Management', 'company_management_orgstr', 'PAGE', '/home/companymanagement/orgstr', 132);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(57, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'Client Management', 'company_management_empdev', 'PAGE', '/home/companymanagement/empdev', 133);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(58, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'Client Management', 'company_management_account', 'PAGE', '/home/companymanagement/account', 134);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(59, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'Client Management', 'company_management_insurance', 'PAGE', '/home/companymanagement/insurance', 135);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(60, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'Report Library', 'report_library', 'PAGE', '/home/reports/report-library', 152);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(61, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'New Hire Onboarding employee List', 'new_hire_onboarding_employee', 'PAGE', '/home/client/new-hire-list', 155);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(62, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'New Hire Onboarding document List', 'new_hire_onboarding_documents', 'PAGE', '/home/client/onboarding-documents', 156);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(63, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'Company', 'message_center', 'PAGE', '/home/messagecenter', 157);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(64, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'Company', 'message_center_notifications', 'PAGE', '/home/messagecenter/notifications', 158);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(65, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'Company', 'message_center_announcements', 'PAGE', '/home/messagecenter/announcements', 159);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(66, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'Company', 'message_center_approvals', 'PAGE', '/home/messagecenter/approvals', 160);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(67, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'Gross to Net Calculator', 'Gross to Net Calculator', 'PAGE', '/home/payroll/pay-cal/net-gross-cal', 161);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(68, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'Configure holidays and pay schedule', 'Calendar Settings', 'PAGE', '/home/calendarsetup', 162);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(69, 'anonymousUser', '2019-09-26 08:56:22.000', 'anonymousUser', '2019-09-26 08:56:22.000', 0, 'Payroll Setting Management', 'Payroll Setting Management', 'PAGE', '/home/payroll/settings', 165);
INSERT INTO webform_v1
(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, feature_code_id)
VALUES(70, 'anonymousUser', '2019-10-10 15:00:00.000', 'anonymousUser', '2019-10-10 15:00:00.000', 0, 'Configure Help & Training', 'Help & Training', 'PAGE', '/home', 168);


-- FEATURE_CODES


INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(3, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'AUD', 'Audit Log', 'Feature Group', 'Audit Log', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(4, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT', 'Role and User Access Management', 'Feature Group', 'Role & User Access', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(5, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NOTF_MGMT', 'Notification Management', 'Feature Group', 'Notification Management', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(6, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ANNC', 'Announcements Information', 'Feature Group', 'Announcements', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(7, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'LOA', 'Leave Of Absence Management', 'Feature Group', 'Leave Of Absence', 0, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(8, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT', 'Employee Management', 'Feature Group', 'Employee Management', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(9, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE', 'New Hire Onboarding', 'Feature Group', 'New Hire Onboarding', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(10, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_LOAN', 'Employee Loans', 'Feature Group', 'Employee Loans', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(11, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'BLK_UPLD', 'Bulk Upload Employee Information', 'Feature Group', 'Bulk Upload', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(12, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS', 'Time sheet', 'Feature Group', 'Time sheet', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(14, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'CLI_INF', 'Client Information', 'Feature', 'Client Info', 1, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(26, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'AUD_LOG', 'Application Audit Log', 'Feature', 'Audit Log', 3, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(27, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT_AGRP', 'Access Group management', 'Feature', 'Access Groups', 4, 1, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(28, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT_ROLE', 'Role management', 'Feature', 'Roles', 4, 2, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(29, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT_CLNT', 'Client roles allocation', 'Feature', 'Clients', 4, 3, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(30, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT_USER', 'User management', 'Feature', 'Users', 4, 4, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(31, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NOTF_MGMT_TMPL', 'Notification Templates Management', 'Feature', 'Notification Templates', 5, 1, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(32, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NOTF_MGMT_RULE', 'Notification Rule Management', 'Feature', 'Notification Rule', 5, 2, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(33, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ANNC_INF', 'Announcements Information', 'Feature', 'Announcements', 6, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(34, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'LOA_LEA_REQ', 'Employee Leave Requests', 'Feature', 'Employee Leave Requests', 7, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(35, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'LOA_LEA_STP', 'Leave of Absence Setup', 'Feature', 'Leave of Absence Setup', 7, 2, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(36, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'LOA_LEA_ARC', 'Archived Requests', 'Feature', 'Archived Requests', 7, 3, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(37, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER', 'Personal Information', 'Feature', 'Personal', 8, 1, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(38, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_JOB', 'Job Information', 'Feature', 'Job', 8, 2, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(39, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY', 'Pay & Deductions Information', 'Feature', 'Pay & Deductions', 8, 3, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(40, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAYHIST', 'Payment History Information', 'Feature', 'Payment History', 8, 4, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(41, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN', 'Benefit Information', 'Feature', 'Benefits', 8, 5, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(42, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PTO', 'Time Off Information', 'Feature', 'Time Off', 8, 6, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(43, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DEV', 'Employee Development Information', 'Feature', 'Employee Development', 8, 7, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(44, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DOC', 'Employee documents Information', 'Feature', 'Documents', 8, 8, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(45, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_ADD', 'New Hire Onboarding', 'Feature', 'New Hire Onboarding', 155, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(46, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER', 'Personal Information', 'Feature', 'Personal', 155, 1, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(47, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_JOB', 'Job & Pay Information', 'Feature', 'Job & Pay', 155, 2, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(48, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_TAX', 'Tax Information', 'Feature', 'Tax', 155, 3, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(49, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9', 'I-9 details Information', 'Feature', 'I-9 Details', 155, 4, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(50, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_DOC', 'Documents Information', 'Feature', 'Documents', 155, 5, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(51, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_REVW', 'Review & Submit Information', 'Feature', 'Review', 155, 6, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(52, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_LOAN_INF', 'Employee Loan Information', 'Feature', 'Loan Overview', 10, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(53, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'BLK_UPLD_INF', 'Bulk Employee Onboarding processs', 'Feature', 'Bulk Employee Onboarding', 11, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(54, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_INF', 'Payroll Batch Information', 'Feature', 'Payrolls Batch', 12, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(55, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR', 'Manual Timesheet Entry', 'Feature', 'Manual Timesheet Entry', 54, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(56, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'TS_UPLD_TMPL', 'Upload Timesheet for the Batch & Template', 'Feature', 'Upload Timesheet', 13, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(64, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ANNC_INF_EMP', 'Employee selection for Announcements', 'Section', 'Select Employees', 33, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(65, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ANNC_INF_HIS', 'History of Announcements sent', 'Section', 'Announcements History', 33, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(66, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_PER', 'Personal Information', 'Section', 'Personal', 37, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(67, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_CNTCT', 'Contact Information', 'Section', 'Contact Information', 37, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(68, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_RADD', 'Residential Address Information', 'Section', 'Residential Address', 37, 3, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(69, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_MADD', 'Mailing Address Information', 'Section', 'Mailing Address', 37, 4, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(70, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_EEO', 'EEO Opportunity & Veteran Status', 'Section', 'Veteran Status', 37, 5, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(71, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_EMER', 'Emergency Contact Information', 'Section', 'Emergency Contact', 37, 6, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(72, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_JOB_EMP', 'Employment Information', 'Section', 'Employment', 38, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(73, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_JOB_ASSIN', 'Primary Work Assignment Information', 'Section', 'Primary Work Assignment', 38, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(74, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_JOB_ALLOC', 'Labor Allocation Information', 'Section', 'Labor Allocation', 38, 3, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(75, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_DD', 'Direct Deposit Information', 'Section', 'Direct Deposit', 39, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(76, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_COMP', 'Compensation Information', 'Section', 'Compensation', 39, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(77, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_RATE', 'Alternate Rates Information', 'Section', 'Alternate Rates', 39, 3, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(78, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_TAX', 'Tax Filing Information', 'Section', 'Tax Filing Information', 39, 4, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(79, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_PDED', 'Payroll Deduction & Payments Information', 'Section', 'Payments & Deductions', 39, 5, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(80, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_LOAN', 'Employee Loans Information', 'Section', 'Employee Loans', 39, 6, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(81, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAYHIST_SUMM', 'Payroll Summary Information', 'Section', 'Payroll Summary', 40, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(82, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAYHIST_STUB', 'Payment History Records - Pay Stubs Information', 'Section', 'Payment History Records - Pay Stubs', 40, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(83, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN_SUMM', 'Benefit Summary Information', 'Section', 'Benefit Overview', 41, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(84, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN_RTIR', 'Retirement Summary Information', 'Section', 'Retirement Summary', 41, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(85, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN_DEP', 'Dependents & Beneficiaries Information', 'Section', 'Dependents & Beneficiaries', 41, 3, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(86, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN_MEXP', 'Medical Expense Account Summary Information', 'Section', 'Medical Expense Account Summary', 41, 4, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(87, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DEV_PREFRW', 'Performance Review Information', 'Section', 'Performance Review', 43, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(88, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DEV_EVNTLG', 'Event Log Information', 'Section', 'Event Log', 43, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(89, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DEV_SKLEDU', 'Competencies, Skills & Education Information', 'Section', 'Competencies, Skills & Education', 43, 3, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(90, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_PER', 'Personal Information', 'Section', 'Personal Information', 46, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(91, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_CNTCT', 'Contact Information', 'Section', 'Contact Information', 46, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(92, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_RADD', 'Residential Address Information', 'Section', 'Residential Address', 46, 3, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(93, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_EMER', 'Emergency Contact Information', 'Section', 'Emergency Contact', 46, 4, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(94, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_DRIV', 'Driver''s License Information', 'Section', 'Driver''s License', 46, 5, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(95, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_EEO', 'EEO Opportunity & Veteran Self-Identification Information', 'Section', 'Veteran', 46, 6, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(96, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_DD', 'Direct Deposit Information', 'Section', 'Direct Deposit', 46, 7, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(97, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_JOB_EMP', 'Employment Details Information', 'Section', 'Employment Details', 47, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(98, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_JOB_PDET', 'Pay Details Information', 'Section', 'Pay Details', 47, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(99, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_TAX_FED', 'Federal Form W4 Details Information', 'Section', 'Federal Form W4 Details', 48, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(100, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_TAX_STAT', 'Home State Tax Form Information', 'Section', 'Home State Tax Form', 48, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(101, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9_INF', 'I-9 Information', 'Section', 'I-9 Information', 49, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(102, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9_DET', 'I-9 Details Information', 'Section', 'I-9 Details', 49, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(103, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'BLK_UPLD_INF_DT', 'Download a template with prefilled fields', 'Section', 'Download Template', 53, 7, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(104, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'BLK_UPLD_INF_LT', 'Upload the data in the downloaded template format', 'Section', 'Load Template', 53, 7, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(105, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_CLR', 'Clear Manual Timesheet Entry', 'Section', 'Clear Sheet', 55, 1, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(106, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_IMPT', 'Import Time Sheet Entries from a file', 'Section', 'Import Time Sheet', 55, 2, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(107, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_ACTN', 'Manual Timesheet Actions', 'Section', 'Action', 55, 3, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(108, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_PDF', 'Show Calculated Manual Timesheet hours in PDF', 'Section', 'Show PDF', 55, 4, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(109, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_SAVE', 'Save Manual Timesheet hours', 'Section', 'Save', 55, 5, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(110, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_CALC', 'Calculate Manual Timesheet hours', 'Section', 'Calculate', 55, 6, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(111, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_APPR', 'Approve Manual Timesheet hours', 'Section', 'Approve', 55, 7, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(112, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'REPORT', 'Report', 'Feature Group', 'Reports', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(113, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'REPORTS', 'Reports', 'Feature', 'Reports', 112, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(114, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ON_BRDNG_DOC', 'On-Boarding Document', 'Feature Group', 'On-Boarding Document', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(115, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ON_BRDNG_DOC_MNGT', 'On-Boarding Document Management', 'Feature', 'On-Boarding Document Management', 114, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(116, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PTO_TOREQ', 'Time Off Requests Information', 'Section', 'Time Off Requests', 42, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(117, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PTO_PTOSUM', 'Paid Time Off Summaries Information', 'Section', 'Paid Time Off Summaries', 42, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(118, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PTO_LOAREQ', 'Leave Of Absence Request Information', 'Section', 'Leave Of Absence Request', 42, 3, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(119, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_CALS', 'Pay Calculators', 'Feature Group', 'Pay Calculators', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(120, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_CALS_GTNCAL', 'Gross to Net Calculator', 'Feature', 'Gross to Net Calculator', 119, 1, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(121, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_CLR', 'Clear Timesheet Entry', 'Section', 'Clear Timesheet', 54, 1, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(122, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_IMPT', 'Import Time Sheet Entries from a file', 'Section', 'Import Time Sheet', 54, 2, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(123, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_SAVE', 'Save Timesheet hours', 'Section', 'Save', 54, 3, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(124, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_SUBMIT', 'Submit Timesheet hours', 'Section', 'Submit', 54, 4, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(125, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_REJECT', 'Reject Timesheet hours', 'Section', 'Reject', 54, 5, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(126, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_CALC', 'Calculate Timesheet
 hours', 'Section', 'Calculate', 54, 6, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(127, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_PAGE', 'Payroll Page', 'Section', 'Payroll Page', 54, 7, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(128, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_APPROVAL_REPORTS', 'Approval Reports Manual Timesheet hours', 'Section', 'Approval Reports', 54, 8, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(129, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_FINALIZE', 'Finalize Timesheet hours', 'Section', 'Finalize', 54, 9, 'BUTTON');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(130, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT', 'Company Management', 'Feature Group', 'Company Management', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(131, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_INF', 'Information', 'Feature', 'Information', 130, 1, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(132, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC', 'Organazation Structure', 'Feature', 'Organazation Structure', 130, 2, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(133, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV', 'Employee Development', 'Feature', 'Employee Development', 130, 3, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(134, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ACC', 'Account', 'Feature', 'Account', 130, 4, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(135, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_INSURANCE', 'Insurance', 'Feature', 'Insurance', 130, 5, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(136, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_DEP', 'Department', 'Section', 'Department', 132, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(137, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_DIV', 'Division', 'Section', 'Division', 132, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(138, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_PROJ', 'Project', 'Section', 'Project', 132, 3, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(139, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_SHIFT', 'Shift', 'Section', 'Shift', 132, 4, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(140, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_UNION', 'Union', 'Section', 'Union', 132, 5, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(141, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_WLOC', 'Worksite Location', 'Section', 'Worksite Location', 132, 6, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(142, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_COURSE', 'Course', 'Section', 'Course', 133, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(143, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_EVENT', 'Event', 'Section', 'Event', 133, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(144, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_OCC', 'Occupation', 'Section', 'Occupation', 133, 3, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(145, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_PER_REW', 'Performance Review', 'Section', 'Performance Review', 133, 4, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(146, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_SKILL', 'Skill', 'Section', 'Skill', 133, 5, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(147, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_WGROUP', 'Work Group', 'Section', 'Work Group', 133, 6, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(148, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ACC_PBANKINFO', 'Primary Bank Information', 'Section', 'Primary Bank Information', 134, 1, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(149, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ACC_TAXINFO', 'Tax Information', 'Section', 'Tax Information', 134, 2, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(150, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'EMP_MGMT_PER_DRIV', 'Driver''s License Information', 'Section', 'Driver''s License', 37, 7, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(151, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'REPORT_LIBRARY', 'Report Library', 'Feature Group', 'Report Library', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(152, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'REPORT_LIBRARY_MGMT', 'Report Library', 'Feature', 'Report Library', 151, 4, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(153, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9_APPROVER_FG', 'I9 Approver', 'Feature Group', 'I-9 Approver Feature Group', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(154, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'NEW_HIRE_I9_APPROVER_F', 'I-9 Approver', 'Feature', 'I-9 Approver', 153, 1, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(155, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'NEW_HIRE_EMP_ONBOARDING', 'Newhire employee onboarding information', 'Feature', 'Employee Onboarding', 9, 7, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(156, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'NEW_HIRE_ONBOARDING_DOCUMENTS', 'Newhire onboarding documents information', 'Feature', 'Onboarding Documents', 9, 8, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(157, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'MSG_CENTER', 'Message Center', 'Feature Group', 'Message Center', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(158, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'MSG_CENTER_NOTIF', 'Message Center Notifications', 'Feature', 'Notifications', 157, 1, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(159, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'MSG_CENTER_ANNCMNT', 'Message Center Announcements', 'Feature', 'Announcements', 157, 2, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(160, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'MSG_CENTER_APPR', 'Message Center Approvals', 'Feature', 'Approvals', 157, 3, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(161, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_CALS_NTGCAL', 'Net to Gross Calculator', 'Feature', 'Net to Gross Calculator', 119, 2, 'TAB');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(162, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'CAL_SETUP_MGMT', 'Setup holiday calendar', 'Feature Group', 'Calendar Settings', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(163, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'CAL_SETUP', 'Setup holday calendar', 'Feature', 'Calendar Settings', 162, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(164, 'anonymousUser', '2019-09-26 08:56:22.000', 'anonymousUser', '2019-09-26 08:56:22.000', 0, 'PAYROLL_SETTING', 'Payroll Settings', 'Feature Group', 'Payroll Settings', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(165, 'anonymousUser', '2019-09-26 08:56:22.000', 'anonymousUser', '2019-09-26 08:56:22.000', 0, 'PAYROLL_SETTING_MNGT', 'Payroll Setting Management', 'Feature', 'Payroll Setting Management', 164, 1, 'PAGE');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(166, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_I9_INFO', 'I-9 Information', 'Section', 'I-9 Information', 37, 8, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(167, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_EMP_ELI_DOC', 'Employment Eligibility Documentation', 'Section', 'Employment Eligibility Documentation', 37, 9, 'ACCORDIAN');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(168, 'anonymousUser', '2019-10-10 08:56:22.000', 'anonymousUser', '2019-10-10 08:56:22.000', 0, 'HELP_TRAINING_MGMT', 'Help & Training', 'Feature Group', 'Help & Training', 0, 1, 'GROUP');
INSERT INTO feature_code_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, seq_num, [type])
VALUES(169, 'anonymousUser', '2019-10-10 08:56:22.000', 'anonymousUser', '2019-10-10 08:56:22.000', 0, 'HELP_TRAINING', 'Help & Training', 'Feature', 'Help & Training', 168, 1, 'PAGE');



-- PRIVILEGES


INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(3, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'CLI_INF.ALL', 'Manage Client Info', 'ALL', 14);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(4, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'CLI_INF.VIEW', 'View Client Info', 'VIEW', 14);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(43, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'AUD.ALL', 'Manage the application audit log Feature Group', 'ALL', 3);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(44, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'AUD.VIEW', 'Show the application audit log Feature Group', 'VIEW', 3);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(45, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'AUD_LOG.ALL', 'Manage Application Audit Log', 'ALL', 26);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(46, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'AUD_LOG.VIEW', 'View Application Audit Log', 'VIEW', 26);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(47, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT.ALL', 'Show Role and Uer Access Management Feature Group', 'ALL', 4);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(48, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT.VIEW', 'Show Role and Uer Access Management Feature Group', 'VIEW', 4);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(49, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT_AGRP.ALL', 'Manage Access Groups', 'ALL', 27);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(50, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT_AGRP.VIEW', 'View Access Groups', 'VIEW', 27);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(51, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT_ROLE.ALL', 'Manage Roles', 'ALL', 28);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(52, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT_ROLE.VIEW', 'View Roles', 'VIEW', 28);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(53, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT_CLNT.ALL', 'Manage Client Roles', 'ALL', 29);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(54, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT_CLNT.VIEW', 'View Client Roles', 'VIEW', 29);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(55, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT_USER.ALL', 'Manage Users', 'ALL', 30);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(56, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ROL_MGMT_USER.VIEW', 'View Users', 'VIEW', 30);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(57, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NOTF_MGMT.ALL', 'Show Notification Management Feature Group', 'ALL', 5);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(58, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NOTF_MGMT.VIEW', 'Show Notification Management Feature Group', 'VIEW', 5);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(59, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NOTF_MGMT_TMPL.ALL', 'Manage Notification Templates', 'ALL', 31);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(60, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NOTF_MGMT_TMPL.VIEW', 'View Notification Templates', 'VIEW', 31);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(61, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NOTF_MGMT_RULE.ALL', 'Manage Notification Rules', 'ALL', 32);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(62, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NOTF_MGMT_RULE.VIEW', 'View Notification Rules', 'VIEW', 32);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(63, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ANNC.ALL', 'Show Announcements Feature Group', 'ALL', 6);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(64, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ANNC.VIEW', 'Show Announcements Feature Group', 'VIEW', 6);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(65, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ANNC_INF.ALL', 'Manage Announcements', 'ALL', 33);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(66, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ANNC_INF.VIEW', 'View Announcements', 'VIEW', 33);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(67, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ANNC_INF_EMP.ALL', 'Manage Announcement Creation', 'ALL', 64);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(68, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ANNC_INF_EMP.VIEW', 'View Created Announcements', 'VIEW', 64);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(69, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ANNC_INF_HIS.ALL', 'Manage Announcement History, Resending', 'ALL', 65);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(70, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ANNC_INF_HIS.VIEW', 'View Announcement History', 'VIEW', 65);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(71, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'LOA.ALL', 'Show Leave Of Absence Feature Group', 'ALL', 7);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(72, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'LOA.VIEW', 'Show Leave Of Absence Feature Group', 'VIEW', 7);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(73, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'LOA_LEA_REQ.ALL', 'Manage Employee Leave Requests', 'ALL', 34);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(74, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'LOA_LEA_REQ.VIEW', 'View Employee Leave Requests', 'VIEW', 34);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(75, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'LOA_LEA_STP.ALL', 'Manage Leave Of Absence Setup', 'ALL', 35);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(76, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'LOA_LEA_STP.VIEW', 'View Leave Of Absence Setup', 'VIEW', 35);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(77, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'LOA_LEA_ARC.ALL', 'Manage Archived Leave Requests', 'ALL', 36);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(78, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'LOA_LEA_ARC.VIEW', 'View Archived Leave Requests', 'VIEW', 36);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(79, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT.ALL', 'Show Client Employees List Feature Group', 'ALL', 8);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(80, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT.VIEW', 'Show Client Employees List Feature Group', 'VIEW', 8);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(81, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER.ALL', 'Manage Employee Personal Information', 'ALL', 37);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(82, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER.VIEW', 'View Employee Personal Information', 'VIEW', 37);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(83, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_JOB.ALL', 'Manage Employee Job Information', 'ALL', 38);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(84, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_JOB.VIEW', 'View Employee Job Information', 'VIEW', 38);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(85, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY.ALL', 'Manage Employee Pay & Deductions Information', 'ALL', 39);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(86, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY.VIEW', 'View Employee Pay & Deductions Information', 'VIEW', 39);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(87, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAYHIST.ALL', 'View Employee Job Information', 'ALL', 40);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(88, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAYHIST.VIEW', 'View Employee Job Information', 'VIEW', 40);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(89, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN.ALL', 'Manage Employee Benefits Information', 'ALL', 41);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(90, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN.VIEW', 'View Employee Benefits Information', 'VIEW', 41);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(91, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PTO.ALL', 'Manage Employee Pay TimeOff Information', 'ALL', 42);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(92, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PTO.VIEW', 'View Employee Pay TimeOff Information', 'VIEW', 42);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(93, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DEV.ALL', 'Manage Employee Development Information', 'ALL', 43);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(94, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DEV.VIEW', 'View Employee Development Information', 'VIEW', 43);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(95, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DOC.ALL', 'Manage Employee Documents Information', 'ALL', 44);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(96, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DOC.VIEW', 'View Employee Documents Information', 'VIEW', 44);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(97, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_PER.ALL', 'Manage Employee Personal Information section', 'ALL', 66);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(98, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_PER.VIEW', 'View Employee Personal Information section', 'VIEW', 66);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(99, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_CNTCT.ALL', 'Manage Employee Contact Information section', 'ALL', 67);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(100, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_CNTCT.VIEW', 'View Employee Contact Information section', 'VIEW', 67);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(101, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_RADD.ALL', 'Manage Employee Residential Address Information section', 'ALL', 68);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(102, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_RADD.VIEW', 'View Employee Residential Address Information section', 'VIEW', 68);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(103, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_EEO.ALL', 'Manage Employee EEO Information section', 'ALL', 70);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(104, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_EEO.VIEW', 'View Employee EEO Information section', 'VIEW', 70);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(105, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_EMER.ALL', 'Manage Employee Emergency Contact Information section', 'ALL', 71);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(106, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_EMER.VIEW', 'View Employee Emergency Contact Information section', 'VIEW', 71);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(107, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_DRIV.ALL', 'Manage Employee Driver''s License Information section', 'ALL', 150);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(108, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PER_DRIV.VIEW', 'View Employee Driver''s License Information section', 'VIEW', 150);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(109, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_JOB_EMP.ALL', 'Manage Employee Employment Information section', 'ALL', 72);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(110, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_JOB_EMP.VIEW', 'View Employee Employment Information section', 'VIEW', 72);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(111, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_JOB_ASSIN.ALL', 'Manage Employee Primary Work Assignment Information section', 'ALL', 73);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(112, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_JOB_ASSIN.VIEW', 'View Employee Primary Work Assignment Information section', 'VIEW', 73);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(113, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_JOB_ALLOC.ALL', 'Manage Employee Labour Allocation Information section', 'ALL', 74);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(114, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_JOB_ALLOC.VIEW', 'View Employee Labour Allocation Information section', 'VIEW', 74);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(115, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_DD.ALL', 'Manage Employee Direct Deposit Information section', 'ALL', 75);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(116, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_DD.VIEW', 'View Employee Direct Deposit Information section', 'VIEW', 75);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(117, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_COMP.ALL', 'Manage Employee Compensation Information section', 'ALL', 76);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(118, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_COMP.VIEW', 'View Employee Compensation Information section', 'VIEW', 76);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(119, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_RATE.ALL', 'Manage Employee Alternate Rate Information section', 'ALL', 77);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(120, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_RATE.VIEW', 'View Employee Alternate Rate Information section', 'VIEW', 77);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(121, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_TAX.ALL', 'Manage Employee Tax Filing Information section', 'ALL', 78);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(122, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_TAX.VIEW', 'View Employee Tax Filing Information section', 'VIEW', 78);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(123, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_PDED.ALL', 'Manage Employee Payroll Deduction & Payments Information section', 'ALL', 79);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(124, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_PDED.VIEW', 'View Employee Payroll Deduction & Payments Information section', 'VIEW', 79);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(125, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_LOAN.ALL', 'Manage Employee Loans Information section', 'ALL', 80);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(126, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAY_LOAN.VIEW', 'View Employee Loans Information section', 'VIEW', 80);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(127, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAYHIST_SUMM.ALL', 'Manage Employee Payroll Summary Information section', 'ALL', 81);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(128, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAYHIST_SUMM.VIEW', 'View Employee Payroll Summary Information section', 'VIEW', 81);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(129, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAYHIST_STUB.ALL', 'Manage Employee Payment history record (check stubs) Information section', 'ALL', 82);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(130, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PAYHIST_STUB.VIEW', 'View Employee Payment history record (check stubs) Information section', 'VIEW', 82);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(131, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN_SUMM.ALL', 'Manage Employee Benefit Summary Information section', 'ALL', 83);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(132, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN_SUMM.VIEW', 'View Employee Benefit Summary Information section', 'VIEW', 83);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(133, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN_RTIR.ALL', 'Manage Employee Retirement Summary Information section', 'ALL', 84);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(134, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN_RTIR.VIEW', 'View Employee Retirement Summary Information section', 'VIEW', 84);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(135, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN_DEP.ALL', 'Manage Employee Dependents & Beneficiaries Information section', 'ALL', 85);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(136, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN_DEP.VIEW', 'View Employee Dependents & Beneficiaries Information section', 'VIEW', 85);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(137, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN_MEXP.ALL', 'Manage Employee Medical Expense Account Information section', 'ALL', 86);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(138, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_BEN_MEXP.VIEW', 'View Employee Medical Expense Account Information section', 'VIEW', 86);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(139, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DEV_PREFRW.ALL', 'Manage Employee Performance Review Information section', 'ALL', 87);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(140, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DEV_PREFRW.VIEW', 'View Employee Performance Review Information section', 'VIEW', 87);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(141, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DEV_EVNTLG.ALL', 'Manage Employee Event Log Information section', 'ALL', 88);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(142, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DEV_EVNTLG.VIEW', 'View Employee Event Log Information section', 'VIEW', 88);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(143, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DEV_SKLEDU.ALL', 'Manage Employee Competencies, Skills & Education Information section', 'ALL', 89);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(144, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_DEV_SKLEDU.VIEW', 'View Employee Competencies, Skills & Education Information section', 'VIEW', 89);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(145, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE.ALL', 'Show List of Onboarding Employees Feature Group', 'ALL', 9);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(146, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE.VIEW', 'Show List of Onboarding Employees Feature Group', 'VIEW', 9);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(147, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_ADD.ALL', 'Create a new Onboarding Employee for a client', 'ALL', 45);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(148, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_ADD.VIEW', 'View Onboarding Employee for a client', 'VIEW', 45);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(149, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER.ALL', 'Manage New Hire Personal Information', 'ALL', 46);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(150, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER.VIEW', 'View New Hire Personal Information', 'VIEW', 46);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(151, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_JOB.ALL', 'Manage New Hire Job & Pay Information', 'ALL', 47);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(152, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_JOB.VIEW', 'View New Hire Job & Pay Information', 'VIEW', 47);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(153, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_TAX.ALL', 'Manage New Hire Tax Information', 'ALL', 48);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(154, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_TAX.VIEW', 'View New Hire Tax Information', 'VIEW', 48);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(155, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9.ALL', 'Manage New Hire I-9 Details Information', 'ALL', 49);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(156, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9.VIEW', 'View New Hire I-9 Details Information', 'VIEW', 49);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(157, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_DOC.ALL', 'Manage New Hire documents Information', 'ALL', 50);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(158, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_DOC.VIEW', 'View New Hire documents Information', 'VIEW', 50);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(159, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_REVW.ALL', 'Review & Submit the New Hire Information', 'ALL', 51);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(160, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_REVW.View', 'Review & Submit the New Hire Information', 'VIEW', 51);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(161, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_LOAN.ALL', 'Show Employee Loans Feature Group', 'ALL', 10);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(162, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_LOAN.VIEW', 'Show Employee Loans Feature Group', 'VIEW', 10);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(163, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_LOAN_INF.ALL', 'Manage Employee Loans Info', 'ALL', 52);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(164, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_LOAN_INF.VIEW', 'View Employee Loans Info', 'VIEW', 52);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(165, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'BLK_UPLD.ALL', 'Show Bulk Employee Upload Feature Group', 'ALL', 11);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(166, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'BLK_UPLD.VIEW', 'Show Bulk Employee Upload Feature Group', 'VIEW', 11);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(167, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'BLK_UPLD_INF.ALL', 'Process Bulk Employee Upload', 'ALL', 53);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(168, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'BLK_UPLD_INF.VIEW', 'Process Bulk Employee Upload', 'VIEW', 53);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(169, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'BLK_UPLD_INF_DT.ALL', 'Template Download for employee onboarding', 'ALL', 103);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(170, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'BLK_UPLD_INF_DT.VIEW', 'Template Download for employee onboarding', 'VIEW', 103);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(171, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'BLK_UPLD_INF_LT.ALL', 'Load the Downloaded Template with employee onboarding data', 'ALL', 104);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(172, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'BLK_UPLD_INF_LT.VIEW', 'Load the Downloaded Template with employee onboarding data', 'VIEW', 104);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(173, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS.ALL', 'Show Payroll Batches Feature Group', 'ALL', 12);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(174, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS.VIEW', 'Show Payroll Batches Feature Group', 'VIEW', 12);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(175, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_INF.ALL', 'Manage the Payroll batches', 'ALL', 54);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(176, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_INF.VIEW', 'View the Payroll batches', 'VIEW', 54);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(177, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR.ALL', 'Manage the Payroll batch Timesheet entry', 'ALL', 55);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(178, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR.VIEW', 'View the Payroll batch Timesheet entry', 'VIEW', 55);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(179, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_CLR.ALL', 'Enable Clear button for Timesheet entries', 'ALL', 105);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(180, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_CLR.VIEW', 'Enable Clear button for Timesheet entries', 'VIEW', 105);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(181, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_IMPT.ALL', 'Enable Import Time Sheet button for importing time from a file', 'ALL', 106);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(182, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_IMPT.VIEW', 'Enable Import Time Sheet button for importing time from a file', 'VIEW', 106);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(183, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_ACTN.ALL', 'Enable Action button to update current Payroll batch', 'ALL', 107);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(184, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_ACTN.VIEW', 'Enable Action button to update current Payroll batch', 'VIEW', 107);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(185, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_PDF.ALL', 'Enable Show PDF button to display Payroll batch summary', 'ALL', 108);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(186, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_PDF.VIEW', 'Enable Show PDF button to display Payroll batch summary', 'VIEW', 108);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(187, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_SAVE.ALL', 'Enable Save button to save manual timesheet entries', 'ALL', 109);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(188, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_SAVE.VIEW', 'Enable Save button to save manual timesheet entries', 'VIEW', 109);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(189, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_CALC.ALL', 'Enable Calculate button to calculate manual timesheet hours against the rates', 'ALL', 110);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(190, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_CALC.VIEW', 'Enable Calculate button to calculate manual timesheet hours against the rates', 'VIEW', 110);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(191, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_APPR.ALL', 'Enable Approve button to approve the manual timesheet entries', 'ALL', 111);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(192, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'PAY_TS_HOUR_APPR.VIEW', 'Enable Approve button to approve the manual timesheet entries', 'VIEW', 111);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(195, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'TS_UPLD_TMPL.ALL', 'Manage Upload/Import Timesheet entries for batch using a template', 'ALL', 56);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(196, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'TS_UPLD_TMPL.VIEW', 'Manage Upload/Import Timesheet entries for batch using a template', 'VIEW', 56);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(199, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_PER.ALL', 'Show Client Info section', 'ALL', 90);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(200, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_PER.VIEW', 'Show Client Info section', 'VIEW', 90);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(201, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_EMER.ALL', 'Show Client Info section', 'ALL', 93);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(202, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_EMER.VIEW', 'Show Client Info section', 'VIEW', 93);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(203, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_DD.ALL', 'Show Client Info section', 'ALL', 96);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(204, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_DD.VIEW', 'Show Client Info section', 'VIEW', 96);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(205, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_DRIV.ALL', 'Show Client Info section', 'ALL', 94);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(206, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_DRIV.VIEW', 'Show Client Info section', 'VIEW', 94);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(207, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_EEO.ALL', 'Show Client Info section', 'ALL', 95);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(208, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_EEO.VIEW', 'Show Client Info section', 'VIEW', 95);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(209, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_CNTCT.ALL', 'Show Client Info section', 'ALL', 91);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(210, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_CNTCT.VIEW', 'Show Client Info section', 'VIEW', 91);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(211, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_JOB_EMP.ALL', 'Show Client Info section', 'ALL', 97);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(212, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_JOB_EMP.VIEW', 'Show Client Info section', 'VIEW', 97);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(213, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_TAX_FED.ALL', 'Show Client Info section', 'ALL', 99);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(214, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_TAX_FED.VIEW', 'Show Client Info section', 'VIEW', 99);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(215, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_TAX_STAT.ALL', 'Show Client Info section', 'ALL', 100);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(216, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_TAX_STAT.VIEW', 'Show Client Info section', 'VIEW', 100);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(217, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9_INF.ALL', 'Show Client Info section', 'ALL', 101);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(218, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9_INF.VIEW', 'Show Client Info section', 'VIEW', 101);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(219, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9_DET.ALL', 'Show Client Info section', 'ALL', 102);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(220, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9_DET.VIEW', 'Show Client Info section', 'VIEW', 102);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(221, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_RADD.ALL', 'Show Client Info section', 'ALL', 92);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(222, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_PER_RADD.VIEW', 'Show Client Info section', 'VIEW', 92);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(223, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_JOB_PDET.ALL', 'Show Client Info section', 'ALL', 98);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(224, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_JOB_PDET.VIEW', 'Show Client Info section', 'VIEW', 98);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(225, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'REPORT.ALL', 'Manage Reports', 'ALL', 112);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(226, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'REPORT.VIEW', 'Show Reports', 'VIEW', 112);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(227, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'REPORTS.ALL', 'Manage Reports', 'ALL', 113);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(228, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'REPORTS.VIEW', 'Show Reports', 'VIEW', 113);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(229, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ON_BRDNG_DOC.ALL', 'Manage On-Boarding Document', 'ALL', 114);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(230, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ON_BRDNG_DOC.VIEW', 'Show On-Boarding Document', 'VIEW', 114);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(231, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ON_BRDNG_DOC_MNGT.ALL', 'Manage On-Boarding Document Management', 'ALL', 115);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(232, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'ON_BRDNG_DOC_MNGT.VIEW', 'Show On-Boarding Document Management', 'VIEW', 115);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(233, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PTO_TOREQ.ALL', 'Manage Time Off Requests Information', 'ALL', 116);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(234, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PTO_TOREQ.VIEW', 'View Time Off Requests Information', 'VIEW', 116);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(235, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PTO_PTOSUM.ALL', 'Manage Paid Time Off Summaries Information', 'ALL', 117);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(236, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PTO_PTOSUM.VIEW', 'View Paid Time Off Summaries Information', 'VIEW', 117);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(237, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PTO_LOAREQ.ALL', 'Manage Leave Of Absence Request Information', 'ALL', 118);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(238, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'EMP_MGMT_PTO_LOAREQ.VIEW', 'View Leave Of Absence Request Information', 'VIEW', 118);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(239, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_CALS.VIEW', 'Show Pay Calculators Feature Group', 'VIEW', 119);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(240, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_CALS.ALL', 'Manage Pay Calculators Feature Group', 'ALL', 119);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(241, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_CALS_GTNCAL.ALL', 'Manage Gross to Net Calculator', 'ALL', 120);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(242, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_CALS_GTNCAL.VIEW', 'View Gross to Net Calculator', 'VIEW', 120);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(243, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_CLR.VIEW', 'Show Clear Timesheet Entry', 'VIEW', 121);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(244, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_CLR.ALL', 'Manage Clear Timesheet Entry', 'ALL', 121);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(245, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_IMPT.VIEW', 'View Import Time Sheet Entries from a file', 'VIEW', 122);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(246, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_IMPT.ALL', 'Manage Import Time Sheet Entries from a file', 'ALL', 122);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(247, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_SAVE.VIEW', 'Show Save Timesheet hours', 'VIEW', 123);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(248, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_SAVE.ALL', 'Manage Save Timesheet hours', 'ALL', 123);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(249, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_SUBMIT.VIEW', 'View Submit Timesheet hours', 'VIEW', 124);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(250, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_SUBMIT.ALL', 'Manage Submit Timesheet hours', 'ALL', 124);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(251, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_REJECT.VIEW', 'Show Reject Timesheet hours', 'VIEW', 125);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(252, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_REJECT.ALL', 'Manage Reject Timesheet hours', 'ALL', 125);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(253, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_CALC.VIEW', 'View Calculate Timesheet hours', 'VIEW', 126);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(254, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_CALC.ALL', 'Manage Calculate Timesheet hours', 'ALL', 126);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(255, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_PAGE.VIEW', 'Show Payroll Page', 'VIEW', 127);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(256, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_PAGE.ALL', 'Manage Payroll Page', 'ALL', 127);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(257, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_APPROVAL_REPORTS.VIEW', 'View Approval Reports Manual Timesheet hours', 'VIEW', 128);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(258, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_APPROVAL_REPORTS.ALL', 'Manage Approval Reports Manual Timesheet hours', 'ALL', 128);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(259, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_FINALIZE.VIEW', 'View Finalize Timesheet hours', 'VIEW', 129);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(260, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'PAY_TS_HOUR_FINALIZE.ALL', 'Manage Finalize Timesheet hours', 'ALL', 129);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(261, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT.VIEW', 'Show Company Management', 'VIEW', 130);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(262, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT.ALL', 'Manage Company Management', 'ALL', 130);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(263, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_INF.VIEW', 'Show Information', 'VIEW', 131);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(264, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_INF.ALL', 'Manage Information', 'ALL', 131);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(265, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC.VIEW', 'Show Organazation Structure', 'VIEW', 132);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(266, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC.ALL', 'Manage Organazation Structure', 'ALL', 132);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(267, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_DEP.VIEW', 'Show Department', 'VIEW', 136);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(268, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_DEP.ALL', 'Manage Department', 'ALL', 136);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(269, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_DIV.VIEW', 'Show Division', 'VIEW', 137);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(270, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_DIV.ALL', 'Manage Division', 'ALL', 137);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(271, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_PROJ.VIEW', 'Show Project', 'VIEW', 138);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(272, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_PROJ.ALL', 'Manage Project', 'ALL', 138);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(273, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_SHIFT.VIEW', 'Show Shift', 'VIEW', 139);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(274, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_SHIFT.ALL', 'Manage Shift', 'ALL', 139);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(275, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_UNION.VIEW', 'Show Union', 'VIEW', 140);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(276, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_UNION.ALL', 'Manage Union', 'ALL', 140);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(277, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_WLOC.VIEW', 'Show Worksite Location', 'VIEW', 141);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(278, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ORG_STRC_WLOC.ALL', 'Manage Worksite Location', 'ALL', 141);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(279, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV.VIEW', 'Show Employee Development', 'VIEW', 133);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(280, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV.ALL', 'Manage Employee Development', 'ALL', 133);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(281, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_COURSE.VIEW', 'Show Course', 'VIEW', 142);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(282, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_COURSE.ALL', 'Manage Course', 'ALL', 142);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(283, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_EVENT.VIEW', 'Show Event', 'VIEW', 143);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(284, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_EVENT.ALL', 'Manage Event', 'ALL', 143);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(285, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_OCC.VIEW', 'Show Occupation', 'VIEW', 144);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(286, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_OCC.ALL', 'Manage Occupation', 'ALL', 144);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(287, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_PER_REW.VIEW', 'Show Performance Review', 'VIEW', 145);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(288, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_PER_REW.ALL', 'Manage Performance Review', 'ALL', 145);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(289, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_SKILL.VIEW', 'Show Skill', 'VIEW', 146);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(290, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_SKILL.ALL', 'Manage Skill', 'ALL', 146);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(291, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_WGROUP.VIEW', 'Show Work Group', 'VIEW', 147);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(292, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_EMP_DEV_WGROUP.ALL', 'Manage Work Group', 'ALL', 147);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(293, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ACC.VIEW', 'Show Account', 'VIEW', 134);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(294, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ACC.ALL', 'Manage Account', 'ALL', 134);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(295, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ACC_PBANKINFO.VIEW', 'Show Primary Bank Information', 'VIEW', 148);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(296, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ACC_PBANKINFO.ALL', 'Manage Primary Bank Information', 'ALL', 148);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(297, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ACC_TAXINFO.VIEW', 'Show Tax Information', 'VIEW', 149);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(298, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_ACC_TAXINFO.ALL', 'Manage Tax Information', 'ALL', 149);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(299, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_INSURANCE.VIEW', 'Show Insurance', 'VIEW', 135);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(300, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'COM_MGMT_INSURANCE.ALL', 'Manage Insurance', 'ALL', 135);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(301, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'EMP_MGMT_PER_MADD.ALL', 'Manage Mailing Address Information section', 'ALL', 69);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(302, 'anonymousUser', '2019-08-28 15:30:22.000', 'anonymousUser', '2019-08-28 15:30:22.000', 0, 'EMP_MGMT_PER_MADD.VIEW', 'View Mailing Address Information section', 'VIEW', 69);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(303, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'REPORT_LIBRARY.ALL', 'Manage Report Library', 'ALL', 151);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(304, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'REPORT_LIBRARY.VIEW', 'Show Report Library', 'VIEW', 151);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(305, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'REPORT_LIBRARY_MGMT.ALL', 'Manage Report Library Management', 'ALL', 152);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(306, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'REPORT_LIBRARY_MGMT.VIEW', 'Show Report Library Management', 'VIEW', 152);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(307, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9_APPROVER_FG.ALL', 'I9 Approver Feature Group', 'ALL', 153);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(308, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9_APPROVER_FG.VIEW', 'I9 Approver Feature Group', 'VIEW', 153);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(309, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9_APPROVER_F.ALL', 'I9 Approver Feature', 'ALL', 154);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(310, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_I9_APPROVER_F.VIEW', 'I9 Approver Feature', 'VIEW', 154);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(313, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_EMP_ONBOARDING.ALL', 'Employee onboarding ', 'ALL', 155);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(314, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_EMP_ONBOARDING.VIEW', 'Employee onboarding ', 'VIEW', 155);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(315, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_ONBOARDING_DOCUMENTS.ALL', 'Onboarding documents', 'ALL', 156);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(316, 'anonymousUser', '2019-07-16 08:56:22.000', 'anonymousUser', '2019-07-16 08:56:22.000', 0, 'NEW_HIRE_ONBOARDING_DOCUMENTS.VIEW', 'Onboarding documents', 'VIEW', 156);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(317, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'MSG_CENTER.VIEW', 'Show Message Center', 'VIEW', 157);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(318, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'MSG_CENTER.ALL', 'Manage Message Center', 'ALL', 157);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(319, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'MSG_CENTER_NOTIF.VIEW', 'Show Notifications', 'VIEW', 158);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(320, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'MSG_CENTER_NOTIF.ALL', 'Manage Notifications', 'ALL', 158);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(321, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'MSG_CENTER_ANNCMNT.VIEW', 'Show Announcements', 'VIEW', 159);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(322, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'MSG_CENTER_ANNCMNT.ALL', 'Manage Announcements', 'ALL', 159);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(323, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'MSG_CENTER_APPR.VIEW', 'Show Approvals', 'VIEW', 160);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(324, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'MSG_CENTER_APPR.ALL', 'Manage Approvals', 'ALL', 160);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(325, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'PAY_CALS_NTGCAL.ALL', 'Manage Net to Gross Calculator', 'ALL', 161);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(326, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'PAY_CALS_NTGCAL.VIEW', 'Show Net to Gross Calculator', 'VIEW', 161);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(327, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'CAL_SETUP_MGMT.ALL', 'Calendar setup all', 'ALL', 162);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(328, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'CAL_SETUP_MGMT.VIEW', 'Calendar setup view', 'VIEW', 162);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(329, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'CAL_SETUP.ALL', 'Calendar setup all', 'ALL', 163);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(330, 'anonymousUser', '2019-09-06 15:00:00.000', 'anonymousUser', '2019-09-06 15:00:00.000', 0, 'CAL_SETUP.VIEW', 'Calendar setup view', 'VIEW', 163);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(331, 'anonymousUser', '2019-09-26 08:56:22.000', 'anonymousUser', '2019-09-26 08:56:22.000', 0, 'PAYROLL_SETTING.ALL', 'Manage Payroll Setting', 'ALL', 164);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(332, 'anonymousUser', '2019-09-26 08:56:22.000', 'anonymousUser', '2019-09-26 08:56:22.000', 0, 'PAYROLL_SETTING.VIEW', 'Show Payroll Setting', 'VIEW', 164);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(333, 'anonymousUser', '2019-09-26 08:56:22.000', 'anonymousUser', '2019-09-26 08:56:22.000', 0, 'PAYROLL_SETTING_MNGT.ALL', 'Manage Payroll Setting Management', 'ALL', 165);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(334, 'anonymousUser', '2019-09-26 08:56:22.000', 'anonymousUser', '2019-09-26 08:56:22.000', 0, 'PAYROLL_SETTING_MNGT.VIEW', 'Show Payroll Setting Management', 'VIEW', 165);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(335, 'anonymousUser', '2019-09-26 08:56:22.000', 'anonymousUser', '2019-09-26 08:56:22.000', 0, 'EMP_MGMT_PER_I9_INFO.ALL', 'I-9 Information', 'ALL', 166);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(336, 'anonymousUser', '2019-09-26 08:56:22.000', 'anonymousUser', '2019-09-26 08:56:22.000', 0, 'EMP_MGMT_PER_I9_INFO.VIEW', 'I-9 Information', 'VIEW', 166);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(337, 'anonymousUser', '2019-09-26 08:56:22.000', 'anonymousUser', '2019-09-26 08:56:22.000', 0, 'EMP_MGMT_PER_EMP_ELI_DOC.ALL', 'Employment Eligibility Documentation', 'ALL', 167);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(338, 'anonymousUser', '2019-09-26 08:56:22.000', 'anonymousUser', '2019-09-26 08:56:22.000', 0, 'EMP_MGMT_PER_EMP_ELI_DOC.VIEW', 'Employment Eligibility Documentation', 'VIEW', 167);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(339, 'anonymousUser', '2019-10-10 15:00:00.000', 'anonymousUser', '2019-10-10 15:00:00.000', 0, 'HELP_TRAINING_MGMT.ALL', 'Help & Training all', 'ALL', 168);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(340, 'anonymousUser', '2019-10-10 15:00:00.000', 'anonymousUser', '2019-10-10 15:00:00.000', 0, 'HELP_TRAINING_MGMT.VIEW', 'Help & Training view', 'VIEW', 168);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(341, 'anonymousUser', '2019-10-10 15:00:00.000', 'anonymousUser', '2019-10-10 15:00:00.000', 0, 'HELP_TRAINING.ALL', 'Help & Training all', 'ALL', 169);
INSERT INTO privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)
VALUES(342, 'anonymousUser', '2019-10-10 15:00:00.000', 'anonymousUser', '2019-10-10 15:00:00.000', 0, 'HELP_TRAINING.VIEW', 'Help & Training view', 'VIEW', 169);



-- Initial Super User Creation



INSERT INTO access_group_v1
(id, created_by, created_on, modified_by, modified_on, version, client_code, description, name, is_active)
VALUES(1, 'admin', '2091-08-01 00:00:00.000', 'anonymousUser', '2019-10-10 13:45:03.342', 3, '909464', 'Admin', 'Admin', 1);


INSERT INTO accessprivileges(access_group_id, privilege_id) VALUES(1, 43);
INSERT INTO accessprivileges(access_group_id, privilege_id) VALUES(1, 44);
INSERT INTO accessprivileges(access_group_id, privilege_id) VALUES(1, 45);
INSERT INTO accessprivileges(access_group_id, privilege_id) VALUES(1, 46);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 47);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 48);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 49);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 50);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 47);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 48);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 51);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 52);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 47);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 48);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 53);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 54);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 47);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 48);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 55);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 56);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 57);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 58);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 59);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 60);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 57);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 58);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 61);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 62);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 63);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 64);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 65);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 66);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 67);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 68);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 69);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 70);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 71);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 72);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 73);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 74);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 71);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 72);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 75);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 76);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 71);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 72);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 77);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 78);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 79);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 80);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 81);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 82);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 97);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 98);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 99);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 100);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 101);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 102);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 103);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 104);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 105);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 106);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 107);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 108);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 79);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 80);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 83);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 84);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 109);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 110);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 111);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 112);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 113);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 114);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 79);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 80);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 85);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 86);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 115);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 116);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 117);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 118);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 119);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 120);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 121);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 122);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 123);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 124);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 125);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 126);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 79);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 80);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 87);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 88);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 127);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 128);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 129);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 130);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 79);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 80);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 89);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 90);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 131);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 132);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 133);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 134);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 135);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 136);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 137);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 138);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 79);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 80);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 91);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 92);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 79);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 80);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 93);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 94);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 139);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 140);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 141);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 142);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 143);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 144);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 79);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 80);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 95);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 96);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 145);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 146);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 313);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 314);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 147);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 148);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 149);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 150);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 151);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 152);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 153);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 154);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 155);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 156);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 157);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 158);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 159);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 160);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 145);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 146);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 315);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 316);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 161);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 162);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 163);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 164);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 165);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 166);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 167);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 168);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 169);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 170);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 171);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 172);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 174);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 176);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 177);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 178);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 243);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 244);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 245);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 246);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 247);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 248);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 249);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 250);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 251);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 252);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 253);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 254);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 255);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 256);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 257);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 258);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 259);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 225);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 226);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 227);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 228);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 229);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 230);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 231);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 232);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 239);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 240);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 241);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 242);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 239);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 240);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 325);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 326);
INSERT INTO accessprivileges (access_group_id, privilege_id) VALUES(1, 261);


INSERT INTO role_v1
(id, created_by, created_on, modified_by, modified_on, version, code, description, name, is_active, [type])
VALUES(1, 'anonymousUser', '2019-07-30 06:46:59.000', 'anonymousUser', '2019-10-10 13:45:15.009', 3, 'Admin', '', 'Admin', 1, 'BRANCH');


INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1216, 'anonymousUser', '2019-10-10 13:45:14.462', 'anonymousUser', '2019-10-10 13:45:14.462', 0, 1, 45, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1217, 'anonymousUser', '2019-10-10 13:45:14.469', 'anonymousUser', '2019-10-10 13:45:14.469', 0, 1, 46, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1218, 'anonymousUser', '2019-10-10 13:45:14.471', 'anonymousUser', '2019-10-10 13:45:14.471', 0, 1, 43, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1219, 'anonymousUser', '2019-10-10 13:45:14.474', 'anonymousUser', '2019-10-10 13:45:14.474', 0, 1, 44, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1220, 'anonymousUser', '2019-10-10 13:45:14.477', 'anonymousUser', '2019-10-10 13:45:14.477', 0, 1, 49, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1221, 'anonymousUser', '2019-10-10 13:45:14.478', 'anonymousUser', '2019-10-10 13:45:14.478', 0, 1, 50, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1222, 'anonymousUser', '2019-10-10 13:45:14.480', 'anonymousUser', '2019-10-10 13:45:14.480', 0, 1, 51, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1223, 'anonymousUser', '2019-10-10 13:45:14.482', 'anonymousUser', '2019-10-10 13:45:14.482', 0, 1, 52, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1224, 'anonymousUser', '2019-10-10 13:45:14.486', 'anonymousUser', '2019-10-10 13:45:14.486', 0, 1, 53, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1225, 'anonymousUser', '2019-10-10 13:45:14.488', 'anonymousUser', '2019-10-10 13:45:14.488', 0, 1, 54, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1226, 'anonymousUser', '2019-10-10 13:45:14.490', 'anonymousUser', '2019-10-10 13:45:14.490', 0, 1, 55, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1227, 'anonymousUser', '2019-10-10 13:45:14.492', 'anonymousUser', '2019-10-10 13:45:14.492', 0, 1, 56, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1228, 'anonymousUser', '2019-10-10 13:45:14.495', 'anonymousUser', '2019-10-10 13:45:14.495', 0, 1, 47, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1229, 'anonymousUser', '2019-10-10 13:45:14.498', 'anonymousUser', '2019-10-10 13:45:14.498', 0, 1, 48, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1230, 'anonymousUser', '2019-10-10 13:45:14.500', 'anonymousUser', '2019-10-10 13:45:14.500', 0, 1, 59, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1231, 'anonymousUser', '2019-10-10 13:45:14.502', 'anonymousUser', '2019-10-10 13:45:14.502', 0, 1, 60, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1232, 'anonymousUser', '2019-10-10 13:45:14.508', 'anonymousUser', '2019-10-10 13:45:14.508', 0, 1, 61, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1233, 'anonymousUser', '2019-10-10 13:45:14.511', 'anonymousUser', '2019-10-10 13:45:14.511', 0, 1, 62, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1234, 'anonymousUser', '2019-10-10 13:45:14.513', 'anonymousUser', '2019-10-10 13:45:14.513', 0, 1, 57, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1235, 'anonymousUser', '2019-10-10 13:45:14.517', 'anonymousUser', '2019-10-10 13:45:14.517', 0, 1, 58, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1236, 'anonymousUser', '2019-10-10 13:45:14.519', 'anonymousUser', '2019-10-10 13:45:14.519', 0, 1, 67, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1237, 'anonymousUser', '2019-10-10 13:45:14.521', 'anonymousUser', '2019-10-10 13:45:14.521', 0, 1, 68, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1238, 'anonymousUser', '2019-10-10 13:45:14.526', 'anonymousUser', '2019-10-10 13:45:14.526', 0, 1, 69, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1239, 'anonymousUser', '2019-10-10 13:45:14.528', 'anonymousUser', '2019-10-10 13:45:14.528', 0, 1, 70, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1240, 'anonymousUser', '2019-10-10 13:45:14.529', 'anonymousUser', '2019-10-10 13:45:14.529', 0, 1, 65, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1241, 'anonymousUser', '2019-10-10 13:45:14.531', 'anonymousUser', '2019-10-10 13:45:14.531', 0, 1, 66, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1242, 'anonymousUser', '2019-10-10 13:45:14.533', 'anonymousUser', '2019-10-10 13:45:14.533', 0, 1, 63, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1243, 'anonymousUser', '2019-10-10 13:45:14.539', 'anonymousUser', '2019-10-10 13:45:14.539', 0, 1, 64, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1244, 'anonymousUser', '2019-10-10 13:45:14.541', 'anonymousUser', '2019-10-10 13:45:14.541', 0, 1, 73, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1245, 'anonymousUser', '2019-10-10 13:45:14.543', 'anonymousUser', '2019-10-10 13:45:14.543', 0, 1, 74, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1246, 'anonymousUser', '2019-10-10 13:45:14.545', 'anonymousUser', '2019-10-10 13:45:14.545', 0, 1, 75, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1247, 'anonymousUser', '2019-10-10 13:45:14.547', 'anonymousUser', '2019-10-10 13:45:14.547', 0, 1, 76, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1248, 'anonymousUser', '2019-10-10 13:45:14.550', 'anonymousUser', '2019-10-10 13:45:14.550', 0, 1, 77, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1249, 'anonymousUser', '2019-10-10 13:45:14.551', 'anonymousUser', '2019-10-10 13:45:14.551', 0, 1, 78, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1250, 'anonymousUser', '2019-10-10 13:45:14.553', 'anonymousUser', '2019-10-10 13:45:14.553', 0, 1, 71, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1251, 'anonymousUser', '2019-10-10 13:45:14.556', 'anonymousUser', '2019-10-10 13:45:14.556', 0, 1, 72, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1252, 'anonymousUser', '2019-10-10 13:45:14.558', 'anonymousUser', '2019-10-10 13:45:14.558', 0, 1, 97, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1253, 'anonymousUser', '2019-10-10 13:45:14.561', 'anonymousUser', '2019-10-10 13:45:14.561', 0, 1, 98, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1254, 'anonymousUser', '2019-10-10 13:45:14.562', 'anonymousUser', '2019-10-10 13:45:14.562', 0, 1, 99, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1255, 'anonymousUser', '2019-10-10 13:45:14.565', 'anonymousUser', '2019-10-10 13:45:14.565', 0, 1, 100, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1256, 'anonymousUser', '2019-10-10 13:45:14.566', 'anonymousUser', '2019-10-10 13:45:14.566', 0, 1, 101, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1257, 'anonymousUser', '2019-10-10 13:45:14.569', 'anonymousUser', '2019-10-10 13:45:14.569', 0, 1, 102, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1258, 'anonymousUser', '2019-10-10 13:45:14.572', 'anonymousUser', '2019-10-10 13:45:14.572', 0, 1, 103, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1259, 'anonymousUser', '2019-10-10 13:45:14.574', 'anonymousUser', '2019-10-10 13:45:14.574', 0, 1, 104, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1260, 'anonymousUser', '2019-10-10 13:45:14.576', 'anonymousUser', '2019-10-10 13:45:14.576', 0, 1, 105, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1261, 'anonymousUser', '2019-10-10 13:45:14.578', 'anonymousUser', '2019-10-10 13:45:14.578', 0, 1, 106, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1262, 'anonymousUser', '2019-10-10 13:45:14.580', 'anonymousUser', '2019-10-10 13:45:14.580', 0, 1, 107, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1263, 'anonymousUser', '2019-10-10 13:45:14.582', 'anonymousUser', '2019-10-10 13:45:14.582', 0, 1, 108, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1264, 'anonymousUser', '2019-10-10 13:45:14.584', 'anonymousUser', '2019-10-10 13:45:14.584', 0, 1, 81, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1265, 'anonymousUser', '2019-10-10 13:45:14.587', 'anonymousUser', '2019-10-10 13:45:14.587', 0, 1, 82, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1266, 'anonymousUser', '2019-10-10 13:45:14.589', 'anonymousUser', '2019-10-10 13:45:14.589', 0, 1, 109, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1267, 'anonymousUser', '2019-10-10 13:45:14.595', 'anonymousUser', '2019-10-10 13:45:14.595', 0, 1, 110, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1268, 'anonymousUser', '2019-10-10 13:45:14.598', 'anonymousUser', '2019-10-10 13:45:14.598', 0, 1, 111, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1269, 'anonymousUser', '2019-10-10 13:45:14.601', 'anonymousUser', '2019-10-10 13:45:14.601', 0, 1, 112, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1270, 'anonymousUser', '2019-10-10 13:45:14.602', 'anonymousUser', '2019-10-10 13:45:14.602', 0, 1, 113, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1271, 'anonymousUser', '2019-10-10 13:45:14.604', 'anonymousUser', '2019-10-10 13:45:14.604', 0, 1, 114, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1272, 'anonymousUser', '2019-10-10 13:45:14.607', 'anonymousUser', '2019-10-10 13:45:14.607', 0, 1, 83, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1273, 'anonymousUser', '2019-10-10 13:45:14.609', 'anonymousUser', '2019-10-10 13:45:14.609', 0, 1, 84, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1274, 'anonymousUser', '2019-10-10 13:45:14.611', 'anonymousUser', '2019-10-10 13:45:14.611', 0, 1, 115, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1275, 'anonymousUser', '2019-10-10 13:45:14.613', 'anonymousUser', '2019-10-10 13:45:14.613', 0, 1, 116, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1276, 'anonymousUser', '2019-10-10 13:45:14.615', 'anonymousUser', '2019-10-10 13:45:14.615', 0, 1, 117, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1277, 'anonymousUser', '2019-10-10 13:45:14.618', 'anonymousUser', '2019-10-10 13:45:14.618', 0, 1, 118, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1278, 'anonymousUser', '2019-10-10 13:45:14.620', 'anonymousUser', '2019-10-10 13:45:14.620', 0, 1, 119, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1279, 'anonymousUser', '2019-10-10 13:45:14.622', 'anonymousUser', '2019-10-10 13:45:14.622', 0, 1, 120, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1280, 'anonymousUser', '2019-10-10 13:45:14.623', 'anonymousUser', '2019-10-10 13:45:14.623', 0, 1, 121, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1281, 'anonymousUser', '2019-10-10 13:45:14.625', 'anonymousUser', '2019-10-10 13:45:14.625', 0, 1, 122, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1282, 'anonymousUser', '2019-10-10 13:45:14.628', 'anonymousUser', '2019-10-10 13:45:14.628', 0, 1, 123, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1283, 'anonymousUser', '2019-10-10 13:45:14.629', 'anonymousUser', '2019-10-10 13:45:14.629', 0, 1, 124, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1284, 'anonymousUser', '2019-10-10 13:45:14.632', 'anonymousUser', '2019-10-10 13:45:14.632', 0, 1, 125, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1285, 'anonymousUser', '2019-10-10 13:45:14.634', 'anonymousUser', '2019-10-10 13:45:14.634', 0, 1, 126, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1286, 'anonymousUser', '2019-10-10 13:45:14.636', 'anonymousUser', '2019-10-10 13:45:14.636', 0, 1, 85, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1287, 'anonymousUser', '2019-10-10 13:45:14.638', 'anonymousUser', '2019-10-10 13:45:14.638', 0, 1, 86, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1288, 'anonymousUser', '2019-10-10 13:45:14.640', 'anonymousUser', '2019-10-10 13:45:14.640', 0, 1, 127, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1289, 'anonymousUser', '2019-10-10 13:45:14.642', 'anonymousUser', '2019-10-10 13:45:14.642', 0, 1, 128, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1290, 'anonymousUser', '2019-10-10 13:45:14.644', 'anonymousUser', '2019-10-10 13:45:14.644', 0, 1, 129, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1291, 'anonymousUser', '2019-10-10 13:45:14.646', 'anonymousUser', '2019-10-10 13:45:14.646', 0, 1, 130, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1292, 'anonymousUser', '2019-10-10 13:45:14.648', 'anonymousUser', '2019-10-10 13:45:14.648', 0, 1, 87, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1293, 'anonymousUser', '2019-10-10 13:45:14.650', 'anonymousUser', '2019-10-10 13:45:14.650', 0, 1, 88, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1294, 'anonymousUser', '2019-10-10 13:45:14.652', 'anonymousUser', '2019-10-10 13:45:14.652', 0, 1, 131, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1295, 'anonymousUser', '2019-10-10 13:45:14.654', 'anonymousUser', '2019-10-10 13:45:14.654', 0, 1, 132, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1296, 'anonymousUser', '2019-10-10 13:45:14.655', 'anonymousUser', '2019-10-10 13:45:14.655', 0, 1, 133, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1297, 'anonymousUser', '2019-10-10 13:45:14.658', 'anonymousUser', '2019-10-10 13:45:14.658', 0, 1, 134, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1298, 'anonymousUser', '2019-10-10 13:45:14.660', 'anonymousUser', '2019-10-10 13:45:14.660', 0, 1, 135, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1299, 'anonymousUser', '2019-10-10 13:45:14.662', 'anonymousUser', '2019-10-10 13:45:14.662', 0, 1, 136, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1300, 'anonymousUser', '2019-10-10 13:45:14.663', 'anonymousUser', '2019-10-10 13:45:14.663', 0, 1, 137, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1301, 'anonymousUser', '2019-10-10 13:45:14.665', 'anonymousUser', '2019-10-10 13:45:14.665', 0, 1, 138, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1302, 'anonymousUser', '2019-10-10 13:45:14.668', 'anonymousUser', '2019-10-10 13:45:14.668', 0, 1, 89, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1303, 'anonymousUser', '2019-10-10 13:45:14.671', 'anonymousUser', '2019-10-10 13:45:14.671', 0, 1, 90, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1304, 'anonymousUser', '2019-10-10 13:45:14.673', 'anonymousUser', '2019-10-10 13:45:14.673', 0, 1, 91, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1305, 'anonymousUser', '2019-10-10 13:45:14.675', 'anonymousUser', '2019-10-10 13:45:14.675', 0, 1, 92, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1306, 'anonymousUser', '2019-10-10 13:45:14.677', 'anonymousUser', '2019-10-10 13:45:14.677', 0, 1, 139, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1307, 'anonymousUser', '2019-10-10 13:45:14.679', 'anonymousUser', '2019-10-10 13:45:14.679', 0, 1, 140, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1308, 'anonymousUser', '2019-10-10 13:45:14.681', 'anonymousUser', '2019-10-10 13:45:14.681', 0, 1, 141, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1309, 'anonymousUser', '2019-10-10 13:45:14.683', 'anonymousUser', '2019-10-10 13:45:14.683', 0, 1, 142, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1310, 'anonymousUser', '2019-10-10 13:45:14.686', 'anonymousUser', '2019-10-10 13:45:14.686', 0, 1, 143, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1311, 'anonymousUser', '2019-10-10 13:45:14.688', 'anonymousUser', '2019-10-10 13:45:14.688', 0, 1, 144, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1312, 'anonymousUser', '2019-10-10 13:45:14.690', 'anonymousUser', '2019-10-10 13:45:14.690', 0, 1, 93, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1313, 'anonymousUser', '2019-10-10 13:45:14.692', 'anonymousUser', '2019-10-10 13:45:14.692', 0, 1, 94, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1314, 'anonymousUser', '2019-10-10 13:45:14.695', 'anonymousUser', '2019-10-10 13:45:14.695', 0, 1, 95, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1315, 'anonymousUser', '2019-10-10 13:45:14.697', 'anonymousUser', '2019-10-10 13:45:14.697', 0, 1, 96, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1316, 'anonymousUser', '2019-10-10 13:45:14.699', 'anonymousUser', '2019-10-10 13:45:14.699', 0, 1, 79, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1317, 'anonymousUser', '2019-10-10 13:45:14.705', 'anonymousUser', '2019-10-10 13:45:14.705', 0, 1, 80, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1318, 'anonymousUser', '2019-10-10 13:45:14.707', 'anonymousUser', '2019-10-10 13:45:14.707', 0, 1, 147, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1319, 'anonymousUser', '2019-10-10 13:45:14.709', 'anonymousUser', '2019-10-10 13:45:14.709', 0, 1, 148, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1320, 'anonymousUser', '2019-10-10 13:45:14.711', 'anonymousUser', '2019-10-10 13:45:14.711', 0, 1, 149, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1321, 'anonymousUser', '2019-10-10 13:45:14.713', 'anonymousUser', '2019-10-10 13:45:14.713', 0, 1, 150, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1322, 'anonymousUser', '2019-10-10 13:45:14.715', 'anonymousUser', '2019-10-10 13:45:14.715', 0, 1, 151, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1323, 'anonymousUser', '2019-10-10 13:45:14.718', 'anonymousUser', '2019-10-10 13:45:14.718', 0, 1, 152, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1324, 'anonymousUser', '2019-10-10 13:45:14.719', 'anonymousUser', '2019-10-10 13:45:14.719', 0, 1, 153, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1325, 'anonymousUser', '2019-10-10 13:45:14.721', 'anonymousUser', '2019-10-10 13:45:14.721', 0, 1, 154, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1326, 'anonymousUser', '2019-10-10 13:45:14.724', 'anonymousUser', '2019-10-10 13:45:14.724', 0, 1, 155, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1327, 'anonymousUser', '2019-10-10 13:45:14.727', 'anonymousUser', '2019-10-10 13:45:14.727', 0, 1, 156, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1328, 'anonymousUser', '2019-10-10 13:45:14.729', 'anonymousUser', '2019-10-10 13:45:14.729', 0, 1, 157, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1329, 'anonymousUser', '2019-10-10 13:45:14.731', 'anonymousUser', '2019-10-10 13:45:14.731', 0, 1, 158, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1330, 'anonymousUser', '2019-10-10 13:45:14.734', 'anonymousUser', '2019-10-10 13:45:14.734', 0, 1, 159, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1331, 'anonymousUser', '2019-10-10 13:45:14.736', 'anonymousUser', '2019-10-10 13:45:14.736', 0, 1, 160, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1332, 'anonymousUser', '2019-10-10 13:45:14.739', 'anonymousUser', '2019-10-10 13:45:14.739', 0, 1, 313, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1333, 'anonymousUser', '2019-10-10 13:45:14.741', 'anonymousUser', '2019-10-10 13:45:14.741', 0, 1, 314, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1334, 'anonymousUser', '2019-10-10 13:45:14.743', 'anonymousUser', '2019-10-10 13:45:14.743', 0, 1, 315, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1335, 'anonymousUser', '2019-10-10 13:45:14.745', 'anonymousUser', '2019-10-10 13:45:14.745', 0, 1, 316, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1336, 'anonymousUser', '2019-10-10 13:45:14.747', 'anonymousUser', '2019-10-10 13:45:14.747', 0, 1, 145, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1337, 'anonymousUser', '2019-10-10 13:45:14.749', 'anonymousUser', '2019-10-10 13:45:14.749', 0, 1, 146, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1338, 'anonymousUser', '2019-10-10 13:45:14.751', 'anonymousUser', '2019-10-10 13:45:14.751', 0, 1, 163, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1339, 'anonymousUser', '2019-10-10 13:45:14.758', 'anonymousUser', '2019-10-10 13:45:14.758', 0, 1, 164, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1340, 'anonymousUser', '2019-10-10 13:45:14.760', 'anonymousUser', '2019-10-10 13:45:14.760', 0, 1, 161, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1341, 'anonymousUser', '2019-10-10 13:45:14.762', 'anonymousUser', '2019-10-10 13:45:14.762', 0, 1, 162, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1342, 'anonymousUser', '2019-10-10 13:45:14.764', 'anonymousUser', '2019-10-10 13:45:14.764', 0, 1, 169, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1343, 'anonymousUser', '2019-10-10 13:45:14.767', 'anonymousUser', '2019-10-10 13:45:14.767', 0, 1, 170, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1344, 'anonymousUser', '2019-10-10 13:45:14.769', 'anonymousUser', '2019-10-10 13:45:14.769', 0, 1, 171, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1345, 'anonymousUser', '2019-10-10 13:45:14.771', 'anonymousUser', '2019-10-10 13:45:14.771', 0, 1, 172, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1346, 'anonymousUser', '2019-10-10 13:45:14.773', 'anonymousUser', '2019-10-10 13:45:14.773', 0, 1, 167, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1347, 'anonymousUser', '2019-10-10 13:45:14.775', 'anonymousUser', '2019-10-10 13:45:14.775', 0, 1, 168, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1348, 'anonymousUser', '2019-10-10 13:45:14.779', 'anonymousUser', '2019-10-10 13:45:14.779', 0, 1, 165, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1349, 'anonymousUser', '2019-10-10 13:45:14.781', 'anonymousUser', '2019-10-10 13:45:14.781', 0, 1, 166, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1350, 'anonymousUser', '2019-10-10 13:45:14.783', 'anonymousUser', '2019-10-10 13:45:14.783', 0, 1, 177, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1351, 'anonymousUser', '2019-10-10 13:45:14.785', 'anonymousUser', '2019-10-10 13:45:14.785', 0, 1, 178, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1352, 'anonymousUser', '2019-10-10 13:45:14.786', 'anonymousUser', '2019-10-10 13:45:14.786', 0, 1, 243, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1353, 'anonymousUser', '2019-10-10 13:45:14.788', 'anonymousUser', '2019-10-10 13:45:14.788', 0, 1, 244, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1354, 'anonymousUser', '2019-10-10 13:45:14.790', 'anonymousUser', '2019-10-10 13:45:14.790', 0, 1, 245, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1355, 'anonymousUser', '2019-10-10 13:45:14.791', 'anonymousUser', '2019-10-10 13:45:14.791', 0, 1, 246, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1356, 'anonymousUser', '2019-10-10 13:45:14.794', 'anonymousUser', '2019-10-10 13:45:14.794', 0, 1, 247, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1357, 'anonymousUser', '2019-10-10 13:45:14.796', 'anonymousUser', '2019-10-10 13:45:14.796', 0, 1, 248, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1358, 'anonymousUser', '2019-10-10 13:45:14.797', 'anonymousUser', '2019-10-10 13:45:14.797', 0, 1, 249, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1359, 'anonymousUser', '2019-10-10 13:45:14.799', 'anonymousUser', '2019-10-10 13:45:14.799', 0, 1, 250, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1360, 'anonymousUser', '2019-10-10 13:45:14.801', 'anonymousUser', '2019-10-10 13:45:14.801', 0, 1, 251, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1361, 'anonymousUser', '2019-10-10 13:45:14.803', 'anonymousUser', '2019-10-10 13:45:14.803', 0, 1, 252, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1362, 'anonymousUser', '2019-10-10 13:45:14.805', 'anonymousUser', '2019-10-10 13:45:14.805', 0, 1, 253, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1363, 'anonymousUser', '2019-10-10 13:45:14.811', 'anonymousUser', '2019-10-10 13:45:14.811', 0, 1, 254, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1364, 'anonymousUser', '2019-10-10 13:45:14.813', 'anonymousUser', '2019-10-10 13:45:14.813', 0, 1, 255, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1365, 'anonymousUser', '2019-10-10 13:45:14.815', 'anonymousUser', '2019-10-10 13:45:14.815', 0, 1, 256, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1366, 'anonymousUser', '2019-10-10 13:45:14.817', 'anonymousUser', '2019-10-10 13:45:14.817', 0, 1, 257, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1367, 'anonymousUser', '2019-10-10 13:45:14.822', 'anonymousUser', '2019-10-10 13:45:14.822', 0, 1, 258, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1368, 'anonymousUser', '2019-10-10 13:45:14.824', 'anonymousUser', '2019-10-10 13:45:14.824', 0, 1, 259, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1369, 'anonymousUser', '2019-10-10 13:45:14.826', 'anonymousUser', '2019-10-10 13:45:14.826', 0, 1, 176, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1370, 'anonymousUser', '2019-10-10 13:45:14.827', 'anonymousUser', '2019-10-10 13:45:14.827', 0, 1, 174, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1371, 'anonymousUser', '2019-10-10 13:45:14.829', 'anonymousUser', '2019-10-10 13:45:14.829', 0, 1, 227, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1372, 'anonymousUser', '2019-10-10 13:45:14.831', 'anonymousUser', '2019-10-10 13:45:14.831', 0, 1, 228, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1373, 'anonymousUser', '2019-10-10 13:45:14.834', 'anonymousUser', '2019-10-10 13:45:14.834', 0, 1, 225, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1374, 'anonymousUser', '2019-10-10 13:45:14.836', 'anonymousUser', '2019-10-10 13:45:14.836', 0, 1, 226, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1375, 'anonymousUser', '2019-10-10 13:45:14.837', 'anonymousUser', '2019-10-10 13:45:14.837', 0, 1, 231, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1376, 'anonymousUser', '2019-10-10 13:45:14.839', 'anonymousUser', '2019-10-10 13:45:14.839', 0, 1, 232, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1377, 'anonymousUser', '2019-10-10 13:45:14.841', 'anonymousUser', '2019-10-10 13:45:14.841', 0, 1, 229, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1378, 'anonymousUser', '2019-10-10 13:45:14.843', 'anonymousUser', '2019-10-10 13:45:14.843', 0, 1, 230, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1379, 'anonymousUser', '2019-10-10 13:45:14.845', 'anonymousUser', '2019-10-10 13:45:14.845', 0, 1, 241, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1380, 'anonymousUser', '2019-10-10 13:45:14.847', 'anonymousUser', '2019-10-10 13:45:14.847', 0, 1, 242, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1381, 'anonymousUser', '2019-10-10 13:45:14.850', 'anonymousUser', '2019-10-10 13:45:14.850', 0, 1, 325, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1382, 'anonymousUser', '2019-10-10 13:45:14.851', 'anonymousUser', '2019-10-10 13:45:14.851', 0, 1, 326, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1383, 'anonymousUser', '2019-10-10 13:45:14.854', 'anonymousUser', '2019-10-10 13:45:14.854', 0, 1, 239, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1384, 'anonymousUser', '2019-10-10 13:45:14.856', 'anonymousUser', '2019-10-10 13:45:14.856', 0, 1, 240, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1385, 'anonymousUser', '2019-10-10 13:45:14.858', 'anonymousUser', '2019-10-10 13:45:14.858', 0, 1, 263, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1386, 'anonymousUser', '2019-10-10 13:45:14.859', 'anonymousUser', '2019-10-10 13:45:14.859', 0, 1, 264, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1387, 'anonymousUser', '2019-10-10 13:45:14.861', 'anonymousUser', '2019-10-10 13:45:14.861', 0, 1, 267, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1388, 'anonymousUser', '2019-10-10 13:45:14.863', 'anonymousUser', '2019-10-10 13:45:14.863', 0, 1, 268, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1389, 'anonymousUser', '2019-10-10 13:45:14.865', 'anonymousUser', '2019-10-10 13:45:14.865', 0, 1, 269, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1390, 'anonymousUser', '2019-10-10 13:45:14.867', 'anonymousUser', '2019-10-10 13:45:14.867', 0, 1, 270, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1391, 'anonymousUser', '2019-10-10 13:45:14.879', 'anonymousUser', '2019-10-10 13:45:14.879', 0, 1, 271, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1392, 'anonymousUser', '2019-10-10 13:45:14.882', 'anonymousUser', '2019-10-10 13:45:14.882', 0, 1, 272, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1393, 'anonymousUser', '2019-10-10 13:45:14.884', 'anonymousUser', '2019-10-10 13:45:14.884', 0, 1, 273, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1394, 'anonymousUser', '2019-10-10 13:45:14.886', 'anonymousUser', '2019-10-10 13:45:14.886', 0, 1, 274, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1395, 'anonymousUser', '2019-10-10 13:45:14.888', 'anonymousUser', '2019-10-10 13:45:14.888', 0, 1, 275, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1396, 'anonymousUser', '2019-10-10 13:45:14.890', 'anonymousUser', '2019-10-10 13:45:14.890', 0, 1, 276, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1397, 'anonymousUser', '2019-10-10 13:45:14.892', 'anonymousUser', '2019-10-10 13:45:14.892', 0, 1, 277, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1398, 'anonymousUser', '2019-10-10 13:45:14.894', 'anonymousUser', '2019-10-10 13:45:14.894', 0, 1, 278, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1399, 'anonymousUser', '2019-10-10 13:45:14.896', 'anonymousUser', '2019-10-10 13:45:14.896', 0, 1, 265, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1400, 'anonymousUser', '2019-10-10 13:45:14.898', 'anonymousUser', '2019-10-10 13:45:14.898', 0, 1, 266, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1401, 'anonymousUser', '2019-10-10 13:45:14.900', 'anonymousUser', '2019-10-10 13:45:14.900', 0, 1, 281, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1402, 'anonymousUser', '2019-10-10 13:45:14.902', 'anonymousUser', '2019-10-10 13:45:14.902', 0, 1, 282, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1403, 'anonymousUser', '2019-10-10 13:45:14.904', 'anonymousUser', '2019-10-10 13:45:14.904', 0, 1, 283, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1404, 'anonymousUser', '2019-10-10 13:45:14.905', 'anonymousUser', '2019-10-10 13:45:14.905', 0, 1, 284, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1405, 'anonymousUser', '2019-10-10 13:45:14.907', 'anonymousUser', '2019-10-10 13:45:14.907', 0, 1, 285, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1406, 'anonymousUser', '2019-10-10 13:45:14.909', 'anonymousUser', '2019-10-10 13:45:14.909', 0, 1, 286, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1407, 'anonymousUser', '2019-10-10 13:45:14.911', 'anonymousUser', '2019-10-10 13:45:14.911', 0, 1, 287, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1408, 'anonymousUser', '2019-10-10 13:45:14.913', 'anonymousUser', '2019-10-10 13:45:14.913', 0, 1, 288, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1409, 'anonymousUser', '2019-10-10 13:45:14.915', 'anonymousUser', '2019-10-10 13:45:14.915', 0, 1, 289, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1410, 'anonymousUser', '2019-10-10 13:45:14.917', 'anonymousUser', '2019-10-10 13:45:14.917', 0, 1, 290, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1411, 'anonymousUser', '2019-10-10 13:45:14.919', 'anonymousUser', '2019-10-10 13:45:14.919', 0, 1, 291, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1412, 'anonymousUser', '2019-10-10 13:45:14.922', 'anonymousUser', '2019-10-10 13:45:14.922', 0, 1, 292, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1413, 'anonymousUser', '2019-10-10 13:45:14.924', 'anonymousUser', '2019-10-10 13:45:14.924', 0, 1, 279, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1414, 'anonymousUser', '2019-10-10 13:45:14.926', 'anonymousUser', '2019-10-10 13:45:14.926', 0, 1, 280, 1);
INSERT INTO role_privilege_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, privilege_id, role_id)
VALUES(1415, 'anonymousUser', '2019-10-10 13:45:14.928', 'anonymousUser', '2019-10-10 13:45:14.928', 0, 1, 295, 1);


INSERT INTO client_role_v1
(id, created_by, created_on, modified_by, modified_on, version, client_code, section_id, role_id)
VALUES(1, 'admin', '2019-08-01 00:00:00.000', 'anonymousUser', '2019-10-10 15:19:24.805', 2, '909464', NULL, 1);


INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2784, 'anonymousUser', '2019-10-10 15:19:22.913', 'anonymousUser', '2019-10-10 15:19:22.913', 0, 1, 1, 45);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2785, 'anonymousUser', '2019-10-10 15:19:22.918', 'anonymousUser', '2019-10-10 15:19:22.918', 0, 1, 1, 46);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2786, 'anonymousUser', '2019-10-10 15:19:22.921', 'anonymousUser', '2019-10-10 15:19:22.921', 0, 1, 1, 43);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2787, 'anonymousUser', '2019-10-10 15:19:22.922', 'anonymousUser', '2019-10-10 15:19:22.922', 0, 1, 1, 44);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2788, 'anonymousUser', '2019-10-10 15:19:22.924', 'anonymousUser', '2019-10-10 15:19:22.924', 0, 1, 1, 49);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2789, 'anonymousUser', '2019-10-10 15:19:22.926', 'anonymousUser', '2019-10-10 15:19:22.926', 0, 1, 1, 50);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2790, 'anonymousUser', '2019-10-10 15:19:22.928', 'anonymousUser', '2019-10-10 15:19:22.928', 0, 1, 1, 51);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2791, 'anonymousUser', '2019-10-10 15:19:22.930', 'anonymousUser', '2019-10-10 15:19:22.930', 0, 1, 1, 52);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2792, 'anonymousUser', '2019-10-10 15:19:22.931', 'anonymousUser', '2019-10-10 15:19:22.931', 0, 1, 1, 53);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2793, 'anonymousUser', '2019-10-10 15:19:22.934', 'anonymousUser', '2019-10-10 15:19:22.934', 0, 1, 1, 54);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2794, 'anonymousUser', '2019-10-10 15:19:22.936', 'anonymousUser', '2019-10-10 15:19:22.936', 0, 1, 1, 55);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2795, 'anonymousUser', '2019-10-10 15:19:22.938', 'anonymousUser', '2019-10-10 15:19:22.938', 0, 1, 1, 56);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2796, 'anonymousUser', '2019-10-10 15:19:22.942', 'anonymousUser', '2019-10-10 15:19:22.942', 0, 1, 1, 47);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2797, 'anonymousUser', '2019-10-10 15:19:22.944', 'anonymousUser', '2019-10-10 15:19:22.944', 0, 1, 1, 48);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2798, 'anonymousUser', '2019-10-10 15:19:22.946', 'anonymousUser', '2019-10-10 15:19:22.946', 0, 1, 1, 59);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2799, 'anonymousUser', '2019-10-10 15:19:22.948', 'anonymousUser', '2019-10-10 15:19:22.948', 0, 1, 1, 60);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2800, 'anonymousUser', '2019-10-10 15:19:22.949', 'anonymousUser', '2019-10-10 15:19:22.949', 0, 1, 1, 61);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2801, 'anonymousUser', '2019-10-10 15:19:22.951', 'anonymousUser', '2019-10-10 15:19:22.951', 0, 1, 1, 62);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2802, 'anonymousUser', '2019-10-10 15:19:22.953', 'anonymousUser', '2019-10-10 15:19:22.953', 0, 1, 1, 57);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2803, 'anonymousUser', '2019-10-10 15:19:22.958', 'anonymousUser', '2019-10-10 15:19:22.958', 0, 1, 1, 58);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2804, 'anonymousUser', '2019-10-10 15:19:22.962', 'anonymousUser', '2019-10-10 15:19:22.962', 0, 1, 1, 67);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2805, 'anonymousUser', '2019-10-10 15:19:22.964', 'anonymousUser', '2019-10-10 15:19:22.964', 0, 1, 1, 68);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2806, 'anonymousUser', '2019-10-10 15:19:22.966', 'anonymousUser', '2019-10-10 15:19:22.966', 0, 1, 1, 69);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2807, 'anonymousUser', '2019-10-10 15:19:22.968', 'anonymousUser', '2019-10-10 15:19:22.968', 0, 1, 1, 70);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2808, 'anonymousUser', '2019-10-10 15:19:22.970', 'anonymousUser', '2019-10-10 15:19:22.970', 0, 1, 1, 65);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2809, 'anonymousUser', '2019-10-10 15:19:22.972', 'anonymousUser', '2019-10-10 15:19:22.972', 0, 1, 1, 66);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2810, 'anonymousUser', '2019-10-10 15:19:22.973', 'anonymousUser', '2019-10-10 15:19:22.973', 0, 1, 1, 63);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2811, 'anonymousUser', '2019-10-10 15:19:22.975', 'anonymousUser', '2019-10-10 15:19:22.975', 0, 1, 1, 64);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2812, 'anonymousUser', '2019-10-10 15:19:22.977', 'anonymousUser', '2019-10-10 15:19:22.977', 0, 1, 1, 73);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2813, 'anonymousUser', '2019-10-10 15:19:22.978', 'anonymousUser', '2019-10-10 15:19:22.978', 0, 1, 1, 74);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2814, 'anonymousUser', '2019-10-10 15:19:22.980', 'anonymousUser', '2019-10-10 15:19:22.980', 0, 1, 1, 75);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2815, 'anonymousUser', '2019-10-10 15:19:22.982', 'anonymousUser', '2019-10-10 15:19:22.982', 0, 1, 1, 76);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2816, 'anonymousUser', '2019-10-10 15:19:22.984', 'anonymousUser', '2019-10-10 15:19:22.984', 0, 1, 1, 77);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2817, 'anonymousUser', '2019-10-10 15:19:22.986', 'anonymousUser', '2019-10-10 15:19:22.986', 0, 1, 1, 78);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2818, 'anonymousUser', '2019-10-10 15:19:22.988', 'anonymousUser', '2019-10-10 15:19:22.988', 0, 1, 1, 71);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2819, 'anonymousUser', '2019-10-10 15:19:22.990', 'anonymousUser', '2019-10-10 15:19:22.990', 0, 1, 1, 72);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2820, 'anonymousUser', '2019-10-10 15:19:22.992', 'anonymousUser', '2019-10-10 15:19:22.992', 0, 1, 1, 97);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2821, 'anonymousUser', '2019-10-10 15:19:22.994', 'anonymousUser', '2019-10-10 15:19:22.994', 0, 1, 1, 98);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2822, 'anonymousUser', '2019-10-10 15:19:22.996', 'anonymousUser', '2019-10-10 15:19:22.996', 0, 1, 1, 99);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2823, 'anonymousUser', '2019-10-10 15:19:22.997', 'anonymousUser', '2019-10-10 15:19:22.997', 0, 1, 1, 100);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2824, 'anonymousUser', '2019-10-10 15:19:23.000', 'anonymousUser', '2019-10-10 15:19:23.000', 0, 1, 1, 101);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2825, 'anonymousUser', '2019-10-10 15:19:23.002', 'anonymousUser', '2019-10-10 15:19:23.002', 0, 1, 1, 102);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2826, 'anonymousUser', '2019-10-10 15:19:23.003', 'anonymousUser', '2019-10-10 15:19:23.003', 0, 1, 1, 103);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2827, 'anonymousUser', '2019-10-10 15:19:23.005', 'anonymousUser', '2019-10-10 15:19:23.005', 0, 1, 1, 104);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2828, 'anonymousUser', '2019-10-10 15:19:23.012', 'anonymousUser', '2019-10-10 15:19:23.012', 0, 1, 1, 105);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2829, 'anonymousUser', '2019-10-10 15:19:23.014', 'anonymousUser', '2019-10-10 15:19:23.014', 0, 1, 1, 106);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2830, 'anonymousUser', '2019-10-10 15:19:23.016', 'anonymousUser', '2019-10-10 15:19:23.016', 0, 1, 1, 107);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2831, 'anonymousUser', '2019-10-10 15:19:23.018', 'anonymousUser', '2019-10-10 15:19:23.018', 0, 1, 1, 108);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2832, 'anonymousUser', '2019-10-10 15:19:23.020', 'anonymousUser', '2019-10-10 15:19:23.020', 0, 1, 1, 81);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2833, 'anonymousUser', '2019-10-10 15:19:23.022', 'anonymousUser', '2019-10-10 15:19:23.022', 0, 1, 1, 82);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2834, 'anonymousUser', '2019-10-10 15:19:23.023', 'anonymousUser', '2019-10-10 15:19:23.023', 0, 1, 1, 109);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2835, 'anonymousUser', '2019-10-10 15:19:23.029', 'anonymousUser', '2019-10-10 15:19:23.029', 0, 1, 1, 110);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2836, 'anonymousUser', '2019-10-10 15:19:23.031', 'anonymousUser', '2019-10-10 15:19:23.031', 0, 1, 1, 111);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2837, 'anonymousUser', '2019-10-10 15:19:23.032', 'anonymousUser', '2019-10-10 15:19:23.032', 0, 1, 1, 112);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2838, 'anonymousUser', '2019-10-10 15:19:23.034', 'anonymousUser', '2019-10-10 15:19:23.034', 0, 1, 1, 113);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2839, 'anonymousUser', '2019-10-10 15:19:23.037', 'anonymousUser', '2019-10-10 15:19:23.037', 0, 1, 1, 114);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2840, 'anonymousUser', '2019-10-10 15:19:23.039', 'anonymousUser', '2019-10-10 15:19:23.039', 0, 1, 1, 83);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2841, 'anonymousUser', '2019-10-10 15:19:23.040', 'anonymousUser', '2019-10-10 15:19:23.040', 0, 1, 1, 84);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2842, 'anonymousUser', '2019-10-10 15:19:23.042', 'anonymousUser', '2019-10-10 15:19:23.042', 0, 1, 1, 115);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2843, 'anonymousUser', '2019-10-10 15:19:23.044', 'anonymousUser', '2019-10-10 15:19:23.044', 0, 1, 1, 116);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2844, 'anonymousUser', '2019-10-10 15:19:23.045', 'anonymousUser', '2019-10-10 15:19:23.045', 0, 1, 1, 117);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2845, 'anonymousUser', '2019-10-10 15:19:23.047', 'anonymousUser', '2019-10-10 15:19:23.047', 0, 1, 1, 118);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2846, 'anonymousUser', '2019-10-10 15:19:23.049', 'anonymousUser', '2019-10-10 15:19:23.049', 0, 1, 1, 119);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2847, 'anonymousUser', '2019-10-10 15:19:23.051', 'anonymousUser', '2019-10-10 15:19:23.051', 0, 1, 1, 120);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2848, 'anonymousUser', '2019-10-10 15:19:23.053', 'anonymousUser', '2019-10-10 15:19:23.053', 0, 1, 1, 121);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2849, 'anonymousUser', '2019-10-10 15:19:23.054', 'anonymousUser', '2019-10-10 15:19:23.054', 0, 1, 1, 122);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2850, 'anonymousUser', '2019-10-10 15:19:23.058', 'anonymousUser', '2019-10-10 15:19:23.058', 0, 1, 1, 123);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2851, 'anonymousUser', '2019-10-10 15:19:23.060', 'anonymousUser', '2019-10-10 15:19:23.060', 0, 1, 1, 124);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2852, 'anonymousUser', '2019-10-10 15:19:23.062', 'anonymousUser', '2019-10-10 15:19:23.062', 0, 1, 1, 125);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2853, 'anonymousUser', '2019-10-10 15:19:23.064', 'anonymousUser', '2019-10-10 15:19:23.064', 0, 1, 1, 126);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2854, 'anonymousUser', '2019-10-10 15:19:23.066', 'anonymousUser', '2019-10-10 15:19:23.066', 0, 1, 1, 85);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2855, 'anonymousUser', '2019-10-10 15:19:23.071', 'anonymousUser', '2019-10-10 15:19:23.071', 0, 1, 1, 86);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2856, 'anonymousUser', '2019-10-10 15:19:23.073', 'anonymousUser', '2019-10-10 15:19:23.073', 0, 1, 1, 127);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2857, 'anonymousUser', '2019-10-10 15:19:23.077', 'anonymousUser', '2019-10-10 15:19:23.077', 0, 1, 1, 128);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2858, 'anonymousUser', '2019-10-10 15:19:23.079', 'anonymousUser', '2019-10-10 15:19:23.079', 0, 1, 1, 129);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2859, 'anonymousUser', '2019-10-10 15:19:23.081', 'anonymousUser', '2019-10-10 15:19:23.081', 0, 1, 1, 130);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2860, 'anonymousUser', '2019-10-10 15:19:23.083', 'anonymousUser', '2019-10-10 15:19:23.083', 0, 1, 1, 87);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2861, 'anonymousUser', '2019-10-10 15:19:23.084', 'anonymousUser', '2019-10-10 15:19:23.084', 0, 1, 1, 88);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2862, 'anonymousUser', '2019-10-10 15:19:23.086', 'anonymousUser', '2019-10-10 15:19:23.086', 0, 1, 1, 131);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2863, 'anonymousUser', '2019-10-10 15:19:23.089', 'anonymousUser', '2019-10-10 15:19:23.089', 0, 1, 1, 132);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2864, 'anonymousUser', '2019-10-10 15:19:23.090', 'anonymousUser', '2019-10-10 15:19:23.090', 0, 1, 1, 133);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2865, 'anonymousUser', '2019-10-10 15:19:23.093', 'anonymousUser', '2019-10-10 15:19:23.093', 0, 1, 1, 134);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2866, 'anonymousUser', '2019-10-10 15:19:23.094', 'anonymousUser', '2019-10-10 15:19:23.094', 0, 1, 1, 135);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2867, 'anonymousUser', '2019-10-10 15:19:23.097', 'anonymousUser', '2019-10-10 15:19:23.097', 0, 1, 1, 136);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2868, 'anonymousUser', '2019-10-10 15:19:23.098', 'anonymousUser', '2019-10-10 15:19:23.098', 0, 1, 1, 137);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2869, 'anonymousUser', '2019-10-10 15:19:23.100', 'anonymousUser', '2019-10-10 15:19:23.100', 0, 1, 1, 138);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2870, 'anonymousUser', '2019-10-10 15:19:23.102', 'anonymousUser', '2019-10-10 15:19:23.102', 0, 1, 1, 89);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2871, 'anonymousUser', '2019-10-10 15:19:23.105', 'anonymousUser', '2019-10-10 15:19:23.105', 0, 1, 1, 90);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2872, 'anonymousUser', '2019-10-10 15:19:23.107', 'anonymousUser', '2019-10-10 15:19:23.107', 0, 1, 1, 91);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2873, 'anonymousUser', '2019-10-10 15:19:23.108', 'anonymousUser', '2019-10-10 15:19:23.108', 0, 1, 1, 92);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2874, 'anonymousUser', '2019-10-10 15:19:23.110', 'anonymousUser', '2019-10-10 15:19:23.110', 0, 1, 1, 139);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2875, 'anonymousUser', '2019-10-10 15:19:23.112', 'anonymousUser', '2019-10-10 15:19:23.112', 0, 1, 1, 140);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2876, 'anonymousUser', '2019-10-10 15:19:23.115', 'anonymousUser', '2019-10-10 15:19:23.115', 0, 1, 1, 141);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2877, 'anonymousUser', '2019-10-10 15:19:23.116', 'anonymousUser', '2019-10-10 15:19:23.116', 0, 1, 1, 142);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2878, 'anonymousUser', '2019-10-10 15:19:23.118', 'anonymousUser', '2019-10-10 15:19:23.118', 0, 1, 1, 143);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2879, 'anonymousUser', '2019-10-10 15:19:23.120', 'anonymousUser', '2019-10-10 15:19:23.120', 0, 1, 1, 144);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2880, 'anonymousUser', '2019-10-10 15:19:23.122', 'anonymousUser', '2019-10-10 15:19:23.122', 0, 1, 1, 93);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2881, 'anonymousUser', '2019-10-10 15:19:23.123', 'anonymousUser', '2019-10-10 15:19:23.123', 0, 1, 1, 94);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2882, 'anonymousUser', '2019-10-10 15:19:23.125', 'anonymousUser', '2019-10-10 15:19:23.125', 0, 1, 1, 95);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2883, 'anonymousUser', '2019-10-10 15:19:23.127', 'anonymousUser', '2019-10-10 15:19:23.127', 0, 1, 1, 96);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2884, 'anonymousUser', '2019-10-10 15:19:23.129', 'anonymousUser', '2019-10-10 15:19:23.129', 0, 1, 1, 79);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2885, 'anonymousUser', '2019-10-10 15:19:23.136', 'anonymousUser', '2019-10-10 15:19:23.136', 0, 1, 1, 80);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2886, 'anonymousUser', '2019-10-10 15:19:23.138', 'anonymousUser', '2019-10-10 15:19:23.138', 0, 1, 1, 147);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2887, 'anonymousUser', '2019-10-10 15:19:23.139', 'anonymousUser', '2019-10-10 15:19:23.139', 0, 1, 1, 148);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2888, 'anonymousUser', '2019-10-10 15:19:23.141', 'anonymousUser', '2019-10-10 15:19:23.141', 0, 1, 1, 149);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2889, 'anonymousUser', '2019-10-10 15:19:23.142', 'anonymousUser', '2019-10-10 15:19:23.142', 0, 1, 1, 150);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2890, 'anonymousUser', '2019-10-10 15:19:23.144', 'anonymousUser', '2019-10-10 15:19:23.144', 0, 1, 1, 151);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2891, 'anonymousUser', '2019-10-10 15:19:23.146', 'anonymousUser', '2019-10-10 15:19:23.146', 0, 1, 1, 152);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2892, 'anonymousUser', '2019-10-10 15:19:23.147', 'anonymousUser', '2019-10-10 15:19:23.147', 0, 1, 1, 153);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2893, 'anonymousUser', '2019-10-10 15:19:23.153', 'anonymousUser', '2019-10-10 15:19:23.153', 0, 1, 1, 154);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2894, 'anonymousUser', '2019-10-10 15:19:23.155', 'anonymousUser', '2019-10-10 15:19:23.155', 0, 1, 1, 155);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2895, 'anonymousUser', '2019-10-10 15:19:23.157', 'anonymousUser', '2019-10-10 15:19:23.157', 0, 1, 1, 156);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2896, 'anonymousUser', '2019-10-10 15:19:23.159', 'anonymousUser', '2019-10-10 15:19:23.159', 0, 1, 1, 157);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2897, 'anonymousUser', '2019-10-10 15:19:23.160', 'anonymousUser', '2019-10-10 15:19:23.160', 0, 1, 1, 158);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2898, 'anonymousUser', '2019-10-10 15:19:23.162', 'anonymousUser', '2019-10-10 15:19:23.162', 0, 1, 1, 159);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2899, 'anonymousUser', '2019-10-10 15:19:23.164', 'anonymousUser', '2019-10-10 15:19:23.164', 0, 1, 1, 160);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2900, 'anonymousUser', '2019-10-10 15:19:23.166', 'anonymousUser', '2019-10-10 15:19:23.166', 0, 1, 1, 313);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2901, 'anonymousUser', '2019-10-10 15:19:23.167', 'anonymousUser', '2019-10-10 15:19:23.167', 0, 1, 1, 314);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2902, 'anonymousUser', '2019-10-10 15:19:23.169', 'anonymousUser', '2019-10-10 15:19:23.169', 0, 1, 1, 315);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2903, 'anonymousUser', '2019-10-10 15:19:23.171', 'anonymousUser', '2019-10-10 15:19:23.171', 0, 1, 1, 316);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2904, 'anonymousUser', '2019-10-10 15:19:23.173', 'anonymousUser', '2019-10-10 15:19:23.173', 0, 1, 1, 145);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2905, 'anonymousUser', '2019-10-10 15:19:23.175', 'anonymousUser', '2019-10-10 15:19:23.175', 0, 1, 1, 146);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2906, 'anonymousUser', '2019-10-10 15:19:23.176', 'anonymousUser', '2019-10-10 15:19:23.176', 0, 1, 1, 163);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2907, 'anonymousUser', '2019-10-10 15:19:23.178', 'anonymousUser', '2019-10-10 15:19:23.178', 0, 1, 1, 164);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2908, 'anonymousUser', '2019-10-10 15:19:23.180', 'anonymousUser', '2019-10-10 15:19:23.180', 0, 1, 1, 161);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2909, 'anonymousUser', '2019-10-10 15:19:23.182', 'anonymousUser', '2019-10-10 15:19:23.182', 0, 1, 1, 162);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2910, 'anonymousUser', '2019-10-10 15:19:23.184', 'anonymousUser', '2019-10-10 15:19:23.184', 0, 1, 1, 169);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2911, 'anonymousUser', '2019-10-10 15:19:23.186', 'anonymousUser', '2019-10-10 15:19:23.186', 0, 1, 1, 170);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2912, 'anonymousUser', '2019-10-10 15:19:23.188', 'anonymousUser', '2019-10-10 15:19:23.188', 0, 1, 1, 171);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2913, 'anonymousUser', '2019-10-10 15:19:23.190', 'anonymousUser', '2019-10-10 15:19:23.190', 0, 1, 1, 172);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2914, 'anonymousUser', '2019-10-10 15:19:23.193', 'anonymousUser', '2019-10-10 15:19:23.193', 0, 1, 1, 167);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2915, 'anonymousUser', '2019-10-10 15:19:23.194', 'anonymousUser', '2019-10-10 15:19:23.194', 0, 1, 1, 168);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2916, 'anonymousUser', '2019-10-10 15:19:23.197', 'anonymousUser', '2019-10-10 15:19:23.197', 0, 1, 1, 165);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2917, 'anonymousUser', '2019-10-10 15:19:23.199', 'anonymousUser', '2019-10-10 15:19:23.199', 0, 1, 1, 166);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2918, 'anonymousUser', '2019-10-10 15:19:23.201', 'anonymousUser', '2019-10-10 15:19:23.201', 0, 1, 1, 177);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2919, 'anonymousUser', '2019-10-10 15:19:23.203', 'anonymousUser', '2019-10-10 15:19:23.203', 0, 1, 1, 178);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2920, 'anonymousUser', '2019-10-10 15:19:23.205', 'anonymousUser', '2019-10-10 15:19:23.205', 0, 1, 1, 243);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2921, 'anonymousUser', '2019-10-10 15:19:23.207', 'anonymousUser', '2019-10-10 15:19:23.207', 0, 1, 1, 244);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2922, 'anonymousUser', '2019-10-10 15:19:23.209', 'anonymousUser', '2019-10-10 15:19:23.209', 0, 1, 1, 245);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2923, 'anonymousUser', '2019-10-10 15:19:23.211', 'anonymousUser', '2019-10-10 15:19:23.211', 0, 1, 1, 246);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2924, 'anonymousUser', '2019-10-10 15:19:23.213', 'anonymousUser', '2019-10-10 15:19:23.213', 0, 1, 1, 247);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2925, 'anonymousUser', '2019-10-10 15:19:23.215', 'anonymousUser', '2019-10-10 15:19:23.215', 0, 1, 1, 248);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2926, 'anonymousUser', '2019-10-10 15:19:23.217', 'anonymousUser', '2019-10-10 15:19:23.217', 0, 1, 1, 249);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2927, 'anonymousUser', '2019-10-10 15:19:23.219', 'anonymousUser', '2019-10-10 15:19:23.219', 0, 1, 1, 250);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2928, 'anonymousUser', '2019-10-10 15:19:23.221', 'anonymousUser', '2019-10-10 15:19:23.221', 0, 1, 1, 251);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2929, 'anonymousUser', '2019-10-10 15:19:23.223', 'anonymousUser', '2019-10-10 15:19:23.223', 0, 1, 1, 252);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2930, 'anonymousUser', '2019-10-10 15:19:23.225', 'anonymousUser', '2019-10-10 15:19:23.225', 0, 1, 1, 253);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2931, 'anonymousUser', '2019-10-10 15:19:23.227', 'anonymousUser', '2019-10-10 15:19:23.227', 0, 1, 1, 254);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2932, 'anonymousUser', '2019-10-10 15:19:23.229', 'anonymousUser', '2019-10-10 15:19:23.229', 0, 1, 1, 255);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2933, 'anonymousUser', '2019-10-10 15:19:23.230', 'anonymousUser', '2019-10-10 15:19:23.230', 0, 1, 1, 256);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2934, 'anonymousUser', '2019-10-10 15:19:23.232', 'anonymousUser', '2019-10-10 15:19:23.232', 0, 1, 1, 257);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2935, 'anonymousUser', '2019-10-10 15:19:23.238', 'anonymousUser', '2019-10-10 15:19:23.238', 0, 1, 1, 258);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2936, 'anonymousUser', '2019-10-10 15:19:23.240', 'anonymousUser', '2019-10-10 15:19:23.240', 0, 1, 1, 259);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2937, 'anonymousUser', '2019-10-10 15:19:23.242', 'anonymousUser', '2019-10-10 15:19:23.242', 0, 1, 1, 176);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2938, 'anonymousUser', '2019-10-10 15:19:23.243', 'anonymousUser', '2019-10-10 15:19:23.243', 0, 1, 1, 174);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2939, 'anonymousUser', '2019-10-10 15:19:23.245', 'anonymousUser', '2019-10-10 15:19:23.245', 0, 1, 1, 227);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2940, 'anonymousUser', '2019-10-10 15:19:23.248', 'anonymousUser', '2019-10-10 15:19:23.248', 0, 1, 1, 228);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2941, 'anonymousUser', '2019-10-10 15:19:23.250', 'anonymousUser', '2019-10-10 15:19:23.250', 0, 1, 1, 225);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2942, 'anonymousUser', '2019-10-10 15:19:23.251', 'anonymousUser', '2019-10-10 15:19:23.251', 0, 1, 1, 226);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2943, 'anonymousUser', '2019-10-10 15:19:23.253', 'anonymousUser', '2019-10-10 15:19:23.253', 0, 1, 1, 231);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2944, 'anonymousUser', '2019-10-10 15:19:23.255', 'anonymousUser', '2019-10-10 15:19:23.255', 0, 1, 1, 232);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2945, 'anonymousUser', '2019-10-10 15:19:23.257', 'anonymousUser', '2019-10-10 15:19:23.257', 0, 1, 1, 229);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2946, 'anonymousUser', '2019-10-10 15:19:23.260', 'anonymousUser', '2019-10-10 15:19:23.260', 0, 1, 1, 230);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2947, 'anonymousUser', '2019-10-10 15:19:23.261', 'anonymousUser', '2019-10-10 15:19:23.261', 0, 1, 1, 241);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2948, 'anonymousUser', '2019-10-10 15:19:23.264', 'anonymousUser', '2019-10-10 15:19:23.264', 0, 1, 1, 242);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2949, 'anonymousUser', '2019-10-10 15:19:23.265', 'anonymousUser', '2019-10-10 15:19:23.265', 0, 1, 1, 325);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2950, 'anonymousUser', '2019-10-10 15:19:23.267', 'anonymousUser', '2019-10-10 15:19:23.267', 0, 1, 1, 326);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2951, 'anonymousUser', '2019-10-10 15:19:23.269', 'anonymousUser', '2019-10-10 15:19:23.269', 0, 1, 1, 239);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2952, 'anonymousUser', '2019-10-10 15:19:23.271', 'anonymousUser', '2019-10-10 15:19:23.271', 0, 1, 1, 240);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2953, 'anonymousUser', '2019-10-10 15:19:23.273', 'anonymousUser', '2019-10-10 15:19:23.273', 0, 1, 1, 263);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2954, 'anonymousUser', '2019-10-10 15:19:23.275', 'anonymousUser', '2019-10-10 15:19:23.275', 0, 1, 1, 264);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2955, 'anonymousUser', '2019-10-10 15:19:23.277', 'anonymousUser', '2019-10-10 15:19:23.277', 0, 1, 1, 267);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2956, 'anonymousUser', '2019-10-10 15:19:23.278', 'anonymousUser', '2019-10-10 15:19:23.278', 0, 1, 1, 268);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2957, 'anonymousUser', '2019-10-10 15:19:23.280', 'anonymousUser', '2019-10-10 15:19:23.280', 0, 1, 1, 269);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2958, 'anonymousUser', '2019-10-10 15:19:23.282', 'anonymousUser', '2019-10-10 15:19:23.282', 0, 1, 1, 270);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2959, 'anonymousUser', '2019-10-10 15:19:23.284', 'anonymousUser', '2019-10-10 15:19:23.284', 0, 1, 1, 271);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2960, 'anonymousUser', '2019-10-10 15:19:23.286', 'anonymousUser', '2019-10-10 15:19:23.286', 0, 1, 1, 272);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2961, 'anonymousUser', '2019-10-10 15:19:23.288', 'anonymousUser', '2019-10-10 15:19:23.288', 0, 1, 1, 273);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2962, 'anonymousUser', '2019-10-10 15:19:23.290', 'anonymousUser', '2019-10-10 15:19:23.290', 0, 1, 1, 274);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2963, 'anonymousUser', '2019-10-10 15:19:23.292', 'anonymousUser', '2019-10-10 15:19:23.292', 0, 1, 1, 275);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2964, 'anonymousUser', '2019-10-10 15:19:23.294', 'anonymousUser', '2019-10-10 15:19:23.294', 0, 1, 1, 276);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2965, 'anonymousUser', '2019-10-10 15:19:23.296', 'anonymousUser', '2019-10-10 15:19:23.296', 0, 1, 1, 277);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2966, 'anonymousUser', '2019-10-10 15:19:23.297', 'anonymousUser', '2019-10-10 15:19:23.297', 0, 1, 1, 278);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2967, 'anonymousUser', '2019-10-10 15:19:23.299', 'anonymousUser', '2019-10-10 15:19:23.299', 0, 1, 1, 265);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2968, 'anonymousUser', '2019-10-10 15:19:23.301', 'anonymousUser', '2019-10-10 15:19:23.301', 0, 1, 1, 266);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2969, 'anonymousUser', '2019-10-10 15:19:23.303', 'anonymousUser', '2019-10-10 15:19:23.303', 0, 1, 1, 281);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2970, 'anonymousUser', '2019-10-10 15:19:23.305', 'anonymousUser', '2019-10-10 15:19:23.305', 0, 1, 1, 282);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2971, 'anonymousUser', '2019-10-10 15:19:23.308', 'anonymousUser', '2019-10-10 15:19:23.308', 0, 1, 1, 283);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2972, 'anonymousUser', '2019-10-10 15:19:23.309', 'anonymousUser', '2019-10-10 15:19:23.309', 0, 1, 1, 284);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2973, 'anonymousUser', '2019-10-10 15:19:23.311', 'anonymousUser', '2019-10-10 15:19:23.311', 0, 1, 1, 285);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2974, 'anonymousUser', '2019-10-10 15:19:23.314', 'anonymousUser', '2019-10-10 15:19:23.314', 0, 1, 1, 286);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2975, 'anonymousUser', '2019-10-10 15:19:23.315', 'anonymousUser', '2019-10-10 15:19:23.315', 0, 1, 1, 287);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2976, 'anonymousUser', '2019-10-10 15:19:23.318', 'anonymousUser', '2019-10-10 15:19:23.318', 0, 1, 1, 288);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2977, 'anonymousUser', '2019-10-10 15:19:23.319', 'anonymousUser', '2019-10-10 15:19:23.319', 0, 1, 1, 289);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2978, 'anonymousUser', '2019-10-10 15:19:23.321', 'anonymousUser', '2019-10-10 15:19:23.321', 0, 1, 1, 290);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2979, 'anonymousUser', '2019-10-10 15:19:23.323', 'anonymousUser', '2019-10-10 15:19:23.323', 0, 1, 1, 291);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2980, 'anonymousUser', '2019-10-10 15:19:23.327', 'anonymousUser', '2019-10-10 15:19:23.327', 0, 1, 1, 292);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2981, 'anonymousUser', '2019-10-10 15:19:23.329', 'anonymousUser', '2019-10-10 15:19:23.329', 0, 1, 1, 279);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2982, 'anonymousUser', '2019-10-10 15:19:23.330', 'anonymousUser', '2019-10-10 15:19:23.330', 0, 1, 1, 280);
INSERT INTO client_privileges_v1
(id, created_by, created_on, modified_by, modified_on, version, access_group_id, client_role_id, privilege_id)
VALUES(2983, 'anonymousUser', '2019-10-10 15:19:23.337', 'anonymousUser', '2019-10-10 15:19:23.337', 0, 1, 1, 295);


INSERT INTO user_v1
(id, created_by, created_on, modified_by, modified_on, version, authentication_type, email, end_date, first_name, invalid_attempts, is_first_login, is_policy_accepted, is_policy_updated, is_primary, last_name, mobile, password, start_date, token_value)
VALUES(1, 'admin', '2019-08-01 00:00:00.000', 'admin@bbsi.com', '2019-10-10 13:40:01.394', 4, NULL, 'admin@bbsi.com', NULL, 'Admin', 0, 0, 1, 0, 1, 'Test', '9999999999', '$2a$12$rXO5mNcTwRCU6MZ6IKgqdOj1ad//1ZrvjzhpnBIWgr9cpFwjWTxJ2', NULL, '');

INSERT INTO user_clients_v1
(id, created_by, created_on, modified_by, modified_on, version, client_code, client_name, cost_center_code, cost_center_desc, employee_code, is_active, is_i9approver, is_primary, new_hire_id, user_type, user_id)
VALUES(4, 'admin@bbsi.com', '2019-10-10 13:40:01.389', 'admin@bbsi.com', '2019-10-10 13:40:01.405', 1, '909143', '2 INFINITY, INC.', '050', 'Ontario', NULL, 1, 0, 0, 0, 'Vancouver Operations Center', 1);
INSERT INTO user_clients_v1
(id, created_by, created_on, modified_by, modified_on, version, client_code, client_name, cost_center_code, cost_center_desc, employee_code, is_active, is_i9approver, is_primary, new_hire_id, user_type, user_id)
VALUES(5, 'admin@bbsi.com', '2019-10-10 13:40:01.389', 'admin@bbsi.com', '2019-10-10 13:40:01.405', 1, '909464', '1 HOUR DRAIN', '015', 'San Jose', NULL, 1, 0, 1, 0, 'Vancouver Operations Center', 1);

INSERT INTO user_role_association
(user_id, role_id)
VALUES(1, 1);
