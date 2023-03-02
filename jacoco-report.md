# Generate Jacoco Report
- make sure you are in the `jacoco-report` branch
- build all service, run `find . -name "pom.xml" -exec mvn clean -f '{}' \;`
- build docker images, run `docker compose -f docker-compose-jacoco.yml build`
- start docker containers, run `docker compose -f docker-compose-jacoco.yml up -d`
- explore the webpage or run the integration test
- generate report, run `./jacoco-generate.sh`
- generated reports are under a folder named `report`
- start docker containers, run `docker compose -f docker-compose-jacoco.yml down`