/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.service.impl;

import com.dariawan.gospel.domain.Menu;
import com.dariawan.gospel.domain.Role;
import com.dariawan.gospel.service.GospelService;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class MenuServiceTestIT extends BaseTestIT {
    
    @Autowired
    private GospelService gospelService;
    
    @Test
    public void testFindAll() {
        Page<Menu> result = gospelService.findAllMenu(new PageRequest(0, 
                gospelService.countAllMenu().intValue()));
        assertTrue(result.getTotalElements() > 0);
    }
    
    @Test
    public void testFindTopLevelMenu(){
        List<Menu> result = gospelService.findTopLevelMenu();
        assertNotNull(result);
        
        //assertTrue(result.size() >= 4);
        
        Menu system = result.get(0);
        assertEquals("system", system.getId());
        assertEquals("#", system.getAction());
        assertEquals("System", system.getLabel());
        assertEquals(Integer.valueOf(0), system.getLevel());
        assertNull(system.getOptions());
        assertEquals(Integer.valueOf(0), system.getOrder());
        assertNull(system.getParent());
    }
    
    @Test
    public void testFindMenuByParent(){
        Menu m = gospelService.findMenuById("system");
        
        List<Menu> result = gospelService.findMenuByParent(m);
        assertNotNull(result);
        
        assertTrue(result.size() == 8);
        
        Menu header = result.get(0);
        assertEquals("system-header", header.getId());
        assertNull(header.getAction());
        assertEquals("System", header.getLabel());
        assertEquals(Integer.valueOf(1), header.getLevel());
        assertEquals("{css-class:nav-header}", header.getOptions());
        assertEquals(Integer.valueOf(0), header.getOrder());
        assertNotNull(header.getParent());
        
        Menu permission = result.get(7);
        assertEquals("system-permission", permission.getId());
        assertEquals("#/system/permission", permission.getAction());
        assertEquals("Permission", permission.getLabel());
        assertEquals(Integer.valueOf(1), permission.getLevel());
        assertNull(permission.getOptions());
        assertEquals(Integer.valueOf(7), permission.getOrder());
        assertNotNull(permission.getParent());
    }
    
    @Test
    public void testFindNotInRole() {
        Role r = new Role();
        r.setId("staff");
        
        List<Menu> result = gospelService.findMenuNotInRole(r);
        //assertTrue(new Integer(13) <= new Integer(result.size()));
        
        for (Menu menu : result) {
            if(menu.getId().equals("master")){
                Assert.fail("Menu master should not available");
            }
        }
    }
}
