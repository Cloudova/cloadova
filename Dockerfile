FROM openjdk:17-jdk AS build

WORKDIR /app

COPY . /app
RUN ./mvnw  -Dmaven.test.skip package

FROM openjdk:20-oracle

WORKDIR /app

COPY --from=build /app/target/cloudova.jar /app

EXPOSE 80

ENTRYPOINT 'java -jar /app/cloudova.jar --spring.profiles.active=prod"