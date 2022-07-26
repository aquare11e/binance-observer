FROM openjdk:17-slim
COPY $DIR/build/fatjar/binobs.jar /binobs.jar

CMD ["java", "-jar", "/binobs.jar"]