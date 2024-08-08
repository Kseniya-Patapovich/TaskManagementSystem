FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /tms
COPY . /tms/.
RUN mvn clean install

FROM openjdk:17-alpine
WORKDIR /tms
COPY --from=builder /tms/target/*.jar /tms/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/tms/*.jar"]