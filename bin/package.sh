#!/usr/bin/env bash
source bin/commons.sh

executeGradle "build createApp -Dkatsu.enableMacBundle=true"
open "build/macApp"
