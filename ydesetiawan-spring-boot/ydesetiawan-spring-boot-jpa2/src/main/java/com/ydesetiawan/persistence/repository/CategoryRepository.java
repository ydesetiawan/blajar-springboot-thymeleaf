package com.ydesetiawan.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ydesetiawan.persistence.model.Category;

/**
 * @author edys
 * @version 1.0, Jan 14, 2015
 * @since 
 */
public interface CategoryRepository extends JpaRepository<Category, String> {

}
