package com.bbsi.platform.user.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author OSI 
 * 
 * 	The persistent class for the privilege mapping database table.
 * 
 */
@Data
@Entity
@EqualsAndHashCode(exclude="mapping")
@Table(name = "PRIVILEGE_MAPPING")
public class PrivilegeMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2826456257267503194L;

	@Id
	@SequenceGenerator(name = "privilege_mapping_gen", sequenceName = "privilege_mapping_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privilege_mapping_gen")
	@Column(name = "ID")
	private long id;

	@Column(name = "CODE", updatable = false)
	private String code;

	@Column(name = "NAME", nullable = false)
	private String name;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "IS_SELECTED")
	private Boolean isSelected = false;
	
	@Column(name = "IS_EDITABLE")
	private Boolean isEditable = true;

	@ManyToOne(fetch = FetchType.LAZY)
	private Mapping mapping;
	
}