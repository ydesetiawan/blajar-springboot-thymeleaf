package com.ydes.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ydes.persistence.model.UserRole;

/**
 * @author edys
 * @version 1.0, Feb 4, 2015
 * @since
 */
public interface UserRoleRepository extends JpaRepository<UserRole, String> {

}
