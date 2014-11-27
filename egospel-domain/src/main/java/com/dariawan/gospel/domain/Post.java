/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
@Entity
@Table(name = "dar_post")
public class Post extends BaseMessage implements Serializable {
    
    // post_date_gmt
    
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
    @Temporal(TemporalType.TIMESTAMP)
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

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the postStatus
     */
    public String getPostStatus() {
        return postStatus;
    }

    /**
     * @param postStatus the postStatus to set
     */
    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    /**
     * @return the commentStatus
     */
    public String getCommentStatus() {
        return commentStatus;
    }

    /**
     * @param commentStatus the commentStatus to set
     */
    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    /**
     * @return the postModified
     */
    public Date getPostModified() {
        return postModified;
    }

    /**
     * @param postModified the postModified to set
     */
    public void setPostModified(Date postModified) {
        this.postModified = postModified;
    }

    /**
     * @return the postType
     */
    public String getPostType() {
        return postType;
    }

    /**
     * @param postType the postType to set
     */
    public void setPostType(String postType) {
        this.postType = postType;
    }
}
