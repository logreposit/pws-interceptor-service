FROM openjdk:11-jre

WORKDIR /opt/logreposit/pws-interceptor-service

COPY run.sh    /opt/logreposit/pws-interceptor-service/run.sh
COPY app.jar   /opt/logreposit/pws-interceptor-service/app.jar
RUN  chmod 755 /opt/logreposit/pws-interceptor-service/run.sh

ENTRYPOINT ["/opt/logreposit/pws-interceptor-service/run.sh"]
