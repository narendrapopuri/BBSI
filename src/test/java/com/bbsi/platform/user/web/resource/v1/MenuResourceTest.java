package com.bbsi.platform.user.web.resource.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;

import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.exception.ExceptionUtils;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableTriConsumer;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.user.businessservice.intf.MenuBusinessService;
import com.bbsi.platform.user.cache.MenuCacheService;

/**
 * This class contains fetch menu Test Cases related to Menu.
 * 
 * @author anandaluru
 */

public class MenuResourceTest {

	@InjectMocks
	private MenuResource menuController;

	@Mock
	private MenuCacheService menuCacheService;

	@Mock
	private MenuBusinessService menuService;

	@Mock
	private UserPrincipal userPrincipal;

	@SuppressWarnings("unchecked")

	@Before
	public void setUp() throws Exception {
		ExceptionUtils.handleException = Mockito.mock(ThrowableTriConsumer.class);
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * This Test case is used to test get the All Menus
	 * 
	 * @throws ParseException
	 */

	@Test
	public void testFetchMenu() {

		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setEmail("admin@osius.com");
		Mockito.doNothing().when(menuCacheService).loadCache();
		String response = menuController.reload(principal);
		assertNotNull(response);
	}

	@Test
	public void testFetchMenuWithNullDetails() {

		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setEmail("test@osius.com");
		Mockito.doNothing().when(menuCacheService).loadCache();
		String response = menuController.reload(null);
		assertNotNull(response);
		assertEquals(true, response.toString().contains("Success"));
	}

	@Test
	public void testFetchMenuWithEmployeeEmail() {

		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setEmail("test@osius.com");
		Mockito.doNothing().when(menuCacheService).loadCache();
		String response = menuController.reload(principal);
		assertNotNull(response);
		assertEquals(true, response.toString().contains("Success"));
	}

	@Test
	public void testFetchMenuThrowsException() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setEmail("admin@osius.com");
		Mockito.doThrow(BbsiException.class).when(menuCacheService).loadCache();
		String response = menuController.reload(principal);
		assertNotNull(response);
	}
}
