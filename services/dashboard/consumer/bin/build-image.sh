#!/usr/bin/env bash

cd "${BASH_SOURCE%/*}/.." || exit

docker build . -t gigmatch/admin-consumer
