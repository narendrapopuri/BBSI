package com.bbsi.platform.user.web.resource.v1;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.user.cache.MenuCacheService;

/**
 * @author anandaluru
 *
 */
@RestController
@RequestMapping("/v1/menu")
@CrossOrigin
@Logging
public class MenuResource {
	
	@Autowired 
	private MenuCacheService menuCacheService;
	
	@GetMapping(path = "/reload", produces = MediaType.APPLICATION_JSON_VALUE)
	public String reload(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		try {
			if(null != userPrincipal && GenericConstants.SUPER_ADMIN.equals(userPrincipal.getEmail())) {
				menuCacheService.loadCache();
			}
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "reload"), e,
					"Error occured while Reloading");
		}
		return "Success";
	}
	
}
