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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author anandaluru
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="USERS")
public class Users extends EntityBase{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2544952612096071427L;

	public Users() {
		super();
	}

	public Users(String email) {
		super();
		this.email = email;
	}

	@Id
    @SequenceGenerator(name = "users_gen", sequenceName = "users_id_seq", allocationSize = 1, initialValue = 150)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_gen")
    @Column(name = "ID")
    private Long id;

    @Column(name = "AUTHENTICATION_TYPE")
    private String authenticationType;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    
    @Column(name = "MFA_EMAIL")
    private String mfaEmail;

    @Column(name = "PASSWORD")
    private String password;
    
    @Column(name = "INVALID_ATTEMPTS")
    private int invalidAttempts = 0;
    
    @Column(name = "MFA_CANCEL_ATTEMPTS")
    private int mfaCancelAttempts = 0;
    
    @Column(name = "IS_FIRST_LOGIN", nullable = false)
    private Boolean isFirstLogin = Boolean.FALSE;
    
    @Column(name = "IS_POLICY_ACCEPTED", nullable = false)
    private Boolean isPolicyAccepted = Boolean.FALSE;
    
    @Column(name = "IS_POLICY_UPDATED", nullable = false)
    private Boolean isPolicyUpdated = Boolean.FALSE;

    @Column(name="TOKEN_VALUE")
    private String tokenValue;

	@Column(name = "DEFAULT_CLIENT")
	private String defaultClient;

	@Column(name = "IS_ACTIVE")
	private Boolean status;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "MOBILE")
	private String mobile;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private List<UserClients> clients;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_RBAC_ASSOCIATION", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<RbacEntity> roles;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private List<UserCostCenterAssociation> costCenterAssociations;

}
