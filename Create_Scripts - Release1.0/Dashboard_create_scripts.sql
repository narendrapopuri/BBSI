create table client_dashboard (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
dashboard_id bigint,
primary key (id));
 
create table client_widget (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
is_visible bit,
seq_num int,
widget_col int,
widget_id bigint,
client_dashboard_id bigint,
primary key (id));

create table dashboard (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
description varchar(255),
name varchar(255),
is_active bit,
type varchar(255),
primary key (id));

create table dashboard_widget (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
seq_num int,
widget_col int,
primary key (id));

create table emp_dashboard (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
employee_code varchar(255),
primary key (id));

create table emp_widget (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
client_widget_id bigint,
is_visible bit,
seq_num int,
source_id bigint,
source_type varchar(255),
widget_col int,
primary key (id));

create table notification_widget (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
end_datetime datetime2 not null,
is_activate bit,
message varchar(255),
message_title varchar(255),
start_datetime datetime2 not null,
primary key (id));

create table parameters (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
name varchar(255),
type varchar(255),
value varchar(255),
widget_id bigint,
primary key (id));
 
create table widget (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
action_uri varchar(255),
client_feature_code varchar(255),
employee_feature_code varchar(255),
is_branch_enabled bit,
is_client_enabled bit,
is_employee_enabled bit,
is_hq_enabled bit,
name varchar(255),
preferences varchar(2048),
service_uri varchar(255),
is_active bit,
type varchar(255),
primary key (id));
 
create table widget_setting (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
enable_settings varchar(255),
is_auto_refreshed bit,
is_closable bit,
is_maximizable bit,
is_minimizable bit,
is_movable bit,
is_resizable bit,
refresh_interval_sec bigint,
source_id varchar(255),
source_type varchar(255),
widget_id bigint,
primary key (id));
 
create sequence client_dashboard_id_seq start with 1 increment by 1;

create sequence client_widget_id_seq start with 150 increment by 1;

create sequence dashboard_id_seq start with 1 increment by 1;

create sequence dashboard_widget_id_seq start with 1 increment by 1;

create sequence emp_dashboard_id_seq start with 1 increment by 1;

create sequence emp_widget_id_seq start with 1 increment by 1;

create sequence notif_widget_id_seq start with 1 increment by 1;

create sequence parameters_id_seq start with 1 increment by 1;

create sequence widget_id_seq start with 1 increment by 1;

create sequence widget_setting_id_seq start with 1 increment by 1;

alter table client_dashboard add constraint FKdp8dd6dlwt7ypkxg493lr1ho6 foreign key (dashboard_id) references dashboard;

alter table client_widget add constraint FK9p54vkm0qsp8hnlf5okfv4alg foreign key (widget_id) references widget;

alter table client_widget add constraint FKmw9ciqfywepdtnsqy7yw1y3a9 foreign key (client_dashboard_id) references client_dashboard;

alter table parameters add constraint FK58ods3u6d5xu7d5einei5jpht foreign key (widget_id) references widget;

alter table widget_setting add constraint FK5ddxxq9377sv8gnmk19r2f17l foreign key (widget_id) references widget;


