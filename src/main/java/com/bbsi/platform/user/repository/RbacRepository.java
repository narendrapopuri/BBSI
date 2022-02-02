package com.bbsi.platform.user.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.bbsi.platform.user.model.RbacEntity;

public interface RbacRepository extends JpaRepository<RbacEntity, Long> {
	
	List<RbacEntity> findByType(final String type);
	
	List<RbacEntity> findByTypeIn(final List<String> types);
	
	List<RbacEntity> findByParentId(final Long parentId);

	@Lock(LockModeType.NONE)
	RbacEntity findByCodeAndType(final String code, final String type);

	List<RbacEntity> findByNameAndType(final String name, final String type);
	
	@Lock(LockModeType.NONE)
	RbacEntity findByMappingId(final long mappingId);

	List<RbacEntity> findByIsClientTemplateAndStatusAndTypeNot(final Boolean isClientTemplate, final Boolean status, final String type);

	List<RbacEntity> findByIsClientTemplateOrClientCodeAndType(final Boolean isClientTemplate, final String clientCode, final String type);

	List<RbacEntity> findByClientCodeAndType(final String clientCode, final String type);

	boolean existsByCodeAndType(final String code, final String type);

	boolean existsByCodeAndTypeAndIdNot(final String code, final String type, final Long Id);

	boolean existsByCodeAndTypeAndClientCode(final String code, final String type, final String clientCode);

	boolean existsByCodeAndTypeAndClientCodeAndIdNot(final String code, final String type, final String clientCode, Long Id);

	RbacEntity findByCodeAndTypeAndClientCode(final String code, final String type, final String clientCode);

}
