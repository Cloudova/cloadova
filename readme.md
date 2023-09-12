# Headless Authentication Service

- **Multi-Application Support** headless authentication service allows users to define and manage multiple applications within a single ecosystem. Whether you're developing web applications, mobile apps, or APIs, this service lets you effortlessly authenticate users across various platforms.

- **Granular User Permissions** Gain fine-grained control over user access by assigning specific permissions and roles to individual users for each application. This ensures that users only have access to the resources and features they need, enhancing security and usability.

- **Seamless User Management** Easily onboard, manage, and monitor users across your applications through a centralized dashboard. Simplify user registration, password reset, and account recovery processes, streamlining the user experience.

- **Integration via gRPC** Seamlessly connect to various services using the efficient gRPC protocol. This enables your applications to communicate securely with other microservices and backend systems within your ecosystem.

## Setup Application 

Rename `src/main/resources/application.example.yml` to `src/main/resources/application.yml`. and setup database and smtp server.

```yaml
app: "cloud"
debug: true
grpc:
  port: 9595
  service-access-token: "token"
jwt:
  secret: "secret"
restrictions:
  max_app_for_user: 3
  max_user_for_app: 100
  storage_limit: "150Mb"
mail-service:
  default-email: "example@example.com"
server:
  error:
    whitelabel:
      enabled: false
    include-message: always
    include-binding-errors: always
    include-stacktrace: NEVER
    include-exception: false
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
    url: "jdbc:mysql://localhost:3306/authentication"
    username: "root"
    password: "secret"
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
springdoc:
  swagger-ui:
    path: "/api/docs"
```

## Run

Start services with docker compose

```
docker compose up --build -d
```
