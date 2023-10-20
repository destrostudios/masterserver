#!/bin/bash

VERSION=$1
TARGET=$2

# Checkout
git clone https://github.com/destrostudios/masterserver
if [ -n "$VERSION" ]; then
  git checkout "$VERSION"
fi

# Build
cp "${TARGET}application-prod.properties" src/main/resources/application-prod.properties
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64;mvn clean install

# Deploy
mv target/masterserver-1.0.0.jar "${TARGET}destrostudios.jar"
mv ecosystem.config.js "${TARGET}"
cd "${TARGET}"
pm2 restart ecosystem.config.js