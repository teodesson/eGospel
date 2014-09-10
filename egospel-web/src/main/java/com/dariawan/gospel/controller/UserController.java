/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.controller;

import com.dariawan.gospel.domain.Role;
import com.dariawan.gospel.domain.User;
import com.dariawan.gospel.service.GospelService;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriTemplate;

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
@Controller
public class UserController extends BaseController { 

    final private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final List<String> FILE_EXTENSION = Arrays.asList("png", "jpg", "jpeg");
    private final String SESSION_KEY_IMAGE = "sessionKeyPathImage";

    private final String DEFAULT_PHOTO = "img/user/no_photo.jpg";
    
    @RequestMapping("/user/{id}")
    @ResponseBody
    public User findById(@PathVariable String id) {
        User x = gospelService.findUserById(id);
        if (x == null) {
            throw new IllegalStateException();
        }
        fixLie(x);
        return x;
    }
    
    @RequestMapping(value = "/user/photo", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Map<String, Object> upload(@RequestParam(value="photo", required=true) MultipartFile multipartFile, 
        HttpServletRequest request, HttpSession session){
        Map<String, Object> result = new HashMap();
        
        if(multipartFile == null || multipartFile.isEmpty()){
            result.put("msg", "No file uploaded");
            result.put("status", "400");
            //result.put("status", "200"); // it's ok
        }else {
            String extension = tokenizer(multipartFile.getOriginalFilename(), ".");
            if(FILE_EXTENSION.contains(extension.toLowerCase())){
                try {
                    String filename = multipartFile.getOriginalFilename();
                    String targetFile = File.separator + "tmp" + File.separator + filename;
                    multipartFile.transferTo(new File(targetFile));
                    session.setAttribute(SESSION_KEY_IMAGE, targetFile);
                    result.put("msg", "Upload Succeded");
                    result.put("status", "200");
                } catch (IOException | IllegalStateException ex) {
                    result.put("msg", ex.getMessage());
                    result.put("status", "500");
                }
            } else {
                result.put("msg", "File extensions not permitted");
                result.put("status", "400");
            }
        }
        return result;
    }
    
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid User x, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String defaultPhoto = DEFAULT_PHOTO;
        String pathPhoto = "img" + File.separator + "user"; // img/user
        
        if(session.getAttribute(SESSION_KEY_IMAGE) != null){
            defaultPhoto = (String) session.getAttribute(SESSION_KEY_IMAGE);
        }
        
        // change to img/user/no_photo.jpg
        String filename = "pic_" + x.getUsername().toLowerCase() + "." + tokenizer(defaultPhoto, ".");
        x.setPhoto(pathPhoto + File.separator + filename);
        logger.debug("SET PHOTO PATH : " + x.getPhoto());
        
        File tmp = new File(defaultPhoto);
        File newFile = new File(request.getSession().getServletContext().getRealPath(pathPhoto), filename);
        if(tmp.exists()){
            logger.info("FILE MOVED TO : " + newFile.getAbsolutePath() + File.separator + filename);
            tmp.renameTo(newFile);
        }
        
        // fix role (tricky)
        if (x.getRole()==null || x.getRole().getId()==null) {
            Role role = gospelService.findRoleById("subscriber"); // default
            x.setRole(role);
        }
        // validate email
        boolean validEmail = true;
        if (x.getEmail()!=null && !x.getEmail().isEmpty()) {
            validEmail = validateEmail(x.getEmail());
        }
            
        if (validEmail) {
            gospelService.save(x);

            session.removeAttribute(SESSION_KEY_IMAGE);

            String requestUrl = request.getRequestURL().toString();
            URI uri = new UriTemplate("{requestUrl}/{id}").expand(requestUrl, x.getId());
            response.setHeader("Location", uri.toASCIIString());
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable String id, @RequestBody @Valid User x, HttpServletRequest request, HttpSession session) {
        User a = gospelService.findUserById(id);
        if (a == null) {
            logger.warn("User with id [{}] not exists", id);
            throw new IllegalStateException();
        }
        x.setId(a.getId());
        
        String defaultPhoto = DEFAULT_PHOTO;
        String pathPhoto = "img" + File.separator + "user"; // img/user
        
        if(session.getAttribute(SESSION_KEY_IMAGE) != null){
            defaultPhoto = (String) session.getAttribute(SESSION_KEY_IMAGE);
        }
        
        // change to img/user/no_photo.jpg
        String filename = "pic_" + x.getUsername().toLowerCase() + "." + tokenizer(defaultPhoto, ".");
        x.setPhoto(pathPhoto + File.separator + filename);
        logger.debug("SET PHOTO PATH : " + x.getPhoto());
        
        File tmp = new File(defaultPhoto);
        File newFile = new File(request.getSession().getServletContext().getRealPath(pathPhoto), filename);
        if(tmp.exists()){
            logger.info("FILE MOVED TO : " + newFile.getAbsolutePath() + File.separator + filename);
            tmp.renameTo(newFile);
        }
        
        gospelService.save(x);
        session.removeAttribute(SESSION_KEY_IMAGE);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        User a = gospelService.findUserById(id);
        if (a == null) {
            logger.warn("User with id [{}] not exists", id);
            throw new IllegalStateException();
        }
        gospelService.delete(a);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public List<User> findAll() {
        List<User> result = gospelService.findAllUsers();
        for(User u : result){
            fixLie(u);
        }
        return result;
    }

    @RequestMapping(value = "/user4page", method = RequestMethod.GET)
    @ResponseBody
    public Page<User> findAll(
            Pageable pageable) {
        Page<User> result = gospelService.findAllUsers(pageable);
        for(User u : result){
            fixLie(u);
        }
        return result;
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({IllegalStateException.class})
    public void handle() {
        logger.debug("Resource with specified URI not exists");
    }

    private void fixLie(User x){
        x.getRole().setPermissionSet(null);
        x.getRole().setMenuSet(null);
        
        if (x.getPhoto()==null || x.getPhoto().isEmpty()) { // no photo
            x.setPhoto(DEFAULT_PHOTO);
        }
    }
    
    private String tokenizer(String filename, String token){
        StringTokenizer st = new StringTokenizer(filename, token);
        String result = "";
        while(st.hasMoreTokens()){
            result = st.nextToken();
        }
        return result;
    }
    
    private boolean validateEmail(String email) {
        boolean isValid = false;
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            isValid = true;
        }
        catch (AddressException ae) {
            logger.info("Email [{}] not valid", email);
        }
        return isValid;
    }
}
