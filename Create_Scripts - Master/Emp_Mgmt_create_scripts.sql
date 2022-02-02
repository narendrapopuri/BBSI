create table alternate_pay_rate (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
description varchar(255),
dt_rate double precision,
employee_code varchar(255),
ot_rate double precision,
pay_code varchar(255),
pay_rate double precision,
primary key (id));

create table assignment (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
allocation_percent varchar(255),
benefit_group_code varchar(255),
client_code varchar(30),
department_code varchar(30),
division_code varchar(30),
eeo_class varchar(255),
eeo_code varchar(255),
effective_end_date datetime2,
effective_start_date datetime2,
employee_code varchar(30),
is_1099_contractor bit,
is_bussiness_owner bit,
is_family_member bit,
is_officer bit,
is_primary bit,
is_scorp bit,
is_tax_credit_eligible bit,
job_code varchar(30),
labour_union_start_date datetime2,
occupatioon_code varchar(255),
project_code varchar(30),
shift_code varchar(30),
supervisor varchar(255),
labour_union_code varchar(30),
work_group_code varchar(30),
work_location_code varchar(30),
workers_comp_class_code varchar(255),
primary key (id));

create table batch (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
access_token varchar(255),
appr_or_resubmit_user_email varchar(255),
appr_or_resubmit_user_id varchar(255),
batch_number varchar(255),
client_code varchar(255),
description varchar(255),
is_batch_locked bit,
is_batch_locked_by_admin bit,
is_reject_allowed bit,
locked_by varchar(255),
locked_by_user varchar(255),
locked_on datetime2,
operator_user_id bigint,
previous_status varchar(255),
prism_status varchar(255),
status varchar(255),
unlocked_by varchar(255),
unlocked_on datetime2,
primary key (id));

create table batch_employee_pay_code (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
is_active bit,
pay_code varchar(255),
seq_num int,
primary key (id));

create table batch_preferences (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
fed_tax_rate_supplemental_pay double precision,
has_direct_deposits bit,
is_suppress_arrears bit,
is_suppress_benefit_adj bit,
is_suppress_benefit_calcs bit,
is_suppress_benefit_upd bit,
is_suppress_earnings bit,
override_tax_method varchar(255),
period varchar(255),
show_future_checks bit,
state_tax_rate_supplemental_pay double precision,
suppress_deductions_type varchar(255),
batch_id bigint,
primary key (id));

create table change_log (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
current_value varchar(255),
effective_end_date datetime2,
effective_start_date datetime2,
employee_code varchar(30),
field_name varchar(255),
new_value varchar(255),
notes varchar(255),
reason_code varchar(255),
primary key (id));

create table contacts (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
contact_type varchar(255),
email varchar(255),
first_name varchar(255),
is_active bit,
is_primary bit,
last_name varchar(255),
middle_name varchar(255),
mobile varchar(255),
source_id bigint,
source_type varchar(255),
title varchar(255),
work_phone varchar(255),
work_phone_ext varchar(255),
primary key (id));

create table deduction_code (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
calculation_method varchar(255),
client_code varchar(255),
code varchar(255),
description varchar(255),
ext_code varchar(255),
include_in_paystub varchar(255),
is_active bit,
is_arrears_allowed bit,
is_cutback_required bit,
is_mandatory bit,
paystub_name varchar(255),
status_date datetime2,
type varchar(255),
primary key (id));

create table document (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
auth_name varchar(255),
doc_number varchar(255) not null,
doc_type varchar(255),
document_path varchar(255),
expiry_date date,
has_expiry_date bit,
issuing_authority varchar(255),
renew_date date,
source_id bigint,
source_type varchar(255),
status varchar(255),
title varchar(255) not null,
upload_date datetime2,
primary key (id));

create table emp_skill (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
certification_date datetime2,
client_code varchar(30) not null,
comments varchar(1024),
emp_code varchar(30) not null,
expiration_date datetime2,
is_active bit,
name varchar(255) not null,
proficiency int not null,
skill_code varchar(255) not null,
primary key (id));

create table employee_batch_assoc (
id bigint not null,
employee_code varchar(255),
batch_id bigint,
primary key (id));

create table employee_email_view (
id int not null,
client_code varchar(255),
code varchar(255),
user_email varchar(255),
work_email varchar(255),
primary key (id));

