# Use the official Gradle image with JDK 21
FROM gradle:jdk21 AS builder

# Set the working directory
WORKDIR /app
RUN apt-get update && apt-get install -y wget

# Copy the Gradle build files and source code to the container
COPY build.gradle settings.gradle ./
COPY src ./src

# Build the project using Gradle
RUN gradle build --no-daemon --warning-mode all -x test

# Use a smaller base image for the runtime environment
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar
ENV POSTGRES_USER=${DBUSER}
ENV POSTGRES_PASSWORD=${DBPASS}
# Command to run the application
CMD ["java", "-jar", "app.jar"]
