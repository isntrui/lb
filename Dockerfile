FROM gradle:jdk21 AS builder

WORKDIR /app
RUN apt-get update && apt-get install -y wget

COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle build --no-daemon --warning-mode all -x test

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar
ENV POSTGRES_USER=${DBUSER}
ENV POSTGRES_PASSWORD=${DBPASS}
CMD ["java", "-jar", "app.jar"]
