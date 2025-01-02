# Set up the base image
FROM docker.io/library/eclipse-temurin:17-jdk-alpine@sha256:4708e1a2c3baa0855eb9b3e6ae6285c8640d574c25ba74fddf6b8a17ccc3673f

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . ./

# Grant execute permissions to the mvnw script
RUN chmod +x mvnw

# Build the app
RUN ./mvnw -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install
