/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.controller;

import com.dariawan.gospel.domain.User;
import com.dariawan.gospel.service.GospelService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
public class BaseController {
    
    private final String CURRENT_USER = "CURRENT_USER";
    
    @Autowired
    protected GospelService gospelService;
    
    protected User getCurrentUser(HttpServletRequest request) {
        User ux = null;
        
        Object o = request.getSession().getAttribute(CURRENT_USER);
        if (o==null) {        
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth != null){
                Object principal = auth.getPrincipal();
                if(principal != null && org.springframework.security.core.userdetails.User.class.isAssignableFrom(principal.getClass())){
                    org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) principal;
                    ux = gospelService.findUserByUsername(u.getUsername());
                    
                    // save session
                    request.getSession().setAttribute(CURRENT_USER, ux);
                }
            }
        }
        else {
            ux = (User) o;
        }
        return ux;
    }
}
