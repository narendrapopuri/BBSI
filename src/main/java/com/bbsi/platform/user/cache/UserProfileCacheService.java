package com.bbsi.platform.user.cache;

import java.util.List;
import java.util.Map;

import com.bbsi.platform.common.dto.UserPrincipal;

/**
 * @author veethakota
 *
 */
public interface UserProfileCacheService {

	/**
	 * @param userProfile
	 */
	void save(UserPrincipal userProfile);

	/**
	 * @param code
	 * @return
	 */
	UserPrincipal find(String code);

	/**
	 * @param keys
	 * @return
	 */
	List<UserPrincipal> findByKeys(List<String> keys);

	/**
	 * @return
	 */
	Map<String, UserPrincipal> findAll();

	/**
	 * @param userProfile
	 */
	void update(UserPrincipal userProfile);

	/**
	 * @param code
	 */
	void delete(String code);
	
	void clearAll();

	/**
	 * @param userProfileMap
	 */
	void saveAll(Map<String, UserPrincipal> userProfileMap);

}
