1- Include CRUD (create/read/update/delete) methods
https://github.com/acuamarco/todo-backend

2- Include a database to store and remove items from the list
Run `docker compose up` to create the local database in PostgreSql (you need to have docker desktop installed)

3- How would the implementation differ if the database was swapped out from a SQL DB to a Non-SQL DB?
To use a Non-Sql DB, like MongoDB, it is necessary to:
- Add the dependency 'org.springframework.boot:spring-boot-starter-data-mongodb'
- Change the annotation in the model class from @Entity to @Document
- Change the data access layer, TaskRespository will need to extend from MongoRepository instead of JpaRepository

4-How would you add testing automation to the implementation?
Unit tests have been added in the solution. You can run them automatically using `gradlew build`
I would create a CI/CD pipeline and make sure to run this command.

5- Share the codebase and readme file on how to test it – (typically Github location or share via onedrive,etc)
It is possible to test the application manually using Postman.
An exported collection of requests has been included in `test/local test.postman_collection.json`.

6- If asked to add on simple authentication for internal users, what direction would you take? How would that direction differ from authentication for REST APIs with third parties? Provide examples
For internal users, we could use HTTP Basic Auth, with a username and password.
A new dependency will need to be added (org.springframework.boot:spring-boot-starter-security) and also add a class where we can configure in memory authentication.
An example has been added in com.example.demo.spring.SecurityConfiguration
To use the API you will need to send the request with Basic Auth, username: user and Password: secret

To use third parties, the process is similar, you will also need to add the specific dependency.
For example, to use Oauth2 Resource server, you need to:
- Add the dependency 'org.springframework.security:spring-security-oauth2-resource-server'
- Change the filterChain method and replace httpBasic(...) with oauth2ResourceServer(...)
- And, off course, set up your provider