package com.bbsi.platform.user.businessservice.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.bbsi.platform.common.enums.Enums;
import com.bbsi.platform.common.enums.Enums.UserEnum;
import com.bbsi.platform.common.exception.ExceptionUtils;
import com.bbsi.platform.common.generic.AuditDetailsUtil;
import com.bbsi.platform.common.generic.GenericConstants;
import com.bbsi.platform.common.generic.GenericUtils;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableBiConsumer;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableConsumer;
import com.bbsi.platform.common.generic.GenericFunctions.ThrowableTriConsumer;
import com.bbsi.platform.common.user.dto.RbacDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.exception.business.UnauthorizedAccessException;
import com.bbsi.platform.user.businessservice.intf.MappingBusinessService;
import com.bbsi.platform.user.businessservice.intf.RbacBusinessService;
import com.bbsi.platform.user.mapper.RbacMapper;
import com.bbsi.platform.user.model.Mapping;
import com.bbsi.platform.user.model.PrivilegeMapping;
import com.bbsi.platform.user.model.RbacEntity;
import com.bbsi.platform.user.repository.ClientRoleRepository;
import com.bbsi.platform.user.repository.MappingRepository;
import com.bbsi.platform.user.repository.RbacRepository;
import com.bbsi.platform.user.repository.UsersRepository;
import com.google.common.collect.Lists;

public class RbacBusinessServiceImplTest {

	@InjectMocks
	private RbacBusinessServiceImpl rbacBusinessServiceImpl;

	@Mock
	private RbacRepository rbacRepository;

	@Spy
	private RbacMapper rbacMapper;

	@Mock
	private MappingBusinessService mappingBusinessService;

	@Mock
	private AuditDetailsUtil auditDetailsUtil;

	private RbacDTO rbacDTO;

	@Mock
	private UserPrincipal userPrincipal;

	@Mock
	private RbacBusinessService rbacBusinessService;

	@Mock
	private UsersRepository usersRepository;

	@Mock
	private ClientRoleRepository clientRoleRepository;

	@Mock
	private MappingRepository mappingRepository;

	private RbacEntity rbacEntity;

	private CommonDTO commonDto;

	private List<RbacEntity> listRbacEntity = new ArrayList<RbacEntity>();

	private List<RbacDTO> listRbacDto = new ArrayList<RbacDTO>();

	private List<CommonDTO> listCommonDto = new ArrayList<CommonDTO>();

	@Before
	public void setUp() throws Exception {
		rbacDTO = populateRbac();
		rbacEntity = populateRbacEntity();
		commonDto = populateCommonDto();
		listCommonDto.add(commonDto);
		listRbacEntity.add(rbacEntity);
		listRbacDto.add(rbacDTO);
		MockitoAnnotations.initMocks(this);
		ExceptionUtils.handleException = Mockito.mock(ThrowableTriConsumer.class);
		GenericUtils.throwCommonNullVariableException = Mockito.mock(ThrowableBiConsumer.class);
		GenericUtils.throwCommonNullVariableExceptionString = Mockito.mock(ThrowableConsumer.class);
	}

	@After
	public void tearDown() throws Exception {
		rbacEntity = null;
		commonDto = null;
		listCommonDto = null;
		rbacDTO = null;
		listRbacEntity = null;
		listRbacDto = null;

	}

	@Test
	public void testCreateRbacThrowsUnauthorizedAccessException() {
		userPrincipal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setClientCode("clientCode");
		Mockito.doThrow(UnauthorizedAccessException.class).when(rbacRepository).findById(Mockito.anyLong());
		Mockito.doThrow(UnauthorizedAccessException.class).when(mappingBusinessService).getMapping(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyBoolean());
		Mockito.doThrow(UnauthorizedAccessException.class).when(rbacMapper).rbacDTOTORbac(Mockito.any());
		Mockito.doThrow(UnauthorizedAccessException.class).when(mappingBusinessService).saveMapping(Mockito.any(),
				Mockito.any());
		Mockito.doThrow(UnauthorizedAccessException.class).when(rbacRepository).save(Mockito.any());
		RbacDTO actualResponse = rbacBusinessServiceImpl.createRbac(rbacDTO, UserEnum.COPY_ROLE.toString(),
				userPrincipal);
		assertEquals(rbacDTO.getId(), actualResponse.getId());
	}

	@Test
	public void testCreateRbac() {
		userPrincipal.setClientCode("909464");
		Mockito.doReturn(Optional.of(rbacEntity)).when(rbacRepository).findById(Mockito.anyLong());
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("code");
		Mockito.doReturn(commonDto).when(mappingBusinessService).getMapping(Mockito.anyLong(), Mockito.anyString(),
				Mockito.anyBoolean());
		when(rbacMapper.rbacDTOTORbac(rbacDTO)).thenReturn(rbacEntity);
		Mockito.doReturn(123l).when(mappingBusinessService).saveMapping(Mockito.any(), Mockito.any());
		Mockito.doReturn(rbacEntity).when(rbacRepository).save(Mockito.any());
		Mockito.doReturn(rbacDTO).when(rbacMapper).rbacTORbacDTO(Mockito.any());
		RbacDTO actualResponse = rbacBusinessServiceImpl.createRbac(rbacDTO, "CLIENT", principal);
		assertEquals(rbacDTO.getId(), actualResponse.getId());
	}

