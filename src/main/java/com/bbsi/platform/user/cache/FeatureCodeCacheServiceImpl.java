package com.bbsi.platform.user.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.user.businessservice.intf.FeatureCodeBusinessService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

//@Logging
@Service
public class FeatureCodeCacheServiceImpl implements FeatureCodeCacheService {

	private static final String KEY = "FEATURE_CODE";
	
	private Map<String, CommonDTO> featureMap = Maps.newHashMap();
	
	private List<CommonDTO> featureCodes;

	
	public List<CommonDTO> getFeatureCodes() {
		if(null != featureCodes) {
			featureCodes.forEach(feature -> resetFlags(feature));
		}
		return featureCodes;
	}
	
	private void resetFlags(CommonDTO dto) {
		if(CollectionUtils.isNotEmpty(dto.getPrivileges())) {
			dto.getPrivileges().forEach(priv -> {
				priv.setIsEditable(true);
				priv.setIsSelected(true);
				resetFlags(priv);
			});
		}
		if(CollectionUtils.isNotEmpty(dto.getChild())) {
			dto.getChild().forEach(child -> {
				resetFlags(child);
			});
		}
	}

	public void setFeatureCodes(List<CommonDTO> features) {
		featureCodes = features;
	}

	@Autowired
	private FeatureCodeBusinessService featureCodeBusinessService;

	private RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, String, CommonDTO> hashOperations;

	@Autowired
	public FeatureCodeCacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
		loadCache();
		
	}

	@Override
	public void save(CommonDTO featureCodeHierarchyDTODTO) {
		hashOperations.put(KEY, featureCodeHierarchyDTODTO.getCode(), featureCodeHierarchyDTODTO);
	}

	@Override
	public CommonDTO find(String code) {
		CommonDTO featureCodeHierarchyDTO = null;
		try {
			featureCodeHierarchyDTO = featureMap.get(code);
			if (null == featureCodeHierarchyDTO) {
				featureCodeHierarchyDTO = featureCodeBusinessService.getFeature(code);
			}
		} catch (Exception e) {
			featureCodeHierarchyDTO = featureCodeBusinessService.getFeature(code);
		}
		return featureCodeHierarchyDTO;
	}

	@Override
	public List<CommonDTO> findByKeys(Set<String> keys) {
		List<CommonDTO> features = featureMap.entrySet().stream()
				.filter(map -> keys.contains(map.getKey()))
				.map(map -> map.getValue())
				.collect(Collectors.toList());
		if(CollectionUtils.isEmpty(features)) {
			List<CommonDTO> dbFeatures = featureCodeBusinessService.loadAllFeatureCodes();
			if(CollectionUtils.isNotEmpty(dbFeatures)) {
				features = Lists.newArrayList();
				for(CommonDTO feature : dbFeatures) {
					if(keys.contains(feature.getCode())) {
						features.add(feature);
					}
				}
			}
		}
		return features;
	}

	@Override
	public Map<String, CommonDTO> findAll() {
		return featureMap;
	}

	@Override
	public void update(CommonDTO featureCodeHierarchyDTO) {
		hashOperations.put(KEY, featureCodeHierarchyDTO.getCode(), featureCodeHierarchyDTO);
	}

	@Override
	public void delete(String code) {
		hashOperations.delete(KEY, code);
	}

	@Override
	public void saveAll(Map<String, CommonDTO> featureCodeHierarchyDTOMap) {
		hashOperations.putAll(KEY, featureCodeHierarchyDTOMap);

	}

	@Override
	public void loadCache() {
		featureMap = Maps.newHashMap();
		List<CommonDTO> features = featureCodeBusinessService.loadAllFeatureCodes();
		if(CollectionUtils.isNotEmpty(features)) {
			for(CommonDTO feature : features) {
				featureMap.put(feature.getCode(), feature);
			}
		}
		List<CommonDTO> featureCodeDTOs = featureCodeBusinessService.getAllFeatures();
		setFeatureCodes(featureCodeDTOs);
	}
}
