/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.controller;

import com.jayway.restassured.authentication.FormAuthConfig;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
public class HomepageControllerTestIT extends BaseControllerTestIT {

    @Override
    public String getModule() {
        return "gospel/homepage";
    }

    @Test
    public void testGetAppinfo() {
        with().header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect()
                .statusCode(200)
                .body("profileDefault", equalTo("development"),
                    //"profileActive", equalTo(""),
                    "applicationName", equalTo("eGospel")
                )
                .when()
                .get(target + "/" + "appinfo");
    }

}
