package com.bbsi.platform.user.businessservice.impl;

import static com.bbsi.platform.common.exception.ExceptionUtils.handleException;
import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.enums.Enums;
import com.bbsi.platform.common.generic.AuditDetailsUtil;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.user.dto.RbacDTO;
import com.bbsi.platform.exception.business.UnauthorizedAccessException;
import com.bbsi.platform.exception.business.ValidationException;
import com.bbsi.platform.exception.enums.ErrorCodes;
import com.bbsi.platform.exception.model.ErrorDetails;
import com.bbsi.platform.user.businessservice.intf.MappingBusinessService;
import com.bbsi.platform.user.businessservice.intf.RbacBusinessService;
import com.bbsi.platform.user.mapper.RbacMapper;
import com.bbsi.platform.user.model.Mapping;
import com.bbsi.platform.user.model.RbacEntity;
import com.bbsi.platform.user.repository.ClientRoleRepository;
import com.bbsi.platform.user.repository.MappingRepository;
import com.bbsi.platform.user.repository.RbacRepository;
import com.bbsi.platform.user.repository.UsersRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;


/**
 * @author anandaluru
 *
 */
@Logging
@Service
public class RbacBusinessServiceImpl implements RbacBusinessService  {

	@Autowired
	private RbacRepository rbacRepository;

	@Autowired
	private MappingRepository mappingRepository;

	@Autowired
	private RbacMapper rbacMapper;

	@Autowired
	private MappingBusinessService  mappingBusinessService;

	@Autowired
	private AuditDetailsUtil auditDetailsUtil;

	@Autowired
	private ClientRoleRepository clientRoleRepository;

	@Autowired
	private UsersRepository usersRepository;

	private static final String GET_ALL_RBACS = "getAllRbacs";


