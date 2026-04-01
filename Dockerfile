# ─── Estágio 1: Build ───────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Define diretório de trabalho
WORKDIR /app

# Copia o pom.xml e baixa dependências (cache de layers)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests

# ─── Estágio 2: Runtime ─────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

# Define diretório de trabalho
WORKDIR /app

# Copia o JAR gerado no estágio de build
COPY --from=build /app/target/*.jar app.jar

# Porta exposta
EXPOSE 8080

# Inicia a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]