-- widget
INSERT INTO widget (id, created_by, created_on, modified_by, modified_on, version, action_uri, is_branch_enabled, is_client_enabled, is_employee_enabled, is_hq_enabled, name, preferences, service_uri, is_active, [type], client_feature_code, employee_feature_code) VALUES(1, 'Admin', '2019-11-25 05:31:10.390', NULL, '2019-11-25 05:31:10.390', 0, NULL, NULL, 0, 1, NULL, 'Pay Stub', '{"primaryColor":"#D3D3D3","fontSize":"","secondaryColor":"#FFFFFF","fontFamily":"","header":{"type":"","label1":"","action1":""},"footer":{"type":"link","label1":"All Pay Stubs","action1":"/home/employeemanagement"},"data":[{"uiInput":"date","format":"MM/dd/yyyy","bEKey":"pay_date","action":"/home/employeemanagement/employee/","header":"Pay Date"},{"uiInput":"pay_stub","bEKey":"ID","header":"Pay Stub Numbers"}]}', '{integrationPort}/integration/v1/employee/client/XXXXXX/employee/XXXXXX/type/PAYSTUB_UNIQUERY?id={year}', 1, 'tableWidget', NULL, 'EMP_PAYMENT_HISTORY_ACD');
INSERT INTO widget (id, created_by, created_on, modified_by, modified_on, version, action_uri, is_branch_enabled, is_client_enabled, is_employee_enabled, is_hq_enabled, name, preferences, service_uri, is_active, [type], client_feature_code, employee_feature_code) VALUES(2, 'Admin', '2019-11-25 05:31:10.390', NULL, '2019-11-25 05:31:10.390', 0, NULL, NULL, 1, 1, NULL, 'Announcement/Notification', '{"dynamicClass":"notification-list","imageFlag":false,"primaryColor":"#D3D3D3","notificationserviceUrl":"assets/mock-json/announcement_notification_widget.json","announcementserviceUrl":"{notificationAnnouncementPort}/notification/v1/announcements/client/{replace_client_code}/employee/{replace_employee_code}?page=0&size=5","fontSize":"","secondaryColor":"#FFFFFF","fontFamily":"","header":{"type":"tab","label1":"Announcements","action1":"","label2":"Notifications","action2":""},"footer":{"type":"modal","label1":"Expired Announcements","action1":"","label2":"All Notifications","action2":""},"data":[{"uiInput":"img","bEKey":"icon"},{"uiInput":"content","bEKey":"title"},{"uiInput":"status","bEKey":"is_checked"},{"uiInput":"time","bEKey":"announcementTime"}]}', '', 1, 'announceNotification', 'NOTF_MGMT_WIDGET', 'EMP_MESSAGE_CENTER');
INSERT INTO widget (id, created_by, created_on, modified_by, modified_on, version, action_uri, is_branch_enabled, is_client_enabled, is_employee_enabled, is_hq_enabled, name, preferences, service_uri, is_active, [type], client_feature_code, employee_feature_code) VALUES(3, 'Admin', '2019-11-25 05:31:10.390', NULL, '2019-11-25 05:31:10.390', 0, NULL, NULL, 1, NULL, NULL, 'Payroll', '{"primaryColor":"#D3D3D3","fontSize":"","secondaryColor":"#FFFFFF","fontFamily":"","tableHeading":"Select a payroll to process","header":{"type":"","label1":"","action1":""},"footer":{"type":"","label1":"","action1":""},"data":[{"uiInput":"payrollID","bEKey":"batch_number","action":"/home/payroll/manual-timesheet","header":"Payroll ID"},{"uiInput":"description","bEKey":"description","header":"Description"},{"uiInput":"date","format":"MM/dd/yyyy - h:mm aa","bEKey":"call_in_date","header":"Payroll Due"}]}', '{empDataPort}/employee/v1/timecard/batches/widget/{replace_client_code}?page={replace_page_no}&size={replace_page_size}', 1, 'tableWidget', 'PAY_TS_INF', NULL);
INSERT INTO widget (id, created_by, created_on, modified_by, modified_on, version, action_uri, is_branch_enabled, is_client_enabled, is_employee_enabled, is_hq_enabled, name, preferences, service_uri, is_active, [type], client_feature_code, employee_feature_code) VALUES(4, 'Admin', '2019-11-25 05:31:10.390', NULL, '2019-11-25 05:31:10.390', 0, NULL, NULL, 1, NULL, NULL, 'Employees', '{"dynamicClass":"employee-list","imageFlag":true,"primaryColor":"#D3D3D3","fontSize":"","secondaryColor":"#FFFFFF","fontFamily":"","header":{"type":"search","label1":"Search","action1":"{empDataPort}/employee/v1/employee/widget/search/{this.searchText}/client/{replace_client_code}"},"footer":{"type":"link","label1":"All Employees","action1":"/home/employeemanagement"},"data":[{"uiInput":"img","bEKey":"imageUrl"},{"uiInput":"content","bEKey":"first_lastName"},{"uiInput":"designation","bEKey":"position"}]}', '{integrationPort}/integration/v1/client/{replace_client_code}/filter/type/CLIENT_EMPLOYEES_FILTER_UQ/user/{replace_user_email}?option=BASIC', 1, 'listingWidget', 'EMP_MGMT', NULL);
INSERT INTO widget (id, created_by, created_on, modified_by, modified_on, version, action_uri, is_branch_enabled, is_client_enabled, is_employee_enabled, is_hq_enabled, name, preferences, service_uri, is_active, [type], client_feature_code, employee_feature_code) VALUES(5, 'Admin', '2019-11-25 05:31:10.390', NULL, '2019-11-25 05:31:10.390', 0, NULL, NULL, 1, NULL, NULL, 'Onboarding', '{"dynamicClass":"employee-list","imageFlag":true,"primaryColor":"#D3D3D3","fontSize":"","secondaryColor":"#FFFFFF","fontFamily":"","header":{"type":"button","label1":"New Hire","action1":"/home/new-hire"},"footer":{"type":"link","label1":"All Employees","action1":"/home/client/new-hire"},"data":[{"uiInput":"img","bEKey":"imageUrl"},{"uiInput":"content","bEKey":"first_lastName"},{"uiInput":"designation","bEKey":"newHireStatus"}]}', '{empNewHirePort}/newhire/v1/employee/client/{replace_client_code}', 1, 'listingWidget', 'NEW_HIRE_EMP_ONBOARDING', NULL);
INSERT INTO widget (id, created_by, created_on, modified_by, modified_on, version, action_uri, is_branch_enabled, is_client_enabled, is_employee_enabled, is_hq_enabled, name, preferences, service_uri, is_active, [type], client_feature_code, employee_feature_code) VALUES(6, 'Admin', '2019-11-25 05:31:10.390', NULL, '2019-11-25 05:31:10.390', 0, NULL, NULL, 1, NULL, NULL, 'Downloads', '{"primaryColor":"#D3D3D3","fontSize":"","secondaryColor":"#FFFFFF","fontFamily":"","header":{"type":"","label1":"","action1":""},"footer":{"type":"","label1":"","action2":"/client/v1/client/download/"},"data":[]}', '{clientDataPort}/client/v1/client/docs/client/{replace_client_code}', 0, 'downloads', 'NEW_HIRE', NULL);
INSERT INTO widget (id, created_by, created_on, modified_by, modified_on, version, action_uri, is_branch_enabled, is_client_enabled, is_employee_enabled, is_hq_enabled, name, preferences, service_uri, is_active, [type], client_feature_code, employee_feature_code) VALUES(7, 'Admin', '2019-11-25 05:31:10.390', NULL, '2019-11-25 05:31:10.390', 0, NULL, NULL, NULL, 1, NULL, 'Time Off', '{"primaryColor":"#D3D3D3","fontSize":"","secondaryColor":"#FFFFFF","fontFamily":"","header":{"type":"","label1":"","action1":""},"footer":{"type":"link","label1":"View Details","action1":"/home/employeemanagement/employee/"},"data":[]}', '{dashBoardPort}/portal-dashboard/v1/employee/dashboard/client/{replace_client_code}/employee/{replace_employee_code}/type/PAIDTIMEOFF', 1, 'graph', NULL, 'EMP_PAID_TIME_OFF');
INSERT INTO widget (id, created_by, created_on, modified_by, modified_on, version, action_uri, is_branch_enabled, is_client_enabled, is_employee_enabled, is_hq_enabled, name, preferences, service_uri, is_active, [type], client_feature_code, employee_feature_code) VALUES(8, 'Admin', '2019-11-25 05:31:10.390', NULL, '2019-11-25 05:31:10.390', 0, NULL, NULL, NULL, 1, NULL, 'Benefits', '{"primaryColor":"#D3D3D3","fontSize":"","secondaryColor":"#FFFFFF","fontFamily":"","header":{"type":"","label1":"","action1":""},"footer":{"type":"link","label1":"All Benefits","action1":"home/employeemanagement/employee/"},"data":[]}', '{integrationPort}/integration/v1/employee/client/{replace_client_code}/employee/{replace_employee_code}/type/BENEFIT_SUMMARY', 1, 'benefits', NULL, 'EMP_BENEFIT_SUMMARY');
INSERT INTO widget (id, created_by, created_on, modified_by, modified_on, version, action_uri, is_branch_enabled, is_client_enabled, is_employee_enabled, is_hq_enabled, name, preferences, service_uri, is_active, [type], client_feature_code, employee_feature_code) VALUES(9, 'Admin', '2019-11-25 05:31:10.390', NULL, '2019-11-25 05:31:10.390', 0, NULL, NULL, 1, NULL, NULL, 'Banner', '{"isCaurosel":false,"isBackgroundColor":false,"isBackgroundImage":true,"primaryColor":"#D3D3D3","fontSize":"","secondaryColor":"#FFFFFF","fontFamily":""}', 'assets/mock-json/banner.json', 1, 'banner', 'NOTF_MGMT_WIDGET', NULL);
INSERT INTO widget (id, created_by, created_on, modified_by, modified_on, version, action_uri, is_branch_enabled, is_client_enabled, is_employee_enabled, is_hq_enabled, name, preferences, service_uri, is_active, [type], client_feature_code, employee_feature_code) VALUES(10, 'Admin', '2019-11-25 05:31:10.390', NULL, '2019-11-25 05:31:10.390', 0, NULL, NULL, 0, 1, NULL, 'Calendar', '{"dynamicClass":"employee-list","imageFlag":true,"primaryColor":"#D3D3D3","fontSize":"","secondaryColor":"#FFFFFF","fontFamily":"","header":{},"footer":{},"data":[{"uiInput":"img","bEKey":"imageUrl"},{"uiInput":"content","bEKey":"first_lastName"},{"uiInput":"designation","bEKey":"newHireStatus"}]}', '{empDataPort}/employee/v1/paycalendar/employee/{replace_employee_code}/client/{replace_client_code}', 1, 'calendar', NULL, 'EMP_PAYROLL_SUMMARY_ACD');
INSERT INTO widget (id, created_by, created_on, modified_by, modified_on, version, action_uri, is_branch_enabled, is_client_enabled, is_employee_enabled, is_hq_enabled, name, preferences, service_uri, is_active, [type], client_feature_code, employee_feature_code) VALUES(11, 'Admin', '2019-11-25 05:31:10.390', NULL, '2019-11-25 05:31:10.390', 0, NULL, NULL, 1, NULL, NULL, 'Pay Calendar', '{"dynamicClass":"employee-list","imageFlag":true,"primaryColor":"#D3D3D3","fontSize":"","secondaryColor":"#FFFFFF","fontFamily":"","header":{"type":"dropDown"},"footer":{},"data":[{"uiInput":"img","bEKey":"imageUrl"},{"uiInput":"content","bEKey":"first_lastName"},{"uiInput":"designation","bEKey":"newHireStatus"}]}', '{clientDataPort}/client/v1/paycalendar/client/{replace_client_code}', 1, 'calendar', 'CAL_SETUP', NULL);
INSERT INTO widget (id, created_by, created_on, modified_by, modified_on, version, action_uri, is_branch_enabled, is_client_enabled, is_employee_enabled, is_hq_enabled, name, preferences, service_uri, is_active, [type], client_feature_code, employee_feature_code) VALUES(12, 'Admin', '2019-11-25 05:31:10.390', NULL, '2019-11-25 05:31:10.390', 0, NULL, NULL, 0, 1, NULL, 'Compensation', '{"primaryColor":"#D3D3D3","fontSize":"","secondaryColor":"#FFFFFF","fontFamily":"","tableHeading":"","header":{"type":"","label1":"","action1":""},"footer":{"type":"","label1":"","action1":""},"data":[]}', '{refDataPort}/ref-data/v1/integration/payrollsummary/client/{replace_client_code}/employee/{replace_employee_code}/year/{date}', 1, 'compensation', NULL, 'EMP_COMPENSATION_ACD');
INSERT INTO widget (id, created_by, created_on, modified_by, modified_on, version, action_uri, is_branch_enabled, is_client_enabled, is_employee_enabled, is_hq_enabled, name, preferences, service_uri, is_active, [type], client_feature_code, employee_feature_code) VALUES(13, 'Admin', '2019-05-04 00:00:00.000', NULL, '2019-05-04 00:00:00.000', 0, NULL, NULL, 1, 0, NULL, 'Pending Approvals', '{"dynamicClass":"pending-approval-list","primaryColor":"#D3D3D3","fontSize":"","secondaryColor":"#FFFFFF","fontFamily":"","header":{"type":"","label1":"","action1":""},"footer":{"type":"link","label1":"All Approvals","action1":"/home/messagecenter/approvals"},"data":[{"uiInput":"date","bEKey":"start_date"},{"uiInput":"content","bEKey":"message"},{"uiInput":"link","bEKey":"title"}]}', '{notificationAnnouncementPort}/notification/v1/notifications/approval/user/{replace_user_email}/type/{replace_user_type}?completed=false&client_code={replace_client_code}', 1, 'listingWidget', 'MSG_CENTER_APPR', NULL);

