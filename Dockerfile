# Stage 1: Run the JAR with a lightweight JDK image
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the JAR built in the first stage
COPY target/s3rha-0.0.1-SNAPSHOT.jar app.jar

# Expose the port Spring Boot will listen on
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
