ARG BUILD_IMAGE=maven:3-openjdk-17-slim
ARG RUNTIME_IMAGE=openjdk:17-jdk-slim

FROM ${BUILD_IMAGE} AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package

FROM ${RUNTIME_IMAGE}
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/*.jar /app/stream.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/app/stream.jar"]