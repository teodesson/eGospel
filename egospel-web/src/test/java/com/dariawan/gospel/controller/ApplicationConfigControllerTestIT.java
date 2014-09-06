/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@gmail.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.controller;

import com.dariawan.gospel.domain.ApplicationConfig;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import com.jayway.restassured.authentication.FormAuthConfig;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

public class ApplicationConfigControllerTestIT {
    private static final String username = "desson";
    private static final String password = "123";

    private String target = "http://{serverName}:9699/config";
    private String login = "http://{serverName}:9699/j_spring_security_check";

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
    public void testSaveInvalid() {
        ApplicationConfig u = new ApplicationConfig();

        given()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .contentType("application/json")
                .body(u)
                .expect().statusCode(400).when().post(target);
    }

    @Test
    public void testSaveUpdateDelete() {

        String id = testSave(target);
        System.out.println("Id : " + id);
        testGetExistingById(id, "tryout", "Tryout Configuration", "test");
        testUpdateExisting(id, "tryout", "Tryout Configuration 001", "test123");
        testGetExistingById(id, "tryout", "Tryout Configuration 001", "test123");
        testDeleteExistingById(id);
    }

    private String testSave(String target) {
        ApplicationConfig config = new ApplicationConfig();
        config.setName("tryout");
        config.setLabel("Tryout Configuration");
        config.setValue("test");

        String location = given()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .contentType("application/json")
                .body(config)
                .expect().statusCode(201).when().post(target)
                .getHeader("Location");

        assertNotNull(location);
        assertTrue(location.startsWith(target));

        String[] locationSplit = location.split("/");
        String id = locationSplit[locationSplit.length - 1];

        return id;
    }

    private void testGetExistingById(String id, String name, String label,
            String value) {
        with().header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect()
                .statusCode(200)
                .body("name", equalTo(name), "label", equalTo(label), "value",
                equalTo(value)).when().get(target + "/" + id);
    }

    private void testUpdateExisting(String id, String name, String label,
            String value) {
        ApplicationConfig config = new ApplicationConfig();
        config.setName(name);
        config.setLabel(label);
        config.setValue(value);

        given()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .contentType("application/json")
                .body(config)
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
    public void testGetExistingConfigById() {
        with().header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect()
                .statusCode(200)
                .body("id", equalTo("abc123"),
                "name", equalTo("applicationName"),
                "label", equalTo("Application Name"),
                "value", equalTo("eGospel")).when()
                .get(target + "/" + "abc123");
    }

    @Test
    public void testGetNonExistentConfigById() {
        with()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect().statusCode(404).when().get(target + "/" + "/nonexistentconfig");
    }

    @Test
    public void testFindAll() {
        with()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .header("Accept", "application/json").expect().statusCode(200)
                .body("id", hasItems("abc123", "def456")).when().get(target);
    }

    @Test
    public void testSearch() {
        with()
                .header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .param("search", "name")
                .expect().statusCode(200)
                .body("id", hasItems("abc123")).when().get(target);

        with()
                .header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .param("search", "xx")
                .expect().statusCode(200)
                .when().get(target);
    }

    @Test
    public void testUploadFile() {
        given()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .multiPart("photo", new File("src/test/resources/snow-monkey.jpg"))
                .multiPart("resume", "John-Doe-Resume.pdf", ApplicationConfig.class.getResourceAsStream("/John-Doe-Resume.pdf"))
                .formParam("description", "John's files")
                .expect()
                .body(
                "description", is("success"),
                "resume", is("success"),
                "photo", is("success"))
                .when()
                .post(target + "/" + "/abc123/files");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUploadUsingRestTemplate() {
        RestTemplate rest = new RestTemplate();

        String jsessionid = rest.execute(login, HttpMethod.POST,
                new RequestCallback() {
                    @Override
                    public void doWithRequest(ClientHttpRequest request) throws IOException {
                        request.getBody().write(("j_username="+username+"&j_password="+password).getBytes());
                    }
                }, new ResponseExtractor<String>() {
            @Override
            public String extractData(ClientHttpResponse response) throws IOException {
                List<String> cookies = response.getHeaders().get("Cookie");

                // assuming only one cookie with jsessionid as the only value
                if (cookies == null) {
                    cookies = response.getHeaders().get("Set-Cookie");
                }

                String cookie = cookies.get(cookies.size() - 1);

                int start = cookie.indexOf('=');
                int end = cookie.indexOf(';');

                return cookie.substring(start + 1, end);
            }
        });

        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("photo", new FileSystemResource("src/test/resources/snow-monkey.jpg"));
        form.add("Filename", "John-Doe-Resume");
        form.add("resume", new FileSystemResource("src/test/resources/John-Doe-Resume.pdf"));
        form.add("description", "John's files");
        Map<String, String> result = rest.postForObject(target + "/abc123/files;jsessionid=" + jsessionid, form, Map.class);

        assertEquals("success", result.get("resume"));
        assertEquals("success", result.get("photo"));
        assertEquals("success", result.get("description"));
    }
}
