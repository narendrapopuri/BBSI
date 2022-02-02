package com.bbsi.platform.user.businessservice.impl;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.common.user.dto.UserFilterConfigDTO;
import com.bbsi.platform.common.user.dto.UserFilterConfigValueDTO;
import com.bbsi.platform.user.businessservice.intf.UserFilterConfigBusinessService;
import com.bbsi.platform.user.mapper.UserFilterConfigMapper;
import com.bbsi.platform.user.mapper.UserFilterConfigValueMapper;
import com.bbsi.platform.user.model.UserFilterConfig;
import com.bbsi.platform.user.model.UserFilterConfigValue;
import com.bbsi.platform.user.repository.UserFilterConfigRepository;
import com.bbsi.platform.user.repository.UserFilterConfigValueRepository;
import com.bbsi.platform.user.utils.MethodNames;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class UserFilterConfigBusinessServiceImpl implements UserFilterConfigBusinessService {
	
	@Autowired
	private UserFilterConfigRepository userFilterConfigRepository;
	
	@Autowired
	private UserFilterConfigValueRepository userFilterConfigValueRepository;

	@Autowired
	private UserFilterConfigMapper  userFilterConfigMapper;
	
	@Autowired
	private UserFilterConfigValueMapper  userFilterConfigValueMapper;
	
	private static final String FILTER_NAME = "filter_name";
	private static final String USER_CONFIG_FILTER_DTO = "user_filter_config_pay_load_dto";

	@Override
	public List<UserFilterConfigDTO> getAllUserFilterConfigsByClientCodeAndUserId(String clientCode,long userId) {
		List<UserFilterConfig> userFilterConfigs = null;
		try {
			userFilterConfigs = (List<UserFilterConfig>) userFilterConfigRepository.findByClientCodeAndUserId(clientCode,userId);
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_ALL_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID), e,
					"Error occured while getAllUserFilterConfigsByClientCodeAndUserId");
		}
		return userFilterConfigMapper.userFilterConfigListToUserFilterConfigDTOList(userFilterConfigs);
	}

	@Override
	public String getAllUserFilterConfigsByClientCodeAndUserIdAndFilterId(String clientCode,
			long userId, long id) {
		UserFilterConfig userFilterConfig = null;
		String userFilterConfigPayload = "";
		try {
			userFilterConfig = userFilterConfigRepository.findByClientCodeAndUserIdAndId(clientCode,userId,id);
			UserFilterConfigDTO userFilterConfigDTO = null;
			userFilterConfigDTO = userFilterConfigMapper.userFilterConfigToUserFilterConfigDTO(userFilterConfig);
			if(userFilterConfigDTO != null) {
				userFilterConfigPayload = constructUserFilterConfigPayload(userFilterConfigDTO);
			}
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID_AND_FILTER_ID), e,
					"Error occured while getAllUserFilterConfigsByClientCodeAndUserIdAndFilterId");
		}
		return userFilterConfigPayload;
	}

	@SuppressWarnings("unchecked")
	private String constructUserFilterConfigPayload(UserFilterConfigDTO userFilterConfigDTO) {
		JSONObject userFilterConfigJson = null;
		userFilterConfigJson = new JSONObject();
		
		try {
			Field changeMap = null;
			changeMap = userFilterConfigJson.getClass().getDeclaredField("map");
			changeMap.setAccessible(true);
			changeMap.set(userFilterConfigJson,new LinkedHashMap<>());
			changeMap.setAccessible(false);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "constructUserFilterConfigPayload"),
					e, "Error occurred while constructUserFilterConfigPayload");
		}
		
		for (UserFilterConfigValueDTO userFilterConfigValueDTO : userFilterConfigDTO.getUserFilterConfigValues()) {
			List<String> filterValues = Arrays.asList(userFilterConfigValueDTO.getJsonValue().split(","));
			JSONArray jsonArray = null;
			jsonArray = new JSONArray();
			for(String filterValue : filterValues) {
				jsonArray.add(filterValue);
			}
			userFilterConfigJson.put(userFilterConfigValueDTO.getJsonKey(), jsonArray);
		}
		
		return userFilterConfigJson.toString();
	}

	@Override
	public void deleteByClientCodeAndUserIdAndFilterId(String clientCode, long userId, long id) {
		try {
			userFilterConfigRepository.deleteByClientCodeAndUserIdAndId(clientCode, userId, id);
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.DELETE_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID_AND_FILTER_ID), e,
					"Error occured while getAllUserFilterConfigsByClientCodeAndUserIdAndFilterId");
		}		
	}

	@Override
	@Transactional
	public UserFilterConfigDTO createUserFilterConfig(String userFilterConfigPayload,String clientCode,long userId) {
		UserFilterConfig userFilterConfig = null;
		UserFilterConfigDTO userFilterConfigDTO = null;
		try {
			userFilterConfigDTO = constructUserFilterConfig(userFilterConfigPayload,clientCode,userId);
			userFilterConfig = userFilterConfigMapper.userFilterConfigDTOToUserFilterConfig(userFilterConfigDTO);
			userFilterConfig = userFilterConfigRepository.save(userFilterConfig);
			userFilterConfigDTO = userFilterConfigMapper.userFilterConfigToUserFilterConfigDTO(userFilterConfig);
			return userFilterConfigDTO;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.CRATE_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID), e,
					String.format("Error occurred while creating user filter config details %s" ,userFilterConfigDTO));
		}
		return null;
	}
	
	@Override
	@Transactional
	public UserFilterConfigDTO updateUserFilterConfig(long filterId,String userFilterConfigPayload,String clientCode,long userId) {
		UserFilterConfig userFilterConfig = null;
		UserFilterConfigDTO userFilterConfigDTO = null;
		try {
			userFilterConfigValueRepository.deleteUserFilterConfigValueByFilterId(filterId);
			userFilterConfig = userFilterConfigRepository.findByClientCodeAndUserIdAndId(clientCode,userId,filterId);
			userFilterConfig = populateUserFilterConfigValueDTO(userFilterConfig,userFilterConfigPayload);
			userFilterConfig = userFilterConfigRepository.save(userFilterConfig);
			userFilterConfigDTO = userFilterConfigMapper.userFilterConfigToUserFilterConfigDTO(userFilterConfig);
			return userFilterConfigDTO;
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.UPDATE_USER_FILTER_CONFIG_BY_CLIENT_CODE_AND_USER_ID), e,
					String.format("Error occurred while updating user filter config details %s" ,userFilterConfigDTO));
		}
		return null;
	}
	
	private UserFilterConfig populateUserFilterConfigValueDTO(UserFilterConfig userFilterConfig,String userConfigPayLoad) {
		JsonObject userFilterConfigJsonObject = null;
		userFilterConfigJsonObject = new Gson().fromJson(userConfigPayLoad, JsonObject.class);
		if (userFilterConfigJsonObject != null) {
			JsonObject jsonObject = null;
			userFilterConfig.setFilterName(userFilterConfigJsonObject.get(FILTER_NAME).getAsString());
			jsonObject = userFilterConfigJsonObject.get(USER_CONFIG_FILTER_DTO).getAsJsonObject();
			if(jsonObject != null && jsonObject.keySet().size() > 0) {
				for (String key : jsonObject.keySet()) {
					if(key != null && StringUtils.isNotEmpty(key)) {
						UserFilterConfigValueDTO userFilterConfigValueDTO = null;
						userFilterConfigValueDTO =	constructUserFilterConfigValue(jsonObject, key);
						UserFilterConfigValue userFilterConfigValue = null;
						userFilterConfigValue = userFilterConfigValueMapper.userFilterConfigValueDTOToUserFilterConfigValue(userFilterConfigValueDTO);
						userFilterConfig.getUserFilterConfigValues().add(userFilterConfigValue);
					}
				}
			}
		}
		return userFilterConfig;
	}

	private UserFilterConfigDTO constructUserFilterConfig(String userConfigPayLoad,String clientCode,long userId) {
		JsonObject userFilterConfigJsonObject = null;
		userFilterConfigJsonObject = new Gson().fromJson(userConfigPayLoad, JsonObject.class);
		UserFilterConfigDTO userFilterConfigDTO = null;
		if (userFilterConfigJsonObject != null) {
			userFilterConfigDTO = new UserFilterConfigDTO();
			userFilterConfigDTO.setClientCode(clientCode);
			userFilterConfigDTO.setUserId(userId);
			userFilterConfigDTO.setFilterName(userFilterConfigJsonObject.get(FILTER_NAME).getAsString());
			JsonObject jsonObject = null;
			jsonObject = userFilterConfigJsonObject.get(USER_CONFIG_FILTER_DTO).getAsJsonObject();
			List<UserFilterConfigValueDTO> userFilterConfigValues = null;
			userFilterConfigValues = new ArrayList<>();
			if(jsonObject != null && jsonObject.keySet().size() > 0) {
				for (String key : jsonObject.keySet()) {
					if(key != null && StringUtils.isNotEmpty(key)) {
						userFilterConfigValues.add(constructUserFilterConfigValue(jsonObject, key));
					}
				}
			}
			userFilterConfigDTO.setUserFilterConfigValues(userFilterConfigValues);
		}
		return userFilterConfigDTO;
	}
	
	private UserFilterConfigValueDTO constructUserFilterConfigValue(JsonObject jsonObject, String jsonKey) {

		UserFilterConfigValueDTO userFilterConfigValueDTO = null;
		userFilterConfigValueDTO = new UserFilterConfigValueDTO();
		userFilterConfigValueDTO.setJsonKey(jsonKey);
		JsonArray jsonArray = null;
		jsonArray = jsonObject.get(jsonKey).getAsJsonArray();
		String value = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			value += jsonArray.get(i).getAsString() + ",";
		}
		value = value.substring(0, value.length() - 1);
		userFilterConfigValueDTO.setJsonValue(value);
		return userFilterConfigValueDTO;
	}
	
	
}
