# syntax=docker/dockerfile:1

FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app
ENV JAVA_OPTS=""
COPY --from=build /app/target/supabase-auth-java-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT:-8080} $JAVA_OPTS -jar /app/app.jar"]
