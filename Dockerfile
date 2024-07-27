# builder image
FROM amazoncorretto:17-al2-jdk AS builder

ARG FLOW_MAIN_URI

ENV FLOW_MAIN_URI=${FLOW_MAIN_URI}

RUN mkdir /flow-api-gateway
WORKDIR /flow-api-gateway

COPY . .

RUN ./gradlew clean bootJar

# runtime image
FROM amazoncorretto:17.0.12-al2

ENV TZ=Asia/Seoul
ENV PROFILE=${PROFILE}
ENV FLOW_MAIN_URI=${FLOW_MAIN_URI}

RUN mkdir /flow-api-gateway
WORKDIR /flow-api-gateway

COPY --from=builder /flow-api-gateway/build/libs/flow-api-gateway-* /flow-api-gateway/app.jar

CMD ["sh", "-c", " \
    java -Dspring.profiles.active=${PROFILE} \
         -Dauth.uri=${FLOW_MAIN_URI} \
         -jar /flow-api-gateway/app.jar"]
