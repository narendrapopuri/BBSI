package com.bbsi.platform.user.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author anandaluru The persistent class for the menu database table.
 * 
 */
@Data
@Entity
@Table(name = "MENU")
public class Menu implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "menu_gen", sequenceName = "menu_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_gen")
	@Column(name = "ID")
	private long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive = Boolean.TRUE;

	@Column(name = "ICON_URL")
	private String iconUrl;

	@Column(name = "URL")
	private String url;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "PARENT_ID")
	private long parentId;

	@Column(name = "SEQUENCE")
	private int sequence;

	@Column(name = "FEATURE_CODE_ID")
	private long featureCodeId;

	@Column(name = "DISPLAY_NAME")
	private String displayName;

}