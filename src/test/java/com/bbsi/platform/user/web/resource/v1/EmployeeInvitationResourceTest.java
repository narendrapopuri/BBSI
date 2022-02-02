package com.bbsi.platform.user.web.resource.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.bbsi.platform.common.dto.UserPrincipal;
import com.bbsi.platform.common.user.dto.EmployeeInvitationDTO;
import com.bbsi.platform.exception.BbsiException;
import com.bbsi.platform.user.businessservice.intf.EmployeeInvitationBusinessService;
import com.bbsi.platform.user.mapper.EmployeeInvitationMapper;
import com.beust.jcommander.internal.Lists;

public class EmployeeInvitationResourceTest {

	@InjectMocks
	private EmployeeInvitationResource employeeInvitationResource;

	@Mock
	private EmployeeInvitationBusinessService employeeInvitationBusinessService;

	@Mock
	private EmployeeInvitationMapper employeeInvitationMapper;

	private EmployeeInvitationDTO employeeInvitationDTO;

	public List<EmployeeInvitationDTO> employeeInvitationDTOList = new ArrayList<EmployeeInvitationDTO>();

	private UserPrincipal userPrincipal;

	@Before
	public void setUp() throws Exception {
		employeeInvitationDTO = getEmployeeInvitationDTO();
		userPrincipal = new UserPrincipal("admin@osius.com", "Osicpl@2", Lists.newArrayList());
		userPrincipal.setEmail("integration@osius.com");
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void testGetAllEmployeeInvitationByClientCode() {

		employeeInvitationDTOList.add(employeeInvitationDTO);
		Mockito.doReturn(employeeInvitationDTOList).when(employeeInvitationBusinessService)
				.getAllNotificatonInfoByClientCode(Mockito.anyString(), Mockito.anyString());
		List<EmployeeInvitationDTO> result = employeeInvitationResource.getAllEmployeeInvitationByClientCode("client4",
				"token1");
		assertNotNull(result);
	}

	@Test(expected = BbsiException.class)
	public void testGetAllEmployeeInvitationByClientCodeThrowException() {
		Mockito.doThrow(BbsiException.class).when(employeeInvitationBusinessService)
				.getAllNotificatonInfoByClientCode(Mockito.any(), Mockito.any());
		List<EmployeeInvitationDTO> result = employeeInvitationResource.getAllEmployeeInvitationByClientCode(" ", " ");
		assertEquals(0, result.size());
	}

	@Test(expected = NullPointerException.class)
	public void testGetAllEmployeeInvitationByClientCodeNotPresent() {
		Mockito.doThrow(NullPointerException.class).when(employeeInvitationBusinessService)
				.getAllNotificatonInfoByClientCode(Mockito.any(), Mockito.any());
		List<EmployeeInvitationDTO> result = employeeInvitationResource.getAllEmployeeInvitationByClientCode(null,
				null);
		assertEquals(0, result.size());
	}

	@Test
	public void testSaveEmployeeInvitationInfo() {
		Mockito.doReturn(employeeInvitationDTO).when(employeeInvitationBusinessService)
				.saveEmployeeInvitationInfo(employeeInvitationDTO, "token1");
		EmployeeInvitationDTO result = employeeInvitationResource.saveEmployeeInvitationInfo(employeeInvitationDTO,
				"token1", userPrincipal);
		assertNotNull(result);
	}

	@Test(expected = BbsiException.class)
	public void testSaveEmployeeInvitationInfoThrowNullException() {
		Mockito.doThrow(BbsiException.class).when(employeeInvitationBusinessService)
				.saveEmployeeInvitationInfo(new EmployeeInvitationDTO(), " ");
		EmployeeInvitationDTO result = employeeInvitationResource
				.saveEmployeeInvitationInfo(new EmployeeInvitationDTO(), " ", userPrincipal);
		assertNull(result);
	}

	@Test
	public void testSaveEmployeeInvitationInfoThrowException() {
		employeeInvitationDTO.setId(100);
		employeeInvitationDTO.setClientCode(null);
		employeeInvitationDTO.setEmployeeCode("Employee1");
		employeeInvitationDTO.setLastSentDate(LocalDateTime.now());
		Mockito.doThrow(BbsiException.class).when(employeeInvitationBusinessService).saveEmployeeInvitationInfo(null,
				null);
		EmployeeInvitationDTO result = employeeInvitationResource.saveEmployeeInvitationInfo(null, null, null);
		assertNull(result);
	}

	@Test
	public void testSaveAllEmployeeInvitationInfo() {
		List<EmployeeInvitationDTO> list = new ArrayList<EmployeeInvitationDTO>();
		EmployeeInvitationDTO data = new EmployeeInvitationDTO();
		data.setClientCode("909464");
		data.setEmployeeCode("E1786Z");
		data.setId(1l);
		data.setLastSentDate(LocalDateTime.now());
		list.add(data);
		list.add(getEmployeeInvitationDTO());
		List<EmployeeInvitationDTO> expected = new ArrayList<>();
		Mockito.doReturn(expected).when(employeeInvitationBusinessService).saveAllEmployeeInvitationInfo(list, "token");
		List<EmployeeInvitationDTO> result = employeeInvitationResource.saveAllEmployeeInvitationInfo(list, "token",
				userPrincipal);
		assertEquals(expected.size(), result.size());
	}

	@Test
	public void testSaveAllEmployeeInvitationInfoThrowException() {
		List<EmployeeInvitationDTO> list = new ArrayList<>();
		list.add(null);
		EmployeeInvitationDTO input = new EmployeeInvitationDTO();
		input.setClientCode("");
		input.setEmployeeCode("");
		input.setId(0);
		input.setLastSentDate(null);
		list.add(input);
		Mockito.doThrow(BbsiException.class).when(employeeInvitationBusinessService).saveAllEmployeeInvitationInfo(list,
				" ");
		List<EmployeeInvitationDTO> result = employeeInvitationResource.saveAllEmployeeInvitationInfo(list, " ",
				new UserPrincipal(" ", " ", Lists.newArrayList()));
		assertNotEquals(result.size(), 0);
	}

	@Test(expected = BbsiException.class)
	public void testsaveAllEmployeeInvitationInfoThrowsException() {
		Mockito.doThrow(BbsiException.class).when(employeeInvitationBusinessService).saveAllEmployeeInvitationInfo(null,
				null);
		List<EmployeeInvitationDTO> result = employeeInvitationResource.saveAllEmployeeInvitationInfo(null, null, null);
		assertNull(result);
	}

	@Test
	public void testGetEmployeesForBulkInvitation() {
		Mockito.doReturn("response").when(employeeInvitationBusinessService)
				.getEmployeesForBulkInvitation(Mockito.any());
		String response = employeeInvitationResource.getEmployeesForBulkInvitation(userPrincipal, "");
		assertNotNull(response);
		assertEquals(true, response.toString().contains("response"));
	}

	@Test
	public void testGetEmployeesForBulkInvitationThrowsException() {
		Mockito.doThrow(NullPointerException.class).when(employeeInvitationBusinessService)
				.getEmployeesForBulkInvitation(Mockito.any());
		String response = employeeInvitationResource.getEmployeesForBulkInvitation(userPrincipal, "");
		assertNull(response);
	}

	public EmployeeInvitationDTO getEmployeeInvitationDTO() {
		EmployeeInvitationDTO invitationDTO = new EmployeeInvitationDTO();
		invitationDTO = new EmployeeInvitationDTO();
		invitationDTO.setId(104);
		invitationDTO.setClientCode("Client4");
		invitationDTO.setEmployeeCode("Employee4");
		invitationDTO.setLastSentDate(LocalDateTime.now());
		return invitationDTO;
	}

}
