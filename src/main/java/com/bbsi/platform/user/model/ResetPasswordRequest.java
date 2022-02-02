package com.bbsi.platform.user.model;


import java.time.LocalDateTime;

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
@Table(name = "reset_password_request")
public class ResetPasswordRequest {

   
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "reset_password_request_gen", sequenceName = "reset_password_request_id_seq", allocationSize = 1, initialValue = 150)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reset_password_request_gen")
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "USER_ID")
    private Long userId;
    
    @Column(name = "SUBMITTED_ON")
	private LocalDateTime submittedOn;
    
    @Column(name = "REQUESTED_ON")
	private LocalDateTime requestedOn;
    
}
