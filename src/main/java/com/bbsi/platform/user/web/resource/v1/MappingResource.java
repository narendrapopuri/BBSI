package com.bbsi.platform.user.web.resource.v1;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.annotation.Secured;
import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.enums.Enums;
import com.bbsi.platform.exception.business.ValidationException;
import com.bbsi.platform.exception.enums.ErrorCodes;
import com.bbsi.platform.exception.model.ErrorDetails;
import com.bbsi.platform.user.businessservice.intf.MappingBusinessService;
import com.bbsi.platform.user.utils.MethodNames;
import com.google.common.collect.Lists;

@RestController
@RequestMapping("/v1/mapping")
@CrossOrigin
@Logging
public class MappingResource {

	@Autowired
	private MappingBusinessService mappingBusinessService;

	/**
	 * API to get the mapping details based on code and type.
	 * 
	 * @param code
	 * @param type
	 * @return
	 */
	@Secured
	@GetMapping(path = "/code/{code}/type/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonDTO getMapping(@PathVariable("code") String code, 
			@PathVariable("type") Enums.UserEnum type, @RequestParam(name = "selected", required = false) boolean selected,
			@RequestParam(name = "flag", required = false) boolean flag,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		CommonDTO commonDTO = null;
		try {
			commonDTO = mappingBusinessService.getMapping(code, type, selected, flag, userPrincipal);
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_MAPPING),
					exception,
					String.format("Error occured while getting mapping for code %s  and type : %s", code, type));
		}
		return commonDTO;
	}
	
	/**
	 * API to get the mapping details based on id and type.
	 * 
	 * @param code
	 * @param type
	 * @return
	 */
	@Secured
	@GetMapping(path = "/id/{id}/type/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CommonDTO getMappingById(@PathVariable("id") Long id, 
			@PathVariable("type") Enums.UserEnum type, @RequestParam(name = "selected", required = false) boolean selected) {
		CommonDTO commonDTO = null;
		try {
			commonDTO = mappingBusinessService.getMapping(id, type.toString(), selected);
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_MAPPING),
					exception,
					String.format("Error occured while getting mapping for id %s  and type : %s", id, type));
		}
		return commonDTO;
	}
	
	/**
	 * API to get the mapping details based on code and type.
	 * 
	 * @param code
	 * @param type
	 * @return
	 */
	@Secured
	@DeleteMapping(path = "/code/{code}/type/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteMapping(@PathVariable("code") String code, @PathVariable("type") Enums.UserEnum type, 
			@RequestParam(name = "parentId", required = false) Long parentId) {
		try {
			mappingBusinessService.deleteMapping(code, type, parentId);
		} catch (ValidationException exception) {
			throw exception;
		} catch (Exception exception) {
			throw new ValidationException(new ErrorDetails(ErrorCodes.UR001));
		}
	}
	
	/**
	 * API to get the mapping details based on id.
	 * 
	 * @param id
	 * @return
	 */
	@Secured
	@DeleteMapping(path = "id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean deleteMappingById(@PathVariable("id") Long id) {
		try {
			mappingBusinessService.deleteMapping(id);
			return true;
		} catch (ValidationException exception) {
			throw exception;
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_MAPPING),
					exception,
					String.format("Error occured while deleting mapping for id: %s", id));
		}
		return false;
	}
	
	/**
	 * API to get the mapping details based on codes and type.
	 * 
	 * @param codes
	 * @param type
	 * @return
	 */
	@Secured
	@GetMapping(path = "/type/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CommonDTO> getMappings(@PathVariable("type") Enums.UserEnum type,
			@RequestParam(name = "codes") List<String> codes,
			@RequestParam(name = "selected", required = false) boolean selected,
			@RequestParam(name = "flag", required = false) boolean flag,
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		List<CommonDTO> commonDTOs = Lists.newArrayList();
		try {
			commonDTOs = mappingBusinessService.getMappings(codes, type, selected, flag, userPrincipal);
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), MethodNames.GET_MAPPING),
					exception,
					String.format("Error occured while getting mapping for codes %s  and type : %s", codes, type));
		}
		return commonDTOs;
	}

}
