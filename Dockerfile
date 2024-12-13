FROM openjdk:23
WORKDIR /app

COPY target/cloudwirm-1.0.0.jar cloudwirm.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "cloudwirm.jar"]