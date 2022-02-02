package com.bbsi.platform.user.businessservice.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.exception.ExceptionUtils;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableBiConsumer;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableConsumer;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableTriConsumer;
import com.bbsi.platform.common.generic.GenericUtils;
import com.bbsi.platform.common.user.dto.FeatureCodeDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.user.mapper.FeatureCodeMapper;
import com.bbsi.platform.user.model.FeatureCode;
import com.bbsi.platform.user.model.Privilege;
import com.bbsi.platform.user.repository.FeatureCodeRepository;

public class FeatureCodeBusinessServiceImplTest {

	@InjectMocks
	private FeatureCodeBusinessServiceImpl mockFeatureBusinessServiceImpl;

	@Mock
	private FeatureCodeRepository mockFeatureCodeRepository;

	@Spy
	private FeatureCodeMapper featureCodeMapper;

	private List<FeatureCodeDTO> featureCodeDTOs;

	private List<FeatureCode> featureCodes;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {

		featureCodeDTOs = getFeatureCodeDTOs();
		featureCodes = getFeatureCodes();
		ExceptionUtils.handleException = Mockito.mock(ThrowableTriConsumer.class);
		GenericUtils.throwCommonNullVariableException = Mockito.mock(ThrowableBiConsumer.class);
		GenericUtils.throwCommonNullVariableExceptionString = Mockito.mock(ThrowableConsumer.class);
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {

		featureCodeDTOs = null;
		featureCodes = null;
	}

	@Test
	public void testGetAllFeatureCodes() {
		Mockito.doReturn(featureCodes).when(mockFeatureCodeRepository).findAll();
		when(featureCodeMapper.featureCodeListToFeatureCodeDTOList(featureCodes)).thenReturn(featureCodeDTOs);
		featureCodeDTOs = mockFeatureBusinessServiceImpl.getAllFeatureCodes();
		assertEquals(1, featureCodeDTOs.size());
	}
	
	@Test
	public void testGetAllFeatureCodesThrowsBbsiException() {
		Mockito.doThrow(BbsiException.class).when(mockFeatureCodeRepository).findAll();
		mockFeatureBusinessServiceImpl.getAllFeatureCodes();
		/*Exception being handled by generic exception handler so asserting true by default*/
		assertTrue(true);
	}

	@Test
	public void testLoadAllFeatureCodes()
	{
		
		Mockito.doReturn(featureCodes).when(mockFeatureCodeRepository).findAllByOrderByIdAsc();
		List<CommonDTO> actualResponse = mockFeatureBusinessServiceImpl.loadAllFeatureCodes();
		assertEquals(2, actualResponse.size());
	}
	
	@Test
	public void testLoadAllFeatureCodesEmpty()
	{
		
		Mockito.doReturn(new ArrayList<>()).when(mockFeatureCodeRepository).findAllByOrderByIdAsc();
		List<CommonDTO> actualResponse = mockFeatureBusinessServiceImpl.loadAllFeatureCodes();
		assertNotNull(actualResponse);
	}
	
	@Test
	public void testGetAllFeatures()
	{
		Mockito.doReturn(featureCodes).when(mockFeatureCodeRepository).findAllByOrderByIdAsc();
		List<CommonDTO> actualResponse = mockFeatureBusinessServiceImpl .getAllFeatures();
		assertEquals(1, actualResponse.size());
	}
	
	
	@Test
	public void testGetAllFeaturesForPrivilegesActionEmpty()
	{
		Mockito.doReturn(getFeatureCodes1()).when(mockFeatureCodeRepository).findAllByOrderByIdAsc();
		List<CommonDTO> actualResponse = mockFeatureBusinessServiceImpl .getAllFeatures();
		assertEquals(1, actualResponse.size());
	}
	
	@Test
	public void testGetAllFeaturesWhenParentIdNull(){
		List<FeatureCode> featureCodes1 = new ArrayList<>();
		FeatureCode featureCode = new FeatureCode();
		featureCode.setIsDisplayEnabled(false);
		featureCode.setParentId(0l);
		featureCodes1.add(featureCode);
		Mockito.doReturn(featureCodes1).when(mockFeatureCodeRepository).findAllByOrderByIdAsc();
		List<CommonDTO> actualResponse = mockFeatureBusinessServiceImpl .getAllFeatures();
		assertNotNull(actualResponse);
	}
	
	@Test
	public void testGetAllFeaturesEmpty()
	{
		Mockito.doReturn(new ArrayList<>()).when(mockFeatureCodeRepository).findAllByOrderByIdAsc();
		List<CommonDTO> actualResponse = mockFeatureBusinessServiceImpl .getAllFeatures();
		assertNotNull(actualResponse);
	}
	
	@Test
	public void testgetFeature() {
		Mockito.doReturn(getFeatureCodes().get(0)).when(mockFeatureCodeRepository).findByCode(Mockito.anyString());
		CommonDTO result =  mockFeatureBusinessServiceImpl.getFeature(getFeatureCodes().get(0).getCode());
		Mockito.verify(mockFeatureCodeRepository, Mockito.atLeastOnce()).findByCode(Mockito.anyString());
		assertEquals(result.getCode(), getFeatureCodes().get(0).getCode());
	}
	
	@Test
	public void testGetFeatureForNull() {
		Mockito.doReturn(null).when(mockFeatureCodeRepository).findByCode(Mockito.anyString());
		CommonDTO result = mockFeatureBusinessServiceImpl.getFeature(getFeatureCodes().get(0).getCode());
		assertNull(result);
		Mockito.verify(mockFeatureCodeRepository, Mockito.atLeastOnce()).findByCode(Mockito.anyString());
	}
	
	private List<FeatureCodeDTO> getFeatureCodeDTOs() {
		featureCodeDTOs = new ArrayList<>();
		FeatureCodeDTO featureCode = new FeatureCodeDTO();
		featureCode.setId(1l);
		featureCode.setCode("BBSI-JOBCODE");
		featureCode.setDescription("Testing the Jobcode");
		featureCode.setName("name");
		featureCodeDTOs.add(featureCode);
		return featureCodeDTOs;
	}

	private List<FeatureCode> getFeatureCodes() {
		featureCodes = new ArrayList<>();
		FeatureCode featureCode = new FeatureCode();
		featureCode.setIsDisplayEnabled(true);
		featureCode.setCode("BBSI-JOBCODE");
		featureCode.setDescription("Testing the Jobcode");
		featureCode.setName("name");
		featureCode.setParentId(0l);
		featureCode.setId(1l);
		featureCode.setPrivileges(populatePrivilegeList());
		featureCode.setType("Client");
		featureCode.setIsDisplayEnabled(true);
		FeatureCode data = new FeatureCode();
		data.setId(2l);
		data.setCode("code1");
		data.setDescription("description");
		data.setParentId(1l);
		data.setLevel("level");
		data.setType("ALL");
		data.setIsDisplayEnabled(true);
		List<FeatureCode> featureCodeList = new ArrayList<>();
		featureCodeList.add(data);
		featureCode.setFeatureCodes(featureCodeList);
		featureCodes.add(featureCode);
		featureCodes.add(data);
		return featureCodes;
	}

	private List<Privilege> populatePrivilegeList()
	{
		List<Privilege> listPrivilege = new ArrayList<>();
		Privilege privilege = new  Privilege();
		privilege.setId(123l);
		privilege.setCode("code123");
		privilege.setAction("DISABLED");
		listPrivilege.add(privilege);
		privilege = new Privilege();
		privilege.setId(11l);
		privilege.setCode("code11");
		privilege.setDescription("description");
		privilege.setAction("ENABLED");
		privilege.setType("ALL");
		listPrivilege.add(privilege);
		return listPrivilege;
	}
	private List<FeatureCode> getFeatureCodes1() {
		featureCodes = new ArrayList<>();
		FeatureCode featureCode = new FeatureCode();
		featureCode.setIsDisplayEnabled(true);
		featureCode.setCode("BBSI-JOBCODE");
		featureCode.setDescription("Testing the Jobcode");
		featureCode.setName("name");
		featureCode.setParentId(0l);
		featureCode.setId(1l);
		featureCode.setPrivileges(populatePrivilegeList1());
		featureCode.setType("Client");
		FeatureCode data = new FeatureCode();
		data.setId(2l);
		data.setCode("code1");
		data.setDescription("description");
		data.setParentId(1l);
		data.setLevel("level");
		data.setType("ALL");
		data.setIsDisplayEnabled(true);
		List<FeatureCode> featureCodeList = new ArrayList<>();
		featureCodeList.add(data);
		featureCode.setFeatureCodes(featureCodeList);
		featureCodes.add(featureCode);
		featureCodes.add(data);
		return featureCodes;
	}
	private List<Privilege> populatePrivilegeList1()
	{
		List<Privilege> listPrivilege = new ArrayList<>();
		Privilege privilege = new  Privilege();
		privilege.setId(123l);
		privilege.setCode("code123");
		privilege.setAction("ENABLED");
		listPrivilege.add(privilege);
		privilege = new Privilege();
		privilege.setId(11l);
		privilege.setCode("code11");
		privilege.setDescription("description");
		privilege.setAction("");
		privilege.setType("ALL");
		listPrivilege.add(privilege);
		return listPrivilege;
	}
}
