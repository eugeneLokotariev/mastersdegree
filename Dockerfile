FROM openjdk:8-jdk-alpine
ADD target/blockchain-accounting-1.jar blockchain-accounting-1.jar
ENTRYPOINT ["java","-jar","/blockchain-accounting-1.jar"]
