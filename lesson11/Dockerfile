FROM gradle:8.9-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle jar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]