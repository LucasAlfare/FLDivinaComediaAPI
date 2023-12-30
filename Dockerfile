FROM openjdk:17-alpine
LABEL authors="Francisco Lucas"
RUN mkdir "/app"
COPY ./build/libs/FLDivinaComediaAPI-1.0-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]