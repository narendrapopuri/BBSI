package com.bbsi.platform.user.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "EMPLOYEE_INVITATION", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "CLIENT_CODE", "EMPLOYEE_CODE" }) })
public class EmployeeInvitation extends EntityBase {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "emp_notification_code_gen", sequenceName = "emp_notification_code_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_notification_code_gen")
	@Column(name = "ID")
	private Long id;

	@Column(name = "CLIENT_CODE")
	private String clientCode;

	@Column(name = "EMPLOYEE_CODE")
	private String employeeCode;

	@Column(name = "LAST_SENT_DATE")
	private LocalDateTime lastSentDate;

}
