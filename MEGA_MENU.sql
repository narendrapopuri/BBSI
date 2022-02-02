--delete menu scripts:
delete from role_privileges;
delete from privilege;
delete from privilege_group;
delete from feature_code;
delete from webform;
delete from menu_favourites;
delete from menu_map;
delete from menu_item;
delete from menu;


INSERT INTO privilege_group(id, created_by, created_on, modified_by, modified_on, version, client_code, description, name)VALUES(1, 'admin', '2019-02-10', 'admin', '2019-02-19', 0, '870', 'Employee Privilege Group', 'Employee');

INSERT INTO privilege_group(id, created_by, created_on, modified_by, modified_on, version, client_code, description, name)VALUES(2, 'admin', '2019-02-10', 'admin', '2019-02-19', 0, '870', 'User Privilege Group', 'User');


-- Administration
INSERT INTO menu(id, description, icon_url, is_active, name, parent_id)VALUES(1, 'Administration', '', 1, 'Administration', 0);
--Company Information
INSERT INTO feature_code(id, code, description, module, name)VALUES(1, 'COMP.INFO', 'Company Information', 'module', 'Company Information');
INSERT INTO webform(id, description, name, [type], value) VALUES(1,'Company Information', 'Company Information', 'Dynamic', 'Companyinformation');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(1, 'Comapany Information', 'Company Information', '2019-02-27', 'assets/images/companyInformation.svg', 1, 'Company Information', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(1,'FULL', 'COMPINFO.FULL', 'Company Information manage', 'MANAGE', 1, 1,1);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(2,'READ', 'COMPINFO.READ', 'Company Information view', 'VIEW', 1, 1,1);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(1,1, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(1, 2, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(2, 2, 2, 2);

-- Audit Log
INSERT INTO feature_code(id, code, description, module, name)VALUES(5, 'AUDITLOG', 'Audit Log', 'module', 'Audit Log');
INSERT INTO webform(id, description, name, [type], value) VALUES(5,'Audit Log', 'Audit Log', 'Dynamic', 'auditlog');

INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(5, 'Audit Log', 'Audit Log', '2019-02-27', 'assets/images/Audit_Log.svg', 1, 'Audit Log', '2019-02-27');

INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(9,'FULL', 'AUDITLOG.FULL', 'Audit Log manage', 'MANAGE', 5, 5, 5);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(10,'READ', 'AUDITLOG.READ', 'Audit Log view', 'VIEW', 5, 5, 5);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(5,1, 1, 5);

INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(9, 9, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(10, 10, 2, 2);

-- Company
INSERT INTO menu(id, description, icon_url, is_active, name, parent_id)VALUES(3, 'Company', 'assets/images/icon-roles-users-light.svg', 1, 'Company', 0);
-- Company Management
INSERT INTO menu(id, description, icon_url, is_active, name, parent_id)VALUES(4, 'Company Management', 'assets/images/companyManagement.svg', 1, 'Company Management', 3);
-- Workers Comp Class
INSERT INTO feature_code(id, code, description, module, name)VALUES(6, 'WORKCOMP', 'Workers Comp Class', 'module', 'Workers Comp Class');
INSERT INTO webform(id, description, name, [type], value) VALUES(6,'Workers Comp Class', 'Workers Comp Class', 'Dynamic', 'WORKCOMPCLASS');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(6, 'Workers Comp Class', 'Workers Comp Class', '2019-02-27', 'assets/images/icons-location-codes-light.svg', 1, 'Workers Comp Class', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(11,'FULL', 'WORKCOMP.FULL', 'Workers Comp Class mange', 'MANAGE', 6, 6, 6);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(12,'READ', 'WORKCOMP.READ', 'Workers Comp Class view', 'VIEW', 6, 6, 6);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(6,1, 4, 6);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(11, 12, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(12, 12, 2, 2);

-- Worksite Location (Location)
INSERT INTO feature_code(id, code, description, module, name)VALUES(7, 'LOC', 'Worksite Location', 'module', 'Worksite Location');
INSERT INTO webform(id, description, name, [type], value) VALUES(7,'Worksite Location', 'worksite location', 'Dynamic', 'worksitelocation');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(7, 'Worksite Location', 'Worksite Location', '2019-02-27', 'assets/images/icons-location-codes-light.svg', 1, 'Worksite Location', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(13,'FULL', 'LOC.FULL', 'Worksite Location manage', 'MANAGE', 7, 7, 7);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(14,'READ', 'LOC.READ', 'Worksite Location view', 'VIEW', 7, 7, 7);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(7,2, 4, 7);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(13, 13, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(14, 14, 2, 2);

--Department
INSERT INTO feature_code(id, code, description, module, name)VALUES(8, 'DEP', 'Department', 'module', 'Department');
INSERT INTO webform(id, description, name, [type], value) VALUES(8,'Department', 'department', 'Dynamic', 'department');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(8, 'Department', 'Department', '2019-02-27', 'assets/images/icon-department-codes-white.svg', 1, 'Department', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(15,'FULL', 'DEP.FULL', 'Department manage', 'MANAGE', 8, 8,8);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(16,'READ', 'DEP.READ', 'Department view', 'VIEW', 8, 8, 8);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(8,3, 4,8);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(15, 15, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(16, 16, 2, 2);

-- Project
INSERT INTO feature_code(id, code, description, module, name)VALUES(9, 'PROJ', 'Project', 'module', 'Project');
INSERT INTO webform(id, description, name, [type], value) VALUES(9,'Project', 'project', 'Dynamic', 'project');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(9, 'Project', 'Project', '2019-02-27', 'assets/images/icon-project-code-white.svg', 1, 'Project', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(17,'FULL', 'PROJ.FULL', 'Project manage', 'MANAGE', 9, 9, 9);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(18,'READ', 'PROJ.READ', 'Project view', 'VIEW', 9, 9, 9);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(9,4, 4, 9);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(17, 17, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(18, 18, 2, 2);

-- Occupation
INSERT INTO feature_code(id, code, description, module, name)VALUES(10, 'OCCU', 'Occupation', 'module', 'Occupation');
INSERT INTO webform(id, description, name, [type], value) VALUES(10,'Occupation', 'Occupation', 'Dynamic', 'occupation');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(10, 'Occupation', 'Occupation', '2019-02-27', 'assets/images/icon-project-code-white.svg', 1, 'Occupation', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(19,'FULL', 'OCCU.FULL', 'Occupation manage', 'MANAGE', 10, 10, 10);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(20,'READ', 'OCCU.READ', 'Occupation view', 'VIEW', 10, 10, 10);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(10,5, 4, 10);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(19, 19, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(20, 20, 2, 2);

--Division
INSERT INTO feature_code(id, code, description, module, name)VALUES(11, 'DIV', 'Division', 'module', 'Division');
INSERT INTO webform(id, description, name, [type], value) VALUES(11,'Division', 'division', 'Dynamic', 'division');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(11, 'Division', 'Division', '2019-02-27', 'assets/images/icon-divisionCodes-white.svg', 1, 'Division', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(21,'FULL', 'DIV.FULL', 'Division manage', 'MANAGE', 11, 11, 11);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(22,'READ', 'DIV.READ', 'Division view', 'VIEW', 11, 11, 11);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(11,6, 4, 11);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(21, 21, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(22, 22, 2, 2);

--Union
INSERT INTO feature_code(id, code, description, module, name)VALUES(12, 'UNI', 'Union', 'module', 'Union');
INSERT INTO webform(id, description, name, [type], value) VALUES(12,'Union', 'Union', 'Dynamic', 'union');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(12, 'Union', 'Union', '2019-02-27', 'assets/images/icon-divisionCodes-white.svg', 1, 'Union', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(23,'FULL', 'UNI.FULL', 'Union manage', 'MANAGE', 12, 12, 12);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(24,'READ', 'UNI.READ', 'Union view', 'VIEW', 12, 12, 12);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(12,7, 4, 12);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(23, 23, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(24, 24, 2, 2);

--SHIFT
INSERT INTO feature_code(id, code, description, module, name)VALUES(13, 'SHIFT', 'Shift', 'module', 'Shift');
INSERT INTO webform(id, description, name, [type], value) VALUES(13,'Shift', 'Shift', 'Dynamic', 'Shift');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(13, 'Shift', 'Shift', '2019-02-27', 'assets/images/icon-divisionCodes-white.svg', 1, 'Shift', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(25,'FULL', 'SHIFT.FULL', 'Shift manage', 'MANAGE', 13, 13, 13);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(26,'READ', 'SHIFT.READ', 'Shift view', 'VIEW', 13, 13, 13);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(13,8, 4, 13);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(25, 25, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(26, 26, 2, 2);

--EVENT
INSERT INTO feature_code(id, code, description, module, name)VALUES(14, 'EVENT', 'Event', 'module', 'Event');
INSERT INTO webform(id, description, name, [type], value) VALUES(14,'Event', 'Event', 'Dynamic', 'Event');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(14, 'Event', 'Event', '2019-02-27', 'assets/images/icon-divisionCodes-white.svg', 1, 'Event', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(27,'FULL', 'EVENT.FULL', 'Event manage', 'MANAGE', 14, 14, 14);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(28,'READ', 'EVENT.READ', 'Event view', 'VIEW', 14, 14, 14);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(14,9, 4, 14);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(27, 27, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(28, 28, 2, 2);

--WORK GROUP
INSERT INTO feature_code(id, code, description, module, name)VALUES(15, 'WRKGRP', 'Work Group', 'module', 'Work Group');
INSERT INTO webform(id, description, name, [type], value) VALUES(15,'Work Group', 'Work Group', 'Dynamic', 'Workgroup');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(15, 'Work Group', 'Work Group', '2019-02-27', 'assets/images/icon-divisionCodes-white.svg', 1, 'Work Group', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(29,'FULL', 'WRKGRP.FULL', 'Event manage', 'MANAGE', 15, 15, 15);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(30,'READ', 'WRKGRP.READ', 'Event view', 'VIEW', 15, 15, 15);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(15,10, 4, 15);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(29, 29, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(30, 30, 2, 2);

-- Skill
INSERT INTO feature_code(id, code, description, module, name)VALUES(16, 'SKILL', 'Skill', 'module', 'Skill');
INSERT INTO webform(id, description, name, [type], value) VALUES(16,'Skill', 'skill', 'Dynamic', 'skill');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(16, 'Skill', 'Skill', '2019-02-27', 'assets/images/icon-skill-codes-light.svg', 1, 'Skill', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(31,'FULL', 'SKILL.FULL', 'Skill manage', 'MANAGE', 16, 16,16);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(32,'READ', 'SKILL.READ', 'Skill view', 'VIEW', 16, 16,16);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(16,11, 4, 16);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(31, 31, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(32, 32, 2, 2);



-- Leave Of Absence
INSERT INTO menu(id, description, icon_url, is_active, name, parent_id)VALUES(5, 'Leave Of Absence', 'assets/images/LeaveofAbsence.svg', 1, 'Leave Of Absence', 3);
-- Employee Leave Requests
INSERT INTO feature_code(id, code, description, module, name)VALUES(17, 'EMPLEAVEREQUEST', 'Employee Leave Requests', 'module', 'Employee Leave Requests');
INSERT INTO webform(id, description, name, [type], value) VALUES(17,'Employee Leave Requests', 'Employee Leave Requests', 'Dynamic', 'EMPLEAVEREQUEST');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(17, 'Employee Leave Requests', 'Employee Leave Requests', '2019-02-27', 'assets/images/icons-location-codes-light.svg', 1, 'Employee Leave Requests', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(33,'FULL', 'EMPLEAVEREQUEST.FULL', 'Employee Leave Requests manage', 'MANAGE', 17,17,17);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(34,'READ', 'EMPLEAVEREQUEST.READ', 'Employee Leave Requests view', 'VIEW', 17, 17, 17);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(17,1, 5, 17);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(33, 33, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(34, 34, 2, 2);

-- Leave of Absence Setup
INSERT INTO feature_code(id, code, description, module, name)VALUES(18, 'LEAVEABSENCESETUP', 'Leave of Absence Setup', 'module', 'Leave of Absence Setup');
INSERT INTO webform(id, description, name, [type], value) VALUES(18,'Leave of Absence Setup', 'Leave of Absence Setup', 'Dynamic', 'LEAVEABSENCESETUP');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(18, 'Leave of Absence Setup', 'Leave of Absence Setup', '2019-02-27', 'assets/images/icons-location-codes-light.svg', 1, 'Leave of Absence Setup', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(35,'FULL', 'LEAVEABSENCESETUP.FULL', 'Leave of Absence Setup manage', 'MANAGE', 18,18,18);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(36,'READ', 'LEAVEABSENCESETUP.READ', 'Leave of Absence Setup view', 'VIEW', 18, 18, 18);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(18,2, 5, 18);


INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(35, 35, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(36, 36, 2, 2);

--Archived Requests
INSERT INTO feature_code(id, code, description, module, name)VALUES(19, 'ARCHIVEDREQ', 'Archived Requests', 'module', 'Archived Requests');
INSERT INTO webform(id, description, name, [type], value) VALUES(19,'Archived Requests', 'Archived Requests', 'Dynamic', 'ARCHIVEDREQ');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(19, 'Archived Requests', 'Archived Requests', '2019-02-27', 'assets/images/icons-location-codes-light.svg', 1, 'Archived Requests', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(37,'FULL', 'ARCHIVEDREQ.FULL', 'Archived Requests manage', 'MANAGE', 19,19,19);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(38,'READ', 'ARCHIVEDREQ.READ', 'Archived Requests view', 'VIEW', 19, 19, 19);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(19,3, 5, 19);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(37, 38, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(38, 38, 2, 2);




-- Announcements

INSERT INTO feature_code(id, code, description, module, name)VALUES(20, 'ANNOUNCEMENTS', 'Announcements', 'module', 'Announcements');
INSERT INTO webform(id, description, name, [type], value) VALUES(20,'Announcements', 'Announcements', 'Dynamic', 'Announcements');

INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(20, 'Announcements', 'Announcements', '2020-06-27', 'assets/images/Announcements.svg', 1, 'Announcements', '2019-06-04');

INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(39,'FULL', 'ANNOUNCEMENTS.FULL', 'Announcements', 'MANAGE', 20, 20, 20);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(40,'READ', 'ANNOUNCEMENTS.READ', 'Announcements', 'VIEW', 20, 20, 20);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(20,2, 3, 20);

INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(39, 39, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(40, 40, 2, 2);


-- Employee
INSERT INTO menu(id, description, icon_url, is_active, name, parent_id)VALUES(6, 'Employee', '', 1, 'Employee', 0);
-- Employee Maintenance
INSERT INTO feature_code(id, code, description, module, name)VALUES(21, 'EMPMANAGEMENT', 'Employee Maintenance', 'module', 'Employee Maintenance');
INSERT INTO webform(id, description, name, [type], value) VALUES(21,'Employee Maintenance', 'Employee Maintenance', 'Dynamic', 'Employeemaintenance');

INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(21, 'Employee Maintenance', 'Employee Maintenance', '2019-02-27', 'assets/images/employeeMaintenance.svg', 1, 'Employee Maintenance', '2019-02-27');

INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(41,'FULL', 'EMPMANAGEMENT.FULL', 'Employee Maintenance', 'MANAGE', 21, 21, 21);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(42,'READ', 'EMPMANAGEMENT.READ', 'Employee Maintenance', 'VIEW', 21, 21, 21);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(21,1, 6, 21);

INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(41, 41, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(42, 42, 2, 2);


-- New Hire Onboarding
INSERT INTO feature_code(id, code, description, module, name)VALUES(22, 'NEWHIRE', 'New Hire Onboarding', 'module', 'New Hire Onboarding');
INSERT INTO webform(id, description, name, [type], value) VALUES(22,'New Hire Onboarding', 'New Hire Onboarding', 'Dynamic', 'newhireonboarding');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(22, 'New Hire Onboarding', 'New Hire Onboarding', '2019-02-27', 'assets/images/newhire.svg', 1, 'New Hire Onboarding', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(43,'FULL', 'NEWHIRE.FULL', 'New Hire manage', 'MANAGE', 22, 22, 22);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(44,'READ', 'NEWHIRE.READ', 'New Hire view', 'VIEW', 22, 22,22);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(22,2, 6, 22);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(43, 43, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(44, 44, 2, 2);

-- Employee Loan
INSERT INTO feature_code(id, code, description, module, name)VALUES(23, 'EMPLOAN', 'Employee Loan', 'module', 'Employee Loan');
INSERT INTO webform(id, description, name, [type], value) VALUES(23,'Employee Loan', 'Employee loan', 'Dynamic', 'employeeloan');

INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(23, 'Employee Loan', 'Employee Loan', '2019-02-27', 'assets/images/employeeloan.svg', 1, 'Employee Loan', '2019-02-27');

INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(45,'FULL', 'EMPLOAN.FULL', 'Employee Loan manage', 'MANAGE', 23, 23, 23);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(46,'READ', 'EMPLOAN.READ', 'Employee Loan view', 'VIEW', 23, 23, 23);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(23,3, 6, 23);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(45, 45, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(46, 46, 2, 2);


-- Bulk Upload (Employee Document Notification)
INSERT INTO feature_code(id, code, description, module, name)VALUES(24, 'BULKUPLOAD', 'Bulk Upload', 'module', 'Bulk Upload');
INSERT INTO webform(id, description, name, [type], value) VALUES(24,'Bulk Upload', 'Bulk Upload', 'Dynamic', 'bulkupload');

INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(24, 'Bulk Upload', 'Bulk Upload', '2019-02-27', 'assets/images/bulkupload.svg', 1, 'Bulk Upload', '2019-02-27');

INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(47,'FULL', 'BULKUPLOAD.FULL', 'Bulk Upload manage', 'MANAGE', 24, 24, 24);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(48,'READ', 'BULKUPLOAD.READ', 'Bulk Upload view', 'VIEW', 24, 24, 24);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(24,4, 6, 24);

INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(47, 47, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(48, 48, 2, 2);

--Payroll
INSERT INTO menu(id, description, icon_url, is_active, name, parent_id)VALUES(7, 'Payroll', '', 1, 'Payroll', 0);

--Timesheet
INSERT INTO feature_code(id, code, description, module, name)VALUES(25, 'TIMESHEET', 'Timesheet', 'module', 'Timesheet');
INSERT INTO webform(id, description, name, [type], value) VALUES(25,'Timesheet', 'Timesheet', 'Dynamic', 'timesheet');

INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(25, 'Timesheet', 'Timesheet', '2019-02-27', 'assets/images/timesheet.svg', 1, 'Timesheet', '2019-02-27');

INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(49,'FULL', 'TIMESHEETS.FULL', 'Timesheet', 'MANAGE', 25, 25, 25);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(50,'READ', 'TIMESHEETS.READ', 'Timesheet', 'VIEW', 25, 25, 25);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(25,1, 7, 25);

INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(49, 49, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(50, 50, 2, 2);


--Reporting
INSERT INTO menu(id, description, icon_url, is_active, name, parent_id)VALUES(8, 'Reporting', '', 1, 'Reporting', 0);

--Reports
INSERT INTO feature_code(id, code, description, module, name)VALUES(26, 'REPORTS', 'Reports', 'module', 'Reports');
INSERT INTO webform(id, description, name, [type], value) VALUES(26,'Reports', 'Reports', 'Dynamic', 'reports');

INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(26, 'Reports', 'Reports', '2019-02-27', 'assets/images/reports.svg', 1, 'Reports', '2019-02-27');

INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(51,'FULL', 'REPORTS.FULL', 'Reports', 'MANAGE', 26, 26, 26);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(52,'READ', 'REPORTS.READ', 'Reports', 'VIEW', 26, 26, 26);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(26,1, 8, 26);

INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(51, 51, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(52, 52, 2, 2);


--Reports Library
INSERT INTO feature_code(id, code, description, module, name)VALUES(27, 'REPORTSLIB', 'Reports Library', 'module', 'Reports Library');
INSERT INTO webform(id, description, name, [type], value) VALUES(27,'Reports Library', 'Reports', 'Dynamic', 'reportslibrary');

INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(27, 'Reports Library', 'Reports Library', '2019-02-27', 'assets/images/reportslibrary.svg', 1, 'Reports Library', '2019-02-27');

INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(53,'FULL', 'REPORTSLIB.FULL', 'Reports Library', 'MANAGE', 27, 27, 27);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(54,'READ', 'REPORTSLIB.READ', 'Reports Library', 'VIEW', 27, 27, 27);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(27,2, 8, 27);

INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(53, 53, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(54, 54, 2, 2);


-- User Access
INSERT INTO menu(id, description, icon_url, is_active, name, parent_id)VALUES(2, 'User Access', 'assets/images/UserAccess.svg', 1, 'User Access', 1);

--Access Group
INSERT INTO feature_code(id, code, description, module, name)VALUES(2, 'ACCESSGROUP', 'Access Group', 'module', 'Access Group');
INSERT INTO webform(id, description, name, [type], value) VALUES(2,'Access Group', 'Access Group', 'Dynamic', 'accessgroup');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(2, 'Access Group', 'Access Group', '2019-02-27', 'assets/images/icon-privileges-light.svg', 1, 'Access Group', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(3,'FULL', 'ACCESSGROUP.FULL', 'Access Group manage', 'MANAGE', 2, 2,2);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(4,'READ', 'ACCESSGROUP.READ', 'Access Group view', 'VIEW', 2, 2,2);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(2,1, 2, 2);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(3, 3, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(4, 4, 2, 2);


--Roles
INSERT INTO feature_code(id, code, description, module, name)VALUES(3, 'ROLES', 'Roles', 'module', 'Roles');
INSERT INTO webform(id, description, name, [type], value) VALUES(3,'Roles', 'Roles', 'Dynamic', 'Roles');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(3, 'Roles', 'Roles', '2019-02-27', 'assets/images/icon-roles-light.svg', 1, 'Roles', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(5,'FULL', 'ROLES.FULL', 'Roles manage', 'MANAGE', 3, 3,3);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(6,'READ', 'ROLES.READ', 'Roles view', 'VIEW', 3, 3,3);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(3,2, 2,3);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(5, 5, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(6, 6, 2, 2);


-- Client
INSERT INTO feature_code(id, code, description, module, name)VALUES(4, 'CLIENT', 'Client', 'module', 'Client');
INSERT INTO webform(id, description, name, [type], value) VALUES(4,'Client', 'Client', 'Dynamic', 'client');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(4, 'Client', 'Client', '2019-02-27', 'assets/images/icon-roles-users-light.svg', 1, 'Client', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(7,'FULL', 'CLIENT.FULL', 'Client manage', 'MANAGE', 4, 4,4);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(8,'READ', 'CLIENT.READ', 'Client view', 'VIEW', 4, 4,4);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(4,3, 2, 4);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(7,7 , 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(8,8, 2, 2);


-- Users
INSERT INTO feature_code(id, code, description, module, name)VALUES(28, 'USERS', 'Users', 'module', 'Users');
INSERT INTO webform(id, description, name, [type], value) VALUES(28,'Users', 'Users', 'Dynamic', 'users');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(28, 'Users', 'Users', '2019-02-27', 'assets/images/icon-roles-users-light.svg', 1, 'Users', '2019-02-27');
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(55,'FULL', 'USERS.FULL', 'User manage', 'MANAGE', 28, 28,28);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(56,'READ', 'USERS.READ', 'User view', 'VIEW', 28, 28,28);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(28,4, 2, 28);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(55,55 , 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(56,56, 2, 2);
--Payroll
--Timesheet
INSERT INTO feature_code(id, code, description, module, name)VALUES(29, 'TIMESHEETUPLOAD', 'Timesheet Upload', 'module', 'Timesheet Upload');
INSERT INTO webform(id, description, name, [type], value) VALUES(29,'Timesheet Upload', 'Timesheet Upload', 'Dynamic', 'TimesheetUpload');

INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(29, 'Timesheet Upload', 'Timesheet Upload', '2019-02-27', 'assets/images/timesheet.svg', 1, 'Timesheet Upload', '2019-02-27');

INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(57,'FULL', 'TIMESHEETUPLOAD.FULL', 'Timesheet Upload', 'MANAGE', 29, 29, 29);
INSERT INTO privilege(id, [action], code, description, [type], feature_code_id, webform_id, menu_item_id)VALUES(58,'READ', 'TIMESHEETUPLOAD.READ', 'Timesheet Upload', 'VIEW', 29, 29, 29);
INSERT INTO menu_map(id,[sequence], menu_id, menu_item_id)VALUES(29,2, 7, 29);

INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(57, 57, 1, 1);
INSERT INTO role_privileges(id, privilege_id, role_id, privilege_group_id)VALUES(58, 58, 2, 2);


-----Notification Management -----


INSERT INTO menu(id, description, icon_url, is_active, name, parent_id)VALUES(9, 'Notification Management', 'assets/images/icon-notification-light.svg', 1, 'Notification Management', 1);

INSERT INTO feature_code(id, code, description, module, name)VALUES(30, 'NOTIFY.TEMPLATE', 'Notification Templates', 'module','Notification Templates');
INSERT INTO feature_code(id, code, description, module, name)VALUES(31, 'NOTIFY.RULE', 'Notification Rule', 'module', 'Notification Rule');
INSERT INTO webform(id, description, name, [type], value)VALUES(30, 'Notification Templates', 'Notification Templates', 'Dynamic', 'Notification Templates');
INSERT INTO webform(id, description, name, [type], value)VALUES(31, 'Notification Rule', 'Notification Rule', 'Dynamic', 'Notification Rule');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(30, 'Notification Templates', 'Notification Templates', '2020-06-27 00:00:00.000', 'assets/images/icon-notification-light.svg', 1, 'Notification Templates', '2019-06-20 00:00:00.000');
INSERT INTO menu_item(id, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(31, 'Notification Rule', 'Notification Rule', '2020-06-27 00:00:00.000', 'assets/images/icon-notification-light.svg', 1, 'Notification Rule', '2019-06-20 00:00:00.000');
INSERT INTO privilege(id, [action], code, description, feature_code_id, [type], webform_id, menu_item_id)VALUES(59, 'FULL', 'NOTIFITEMPLATE.FULL', 'Notifications Template Manage', 30, 'MANAGE', 30, 30);
INSERT INTO privilege(id, [action], code, description, feature_code_id, [type], webform_id, menu_item_id)VALUES(60, 'READ', 'NOTIFITEMPLATE.READ', 'Notifications Template View', 30, 'VIEW', 30, 30);
INSERT INTO privilege(id, [action], code, description, feature_code_id, [type], webform_id, menu_item_id)VALUES(61, 'FULL', 'NOTIFIRULE.FULL', 'Notifications Rule Manage', 31, 'MANAGE', 31, 31);
INSERT INTO privilege(id, [action], code, description, feature_code_id, [type], webform_id, menu_item_id)VALUES(62, 'READ', 'NOTIFIRULE.READ', 'Notifications Rule View', 31, 'VIEW', 31, 31);
INSERT INTO menu_map(id, [sequence], menu_id, menu_item_id)VALUES(30, 1, 9, 30);
INSERT INTO menu_map(id, [sequence], menu_id, menu_item_id)VALUES(31, 2, 9, 31);
INSERT INTO role_privileges(id, privilege_id, privilege_group_id, role_id)VALUES(59, 59, 2, 1);
INSERT INTO role_privileges(id, privilege_id, privilege_group_id, role_id)VALUES(60, 60, 1, 2);
INSERT INTO role_privileges(id, privilege_id, privilege_group_id, role_id)VALUES(61, 61, 2, 1);
INSERT INTO role_privileges(id, privilege_id, privilege_group_id, role_id)VALUES(62, 62, 1, 2);

