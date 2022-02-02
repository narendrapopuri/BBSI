package com.bbsi.platform.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bbsi.platform.user.model.Menu;

/**
 * @author anandaluru
 *
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
	
	List<Menu> findByParentId(final long id);
	
}
