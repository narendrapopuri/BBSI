package com.bbsi.platform.user.businessservice.intf;

import java.util.List;

import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.user.dto.FeatureCodeDTO;

/**
 * Class for managing the feature codes.
 * 
 * @author anandaluru
 *
 */
public interface FeatureCodeBusinessService {

	/**
	 * Method for getting all the feature list by client id.
	 * 
	 * @param clientCode
	 * @return Returns the {@link FeatureCodeDTO} list.
	 */
	public List<FeatureCodeDTO> getAllFeatureCodes();
	
	List<CommonDTO> getAllFeatures();
	
	List<CommonDTO> loadAllFeatureCodes();
	
	CommonDTO getFeature(String code);

}
