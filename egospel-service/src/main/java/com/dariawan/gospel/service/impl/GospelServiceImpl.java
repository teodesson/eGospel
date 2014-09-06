/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@gmail.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.service.impl;

import com.dariawan.gospel.dao.PermissionDao;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.dariawan.gospel.dao.ApplicationConfigDao;
import com.dariawan.gospel.dao.MenuDao;
import com.dariawan.gospel.dao.PostDao;
import com.dariawan.gospel.dao.RoleDao;
import com.dariawan.gospel.dao.UserDao;
import com.dariawan.gospel.domain.ApplicationConfig;
import com.dariawan.gospel.domain.Menu;
import com.dariawan.gospel.domain.Permission;
import com.dariawan.gospel.domain.Post;
import com.dariawan.gospel.domain.Role;
import com.dariawan.gospel.domain.User;
import com.dariawan.gospel.service.GospelService;
import java.util.ArrayList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service("gospelService")
@Transactional
public class GospelServiceImpl implements GospelService {

    @Autowired
    private ApplicationConfigDao applicationConfigDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private PostDao postDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDao userDao;

    @Override
    public void save(ApplicationConfig ac) {
        applicationConfigDao.save(ac);
    }

    @Override
    public void delete(ApplicationConfig ac) {
        if (ac == null || ac.getId() == null) {
            return;
        }
        applicationConfigDao.delete(ac);
    }

    @Override
    public ApplicationConfig findApplicationConfigById(String id) {
        if (!StringUtils.hasText(id)) {
            return null;
        }
        return applicationConfigDao.findOne(id);
    }

    @Override
    public Page<ApplicationConfig> findAllApplicationConfigs(Pageable pageable) {
        if(pageable == null){
            pageable = new PageRequest(0, 20);
        }
        return applicationConfigDao.findAll(pageable);
    }

    @Override
    public Long countAllApplicationConfigs() {
        return applicationConfigDao.count();
    }

    @Override
    public Page<ApplicationConfig> findApplicationConfigs(String search, Pageable pageable) {
        if (!StringUtils.hasText(search)) {
            return findAllApplicationConfigs(pageable);
        }
        
        if(pageable == null){
            pageable = new PageRequest(0, 20);
        }

        return applicationConfigDao.search("%" + search + "%", pageable);
    }

    @Override
    public Long countApplicationConfigs(String search) {
        if (!StringUtils.hasText(search)) {
            return countAllApplicationConfigs();
        }
        return applicationConfigDao.count("%" + search + "%");
    }

    @Override
    public void save(Menu m) {
        menuDao.save(m);
    }

    @Override
    public void delete(Menu m) {
        menuDao.delete(m);
    }

    @Override
    public Menu findMenuById(String id) {
        if (!StringUtils.hasText(id)) {
            return null;
        }
        return menuDao.findOne(id);
    }

    @Override
    public List<Menu> findTopLevelMenu() {
        return menuDao.findTopLevelMenu();
    }
    
    @Override
    public Page<Menu> findAllMenu(Pageable pageable) {
        return menuDao.findAll(pageable);
    }
    
    @Override
    public List<Menu> findAllMenu() {
        return (List<Menu>) menuDao.findAll();
    }
    
    @Override
    public Long countAllMenu() {
        return menuDao.count();
    }

    @Override
    public List<Menu> findMenuByParent(Menu m) {
        if(m == null || !StringUtils.hasText(m.getId())) {
            return new ArrayList<>();
        }
        return menuDao.findMenuByParent(m.getId());
    }
    
    @Override
    public List<Menu> findMenuNotInRole(Role role){
        if(role == null){
            return new ArrayList<>();
        }
        
        Role r = findRoleById(role.getId());
        if (r == null || r.getMenuSet()==null){
            return new ArrayList<>();
        }        
        
        List<String> ids = new ArrayList<>();
        for (Menu m : r.getMenuSet()) {
            ids.add(m.getId());
        }
        
        if (ids.isEmpty()) {
            ids.add("### DUMMY ###");
        }
        return menuDao.findByIdNotIn(ids);        
    }

