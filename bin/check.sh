#!/usr/bin/env bash
source bin/commons.sh

executeGradle "clean check checkTodo test jacocoTestCoverageVerification"
executeGradle "test -Dkatsu.uiTest=true"
