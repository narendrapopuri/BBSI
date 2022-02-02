package com.bbsi.platform.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author anandaluru The persistent class for the privilege database table.
 * 
 */
@Data
@Entity
@Table(name = "PRIVILEGE")
public class Privilege {

	@Id
	@SequenceGenerator(name = "privilege_gen", sequenceName = "privilege_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privilege_gen")
	@Column(name = "ID")
	private long id;

	@Column(name = "CODE", updatable = false)
	private String code;

	@Column(name = "DESCRIPTION", updatable = false)
	private String description;

	@Column(name = "ACTION", updatable = false)
	private String action;

	@Column(name = "TYPE", updatable = false)
	private String type;

	@ManyToOne
	private FeatureCode featureCode;
	
}