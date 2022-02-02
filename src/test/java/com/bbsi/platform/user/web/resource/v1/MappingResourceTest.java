package com.bbsi.platform.user.web.resource.v1;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.enums.Enums;
import com.bbsi.platform.common.enums.Enums.UserEnum;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.exception.business.ValidationException;
import com.bbsi.platform.user.businessservice.intf.MappingBusinessService;
import com.google.common.collect.Lists;

public class MappingResourceTest {
	
	@InjectMocks
	private MappingResource mappingResource;
	
	@Mock
	private MappingBusinessService mappingBusinessService;
	
	@Mock
	private CommonDTO commonDTO;
	
	@Mock
	private UserPrincipal userPrincipal;

	@Before
	public void setUp() throws Exception {
		userPrincipal = new UserPrincipal("admin@osius.com", "password", Lists.newArrayList());
		userPrincipal.setUserType(GenericConstants.USERTYPE_CLIENT.toString());
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetMapping() {
		Mockito.doReturn(commonDTO).when(mappingBusinessService).getMapping(Mockito.anyString(), Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		CommonDTO response = mappingResource.getMapping("BBSI", Enums.UserEnum.CLIENT, true, true, userPrincipal);
		assertNotNull(response);
	}
	
	@Test
	public void testGetMappingWhenNotSelected() {
		Mockito.doReturn(commonDTO).when(mappingBusinessService).getMapping(Mockito.anyString(), Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		CommonDTO response = mappingResource.getMapping("BBSI", Enums.UserEnum.CLIENT, false, false, userPrincipal);
		assertNotNull(response);
	}
	
	@Test
	public void testGetMappingWhenCodeIsNotFound() {
		
		commonDTO = null;
		Mockito.doReturn(commonDTO).when(mappingBusinessService).getMapping(Mockito.anyString(), Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		CommonDTO response =mappingResource.getMapping("BBSI", Enums.UserEnum.ACCESS_GROUP, true, true, userPrincipal);
		assertNull(response);
	}
	
	@Test
	public void testGetMappingWhenCodeIsNull() {
		commonDTO = null;
		Mockito.doThrow(BbsiException.class).when(mappingBusinessService).getMapping(Mockito.anyString(), Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		CommonDTO response =mappingResource.getMapping("BBSI",Enums.UserEnum.ACCESS_GROUP,true, true, null);
		assertNull(response);
	}

	@Test
	public void testGetMappingById() {
		long id = 1;
		Mockito.doReturn(commonDTO).when(mappingBusinessService).getMapping(Mockito.anyLong(), Mockito.anyString(), Mockito.anyBoolean());
		CommonDTO actualResponse = mappingResource.getMappingById(id, Enums.UserEnum.CLIENT, true);
		assertNotNull(actualResponse);
	}
	
	@Test
	public void testGetMappingByIdWhenIdIsNotCorrect() {
		long id=-1;
		Mockito.doThrow(BbsiException.class).when(mappingBusinessService).getMapping(Mockito.anyLong(), Mockito.anyString(), Mockito.anyBoolean());
		CommonDTO actualResponse = mappingResource.getMappingById(id,Enums.UserEnum.CLIENT, true);
		assertNull(actualResponse);
	}
	
	@Test
	public void testDeleteMapping() {
		long id=1;
		Mockito.doNothing().when(mappingBusinessService).deleteMapping(Mockito.anyString(), Mockito.any(), Mockito.anyLong());
		mappingResource.deleteMapping("BBSI", Enums.UserEnum.CLIENT, id);
		assertTrue(true);
	}
	
	@Test(expected = ValidationException.class)
	public void testDeleteMappingException() {
		Mockito.doThrow(ValidationException.class).when(mappingBusinessService).deleteMapping(Mockito.anyString(), Mockito.any(), Mockito.anyLong());
		mappingResource.deleteMapping(" ", null, 0l);
		assertTrue(true);
	}
	
	@Test(expected = ValidationException.class)
	public void testDeleteMappingThrowsException() {
		long id=1;
		Mockito.doThrow(NullPointerException.class).when(mappingBusinessService).deleteMapping(Mockito.anyString(), Mockito.any(), Mockito.anyLong());
		mappingResource.deleteMapping("BBSI", Enums.UserEnum.CLIENT, id);
	   fail("Throwing Validation Exception"); 
	}
	
	@Test
	public void testDeleteMappingExceptionForBbsiException() {
		Mockito.doThrow(BbsiException.class).when(mappingBusinessService).deleteMapping(Mockito.anyString(), Mockito.any(), Mockito.anyLong());
		mappingResource.deleteMapping(null, null, 0l);
		assertTrue(true);
	}

	@Test
	public void testGetMappings() {
		List<CommonDTO> commonDTOList = new ArrayList<CommonDTO>();
		List<String> codeList = new ArrayList<String>();
		codeList.add("909464");
		Mockito.doReturn(commonDTOList).when(mappingBusinessService).getMappings(Mockito.any(), Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		List<CommonDTO> resonse = mappingResource.getMappings(UserEnum.CLIENT, codeList, false, false, userPrincipal);
		assertNotNull(resonse);
	}
	
	@Test
	public void testGetMappingsThrowsException() {
		List<String> codeList = new ArrayList<String>();
		codeList.add("909464");
		Mockito.doThrow(BbsiException.class).when(mappingBusinessService).getMappings(Mockito.any(), Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		List<CommonDTO> resonse = mappingResource.getMappings(UserEnum.CLIENT, codeList, false, false, userPrincipal);
		assertNotNull(resonse);
	}
	
	@Test
	public void testDeleteMappingById()
	{
		Mockito.doNothing().when(mappingBusinessService).deleteMapping(Mockito.anyLong());
		Boolean value = mappingResource.deleteMappingById(12l);
		assertNotNull(value);
	}
	
	@Test
	public void testDeleteMappingByIdThrowsException()
	{
		Mockito.doThrow(BbsiException.class).when(mappingBusinessService).deleteMapping(Mockito.anyLong());
		Boolean value =  mappingResource.deleteMappingById(-1l);
		assertNotNull(value);
	}
	
	@Test(expected=ValidationException.class)
	public void testDeleteMappingByIdThrowsValidationException()
	{
		Mockito.doThrow(ValidationException.class).when(mappingBusinessService).deleteMapping(Mockito.anyLong());
		Boolean value = mappingResource.deleteMappingById(0l);
		assertNotNull(value);
	}
}
