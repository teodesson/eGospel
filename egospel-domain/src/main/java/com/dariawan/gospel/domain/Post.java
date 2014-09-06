/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@gmail.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Desson
 */
@Entity
@Table(name = "dar_post")
public class Post {
    
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_author", nullable = false)
    private User author;
     
    @NotNull
    @Column(name = "post_on", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date postOn = new Date();
    
    // post_date_gmt
    
    @NotNull
    @NotEmpty
    @Column(name = "content", nullable = false)
    @Type(type="text")
    private String content;
    
    @NotNull
    @NotEmpty
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    
    // post_excerpt
    
    @NotNull
    @NotEmpty
    @Column(name = "post_status", nullable = false, length = 20)
    private String postStatus = "draft";
    
    @NotNull
    @NotEmpty
    @Column(name = "comment_status", nullable = false, length = 20)
    private String commentStatus = "open";
    
    // ping_status
    // post_password
    // post_name 
    // to_ping 
    // pinged
    
    @Column(name = "post_modified")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date postModified = new Date();
    
    // post_modified_gmt
    // post_content_filtered
    // post_parent
    // guid 
    // menu_order 
    
    @NotNull
    @NotEmpty
    @Column(name = "post_type", nullable = false, length = 20)
    private String postType = "post";
    
    // post_mime_type
    // comment_count

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getPostOn() {
        return postOn;
    }

    public void setPostOn(Date postOn) {
        this.postOn = postOn;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    public Date getPostModified() {
        return postModified;
    }

    public void setPostModified(Date postModified) {
        this.postModified = postModified;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }
}
