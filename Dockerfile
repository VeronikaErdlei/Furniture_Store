
FROM openjdk:17-jdk-slim

LABEL authors="erdlei_v"

WORKDIR /app

COPY target/Furniture_Store-1.0-SNAPSHOT.jar app.jar

COPY wait-for-it.sh /wait-for-it.sh

RUN chmod +x /wait-for-it.sh


EXPOSE 8080

ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "/app/app.jar"]



