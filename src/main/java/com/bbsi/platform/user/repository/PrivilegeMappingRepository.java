package com.bbsi.platform.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.user.model.PrivilegeMapping;

/**
 * @author OSI
 *
 */
@Repository
public interface PrivilegeMappingRepository extends PagingAndSortingRepository<PrivilegeMapping, Long> {
	
	List<PrivilegeMapping> findByMapping_Id(final Long mappingId);
	
	@Query(value ="SELECT id,code,name,type,is_selected,is_editable,mapping_id from PRIVILEGE_MAPPING where mapping_id in (:mappingIds)", nativeQuery = true)
	List<Object[]> getPrivileges(@Param("mappingIds") List<Long> mappingIds);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE PRIVILEGE_MAPPING SET IS_SELECTED=?1, IS_EDITABLE=?2 WHERE MAPPING_ID IN (?3)", nativeQuery = true)
	void updatePrivileges(final boolean selected, final boolean editable, final List<Long> mappingIds);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE PRIVILEGE_MAPPING SET IS_SELECTED=?1, IS_EDITABLE=?2 WHERE MAPPING_ID=?3 AND CODE IN (?4)", nativeQuery = true)
	void updateMappingPrivileges(final boolean selected, final boolean editable, final Long id, final List<String> privileges);
}
