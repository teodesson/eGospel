/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.controller;

import java.net.InetAddress;
import org.junit.Before;

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
public class BaseControllerTestIT {
 
    protected static final String username = "desson";
    protected static final String password = "123";

    protected String target = "http://{serverName}:9699/{module}";
    protected String login = "http://{serverName}:9699/j_spring_security_check";

    protected String getModule() {
        return "";
    }
    
    @Before
    public void changeTarget() throws Exception {
        String computerName = InetAddress.getLocalHost().getHostName();
        if (computerName.equals("")) {
            computerName = "localhost";
        }
        target = target.replace("{serverName}", computerName).replace("{module}", getModule());        
        login = login.replace("{serverName}", computerName);
    }
}
