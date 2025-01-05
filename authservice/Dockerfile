FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/authservice.jar authservice.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "authservice.jar"]
