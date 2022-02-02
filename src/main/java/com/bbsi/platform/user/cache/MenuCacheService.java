package com.bbsi.platform.user.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bbsi.platform.user.model.Menu;

public interface MenuCacheService {

	/**
	 * @param menu
	 */
	void save(Menu menu);

	/**
	 * @param code
	 * @return
	 */
	Menu find(Long code);
	
	/**
	 * @param keys
	 * @return
	 */
	List<Menu> findByKeys(Set<Long> keys);
	
	/**
	 * @param code
	 * @return
	 */
	List<Menu> findByParentCode(Long code);

	/**
	 * @return
	 */
	Map<Long, Menu> findAll();

	/**
	 * @param menu
	 */
	void update(Menu menu);

	/**
	 * @param code
	 */
	void delete(Long code);

	/**
	 * @param menuMap
	 */
	void saveAll(Map<Long, Menu> menuMap);
	
	/**
	 * 
	 */
	public void loadCache();
	
}