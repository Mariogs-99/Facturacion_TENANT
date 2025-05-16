# Usa una imagen oficial de OpenJDK como base
FROM openjdk:17-jdk-slim

# Instalar libfreetype6 y fontconfig (para soporte de fuentes)
RUN apt-get update && apt-get install -y libfreetype6 fontconfig && rm -rf /var/lib/apt/lists/*

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR generado a la imagen
COPY target/factElectrCnrPuntoDeVentaBackend-0.0.1-SNAPSHOT.jar app.jar

# Copia application.properties dentro del contenedor en la ruta correcta
COPY app/admin/sisucc3/config/application.properties /app/admin/sisucc3/config/

# Expone el puerto en el que corre la aplicación
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
