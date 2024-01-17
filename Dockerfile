#Dockerfile
# jdk17
FROM amazoncorretto:17
WORKDIR /app
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "build/libs/heygongc-0.0.1-SNAPSHOT.jar"]