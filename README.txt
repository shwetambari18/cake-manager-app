Cake Manager Micro Service (fictitious)
=======================================

A summer intern started on this project but never managed to get it finished.
The developer assured us that some of the above is complete, but at the moment accessing the /cakes endpoint
returns a 404, so getting this working should be the first priority.

Requirements:
* By accessing /cakes, it should be possible to list the cakes currently in the system. JSON would be an acceptable response format.

* It must be possible to add a new cake.

* It must be possible to update an existing cake.

* It must be possible to delete an existing cake.

Comments:
* We feel like the software stack used by the original developer is quite outdated, it would be good to migrate the entire application to something more modern. If you wish to update the repo in this manner, feel free! An explanation of the benefits of doing so (and any downsides) can be discussed at interview.

* Any other changes to improve the repo are appreciated (implementation of design patterns, seperation of concerns, ensuring the API follows REST principles etc)

Bonus points:
* Add some suitable tests (unit/integration...)
* Add some Authentication / Authorisation to the API
* Continuous Integration via any cloud CI system
* Containerisation

Scope
* Only the API and related code is in scope. No GUI of any kind is required


Original Project Info
=====================

To run a server locally execute the following command:

`mvn jetty:run`

and access the following URL:

`http://localhost:8282/`

Feel free to change how the project is run, but clear instructions must be given in README
You can use any IDE you like, so long as the project can build and run with Maven or Gradle.

The project loads some pre-defined data in to an in-memory database, which is acceptable for this exercise.  There is
no need to create persistent storage.


Submission
==========

Please provide your version of this project as a git repository (e.g. Github, BitBucket, etc).

A fork of this repo, or a Pull Request would be suitable.

Good luck!


Test submission:
Re-designed the project using Spring boot with maven, in memory database H2-console and used docker container for deployment.

Running instructions:
1.Docker(Prerequisite - Docker desktop should be installed on the machine)
    a.mvn clean package
    b.Build the Docker Image- Run below command through terminal
      docker build -t cake-manager .
    c. Run Docker Container through terminal:
       docker run -p 8080:8080 cake-manager
    d. Access the app at http://localhost:8080

2. Intellij Idea:
   a. Run the Springboot application by specifying CakeManagerApplication class file through Edit Configurations.
   b. Access the app at http://localhost:8080

3. Access apis though curl commands from terminal with Git Bash:
Apis are protected with Basic auth with username and password added in application.properties.
    user=user123, admin=admin123
   a. Get all cakes
      curl -u user:user123 http://localhost:8080/cakes
   b.Create Cake (POST)
       curl -X POST http://localhost:8080/cakes \
       -u admin:admin123 \
       -H "Content-Type: application/json" \
       -d '{"title": "Choco", "desc": "Dark", "image": "img.jpg"}'
   c.Delete Cake
      curl -X DELETE http://localhost:8080/cakes/1 -u admin:admin123
   d.Update Cake
      curl -X PUT http://localhost:8080/cakes/1 \
      -u admin:admin123 \
      -H "Content-Type: application/json" \
      -d '{"title": "Updated", "desc": "New desc", "image": "new.jpg"}'
    e. Find a cake
        curl -u admin:admin123 http://localhost:8080/cakes/1
4. Through postman Basic Auth can access all the apis.
5. Access the in memory database H2-console-http://localhost:8080/h2-console
   Select * from cakes;
6. Swagger ui- http://localhost:8080/swagger-ui/index.html

Future scope:
a. To access H2-console in docker mode need to add server config with webAllowOthers and tcpAllowOthers.
b. Add .dockerignore file for project to optimize Docker builds by excluding unnecessary files.



