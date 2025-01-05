FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/bookservice.jar bookservice.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "bookservice.jar"]
