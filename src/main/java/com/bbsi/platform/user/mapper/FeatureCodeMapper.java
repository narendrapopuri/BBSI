package com.bbsi.platform.user.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import com.bbsi.platform.common.user.dto.FeatureCodeDTO;
import com.bbsi.platform.user.model.FeatureCode;

/**
 * 
 * Mapper for featureCode mapping
 * 
 * @author jkolla
 *
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface FeatureCodeMapper {

	
	/**
	 * @param featureCodeDTO
	 * @return
	 */
	public FeatureCode featureCodeDTOToFeatureCode(FeatureCodeDTO featureCodeDTO);

	/**
	 * @param featureCode
	 * @return
	 */
	public FeatureCodeDTO featureCodeToFeatureCodeDTO(FeatureCode featureCode);

	
	/**
	 * @param featureCodes
	 * @return
	 */
	public default List<FeatureCodeDTO> featureCodeListToFeatureCodeDTOList(List<FeatureCode> featureCodes) {
		return CollectionUtils.isNotEmpty(featureCodes)
				? featureCodes.stream().map(m -> featureCodeToFeatureCodeDTO(m)).collect(Collectors.toList())
				: Collections.emptyList();
	}
}
