FROM eclipse-temurin:22-jre-alpine
WORKDIR /home
COPY target/masterserver-1.0.0.jar private.pem app.sh ./
ENTRYPOINT ["sh", "app.sh"]