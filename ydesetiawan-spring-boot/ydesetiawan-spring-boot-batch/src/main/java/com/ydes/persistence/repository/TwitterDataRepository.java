package com.ydes.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ydes.persistence.model.TwitterData;

public interface TwitterDataRepository extends
		JpaRepository<TwitterData, String> {

}
