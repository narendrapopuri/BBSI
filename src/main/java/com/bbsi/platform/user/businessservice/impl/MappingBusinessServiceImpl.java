package com.bbsi.platform.user.businessservice.impl;

import static com.bbsi.platform.common.generic.GenericUtils.basicMethodInfo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.common.annotation.Logging;
import com.bbsi.platform.common.dto.CommonDTO;
import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.enums.Enums;
import com.bbsi.platform.common.enums.Enums.UserEnum;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.LogUtils;
import com.bbsi.platform.exception.business.UnauthorizedAccessException;
import com.bbsi.platform.exception.business.ValidationException;
import com.bbsi.platform.exception.enums.ErrorCodes;
import com.bbsi.platform.exception.model.ErrorDetails;
import com.bbsi.platform.user.businessservice.intf.MappingBusinessService;
import com.bbsi.platform.user.cache.FeatureCodeCacheService;
import com.bbsi.platform.user.model.ClientRole;
import com.bbsi.platform.user.model.Mapping;
import com.bbsi.platform.user.model.PrivilegeMapping;
import com.bbsi.platform.user.model.RbacEntity;
import com.bbsi.platform.user.repository.ClientRoleRepository;
import com.bbsi.platform.user.repository.MappingRepository;
import com.bbsi.platform.user.repository.PrivilegeMappingRepository;
import com.bbsi.platform.user.repository.RbacRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author OSI
 *
 */
@Logging
@Service
public class MappingBusinessServiceImpl implements MappingBusinessService {

	@Autowired
	private MappingRepository mappingRepository;

	@Autowired
	private PrivilegeMappingRepository privilegeMappingRepository;

	@Autowired
	private RbacRepository rbacRepository;

	@Autowired
	private ClientRoleRepository clientRoleRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private FeatureCodeCacheService featureCodeCacheService;

