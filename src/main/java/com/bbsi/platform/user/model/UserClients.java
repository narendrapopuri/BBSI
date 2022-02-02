package com.bbsi.platform.user.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "USER_CLIENTS")
public class UserClients extends EntityBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "user_client_code_gen", sequenceName = "user_client_code_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_client_code_gen")
	@Column(name = "ID")
	private Long id;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CLIENT_CODE", nullable = false, insertable = false, updatable = false)
	private ClientMaster client;

	@Column(name = "CLIENT_CODE", nullable = false)
	private String clientCode;

	@Column(name = "IS_PRIMARY")
	private Boolean isPrimary;

	@Column(name = "EMPLOYEE_CODE")
	private String employeeCode;

	@Column(name = "IS_I9APPROVER")
	private boolean isI9Approver = Boolean.FALSE;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "USER_TYPE", nullable = false)
	private String userType;

	@Column(name = "NEW_HIRE_ID")
	private long newHireId;
	
	@Column(name = "REMEMBER_TIMENET_COMPANY_ID")
	private Boolean rememberTimenetCompanyId=Boolean.FALSE;
	
	@Column(name = "REMEMBER_TIMENET_USER_ID")
	private Boolean rememberTimenetUserId=Boolean.FALSE;
	
	@Column(name = "REMEMBER_TIMENET_PASSWORD")
	private Boolean rememberTimenetPassword=Boolean.FALSE;
	
	@Column(name = "TIMENET_COMPANY_ID")
	private String timenetCompanyId;
	
	@Column(name = "TIMENET_USER_ID")
	private String timenetUserId;

	@Column(name = "TIMENET_PASSWORD")
	private String timenetPassword;

	@Column(name = "START_DATE")
	private LocalDateTime startDate;

	@Column(name = "END_DATE")
	private LocalDateTime endDate;

	@Column(name = "FIRST_NAME", nullable = false)
	private String firstName;

	@Column(name = "LAST_NAME", nullable = false)
	private String lastName;

	@Column(name = "MOBILE")
	private String mobile;

	@ManyToOne
	private Users user;

	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "USER_CLIENT_ID")
	private List<UserSecurity> userFilters;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_CLIENT_ID")
	private List<ClientRole> clientRoles;

	@Column(name = "IS_CALIFORNIA_USER", nullable = false)
	private Boolean isCaliforniaUser = Boolean.FALSE;

	@Column(name = "IS_CCPA_ACCEPTED", nullable = false)
	private Boolean isCCPAAccepted = Boolean.FALSE;

	@Column(name = "IS_CCPA_UPDATED", nullable = false)
	private Boolean isCCPAUpdated = Boolean.FALSE;

}
