/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
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

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
public class UserServiceTestIT extends BaseTestIT {

    @Test
    public void testFindById() {
        User ac = gospelService.findUserById("robot");
        assertNotNull(ac);
        assertEquals("robot", ac.getUsername());
        assertEquals("Cyborg Man", ac.getFullName());
        //assertEquals("123", ac.getPassword()); // now encrypted
        assertEquals(Boolean.TRUE, ac.isEnabled());
        assertEquals("Super User", ac.getRole().getName());
        
        assertNull(gospelService.findUserById(null));
        assertNull(gospelService.findUserById(""));
    }

    @Test
    public void testFindAll() {
        Page<User> result = gospelService.findAllUsers(new PageRequest(0, gospelService.countAllUsers().intValue()));
        assertTrue(result.getTotalElements() > 0);
    }

    @Test
    public void testFindByUsername() {
        assertNotNull(gospelService.findUserByUsername("robot"));
        assertNull(gospelService.findUserByUsername("cyborg"));
    }
}
