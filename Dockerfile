FROM openjdk:8u332-jre-buster
COPY build/fatjar/binobs.jar /binobs.jar

CMD ["java", "-jar", "/binobs.jar"]