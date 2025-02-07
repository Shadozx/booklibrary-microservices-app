FROM openjdk:17-jdk-alpine

RUN apk --no-cache add bash curl
WORKDIR /app

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]