package com.bbsi.platform.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bbsi.platform.user.model.UsersPasswords;


@Repository
public interface UsersPasswordsRepository extends JpaRepository<UsersPasswords, Long> {

	List<UsersPasswords> findByUserId(Long userId);

	List<UsersPasswords> findByPasswordIndex(Integer passwordIndex);

	List<UsersPasswords> findByUserIdAndPasswordIndex(Long userId, Integer passwordIndex);

	void deleteByIdIn(List<Long> removeIds);
}
