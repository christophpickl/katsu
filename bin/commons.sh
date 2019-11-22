#!/usr/bin/env bash

executeGradle() {
  CMD="./gradlew $1"
  echo "$CMD"
  $CMD
  checkLastCommand
}

executeScript() {
  SCRIPT="./bin/$1"
  echo "Executing script: $SCRIPT"
  $SCRIPT
  checkLastCommand
}

checkLastCommand() {
    if [ $? -ne 0 ] ; then
        MSG="Last command did not end successful ‼️"
        showNotification "$MSG"
        echo "$MSG"
        exit 1
    fi
}

showNotification() {
  TITLE="Katsu says ..."
  MESSAGE=$1
  osascript <<EOD
    display notification "$MESSAGE" with title "$TITLE"
EOD
}
