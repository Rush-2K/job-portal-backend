# Step 1: Use an official JDK image to build/run the app
FROM openjdk:17-jdk-slim

# Step 2: Set working directory inside the container
WORKDIR /app

# Step 3: Copy the JAR file into the container
COPY target/jobportal-api-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Tell Docker how to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]

# Step 5: Expose the port Spring Boot runs on
EXPOSE 8080
