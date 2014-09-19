/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.controller;

import com.dariawan.gospel.domain.Post;
import com.dariawan.gospel.domain.Role;
import com.dariawan.gospel.domain.User;
import java.net.URI;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
@Controller
public class PostController extends BaseController { 
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/post/{id}")
    @ResponseBody
    public Post findById(@PathVariable String id) {
        Post x = gospelService.findPostById(id);
        
        // set role to null?
        if (x!=null) {
            fixLie(x);
        }
        
        if (x == null) {
            throw new IllegalStateException();
        }
        return x;
    }
    
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Post x, HttpServletRequest request, HttpServletResponse response) {
        User ux = getCurrentUser(request);
        x.setAuthor(ux);
        
        gospelService.save(x);
        String requestUrl = request.getRequestURL().toString();
        URI uri = new UriTemplate("{requestUrl}/{id}").expand(requestUrl, x.getId());
        response.setHeader("Location", uri.toASCIIString());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable String id, @RequestBody @Valid Post x) {
        Post a = gospelService.findPostById(id);
        if (a == null) {
            logger.warn("Post with id [{}] not found", id);
            throw new IllegalStateException();
        }
        //x.setId(a.getId());
        a.setTitle(x.getTitle());
        a.setContent(x.getContent());
        gospelService.save(a);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        Post a = gospelService.findPostById(id);
        if (a == null) {
            logger.warn("Post with id [{}] not found", id);
            throw new IllegalStateException();
        }
        gospelService.delete(a);
    }

    @RequestMapping(value = "/post4page", method = RequestMethod.GET)
    @ResponseBody
    public Page<Post> findAll(
            @RequestParam(required = true) String postType,
            Pageable pageable) {
        Page<Post> result = gospelService.findAllPosts(postType, pageable);
        for(Post p : result){
            fixLie(p);
        }
        return result;

    }   
    
    // help creation of object
    @RequestMapping("/post/dummy")
    @ResponseBody
    public Post getADummyPost() {
        Role r = new Role();
        r.setId("peon");

        User u = new User();
        u.setFirstName("Loyal");
        u.setLastName("Peon");
        u.setUsername("peon");
        u.setPassword("pwd");
        u.setEnabled(true);
        u.setRole(r);
        
        Post x = new Post();
        x.setTitle("New Post");
        x.setContent("Content of new post");
        x.setAuthor(u);
        
        return x;
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({IllegalStateException.class})
    public void handle() {
        logger.debug("Resource with specified URI not found");
    }
    
     private void fixLie(Post p){
       if (p.getAuthor()!=null) {
            p.getAuthor().setRole(null);
        }
    }
}
