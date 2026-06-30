# ============================================================
# Dockerfile - api-gateway
# ============================================================
# Patron identico: multi-stage build para imagen liviana.
# El Gateway usa WebFlux (reactivo) pero el proceso de build
# es el mismo que un proyecto MVC estandar.
# ============================================================

# -------------------------------------------------------
# STAGE 1: BUILD
# -------------------------------------------------------
FROM eclipse-temurin:21-jdk-alpine AS build

# NUEVO: Alpine no incluye Maven, se instala con su gestor de paquetes apk
RUN apk add --no-cache maven

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B -q

COPY src ./src
RUN mvn package -DskipTests -B -q

# -------------------------------------------------------
# STAGE 2: RUNTIME
# -------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Puerto del API Gateway (punto de entrada unico del sistema)
EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]