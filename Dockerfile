# Use the official OpenJDK image from the Docker Hub
FROM openjdk:17-jdk-slim

# ARG is used to pass in the JAR file during the build process
ARG JAR_FILE=target/*.jar

# Copy the JAR file to the container
COPY ${JAR_FILE} app.jar

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
