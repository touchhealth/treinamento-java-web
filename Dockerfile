FROM maven:3-eclipse-temurin-8-focal as dependencias
RUN mkdir -p /opt/workspace
COPY pom.xml /opt/workspace
WORKDIR /opt/workspace
RUN mvn clean package

FROM maven:3-eclipse-temurin-8-focal as builder
COPY --from=dependencias /root/.m2/repository /root/.m2/repository
RUN mkdir -p /opt/workspace
WORKDIR /opt/workspace
COPY pom.xml /opt/workspace
COPY src /opt/workspace/src
RUN mvn clean package

FROM tomcat:9-jdk8-temurin-focal
COPY --from=builder /opt/workspace/target/treinamento /usr/local/tomcat/webapps/treinamento
CMD ["catalina.sh", "jpda", "run"]