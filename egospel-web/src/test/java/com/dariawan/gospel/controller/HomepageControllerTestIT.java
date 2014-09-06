/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@gmail.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.controller;

import com.jayway.restassured.authentication.FormAuthConfig;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.with;
import java.net.InetAddress;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;

public class HomepageControllerTestIT {

    private String target = "http://{serverName}:9699/homepage";
    private String login = "http://{serverName}:9699/j_spring_security_check";
    private final String username = "desson";
    private final String password = "123";

    @Before
    public void changeTarget() throws Exception {
        String computerName = InetAddress.getLocalHost().getHostName();
        if (computerName.equals("")) {
            computerName = "localhost";
        }
        target = target.replace("{serverName}", computerName);
        login = login.replace("{serverName}", computerName);
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
