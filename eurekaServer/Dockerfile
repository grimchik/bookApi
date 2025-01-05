FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/eurekaserver.jar eurekaserver.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "eurekaserver.jar"]