create table employee_payment (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
amount double precision,
client_code varchar(255),
description varchar(255),
employee_code varchar(255),
is_active bit,
is_recurring bit,
pay_code varchar(255),
period varchar(255),
start_date date,
stop_date date,
primary key (id));

create table employee_sign_details (
id bigint not null,
agreement_id varchar(255),
approver_employee_code varchar(30),
client_code varchar(30) not null,
client_signed bit,
employee_code varchar(30),
employee_signed bit,
sign_url varchar(255),
primary key (id));

create table leave_of_absence (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(30),
dept_code varchar(30),
employee_code varchar(30),
employee_name varchar(255),
ext_code varchar(30),
first_name varchar(255),
is_active bit,
is_paid_leave bit,
last_name varchar(255),
leave_type varchar(30),
notes varchar(255),
pay_status varchar(30),
plan_end_date date,
plan_return_date date,
reason_code varchar(30),
reason_desc varchar(255),
request_date date,
start_date date,
status varchar(255),
supervisor varchar(255),
primary key (id));

create table pay_code (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
bill_rate double precision,
calculation_method varchar(255),
client_code varchar(255),
code varchar(255),
compensation_type varchar(255),
description varchar(255),
ext_code varchar(255),
is_ts_pay_code bit,
pay_class varchar(255),
pay_rate double precision,
type varchar(255),
primary key (id));

create table pay_code_hours (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
is_display varchar(255),
no_of_hours double precision,
pay_code varchar(255),
seq_num int,
time_entry_id bigint,
primary key (id));

create table pay_code_pay_code_hours (
pay_code_id bigint not null,
pay_code_hours_id bigint not null);

create table pay_group (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
call_in_date datetime2,
client_code varchar(255),
code varchar(255),
delivery_date datetime2,
description varchar(255),
ext_code varchar(255),
is_active bit,
next_pay_date datetime2,
pay_period_no int,
primary key (id));

create table pay_roll_status (
id bigint not null,
exist_status varchar(255),
upcoming_status varchar(255),
primary key (id));

create table payroll_combinations (
id bigint not null,
pay_roll_features varchar(255),
role varchar(255),
primary key (id));

create table payroll_employee_filter (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
batch_id int,
department_code varchar(255),
division_code varchar(255),
emp_type varchar(255),
location_code varchar(255),
occupation_code varchar(255),
operator_code varchar(255),
project_code varchar(255),
shift_code varchar(255),
union_code varchar(255),
wcc_code varchar(255),
work_group varchar(255),
primary key (id));

create table payroll_workflow_status (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
approve_reject_email varchar(255),
approve_reject_user_id varchar(255),
batch_number varchar(255),
client_code varchar(255),
current_status varchar(255),
operator_email varchar(255),
previous_status varchar(255),
user_id varchar(255),
primary key (id));

create table time_entry (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
batch_id bigint,
currency double precision,
disable_summary bit,
employee_code varchar(255),
pay_rate double precision,
payments double precision,
profile_pic_url varchar(255),
primary key (id));

create table time_entry_detail (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
dept_code varchar(255),
division_code varchar(255),
fringe_per_hour double precision,
hourly_rate double precision,
job_code varchar(255),
location_code varchar(255),
other_deducts_per_hour double precision,
pay_code_type varchar(255),
project_code varchar(255),
shift_code varchar(255),
skill_code varchar(255),
total_hours double precision,
time_entry_id bigint,
primary key (id));

create table time_entry_detail_config (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
type varchar(255),
primary key (id));

create table time_entry_detail_config_assoc (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
time_entry_detail_config_id bigint,
type varchar(255),
time_entry_id bigint,
primary key (id));

create table time_entry_init_summary (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
batch_number varchar(255),
client_code varchar(255),
tab_json varchar(8000),
tab_name varchar(255),
primary key (id));

create table time_off_request (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
employee_code varchar(255),
employee_email varchar(255),
end_date date,
time_off_hours double precision,
leave_id varchar(255),
notes varchar(1024),
plan_name varchar(255),
register_type varchar(255),
request_id varchar(255),
start_date date,
status varchar(255),
supervisor_id varchar(255),
supervisor_email varchar(255),
time_off_request_date date,
primary key (id));

create table timesheet_detail_default (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(255),
dept_code varchar(255),
division_code varchar(255),
employee_code varchar(255),
job_code varchar(255),
location_code varchar(255),
pay_code_type varchar(255),
project_code varchar(255),
serial_number bigint,
shift_code varchar(255),
skill_code varchar(255),
primary key (id));

