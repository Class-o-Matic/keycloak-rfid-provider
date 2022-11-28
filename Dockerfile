#
#   This file is a docker script to build an debuggable instance of keycloak. It exposes the Java debug port to
#   9696 in order to launch debug on a IDE (i.e. IntelliJ)
#

FROM quay.io/keycloak/keycloak:20.0.1
ENV DEBUG_MODE=true
ENV DEBUG_PORT="*:9696"
ENV DEBUG_SUSPEND="n"
EXPOSE 9696
COPY build/libs/keycloak-rfid-spi-1.0-all.jar /opt/keycloak/providers/keycloak-rfid-spi-1.0-all.jar
RUN mkdir /opt/keycloak/certs
COPY src/main/resources/ca/intermediate/ca-chain.cert.p12 /opt/keycloak/certs/ca-chain.cert.p12
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev","--debug","--https-trust-store-file", "$TRUSTSTORE_FILE","--https-trust-store-password", "$TRUSTSTORE_PASS","--proxy", "edge","--hostname-strict=false"]
