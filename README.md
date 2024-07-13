# Sitpass - Fitness App
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-black?logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-black?logo=springsecurity)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue?logo=postgresql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-red?logo=hibernate&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-blue?logo=docker&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-yellow?logo=mockito&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-brightgreen?logo=junit&logoColor=white)
![MockMVC](https://img.shields.io/badge/MockMVC-brightgreen?logo=mockmvc&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-red?logo=angular&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-blue?logo=tailwind-css&logoColor=white)
![Karma](https://img.shields.io/badge/Karma-yellow?logo=karma&logoColor=white)
![Jasmine](https://img.shields.io/badge/Jasmine-brightgreen?logo=jasmine&logoColor=white)
![Chart.js](https://img.shields.io/badge/Chart.js-brightgreen?logo=chartdotjs)

## Technologies Used
Main technologies which are used in this project are **Angular**, **Spring Boot** and **PostgreSQL**.

## Overview:  
Sitpass is an application that is very similar to fitpass. It provides recreation and sports services to different cities and countries...  You can read specification for better understanding of app:  

The **user** of the application has the following functionalities at his disposal:  
[K1] User registration request. Received registration requests are processed
system administrator (requirement A1). The system administrator is a predefined user in the
system.  
[K2] Login and logout from the system. When the user successfully logs in to the application
allow the user to log out. Without logging into the system, it is not possible to access the rest
applications.  
[K3] Handling1 objects. Mandatory object fields are city, address, object description,
disciplines and working hours. Only the system administrator can handle the object, while
facility manager can update facility attributes such as disciplines, working hours
and impressions. The object may or may not contain images. If it contains them, it must contain them
more of them. In addition to the name of the object, show the total, mean value of the object's rating.  
[K4] Reservation of departure (eng. excercise) to the facility. When making a reservation, the user
specifies the date of departure and the time period of the appointment. It is not possible to book an appointment outside
working hours of the facility.  
[K5] Leaving an impression (English review) on the object. When writing, an impression is formed
rate the object and optionally leave a comment. It is possible to evaluate the object based on the impression
according to: 1) equipment; 2) staff; 3) hygiene; 4) room; on a scale of 1-10, where
each of the listed items has its own rating. Users can only leave an impression
if they have at least one departure in the given facility at the time of writing the impression. On the
in the review, display information on how many departures the user has been on at the time of writing
reviews.  
[K6] Search and filter objects. It is possible to search all objects in the city,
discipline, grade range or working hours. It is possible to combine search and
filtering by multiple cities, disciplines, grade ranges and working hours.  
[K7] Sorting impressions. On the object's page, it is possible to sort the impressions by ratings and
date of publication ascending and descending.  
[K8] Overview of the home page. Users see objects from it on the home page
city ​​as well as a registered user. Users also see: 1) "most popular objects",
that is, facilities with the most positive ratings; 2) facilities to which the user has already visited; 3)
"try new" objects, ie 5 objects that the user of the system has not been on before
booked departure and whose discipline is not a discipline that the user has been to before.
Give the user the option to explore more new objects by clicking the "explore more" button.  
[K9] Change password. When changing the password, the current password is first entered twice
a new password is entered. After changing the password, an email is sent to the user.  
[K10] Changing additional data on the profile as well as the picture. You can see the list on the profile
of all impressions, objects on which the user is the manager and the history of visits to objects for
of the given user.  

The facility **manager** has available all the functionalities that he has and registered
user as well as:  
[M1] Update of disciplines, working hours and published pictures on the facility.  
[M2] Impression handling. The manager has insight into all the impressions that have been left on
object by the user and can hide or remove individual impressions. Removed
impressions are logically deleted and the grade from the impression is canceled and is not counted in the total grade
of the object. Marks left on the hidden impression are still counted in the total mark
of the object.  
[M3] Leaving a comment on an impression. It is possible to reply to an impression.
If the manager has responded to the impression, an ordinary user can respond to
manager's comment. An arbitrary number of replies to a comment is possible (reply to
reply to reply...).  
[M4] Analytics of objects. For each object the user is on
manager, it is possible to see the number of users and the number of object reviews by default
weekly, monthly and yearly. It is possible to see the periods of the day in which
were the most or least users of the facility (most or least traffic periods)
on a daily, weekly and monthly basis. In addition to the default levels, enable
overview of each of the analytics for a randomly defined time range (start and end
date). [For KVT subject]: Use a graphics library for visualization, like
Chart.js, Apache ECharts or similar.  

