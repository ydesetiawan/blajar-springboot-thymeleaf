/* Copyright (C) 2015 ASYX International B.V. All rights reserved. */
package com.ydesetiawan.persistence.repository;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.ydesetiawan.config.DatabaseConfig;
import com.ydesetiawan.config.MockEnvironmentApplicationContextInitializer;
import com.ydesetiawan.config.H2DataSourceConfig;
import com.ydesetiawan.persistence.model.Category;
import com.ydesetiawan.persistence.model.Item;

/**
 * @author edys
 * @version 1.0, Jan 14, 2015
 * @since
 */
@ContextConfiguration(classes = { H2DataSourceConfig.class,
        DatabaseConfig.class },
        initializers = MockEnvironmentApplicationContextInitializer.class)
@Test(dependsOnGroups = { "categoryRepositoryGroup" })
public class ItemRepositoryTest extends AbstractTestNGSpringContextTests {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(ItemRepositoryTest.class);

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static String UUID_ITEM1 = "3bac4dfc-9c8c-11e4-af2a-206a8a6823b0";
    private static String UUID_CATEGORY2 = "4b6d2a4e-9bde-11e4-b509-206a8a6823b0";

    @Test
    public void testAddItem() {

        Category category1 = categoryRepository.findOne(UUID_CATEGORY2);
        log.info("............... category : " + category1);
        Item item1 = new Item(UUID_ITEM1, category1, "Sepatu", 2,
                new BigDecimal("1000000"), "Sepatu Baru");

        itemRepository.save(Arrays.asList(item1));
        itemRepository.flush();
    }

    @Test(dependsOnMethods = { "testAddItem" })
    public void testCountItem() {
        assertEquals(itemRepository.count(), 1);
        Item item = itemRepository.findOne(UUID_ITEM1);
        log.info("............... item : " + item.toString());
    }

}
