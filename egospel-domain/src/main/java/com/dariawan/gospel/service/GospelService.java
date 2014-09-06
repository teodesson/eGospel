/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.service;

import com.dariawan.gospel.domain.ApplicationConfig;
import com.dariawan.gospel.domain.Menu;
import com.dariawan.gospel.domain.Permission;
import com.dariawan.gospel.domain.Post;
import com.dariawan.gospel.domain.Role;
import com.dariawan.gospel.domain.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GospelService extends MonitoredService {
        // appication configuration
	void save(ApplicationConfig ac);
	void delete(ApplicationConfig ac);
	ApplicationConfig findApplicationConfigById(String id);
        Page<ApplicationConfig> findAllApplicationConfigs(Pageable pageable);
	Long countAllApplicationConfigs();
	Long countApplicationConfigs(String search);
	Page<ApplicationConfig> findApplicationConfigs(String search, Pageable pageable);

        // menu
        void save(Menu m);
        void delete(Menu m);
        Menu findMenuById(String id);
        List<Menu> findTopLevelMenu();
        Page<Menu> findAllMenu(Pageable pageable);
        List<Menu> findAllMenu();        
        Long countAllMenu();
        List<Menu> findMenuByParent(Menu m);
        List<Menu> findMenuNotInRole(Role r);

        // permission
        void save(Permission m);
        void delete(Permission m);
        Permission findPermissionById(String id);
        Page<Permission> findAllPermissions(Pageable pageable);
        List<Permission> findAllPermissions();
        Long countAllPermissions();
        List<Permission> findPermissionsNotInRole(Role r);

        // role
        void save(Role role);
        void delete(Role role);
        Role findRoleById(String id);
        Page<Role> findAllRoles(Pageable pageable);
        List<Role> findAllRoles();
        Long countAllRoles();

        // user
        void save(User m);
        void delete(User m);
        User findUserById(String id);
        User findUserByUsername(String username);
        Page<User> findAllUsers(Pageable pageable);
        List<User> findAllUsers();
        Long countAllUsers();

        // post
        void save(Post post);
        void delete(Post post);
        Post findPostById(String id);
        Page<Post> findAllPosts(String postType, Pageable pageable);        
        Long countAllPosts();
}
