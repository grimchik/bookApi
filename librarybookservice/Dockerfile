FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/librarybookservice.jar librarybookservice.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "librarybookservice.jar"]
