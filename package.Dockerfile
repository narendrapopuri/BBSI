FROM bbsiregistry.azurecr.io/oracle-java11:development

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


ARG port_num
ENV port_num $port_num
RUN echo $port_num

WORKDIR /apps/

ENV COMMIT_SHA=${COMMIT}
ENV COMMIT_BRANCH=${BRANCH}
ENV COMMIT_TIMESTAMP=${DATE}
ENV BUILD_USER=${BUILD_USER}
ENV BUILD_USER_ID=${BUILD_USER_ID}


COPY com/bbsi/platform/user-management/1.0-SNAPSHOT/*.jar /apps/user-management-1.0-SNAPSHOT.jar
EXPOSE $port_num
ARG user=portaluser

USER $user
ENTRYPOINT  java -jar -Dspring.profiles.active=azure_$env_name /apps/user-management-1.0-SNAPSHOT.jar