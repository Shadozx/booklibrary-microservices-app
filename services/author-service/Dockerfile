# Етап 1: Побудова проекту
#FROM gradle:8.4-jdk17 AS build

# Встановлюємо робочу директорію
#WORKDIR /app

# Копіюємо Gradle файли
#COPY build.gradle settings.gradle ./
#COPY gradle gradle
#
# Завантажуємо залежності Gradle
#RUN gradle dependencies
#
# Копіюємо код проекту
#COPY . .
#
# Будуємо проект
#RUN gradle build --no-daemon
#
# Етап 2: Створення Docker образу
#FROM openjdk:17-jdk-alpine
#
# Встановлюємо робочу директорію
#WORKDIR /app
#
# Копіюємо JAR файл з попереднього етапу
#COPY --from=build /app/build/libs/*.jar app.jar
#
# Виставляємо порт, який буде використовувати додаток
#EXPOSE 9085
#
# Запускаємо додаток
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]

#FROM openjdk:17-jdk-alpine
#
#WORKDIR /app
#
#COPY /src ./
#COPY . ./
#
#RUN ./gradlew build
#
#ARG JAR_FILE=build/libs/*.jar
#
#COPY ${JAR_FILE} app.jar
#
#EXPOSE 9085
#
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]

#FROM gradle:8.8-jdk17 as build
#
#WORKDIR /app
#
#
#COPY build.gradle settings.gradle gradlew /app/
#COPY gradle /app/gradle
#COPY services/api /app/services/api
#COPY services/exception /app/services/exception-handler
#COPY services/author-service /app/services/author-service
#COPY src /app/src
#
#RUN ./gradlew build

FROM openjdk:17-jdk-alpine

RUN apk --no-cache add bash curl
WORKDIR /app

COPY build/libs/*.jar /app/app.jar

EXPOSE 9085
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
