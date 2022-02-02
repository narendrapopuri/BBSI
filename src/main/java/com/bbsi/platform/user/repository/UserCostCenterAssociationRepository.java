package com.bbsi.platform.user.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bbsi.platform.user.model.UserCostCenterAssociation;

@Repository
public interface UserCostCenterAssociationRepository extends JpaRepository<UserCostCenterAssociation, Long> {

	@Lock(LockModeType.NONE)
	List<UserCostCenterAssociation> findByUserId(final Long userId);
	
	@Lock(LockModeType.NONE)
	List<UserCostCenterAssociation> findByUserType(final String userType);
	
	@Lock(LockModeType.NONE)
	List<UserCostCenterAssociation> findByCostCenterCode(final String costCenterCode);
	
	@Modifying
	@Transactional
	void deleteByUserId(Long userId);
	
	 @Query(value ="select uc.user_type, uc.user_id, re.mapping_id from user_cost_center_association uc, user_rbac_association ur, rbac_entity re where uc.user_id=ur.user_id and uc.cost_center_code=?1 and uc.user_type='Branch' and re.id=ur.role_id", nativeQuery = true)
	 List<Object[]> findByBranchUsersByCostCenterCode(final String clientCode);
}
