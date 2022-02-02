package com.bbsi.platform.user.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

/**
 * @author veethakota
 *
 */
@Data
@MappedSuperclass
@EntityListeners({ AuditingEntityListener.class, EntityListener.class })
public class EntityBase implements Serializable {

	private static final long serialVersionUID = 8247563368473688577L;

	@Column(name = "CREATED_ON", nullable = false, updatable = false)
	private LocalDateTime createdOn;

	@CreatedBy
	@Column(name = "CREATED_BY", updatable = false, nullable = false)
	private String createdBy;

	@Column(name = "MODIFIED_ON", nullable = false)
	private LocalDateTime modifiedOn;

	@LastModifiedBy
	@Column(name = "MODIFIED_BY", nullable = false)
	private String modifiedBy;

	@Version
	@Column(name = "VERSION")
	private long version;

}