    @Override
    public void save(Permission m) {
        permissionDao.save(m);
    }

    @Override
    public void delete(Permission m) {
        permissionDao.delete(m);
    }

    @Override
    public Permission findPermissionById(String id) {
        if(!StringUtils.hasText(id)){
            return null;
        }
        return permissionDao.findOne(id);
    }

    @Override
    public Page<Permission> findAllPermissions(Pageable pageable) {
        if(pageable == null){
            pageable = new PageRequest(0, 20);
        }
        return permissionDao.findAll(pageable);
    }

    @Override
    public List<Permission> findAllPermissions() {
        return (List<Permission>) permissionDao.findAll();
    }
    
    @Override
    public Long countAllPermissions() {
        return permissionDao.count();
    }
    
    @Override
    public List<Permission> findPermissionsNotInRole(Role role) {
        if(role == null){
            return new ArrayList<>();
        }
        
        Role r = findRoleById(role.getId());
        if (r == null || r.getPermissionSet()==null){
            return new ArrayList<>();
        }
        
        List<String> ids = new ArrayList<>();            
        for (Permission p : r.getPermissionSet()) {
            ids.add(p.getId());
        }
        
        if (ids.isEmpty()) {
            ids.add("### DUMMY ###");
        }
        return permissionDao.findByIdNotIn(ids);
    }

    @Override
    public void save(Post p) {
        // check author (user)
        if (p.getAuthor()!=null) {
            if (p.getAuthor().getId()==null) {
                save(p.getAuthor()); // save user 1st
            }
            else {
                User u = userDao.findOne(p.getAuthor().getId());
                if (u!=null) {
                    p.setAuthor(u); // get the real User
                }
                else {
                    save(u); // save user 1st
                }
            }
        }
        postDao.save(p);
    }

    @Override
    public void delete(Post p) {
        postDao.delete(p);
    }

    @Override
    public Post findPostById(String id) {
        if(!StringUtils.hasText(id)){
            return null;
        }
        return postDao.findOne(id);
    }

    @Override
    public Page<Post> findAllPosts(String postType, Pageable pageable) {
        return postDao.search(postType, pageable);
    }

    @Override
    public Long countAllPosts() {
        return postDao.count();
    }
    
    @Override
    public void save(Role role) {
        roleDao.save(role);
    }

    @Override
    public void delete(Role role) {
        roleDao.delete(role);
    }

    @Override
    public Role findRoleById(String id) {
        if(!StringUtils.hasText(id)){
            return null;
        }
        
        Role r = roleDao.findOne(id);
        if(r != null){
            r.getPermissionSet().size();
            r.getMenuSet().size();
        }
        
        return r;
    }

    @Override
    public Page<Role> findAllRoles(Pageable pageable) {
        return roleDao.findAll(pageable);
    }

    @Override
    public List<Role> findAllRoles() {
        return (List<Role>) roleDao.findAll();
    }
    
    @Override
    public Long countAllRoles() {
        return roleDao.count();
    }

    @Override
    public void save(User u) {
        // since this is 1st time
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(u.getPassword());
        u.setPassword(hashedPassword);

        userDao.save(u);
    }

    @Override
    public void delete(User m) {
        //System.out.println("delete " + m.getId());
        userDao.delete(m);
    }

    @Override
    public User findUserById(String id) {
        if(!StringUtils.hasText(id)){
            return null;
        }
        return userDao.findOne(id);
    }

    @Override
    public User findUserByUsername(String username) {
        if(!StringUtils.hasText(username)){
            return null;
        }
        return userDao.findByUsername(username);
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return userDao.findAll(pageable);
    }

    @Override
    public List<User> findAllUsers() {
        return (List<User>) userDao.findAll();
    }
    
    @Override
    public Long countAllUsers() {
        return userDao.count();
    }
}
