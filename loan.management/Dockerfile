FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/loan-management-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.h2.console.settings.web-allow-others=true", "-jar", "app.jar"]
