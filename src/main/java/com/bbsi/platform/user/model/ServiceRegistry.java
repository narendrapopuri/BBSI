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
@Table(name="SERVICE_REGISTRY")
public class ServiceRegistry{

	@Id
	@SequenceGenerator(name = "service_registry_gen", sequenceName = "service_registry_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_registry_gen")
	@Column(name = "ID")
	private long id;

	@Column(name = "CODE")
	private String code;

	@Column(name = "SERVICE_NAME")
	private String serviceName;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "SERVICE_URI")
	private String serviceUri;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive= Boolean.TRUE;
	
	@Column(name="PRIVILEGE_ID", updatable=false)
	private Long privilegeId;
}