	@Override
	@Transactional
	public RbacDTO createRbac(RbacDTO rbacDTO, String type, UserPrincipal userPrincipal) {
		RbacEntity rbac = null;
		CommonDTO mapping = new CommonDTO();
		Map<String, List<CommonDTO>> privileges = Maps.newTreeMap();
		Map<String, String> codeMap = Maps.newHashMap();
		try {

			if (type.equals(Enums.UserEnum.COPY_ROLE.toString())
					&& !rbacDTO.getClientCode().equals(userPrincipal.getClientCode())) {
				throw new UnauthorizedAccessException(
						new ErrorDetails(ErrorCodes.UA001, "Unauthorized access!!"));
			}

			rbac = rbacMapper.rbacDTOTORbac(rbacDTO);

			// Before saving, check any record exists with this code
			boolean isCodeAlreadyExists = false;
			if (type.equals(Enums.UserEnum.COPY_ROLE.toString())) {
				isCodeAlreadyExists = rbacRepository.existsByCodeAndTypeAndClientCode(rbac.getCode(), type, rbacDTO.getClientCode());
			} else {
				isCodeAlreadyExists = rbacRepository.existsByCodeAndType(rbac.getCode(), type);
			}
			
			if (isCodeAlreadyExists) {
				throw new ValidationException(new ErrorDetails(ErrorCodes.U001));
			}

			mapping.setCode(rbacDTO.getCode());
			mapping.setName(rbacDTO.getName());
			mapping.setDescription(rbacDTO.getDescription());
			mapping.setStatus(rbacDTO.getStatus());
			mapping.setType(type);
			mapping.setChild(rbacDTO.getMappings());
			updateFeatureMappings(codeMap, privileges, rbacDTO.getMappings());
			long mappingId = mappingBusinessService.saveMapping(mapping, userPrincipal);
			rbac.setMappingId(mappingId);
			rbac.setCreatedBy(userPrincipal.getEmail());
			rbac.setModifiedBy(userPrincipal.getEmail());
			rbac = rbacRepository.save(rbac);

			Map<String, List<String>> auditPrivileges = Maps.newTreeMap();
			for(Entry<String, List<CommonDTO>> key : privileges.entrySet()) {
				List<String> auditList = Lists.newArrayList();
				privileges.get(key.getKey()).forEach(priv -> auditList.add(priv.getType()));
				auditPrivileges.put(codeMap.get(key.getKey()), auditList);
			}
			String privilegesString = new Gson().toJson(auditPrivileges);

			rbacDTO = rbacMapper.rbacTORbacDTO(rbac);

			auditDetailsUtil.buildAuditDetailsAndSave(type, "rbac_id", rbacDTO.getName(), "Role Management", type, userPrincipal, null, privilegesString, Enums.AuditDetailsEventEnum.INSERT.toString(), rbacDTO);

		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "createRbac"), e,
					"Error occured while creating rbac details");
		}
		return rbacDTO;
	}

	@Override
	@Transactional
	public RbacDTO updateRbac(RbacDTO rbacDTO, String type, UserPrincipal userPrincipal) {
		RbacEntity rbac = null;
		CommonDTO mapping = new CommonDTO();
		Map<String, List<CommonDTO>> oldPrivileges = Maps.newTreeMap();
		Map<String, List<CommonDTO>> newPrivileges = Maps.newTreeMap();
		Map<String, String> codeMap = Maps.newHashMap();
		try {
			if (type.equals(Enums.UserEnum.COPY_ROLE.toString())
					&& !rbacDTO.getClientCode().equals(userPrincipal.getClientCode())) {
				throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA001, "Unauthorized access!!"));
			}

			//#29453:client termination accessgroup or role can be edited by VOC user only
			validateUserTypeForClientTerminationRole(rbacDTO.getCode(), userPrincipal.getUserType());
			
			Optional<RbacEntity> optional = rbacRepository.findById(rbacDTO.getId());
			if(optional.isPresent()) {
				RbacEntity dbRbac = optional.get();

				boolean isCodeAlreadyExists = false;
				if (type.equals(Enums.UserEnum.COPY_ROLE.toString())) {
					isCodeAlreadyExists = rbacRepository.existsByCodeAndTypeAndClientCodeAndIdNot(rbacDTO.getCode(), type, rbacDTO.getClientCode(), rbacDTO.getId());
				} else {
					isCodeAlreadyExists = rbacRepository.existsByCodeAndTypeAndIdNot(rbacDTO.getCode(), rbacDTO.getType(), rbacDTO.getId());
				}

				if (isCodeAlreadyExists) {
					throw new ValidationException(new ErrorDetails(ErrorCodes.U001));
				}

				rbac = rbacMapper.rbacDTOTORbac(rbacDTO);
				// When updating role with status inactive, checking any user is assigned with this role.If exists, throw an error.
				handleUpdateValidation(rbac, dbRbac, rbacDTO, type);
				
				mapping.setId(dbRbac.getMappingId());
				mapping.setCode(rbacDTO.getCode());
				mapping.setName(rbacDTO.getName());
				mapping.setDescription(rbacDTO.getDescription());
				mapping.setStatus(rbacDTO.getStatus());
				mapping.setType(type);
				mapping.setChild(rbacDTO.getMappings());
				CommonDTO existingMapping = mappingBusinessService.getMapping(dbRbac.getMappingId(), type, false);
				updateFeatureMappings(codeMap, oldPrivileges, existingMapping.getChild());
				updateFeatureMappings(codeMap, newPrivileges, rbacDTO.getMappings());
				long mappingId = mappingBusinessService.saveMapping(mapping, userPrincipal);
				rbac.setMappingId(mappingId);
				rbac.setVersion(dbRbac.getVersion());
				rbac.setModifiedBy(userPrincipal.getEmail());
				rbac.setModifiedOn(LocalDateTime.now());
				rbac = rbacRepository.save(rbac);

				Map<String, List<String>> auditNewPrivileges = Maps.newTreeMap();
				for(Entry<String, List<CommonDTO>> key : newPrivileges.entrySet()) {
					List<String> auditList = Lists.newArrayList();
					newPrivileges.get(key.getKey()).forEach(priv -> auditList.add(priv.getType()));
					auditNewPrivileges.put(codeMap.get(key.getKey()), auditList);
				}
				String newPrivilegesString = new Gson().toJson(auditNewPrivileges);

				Map<String, List<String>> auditOldPrivileges = Maps.newTreeMap();
				Set<String> removedFeatures = Sets.newHashSet();
				Map<String, List<String>> reducedFeatureMap = Maps.newHashMap();
				populateRemovedFeatures(oldPrivileges, newPrivileges, codeMap, auditOldPrivileges, removedFeatures, reducedFeatureMap);
				
				String oldPrivilegesString = new Gson().toJson(auditOldPrivileges);

				updateRbacPrivileges(removedFeatures, reducedFeatureMap, rbacDTO, type, dbRbac);
				rbacDTO = rbacMapper.rbacTORbacDTO(rbac);

				auditDetailsUtil.buildAuditDetailsAndSave(type, "rbac_id", rbacDTO.getName(), "Role Management", type, userPrincipal, oldPrivilegesString, newPrivilegesString, Enums.AuditDetailsEventEnum.UPDATE.toString(), rbacDTO);
			}
		} catch (Exception e) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "updateRbac"), e,
					"Error occured while creating rbac details");
		}
		return rbacDTO;
	}
	
	private void validateUserTypeForClientTerminationRole(String code, String userType) {
		if ((GenericConstants.TERMINATED_CLIENT_ROLE_CODE.equalsIgnoreCase(code) || GenericConstants.TERMINATED_CLIENT_ACCESSGROUP_CODE.equalsIgnoreCase(code))
			&& !GenericConstants.USERTYPE_VANCOUVER.equalsIgnoreCase(userType)) {
		throw new UnauthorizedAccessException(new ErrorDetails(ErrorCodes.UA001, "Unauthorized access!!"));
	}}
	
	private void updateRbacPrivileges(Set<String> removedFeatures, Map<String, List<String>> reducedFeatureMap, RbacDTO rbacDTO, String type, RbacEntity dbRbac) {
		if(CollectionUtils.isNotEmpty(removedFeatures) || !reducedFeatureMap.isEmpty()) {
			List<Long> parentIds = fetchParentIdList(rbacDTO, type, dbRbac);
			if(CollectionUtils.isNotEmpty(parentIds)) {
				if(CollectionUtils.isNotEmpty(removedFeatures)) {
					mappingBusinessService.updatePrivileges(rbacDTO.getCode(), parentIds, removedFeatures);
				}
				if(!reducedFeatureMap.isEmpty()) {
					mappingBusinessService.updatePrivileges(rbacDTO.getCode(), parentIds, reducedFeatureMap);
				}
			}
		}
	}
	
	private List<Long> fetchParentIdList(RbacDTO rbacDTO, String type, RbacEntity dbRbac) {
		List<Long> parentIds = Lists.newArrayList();
		if (type.equals(Enums.UserEnum.ACCESS_GROUP.toString())) {
			List<RbacEntity> rbacs = rbacRepository.findByType(Enums.UserEnum.ROLE.toString());
			Map<Long, Long> roleMap = Maps.newHashMap();
			rbacs.forEach(entity -> roleMap.put(entity.getMappingId(), entity.getId()));
			List<Mapping> roles = mappingRepository.findByParentIdIn(roleMap.keySet());
			if(CollectionUtils.isNotEmpty(roles)) {
				for(Mapping role : roles) {
					if(role.getCode().equals(rbacDTO.getCode())) {
						parentIds.add(role.getParentId());
						populatePayrentIdList(roleMap.get(role.getParentId()), parentIds);
					}
				}
			}
		} else if (type.equals(Enums.UserEnum.ROLE.toString())) {
			populatePayrentIdList(dbRbac.getId(), parentIds);
		}
		return parentIds;
	}
	
	private void populatePayrentIdList(Long parentId, List<Long> parentIds) {
		List<RbacEntity> clientRoles = rbacRepository.findByParentId(parentId);
		if(CollectionUtils.isNotEmpty(clientRoles)) {
			clientRoles.forEach(entity -> parentIds.add(entity.getMappingId()));
		}
	}
	
	private void populateRemovedFeatures(Map<String, List<CommonDTO>> oldPrivileges, Map<String, List<CommonDTO>> newPrivileges, Map<String, String> codeMap, 
			Map<String, List<String>> auditOldPrivileges, Set<String> removedFeatures, Map<String, List<String>> reducedFeatureMap) {
		for(Entry<String, List<CommonDTO>> key : oldPrivileges.entrySet()) {
			List<String> auditList = Lists.newArrayList();
			oldPrivileges.get(key.getKey()).forEach(priv -> auditList.add(priv.getType()));
			auditOldPrivileges.put(codeMap.get(key.getKey()), auditList);
			if(!newPrivileges.containsKey(key.getKey())) {
				removedFeatures.add(key.getKey());
			} else if(!newPrivileges.get(key.getKey()).containsAll(oldPrivileges.get(key.getKey()))) {
				List<String> removedList = Lists.newArrayList();
				for(CommonDTO priv : oldPrivileges.get(key.getKey())) {
					List<String> pCodes = newPrivileges.get(key.getKey()).stream().map(CommonDTO::getCode).collect(Collectors.toList());
					if(!pCodes.contains(priv.getCode())) {
						removedList.add(priv.getCode());
					}
				}
				if(CollectionUtils.isNotEmpty(removedList)) {
					reducedFeatureMap.put(key.getKey(), removedList);
				}
			}
		}
	}
	
	private void handleUpdateValidation(RbacEntity rbac, RbacEntity dbRbac, RbacDTO rbacDTO, String type) {
		handleUpdateValidationForNotAccessGroup(rbac,dbRbac, type);
		handleUpdateValidationForAccessGroup(rbac, rbacDTO, type);
	}
	
	private void handleUpdateValidationForAccessGroup(RbacEntity rbac, RbacDTO rbacDTO, String type) {
		if (null != rbac.getStatus() && rbac.getStatus().equals(Boolean.FALSE) && type.equals(Enums.UserEnum.ACCESS_GROUP.toString())) {
			List<RbacEntity> rbacs = rbacRepository.findByType(Enums.UserEnum.ROLE.toString());
			List<Long> roleList = Lists.newArrayList();
			rbacs.forEach(entity -> roleList.add(entity.getMappingId()));
			List<Mapping> roles = mappingRepository.findByParentIdIn(roleList);
			if(CollectionUtils.isNotEmpty(roles)) {
				for(Mapping role : roles) {
					if(role.getCode().equals(rbacDTO.getCode())) {
						throw new ValidationException(new ErrorDetails(ErrorCodes.UPG001));
					}
				}
			}
		}
	}
	
	private void handleUpdateValidationForNotAccessGroup(RbacEntity rbac, RbacEntity dbRbac, String type) {
		if (null != rbac.getStatus() && rbac.getStatus().equals(Boolean.FALSE) && !type.equals(Enums.UserEnum.ACCESS_GROUP.toString())) {
			List<Object[]> userIdList = usersRepository.findUsersByRoleId(rbac.getId());
			if(CollectionUtils.isNotEmpty(userIdList)) {
				throw new ValidationException(new ErrorDetails(ErrorCodes.UR001));
			}
			boolean isMappingIdExists = clientRoleRepository.existsByRole_Id(dbRbac.getMappingId());
			if (isMappingIdExists) {
				throw new ValidationException(new ErrorDetails(ErrorCodes.UR001));
			}
		}
	}

	@Async
	public void updateFeatureMappings(Map<String, String> codeMap, Map<String, List<CommonDTO>> map, List<CommonDTO> mappings) {
		for (CommonDTO clientMapping : mappings) {
			List<CommonDTO> privileges = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(clientMapping.getPrivileges())) {
				verifyParentPrivileges(clientMapping);
				for (CommonDTO clientPrivilege : clientMapping.getPrivileges()) {
					if (null != clientPrivilege.getIsSelected() && clientPrivilege.getIsSelected()) {
						privileges.add(clientPrivilege);
					}
				}
				map.put(clientMapping.getCode(), privileges);
				codeMap.put(clientMapping.getCode(), clientMapping.getName());
			}
			if (CollectionUtils.isNotEmpty(clientMapping.getChild())) {
				updateFeatureMappings(codeMap, map, clientMapping.getChild());
			}
		}
	}

	private void verifyParentPrivileges(CommonDTO clientMapping) {
		boolean hasPrivilegeSelected = false;
		for (CommonDTO clientPrivilege : clientMapping.getPrivileges()) {
			if(null != clientPrivilege.getIsSelected()
					&& clientPrivilege.getIsSelected()) {
				hasPrivilegeSelected = true;
				if(null != clientPrivilege.getType()
						&& clientPrivilege.getType().equals("ALL")) {
					clientMapping.getPrivileges().forEach(priv -> priv.setIsSelected(Boolean.TRUE));
				}
			}
		}
		verifyParentPrivileges(clientMapping, hasPrivilegeSelected);
	}

	private void verifyParentPrivileges(CommonDTO clientMapping, boolean hasPrivilegeSelected) {
		if (!hasPrivilegeSelected && CollectionUtils.isNotEmpty(clientMapping.getChild())) {
			for(CommonDTO childDTO : clientMapping.getChild()) {
				hasPrivilegeSelected = updateDTOPrivilege(clientMapping, childDTO, hasPrivilegeSelected);
				if(!hasPrivilegeSelected && CollectionUtils.isNotEmpty(childDTO.getChild())) {
					for(CommonDTO subChildDTO : childDTO.getChild()) {
						hasPrivilegeSelected = updateDTOPrivilege(clientMapping, subChildDTO, hasPrivilegeSelected);
					}
				}
			}
		}
	}

	private boolean updateDTOPrivilege(CommonDTO clientMapping, CommonDTO childDTO, boolean hasPrivilegeSelected) {
		for(CommonDTO privilege : childDTO.getPrivileges()) {
			if(null != privilege.getIsSelected()
					&& privilege.getIsSelected()) {
				for(CommonDTO parentPrivilege : clientMapping.getPrivileges()) {
					if(null != parentPrivilege.getType()
							&& parentPrivilege.getType().equals("VIEW")) {
						parentPrivilege.setIsSelected(Boolean.TRUE);
						hasPrivilegeSelected = true;
					}
				}
			}
		}
		return hasPrivilegeSelected;
	}

	@Override
	public List<RbacDTO> getAllRbacs(UserPrincipal userPrincipal, String type) { 
		List<RbacEntity> rbacList = null;
		List<RbacDTO> rbacDTOList = null;
		try {
			rbacList = rbacRepository.findByType(type);
			if (Enums.UserEnum.ROLE.toString().equals(type)) {
				List<RbacEntity> copyRolerbacList = rbacRepository.findByClientCodeAndType(userPrincipal.getClientCode(), Enums.UserEnum.COPY_ROLE.toString());
				if (CollectionUtils.isNotEmpty(copyRolerbacList)) {
					rbacList.addAll(copyRolerbacList);
				}
			}
			if (CollectionUtils.isNotEmpty(rbacList)) {
				rbacDTOList = rbacMapper.rbacListToRbacDTOList(rbacList);
				rbacDTOList=rbacDTOList.stream().filter(rbac->isViewAndEditable(userPrincipal.getUserType(), rbac)).collect(Collectors.toList());
				rbacDTOList.sort((p1, p2) -> p2.getModifiedOn().compareTo(p1.getModifiedOn()));
				
			}
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), GET_ALL_RBACS), exception,
					"Error occured while getting All rbacs");
		}

		return rbacDTOList;
	}

	private boolean isViewAndEditable(String userType, RbacDTO rbacDTO) {
		boolean allowViewAndEdit = false;
		if(GenericConstants.USERTYPE_VANCOUVER.equalsIgnoreCase(userType) && null!=rbacDTO.getIsVocEditable() && rbacDTO.getIsVocEditable()) {
			allowViewAndEdit = true;
		}
		else if(GenericConstants.USERTYPE_BRANCH.equalsIgnoreCase(userType) && null!=rbacDTO.getIsBranchEditable() && rbacDTO.getIsBranchEditable()) {
			allowViewAndEdit = true;
		}
		else if(GenericConstants.USERTYPE_CLIENT.equalsIgnoreCase(userType) && null!=rbacDTO.getIsClientEditable() && rbacDTO.getIsClientEditable()) {
			allowViewAndEdit = true;
		}
		return allowViewAndEdit;
	}
	
	@Override
	public List<RbacDTO> getClientRoles(String clientCode, UserPrincipal userPrincipal) {
		List<RbacEntity> rbacList = null;
		List<RbacDTO> rbacDTOList = null;
		try {
			rbacList = rbacRepository.findByType(Enums.UserEnum.ROLE.toString());
			if (CollectionUtils.isNotEmpty(rbacList)) {
				rbacDTOList = rbacMapper.rbacListToRbacDTOList(rbacList);
				rbacDTOList.sort((p1, p2) -> p2.getModifiedOn().compareTo(p1.getModifiedOn()));
				List<String> dbRoles = rbacDTOList.stream().map(RbacDTO::getCode).collect(Collectors.toList());

				CommonDTO roleMapping = mappingBusinessService.getMapping(clientCode, Enums.UserEnum.CLIENT, false, false, userPrincipal);
				if(CollectionUtils.isNotEmpty(roleMapping.getChild())) {
					for(CommonDTO role : roleMapping.getChild()) {
						if(!dbRoles.contains(role.getCode())) {
							RbacDTO copyRole = new RbacDTO();
							copyRole.setCode(role.getCode());
							copyRole.setName(role.getName());
							copyRole.setDescription(role.getDescription());
							copyRole.setType(Enums.UserEnum.COPY_ROLE.toString());
							copyRole.setMappings(Lists.newArrayList(role));
							rbacDTOList.add(copyRole);
						}
					}
				}
			}
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), GET_ALL_RBACS), exception,
					"Error occured while getting All rbacs");
		}

		return rbacDTOList;
	}

	@Override
	public List<RbacDTO> getAllClientRoles(UserPrincipal userPrincipal) {
		List<RbacEntity> rbacList = null;
		List<RbacDTO> rbacDTOList = null;
		try {
			rbacList = rbacRepository.findByIsClientTemplateAndStatusAndTypeNot(Boolean.TRUE, Boolean.TRUE, Enums.UserEnum.COPY_ROLE.toString());
			if (CollectionUtils.isNotEmpty(rbacList)) {
				rbacDTOList = rbacMapper.rbacListToRbacDTOList(rbacList);
				rbacDTOList=rbacDTOList.stream().filter(rbac->isVocAndClientTermRole(rbac.getCode(), userPrincipal.getUserType())).collect(Collectors.toList());
				rbacDTOList.sort((p1, p2) -> p2.getModifiedOn().compareTo(p1.getModifiedOn()));
			}
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), GET_ALL_RBACS), exception,
					"Error occured while getting All client specific roles");
		}

		return rbacDTOList;
	}

	private boolean isVocAndClientTermRole(String code, String userType) {
		boolean isVocAndTemRole=false;
		if(GenericConstants.TERMINATED_CLIENT_ROLE_CODE.equalsIgnoreCase(code) || GenericConstants.TERMINATED_CLIENT_ACCESSGROUP_CODE.equalsIgnoreCase(code)) {
			if(GenericConstants.USERTYPE_VANCOUVER.equalsIgnoreCase(userType))
				isVocAndTemRole=true;
			else
				isVocAndTemRole=false;
		}
		else
			isVocAndTemRole=true;
		return isVocAndTemRole;
	}

	@Override
	public List<RbacDTO> getAllRoles(boolean clientOnly, String clientCode) {
		List<RbacEntity> rbacList = null;
		List<RbacDTO> rbacDTOList = null;
		try {
			// if the flag clientOnly = true, then return only client copied roles
			if (clientOnly) {
				rbacList = rbacRepository.findByClientCodeAndType(clientCode, Enums.UserEnum.COPY_ROLE.toString());
			} else {
				// returns clientTemplate only true + client copied roles
				rbacList = rbacRepository.findByIsClientTemplateOrClientCodeAndType(Boolean.TRUE,  clientCode, Enums.UserEnum.COPY_ROLE.toString());
			}
			if (CollectionUtils.isNotEmpty(rbacList)) {
				rbacDTOList = rbacMapper.rbacListToRbacDTOList(rbacList);

				rbacDTOList.sort((p1, p2) -> p2.getModifiedOn().compareTo(p1.getModifiedOn()));
			}
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "GET_ALL_ROLES"), exception,
					"Error occured while getting All client specific roles");
		}

		return rbacDTOList;
	}

	@Override
	public RbacDTO getEntriesByCode(String code, Enums.UserEnum type, boolean selected, boolean clearIds, UserPrincipal userPrincipal) {
		RbacDTO rbacDTO = null;
		try {
			RbacEntity rbacentity = rbacRepository.findByCodeAndType(code, type.toString());
			if (null != rbacentity) {
				rbacDTO = rbacMapper.rbacTORbacDTO(rbacentity);
				CommonDTO mappingDTO = mappingBusinessService.getMapping(code, type, selected, clearIds, userPrincipal);
				if(null != mappingDTO && CollectionUtils.isNotEmpty(mappingDTO.getChild())) {
					rbacDTO.setMappings(mappingDTO.getChild());
				}
			}
		} catch (Exception exception) {
			handleException.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "getAllEntriesByName"), exception,
					"Error occured while getting All rbacs by name");
		}
		return rbacDTO;
	}
}