create table user_pay_code (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
calculation_method varchar(255),
client_code varchar(255),
description varchar(255),
is_active varchar(255),
operator_code varchar(255),
pay_code varchar(255),
seq_no int,
primary key (id));

create table week_details (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
week_day varchar(255),
week_number int,
time_entry_detail_id bigint,
primary key (id));

create table week_hours (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
sync bit,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
hours double precision,
log_date varchar(255),
week_details_id bigint,
primary key (id));

alter table alternate_pay_rate add constraint UKlcdaig6o5wsi40chksg9vqxb5 unique (client_code, employee_code, pay_code);

alter table employee_payment add constraint UK35xkd5smn1rokpr1xfnck0bc5 unique (client_code, employee_code, pay_code);

alter table employee_sign_details add constraint UKpo6jnrflqkresknyaouyuwgpr unique (employee_code, client_code);

alter table pay_code_pay_code_hours add constraint UK_k8xyv3gp2tc80kti2y9exwpgw unique (pay_code_hours_id);

alter table payroll_employee_filter add constraint UK_qw202bs9xu8p2is6bn53eaox2 unique (operator_code);

alter table timesheet_detail_default add constraint UKjrjllnhor46tl8ve6ecbv3jpp unique (pay_code_type, client_code, employee_code, project_code, job_code, division_code, location_code, shift_code, skill_code, dept_code);

create index IDXkb6ix07rsu01m4j0em6pri7bl on user_pay_code (operator_code, is_active);

create index IDXntjfbrhuakjtltscr08ywrov3 on user_pay_code (operator_code, client_code, is_active);

create sequence alt_pay_rate_id_seq start with 1 increment by 1;

create sequence assignment_id_seq start with 1 increment by 1;

create sequence batch_id_seq start with 1 increment by 1;

create sequence batch_pay_id_seq start with 1 increment by 1;

create sequence batch_preferences_id_seq start with 1 increment by 1;

create sequence changelog_id_seq start with 1 increment by 1;

create sequence contacts_seq start with 1 increment by 1;

create sequence deduction_code_id_seq start with 1 increment by 1;

create sequence document_id_seq start with 1 increment by 1;

create sequence emp_skill_id_seq start with 1 increment by 1;

create sequence empbatch_id_seq start with 1 increment by 1;

create sequence employee_sign_gen start with 1 increment by 50;

create sequence leave_of_absence_id_seq start with 1 increment by 1;

create sequence pay_code_hours_id_seq start with 1 increment by 1;

create sequence pay_code_id_seq start with 1 increment by 1;

create sequence pay_group_id_seq start with 1 increment by 1;

create sequence pay_roll_status_id_seq start with 1 increment by 1;

create sequence payment_id_seq start with 1 increment by 1;

create sequence payroll_combinations_id_seq start with 1 increment by 1;

create sequence payroll_employee_filter_id_seq start with 1 increment by 1;

create sequence pay_roll_workflow_status_id_seq start with 1 increment by 1;

create sequence time_entry_detail_config_assoc_id_seq start with 1 increment by 1;

create sequence time_entry_detail_config_id_seq start with 1 increment by 1;

create sequence time_entry_detail_id_seq start with 1 increment by 1;

create sequence time_entry_id_seq start with 1 increment by 1;

create sequence time_entry_init_summary_id_seq start with 1 increment by 1;

create sequence time_off_request_id_seq start with 1 increment by 1;

create sequence timesheet_det_def_id_seq start with 1 increment by 1;

create sequence user_pay_code_id_seq start with 1 increment by 1;

create sequence week_details_id_seq start with 1 increment by 1;

create sequence week_hours_id_seq start with 1 increment by 1;

alter table batch_preferences add constraint FK43mktvxo0oru97hv5vvu2ifeb foreign key (batch_id) references batch;

alter table employee_batch_assoc add constraint FKawdlpxcsdtglm7y4u7s52y2ix foreign key (batch_id) references batch;

alter table pay_code_hours add constraint FKgefax3awad0cdlmgyn13p5ywr foreign key (time_entry_id) references time_entry;

alter table pay_code_pay_code_hours add constraint FKd3rr1m3oe8lxwisbb6f7vqdv7 foreign key (pay_code_hours_id) references pay_code_hours;

alter table pay_code_pay_code_hours add constraint FKbxfu1b5v29ni8dprnjnyu8idn foreign key (pay_code_id) references pay_code;

