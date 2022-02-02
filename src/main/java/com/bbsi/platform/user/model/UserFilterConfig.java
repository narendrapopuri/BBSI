package com.bbsi.platform.user.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "USER_FILTER_CONFIG")
public class UserFilterConfig extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7248943600240501297L;

	@Id
	@SequenceGenerator(name = "user_filter_config_gen", sequenceName = "user_filter_config_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_filter_config_gen")
	@Column(name = "ID")
	private long id;

	@Column(name = "CLIENT_CODE", nullable = false)
	private String clientCode;

	@Column(name = "USER_ID", nullable = false)
	private long userId;

	@Column(name = "FILTER_NAME", nullable = false)
	private String filterName;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_FILTER_CONFIG_ID")
	private List<UserFilterConfigValue> userFilterConfigValues;

}
