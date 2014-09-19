/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@yahoo.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.config;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.configuration.SwaggerModelsConfiguration;
import com.mangofactory.swagger.paths.AbsoluteSwaggerPathProvider;
import com.mangofactory.swagger.paths.SwaggerPathProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author Desson Ariawan <teodesson@yahoo.com>
 */
@Configuration
@ComponentScan(basePackages = {"com.dariawan.gospel.controller"})
@Import(SwaggerModelsConfiguration.class)
public class MySpringSwaggerConfig extends SpringSwaggerConfig {

    @Bean
    @Override
    public SwaggerPathProvider defaultSwaggerPathProvider() {
        return new AbsoluteSwaggerPathProvider();
    }
}