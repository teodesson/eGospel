/**
 * eGospel Project Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions
 * of the license agreement by which you acquired this software. You may not use
 * this software if you have not validly acquired a license for the software
 * from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dariawan.gospel.service.GospelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
@Controller
public class HomepageController extends BaseController {

    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/homepage/userinfo")
    @ResponseBody
    public Map<String, String> userInfo() {
        Map<String, String> result = new HashMap<>();
        com.dariawan.gospel.domain.User ux = getMyUserInfo();
        if (ux != null) {
            result.put("user", ux.getUsername());
            if (ux.getRole() != null || ux.getRole().getName() != null) {
                result.put("group", ux.getRole().getName());
            } else {
                result.put("group", "undefined");
            }
        } else {
            result.put("user", "undefined");
            result.put("group", "undefined");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal != null && User.class.isAssignableFrom(principal.getClass())) {
                User u = (User) principal;

            }
        }

        return result;
    }

    @RequestMapping("/homepage/myuserinfo")
    @ResponseBody
    public com.dariawan.gospel.domain.User getMyUserInfo() {
        com.dariawan.gospel.domain.User ux = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal != null && User.class.isAssignableFrom(principal.getClass())) {
                User u = (User) principal;
                ux = gospelService.findUserByUsername(u.getUsername());
            }
        }

        return ux;
    }

    @RequestMapping("/homepage/appinfo")
    @ResponseBody
    public Map<String, String> appInfo(HttpServletRequest request) {

        ApplicationContext ctx = WebApplicationContextUtils
                .getWebApplicationContext(request.getSession().getServletContext());

        Map<String, String> result = new HashMap<>();

        result.put("profileDefault", StringUtils.arrayToCommaDelimitedString(ctx.getEnvironment().getDefaultProfiles()));
        result.put("profileActive", StringUtils.arrayToCommaDelimitedString(ctx.getEnvironment().getActiveProfiles()));
        result.put("applicationName", messageSource.getMessage("app.name", null, "undefined", null));
        result.put("applicationVersion", messageSource.getMessage("app.version", null, "x.x.x", null));

        return result;
    }

    @RequestMapping("/homepage/sessioninfo")
    @ResponseBody
    public List<Map<String, String>> sessionInfo() {

        List<Map<String, String>> userAktif = new ArrayList<>();

        for (Object object : sessionRegistry.getAllPrincipals()) {
            List<SessionInformation> info = sessionRegistry.getAllSessions(object, true);
            for (SessionInformation i : info) {
                Object p = i.getPrincipal();
                if (p != null && User.class.isAssignableFrom(p.getClass())) {
                    Map<String, String> usermap = new HashMap<String, String>();

                    User u = (User) p;
                    usermap.put("username", u.getUsername());
                    usermap.put("permission", u.getAuthorities().toString());
                    usermap.put("sessionid", i.getSessionId());
                    usermap.put("status", i.isExpired() ? "Expired" : "Aktif");
                    userAktif.add(usermap);
                }
            }
        }

        return userAktif;
    }

    @RequestMapping(value = "/homepage/kick/{sessionid}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void forceLogout(@PathVariable String sessionid) {
        SessionInformation info = sessionRegistry.getSessionInformation(sessionid);
        if (info != null) {
            info.expireNow();
        }
    }

    @RequestMapping("/homepage/usermsg")
    @ResponseBody
    public Map<String, String> userPage(HttpServletRequest request) {

        Map<String, String> result = new HashMap<>();

        result.put("inputRequired", messageSource.getMessage("global.input.required", null, "Input required [Default]", null));
        result.put("userAlreadyExists", messageSource.getMessage("error.user.already.exists", null, "User already exists [Default]", null));

        return result;
    }
}
