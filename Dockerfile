FROM ubuntu:latest
LABEL authors="yosri-harrabi"

# BUILD STAGE
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy gradle wrapper and configs first (for caching)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

# Pre-download dependencies (cache friendly)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src src

RUN ./gradlew clean bootJar --no-daemon

# RUNTIME STAGE
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]