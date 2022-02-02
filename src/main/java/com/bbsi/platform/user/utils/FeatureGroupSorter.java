package com.bbsi.platform.user.utils;

import java.util.Comparator;

import com.bbsi.platform.common.dto.CommonDTO;

public class FeatureGroupSorter implements Comparator<CommonDTO> {
	// sort by name
	@Override
	public int compare(CommonDTO c1, CommonDTO c2) {
		return c1.getName().compareTo(c2.getName());
	}

}
