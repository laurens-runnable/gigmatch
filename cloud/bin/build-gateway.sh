#!/usr/bin/env bash

cd "${BASH_SOURCE%/*}/.." || exit

./mvnw package -pl gateway -am && \
  docker build gateway -t gigmatch/gateway
