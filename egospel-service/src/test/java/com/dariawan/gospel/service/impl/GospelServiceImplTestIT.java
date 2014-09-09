/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dariawan.gospel.domain.Configuration;
import com.dariawan.gospel.service.GospelService;
import org.springframework.data.domain.PageRequest;

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
public class GospelServiceImplTestIT extends BaseTestIT {

    @Autowired
    private DataSource dataSource;

    @Before
    public void resetDatabase() throws Exception {

        IDataSet[] daftarDataset = new IDataSet[]{
                new FlatXmlDataSetBuilder().build(new File("src/test/resources/sample-data.xml"))
        };

        Connection conn = dataSource.getConnection();
        DatabaseOperation.CLEAN_INSERT
                .execute(new DatabaseConnection(conn),
                        new CompositeDataSet(daftarDataset));
    }

    @Test
    public void testFindById() {
        Configuration ac = gospelService.findConfigurationById("def456");
        assertNotNull(ac);
        assertEquals("applicationVersion", ac.getName());
        assertEquals("Application Version", ac.getLabel());
        assertEquals("0.0.1", ac.getValue());
    }

    @Test
    public void testSaveNew() {
        Configuration ac = new Configuration();
        ac.setName("base.path");
        ac.setLabel("Installation Path");
        ac.setValue("/opt");
        Long countAll = gospelService.countAllConfiguration();
        gospelService.save(ac);
        assertEquals(Long.valueOf(countAll + 1), gospelService.countAllConfiguration());
        assertNotNull(ac.getId());
    }

    @Test
    public void testSaveExisting() {
        Configuration ac = gospelService.findConfigurationById("abc123");
        assertNotNull(ac);
        ac.setLabel("Application Version");
        ac.setValue("1.0");
        gospelService.save(ac);
        Configuration ac1 = gospelService.findConfigurationById("abc123");
        assertNotNull(ac1);
        assertEquals("Application Version", ac1.getLabel());
        assertEquals("1.0", ac1.getValue());
    }

    @Test
    public void testDeleteExisting() {
        Configuration ac = gospelService.findConfigurationById("abc123");
        assertNotNull(ac);
        Long countAll = gospelService.countAllConfiguration();
        gospelService.delete(ac);
        assertEquals(Long.valueOf(countAll - 1), gospelService.countAllConfiguration());
        Configuration ac1 = gospelService.findConfigurationById("applicationVersion");
        assertNull(ac1);
    }

    @Test
    public void testFindAll() {
        List<Configuration> result =
                gospelService.findAllConfiguration(new PageRequest(0, gospelService.countAllConfiguration().intValue())).getContent();
        assertTrue(result.size() > 0);
    }

    @Test
    public void testSearch() {
        List<Configuration> result = gospelService.findConfiguration("name", new PageRequest(0, 5)).getContent();
        assertTrue(result.size() == 1);
    }

    @Test
    public void testCountSearch() {
        Long result = gospelService.countConfiguration("name");
        assertTrue(result == 1);
    }
}
