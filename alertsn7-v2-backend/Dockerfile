# ---- Stage 1: Build ----
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia primero el pom para aprovechar cache de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el código fuente y compila
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---- Stage 2: Runtime ----
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crea directorio dedicado para la base de datos
RUN mkdir -p /app/data

# Copia el jar generado en el stage anterior
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8077

ENTRYPOINT ["java", "-jar", "app.jar"]