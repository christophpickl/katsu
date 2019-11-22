#!/usr/bin/env bash
source bin/commons.sh

echo "[KATSU] Release script starting ..."

VERSION_FILE="src/main/build/version.txt"
NEXT_VERSION="--not set--"
calculateNextVersion() {
  CURRENT_VERSION=$(cat $VERSION_FILE)
  DEFAULT_RELEASE_VERSION=$(echo "${CURRENT_VERSION}"| cut -d "-" -f 1)
  CURRENT_MAJOR_VERSION=$(echo "${DEFAULT_RELEASE_VERSION}"| cut -d "." -f 1)
  CURRENT_MINOR_VERSION=$(echo "${DEFAULT_RELEASE_VERSION}"| cut -d "." -f 2)
  NEXT_MINOR_VERSION=$(($CURRENT_MINOR_VERSION + 1))
  DEFAULT_NEXT_VERSION="$CURRENT_MAJOR_VERSION.$NEXT_MINOR_VERSION"

  echo
  read -p "[KATSU] Enter RELEASE Version [$DEFAULT_NEXT_VERSION]: " ENTERED_NEXT_VERSION

  if [ ! -z "$ENTERED_NEXT_VERSION" ]; then
      NEXT_VERSION=${ENTERED_NEXT_VERSION}
  else
      NEXT_VERSION=${DEFAULT_NEXT_VERSION}
  fi
}

confirmRelease() {
  echo
  while true; do
      read -p "[KATSU] Do you confirm this release? [y/n] >> " yn
      case ${yn} in
          [Yy]* ) break;;
          [Nn]* ) echo "[KATSU] Aborted."; exit;;
          * ) myEcho "[KATSU] Please answer y(es) or n(o)";;
      esac
  done
  echo
}

gitCommitTagPush() {
  echo "[KATSU] Going to GIT commit, tag and push ..."
  git add .
  git commit -m "[Auto-Release] current release version: $NEXT_VERSION"
  checkLastCommand
  git tag "${NEXT_VERSION}"
  checkLastCommand
  git push
  checkLastCommand
  git push origin --tags
  checkLastCommand
}

calculateNextVersion
confirmRelease
executeScript "check.sh"
echo $NEXT_VERSION > ${VERSION_FILE}
gitCommitTagPush
executeScript "package.sh"
