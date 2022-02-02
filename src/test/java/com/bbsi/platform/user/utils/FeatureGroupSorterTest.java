package com.bbsi.platform.user.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.bbsi.platform.common.dto.CommonDTO;

public class FeatureGroupSorterTest {
	
	@InjectMocks
	private FeatureGroupSorter featureGroupSorter;
	

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testcompare() {
		CommonDTO commonDTO1 = new CommonDTO();
		commonDTO1.setCode("code1");
		commonDTO1.setName("name1");
		CommonDTO commonDTO2 = new CommonDTO();
		commonDTO2.setCode("code2");
		commonDTO2.setName("name2");
		int result = featureGroupSorter.compare(commonDTO1, commonDTO2);
		assertEquals(result, -1);
	}

}
