/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.dao;

import com.dariawan.gospel.domain.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface ConfigurationDao extends PagingAndSortingRepository<Configuration, String> {
    @Query("select c from Configuration c " +
			"where lower(c.name) like lower(:search) " +
			"or lower(c.label) like lower(:search) " +
			"or lower(c.value) like lower(:search) " +
			"order by c.name")
    Page<Configuration> search(@Param("search") String search, Pageable page);
    
    @Query("select count(c) from Configuration c " +
			"where lower(c.name) like lower(:search) " +
			"or lower(c.label) like lower(:search) " +
			"or lower(c.value) like lower(:search)")
    Long count(@Param("search") String search);
}
