#!/usr/bin/env bash

cd "${BASH_SOURCE%/*}/.." || exit

echo "Copying Avro schemas to dashboard-consumer"
cp ./match-service/framework/src/main/avro/events/*.avsc ./dashboard/consumer/src/application/events
for name in ./dashboard/consumer/src/application/events/*.avsc
do
    mv $name "$name.json"
done

echo "Copying Avro schemas to dashboard-app"
cp ./match-service/framework/src/main/avro/commands/*.avsc ./dashboard/app/server/avro/commands
for name in ./dashboard/app/server/avro/commands/*.avsc
do
    mv $name "$name.json"
done

echo "Copying Avro schemas to website-consumer"
cp ./match-service/framework/src/main/avro/events/*.avsc ./website/Gigmatch.Website.Consumer/avro/events
