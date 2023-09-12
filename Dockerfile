FROM openjdk:17-jdk AS build

WORKDIR /app

COPY . /app
RUN ./gradlew buildJar

FROM openjdk:20-oracle

WORKDIR /app

COPY --from=build /app/target/authentication-service.jar /app

EXPOSE 80

ENTRYPOINT 'java -jar /app/authentication-service.jar --spring.profiles.active=prod"