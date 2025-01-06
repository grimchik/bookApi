FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

COPY ../../Telegram%20Saved .

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/libraryBookService-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

CMD ["java", "-jar", "app.jar"]
