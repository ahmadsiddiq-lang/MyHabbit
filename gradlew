#!/bin/sh
APP_HOME=$(cd "$(dirname "$0")"; pwd -P)
exec "$APP_HOME/gradlew" "$@"
