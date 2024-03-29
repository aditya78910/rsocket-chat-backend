
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/chat-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]