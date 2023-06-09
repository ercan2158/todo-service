# Todo Service

This is a backend service that provides basic management of a simple to-do list. Users can add to-do items, update descriptions, mark items as done or not done, and retrieve item details or lists of items based on their status.

## Assumptions

- No authentication is implemented.
- The service uses an H2 in-memory database.
- Items that are past their due date are automatically marked as "past due" and cannot be modified through the REST API.

## Tech Stack

- Java 17
- Spring Boot 2.7
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- JUnit 5
- OpenAPI 3
- Maven

## Build the service

To build the service, navigate to the project root directory and run the following command:

```mvn clean install```


This command will compile the code, run tests, and create an executable JAR file in the `target` directory.

## Run automatic tests

To run the automatic tests, navigate to the project root directory and execute the following command:

```mvn test```


This command will run all the test cases included in the project.

## Run the service locally

To run the service locally, navigate to the project root directory and use the following command:

```mvn spring-boot:run```


This command will start the service on the default port 8080. You can now interact with the REST API using tools like curl or Postman.

## Docker
To run the service in a Docker container, first, build the project and generate the JAR file using the instructions above. Then, create a Docker image using the provided Dockerfile:

```docker build -t todo-service .```

Run the Docker container using the created image:

```docker run -d -p 8080:8080 --name todo-service-container todo-service```

The service will be accessible at http://localhost:8080.

For example, to add a new item, you can use the following curl command:

``curl -X POST -H "Content-Type: application/json" -d '{"description": "Buy groceries", "dueDateTime": "2023-04-10T12:00:00"}' http://localhost:8080/todos``



To get all not-done items:

```curl -X GET http://localhost:8080/todos```


For more information about the API, please refer to the API documentation generated by OpenAPI 3.
