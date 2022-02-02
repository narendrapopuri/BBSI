package com.bbsi.platform.user.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

import com.bbsi.platform.common.user.dto.v2.ClientDTOV2;
import com.bbsi.platform.user.model.UserClients;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserClientMapper {

	@Mappings({@Mapping(target = "clientName", source = "client.clientName"),
		@Mapping(target = "costCenterCode", source = "client.costCenterCode"),
		@Mapping(target = "costCenterDescription", source = "client.costCenterDescription")})
    ClientDTOV2 userAccessToClientDTO(UserClients userAccess);

    UserClients clientDTOToUserAccess(ClientDTOV2 roleDTO);

    default List<ClientDTOV2> userAccessListToClientDTOList(List<UserClients> userAccess) {
        return CollectionUtils.isNotEmpty(userAccess)
                ? userAccess.stream().map(m -> userAccessToClientDTO(m)).collect(Collectors.toList())
                : Collections.emptyList();
    }

    default List<UserClients> clientDTOListToUserAccessList(List<ClientDTOV2> userDTO) {
        return CollectionUtils.isNotEmpty(userDTO)
                ? userDTO.stream().map(m -> clientDTOToUserAccess(m)).collect(Collectors.toList())
                : Collections.emptyList();
    }
}
