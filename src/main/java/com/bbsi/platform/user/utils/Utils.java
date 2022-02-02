package com.bbsi.platform.user.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.bbsi.platform.common.dto.CostCenterDTO;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.employee.dto.CustomPersonalDTO;
import com.bbsi.platform.common.enums.BoomiEnum;
import com.bbsi.platform.common.generic.BoomiHelper;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.RestClient;
import com.bbsi.platform.common.generic.WebClientTemplate;
import com.bbsi.platform.exception.business.UnauthorizedAccessException;
import com.bbsi.platform.exception.enums.ErrorCodes;
import com.bbsi.platform.exception.model.ErrorDetails;
import com.google.common.collect.Maps;

public class Utils {
	
	private Utils() {
	}

	public static boolean checkIfEmailHasExtension(String s, String[] extn) {
	    return Arrays.stream(extn).anyMatch(entry -> s.trim().toLowerCase().endsWith(entry));
	}
	
	public static boolean isValidClientCode(UserPrincipal userPrincipal, String clientCode) {
		return !userPrincipal.getClientMap().containsKey(clientCode);
	}
	
	public static String validateEmployeeCodes(CustomPersonalDTO customPersonalDTO, RestClient restClient, BoomiHelper boomiHelper) {
		String jsonResponse = null;
		try {
			String uri = boomiHelper.getUrl(BoomiEnum.EMPLOYEE_INFO);
	        uri = String.format(uri, customPersonalDTO.getClientCode(), customPersonalDTO.getEmployeeCode(), "");
	        jsonResponse = restClient.getForString(uri, Collections.emptyMap(), boomiHelper.getHeaders());
		} catch(Exception e) {
			throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA001, MethodNames.UNAUTHORIZED_ACCESS));
		}
		
		return jsonResponse;
		
	}
}
