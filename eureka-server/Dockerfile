#
#FROM openjdk:17-jdk-alpine
#WORKDIR /app
#
#COPY build /app/build
#COPY build/libs/*.jar /app/app.jar
#
#EXPOSE 8761
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]
#FROM gradle:8.4-jdk17 AS build
#WORKDIR /app
#
# Копіюємо файли Gradle
#COPY gradle gradle
#COPY build.gradle settings.gradle ./
#COPY src ./src

# Будуємо проект
#RUN gradle build --no-daemon

# Stage 2: Run stage
#FROM openjdk:17-jdk-slim
#WORKDIR /app

# Копіюємо зібраний JAR файл з попереднього етапу
#COPY --from=build /app/build/libs/*.jar app.jar

#EXPOSE 8761
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]


FROM openjdk:17-jdk-alpine

RUN apk --no-cache add bash curl
WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
#COPY build/libs/*.jar /app/app.jar

COPY ${JAR_FILE} app.jar
EXPOSE 8761
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]
ENTRYPOINT ["java", "-jar", "/app/app.jar"]