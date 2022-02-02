package com.bbsi.platform.user.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import com.bbsi.platform.common.user.dto.UsersDTO;
import com.bbsi.platform.user.model.Users;

/**
 * @author anandaluru
 *
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = {UserClientMapper.class})
public interface UsersMapper {
	
	UsersDTO userToUserDTO(Users user);

	Users userDTOToUser(UsersDTO roleDTO);

    default List<UsersDTO> userListToUserDTOList(List<Users> users) {
        return CollectionUtils.isNotEmpty(users)
                ? users.stream().map(m -> userToUserDTO(m)).collect(Collectors.toList())
                : Collections.emptyList();
    }

}