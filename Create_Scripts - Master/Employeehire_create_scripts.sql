create table address (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
address1 varchar(255),
address2 varchar(255),
city varchar(255),
country varchar(255),
county varchar(255),
geo_code varchar(255),
is_active bit,
is_unicorporated_area bit,
state varchar(255),
zip_code varchar(255),
zip_code_suffix varchar(255),
primary key (id));

create table doc_template (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
doc_name varchar(255),
doc_type varchar(255),
document_id varchar(255),
is_fed_template bit,
is_mandatory bit,
is_state_template bit,
no_of_pages int,
state varchar(255),
primary key (id));

create table employee_documents (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
auth_name varchar(255),
client_code varchar(255),
doc_number varchar(255) not null,
document_path varchar(255),
employee_code varchar(255),
source_type varchar(255),
status varchar(255),
title varchar(255) not null,
upload_date datetime2,
primary key (id));

create table hire_document (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
agreement_id varchar(255),
approved_by varchar(255),
approved_date date,
description varchar(255),
doc_name varchar(255),
doc_path varchar(255),
doc_props varchar(MAX),
doc_template_id bigint,
is_approved bit,
is_submitted bit,
library_doc_id varchar(255),
title varchar(255) not null,
reject_reason varchar(255),
seq_num int,
signed_url varchar(1024),
status varchar(255),
submitted_date date,
new_hire_id bigint,
primary key (id));

create table new_hire (
new_hire_id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
agreement_id varchar(255),
anticipated_hire_date date,
client_code varchar(30) not null,
client_name varchar(255),
date_of_birth date,
doc_path varchar(255),
emp_image varbinary(MAX),
employee_code varchar(255),
first_name varchar(255) not null,
hire_doc_props varchar(MAX),
home_phone varchar(255),
i9approval_email varchar(255),
i9approval_firstname varchar(255),
is_admin_signed bit,
is_doc_submission_done bit not null,
is_electronic_onboard bit,
is_emp_i9_signed bit,
is_emp_signed bit,
is_emp_submitted bit,
last_name varchar(255) not null,
location varchar(255),
middle_name varchar(255),
mobile varchar(255),
notification_date date,
onboarding_initiation_date date not null,
personal_email varchar(255),
position varchar(255),
signed_url varchar(1024),
ssn_number varchar(255),
status varchar(255) not null,
resident_address bigint,
primary key (new_hire_id));

create table newhire_detail (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
input_props varchar(MAX),
status varchar(255) not null,
type varchar(255) not null,
new_hire_id bigint,
primary key (id));

create table sign_token_details (
id bigint not null,
created_by varchar(255),
created_on datetime2 not null,
modified_by varchar(255),
modified_on datetime2 not null,
version bigint,
access_token varchar(255),
api_base_url varchar(255),
client_id varchar(255),
client_secret varchar(255),
refresh_token varchar(255),
sign_type varchar(255),
time_to_live bigint,
web_base_url varchar(255),
primary key (id));

create index IDXglm8bmatiw5hrqgffgq2g6tfi on hire_document (agreement_id);

create index IDXmnb7tqb8o3dcp5d2qw1fy5a8o on new_hire (client_code);

create index IDX2tpl76lwgxueqjf48gyelb7sq on new_hire (agreement_id);

alter table new_hire add constraint UKrtro1y9cgwlgs8p1c17nxov8q unique (personal_email);

alter table sign_token_details add constraint UK82o5khaexisasaqnujffx8yv2 unique (sign_type);

create sequence address_id_seq start with 1 increment by 1;

create sequence doc_template_id_seq start with 1 increment by 1;

create sequence document_id_seq start with 1 increment by 1;

create sequence newhire_detail_id_seq start with 1 increment by 1;

create sequence newhire_document_id_seq start with 1 increment by 1;

create sequence newhire_id_seq start with 1 increment by 1;

create sequence sign_token_details_id_seq start with 1 increment by 1;

alter table hire_document add constraint FKa0ubhyjcc6xb8bomlm2g9u188 foreign key (new_hire_id) references new_hire;

alter table new_hire add constraint FK8oph4p4vsm6542y8de1x3bh0o foreign key (resident_address) references address;

alter table newhire_detail add constraint FKi0ah7ne8rfpsvvxui6bp2c37o foreign key (new_hire_id) references new_hire;
