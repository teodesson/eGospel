/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.dao;

import com.dariawan.gospel.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDao extends PagingAndSortingRepository<User, String> {
    //, UserDetailsService
    
    User findByUsername(String username);
    
    /*
    @Override
    @Query("select u from User u where u.username=:username")
    UserDetails loadUserByUsername(@Param("username") String string) throws UsernameNotFoundException;
    */
}