ALTER sequence widget_id_seq RESTART WITH 20;

-- widget_setting
INSERT INTO widget_setting (id, created_by, created_on, modified_by, modified_on, version, enable_settings, is_auto_refreshed, is_closable, is_maximizable, is_minimizable, is_movable, is_resizable, refresh_interval_sec, source_id, source_type, widget_id) VALUES(1, 'Admin', '2019-11-25 05:31:27.283', 'Admin', '2019-11-25 05:31:27.283', 0, '1', NULL, 1, 1, 1, 1, 1, 0, NULL, NULL, 3);
INSERT INTO widget_setting (id, created_by, created_on, modified_by, modified_on, version, enable_settings, is_auto_refreshed, is_closable, is_maximizable, is_minimizable, is_movable, is_resizable, refresh_interval_sec, source_id, source_type, widget_id) VALUES(2, 'Admin', '2019-11-25 05:31:27.283', 'Admin', '2019-11-25 05:31:27.283', 0, '1', NULL, 1, 1, 1, 1, 1, 0, NULL, NULL, 4);
INSERT INTO widget_setting (id, created_by, created_on, modified_by, modified_on, version, enable_settings, is_auto_refreshed, is_closable, is_maximizable, is_minimizable, is_movable, is_resizable, refresh_interval_sec, source_id, source_type, widget_id) VALUES(3, 'Admin', '2019-11-25 05:31:27.283', 'Admin', '2019-11-25 05:31:27.283', 0, '1', NULL, 1, 1, 1, 1, 1, 0, NULL, NULL, 5);
INSERT INTO widget_setting (id, created_by, created_on, modified_by, modified_on, version, enable_settings, is_auto_refreshed, is_closable, is_maximizable, is_minimizable, is_movable, is_resizable, refresh_interval_sec, source_id, source_type, widget_id) VALUES(4, 'Admin', '2019-11-25 05:31:27.283', 'Admin', '2019-11-25 05:31:27.283', 0, '1', NULL, 1, 1, 1, 1, 1, 0, NULL, NULL, 6);
INSERT INTO widget_setting (id, created_by, created_on, modified_by, modified_on, version, enable_settings, is_auto_refreshed, is_closable, is_maximizable, is_minimizable, is_movable, is_resizable, refresh_interval_sec, source_id, source_type, widget_id) VALUES(5, 'Admin', '2019-11-25 05:31:27.283', 'Admin', '2019-11-25 05:31:27.283', 0, '1', NULL, 1, 1, 1, 1, 1, 0, NULL, NULL, 8);
INSERT INTO widget_setting (id, created_by, created_on, modified_by, modified_on, version, enable_settings, is_auto_refreshed, is_closable, is_maximizable, is_minimizable, is_movable, is_resizable, refresh_interval_sec, source_id, source_type, widget_id) VALUES(6, 'Admin', '2019-11-25 05:31:27.283', 'Admin', '2019-11-25 05:31:27.283', 0, '1', NULL, 1, 1, 1, 1, 1, 0, NULL, NULL, 9);
INSERT INTO widget_setting (id, created_by, created_on, modified_by, modified_on, version, enable_settings, is_auto_refreshed, is_closable, is_maximizable, is_minimizable, is_movable, is_resizable, refresh_interval_sec, source_id, source_type, widget_id) VALUES(7, 'Admin', '2019-11-25 05:31:27.283', 'Admin', '2019-11-25 05:31:27.283', 0, '1', NULL, 1, 1, 1, 1, 1, 0, NULL, NULL, 10);
INSERT INTO widget_setting (id, created_by, created_on, modified_by, modified_on, version, enable_settings, is_auto_refreshed, is_closable, is_maximizable, is_minimizable, is_movable, is_resizable, refresh_interval_sec, source_id, source_type, widget_id) VALUES(8, 'Admin', '2019-11-25 05:31:27.283', 'Admin', '2019-11-25 05:31:27.283', 0, '1', NULL, 1, 1, 1, 1, 1, 0, NULL, NULL, 1);
INSERT INTO widget_setting (id, created_by, created_on, modified_by, modified_on, version, enable_settings, is_auto_refreshed, is_closable, is_maximizable, is_minimizable, is_movable, is_resizable, refresh_interval_sec, source_id, source_type, widget_id) VALUES(9, 'Admin', '2019-11-25 05:31:27.283', 'Admin', '2019-11-25 05:31:27.283', 0, '1', NULL, 1, 1, 1, 1, 1, 0, NULL, NULL, 2);
INSERT INTO widget_setting (id, created_by, created_on, modified_by, modified_on, version, enable_settings, is_auto_refreshed, is_closable, is_maximizable, is_minimizable, is_movable, is_resizable, refresh_interval_sec, source_id, source_type, widget_id) VALUES(10, 'Admin', '2019-11-25 05:31:27.283', 'Admin', '2019-11-25 05:31:27.283', 0, '1', NULL, 1, 1, 1, 1, 1, 0, NULL, NULL, 7);
INSERT INTO widget_setting (id, created_by, created_on, modified_by, modified_on, version, enable_settings, is_auto_refreshed, is_closable, is_maximizable, is_minimizable, is_movable, is_resizable, refresh_interval_sec, source_id, source_type, widget_id) VALUES(11, 'Admin', '2019-11-25 05:31:27.283', 'Admin', '2019-11-25 05:31:27.283', 0, '1', NULL, 1, 1, 1, 1, 1, 0, NULL, NULL, 11);
INSERT INTO widget_setting (id, created_by, created_on, modified_by, modified_on, version, enable_settings, is_auto_refreshed, is_closable, is_maximizable, is_minimizable, is_movable, is_resizable, refresh_interval_sec, source_id, source_type, widget_id) VALUES(12, 'Admin', '2019-11-25 05:31:27.283', 'Admin', '2019-11-25 05:31:27.283', 0, '1', NULL, 1, 1, 1, 1, 1, 0, NULL, NULL, 12);
INSERT INTO widget_setting (id, created_by, created_on, modified_by, modified_on, version, enable_settings, is_auto_refreshed, is_closable, is_maximizable, is_minimizable, is_movable, is_resizable, refresh_interval_sec, source_id, source_type, widget_id) VALUES(13, 'Admin', '2019-11-25 05:31:27.283', 'Admin', '2019-11-25 05:31:27.283', 0, '1', NULL, 1, 1, 1, 1, 1, 0, NULL, NULL, 13);

