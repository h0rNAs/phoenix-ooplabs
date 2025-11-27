# ---- Stage 1: Build ----
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Копируем файлы проекта
COPY pom.xml .
COPY src ./src

# Собираем приложение (skip тесты для ускорения)
RUN mvn clean package -DskipTests

# ---- Stage 2: Run ----
FROM tomcat:9.0-jre17

COPY out/artifacts/mathhub_archive/mathhub.war /usr/local/tomcat/webapps/

EXPOSE 8080
