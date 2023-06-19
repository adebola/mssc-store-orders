FROM openjdk:17-jdk-slim as builder
MAINTAINER factorialsystems.io
WORKDIR application
ADD maven/${project.build.finalName}.jar ${project.build.finalName}.jar
RUN java -Djarmode=layertools -jar ${project.build.finalName}.jar extract

FROM --platform=linux/x86_64 amd64/openjdk:17-jdk-alpine
LABEL PROJECT_NAME=${project.artifactId} \
      PROJECT={project.id}

WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-Djava.io.factorialsystems.msscstoreorders.security.edg=file:/dev/./urandom", "-Dspring.profiles.active=production", "org.springframework.boot.loader.JarLauncher"]