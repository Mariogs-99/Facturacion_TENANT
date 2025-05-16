#FROM openjdk:8-jdk-alpine
#VOLUME /tmp
#ARG DEPENDENCY=target/dependency

#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app
#ENTRYPOINT ["java","-cp","app:app/lib/*","sv.mh.fe.Application"]

FROM openjdk:17-jdk-slim

WORKDIR /app

# Crear el directorio de uploads dentro del contenedor
RUN mkdir -p /app/uploads

COPY target/infologic-firmador-0.1.1.jar app.jar

EXPOSE 8113

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8113"]
