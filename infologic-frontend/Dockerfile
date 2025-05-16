# Usa una imagen de Nginx como base
FROM nginx:alpine

# Copia el archivo de configuración de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copia los archivos construidos a la ubicación de Nginx
COPY dist/fact-electr-cnr /usr/share/nginx/html/facturacion

# Expone el puerto 80
EXPOSE 80

# Comando para ejecutar Nginx
CMD ["nginx", "-g", "daemon off;"]