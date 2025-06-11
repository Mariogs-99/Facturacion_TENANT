# Usa Maven para compilar y luego OpenJDK para ejecutar
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
RUN mkdir -p /app/uploads
COPY --from=build /app/target/infologic-firmador-0.1.1.jar app.jar
EXPOSE 8113
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8113"]
