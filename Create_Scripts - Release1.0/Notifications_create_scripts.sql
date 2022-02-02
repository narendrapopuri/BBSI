create table action_tracker (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
action_type varchar(255),
message varchar(255),
msg_sent_time datetime2,
runtime_action_id bigint not null,
user_id bigint not null,
primary key (id));

create table actions (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
action_id bigint not null,
client_code varchar(255),
description varchar(255),
name varchar(255) not null,
type varchar(255) not null,
primary key (id));

create table announcement (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
stop_date datetime2,
filters varchar(255),
status bit,
is_checked bit,
sync bit,
message varchar(255),
sender_code varchar(255),
sender_name varchar(255),
start_date datetime2,
title varchar(255),
primary key (id));

create table announcement_list (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
emp_id varchar(255),
emp_code varchar(255),
emp_type varchar(255),
emp_location varchar(255),
sync bit,
emp_name varchar(255),
emp_email varchar(255),
announcement_id bigint,
primary key (id));

create table approval_settings (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
email varchar(255),
event_name varchar(255),
feature_code varchar(255),
group_name varchar(255),
name varchar(255),
privilege_code varchar(255),
primary key (id));

create table client (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
name varchar(255),
primary key (id));

create table client_notification_association (
fk_client bigint not null,
fk_notification_event bigint not null);

create table email_template (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
disclaimer_msg varchar(2048),
from_address varchar(255),
is_active bit,
message varchar(4096),
subject varchar(255) not null,
template_url varchar(1024),
to_address varchar(255),
notification_template_id bigint,
primary key (id));

create table feature (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
module varchar(255) not null,
name varchar(255) not null,
primary key (id));

create table mfa_otp (
id bigint not null,
email_id varchar(255) not null,
generated_on datetime2 not null,
otp varchar(255) not null,
request_id varchar(255) not null,
primary key (id));

create table notification_action (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
action_type varchar(255),
action_description varchar(255),
notif_type varchar(255),
notify_to varchar(255),
sequence int,
notification_template_id bigint,
notification_rule_id bigint,
primary key (id));

create table notification_config (
id bigint not null,
category varchar(255),
feature_code varchar(255),
ignore_generic_flow bit,
integration_enum_type varchar(255),
integration_type varchar(255),
name varchar(255),
notification_event varchar(255),
notification_group varchar(255),
path varchar(255),
privilege_code varchar(255),
status bit,
trigger_criteria varchar(255),
trigger_value varchar(255),
primary key (id));

create table notification_config_fields (
id bigint not null,
context_field varchar(255),
context_field_path varchar(255),
notification_config_id bigint,
primary key (id));

create table notification_event (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
description varchar(255),
event varchar(255),
event_name varchar(255) not null,
feature_code varchar(255) not null,
validate_message varchar(255),
primary key (id));

create table notification_rule (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
code varchar(30),
description varchar(2048),
is_active bit,
is_global bit,
is_mandatory bit,
linked_work_flow bit,
name varchar(255) not null,
privilege_code varchar(255),
notification_config_id bigint,
primary key (id));

create table notification_template (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
code varchar(30) not null,
description varchar(1024),
is_active bit,
is_auditable bit,
is_configurable bit,
is_global bit,
message_type varchar(255) not null,
name varchar(255) not null,
severity varchar(255),
type varchar(255) not null,
primary key (id));

create table notification_template_notification_action (
notification_template_id bigint not null,
notification_action_id bigint not null);

create table notification_user (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
email varchar(255) not null,
is_active bit,
name varchar(255) not null,
user_type varchar(255),
primary key (id));

create table push_notification (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
is_active bit,
message varchar(4096),
name varchar(255) not null,
subject varchar(255),
uri varchar(1024),
notification_template_id bigint,
primary key (id));

create table rule_exclusions (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
email varchar(255),
is_sync bit,
notif_rule_id bigint,
primary key (id));

create table runtime_action (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(255) not null,
executed_time datetime2 not null,
is_executed bit,
runtime_event_log_id bigint not null,
primary key (id));

create table runtime_event_log (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
event_data varchar(255) not null,
event_source varchar(255) not null,
occurence_time datetime2 not null,
severity varchar(255),
status varchar(255) not null,
client_id bigint not null,
notification_event_id bigint not null,
primary key (id));

create table sms_template (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
message varchar(4096),
mobile varchar(255),
notification_template_id bigint,
primary key (id));

alter table notification_config add constraint UK_j2a3260hfch43n5cvh3omubtp unique (name);

alter table notification_config add constraint UK_sdvqeml9n2nh8wmweq3q7kjg9 unique (notification_event);

alter table notification_rule add constraint UKsvhwi7giwdf6lrk1kfhf3sgcu unique (client_code, name);

alter table notification_template_notification_action add constraint UK_f51ynjnu9gjb646k8olxj5u02 unique (notification_action_id);

create sequence action_tracker_id_seq start with 1 increment by 1;

create sequence actions_id_seq start with 1 increment by 1;

create sequence announcement_id_seq start with 1 increment by 1;

create sequence announcement_list_id_seq start with 1 increment by 1;

create sequence approval_settings_id_seq start with 1 increment by 1;

create sequence client_id_seq start with 1 increment by 1;

create sequence email_template_id_seq start with 1 increment by 1;

create sequence employee_notif_rule_id_seq start with 1 increment by 1;

create sequence feature_id_seq start with 1 increment by 1;

create sequence mfaotp_gen start with 1 increment by 50;

create sequence notification_action_id_seq start with 1 increment by 1;

create sequence notification_config_fields_id_seq start with 1 increment by 1;

create sequence notification_configuration_id_seq start with 1 increment by 1;

create sequence notification_event_id_seq start with 1 increment by 1;

create sequence notification_rule_id_seq start with 1 increment by 1;

create sequence notification_template_id_seq start with 1 increment by 1;

create sequence push_notification_id_seq start with 1 increment by 1;

create sequence runtime_action_id_seq start with 1 increment by 1;

create sequence runtime_event_id_seq start with 1 increment by 1;

create sequence sms_template_id_seq start with 1 increment by 1;

create sequence user_id_seq start with 1 increment by 1;

alter table action_tracker add constraint FK3doeuqsjpdi79lwj4diil7j8i foreign key (runtime_action_id) references runtime_action;

alter table action_tracker add constraint FK4sl3mdkr9kap4avce7f6jp3h3 foreign key (user_id) references notification_user;

alter table announcement_list add constraint FKdb7jttfumkirx17757y81imjt foreign key (announcement_id) references announcement;

alter table client_notification_association add constraint FKfysdsn8673pao780oogjpqnup foreign key (fk_notification_event) references notification_event;

alter table client_notification_association add constraint FKtkqcko0o0m75x74yy1nrfh3ey foreign key (fk_client) references client;

alter table email_template add constraint FK2h54vpaevuwib9mj425c3xuhy foreign key (notification_template_id) references notification_template;

alter table notification_action add constraint FK2g7f3u5onjgu1cq0lnxycwkud foreign key (notification_template_id)references notification_template;

alter table notification_action add constraint FK8jg6w6chx022s32qquym57bts foreign key (notification_rule_id) references notification_rule;

alter table notification_config_fields add constraint FK1op8lnxs0dqs9fmw4ulbgy1xx foreign key (notification_config_id) references notification_config;

alter table notification_rule add constraint FKnin3873l22vokwhqn66rmo4up foreign key (notification_config_id) references notification_config;

alter table notification_template_notification_action add constraint FKl62taumkcq8njfx21ob1rqpsc foreign key (notification_action_id) references notification_action;

alter table notification_template_notification_action add constraint FKlq6yfc2rwqdns363l76g89yp8 foreign key (notification_template_id) references notification_template;

alter table push_notification add constraint FK34h7vawan9dnmis4idxys9aqq foreign key (notification_template_id) references notification_template;

alter table rule_exclusions add constraint FK9nnr3bl9yrcx1wck6wd546923 foreign key (notif_rule_id) references notification_rule;

alter table runtime_action add constraint FK5qlhogvi48qbu0eljuebs4y0n foreign key (runtime_event_log_id) references runtime_event_log;

alter table runtime_event_log add constraint FKq09rerrck7j8an6wqamaqn93j foreign key (client_id) references client;

alter table runtime_event_log add constraint FKob390mh1rmgk8qpxdp0gmd5p5 foreign key (notification_event_id) references notification_event;

alter table sms_template add constraint FKaib9x5hnxrdml5axijltpuyrj foreign key (notification_template_id) references notification_template;


