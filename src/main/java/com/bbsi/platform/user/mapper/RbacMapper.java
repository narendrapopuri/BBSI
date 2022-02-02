package com.bbsi.platform.user.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import com.bbsi.platform.common.user.dto.RbacDTO;
import com.bbsi.platform.user.model.RbacEntity;

/**
 * @author anandaluru
 *
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RbacMapper {

	public RbacDTO rbacTORbacDTO(RbacEntity rbac);

	public RbacEntity rbacDTOTORbac(RbacDTO rbacDTO);

	public default List<RbacDTO> rbacListToRbacDTOList(List<RbacEntity> rbacs) {
		return CollectionUtils.isNotEmpty(rbacs)
				? rbacs.stream().map(m -> rbacTORbacDTO(m)).collect(Collectors.toList())
				: Collections.emptyList();
	}
}
