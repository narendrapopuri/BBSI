FROM bbsiregistry.azurecr.io/oracle-java11:production
#FROM openjdk:8u242-jre-stretch

ARG env_name
ENV env_name $env_name
RUN echo $env_name

ARG BRANCH=""
ARG COMMIT=""
ARG DATE=""
ARG BUILD_USER=""
ARG BUILD_USER_ID=""


LABEL BRANCH=${BRANCH}
LABEL COMMIT=${COMMIT}
LABEL COMMIT_TIMESTAMP=${DATE}
LABEL BUILD_USER=${BUILD_USER}
LABEL BUILD_USER_ID=${BUILD_USER_ID}

ENV JVM_OPTS $JVM_OPTS


ARG port_num
ENV port_num $port_num
RUN echo $port_num

ARG version
ENV version $version
RUN echo $version


WORKDIR /apps/

ENV COMMIT_SHA=${COMMIT}
ENV COMMIT_BRANCH=${BRANCH}
ENV COMMIT_TIMESTAMP=${DATE}
ENV BUILD_USER=${BUILD_USER}
ENV BUILD_USER_ID=${BUILD_USER_ID}

COPY com/bbsi/platform/user-management/$version/user-management-R1320-$version.jar /apps/user-management-$version.jar
EXPOSE $port_num

ADD ./dd-java-agent.jar /apps/dd-java-agent.jar

RUN chmod -R 755 /apps/dd-java-agent.jar

ARG user=portaluser
USER $user

ENTRYPOINT java $JVM_OPTS -javaagent:/apps/dd-java-agent.jar -Ddd.service=$env_name-user-management -jar -Dspring.profiles.active=azure_$env_name /apps/user-management-$version.jar

