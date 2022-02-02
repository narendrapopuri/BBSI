package com.bbsi.platform.user.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author mprasad
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "ACCESS_TERMINATION")
public class AccessTermination extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3823159050319856952L;

	@Id
	@SequenceGenerator(name = "access_termination_gen", sequenceName = "access_termination_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "access_termination_gen")
	@Column(name = "ID")
	private Long id;

	@Column(name = "CLIENT_CODE", nullable = false)
	private String clientCode;

	@Column(name = "USER_TYPE", nullable = false)
	private String userType;

	@Column(name = "END_DATE")
	private LocalDate endDate;

	@Column(name = "START_DATE")
	private LocalDate startDate;
	
	@Column(name = "EMPLOYEE_CODE")
	private String employeeCode;

	@Column(name = "LIMIT_TO_18_MONTHS")
	private boolean limited;

	@Column(name = "END_DATE_PROCESSED")
	private Boolean endDateProcessed = Boolean.FALSE;

	@Column(name = "START_DATE_PROCESSED")
	private Boolean startDateProcessed = Boolean.FALSE;

}
