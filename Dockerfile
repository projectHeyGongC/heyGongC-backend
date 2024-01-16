#Dockerfile
# 사용하고자 하는 도커 이미지를 적습니다. jdk17을 사용하기 위해 amazoncorretto:17을 사용합니다.
FROM amazoncorretto:17
WORKDIR /app
COPY . /app
RUN chmod +x ./gradlew
# gradlew의 권한 문제를 해결하기 위해 chmod를 진행합니다. 
RUN ./gradlew build -x test
# 테스트에 필요한 Database가 없으므로 테스트를 제외합니다. 
# 테스트를 포함하고 싶다면, Spring 프로필을 통해서 테스트 전용 환경을 추가합니다. 
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "build/libs/heygongc-0.0.1-SNAPSHOT.jar"]
# 실행시킬 때 프로필을 명시할 수 있습니다. 