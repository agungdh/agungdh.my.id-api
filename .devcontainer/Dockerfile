FROM openjdk:21-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /workspace

# Expose port untuk Spring Boot (default 8080)
EXPOSE 8080
