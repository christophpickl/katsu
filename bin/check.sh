#!/usr/bin/env bash

./gradlew clean check checkTodo test jacocoTestCoverageVerification

if [ $? -ne 0 ]; then
  exit
fi

./gradlew test -Dkatsu.uiTest=true
