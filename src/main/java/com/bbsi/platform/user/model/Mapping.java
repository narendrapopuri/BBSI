package com.bbsi.platform.user.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author OSI
 *  
 * 	The persistent class for RBAC mappings.
 */
@Data
@Entity
@EntityListeners({ AuditingEntityListener.class})
@EqualsAndHashCode(exclude="mappings")
@Table(name = "MAPPING", indexes = {@Index(columnList = "PARENT_ID"),@Index(columnList = "CODE,TYPE")})
public class Mapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3086626838967435335L;

	@Id
	@SequenceGenerator(name = "mapping_code_gen", sequenceName = "mapping_code_id_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mapping_code_gen")
	@Column(name = "ID")
	private long id;

	@Column(name = "CODE", nullable = false)
	private String code;

	@Column(name = "NAME", nullable = false)
	private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_ACTIVE")
    private Boolean status = Boolean.TRUE;
    
    @Column(name = "IS_REPLICA")
	private Boolean isReplica = false;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "PARENT_ID")
	private Long parentId;

	@OneToMany
	@JoinColumn(name = "PARENT_ID", insertable = false, updatable = false, foreignKey = @javax.persistence.ForeignKey(javax.persistence.ConstraintMode.NO_CONSTRAINT))
	private List<Mapping> mappings;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "MAPPING_ID")
	private List<PrivilegeMapping> privileges;

	@CreatedDate
	@Column(name = "CREATED_ON", nullable = false, updatable = false)
	private LocalDateTime createdOn;

	@CreatedBy
	@Column(name = "CREATED_BY", updatable = false, nullable = false)
	private String createdBy;

	@LastModifiedDate
	@Column(name = "MODIFIED_ON", nullable = false)
	private LocalDateTime modifiedOn;

	@LastModifiedBy
	@Column(name = "MODIFIED_BY", nullable = false)
	private String modifiedBy;
	
}