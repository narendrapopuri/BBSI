package com.bbsi.platform.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bbsi.platform.user.model.Privilege;

/**
 * @author anandaluru
 *
 */
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

	@Query(value="select p.code from privilege p, feature_code fc where p.feature_code_id=fc.id and p.[type]=?1 and fc.code in (?3) and fc.parent_id=(select id from feature_code where code =?2)", nativeQuery = true)
	List<String> getPrivilegeCodesByFeatureCodes(String type, String parentId, List<String> features);
}
