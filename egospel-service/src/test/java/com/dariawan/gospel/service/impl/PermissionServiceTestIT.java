/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.service.impl;

import com.dariawan.gospel.domain.Permission;
import com.dariawan.gospel.domain.Role;
import com.dariawan.gospel.service.GospelService;
import java.util.List;
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
public class PermissionServiceTestIT extends BaseTestIT {

    @Test
    public void testFindById() {
        Permission ac = gospelService.findPermissionById("user-edit");
        assertNotNull(ac);
        assertEquals("User-Edit", ac.getLabel());
        assertEquals("ROLE_USER_EDIT", ac.getValue());
        assertNull(gospelService.findPermissionById(null));
        assertNull(gospelService.findPermissionById(""));
    }

    @Test
    public void testFindAll() {
        Page<Permission> result = gospelService.findAllPermissions(new PageRequest(0, gospelService.countAllPermissions().intValue()));
        assertTrue(result.getTotalElements() > 0);
    }
    
    @Test
    public void testFindNotInRole() {
        Role r = new Role();
        r.setId("staff");
        
        List<Permission> result = gospelService.findPermissionsNotInRole(r);
        //assertEquals(new Integer(5), new Integer(result.size()));
        assertTrue(new Integer(5) <= new Integer(result.size()));
        
//        for (Permission permission : result) {
//            if(permission.getId().equals("role-view")){
//                Assert.fail("View permission should not available");
//            }
//        }
    }
}
