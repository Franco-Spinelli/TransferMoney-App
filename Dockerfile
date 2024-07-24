FROM openjdk:17-jdk-slim


WORKDIR /app


COPY target/transferMoney-0.0.1-SNAPSHOT.jar transferMoney.jar


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "transferMoney.jar"]