# Etapa de construcción (Build)
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY src /home/backend/src
COPY pom.xml /home/backend
RUN mvn -f /home/backend/pom.xml clean package -DskipTests

# Etapa de ejecución (Runtime)
FROM eclipse-temurin:21-jre-jammy
COPY --from=build /home/backend/target/*.jar /usr/local/lib/challenge.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/challenge.jar"]
