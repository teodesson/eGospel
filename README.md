# eGospel - Powered by Dallanube Project#

## Project Modules ##

Sub-projects : 

* config : contains jdbc.properties, dataSource, transactionManager, etc
* domain : contains '@Entity' and service interface
* service : contains '@Service' and '@Repository'
* web : contains '@Controller', HTML, JavaScript, etc

## Automated Test ##

Automated testing used : 

* service : normal JUnit with failsafe plugin to be executed on integration-test phase

* web : rest-assured
	* http://code.google.com/p/rest-assured/wiki/Usage 
	* http://www.hascode.com/2011/10/testing-restful-web-services-made-easy-using-the-rest-assured-framework/ 

## REST API Discovery & Documentation ##	

Swagger UI is a dependency-free collection of HTML, Javascript, and CSS assets that dynamically generate beautiful documentation from a Swagger-compliant API
http://swagger.wordnik.com/

## Build dan Run ##

Running the project : 

1. prepare MySQL database
    * db name  : e_gospel 
    * username : e_gospel_servant 
    * password : gen1:1
2. Execute mvn clean install in top level folder
3. Go to web folder, and run mvn jetty:run
4. Browse your http://localhost:9699

## Configuration ##

* Database connection : edit 'pom.xml' in top level folder, property: 'db.driver', 'db.url', 'db.username', 'db.password'
* Application's port : edit 'pom.xml' in top level folder, property: 'appserver.port.http'
* Context Path : edit 'pom.xml' in top level folder, property: 'appserver.deployment.context'

## For Techies, Geeks 'n Coders ##

### Framework dan Libraries ###

* Spring Framework 4.0.6
* Spring Security 3.2.5
* Spring Data JPA 1.6.4
* Hibernate 4.3.6
* Joda Time 2.3
* Logback 1.0.13
* AngularJS 1.2.20
* AngularUI 0.4.0
* Twitter Bootstrap 3.2.0
* jQuery 1.11.1
* Underscore JS 1.5.2

### Tools ###

* Build Tool : Maven 3
* Database Schema : Liquibase
* Unit Test Runner : Maven Surefire Plugin
* Integration Test Runner : Maven Failsafe Plugin
* Functional Test : Rest-Assured, swagger-ui
* Performance Monitoring : Javamelody
* Application Server : Jetty 6 (you can deploy to any app server like Tomcat)


## Dariawan or Dallanube? ##
Dariawan is short-form of my full name Desson Ariawan. It's translated into "From The Cloud" in English or "Dalla Nube" in Italian. That's the background of my project name

Cheers,
Desson Ariawan