	@Test
	public void testCreateRbacForValidationException() {
		Mockito.doReturn(rbacEntity).when(rbacMapper).rbacDTOTORbac(Mockito.any());
		Mockito.doReturn(true).when(rbacRepository).existsByCodeAndType(Mockito.any(), Mockito.any());
		RbacDTO actualResponse = rbacBusinessServiceImpl.createRbac(rbacDTO,
				GenericConstants.USERTYPE_CLIENT.toString(), userPrincipal);
		assertEquals(actualResponse.getClientCode(), rbacDTO.getClientCode());
		Mockito.verify(rbacMapper, Mockito.atLeastOnce()).rbacDTOTORbac(Mockito.any());
		Mockito.verify(rbacRepository, Mockito.atLeastOnce()).existsByCodeAndType(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void testCreateRbacForCopyRole() {
		UserPrincipal principal = userPrincipal;
		principal.setClientCode("909464");
		Mockito.doReturn(rbacEntity).when(rbacMapper).rbacDTOTORbac(Mockito.any());
		Mockito.doReturn(true).when(rbacRepository).existsByCodeAndTypeAndClientCode(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString());
		RbacDTO actualResponse = rbacBusinessServiceImpl.createRbac(rbacDTO, Enums.UserEnum.COPY_ROLE.toString(),
				principal);
		assertEquals(actualResponse.getClientCode(), rbacDTO.getClientCode());

	}

	@Test
	public void testUpdateRbac() {
		RbacDTO inputDTO = populateRbac();
		Optional<RbacEntity> data = Optional.of(getRbacList().get(0));
		inputDTO.setCode("updatedCode");
		List<Mapping> roles = new ArrayList<>();
		roles.add(getMapping());
		Mockito.doReturn(data).when(rbacRepository).findById(Mockito.anyLong());
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode(inputDTO.getClientCode());
		Mockito.doReturn(populateCommonDtoWithView()).when(mappingBusinessService).getMapping(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyBoolean());
		Mockito.doReturn(getRbacList().get(0)).when(rbacMapper).rbacDTOTORbac(Mockito.any());
		Mockito.doReturn(getRbacList()).when(rbacRepository).findByType(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findByParentId(Mockito.any());
		Mockito.doReturn(123l).when(mappingBusinessService).saveMapping(Mockito.any(), Mockito.any());
		Mockito.doReturn(rbacEntity).when(rbacRepository).save(Mockito.any());
		Mockito.doReturn(rbacDTO).when(rbacMapper).rbacTORbacDTO(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findByParentIdIn(Mockito.any());
		RbacDTO actualResponse = rbacBusinessServiceImpl.updateRbac(inputDTO, Enums.UserEnum.ACCESS_GROUP.toString(),
				principal);
		assertEquals(actualResponse.getClientCode(), inputDTO.getClientCode());
	}

	@Test
	public void testUpdateRbacTypeNull() {
		RbacDTO inputDTO = populateRbac();
		Optional<RbacEntity> data = Optional.of(getRbacList().get(0));
		inputDTO.setCode("updatedCode");
		List<Mapping> roles = new ArrayList<>();
		roles.add(getMapping());
		Mockito.doReturn(data).when(rbacRepository).findById(Mockito.anyLong());
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode(inputDTO.getClientCode());
		Mockito.doReturn(populateCommonDtoWithViewTypeNull()).when(mappingBusinessService).getMapping(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyBoolean());
		Mockito.doReturn(getRbacList().get(0)).when(rbacMapper).rbacDTOTORbac(Mockito.any());
		Mockito.doReturn(getRbacList()).when(rbacRepository).findByType(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findByParentId(Mockito.any());
		Mockito.doReturn(123l).when(mappingBusinessService).saveMapping(Mockito.any(), Mockito.any());
		Mockito.doReturn(rbacEntity).when(rbacRepository).save(Mockito.any());
		Mockito.doReturn(rbacDTO).when(rbacMapper).rbacTORbacDTO(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findByParentIdIn(Mockito.any());
		RbacDTO actualResponse = rbacBusinessServiceImpl.updateRbac(inputDTO, Enums.UserEnum.ACCESS_GROUP.toString(),
				principal);
		assertEquals(actualResponse.getClientCode(), inputDTO.getClientCode());
	}

	@Test
	public void testUpdateRbacForRoleGetCode() {
		RbacDTO inputDTO = populateRbac();
		Optional<RbacEntity> data = Optional.of(getRbacList().get(0));
		inputDTO.setCode("rbac");
		List<Mapping> roles = new ArrayList<>();
		roles.add(getMapping());
		Mockito.doReturn(data).when(rbacRepository).findById(Mockito.anyLong());
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode(inputDTO.getClientCode());
		Mockito.doReturn(populateCommonDtoWithView()).when(mappingBusinessService).getMapping(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyBoolean());
		Mockito.doReturn(getRbacList().get(0)).when(rbacMapper).rbacDTOTORbac(Mockito.any());
		Mockito.doReturn(getRbacList()).when(rbacRepository).findByType(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findByParentId(Mockito.any());
		Mockito.doReturn(123l).when(mappingBusinessService).saveMapping(Mockito.any(), Mockito.any());
		Mockito.doReturn(rbacEntity).when(rbacRepository).save(Mockito.any());
		Mockito.doReturn(rbacDTO).when(rbacMapper).rbacTORbacDTO(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findByParentIdIn(Mockito.any());
		RbacDTO actualResponse = rbacBusinessServiceImpl.updateRbac(inputDTO, Enums.UserEnum.ACCESS_GROUP.toString(),
				principal);
		assertEquals(actualResponse.getClientCode(), inputDTO.getClientCode());
	}

	@Test
	public void testUpdateRbacWithRole() {
		RbacDTO inputDTO = populateRbac();
		Optional<RbacEntity> data = Optional.of(getRbacList().get(0));
		inputDTO.setCode("updatedCode");
		List<Mapping> roles = new ArrayList<>();
		roles.add(getMapping());
		List<RbacEntity> clientRoles = new ArrayList<>();
		clientRoles.add(rbacEntity);
		Mockito.doReturn(data).when(rbacRepository).findById(Mockito.anyLong());
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode(inputDTO.getClientCode());
		Mockito.doReturn(populateCommonDtoWithView()).when(mappingBusinessService).getMapping(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyBoolean());
		Mockito.doReturn(getRbacList().get(0)).when(rbacMapper).rbacDTOTORbac(Mockito.any());
		Mockito.doReturn(getRbacList()).when(rbacRepository).findByType(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findByParentId(Mockito.any());
		Mockito.doReturn(123l).when(mappingBusinessService).saveMapping(Mockito.any(), Mockito.any());
		Mockito.doReturn(rbacEntity).when(rbacRepository).save(Mockito.any());
		Mockito.doReturn(rbacDTO).when(rbacMapper).rbacTORbacDTO(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findByParentIdIn(Mockito.any());
		Mockito.doReturn(listRbacEntity).when(mappingRepository).findByParentIdIn(Mockito.any());
		Mockito.doReturn(clientRoles).when(rbacRepository).findByParentId(Mockito.anyLong());
		RbacDTO actualResponse = rbacBusinessServiceImpl.updateRbac(inputDTO, Enums.UserEnum.ROLE.toString(),
				principal);
		assertEquals(actualResponse.getClientCode(), inputDTO.getClientCode());
	}

	@Test
	public void testUpdateRbacExpectingValidationException() {
		RbacDTO inputDTO = populateRbac();
		RbacEntity rbac = getRbacList().get(0);
		rbac.setStatus(Boolean.FALSE);
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode(inputDTO.getClientCode());
		Optional<RbacEntity> data = Optional.of(getRbacList().get(0));
		inputDTO.setCode("updatedCode");
		List<Mapping> roles = new ArrayList<>();
		Mapping mapping = new Mapping();
		mapping.setCode("updatedCode");
		roles.add(mapping);
		Mockito.doReturn(data).when(rbacRepository).findById(Mockito.anyLong());
		Mockito.doReturn(commonDto).when(mappingBusinessService).getMapping(Mockito.anyLong(), Mockito.anyString(),
				Mockito.anyBoolean());
		Mockito.doReturn(rbac).when(rbacMapper).rbacDTOTORbac(Mockito.any());
		Mockito.doReturn(true).when(clientRoleRepository).existsByRole_Id(Mockito.anyLong());
		Mockito.doReturn(getRbacList()).when(rbacRepository).findByType(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findByParentId(Mockito.any());
		Mockito.doReturn(123l).when(mappingBusinessService).saveMapping(Mockito.any(), Mockito.any());
		Mockito.doReturn(rbac).when(rbacRepository).save(Mockito.any());
		Mockito.doReturn(inputDTO).when(rbacMapper).rbacTORbacDTO(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findByParentIdIn(Mockito.any());
		RbacDTO actualResponse = rbacBusinessServiceImpl.updateRbac(inputDTO, Enums.UserEnum.CLIENT.toString(),
				principal);
		assertEquals(actualResponse.getClientCode(), inputDTO.getClientCode());
	}

	@Test
	public void testUpdateRbacForValidationException1() {
		RbacDTO inputDTO = populateRbac();
		RbacEntity rbacEntity = getRbacList().get(0);
		Optional<RbacEntity> data = Optional.of(getRbacList().get(0));
		inputDTO.setCode("updatedCode");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode(inputDTO.getClientCode());
		List<Mapping> roles = new ArrayList<>();
		Mapping role = new Mapping();
		role.setCode(inputDTO.getCode());
		roles.add(role);
		Mockito.doReturn(data).when(rbacRepository).findById(Mockito.anyLong());
		Mockito.doReturn(rbacEntity).when(rbacMapper).rbacDTOTORbac(Mockito.any());
		Mockito.doReturn(roles).when(mappingRepository).findByParentIdIn(Mockito.any());
		RbacDTO actualResponse = rbacBusinessServiceImpl.updateRbac(inputDTO, Enums.UserEnum.ACCESS_GROUP.toString(),
				principal);
		assertEquals(actualResponse.getClientCode(), inputDTO.getClientCode());
	}

	@Test
	public void testUpdateRbacForValidationException2() {
		RbacDTO inputDTO = populateRbac();
		RbacEntity rbacEntity = getRbacList().get(0);
		rbacEntity.setStatus(false);
		Optional<RbacEntity> data = Optional.of(getRbacList().get(0));
		inputDTO.setCode("updatedCode");
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode(inputDTO.getClientCode());
		List<Object[]> objectList = new ArrayList<Object[]>();
		Object[] object = { 1 };
		objectList.add(object);
		Mockito.doReturn(data).when(rbacRepository).findById(Mockito.anyLong());
		Mockito.doReturn(rbacEntity).when(rbacMapper).rbacDTOTORbac(Mockito.any());
		Mockito.doReturn(objectList).when(usersRepository).findUsersByRoleId(Mockito.anyLong());
		RbacDTO actualResponse = rbacBusinessServiceImpl.updateRbac(inputDTO, Enums.UserEnum.CLIENT.toString(),
				principal);
		assertEquals(actualResponse.getClientCode(), inputDTO.getClientCode());
	}

	@Test
	public void testUpdateRbacWhenChildIsNotAvailable() {
		commonDto = populateCommonDtoWhenChildIsNotPresent();
		rbacDTO = populateRbacNotContainsMapping();
		Mockito.doReturn(Optional.of(rbacEntity)).when(rbacRepository).findById(Mockito.anyLong());
		Set<GrantedAuthority> grantedAuthorities1 = new HashSet<GrantedAuthority>();
		UserPrincipal principal = new UserPrincipal(" ", " ", grantedAuthorities1);
		principal.setClientCode("code");
		Mockito.doReturn(commonDto).when(mappingBusinessService).getMapping(Mockito.anyLong(), Mockito.anyString(),
				Mockito.anyBoolean());
		Mockito.doReturn(rbacEntity).when(rbacMapper).rbacDTOTORbac(Mockito.any());
		Mockito.doReturn(123l).when(mappingBusinessService).saveMapping(Mockito.any(), Mockito.any());
		Mockito.doReturn(rbacEntity).when(rbacRepository).save(Mockito.any());
		Mockito.doReturn(rbacDTO).when(rbacMapper).rbacTORbacDTO(Mockito.any());
		RbacDTO actualResponse = rbacBusinessServiceImpl.updateRbac(rbacDTO, "CLIENT", principal);
		assertEquals(rbacDTO.getClientCode(), actualResponse.getClientCode());
	}

	@Test
	public void testUpdateRbacWhenclientCodeIsNotEqual() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode("clientcode");
		RbacDTO input = populateRbac();
		RbacDTO actualResponse = rbacBusinessServiceImpl.updateRbac(input, UserEnum.CLIENT.toString(), principal);
		assertEquals(input.getId(), actualResponse.getId());
	}

	@Test
	public void testUpdateRbacThrowsUnauthorizedAccessException() {
		RbacDTO input = populateRbac();
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode("code");
		principal.setUserId(1l);
		input.setStatus(false);
		Mockito.doThrow(UnauthorizedAccessException.class).when(rbacRepository).save(Mockito.any());
		RbacDTO actualResponse = rbacBusinessServiceImpl.updateRbac(input, UserEnum.COPY_ROLE.toString(), principal);
		assertEquals(input.getId(), actualResponse.getId());
	}

	@Test
	public void testUpdateRbacForCopyRole() {
		RbacDTO input = populateRbac();
		input.setCode("code100");
		input.setStatus(Boolean.FALSE);
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode(input.getClientCode());
		principal.setUserId(1l);
		Mockito.doReturn(rbacDTO).when(rbacMapper).rbacTORbacDTO(Mockito.any());
		RbacDTO actualResponse = rbacBusinessServiceImpl.updateRbac(input, UserEnum.COPY_ROLE.toString(), principal);
		assertEquals(actualResponse.getClientCode(), input.getClientCode());
		assertEquals("code100", actualResponse.getCode());
	}

	@Test
	public void testGetAllRbacs() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode("code");
		principal.setUserType("Client");
		Mockito.doReturn(listRbacEntity).when(rbacRepository).findByType(Mockito.anyString());
		when(rbacMapper.rbacListToRbacDTOList(listRbacEntity)).thenReturn(listRbacDto);
		Mockito.doReturn(listRbacEntity).when(rbacRepository).findByClientCodeAndType(Mockito.anyString(),
				Mockito.anyString());
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getAllRbacs(principal, Enums.UserEnum.ROLE.toString());
		assertEquals(1, actualResponse.size());
	}

	@Test
	public void testGetAllRbacsrbacListEmpty() {
		Mockito.doReturn(new ArrayList<>()).when(rbacRepository).findByType(Mockito.anyString());
		when(rbacMapper.rbacListToRbacDTOList(listRbacEntity)).thenReturn(new ArrayList<>());
		Mockito.doReturn(new ArrayList<>()).when(rbacRepository).findByClientCodeAndType(Mockito.anyString(),
				Mockito.anyString());
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getAllRbacs(userPrincipal, Enums.UserEnum.ROLE.toString());
		assertNull(actualResponse);
	}

	@Test
	public void testGetAllRbacsTypeClient() {
		UserPrincipal principal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		principal.setClientCode("code");
		principal.setUserType("Client");
		Mockito.doReturn(listRbacEntity).when(rbacRepository).findByType(Mockito.anyString());
		when(rbacMapper.rbacListToRbacDTOList(listRbacEntity)).thenReturn(listRbacDto);
		Mockito.doReturn(listRbacEntity).when(rbacRepository).findByClientCodeAndType(Mockito.anyString(),
				Mockito.anyString());
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getAllRbacs(principal, Enums.UserEnum.CLIENT.toString());
		assertEquals(1, actualResponse.size());
	}

	@Test
	public void testGetAllRbacsThrowsException() {
		Mockito.doThrow(BbsiException.class).when(rbacRepository).findByType(Mockito.anyString());
		when(rbacMapper.rbacListToRbacDTOList(new ArrayList<>())).thenReturn(new ArrayList<>());
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getAllRbacs(userPrincipal, "Client");
		assertNull(actualResponse);
	}

	@Test
	public void testGetClientRoles() {
		Mockito.doReturn(listRbacEntity).when(rbacRepository).findByType(Mockito.anyString());
		Mockito.doReturn(listRbacDto).when(rbacMapper).rbacListToRbacDTOList(Mockito.any());
		Mockito.doReturn(commonDto).when(mappingBusinessService).getMapping(Mockito.anyString(), Mockito.any(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getClientRoles("909464", userPrincipal);
		assertEquals(2, actualResponse.size());

	}

	@Test
	public void testGetClientRolesrbacListEmpty() {
		commonDto.setChild(new ArrayList<>());
		Mockito.doReturn(new ArrayList<>()).when(rbacRepository).findByType(Mockito.anyString());
		Mockito.doReturn(listRbacDto).when(rbacMapper).rbacListToRbacDTOList(Mockito.any());
		Mockito.doReturn(commonDto).when(mappingBusinessService).getMapping(Mockito.anyString(), Mockito.any(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getClientRoles("909464", userPrincipal);
		assertNull(actualResponse);

	}

	@Test
	public void testGetClientRolesChildEmpty() {
		commonDto.setChild(new ArrayList<>());
		Mockito.doReturn(listRbacEntity).when(rbacRepository).findByType(Mockito.anyString());
		Mockito.doReturn(listRbacDto).when(rbacMapper).rbacListToRbacDTOList(Mockito.any());
		Mockito.doReturn(commonDto).when(mappingBusinessService).getMapping(Mockito.anyString(), Mockito.any(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getClientRoles("909464", userPrincipal);
		assertNotNull(actualResponse);

	}

	@Test
	public void testGetClientRolesCommonDtoChildEmpty() {
		Mockito.doReturn(listRbacEntity).when(rbacRepository).findByType(Mockito.anyString());
		Mockito.doReturn(listRbacDto).when(rbacMapper).rbacListToRbacDTOList(Mockito.any());
		Mockito.doReturn(populateCommonDto1()).when(mappingBusinessService).getMapping(Mockito.anyString(),
				Mockito.any(), Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getClientRoles("909464", userPrincipal);
		assertNotNull(actualResponse);

	}

	@Test
	public void testGetClientRolesThrowsException() {
		Mockito.doThrow(BbsiException.class).when(rbacRepository).findByType(Mockito.anyString());
		Mockito.doThrow(BbsiException.class).when(rbacMapper).rbacListToRbacDTOList(Mockito.any());
		Mockito.doThrow(BbsiException.class).when(mappingBusinessService).getMapping(Mockito.anyString(), Mockito.any(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getClientRoles("909464", null);
		assertNull(actualResponse);

	}

	@Test
	public void testGetEntriesByCode() {

		Mockito.doReturn(rbacEntity).when(rbacRepository).findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(rbacDTO).when(rbacMapper).rbacTORbacDTO(Mockito.any());
		Mockito.doReturn(commonDto).when(mappingBusinessService).getMapping(Mockito.anyString(), Mockito.any(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		RbacDTO rbac = rbacBusinessServiceImpl.getEntriesByCode("code", UserEnum.CLIENT, Boolean.TRUE, Boolean.FALSE,
				userPrincipal);
		assertEquals("role1", rbac.getCode());
	}

	@Test
	public void testGetEntriesByCoderbacentityNull() {

		Mockito.doReturn(null).when(rbacRepository).findByCodeAndType(Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(rbacDTO).when(rbacMapper).rbacTORbacDTO(Mockito.any());
		Mockito.doReturn(commonDto).when(mappingBusinessService).getMapping(Mockito.anyString(), Mockito.any(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		RbacDTO rbac = rbacBusinessServiceImpl.getEntriesByCode("code", UserEnum.CLIENT, Boolean.TRUE, Boolean.FALSE,
				userPrincipal);
		assertNull(rbac);
	}

	@Test
	public void testGetEntriesByCodeThrowsException() {

		Mockito.doThrow(BbsiException.class).when(rbacRepository).findByCodeAndType(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doThrow(BbsiException.class).when(rbacMapper).rbacTORbacDTO(Mockito.any());
		Mockito.doThrow(BbsiException.class).when(mappingBusinessService).getMapping(Mockito.anyString(), Mockito.any(),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any());
		RbacDTO rbac = rbacBusinessServiceImpl.getEntriesByCode("code", UserEnum.CLIENT, Boolean.TRUE, Boolean.FALSE,
				userPrincipal);
		assertNull(rbac);
	}

	@Test
	public void testGetAllRoles() {
		List<RbacEntity> expected = getRbacList();
		RbacDTO rbacDTO1 = new RbacDTO();
		LocalDateTime modifiedOn = LocalDateTime.now();
		rbacDTO1.setModifiedOn(modifiedOn);
		RbacDTO rbacDTO2 = new RbacDTO();
		LocalDateTime modifiedOn1 = LocalDateTime.now();
		rbacDTO1.setModifiedOn(modifiedOn1);
		List<RbacDTO> rbacDTOList = new ArrayList<>();
		rbacDTOList.add(rbacDTO1);
		rbacDTOList.add(rbacDTO2);
		Mockito.doReturn(expected).when(rbacRepository).findByClientCodeAndType("909464",
				UserEnum.COPY_ROLE.toString());
		Mockito.doReturn(expected).when(rbacRepository).findByIsClientTemplateOrClientCodeAndType(true, "909464",
				UserEnum.COPY_ROLE.toString());
		Mockito.doReturn(rbacDTOList).when(rbacMapper).rbacListToRbacDTOList(expected);
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getAllRoles(false, "909464");
		assertEquals(actualResponse.size(), rbacDTOList.size());
	}

	@Test
	public void testGetAllRolesWhenClientOnlyTrue() {
		List<RbacEntity> expected = new ArrayList<>();
		List<RbacDTO> rbacDTOList = new ArrayList<>();
		Mockito.doReturn(expected).when(rbacRepository).findByClientCodeAndType("909464",
				UserEnum.COPY_ROLE.toString());
		Mockito.doReturn(expected).when(rbacRepository).findByIsClientTemplateOrClientCodeAndType(true, "909464",
				UserEnum.COPY_ROLE.toString());
		Mockito.doReturn(rbacDTOList).when(rbacMapper).rbacListToRbacDTOList(expected);
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getAllRoles(true, "909464");
		assertNull(actualResponse);
	}

	@Test
	public void testGetAllRolesForExceptions() {
		List<RbacEntity> expected = new ArrayList<>();
		List<RbacDTO> rbacDTOList = new ArrayList<>();
		Mockito.doThrow(BbsiException.class).when(rbacRepository).findByClientCodeAndType(Mockito.anyString(),
				Mockito.anyString());
		Mockito.doReturn(expected).when(rbacRepository).findByIsClientTemplateOrClientCodeAndType(Mockito.anyBoolean(),
				Mockito.anyString(), Mockito.anyString());
		Mockito.doReturn(rbacDTOList).when(rbacMapper).rbacListToRbacDTOList(Mockito.any());
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getAllRoles(true, "909464");
		assertNull(actualResponse);
	}

	@Test
	public void testGetAllRolesForOtherExceptions() {
		Mockito.doThrow(BbsiException.class).when(rbacRepository).findByClientCodeAndType(null, null);
		Mockito.doThrow(BbsiException.class).when(rbacRepository).findByIsClientTemplateOrClientCodeAndType(false, null,
				null);
		Mockito.doThrow(BbsiException.class).when(rbacMapper).rbacListToRbacDTOList(null);
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getAllRoles(false, null);
		assertNull(actualResponse);
	}

	@Test
	public void testgetAllClientRoles() {
		List<RbacEntity> expected = getRbacList();
		List<RbacDTO> rbacDTOList = new ArrayList<>();
		rbacDTOList.add(populateRbac());
		Mockito.doReturn(expected).when(rbacRepository).findByIsClientTemplateAndStatusAndTypeNot(true, true, UserEnum.COPY_ROLE.toString());
		Mockito.doReturn(rbacDTOList).when(rbacMapper).rbacListToRbacDTOList(getRbacList());
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getAllClientRoles(userPrincipal);
		assertEquals(1, actualResponse.size());
	}

	@Test
	public void testGetAllClientRolesForExceptions() {
		Mockito.doReturn(new ArrayList<>()).when(rbacRepository).findByIsClientTemplateAndStatusAndTypeNot(false, false, UserEnum.COPY_ROLE.toString());
		Mockito.doReturn(new ArrayList<>()).when(rbacMapper).rbacListToRbacDTOList(new ArrayList<>());
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getAllClientRoles(userPrincipal);
		assertNull(actualResponse);
	}

	@Test
	public void testGetAllClientRolesForOtherExceptions() {
		Mockito.doThrow(BbsiException.class).when(rbacRepository).findByIsClientTemplateAndStatusAndTypeNot(Mockito.any(),
				Mockito.any(), Mockito.any());
		Mockito.doReturn(null).when(rbacMapper).rbacListToRbacDTOList(Mockito.any());
		List<RbacDTO> actualResponse = rbacBusinessServiceImpl.getAllClientRoles(userPrincipal);
		assertNull(actualResponse);
	}

	@Test
	public void testupdateFeatureMappings() {
		List<CommonDTO> mappings = new ArrayList<>();
		mappings.add(populateCommonDto());
		mappings.addAll(populateChildHavingNoPrivileg());
		mappings.add(populateCommonDtoWhenChildIsNotPresent());
		Map<String, String> codeMap = new HashMap<>();
		Map<String, List<CommonDTO>> map = new HashMap<>();
		rbacBusinessServiceImpl.updateFeatureMappings(codeMap, map, mappings);
		assertTrue(true);
	}

	private RbacEntity populateRbacEntity() {
		RbacEntity rbac = new RbacEntity();
		rbac.setCode("code");
		rbac.setMappingId(1l);
		rbac.setIsVocEditable(true);
		rbac.setIsBranchEditable(true);
		rbac.setIsClientEditable(true);
		return rbac;
	}

	private RbacDTO populateRbac() {
		RbacDTO rbacDto = new RbacDTO();
		rbacDto.setClientCode("909464");
		rbacDto.setCode("role1");
		rbacDto.setName("Bbsi");
		rbacDto.setMappings(populateChildList());
		rbacDto.setDescription("description");
		rbacDto.setId(1l);
		rbacDto.setIsClientTemplate(true);
		rbacDto.setStatus(true);
		rbacDto.setVersion(1l);
		rbacDto.setType(GenericConstants.USERTYPE_CLIENT.toString());
		rbacDto.setModifiedOn(LocalDateTime.now());
		rbacDto.setIsVocEditable(true);
		rbacDto.setIsBranchEditable(true);
		rbacDto.setIsClientEditable(true);
		RbacDTO rbacDto1 = new RbacDTO();
		rbacDto1.setModifiedOn(LocalDateTime.now());
		return rbacDto;
	}

	private CommonDTO populateCommonDto() {
		CommonDTO commonDto = new CommonDTO();
		commonDto.setCode("code123");
		commonDto.setType("Client");
		commonDto.setName("Bbsi");
		commonDto.setId(1l);
		commonDto.setPrivileges(populateChild());
		commonDto.setChild(populateChildList());
		return commonDto;
	}

	private CommonDTO populateCommonDto1() {
		CommonDTO commonDto = new CommonDTO();
		commonDto.setCode("code123");
		commonDto.setType("Client");
		commonDto.setName("Bbsi");
		commonDto.setId(1l);
		commonDto.setPrivileges(populateChild());
		commonDto.setChild(populateChildList1());
		return commonDto;
	}

	private List<CommonDTO> populateChildList1() {
		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO commonDTO = new CommonDTO();
		commonDTO.setId(1233);
		commonDTO.setName("Bbsi");
		commonDTO.setCode("role1");
		commonDto.add(commonDTO);
		return commonDto;
	}

	private List<CommonDTO> populateChildList() {
		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO commonDTO = new CommonDTO();
		commonDTO.setId(1233);
		commonDTO.setName("Bbsi");
		commonDTO.setCode("code12");
		commonDTO.setType("Client");
		commonDTO.setIsSelected(true);
		commonDTO.setChild(populateChild());
		List<CommonDTO> privileges = new ArrayList<>();
		CommonDTO privilege = new CommonDTO();
		privilege.setCode("AUD_LOG.ALL");
		privilege.setDescription("Manage Application Audit Log");
		privilege.setType("ALL");
		privilege.setId(1l);
		privileges.add(privilege);
		commonDTO.setPrivileges(privileges);
		commonDTO.setParentId(1l);
		commonDTO.setChild(populateSubChildList());
		commonDto.add(commonDTO);
		return commonDto;
	}

	private List<CommonDTO> populateSubChildList() {
		List<CommonDTO> list = new ArrayList<>();
		CommonDTO data = new CommonDTO();
		data.setCode("subchild");
		data.setDescription("description");
		data.setId(4l);
		data.setName("name");
		data.setParentId(1233);
		data.setNewStatus(true);
		data.setPrivileges(populatePrivilegesWithIsSelected());
		data.setSeqNum(1l);
		data.setStatus(true);
		data.setType("ALL");
		list.add(data);
		return list;
	}

	private CommonDTO populateCommonDtoWithView() {
		CommonDTO commonDto = new CommonDTO();
		commonDto.setCode("code123");
		commonDto.setType("Client");
		commonDto.setName("Bbsi");
		commonDto.setId(1l);
		commonDto.setPrivileges(populateChild());
		commonDto.setChild(populateChildListWithView());
		return commonDto;
	}

	private CommonDTO populateCommonDtoWithViewTypeNull() {
		CommonDTO commonDto = new CommonDTO();
		commonDto.setCode("code123");
		commonDto.setType(null);
		commonDto.setName("Bbsi");
		commonDto.setId(1l);
		commonDto.setPrivileges(populateChildListWithView1());
		commonDto.setChild(populateChildListWithView1());
		return commonDto;
	}

	private List<CommonDTO> populateChildListWithView1() {
		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO commonDTO = new CommonDTO();
		commonDTO.setId(1233);
		commonDTO.setName("Bbsi");
		commonDTO.setCode("code12");
		commonDTO.setType("Client");
		commonDTO.setIsSelected(true);
		commonDTO.setChild(populateChild());
		commonDTO.setPrivileges(populateChild());
		commonDto.add(commonDTO);
		return commonDto;
	}

	private List<CommonDTO> populateChildListWithView() {
		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO commonDTO = new CommonDTO();
		commonDTO.setId(1233);
		commonDTO.setName("Bbsi");
		commonDTO.setCode("code12");
		commonDTO.setType("Client");
		commonDTO.setIsSelected(true);
		commonDTO.setChild(populateChild());
		List<CommonDTO> privileges = new ArrayList<>();
		CommonDTO privilege = new CommonDTO();
		privilege.setCode("AUD_LOG.ALL");
		privilege.setDescription("Manage Application Audit Log");
		privilege.setType("VIEW");
		privilege.setId(1l);
		privileges.add(privilege);
		commonDTO.setPrivileges(privileges);
		commonDTO.setParentId(1l);
		commonDTO.setChild(populateSubChildListWithView());
		commonDto.add(commonDTO);
		return commonDto;
	}

	private List<CommonDTO> populateSubChildListWithView() {
		List<CommonDTO> list = new ArrayList<>();
		CommonDTO data = new CommonDTO();
		data.setCode("subchild");
		data.setDescription("description");
		data.setId(4l);
		data.setName("name");
		data.setParentId(1233);
		data.setNewStatus(true);
		data.setPrivileges(populatePrivilegesWithIsSelected());
		data.setSeqNum(1l);
		data.setStatus(true);
		data.setType("VIEW");
		list.add(data);
		return list;
	}

	private List<CommonDTO> populatePrivilegesWithIsSelected() {
		List<CommonDTO> commonDtoList = new ArrayList<CommonDTO>();
		CommonDTO commonDTO = new CommonDTO();
		commonDTO.setIsSelected(true);
		commonDTO.setType("VIEW");
		commonDtoList.add(commonDTO);
		return commonDtoList;

	}

	private List<CommonDTO> populateChild() {
		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO commonDTO = new CommonDTO();
		commonDTO.setId(1233);
		commonDTO.setName("Bbsi");
		commonDTO.setType("Client");
		commonDTO.setIsSelected(true);
		List<CommonDTO> privileges = new ArrayList<>();
		CommonDTO privilege = new CommonDTO();
		privilege.setCode("code");
		privilege.setDescription("description");
		privileges.add(privilege);
		privileges.addAll(populateSubChildList());
		commonDTO.setParentId(1l);
		commonDTO.setType("ALL");
		commonDto.add(commonDTO);
		return commonDto;
	}

	private CommonDTO populateCommonDtoWhenChildIsNotPresent() {
		CommonDTO commonDto = new CommonDTO();
		commonDto.setCode("code123");
		commonDto.setType("Client");
		commonDto.setName("Bbsi");
		commonDto.setParentId(1l);
		return commonDto;
	}

	private RbacDTO populateRbacNotContainsMapping() {
		RbacDTO rbacDto = new RbacDTO();
		rbacDto.setClientCode("909464");
		rbacDto.setCode("code");
		rbacDto.setName("Bbsi");
		rbacDto.setMappings(populateChildHavingNoPrivileg());
		return rbacDto;
	}

	private List<CommonDTO> populateChildHavingNoPrivileg() {
		List<CommonDTO> commonDto = new ArrayList<CommonDTO>();
		CommonDTO commonDTO = new CommonDTO();
		commonDTO.setId(1233);
		commonDTO.setName("Bbsi");
		commonDTO.setCode("9011");
		commonDTO.setType("Client");
		commonDTO.setIsSelected(true);
		commonDTO.setChild(populateChild());
		commonDTO.setParentId(1l);
		commonDto.add(commonDTO);
		return commonDto;
	}

	private List<RbacEntity> getRbacList() {
		RbacEntity data = new RbacEntity();
		data.setClientCode("909464");
		data.setCode("9099");
		data.setDescription("description");
		data.setId(1l);
		data.setIsClientTemplate(Boolean.TRUE);
		data.setMappingId(1l);
		data.setName("name");
		data.setParentId(0l);
		data.setStatus(Boolean.FALSE);
		data.setType(GenericConstants.USERTYPE_CLIENT.toString());
		data.setVersion(1l);

		RbacEntity data1 = new RbacEntity();
		data1.setModifiedOn(LocalDateTime.now());
		List<RbacEntity> dataList = new ArrayList<>();
		data.setModifiedOn(LocalDateTime.now());
		dataList.add(data);
		dataList.add(data1);
		return dataList;
	}

	private Mapping getMapping() {
		Mapping data = new Mapping();
		data.setCode("rbac");
		data.setDescription("description");
		data.setId(1l);
		data.setParentId(1l);
		data.setName("name");
		List<PrivilegeMapping> privileges = new ArrayList<>();
		PrivilegeMapping privilege = new PrivilegeMapping();
		privilege.setCode("privilege1");
		privilege.setId(1l);
		privilege.setName("Privilege");
		privilege.setType("ALL");
		privileges.add(privilege);
		data.setPrivileges(privileges);
		data.setStatus(true);
		data.setType("ALL");
		return data;
	}
}
