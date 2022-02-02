package com.bbsi.platform.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CLIENT_ROLE", uniqueConstraints = {@UniqueConstraint(columnNames = { "CLIENT_CODE", "USER_CLIENT_ID", "ROLE_ID" }) })
public class ClientRole extends EntityBase {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "client_role_code_gen", sequenceName = "client_role_code_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_role_code_gen")
	@Column(name = "ID")
	private Long id;

	@Column(name = "CLIENT_CODE")
	private String clientCode;

	@Column(name = "SECTION_ID")
	private Long sectionId;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ROLE_ID", nullable = true)
	private Mapping role;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private UserClients userClient;
	
}
