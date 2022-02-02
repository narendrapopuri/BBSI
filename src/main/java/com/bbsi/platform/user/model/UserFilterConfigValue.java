package com.bbsi.platform.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "USER_FILTER_CONFIG_VALUE")
public class UserFilterConfigValue {

	@Id
	@SequenceGenerator(name = "user_filter_config_value_gen", sequenceName = "user_filter_config_value_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_filter_config_value_gen")
	@Column(name = "ID")
	private long id;
	
	@Column(name = "JSON_KEY", nullable = false)
	private String jsonKey;
	
	@Column(name = "JSON_VALUE", nullable = false)
	private String jsonValue;
}
