# ============================================
# DOCKERFILE FOR SPRING BOOT BACKEND
# ============================================

# Use Java 23
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy build files
COPY gradlew .
COPY gradlew.bat .
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle/ gradle/

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src/ src/

# Build the application
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy wait-for-it script
COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh

# Create uploads directory
RUN mkdir -p /app/uploads

# Copy built JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Create non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring
RUN chown -R spring:spring /app
USER spring

# Expose port
EXPOSE 9090

# Run with memory limits
ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]