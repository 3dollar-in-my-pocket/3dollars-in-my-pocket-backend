# Stage 1
FROM adoptopenjdk/openjdk11:alpine-slim AS BUILD
ENV HOME=/usr/app
WORKDIR $HOME
COPY . $HOME
RUN ./gradlew clean :threedollar-api-admin-service:buildNeeded

# Stage 2
FROM adoptopenjdk/openjdk11:alpine-jre
ENV HOME=/usr/app
COPY --from=BUILD  $HOME/threedollar-api-admin-service/build/libs/threedollar-api-admin-service.jar /threedollar-api-admin-service.jar

EXPOSE 5100
ENTRYPOINT ["java", "-server", "-XX:+UseG1GC", "-Duser.timezone=Asia/Seoul", "-jar", "/threedollar-api-admin-service.jar"]
