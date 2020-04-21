FROM maven:3.6.2-jdk-11-slim AS plr

LABEL maintaner="Pavel Seda"

COPY ./ /apps
WORKDIR /apps
RUN mvn clean install -DskipTests

FROM openjdk:11-jdk AS jdk
COPY --from=plr /apps/network-minimisation-rest/target/network-minimisation-rest-*.jar /apps/network-minimisation.jar
WORKDIR /apps

CMD ["java", "-jar", "network-minimisation.jar"]