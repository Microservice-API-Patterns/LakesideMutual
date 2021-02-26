#!/bin/sh

set -e

echo "Serializing environment:"

react-env --dest .

sed -i 's/REACT_APP_/VUE_APP_/g' env.js

cat env.js

exec "$@"
