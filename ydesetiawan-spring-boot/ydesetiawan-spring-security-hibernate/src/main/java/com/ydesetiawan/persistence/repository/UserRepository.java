package com.ydesetiawan.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ydesetiawan.persistence.model.User;

/**
 * @author edys
 * @version 1.0, Feb 4, 2015
 * @since
 */
public interface UserRepository extends JpaRepository<User, String> {

	User findByUsername(String username);

}
