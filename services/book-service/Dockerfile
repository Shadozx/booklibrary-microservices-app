FROM openjdk:17-jdk-alpine
WORKDIR /app

COPY build/libs/*.jar /app/app.jar

EXPOSE 9055
ENTRYPOINT ["java", "-jar", "/app/app.jar"]