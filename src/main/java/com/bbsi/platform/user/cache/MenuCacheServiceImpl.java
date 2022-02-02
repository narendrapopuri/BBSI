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

import com.bbsi.platform.user.model.Menu;
import com.bbsi.platform.user.repository.MenuRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

//@Logging
@Service
public class MenuCacheServiceImpl implements MenuCacheService {

	private static final String KEY = "MENU_DATA";
	
	private Map<Long, Menu> menuMap = Maps.newHashMap();

	@Autowired
	private MenuRepository menuRepository;

	private RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, Long, Menu> hashOperations;

	@Autowired
	public MenuCacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
		loadCache();
	}

	@Override
	public void save(Menu menu) {
		hashOperations.put(KEY, menu.getId(), menu);
	}

	@Override
	public Menu find(Long code) {
		Menu menu = null;
		try {
			menu = menuMap.get(code);
			if (null == menu) {
				menu = getMenu(code, menu);
			}
		} catch (Exception e) {
			menu = getMenu(code, menu);
		}
		return menu;
	}

	private Menu getMenu(Long code, Menu menu) {
		List<Menu> features = menuRepository.findAll();
		if (CollectionUtils.isNotEmpty(features)) {
			for (Menu feature : features) {
				if (feature.getId() == code) {
					menu = feature;
					break;
				}
			}
		}
		return menu;
	}

	@Override
	public List<Menu> findByKeys(Set<Long> keys) {
		List<Menu> menus = menuMap.entrySet().stream()
				.filter(map -> keys.contains(map.getKey()))
				.map(map -> map.getValue())
				.collect(Collectors.toList());
		if(CollectionUtils.isEmpty(menus)) {
			List<Menu> dbFeatures = menuRepository.findAll();
			if(CollectionUtils.isNotEmpty(dbFeatures)) {
				menus = Lists.newArrayList();
				for(Menu feature : dbFeatures) {
					if(keys.contains(feature.getId())) {
						menus.add(feature);
					}
				}
			}
		}
		return menus;
	}
	
	/**
	 * @param code
	 * @return
	 */
	@Override
	public List<Menu> findByParentCode(Long code) {
		List<Menu> menus = Lists.newArrayList();
		for(Menu menu : menuMap.values()) {
			if(menu.getParentId() == code) {
				menus.add(menu);
			}
		}
		return menus;
	}

	@Override
	public Map<Long, Menu> findAll() {
		return menuMap;
	}

	@Override
	public void update(Menu menu) {
		hashOperations.put(KEY, menu.getId(), menu);
	}

	@Override
	public void delete(Long code) {
		hashOperations.delete(KEY, code);
	}

	@Override
	public void saveAll(Map<Long, Menu> menuMap) {
		hashOperations.putAll(KEY, menuMap);
	}

	@Override
	public void loadCache() {
		menuMap = Maps.newHashMap();
		List<Menu> menus = menuRepository.findAll();
		if(CollectionUtils.isNotEmpty(menus)) {
			for(Menu menu : menus) {
				menuMap.put(menu.getId(), menu);
			}
		}
	}
}
