#!/bin/sh

echo "Starting application /app.jar ..."
java -Djava.security.egd=file:/dev/./urandom -jar ./app.jar
