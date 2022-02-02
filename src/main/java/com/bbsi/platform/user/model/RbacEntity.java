package com.bbsi.platform.user.model;

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
 * @author OSI
 *  
 * 	The persistent class for the RBAC tables.
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "RBAC_ENTITY")
public class RbacEntity extends EntityBase {

	private static final long serialVersionUID = 4681568757740776497L;

	@Id
	@SequenceGenerator(name = "rbac_gen", sequenceName = "rbac_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rbac_gen")
	@Column(name = "ID")
	private long id;

	@Column(name = "CODE", nullable = false)
	private String code;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "TYPE")
	private String type;

	@Column(name = "IS_ACTIVE")
	private Boolean status;
	
	@Column(name = "MAPPING_ID")
	private long mappingId;

	@Column(name="IS_CLIENT_TEMPLATE")
	private Boolean isClientTemplate;

	@Column(name = "PARENT_ID")
	private Long parentId;

	@Column(name = "CLIENT_CODE")
	private String clientCode;

	@Column(name="IS_VOC_EDITABLE", nullable = false)
	private Boolean isVocEditable= Boolean.TRUE;
	
	@Column(name="IS_BRANCH_EDITABLE", nullable = false)
	private Boolean isBranchEditable= Boolean.TRUE;
	
	@Column(name="IS_CLIENT_EDITABLE", nullable = false)
	private Boolean isClientEditable= Boolean.TRUE;
	
}