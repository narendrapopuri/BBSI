package com.bbsi.platform.user.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author anandaluru 
 * 	The persistent class for the featurecode database table.
 * 
 */
@Data
@Entity
@Table(name = "FEATURE_CODE")
public class FeatureCode {

	@Id
	@SequenceGenerator(name = "feature_code_gen", sequenceName = "feature_code_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feature_code_gen")
	@Column(name = "ID")
	private long id;

	@Column(name = "CODE", nullable = false)
	private String code;

	@Column(name = "LEVEL", nullable = false)
	private String level;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "SEQ_NUM")
	private long seqNum;

	@Column(name = "PARENT_ID")
	private Long parentId;

	@OneToMany
	@JoinColumn(name = "PARENT_ID", insertable = false, updatable = false, foreignKey = @javax.persistence.ForeignKey(javax.persistence.ConstraintMode.NO_CONSTRAINT))
	private List<FeatureCode> featureCodes;
	
	@OneToMany
	@JoinColumn(name = "FEATURE_CODE_ID")
	private List<Privilege> privileges;
	
	
	@Column(name = "IS_DISPLAY_ENABLED", nullable = false)
    private Boolean isDisplayEnabled = Boolean.FALSE;

}