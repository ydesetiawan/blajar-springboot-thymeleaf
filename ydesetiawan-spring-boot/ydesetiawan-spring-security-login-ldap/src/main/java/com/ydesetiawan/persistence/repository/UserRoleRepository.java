package com.ydesetiawan.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ydesetiawan.persistence.model.UserRole;

/**
 * @author edys
 * @version 1.0, Feb 4, 2015
 * @since
 */
public interface UserRoleRepository extends JpaRepository<UserRole, String> {

}
