/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@gmail.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.controller;

import com.dariawan.gospel.domain.Menu;
import com.dariawan.gospel.domain.Permission;
import com.dariawan.gospel.domain.Role;
import com.dariawan.gospel.service.GospelService;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

@Controller
public class RoleController {

    @Autowired
    private GospelService gospelService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/role/{id}")
    @ResponseBody
    public Role findById(@PathVariable String id) {
        Role x = gospelService.findRoleById(id);
        if (x == null) {
            throw new IllegalStateException();
        }
        return x;
    }
    
    @RequestMapping("/role/{id}/unselected-permission")
    @ResponseBody
    public List<Permission> findPermissionNotInRole(@PathVariable String id) {
        return gospelService.findPermissionsNotInRole(gospelService.findRoleById(id));
    }
    
    @RequestMapping("/role/{id}/unselected-menu")
    @ResponseBody
    public List<Menu> findMenuNotInRole(@PathVariable String id) {
        return gospelService.findMenuNotInRole(gospelService.findRoleById(id));
    }

    @RequestMapping(value = "/role", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Role x, HttpServletRequest request, HttpServletResponse response) {
        gospelService.save(x);
        String requestUrl = request.getRequestURL().toString();
        URI uri = new UriTemplate("{requestUrl}/{id}").expand(requestUrl, x.getId());
        response.setHeader("Location", uri.toASCIIString());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/role/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable String id, @RequestBody @Valid Role x) {
        Role a = gospelService.findRoleById(id);
        if (a == null) {
            logger.warn("Role with id [{}] not found", id);
            throw new IllegalStateException();
        }
        x.setId(a.getId());
        gospelService.save(x);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/role/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        Role a = gospelService.findRoleById(id);
        if (a == null) {
            logger.warn("Role with id [{}] not found", id);
            throw new IllegalStateException();
        }
        gospelService.delete(a);
    }

    @RequestMapping(value = "/role", method = RequestMethod.GET)
    @ResponseBody
    public List<Role> findAll() {
        List<Role> result = gospelService.findAllRoles();
        for(Role r : result){
            r.setPermissionSet(null);
            r.setMenuSet(null);
        }
        return result;
    }
    
    @RequestMapping(value = "/role4page", method = RequestMethod.GET)
    @ResponseBody
    public Page<Role> findAll(
            Pageable pageable) {
        Page<Role> result = gospelService.findAllRoles(pageable);
        for(Role r : result) {
            r.setPermissionSet(null);
            r.setMenuSet(null);
        }
        return result;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({IllegalStateException.class})
    public void handle() {
        logger.debug("Resource with specified URI not found");
    }
}
