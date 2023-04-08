# Use OpenJDK 17 image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file to the working directory
COPY target/todo-service-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the application's port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]