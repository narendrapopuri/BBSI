package com.bbsi.platform.user.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Users_Passwords")
public class UsersPasswords extends EntityBase {

   
	private static final long serialVersionUID = 1L;

	@Id
    @SequenceGenerator(name = "user_passwords_code_gen", sequenceName = "user_passwords_code_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_passwords_code_gen")
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "USER_ID")
    private Long userId;
    
    @Column(name="PASSWORD_INDEX")
    private Integer passwordIndex;
   
    @Column(name = "CLIENT_CODE")
    private String clientCode;
    
    @Column(name = "PASSWORD")
    private String password;
    
    
    
    
    
    
}
