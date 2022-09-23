## CS 122B Project 1 Star example

This example shows a basic Java Servlet that shows a star list page.

### Before running the example
#### If you do not have USER `mytestuser` setup in MySQL, follow the below steps to create it:

 - login to mysql as a root user 
    ```
    local> mysql -u root -p
    ```

 - create a test user and grant privileges:
    ```
    mysql> CREATE USER 'mytestuser'@'localhost' IDENTIFIED BY 'My6$Password';
    mysql> GRANT ALL PRIVILEGES ON * . * TO 'mytestuser'@'localhost';
    mysql> quit;
    ```

#### prepare the database `moviedbexample`
 
```    
    local> mysql -u mytestuser -p
    mysql> CREATE DATABASE IF NOT EXISTS moviedbexample;
    mysql> USE moviedbexample;
    mysql> CREATE TABLE IF NOT EXISTS stars(
                   id varchar(10) primary key,
                   name varchar(100) not null,
                   birthYear integer
               );
    
    mysql> INSERT IGNORE INTO stars VALUES('755011', 'Arnold Schwarzeneggar', 1947);
    mysql> INSERT IGNORE INTO stars VALUES('755017', 'Eddie Murphy', 1961);
```    
    
### To run this example: 
1. Clone this repository using `git clone https://github.com/uci-jherold2-teaching/cs122b-fall22-project1-star-example.git`
2. Open IntelliJ -> Import Project -> Choose the project you just cloned (The root path must contain the pom.xml!) -> Choose Import project from external model -> choose Maven -> Click on Finish -> The IntelliJ will load automatically
3. For "Root Directory", right click "cs122b-fall22-project1-star-example" -> Mark Directory as -> sources root
4. In `src/StarServlet.java`, make sure the mysql username is `mytestuser` and password is `mypassword`.
5. Also make sure you have the `moviedbexample` database.
6. To run the example, follow the instructions in [canvas](https://canvas.eee.uci.edu/courses/40150/pages/intellij-idea-tomcat-configuration)


### To Export the WAR file
Export using maven in command line: Go to your project folder first, then do `mvn clean`, then do `mvn package`. Your WAR file should be in `target/cs122b-fall22-project1-star-example.war`

### Brief Explanation
- `index.html` is a simple welcome page. Open the url `localhost:8080/cs122b-fall22-project1-star-example`, if the welcome text shows up, then means that you have successfully deployed the project.

- `StarServlet.java` is a Java servlet that talks to the database and get the stars data. It generates the HTML strings and write it to response. Open the url `localhost:8080/cs122b-fall22-project1-star-example/stars` to see the page generated by this Servlet.

- Note: If using IntelliJ, the url has to be set correctly: In `Edit configuration` -> `Tomcat` -> `Deployment` tab -> `Application context`, change the url to be from `cs122b_fall22_project1_star_example_war` to `cs122b-fall22-project1-star-example`
