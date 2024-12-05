
FROM openjdk:17-jdk-slim

LABEL authors="erdlei_v"

WORKDIR /app

COPY target/Furniture_Store-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
