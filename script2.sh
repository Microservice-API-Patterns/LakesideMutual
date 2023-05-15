#!/bin/bash

PROJECT_DIR="./"

ADDITIONAL_PARAM='@RequestHeader(value = "rest-tester", required = false) String restTester'

JAVA_FILES=$(find "$PROJECT_DIR" -type f -name '*.java' -exec grep -l -e '@GetMapping' -e '@PutMapping' -e '@PostMapping' -e '@DeleteMapping' {} \;)

for FILE in $JAVA_FILES; do
    sed -i -e "s/\(@GetMapping([^)]*)\)/\1, $ADDITIONAL_PARAM/" \
           -e "s/\(@PutMapping([^)]*)\)/\1, $ADDITIONAL_PARAM/" \
           -e "s/\(@PostMapping([^)]*)\)/\1, $ADDITIONAL_PARAM/" \
           -e "s/\(@DeleteMapping([^)]*)\)/\1, $ADDITIONAL_PARAM/" "$FILE"
done

echo "Additional parameter added to endpoint methods successfully!"