alter table time_entry_detail add constraint FK42b3cf7907hnsjjtmi63hbig1 foreign key (time_entry_id) references time_entry;

alter table time_entry_detail_config_assoc add constraint FKn3vjmoygxt7o22rmrmvwr0j3o foreign key (time_entry_id) references time_entry;

alter table week_details add constraint FKgja5ulngaqsel490j4ukk8ua4 foreign key (time_entry_detail_id) references time_entry_detail;

alter table week_hours add constraint FK6bcelu26o736xmm9c7x4ttqur foreign key (week_details_id) references week_details;


-- pay_roll_status
INSERT INTO pay_roll_status (id,exist_status,upcoming_status) VALUES
(1,'INITIATED','INPROGRESS,SUBMITTED FOR APPROVAL,APPROVAL INPROGRESS,CALCULATE INPROGRESS,FINALIZE INPROGRESS,FINALIZE CALCULATE INPROGRESS'),
(2,'INPROGRESS','INPROGRESS,SUBMITTED FOR APPROVAL'),
(3,'SUBMITTED FOR APPROVAL','APPROVAL INPROGRESS,CALCULATE INPROGRESS,APPROVAL REJECTED,FINALIZE INPROGRESS,FINALIZE CALCULATE INPROGRESS,FINALIZE REJECTED'),
(4,'APPROVAL INPROGRESS','APPROVAL INPROGRESS,CALCULATE INPROGRESS,APPROVAL REJECTED'),
(5,'CALCULATE INPROGRESS ','APPROVAL INPROGRESS,CALCULATE INPROGRESS,APPROVAL REJECTED,SUBMITTED FOR FINALIZE'),
(6,'APPROVAL REJECTED','INPROGRESS,SUBMITTED FOR APPROVAL'),
(7,'SUBMITTED FOR FINALIZE','FINALIZE INPROGRESS,FINALIZE CALCULATE INPROGRESS,FINALIZE REJECTED,FINALIZED'),
(8,'FINALIZE INPROGRESS','FINALIZE INPROGRESS,FINALIZE CALCULATE INPROGRESS,FINALIZE REJECTED'),
(9,'FINALIZE CALCULATE INPROGRESS ','FINALIZE INPROGRESS,FINALIZE CALCULATE INPROGRESS,FINALIZE REJECTED,FINALIZED'),
(10,'FINALIZE REJECTED','APPROVAL INPROGRESS,SUBMITTED FOR FINALIZE,INPROGRESS,SUBMITTED FOR APPROVAL,APPROVAL REJECTED,CALCULATE INPROGRESS'),
(11,'FINALIZED','OTHER');

ALTER SEQUENCE pay_roll_status_id_seq RESTART WITH 15;

-- time_entry_detail_config
INSERT INTO time_entry_detail_config (id, created_by, created_on, sync, modified_by, modified_on, version, client_code, [type]) values
(1, 'anonymousUser', '2019-07-15 07:05:44', 0, 'anonymousUser', '2019-07-15 07:05:44', 0, NULL, 'Project'),
(2, 'anonymousUser', '2019-07-15 07:05:44', 0, 'anonymousUser', '2019-07-15 07:05:44', 0, NULL, 'Workers'' Comp Class'),
(3, 'anonymousUser', '2019-07-15 07:05:44', 0, 'anonymousUser', '2019-07-15 07:05:44', 0, NULL, 'Division'),
(4, 'anonymousUser', '2019-07-15 07:05:44', 0, 'anonymousUser', '2019-07-15 07:05:44', 0, NULL, 'Department'),
(5, 'anonymousUser', '2019-07-15 07:05:44', 0, 'anonymousUser', '2019-07-15 07:05:44', 0, NULL, 'Worksite Location'),
(6, 'anonymousUser', '2019-07-15 07:05:44', 0, 'anonymousUser', '2019-07-15 07:05:44', 0, NULL, 'Shift'),
(7, 'anonymousUser', '2019-07-15 07:05:44', 0, 'anonymousUser', '2019-07-15 07:05:44', 0, NULL, 'Classification'),
(8, 'anonymousUser', '2019-07-15 07:05:44', 0, 'anonymousUser', '2019-07-15 07:05:44', 0, NULL, 'Dates');

ALTER SEQUENCE time_entry_detail_config_id_seq RESTART WITH 10;

