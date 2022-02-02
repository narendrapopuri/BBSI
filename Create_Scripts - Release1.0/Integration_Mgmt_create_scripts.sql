create table client_config (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(255) not null,
json_key varchar(255),
type varchar(255) not null,
unique_code varchar(255),
value varchar(8000),
primary key (id));

create table employee_config (
id bigint not null,
created_by varchar(255) not null,
created_on datetime2 not null,
modified_by varchar(255) not null,
modified_on datetime2 not null,
version bigint,
client_code varchar(255) not null,
employee_code varchar(255) not null,
json_key varchar(255),
type varchar(255) not null,
unique_code varchar(255),
value varchar(4000),
primary key (id));

create table json_ui_mapping (
id bigint not null,
json_key varchar(255),
type varchar(255) not null,
ui_field varchar(255),
primary key (id));

create table master_config (
id bigint not null,
aplicable_for varchar(255),
json_key varchar(255),
type varchar(255) not null,
primary key (id));

alter table json_ui_mapping add constraint UK2n90g5j7jbi9beu2qf10oyudc unique (type, json_key);

create sequence emploiyee_config_id_seq start with 1 increment by 1;

create sequence master_config_id_seq start with 1 increment by 1;


-- master_config
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(1, 'GET,PUT', '$.policy_number', 'INSURANCE_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(6, 'GET,PUT', '$.bank_account[0].account_type', 'ACH');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(13, 'GET,PUT', '$.auto_pay_hours', 'COMPENSATION');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(14, 'GET,PUT', '$.is_auto_pay', 'COMPENSATION');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(18, 'GET,POST,PUT', '$.certified_pay_rates', 'PROJECT_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(19, 'GET,POST,PUT', '$.effective_date', 'COMPENSATION');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(20, 'GET,POST,PUT', '$.percentage_change', 'COMPENSATION');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(21, 'GET,POST,PUT', '$.amount_change', 'COMPENSATION');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(22, 'GET,POST,PUT', '$.change_reason', 'COMPENSATION');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(25, 'GET,POST,PUT', '$.description', 'RECURING_DEDUCTION');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(26, 'GET,POST,PUT', '$.benefit_plan_id', 'RECURING_DEDUCTION');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(27, 'GET,POST,PUT', '$.loan_or_garnish_id', 'RECURING_DEDUCTION');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(28, 'GET,POST,PUT', '$.contractor_type', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(29, 'GET,POST,PUT', '$.project_or_contractor_number', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(30, 'GET,POST,PUT', '$.signatory_name', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(31, 'GET,POST,PUT', '$.signatory_title', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(34, 'GET,POST,PUT', '$.project_address', 'PROJECT_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(39, 'GET,POST,PUT', '$.other_parameters.union', 'LOCATION_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(40, 'GET,POST,PUT', '$.workers_compensation.bank_name', 'LOCATION_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(41, 'GET,POST,PUT', '$.policy_number', 'LOCATION_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(43, 'GET', '$.profile_image', 'CLIENT_EMPLOYEES_FILTER');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(44, 'GET,POST,PUT', '$.is_disabled', 'DEPENDENT_BENEFICIARY');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(45, 'GET,POST,PUT', '$.is_emp_address', 'DEPENDENT_BENEFICIARY');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(46, 'GET,POST,PUT', '$.contractor_address', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(47, 'GET,POST,PUT', '$.certified_pay_rate', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(48, 'GET,POST,PUT', '$.contractor_name', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(49, 'GET,POST,PUT', '$.contract_agency', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(50, 'GET,POST,PUT', '$.is_fringe_paidtoapprovedplans', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(51, 'GET,POST,PUT', '$.is_fringe_benefitspaidincash', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(52, 'GET,POST,PUT', '$.is_reporthourspaid', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(53, 'GET,POST,PUT', '$.is_california_xml', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(54, 'GET,POST,PUT', '$.ca_dlse_project_id_number_field', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(55, 'GET,POST,PUT', '$.ca_contract_id_field', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(56, 'GET,POST,PUT', '$.ca_awarding_body_id_number_field', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(57, 'GET,POST,PUT', '$.contractor_email_address', 'PROJECT_INFO');
--INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(58, 'GET,POST,PUT', '$.statement_of_nonperformance', 'PROJECT_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(59, 'GET,POST,PUT', '$.login_account_user_id', 'PERSONAL_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(65, 'GET,POST,PUT', '$.contingent_precedence', 'DEPENDENT_BENEFICIARY');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(66, 'GET,POST,PUT', '$.is_same_as_address', 'MAILING_ADDRESS');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(68, 'GET,PUT,POST', '$.description', 'SKILL_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(69, 'GET,PUT,POST', '$.group', 'SKILL_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(70, 'GET,PUT,POST', '$.group_description', 'SKILL_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(73, 'GET,POST,PUT', '$.description', 'PAYROLL_HOLIDAYS_LIST');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(74, 'GET,POST,PUT', '$.observing', 'PAYROLL_HOLIDAYS_LIST');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(75, 'GET,POST,PUT', '$.number_type', 'CLIENT_PRIMARY_CONTACTS');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(76, 'GET,PUT', '$.county', 'MAILING_ADDRESS');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(78, 'GET,PUT,POST', '$.profile_image', 'EMPLOYEE_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(79, 'GET,PUT,POST', '$.i9_type', 'EMPLOYMENT_ELIGIBILITY_DOCUMENTATION');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(80, 'PUT,POST', '$.next_review_date', 'PERFORMANCE_REVIEW');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(81, 'GET,PUT,POST', '$.merit_type', 'PERFORMANCE_REVIEW');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(82, 'GET,PUT,POST', '$.merit_increase', 'PERFORMANCE_REVIEW');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(83, 'GET,PUT,POST', '$.merit_percent', 'PERFORMANCE_REVIEW');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(84, 'GET,PUT,POST', '$.review_type', 'PERFORMANCE_REVIEW');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(85, 'GET,PUT,POST', '$.other_review_type', 'PERFORMANCE_REVIEW');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(86, 'GET,PUT', '$.occupation', 'ASSIGNMENT_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(88, 'GET,PUT', '$.is_time_net', 'CLIENT_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(89, 'GET,PUT', '$.is_time_net_employee', 'EMPLOYMENT_INFO');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(90, 'GET,PUT,POST', '$.group', 'SKILL');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(91, 'GET,POST', '$.memo', 'GROSS_TO_NET');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(92, 'GET', '$.memo', 'MANUAL_CHECKS');
INSERT INTO master_config (id, aplicable_for, json_key, [type]) VALUES(95, 'GET', '$.profile_image', 'CLIENT_EMPLOYEES_FILTER_UQ');

ALTER sequence master_config_id_seq RESTART WITH 100;

-- json_ui_mapping
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(1, '$.division_name', 'DIVISION_INFO', 'Division Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(2, '$.eligible_benefits.benifit', 'DIVISION_INFO', 'Benefit Test');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(3, '$.eligible_benefits', 'DIVISION_INFO', 'Eligible Benefits');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(4, '$.per_diem_percent', 'DEPARTMENT_INFO', 'Per Diem Percent');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(5, '$.union_eligible_days', 'DEPARTMENT_INFO', 'Union Eligible Days');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(6, '$.eligible_benefits', 'DEPARTMENT_INFO', 'Eligible Benefits');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(7, '$.is_certified', 'PROJECT_INFO', 'Certified');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(8, '$.track_classifications', 'PROJECT_INFO', 'Track Classifications');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(9, '$.contractor_type', 'PROJECT_INFO', 'Contractor Type');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(10, '$.contractor_name', 'PROJECT_INFO', 'Contractor Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(11, '$.project_or_contractor_number', 'PROJECT_INFO', 'Project or Contractor Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(12, '$.signatory_name', 'PROJECT_INFO', 'Signatory Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(13, '$.signatory_title', 'PROJECT_INFO', 'Signatory Title');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(14, '$.contract_agency', 'PROJECT_INFO', 'Contract Agency');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(15, '$.project_start', 'PROJECT_INFO', 'Project Start');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(16, '$.project_end', 'PROJECT_INFO', 'Project End');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(17, '$.certified_pay_rates', 'PROJECT_INFO', 'Certified Pay Rate');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(18, '$.certified_pay_rates.work_comp_class', 'PROJECT_INFO', 'Work Comp Class');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(19, '$.certified_pay_rates.work_comp_title', 'PROJECT_INFO', 'Title');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(20, '$.certified_pay_rates.base_rate', 'PROJECT_INFO', 'Base Rate');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(21, '$.certified_pay_rates.fringe_rate', 'PROJECT_INFO', 'fringe_rate');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(22, '$.certified_pay_rates.prevailing_rate', 'PROJECT_INFO', 'Prevailing Rate');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(23, '$.project_address', 'PROJECT_INFO', 'Project Address');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(24, '$.project_address.address_line_1', 'PROJECT_INFO', 'Address Line 1');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(25, '$.project_address.address_line_2', 'PROJECT_INFO', 'Address Line 2');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(26, '$.project_address.zipcode', 'PROJECT_INFO', 'Zip Code');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(27, '$.project_address.country', 'PROJECT_INFO', 'County');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(28, '$.project_address.city', 'PROJECT_INFO', 'City ');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(29, '$.project_address.state', 'PROJECT_INFO', 'State');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(30, '$.contractor_address', 'PROJECT_INFO', 'Contractor Address');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(31, '$.contractor_address.address_line1', 'PROJECT_INFO', 'Address Line 1');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(32, '$.contractor_address.address_line2', 'PROJECT_INFO', 'Address Line 2');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(33, '$.contractor_address.zipcode', 'PROJECT_INFO', 'Zip Code');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(34, '$.contractor_address.county', 'PROJECT_INFO', 'County');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(35, '$.contractor_address.city', 'PROJECT_INFO', 'City');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(36, '$.contractor_address.state', 'PROJECT_INFO', 'State');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(37, '$.is_fringe_paidtoapprovedplans', 'PROJECT_INFO', 'Fringe Paid to Approved Plans');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(38, '$.is_fringe_benefitspaidincash', 'PROJECT_INFO', 'Fringe Benefits Paid in Cash');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(39, '$.is_reporthourspaid', 'PROJECT_INFO', 'Report Hours Paid');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(40, '$.is_california_xml', 'PROJECT_INFO', 'California XML');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(41, '$.ca_dlse_project_id_number_field', 'PROJECT_INFO', 'CA DLSE Project ID Number Field');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(42, '$.ca_contract_id_field', 'PROJECT_INFO', 'CA Contract ID Field');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(43, '$.ca_awarding_body_id_number_field', 'PROJECT_INFO', 'CA Awarding Body ID Number Field');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(44, '$.contractor_email_address', 'PROJECT_INFO', 'Contractor Email Address ');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(45, '$.statement_of_nonperformance', 'PROJECT_INFO', 'Statement of Nonperformance');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(46, '$.status', 'PROJECT_INFO', 'Status');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(47, '$.state', 'SHIFT_INFO', 'State');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(48, '$.differential_method', 'SHIFT_INFO', 'Differential Method');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(49, '$.differential_amount', 'SHIFT_INFO', 'Differential Amount');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(50, '$.is_calc_OT_differential_on_straight_rate', 'SHIFT_INFO', 'Calc OT Differential on Straight Rate');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(51, '$.include_in_pay_rate', 'SHIFT_INFO', 'Include in Pay Rate');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(52, '$.regular_pay_type', 'SHIFT_INFO', 'Regular Pay Type');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(53, '$.overtime_pay_type', 'SHIFT_INFO', 'Overtime Pay Type');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(54, '$.overtime_pay_factor', 'SHIFT_INFO', 'Overtime Pay Factor');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(55, '$.status', 'LOCATION_INFO', 'Status');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(56, '$.status_effective_date', 'LOCATION_INFO', 'Status Effective Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(57, '$.address_line_1', 'LOCATION_INFO', 'Address Line 1');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(58, '$.address_line_2', 'LOCATION_INFO', 'Address Line 2 ');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(59, '$.zip_code', 'LOCATION_INFO', 'Zip Code');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(60, '$.county', 'LOCATION_INFO', 'County');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(61, '$.city', 'LOCATION_INFO', 'City');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(62, '$.state', 'LOCATION_INFO', 'State');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(63, '$.workers_comp_state', 'LOCATION_INFO', 'Workers Comp State');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(64, '$.Worksite_location_minimum_wage', 'LOCATION_INFO', 'Worksite Location Minimum Wage');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(65, '$.Worksite_location_minimum_wage.minimum_wage_rate_date', 'LOCATION_INFO', 'Minimum WageRate Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(66, '$.Worksite_location_minimum_wage.minimum_wage', 'LOCATION_INFO', 'Minimum Wage');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(67, '$.Worksite_location_minimum_wage.minimum_cash_wage', 'LOCATION_INFO', 'Minimum Cash Wage');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(68, '$.Worksite_location_minimum_wage.minimum_wage_minor', 'LOCATION_INFO', 'Minimum Wage Minor');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(69, '$.Worksite_location_minimum_wage.minimum_cash_wage_minor', 'LOCATION_INFO', 'Minimum Cash Wage Minor');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(70, '$.this_location_classifies_a_minor_as_anyone_under_the_age_of', 'LOCATION_INFO', 'This location classifies a minor as anyone under the age of');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(71, '$.eligible_benefits', 'LOCATION_INFO', 'Eligible Benefits');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(72, '$.primary_contacts', 'LOCATION_INFO', 'Primary Contacts');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(73, '$.contact_name', 'LOCATION_INFO', 'Contact Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(74, '$.contact_title', 'LOCATION_INFO', 'Contact Title');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(75, '$.telephone_number', 'LOCATION_INFO', 'Telephone Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(76, '$.extension', 'LOCATION_INFO', 'Extension');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(77, '$.number_type', 'LOCATION_INFO', 'Number Type');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(78, '$.email_address', 'LOCATION_INFO', 'Email Address ');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(79, '$.workers_compensation', 'LOCATION_INFO', 'workers_compensation');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(80, '$.workers_compensation.routing_transit_number', 'LOCATION_INFO', 'Routing Transit Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(81, '$.workers_compensation.bank_name', 'LOCATION_INFO', 'Bank Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(82, '$.workers_compensation.bank_account_number', 'LOCATION_INFO', 'Bank Account Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(83, '$.workers_compensation.account_type', 'LOCATION_INFO', 'Account Type');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(84, '$.other_parameters', 'LOCATION_INFO', 'Other Parameters');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(85, '$.other_parameters.union', 'LOCATION_INFO', 'Union');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(86, '$.other_parameters.eeo_unit_number', 'LOCATION_INFO', 'EEO Unit Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(87, '$.other_parameters.default_department', 'LOCATION_INFO', 'Default Department');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(88, '$.other_parameters.default_division', 'LOCATION_INFO', 'Default Division');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(89, '$.other_parameters.default_project', 'LOCATION_INFO', 'Default Project');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(90, '$.bank_account.bank_account_number', 'ACH', 'Bank Account Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(91, '$.bank_account.bank_name', 'ACH', 'Bank Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(92, '$.bank_account.routing_transit_number', 'ACH', 'Routing Transit Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(93, '$.bank_account.ach_status', 'ACH', 'ACH Status');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(94, '$.federal_tax_identification_number', 'TAX', 'Tax Identification Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(95, '$.tax_setups', 'TAX', 'State Tax Information');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(96, '$.tax_setups.misc_rate_1', 'TAX', 'Misc Rate 1');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(97, '$.tax_setups.state', 'TAX', 'State');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(98, '$.tax_setups.state_tax_rate', 'TAX', 'State Tax Rate');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(99, '$.tax_setups.tax_identification_number', 'TAX', 'Tax Identification Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(100, '$.tax_setups.effective_date', 'TAX', 'Effective Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(101, '$.workers_comp_policy_effective_date', 'INSURANCE_INFO', 'Workers Comp Policy Effective Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(102, '$.workers_comp_policy', 'LOCATION_INFO', 'Workers Comp Policy');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(103, '$.wc_class_codes.valid_workers_comp_class_codes', 'LOCATION_INFO', 'Workers Comp Class Code');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(104, '$..wc_class_codes.description', 'LOCATION_INFO', 'Workers’ Comp Class Title');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(105, '$.first_name', 'PERSONAL_INFO', 'First Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(106, '$.last_name', 'PERSONAL_INFO', 'Last Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(107, '$.middle_name', 'PERSONAL_INFO', 'Middle Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(108, '$.preferred_name', 'PERSONAL_INFO', 'Preferred Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(109, '$.social_security_number', 'PERSONAL_INFO', 'Social Security Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(110, '$.work_email_address', 'PERSONAL_INFO', 'Work Email Address');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(111, '$.bbsi_assigned_employee_id', 'PERSONAL_INFO', 'BBSI Assigned Employee ID');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(112, '$.preferred_language', 'PERSONAL_INFO', 'Preferred Language');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(113, '$.date_of_birth', 'PERSONAL_INFO', 'Date of Birth');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(114, '$.age', 'PERSONAL_INFO', 'Age');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(115, '$.home_telephone_number', 'CONTACT_INFO', 'Home Telephone Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(116, '$.mobile_phone_number', 'CONTACT_INFO', 'Mobile Phone Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(117, '$.work_telephone_number', 'CONTACT_INFO', 'Work Telephone Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(118, '$.personal_email_address', 'CONTACT_INFO', 'Personal Email Address');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(119, '$.address_line1', 'RESIDENTIAL_ADDRESS', 'Address Line 1');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(120, '$.address_line2	', 'RESIDENTIAL_ADDRESS', 'Address Line 2');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(121, '$.zip_code', 'RESIDENTIAL_ADDRESS', 'Zip Code');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(122, '$.school_district', 'RESIDENTIAL_ADDRESS', 'School District');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(123, '$.county', 'RESIDENTIAL_ADDRESS', 'County');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(124, '$.city', 'RESIDENTIAL_ADDRESS', 'City');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(125, '$.state', 'RESIDENTIAL_ADDRESS', 'State');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(126, '$.is_unicorporated_area', 'RESIDENTIAL_ADDRESS', 'Unincorporated Area');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(127, '$.address_line1', 'MAILING_ADDRESS', 'Address Line 1');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(128, '$.address_line2	', 'MAILING_ADDRESS', 'Address Line 2');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(129, '$.zip_code', 'MAILING_ADDRESS', 'Zip Code');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(130, '$.school_district', 'MAILING_ADDRESS', 'School District');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(131, '$.county', 'MAILING_ADDRESS', 'County');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(132, '$.city', 'MAILING_ADDRESS', 'City');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(133, '$.state', 'MAILING_ADDRESS', 'State');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(134, '$.is_unicorporated_area', 'MAILING_ADDRESS', 'Unincorporated Area');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(135, '$.is_same_as_address', 'MAILING_ADDRESS', 'Same as Residential');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(136, '$.ethnicity', 'VETERAN_STATUS', 'Race/Ethnicity');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(137, '$.is_handicapped', 'VETERAN_STATUS', 'Handicapped');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(138, '$.is_blind', 'VETERAN_STATUS', 'Blind');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(139, '$.veteran', 'VETERAN_STATUS', 'Veteran');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(140, '$.is_vietnam_veteran', 'VETERAN_STATUS', 'Vietnam Veteran');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(141, '$.is_disable_veteran', 'VETERAN_STATUS', 'Special Disabled');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(142, '$.is_service_veteran', 'VETERAN_STATUS', 'Service Medal Veteran');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(143, '$.is_separated_veteran', 'VETERAN_STATUS', 'Newly Separated Veteran');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(144, '$.is_protected_veteran', 'VETERAN_STATUS', 'Other Protected Veteran');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(145, '$.is_active_duty_wartime', 'VETERAN_STATUS', 'Active Duty Wartime or Campaign Badge Veteran');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(146, '$.contact_name', 'EMERGENCY_CONTACT', 'Contact Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(147, '$.telephone_number', 'EMERGENCY_CONTACT', 'Telephone Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(148, '$.contact_relation', 'EMERGENCY_CONTACT', 'Contact Relation');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(149, '$.driver_license_number', 'DRIVING_LICENCE', 'Drivers License Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(150, '$.license_class', 'DRIVING_LICENCE', 'License Class');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(151, '$.expiration_date', 'DRIVING_LICENCE', 'Expiration Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(152, '$.state', 'DRIVING_LICENCE', 'State');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(153, '$.license_plate_number', 'DRIVING_LICENCE', 'License Plate Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(154, '$.employee_status', 'EMPLOYMENT_INFO', 'Employee Status');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(155, '$.employee_type', 'EMPLOYMENT_INFO', 'Employee Type');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(156, '$.status_effective_date', 'EMPLOYMENT_INFO', 'Status Effective Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(157, '$.last_hire_date', 'EMPLOYMENT_INFO', 'Last Hire Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(158, '$.hire_date', 'EMPLOYMENT_INFO', 'Hire Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(159, '$.type_effective_date', 'EMPLOYMENT_INFO', 'Type Effective Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(160, '$.company_employee_number', 'EMPLOYMENT_INFO', 'Company Employee Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(161, '$.Clock Number', 'EMPLOYMENT_INFO', 'Clock Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(162, '$.current_employee_status', 'STATUS_TYPE', 'Current Employee Status');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(163, '$.new_employee_status', 'STATUS_TYPE', 'New Employee Status');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(164, '$.reason_code', 'STATUS_TYPE', 'Reason Code');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(165, '$.status_effective_date', 'STATUS_TYPE', 'Effective Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(166, '$.Current Employee Status', 'TERMINATE', 'Current Employee Status');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(167, '$.new_employee_status', 'TERMINATE', 'New Employee Status');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(168, '$.reason_code', 'TERMINATE', 'Reason Code');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(169, '$.status_effective_date', 'TERMINATE', 'Status Effective Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(170, '$.details', 'TERMINATE', 'Details ');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(171, '$.direct_deposit_inactive_date', 'TERMINATE', 'Direct Deposit Inactive Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(172, '$.access_end_date', 'TERMINATE', 'Employee Portal Access End Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(173, '$.is_rehire_ok', 'TERMINATE', 'Eligible for Rehire?');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(174, '$.work_site_location', 'ASSIGNMENT_INFO', 'Worksite Location');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(175, '$.division', 'ASSIGNMENT_INFO', 'Division');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(176, '$.workers_comp_class', 'ASSIGNMENT_INFO', 'Workers Comp Class');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(177, '$.occupation', 'ASSIGNMENT_INFO', 'Occupation');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(178, '$.department', 'ASSIGNMENT_INFO', 'Department');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(179, '$.shift', 'ASSIGNMENT_INFO', 'Shift');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(180, '$.project', 'ASSIGNMENT_INFO', 'Project');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(181, '$.union', 'ASSIGNMENT_INFO', 'Union');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(182, '$.supervisor', 'ASSIGNMENT_INFO', 'Supervisor');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(183, '$.work_group', 'ASSIGNMENT_INFO', 'Work Group');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(184, '$.union_start_date', 'ASSIGNMENT_INFO', 'Union Start Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(185, '$.benefit_group', 'ASSIGNMENT_INFO', 'Benefit Group');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(186, '$.is_corp', 'ASSIGNMENT_INFO	', 'S - Corp');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(187, '$.is_owner', 'ASSIGNMENT_INFO	', 'Owner');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(188, '$.direct_deposit_status', 'DIRECT_DEPOSIT', 'Direct Deposit Status');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(189, '$.direct_deposit.account_type', 'DIRECT_DEPOSIT', 'Account Type');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(190, '$.direct_deposit.routing_transit_number', 'DIRECT_DEPOSIT', 'Routing Transit Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(191, '$.direct_deposit.bank_name', 'DIRECT_DEPOSIT', 'Bank Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(192, '$.direct_deposit.bank_account_number', 'DIRECT_DEPOSIT', 'Bank Account Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(193, '$.direct_deposit.pay_type', 'DIRECT_DEPOSIT', 'Pay Type');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(194, '$.direct_deposit.calculation_method', 'DIRECT_DEPOSIT', 'Calculation Method ');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(195, '$.direct_deposit.is_suppressaccountnumberprinting', 'DIRECT_DEPOSIT', 'Suppress Account Number Printing');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(196, '$.direct_deposit.status', 'DIRECT_DEPOSIT', 'Status');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(197, '$.direct_deposit.amount', 'DIRECT_DEPOSIT', 'Amount');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(198, '$.pay_method', 'COMPENSATION', 'Pay Method');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(199, '$.pay_rate', 'COMPENSATION', 'Pay Rate');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(200, '$.pay_rate_type', 'COMPENSATION', 'Pay Rate Type');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(201, '$.annual_pay', 'COMPENSATION', 'Annual Pay');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(202, '$.standard_hours', 'COMPENSATION', 'Standard Hours');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(203, '$.pay_group', 'COMPENSATION', 'Pay Group');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(204, '$.is_electronic_stub', 'COMPENSATION', 'Electronic Pay Stub');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(205, '$.effective_date', 'COMPENSATION', 'Effective Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(206, '$.description', 'ALTERNATE_PAYRATE', 'Pay Description');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(207, '$.pay_rate', 'ALTERNATE_PAYRATE', 'Pay Rate');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(208, '$.pay_code', 'ALTERNATE_PAYRATE', 'Pay Type');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(209, '$.federal_tax.filing_status', 'TAX_FILING	', 'Filing Status');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(210, '$.federal_tax.alt_tax_calc_method', 'TAX_FILING', 'Alternate Tax Calc Method');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(211, '$.federal_tax.override_amount', 'TAX_FILING', 'Override Amount');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(212, '$.deduction', 'RECURING_DEDUCTION', 'Deduction');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(213, '$.description', 'RECURING_DEDUCTION', 'Description');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(214, '$.Deduction Periods', 'RECURING_DEDUCTION', 'Pay Period Maximum');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(215, '$.amount', 'RECURING_DEDUCTION', 'Deduction Amount');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(216, '$.annual_deduction_limit', 'RECURING_DEDUCTION', 'Annual Deduction Limit');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(217, '$.track_arrears', 'RECURING_DEDUCTION', 'Track Arrears');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(218, '$.mandatory', 'RECURING_DEDUCTION', 'Mandatory Deduction');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(219, '$.start_date', 'RECURING_DEDUCTION', 'Deduction Start Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(220, '$.stop_date', 'RECURING_DEDUCTION', 'Deduction End Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(221, '$.recurring_basis', 'RECURING_DEDUCTION', 'recurring_basis');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(222, '$.periods', 'RECURING_DEDUCTION', 'Deduction Periods ');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(223, '$.loan_amount', 'EMPLOYEE_LOAN', 'Original Loan Amount');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(224, '$.loan_reason', 'EMPLOYEE_LOAN', 'Loan Reason');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(225, '$.deduction_amount', 'EMPLOYEE_LOAN', 'Deduction Amount');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(226, '$.interest_amount', 'EMPLOYEE_LOAN', 'Interest Amount');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(227, '$.deduction_frequency', 'EMPLOYEE_LOAN', 'Deduction Frequency');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(228, '$.total_payback_amount', 'EMPLOYEE_LOAN', 'Total Loan Amount');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(229, '$.deduction_code', 'EMPLOYEE_LOAN', 'Deduction');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(230, '$.deduction_end_date', 'EMPLOYEE_LOAN', 'Deduction End Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(231, '$.deduction_start_date', 'EMPLOYEE_LOAN', 'Deduction Start Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(232, '$.loan_date', 'EMPLOYEE_LOAN', 'Loan Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(233, '$.total_remaining_balance', 'EMPLOYEE_LOAN', 'Remaining Balance');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(234, '$.status', 'EMPLOYEE_LOAN', 'Status');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(235, '$.first_name', 'DEPENDENT_BENEFICIARY', 'First Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(236, '$.middle_name', 'DEPENDENT_BENEFICIARY', 'Middle Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(237, '$.last_name', 'DEPENDENT_BENEFICIARY', 'Last Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(238, '$.relation', 'DEPENDENT_BENEFICIARY', 'Relationship');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(239, '$.home_phone', 'DEPENDENT_BENEFICIARY', 'Home Telephone Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(240, '$.work_phone', 'DEPENDENT_BENEFICIARY', 'Mobile');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(241, '$.gender', 'DEPENDENT_BENEFICIARY', 'Gender');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(242, '$.is_beneficiary', 'DEPENDENT_BENEFICIARY', 'Mark as Beneficiary');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(243, '$.dob', 'DEPENDENT_BENEFICIARY', 'Date of Birth');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(244, '$.is_fulltime_student', 'DEPENDENT_BENEFICIARY', 'Student');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(245, '$.is_tobacco_user', 'DEPENDENT_BENEFICIARY', 'Tobbaco user');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(246, '$.ssn_number', 'DEPENDENT_BENEFICIARY', 'Social Security Number');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(247, '$.is_disabled', 'DEPENDENT_BENEFICIARY', 'Disabled status');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(248, '$.beneficiaryType', 'DEPENDENT_BENEFICIARY', 'Beneficiary Type');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(249, '$.request_date', 'LOA', 'Leave Request Date ');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(250, '$.start_date', 'LOA', 'Leave Start Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(251, '$.plan_return_date', 'LOA', 'Anticipated Return Date');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(252, '$.reason_desc', 'LOA', 'Reason');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(253, '$.school_name', 'EDUCATION', 'School Name');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(254, '$.is_graduated', 'EDUCATION', 'Graduated');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(255, '$.year_graduated', 'EDUCATION', 'Year Graduated');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(256, '$.degree', 'EDUCATION', 'Degree');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(257, '$.comments', 'EDUCATION', 'Comments');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(258, '$.educations', 'SKILL_EDUCATION', 'EDUCATION');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(259, '$.skills', 'SKILL_EDUCATION', 'SKILL');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(260, '$.address', 'DEPENDENT_BENEFICIARY', 'DEPENDENT_BENEFICIARY.ADDRESS');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(261, '$.one_time_deductions', 'ONE_TIME_DEDUCTION', 'ONE_TIME_DEDUCTION');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(262, '$.recurring_deductions', 'RECURING_DEDUCTION', 'RECURING_DEDUCTION');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(263, '$.pay_code_rate', 'ALTERNATE_PAYRATE', 'ALTERNATE_PAYRATE.PAY_CODE_RATE');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(264, '$.direct_deposit', 'DIRECT_DEPOSIT', 'DIRECT_DEPOSIT');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(265, '$.employment_eligibility_documentation', 'EMPLOYMENT_ELIGIBILITY_DOCUMENTATION', 'EMPLOYMENT_ELIGIBILITY_DOCUMENTATION');
INSERT INTO json_ui_mapping (id, json_key, [type], ui_field) VALUES(267, '$.certifiedPayrollCaXmlParams', 'PROJECT_INFO', 'California XML');
