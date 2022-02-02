package com.bbsi.platform.user.businessservice.impl;

import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.dto.BasicDTO;
import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.generic.LogUtils;
import com.bbsi.platform.common.user.dto.MenuMappingDTO;
import com.bbsi.platform.user.businessservice.intf.MenuBusinessService;
import com.bbsi.platform.user.cache.FeatureCodeCacheService;
import com.bbsi.platform.user.cache.MenuCacheService;
import com.bbsi.platform.user.model.Menu;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * This class contains fetch operations related to Menu
 *
 */
@Logging
@Service
public class MenuBusinessServiceImpl implements MenuBusinessService {

	@Autowired
	private MenuCacheService menuCacheService;
	
	@Autowired
	private FeatureCodeCacheService featureCacheService;
	
	@Autowired
	private ObjectMapper mapper;

	@Override
	public List<MenuMappingDTO> fetchUserMenu(List<CommonDTO> userFeatures, List<CommonDTO> clientMappings) {
		Map<Long, MenuMappingDTO> menuMap = Maps.newHashMap();
		if (!CollectionUtils.isEmpty(userFeatures)) {
			Map<Long, Menu> menuCache = menuCacheService.findAll();
			Collection<Menu> menus = menuCache.values();
			if (!CollectionUtils.isEmpty(menus)) {
				Map<String, List<CommonDTO>> privilegeMap = Maps.newHashMap();
				for (CommonDTO parent : userFeatures) {
					privilegeMap.put(parent.getCode(), parent.getPrivileges());
				}
				List<CommonDTO> features = featureCacheService.findByKeys(privilegeMap.keySet());
				if (!CollectionUtils.isEmpty(features)) {
					menuMapping(clientMappings, menuMap, menus, privilegeMap, features);
				}
			}
		}
		return Lists.newArrayList(menuMap.values());
	}

	private void menuMapping(List<CommonDTO> clientMappings, Map<Long, MenuMappingDTO> menuMap, Collection<Menu> menus,
			Map<String, List<CommonDTO>> privilegeMap, List<CommonDTO> features) {
		for(CommonDTO feature : features) {
			for (Menu menu : menus) {
				if (feature.getId() == menu.getFeatureCodeId() && !CollectionUtils.isEmpty(privilegeMap.get(feature.getCode()))) {
						populateMenuMapping(clientMappings, menuMap, privilegeMap, feature, menu);
				}
			}
		}
	}

	private void populateMenuMapping(List<CommonDTO> clientMappings, Map<Long, MenuMappingDTO> menuMap,
			Map<String, List<CommonDTO>> privilegeMap, CommonDTO feature, Menu menu) {
		for (CommonDTO privileges : privilegeMap.get(feature.getCode())) {
			if (Boolean.TRUE.equals(privileges.getIsSelected())) {
				populateMenuMapping(menuMap, menu, clientMappings, feature);
			}
		}
	}
	
	private CommonDTO fetchMenuFeatures(List<CommonDTO> clientMappings, CommonDTO feature) {
		for (CommonDTO mapping : clientMappings) {
			if(!CollectionUtils.isEmpty(mapping.getPrivileges()) && mapping.getCode().equals(feature.getCode())) {
				return mapping;
			}
			if(!CollectionUtils.isEmpty(mapping.getChild())) {
				CommonDTO featureMapping = fetchMenuFeatures(mapping.getChild(), feature);
				if(null != featureMapping) {
					return featureMapping;
				}
			}
		}
		return null;
	}
	
