FROM gradle:8-jdk21-corretto AS builder

WORKDIR /app

COPY build.gradle settings.gradle  ./

RUN gradle dependencies --no-daemon || return 0

COPY . .

RUN gradle bootJar --no-daemon

FROM amazoncorretto:21

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080
