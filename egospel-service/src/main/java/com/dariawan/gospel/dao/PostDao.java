/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.dao;

import com.dariawan.gospel.domain.ApplicationConfig;
import com.dariawan.gospel.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Desson
 */
public interface PostDao extends PagingAndSortingRepository<Post, String> {
    @Query("select p from Post p " +
			"where p.postType = :postType " +
			"order by p.postOn desc")
    Page<Post> search(@Param("postType") String postType, Pageable page);
}