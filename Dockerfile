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
ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV JWT_SECRET=${JWT_SECRET}
ENV ACCESS_EXPIRATION=${ACCESS_EXPIRATION}
ENV REFRESH_EXPIRATION=${REFRESH_EXPIRATION}

RUN mkdir /flow-api-gateway
WORKDIR /flow-api-gateway

COPY --from=builder /flow-api-gateway/build/libs/flow-api-gateway-* /flow-api-gateway/app.jar

CMD ["sh", "-c", " \
    java -Dspring.profiles.active=${PROFILE} \
         -Dspring.datasource.url=${DB_URL} \
         -Dspring.datasource.username=${DB_USERNAME} \
         -Dspring.datasource.password=${DB_PASSWORD} \
         -Dspring.jwt.secret=${JWT_SECRET} \
         -Dspring.jwt.access.expiration=${ACCESS_EXPIRATION} \
         -Dspring.jwt.refresh.expiration=${REFRESH_EXPIRATION} \
         -jar /flow-api-gateway/app.jar"]
