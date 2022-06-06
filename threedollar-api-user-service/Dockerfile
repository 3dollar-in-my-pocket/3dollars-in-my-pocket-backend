# Stage 1
FROM adoptopenjdk/openjdk11:alpine-slim AS BUILD
ENV HOME=/usr/app
WORKDIR $HOME
COPY . $HOME
RUN ./gradlew clean :threedollar-api-user-service:buildNeeded


# Stage 2
FROM adoptopenjdk/openjdk11:alpine-jre
ENV HOME=/usr/app

RUN apk add --no-cache curl unzip \
&& curl -O https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip \
&& unzip newrelic-java.zip -d $HOME/ \
&& rm newrelic-java.zip \
&& rm $HOME/newrelic/newrelic.yml

COPY --from=BUILD  $HOME/threedollar-api-user-service/build/libs/threedollar-api-user-service.jar /threedollar-api-user-service.jar
COPY ./infrastructure/newrelic.yml $HOME/newrelic/newrelic.yml

EXPOSE 5000
ENTRYPOINT ["java", "-server", "-XX:+UseG1GC", "-javaagent:/usr/app/newrelic/newrelic.jar", "-Duser.timezone=Asia/Seoul", "-jar", "/threedollar-api-user-service.jar"]
