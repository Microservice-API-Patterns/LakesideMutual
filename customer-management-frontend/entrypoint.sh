#!/bin/sh

set -e

echo "Serializing environment:"

react-env --dest .

cat __ENV.js

exec "$@"
