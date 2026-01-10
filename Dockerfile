# ---- Build ----
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ---- Run ----
FROM tomcat:10.1-jdk17

# Удаляем дефолтные приложения
RUN rm -rf /usr/local/tomcat/webapps/*

# Копируем WAR как ROOT
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/mathhub.war

EXPOSE 8080
