/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.controller;

import com.dariawan.gospel.domain.User;
import com.dariawan.gospel.domain.Role;
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
public class UserControllerTestIT extends BaseControllerTestIT {

    @Override
    public String getModule() {
        return "gospel/user";
    }

    @Test
    public void testSaveInvalid() {
        User u = new User();

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

        User x = new User();
        x.setFirstName("Tryout");
        x.setFirstName("User");
        x.setUsername("test");
        x.setPassword("test123");
        x.setEnabled(true);
        x.setRole(r);

        String id = testSave(target, x);
        testGetExistingById(id, x);

        x.setLastName("Changed");
        x.setUsername("testchanged");

        testUpdateExisting(id, x);
        testGetExistingById(id, x);
        testDeleteExistingById(id);
    }

    private String testSave(String target, User x) {
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

    private void testGetExistingById(String id, User x) {
        with().header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect()
                .statusCode(200)
                .body("username", equalTo(x.getUsername()), "firstName", equalTo(x.getFirstName()))
                .when().get(target + "/" + id);
    }

    private void testUpdateExisting(String id, User x) {

        given()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .contentType("application/json")
                .body(x).expect()
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
                .body("id", equalTo("robot"),
                        "username", equalTo("robot"),
                        "firstName", equalTo("Cyborg")).when()
                .get(target + "/" + "robot");
    }

    @Test
    public void testGetNonExistentById() {
        with()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect().statusCode(404).when().get(target + "/" + "/nonexistentconfig");
    }

    @Test
    public void testFindAll() {
        with()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .header("Accept", "application/json").expect().statusCode(200)
                .body("id", hasItems("robot", "desson")).when().get(target);
    }

}
