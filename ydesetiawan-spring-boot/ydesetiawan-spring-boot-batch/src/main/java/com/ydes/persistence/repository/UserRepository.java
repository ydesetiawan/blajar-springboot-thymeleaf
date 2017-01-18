package com.ydes.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ydes.persistence.model.User;

/**
 * @author edys
 * @version 1.0, Feb 4, 2015
 * @since
 */
public interface UserRepository extends JpaRepository<User, String> {

	User findByUsername(String username);

}
