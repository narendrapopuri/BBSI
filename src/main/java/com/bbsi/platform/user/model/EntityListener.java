package com.bbsi.platform.user.model;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class EntityListener {

	@PreUpdate
	public void commonInfo(EntityBase base) {
		base.setModifiedOn(LocalDateTime.now());
	}

	@PrePersist
	public void setDefaultCreatedDate(EntityBase base) {
		base.setCreatedOn(LocalDateTime.now());
		base.setModifiedOn(LocalDateTime.now());
	}
}
