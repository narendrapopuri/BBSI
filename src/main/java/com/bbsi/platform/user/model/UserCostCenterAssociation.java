package com.bbsi.platform.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "USER_COST_CENTER_ASSOCIATION", indexes = {@Index(columnList = "USER_ID"), @Index(columnList = "USER_TYPE")})
public class UserCostCenterAssociation extends EntityBase {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "user_cost_center_assoc_gen", sequenceName = "user_cost_center_assoc_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_cost_center_assoc_gen")
	@Column(name = "ID")
	private Long id;
	
	@Column(name="COST_CENTER_CODE", nullable = false)
	private String costCenterCode;

	@Column(name = "USER_TYPE", nullable = false)
	private String userType;

	@ManyToOne
	private Users user;

}
