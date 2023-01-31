#!/usr/bin/env bash

cd "${BASH_SOURCE%/*}/.." || exit

./mvnw package -pl discovery-server -am && \
  docker build discovery-server -t gigmatch/discovery-server
