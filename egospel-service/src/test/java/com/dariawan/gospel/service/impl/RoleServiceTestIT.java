/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.service.impl;

import com.dariawan.gospel.domain.Menu;
import com.dariawan.gospel.domain.Permission;
import com.dariawan.gospel.domain.Role;
import com.dariawan.gospel.service.GospelService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class RoleServiceTestIT extends BaseTestIT {

    @Autowired
    private GospelService service;

    @Test
    public void testFindById() {
        Role role = service.findRoleById("superuser");
        assertNotNull(role);
        assertEquals("Super User", role.getName());
        assertEquals("Full Access", role.getDescription());
        
        assertNotNull(role.getMenuSet());
        
        for (Menu menu : role.getMenuSet()) {
            assertNotNull(menu.getLabel());
        }
        
        assertNotNull(role.getPermissionSet());
        
        for (Permission perm : role.getPermissionSet()) {
            assertNotNull(perm.getValue());
        }
        
        assertNull(service.findRoleById(null));
        assertNull(service.findRoleById(""));
    }

    @Test
    public void testFindAll() {
        Page<Role> result = service.findAllRoles(new PageRequest(0, service.countAllRoles().intValue()));
        assertTrue(result.getTotalElements() > 0);
    }
}
