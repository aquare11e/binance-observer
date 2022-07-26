FROM openjdk:17-slim
WORKDIR project
COPY build/fatjar/binobs.jar /binobs.jar

CMD ["java", "-jar", "/binobs.jar"]