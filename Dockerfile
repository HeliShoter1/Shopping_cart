# ========================
# Stage 1: Build
# ========================
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# ========================
# Stage 2: Runtime
# ========================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /app/target/*.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

# ✅ Thêm 1: JVM tuning cho container
# -XX:+UseContainerSupport    → JVM tự đọc memory limit của Docker
# -XX:MaxRAMPercentage=75     → dùng tối đa 75% RAM container
# -XX:+ExitOnOutOfMemoryError → tự tắt khi OOM thay vì treo
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-jar", "app.jar"]

# ✅ Thêm 2: Health check — Docker biết app đã sẵn sàng chưa
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1