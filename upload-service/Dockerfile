# Stage 1: Build the upload-service
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy gradle wrapper and related files
COPY gradlew .
COPY gradle ./gradle

# Copy root Gradle files and settings
COPY settings.gradle build.gradle ./

# Copy upload-service specific gradle configuration and source code
COPY upload-service/build.gradle ./upload-service/
COPY upload-service/src ./upload-service/src/

# Ensure wrapper script is executable, then build the executable JAR
RUN chmod +x ./gradlew && ./gradlew :upload-service:bootJar --no-daemon

# Stage 2: Create the minimal runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Expose the application port
EXPOSE 8081

# Copy the built executable jar from the build stage
COPY --from=build /app/upload-service/build/libs/upload-service-0.0.1-SNAPSHOT.jar /app/app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
