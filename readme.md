# Headless Authentication Service

## Setup Application 

Rename `src/main/resources/application.example.yml` to `src/main/resources/application.yml`. and setup database and smtp server.

```yaml
spring:
  mail:
    host: "smtp.example.com"
    port: 587
    username: "example"
    password: "secret"
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
  datasource:
    url: "jdbc:mysql://localhost:3306/cloudova"
    username: "root"
    password: "secret"
```

## Run

Start services with docker compose

```
docker compose up --build -d
```
