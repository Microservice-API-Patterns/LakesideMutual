#!/bin/bash

directory="./"
java_files=$(find "$directory" -name "*.java")

for java_file in $java_files; do
    grep -q -E "(@GetMapping|@PostMapping|@PutMapping|@DeleteMapping|@PatchMapping)[[:space:]]*\(" "$java_file"
    if [ $? -eq 0 ]; then
        temp_file="temp.java"

        cp "$java_file" "$temp_file"

        awk '/(@GetMapping|@PostMapping|@PutMapping|@DeleteMapping|@PatchMapping)[[:space:]]*\(/ {
                print
                getline
                print "\tSystem.out.println(\"Logging method: \" getClass().getSimpleName() \".\" gensub(/.*[[:space:]]([A-Za-z0-9_]+)[[:space:]]*\\\\(.*/, \"\\\\\\\\1\", 1) \");"
                print
                print $0
                next
            }
            { print }' "$temp_file" > "$java_file"

        echo "Log statements added to endpoint methods in $java_file"
    fi
done
