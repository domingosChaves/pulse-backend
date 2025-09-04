# Multi-stage build: build JAR com Maven e depois roda em JRE leve
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build do fat JAR
RUN mvn -q -DskipTests clean package

FROM eclipse-temurin:17-jre
WORKDIR /app
# Copia o JAR gerado
COPY --from=build /app/target/pulse-backend-0.0.1-SNAPSHOT.jar app.jar
# Porta da aplicação (configurada como 8081)
EXPOSE 8081
# Perfil docker usa Postgres
ENV SPRING_PROFILES_ACTIVE=docker
# Ajuste de memória do Java (opcional)
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]

