INSERT INTO menu_v1(id, created_by, created_on, modified_by, modified_on, version, description, icon_url, is_active, name)VALUES(1, 'admin', '2019-07-10','admin','2019-07-10',0, 'Administration', 'url', 1, 'Administration');
INSERT INTO menu_item_v1(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(1, 'admin', '2019-07-10','admin','2019-07-10',0,'Company Information', 'Company Information', '9999-12-31', 'url', 1, 'Company Information', '2001-01-01');
INSERT INTO menu_map_v1(id,[sequence], menu_id, menu_item_id)VALUES(1, 1, 1, 1);

INSERT INTO webform_v1(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, menu_item_id)VALUES(1,'admin', '2019-07-10','admin','2019-07-10',0, 'Company Information', 'Company Information', 'CLI.INF', '/infoclient', 1);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(1,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF', 'Company Information', '1', 'Company Information', 0, '1', 1, 1);
--INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(2,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF', 'Company Information', '2', 'Company Information', 1, '2', 1, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(3,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.COMP', 'Company', '3', 'Company', 1, '3', 1, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(4,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.CONT', 'Contact Information', '3', 'Contact Information', 1, '3', 2, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(5,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.BANK', 'Primary Bank Information', '3', 'Primary Bank Information', 1, '3', 3, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(6,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.TAX', 'Tax Information', '3', 'Tax Information', 1, '3', 4, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(7,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.WORK', 'Workers Compensation', '3', 'Workers Compensation', 1, '3', 5, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(8, 'admin', '2019-07-10','admin','2019-07-10',0,'CLI.INF.PAYR', 'Payroll Preferences', '3', 'Payroll Preferences', 1, '3', 6, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(9,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.EEO', 'EEO Preferences', '3', 'EEO Preferences', 1, '3', 7, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(10, 'admin', '2019-07-10','admin','2019-07-10',0,'CLI.INF.CAX', 'CA XML', '3', 'CA XML', 1, '3', 8, 0);

INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(1,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF', 'Company Information', 'ALL', 1);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(2, 'admin', '2019-07-10','admin','2019-07-10',0,'CLI.INF', 'Company Information', 'VIEW', 1);
--INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(3, 'admin', '2019-07-10','admin','2019-07-10',0,'CLI.INF', 'Company Information', 'ALL', 2);
--INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(4,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF', 'Company Information', 'VIEW', 2);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(5, 'admin', '2019-07-10','admin','2019-07-10',0,'CLI.INF.COMP', 'Company', 'ALL', 3);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(6,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.COMP', 'Company', 'VIEW',3);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(7,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.CONT', 'Contact Information', 'ALL', 4);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(8,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.CONT', 'Contact Information', 'VIEW', 4);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(9,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.BANK', 'Primary Bank Information', 'ALL', 5);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(10,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.BANK', 'Primary Bank Information', 'VIEW', 5);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(11,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.TAX', 'Tax Information', 'ALL', 6);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(12,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.TAX', 'Tax Information', 'VIEW', 6);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(13,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.WORK', 'Workers Compensation', 'ALL', 7);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(14,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.WORK', 'Workers Compensation', 'VIEW', 7);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(15,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.PAYR', 'Payroll Preferences', 'ALL', 8);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(16,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.PAYR', 'Payroll Preferences', 'VIEW',8);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(17,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.EEO', 'EEO Preferences', 'ALL', 9);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(18,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.EEO', 'EEO Preferences', 'VIEW', 9);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(19,'admin', '2019-07-10','admin','2019-07-10',0, 'CLI.INF.CAX', 'CA XML', 'ALL', 10);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(20, 'admin', '2019-07-10','admin','2019-07-10',0,'CLI.INF.CAX', 'CA XML', 'VIEW', 10);

INSERT INTO menu_v1(id, created_by, created_on, modified_by, modified_on, version, description, icon_url, is_active, name)VALUES(2, 'admin', '2019-07-10','admin','2019-07-10',0,'Company', 'url', 1, 'Company');
INSERT INTO menu_item_v1(id, created_by, created_on, modified_by, modified_on, version, description, display_name, end_date, icon_url, is_active, name, start_date)VALUES(2,'admin', '2019-07-10','admin','2019-07-10',0, 'Company Management', 'Company Management', '9999-12-31', 'url', 1, 'Company Management', '2001-01-01');
INSERT INTO menu_map_v1(id, [sequence], menu_id, menu_item_id)VALUES(2, 1, 2, 2);
INSERT INTO webform_v1(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, menu_item_id)VALUES(2,'admin', '2019-07-10','admin','2019-07-10',0, 'Company Management', 'Company Management', 'CMGT', '/compmgmt', 1);
--INSERT INTO webform_v1(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, menu_item_id)VALUES(2,'admin', '2019-07-10','admin','2019-07-10',0, 'Workers Comp Class', 'Workers Comp Class', 'CMGT.WCC', '/compmgmt', 1);
--INSERT INTO webform_v1(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, menu_item_id)VALUES(3,'admin', '2019-07-10','admin','2019-07-10',0, 'Worksite Location', 'Worksite Location', 'CMGT.WLOC', '/compmgmt', 1);
--INSERT INTO webform_v1(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, menu_item_id)VALUES(4,'admin', '2019-07-10','admin','2019-07-10',0, 'Department', 'Department', 'CMGT.DEPT', '/compmgmt', 1);
--INSERT INTO webform_v1(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, menu_item_id)VALUES(5,'admin', '2019-07-10','admin','2019-07-10',0, 'Project', 'Project', 'CMGT.PROJ', '/compmgmt', 1);
--INSERT INTO webform_v1(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, menu_item_id)VALUES(6,'admin', '2019-07-10','admin','2019-07-10',0, 'Occupation', 'Occupation', 'CMGT.OCC', '/compmgmt', 1);
--INSERT INTO webform_v1(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, menu_item_id)VALUES(7, 'admin', '2019-07-10','admin','2019-07-10',0,'Division', 'Division', 'CMGT.DIV', '/compmgmt', 1);
--INSERT INTO webform_v1(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, menu_item_id)VALUES(8, 'admin', '2019-07-10','admin','2019-07-10',0,'Union', 'Union', 'CMGT.UNI', '/compmgmt', 1);
--INSERT INTO webform_v1(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, menu_item_id)VALUES(9, 'admin', '2019-07-10','admin','2019-07-10',0,'Event', 'Event', 'CMGT.EVNT', '/compmgmt', 1);
--INSERT INTO webform_v1(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, menu_item_id)VALUES(10,'admin', '2019-07-10','admin','2019-07-10',0, 'Work Group', 'Work Group', 'CMGT.WGRP', '/compmgmt', 1);
--INSERT INTO webform_v1(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, menu_item_id)VALUES(11,'admin', '2019-07-10','admin','2019-07-10',0, 'Skill', 'Skill', 'CMGT.SKL', '/compmgmt', 1);
--INSERT INTO webform_v1(id, created_by, created_on, modified_by, modified_on, version, description, name, [type], value, menu_item_id)VALUES(12,'admin', '2019-07-10','admin','2019-07-10',0, 'Shift', 'Shift', 'CMGT.SKL', '/compmgmt', 1);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(11,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT', 'Company Management', '1', 'Company Management', 0, '1', 1, 2);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(12,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.WCC', 'Workers Comp Class', '2', 'Workers Comp Class', 11, '2', 1, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(13,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.WLOC', 'Worksite Location', '2', 'Worksite Location', 11, '2', 2, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(14,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.DEPT', 'Department', '2', 'Department', 11, '2', 3, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(15,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.PROJ', 'Project', '2', 'Project', 11, '2', 4, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(16,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.OCC', 'Occupation', '2', 'Occupation', 11, '2', 5, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(17,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.DIV', 'Division', '2', 'Division', 11, '2', 6, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(18,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.UNI', 'Union', '2', 'Union', 11, '2', 7, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(19,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.EVNT', 'Event', '2', 'Event', 11, '2', 9, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(20,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.WGRP', 'Work Group', '2', 'Work Group', 11, '2', 10, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(21,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.SKL', 'Skill', '2', 'Skill', 11, '2', 11, 0);
INSERT INTO feature_code_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [level], name, parent_id, [type], seq_num, web_form_id)VALUES(22,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.SKL', 'Shift', '2', 'Shift', 11, '2', 8, 0);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(21, 'admin', '2019-07-10','admin','2019-07-10',0,'CMGT', 'Company Management', 'ALL', 11);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(22, 'admin', '2019-07-10','admin','2019-07-10',0,'CMGT', 'Company Management', 'VIEW', 11);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(23, 'admin', '2019-07-10','admin','2019-07-10',0,'CMGT.WCC', 'Workers Comp Class', 'ALL', 12);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(24,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.WCC', 'Workers Comp Class', 'VIEW', 12);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(25,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.WLOC', 'Worksite Location', 'ALL', 13);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(26,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.WLOC', 'Worksite Location', 'VIEW', 13);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(27,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.DEPT', 'Department', 'ALL', 14);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(28,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.DEPT', 'Department', 'VIEW', 14);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(29,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.PROJ', 'Project', 'ALL', 15);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(30,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.PROJ', 'Project', 'VIEW', 15);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(31,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.OCC', 'Occupation', 'ALL', 16);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(32,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.OCC', 'Occupation', 'VIEW', 16);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(33,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.DIV', 'Division', 'ALL', 17);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(34,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.DIV', 'Division', 'VIEW', 17);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(35,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.UNI', 'Union', 'ALL', 18);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(36,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.UNI', 'Union', 'VIEW', 18);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(37,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.EVNT', 'Event', 'ALL', 19);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(38,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.EVNT', 'Event', 'VIEW', 19);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(39,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.WGRP', 'Work Group', 'ALL', 20);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(40,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.WGRP', 'Work Group', 'VIEW', 20);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(41,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.SKL', 'Skill', 'ALL', 21);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(42,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.SKL', 'Skill', 'VIEW', 21);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(43,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.SHFT', 'Shift', 'ALL', 22);
INSERT INTO privilege_v1(id, created_by, created_on, modified_by, modified_on, version, code, description, [type], feature_code_id)VALUES(44,'admin', '2019-07-10','admin','2019-07-10',0, 'CMGT.SHFT', 'Shift', 'VIEW', 22);

