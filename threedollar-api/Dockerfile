# Build
FROM adoptopenjdk/openjdk11:alpine-slim AS BUILD
ENV HOME=/usr/app
WORKDIR $HOME
COPY . $HOME
RUN ./gradlew clean :threedollar-api:bootJar \
 :threedollar-api:test \
 :threedollar-application-service:test \
 :threedollar-domain:test \
 :threedollar-external:test \
 :threedollar-common:test

# Run
FROM adoptopenjdk/openjdk11:alpine-jre
ENV HOME=/usr/app

RUN apk add --no-cache curl unzip
RUN curl -O https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip \
&& unzip newrelic-java.zip -d $HOME/ \
&& rm newrelic-java.zip \
&& rm $HOME/newrelic/newrelic.yml

COPY --from=BUILD  $HOME/threedollar-api/build/libs/threedollar-api.jar /threedollar-api.jar
COPY ./deploy/newrelic.yml $HOME/newrelic/newrelic.yml

EXPOSE 5000

ENTRYPOINT ["java", "-XX:CompileThreshold=100", "-XX:+TieredCompilation", "-javaagent:/usr/app/newrelic/newrelic.jar", "-Duser.timezone=Asia/Seoul", "-jar", "/threedollar-api.jar"]
