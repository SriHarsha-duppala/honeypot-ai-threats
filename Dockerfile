# Use OpenJDK 17
FROM openjdk:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Copy all source code
COPY src src

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build the app inside Docker
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the jar directly from target
CMD ["java", "-jar", "target/spamdetector-0.0.1-SNAPSHOT.jar"]
