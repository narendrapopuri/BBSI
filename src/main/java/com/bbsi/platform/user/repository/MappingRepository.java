package com.bbsi.platform.user.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bbsi.platform.user.model.Mapping;

/**
 * @author OSI
 *
 */
@Repository
public interface MappingRepository extends PagingAndSortingRepository<Mapping, Long> {
	
	List<Mapping> findByParentId(Long parentId);
	
	List<Mapping> findByParentIdIn(Collection<Long> parentIds);
	
	void deleteByCodeAndType(String code, String type);
	
	List<Mapping> findByIdIn(List<Long> ids);
	
	void deleteByCodeAndTypeAndParentId(String code, String type, Long parentId);
	
	Mapping findFirstByCodeAndTypeAndModifiedOnGreaterThanOrderByModifiedOnDesc(String code, String type, LocalDateTime date);
	
	@Query(value = "WITH Mapping_CTE (ID, CODE, NAME, DESCRIPTION, IS_ACTIVE, IS_REPLICA, TYPE, PARENT_ID)" + 
			"AS (" + 
			"    SELECT ID, CODE, NAME, DESCRIPTION, IS_ACTIVE, IS_REPLICA, TYPE, PARENT_ID" + 
			"    FROM mapping" + 
			"    WHERE PARENT_ID = ?1" + 
			"    UNION ALL" + 
			"        SELECT e.ID, e.CODE, e.NAME, e.DESCRIPTION, e.IS_ACTIVE, e.IS_REPLICA, e.TYPE, e.PARENT_ID" + 
			"        FROM mapping e" + 
			"        INNER JOIN Mapping_CTE ON Mapping_CTE.ID = e.PARENT_ID" + 
			"    )" + 
			"SELECT * FROM Mapping_CTE", nativeQuery = true)
	List<Object[]> findMappingHierarchy(Long parentId);
	
	@Query(value = "WITH Mapping_CTE (ID, CODE, NAME, DESCRIPTION, IS_ACTIVE, IS_REPLICA, TYPE, PARENT_ID)" + 
			"AS (" + 
			"    SELECT ID, CODE, NAME, DESCRIPTION, IS_ACTIVE, IS_REPLICA, TYPE, PARENT_ID" + 
			"    FROM mapping" + 
			"    WHERE PARENT_ID IN (:parentIds)" + 
			"    UNION ALL" + 
			"        SELECT e.ID, e.CODE, e.NAME, e.DESCRIPTION, e.IS_ACTIVE, e.IS_REPLICA, e.TYPE, e.PARENT_ID" + 
			"        FROM mapping e" + 
			"        INNER JOIN Mapping_CTE ON Mapping_CTE.ID = e.PARENT_ID" + 
			"    )" + 
			"SELECT * FROM Mapping_CTE WHERE CODE IN (:codes)", nativeQuery = true)
	List<Object[]> findMappingDetails(List<Long> parentIds, Set<String> codes);
	
	List<Mapping> findByCodeAndTypeOrderByModifiedOnDesc(String code, String type);
	
	void deleteByIdIn(List<Long> ids);
	
	List<Mapping> findByCodeAndType(String code, String type);
	
	Mapping findByCodeAndTypeAndParentId(String code, String type, Long parentId);
}
