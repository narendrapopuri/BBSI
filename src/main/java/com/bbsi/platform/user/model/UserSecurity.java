package com.bbsi.platform.user.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(exclude="userClient")
@Table(name = "USER_SECURITY")
public class UserSecurity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7753649177091761239L;

	@Id
    @SequenceGenerator(name = "user_security_gen", sequenceName = "user_security_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_security_gen")
	private Long id;
	
	@Column(name = "TYPE", nullable = false)
	private String type;
	
	@Column(name = "VALUE", nullable = false)
	private String value;
	
	@ManyToOne
	private UserClients userClient;
	
}
