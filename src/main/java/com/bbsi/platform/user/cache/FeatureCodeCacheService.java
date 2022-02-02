package com.bbsi.platform.user.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bbsi.platform.common.dto.CommonDTO;

public interface FeatureCodeCacheService {

	/**
	 * @param featureCodeHierarchyDTO
	 */
	void save(CommonDTO featureCodeHierarchyDTO);

	/**
	 * @param code
	 * @return
	 */
	CommonDTO find(String code);
	
	/**
	 * @param keys
	 * @return
	 */
	List<CommonDTO> findByKeys(Set<String> keys);

	/**
	 * @return
	 */
	Map<String, CommonDTO> findAll();

	/**
	 * @param featureCodeHierarchyDTO
	 */
	void update(CommonDTO featureCodeHierarchyDTO);

	/**
	 * @param code
	 */
	void delete(String code);

	/**
	 * @param featureCodeHierarchyDTOMap
	 */
	void saveAll(Map<String, CommonDTO> featureCodeHierarchyDTOMap);
	
	/**
	 * 
	 */
    void loadCache();
	
	List<CommonDTO> getFeatureCodes();

    void setFeatureCodes(List<CommonDTO> featureCodes);
	
}