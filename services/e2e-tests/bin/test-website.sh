#!/usr/bin/env bash

cd "${BASH_SOURCE%/*}/.." || exit

spec="website"
if [ $1 ]
then
    spec="${spec}/$1"
fi

npx playwright test $spec --project=chromium