	private void populateDTO(CommonDTO commonDTO, BasicDTO baseDTO) {
		baseDTO.setCode(commonDTO.getCode());
		baseDTO.setName(commonDTO.getName());
		if(!CollectionUtils.isEmpty(commonDTO.getChild())) {
			baseDTO.setChild(Lists.newArrayList());
			for(CommonDTO child : commonDTO.getChild()) {
				BasicDTO childBaseDTO = new BasicDTO();
	    		populateDTO(child, childBaseDTO);
	    		baseDTO.getChild().add(childBaseDTO);
    		}
		}
		if(!CollectionUtils.isEmpty(commonDTO.getPrivileges())) {
			baseDTO.setPrivileges(Lists.newArrayList());
			for(CommonDTO privilege : commonDTO.getPrivileges()) {
				if(null != privilege.getIsSelected() && privilege.getIsSelected()) {
					BasicDTO privDTO = new BasicDTO();
					privDTO.setCode(privilege.getCode());
					privDTO.setIsSelected(privilege.getIsSelected());
					baseDTO.getPrivileges().add(privDTO);
				}
			}
		}
	}

	private void populateMenuMapping(Map<Long, MenuMappingDTO> menuMap, Menu menu, List<CommonDTO> clientMappings, CommonDTO feature) {
		if(menuMap.containsKey(menu.getId())) {
			return;
		}
		MenuMappingDTO menuMappingDTO = new MenuMappingDTO();
		populateMenuDTO(menu, menuMappingDTO);
		CommonDTO featureDTO = fetchMenuFeatures(clientMappings, feature);
		if(null != featureDTO) {
			BasicDTO baseDTO = new BasicDTO();
			populateDTO(featureDTO, baseDTO);
			String featureData;
			try {
				featureData = Base64.getEncoder().encodeToString(mapper.writeValueAsBytes(baseDTO));
				menuMappingDTO.setFeature(featureData);
			} catch (JsonProcessingException e) {
				LogUtils.basicErrorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "populateMenuMapping"),e.getMessage());
			}
			
		}
		List<Menu> menuItems = menuCacheService.findByParentCode(menu.getId());
		if(!CollectionUtils.isEmpty(menuItems)) {
			List<MenuMappingDTO> menuItemDTOs = new ArrayList<>();
			for (Menu menuItem : menuItems) {
				MenuMappingDTO menuItemDTO = new MenuMappingDTO();
				populateMenuDTO(menuItem, menuItemDTO);
				menuItemDTOs.add(menuItemDTO);
			}
			menuMappingDTO.setMenuItems(menuItemDTOs);
			menuMap.put(menuMappingDTO.getId(), menuMappingDTO);
		} else if(menuMappingDTO.getParentId() > 0 && menuMap.containsKey(menuMappingDTO.getParentId())) {
			if(!menuMap.get(menuMappingDTO.getParentId()).getMenuItems().contains(menuMappingDTO)) {
				menuMap.get(menuMappingDTO.getParentId()).getMenuItems().add(menuMappingDTO);
			}
		} else if(menuMappingDTO.getParentId() > 0) {
			Menu parentMenu = menuCacheService.find(menuMappingDTO.getParentId());
			if(null != parentMenu) {
				MenuMappingDTO parentDTO = new MenuMappingDTO();
				populateMenuDTO(parentMenu, parentDTO);
				List<MenuMappingDTO> menuItemDTOs = new ArrayList<>();
				menuItemDTOs.add(menuMappingDTO);
				parentDTO.setMenuItems(menuItemDTOs);
				menuMap.put(parentDTO.getId(), parentDTO);
			}
		} else if(!menuMap.containsKey(menuMappingDTO.getId())) {
			menuMap.put(menuMappingDTO.getId(), menuMappingDTO);
		}
	}
	
	private void populateMenuDTO(Menu menu, MenuMappingDTO menuMappingDTO) {
		menuMappingDTO.setId(menu.getId());
		menuMappingDTO.setName(menu.getName());
		menuMappingDTO.setCategory(menu.getCategory());
		menuMappingDTO.setDisplayName(menu.getDisplayName());
		menuMappingDTO.setFeatureCodeId(menu.getFeatureCodeId());
		menuMappingDTO.setIconUrl(menu.getIconUrl());
		menuMappingDTO.setIsActive(menu.getIsActive());
		menuMappingDTO.setParentId(menu.getParentId());
		menuMappingDTO.setUrl(menu.getUrl());
		menuMappingDTO.setSequence(menu.getSequence());
	}

}
