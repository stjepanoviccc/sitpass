# build
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY sitpass-bucket ./sitpass-bucket

RUN mvn clean package -DskipTests

# run
FROM openjdk:17-jdk-slim AS runtime
VOLUME /tmp
ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} app.jar
COPY --from=build /app/sitpass-bucket /sitpass-bucket
ENTRYPOINT ["java","-jar","/app.jar"]