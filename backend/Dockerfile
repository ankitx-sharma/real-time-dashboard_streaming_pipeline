# ---- build stage ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# copy parent + backend module only
COPY pom.xml /app/pom.xml
COPY backend/pom.xml /app/backend/pom.xml
COPY load-runner/pom.xml /app/load-runner/pom.xml
COPY backend/src /app/backend/src

RUN mvn -pl backend -am -DskipTests package

# ---- run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/backend/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]