FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml /app/pom.xml
COPY load-runner/pom.xml /app/load-runner/pom.xml
COPY backend/pom.xml /app/backend/pom.xml
COPY load-runner/src /app/load-runner/src

RUN mvn -pl load-runner -am -DskipTests package

# ---- run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/load-runner/target/*.jar /app/load-runner.jar

ENV BASE_URL="http://backend:8080"
ENV EPS="200"
ENV ERROR_PERC="2"

ENTRYPOINT [ "java", "-jar", "/app/load-runner.jar" ]