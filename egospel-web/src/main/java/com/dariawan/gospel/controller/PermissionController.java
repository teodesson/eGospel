/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.controller;

import com.dariawan.gospel.domain.Permission;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
@Controller
public class PermissionController extends BaseController { 

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/permission/{id}")
    @ResponseBody
    public Permission findById(@PathVariable String id) {
        Permission x = gospelService.findPermissionById(id);
        if (x == null) {
            throw new IllegalStateException();
        }
        return x;
    }

    @RequestMapping(value = "/permission", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Permission x, HttpServletRequest request, HttpServletResponse response) {
        gospelService.save(x);
        String requestUrl = request.getRequestURL().toString();
        URI uri = new UriTemplate("{requestUrl}/{id}").expand(requestUrl, x.getId());
        response.setHeader("Location", uri.toASCIIString());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/permission/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable String id, @RequestBody @Valid Permission x) {
        Permission a = gospelService.findPermissionById(id);
        if (a == null) {
            logger.warn("Permission with id [{}] not found", id);
            throw new IllegalStateException();
        }
        x.setId(a.getId());
        gospelService.save(x);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/permission/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        Permission a = gospelService.findPermissionById(id);
        if (a == null) {
            logger.warn("Permission with id [{}] not found", id);
            throw new IllegalStateException();
        }
        gospelService.delete(a);
    }

    @RequestMapping(value = "/permission4page", method = RequestMethod.GET)
    @ResponseBody
    public Page<Permission> findAll(
            Pageable pageable) {
        return gospelService.findAllPermissions(pageable);
    }

    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    @ResponseBody
    public List<Permission> findAll() {
        return gospelService.findAllPermissions();
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({IllegalStateException.class})
    public void handle() {
        logger.debug("Resource with specified URI not found");
    }
}
