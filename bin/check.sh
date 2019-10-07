#!/usr/bin/env bash

./gradlew clean test check checkTodo jacocoTestCoverageVerification

if [ $? -ne 0 ]; then
  exit
fi

./gradlew test -Dkatsu.uiTest=true
