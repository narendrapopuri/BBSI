package com.bbsi.platform.user.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bbsi.platform.user.model.FeatureCode;

/**
 * @author anandaluru
 *
 */
@Repository
public interface FeatureCodeRepository extends PagingAndSortingRepository<FeatureCode, Long> {
	
	List<FeatureCode> findAllByOrderByIdAsc();
	
	List<FeatureCode> findByCodeIn(Set<String> codes);
	
	FeatureCode findByCode(String code);
}
