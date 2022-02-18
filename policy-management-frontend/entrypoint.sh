#!/bin/sh

set -e

echo "Serializing environment:"

react-env --prefix VUE_APP --dest .

cat __ENV.js

exec "$@"
