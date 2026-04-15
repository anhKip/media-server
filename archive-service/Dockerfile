FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY gradlew .
COPY gradle ./gradle

COPY settings.gradle ./
COPY build.gradle ./

COPY common/ ./common/
# COPY upload-service/ ./upload-service/
COPY archive-service/ ./archive-service/

RUN chmod +x ./gradlew && ./gradlew :archive-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
EXPOSE 8082

COPY --from=build /app/archive-service/build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]