-- Notification_config
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (1,'ENTITY','EMP_MGMT_PAY_DD',NULL,'DIRECT_DEPOSIT','EMPLOYEE','Direct Deposit Change','DIRECT_DEPOSIT_CHANGE','Employee Management',NULL,'EMP_MGMT_PAY_DD.ALL',1,'PUT',NULL)
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (2,'ELEMENT','EMP_MGMT_PER_PER',1,'PERSONAL_INFO','EMPLOYEE','Employee SSN Change','EMP_SSN_CHANGE','Employee Management',NULL,'EMP_MGMT_PER.ALL',1,NULL,NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (3,'ENTITY',NULL,NULL,'DIRECT_DEPOSIT','EMPLOYEE','Direct Deposit All Inactive','DIRECT_DEPOSIT_ALL_INACTIVE','Employee Management',NULL,NULL,1,'PUT',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (4,'ELEMENT','EMP_MGMT_PER_PER',NULL,'PERSONAL_INFO','EMPLOYEE','Employee DOB Change','EMP_DOB_CHANGE','Employee Management','$.date_of_birth','EMP_MGMT_PER.ALL',1,'PUT',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (5,'ELEMENT',NULL,NULL,'EMP_WORK_COMP_CLASS','EMPLOYEE','Employee Work Comp Class','EMP_WORK_COMP_CLASS','Employee Management','',NULL,1,'POST',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (6,'ELEMENT',NULL,NULL,'RESIDENTIAL_ADDRESS','EMPLOYEE','Employee Address State Change','EMP_ADDRESS_STATE_CHANGE','Employee Management','',NULL,1,'PUT',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (7,'ENTITY',NULL,NULL,'DRIVING_LICENCE','EMPLOYEE','Driving License Change','DRIVING_LICENCE_CHANGE','Employee Management','',NULL,1,'PUT',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (8,'ELEMENT','EMP_MGMT_PER',1,'PERSONAL_INFO','EMPLOYEE','Employee Name Change','EMP_NAME_CHANGE','Employee Management',NULL,'EMP_MGMT_PER.ALL',1,NULL,NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (9,'ENTITY',NULL,NULL,'TAX_FILING','EMPLOYEE','W4 Change','W4_CHANGE','Employee Management','',NULL,0,'PUT',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (10,'ENTITY',NULL,NULL,'PAYRATE','EMPLOYEE','Payrate Change','PAYRATE_CHNAGE','Employee Management','',NULL,1,'PUT',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (11,'ENTITY',NULL,NULL,'LOCATION_INFO','EMPLOYEE','Location State Create','LOCATION_STATE_CREATE','Company Management','',NULL,1,'PUT',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (12,'ENTITY',NULL,NULL,'TERMINATE','EMPLOYEE','Terminate','TERMINATE','Employee Management','','EMP_MGMT_JOB.ALL',1,'POST',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (13,'ENTITY',NULL,NULL,'NA','NA','Newhire Employee Submitted','NEWHIRE_EMPLOYEE_SUBMITTED','Employee Onboarding','','NEW_HIRE.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (14,'ENTITY','NEW_HIRE_I9_APPROVER_F',NULL,'NA','NA','Newhire Employee Document Submitted','NEWHIRE_EMPLOYEE_DOCUMENT_SUBMITTED','Employee Onboarding','','NEW_HIRE.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (15,'ENTITY','EMP_MGMT_PTO',1,'NA','NA','Time Off Request','TIME_OFF_REQUEST','Time Off','','EMP_MGMT_PTO.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (16,'ENTITY','EMP_MGMT_PTO',1,'NA','NA','Time Off Request Approve','TIME_OFF_REQUEST_APPROVE','Time Off','','EMP_MGMT_PTO.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (17,'ENTITY','EMP_MGMT_PTO',1,'NA','NA','Time Off Request Reject','TIME_OFF_REQUEST_REJECT','Time Off','','EMP_MGMT_PTO.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (18,'ENTITY','EMP_MGMT_PTO',1,'NA','NA','Time Off Request Cancel','TIME_OFF_REQUEST_CANCEL','Time Off','','EMP_MGMT_PTO.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (19,'ENTITY',NULL,NULL,'NA','NA','Payroll Submitted','PAYROLL_SUBMITTED','Payroll Timesheet','','PAY_TS_HOUR_SUBMIT.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (20,'ENTITY',NULL,NULL,'NA','NA','Manual Payroll Created','MANUAL_PAYROLL_CREATED','Payroll Timesheet','','PAY_TS.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (21,'ENTITY',NULL,NULL,'NA','NA','Payroll Due','PAYROLL_DUE','Payroll Timesheet','','PAY_TS.ALL',1,'',NULL)
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (22,'ENTITY',NULL,NULL,'NA','NA','Save Gross To Net','SAVE_GROSS_TO_NET','Payroll Timesheet','','PAY_TS.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (23,'ENTITY',NULL,NULL,'NA','NA','Payroll Calculated','PAYROLL_CALCULATED','Payroll Timesheet','','PAY_TS.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (24,'ENTITY',NULL,NULL,'NET_TO_GROSS_CALCULATOR','EMPLOYEE','Save Net To Gross','SAVE_NET_TO_GROSS','Payroll Timesheet','','PAY_TS.ALL',1,'POST',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (25,'ENTITY',NULL,NULL,'NA','NA','Duplicate SSN','DUPLICATE_SSN','Employee Onboarding','',NULL,1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (26,'ENTITY',NULL,NULL,'NA','NA','Newhire Created','NEWHIRE_CREATED','Employee Onboarding','','NEW_HIRE.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (27,'ENTITY',NULL,NULL,'NA','NA','Employee Onbording Not Initiated','EMPLOYEE_ONBORDING_NOT_INITIATED','Employee Onboarding','',NULL,1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (28,'ENTITY',NULL,NULL,'NA','NA','Password Expire','PASSWORDS_EXPIRE','User Management','',NULL,1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (29,'ENTITY',NULL,NULL,'NA','NA','Newhire Employee I9 Documents Not Initiated','NEWHIRE_EMPLOYEE_I9_DOCUMENTS_NOT_INITIATED','Employee Onboarding','','NEW_HIRE.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (30,'ELEMENT','EMP_PERSONAL',1,'PERSONAL_INFO','EMPLOYEE','Employee Personal SSN Change','EMP_PERSONAL_SSN_CHANGE','Employee Management',NULL,'EMP_MGMT_PER.ALL',1,NULL,NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (31,'ELEMENT','EMP_PERSONAL',1,'PERSONAL_INFO','EMPLOYEE','Employee Personal Name Change','EMP_PERSONAL_NAME_CHANGE','Employee Management',NULL,'EMP_MGMT_PER.ALL',1,NULL,NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (32,'ENTITY',NULL,NULL,'NA','NA','Timesheet Finalized','TIMESHEET_FINALIZED','Payroll Timesheet','','PAY_TS_HOUR_FINALIZE.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (33,'ENTITY',NULL,NULL,'NA','NA','Timesheet Not Finalized','TIMESHEET_NOT_FINALIZED','Payroll Timesheet','','PAY_TS.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (34,'ENTITY',NULL,NULL,'NA','NA','Timesheet Rejected','TIMESHEET_REJECTED','Payroll Timesheet','','PAY_TS.ALL',1,'',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (35,'ENTITY','EMP_MGMT_PAY_RATE',NULL,'PAYRATE','EMPLOYEE','Payrate Type Change','PAYRATE_TYPE_CHANGE','Employee Management','','EMP_MGMT_PAY_RATE.ALL',1,'PUT',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (36,'ENTITY',NULL,1,'LOA','EMPLOYEE','LOA Request','LOA_REQUEST','Employee Management','','LOA.ALL',1,'POST',NULL);
INSERT INTO notification_config (id,category,feature_code,ignore_generic_flow,integration_enum_type,integration_type,name,notification_event,notification_group,[path],privilege_code,status,trigger_criteria,trigger_value) VALUES (37,'ENTITY',NULL,1,'LOA','EMPLOYEE','LOA Approve','LOA_APPROVE','Employee Management','','LOA.ALL',1,'GET',NULL);
INSERT INTO notification_config (id, category, feature_code, ignore_generic_flow, integration_enum_type, integration_type, name, notification_event, notification_group, [path], privilege_code, status, trigger_criteria, trigger_value) VALUES(38, 'ENTITY', '', 1, 'NA', 'NA', 'Background Report', 'BACKGROUND_REPORT', 'Report', '', '', 1, '', NULL);
INSERT INTO notification_config (id, category, feature_code, ignore_generic_flow, integration_enum_type, integration_type, name, notification_event, notification_group, [path], privilege_code, status, trigger_criteria, trigger_value) VALUES(39, 'ENTITY', NULL, NULL, 'TAX_FILING', 'EMPLOYEE', 'Employee Tax Filing', 'TAX_FILING', 'Employee Management', NULL, NULL, 1, 'PUT', NULL);
INSERT INTO notification_config (id, category, feature_code, ignore_generic_flow, integration_enum_type, integration_type, name, notification_event, notification_group, [path], privilege_code, status, trigger_criteria, trigger_value) VALUES(40, 'ENTITY', '', 1, 'NA', 'NA', 'Tax Document Upload', 'TAX_FILING_DOCUMENT_UPLOAD', 'Employee Management', '', '', 1, '', NULL);

ALTER sequence notification_configuration_id_seq RESTART WITH 40;

-- Notification_Template
INSERT INTO notification_template (id,created_by,created_on,modified_by,modified_on,version,client_code,code,description,is_active,is_auditable,is_configurable,is_global,message_type,name,severity,[type]) VALUES
(70,'admin@osius.com','2019-10-21 09:53:22.449','admin@osius.com','2019-12-16 07:42:05.176',3,'909464','RFNWSYRO211019094841',NULL,1,NULL,NULL,1,'Informational','Direct Deposit Change','Urgent','Push Notification')
,(71,'admin@osius.com','2019-10-21 13:25:47.913','admin@osius.com','2019-12-16 08:54:11.310',3,'909464','27K2CJV1211019132427',NULL,1,NULL,NULL,1,'Workflow','SSN Change Client','Urgent','Push Notification')
,(72,'admin@osius.com','2019-10-21 13:27:07.389','admin@osius.com','2019-12-16 08:53:43.206',3,'909464','DHDREDWO211019132551',NULL,1,NULL,NULL,1,'Workflow','SSN Change Branch','Urgent','Push Notification')
,(73,'admin@osius.com','2019-10-21 13:28:14.684','admin@osius.com','2019-12-16 08:55:38.394',2,'909464','FARSR3FD211019132711',NULL,1,NULL,NULL,1,'Informational','SSN Change Client Bak','Urgent','Push Notification')
,(74,'admin@osius.com','2019-10-21 13:32:51.396','admin@osius.com','2019-12-16 08:59:08.331',5,'909464','ZDGZKZIA211019132819',NULL,1,NULL,NULL,1,'Informational','SSN Change Personal Employee','Urgent','Push Notification')
,(75,'admin@osius.com','2019-10-22 07:22:39.148','admin@osius.com','2019-12-16 07:48:46.985',1,'909464','QNZKLMJ3221019072052',NULL,1,NULL,NULL,1,'Informational','Name Change Employee','Urgent','Push Notification')
,(76,'admin@osius.com','2019-10-22 07:24:57.695','admin@osius.com','2019-12-16 08:44:17.516',4,'909464','REVVD8VZ221019072245',NULL,1,NULL,NULL,1,'Workflow','Name Change Client','Urgent','Push Notification')
,(77,'admin@osius.com','2019-10-22 07:27:05.028','admin@osius.com','2019-12-16 08:43:54.663',2,'909464','03MT5XKN221019072516',NULL,1,NULL,NULL,1,'Workflow','Name Change Branch','Urgent','Push Notification')
,(78,'admin@osius.com','2019-10-22 07:29:28.763','admin@osius.com','2019-12-16 07:48:20.086',1,'909464','OXAIHDSC221019072840',NULL,1,NULL,NULL,1,'Informational','Name Change Client Bak','Urgent','Push Notification')
,(79,'admin@osius.com','2019-10-22 09:08:39.571','admin@osius.com','2019-12-16 09:00:13.116',3,'909464','ZLAO0KFY221019090656',NULL,1,NULL,NULL,1,'Informational','Work Comp Class Change','Urgent','Push Notification')
,(80,'admin@osius.com','2019-10-22 09:36:57.546','admin@osius.com','2019-12-16 07:43:46.113',2,'909464','3VBZQNL1221019093630',NULL,1,NULL,NULL,1,'Informational','Emp Address State Change','Urgent','Push Notification')
,(81,'admin@osius.com','2019-10-22 09:38:03.442','admin@osius.com','2019-12-16 07:45:25.810',2,'909464','YVDLUPDW221019093701',NULL,1,NULL,NULL,1,'Informational','Emp New Hire Created','Urgent','Push Notification')
,(82,'admin@osius.com','2019-10-22 09:39:29.912','admin@osius.com','2019-12-16 07:42:25.514',2,'909464','ODMFSZBY221019093818',NULL,1,NULL,NULL,1,'Informational','Duplicate SSN','Urgent','Push Notification')
,(83,'admin@osius.com','2019-10-22 14:47:00.645','admin@osius.com','2019-12-16 08:47:35.188',1,'909464','SNMNSD6L221019144518',NULL,1,NULL,NULL,1,'Informational','Newhire Employee Document Sub','Urgent','Push Notification')
,(84,'admin@osius.com','2019-10-22 14:48:16.629','admin@osius.com','2019-12-16 08:46:02.035',1,'909464','EAEMALXG221019144704',NULL,1,NULL,NULL,1,'Informational','Newhire Employee I9 Document','Urgent','Push Notification')
,(85,'admin@osius.com','2019-10-23 08:55:34.392','admin@osius.com','2019-12-16 07:44:51.709',4,'909464','N4A35A8T231019085139',NULL,1,NULL,NULL,1,'Informational','Emp Inactive All Dir Dep Acc','Urgent','Push Notification')
,(86,'admin@osius.com','2019-10-23 09:18:05.055','admin@osius.com','2019-12-16 07:46:26.820',5,'909464','TGSIQRUU231019091614',NULL,1,NULL,NULL,1,'Informational','Emp Submits DOB Change','Urgent','Push Notification')
,(87,'admin@osius.com','2019-10-23 09:52:21.895','admin@osius.com','2019-12-16 07:44:18.637',6,'909464','Y7AONL4S231019095010',NULL,1,NULL,NULL,1,'Informational','Emp Change Driver License Info','Urgent','Push Notification')
,(88,'admin@osius.com','2019-10-23 10:47:16.918','admin@osius.com','2019-12-16 07:46:43.732',3,'909464','FD09LD8H231019104533',NULL,1,NULL,NULL,1,'Informational','Emp W4 Change','Urgent','Push Notification')
,(89,'admin@osius.com','2019-10-23 10:53:01.976','admin@osius.com','2019-12-16 07:42:51.316',3,'909464','FUJ6TFVU231019105153',NULL,1,NULL,NULL,1,'Informational','Employee Submit I9 Form','Urgent','Push Notification')
,(90,'admin@osius.com','2019-10-24 09:20:47.448','admin@osius.com','2019-10-24 09:20:47.448',0,'909464','MEONLUK8241019092002',NULL,1,NULL,NULL,1,'Informational','Time Sheet Submitted','High','Push Notification')
,(91,'admin@osius.com','2019-10-24 09:22:27.267','admin@osius.com','2019-12-16 07:49:49.345',2,'909464','N9SEASID241019092039',NULL,1,NULL,NULL,1,'Informational','Name Change Personal Client','Urgent','Push Notification')
,(92,'admin@osius.com','2019-10-24 09:24:48.799','admin@osius.com','2019-12-16 07:49:20.627',1,'909464','13LX6JM4241019092413',NULL,1,NULL,NULL,1,'Workflow','Name Change Personal Branch','Urgent','Push Notification')
,(93,'admin@osius.com','2019-10-24 09:26:29.829','admin@osius.com','2019-12-16 08:44:58.133',1,'909464','BLOF8S9E241019092528',NULL,1,NULL,NULL,1,'Informational','Name Change Personal Clientbak','Urgent','Push Notification')
,(94,'admin@osius.com','2019-10-25 09:41:23.597','admin@osius.com','2019-12-16 07:41:42.370',7,'909464','EGX8JV4Q251019093834',NULL,1,NULL,NULL,1,'Informational','Client Compensation PR Change','High','Push Notification')
,(95,'admin@osius.com','2019-10-25 10:17:51.143','admin@osius.com','2019-12-16 07:43:19.503',4,'909464','BHPBIDRK251019101610',NULL,1,NULL,NULL,1,'Informational','Employee Terminated By Client','Urgent','Push Notification')
,(96,'admin@osius.com','2019-10-25 11:11:52.503','admin@osius.com','2019-12-16 08:50:11.946',3,'909464','FSKD9WHK251019110932',NULL,1,NULL,NULL,1,'Informational','Payroll Due','High','Push Notification')
,(97,'admin@osius.com','2019-10-25 11:18:37.896','admin@osius.com','2019-12-16 08:48:51.735',2,'909464','TB5UGNJM251019111538',NULL,1,NULL,NULL,1,'Informational','Password Expire Notice','Urgent','Push Notification')
,(98,'admin@osius.com','2019-10-25 11:26:13.270','admin@osius.com','2019-12-16 08:45:28.392',1,'909464','PVPFACGA251019112347',NULL,1,NULL,NULL,1,'Informational','Name Change Personal Empbak','Urgent','Push Notification')
,(99,'admin@osius.com','2019-10-25 11:36:04.589','admin@osius.com','2019-12-16 07:47:17.332',3,'909464','JQXVP61K251019113253',NULL,1,NULL,NULL,1,'Informational','I-9 Expiration Notice','Urgent','Push Notification')
,(100,'admin@osius.com','2019-10-25 11:42:35.792','admin@osius.com','2019-12-16 07:46:02.131',2,'909464','KODZWPWO251019113952',NULL,1,NULL,NULL,1,'Informational','Emp  Not Completed Req Onboard','Urgent','Push Notification')
,(106,'admin@osius.com','2019-10-28 21:20:21.316','admin@osius.com','2019-10-28 21:20:21.316',0,'909143','XMTFRKTC281019211846',NULL,1,NULL,NULL,1,'Informational','Time Off Request Approved','Normal','Push Notification')
,(108,'admin@osius.com','2019-10-29 10:41:02.685','admin@osius.com','2019-12-16 08:58:37.793',8,'909464','VDWSQ6QT291019104039',NULL,1,NULL,NULL,1,'Informational','Time Off Requested','Urgent','Push Notification')
,(109,'admin@osius.com','2019-10-29 11:45:38.056','admin@osius.com','2019-12-11 09:27:53.432',3,'909464','EPXORDAC291019114429',NULL,1,NULL,NULL,1,'Informational','Time Off Request Canceled','Urgent','Push Notification')
,(110,'admin@osius.com','2019-10-30 12:33:53.018','admin@osius.com','2019-12-16 08:50:54.813',2,'909464','Y5SKYB0W301019123301',NULL,1,NULL,NULL,1,'Workflow','Payroll Submit','Urgent','Push Notification')
,(111,'admin@osius.com','2019-10-30 12:38:01.120','admin@osius.com','2019-12-16 08:50:33.939',1,'909464','76H0HVF1301019123724',NULL,1,NULL,NULL,1,'Informational','Payroll Finalize','Urgent','Push Notification')
,(118,'admin@osius.com','2019-11-04 09:06:34.962','admin@osius.com','2019-12-16 08:57:12.878',2,'909464','UXCZEQFI041119090413',NULL,1,NULL,NULL,1,'Workflow','SSN Change Personal Client','Urgent','Push Notification')
,(119,'admin@osius.com','2019-11-04 09:08:54.506','admin@osius.com','2019-12-16 08:56:08.107',1,'909464','OFIRQ6XM041119090735',NULL,1,NULL,NULL,1,'Workflow','SSN Change Personal Branch','Urgent','Push Notification')
,(120,'admin@osius.com','2019-11-04 09:10:13.140','admin@osius.com','2019-12-16 08:58:15.318',3,'909464','IJCCF58S041119090913',NULL,1,NULL,NULL,1,'Informational','SSN Change Personal Client Bak','Urgent','Push Notification')
,(121,'admin@osius.com','2019-11-04 21:50:56.739','admin@osius.com','2019-12-16 08:59:47.419',7,'909464','RNNT6QN4041119214944',NULL,1,NULL,NULL,1,'Informational','Veterans Day','Urgent','Push Notification')
,(140,'admin@osius.com','2019-11-07 20:07:00.312','admin@osius.com','2019-12-16 07:47:34.224',6,'909464','H8OTRPBJ071119200509',NULL,1,NULL,NULL,1,'Informational','LOA Approved','Urgent','Push Notification')
,(141,'admin@osius.com','2019-11-13 05:36:23.322','admin@osius.com','2019-12-16 08:48:05.672',1,'909464','ZWXVRZZR131119053132',NULL,1,NULL,NULL,1,'Informational','Newhire Onboarding Started','Urgent','Push Notification')
,(142,'admin@osius.com','2019-11-14 09:19:17.084','admin@osius.com','2019-12-16 08:49:31.994',1,'909464','NVATE0R4141119091827',NULL,1,NULL,NULL,1,'Informational','Payroll Calculate','Urgent','Push Notification')
,(143,'admin@osius.com','2019-11-14 09:31:44.544','admin@osius.com','2019-12-16 08:49:51.727',3,'909464','WZWQXTCU141119093036',NULL,1,NULL,NULL,1,'Workflow','Payroll Submitted','Urgent','Push Notification')
,(144,'admin@osius.com','2019-11-14 11:59:47.254','admin@osius.com','2019-12-16 08:54:38.260',2,'909464','TUQG4GNA141119115851',NULL,1,NULL,NULL,1,'Informational','Timesheet Finalized','Urgent','Push Notification')
,(147,'admin@osius.com','2019-11-18 10:25:49.107','admin@osius.com','2019-12-16 08:57:43.661',1,'909464','O53ZVX4Y181119102528',NULL,1,NULL,NULL,1,'Informational','SSN Change Personal Clientbak','Urgent','Push Notification')
,(160,'admin@osius.com','2019-12-03 06:49:14.419','admin@osius.com','2019-12-16 08:46:31.412',1,'909464','LGOTWLGK031219064611',NULL,1,NULL,NULL,1,'Informational','Newhire Employee Submitted','Urgent','Push Notification')
,(161,'admin@osius.com','2019-12-03 07:12:29.974','admin@osius.com','2019-12-16 08:47:08.207',1,'909464','QBLN5KDD031219071122',NULL,1,NULL,NULL,1,'Informational','Name Change Personal Employee','Urgent','Push Notification')
,(162,'admin@osius.com','2019-12-04 07:15:50.953','admin@osius.com','2019-12-16 08:49:12.817',1,'909464','04EDY4SC041219071435',NULL,1,NULL,NULL,1,'Informational','Payrate Type Change','Urgent','Push Notification')
,(164,'admin@osius.com','2019-12-04 07:51:56.859','admin@osius.com','2019-12-16 08:43:04.718',5,'909464','2WZZNZQC041219075110',NULL,1,NULL,NULL,1,'Informational','LOA Requested','Urgent','Push Notification')
,(165,'admin@osius.com','2019-12-04 08:37:55.908','admin@osius.com','2019-12-16 08:51:50.519',2,'909464','IEIMFX1F041219083521',NULL,1,NULL,NULL,1,'Workflow','Payroll Submitted Push','Urgent','Push Notification')
,(167,'admin@osius.com','2019-12-04 08:46:13.572','admin@osius.com','2019-12-16 08:51:26.109',1,'909464','UXMGAFST041219084406',NULL,1,NULL,NULL,1,'Informational','Payroll Submitted Email','NA','Email')
,(168,'admin@osius.com','2019-12-04 08:56:30.168','admin@osius.com','2019-12-16 08:55:04.315',1,'909464','FXSSL7SM041219085521',NULL,1,NULL,NULL,1,'Informational','Timesheet Rejected','Urgent','Push Notification')
,(169,'admin@osius.com','2019-12-04 09:28:36.274','admin@osius.com','2019-12-16 08:43:32.270',1,'909464','ACGRKVF1041219092747',NULL,1,NULL,NULL,1,'Informational','Location State Create','Urgent','Push Notification')
,(170,'admin@osius.com','2019-12-06 09:15:44.303','admin@osius.com','2019-12-06 09:15:44.303',0,'909464','YVCLXGF5061219091449',NULL,1,NULL,NULL,1,'Informational','Time Off Request Not Approved','Urgent','Push Notification')
,(172,'admin@osius.com','2019-12-12 12:32:18.446','admin@osius.com','2019-12-16 07:47:53.693',2,'909464','FWDCXGKH121219123123',NULL,1,NULL,NULL,1,'Informational','LOA Not Approved','Urgent','Push Notification')
,(173,'admin@osius.com','2020-02-24 12:04:48.138','admin@osius.com','2020-02-24 12:04:48.138',0,'904975','HAB1FN6D240220120312',NULL,1,NULL,NULL,1,'Workflow','LeaveRequest','NA','Text')
,(174,'admin@osius.com','2020-02-28 06:26:51.933','admin@osius.com','2020-02-28 06:26:51.933',0,'900143','M2R7MHME280220062537',NULL,1,NULL,NULL,0,'Informational','Test','Urgent','Push Notification')
,(175, 'admin@osius.com', '2020-05-04 12:04:43.311', 'admin@osius.com', '2020-05-04 12:04:43.311', 0, '909464', 'RJJQTN9U040520120337', NULL, 1, NULL, NULL, 1, 'Informational', 'BACKGROUND_REPORT', 'High', 'Push Notification')
,(176, 'admin@osius.com', '2020-05-14 20:12:58.871', 'admin@osius.com', '2020-05-14 20:12:58.871', 0, '909464', 'DJBOBRR0140520200812', NULL, 1, NULL, NULL, 1, 'Informational', 'TAX_FILING_TEMPLATE', 'High', 'Push Notification')
,(177, 'admin@osius.com', '2020-05-15 06:55:18.590', 'admin@osius.com', '2020-05-15 06:55:18.590', 0, '909464', 'RBPAD0UM150520065236', NULL, 1, NULL, NULL, 1, 'Informational', 'TAX_DOCUMENT_UPLOAD', 'High', 'Push Notification');

ALTER sequence notification_template_id_seq RESTART WITH 200;

-- Push_notification
INSERT INTO push_notification (id,created_by,created_on,modified_by,modified_on,version,is_active,message,name,subject,uri,notification_template_id) VALUES
(47,'admin@osius.com','2019-10-21 09:53:22.517','admin@osius.com','2019-12-16 07:42:05.175',3,1,'Direct deposit account has been updated for an employee with code {{employee_code}}','Direct Deposit Change','Direct Deposit changed by Emp','',70)
,(48,'admin@osius.com','2019-10-21 13:25:47.929','admin@osius.com','2019-12-16 08:54:11.309',3,1,'Social security number correction request successfully submitted for approval. Additional documentation may be required prior to approval. You will receive a notification once the request has been processed.','SSN Change Client','SSN Change Request','',71)
,(49,'admin@osius.com','2019-10-21 13:27:07.393','admin@osius.com','2019-12-16 08:53:43.206',3,1,'Employee [{{employee_code}}, {{employee_name}}], for [ {{client_name}}, {{client_code}}]  has submitted a social security number correction. Confirm all required documentation is acquired and forward to corporate payroll for approval.','SSN Change Branch','SSN correction request','',72)
,(50,'admin@osius.com','2019-10-21 13:28:14.687','admin@osius.com','2019-12-16 08:55:38.394',2,1,'The social security number correction request for [{{employee_code}}, {{employee_name}}] has been approved. The employee record has been updated.','SSN Change Client Bak','SSN Change Approved','',73)
,(51,'admin@osius.com','2019-10-21 13:32:51.403','admin@osius.com','2019-12-16 08:59:08.331',5,1,'Social security number correction request successfully submitted for approval. Additional documentation may be required prior to approval. You will receive a notification once the request has been processed.','SSN Change Personal Employee','SSN Change Request Submitted','',74)
,(52,'admin@osius.com','2019-10-22 07:22:39.164','admin@osius.com','2019-12-16 07:48:46.984',1,1,'Name change request successfully submitted for approval. Additional documentation may be required prior to approval. You will receive a notification once the request has been processed.','Name Change Employee','Name Change Request submitted','',75)
,(53,'admin@osius.com','2019-10-22 07:24:57.701','admin@osius.com','2019-12-16 08:44:17.516',4,1,'Employee name correction request successfully submitted for approval. Additional documentation may be required prior to approval. You will receive a notification once the request has been processed.','Name Change Client','Name Change Request','',76)
,(54,'admin@osius.com','2019-10-22 07:27:05.031','admin@osius.com','2019-12-16 08:43:54.663',2,1,'Employee [{{employee_code}}, {{employee_name}}], for [ {{client_name}}, {{client_code}}]  has submitted a name  correction. Confirm all required documentation is acquired and forward to corporate payroll for approval.','Name Change Branch','Name correction request','',77)
,(55,'admin@osius.com','2019-10-22 07:29:28.770','admin@osius.com','2019-12-16 07:48:20.085',1,1,'The Name correction request for [{{employee_code}}, {{employee_name}}] has been approved. The employee record has been updated.','Name Change Client Bak','Name Change Approved','',78)
,(56,'admin@osius.com','2019-10-22 09:08:39.589','admin@osius.com','2019-12-16 09:00:13.115',3,1,'{{employee_code}} is now in the {{new_code}} - {{new_desc}} Workers'' Comp class. This employee''s previous Workers'' Comp class was {{old_code}} - {{old_desc}}. The effective date of the change is {{effective_date}}.','Work Comp Class Change','Work Comp class changed','',79)
,(57,'admin@osius.com','2019-10-22 09:36:57.559','admin@osius.com','2019-12-16 07:43:46.112',2,1,'{{employee_code}},{{name}} has a home State of {{state}} that is not set up as a work state or tax for {{client_name}}.','Emp Address State Change','Employee address is changed','',80)
,(58,'admin@osius.com','2019-10-22 09:38:03.446','admin@osius.com','2019-12-16 07:45:25.809',2,1,'{{employee_name}} is now Active.','Emp New Hire Created','New Hire created','',81)
,(59,'admin@osius.com','2019-10-22 09:39:29.919','admin@osius.com','2019-12-16 07:42:25.513',2,1,'{{employee_name}} ''s record has been selected for secondary verification.','Duplicate SSN','Duplicate SSN observed','',82)
,(60,'admin@osius.com','2019-10-22 14:47:00.656','admin@osius.com','2019-12-16 08:47:35.188',1,1,'New Hire Employee {{employee_name}} has submitted documents','Newhire Employee Document Sub','New Hire Employee Submit','',83)
,(61,'admin@osius.com','2019-10-22 14:48:16.632','admin@osius.com','2019-12-16 08:46:02.035',1,1,'New Hire Employee {{employee_name}} has submitted the i9 Document','Newhire Employee I9 Document','New Hire Employee I9 Document','',84)
,(62,'admin@osius.com','2019-10-23 08:55:34.403','admin@osius.com','2019-12-16 07:44:51.708',4,1,'Employee {{employee_code}},{{employee_name}} inactivated all direct deposit accounts.','Emp Inactive All Dir Dep Acc','Emp Inactive all DD Accounts','',85)
,(63,'admin@osius.com','2019-10-23 09:18:05.067','admin@osius.com','2019-12-16 07:46:26.819',5,1,'Employee {{employee_code}},{{employee_name}} has updated  the date of birth change','Emp Submits DOB Change','Employee submits DOB change','',86)
,(64,'admin@osius.com','2019-10-23 09:52:21.937','admin@osius.com','2019-12-16 07:44:18.636',6,1,'Employee {{employee_code}},{{employee_name}} has updated the driving licence information','Emp Change Driver Licence Info','Driver Licence Info Updated','',87)
,(65,'admin@osius.com','2019-10-23 10:47:16.927','admin@osius.com','2019-12-16 07:46:43.732',3,1,'Employee {{employee_code}},{{employee_name}} has updated W4 information','Emp W4 Change','Employee updated the W4','',88)
,(66,'admin@osius.com','2019-10-23 10:53:01.987','admin@osius.com','2019-12-16 07:42:51.315',3,1,'Employee {{employee_name}} has submitted I9 forms','Employee Submit I9 Form','Employee submitted I9 Form','',89)
,(68,'admin@osius.com','2019-10-24 09:22:27.270','admin@osius.com','2019-12-16 07:49:49.344',2,1,'Name change request successfully submitted for approval. Additional documentation may be required prior to approval. You will receive a notification once the request has been processed.','Name Change Personal Client','Name Change Request submited','',91)
,(69,'admin@osius.com','2019-10-24 09:24:48.803','admin@osius.com','2019-12-16 07:49:20.627',1,1,'Employee [{{employee_code}}, {{employee_name}}], for [ {{client_name}}, {{client_code}}]  has submitted a name  correction. Confirm all required documentation is acquired and forward to corporate payroll for approval.','Name Change Personal Branch','Name correction request','',92)
,(70,'admin@osius.com','2019-10-24 09:26:29.838','admin@osius.com','2019-12-16 08:44:58.133',1,1,'The Name correction request for [{{employee_code}}, {{employee_name}}] has been approved. The employee record has been updated.','Name Change Personal Clientbak','Name Change Approved','',93)
,(71,'admin@osius.com','2019-10-25 09:41:23.633','admin@osius.com','2019-12-16 07:41:42.365',7,1,'Client {{client_code}} changes Compensation Payrate for an employee {{employee_code}},{{employee_name}}','Client Compensation PR Change','Client Change Employee Payrate','',94)
,(72,'admin@osius.com','2019-10-25 10:17:51.157','admin@osius.com','2019-12-16 07:43:19.502',4,1,'Employee {{employee_code}},{{employee_name}} terminated by Client Admin {{client_name}}','Employee Terminated By Client','Employee terminated by Client','',95)
,(73,'admin@osius.com','2019-10-25 11:11:52.513','admin@osius.com','2019-12-16 08:50:11.946',3,1,'Payroll is due on {{due_date}}.','Payroll Due','Payroll due','',96)
,(74,'admin@osius.com','2019-10-25 11:18:37.905','admin@osius.com','2019-12-16 08:48:51.734',2,1,'Your password will expire in {{days}}','Password Expire Notice','PASSWORD_EXPIRE_NOTICE','',97)
,(75,'admin@osius.com','2019-10-25 11:26:13.278','admin@osius.com','2019-12-16 08:45:28.392',1,1,'Your name change has been approved and will be reflected once admin is updates your details in portal','Name Change Personal Empbak','Name change approved','',98)
,(76,'admin@osius.com','2019-10-25 11:36:04.596','admin@osius.com','2019-12-16 07:47:17.331',3,1,'Employee {{employee_name}} has not initiated I9 process','I-9 Expiration Notice','I-9 Expiration notice','',99)
,(77,'admin@osius.com','2019-10-25 11:42:35.800','admin@osius.com','2019-12-16 07:46:02.131',2,1,'Employee {{employee_name}} has not completed the required onboarding','Emp  Not Completed Req Onboard','Emp not completed onboarding','',100)
,(78,'admin@osius.com','2019-10-28 21:20:21.333','admin@osius.com','2019-10-28 21:20:21.333',0,1,'Your Time Off request was approved.','Time Off Request Approved','Time Off Request Approved',NULL,106)
,(79,'admin@osius.com','2019-10-29 10:41:02.701','admin@osius.com','2019-12-16 08:58:37.793',8,1,'Employee  {{employee_code}},{{name}} has Requested Time Off from {{start_date}} to {{end_date}} .','Time Off Requested','Time Off Requested','',108)
,(80,'admin@osius.com','2019-10-29 11:45:38.065','admin@osius.com','2019-12-11 09:27:53.431',3,1,'Employee  {{employee_code}},{{name}} has cancelled Time Off  from {{start_date}} to {{end_date}} .','Time Off Request Canceled','Time Off Request Canceled','',109)
,(81,'admin@osius.com','2019-10-30 12:33:53.034','admin@osius.com','2019-12-16 08:50:54.812',2,1,'{{message}}','Payroll Submit','PAYROLL_SUBMIT','',110)
,(82,'admin@osius.com','2019-10-30 12:38:01.122','admin@osius.com','2019-12-16 08:50:33.939',1,1,'Payroll finalized','Payroll Finalize','PAYROLL_FINALIZE','',111)
,(84,'admin@osius.com','2019-11-04 09:06:34.970','admin@osius.com','2019-12-16 08:57:12.878',2,1,'Employee [{{employee_code}}, {{employee_name}}] has submitted a social security number correction. Please review the required Social Security Card and verify the information is correct prior to approving the request. Updated documentation must be submitted to BBSI prior to any changes being made','SSN Change Personal Client','SSN  Change Request Submited','',118)
,(85,'admin@osius.com','2019-11-04 09:08:54.508','admin@osius.com','2019-12-16 08:56:08.107',1,1,'Employee [{{employee_code}}, {{employee_name}}], for [ {{client_name}}, {{client_code}}]  has submitted a social security number correction. Confirm all required documentation is acquired and forward to corporate payroll for approval.','SSN Change Personal Branch','SSN Change Request Submited','',119)
,(86,'admin@osius.com','2019-11-04 09:10:13.142','admin@osius.com','2019-12-16 08:58:15.318',3,1,'The social security number correction request for [{{employee_code}}, {{employee_name}}] has been approved. The employee record has been updated.','SSN Change Personal Client Bak','SSN Change Approved','',120)
,(87,'admin@osius.com','2019-11-04 21:50:56.751','admin@osius.com','2019-12-16 08:59:47.419',7,1,'As a reminder, Monday November 11th is a bank holiday.  The Federal Reserve and some banks will be closed.','Veterans Day','Bank Holiday','',121)
,(92,'admin@osius.com','2019-11-07 20:07:00.328','admin@osius.com','2019-12-16 07:47:34.224',6,1,'Your leave of absence from {{start_date}} to {{end_date}} has been approved.','LOA Approved','Leave of Absence Approved','',140)
,(93,'admin@osius.com','2019-11-13 05:36:23.341','admin@osius.com','2019-12-16 08:48:05.672',1,1,'{{employee_name}} Started NewHire Onboarding.','Newhire Onboarding Started','NewHire Onboarding Started','',141)
,(94,'admin@osius.com','2019-11-14 09:19:17.126','admin@osius.com','2019-12-16 08:49:31.993',1,1,'PAYROLL CALCULATED','Payroll Calculate','PAYROLL CALCULATED','',142)
,(95,'admin@osius.com','2019-11-14 09:31:44.552','admin@osius.com','2019-12-16 08:49:51.726',3,1,'PAYROLL SUBMITTED {{message}}','Payroll Submitted','PAYROLL SUBMITTED','',143)
,(96,'admin@osius.com','2019-11-14 11:59:47.265','admin@osius.com','2019-12-16 08:54:38.260',2,1,'Payroll Finalized','Timesheet Finalized','PAYROLL_FINALIZED','',144)
,(99,'admin@osius.com','2019-11-18 10:25:49.147','admin@osius.com','2019-12-16 08:57:43.661',1,1,'The social security number correction request for [{{employee_code}}, {{employee_name}}] has been approved. The employee record has been updated.','SSN Change Personal Clientbak','SSN Change Approved','',147)
,(110,'admin@osius.com','2019-12-03 06:49:14.428','admin@osius.com','2019-12-16 08:46:31.412',1,1,'Employee {{employee_name}} has been submitted.','Newhire Employee Submitted','NewHire Employee Submitted','',160)
,(111,'admin@osius.com','2019-12-03 07:12:29.982','admin@osius.com','2019-12-16 08:47:08.206',1,1,'The name correction request successfully submitted for approval. Additional documentation may be required prior to approval. You will receive a notification once the request has been processed.','Name Change Personal Employee','Name Change Request Submitted','',161)
,(112,'admin@osius.com','2019-12-04 07:15:50.971','admin@osius.com','2019-12-16 08:49:12.817',1,1,'{{employee_code}} is now in the {{new_pay_rate_type}} - {{new_desc}} PayRate Type. This employee''s previous PayRate Type was {{old_pay_rate_type}} - {{old_desc}}. The effective date of the change is {{effective_date}}.','Payrate Type Change','Pay Frequency Change','',162)
,(114,'admin@osius.com','2019-12-04 07:51:56.867','admin@osius.com','2019-12-16 08:43:04.716',5,1,'Employee  {{employee_code}},{{name}} has Requested LOA from {{start_date}} to {{end_date}} .','LOA Requested','LOA Requested','',164)
,(115,'admin@osius.com','2019-12-04 08:37:55.919','admin@osius.com','2019-12-16 08:51:50.519',2,1,'{{message}}.','Payroll Submitted Push','PayRoll Submitted','',165)
,(117,'admin@osius.com','2019-12-04 08:56:30.177','admin@osius.com','2019-12-16 08:55:04.315',1,1,'Payroll Rejected','Timesheet Rejected','Payroll Rejected','',168)
,(118,'admin@osius.com','2019-12-04 09:28:36.281','admin@osius.com','2019-12-16 08:43:32.270',1,1,'Location State Create.','Location State Create','Location State Create','',169)
,(119,'admin@osius.com','2019-12-06 09:15:44.312','admin@osius.com','2019-12-06 09:15:44.312',0,1,'Your Time Off Request is not Approved','Time Off Request Not Approved','Time Off Request not Approved',NULL,170)
,(120,'admin@osius.com','2019-12-12 12:32:18.455','admin@osius.com','2019-12-16 07:47:53.692',2,1,'Your leave of absence from {{start_date}} to {{end_date}} has not approved.','LOA Not Approved','LOA Not Approved','',172)
,(122,'admin@osius.com','2020-02-28 06:26:51.945','admin@osius.com','2020-02-28 06:26:51.945',0,1,'Hurray!','Test','You have a notification',NULL,174)
,(123, 'admin@osius.com', '2020-05-04 12:04:43.317', 'admin@osius.com', '2020-05-04 12:04:43.317', 0, 1, '{{name}} Report generation successful', 'BACKGROUND_REPORT', 'Report generated Successfully', NULL, 175)
,(124, 'admin@osius.com', '2020-05-14 20:12:58.912', 'admin@osius.com', '2020-05-14 20:12:58.912', 0, 1, '{{employee_name}} ({{employee_code}}) of {{client_name}} ({{client_code}}) has changed the tax information on myBBSI Portal. Please send out the needed information to complete the next steps.', 'TAX_FILING_TEMPLATE', 'Employee updated his tax info', NULL, 176)
,(125, 'admin@osius.com', '2020-05-15 06:55:18.597', 'admin@osius.com', '2020-05-15 06:55:18.597', 0, 1, '{{employee_name}} ({{employee_code}}) of {{client_name}} ({{client_code}}) has uploaded new documents through myBBSI Portal. Please provide input to the employee if needed.', 'TAX_DOCUMENT_UPLOAD', 'Employee uploaded tax document', NULL, 177);

ALTER sequence push_notification_id_seq RESTART WITH 150;

-- Email_template
INSERT INTO email_template (id,created_by,created_on,modified_by,modified_on,version,disclaimer_msg,from_address,is_active,message,subject,template_url,to_address,notification_template_id) VALUES
(30,'admin@osius.com','2019-12-04 08:46:13.583','admin@osius.com','2019-12-16 08:51:26.108',1,'PLEASE DO NOT REPLY',NULL,1,'Hi {{receiver_name}},<br> Your Payroll with reference {{batch_number}} has been rejected by {{sender_name}}.<br>Comments for reference: {{message}}<br> Thanks, BBSI TEAM','Payroll Submitted','',NULL,167);

ALTER sequence email_template_id_seq RESTART WITH 31;

-- Notification_rule
INSERT INTO notification_rule (id,created_by,created_on,modified_by,modified_on,version,client_code,code,description,is_active,is_global,is_mandatory,linked_work_flow,name,privilege_code,notification_config_id) VALUES
(75,'admin@osius.com','2019-10-21 12:06:29.492','admin@osius.com','2019-12-16 07:27:46.275',4,'909464','9C0EH0UL211019120534',NULL,1,1,1,NULL,'Direct Deposit Change',NULL,1)
,(82,'admin@osius.com','2019-10-21 13:35:33.961','admin@osius.com','2019-12-12 06:50:38.688',9,'902006','37FHMHP1211019133315',NULL,1,1,1,1,'Employee SSN Change',NULL,2)
,(86,'admin@osius.com','2019-10-23 08:40:51.109','admin@osius.com','2019-12-11 09:32:24.140',18,'900671','Q9QSOVRY231019083417',NULL,1,1,1,1,'Employee Personal Name Change',NULL,31)
,(87,'admin@osius.com','2019-10-23 08:43:17.340','admin@osius.com','2019-12-12 06:46:26.422',5,'902006','EY8LOJSG231019084034',NULL,1,1,1,NULL,'Workers Comp Class',NULL,5)
,(88,'admin@osius.com','2019-10-23 08:46:16.616','admin@osius.com','2019-11-20 08:56:32.557',1,'909464','6ICQTZFG231019084545',NULL,1,1,1,NULL,'Emp Address State Change',NULL,6)
,(89,'admin@osius.com','2019-10-23 08:51:40.099','admin@osius.com','2019-12-06 11:27:42.518',1,'909464','QTRVOUBC231019084720',NULL,1,1,1,NULL,'Emp New Hire Created',NULL,26)
,(90,'admin@osius.com','2019-10-23 08:55:29.514','admin@osius.com','2019-10-23 08:55:29.514',0,'909464','RJWSCID5231019085234',NULL,1,1,1,NULL,'Duplicate SSN',NULL,25)
,(91,'admin@osius.com','2019-10-23 08:57:21.721','admin@osius.com','2019-12-16 07:32:01.465',2,'909464','JNVLTTVR231019085553',NULL,1,1,1,NULL,'Emp Inactive All Dir Dep Accs',NULL,3)
,(92,'admin@osius.com','2019-10-23 09:19:28.320','admin@osius.com','2019-12-16 07:33:54.517',6,'909464','RSUYQVHY231019091818',NULL,1,1,1,NULL,'Emp Submits DOB Change',NULL,4)
,(93,'admin@osius.com','2019-10-23 09:53:35.097','admin@osius.com','2019-12-16 07:30:45.023',5,'909464','ATJCVX30231019095232',NULL,1,1,1,NULL,'Emp Change Driver License Info',NULL,7)
,(94,'admin@osius.com','2019-10-23 10:48:11.963','admin@osius.com','2019-11-08 08:55:23.604',1,'909464','CM1NITNA231019104727',NULL,1,1,1,NULL,'W4 Change',NULL,9)
,(95,'admin@osius.com','2019-10-23 10:56:06.127','admin@osius.com','2019-12-16 07:29:26.984',10,'909464','WB9VHOOY231019105506',NULL,1,1,1,NULL,'Employee Submit I9 Form',NULL,14)
,(97,'admin@osius.com','2019-10-24 09:29:38.962','admin@osius.com','2019-12-12 11:30:56.297',19,'902006','3BDGDEIU241019092755',NULL,1,1,1,1,'Employee Name Change',NULL,8)
,(98,'admin@osius.com','2019-10-25 09:42:34.437','admin@osius.com','2019-12-16 07:27:08.676',6,'909464','OJFOSFC6251019094136',NULL,1,1,1,NULL,'Client Compensation PR Change',NULL,10)
,(99,'admin@osius.com','2019-10-25 10:19:40.571','admin@osius.com','2019-12-16 07:30:03.502',7,'909464','ZXEMEWVH251019101812',NULL,1,1,1,NULL,'Employee Terminated By Client',NULL,12)
,(100,'admin@osius.com','2019-10-25 11:13:36.638','admin@osius.com','2019-12-10 11:42:33.293',2,'902006','FO0OXPPI251019111214',NULL,1,1,1,NULL,'Payroll Due',NULL,21)
,(101,'admin@osius.com','2019-10-25 11:19:42.202','admin@osius.com','2019-12-16 07:36:24.286',2,'909464','SN7VR0OR251019111851',NULL,1,1,1,NULL,'Password Expire Notice',NULL,28)
,(102,'admin@osius.com','2019-10-25 11:37:12.671','admin@osius.com','2019-10-25 11:37:12.671',0,'909464','GCGAM4VD251019113615',NULL,1,1,1,NULL,'I-9 Expire notice',NULL,29)
,(103,'admin@osius.com','2019-10-25 11:43:54.819','admin@osius.com','2019-12-16 07:33:16.554',5,'909464','JNOR4D9D251019114245',NULL,1,1,1,NULL,'Emp Not Completed Req Onboard',NULL,26)
,(111,'admin@osius.com','2019-10-29 10:55:00.474','admin@osius.com','2019-12-12 06:53:38.968',14,'902006','SHBL0TAG291019105425',NULL,1,1,1,NULL,'Time Off Request',NULL,15)
,(113,'admin@osius.com','2019-10-30 08:41:16.605','admin@osius.com','2019-12-12 06:52:28.373',26,'902006','IQAVDQOL301019083808',NULL,1,1,1,1,'Employee Personal SSN Change',NULL,30)
,(119,'admin@osius.com','2019-11-05 06:36:22.870','admin@osius.com','2019-12-16 07:28:47.792',3,'909464','D22YFDKI051119063516',NULL,1,1,1,NULL,'Employee Submitted Document',NULL,14)
,(125,'admin@osius.com','2019-11-14 09:21:36.854','admin@osius.com','2019-12-16 07:38:18.230',7,'909464','HTIFA3MP141119091929',NULL,1,1,1,NULL,'Payroll Calculate',NULL,23)
,(127,'admin@osius.com','2019-11-14 12:01:48.283','admin@osius.com','2019-12-16 07:39:34.614',4,'909464','UXRNHHCX141119120000',NULL,1,1,1,NULL,'Timesheet Finalized',NULL,32)
,(140,'admin@osius.com','2019-12-03 06:50:40.648','admin@osius.com','2019-12-03 13:30:22.513',1,'904002','TFRVIFPC031219064924',NULL,1,1,1,NULL,'New Hire Employee Submitted',NULL,13)
,(142,'admin@osius.com','2019-12-04 07:18:16.923','admin@osius.com','2019-12-16 07:37:56.355',1,'909464','8NMNFG8F041219071556',NULL,1,1,1,0,'Payrate Type Change',NULL,35)
,(145,'admin@osius.com','2019-12-04 07:53:28.901','admin@osius.com','2019-12-13 07:09:48.611',5,'909464','EDIKUROI041219075206',NULL,1,1,1,NULL,'LOA Requested',NULL,36)
,(146,'admin@osius.com','2019-12-04 08:47:40.623','admin@osius.com','2019-12-16 07:39:05.701',8,'909464','EFARD55C041219084619',NULL,1,1,1,NULL,'Payroll Submitted',NULL,19)
,(147,'admin@osius.com','2019-12-04 08:57:19.085','admin@osius.com','2019-12-16 07:40:00.894',1,'909464','KEV61P6M041219085638',NULL,1,1,1,NULL,'Timesheet Rejected',NULL,34)
,(149,'admin@osius.com','2019-12-04 09:29:11.996','admin@osius.com','2019-12-16 07:36:52.059',3,'909464','COLOYIJT041219092842',NULL,1,1,1,NULL,'Location State Create',NULL,11)
,(150,'admin@osius.com','2019-12-04 11:12:28.160','admin@osius.com','2019-12-12 12:25:46.558',5,'909464','HK2T6XR5041219111123',NULL,1,1,1,NULL,'LOA Approved',NULL,37)
,(152,'admin@osius.com','2019-12-06 09:03:24.961','admin@osius.com','2019-12-06 09:19:38.643',1,'909464','C5PPVJYH061219090218',NULL,1,1,1,NULL,'Time Off Request Canceled',NULL,18)
,(153,'admin@osius.com','2019-12-06 09:12:14.614','admin@osius.com','2019-12-12 09:43:56.546',2,'909464','XF7S3KQW061219091112',NULL,1,1,1,NULL,'Time Off Request Approved',NULL,16)
,(154,'admin@osius.com','2019-12-06 09:17:55.412','admin@osius.com','2019-12-13 10:33:20.139',2,'909464','IY9BTV9C061219091712',NULL,1,1,1,NULL,'Time Off Request not Approved',NULL,17)
,(163,'admin@osius.com','2019-12-16 17:44:48.497','admin@osius.com','2019-12-16 17:44:48.497',0,'904002','HJKSLRPK161219174353',NULL,1,0,1,NULL,'LOA Requesst Submitted',NULL,36)
,(164,'admin@osius.com','2020-02-24 12:21:14.240','admin@osius.com','2020-02-24 12:21:14.240',0,'902185','ORQF3ZOR240220121820',NULL,1,0,1,NULL,'Location State Create',NULL,11)
,(165,'admin@osius.com','2020-02-28 13:03:57.494','admin@osius.com','2020-02-28 13:03:57.494',0,'900143','MUW9VIY4280220125354',NULL,1,0,0,NULL,'Employee Termination',NULL,11)
,(166, 'admin@osius.com', '2020-05-04 12:05:57.412', 'admin@osius.com', '2020-05-04 12:05:57.412', 0, '909464', 'WIRBSQVZ040520120457', NULL, 1, 1, 0, NULL, 'BACKGROUND_REPORT', NULL, 38)
,(167, 'admin@osius.com', '2020-05-14 20:15:35.533', 'admin@osius.com', '2020-05-14 20:15:35.533', 0, '909464', 'RUNFVTUS140520201337', NULL, 1, 1, 0, NULL, 'TAX_FILING_RULE', NULL, 39)
,(168, 'admin@osius.com', '2020-05-15 06:59:00.832', 'admin@osius.com', '2020-05-15 06:59:00.832', 0, '909464', '77PZB35L150520065732', NULL, 1, 1, 1, NULL, 'TAX_DOCUMENT_UPLOAD', NULL, 40);

ALTER sequence notification_rule_id_seq RESTART WITH 200;

-- Notification_Action
INSERT INTO notification_action (id,created_by,created_on,modified_by,modified_on,version,action_type,action_description,notif_type,notify_to,[sequence],notification_template_id,notification_rule_id) VALUES
(84,'admin@osius.com','2019-10-21 12:06:29.527','admin@osius.com','2019-12-16 07:27:46.274',4,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,70,75)
,(91,'admin@osius.com','2019-10-21 13:35:33.970','admin@osius.com','2019-12-12 06:50:38.688',9,'NOTIFY',NULL,'Push Notification','CLIENT',1,71,82)
,(92,'admin@osius.com','2019-10-21 13:35:33.978','admin@osius.com','2019-12-12 06:50:38.689',9,'APPROVE',NULL,'Push Notification','BRANCH',2,72,82)
,(93,'admin@osius.com','2019-10-21 13:35:33.981','admin@osius.com','2019-12-12 06:50:38.689',9,'NOTIFY',NULL,'Push Notification','CLIENT',3,73,82)
,(94,'admin@osius.com','2019-10-21 13:35:33.983','admin@osius.com','2019-10-22 10:47:12.625',1,'NOTIFY',NULL,'Push Notification','EMPLOYEE',1,74,NULL)
,(98,'admin@osius.com','2019-10-23 08:40:51.141','admin@osius.com','2019-12-11 09:32:24.139',18,'NOTIFY',NULL,'Push Notification','EMPLOYEE',1,75,86)
,(99,'admin@osius.com','2019-10-23 08:40:51.149','admin@osius.com','2019-12-11 09:32:24.141',18,'APPROVE',NULL,'Push Notification','CLIENT',2,76,86)
,(100,'admin@osius.com','2019-10-23 08:40:51.151','admin@osius.com','2019-12-11 09:32:24.141',18,'APPROVE',NULL,'Push Notification','BRANCH',3,92,86)
,(101,'admin@osius.com','2019-10-23 08:40:51.154','admin@osius.com','2019-12-11 09:32:24.141',18,'NOTIFY',NULL,'Push Notification','CLIENT',4,93,86)
,(102,'admin@osius.com','2019-10-23 08:43:17.342','admin@osius.com','2019-12-12 06:46:26.421',5,'NOTIFY',NULL,'Push Notification','BRANCH',NULL,79,87)
,(103,'admin@osius.com','2019-10-23 08:46:16.628','admin@osius.com','2019-11-20 08:56:32.548',1,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,80,88)
,(104,'admin@osius.com','2019-10-23 08:51:40.108','admin@osius.com','2019-12-06 11:27:42.518',1,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,81,89)
,(105,'admin@osius.com','2019-10-23 08:55:29.521','admin@osius.com','2019-10-23 08:55:29.521',0,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,82,90)
,(106,'admin@osius.com','2019-10-23 08:57:21.725','admin@osius.com','2019-12-16 07:32:01.464',2,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,85,91)
,(107,'admin@osius.com','2019-10-23 09:19:28.327','admin@osius.com','2019-12-16 07:33:54.517',6,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,86,92)
,(108,'admin@osius.com','2019-10-23 09:53:35.109','admin@osius.com','2019-12-16 07:30:45.023',5,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,87,93)
,(109,'admin@osius.com','2019-10-23 10:48:11.970','admin@osius.com','2019-11-08 08:55:23.604',1,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,88,94)
,(110,'admin@osius.com','2019-10-23 10:56:06.135','admin@osius.com','2019-12-16 07:29:26.984',10,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,89,95)
,(112,'admin@osius.com','2019-10-24 09:29:38.971','admin@osius.com','2019-12-03 13:31:20.333',8,'NOTIFY',NULL,'Push Notification','CLIENT',1,91,NULL)
,(113,'admin@osius.com','2019-10-24 09:29:38.979','admin@osius.com','2019-12-12 11:30:56.297',19,'NOTIFY',NULL,'Push Notification','CLIENT',1,76,97)
,(114,'admin@osius.com','2019-10-24 09:29:38.981','admin@osius.com','2019-12-12 11:30:56.298',19,'APPROVE',NULL,'Push Notification','BRANCH',2,92,97)
,(115,'admin@osius.com','2019-10-25 09:42:34.453','admin@osius.com','2019-12-16 07:27:08.675',6,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,94,98)
,(116,'admin@osius.com','2019-10-25 10:19:40.580','admin@osius.com','2019-12-16 07:30:03.501',7,'APPROVE',NULL,'Push Notification','CLIENT',NULL,95,99)
,(118,'admin@osius.com','2019-10-25 11:19:42.214','admin@osius.com','2019-12-16 07:36:24.285',2,'NOTIFY',NULL,'Push Notification','EMPLOYEE',NULL,97,101)
,(119,'admin@osius.com','2019-10-25 11:31:41.736','admin@osius.com','2019-10-25 11:31:41.736',0,'NOTIFY',NULL,'Push Notification','EMPLOYEE',5,98,NULL)
,(120,'admin@osius.com','2019-10-25 11:31:41.746','admin@osius.com','2019-10-25 11:31:41.746',0,'NOTIFY',NULL,'Push Notification','EMPLOYEE',1,75,NULL)
,(121,'admin@osius.com','2019-10-25 11:37:12.681','admin@osius.com','2019-10-25 11:37:12.681',0,'NOTIFY',NULL,'Push Notification','EMPLOYEE',NULL,99,102)
,(122,'admin@osius.com','2019-10-25 11:43:54.829','admin@osius.com','2019-12-16 07:33:16.554',5,'NOTIFY',NULL,'Push Notification','EMPLOYEE',NULL,100,103)
,(130,'admin@osius.com','2019-10-29 10:55:00.490','admin@osius.com','2019-12-03 13:29:30.712',4,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,106,NULL)
,(132,'admin@osius.com','2019-10-30 08:41:16.619','admin@osius.com','2019-12-03 13:31:57.316',6,'NOTIFY',NULL,'Push Notification','EMPLOYEE',1,74,NULL)
,(133,'admin@osius.com','2019-10-30 08:41:16.630','admin@osius.com','2019-12-04 11:57:13.506',19,'NOTIFY',NULL,'Push Notification','EMPLOYEE',1,74,NULL)
,(134,'admin@osius.com','2019-10-30 08:41:16.633','admin@osius.com','2019-12-03 13:35:34.576',9,'APPROVE',NULL,'Push Notification','BRANCH',2,74,NULL)
,(135,'admin@osius.com','2019-10-30 08:41:16.635','admin@osius.com','2019-12-03 13:31:57.317',6,'NOTIFY',NULL,'Push Notification','CLIENT',4,147,NULL)
,(141,'admin@osius.com','2019-11-05 06:36:22.879','admin@osius.com','2019-12-16 07:28:47.791',3,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,83,119)
,(149,'admin@osius.com','2019-11-14 09:21:36.867','admin@osius.com','2019-12-16 07:38:18.230',7,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,142,125)
,(151,'admin@osius.com','2019-11-14 12:01:48.292','admin@osius.com','2019-12-16 07:39:34.614',4,'NOTIFY',NULL,'Push Notification','BRANCH',NULL,144,127)
,(153,'admin@osius.com','2019-11-22 10:29:22.523','admin@osius.com','2019-12-12 06:53:38.968',12,'APPROVE',NULL,'Push Notification','CLIENT',NULL,108,111)
,(163,'admin@osius.com','2019-12-03 06:50:40.657','admin@osius.com','2019-12-03 13:30:22.513',1,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,160,140)
,(168,'admin@osius.com','2019-12-04 07:18:16.931','admin@osius.com','2019-12-16 07:37:56.355',1,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,162,142)
,(171,'admin@osius.com','2019-12-04 07:53:28.910','admin@osius.com','2019-12-13 07:09:48.602',5,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,164,145)
,(172,'admin@osius.com','2019-12-04 08:47:40.636','admin@osius.com','2019-12-16 07:39:05.700',8,'APPROVE',NULL,'Push Notification','CLIENT',NULL,165,146)
,(173,'admin@osius.com','2019-12-04 08:47:40.645','admin@osius.com','2019-12-16 07:39:05.701',8,'NOTIFY',NULL,'Email','EMPLOYEE',NULL,167,146)
,(174,'admin@osius.com','2019-12-04 08:57:19.093','admin@osius.com','2019-12-16 07:40:00.894',1,'NOTIFY',NULL,'Push Notification','EMPLOYEE',NULL,168,147)
,(176,'admin@osius.com','2019-12-04 09:29:12.007','admin@osius.com','2019-12-16 07:36:52.059',3,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,169,149)
,(177,'admin@osius.com','2019-12-04 11:12:28.169','admin@osius.com','2019-12-12 12:25:46.558',5,'NOTIFY',NULL,'Push Notification','EMPLOYEE',NULL,140,150)
,(178,'admin@osius.com','2019-12-04 11:49:17.131','admin@osius.com','2019-12-16 07:29:26.985',8,'APPROVE',NULL,'Push Notification','CLIENT',NULL,89,95)
,(179,'admin@osius.com','2019-12-04 11:57:13.474','admin@osius.com','2019-12-12 06:52:28.372',7,'APPROVE',NULL,'Push Notification','CLIENT',2,118,113)
,(180,'admin@osius.com','2019-12-04 11:57:13.487','admin@osius.com','2019-12-12 06:52:28.373',7,'APPROVE',NULL,'Push Notification','BRANCH',3,119,113)
,(181,'admin@osius.com','2019-12-04 11:57:13.491','admin@osius.com','2019-12-12 06:52:28.374',7,'NOTIFY',NULL,'Push Notification','CLIENT',4,147,113)
,(183,'admin@osius.com','2019-12-06 07:10:07.332','admin@osius.com','2019-12-12 06:52:28.374',4,'NOTIFY',NULL,'Push Notification','EMPLOYEE',1,74,113)
,(184,'admin@osius.com','2019-12-06 09:03:24.974','admin@osius.com','2019-12-06 09:19:38.643',1,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,109,152)
,(185,'admin@osius.com','2019-12-06 09:12:14.622','admin@osius.com','2019-12-12 09:43:56.545',2,'NOTIFY',NULL,'Push Notification','EMPLOYEE',NULL,106,153)
,(186,'admin@osius.com','2019-12-06 09:17:55.422','admin@osius.com','2019-12-13 10:33:20.139',2,'NOTIFY',NULL,'Push Notification','EMPLOYEE',NULL,170,154)
,(187,'admin@osius.com','2019-12-09 09:04:53.107','admin@osius.com','2019-12-12 11:30:56.298',7,'NOTIFY',NULL,'Push Notification','CLIENT',3,78,97)
,(188,'admin@osius.com','2019-12-10 11:42:33.279','admin@osius.com','2019-12-10 11:42:33.279',0,'NOTIFY',NULL,'Push Notification','CLIENT',NULL,96,100)
,(198,'admin@osius.com','2020-02-24 12:21:14.254','admin@osius.com','2020-02-24 12:21:14.254',0,'NOTIFY',NULL,'Email','CLIENT',NULL,169,164)
,(199,'admin@osius.com','2020-02-28 13:03:57.561','admin@osius.com','2020-02-28 13:03:57.561',0,'APPROVE',NULL,'Email','BRANCH',NULL,82,165)
,(200, 'admin@osius.com', '2020-05-04 12:05:57.422', 'admin@osius.com', '2020-05-04 12:05:57.422', 0, 'NOTIFY', NULL, 'Push Notification', 'CLIENT', NULL, 175, 166)
,(201, 'admin@osius.com', '2020-05-04 12:05:57.422', 'admin@osius.com', '2020-05-04 12:05:57.422', 0, 'NOTIFY', NULL, 'Push Notification', 'BRANCH', NULL, 175, 166)
,(202, 'admin@osius.com', '2020-05-04 12:05:57.422', 'admin@osius.com', '2020-05-04 12:05:57.422', 0, 'NOTIFY', NULL, 'Push Notification', 'VANCOUVER', NULL, 175, 166)
,(203, 'admin@osius.com', '2020-05-14 20:15:35.535', 'admin@osius.com', '2020-05-14 20:15:35.535', 0, 'NOTIFY', NULL, 'Push Notification', 'CLIENT', NULL, 176, 167)
,(204, 'admin@osius.com', '2020-05-14 20:15:35.537', 'admin@osius.com', '2020-05-14 20:15:35.537', 0, 'NOTIFY', NULL, 'Push Notification', 'BRANCH', NULL, 176, 167)
,(205, 'admin@osius.com', '2020-05-15 06:59:00.839', 'admin@osius.com', '2020-05-15 06:59:00.839', 0, 'NOTIFY', NULL, 'Push Notification', 'CLIENT', NULL, 177, 168)
,(206, 'admin@osius.com', '2020-05-15 06:59:00.845', 'admin@osius.com', '2020-05-15 06:59:00.845', 0, 'NOTIFY', NULL, 'Push Notification', 'BRANCH', NULL, 177, 168);

ALTER sequence notification_action_id_seq RESTART WITH 250;






update push_notification set message ='Your submitted {{name}} generation request (# {{report_id}}) completed. Please navigate to Report Library to view'
where notification_template_id = (select id from notification_template where name='BACKGROUND_REPORT');