The system **administrator** has at his disposal all the functionalities that the manager has
facility and registered user, but also:  
[A1] continuation to K1. Processing of registration requests. The administrator has the right to
accept or reject the registration request. The user can only log in to the system
when the registration request is accepted. After the request is processed, the user is
sends an email.  
[A2] continuation to K3. Facility management and facility managers. Administrator
can assign the system user the object manager role. It is possible to add
remove managers from the facility. A manager that is removed from an object becomes plain
the user. If the object has no manager, the object becomes inactive and no
is displayed in the list of available objects. It is not possible to reserve for inactive objects
leaving or making an impression. The administrator has insight into all inactive objects and can
assign a manager, whereby the object goes into active status.  


## Getting Started:  
There are two different ways to start this project, you can pick to start with on local or docker.  
I keep them with different branches because there are some changes in configuration so I made it cleaner this way.  

### Start with Docker (branch: main):  
To start this project you need to have installed **Docker** and **Docker Compose**. 
In **docker-compose.yml** in **sitpass-backend** service you will need to change next line:  
**volumes: - /home/andrej/Projects/sitpass/sitpass-images:/sitpass-images**  
... Change this **/home/andrej/Projects** path to path where you have cloned/downloaded this repo ... It will be mapped  to **/sitpass-images** (You have sitpass-images folder in root of project with some random images i used to develop project, feel free to use these.)  
Navigate to root of your project and run **docker-compose up --build** and your app should start.  

**NOTE**: If you get problem with build, you may need to kill services on your local machine.  
In order to do that you need next set of commands:  

**Linux**:  
1. sudo lsof -i :PORT_NUMBER
2. sudo kill -9 PID
   
**Windows**:
1. netstat -ano | findstr :PORT_NUMBER
2. tasklist /FI "PID eq PID_NUMBER"
3. taskkill /PID PID_NUMBER /F

   
### Local Start (branch: local-start-v1):  
To start this project locally you need to have installed **JDK17**, **Postgresql**, **Node.js** and **Angular CLI**.
Create database with name sitpass_db and you need to change paths from AppConfig based on your preferences **/home/andrej/Projects** is path that need to be changed:  

1. public static final String IMAGE_PATH = "/home/andrej/Projects/sitpass/sitpass-images";
2. public static final String ABSOLUTE_PATH = "/home/andrej/Projects/sitpass/sitpass-backend/";  
   
Also you have init.sql file if you want to include some data.


## Spring Security:  
In this project I implemented **JWT(Json Web Token)** for Authentication.  
There are two different roles in this app **(USER, ADMIN)** and subrole **MANAGER**.  
There is comprehensive documentation which I have provided in overview to check which role have access to specific endpoints.

## Error Handling:  
**Global Exception Handling**: Spring Boot's global exception handling mechanism is employed to manage and respond to exceptions effectively throughout the application. This ensures consistency and reliability in handling various types of errors.  

**NotFoundException**: For model-related errors, such as when a requested resource is not found, the application utilizes a custom NotFoundException. This exception is thrown when attempting to access a resource that does not exist, providing clear feedback to the client.  

**BadRequestException**: In cases of technical errors or invalid requests, the application employs BadRequestException. This exception is used to indicate problems with the client's request, such as malformed input or missing parameters. By utilizing this exception, the application can provide meaningful error messages and guide clients towards resolving their requests.  

**UnauthorizedException**: When a client attempts to access a resource without proper authentication or authorization, the application throws UnauthorizedException. This exception signals that the request lacks the necessary credentials or permissions to perform the operation, prompting clients to authenticate or obtain appropriate authorization.

## Testing:  
Sitpass app has been thoroughly tested to ensure reliability and functionality across its features.  

**Unit Tests**  
Unit tests were implemented to test individual components, methods, and classes in isolation. These tests validate the correctness of business logic and ensure that each unit behaves as expected.   

1. JUnit: Used for writing and executing unit tests.
2. Mockito: Employed for mocking dependencies and verifying interactions between components.
3. Jasmine: Used for unit testing Angular components.
4. Karma: Test runner for executing JavaScript tests.


## Deploy:  
### Dockerfile  
The multistage Dockerfiles for this application optimizes the Docker image build process by leveraging separate stages for build and runtime environments. This approach enhances efficiency, security, and maintainability of the Docker image, adhering to best practices for Java application deployment in Docker containers.  

**BACKEND - BUILD STAGE**:  

**Base Image - maven:3.8.4-openjdk-17**: This stage starts with a Maven image that includes JDK 17, suitable for building Java applications.  
**Working Directory: /app**: Sets the working directory within the container where the application source code will be copied and built.  
**Copy Files**: Copies pom.xml and the entire src directory into the container also copies the sitpass-bucket directory into the container.     
**Build Application**: Executes mvn clean package -DskipTests to build the application. It cleans build enviroment, then package and skip tests during build process to speed up process.  

