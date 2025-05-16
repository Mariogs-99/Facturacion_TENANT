# Usa la imagen completa en lugar de slim
FROM openjdk:17-jdk

WORKDIR /app

# Crear el directorio de uploads dentro del contenedor
RUN mkdir -p /app/uploads

COPY target/factElectrCnrService-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8080"]
