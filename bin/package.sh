#!/usr/bin/env bash
source bin/commons.sh

RELEASE=""
for arg in "$@"
do
    if [ "$arg" == "release" ]
    then
        RELEASE="-Dkatsu.isReleaseBuild=true"
    fi
done

executeGradle "build createApp -Dkatsu.enableMacBundle=true $RELEASE"
open "build/macApp"
