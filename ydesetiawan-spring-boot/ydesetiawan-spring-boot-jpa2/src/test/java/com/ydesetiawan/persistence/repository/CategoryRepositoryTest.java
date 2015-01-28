/* Copyright (C) 2015 ASYX International B.V. All rights reserved. */
package com.ydesetiawan.persistence.repository;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.ydesetiawan.config.DatabaseConfig;
import com.ydesetiawan.config.MockEnvironmentApplicationContextInitializer;
import com.ydesetiawan.config.H2DataSourceConfig;
import com.ydesetiawan.persistence.model.Category;

/**
 * @author edys
 * @version 1.0, Jan 14, 2015
 * @since
 */
@ContextConfiguration(classes = { H2DataSourceConfig.class,
        DatabaseConfig.class },
        initializers = MockEnvironmentApplicationContextInitializer.class)
@Test(groups = { "categoryRepositoryGroup" })
public class CategoryRepositoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private CategoryRepository categoryRepository;

    private static String UUID_CATEGORY1 = "69ca6822-9bdd-11e4-b509-206a8a6823b0";
    private static String UUID_CATEGORY2 = "4b6d2a4e-9bde-11e4-b509-206a8a6823b0";

    @Test
    public void testAddCategory() {

        Category c1 = new Category(UUID_CATEGORY1, "Food", "Food");
        Category c2 = new Category(UUID_CATEGORY2, "Football", "Football");

        categoryRepository.save(Arrays.asList(c1, c2));
        categoryRepository.flush();
    }

    @Test(dependsOnMethods = { "testAddCategory" })
    public void testCountCategory() {
        assertEquals(categoryRepository.count(), 2);
    }

    @Test(dependsOnMethods = { "testCountCategory" })
    public void testRemoveCategory() {
        categoryRepository.delete(UUID_CATEGORY1);
        assertEquals(categoryRepository.count(), 1);
        assertFalse(categoryRepository.exists(UUID_CATEGORY1));
    }

}
