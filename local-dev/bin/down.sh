#!/usr/bin/env bash

cd "${BASH_SOURCE%/*}/.." || exit

arch=`uname -m`
files="-f infrastructure.yml -f elasticsearch.${arch}.yml"
if [ $1 ]
then
    files="${files} -f $1.yml"
fi

docker-compose $files down
