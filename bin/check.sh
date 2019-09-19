#!/usr/bin/env bash

./gradlew test check checkTodo jacocoTestCoverageVerification -Dkatsu.uiTest=true
