# Build stage
FROM gradle:8.5.0-jdk17 AS BUILD
WORKDIR /usr/app/
COPY . .
RUN gradle clean
RUN gradle build

# Package stage
LABEL authors="Francisco Lucas"
FROM openjdk:17-alpine
ENV JAR_NAME=FLDivinaComediaAPI-1.0-SNAPSHOT.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME