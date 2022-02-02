package com.bbsi.platform.user.web.resource.v1;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.annotation.Secured;
import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.LogUtils;
import com.bbsi.platform.user.businessservice.intf.FeatureCodeBusinessService;
import com.bbsi.platform.user.cache.FeatureCodeCacheService;
import com.bbsi.platform.user.utils.MethodNames;

/**
 * Class that exposes the methods to get the feature code data.
 * 
 * @author jkolla
 *
 */
@RestController
@RequestMapping("/v1/featurecode")
@CrossOrigin
@Logging
public class FeatureCodeResource {

	@Autowired
	private FeatureCodeBusinessService featureCodeBusinessService;
	@Autowired
	private FeatureCodeCacheService featureCodeCacheService;
	
	/**
	 * Method for getting the feature code.
	 * 
	 * @return
	 */
	@Secured
	@GetMapping
	public List<CommonDTO> getAllFeatureCodes() {
		List<CommonDTO> featureCodeDTOs = null;
		try {
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_FEATURECODES),
					"Retriving featureCode details");

			featureCodeDTOs = featureCodeBusinessService.getAllFeatures();
			LogUtils.debugLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_FEATURECODES),
					String.format("Retrived featureCode details %s", featureCodeDTOs));

		} catch (Exception e) {
			handleException.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_FEATURECODES), e,
					"Error occured while getting featureCode Details by");
		}
		return featureCodeDTOs;
	}
	
	@GetMapping(path = "/reload", produces = MediaType.APPLICATION_JSON_VALUE)
	public String reload(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			if(null != userPrincipal && GenericConstants.SUPER_ADMIN.equals(userPrincipal.getEmail())) {
        		featureCodeCacheService.loadCache();
			}
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "reload"), e,
					"Error occured while Reloading");
		}
		return "Success";
	}

}