ALTER sequence widget_setting_id_seq RESTART WITH 20;

-- client_widget
INSERT INTO client_widget (id, created_by, created_on, modified_by, modified_on, version, client_code, is_visible, seq_num, widget_col, widget_id, client_dashboard_id) VALUES(1, 'Admin', '2019-11-25 05:31:45.576', 'Admin', '2019-11-25 05:31:45.576', 0, NULL, 1, 1, 2, 1, NULL);
INSERT INTO client_widget (id, created_by, created_on, modified_by, modified_on, version, client_code, is_visible, seq_num, widget_col, widget_id, client_dashboard_id) VALUES(2, 'Admin', '2019-11-25 05:31:45.580', 'Admin', '2019-11-25 05:31:45.580', 0, NULL, 1, 1, 2, 2, NULL);
INSERT INTO client_widget (id, created_by, created_on, modified_by, modified_on, version, client_code, is_visible, seq_num, widget_col, widget_id, client_dashboard_id) VALUES(3, 'Admin', '2019-11-25 05:31:45.586', 'Admin', '2019-11-25 05:31:45.586', 0, NULL, 1, 1, 1, 3, NULL);
INSERT INTO client_widget (id, created_by, created_on, modified_by, modified_on, version, client_code, is_visible, seq_num, widget_col, widget_id, client_dashboard_id) VALUES(4, 'Admin', '2019-11-25 05:31:45.590', 'Admin', '2019-11-25 05:31:45.590', 0, NULL, 1, 2, 1, 4, NULL);
INSERT INTO client_widget (id, created_by, created_on, modified_by, modified_on, version, client_code, is_visible, seq_num, widget_col, widget_id, client_dashboard_id) VALUES(5, 'Admin', '2019-11-25 05:31:45.593', 'Admin', '2019-11-25 05:31:45.593', 0, NULL, 1, 2, 2, 5, NULL);
INSERT INTO client_widget (id, created_by, created_on, modified_by, modified_on, version, client_code, is_visible, seq_num, widget_col, widget_id, client_dashboard_id) VALUES(6, 'Admin', '2019-11-25 05:31:45.596', 'Admin', '2019-11-25 05:31:45.596', 0, NULL, 1, 3, 1, 6, NULL);
INSERT INTO client_widget (id, created_by, created_on, modified_by, modified_on, version, client_code, is_visible, seq_num, widget_col, widget_id, client_dashboard_id) VALUES(7, 'Admin', '2019-11-25 05:31:45.600', 'Admin', '2019-11-25 05:31:45.600', 0, NULL, 1, 1, 3, 7, NULL);
INSERT INTO client_widget (id, created_by, created_on, modified_by, modified_on, version, client_code, is_visible, seq_num, widget_col, widget_id, client_dashboard_id) VALUES(8, 'Admin', '2019-11-25 05:31:45.603', 'Admin', '2019-11-25 05:31:45.603', 0, NULL, 1, 1, 3, 8, NULL);
INSERT INTO client_widget (id, created_by, created_on, modified_by, modified_on, version, client_code, is_visible, seq_num, widget_col, widget_id, client_dashboard_id) VALUES(9, 'Admin', '2019-11-25 05:31:45.606', 'Admin', '2019-11-25 05:31:45.606', 0, NULL, 1, 1, 3, 9, NULL);
INSERT INTO client_widget (id, created_by, created_on, modified_by, modified_on, version, client_code, is_visible, seq_num, widget_col, widget_id, client_dashboard_id) VALUES(10, 'Admin', '2019-11-25 05:31:45.610', 'Admin', '2019-11-25 05:31:45.610', 0, NULL, 1, 2, 3, 10, NULL);
INSERT INTO client_widget (id, created_by, created_on, modified_by, modified_on, version, client_code, is_visible, seq_num, widget_col, widget_id, client_dashboard_id) VALUES(11, 'Admin', '2019-11-25 05:31:45.613', 'Admin', '2019-11-25 05:31:45.613', 0, NULL, 1, 2, 3, 11, NULL);
INSERT INTO client_widget (id, created_by, created_on, modified_by, modified_on, version, client_code, is_visible, seq_num, widget_col, widget_id, client_dashboard_id) VALUES(12, 'Admin', '2019-11-25 05:31:45.620', 'Admin', '2019-11-25 05:31:45.620', 0, NULL, 1, 2, 3, 12, NULL);
INSERT INTO client_widget (id, created_by, created_on, modified_by, modified_on, version, client_code, is_visible, seq_num, widget_col, widget_id, client_dashboard_id) VALUES(13, 'Admin', '2019-05-23 00:00:00.000', 'Admin', '2019-05-23 00:00:00.000', 0, NULL, 1, 4, 1, 13, NULL);

