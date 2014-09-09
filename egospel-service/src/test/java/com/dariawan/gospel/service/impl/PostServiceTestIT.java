/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.service.impl;

import com.dariawan.gospel.domain.Post;
import com.dariawan.gospel.service.GospelService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
public class PostServiceTestIT extends BaseTestIT {
    
    @Test
    public void testFindById() {
        Post post = gospelService.findPostById("test");
        assertNotNull(post);
        assertEquals("Test Title", post.getTitle());
        assertEquals("publish", post.getPostStatus());
        assertEquals("open", post.getCommentStatus());
        
        assertNull(gospelService.findPostById(null));
        assertNull(gospelService.findPostById(""));
    }

    @Test
    public void testFindAll() {
        Page<Post> result = gospelService.findAllPosts("post", new PageRequest(0, gospelService.countAllPosts().intValue()));
        assertTrue(result.getTotalElements() > 0);
    }
}
