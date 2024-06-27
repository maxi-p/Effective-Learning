FROM openjdk:17
EXPOSE 8761
ADD target/code-upload-service-registry.jar code-upload-service-registry.jar
ENTRYPOINT ["java", "-jar", "/code-upload-service-registry.jar"]