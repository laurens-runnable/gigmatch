#!/usr/bin/env bash

cd "${BASH_SOURCE%/*}/.." || exit

./mvnw package -pl config-server -am && \
  docker build config-server -t gigmatch/config-server