	@Transactional(readOnly = true)
	public CommonDTO getMapping(String code, Enums.UserEnum type, boolean selected, boolean clearIds, UserPrincipal principal) {
		CommonDTO mapping = new CommonDTO();
		mapping.setNewStatus(false);
		Mapping dbMapping = getMapping(code, type.toString(), principal.getClientCode());
		if(null != dbMapping) {
			populateDTO(mapping, dbMapping, selected);
			if((selected || type.toString().equals(Enums.UserEnum.ROLE.toString())) && (null != dbMapping.getModifiedOn())
					&& !type.toString().equals(Enums.UserEnum.ACCESS_GROUP.toString())) {
				List<Mapping> mappingList = mappingRepository.findByParentId(dbMapping.getId());
				if(CollectionUtils.isNotEmpty(mappingList)) {
					if(mapping.getChild() == null) {
						mapping.setChild(Lists.newArrayList());
					}
					for(Mapping childMapping : mappingList) {
						Mapping refMapping = getExistingMapping(dbMapping, childMapping, type);
						
						boolean flag = selected;
						if(UserEnum.COPY_ROLE.toString().equals(childMapping.getType())) {
							flag = false;
						}
						CommonDTO child = new CommonDTO();
						child = getMapping(childMapping.getId(), type.toString(), flag);

						updateChildMapping(mapping, child, refMapping, type, clearIds, principal);

						mapping.getChild().add(child);
					}
				}
			} else {
				mapping = getMapping(dbMapping.getId(), type.toString(), selected);
				if(!selected && type.toString().equals(Enums.UserEnum.ACCESS_GROUP.toString())){
					CommonDTO newData=new CommonDTO();
					List<CommonDTO> featureCodes = featureCodeCacheService.getFeatureCodes();
					if(CollectionUtils.isNotEmpty(featureCodes)){
						newData.setChild(featureCodes);
						clearIds(newData);
						populateUpdatedData(newData, mapping);
					}
				} else if(type.toString().equals(Enums.UserEnum.ROLE.toString()) && !principal.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER)) {
					clearIsSelected(mapping);
				}
			}
		}
		return mapping;
	}
	
	private Mapping getMapping(final String code, final String type, final String clientCode) {
		RbacEntity rbacEntity = null;
		if(type.equals(Enums.UserEnum.ROLE.toString()) || type.equals(Enums.UserEnum.ACCESS_GROUP.toString())) {
			rbacEntity = rbacRepository.findByCodeAndType(code, type);
		} else {
			rbacEntity = rbacRepository.findByCodeAndTypeAndClientCode(code, type, clientCode);
		}
		if(null != rbacEntity) {
			Optional<Mapping> optional = mappingRepository.findById(rbacEntity.getMappingId());
			return optional.isPresent() ? optional.get() : null;
		}
		return null;
	}
	
	private Mapping getExistingMapping(Mapping dbMapping, Mapping childMapping, Enums.UserEnum type) {
		Mapping refMapping = null;
		if(type.toString().equals(Enums.UserEnum.ROLE.toString())) {
			refMapping = mappingRepository.findFirstByCodeAndTypeAndModifiedOnGreaterThanOrderByModifiedOnDesc(childMapping.getCode(), Enums.UserEnum.ACCESS_GROUP.toString(), dbMapping.getModifiedOn());
			// When AccessGroup is inactive, in role also, the status should be same
			RbacEntity agRbac = rbacRepository.findByCodeAndType(childMapping.getCode(), Enums.UserEnum.ACCESS_GROUP.toString());
			if(null != agRbac && null != refMapping) {
				refMapping.setStatus(agRbac.getStatus());
			}
		} else if(type.toString().equals(Enums.UserEnum.COPY_ROLE.toString())) {
			refMapping = mappingRepository.findFirstByCodeAndTypeAndModifiedOnGreaterThanOrderByModifiedOnDesc(childMapping.getCode(), Enums.UserEnum.ROLE.toString(), dbMapping.getModifiedOn());
		}
		return refMapping;
	}
	
	private void updateChildMapping(CommonDTO mapping, CommonDTO child, Mapping refMapping, Enums.UserEnum type, boolean clearIds, UserPrincipal principal) {
		if((mapping.getNewStatus() && type.toString().equals(Enums.UserEnum.ROLE.toString())) ||
				(type.toString().equals(Enums.UserEnum.ROLE.toString()) && !principal.getUserType().equals(GenericConstants.USERTYPE_VANCOUVER))) {
			clearIsSelected(child);
		}

		CommonDTO newData = null;
		if(null != refMapping) {
			mapping.setNewStatus(true);
			newData = getMapping(refMapping.getId(), refMapping.getType(), true);
			if(null != newData) {
				newData.setStatus(refMapping.getStatus());
			}
		}
		if(null != newData && mapping.getNewStatus()) {
			populateUpdatedData(newData, child);
			child.setStatus(newData.getStatus());
		}
		if(clearIds || mapping.getNewStatus()) {
			clearIds(child);
		}
	}

	private void populateUpdatedData(CommonDTO newData, CommonDTO existing) {
		if(null != newData && CollectionUtils.isNotEmpty(newData.getChild())) {
			if(CollectionUtils.isEmpty(existing.getChild())) {
				existing.setChild(newData.getChild());
				existing.getChild().forEach(feature -> {
					if(CollectionUtils.isNotEmpty(feature.getPrivileges())) {
						updateSelectedFlag(feature);
					}
				});
			} else {
				List<CommonDTO> newChild = Lists.newArrayList();
				boolean doSort = addUpdatedChildData(newData, existing, newChild);
				if(doSort) {
					sortFeatures(newChild);
				}
				existing.setChild(newChild);
			}
		}
	}
	
	private boolean addUpdatedChildData(CommonDTO newData, CommonDTO existing, List<CommonDTO> newChild) {
		boolean doSort = false;
		for(CommonDTO child : newData.getChild()) {
			boolean isPresent = populateUpdatedChildData(newChild, child, existing);					
			if(!isPresent) {
				if(CollectionUtils.isNotEmpty(child.getPrivileges())) {
					updateSelectedFlag(child);
					newChild.add(child);
					doSort = true;
				}
				updateChildSelectionFlag(child);
			}
		}
		return doSort;
	}
	
	private boolean populateUpdatedChildData(List<CommonDTO> newChild, CommonDTO child, CommonDTO existing) {
		for(CommonDTO feature : existing.getChild()) {
			if(feature.getCode().equals(child.getCode())) {
				if(CollectionUtils.isNotEmpty(child.getPrivileges())) {
					if(CollectionUtils.isEmpty(feature.getPrivileges())) {
						feature.setPrivileges(child.getPrivileges());
						updateSelectedFlag(feature);
					} else {
						checkAndUpdateFlag(feature, child);
					}
				}
				populateUpdatedData(child, feature);
				newChild.add(feature);
				return true;
			}
		}
		return false;
	}

	private void sortFeatures(List<CommonDTO> newChild) {
		try {
			newChild.forEach(feature -> {
				CommonDTO featureCodeDetails = featureCodeCacheService.find(feature.getCode());
				if(null != featureCodeDetails) {
					feature.setSeqNum(featureCodeDetails.getSeqNum());
				} else {
					feature.setSeqNum(0L);
				}
			});
			Collections.sort(newChild, Comparator.comparingLong(CommonDTO::getSeqNum));
		} catch(Exception e) {
			LogUtils.basicErrorLog.accept(basicMethodInfo.apply(getClass().getCanonicalName(), "sortFeatures"), e.getMessage());
		}
	}

	private void checkAndUpdateFlag(CommonDTO feature, CommonDTO child) {
		for(CommonDTO childPriv : child.getPrivileges()) {
			for(CommonDTO existingPriv : feature.getPrivileges()) {
				if(existingPriv.getCode().equals(childPriv.getCode())
						&& (null != childPriv.getIsEditable() && childPriv.getIsEditable().equals(Boolean.TRUE))
						&& (null != existingPriv.getIsEditable() && existingPriv.getIsEditable().equals(Boolean.FALSE))) {
					existingPriv.setIsEditable(Boolean.TRUE);
				}
			}
		}
	}

	private void updateChildSelectionFlag(CommonDTO child) {
		if(CollectionUtils.isNotEmpty(child.getChild())) {
			child.getChild().forEach(childFeature -> {
				if(CollectionUtils.isNotEmpty(childFeature.getPrivileges())) {
					updateSelectedFlag(childFeature);
					if(CollectionUtils.isNotEmpty(childFeature.getChild())) {
						childFeature.getChild().forEach(subFeature -> {
							if(CollectionUtils.isNotEmpty(subFeature.getPrivileges())) {
								updateSelectedFlag(subFeature);
							}
						});
					}
				}
			});
		}
	}

	private void updateSelectedFlag(CommonDTO feature) {
		feature.getPrivileges().forEach(priv -> {
			if(null != priv.getIsSelected() && priv.getIsSelected().equals(Boolean.FALSE)) {
				priv.setIsEditable(Boolean.FALSE);
			}
			priv.setIsSelected(Boolean.FALSE);
		});
	}

	private void clearIsSelected(CommonDTO dto) {
		if(CollectionUtils.isNotEmpty(dto.getPrivileges())) {
			dto.getPrivileges().forEach(priv -> {
				if(null != priv.getIsSelected() && priv.getIsSelected().equals(Boolean.FALSE)) {
					priv.setIsEditable(Boolean.FALSE);
				}
				clearIsSelected(priv);
			});
		}
		if(CollectionUtils.isNotEmpty(dto.getChild())) {
			dto.getChild().forEach(child -> {
				clearIsSelected(child);
			});
		}
	}

	private void clearIds(CommonDTO dto) {
		dto.setId(0);
		dto.setParentId(0);
		if(CollectionUtils.isNotEmpty(dto.getPrivileges())) {
			dto.getPrivileges().forEach(priv -> {
				clearIds(priv);
			});
		}
		if(CollectionUtils.isNotEmpty(dto.getChild())) {
			dto.getChild().forEach(child -> {
				clearIds(child);
			});
		}
	}

	@Transactional(readOnly = true)
	public CommonDTO getMapping(Long id, String type, boolean selected) {
		CommonDTO mapping = new CommonDTO();
		Optional<Mapping> optional = mappingRepository.findById(id);
		if(optional.isPresent()) {
			List<Long> ids = Lists.newArrayList();
			populateDTO(mapping, optional.get());
			ids.add(mapping.getId());
			List<Object[]> mappings = mappingRepository.findMappingHierarchy(mapping.getId());
			if(CollectionUtils.isNotEmpty(mappings)) {
				List<CommonDTO> mappingDTOs = Lists.newArrayList();
				for(Object[] record : mappings) {
					buildMappingDTOs(mappingDTOs, record);
				}
				sortFeatures(mappingDTOs);
				prepareHierarchy(mapping, mappingDTOs, ids);
			} else {
				populateChildNodes(mapping, ids);
			}
			List<Object[]> privilegeList = fetchPrivileges(ids);
			populatePrivileges(mapping, privilegeList, selected);
		}
		return mapping;
	}

	private void buildMappingDTOs(List<CommonDTO> mappingDTOs, Object[] record) {
		CommonDTO elemDTO = new CommonDTO();
		elemDTO.setId(Long.parseLong(record[0].toString()));
		elemDTO.setCode(record[1].toString());
		if(null != record[2]) {
			elemDTO.setName(record[2].toString());
		}
		if(null != record[3]) {
			elemDTO.setDescription(record[3].toString());
		}
		if(null != record[4]) {
			elemDTO.setStatus(Boolean.valueOf(record[4].toString()));
		}
		if(null != record[5]) {
			elemDTO.setIsEditable(Boolean.valueOf(record[5].toString()));
		}
		if(null != record[6]) {
			elemDTO.setType(record[6].toString());
		}
		if(null != record[7]) {
			elemDTO.setParentId(Long.parseLong(record[7].toString()));
		}
		mappingDTOs.add(elemDTO);
	}

	private List<Object[]> fetchPrivileges(List<Long> ids) {
		List<Object[]> privilegeList = Lists.newArrayList();
		while(ids.size() > 2000) {
			List<Long> temp = ids.subList(0, 2000);
			privilegeList.addAll(privilegeMappingRepository.getPrivileges(temp));
			ids = ids.subList(2000, ids.size());
		}
		privilegeList.addAll(privilegeMappingRepository.getPrivileges(ids));
		return privilegeList;
	}

	private void prepareHierarchy(CommonDTO parent, List<CommonDTO> mappingList, List<Long> ids) {
		if(CollectionUtils.isNotEmpty(mappingList)) {
			List<CommonDTO> removeList = Lists.newArrayList();
			for(CommonDTO dbMapping : mappingList) {
				if(dbMapping.getParentId() == parent.getId()) {
					ids.add(dbMapping.getId());
					removeList.add(dbMapping);
					parent.getChild().add(dbMapping);
				}
			}
			mappingList.removeAll(removeList);
			if(CollectionUtils.isNotEmpty(parent.getChild())) {
				for(CommonDTO child : parent.getChild()) {
					prepareHierarchy(child, mappingList, ids);
				}
			}
		}
	}

	private void populateDTO(CommonDTO element, Mapping dbElement) {
		element.setId(dbElement.getId());
		element.setCode(dbElement.getCode());
		element.setName(dbElement.getName());
		element.setDescription(dbElement.getDescription());
		element.setStatus(dbElement.getStatus());
		element.setIsEditable(dbElement.getIsReplica());
		element.setType(dbElement.getType());
	}

	private void populateChildNodes(CommonDTO parent, List<Long> ids) {
		if(parent.getId() == 0) {
			return;
		}
		List<Mapping> mappingList = mappingRepository.findByParentId(parent.getId());
		if(CollectionUtils.isNotEmpty(mappingList)) {
			if(parent.getChild() == null) {
				parent.setChild(Lists.newArrayList());
			}
			for(Mapping dbMapping : mappingList) {
				CommonDTO child = new CommonDTO();
				populateDTO(child, dbMapping);
				ids.add(child.getId());
				child.setParentId(parent.getId());
				parent.getChild().add(child);
			}
			if(CollectionUtils.isNotEmpty(parent.getChild())) {
				for(CommonDTO child : parent.getChild()) {
					populateChildNodes(child, ids);
				}
			}
		}
	}

	private void populatePrivileges(CommonDTO parent, List<Object[]> privilegeList, boolean selected) {
		if(parent.getId() == 0) {
			return;
		}
		if(CollectionUtils.isNotEmpty(parent.getChild())) {
			List<CommonDTO> filteredList = getFilteredPrivilegeList(parent, privilegeList, selected);
			parent.setChild(filteredList);
			if(CollectionUtils.isNotEmpty(parent.getChild())) {
				for(CommonDTO child : parent.getChild()) {
					populatePrivileges(child, privilegeList, selected);
				}
				filteredList = Lists.newArrayList();
				for(CommonDTO child : parent.getChild()) {
					if(!selected || CollectionUtils.isNotEmpty(child.getChild())) {
						filteredList.add(child);
					} else if(selected && CollectionUtils.isEmpty(child.getChild())) {
						boolean isSelected = false;
						for(CommonDTO privDTO : child.getPrivileges()) {
							if(null != privDTO.getIsSelected() && privDTO.getIsSelected()) {
								isSelected = true;
								break;
							}
						}
						if(isSelected) {
							filteredList.add(child);
						}
					}
				}
				parent.setChild(filteredList);
			}
		}
	}
	
	private List<CommonDTO> getFilteredPrivilegeList(CommonDTO parent, List<Object[]> privilegeList, boolean selected) {
		List<CommonDTO> filteredList = Lists.newArrayList();
		for(CommonDTO child : parent.getChild()) {
			child.setPrivileges(Lists.newArrayList());
			boolean hasPrivileges = false;
			for(Object[] privileges : privilegeList) {
				if(null != privileges[6]) {
					Long featureId = Long.parseLong(privileges[6].toString());
					if(featureId.equals(child.getId())) {
						if(!selected ||
								(selected && privileges[4] != null
										&& privileges[4].equals(Boolean.TRUE))) {
							hasPrivileges = true;
						}
						CommonDTO privDTO = new CommonDTO();
						privDTO.setId(Long.parseLong(privileges[0].toString()));
						privDTO.setCode(privileges[1].toString());
						if(null != privileges[2]) {
							privDTO.setName(privileges[2].toString());
						}
						if(null != privileges[3]) {
							privDTO.setType(privileges[3].toString());
						}
						privDTO.setIsSelected(Boolean.valueOf(privileges[4].toString()));
						if(null != privileges[5]) {
							privDTO.setIsEditable(Boolean.valueOf(privileges[5].toString()));
						}
						child.getPrivileges().add(privDTO);
					}
				}
			}
			if(hasPrivileges || CollectionUtils.isNotEmpty(child.getChild())) {
				filteredList.add(child);
			}
		}
		return filteredList;
	}

	@Transactional(readOnly = true)
	public List<CommonDTO> getMappings(List<String> codeList, Enums.UserEnum type, boolean selected, boolean clearIds, UserPrincipal principal) {
		List<CommonDTO> mappings = Lists.newArrayList();
		for(String code : codeList) {
			CommonDTO mapping = getMapping(code, type, selected, clearIds, principal);
			clearIds(mapping);
			mappings.add(mapping);
		}

		return mappings;
	}

	private boolean populateDTO(CommonDTO element, Mapping dbElement, boolean selected) {
		element.setId(dbElement.getId());
		element.setCode(dbElement.getCode());
		element.setName(dbElement.getName());
		element.setDescription(dbElement.getDescription());
		element.setStatus(dbElement.getStatus());
		element.setIsEditable(dbElement.getIsReplica());
		element.setType(dbElement.getType());

		//populating createdOn property in copy role list view only.
		if(dbElement.getType().equals(Enums.UserEnum.COPY_ROLE.toString())) {
			element.setCreatedOn(dbElement.getCreatedOn());
		}
		List<PrivilegeMapping> privileges = privilegeMappingRepository.findByMapping_Id(dbElement.getId());
		boolean hasPrivileges = false;
		if(CollectionUtils.isNotEmpty(privileges)) {
			for(PrivilegeMapping privilege : privileges) {
				if(!selected ||
						(selected && privilege.getIsSelected() != null
								&& privilege.getIsSelected().equals(Boolean.TRUE))) {
					hasPrivileges = true;
				}
				CommonDTO privDTO = new CommonDTO();
				privDTO.setId(privilege.getId());
				privDTO.setCode(privilege.getCode());
				privDTO.setName(privilege.getName());
				privDTO.setType(privilege.getType());
				privDTO.setIsSelected(privilege.getIsSelected());
				privDTO.setIsEditable(privilege.getIsEditable());
				element.getPrivileges().add(privDTO);
			}
		} else {
			hasPrivileges = true;
		}
		return hasPrivileges;
	}

	private Mapping populateMapping(Mapping element, CommonDTO elementDTO, String type) {
		element.setId(elementDTO.getId());
		element.setCode(elementDTO.getCode());
		element.setName(elementDTO.getName());
		element.setDescription(elementDTO.getDescription());
		element.setStatus(elementDTO.getStatus());
		element.setIsReplica(elementDTO.getIsEditable());
		element.setModifiedOn(LocalDateTime.now());
		if(elementDTO.getParentId() > 0) {
			element.setParentId(elementDTO.getParentId());
		}
		if(StringUtils.isEmpty(elementDTO.getType())
				|| (!elementDTO.getType().equals(Enums.UserEnum.ACCESS_GROUP.toString())
				&& !elementDTO.getType().equals(Enums.UserEnum.ROLE.toString())
				&& !elementDTO.getType().equals(Enums.UserEnum.CLIENT.toString())
				&& !elementDTO.getType().equals(Enums.UserEnum.COPY_ROLE.toString()))) {
			elementDTO.setType(type);
		}
		element.setType(elementDTO.getType());
		if(element.getPrivileges() == null) {
			element.setPrivileges(Lists.newArrayList());
		}
		if(CollectionUtils.isNotEmpty(elementDTO.getPrivileges())) {
			populatePrivileges(element, elementDTO);
		}
		element = mappingRepository.save(element);
		if(CollectionUtils.isNotEmpty(elementDTO.getChild())) {
			if(element.getMappings() == null) {
				element.setMappings(Lists.newArrayList());
			}
			type = elementDTO.getCode()+type;
			for(CommonDTO childDTO : elementDTO.getChild()) {
				Mapping child = new Mapping();
				childDTO.setParentId(element.getId());
				populateMapping(child, childDTO, type);
				element.getMappings().add(child);
			}
			mappingRepository.saveAll(element.getMappings());
		}
		return element;
	}
	
	private void populatePrivileges(Mapping element, CommonDTO elementDTO) {
		boolean hasPrivilegeSelected = false;
		for(CommonDTO privilege : elementDTO.getPrivileges()) {
			PrivilegeMapping dbPrivilege = new PrivilegeMapping();
			dbPrivilege.setId(privilege.getId());
			dbPrivilege.setCode(privilege.getCode());
			dbPrivilege.setName(privilege.getName());
			dbPrivilege.setType(privilege.getType());
			dbPrivilege.setIsSelected(privilege.getIsSelected());
			dbPrivilege.setIsEditable(privilege.getIsEditable());
			dbPrivilege.setMapping(element);
			element.getPrivileges().add(dbPrivilege);
			if(null != privilege.getIsSelected()
					&& privilege.getIsSelected()
					&& null != privilege.getType()
					&& privilege.getType().equals("ALL")) {
				element.getPrivileges().forEach(priv -> priv.setIsSelected(Boolean.TRUE));
				elementDTO.getPrivileges().forEach(priv -> priv.setIsSelected(Boolean.TRUE));
			}
			if(null != privilege.getIsSelected()
					&& privilege.getIsSelected()) {
				hasPrivilegeSelected = true;
			}
		}
		if(!hasPrivilegeSelected && CollectionUtils.isNotEmpty(elementDTO.getChild())) {
			verifyChildPrivileges(element, elementDTO, hasPrivilegeSelected);
		}
	}
	
	private void verifyChildPrivileges(Mapping element, CommonDTO elementDTO, boolean hasPrivilegeSelected) {
		for(CommonDTO childDTO : elementDTO.getChild()) {
			hasPrivilegeSelected = updateChildPrivileges(element, childDTO);
			if(!hasPrivilegeSelected && CollectionUtils.isNotEmpty(childDTO.getChild())) {
				for(CommonDTO subChildDTO : childDTO.getChild()) {
					hasPrivilegeSelected = updateChildPrivileges(element, subChildDTO);
				}
			}
		}
	}
	
	private boolean updateChildPrivileges(Mapping element, CommonDTO childDTO) {
		boolean hasPrivilegeSelected = false;
		for(CommonDTO privilege : childDTO.getPrivileges()) {
			if(null != privilege.getIsSelected()
					&& privilege.getIsSelected()) {
				for(PrivilegeMapping parentPrivilege : element.getPrivileges()) {
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

	@Transactional
	public long saveMapping(CommonDTO mappingDTO, UserPrincipal userPrincipal) {

		Set<String> existingPrivileges = userPrincipal.getPrivilegeCodes();
		Set<String> newPrivileges = getPrivileges(mappingDTO);

		if (!existingPrivileges.containsAll(newPrivileges)) {
			throw new UnauthorizedAccessException(
					new ErrorDetails(ErrorCodes.UA001, "Unauthorized access!!"));
		}

		Mapping mapping = new Mapping();
		// Delete existing mapping
		Map<String, List<ClientRole>> clientRoleMap = Maps.newHashMap();
		if(mappingDTO.getId() > 0) {
			Optional<Mapping> optional = mappingRepository.findById(mappingDTO.getId());
			if(optional.isPresent()) {
				mappingDTO.setParentId(null != optional.get().getParentId() ? optional.get().getParentId() : 0);
			}
			
			updateMappingReference(mappingDTO.getId(), clientRoleMap);
			if(CollectionUtils.isNotEmpty(mappingDTO.getChild())) {
				for(CommonDTO child : mappingDTO.getChild()) {
					child.setParentId(mappingDTO.getId());
				}
			}
			validateExistingMapping(mappingDTO);
		}
		String type = mappingDTO.getCode();
		if(StringUtils.isNotEmpty(mappingDTO.getType())) {
			type += mappingDTO.getType();
		}
		mapping = populateMapping(mapping, mappingDTO, type);
		if(mappingDTO.getId() > 0 && !clientRoleMap.isEmpty()) {
			updateRoleMapping(mapping, clientRoleMap);
		}
		return mapping.getId();
	}

	private Set<String> getPrivileges(CommonDTO commonDTO) {
		Set<String> privileges = new HashSet<>();

		for (CommonDTO privDTO : commonDTO.getPrivileges()) {
			if (null != privDTO.getIsSelected() && privDTO.getIsSelected()) {
				privileges.add(privDTO.getCode());
				if(CollectionUtils.isNotEmpty(privDTO.getChild())) {
					getPrivileges(privDTO);
				}
			}
		}
		return privileges;
	}

	@Transactional
	public void validateExistingMapping(CommonDTO mappingDTO) {
		List<Long> existingList = Lists.newArrayList();
		List<Long> newList = Lists.newArrayList();
		List<Mapping> dbList = mappingRepository.findByParentId(mappingDTO.getId());
		if(CollectionUtils.isNotEmpty(dbList)) {
			dbList.forEach(elem -> {
				existingList.add(elem.getId());
			});
		}
		if(CollectionUtils.isNotEmpty(mappingDTO.getChild())) {
			for(CommonDTO child : mappingDTO.getChild()) {
				newList.add(child.getId());
			}
		}
		existingList.forEach(id -> {
			if(!newList.contains(id)) {
				try {
					List<Long> ids = Lists.newArrayList();
					ids.add(id);
					List<Object[]> mappings = mappingRepository.findMappingHierarchy(id);
					if(CollectionUtils.isNotEmpty(mappings)) {
						getMappingIdsList(ids, mappings);
					}

					deleteMappings(ids);
				} catch(Exception e) {
					LogUtils.basicErrorLog.accept(
							basicMethodInfo.apply(getClass().getCanonicalName(), "validateExistingMapping"),
							e.getMessage());
				}
			}
		});
		try {
			entityManager.flush();
		} catch (Exception exception) {
			LogUtils.basicErrorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), "validateExistingMapping"),
					exception.getMessage());
		}
	}

	private void getMappingIdsList(List<Long> ids, List<Object[]> mappings) {
		for(Object[] record : mappings) {
			ids.add(Long.parseLong(String.valueOf(record[0])));
		}
	}

	public void deleteMappings(List<Long> ids) {
		List<Mapping> mappings = mappingRepository.findByIdIn(ids);
		mappingRepository.deleteAll(mappings);
	}

	//@Transactional
	public void deleteMapping(Long mappingId) {
		String type = null;
		try {
			Optional<Mapping> mapping = mappingRepository.findById(mappingId);
			if(mapping.isPresent()) {
				type = mapping.get().getType();
			}
			mappingRepository.deleteById(mappingId);
		} catch(Exception e) {
			ErrorCodes code = ErrorCodes.UR0014;
			if(StringUtils.isNotEmpty(type) && type.equals(Enums.UserEnum.ROLE.toString())) {
				code = ErrorCodes.UR001;
			}
			throw new ValidationException(new ErrorDetails(code));
		}
	}

	@Transactional
	public void updateRoleMapping(Mapping mapping, Map<String, List<ClientRole>> clientRoleMap) {
		for(Mapping dbMapping : mapping.getMappings()) {
			if(clientRoleMap.containsKey(dbMapping.getCode())) {
				List<ClientRole> mappedRoles = clientRoleMap.get(dbMapping.getCode());
				List<ClientRole> newRoles = Lists.newArrayList();
				mappedRoles.forEach(role -> {
					ClientRole clientRole = new ClientRole();
					clientRole.setClientCode(role.getClientCode());
					clientRole.setRole(dbMapping);
					clientRole.setUserClient(role.getUserClient());
					newRoles.add(clientRole);
				});
				clientRoleRepository.saveAll(newRoles);
			}
		}
	}

	@Transactional
	public void updateMappingReference(Long mappingId, Map<String, List<ClientRole>> clientRoleMap) {
		List<Mapping> mappingList = mappingRepository.findByParentId(mappingId);
		if(CollectionUtils.isNotEmpty(mappingList)) {
			for(Mapping dbMapping : mappingList) {
				List<ClientRole> mappedRoles = clientRoleRepository.findByRole(dbMapping);
				if(CollectionUtils.isNotEmpty(mappedRoles)) {
					clientRoleMap.put(dbMapping.getCode(), mappedRoles);
					clientRoleRepository.deleteAll(mappedRoles);
					try {
						mappingRepository.deleteById(dbMapping.getId());
					} catch(Exception e) {
						ErrorCodes code = ErrorCodes.UR0014;
						code = getErrorCodeRoleIsAlreadyAssigned(dbMapping, code);
						throw new ValidationException(new ErrorDetails(code));
					}
				}
			}
		}
	}

	private ErrorCodes getErrorCodeRoleIsAlreadyAssigned(Mapping dbMapping, ErrorCodes code) {
		if(StringUtils.isNotEmpty(dbMapping.getType())
				&& dbMapping.getType().equals(Enums.UserEnum.ROLE.toString())) {
			code = ErrorCodes.UR001;
		}
		return code;
	}

	@Transactional
	public void deleteMapping(String code, Enums.UserEnum type, Long parentId) {
		if (null != parentId && parentId > 0) {
			Mapping map = mappingRepository.findByCodeAndTypeAndParentId(code, type.toString(), parentId);
			List<Long> ids = Lists.newArrayList();
			ids.add(map.getId());
			List<Object[]> objectList = mappingRepository.findMappingHierarchy(map.getId());
			if (CollectionUtils.isNotEmpty(objectList)) {
				for (Object[] record : objectList) {
					ids.add(Long.parseLong(String.valueOf(record[0])));
				}
			}
			mappingRepository.deleteByIdIn(ids);
		} else {
			if(Enums.UserEnum.COPY_ROLE.equals(type)) {
				deleteCopyRole(code, type);
			} else {
				List<Mapping> mappingList = mappingRepository.findByCodeAndTypeOrderByModifiedOnDesc(code, type.toString());
				deleteMappingByCodeAndType(mappingList, code, type);
			}
		}
	}
	
	private void deleteMappingByCodeAndType(List<Mapping> mappingList, String code, Enums.UserEnum type) {
		if(CollectionUtils.isNotEmpty(mappingList)) {
			if(mappingList.size() == 1 && (mappingList.get(0).getParentId() == null)) {
				deleteMappingHierarchy(code, type);
			} else if(mappingList.size() > 1) {
				deleteMappingWithRole(mappingList, code, type);
			}
		}
	}
	
	private void deleteMappingWithRole(List<Mapping> mappingList, String code, Enums.UserEnum type) {
		boolean removeAll = true;
		for(Mapping dbMapping : mappingList) {
			if (null != dbMapping.getParentId() && dbMapping.getParentId() > 0) {
				RbacEntity entity = rbacRepository.findByMappingId(dbMapping.getParentId());
				if (null != entity) {
					removeAll = false;
					break;
				}
			}
			List<ClientRole> roles = clientRoleRepository.findByRole_Id(dbMapping.getId());
			if(CollectionUtils.isNotEmpty(roles)) {
				removeAll = false;
				break;
			}
		}
		if(removeAll) {
			mappingRepository.deleteByCodeAndType(code, type.toString());
		} else {
			ErrorCodes errCode = ErrorCodes.UR0014;
			if(StringUtils.isNotEmpty(type.toString()) && type.toString().equals(Enums.UserEnum.ROLE.toString())) {
				errCode = ErrorCodes.UR001;
			}
			throw new ValidationException(new ErrorDetails(errCode));
		}
	}
	
	private void deleteMappingHierarchy(String code, Enums.UserEnum type) {
		try {
			List<Mapping> mappings = mappingRepository.findByCodeAndType(code, type.toString());
			if (CollectionUtils.isNotEmpty(mappings)) {
				for (Mapping map : mappings) {
					List<Long> ids = Lists.newArrayList();
					ids.add(map.getId());
					List<Object[]> objectList = mappingRepository.findMappingHierarchy(map.getId());
					if (CollectionUtils.isNotEmpty(objectList)) {
						for (Object[] record : objectList) {
							ids.add(Long.parseLong(String.valueOf(record[0])));
						}
					}
					mappingRepository.deleteByIdIn(ids);
				}
			}
		} catch(Exception e) {
			ErrorCodes errCode = ErrorCodes.UR0014;
			if(StringUtils.isNotEmpty(type.toString()) && type.toString().equals(Enums.UserEnum.ROLE.toString())) {
				errCode = ErrorCodes.UR001;
			}
			throw new ValidationException(new ErrorDetails(errCode));
		}
	}
	
	private void deleteCopyRole(String code, Enums.UserEnum type) {
		try {
			List<Mapping> mappings = mappingRepository.findByCodeAndType(code, type.toString());
			if (CollectionUtils.isNotEmpty(mappings)) {
				for (Mapping map : mappings) {
					List<Long> ids = Lists.newArrayList();
					ids.add(map.getId());
					List<Object[]> objectList = mappingRepository.findMappingHierarchy(map.getId());
					if (CollectionUtils.isNotEmpty(objectList)) {
						for (Object[] record : objectList) {
							ids.add(Long.parseLong(String.valueOf(record[0])));
						}
					}
					mappingRepository.deleteByIdIn(ids);
				}
			}
		} catch(Exception e) {
			throw new ValidationException(new ErrorDetails(ErrorCodes.UR0014));
		}
	}

	@Transactional(readOnly = true)
	public List<CommonDTO> getPrivileges(Long id, Enums.UserEnum type) {
		List<CommonDTO> privilegeDTOs = Lists.newArrayList();
		Optional<Mapping> optional = mappingRepository.findById(id);
		if(optional.isPresent()) {
			Mapping dbMapping = optional.get();
			List<Long> ids = Lists.newArrayList();
			ids.add(dbMapping.getId());
			List<Object[]> mappings = mappingRepository.findMappingHierarchy(dbMapping.getId());
			if(CollectionUtils.isNotEmpty(mappings)) {
				for(Object[] record : mappings) {
					ids.add(Long.parseLong(String.valueOf(record[0])));
				}
			}
			List<Object[]> privilegeList = Lists.newArrayList();
			if(ids.size() > 2000) {
				List<Long> limitedIds = ids.subList(0, 2000);
				privilegeList = privilegeMappingRepository.getPrivileges(limitedIds);
				limitedIds = ids.subList(2000, ids.size());
				privilegeList.addAll(privilegeMappingRepository.getPrivileges(limitedIds));
			} else {
				privilegeList = privilegeMappingRepository.getPrivileges(ids);
			}
			for(Object[] privileges : privilegeList) {
				buildPrivilegeDTOs(privilegeDTOs, privileges);
			}
		}
		return privilegeDTOs;
	}

	private void buildPrivilegeDTOs(List<CommonDTO> privilegeDTOs, Object[] privileges) {
		if(null != privileges[6] && privileges[4] != null
				&& privileges[4].equals(Boolean.TRUE)) {
			CommonDTO privDTO = new CommonDTO();
			privDTO.setId(Long.parseLong(privileges[0].toString()));
			privDTO.setCode(privileges[1].toString());
			if(null != privileges[2]) {
				privDTO.setName(privileges[2].toString());
			}
			if(null != privileges[3]) {
				privDTO.setType(privileges[3].toString());
			}
			privDTO.setIsSelected(Boolean.valueOf(privileges[4].toString()));
			if(null != privileges[5]) {
				privDTO.setIsEditable(Boolean.valueOf(privileges[5].toString()));
			}
			privilegeDTOs.add(privDTO);
		}
	}

	@Transactional(readOnly = true)
	public List<CommonDTO> getPrivilegesByRoleId(long id, Enums.UserEnum type) {
		List<CommonDTO> privileges = Lists.newArrayList();
		Mapping dbMapping = mappingRepository.findById(id).orElse(null);
		if(null!=dbMapping) {
			populatePrivileges(dbMapping.getId(), privileges);
		}
		return privileges;
	}

	private void populatePrivileges(Long parentId, List<CommonDTO> privileges) {
		if(parentId == 0) {
			return;
		}
		List<PrivilegeMapping> dbPrivileges = privilegeMappingRepository.findByMapping_Id(parentId);
		if(CollectionUtils.isNotEmpty(dbPrivileges)) {
			buildPrivilegesList(privileges, dbPrivileges);
		}
		List<Mapping> mappingList = mappingRepository.findByParentId(parentId);
		if(CollectionUtils.isNotEmpty(mappingList)) {
			for(Mapping dbMapping : mappingList) {
				dbPrivileges = privilegeMappingRepository.findByMapping_Id(dbMapping.getId());
				if(CollectionUtils.isNotEmpty(dbPrivileges)) {
					buildPrivilegesList(privileges, dbPrivileges);
				}
				populatePrivileges(dbMapping.getId(), privileges);
			}
		}
	}

	private void buildPrivilegesList(List<CommonDTO> privileges, List<PrivilegeMapping> dbPrivileges) {
		for(PrivilegeMapping privilege : dbPrivileges) {
			if(privilege.getIsSelected() != null
					&& privilege.getIsSelected().equals(Boolean.TRUE)) {
				CommonDTO privDTO = new CommonDTO();
				privDTO.setId(privilege.getId());
				privDTO.setCode(privilege.getCode());
				privDTO.setName(privilege.getName());
				privDTO.setType(privilege.getType());
				privDTO.setIsSelected(privilege.getIsSelected());
				privDTO.setIsEditable(privilege.getIsEditable());
				privileges.add(privDTO);
			}
		}
	}

	@Transactional
	public void updatePrivileges(String code, List<Long> parentIds, Set<String> codes) {
		try {
			List<Long> ids = Lists.newArrayList();
			List<Object[]> mappings = mappingRepository.findMappingDetails(parentIds, codes);
			if(CollectionUtils.isNotEmpty(mappings)) {
				for(Object[] record : mappings) {
					ids.add(Long.parseLong(String.valueOf(record[0])));
				}
			}
			if(ids.size() > 0) {
				privilegeMappingRepository.updatePrivileges(false, false, ids);
			}
		} catch(Exception e) {
			LogUtils.basicErrorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), "updatePrivileges"),
					e.getMessage());
		}
	}

	@Transactional
	public void updatePrivileges(String code, List<Long> parentIds, Map<String, List<String>> privilegeMap) {
		try {
			if(CollectionUtils.isNotEmpty(parentIds)) {
				List<Object[]> mappings = mappingRepository.findMappingDetails(parentIds, privilegeMap.keySet());
				Map<Long, String> dbMap = Maps.newHashMap();
				if(CollectionUtils.isNotEmpty(mappings)) {
					for(Object[] record : mappings) {
						dbMap.put(Long.parseLong(String.valueOf(record[0])), String.valueOf(record[1]));
					}
				}
				if(!dbMap.isEmpty()) {
					for(Entry<Long, String> id: dbMap.entrySet()) {
						privilegeMappingRepository.updateMappingPrivileges(false, false, id.getKey(), privilegeMap.get(dbMap.get(id.getKey())));
					}
				}
			}
		} catch(Exception e) {
			LogUtils.basicErrorLog.accept(
					basicMethodInfo.apply(getClass().getCanonicalName(), "updatePrivileges"),
					e.getMessage());
		}
	}

}
