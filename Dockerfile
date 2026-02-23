FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /trash

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew clean bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /trash

COPY --from=build /trash/build/libs/*.jar trash.jar

ENTRYPOINT ["java", "-jar", "trash.jar"]
EXPOSE 8080