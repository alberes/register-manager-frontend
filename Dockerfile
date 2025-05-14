FROM maven:3.8.5-openjdk-17 as build

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17

WORKDIR /app

COPY --from=build ./build/target/*.jar ./register-manager-frontend.jar

expose 8080

ENV REGISTER_MANAGER_URL=http://localhost:8081/api/v1
ENV EXPIRATION_TIME=30

ENTRYPOINT java -jar register-manager-frontend.jar