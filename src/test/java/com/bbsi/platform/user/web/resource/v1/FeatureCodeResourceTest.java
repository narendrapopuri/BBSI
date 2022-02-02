package com.bbsi.platform.user.web.resource.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.core.GrantedAuthority;

import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.exception.ExceptionUtils;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableTriConsumer;
import com.bbsi.platform.common.user.dto.FeatureCodeDTO;
import com.bbsi.platform.common.user.dto.PrivilegeDTO;
import com.bbsi.platform.common.user.dto.WebformDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.user.businessservice.intf.FeatureCodeBusinessService;
import com.bbsi.platform.user.cache.FeatureCodeCacheService;
import com.bbsi.platform.user.mapper.FeatureCodeMapper;

public class FeatureCodeResourceTest {

	@InjectMocks
	private FeatureCodeResource featureCodeResource;

	@Mock
	private FeatureCodeBusinessService featureCodeBusinessService;

	@Mock
	private FeatureCodeMapper featureCodeMapper;

	@Mock
	private FeatureCodeCacheService featureCodeCacheService;

	@Spy
	private FeatureCodeResource spyFeatureCodeResource;

	private List<FeatureCodeDTO> featureCodeDTOs = new ArrayList<FeatureCodeDTO>();

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		featureCodeDTOs = getFeatureCodeDTOs();
		ExceptionUtils.handleException = Mockito.mock(ThrowableTriConsumer.class);
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {

		featureCodeDTOs = null;
	}

	@Test
	public void testGetAllFeatureCodes() {
		Mockito.doReturn(getFeatureCodeDTOs()).when(featureCodeBusinessService).getAllFeatureCodes();
		List<CommonDTO> actualResponse = featureCodeResource.getAllFeatureCodes();
		assertEquals(0, actualResponse.size());

	}

	@Test
	public void testGetAllFeatureCodesThrowsException() {
		Mockito.doThrow(new IllegalArgumentException()).when(featureCodeBusinessService).getAllFeatures();
		List<CommonDTO> result = featureCodeResource.getAllFeatureCodes();
		assertNull(result);
	}

	@Test
	public void testReload() {

		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909464");
		principal.setEmail("admin@osius.com");
		Mockito.doNothing().when(featureCodeCacheService).loadCache();
		String response = featureCodeResource.reload(principal);
		assertEquals("Success", response);
	}

	@Test
	public void testReloadThrowsException() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909464");
		principal.setEmail("admin@osius.com");
		Mockito.doThrow(BbsiException.class).when(featureCodeCacheService).loadCache();
		String response = featureCodeResource.reload(principal);
		assertEquals("Success", response);

	}

	@Test
	public void testReloadUserPrincipal() {
		Mockito.doNothing().when(featureCodeCacheService).loadCache();
		String response = featureCodeResource.reload(null);
		assertEquals("Success", response);
	}

	@Test
	public void testReloadUser() {
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("909464");
		principal.setEmail("admin@gmail.com");
		Mockito.doNothing().when(featureCodeCacheService).loadCache();
		String response = featureCodeResource.reload(principal);
		assertEquals("Success", response);
	}

	private List<FeatureCodeDTO> getFeatureCodeDTOs() {
		List<FeatureCodeDTO> data = new ArrayList<>();
		FeatureCodeDTO featureCode = new FeatureCodeDTO();
		featureCode.setCode("45004");
		featureCode.setDescription("description");
		featureCode.setId(1l);
		featureCode.setModule("Client");
		featureCode.setName("Company Management");
		List<PrivilegeDTO> privileges = new ArrayList<>();
		PrivilegeDTO privilege = new PrivilegeDTO();
		privilege.setAction("Edit");
		privilege.setCode("ROL_MGMT_USER.VIEW");
		privilege.setDescription("View Users");
		privilege.setFeatureCodeId(45004);
		privilege.setFeatureCodeName("Users");
		privilege.setIsActive(true);
		privilege.setName("ROL_MGMT_USER.VIEW");
		privilege.setServiceRegistryId(1l);
		privilege.setType("VIEW");
		WebformDTO webform = new WebformDTO();
		webform.setDescription("description");
		webform.setId(1l);
		webform.setName("name");
		webform.setType("Users");
		webform.setValue("View Users");
		privilege.setWebform(webform);
		privileges.add(privilege);
		featureCode.setPrivileges(privileges);
		data.add(featureCode);
		return data;
	}

}
