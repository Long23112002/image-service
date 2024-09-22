FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/image-service-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 9099
ENTRYPOINT ["java", "-jar", "/app/app.jar"]