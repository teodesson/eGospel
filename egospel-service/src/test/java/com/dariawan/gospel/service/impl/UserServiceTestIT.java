/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@gmail.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.service.impl;

import com.dariawan.gospel.domain.User;
import com.dariawan.gospel.service.GospelService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class UserServiceTestIT extends BaseTestIT {

    @Autowired
    private GospelService service;

    @Test
    public void testFindById() {
        User ac = service.findUserById("robot");
        assertNotNull(ac);
        assertEquals("robot", ac.getUsername());
        assertEquals("Cyborg Man", ac.getFullName());
        //assertEquals("123", ac.getPassword()); // now encrypted
        assertEquals(Boolean.TRUE, ac.isEnabled());
        assertEquals("Super User", ac.getRole().getName());
        
        assertNull(service.findUserById(null));
        assertNull(service.findUserById(""));
    }

    @Test
    public void testFindAll() {
        Page<User> result = service.findAllUsers(new PageRequest(0, service.countAllUsers().intValue()));
        assertTrue(result.getTotalElements() > 0);
    }

    @Test
    public void testFindByUsername() {
        assertNotNull(service.findUserByUsername("robot"));
        assertNull(service.findUserByUsername("cyborg"));
    }
}
