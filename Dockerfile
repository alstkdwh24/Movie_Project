FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew && ./gradlew dependencies --no-daemon > /dev/null
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/movie-0.0.1-SNAPSHOT.jar app.jar
ENV PROJECT_UPLOAD_PATH=/app/uploads
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
