FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY gradlew .
COPY gradle ./gradle

COPY settings.gradle ./
COPY build.gradle ./

COPY common/ ./common/
COPY upload-service/ ./upload-service/

RUN chmod +x ./gradlew && ./gradlew :upload-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
EXPOSE 8081

COPY --from=build /app/upload-service/build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]