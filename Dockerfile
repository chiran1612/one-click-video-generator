# Multi-stage build for Riding Roney Video Generator
FROM maven:3.9.4-openjdk-17-slim AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jre-slim

# Install necessary packages
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Create app user for security
RUN groupadd -r ridingroney && useradd -r -g ridingroney ridingroney

# Set working directory
WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/target/riding-roney-generator-*.jar app.jar

# Create directories for generated videos
RUN mkdir -p /app/generated-videos && \
    chown -R ridingroney:ridingroney /app

# Switch to non-root user
USER ridingroney

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
