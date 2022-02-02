package com.bbsi.platform.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author veethakota
 *
 */
@Data
@Entity
@Table(name = "USER_PRIVEGE_VIEW")
public class UserPrivilegeView{

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "URL")
	private String url;

	@Column(name = "USER_NAME")
	private String userName;

}