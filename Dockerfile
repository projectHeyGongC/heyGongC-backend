#Dockerfile
# jdk17
FROM amazoncorretto:17
COPY build/libs/heygongc-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]