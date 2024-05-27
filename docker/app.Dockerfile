FROM eclipse-temurin:17-jre-alpine
WORKDIR /home
COPY ../target/masterserver-1.0.0.jar keystore.p12 private.der public.pem ./
ENTRYPOINT ["java", "-jar", "masterserver-1.0.0.jar"]