FROM openjdk:17-jdk-alpine

RUN apk --no-cache add bash curl
WORKDIR /app

COPY build/libs/*.jar /app/app.jar

EXPOSE 9100
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
