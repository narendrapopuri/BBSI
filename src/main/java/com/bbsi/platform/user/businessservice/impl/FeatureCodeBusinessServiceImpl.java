package com.bbsi.platform.user.businessservice.impl;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.user.dto.FeatureCodeDTO;
import com.bbsi.platform.user.businessservice.intf.FeatureCodeBusinessService;
import com.bbsi.platform.user.mapper.FeatureCodeMapper;
import com.bbsi.platform.user.model.FeatureCode;
import com.bbsi.platform.user.model.Privilege;
import com.bbsi.platform.user.repository.FeatureCodeRepository;
import com.google.common.collect.Lists;

/**
 * @author vpula
 *
 */
@Logging
@Service
public class FeatureCodeBusinessServiceImpl implements FeatureCodeBusinessService {

	@Autowired
	private FeatureCodeRepository featureCodeRepository;

	@Autowired
	private FeatureCodeMapper featureCodeMapper;

	/*
	 * Method for getting feature code list by client code (non-Javadoc)
	 * 
	 * @see com.bbsi.platform.user.businessservice.intf.FeatureCodeBusinessService#
	 * getAllFeatureCodesByClientCode(java.lang.String)
	 */
	@Override
	public List<FeatureCodeDTO> getAllFeatureCodes() {
		List<FeatureCode> featureCodes = null;
		try {
			featureCodes = (List<FeatureCode>) featureCodeRepository.findAll();
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "getAllFeatureCodes"), e,
					"Error occured while getting FeatureCodes");
		}
		return featureCodeMapper.featureCodeListToFeatureCodeDTOList(featureCodes);
	}
	
	@Override
	@Transactional(readOnly = true)
	public CommonDTO getFeature(String code) {
		FeatureCode featureCode = featureCodeRepository.findByCode(code);
		if(null != featureCode) {
			CommonDTO parent = new CommonDTO();
			populateDTO(parent, featureCode);
			return parent;
		}
		return null;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CommonDTO> loadAllFeatureCodes() {
		List<CommonDTO> features = Lists.newArrayList();
		List<FeatureCode> featureCodes = featureCodeRepository.findAllByOrderByIdAsc();
		if(CollectionUtils.isNotEmpty(featureCodes)) {
			for(FeatureCode featureCode : featureCodes) {
				CommonDTO parent = new CommonDTO();
				populateDTO(parent, featureCode);
				features.add(parent);
			}
		}
		return features;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CommonDTO> getAllFeatures() {
		List<CommonDTO> features = Lists.newArrayList();
		List<FeatureCode> featureCodes = featureCodeRepository.findAllByOrderByIdAsc();
		if(CollectionUtils.isNotEmpty(featureCodes)) {
			List<FeatureCode> removeList = Lists.newArrayList();
			for(FeatureCode featureCode : featureCodes) {
				if(featureCode.getIsDisplayEnabled() && (featureCode.getParentId() == null 
						|| featureCode.getParentId().equals(0l))) {
					CommonDTO parent = new CommonDTO();
					populateDTO(parent, featureCode);
					features.add(parent);
					removeList.add(featureCode);
				}
			}
			featureCodes.removeAll(removeList);
			for(CommonDTO parent : features) {
				populateChildNodes(parent, featureCodes);
			}
		}
		return features;
	}
	
	private void populateChildNodes(CommonDTO parent, List<FeatureCode> mappingList) {
		if(CollectionUtils.isNotEmpty(mappingList)) {
    		List<FeatureCode> removeList = Lists.newArrayList();
    		List<FeatureCode> childList = Lists.newArrayList();
    		for(FeatureCode dbMapping : mappingList) {
    			if(dbMapping.getIsDisplayEnabled() && dbMapping.getParentId().equals(parent.getId())) {
    				childList.add(dbMapping);
    				removeList.add(dbMapping);
    			}
    		}
    		Collections.sort(childList, 
                    Comparator.comparingLong( 
                    		FeatureCode::getSeqNum));
    		for(FeatureCode dbMapping : childList) {
				CommonDTO child = new CommonDTO();
				populateDTO(child, dbMapping);
				child.setParentId(parent.getId());
	    		parent.getChild().add(child);
    		}
    		mappingList.removeAll(removeList);
    		if(CollectionUtils.isNotEmpty(parent.getChild())) {
    	    	for(CommonDTO child : parent.getChild()) {
	    			populateChildNodes(child, mappingList);
	    		}
    		}
    	}
    }
	
	private void populateDTO(CommonDTO element, FeatureCode feature) {
		element.setId(feature.getId());
		element.setCode(feature.getCode());
		element.setName(feature.getName());
		element.setType(feature.getType());
		element.setSeqNum(feature.getSeqNum());
		if(CollectionUtils.isNotEmpty(feature.getPrivileges())) {
			for(Privilege privilege : feature.getPrivileges()) {
				CommonDTO privDTO = new CommonDTO();
				privDTO.setId(privilege.getId());
				privDTO.setCode(privilege.getCode());
				privDTO.setName(privilege.getDescription());
				privDTO.setType(privilege.getType());
				if(StringUtils.isNotEmpty(privilege.getAction()) && privilege.getAction().equals("DISABLED")) {
					privDTO.setIsSelected(false);
					privDTO.setIsEditable(false);
				} else {
					privDTO.setIsSelected(true);
					privDTO.setIsEditable(true);
				}
				element.getPrivileges().add(privDTO);
			}
		}
	}

}