ALTER sequence client_widget_id_seq RESTART WITH 20;

-- emp_widget
INSERT INTO emp_widget (id, created_by, created_on, modified_by, modified_on, version, client_widget_id, is_visible, seq_num, source_id, source_type, widget_col) VALUES(1, 'Admin', '2019-11-25 05:32:01.133', 'Admin', '2019-11-25 05:32:01.133', 0, 1, 1, 1, 1, 'clientwidget', 1);
INSERT INTO emp_widget (id, created_by, created_on, modified_by, modified_on, version, client_widget_id, is_visible, seq_num, source_id, source_type, widget_col) VALUES(2, 'Admin', '2019-11-25 05:32:01.140', 'Admin', '2019-11-25 05:32:01.140', 0, 2, 1, 2, 2, 'clientwidget', 2);
INSERT INTO emp_widget (id, created_by, created_on, modified_by, modified_on, version, client_widget_id, is_visible, seq_num, source_id, source_type, widget_col) VALUES(3, 'Admin', '2019-11-25 05:32:01.146', 'Admin', '2019-11-25 05:32:01.146', 0, 7, 1, 3, 7, 'clientwidget', 3);
INSERT INTO emp_widget (id, created_by, created_on, modified_by, modified_on, version, client_widget_id, is_visible, seq_num, source_id, source_type, widget_col) VALUES(4, 'Admin', '2019-11-25 05:32:01.153', 'Admin', '2019-11-25 05:32:01.153', 0, 8, 1, 1, 8, 'clientwidget', 1);
INSERT INTO emp_widget (id, created_by, created_on, modified_by, modified_on, version, client_widget_id, is_visible, seq_num, source_id, source_type, widget_col) VALUES(5, 'Admin', '2019-11-25 05:32:01.156', 'Admin', '2019-11-25 05:32:01.156', 0, 10, 1, 1, 10, 'clientwidget', 3);
INSERT INTO emp_widget (id, created_by, created_on, modified_by, modified_on, version, client_widget_id, is_visible, seq_num, source_id, source_type, widget_col) VALUES(6, 'Admin', '2019-11-25 05:32:01.163', 'Admin', '2019-11-25 05:32:01.163', 0, 12, 1, 1, 12, 'clientwidget', 2);

ALTER sequence emp_widget_id_seq RESTART WITH 10;