FROM adoptopenjdk/openjdk11:alpine-slim AS BUILD
ENV HOME=/usr/app
WORKDIR $HOME
COPY . $HOME
RUN ./gradlew clean :threedollar-api-admin:buildNeeded --parallel

FROM adoptopenjdk/openjdk11:alpine-jre
ENV HOME=/usr/app
COPY --from=BUILD  $HOME/threedollar-api-admin/build/libs/threedollar-api-admin.jar /threedollar-api-admin.jar
EXPOSE 5100

ENTRYPOINT ["java", "-XX:CompileThreshold=100", "-XX:+TieredCompilation", "-Duser.timezone=Asia/Seoul", "-jar", "/threedollar-api-admin.jar"]
