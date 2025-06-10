# Usa Maven para compilar y luego OpenJDK para ejecutar
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
# Instalar libfreetype6 y fontconfig (para soporte de fuentes)
RUN apt-get update && apt-get install -y libfreetype6 fontconfig && rm -rf /var/lib/apt/lists/*
COPY --from=build /app/target/factElectrCnrPuntoDeVentaBackend-0.0.1-SNAPSHOT.jar app.jar
COPY app/admin/sisucc3/config/application.properties /app/admin/sisucc3/config/
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
