package com.ydes.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ydes.persistence.model.User;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
public interface UserRepository extends JpaRepository<User, String> {

	User findByUsername(String username);

}
