FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/apigateway.jar apigateway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "apigateway.jar"]