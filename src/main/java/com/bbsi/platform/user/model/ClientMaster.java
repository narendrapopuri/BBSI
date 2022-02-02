package com.bbsi.platform.user.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "CLIENT_MASTER")
public class ClientMaster implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CLIENT_CODE", nullable = false)
	private String clientCode;

	@Column(name = "CLIENT_NAME")
	private String clientName;

	@Column(name = "COST_CENTER_CODE")
	private String costCenterCode;

	@Column(name = "COST_CENTER_DESC")
	private String costCenterDescription;
	
	@Column(name = "MODIFIED_ON")
	private LocalDateTime modifiedOn;

}
