FROM openjdk:17-jdk-alpine
ARG CACHEBUST=1
WORKDIR /app
COPY build/libs/warehouse-1.0.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]