**BACKEND - RUNTIME STAGE**:  

**Base Image: openjdk:17-jdk-slim**: This stage starts with a minimal OpenJDK 17 image, optimized for runtime.  
**Volume:**: Defines a volume mount point at /tmp, allowing the application to write temporary files inside the container.    
**Copy Artifact**: Copies the built JAR file (app/target/*.jar) from the build stage into the current runtime stage, renaming it to app.jar.      
**Entry Point**: Defines the command to run when the container starts. java -jar /app.jar executes the JAR file as the main application entry point.  

**FRONTEND - BUILD STAGE**:  

**Base Image - node:22-alpine3.19**: This stage starts with a Node.js image based on Alpine Linux, suitable for building Angular applications due to its lightweight nature.  
**Working Directory - /app**: Sets the working directory within the container where the application source code will be copied and built.  
**Copy Files**: Copies the package.json and package-lock.json files into the container.  
**Install Angular CLI and Dependencies**:  
1. Installs the Angular CLI globally using npm install -g @angular/cli.  
2. Installs the project dependencies using npm install --force.  
**Copy Application Code**: Copies the entire application code into the container.  
**Build Application**: Executes ng build to build the Angular application. The build artifacts will be stored in the dist/sitpass-frontend/browser directory.

**FRONTEND - RUNTIME STAGE**:
**Base Image - nginx:alpine**: This stage starts with a minimal Nginx image based on Alpine Linux, optimized for serving static files.  
**Copy Build Artifacts**: Copies the built Angular application from the build stage (/app/dist/sitpass-frontend/browser) to the Nginx default HTML directory (/usr/share/nginx/html).  
**Copy Nginx Configuration**: Copies a custom Nginx configuration file (nginx.conf) into the Nginx configuration directory.  
**Expose Port**: Exposes port 80 to allow access to the application via HTTP.  
**Entry Point**: Defines the command to run when the container starts. nginx -g 'daemon off;' starts Nginx in the foreground.  

### docker-compose.yml  

This Docker Compose file defines a multi-container setup for Sitpass app, including PostgreSQL for the database, Angular for frontend and SpringBoot for backend part. Here's an explanation of each section:  

### Services:  

**1. sitpass-db**:    

**Image**: Uses the official PostgreSQL image.  
**Environment Variables**: Configures environment variables required for PostgreSQL:  
1. **POSTGRES_USER**: Specifies the PostgreSQL username as postgres.  
2. **POSTGRES_PASSWORD**: Specifies the password for the PostgreSQL user as root.  
3. **POSTGRES_DB**: Specifies the name of the PostgreSQL database as sitpass_db.  
**Ports Mapping**: Maps port 5432 of the host machine to port 5432 of the PostgreSQL container, allowing access to the PostgreSQL service from outside the container.  
**Volumes**: Mounts a volume (db_data) to persist PostgreSQL data and init.sql for data initialize.  

**2. sitpass-frontend**:  

**Build**: Building dockerfile from **sitpass-front**.  
**Ports Mapping**: Maps port 80 of the host machine to port 80 of container.  

**3. sitpass-backend**:  

**Build**: Builds the EMS application using the Dockerfile (Dockerfile) in the current context .  
**Ports**: Maps host port 8080 to container port 8080 for the app.  
**Volumes**: Maps local image file system to /sitpass-images which you can use for creating and uploading images.  
**Enviroment Variables**:  
1. **SPRING_DATASOURCE_URL**: JDBC URL to connect to PostgreSQL (jdbc:postgresql://psql-db:5432/sitpass_db).  
2. **SPRING_DATASOURCE_USERNAME**: PostgreSQL username (postgres).  
3. **SPRING_DATASOURCE_PASSWORD**: PostgreSQL password (root).  
4. **SPRING_JPA_HIBERNATE_DDL_AUTO**: Hibernate DDL auto strategy.  
**Depends On**: Specifies dependencies on sitpass-db.
**Command**: Executes Maven test (./mvnw test) command when starting the container.

**NOTE**: Ensure that **Docker** and **Docker Compose** are installed on your system and that there are no conflicts with ports already in use by other applications. Adjust configurations as needed based on your specific development environment and requirements also **IF YOU FACE ANY PROBLEM, PLEASE CONTACT ME :)**

## Author

**Andrej Stjepanović**  
Student at the **Faculty of Technical Sciences** in Novi Sad  
Undergraduate of Software Engineering
