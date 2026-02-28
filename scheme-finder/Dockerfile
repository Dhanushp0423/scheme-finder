
# ── Stage 1: Build with Maven ────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml first — caches dependency download layer
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build JAR (skip tests for faster build)
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── Stage 2: Minimal runtime image ──────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Health check — Render uses this to verify the service is up
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
    CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

# ── JVM flags tuned for Render free tier (512MB RAM) ─
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=prod", \
    "-Xmx400m", \
    "-Xms128m", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", "app.jar"]