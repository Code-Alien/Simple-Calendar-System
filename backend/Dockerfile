# ===== STAGE 1: Build =====
FROM openjdk:17-jdk-slim AS build

# Install basic tools
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the entire multi-module project (so parent pom.xml is available)
COPY . /app

# Build the backend module only (skip tests for speed)
RUN mvn clean package -DskipTests

# ===== STAGE 2: Runtime =====
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy only the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your app listens on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
