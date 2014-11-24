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
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import com.jayway.restassured.authentication.FormAuthConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
public class PostControllerTestIT extends BaseControllerTestIT {
    
    @Override
    public String getModule() {
        return "gospel/post";
    }
    
    @Test
    public void testSaveInvalid(){
        Post u = new Post();
        
        given()
            .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
            .contentType("application/json")
            .body(u)
            .expect().statusCode(400).when().post(target);
    }
    
    @Test
    public void testSaveUpdateDelete() {

        Role r = new Role();
        r.setId("superuser");

        User u = new User();
        u.setFirstName("Tryout");
        u.setFirstName("User");
        u.setUsername("test");
        u.setPassword("test123");
        u.setEnabled(true);
        u.setRole(r);
        
        Post x = new Post();
        x.setTitle("Tryout Post");
        x.setContent("Content tryout post");
        x.setAuthor(u);

        String id = testSave(target, x);
        System.out.println("Id : " + id);
        testGetExistingById(id, x);
        
        x.setTitle("Change Tryout Post");
        x.setContent("Changed content tryout post");
        
        testUpdateExisting(id, x);
        testGetExistingById(id, x);
        testDeleteExistingById(id);
    }

    private String testSave(String target, Post x) {
        String location = given()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .contentType("application/json")
                .body(x)
                .expect().statusCode(201).when().post(target)
                .getHeader("Location");

        assertNotNull(location);
        assertTrue(location.startsWith(target));

        String[] locationSplit = location.split("/");
        String id = locationSplit[locationSplit.length - 1];

        return id;
    }

    private void testGetExistingById(String id, Post x) {
        with().header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect()
                .statusCode(200)
                .body("title", equalTo(x.getTitle()), "content", equalTo(x.getContent()))
                .when().get(target + "/" + id);
    }

    private void testUpdateExisting(String id, Post x) {
        // get Post as JSON
        /*
        ObjectMapper mapper = new ObjectMapper();

        try {
            // display to console
            *.println(mapper.writeValueAsString(x));
        } catch (JsonGenerationException | JsonMappingException | IOException e) {
            //e.printStackTrace();
        }
        */
        
        given()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .contentType("application/json")
                .body(x)
                .expect()
                .statusCode(200).when().put(target + "/" + id);
    }

    private void testDeleteExistingById(String id) {
        given().auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect().statusCode(200).when().delete(target + "/" + id);

        given().auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect().statusCode(404).when().get(target + "/" + id);
    }
    
    @Test
    public void testGetExistingById() {
        with().header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect()
                .statusCode(200)
                .body("id", equalTo("test"),
                "title", equalTo("Test Title"),
                "content", equalTo("This is test content")).when()
                .get(target + "/" + "test");
    }
    
    @Test
    public void testGetNonExistentById() {
        with()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect().statusCode(404).when().get(target + "/" + "/nonexistentconfig");
    }

    @Test
    public void testFindAll() {
        String target4page = target + "4page";
        
//        *.println(with().auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
//            .contentType("application/json").get(target4page).getBody().asString());
        
        with()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .param("postType", "post")
                .header("Accept", "application/json").expect().statusCode(200)
                .body("content.id", hasItems("test")).when().get(target4page);
    }
}
