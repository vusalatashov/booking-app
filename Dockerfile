# Build Stage
FROM openjdk:21-slim as builder

# Install Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app

# Copy the pom.xml file to the /app directory
COPY pom.xml /app

# Copy the source code to the /app/src directory
COPY src /app/src

# Go offline to download dependencies
RUN mvn dependency:go-offline -B

# Package the application, skipping tests
RUN mvn package -DskipTests

# Diagnostic step: List the contents of the /app/target directory
RUN ls -l /app/target

# Runtime Stage
FROM openjdk:21-slim

WORKDIR /app

# Diagnostic step: Copy all files from the target directory to verify the build output
COPY --from=builder /app/target /app/target

# Copy the built JAR from the builder stage to the runtime stage
COPY --from=builder /app/target/*.jar /app/booking-app.jar

# Command to run the application
CMD ["java", "-jar", "/app/booking-app.jar"]
