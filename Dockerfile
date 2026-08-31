FROM eclipse-temurin:21-jdk AS build

ARG SERVICE_DIR
WORKDIR /workspace

COPY ${SERVICE_DIR}/mvnw ./mvnw
COPY ${SERVICE_DIR}/.mvn ./.mvn
COPY ${SERVICE_DIR}/pom.xml ./pom.xml

RUN chmod +x ./mvnw && ./mvnw -q -DskipTests dependency:go-offline

COPY ${SERVICE_DIR}/src ./src

RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
