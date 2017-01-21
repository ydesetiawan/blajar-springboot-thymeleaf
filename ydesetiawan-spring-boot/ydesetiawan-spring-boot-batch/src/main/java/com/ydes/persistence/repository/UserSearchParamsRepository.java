package com.ydes.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ydes.persistence.model.UserSearchParams;
/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
public interface UserSearchParamsRepository extends
		JpaRepository<UserSearchParams, String> {

}
