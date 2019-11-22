#!/usr/bin/env bash
source bin/commons.sh

executeGradle  "dependencyUpdates -Plocal"
