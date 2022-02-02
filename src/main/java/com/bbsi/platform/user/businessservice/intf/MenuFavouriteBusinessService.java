package com.bbsi.platform.user.businessservice.intf;

import java.util.List;

import com.bbsi.platform.common.user.dto.MenuFavouritesDTO;

/**
 * @author anandaluru
 *
 */
public interface MenuFavouriteBusinessService {

	/**
	 * @param menuFavouritesBO
	 * @return
	 */
	public List<MenuFavouritesDTO> updateMenuFavourite(List<MenuFavouritesDTO> menuFavouritesBO);

	/**
	 * @param userId
	 * @return
	 */
	public List<MenuFavouritesDTO> getMenuFavouritesByUserId(int userId);
}
